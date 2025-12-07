package ies.sequeros.com.dam.pmdm.cliente.ui.viewmodel

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.ItemOption
import kotlinx.coroutines.flow.MutableStateFlow

data class ItemOption(val icon: ImageVector, val action:()->Unit, val name:String, val admin: Boolean)
class ClienteTPVViewModel(): ViewModel() {

    private val _options= MutableStateFlow<List<ies.sequeros.com.dam.pmdm.administrador.ui.ItemOption>>(emptyList())

    fun setOptions(options:List<ItemOption>){

        _options.value = options.toList() // fuerza una nueva referencia

    }
    val filteredItems = _options
    //El ViewModel gestiona el estado de la interfaz y la lógica de interacción con los use cases.

    //Puede tener funciones como cargarCategorias(), agregarProductoAlCarrito(), realizarPago()
}