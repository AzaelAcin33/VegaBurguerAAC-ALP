package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AssistChip
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto

@Suppress("UnrememberedMutableState")
@Composable
fun PedidoCard(
    item: PedidoDTO,
    productos: List<Producto>,
    mapaDependientes: Map<String, String>,
    mapaProductos: Map<String, String>,
    onView: () -> Unit,
    onEdit: (PedidoDTO) -> Unit,
    onDelete: (item: PedidoDTO) -> Unit
) {
    val cardAlpha by animateFloatAsState(1f)
    val nombreDependiente = mapaDependientes[item.dependienteId] ?: "ID: ${item.dependienteId}"
    val nombreProducto = mapaProductos[item.lineas.firstOrNull()?.productoId] ?: "ID: ${item.lineas.firstOrNull()?.productoId}"

    // Normalización de IDs para evitar problemas de espacios, mayúsculas o unicode
    fun normalizeId(id: String?): String =
        id?.trim()?.lowercase()?.replace("\\s+".toRegex(), " ") ?: ""

    // Preconstruimos un mapa de productos para búsqueda rápida
    val productosMap: Map<String, Producto> = productos.associateBy { normalizeId(it.id) }

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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.size(40.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Nombre del cliente: ${item.clienteName}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Total de la compra: ${String.format("%.2f", item.total)} €",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Con el dependiente: $nombreDependiente",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Productos comprados:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                item.lineas.forEach { linea ->
                    val producto = productosMap[normalizeId(linea.productoId)]

                    val precioUnitario = linea.precioUnitario.toString().toDoubleOrNull() ?: 0.0

                    Text(
                        text = "- ${(producto?.name ?: "Producto Name: ${nombreProducto}")} | " +
                                "${linea.cantidad} ud. | " +
                                "${"%.2f".format(precioUnitario)} € c/u",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text(item.estado) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }

            HorizontalDivider(
                Modifier.fillMaxWidth(0.8f),
                DividerDefaults.Thickness,
                MaterialTheme.colorScheme.outlineVariant
            )

            // Acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ver detalles
                /*OutlinedIconButton(onClick = onView) {
                    Icon(Icons.AutoMirrored.Filled.Article, contentDescription = "Ver")
                }

                // Editar
                OutlinedIconButton(onClick = { onEdit(item) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }*/

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
