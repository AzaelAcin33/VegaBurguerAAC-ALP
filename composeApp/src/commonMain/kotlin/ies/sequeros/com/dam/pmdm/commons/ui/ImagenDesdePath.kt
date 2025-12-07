package ies.sequeros.com.dam.pmdm.commons.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource

@Composable
expect fun ImagenDesdePath(
    path: MutableState<String>,
    default: DrawableResource,
    modifier: Modifier
)
