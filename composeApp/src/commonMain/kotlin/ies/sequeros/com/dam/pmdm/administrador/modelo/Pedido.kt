package ies.sequeros.com.dam.pmdm.administrador.modelo

import kotlinx.serialization.Serializable


@Serializable
data class Pedido (
    val id:String,
    val clienteName:String,
    val estado:String,
    val fecha: String
)