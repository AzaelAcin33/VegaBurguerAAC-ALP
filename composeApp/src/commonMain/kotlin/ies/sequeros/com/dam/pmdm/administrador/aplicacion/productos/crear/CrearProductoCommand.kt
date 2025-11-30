package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.crear

class CrearProductoCommand (
    val name:String,
    val imagePath:String,
    val price:String,
    val description:String,
    val enabled:Boolean,
    val categoriaId: String
)