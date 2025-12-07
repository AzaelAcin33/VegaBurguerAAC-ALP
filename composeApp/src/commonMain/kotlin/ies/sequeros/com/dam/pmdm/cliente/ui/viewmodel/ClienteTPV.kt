package ies.sequeros.com.dam.pmdm.cliente.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import ies.sequeros.com.dam.pmdm.AppViewModel

@Suppress("ViewModelConstructorInComposable")
@Composable
fun ClienteTPVViewModel(
    appViewModel: AppViewModel,
    clienteTPVViewModel: ClienteTPVViewModel,
    onExit: () -> Unit
){
    val navController = rememberNavController()
    val options by clienteTPVViewModel.filteredItems.collectAsState()
    val wai by appViewModel.windowsAdaptativeInfo.collectAsState()

    val navegador: @Composable () -> Unit = {
        /**
            primero saldra una pantalla donde nos pedira inteoducir el nombre y le
            daremos a siguiente, como un login de solo el nombre

            despues, aqui tendria que hacer que aparezcan todas las
            categorias que tenga creadas

            y al pinchar en una de las categorias existente, me llevara a otro
            punto de navegacion, donde aparezcan todos los producto de esa categoria

            y ahi poder escoger la cantidad de esos productos que quiero y se vayan
            añadiendo a la lista de carrito actualizando automaticamente el precio
            del carrito de compra,

            una pantalla de pago, donde nos saldra la cantidad de items que hemos escogido
            con su precio, nos dejara elegir un dependiente desde el combobox,
            la fecha de compra realizada

            por ultimo boton de guardar para guardar el contenido del pedido
            este se almacenara en la base de datos para luego poder cargar esta informacion
            en el apartado de pedidos en administrador
         */



    }
}