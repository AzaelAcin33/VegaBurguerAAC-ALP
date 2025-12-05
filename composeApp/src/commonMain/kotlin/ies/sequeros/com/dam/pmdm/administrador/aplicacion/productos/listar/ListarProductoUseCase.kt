package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar


import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ListarProductoUseCase(
    private val repositorio: IProductoRepositorio,
    private val categoria: ICategoriaRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(): List<ProductoDTO> {
        val items = repositorio.getAll().map {
            it.toDTO(
                if (it.imagePath.isEmpty()) "" else
                    almacenDatos.getAppDataDir() + "/productos/",
                    categoria.getById(it.categoriaId)?.name ?: "")
        }
        return items
    }
}
