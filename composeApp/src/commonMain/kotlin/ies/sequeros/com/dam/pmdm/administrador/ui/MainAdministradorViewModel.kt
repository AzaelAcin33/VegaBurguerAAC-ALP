package ies.sequeros.com.dam.pmdm.administrador.ui


import androidx.compose.runtime.State
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.DependienteDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.ListarDependientesUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ListarProductoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class ItemOption(val icon: ImageVector, val action:()->Unit, val name:String, val admin: Boolean)
class MainAdministradorViewModel(
    //intentar obtener dependiente nombre
    private val listarDependientesUseCase: ListarDependientesUseCase,
    private val listarProductoUseCase: ListarProductoUseCase
): ViewModel() {


    private val _options= MutableStateFlow<List<ItemOption>>(emptyList())
    //intentar obtener dependiente nombre
    private val _dependientes = MutableStateFlow<List<DependienteDTO>>(emptyList())
    private val _productos = MutableStateFlow<List<ProductoDTO>>(emptyList())
    val dependientes: StateFlow<List<DependienteDTO>> = _dependientes.asStateFlow()
    val productos: StateFlow<List<ProductoDTO>> = _productos.asStateFlow()

    init {
        // 3. Carga los dependientes automáticamente al iniciar el ViewModel
        cargarDependientes()
        cargarProductos()
    }
    private fun cargarDependientes() {
        viewModelScope.launch {
            try {
                // Llama al invoke() de tu UseCase
                _dependientes.value = listarDependientesUseCase.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun cargarProductos() {
        viewModelScope.launch {
            try {
                // Llama al invoke() de tu UseCase
                _productos.value = listarProductoUseCase.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun setOptions(options:List<ItemOption>){

        _options.value = options.toList() // fuerza una nueva referencia

    }
    val filteredItems = _options/*combine(_options, appUser) { items, user ->
        val isAdmin = user?.isAdmin ?: false
       // if (isAdmin)
            items // muestra todo
        //else
        //   items.filter { !it.admin } // oculta admin-only
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )*/


}