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

        // Se pasa a DTO para mantener el mismo patrón que Dependiente
        val itemDTO: PedidoDTO = item.toDTO(almacenDatos.getAppDataDir() + "/pedidos/")

        // No hay gestión de imágenes, pero mantenemos la variable por consistencia
        var nuevaRuta: String? = null
        nuevaRuta = "" // No se copia ningún archivo, pero mantenemos la variable

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
