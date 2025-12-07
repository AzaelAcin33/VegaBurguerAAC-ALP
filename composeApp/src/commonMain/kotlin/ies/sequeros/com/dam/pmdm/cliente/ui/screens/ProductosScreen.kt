package ies.sequeros.com.dam.pmdm.cliente.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.cliente.ui.viewmodel.ClienteTPVViewModel
import ies.sequeros.com.dam.pmdm.commons.ui.ImagenDesdePath
import vegaburguer.composeapp.generated.resources.Res
import vegaburguer.composeapp.generated.resources.hombre

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    categoriaId: String,
    viewModel: ClienteTPVViewModel,
    onNavigateToPago: () -> Unit,
    onBack: () -> Unit
) {
    val productos by viewModel.productos.collectAsState()
    val carrito by viewModel.carrito.collectAsState()
    val total by viewModel.totalCarrito.collectAsState()

    LaunchedEffect(categoriaId) {
        viewModel.loadProductos(categoriaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            if (carrito.isNotEmpty()) {
                FloatingActionButton(onClick = onNavigateToPago) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Pagar")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${total}€")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn {
                items(productos) { producto ->
                    val enCarrito = carrito.find { it.producto.id == producto.id }?.cantidad ?: 0

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val imagePathState = remember {
                                mutableStateOf(producto.imagePath ?: "")
                            }

                            ImagenDesdePath(
                                path = imagePathState,
                                default = Res.drawable.hombre,
                                modifier = Modifier.size(60.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(producto.nombre, style = MaterialTheme.typography.bodyLarge)
                                Text("${producto.precio}€", style = MaterialTheme.typography.bodyMedium)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (enCarrito > 0) {
                                    TextButton(onClick = { viewModel.removeFromCarrito(producto) }) {
                                        Text("-")
                                    }
                                    Text(enCarrito.toString())
                                }
                                IconButton(onClick = { viewModel.addToCarrito(producto) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Añadir")
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
