package ies.sequeros.com.dam.pmdm.administrador.modelo
import kotlinx.serialization.Serializable


@Serializable
data class LineaPedido (
    val id:String,
    val cantidad: Int,
    val precioUnitario:String,
    val entregado:Boolean,
    //val pedidoId: String,
    val productoId: String
)