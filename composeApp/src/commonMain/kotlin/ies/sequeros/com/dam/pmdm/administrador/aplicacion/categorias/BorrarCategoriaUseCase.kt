package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class BorrarCategoriaUseCase( private val repositorio: ICategoriaRepositorio, private val almacenDatos: AlmacenDatos) {

    suspend  fun invoke(id: String) {
        val tempo=repositorio.getById(id)
        val elementos=repositorio.getAll();
        //this.validateUser(user)
        //Comprobamos is existe id
        if (tempo==null) {
            throw IllegalArgumentException("El id no está registrado.")
        }
        //se borra del repositorio
        val tempoDto=tempo.toDTO(almacenDatos.getAppDataDir()+"/categorias/")

        repositorio.remove(id)
        //se borra la imagen una vez borrado del repositorio
        almacenDatos.remove(tempoDto.imagePath)
    }
}