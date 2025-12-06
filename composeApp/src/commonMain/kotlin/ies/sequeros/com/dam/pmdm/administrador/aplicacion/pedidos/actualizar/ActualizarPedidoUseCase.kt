package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.actualizar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.toDTO
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio


class ActualizarPedidoUseCase(
    private val repositorio: IPedidoRepositorio,
    private val almacenDatos: AlmacenDatos
) {

    suspend fun invoke(command: ActualizarPedidoCommand): PedidoDTO {

        val item: Pedido? = repositorio.getById(command.id)

        if (item == null) {
            throw IllegalArgumentException("El pedido no está registrado.")
        }

        val nuevoItem = item.copy(
            clienteName = command.clienteName,
            estado = command.estado,
            fecha = command.fecha
        )

        repositorio.update(nuevoItem)

        // Se devuelve con la ruta "igual" que en el patrón Dependiente
        return nuevoItem.toDTO(almacenDatos.getAppDataDir() + "/pedidos/")
    }
}
