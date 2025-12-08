package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.ui.MainAdministradorViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.PedidosViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.PedidoCard

@Composable
fun Pedidos(
    mainAdministradorViewModel: MainAdministradorViewModel,
    pedidosViewModel: PedidosViewModel,
    onSelectItem: (PedidoDTO?) -> Unit
) {
    val items by pedidosViewModel.items.collectAsState()
    val productos by pedidosViewModel.productos.collectAsState()

    //intentar obtener nombre de dependiente
    val listaDependientes by mainAdministradorViewModel.dependientes.collectAsState()
    val listaProductos by mainAdministradorViewModel.productos.collectAsState()

    val mapaProductos = remember (listaProductos){
        listaProductos.associate { producto ->
            producto.id to producto.name // Mapeamos ID a NAME según tu DTO
        }
    }

    val mapaDependientes = remember(listaDependientes) {
        listaDependientes.associate { dependiente ->
            dependiente.id to dependiente.name // Mapeamos ID a NAME según tu DTO
        }
    }
    var searchText by remember { mutableStateOf("") }
    val filteredItems = items.filter {
        if (searchText.isNotBlank()) {
            it.clienteName.contains(searchText, ignoreCase = true)
        } else {
            true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 🔹 Barra superior fija con buscador y botón añadir
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Buscar...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = {
                    pedidosViewModel.setSelectedPedido(null)
                    onSelectItem(null);

                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier
                        .size(ButtonDefaults.IconSize)
                )
            }

        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(
                minSize = 512.dp
            )
        ) {
            items(filteredItems.size) { index ->

                val pedido = filteredItems[index]

                PedidoCard(
                    item = pedido,
                    mapaDependientes = mapaDependientes,
                    mapaProductos = mapaProductos,
                    onView = {

                        onSelectItem(pedido)
                    },
                    onEdit = {

                        onSelectItem(it)
                    },
                    onDelete = {

                        pedidosViewModel.delete(pedido)
                    },
                    productos = productos
                )
            }
        }

    }


}