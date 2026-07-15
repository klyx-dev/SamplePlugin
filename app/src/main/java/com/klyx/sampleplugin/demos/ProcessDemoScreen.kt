package com.klyx.sampleplugin.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.klyx.api.data.fs.FileSystem
import com.klyx.api.system.command
import com.klyx.api.system.commandExists
import com.klyx.api.system.firstAvailable
import com.klyx.api.system.isSuccess
import com.klyx.api.system.outputText
import com.klyx.api.system.shell
import com.klyx.api.system.which
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessDemoScreen(fileSystem: FileSystem) {
    val scope = rememberCoroutineScope()
    var running by remember { mutableStateOf(false) }

    val demos = listOf(
        ProcessDemo("Run command: echo") {
            command("echo").args("Hello", "from", "Klyx", "Plugin!").outputText()
        },
        ProcessDemo("Shell: ls -la") {
            shell("ls -la /data/data/com.klyx").outputText()
        },
        ProcessDemo("commandExists: bash") {
            "bash exists: ${commandExists("bash")}"
        },
        ProcessDemo("which: bash") {
            "bash path: ${which("bash") ?: "not found"}"
        },
        ProcessDemo("firstAvailable: nano,vim,vi") {
            "first available: ${firstAvailable("nano", "vim", "vi") ?: "none"}"
        },
        ProcessDemo("which: python3") {
            "python3 path: ${which("python3") ?: "not found"}"
        },
        ProcessDemo("Uptime via shell") {
            shell("uptime").outputText()
        },
        ProcessDemo("Process info") {
            "pid=$$, isSuccess=${command("true").isSuccess()}"
        },
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Process Execution API") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                    text = "Demonstrates: command(), shell(), CommandBuilder, commandExists(), which(), firstAvailable(), outputText(), pipeTo(), stream(), retry(), timeout",
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
                                "Running: ${demo.description}\n\n" + demo.execute()
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class ProcessDemo(
    val description: String,
    val execute: suspend () -> String,
)
