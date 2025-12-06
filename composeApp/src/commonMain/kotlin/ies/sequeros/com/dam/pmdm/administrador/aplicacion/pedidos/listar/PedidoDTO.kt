package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar

data class PedidoDTO(
    val id: String,
    val clienteName: String,
    val estado: String,
    val fecha: String,
    val dependienteId: String?
)