package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.ILinePedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoForm(
    pedidoId: String,
    pedidoRepo: IPedidoRepositorio,
    lineaPedidoRepo: ILinePedidoRepositorio,
    productoRepo: IProductoRepositorio,
    dependienteRepo: IDependienteRepositorio,
    onBack: () -> Unit
) {
    // Creamos el ViewModel manualmente usando una factory simple implicita al pasarle parametros
    // Si te da error de compilacion aqui, revisa el paso 4 abajo.
    val viewModel = viewModel {
        PedidoFormViewModel(
            pedidoId = pedidoId,
            pedidoRepo = pedidoRepo,
            lineaPedidoRepo = lineaPedidoRepo,
            productoRepo = productoRepo,
            dependienteRepo = dependienteRepo
        )
    }

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Pedido") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // --- Cabecera ---
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        CampoSoloLectura("Cliente", state.clienteName, Icons.Default.Person)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(Modifier.weight(1f)) { CampoSoloLectura("Fecha", state.fecha, Icons.Default.DateRange) }
                            Box(Modifier.weight(1f)) { CampoSoloLectura("Estado", state.estado, Icons.Default.Info) }
                        }
                        CampoSoloLectura("Atendido por", state.nombreDependiente, Icons.Default.Person)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Productos", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))

                // --- Lista de Productos ---
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.lineas) { linea ->
                        Card(elevation = CardDefaults.cardElevation(2.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(linea.nombreProducto, fontWeight = FontWeight.Bold)
                                    Text("${linea.cantidad} x ${linea.precioUnitario}€", style = MaterialTheme.typography.bodySmall)
                                }
                                Text("${"%.2f".format(linea.totalLinea)}€", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // --- Total ---
                Spacer(Modifier.height(16.dp))
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("TOTAL PEDIDO", style = MaterialTheme.typography.titleLarge)
                        Text("${"%.2f".format(state.total)}€", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CampoSoloLectura(label: String, valor: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    OutlinedTextField(
        value = valor,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        leadingIcon = { Icon(icon, null) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        enabled = false // Deshabilitado para que se vea gris pero legible gracias a los colores custom
    )
}