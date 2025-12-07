package ies.sequeros.com.dam.pmdm.cliente.application.usecases

import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido
import ies.sequeros.com.dam.pmdm.cliente.domain.dto.PedidoTPVDTO
import java.util.UUID

class SavePedidoUseCase(private val repositorio: IPedidoRepositorio) {
    suspend fun invoke(pedidoDTO: PedidoTPVDTO) {
        // Mapeo del DTO al Modelo de Dominio
        val nuevoPedido = Pedido(
            id = UUID.randomUUID().toString(),
            clienteName = pedidoDTO.nombreCliente,
            fecha = pedidoDTO.fecha,
            dependienteId = pedidoDTO.dependienteId ?: "",
            total = pedidoDTO.total,
            estado = "PAGADO", // O el estado que corresponda
            lineas = pedidoDTO.lineas.map {
                LineaPedido(
                    id = UUID.randomUUID().toString(),
                    productoId = it.producto.id,
                    cantidad = it.cantidad,
                    entregado = false,
                    //pedidoId = nuevoPedido.id,
                    precioUnitario = it.producto.precio.toString()
                )
            }
        )
        repositorio.add(nuevoPedido)
    }
}