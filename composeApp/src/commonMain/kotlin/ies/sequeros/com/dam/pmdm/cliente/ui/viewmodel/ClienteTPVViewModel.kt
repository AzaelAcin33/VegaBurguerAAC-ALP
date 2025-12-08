package ies.sequeros.com.dam.pmdm.cliente.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.cliente.application.usecases.GetCategoriasUseCase
import ies.sequeros.com.dam.pmdm.cliente.application.usecases.SavePedidoUseCase
import ies.sequeros.com.dam.pmdm.cliente.domain.dto.LineaPedidoTPVDTO
import ies.sequeros.com.dam.pmdm.cliente.domain.dto.PedidoTPVDTO
import ies.sequeros.com.dam.pmdm.cliente.domain.dto.ProductoTPVDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.DependienteDTO // Asumiendo que existe
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.toDTO
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClienteTPVViewModel(
    private val categoriaRepo: ICategoriaRepositorio,
    private val productoRepo: IProductoRepositorio,
    private val dependienteRepo: IDependienteRepositorio,
    private val pedidoRepo: IPedidoRepositorio,
    private val almacenDatos: AlmacenDatos
) : ViewModel() {

    // Casos de uso
    private val getCategoriasUseCase = GetCategoriasUseCase(categoriaRepo)
    private val savePedidoUseCase = SavePedidoUseCase(pedidoRepo)

    // Estado
    private val _nombreCliente = MutableStateFlow("")
    val nombreCliente: StateFlow<String> = _nombreCliente.asStateFlow()

    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias.asStateFlow()

    private val _productos = MutableStateFlow<List<ProductoTPVDTO>>(emptyList())
    val productos: StateFlow<List<ProductoTPVDTO>> = _productos.asStateFlow()

    private val _carrito = MutableStateFlow<List<LineaPedidoTPVDTO>>(emptyList())
    val carrito: StateFlow<List<LineaPedidoTPVDTO>> = _carrito.asStateFlow()

    private val _totalCarrito = MutableStateFlow(0.0)
    val totalCarrito: StateFlow<Double> = _totalCarrito.asStateFlow()

    private val _dependientes = MutableStateFlow<List<DependienteDTO>>(emptyList())
    val dependientes: StateFlow<List<DependienteDTO>> = _dependientes.asStateFlow()

    private val _dependienteSeleccionado = MutableStateFlow<DependienteDTO?>(null)
    val dependienteSeleccionado: StateFlow<DependienteDTO?> = _dependienteSeleccionado.asStateFlow()

    // Lógica
    fun setNombreCliente(nombre: String) {
        _nombreCliente.value = nombre
    }

    fun loadCategorias() {
        viewModelScope.launch {
            // 1. Obtenemos la ruta base
            val rawPath = almacenDatos.getAppDataDir()+"/categorias/"

            // 2. Aseguramos que termine en "/"
            val path = if (rawPath.endsWith("/")) rawPath else "$rawPath/"

            // 3. Cargamos y mapeamos
            val listaCategorias = getCategoriasUseCase.invoke().map { categoria ->
                // Solo concatenamos si hay una imagen definida
                val finalPath = if (categoria.imagePath.isNotEmpty()) {
                    path + categoria.imagePath
                } else {
                    ""
                }

                // LOG PARA DEPURAR (Míralo en Logcat)
                //println("TPV DEBUG - Categoria: ${categoria.name} -> Ruta: $finalPath")

                categoria.copy(imagePath = finalPath)
            }
            _categorias.value = listaCategorias
        }
    }

    fun loadProductos(categoriaId: String) {
        viewModelScope.launch {
            // 1. Obtenemos la ruta base asegurando la barra
            val rawPath = almacenDatos.getAppDataDir()+"/productos/"
            val path = if (rawPath.endsWith("/")) rawPath else "$rawPath/"

            val allProds = productoRepo.getAll()

            _productos.value = allProds
                .filter { it.categoriaId == categoriaId }
                .map { producto ->

                    val finalPath = if (producto.imagePath.isNotEmpty()) {
                        path + producto.imagePath
                    } else {
                        ""
                    }

                    // LOG PARA DEPURAR
                    //println("TPV DEBUG - Producto: ${producto.name} -> Ruta: $finalPath")

                    ProductoTPVDTO(
                        id = producto.id,
                        nombre = producto.name,
                        precio = producto.price,
                        imagePath = finalPath, // Usamos la ruta corregida
                        categoriaId = producto.categoriaId,
                        enabled = producto.enabled
                    )
                }
        }
    }

    fun loadDependientes() {
        viewModelScope.launch {
            _dependientes.value = dependienteRepo.getAll().map { it.toDTO("") } // Ajustar mapper
        }
    }

    fun addToCarrito(producto: ProductoTPVDTO) {
        _carrito.update { currentList ->
            val existing = currentList.find { it.producto.id == producto.id }
            if (existing != null) {
                currentList.map {
                    if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                }
            } else {
                currentList + LineaPedidoTPVDTO(producto, 1)
            }
        }
        recalculateTotal()
    }

    fun removeFromCarrito(producto: ProductoTPVDTO) {
        _carrito.update { currentList ->
            val existing = currentList.find { it.producto.id == producto.id }
            if (existing != null && existing.cantidad > 1) {
                currentList.map {
                    if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad - 1) else it
                }
            } else {
                currentList.filter { it.producto.id != producto.id }
            }
        }
        recalculateTotal()
    }

    private fun recalculateTotal() {
        _totalCarrito.value = _carrito.value.sumOf { it.total }
    }

    fun selectDependiente(dependiente: DependienteDTO) {
        _dependienteSeleccionado.value = dependiente
    }

    fun finalizarPedido(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val pedido = PedidoTPVDTO(
                nombreCliente = _nombreCliente.value,
                lineas = _carrito.value,
                dependienteId = _dependienteSeleccionado.value?.id,
                fecha = System.currentTimeMillis(),
                total = _totalCarrito.value
            )
            savePedidoUseCase.invoke(pedido)
            // Limpiar estado
            _carrito.value = emptyList()
            _nombreCliente.value = ""
            _totalCarrito.value = 0.0
            _dependienteSeleccionado.value = null
            onSuccess()
        }
    }

    fun resetPedido() {
        _carrito.value = emptyList()
        _totalCarrito.value = 0.0
        _nombreCliente.value = ""
        _dependienteSeleccionado.value = null
    }

}