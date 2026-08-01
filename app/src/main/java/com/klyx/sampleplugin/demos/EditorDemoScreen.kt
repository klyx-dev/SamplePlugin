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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klyx.api.data.file.KxFile
import com.klyx.api.data.file.wrap
import com.klyx.api.data.fs.Paths
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorDemoScreen(fileName: String? = null) {

    val demos = remember(fileName) {
        listOf(
            EditDemo(
                "WorkspaceTab.TextFile",
                buildString {
                    appendLine("A WorkspaceTab.TextFile instance represents an open")
                    appendLine("text file in the editor. It contains:")
                    appendLine("  - file: KxFile reference")
                    appendLine("  - text: String content")
                    appendLine("  - hasUnsavedChanges: Boolean")
                    appendLine("  - title: Display name")
                    appendLine("  - id: Unique tab identifier")
                    appendLine()
                    val f = File(Paths.tempDir, "sample_demo.txt")
                    f.writeText("This file was created by SamplePlugin!\nTime: ${System.currentTimeMillis()}")
                    appendLine("Created file at: ${f.absolutePath}")
                    appendLine("Content: ${f.readText()}")
                },
            ),
            EditDemo(
                "WorkspaceTab.ImageFile",
                buildString {
                    appendLine("WorkspaceTab.ImageFile is used for image preview.")
                    appendLine("Properties:")
                    appendLine("  - uri: Uri of the image")
                    appendLine("  - projectUri: Optional project context")
                    appendLine("  - title: Display name")
                    appendLine("  - id: Unique tab identifier")
                    appendLine()
                    appendLine("Image files are auto-detected by extension")
                    appendLine("(.png, .jpg, .jpeg, .gif, .webp, .bmp)")
                },
            ),
            EditDemo(
                "WorkspaceTab.Custom",
                buildString {
                    appendLine("WorkspaceTab.Custom allows plugins to register")
                    appendLine("arbitrary composable content as editor tabs.")
                    appendLine("Properties:")
                    appendLine("  - title: Display name")
                    appendLine("  - id: Unique tab identifier")
                    appendLine("  - content: @Composable () -> Unit")
                    appendLine()
                    appendLine("Used by FileOpener to show custom views.")
                    if (fileName != null) {
                        appendLine()
                        appendLine("This tab was opened by the .mp3 FileOpener!")
                    }
                },
            ),
            EditDemo(
                "WorkspaceTab.Welcome",
                "WorkspaceTab.Welcome represents the default welcome screen.\nIt is a singleton data object.",
            ),
            EditDemo(
                "EditorAction.Save",
                buildString {
                    appendLine("EditorAction.Save represents a save action.")
                    appendLine("It contains the target KxFile to save.")
                    appendLine()
                    appendLine("Usage:")
                    appendLine("  EditorAction.Save(file)")
                    appendLine()
                    appendLine("EditorAction.SaveAs represents saving")
                    appendLine("to a new location:")
                    appendLine("  EditorAction.SaveAs(oldTabId, newFile)")
                },
            ),
            EditDemo(
                "FileOpenRequest",
                buildString {
                    appendLine("FileOpenRequest contains metadata about")
                    appendLine("a file that is being opened:")
                    appendLine("  - uri: Uri")
                    appendLine("  - fileName: String")
                    appendLine("  - extension: String")
                    appendLine("  - mimeType: String?")
                    appendLine("  - projectUri: Uri?")
                    appendLine()
                    appendLine("FileOpener receives this and returns")
                    appendLine("a WorkspaceTab or null to pass to next opener.")
                },
            ),
            EditDemo(
                "FileOpenerRegistry",
                buildString {
                    appendLine("FileOpenerRegistry allows plugins to")
                    appendLine("register custom file openers.")
                    appendLine()
                    appendLine("Key interface:")
                    appendLine("  FileOpener {")
                    appendLine("    val id: String")
                    appendLine("    val priority: Int")
                    appendLine("    suspend fun open(request): WorkspaceTab?")
                    appendLine("  }")
                    appendLine()
                    appendLine("This SamplePlugin registers an opener for")
                    appendLine("'.mp3' extension files via openers.register()")
                    appendLine("in onStart().")
                },
            ),
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = "Demonstrates: WorkspaceTab (TextFile/ImageFile/Custom/Welcome), EditorAction (Save/SaveAs), FileOpener, FileOpenerRegistry, FileOpenRequest",
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
                        text = demo.description,
                        style = MaterialTheme.typography.bodyMedium,
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

private data class EditDemo(
    val description: String,
    val content: String,
)
