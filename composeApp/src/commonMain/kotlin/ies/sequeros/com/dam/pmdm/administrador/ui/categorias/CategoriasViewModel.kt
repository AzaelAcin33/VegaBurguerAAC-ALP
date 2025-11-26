package ies.sequeros.com.dam.pmdm.administrador.ui.categorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.BorrarCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.activar.ActivarCategoriaCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.activar.ActivarCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.actualizar.ActualizarCategoriaCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.actualizar.ActualizarCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.crear.CrearCategoriaCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.crear.CrearCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.ListarCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.ui.categorias.form.CategoriaFormState
import ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.form.DependienteFormState
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.mutableListOf


class CategoriasViewModel (
    private val categoriaRepository: ICategoriaRepositorio,
    val almacenDatos: AlmacenDatos
): ViewModel(){

    private val borrarCategoriaUseCase: BorrarCategoriaUseCase
    private val crearCategoriaUseCase: CrearCategoriaUseCase
    private val listarCategoriasUseCase: ListarCategoriaUseCase

    private val actualizarCategoriaUseCase: ActualizarCategoriaUseCase
    private val activarCategoriaUseCase: ActivarCategoriaUseCase

    private val _items = MutableStateFlow<MutableList<CategoriaDTO>>(mutableListOf())
    val items: StateFlow<List<CategoriaDTO>> = _items.asStateFlow()
    private val _selected = MutableStateFlow<CategoriaDTO?>(null)
    val selected = _selected.asStateFlow()

    init {
        actualizarCategoriaUseCase = ActualizarCategoriaUseCase(categoriaRepository,almacenDatos)
        borrarCategoriaUseCase = BorrarCategoriaUseCase(categoriaRepository,almacenDatos)
        crearCategoriaUseCase = CrearCategoriaUseCase(categoriaRepository,almacenDatos)
        listarCategoriasUseCase = ListarCategoriaUseCase(categoriaRepository,almacenDatos)
        activarCategoriaUseCase = ActivarCategoriaUseCase(categoriaRepository,almacenDatos)
        viewModelScope.launch {
            var items = listarCategoriasUseCase.invoke()
            _items.value.clear()
            _items.value.addAll(items)
        }
    }

    fun setSelectedCategoria(item: CategoriaDTO?) {
        _selected.value = item
    }

    fun switchEnableCategoria(item: CategoriaDTO){
        val command= ActivarCategoriaCommand(
            item.id,
            item.enabled,
        )
        viewModelScope.launch {
            val item=activarCategoriaUseCase.invoke(command)
            _items.value = _items.value.map{
                if (item.id == it.id)
                    item
                else
                    it
            } as MutableList<CategoriaDTO>
        }
    }

    fun delete(item: CategoriaDTO) {
        viewModelScope.launch {
            borrarCategoriaUseCase.invoke(item.id)
            _items.update { current ->
                current.filterNot { it.id == item.id }.toMutableList()
            }
        }

    }

    fun add(formState: CategoriaFormState) {
        val command = CrearCategoriaCommand(
            formState.nombre,
            formState.description,
            formState.imagePath,
            formState.enabled
        )
        viewModelScope.launch {
            try {
                val user = crearCategoriaUseCase.invoke(command)
                _items.value = (_items.value + user) as MutableList<CategoriaDTO>
            }catch (e:Exception){
                throw  e
            }

        }
    }

    fun update(formState: CategoriaFormState) {
        val command = ActualizarCategoriaCommand(
            selected.value!!.id!!,
            formState.nombre,
            formState.description,
            formState.imagePath,
            formState.enabled
        )
        viewModelScope.launch {
            val item = actualizarCategoriaUseCase.invoke(command)
            _items.update { current ->
                current.map { if (it.id == item.id) item else it } as MutableList<CategoriaDTO>
            }
        }
    }

    fun save(item: CategoriaFormState) {
        if (_selected.value == null)
            this.add(item)
        else
            this.update(item)
    }


}