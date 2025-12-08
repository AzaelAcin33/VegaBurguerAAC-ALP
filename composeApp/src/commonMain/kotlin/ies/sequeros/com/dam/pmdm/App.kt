package ies.sequeros.com.dam.pmdm

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.ListarDependientesUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ListarProductoUseCase
//import ies.sequeros.com.dam.pmdm.administrador.infraestructura.memoria.FileDependienteRepository
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.ILinePedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.ui.*

import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.PedidosViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.DependientesViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.categorias.CategoriasViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.productos.ProductosViewModel
import ies.sequeros.com.dam.pmdm.cliente.ui.viewmodel.ClienteMainScreen
import ies.sequeros.com.dam.pmdm.cliente.ui.viewmodel.ClienteTPVViewModel

@Suppress("ViewModelConstructorInComposable")
@Composable

fun App( almacenImagenes:AlmacenDatos,
         dependienteRepositorio : IDependienteRepositorio,
         categoriaRepositorio: ICategoriaRepositorio,
         productoRepositorio: IProductoRepositorio,
         pedidoRepositorio: IPedidoRepositorio,
         //intentos de poner el dependiente nombre
         lineaPedidoRepositorio: ILinePedidoRepositorio,
         listarDependientesUseCase: ListarDependientesUseCase,
         listarProductoUseCase: ListarProductoUseCase
) {

    //view model
    val appViewModel= viewModel {  AppViewModel() }
    val mainViewModel= remember { MainAdministradorViewModel(listarDependientesUseCase, listarProductoUseCase) }
    val administradorViewModel= viewModel { AdministradorViewModel() }
    val dependientesViewModel = viewModel{ DependientesViewModel(
        dependienteRepositorio, almacenImagenes
    )}
    val categoriasViewModel= viewModel { CategoriasViewModel(categoriaRepositorio,almacenImagenes) }

    val productosViewModel= viewModel {ProductosViewModel(productoRepositorio, categoriaRepositorio,almacenImagenes)}
    val pedidosViewModel= viewModel { PedidosViewModel(pedidoRepositorio, almacenImagenes) }
    //val lineaPedidoRepositorio = viewModel { pedidosViewModel.pedidoRepositorio.lineaPedidoRepositorio }


    appViewModel.setWindowsAdatativeInfo( currentWindowAdaptiveInfo())
    val navController= rememberNavController()
//para cambiar el modo
    AppTheme(appViewModel.darkMode.collectAsState()) {

        NavHost(
            navController,
            startDestination = AppRoutes.Main
        ) {
            composable(AppRoutes.Main) {
                Principal({
                    navController.navigate(AppRoutes.Administrador)
                }, {}, {
                    navController.navigate(AppRoutes.Cliente)
                },)
            }

            composable(AppRoutes.Administrador) {
                val loginVM = remember { LoginAdministradorViewModel() }

                val isLogged by loginVM.loginSuccess.collectAsState()

                if (!isLogged) {
                    LoginAdminScreen(
                        viewModel = loginVM,
                        onLoginSuccess = {
                        }
                    )
                } else {
                    MainAdministrador(
                        appViewModel,
                        mainViewModel,
                        administradorViewModel,
                        dependientesViewModel,
                        categoriasViewModel,
                        productosViewModel,
                        pedidosViewModel,
                        lineaPedidoRepositorio,
                        onExit = {
                            loginVM.resetState()
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(AppRoutes.Cliente){

                ClienteMainScreen(
                    appViewModel,
                    viewModel = ClienteTPVViewModel(
                        categoriaRepo = categoriaRepositorio,
                        productoRepo = productoRepositorio,
                        dependienteRepo = dependienteRepositorio,
                        pedidoRepo = pedidoRepositorio,
                        almacenDatos = almacenImagenes
                    ),
                    onExit = {
                        navController.popBackStack()
                    }
                )
            }


        }
    }

}