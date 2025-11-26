package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.actualizar

import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ActualizarPedidoUseCase(private val repositorio: IPedidoRepositorio,
                              private val almacenDatos: AlmacenDatos) {
}