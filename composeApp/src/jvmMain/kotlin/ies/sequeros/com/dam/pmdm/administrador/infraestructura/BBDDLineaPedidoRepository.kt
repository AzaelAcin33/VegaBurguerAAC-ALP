package ies.sequeros.com.dam.pmdm.administrador.infraestructura

import ies.sequeros.com.dam.pmdm.administrador.infraestructura.lineaPedido.BBDDRepositorioLineaPedidoJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido
import ies.sequeros.com.dam.pmdm.administrador.modelo.ILinePedidoRepositorio

class BBDDLineaPedidoRepository(
    private val bbddRepositorioDepedientesJava: BBDDRepositorioLineaPedidoJava
) : ILinePedidoRepositorio {
    override suspend fun add(item: LineaPedido) {
        bbddRepositorioDepedientesJava.add(item)
    }

    override suspend fun remove(item: LineaPedido): Boolean {
        bbddRepositorioDepedientesJava.remove(item)
        return true
    }
    override suspend fun remove(id: String): Boolean {

        bbddRepositorioDepedientesJava.remove(id)
        return true

    }

    override suspend fun update(item: LineaPedido): Boolean {

        bbddRepositorioDepedientesJava.update(item)
        return true
    }

    override suspend fun getAll(): List<LineaPedido> {

        return bbddRepositorioDepedientesJava.all
    }

    override suspend fun findByName(name: String): LineaPedido? {

        return bbddRepositorioDepedientesJava.findByName( name)
    }
    override suspend fun getById(id: String): LineaPedido? {
        return bbddRepositorioDepedientesJava.getById(id)
    }
}