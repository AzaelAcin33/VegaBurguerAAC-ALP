package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoForm(
    productoRepo: IProductoRepositorio,
    dependienteRepo: IDependienteRepositorio,
    onClose: () -> Unit,
    onConfirm: (PedidoFormState) -> Unit = {},
    viewModel: PedidoFormViewModel = viewModel {
        PedidoFormViewModel(
            productoRepo = productoRepo,
            dependienteRepo = dependienteRepo,
            onSuccess = onConfirm
        )
    }
) {
    val state by viewModel.uiState.collectAsState()
    val isValid by viewModel.isFormValid.collectAsState()
    var dependienteExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(24.dp).fillMaxSize()) {

            // --- HEADER ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ListAlt, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("Nuevo Pedido", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.weight(1f))
                Text("Fecha: ${state.fecha}", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(16.dp))

            // --- CLIENTE ---
            OutlinedTextField(
                value = state.clienteName,
                onValueChange = { viewModel.onClienteNameChange(it) },
                label = { Text("Nombre del Cliente") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, null) },
                isError = state.clienteNameError != null,
                singleLine = true
            )
            state.clienteNameError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall) }

            Spacer(Modifier.height(12.dp))

            // --- DEPENDIENTE (COMBOBOX) ---
            ExposedDropdownMenuBox(
                expanded = dependienteExpanded,
                onExpandedChange = { dependienteExpanded = !dependienteExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.dependienteSeleccionadoNombre,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Atendido por (Dependiente)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dependienteExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    isError = state.dependienteId == null
                )

                ExposedDropdownMenu(
                    expanded = dependienteExpanded,
                    onDismissRequest = { dependienteExpanded = false }
                ) {
                    if (state.dependientesDisponibles.isEmpty()) {
                        DropdownMenuItem(text = { Text("Sin datos") }, onClick = { dependienteExpanded = false })
                    } else {
                        state.dependientesDisponibles.forEach { dep ->
                            DropdownMenuItem(
                                text = { Text(dep.name) },
                                onClick = {
                                    viewModel.onDependienteSelected(dep)
                                    dependienteExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            // --- CATALOGO PRODUCTOS ---
            Text("Catálogo Disponible", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            if (state.productosDisponibles.isEmpty()) {
                Text("No hay productos cargados.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.productosDisponibles) { producto ->
                        SuggestionChip(
                            onClick = { viewModel.onProductoRealSelect(producto) },
                            label = { Text("${producto.name} (${producto.price}€)") },
                            icon = { Icon(Icons.Default.Fastfood, null, modifier = Modifier.size(16.dp)) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = if (state.tempProductoId == producto.id) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        )
                    }
                }
            }

            // --- INPUT LINEA ---
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.tempProductoNombre,
                    onValueChange = {},
                    label = { Text("Producto") },
                    modifier = Modifier.weight(2f),
                    readOnly = true,
                    enabled = false
                )
                OutlinedTextField(
                    value = state.tempCantidad,
                    onValueChange = { viewModel.onTempCantidadChange(it) },
                    label = { Text("Cant.") },
                    modifier = Modifier.weight(0.7f),
                    singleLine = true
                )
                Button(onClick = { viewModel.agregarLinea() }, shape = RoundedCornerShape(12.dp), modifier = Modifier.padding(top = 8.dp)) {
                    Icon(Icons.Default.AddShoppingCart, null)
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- LISTA LINEAS ---
            Card(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.3f))
            ) {
                LazyColumn(contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(state.lineas) { index, linea ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { viewModel.toggleEntregado(index) }) {
                                Icon(
                                    if (linea.entregado) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                    null,
                                    tint = if (linea.entregado) Color(0xFF4CAF50) else Color.Gray
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(linea.productName, fontWeight = FontWeight.Bold)
                                Text("${linea.cantidad} x ${linea.precioUnitario}€", style = MaterialTheme.typography.bodySmall)
                            }
                            Text("${"%.2f".format(linea.totalLinea)} €", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                            IconButton(onClick = { viewModel.eliminarLinea(index) }) {
                                Icon(Icons.Default.DeleteOutline, null, tint = MaterialTheme.colorScheme.error)
                            }
                        }
                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
                    }
                }
            }
            if (state.lineasError != null) Text(state.lineasError!!, color = MaterialTheme.colorScheme.error)

            Spacer(Modifier.height(16.dp))

            // --- FOOTER ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text("TOTAL: ", style = MaterialTheme.typography.titleLarge)
                Text("${"%.2f".format(state.totalPedido)} €", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = onClose) { Text("Cancelar") }
                Button(onClick = { viewModel.submit(onConfirm) }, enabled = isValid) {
                    Icon(Icons.Default.Save, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Guardar Pedido")
                }
            }
        }
    }
}