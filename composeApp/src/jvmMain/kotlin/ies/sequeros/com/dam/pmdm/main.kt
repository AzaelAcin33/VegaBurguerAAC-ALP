package ies.sequeros.com.dam.pmdm

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.ListarDependientesUseCase
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.BBDDCategoriaRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.BBDDDependienteRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.BBDDLineaPedidoRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.BBDDPedidoRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.BBDDProductoRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias.BBDDRepositorioCategoriasJava
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.dependientes.BBDDRepositorioDependientesJava
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.lineaPedido.BBDDRepositorioLineaPedidoJava
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.pedidos.BBDDRepositorioPedidosJava
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.productos.BBDDRepositorioProductosJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import java.io.FileInputStream
import java.util.logging.LogManager
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection

fun main() = application {

    //nueva configuracion de acceso

    val connection = DataBaseConnection()
    connection.setConfig_path("/app.properties")
    connection.open()
    val dependienteRepositorioJava=BBDDRepositorioDependientesJava(connection)
    val categoriaRepositorioJava= BBDDRepositorioCategoriasJava(connection)
    val productoRepositorioJava= BBDDRepositorioProductosJava(connection)
    val pedidoRepositorioJava= BBDDRepositorioPedidosJava(connection)
    val lineaPedidoRepositorioJava= BBDDRepositorioLineaPedidoJava(connection)

    val dependienteRepositorio: IDependienteRepositorio = BBDDDependienteRepository(dependienteRepositorioJava )
    val categoriaRepositorio: ICategoriaRepositorio = BBDDCategoriaRepository(categoriaRepositorioJava)
    val productoRepositorio: BBDDProductoRepository = BBDDProductoRepository(productoRepositorioJava)
    val pedidoRepositorio: BBDDPedidoRepository = BBDDPedidoRepository(pedidoRepositorioJava)
    val lineaPedidoRepositorio: BBDDLineaPedidoRepository = BBDDLineaPedidoRepository(lineaPedidoRepositorioJava)


    //esto se queda igual
    configureExternalLogging("./logging.properties")
    Window(
        onCloseRequest = {
            //se cierra la conexion
            dependienteRepositorioJava.close()
            exitApplication()},
        title = "VegaBurguer",
    ) {
        //se envuelve el repositorio en java en uno que exista en Kotlin
        //nueva configuracion de acceso categoria
        App(AlmacenDatos(), dependienteRepositorio, categoriaRepositorio, productoRepositorio, pedidoRepositorio, lineaPedidoRepositorio, listarDependientesUseCase = ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.ListarDependientesUseCase(dependienteRepositorio, AlmacenDatos()) )
    }
}
fun configureExternalLogging(path: String) {
    try {
        FileInputStream(path).use { fis ->
            LogManager.getLogManager().readConfiguration(fis)
            println("Logging configurado desde: $path")
        }
    } catch (e: Exception) {
        println("⚠ No se pudo cargar logging.properties externo: $path")
        e.printStackTrace()
        }
}