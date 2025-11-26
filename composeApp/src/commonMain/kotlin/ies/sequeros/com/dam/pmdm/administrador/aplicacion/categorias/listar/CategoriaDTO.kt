package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar

data class CategoriaDTO (
    val id:String,
    val name:String,
    val description:String,
    val imagePath:String,
    val enabled: Boolean
)