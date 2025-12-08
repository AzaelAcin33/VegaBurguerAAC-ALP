package ies.sequeros.com.dam.pmdm.administrador.ui


import androidx.compose.runtime.State
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.DependienteDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.ListarDependientesUseCase


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class ItemOption(val icon: ImageVector, val action:()->Unit, val name:String, val admin: Boolean)
class MainAdministradorViewModel(
    //intentar obtener dependiente nombre
    private val listarDependientesUseCase: ListarDependientesUseCase
): ViewModel() {


    private val _options= MutableStateFlow<List<ItemOption>>(emptyList())
    //intentar obtener dependiente nombre
    private val _dependientes = MutableStateFlow<List<DependienteDTO>>(emptyList())
    val dependientes: StateFlow<List<DependienteDTO>> = _dependientes.asStateFlow()
    init {
        // 3. Carga los dependientes automáticamente al iniciar el ViewModel
        cargarDependientes()
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