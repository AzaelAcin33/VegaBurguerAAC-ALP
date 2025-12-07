package ies.sequeros.com.dam.pmdm.cliente.domain.dto

data class LineaPedidoTPVDTO(
    val producto: ProductoTPVDTO,
    var cantidad: Int
) {
    val total: Double
        get() = producto.precio.toDouble() * cantidad
}