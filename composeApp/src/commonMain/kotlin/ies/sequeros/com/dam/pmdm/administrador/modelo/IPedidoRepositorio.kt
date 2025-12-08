package ies.sequeros.com.dam.pmdm.administrador.modelo

interface IPedidoRepositorio {
    suspend fun add(item:Pedido):Unit
    suspend fun remove(item:Pedido): Boolean
    suspend fun remove(id:String): Boolean
    suspend fun update(item:Pedido): Boolean
    suspend fun getAll():List<Pedido>
    suspend fun findByName(name:String): Pedido?
    suspend fun getById(id:String):Pedido?
    abstract fun listarProductos(): List<Producto>
}