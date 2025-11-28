package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear

data class CrearPedidoCommand (
    val id:String,
    val clienteName:String,
    val estado:String,
    val fecha: String
)