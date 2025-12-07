package ies.sequeros.com.dam.pmdm.commons.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import coil3.compose.rememberAsyncImagePainter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun ImagenDesdePath(
    path: MutableState<String>, //No quitar mutablestate porque peta
    default: DrawableResource,
    modifier: Modifier
) {
    val defaultPainter = painterResource(default)

    val painter = rememberAsyncImagePainter(
        model = path.value.ifEmpty { null },
        placeholder = defaultPainter,
        error = defaultPainter,
        fallback = defaultPainter
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
    )
}
