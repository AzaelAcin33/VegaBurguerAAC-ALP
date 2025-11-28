package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.actualizar

data class ActualizarProductoCommand (
    val id:String,
    val name:String,
    val imagePath:String,
    val price:String,
    val description:String,
    val enabled:Boolean
)