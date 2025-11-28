package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.generateUUID

class CrearPedidoUseCase(private val repositorio: IPedidoRepositorio,private val almacenDatos: AlmacenDatos) {
    suspend  fun invoke(createUserCommand: CrearPedidoCommand): PedidoDTO {
        //this.validateUser(user)
        if (repositorio.findByName(createUserCommand.clienteName)!=null) {
            throw IllegalArgumentException("El nombre ya está registrado.")
        }
        val id=generateUUID()

        val item = Pedido(
            id = id,
            clienteName = createUserCommand.clienteName,
            estado = createUserCommand.estado,
            fecha = createUserCommand.fecha
        )
        val element=repositorio.findByName(item.clienteName)
        if(element!=null)
            throw IllegalArgumentException("El nombre ya está registrado.")
        repositorio.add(item)
        return item.toDTO( almacenDatos.getAppDataDir()+"/pedidos/");
    }
}