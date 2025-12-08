package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

data class PedidoFormState(
    val isLoading: Boolean = false,
    val error: String? = null,

    // Datos del pedido para mostrar
    val id: String = "",
    val clienteName: String = "",
    val fecha: String = "",
    val estado: String = "",
    val nombreDependiente: String = "",
    val total: Double = 0.0,
    val dependienteId: String = "",


    // Lista de productos del pedido
    val lineas: List<LineaPedidoLectura> = emptyList()
)

data class LineaPedidoLectura(
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val totalLinea: Double
)