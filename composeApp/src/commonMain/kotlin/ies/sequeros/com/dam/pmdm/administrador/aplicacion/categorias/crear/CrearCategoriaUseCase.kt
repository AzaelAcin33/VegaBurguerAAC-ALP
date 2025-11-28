package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.crear

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.generateUUID

class CrearCategoriaUseCase ( private val repositorio: ICategoriaRepositorio, private val almacenDatos: AlmacenDatos){

    suspend  fun invoke(createCategoryCommand: CrearCategoriaCommand): CategoriaDTO {
        //this.validateCategory(user)
        if (repositorio.findByName(createCategoryCommand.name)!=null) {
            throw IllegalArgumentException("El nombre ya está registrado.")
        }
        val id=generateUUID()
        //se almacena el fichero
        val imageName=almacenDatos.copy(createCategoryCommand.imagePath,id,"/categorias/")
        val item = Categoria(
            id = id,
            name = createCategoryCommand.name,
            description = createCategoryCommand.description,
            imagePath = imageName,
            enabled = createCategoryCommand.enabled
        )
        val element=repositorio.findByName(item.name)
        if(element!=null)
            throw IllegalArgumentException("El nombre ya está registrado.")
        repositorio.add(item)
        return item.toDTO( almacenDatos.getAppDataDir()+"/categorias/");
    }
}
