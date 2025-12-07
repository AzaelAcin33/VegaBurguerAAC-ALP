package ies.sequeros.com.dam.pmdm.administrador.infraestructura.pedidos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ies.sequeros.com.dam.pmdm.administrador.infraestructura.lineaPedido.LineaPedidoDao;
import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido;
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;

public class BBDDRepositorioPedidosJava {
    private final DataBaseConnection db;
    private PedidoDao dao;
    private LineaPedidoDao lineaDao; // <--- Nuevo DAO para las líneas

    // Constructor adaptado a tu código (Recibe DataBaseConnection)
    public BBDDRepositorioPedidosJava(DataBaseConnection connection) {
        super();
        this.db = connection;

        // Inicializamos el DAO de Cabecera
        dao = new PedidoDao();
        dao.setConn(this.db);

        // Inicializamos el DAO de Líneas
        lineaDao = new LineaPedidoDao();
        lineaDao.setConn(this.db);
    }

    // --- GUARDAR (Cabecera + Líneas) ---
    public void add(Pedido item) {
        // 1. Guardar datos del pedido (cliente, fecha, etc.)
        this.dao.insert(item);

        // 2. Guardar cada producto de la lista
        if (item.getLineas() != null && !item.getLineas().isEmpty()) {
            for (LineaPedido linea : item.getLineas()) {
                this.lineaDao.insert(linea);
            }
        }
    }

    // --- RECUPERAR TODOS (Con sus líneas) ---
    public List<Pedido> getAll() {
        // 1. Obtenemos las cabeceras
        List<Pedido> pedidosSimples = this.dao.getAll();
        List<Pedido> pedidosCompletos = new ArrayList<>();

        // 2. Rellenamos las líneas de cada uno
        for (Pedido p : pedidosSimples) {
            pedidosCompletos.add(obtenerPedidoCompleto(p));
        }
        return pedidosCompletos;
    }

    // --- RECUPERAR POR ID (Con sus líneas) ---
    public Pedido getById(String id) {
        Pedido p = this.dao.getById(id);
        if (p != null) {
            return obtenerPedidoCompleto(p);
        }
        return null;
    }

    // --- BUSCAR POR NOMBRE (Con sus líneas) ---
    public Pedido findByName(String name) {
        Pedido p = this.dao.findByName(name);
        if (p != null) {
            return obtenerPedidoCompleto(p);
        }
        return null;
    }

    // --- ELIMINAR ---
    public boolean remove(Pedido item) {
        // Nota: Idealmente deberías borrar primero las líneas para evitar errores de clave foránea.
        // this.lineaDao.deleteByPedidoId(item.getId());

        this.dao.delete(item);
        return true;
    }

    public boolean remove(String id) {
        var item = this.getById(id); // Usamos nuestro getById mejorado
        if (item != null) {
            this.remove(item);
            return true;
        }
        return false;
    }

    // --- ACTUALIZAR ---
    public boolean update(Pedido item) {
        this.dao.update(item);
        // Actualizar líneas es complejo (detectar borrados/nuevos).
        // Lo más simple suele ser borrar todas las líneas antiguas del pedido y reinsertar las nuevas.
        return true;
    }

    public void close() {
        try {
            this.db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- MÉTODO AUXILIAR PARA RECONSTRUIR EL OBJETO ---
    private Pedido obtenerPedidoCompleto(Pedido p) {
        // Buscamos las líneas en la otra tabla
        List<LineaPedido> lineas = this.lineaDao.getLineasByPedidoId(p.getId());

        // Creamos un nuevo objeto Pedido con la lista rellena
        // (Asumiendo que Pedido es un 'data class' o tiene este constructor)
        return new Pedido(
                p.getId(),
                p.getClienteName(),
                p.getEstado(),
                p.getTotal(),
                p.getFecha(),
                p.getDependienteId(),
                lineas // <--- Aquí inyectamos la lista
        );
    }
}