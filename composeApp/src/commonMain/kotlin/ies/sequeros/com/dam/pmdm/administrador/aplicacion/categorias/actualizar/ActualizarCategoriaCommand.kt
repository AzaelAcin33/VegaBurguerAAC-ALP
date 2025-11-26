package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.actualizar

data class ActualizarCategoriaCommand (

    val id: String,
    val name: String,
    val description: String,
    val imagePath: String,
    val enabled: Boolean,

)