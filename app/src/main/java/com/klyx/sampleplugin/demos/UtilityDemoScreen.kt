package com.klyx.sampleplugin.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klyx.api.platform.Architecture
import com.klyx.api.platform.currentArchitecture
import com.klyx.api.ui.theme.JetBrainsMonoFontFamily
import com.klyx.api.ui.theme.LocalIsDarkMode
import com.klyx.api.ui.theme.backgroundDark
import com.klyx.api.ui.theme.backgroundLight
import com.klyx.api.ui.theme.errorDark
import com.klyx.api.ui.theme.errorLight
import com.klyx.api.ui.theme.primaryDark
import com.klyx.api.ui.theme.primaryLight
import com.klyx.api.ui.theme.secondaryDark
import com.klyx.api.ui.theme.secondaryLight
import com.klyx.api.ui.theme.surfaceDark
import com.klyx.api.ui.theme.surfaceLight
import com.klyx.api.ui.theme.tertiaryDark
import com.klyx.api.ui.theme.tertiaryLight
import com.klyx.api.util.dayWithSuffix
import com.klyx.api.util.decodeEscaped
import com.klyx.api.util.encodeEscaped
import com.klyx.api.util.formatDate
import com.klyx.api.util.formatDateTime
import com.klyx.api.util.humanBytes
import com.klyx.api.util.thenIf
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityDemoScreen() {
    val scope = rememberCoroutineScope()
    var result by remember { mutableStateOf("") }
    val isDark = LocalIsDarkMode.current

    val sections = listOf(
        UtilitySection(
            "Theme & Colors",
            buildString {
                appendLine("Color constants from com.klyx.api.ui.theme:")
                appendLine("  primaryLight: $primaryLight")
                appendLine("  primaryDark: $primaryDark")
                appendLine("  secondaryLight: $secondaryLight")
                appendLine("  secondaryDark: $secondaryDark")
                appendLine("  tertiaryLight: $tertiaryLight")
                appendLine("  tertiaryDark: $tertiaryDark")
                appendLine("  errorLight: $errorLight")
                appendLine("  errorDark: $errorDark")
                appendLine("  backgroundLight: $backgroundLight")
                appendLine("  backgroundDark: $backgroundDark")
                appendLine("  surfaceLight: $surfaceLight")
                appendLine("  surfaceDark: $surfaceDark")
                appendLine("  LocalIsDarkMode: $isDark")
                appendLine()
                appendLine("Color extensions:")
                appendLine("  Color.blend(other, fraction)")
                appendLine("  Color.inverse(fraction)")
                appendLine("  Color.harmonizeWithPrimary(fraction)")
                appendLine("  Color.takeIf(predicate)")
                appendLine("  Color.takeUnless(predicate)")
            },
        ),
        UtilitySection(
            "Typography",
            buildString {
                appendLine("Font families provided:")
                appendLine("  bodyFontFamily: Inter (Google Fonts)")
                appendLine("  displayFontFamily: Inter")
                appendLine("  JetBrainsMonoFontFamily: Monospace")
                appendLine("  GoogleSansRounded: Variable font")
                appendLine()
                appendLine("Pre-built Typography objects:")
                appendLine("  Typography (Material3 + Inter)")
                appendLine("  GoogleSansTypography (Google Sans Rounded)")
            },
        ),
        UtilitySection(
            "Platform: Architecture",
            buildString {
                val arch: Architecture = currentArchitecture()
                appendLine("currentArchitecture(): $arch")
                appendLine()
                appendLine("Architecture values:")
                appendLine("  Aarch64: ${Architecture.Aarch64}")
                appendLine("  Arm: ${Architecture.Arm}")
                appendLine("  X86: ${Architecture.X86}")
                appendLine("  X86_64: ${Architecture.X86_64}")
                appendLine("  Unknown: ${Architecture.Unknown}")
            },
        ),
        UtilitySection(
            "DateTime Utils",
            buildString {
                val now = LocalDateTime.now()
                val today = LocalDate.now()
                appendLine("asLocalDateTime():")
                appendLine("  Duration.ofHours(2).asLocalDateTime()")
                appendLine("    = ${Duration.ofHours(2)} converted to local time")
                appendLine()
                appendLine("dayWithSuffix():")
                (1..31 step 5).forEach { appendLine("  dayWithSuffix($it) = \"${dayWithSuffix(it)}\"") }
                appendLine()
                appendLine("formatDate():")
                appendLine("  $today -> ${today.formatDate()}")
                appendLine()
                appendLine("formatDateTime():")
                appendLine("  $now -> ${now.formatDateTime()}")
            },
        ),
        UtilitySection(
            "FileUtils: humanBytes",
            buildString {
                appendLine("humanBytes() for different sizes:")
                listOf(
                    0L, 512L, 1024L, 1536L,
                    1048576L, 1073741824L,
                ).forEach { appendLine("  $it -> \"${it.humanBytes()}\"") }
            },
        ),
        UtilitySection(
            "UriUtils",
            buildString {
                val original = "hello world/test file.txt"
                val encoded = original.encodeEscaped()
                appendLine("encodeEscaped():")
                appendLine("  \"$original\"")
                appendLine("  -> \"$encoded\"")
                appendLine()
                val decoded = encoded.decodeEscaped()
                appendLine("decodeEscaped():")
                appendLine("  \"$encoded\"")
                appendLine("  -> \"$decoded\"")
                appendLine()
                appendLine("shareableUri:")
                appendLine("  Converts file:// URIs to content://")
                appendLine("  via FileProvider for sharing.")
            },
        ),
        UtilitySection(
            "Context Utils",
            buildString {
                appendLine("openUrl(url: String)")
                appendLine("  Opens a URL in the default browser.")
                appendLine()
                appendLine("shareText(text: String)")
                appendLine("  Shares text via Android share sheet.")
                appendLine()
                appendLine("Context.isGestureNavigation()")
                appendLine("  Checks if gesture nav is active (API 29+).")
            },
        ),
        UtilitySection(
            "Utils & ViewModelExt",
            buildString {
                appendLine("tryOrNull<T>(block):")
                appendLine("  val result = tryOrNull { riskyOperation() }")
                appendLine("  Returns null if block throws.")
                appendLine()
                appendLine("Throwable.extractMessage():")
                appendLine("  val msg = error.extractMessage()")
                appendLine("  User-friendly error message.")
                appendLine()
                appendLine("Context.findActivity(): Activity?")
                appendLine("  Walks up the context tree to find Activity.")
                appendLine()
                appendLine("ViewModel Flow extensions:")
                appendLine("  Flow<T>.stateInWhileSubscribed(initial)")
                appendLine("  Flow<T>.stateInEagerly(initial)")
                appendLine("  Flow<T>.stateInLazily(initial)")
                appendLine("  Syntax: flow.stateInWhileSubscribed()")
            },
        ),
        UtilitySection(
            "ModifierExt",
            buildString {
                val condition = true
                appendLine("thenIf(condition) { modifier }:")
                appendLine("  Modifier.thenIf(isVisible) {")
                appendLine("    this.alpha(0.5f)")
                appendLine("  }")
                appendLine()
                appendLine("thenIfElse(cond, ifTrue, ifFalse):")
                appendLine("  Applies one modifier or the other.")
                appendLine()
                appendLine("conditional(predicate) { modifier }:")
                appendLine("  Lazy variant with suspend predicate.")
                appendLine()
                appendLine("applyIfNotNull(value) { modifier }:")
                appendLine("  Applies only if value != null.")
                appendLine()
                appendLine("Example: condition=$condition")
                val testMod = Modifier
                    .thenIf(condition) { this.size(100.dp) }
                appendLine("  Applied thenIf: $testMod")
            },
        ),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Utilities & Theme API") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp),
        ) {
            item {
                Text(
                    text = "Demonstrates: Theme colors, Typography, Architecture (platform), DateTime utils, FileUtils (humanBytes), UriUtils (encode/decode/share), ContextUtils, Utils (tryOrNull/extractMessage), ViewModelExt, ModifierExt",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            item { Spacer(modifier = Modifier.height(4.dp)) }

            items(sections) { section ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = section.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = section.content,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = JetBrainsMonoFontFamily,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Live Color Swatches",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val colors = listOf(
                            "Primary" to (if (isDark) primaryDark else primaryLight),
                            "Secondary" to (if (isDark) secondaryDark else secondaryLight),
                            "Tertiary" to (if (isDark) tertiaryDark else tertiaryLight),
                            "Error" to (if (isDark) errorDark else errorLight),
                            "Background" to (if (isDark) backgroundDark else backgroundLight),
                            "Surface" to (if (isDark) surfaceDark else surfaceLight),
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            colors.forEach { (name, color) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(color, RoundedCornerShape(8.dp)),
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class UtilitySection(
    val title: String,
    val content: String,
)
