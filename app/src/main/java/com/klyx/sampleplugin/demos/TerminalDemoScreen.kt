package com.klyx.sampleplugin.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klyx.api.data.terminal.TerminalManager
import com.klyx.api.system.command
import com.klyx.api.system.outputText
import com.klyx.api.terminal.processEnv
import com.klyx.api.terminal.terminalArgs
import com.klyx.api.terminal.terminalEnv
import com.klyx.sampleplugin.icons.ArrowBack
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalDemoScreen(terminalManager: TerminalManager) {
    val scope = rememberCoroutineScope()
    var running by remember { mutableStateOf(false) }

    val demos = listOf(
        TermDemo("terminalEnv() keys") {
            terminalEnv().entries.joinToString("\n") { "  ${it.key}=${it.value}" }
                .let { "Terminal environment (${terminalEnv().size} vars):\n$it" }
        },
        TermDemo("processEnv() keys") {
            processEnv().entries.joinToString("\n") { "  ${it.key}=${it.value}" }
                .let { "Process environment (${processEnv().size} vars):\n$it" }
        },
        TermDemo("terminalArgs()") {
            terminalArgs(showMotd = false).joinToString("\n") { "  $it" }
                .let { "Terminal PRoot args:\n$it" }
        },
        TermDemo("terminalArgs(showMotd=true)") {
            terminalArgs(showMotd = true).joinToString("\n") { "  $it" }
                .let { "Terminal PRoot args (with MOTD):\n$it" }
        },
        TermDemo("SessionManager.sessions") {
            val sessions = terminalManager.sessionManager.sessions.value
            if (sessions.isEmpty()) "No active terminal sessions."
            else sessions.joinToString("\n") {
                "  [${it.id}] session=${it.session}"
            }
        },
        TermDemo("SessionManager.currentSessionId") {
            val id = terminalManager.sessionManager.currentSessionId.value
            "Current session ID: ${id ?: "none"}"
        },
        TermDemo("SessionBinder.isServiceBound") {
            "Terminal service bound: ${terminalManager.sessionBinder.isServiceBound.value}"
        },
        TermDemo("Execute command in terminal env") {
            command("bash", "-c", $$"echo TERM=$TERM && echo SHELL=$SHELL")
                .env(terminalEnv())
                .output().stdoutText
        },
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terminal Sessions API") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Text(
                    text = "Demonstrates: TerminalManager, TerminalSessionManager, TerminalSessionBinder, terminalEnv(), processEnv(), terminalArgs(), session lifecycle",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }

            if (running) {
                item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }
            }

            items(demos) { demo ->
                var resultText by remember { mutableStateOf<String?>(null) }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    onClick = {
                        scope.launch {
                            running = true
                            resultText = try {
                                demo.execute()
                            } catch (e: Exception) {
                                "Error: ${e.message}"
                            }
                            running = false
                        }
                    },
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = demo.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        if (resultText != null) {
                            Text(
                                text = resultText!!,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class TermDemo(
    val description: String,
    val execute: suspend () -> String,
)
