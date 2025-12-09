package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.activar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ActivarCategoriaUseCase ( private val repositorio: ICategoriaRepositorio, private val almacenDatos: AlmacenDatos){

    suspend fun invoke(command: ActivarCategoriaCommand ): CategoriaDTO {
        //Busca categoria mediante su Id
        val item: Categoria?=repositorio.getById(command.id)
        if (item==null) {
            throw IllegalArgumentException("El usuario no esta registrado.")
        }
        //Crea una copia modificada
        var newUser= item.copy(
            enabled = command.enabled,
        )
        //Guarfa los cambios en la base de datos
        repositorio.update(newUser)
        //Se devuelve con el path correcto
        return newUser.toDTO(almacenDatos.getAppDataDir()+"/categorias/")
    }
}