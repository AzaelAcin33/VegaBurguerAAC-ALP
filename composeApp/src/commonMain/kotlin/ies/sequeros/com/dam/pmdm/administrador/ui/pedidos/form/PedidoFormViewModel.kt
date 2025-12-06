package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.modelo.Dependiente
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PedidoFormViewModel(
    private val productoRepo: IProductoRepositorio,
    private val dependienteRepo: IDependienteRepositorio,
    private val currentDependienteId: String? = null,
    onSuccess: (PedidoFormState) -> Unit
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoFormState(
        fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
        dependienteId = currentDependienteId
    ))
    val uiState: StateFlow<PedidoFormState> = _uiState.asStateFlow()

    // Validamos que haya cliente, líneas y dependiente seleccionado
    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        state.clienteName.isNotBlank() &&
                state.lineas.isNotEmpty() &&
                !state.dependienteId.isNullOrBlank() &&
                state.clienteNameError == null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        cargarDatosIniciales()
    }

    private fun cargarDatosIniciales() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Cargar listados desde BBDD
                val productos = productoRepo.getAll()
                val dependientes = dependienteRepo.getAll()

                // Buscar nombre del dependiente actual si existe para mostrarlo
                val depActualName = dependientes.find { it.id == currentDependienteId }?.name ?: ""

                _uiState.update {
                    it.copy(
                        productosDisponibles = productos,
                        dependientesDisponibles = dependientes,
                        dependienteSeleccionadoNombre = depActualName
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // --- Eventos UI ---

    fun onClienteNameChange(nombre: String) {
        val error = if (nombre.isBlank()) "El nombre es obligatorio" else null
        _uiState.update { it.copy(clienteName = nombre, clienteNameError = error) }
    }

    fun onDependienteSelected(dependiente: Dependiente) {
        _uiState.update {
            it.copy(
                dependienteId = dependiente.id,
                dependienteSeleccionadoNombre = dependiente.name
            )
        }
    }

    fun onProductoRealSelect(producto: Producto) {
        _uiState.update {
            it.copy(
                tempProductoId = producto.id,
                tempProductoNombre = producto.name,
                tempPrecioUnitario = producto.price.toString()
            )
        }
    }

    fun onTempCantidadChange(cant: String) {
        if (cant.all { it.isDigit() }) {
            _uiState.update { it.copy(tempCantidad = cant) }
        }
    }

    fun agregarLinea() {
        val state = _uiState.value
        val cantidad = state.tempCantidad.toIntOrNull() ?: 1
        val precio = state.tempPrecioUnitario.toDoubleOrNull() ?: 0.0

        if (state.tempProductoId.isBlank() || precio <= 0) return

        val nuevaLinea = LineaPedidoFormState(
            id = "LP-${System.currentTimeMillis()}", // ID Temporal
            cantidad = cantidad,
            precioUnitario = precio,
            entregado = false,
            pedidoId = state.id,
            productoId = state.tempProductoId,
            productName = state.tempProductoNombre,
            totalLinea = cantidad * precio
        )

        val nuevasLineas = state.lineas + nuevaLinea
        recalcularTotales(nuevasLineas)
        limpiarTemp()
    }

    fun eliminarLinea(index: Int) {
        val nuevasLineas = _uiState.value.lineas.toMutableList().apply { removeAt(index) }
        recalcularTotales(nuevasLineas)
    }

    fun toggleEntregado(index: Int) {
        val lineas = _uiState.value.lineas.toMutableList()
        val linea = lineas[index]
        lineas[index] = linea.copy(entregado = !linea.entregado)
        _uiState.update { it.copy(lineas = lineas) }
    }

    private fun recalcularTotales(lineas: List<LineaPedidoFormState>) {
        val total = lineas.sumOf { it.totalLinea }
        _uiState.update {
            it.copy(
                lineas = lineas,
                totalPedido = total,
                lineasError = if (lineas.isEmpty()) "Añade productos al pedido" else null
            )
        }
    }

    private fun limpiarTemp() {
        _uiState.update {
            it.copy(tempProductoId = "", tempProductoNombre = "", tempPrecioUnitario = "", tempCantidad = "1")
        }
    }

    fun submit(onSuccess: (PedidoFormState) -> Unit) {
        viewModelScope.launch {
            if (isFormValid.value) {
                onSuccess(_uiState.value)
            }
        }
    }
}