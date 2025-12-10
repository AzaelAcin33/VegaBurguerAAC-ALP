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

    //si es vacio o null imagen por defecto
    if (raw.isNullOrBlank()) {
        Image(
            painter = painterResource(default),
            contentDescription = null,
            modifier = modifier
        )
        return
    }

    // Obtenemos el contexto (necesario para construir el ImageRequest de Coil).
    val context = LocalContext.current

    // Normalización de datos: Convertimos el string en algo que Coil entienda (Uri o String).
    val data: Any? = try {
        when {
            // Caso Web: Si es http/https, Coil lo maneja como String.
            raw.startsWith("http://", true) ||
                    raw.startsWith("https://", true) -> raw

            // Caso Android URI: Content Providers o File Providers.
            raw.startsWith("content://", true) ||
                    raw.startsWith("file://", true) -> Uri.parse(raw)

            // Caso Archivo Local Absoluto: Empieza con "/" (ej: /storage/...).
            raw.startsWith("/") -> {
                val f = File(raw)
                // Verificamos físicamente que el archivo exista en el disco.
                if (f.exists()) Uri.fromFile(f)
                else {
                    Log.w("ImagenDesdePath", "Archivo no existe: $raw")
                    null // Si no existe, forzamos null para ir al 'else' final.
                }
            }

            //Intentamos parsear cualquier otra cosa como URI.
            else -> Uri.parse(raw)
        }
    } catch (e: Exception) {
        // Capturamos errores de sintaxis en el path para evitar crashes.
        Log.e("ImagenDesdePath", "Error procesando path: $raw", e)
        null
    }

    //Decidimos qué componente pintar según el resultado anterior.
    if (data != null) {
        // Carga asíncrona con Coil si tenemos datos válidos.
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(data)
                // Configuramos caché agresiva (memoria y disco) para rendimiento.
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .listener(
                    // Para depurar
                    onError = { _, result ->
                        Log.e("Coil", "Error cargando imagen: ${result.throwable}")
                    }
                )
                .build(),
            contentDescription = null,
            modifier = modifier
        )
    } else {
        // Si el path era inválido, el archivo no existía o hubo error.
        Image(
            painter = painterResource(default),
            contentDescription = null,
            modifier = modifier
        )
    }
}
