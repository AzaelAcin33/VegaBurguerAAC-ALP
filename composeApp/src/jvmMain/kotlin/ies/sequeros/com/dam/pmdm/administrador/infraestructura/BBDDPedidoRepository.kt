package ies.sequeros.com.dam.pmdm.administrador.infraestructura

import ies.sequeros.com.dam.pmdm.administrador.infraestructura.pedidos.BBDDRepositorioPedidosJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio

class BBDDPedidoRepository(
    private val bbddRepositorioDepedientesJava: BBDDRepositorioPedidosJava
) : IPedidoRepositorio {
    override suspend fun add(item: Pedido) {
        bbddRepositorioDepedientesJava.add(item)
    }

    override suspend fun remove(item: Pedido): Boolean {
        bbddRepositorioDepedientesJava.remove(item)
        return true
    }
    override suspend fun remove(id: String): Boolean {

        bbddRepositorioDepedientesJava.remove(id)
        return true

    }

    override suspend fun update(item: Pedido): Boolean {

        bbddRepositorioDepedientesJava.update(item)
        return true
    }

    override suspend fun getAll(): List<Pedido> {

        return bbddRepositorioDepedientesJava.all
    }

    override suspend fun findByName(name: String): Pedido? {

        return bbddRepositorioDepedientesJava.findByName( name)
    }
    override suspend fun getById(id: String): Pedido? {
        return bbddRepositorioDepedientesJava.getById(id)
    }
}