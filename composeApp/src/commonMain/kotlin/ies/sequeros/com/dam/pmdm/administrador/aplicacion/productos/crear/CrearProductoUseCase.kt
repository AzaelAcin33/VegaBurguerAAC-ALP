package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.crear

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.generateUUID

class CrearProductoUseCase(
    private val repositorio: IProductoRepositorio,
    private val categoria: ICategoriaRepositorio,
    private val almacenDatos: AlmacenDatos) {
    suspend  fun invoke(createUserCommand: CrearProductoCommand): ProductoDTO {
        //this.validateUser(user)
        if (repositorio.findByName(createUserCommand.name)!=null) {
            throw IllegalArgumentException("El producto ya está registrado.")
        }

        val element=repositorio.findByName(createUserCommand.name)
        if(element!=null)
            throw IllegalArgumentException("El nombre ya está registrado.")
        val id=generateUUID()
        val imageName=almacenDatos.copy(createUserCommand.imagePath,id,"/productos/")
        val item = Producto(
            id = id,
            name = createUserCommand.name,
            imagePath = imageName,
            price = createUserCommand.price,
            description = createUserCommand.description,
            enabled = createUserCommand.enabled,
            categoriaId = createUserCommand.categoriaId
        )
        repositorio.add(item)
        return item.toDTO(almacenDatos.getAppDataDir()+"/productos/",
            categoria.getById(item.categoriaId)?.name ?: ""
            );
    }
}