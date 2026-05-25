package ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IconVisibility: ImageVector get() = ImageVector.Builder(
    name           = "Visibility",
    defaultWidth   = 24.dp,
    defaultHeight  = 24.dp,
    viewportWidth  = 24f,
    viewportHeight = 24f,
).apply {
    path(fill = SolidColor(Color(0xFF000000))) {
        moveTo(12f, 4.5f)
        curveTo(7f, 4.5f, 2.73f, 7.61f, 1f, 12f)
        curveTo(2.73f, 16.39f, 7f, 19.5f, 12f, 19.5f)
        curveTo(17f, 19.5f, 21.27f, 16.39f, 23f, 12f)
        curveTo(21.27f, 7.61f, 17f, 4.5f, 12f, 4.5f)
        close()
        moveTo(12f, 17f)
        curveTo(9.24f, 17f, 7f, 14.76f, 7f, 12f)
        curveTo(7f, 9.24f, 9.24f, 7f, 12f, 7f)
        curveTo(14.76f, 7f, 17f, 9.24f, 17f, 12f)
        curveTo(17f, 14.76f, 14.76f, 17f, 12f, 17f)
        close()
        moveTo(12f, 9f)
        curveTo(10.34f, 9f, 9f, 10.34f, 9f, 12f)
        curveTo(9f, 13.66f, 10.34f, 15f, 12f, 15f)
        curveTo(13.66f, 15f, 15f, 13.66f, 15f, 12f)
        curveTo(15f, 10.34f, 13.66f, 9f, 12f, 9f)
        close()
    }
}.build()

val IconVisibilityOff: ImageVector get() = ImageVector.Builder(
    name           = "VisibilityOff",
    defaultWidth   = 24.dp,
    defaultHeight  = 24.dp,
    viewportWidth  = 24f,
    viewportHeight = 24f,
).apply {
    path(fill = SolidColor(Color(0xFF000000))) {
        moveTo(2f, 4.27f); lineTo(3.27f, 3f); lineTo(21f, 20.73f)
        lineTo(19.73f, 22f); lineTo(16.06f, 18.33f)
        curveTo(14.77f, 18.75f, 13.41f, 19f, 12f, 19f)
        curveTo(7f, 19f, 2.73f, 15.9f, 1f, 11.5f)
        curveTo(1.69f, 9.76f, 2.79f, 8.24f, 4.19f, 7.04f)
        lineTo(2f, 4.27f); close()
        moveTo(12f, 6f)
        curveTo(14.76f, 6f, 17f, 8.24f, 17f, 11f)
        curveTo(17f, 11.64f, 16.87f, 12.26f, 16.64f, 12.82f)
        lineTo(19.57f, 15.75f)
        curveTo(21.07f, 14.49f, 22.27f, 12.86f, 23f, 11f)
        curveTo(21.27f, 6.61f, 17f, 3.5f, 12f, 3.5f)
        curveTo(10.59f, 3.5f, 9.24f, 3.75f, 7.97f, 4.2f)
        lineTo(10.16f, 6.38f)
        curveTo(10.74f, 6.14f, 11.35f, 6f, 12f, 6f); close()
        moveTo(10.93f, 7.14f); lineTo(13f, 9.21f)
        curveTo(13.57f, 9.53f, 14f, 10.21f, 14f, 11f)
        curveTo(14f, 12.1f, 13.1f, 13f, 12f, 13f)
        curveTo(11.21f, 13f, 10.53f, 12.57f, 10.21f, 12f)
        lineTo(8.15f, 9.94f)
        curveTo(8.06f, 10.27f, 8f, 10.63f, 8f, 11f)
        curveTo(8f, 13.76f, 10.24f, 16f, 13f, 16f)
        curveTo(13.37f, 16f, 13.72f, 15.94f, 14.05f, 15.85f)
        lineTo(16.53f, 18.32f)
        curveTo(15.11f, 18.75f, 13.59f, 19f, 12f, 19f)
        curveTo(7f, 19f, 2.73f, 15.9f, 1f, 11.5f)
        curveTo(1.85f, 9.28f, 3.35f, 7.38f, 5.27f, 6.1f)
        lineTo(7.32f, 8.14f); close()
    }
}.build()
