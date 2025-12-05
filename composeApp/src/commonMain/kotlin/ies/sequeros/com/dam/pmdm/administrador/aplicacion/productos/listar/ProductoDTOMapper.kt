package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto

fun Producto.toDTO(path: String = "", string: String) = ProductoDTO(
    id = id,
    name = name,
    imagePath=path+imagePath,
    price = price,
    description = description,
    enabled,
    categoriaId = categoriaId
)
fun ProductoDTO.toProducto()= Producto(
    id = id,
    name = name,
    imagePath = imagePath,
    price = price,
    description = description,
    enabled,
    categoriaId = categoriaId
)