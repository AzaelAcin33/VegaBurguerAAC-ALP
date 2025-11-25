package ies.sequeros.com.dam.pmdm.administrador.infraestructura

import ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias.BBDDRepositorioCategoriasJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio


class BBDDCategoriaRepository(
    private val bbddRepositorioDepedientesJava: BBDDRepositorioCategoriasJava
) : ICategoriaRepositorio {
    override suspend fun add(item: Categoria) {
        bbddRepositorioDepedientesJava.add(item)
    }

    override suspend fun remove(item: Categoria): Boolean {
        bbddRepositorioDepedientesJava.remove(item)
        return true
    }
    override suspend fun remove(id: String): Boolean {

        bbddRepositorioDepedientesJava.remove(id)
        return true

    }

    override suspend fun update(item: Categoria): Boolean {

        bbddRepositorioDepedientesJava.update(item)
        return true
    }

    override suspend fun getAll(): List<Categoria> {

        return bbddRepositorioDepedientesJava.all
    }

    override suspend fun findByName(name: String): Categoria? {

        return bbddRepositorioDepedientesJava.findByName( name)
    }
    override suspend fun getById(id: String): Categoria? {
        return bbddRepositorioDepedientesJava.getById(id)
    }
}