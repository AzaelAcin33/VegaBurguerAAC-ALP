package ies.sequeros.com.dam.pmdm.commons.ui

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import java.io.File

@Composable
actual fun ImagenDesdePath(
    path: MutableState<String>,
    default: DrawableResource,
    modifier: Modifier
) {
    val raw = path.value

    if (raw.isNullOrBlank()) {
        Image(
            painter = painterResource(default),
            contentDescription = null,
            modifier = modifier
        )
        return
    }

    val context = LocalContext.current

    val data: Any? = try {
        when {
            raw.startsWith("http://", true) ||
                    raw.startsWith("https://", true) -> raw

            raw.startsWith("content://", true) ||
                    raw.startsWith("file://", true) -> Uri.parse(raw)

            raw.startsWith("/") -> {
                val f = File(raw)
                if (f.exists()) Uri.fromFile(f)
                else {
                    Log.w("ImagenDesdePath", "Archivo no existe: $raw")
                    null
                }
            }

            else -> Uri.parse(raw)
        }
    } catch (e: Exception) {
        Log.e("ImagenDesdePath", "Error procesando path: $raw", e)
        null
    }

    if (data != null) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(data)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .listener(
                    onError = { _, result ->
                        Log.e("Coil", "Error cargando imagen: ${result.throwable}")
                    }
                )
                .build(),
            contentDescription = null,
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(default),
            contentDescription = null,
            modifier = modifier
        )
    }
}
