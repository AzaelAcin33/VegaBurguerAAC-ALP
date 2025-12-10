package ies.sequeros.com.dam.pmdm.cliente.application.usecases

import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria

class GetCategoriasUseCase(private val repositorio: ICategoriaRepositorio) {
    //Pide al repositorio lista de categorias
    suspend fun invoke(): List<Categoria> {
        return repositorio.getAll()
    }
}