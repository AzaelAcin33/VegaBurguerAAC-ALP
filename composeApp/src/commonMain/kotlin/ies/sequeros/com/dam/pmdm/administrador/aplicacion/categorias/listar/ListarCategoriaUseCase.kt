package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ListarCategoriaUseCase( private val repositorio: ICategoriaRepositorio, private val almacenDatos: AlmacenDatos) {

    suspend fun invoke( ): List<CategoriaDTO> {
        //this.validateUser(user)
        //si tiene imagen
        //Obtenemos las categorias las convertimos en DTO y las devolvemos
        val items= repositorio.getAll().map { it.toDTO(if(it.imagePath.isEmpty()) "" else almacenDatos.getAppDataDir()+"/categorias/") }
        return items
    }
}
