// Archivo: composeApp/src/commonMain/kotlin/ies/sequeros/com/dam/pmdm/cliente/ui/navigation/ClienteNavGraph.kt
package ies.sequeros.com.dam.pmdm.cliente.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost      // Importante
import androidx.navigation.compose.composable   // Importante
import androidx.navigation.NavType              // Importante si usas argumentos
import androidx.navigation.navArgument          // Importante si usas argumentos
import androidx.savedstate.SavedState
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
        composable(ClienteRoutes.LOGIN) {
            LoginScreen(
                viewModel = viewModel,
                onNavigateNext = { navController.navigate(ClienteRoutes.CATEGORIAS) }
            )
        }
        composable(ClienteRoutes.CATEGORIAS) {
            CategoriasScreen(
                viewModel = viewModel,
                onCategoriaSelected = { catId ->
                    navController.navigate(ClienteRoutes.buildProductosRoute(catId))
                }
            )
        }
        // Definición de la ruta con argumento
        composable(
            route = ClienteRoutes.PRODUCTOS,
            arguments = listOf(navArgument("categoriaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val catId = backStackEntry.arguments?.getString("categoriaId") ?: return@composable
            ProductosScreen(
                categoriaId = catId,
                viewModel = viewModel,
                onNavigateToPago = { navController.navigate(ClienteRoutes.PAGO) }
            )
        }
        composable(ClienteRoutes.PAGO) {
            PagoScreen(
                viewModel = viewModel,
                onPedidoGuardado = {
                    navController.navigate(ClienteRoutes.LOGIN) {
                        popUpTo(ClienteRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
    }
}


