package ies.sequeros.com.dam.pmdm.administrador.infraestructura

import ies.sequeros.com.dam.pmdm.administrador.infraestructura.productos.BBDDRepositorioProductosJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio

class BBDDProductoRepository(
    private val bbddRepositorioDepedientesJava: BBDDRepositorioProductosJava
) : IProductoRepositorio {
    override suspend fun add(item: Producto) {
        bbddRepositorioDepedientesJava.add(item)
    }

    override suspend fun remove(item: Producto): Boolean {
        bbddRepositorioDepedientesJava.remove(item)
        return true
    }
    override suspend fun remove(id: String): Boolean {

        bbddRepositorioDepedientesJava.remove(id)
        return true

    }

    override suspend fun update(item: Producto): Boolean {

        bbddRepositorioDepedientesJava.update(item)
        return true
    }

    override suspend fun getAll(): List<Producto> {

        return bbddRepositorioDepedientesJava.all
    }

    override suspend fun findByName(name: String): Producto? {

        return bbddRepositorioDepedientesJava.findByName( name)
    }
    override suspend fun getById(id: String): Producto? {
        return bbddRepositorioDepedientesJava.getById(id)
    }
}