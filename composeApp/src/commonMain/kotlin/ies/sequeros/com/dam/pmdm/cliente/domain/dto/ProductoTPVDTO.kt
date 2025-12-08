package ies.sequeros.com.dam.pmdm.cliente.domain.dto

data class ProductoTPVDTO (
    //Info de producto
    val id: String,
    val nombre: String,
    val precio: String,
    val imagePath: String,
    val categoriaId: String,
    val enabled: Boolean
)