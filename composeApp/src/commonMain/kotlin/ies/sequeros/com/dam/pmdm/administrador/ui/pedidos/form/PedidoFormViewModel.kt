package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.ILinePedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PedidoFormViewModel(
    private val pedidoId: String,
    private val pedidoRepo: IPedidoRepositorio,
    private val lineaPedidoRepo: ILinePedidoRepositorio,
    private val productoRepo: IProductoRepositorio,
    private val dependienteRepo: IDependienteRepositorio
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoFormState(isLoading = true))
    val uiState: StateFlow<PedidoFormState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Buscar el pedido
                val pedido = pedidoRepo.getAll().find { it.id == pedidoId }

                if (pedido == null) {
                    _uiState.update { it.copy(isLoading = false, error = "Pedido no encontrado") }
                    return@launch
                }

                // 2. Buscar el dependiente
                val dependiente = dependienteRepo.getAll().find { it.id == pedido.dependienteId }
                val nombreDep = dependiente?.name ?: "Desconocido"

                // 3. Buscar las líneas de este pedido
                val todasLineas = lineaPedidoRepo.getAll()
                val lineasPedido = todasLineas.filter { it.pedidoId == pedido.id }

                // 4. Cargar productos para obtener nombres
                val productos = productoRepo.getAll()

                // 5. Mapear las líneas para la vista
                val lineasVista = lineasPedido.map { linea ->
                    val prod = productos.find { it.id == linea.productoId }
                    LineaPedidoLectura(
                        nombreProducto = prod?.name ?: "Producto Eliminado",
                        cantidad = linea.cantidad,
                        precioUnitario = linea.precioUnitario.toDouble(),
                        totalLinea = linea.cantidad * linea.precioUnitario.toDouble()
                    )
                }

                // 6. Actualizar UI
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        id = pedido.id,
                        clienteName = pedido.clienteName,
                        fecha = pedido.fecha.toString(),
                        estado = pedido.estado,
                        nombreDependiente = nombreDep,
                        total = pedido.total,
                        lineas = lineasVista
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}