package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.BorrarPedidoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.actualizar.ActualizarPedidoCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.actualizar.ActualizarPedidoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear.CrearPedidoCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear.CrearPedidoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.ListarPedidoUseCase
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form.PedidoFormState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PedidosViewModel(
    //private val administradorViewModel: MainAdministradorViewModel,
    val pedidoRepositorio: IPedidoRepositorio,
    val almacenDatos: AlmacenDatos
) : ViewModel() {
    //los casos de uso se crean dentro para la recomposición
    //se pueden injectar también, se tratará en próximos temas
    private val borrarPedidoUseCase: BorrarPedidoUseCase
    private val crearPedidoUseCase: CrearPedidoUseCase
    private val listarPedidosUseCase: ListarPedidoUseCase

    private val actualizarPedidoUseCase: ActualizarPedidoUseCase
    private val _items = MutableStateFlow<MutableList<PedidoDTO>>(mutableListOf())
    val items: StateFlow<List<PedidoDTO>> = _items.asStateFlow()
    private val _selected = MutableStateFlow<PedidoDTO?>(null)
    val selected = _selected.asStateFlow()

    init {
        actualizarPedidoUseCase = ActualizarPedidoUseCase(pedidoRepositorio,almacenDatos)
        borrarPedidoUseCase = BorrarPedidoUseCase(pedidoRepositorio,almacenDatos)
        crearPedidoUseCase = CrearPedidoUseCase(pedidoRepositorio,almacenDatos)
        listarPedidosUseCase = ListarPedidoUseCase(pedidoRepositorio,almacenDatos)
        viewModelScope.launch {
            var items = listarPedidosUseCase.invoke()
            _items.value.clear()
            _items.value.addAll(items)

        }
    }

    fun setSelectedPedido(item: PedidoDTO?) {
        _selected.value = item
    }

    fun delete(item: PedidoDTO) {
        viewModelScope.launch {
            borrarPedidoUseCase.invoke(item.id)
            _items.update { current ->
                current.filterNot { it.id == item.id }.toMutableList()
            }
        }

    }

    fun add(formState: PedidoFormState) {
        val command = CrearPedidoCommand(
            formState.clienteName,
            formState.estado,
            formState.total,
            formState.fecha,
            formState.dependienteId
        )
        viewModelScope.launch {
            try {
                val user = crearPedidoUseCase.invoke(command)
                _items.value = (_items.value + user) as MutableList<PedidoDTO>
            }catch (e:Exception){
                throw  e
            }

        }
    }

    fun update(formState: PedidoFormState) {
        val command = ActualizarPedidoCommand(
            selected.value!!.id!!,
            formState.clienteName,
            formState.estado,
            formState.fecha
        )
        viewModelScope.launch {
            val item = actualizarPedidoUseCase.invoke(command)
            _items.update { current ->
                current.map { if (it.id == item.id) item else it } as MutableList<PedidoDTO>
            }
        }


    }

    fun save(item: PedidoFormState) {
        if (_selected.value == null)
            this.add(item)
        else
            this.update(item)
    }



}