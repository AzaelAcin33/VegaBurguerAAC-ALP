package ies.sequeros.com.dam.pmdm.administrador.ui.categorias

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.ui.MainAdministradorViewModel

@Composable
fun Categoria (
    mainAdministradorViewModel: MainAdministradorViewModel,
    categoriasViewModel: CategoriasViewModel,
    onSelectItem:(CategoriaDTO?)->Unit
){
    val items by categoriasViewModel.items.collectAsState()
    var searchText by remember { mutableStateOf("")}

}