package com.clurio.scmu.mailbox.ui.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

fun getLockIcon(): ImageVector = ImageVector.Builder(
    name = "Locked", defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
    viewportWidth = 330.0f, viewportHeight = 330.0f
).apply {
    path(
        fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0.0f,
        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
        pathFillType = NonZero
    ) {
        moveTo(65.0f, 330.0f)
        horizontalLineToRelative(200.0f)
        curveToRelative(8.28f, 0.0f, 15.0f, -6.72f, 15.0f, -15.0f)
        verticalLineTo(145.0f)
        curveToRelative(0.0f, -8.28f, -6.72f, -15.0f, -15.0f, -15.0f)
        horizontalLineToRelative(-15.0f)
        verticalLineTo(85.0f)
        curveToRelative(0.0f, -46.87f, -38.13f, -85.0f, -85.0f, -85.0f)
        reflectiveCurveTo(80.0f, 38.13f, 80.0f, 85.0f)
        verticalLineToRelative(45.0f)
        horizontalLineTo(65.0f)
        curveToRelative(-8.28f, 0.0f, -15.0f, 6.72f, -15.0f, 15.0f)
        verticalLineToRelative(170.0f)
        curveTo(50.0f, 323.28f, 56.72f, 330.0f, 65.0f, 330.0f)
        close()
        moveTo(110.0f, 85.0f)
        curveToRelative(0.0f, -30.33f, 24.67f, -55.0f, 55.0f, -55.0f)
        curveToRelative(30.33f, 0.0f, 55.0f, 24.67f, 55.0f, 55.0f)
        verticalLineToRelative(45.0f)
        horizontalLineTo(110.0f)
        verticalLineTo(85.0f)
        close()
    }
}
    .build()


@Preview
@Composable
private fun PreviewLockIcon() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(
            imageVector = getLockIcon(),
            contentDescription = "",
            Modifier.size(256.dp)
        )
    }
}
