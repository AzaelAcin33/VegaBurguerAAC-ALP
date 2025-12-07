package ies.sequeros.com.dam.pmdm.cliente.domain.dto

data class PedidoTPVDTO(
    val nombreCliente: String,
    val lineas: List<LineaPedidoTPVDTO>,
    val dependienteId: String?,
    val fecha: Long, // Timestamp
    val total: Double
)