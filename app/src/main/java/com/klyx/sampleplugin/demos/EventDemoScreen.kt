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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klyx.sampleplugin.icons.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDemoScreen() {
    val demos = listOf(
        EventDemo(
            "FileOpenedEvent",
            buildString {
                appendLine("Fired when a file is opened in the editor.")
                appendLine("Properties:")
                appendLine("  uri: Uri - The opened file's URI")
                appendLine("  fileName: String - File name")
                appendLine("  tabId: String - Editor tab ID")
                appendLine("  projectUri: Uri? - Optional project context")
                appendLine()
                appendLine("Subscribed in SamplePlugin.onLoad():")
                appendLine("  bus.subscribe<FileOpenedEvent> { event ->")
                appendLine($$"    showToast(\"File opened: ${event.fileName}\")")
                appendLine("  }")
            },
        ),
        EventDemo(
            "NewSessionEvent",
            buildString {
                appendLine("Fired when a new terminal session is created.")
                appendLine("Properties:")
                appendLine("  id: Uuid - Session ID")
                appendLine("  session: TerminalSession - The session object")
                appendLine()
                appendLine("Subscribed in SamplePlugin.onLoad():")
                appendLine("  bus.subscribe<NewSessionEvent> { event ->")
                appendLine($$"    showToast(\"New terminal: ${event.id}\")")
                appendLine("  }")
            },
        ),
        EventDemo(
            "SessionTerminateEvent",
            buildString {
                appendLine("Fired when a terminal session is terminated.")
                appendLine("Properties:")
                appendLine("  id: Uuid - The terminated session ID")
            },
        ),
        EventDemo(
            "TerminateAllSessionEvent",
            buildString {
                appendLine("Fired when all terminal sessions are terminated.")
                appendLine("(data object - no properties)")
                appendLine()
                appendLine("Subscribed in SamplePlugin.onLoad():")
                appendLine("  bus.subscribe<TerminateAllSessionEvent> {")
                appendLine("    showToast(\"All sessions terminated!\")")
                appendLine("  }")
            },
        ),
        EventDemo(
            "TerminalNotificationTapEvent",
            "Fired when the user taps on a terminal notification.\nA data object with no additional properties.",
        ),
        EventDemo(
            "EventBus access",
            buildString {
                appendLine("The EventBus is accessed via PluginContext extension:")
                appendLine()
                appendLine("  val bus = context.eventBus")
                appendLine()
                appendLine("Usage:")
                appendLine("  bus.subscribe<EventType> { event ->")
                appendLine("    // handle event")
                appendLine("  }")
            },
        ),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event System API") },
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
                    text = "Demonstrates: EventBus, FileOpenedEvent, NewSessionEvent, SessionTerminateEvent, TerminateAllSessionEvent, TerminalNotificationTapEvent, EventBusHolder",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }

            items(demos) { demo ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = demo.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = demo.content,
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

private data class EventDemo(
    val title: String,
    val content: String,
)
