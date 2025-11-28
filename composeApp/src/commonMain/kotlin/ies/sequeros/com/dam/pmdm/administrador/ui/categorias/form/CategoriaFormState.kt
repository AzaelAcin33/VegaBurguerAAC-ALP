package ies.sequeros.com.dam.pmdm.administrador.ui.categorias.form

data class CategoriaFormState (
    val nombre: String="",
    val description:String="",
    val imagePath:String="default",
    val enabled: Boolean = false,
    // errores (null = sin error)
    val nombreError: String?=null,
    val descriptionError:String?=null,
    val imagePathError:String?=null
)