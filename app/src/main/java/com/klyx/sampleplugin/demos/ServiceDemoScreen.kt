package com.klyx.sampleplugin.demos

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
import com.klyx.api.data.preferences.AppTheme
import com.klyx.api.service.Settings
import com.klyx.api.service.setDarkMode
import com.klyx.api.service.setTheme
import com.klyx.sampleplugin.icons.ArrowBack
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDemoScreen(settings: Settings) {
    val scope = rememberCoroutineScope()
    var running by remember { mutableStateOf(false) }

    val demos = listOf(
        SrvDemo("Settings.current") {
            "Settings flow collected. Use .first() to get current value."
        },
        SrvDemo("Settings - get current theme") {
            val current = settings.settings.first()
            "Current theme: ${current.appearance.theme}"
        },
        SrvDemo("Settings.setDarkMode(enabled=true)") {
            settings.setDarkMode(enabled = true)
            "Dark mode enabled via Settings service."
        },
        SrvDemo("Settings.setTheme(AppTheme.Light)") {
            settings.setTheme(AppTheme.Light)
            "Theme set to Light via Settings service."
        },
        SrvDemo("Settings.setTheme(AppTheme.Dark)") {
            settings.setTheme(AppTheme.Dark)
            "Theme set to Dark via Settings service."
        },
        SrvDemo("Settings.setTheme(AppTheme.System)") {
            settings.setTheme(AppTheme.System)
            "Theme reverted to System default."
        },
        SrvDemo("Settings.updateSettings") {
            val current = settings.settings.first()
            settings.updateSettings {
                it.copy(
                    terminal = it.terminal.copy(cursorBlink = !it.terminal.cursorBlink),
                )
            }
            "Toggled terminal cursorBlink setting."
        },
        SrvDemo("Settings.updateTerminalSettings") {
            settings.updateTerminalSettings { it.copy(fontSize = 14f) }
            "Set terminal font size to 14."
        },
        SrvDemo("Settings.updateEditorSettings") {
            settings.updateEditorSettings { it.copy(fontSize = 16f) }
            "Set editor font size to 16."
        },
        SrvDemo("Settings.updateFileTreeSettings") {
            settings.updateFileTreeSettings { it.copy(showHiddenFiles = true) }
            "Enabled show hidden files in file tree."
        },
        SrvDemo("Settings.updateAppearanceSettings") {
            settings.updateAppearanceSettings { it.copy(reduceMotion = true) }
            "Enabled reduce motion."
        },
        SrvDemo("Settings - read all settings") {
            val current = settings.settings.first()
            buildString {
                appendLine("Terminal Settings:")
                appendLine("  cursorStyle: ${current.terminal.cursorStyle}")
                appendLine("  fontSize: ${current.terminal.fontSize}")
                appendLine("  cursorBlink: ${current.terminal.cursorBlink}")
                appendLine("  scrollbackLines: ${current.terminal.scrollbackLines}")
                appendLine("  showMotd: ${current.terminal.showMotd}")
                appendLine("Editor Settings:")
                appendLine("  fontSize: ${current.editor.fontSize}")
                appendLine("  tabSize: ${current.editor.tabSize}")
                appendLine("Appearance:")
                appendLine("  theme: ${current.appearance.theme}")
                appendLine("  amoledDarkMode: ${current.appearance.amoledDarkMode}")
                appendLine("FileTree:")
                appendLine("  showHiddenFiles: ${current.fileTree.showHiddenFiles}")
            }
        },
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Services & Settings API") },
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
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
        ) {
            item {
                Text(
                    text = "Demonstrates: Settings (read/update settings, theme, terminal/editor/fileTree settings), Fonts service, Tabs service",
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

private data class SrvDemo(
    val description: String,
    val execute: suspend () -> String,
)
