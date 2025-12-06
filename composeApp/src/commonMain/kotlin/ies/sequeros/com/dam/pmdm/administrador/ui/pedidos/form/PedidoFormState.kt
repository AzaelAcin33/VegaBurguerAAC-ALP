package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import ies.sequeros.com.dam.pmdm.administrador.modelo.Dependiente
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto

data class PedidoFormState(
    // Datos Cabecera
    val id: String = "",
    val clienteName: String = "",
    val estado: String = "Pendiente",
    val fecha: String = "",

    // Dependiente seleccionado
    val dependienteId: String? = null,
    val dependienteSeleccionadoNombre: String = "", // Para mostrar en el input

    // Listas de datos disponibles (ComboBox / Catálogo)
    val dependientesDisponibles: List<Dependiente> = emptyList(),
    val productosDisponibles: List<Producto> = emptyList(),

    // Líneas del pedido
    val lineas: List<LineaPedidoFormState> = emptyList(),

    // Datos temporales para añadir linea
    val tempProductoId: String = "",
    val tempProductoNombre: String = "",
    val tempPrecioUnitario: String = "",
    val tempCantidad: String = "1",

    // Totales y Validaciones
    val totalPedido: Double = 0.0,
    val clienteNameError: String? = null,
    val lineasError: String? = null,
    val submitted: Boolean = false
)

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