package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio

@Composable
fun PedidoForm(
    productoRepo: IProductoRepositorio, // <-- Inyectamos el repo aquí
    onClose: () -> Unit,
    onConfirm: (PedidoFormState) -> Unit = {},
    viewModel: PedidoFormViewModel = viewModel {
        PedidoFormViewModel(
            productoRepo = productoRepo, // Se lo pasamos al ViewModel
            onSuccess = onConfirm
        )
    }
) {
    val state by viewModel.uiState.collectAsState()
    val isValid by viewModel.isFormValid.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize()
        ) {

            // --- 1. ENCABEZADO ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.ListAlt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text("Nuevo Pedido", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text("Fecha: ${state.fecha}", style = MaterialTheme.typography.bodySmall)
                    state.dependienteId?.let {
                        Text(
                            "Atiende: $it",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- 2. DATOS DEL CLIENTE ---
            OutlinedTextField(
                value = state.clienteName,
                onValueChange = { viewModel.onClienteNameChange(it) },
                label = { Text("Nombre del Cliente") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = state.clienteNameError != null,
                singleLine = true
            )
            state.clienteNameError?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            // --- 3. CATÁLOGO DE PRODUCTOS (Desde BBDD) ---
            Text(
                "Catálogo Disponible",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (state.productosDisponibles.isEmpty()) {
                Box(modifier = Modifier.padding(8.dp)) {
                    Text(
                        "Cargando productos o lista vacía...",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.productosDisponibles) { producto ->
                        SuggestionChip(
                            onClick = { viewModel.onProductoRealSelect(producto) },
                            label = {
                                Text("${producto.name} (${producto.price}€)")
                            },
                            icon = {
                                Icon(
                                    Icons.Default.Fastfood,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = if (state.tempProductoId == producto.id)
                                    MaterialTheme.colorScheme.primaryContainer
                                else Color.Transparent
                            )
                        )
                    }
                }
            }

            // --- 4. INPUTS PARA AGREGAR LÍNEA ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Nombre del producto (Solo lectura, se llena al seleccionar arriba)
                OutlinedTextField(
                    value = state.tempProductoNombre,
                    onValueChange = {},
                    label = { Text("Producto seleccionado") },
                    modifier = Modifier.weight(2f),
                    readOnly = true,
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.primary
                    )
                )

                // Cantidad (Editable)
                OutlinedTextField(
                    value = state.tempCantidad,
                    onValueChange = { viewModel.onTempCantidadChange(it) },
                    label = { Text("Cant.") },
                    modifier = Modifier.weight(0.7f),
                    singleLine = true
                )

                // Botón Agregar (+)
                Button(
                    onClick = { viewModel.agregarLinea() },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(top = 8.dp),
                    enabled = state.tempProductoId.isNotBlank()
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = "Añadir")
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- 5. LISTA DE LÍNEAS DE PEDIDO ---
            Text("Carrito del Pedido", style = MaterialTheme.typography.titleMedium)

            Card(
                modifier = Modifier
                    .weight(1f) // Ocupa el espacio restante
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                        alpha = 0.3f
                    )
                )
            ) {
                if (state.lineas.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay productos añadidos", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(state.lineas) { index, linea ->
                            LineaPedidoItem(
                                linea = linea,
                                onToggleEntregado = { viewModel.toggleEntregado(index) },
                                onDelete = { viewModel.eliminarLinea(index) }
                            )
                        }
                    }
                }
            }

            if (state.lineasError != null) {
                Text(
                    text = state.lineasError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // --- 6. TOTALES Y ACCIONES ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Text("TOTAL: ", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "${"%.2f".format(state.totalPedido)} €",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onClose,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cancelar")
                }

                Button(
                    onClick = { viewModel.submit(onConfirm) },
                    enabled = isValid,
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Guardar Pedido")
                }
            }
        }
    }
}

// Componente auxiliar para pintar cada fila del carrito
@Composable
private fun LineaPedidoItem(
    linea: LineaPedidoFormState,
    onToggleEntregado: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Estado Entregado (Check)
            IconButton(onClick = onToggleEntregado) {
                Icon(
                    imageVector = if (linea.entregado) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Estado Entregado",
                    tint = if (linea.entregado) Color(0xFF4CAF50) else Color.Gray
                )
            }

            Spacer(Modifier.width(8.dp))

            // Info Producto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = linea.productName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${linea.cantidad} x ${linea.precioUnitario}€",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Precio Total Línea
            Text(
                text = "${"%.2f".format(linea.totalLinea)} €",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Borrar
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}