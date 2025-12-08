package ies.sequeros.com.dam.pmdm.administrador.infraestructura.ficheros
import androidx.lifecycle.ViewModel
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import kotlinx.serialization.json.Json
import java.io.File

class FilePedidoRepository (
    private val almacenDatos: AlmacenDatos,
    private val fileName: String = "pedidos.json"
) : IPedidoRepositorio{

    private val subdirectory="/data/"

    private suspend fun getDirectoryPath(): String {
        val dir = almacenDatos.getAppDataDir()
        val directory = File(dir, subdirectory)

        return directory.absolutePath
    }

    private suspend fun save(items: MutableList<Pedido>) {
        if(!File(this.getDirectoryPath()).exists())
            File(this.getDirectoryPath()).mkdirs()
        this.almacenDatos.writeFile(this.getDirectoryPath()+"/"+this.fileName, Json.encodeToString(items))
    }

    override suspend fun add(item: Pedido) {
        val items = this.getAll().toMutableList()

        if (items.firstOrNull { it.clienteName == item.clienteName } == null) {
            items.add(item)
        } else {
            throw IllegalArgumentException("ALTA:El usuario con id:" + item.id + " ya existe")
        }
        this.save(items)
    }

    override suspend fun remove(item: Pedido): Boolean {
        return this.remove(item.id!!)
    }

    override suspend fun remove(id: String): Boolean {
        val items = this.getAll().toMutableList()
        var item = items.firstOrNull { it.id == id }
        if (item != null) {
            items.remove((item))
            this.save(items)
            return true
        } else {
            throw IllegalArgumentException(
                "BORRADO:" +
                        " El usuario con id:" + id + " NO  existe"
            )
        }
        return true
    }

    override suspend fun update(item: Pedido): Boolean {
        val items = this.getAll().toMutableList()
        val newItems= items.map { if (it.id == item.id) item else it }.toMutableList()
        this.save(newItems)
        return true
    }

    override suspend fun getAll(): List<Pedido> {
        val path = getDirectoryPath()+"/"+this.fileName
        val items= mutableListOf<Pedido>()
        var json=""
        if(File(path).exists()) {
            json = almacenDatos.readFile(path)
            if (!json.isEmpty())
                items.addAll(Json.decodeFromString<List<Pedido>>(json))
        }
        return items.toList()
    }

    override suspend fun findByName(name: String): Pedido? {
        val elements=this.getAll()
        for(element in elements){
            if(element.clienteName==name)
                return element
        }
        return null; //this.items.values.firstOrNull { it.name.equals(name) };
    }

    override suspend fun getById(id: String): Pedido? {
        val elements=this.getAll()
        for(element in elements){
            if(element.id==id)
                return element
        }
        return null;
    }

    override suspend fun listarProductos(): List<Producto> {
        val productoRepository = FileProductoRepository(almacenDatos)
        return productoRepository.getAll()
    }

}