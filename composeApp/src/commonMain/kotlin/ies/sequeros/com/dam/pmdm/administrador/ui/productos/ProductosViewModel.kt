package ies.sequeros.com.dam.pmdm.administrador.ui.productos

import androidx.lifecycle.ViewModel
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ProductosViewModel (
    private val productoRepositorio: IProductoRepositorio,
    val almacenDatos: AlmacenDatos
): ViewModel(){
}