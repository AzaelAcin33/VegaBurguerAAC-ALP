package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos

import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class BorrarProductoUseCase(private val repositorio: IProductoRepositorio,private val almacenDatos: AlmacenDatos) {
}