package ies.sequeros.com.dam.pmdm.administrador.infraestructura.ficheros

import ies.sequeros.com.dam.pmdm.administrador.modelo.ILinePedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class FileLineaPedidoRepository(
    private val almacenDatos: AlmacenDatos,
    private val fileName: String = "lineaPedidos.json"
): ILinePedidoRepositorio {
    override suspend fun add(item: LineaPedido) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(item: LineaPedido): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun remove(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun update(item: LineaPedido): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<LineaPedido> {
        TODO("Not yet implemented")
    }

    override suspend fun findByName(name: String): LineaPedido? {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: String): LineaPedido? {
        TODO("Not yet implemented")
    }
}