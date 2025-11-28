package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.actualizar

data class ActualizarPedidoCommand(
    val id:String,
    val clienteName:String,
    val estado:String,
    val fecha: String
)