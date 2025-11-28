package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class BorrarPedidoUseCase(private val repositorio: IPedidoRepositorio,private val almacenDatos: AlmacenDatos) {
    suspend  fun invoke(id: String) {
        val tempo = repositorio.getById(id)
        val elementos = repositorio.getAll();
        //this.validateUser(user)
        if (tempo == null) {
            throw IllegalArgumentException("El id no está registrado.")
        }
        //se borra del repositorio
        val tempoDto = tempo.toDTO(almacenDatos.getAppDataDir() + "/pedidos/")

        repositorio.remove(id)
    }
}