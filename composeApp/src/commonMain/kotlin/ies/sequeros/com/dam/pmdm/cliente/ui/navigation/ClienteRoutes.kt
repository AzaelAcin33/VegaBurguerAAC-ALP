package ies.sequeros.com.dam.pmdm.cliente.ui.navigation

object ClienteRoutes {
    const val LOGIN = "cliente_login"
    const val CATEGORIAS = "cliente_categorias"
    const val PRODUCTOS = "cliente_productos/{categoriaId}"
    const val PAGO = "cliente_pago"

    fun buildProductosRoute(categoriaId: String) = "cliente_productos/$categoriaId"
}