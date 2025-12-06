package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class BorrarPedidoUseCase(private val repositorio: IPedidoRepositorio,private val almacenDatos: AlmacenDatos) {
    suspend  fun invoke(id: String) {
        val item = repositorio.getById(id)
        //this.validateUser(user)
        if (item == null) {
            throw IllegalArgumentException("El id no está registrado.")
        }
        repositorio.remove(id)
    }
}