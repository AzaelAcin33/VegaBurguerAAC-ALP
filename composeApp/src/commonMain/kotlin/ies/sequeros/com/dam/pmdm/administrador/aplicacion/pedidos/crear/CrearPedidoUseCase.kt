package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.generateUUID

class CrearPedidoUseCase(private val repositorio: IPedidoRepositorio,private val almacenDatos: AlmacenDatos) {
    suspend  fun invoke(createPedidoCommand: CrearPedidoCommand): PedidoDTO {
        //this.validatePedido(user)
        if (repositorio.findByName(createPedidoCommand.clienteName)!=null) {
            throw IllegalArgumentException("El nombre ya está registrado.")
        }
        val id=generateUUID()

        val item = Pedido(
            id = id,
            clienteName = createPedidoCommand.clienteName,
            estado = createPedidoCommand.estado,
            fecha = createPedidoCommand.fecha,
            dependienteId = createPedidoCommand.dependienteId
        )
        val element=repositorio.findByName(item.clienteName)
        if(element!=null)
            throw IllegalArgumentException("El nombre ya está registrado.")
        repositorio.add(item)
        return item.toDTO( almacenDatos.getAppDataDir()+"/pedidos/");
    }
}