package ies.sequeros.com.dam.pmdm.administrador.infraestructura.ficheros

import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class FilePedidoRepository(
    private val almacenDatos: AlmacenDatos,
    private val fileName: String = "pedidos.json"
): IPedidoRepositorio {
    override suspend fun add(item: Pedido) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(item: Pedido): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun remove(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun update(item: Pedido): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<Pedido> {
        TODO("Not yet implemented")
    }

    override suspend fun findByName(name: String): Pedido? {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: String): Pedido? {
        TODO("Not yet implemented")
    }

    override fun listarProductos(): List<Producto> {
        TODO("Not yet implemented")
    }
}