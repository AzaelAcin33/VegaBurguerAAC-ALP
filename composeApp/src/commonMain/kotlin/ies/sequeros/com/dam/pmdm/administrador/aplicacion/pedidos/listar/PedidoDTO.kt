package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido

data class PedidoDTO(
    val id: String,
    val clienteName: String,
    val estado: String,
    val total: Double,
    val fecha: String,
    val dependienteId: String?,
    val lineas: List<LineaPedido>
)