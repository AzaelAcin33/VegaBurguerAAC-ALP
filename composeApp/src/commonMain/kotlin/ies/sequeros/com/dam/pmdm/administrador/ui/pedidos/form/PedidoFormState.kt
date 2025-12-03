package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

data class PedidoFormState (
    val clienteName: String="",
    val estado:String="default",
    val fecha:String="default",
    val dependienteId: String="",
    // errores (null = sin error)
    val clienteNameError: String?=null,
    val estadoError:String?=null,
    val fechaError:String?=null,
    val dependienteIdError:String?=null,

    val submitted: Boolean = false
)