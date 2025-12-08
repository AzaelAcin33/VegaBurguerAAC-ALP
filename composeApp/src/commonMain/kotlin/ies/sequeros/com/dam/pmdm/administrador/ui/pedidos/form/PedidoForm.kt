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
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") } }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            Column(
                modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre elementos
            ) {
                // SECCIÓN DATOS CABECERA
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Verifica que 'value' usa 'state.clienteName'
                        OutlinedTextField(
                            value = state.clienteName,
                            onValueChange = {},
                            label = { Text("Cliente") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = state.fecha,
                                onValueChange = {},
                                label = { Text("Fecha") },
                                readOnly = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = state.estado,
                                onValueChange = {},
                                label = { Text("Estado") },
                                readOnly = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = state.nombreDependiente,
                            onValueChange = {},
                            label = { Text("Dependiente") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Text("Líneas de Pedido (${state.lineas.size})", style = MaterialTheme.typography.titleMedium)

                // SECCIÓN LISTA PRODUCTOS
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.lineas) { linea ->
                        Card(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(linea.nombreProducto, fontWeight = FontWeight.Bold)
                                    Text("${linea.cantidad} x ${linea.precioUnitario} €")
                                }
                                Text("${linea.totalLinea} €", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // SECCIÓN TOTAL
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("TOTAL", style = MaterialTheme.typography.titleLarge)
                        Text("${state.total} €", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}