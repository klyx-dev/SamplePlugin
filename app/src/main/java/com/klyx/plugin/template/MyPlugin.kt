package com.klyx.plugin.template

import com.klyx.api.plugin.KlyxPlugin
import com.klyx.api.plugin.plugin
import com.klyx.api.service.Logger
import com.klyx.api.service.info

class MyPlugin : KlyxPlugin {

    private val logger: Logger by plugin()

    override suspend fun onLoad() {
        logger.info("Hello from MyPlugin")
    }

    override suspend fun onStart() {
        logger.info("MyPlugin is running")
    }

    override suspend fun onStop() {
        logger.info("MyPlugin is stopping")
    }

    override suspend fun onUnload() {
        logger.info("MyPlugin is unloading")
    }
}
