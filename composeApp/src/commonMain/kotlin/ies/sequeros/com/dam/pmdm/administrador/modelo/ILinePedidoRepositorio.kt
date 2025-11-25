package ies.sequeros.com.dam.pmdm.administrador.modelo

interface ILinePedidoRepositorio {
    suspend fun add(item:LineaPedido):Unit
    suspend fun remove(item:LineaPedido): Boolean
    suspend fun remove(id:String): Boolean
    suspend fun update(item:LineaPedido): Boolean
    suspend fun getAll():List<LineaPedido>
    suspend fun findByName(name:String):LineaPedido?
    suspend fun getById(id:String):LineaPedido?
}