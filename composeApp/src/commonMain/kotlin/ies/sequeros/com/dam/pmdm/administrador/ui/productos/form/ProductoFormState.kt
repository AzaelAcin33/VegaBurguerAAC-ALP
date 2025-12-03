package ies.sequeros.com.dam.pmdm.administrador.ui.productos.form

data class ProductoFormState (
    val name: String="",
    val description:String="",
    val imagePath:String="default",
    val price:String="",
    val enabled: Boolean = false,
    val categoriaId: String="",
    // errores (null = sin error)
    val nameError: String?=null,
    //val descriptionError:String?=null,
    val imagePathError:String?=null,
    val priceError:String?=null,
    val categoriaIdError:String?=null,

    val submitted: Boolean = false
)