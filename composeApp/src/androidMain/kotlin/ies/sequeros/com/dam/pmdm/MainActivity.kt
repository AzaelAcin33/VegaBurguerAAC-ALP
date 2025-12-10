package ies.sequeros.com.dam.pmdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.ficheros.FileCategoriaRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.ficheros.FileDependienteRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.ficheros.FileLineaPedidoRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.ficheros.FilePedidoRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.ficheros.FileProductoRepository
//import ies.sequeros.com.dam.pmdm.administrador.infraestructura.memoria.FileDependienteRepository
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.ILinePedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //variables para el repositorio
        //se crea el almacen para el json
        val almacenDatos:AlmacenDatos=  AlmacenDatos(this)
        //se le pasa al repositorio
        val dependienteRepositorio: IDependienteRepositorio =
            FileDependienteRepository(almacenDatos)
        val categoriaRepositorio: ICategoriaRepositorio =
            FileCategoriaRepository(almacenDatos)
        val productoRepositorio: IProductoRepositorio =
            FileProductoRepository(almacenDatos)
        val pedidoRepositorio: IPedidoRepositorio =
            FilePedidoRepository(almacenDatos)
        val lineaPedidoRepositorio: ILinePedidoRepositorio =
            FileLineaPedidoRepository(almacenDatos)
        val listarDependientesUseCase = ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.ListarDependientesUseCase(dependienteRepositorio, almacenDatos)
        val listarProductoUseCase = ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ListarProductoUseCase(productoRepositorio, categoriaRepositorio, almacenDatos)


        enableEdgeToEdge()
        super.onCreate(savedInstanceState)


        setContent {
            //se crean almacenes de datos y de imagenes propias de la plataforma y se
            //pasan a la aplicación,
            val almacenImagenes:AlmacenDatos=  AlmacenDatos(this)

            App(almacenImagenes,dependienteRepositorio, categoriaRepositorio,
                productoRepositorio, pedidoRepositorio, lineaPedidoRepositorio,
                listarDependientesUseCase, listarProductoUseCase )
        }
    }
}

