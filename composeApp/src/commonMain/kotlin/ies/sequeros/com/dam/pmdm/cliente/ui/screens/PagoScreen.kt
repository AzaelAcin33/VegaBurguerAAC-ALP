package ies.sequeros.com.dam.pmdm.cliente.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.cliente.ui.viewmodel.ClienteTPVViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoScreen(
    viewModel: ClienteTPVViewModel,
    onPedidoGuardado: () -> Unit
) {
    val carrito by viewModel.carrito.collectAsState()
    val total by viewModel.totalCarrito.collectAsState()
    val dependientes by viewModel.dependientes.collectAsState()
    val dependienteSeleccionado by viewModel.dependienteSeleccionado.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadDependientes()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Resumen del Pedido", style = MaterialTheme.typography.headlineSmall)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(carrito) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${item.cantidad}x ${item.producto.nombre}")
                    Text("${item.total}€")
                }
            }
        }

        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Total a pagar: ${total}€", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Selector de Dependiente
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = dependienteSeleccionado?.name ?: "Seleccionar dependiente",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                dependientes.forEach { dependiente ->
                    DropdownMenuItem(
                        text = { Text(dependiente.name) },
                        onClick = {
                            viewModel.selectDependiente(dependiente)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.finalizarPedido(onPedidoGuardado) },
            modifier = Modifier.fillMaxWidth(),
            enabled = dependienteSeleccionado != null && carrito.isNotEmpty()
        ) {
            Text("Confirmar y Pagar")
        }
    }
}