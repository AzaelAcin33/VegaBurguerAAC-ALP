package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear

data class CrearPedidoCommand (
    val clienteName: String,
    val estado: String,
    val total: Double,
    val fecha: String,
    val dependienteId: String?
)