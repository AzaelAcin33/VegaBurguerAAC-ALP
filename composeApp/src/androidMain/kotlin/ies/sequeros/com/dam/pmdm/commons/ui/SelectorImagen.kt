package ies.sequeros.com.dam.pmdm.commons.ui

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun SelectorImagen(onImageSelected: (Uri) -> Unit) {
    val myuri = remember { mutableStateOf<Uri?>(null) }

    // Launcher para solicitar permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Si se concede el permiso, no hacemos nada aquí
        // El usuario tendrá que volver a presionar el botón
    }

    // Launcher para seleccionar imagen
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            myuri.value = uri
            onImageSelected(uri)
        }
    }

    Row {
        Button(onClick = {
            // Solicitar permiso apropiado según la versión de Android
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ (API 33+)
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                // Android 12 y anteriores
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            // Lanzar el selector de imágenes
            imageLauncher.launch("image/*")
        }) {
            Text("Seleccionar imagen")
        }
    }
}