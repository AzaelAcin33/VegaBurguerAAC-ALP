package ies.sequeros.com.dam.pmdm.cliente.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.cliente.ui.viewmodel.ClienteTPVViewModel

@Composable
fun LoginScreen(
    viewModel: ClienteTPVViewModel,
    onNavigateNext: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido a VegaBurguer", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Introduce tu nombre") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.setNombreCliente(nombre)
                onNavigateNext()
            },
            enabled = nombre.isNotBlank()
        ) {
            Text("Comenzar Pedido")
        }
    }
}