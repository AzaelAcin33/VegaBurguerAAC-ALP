package ies.sequeros.com.dam.pmdm.administrador.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import ies.sequeros.com.dam.pmdm.AppViewModel
import ies.sequeros.com.dam.pmdm.administrador.modelo.ILinePedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.ui.categorias.CategoriasViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.categorias.Categorias
import ies.sequeros.com.dam.pmdm.administrador.ui.categorias.form.CategoriaForm
import ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.Dependientes
import ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.DependientesViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.form.DependienteForm
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.PedidosViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.Pedidos
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form.PedidoForm
import ies.sequeros.com.dam.pmdm.administrador.ui.productos.ProductosViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.productos.Productos
import ies.sequeros.com.dam.pmdm.administrador.ui.productos.form.ProductoForm

@Suppress("ViewModelConstructorInComposable")
@Composable
fun MainAdministrador(
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
    val navController = rememberNavController()
    val options by mainViewModel.filteredItems.collectAsState()
    val wai by appViewModel.windowsAdaptativeInfo.collectAsState()

    mainViewModel.setOptions(
        listOf(
            ItemOption(Icons.Default.Home, {
                navController.navigate(AdminRoutes.Main) { launchSingleTop = true; popUpTo(AdminRoutes.Main) }
            }, "Home", false),
            ItemOption(Icons.Default.Person, {
                navController.navigate(AdminRoutes.Dependientes) { launchSingleTop = true; popUpTo(AdminRoutes.Main) }
            }, "Dependientes", true),
            ItemOption(Icons.Default.Icecream, {
                navController.navigate(AdminRoutes.Categorias) { launchSingleTop = true; popUpTo(AdminRoutes.Main) }
            }, "Categorias", true),
            ItemOption(Icons.Default.Fastfood, {
                navController.navigate(AdminRoutes.Productos) { launchSingleTop = true; popUpTo(AdminRoutes.Main) }
            }, "Productos", true),
            ItemOption(Icons.AutoMirrored.Filled.FactCheck, {
                navController.navigate(AdminRoutes.Pedidos) { launchSingleTop = true; popUpTo(AdminRoutes.Main) }
            }, "Pedidos", true),
            ItemOption(Icons.Default.DarkMode, { appViewModel.swithMode() }, "Darkmode", true),
            ItemOption(Icons.Default.Close, { onExit() }, "Close", false)
        )
    )

    val navegador: @Composable () -> Unit = {
        NavHost(navController, startDestination = AdminRoutes.Main) {
            composable(AdminRoutes.Main) { PrincipalAdministrador() }

            // --- Dependientes ---
            composable(AdminRoutes.Dependientes) {
                Dependientes(mainViewModel, dependientesViewModel, {
                    dependientesViewModel.setSelectedDependiente(it)
                    navController.navigate(AdminRoutes.Dependiente) { launchSingleTop = true }
                })
            }
            composable(AdminRoutes.Dependiente) {
                DependienteForm(dependientesViewModel, { navController.popBackStack() }, {
                    dependientesViewModel.save(it)
                    navController.popBackStack()
                })
            }

            // --- Categorias ---
            composable(AdminRoutes.Categorias) {
                Categorias(mainViewModel, categoriasViewModel, {
                    categoriasViewModel.setSelectedCategoria(it)
                    navController.navigate(AdminRoutes.Categoria) { launchSingleTop = true }
                })
            }
            composable(AdminRoutes.Categoria) {
                CategoriaForm(categoriasViewModel, { navController.popBackStack() }, {
                    categoriasViewModel.save(it)
                    navController.popBackStack()
                })
            }

            // --- Productos ---
            composable(AdminRoutes.Productos) {
                Productos(mainViewModel, productosViewModel, {
                    productosViewModel.setSelectedProducto(it)
                    navController.navigate(AdminRoutes.Producto) { launchSingleTop = true }
                })
            }
            composable(AdminRoutes.Producto) {
                ProductoForm(productosViewModel, categoriasViewModel, { navController.popBackStack() }, {
                    productosViewModel.save(it)
                    navController.popBackStack()
                })
            }

            // --- Pedidos ---
            composable(AdminRoutes.Pedidos) {
                Pedidos(mainViewModel, pedidosViewModel, {
                    navController.navigate(AdminRoutes.Pedido) { launchSingleTop = true }
                })
            }

            // --- FORMULARIO PEDIDO (INTEGRACIÓN COMPLETA) ---
            /*composable(AdminRoutes.Pedido) {
                PedidoForm(
                    // Inyectamos repositorios públicos
                    productoRepo = productosViewModel.productoRepositorio,
                    dependienteRepo = dependientesViewModel.dependienteRepositorio,

                    onClose = { navController.popBackStack() },
                    onConfirm = {
                        pedidosViewModel.save(it)
                        navController.popBackStack()
                    }
                )
            }*/

            composable(AdminRoutes.Pedido) {
                // Obtenemos el pedido seleccionado del ViewModel de listado
                val pedidoSeleccionado by pedidosViewModel.selected.collectAsState()

                // IMPORTANTE: Aquí asumo que tienes acceso a estos repositorios.
                // Si te sale en rojo 'lineaPedidoRepo', significa que debes pasarlo como parámetro
                // a la función MainAdministrador, igual que pasas 'dependientesViewModel'.

                if (pedidoSeleccionado != null) {
                    PedidoForm(
                        pedidoId = pedidoSeleccionado!!.id,
                        pedidoRepo = pedidosViewModel.pedidoRepositorio, // Este ya lo tienes en el VM
                        // ¡ATENCIÓN!: Estos dos deben estar disponibles en este ámbito.
                        // Si da error, añádelos a los argumentos de MainAdministrador(...) arriba del todo
                        lineaPedidoRepo = lineaPedidoRepositorio,
                        productoRepo = productosViewModel.productoRepositorio,
                        dependienteRepo = dependientesViewModel.dependienteRepositorio,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }

    if (wai?.windowSizeClass?.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    mainViewModel.filteredItems.collectAsState().value.forEach { item ->
                        NavigationBarItem(
                            selected = true,
                            onClick = { item.action() },
                            icon = { Icon(item.icon, contentDescription = item.name) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) { navegador() }
        }
    } else {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(Modifier.width(128.dp)) {
                    Column(
                        modifier = Modifier.fillMaxHeight().padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(16.dp))
                        options.forEach { item ->
                            NavigationDrawerItem(
                                icon = {
                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        Icon(item.icon, tint = MaterialTheme.colorScheme.primary, contentDescription = item.name)
                                    }
                                },
                                label = { },
                                selected = false,
                                onClick = { item.action() },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            },
            content = {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) { navegador() }
            }
        )
    }
}