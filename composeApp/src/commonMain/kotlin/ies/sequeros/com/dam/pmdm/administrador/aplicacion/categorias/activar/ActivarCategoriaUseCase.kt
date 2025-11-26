package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.activar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ActivarCategoriaUseCase ( private val repositorio: ICategoriaRepositorio, private val almacenDatos: AlmacenDatos){

    suspend fun invoke(command: ActivarCategoriaCommand ): CategoriaDTO {
        val item: Categoria?=repositorio.getById(command.id)
        if (item==null) {
            throw IllegalArgumentException("El usuario no esta registrado.")
        }
        var newUser= item.copy(
            enabled = command.enabled,
        )
        repositorio.update(newUser)
        //se devuelve con el path correcto
        return newUser.toDTO(almacenDatos.getAppDataDir()+"/categorias/")
    }
}