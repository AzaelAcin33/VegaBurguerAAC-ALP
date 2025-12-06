package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PedidoFormViewModel(
    // Inyectamos el repositorio en lugar de crearlo aquí
    private val productoRepo: IProductoRepositorio,
    private val currentDependienteId: String? = null,
    onSuccess: (PedidoFormState) -> Unit
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoFormState(
        fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
        dependienteId = currentDependienteId ?: ""
    ))
    val uiState: StateFlow<PedidoFormState> = _uiState.asStateFlow()

    // Validador
    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        state.clienteName.isNotBlank() &&
                state.lineas.isNotEmpty() &&
                state.clienteNameError == null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        cargarProductosDeBBDD()
    }

    private fun cargarProductosDeBBDD() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Usamos la interfaz común
                val listaProductos = productoRepo.getAll()

                _uiState.update { currentState ->
                    currentState.copy(productosDisponibles = listaProductos)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Opcional: Manejar error en UI
            }
        }
    }

    // --- LOGICA DEL FORMULARIO ---

    fun onClienteNameChange(nombre: String) {
        val error = if (nombre.isBlank()) "El nombre es obligatorio" else null
        _uiState.update { it.copy(clienteName = nombre, clienteNameError = error) }
    }

    // Selección de producto desde el catálogo (BBDD)
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
        val cantidadInt = state.tempCantidad.toIntOrNull() ?: 1
        val precioDouble = state.tempPrecioUnitario.toDoubleOrNull() ?: 0.0

        if (state.tempProductoId.isBlank() || precioDouble <= 0) return

        val nuevaLinea = LineaPedidoFormState(
            id = "LP-${System.currentTimeMillis()}", // ID Temporal
            cantidad = cantidadInt,
            precioUnitario = precioDouble,
            entregado = false,
            pedidoId = state.id,
            productoId = state.tempProductoId,
            productName = state.tempProductoNombre,
            totalLinea = cantidadInt * precioDouble
        )

        val nuevasLineas = state.lineas + nuevaLinea
        recalcularTotales(nuevasLineas)
        limpiarTemp()
    }

    fun toggleEntregado(index: Int) {
        val lineas = _uiState.value.lineas.toMutableList()
        val linea = lineas[index]
        lineas[index] = linea.copy(entregado = !linea.entregado)
        _uiState.update { it.copy(lineas = lineas) }
    }

    fun eliminarLinea(index: Int) {
        val nuevasLineas = _uiState.value.lineas.toMutableList().apply { removeAt(index) }
        recalcularTotales(nuevasLineas)
    }

    private fun recalcularTotales(nuevasLineas: List<LineaPedidoFormState>) {
        val total = nuevasLineas.sumOf { it.totalLinea }
        _uiState.update {
            it.copy(
                lineas = nuevasLineas,
                totalPedido = total,
                lineasError = if (nuevasLineas.isEmpty()) "Añade productos al pedido" else null
            )
        }
    }

    private fun limpiarTemp() {
        _uiState.update {
            it.copy(
                tempProductoId = "",
                tempProductoNombre = "",
                tempPrecioUnitario = "",
                tempCantidad = "1"
            )
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