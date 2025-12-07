package ies.sequeros.com.dam.pmdm.cliente.ui.navigation

import kotlinx.serialization.Serializable

object ClienteRoutes {
    const val LOGIN = "cliente"
    const val CATEGORIAS = "categorias"

    @Serializable
    data class Productos(val categoriaId: String)
    const val PAGO = "pago"
}