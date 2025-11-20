package ies.sequeros.com.dam.pmdm.administrador.modelo

import jdk.jfr.Description
import kotlinx.serialization.Serializable

@Serializable
data class Categoria (
    val id:String,
    val name:String,
    val imagePath:String,
    val description: String,
    val enabled: Boolean
)