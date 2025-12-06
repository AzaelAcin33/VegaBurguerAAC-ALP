package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto // Asegúrate de tener este modelo

data class PedidoFormState(
    // ... tus campos anteriores (id, clienteName, etc.) ...
    val id: String = "",
    val clienteName: String = "",
    val estado: String = "Pendiente",
    val fecha: String = "",
    val dependienteId: String? = null,

    // Lista de líneas del pedido actual
    val lineas: List<LineaPedidoFormState> = emptyList(),

    // --- NUEVO: Catálogo de productos disponibles desde BBDD ---
    val productosDisponibles: List<Producto> = emptyList(),

    // Campos temporales
    val tempProductoId: String = "",
    val tempProductoNombre: String = "",
    val tempPrecioUnitario: String = "",
    val tempCantidad: String = "1",

    // Validaciones y totales
    val totalPedido: Double = 0.0,
    val clienteNameError: String? = null,
    val lineasError: String? = null,
    val submitted: Boolean = false
)

// LineaPedidoFormState se queda igual...
data class LineaPedidoFormState(
    val id: String = "",
    val cantidad: Int = 1,
    val precioUnitario: Double = 0.0,
    val entregado: Boolean = false,
    val pedidoId: String = "",
    val productoId: String = "",
    val productName: String = "",
    val totalLinea: Double = 0.0
)