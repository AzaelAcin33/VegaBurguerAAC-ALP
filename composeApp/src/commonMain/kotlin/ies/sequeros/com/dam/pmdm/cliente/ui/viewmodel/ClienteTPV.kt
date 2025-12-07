// Archivo: composeApp/src/commonMain/kotlin/ies/sequeros/com/dam/pmdm/cliente/ui/viewmodel/ClienteTPV.kt
package ies.sequeros.com.dam.pmdm.cliente.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import ies.sequeros.com.dam.pmdm.AppViewModel
import ies.sequeros.com.dam.pmdm.cliente.ui.navigation.ClienteNavGraph

@Composable
fun ClienteMainScreen( // <--- CAMBIAR NOMBRE AQUÍ (antes ClienteTPVViewModel)
    appViewModel: AppViewModel,
    viewModel: ClienteTPVViewModel, // Ahora se refiere correctamente a la Clase
    onExit: () -> Unit
){
    val navController = rememberNavController()

    // Llamamos al grafo de navegación que gestionará las pantallas
    ClienteNavGraph(
        navController = navController,
        viewModel = viewModel,
        onExit = onExit
    )
}