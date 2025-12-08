package ies.sequeros.com.dam.pmdm.administrador.infraestructura.ficheros

import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class FileProductoRepository(
    private val almacenDatos: AlmacenDatos,
    private val fileName: String = "productos.json"
): IProductoRepositorio {

    private val subdirectory="/data/"

    override suspend fun add(item: Producto) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(item: Producto): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun remove(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun update(item: Producto): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<Producto> {
        TODO("Not yet implemented")
    }

    override suspend fun findByName(name: String): Producto? {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: String): Producto? {
        TODO("Not yet implemented")
    }
}