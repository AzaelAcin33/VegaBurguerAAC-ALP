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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClienteTPVViewModel(
    categoriaRepo: ICategoriaRepositorio,
    private val productoRepo: IProductoRepositorio,
    private val dependienteRepo: IDependienteRepositorio,
    pedidoRepo: IPedidoRepositorio
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
            _categorias.value = getCategoriasUseCase.invoke()
        }
    }

    fun loadProductos(categoriaId: String) {
        viewModelScope.launch {
            // Asumiendo que productoRepo tiene getByCategoria o getAll y filtramos
            val allProds = productoRepo.getAll() // Ajustar según tu repo real
            _productos.value = allProds
                .filter { it.categoriaId == categoriaId } // Asumiendo campo categoriaId en Producto
                .map {
                    ProductoTPVDTO(it.id, it.name, it.price, it.imagePath, it.categoriaId)
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
}