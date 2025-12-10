package ies.sequeros.com.dam.pmdm.cliente.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun CategoriasScreen(
    viewModel: ClienteTPVViewModel,
    onCategoriaSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    //Para el tema de que salgan todas las categorias y se puedan escoger
    val categorias by viewModel.categorias.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCategorias()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Selecciona una Categoría") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias) { categoria ->
                    if (categoria.enabled != false){
                        Card(
                            modifier = Modifier
                                .height(150.dp)
                                .clickable { onCategoriaSelected(categoria.id) },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                val imagePathState = remember { mutableStateOf(categoria.imagePath) }

                                ImagenDesdePath(
                                    path = imagePathState,
                                    default = Res.drawable.hombre,
                                    modifier = Modifier.size(80.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(categoria.name)
                            }
                        }
                    }
                }
            }
        }
    }
}
