package ies.sequeros.com.dam.pmdm.administrador.ui.Pedidos

import androidx.lifecycle.ViewModel
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class PedidosViewModel (
    private val pedidoRepositorio: IPedidoRepositorio,
    val almacenDatos: AlmacenDatos
): ViewModel(){
}