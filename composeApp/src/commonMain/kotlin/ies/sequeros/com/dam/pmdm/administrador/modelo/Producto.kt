package ies.sequeros.com.dam.pmdm.administrador.modelo

import jdk.jfr.Enabled
import kotlinx.serialization.Serializable

@Serializable
data class Producto (
    val id:String,
    val name:String,
    val imagePath:String,
    val price:String,
    val description:String,
    val enabled:Boolean
)