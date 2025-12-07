package ies.sequeros.com.dam.pmdm.cliente.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.cliente.ui.viewmodel.ClienteTPVViewModel

@Composable
fun CategoriasScreen(
    viewModel: ClienteTPVViewModel,
    onCategoriaSelected: (String) -> Unit
) {
    val categorias by viewModel.categorias.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCategorias()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Selecciona una Categoría", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { categoria ->
                Card(
                    modifier = Modifier
                        .height(100.dp)
                        .clickable { onCategoriaSelected(categoria.id) }, // Asumiendo id
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(categoria.name) // Ajustar campo nombre según modelo
                    }
                }
            }
        }
    }
}