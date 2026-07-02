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
import com.klyx.api.data.file.KxFile
import com.klyx.api.data.fs.FileSystem
import com.klyx.api.data.fs.Paths
import com.klyx.api.util.humanBytes
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileSystemDemoScreen(fileSystem: FileSystem) {
    val scope = rememberCoroutineScope()
    var running by remember { mutableStateOf(false) }

    val demos = listOf(
        FsDemo("Paths.dataDir") {
            "dataDir: ${Paths.dataDir.absolutePath}\nexists: ${Paths.dataDir.exists()}"
        },
        FsDemo("Paths.filesDir") {
            "filesDir: ${Paths.filesDir.absolutePath}\nexists: ${Paths.filesDir.exists()}"
        },
        FsDemo("Paths.tempDir") {
            "tempDir: ${Paths.tempDir.absolutePath}\nexists: ${Paths.tempDir.exists()}"
        },
        FsDemo("KxFile from path") {
            val f = KxFile("/data/data/com.klyx")
            "name: ${f.name}\nuri: ${f.uri}\nisDir: ${f.isDirectory}\nexists: ${f.exists}\ncanRead: ${f.canRead}"
        },
        FsDemo("KxFile file ops") {
            val file = File(Paths.tempDir, "sample_plugin_test.txt")
            val f = KxFile(file.absolutePath)
            f.writeText("Hello from SamplePlugin!\nCreated at: ${System.currentTimeMillis()}")
            val read = f.readText()
            "wrote to: ${f.absolutePath}\nread: $read\nsize: ${f.length.humanBytes()}\next: ${f.extension}"
        },
        FsDemo("FileCategory detection") {
            val file = File(Paths.dataDir, "test.txt")
            file.writeText("hello")
            val textFile = KxFile(file.absolutePath)
            val cat = fileSystem.determineFileCategory(textFile.uri)
            "text file category: $cat"
        },
        FsDemo("KxFile.listFiles") {
            val dir = KxFile(Paths.dataDir.absolutePath)
            val files = dir.listFiles().take(10)
            files.joinToString("\n") { "  ${if (it.isDirectory) "[DIR]" else "[FILE]"} ${it.name} (${it.length.humanBytes()})" }
                .let { "${dir.absolutePath}\n$it" + if (dir.listFiles().size > 10) "\n  ... and more" else "" }
        },
        FsDemo("humanBytes examples") {
            listOf(
                0L, 512L, 1024L, 1536L, 1048576L, 1073741824L,
            ).joinToString("\n") { "${it}.humanBytes() = ${it.humanBytes()}" }
        },
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("File System API") },
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
                    text = "Demonstrates: Paths object, KxFile (read/write/list/properties), FileSystem service (list/inputStream/outputStream/search/capabilities), FileCategory detection, humanBytes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            if (running) {
                item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }
            }

            items(demos) { demo ->
                var demoResult by remember { mutableStateOf<String?>(null) }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    onClick = {
                        scope.launch {
                            running = true
                            demoResult = try {
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
                        if (demoResult != null) {
                            Text(
                                text = demoResult!!,
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

private data class FsDemo(
    val description: String,
    val execute: suspend () -> String,
)
