package ies.sequeros.com.dam.pmdm.administrador.ui.categorias

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.commons.ui.ImagenDesdePath
import vegaburguer.composeapp.generated.resources.Res
import vegaburguer.composeapp.generated.resources.hombre

@Suppress("UnrememberedMutableState")
@Composable
fun CategoriaCard(
    item: CategoriaDTO,
    onActivate: (item: CategoriaDTO) -> Unit,
    onDeactivate: (item: CategoriaDTO) -> Unit,
    onView: () -> Unit,
    onEdit: (CategoriaDTO) -> Unit,
    onDelete: (item: CategoriaDTO) -> Unit
) {

    val cardAlpha by animateFloatAsState(if (item.enabled) 1f else 0.5f)
    val imagePath = mutableStateOf(if (item.imagePath != null && item.imagePath.isNotEmpty()) item.imagePath else "")
    val borderColor = when {
        !item.enabled -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.secondary
    }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .alpha(cardAlpha),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen circular con borde
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(3.dp, borderColor, CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                ImagenDesdePath(imagePath, Res.drawable.hombre, Modifier.fillMaxWidth())

            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = {},
                    label = {
                        Text(if (item.enabled) "Activo" else "Inactivo")
                    },
                    leadingIcon = {
                        Icon(
                            if (item.enabled) Icons.Default.CheckCircle else Icons.Default.Block,
                            contentDescription = null,
                            tint = if (item.enabled)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
            HorizontalDivider(
                Modifier.fillMaxWidth(0.8f),
                DividerDefaults.Thickness, MaterialTheme.colorScheme.outlineVariant
            )
            // ️ Acciones (fila inferior)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Activar / Desactivar
                OutlinedIconButton(
                    onClick = {
                        if (item.enabled)
                            onDeactivate(item)
                        else
                            onActivate(item)
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (item.enabled)
                            MaterialTheme.colorScheme.errorContainer
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Icon(
                        if (item.enabled) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (item.enabled) "Desactivar" else "Activar"
                    )
                }
                // Ver detalles
                OutlinedIconButton(onClick = onView) {
                    Icon(Icons.AutoMirrored.Filled.Article, contentDescription = "Ver")
                }

                // Editar
                OutlinedIconButton(onClick = { onEdit(item) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }

                // Eliminar
                OutlinedIconButton(
                    onClick = { onDelete(item) },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}