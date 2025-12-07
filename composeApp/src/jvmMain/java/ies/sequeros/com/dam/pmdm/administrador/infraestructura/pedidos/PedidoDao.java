package ies.sequeros.com.dam.pmdm.administrador.infraestructura.pedidos;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.IDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PedidoDao implements IDao<Pedido> {
    private DataBaseConnection conn;

    // 1. Nombre de la tabla correcto
    private final String table_name = "pedidos";

    // 2. Sentencias SQL corregidas con las columnas de PEDIDOS
    private final String selectall = "select * from " + table_name;
    private final String selectbyid = "select * from " + table_name + " where id=?";

    // Ojo: En pedidos buscamos por 'clienteName', no por 'name'
    private final String findbyname = "select * from " + table_name + " where clienteName=?";

    private final String deletebyid = "delete from " + table_name + " where id=?";

    // INSERT: Usamos clienteName, estado, fecha, dependienteId
    private final String insert = "INSERT INTO " + table_name + " (id, clienteName, estado, fecha, dependienteId) " +
            "VALUES (?, ?, ?, ?, ?)";

    // UPDATE
    private final String update =
            "UPDATE " + table_name + " SET clienteName = ?, estado = ?, fecha = ?, dependienteId = ? " +
                    "WHERE id = ?";

    public PedidoDao() {
    }

    public DataBaseConnection getConn() {
        return this.conn;
    }

    public void setConn(final DataBaseConnection conn) {
        this.conn = conn;
    }

    @Override
    public Pedido getById(final String id) {
        Pedido p = null;
        try {
            final PreparedStatement pst = conn.getConnection().prepareStatement(selectbyid);
            pst.setString(1, id);
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                p = registerToObject(rs);
            }
            pst.close();
            return p;
        } catch (final SQLException ex) {
            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }

    // Método opcional si necesitas buscar por nombre de cliente
    public Pedido findByName(final String clienteName) {
        Pedido p = null;
        try {
            final PreparedStatement pst = conn.getConnection().prepareStatement(findbyname);
            pst.setString(1, clienteName);
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                p = registerToObject(rs);
            }
            pst.close();
            return p;
        } catch (final SQLException ex) {
            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }

    @Override
    public List<Pedido> getAll() {
        final ArrayList<Pedido> lista = new ArrayList<>();
        try {
            PreparedStatement pst = conn.getConnection().prepareStatement(selectall);
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                lista.add(registerToObject(rs));
            }
            pst.close();
        } catch (final SQLException ex) {
            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    @Override
    public void update(final Pedido item) {
        try {
            final PreparedStatement pst = conn.getConnection().prepareStatement(update);
            // Orden de los ? en el UPDATE:
            // 1=clienteName, 2=estado, 3=fecha, 4=dependienteId, 5=id (WHERE)
            pst.setString(1, item.getClienteName());
            pst.setString(2, item.getEstado());
            pst.setLong(3, item.getFecha());
            pst.setString(4, item.getDependienteId());
            pst.setString(5, item.getId());

            pst.executeUpdate();
            pst.close();
        } catch (final SQLException ex) {
            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(final Pedido item) {
        try {
            final PreparedStatement pst = conn.getConnection().prepareStatement(deletebyid);
            pst.setString(1, item.getId());
            pst.executeUpdate();
            pst.close();
        } catch (final SQLException ex) {
            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insert(final Pedido item) {
        try {
            PreparedStatement pst = conn.getConnection().prepareStatement(insert);
            // Orden de los ? en el INSERT:
            // 1=id, 2=clienteName, 3=estado, 4=fecha, 5=dependienteId
            pst.setString(1, item.getId());
            pst.setString(2, item.getClienteName());
            pst.setString(3, item.getEstado());
            pst.setDouble(4, item.getTotal());
            pst.setLong(5, item.getFecha());

            // dependienteId puede ser nulo si no hay nadie asignado
            if (item.getDependienteId() != null) {
                pst.setString(5, item.getDependienteId());
            } else {
                pst.setNull(5, java.sql.Types.VARCHAR);
            }

            pst.executeUpdate();
            pst.close();

        } catch (final SQLException ex) {
            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, "Error insertando pedido: " + ex.getMessage(), ex);
        }
    }

    // Convertir fila de la BBDD a Objeto Java
    private Pedido registerToObject(final ResultSet r) {
        Pedido p = null;
        try {
            p = new Pedido(
                    r.getString("id"),
                    r.getString("clienteName"),
                    r.getString("estado"),
                    r.getDouble("total"),
                    r.getLong("fecha"),
                    r.getString("dependienteId"),
                    new ArrayList<>() // <--- ¡AQUÍ ESTÁ LA SOLUCIÓN! Pasamos una lista vacía.
            );
            return p;
        } catch (final SQLException ex) {
            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
}