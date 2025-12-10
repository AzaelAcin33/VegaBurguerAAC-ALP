package ies.sequeros.com.dam.pmdm.administrador.ui

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import ies.sequeros.com.dam.pmdm.AppViewModel
import ies.sequeros.com.dam.pmdm.administrador.modelo.ILinePedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.ui.categorias.CategoriasViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.DependientesViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.PedidosViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.productos.ProductosViewModel

@Composable
fun AdminRoot(
    //Lo que hace este archivo es mostrar login si no hay sesión, mostrar pantalla de admin si se conecta y mantener la UI con todos los View Models
    appViewModel: AppViewModel,
    mainViewModel: MainAdministradorViewModel,
    administradorViewModel: AdministradorViewModel,
    dependientesViewModel: DependientesViewModel,
    categoriasViewModel: CategoriasViewModel,
    productosViewModel: ProductosViewModel,
    pedidosViewModel: PedidosViewModel,
    lineaPedidoRepositorio: ILinePedidoRepositorio,
    onExit: () -> Unit
) {
    val loginVM: LoginAdministradorViewModel = viewModel()
    val loggedIn by loginVM.loginSuccess.collectAsState()

    if (!loggedIn) {
        LoginAdminScreen(
            viewModel = loginVM,
            onLoginSuccess = {}
        )
    } else {
        MainAdministrador(
            appViewModel = appViewModel,
            mainViewModel = mainViewModel,
            administradorViewModel = administradorViewModel,
            dependientesViewModel = dependientesViewModel,
            categoriasViewModel = categoriasViewModel,
            productosViewModel = productosViewModel,
            pedidosViewModel = pedidosViewModel,
            lineaPedidoRepositorio = lineaPedidoRepositorio,
            onExit = {
                loginVM.resetState()
                onExit()
            }
        )
    }
}
