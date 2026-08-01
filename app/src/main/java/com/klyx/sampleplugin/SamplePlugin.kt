package com.klyx.sampleplugin

import com.klyx.api.NavDestination
import com.klyx.api.Navigator
import com.klyx.api.data.editor.FileOpenRequest
import com.klyx.api.data.editor.FileOpener
import com.klyx.api.data.editor.FileOpenerRegistry
import com.klyx.api.data.editor.WorkspaceTab
import com.klyx.api.data.fs.FileSystem
import com.klyx.api.data.fs.Paths
import com.klyx.api.data.terminal.TerminalManager
import com.klyx.api.event.editor.FileOpenedEvent
import com.klyx.api.event.eventBus
import com.klyx.api.event.terminal.NewSessionEvent
import com.klyx.api.event.terminal.TerminateAllSessionEvent
import com.klyx.api.plugin.Author
import com.klyx.api.plugin.KlyxPlugin
import com.klyx.api.plugin.Links
import com.klyx.api.plugin.PluginInfo
import com.klyx.api.plugin.PluginManifest
import com.klyx.api.plugin.currentLifecycleOwner
import com.klyx.api.plugin.currentPluginContext
import com.klyx.api.plugin.plugin
import com.klyx.api.plugin.pluginScope
import com.klyx.api.plugin.runtime
import com.klyx.api.service.Fonts
import com.klyx.api.service.Settings
import com.klyx.api.service.Tabs
import com.klyx.api.ui.ScreenId
import com.klyx.api.ui.ScreenRegistry
import com.klyx.api.ui.ToolbarAction
import com.klyx.api.ui.ToolbarCategory
import com.klyx.api.ui.ToolbarIcon
import com.klyx.api.ui.ToolbarRegistry
import com.klyx.api.ui.toastHostState
import com.klyx.sampleplugin.demos.EditorDemoScreen
import com.klyx.sampleplugin.demos.EventDemoScreen
import com.klyx.sampleplugin.demos.ProcessDemoScreen
import com.klyx.sampleplugin.demos.ServiceDemoScreen
import com.klyx.sampleplugin.demos.TerminalDemoScreen
import com.klyx.sampleplugin.demos.UtilityDemoScreen
import com.klyx.sampleplugin.icons.Android
import com.klyx.sampleplugin.icons.DeployedCode
import com.klyx.sampleplugin.icons.Description
import com.klyx.sampleplugin.icons.EventNote
import com.klyx.sampleplugin.icons.Palette
import com.klyx.sampleplugin.icons.Settings
import com.klyx.sampleplugin.icons.Terminal
import com.klyx.sampleplugin.ui.FeatureItem
import com.klyx.sampleplugin.ui.MainDemoScreen
import kotlinx.coroutines.launch

private val features = listOf(
    FeatureItem(
        id = "process", label = "Process Execution",
        icon = DeployedCode, screenId = ScreenId("demo.process"),
    ),
    FeatureItem(
        id = "editor", label = "Editor & Tabs",
        icon = Description, screenId = ScreenId("demo.editor"),
    ),
    FeatureItem(
        id = "services", label = "Services & Settings",
        icon = Settings, screenId = ScreenId("demo.services"),
    ),
    FeatureItem(
        id = "terminal", label = "Terminal Sessions",
        icon = Terminal, screenId = ScreenId("demo.terminal"),
    ),
    FeatureItem(
        id = "events", label = "Event System",
        icon = EventNote, screenId = ScreenId("demo.events"),
    ),
    FeatureItem(
        id = "utility", label = "Utilities & Theme",
        icon = Palette, screenId = ScreenId("demo.utility"),
    ),
)

@PluginManifest(
    id = "com.klyx.sampleplugin",
    name = "Sample Plugin",
    description = "A comprehensive reference implementation demonstrating every feature of the Klyx Plugin API. Covers: plugin lifecycle, navigation, system/process execution, terminal sessions, file system, editor tabs, settings, fonts, events, screen registry, toolbar, toast, theme/colors, platform architecture, and utilities.",
    icon = "icon.png",
    author = Author(name = "Klyx"),
    license = "MIT",
    links = Links(
        source = "https://github.com/klyx-dev/SamplePlugin",
        issues = "https://github.com/klyx-dev/SamplePlugin/issues"
    )
)
class SamplePlugin : KlyxPlugin {

    private val screens: ScreenRegistry by plugin()
    private val toolbar: ToolbarRegistry by plugin()
    private val navigator: Navigator by plugin()
    private val fileSystem: FileSystem by plugin()
    private val openers: FileOpenerRegistry by plugin()
    private val settings: Settings by plugin()
    private val fonts: Fonts by plugin()
    private val tabs: Tabs by plugin()
    private val terminalManager: TerminalManager by plugin()

    // These use the `runtime()` delegate which resolves services
    // tied to this specific plugin instance.
    private val info: PluginInfo by runtime()

    override suspend fun onLoad() {
        val ctx = currentPluginContext()
        val lifecycleOwner = currentLifecycleOwner()
        ctx.app.toastHostState.showToast("${info.descriptor.name} v${info.version} loaded!")

        ctx.app.toastHostState.showToast("Plugin data dir: ${Paths.dataDir}")

        registerAllScreens()
        registerToolbarActions()
        pluginScope.launch { subscribeToEvents() }
    }

    override suspend fun onStart() {
        // onStart is called after dependencies are ready.

        val apiVersion = info.descriptor.minAppVersion

        // Register a custom FileOpener for ".mp3" extension files
        openers.register(object : FileOpener {
            override val id: String = "com.klyx.sampleplugin.opener"
            override val priority: Int = 50

            override suspend fun open(request: FileOpenRequest): WorkspaceTab? {
                if (request.extension != "mp3") return null
                return WorkspaceTab.Custom(
                    title = request.fileName,
                    id = request.uri.toString(),
                    content = { EditorDemoScreen(fileName = request.fileName) }
                )
            }
        })

        currentPluginContext().app.toastHostState.showToast(
            "SamplePlugin started! Min app version: $apiVersion",
        )
    }

    override suspend fun onStop() {
        currentPluginContext().app.toastHostState.showToast("SamplePlugin stopping!")
    }

    override suspend fun onUnload() {
        // Clean up all registered screens
        features.forEach { feature ->
            screens.unregister(feature.screenId)
        }

        // Clean up toolbar actions
        toolbar.unregister("demo.show_main")
        features.forEach { feature ->
            toolbar.unregister("demo.open.${feature.id}")
        }

        currentPluginContext().app.toastHostState.showToast("SamplePlugin unloaded!")
    }

    private fun registerAllScreens() {
        val mainId = ScreenId("demo.main")
        screens[mainId] = {
            MainDemoScreen(
                features = features,
                onFeatureClick = { feature ->
                    navigator.navigateTo(NavDestination.Custom(feature.screenId))
                },
                onBack = { navigator.navigateBack() },
            )
        }

        screens[ScreenId("demo.process")] = { ProcessDemoScreen(fileSystem) }
        screens[ScreenId("demo.editor")] = { EditorDemoScreen() }
        screens[ScreenId("demo.services")] = { ServiceDemoScreen(settings) }
        screens[ScreenId("demo.terminal")] = { TerminalDemoScreen(terminalManager) }
        screens[ScreenId("demo.events")] = { EventDemoScreen() }
        screens[ScreenId("demo.utility")] = { UtilityDemoScreen() }
    }

    private fun registerToolbarActions() {
        val cat = ToolbarCategory("SamplePlugin")

        toolbar.register(
            ToolbarAction(
                id = "demo.show_main",
                label = "SamplePlugin: Feature Demos",
                icon = ToolbarIcon(Android),
                category = cat,
                priority = 100,
                onClick = {
                    navigator.navigateTo(NavDestination.Custom(ScreenId("demo.main")))
                },
            ),
        )

        features.forEach { feature ->
            toolbar.register(
                ToolbarAction(
                    id = "demo.open.${feature.id}",
                    label = feature.label,
                    icon = ToolbarIcon(feature.icon),
                    category = cat,
                    priority = 90,
                    onClick = {
                        navigator.navigateTo(NavDestination.Custom(feature.screenId))
                    },
                ),
            )
        }
    }

    private suspend fun subscribeToEvents() {
        val context = currentPluginContext()
        val bus = context.eventBus
        val toast = context.app.toastHostState
        bus.subscribe(FileOpenedEvent::class) { event ->
            pluginScope.launch {
                toast.showToast(
                    "File opened: ${event.fileName} (tab: ${event.tabId})",
                )
            }
        }
        bus.subscribe(NewSessionEvent::class) { event ->
            pluginScope.launch {
                toast.showToast(
                    "New terminal session: ${event.id}",
                )
            }
        }
        bus.subscribe(TerminateAllSessionEvent::class) {
            pluginScope.launch {
                toast.showToast("All terminal sessions terminated!")
            }
        }
    }
}
