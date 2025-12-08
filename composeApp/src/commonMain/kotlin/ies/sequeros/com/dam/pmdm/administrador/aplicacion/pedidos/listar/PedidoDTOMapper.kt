package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido

fun Pedido.toDTO(path:String="") = PedidoDTO(
    id = id,
    clienteName = clienteName,
    estado = estado,
    total = total,
    fecha = fecha.toString(),
    dependienteId = dependienteId,
    lineas = lineas
)
fun PedidoDTO.toPedido()= Pedido(
    id = id,
    clienteName = clienteName,
    estado = estado,
    total = total,
    fecha = fecha.toLong(),
    dependienteId = dependienteId
)