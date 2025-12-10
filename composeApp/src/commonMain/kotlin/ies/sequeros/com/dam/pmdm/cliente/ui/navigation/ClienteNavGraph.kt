package ies.sequeros.com.dam.pmdm.cliente.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute // [!IMPORTANTE!] Necesario para leer los argumentos
import ies.sequeros.com.dam.pmdm.cliente.ui.screens.*
import ies.sequeros.com.dam.pmdm.cliente.ui.viewmodel.ClienteTPVViewModel

@Composable
fun ClienteNavGraph(
    navController: NavHostController,
    viewModel: ClienteTPVViewModel,
    onExit: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = ClienteRoutes.LOGIN
    ) {
        // Pantalla login
        composable(ClienteRoutes.LOGIN) {
            LoginScreen(
                viewModel = viewModel,
                onNavigateNext = { navController.navigate(ClienteRoutes.CATEGORIAS) }
            )
        }

        // Pantalla categorias
        composable(ClienteRoutes.CATEGORIAS) {
            CategoriasScreen(
                viewModel = viewModel,
                onCategoriaSelected = { catId ->
                    navController.navigate(
                        ClienteRoutes.Productos(categoriaId = catId)
                    )
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<ClienteRoutes.Productos> { backStackEntry ->
            val args = backStackEntry.toRoute<ClienteRoutes.Productos>()

            ProductosScreen(
                categoriaId = args.categoriaId,
                viewModel = viewModel,
                onNavigateToPago = { navController.navigate(ClienteRoutes.PAGO) },
                onNavigateToLogin = { navController.navigate(ClienteRoutes.LOGIN) },
                onBack = { navController.popBackStack() }
            )
        }

        // Pantalla Pago
        composable(ClienteRoutes.PAGO) {
            PagoScreen(
                viewModel = viewModel,
                onPedidoGuardado = {
                    navController.navigate(ClienteRoutes.LOGIN) {
                        popUpTo(ClienteRoutes.LOGIN) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}