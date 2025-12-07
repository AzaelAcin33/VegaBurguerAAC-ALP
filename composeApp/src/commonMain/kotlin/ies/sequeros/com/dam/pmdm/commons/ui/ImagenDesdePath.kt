package ies.sequeros.com.dam.pmdm.commons.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import java.io.File
import javax.imageio.ImageIO

@Composable
actual fun ImagenDesdePath(
    path: MutableState<String>,
    default: DrawableResource,
    modifier: Modifier = Modifier
) {
    val file = File(path.value)

    if (file.exists()) {
        val buffered = ImageIO.read(file)
        if (buffered != null) {
            Image(
                painter = BitmapPainter(buffered.asImageBitmap()),
                contentDescription = null,
                modifier = modifier
            )
            return
        }
    }

    Image(
        painter = painterResource(default),
        contentDescription = null,
        modifier = modifier
    )
}
