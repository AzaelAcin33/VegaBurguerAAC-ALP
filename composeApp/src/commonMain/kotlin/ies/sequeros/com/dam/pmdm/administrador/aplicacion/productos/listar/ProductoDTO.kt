package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar

data class ProductoDTO (
    val id:String,
    val name:String,
    val imagePath:String,
    val price:String,
    val description:String,
    val enabled:Boolean,
    val categoriaId: String
)