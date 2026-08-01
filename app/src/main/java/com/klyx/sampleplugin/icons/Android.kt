package com.klyx.sampleplugin.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Android: ImageVector
    get() {
        if (_Android != null) {
            return _Android!!
        }
        _Android = ImageVector.Builder(
            name = "Android",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(40f, 720f)
                quadToRelative(10f, -105f, 64f, -191f)
                reflectiveQuadToRelative(139f, -140f)
                lineToRelative(-77f, -129f)
                quadToRelative(-9f, -14f, -4.5f, -30f)
                reflectiveQuadToRelative(18.5f, -24f)
                quadToRelative(14f, -9f, 30f, -5f)
                reflectiveQuadToRelative(24f, 18f)
                lineToRelative(80f, 133f)
                quadToRelative(38f, -16f, 79.5f, -24f)
                reflectiveQuadToRelative(86.5f, -8f)
                quadToRelative(45f, 0f, 86.5f, 8f)
                reflectiveQuadToRelative(79.5f, 24f)
                lineToRelative(80f, -133f)
                quadToRelative(8f, -14f, 24f, -18f)
                reflectiveQuadToRelative(30f, 5f)
                quadToRelative(14f, 8f, 18f, 24f)
                reflectiveQuadToRelative(-4f, 30f)
                lineToRelative(-77f, 129f)
                quadToRelative(85f, 54f, 139f, 140f)
                reflectiveQuadToRelative(64f, 191f)
                lineTo(40f, 720f)
                close()
                moveTo(720.5f, 568.5f)
                quadTo(722f, 551f, 711f, 534f)
                reflectiveQuadToRelative(-27f, -22f)
                quadToRelative(-16f, -5f, -29f, 4f)
                reflectiveQuadToRelative(-15f, 26f)
                quadToRelative(-2f, 17f, 9f, 34f)
                reflectiveQuadToRelative(27.5f, 22.5f)
                quadTo(693f, 604f, 706f, 595f)
                reflectiveQuadToRelative(14.5f, -26.5f)
                close()
                moveTo(284f, 598f)
                quadToRelative(16f, -5f, 27f, -22f)
                reflectiveQuadToRelative(9.5f, -34.5f)
                quadTo(319f, 524f, 306f, 515f)
                reflectiveQuadToRelative(-29.5f, -3.5f)
                quadTo(260f, 517f, 249f, 534f)
                reflectiveQuadToRelative(-9f, 34f)
                quadToRelative(2f, 17f, 15f, 26f)
                reflectiveQuadToRelative(29f, 4f)
                close()
            }
        }.build()

        return _Android!!
    }

@Suppress("ObjectPropertyName")
private var _Android: ImageVector? = null
