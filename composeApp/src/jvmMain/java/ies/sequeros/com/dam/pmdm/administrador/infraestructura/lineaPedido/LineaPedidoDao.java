package ies.sequeros.com.dam.pmdm.administrador.infraestructura.lineaPedido;

import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido;
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.IDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LineaPedidoDao implements IDao<LineaPedido> {
    private DataBaseConnection conn;
    private final String table_name = "PRODUCTO";
    private final String selectall = "select * from " + table_name;
    private final String selectbyid = "select * from " + table_name + " where id=?";
    private final String findbyname = "select * from " + table_name + " where name=?";

    private final String deletebyid = "delete from " + table_name + " where id='?'";
    private final String insert = "INSERT INTO " + table_name + " (id, name, image_path, price, description, enabled) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String update =
            "UPDATE " + table_name + " SET name = ?, image_path = ?, price = ?, description = ?, enabled = ? " +
                    "WHERE id = ?";
    public LineaPedidoDao() {
    }

    public DataBaseConnection getConn() {
        return this.conn;
    }

    public void setConn(final DataBaseConnection conn) {
        this.conn = conn;
    }

    @Override
    public LineaPedido getById(final String id) {
        LineaPedido sp = null;
        try {
            final PreparedStatement pst = conn.getConnection().prepareStatement(selectbyid);
            pst.setString(1, id);
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                sp = registerToObject(rs);
            }
            pst.close();
            Logger logger = Logger.getLogger(LineaPedidoDao.class.getName());
            logger.info("Ejecutando SQL: " + selectbyid + " | Parametros: [id=" + id + "]");
            return sp;
        } catch (final SQLException ex) {
            Logger.getLogger(LineaPedidoDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return sp;
    }

    public LineaPedido findByName(final String name) {
        LineaPedido sp = null;
        try {
            final PreparedStatement pst = conn.getConnection().prepareStatement(findbyname);
            pst.setString(1, name);
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                sp = registerToObject(rs);
            }
            pst.close();
            Logger logger = Logger.getLogger(LineaPedidoDao.class.getName());
            logger.info("Ejecutando SQL: " + findbyname + " | Parametros: [name=" + name + "]");

            return sp;
        } catch (final SQLException ex) {
            Logger.getLogger(LineaPedidoDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return sp;
    }
    @Override
    public List<LineaPedido> getAll() {
        final ArrayList<LineaPedido> scl = new ArrayList<>();
        LineaPedido tempo;
        PreparedStatement pst = null;
        try {
            try {
                pst = conn.getConnection().prepareStatement(selectall);
            } catch (final SQLException ex) {
                Logger.getLogger(LineaPedidoDao.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                tempo = registerToObject(rs);
                scl.add(tempo);
            }

            pst.close();
            Logger logger = Logger.getLogger(LineaPedidoDao.class.getName());
            logger.info("Ejecutando SQL: " + selectall+ " | Parametros: ");

        } catch (final SQLException ex) {
            Logger.getLogger(LineaPedidoDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

        return scl;
    }

    @Override
    public void update(final LineaPedido item) {

        try {
            final PreparedStatement pst =
                    conn.getConnection().prepareStatement(update);
            pst.setInt(1, item.getCantidad());
            pst.setString(2, item.getPrecioUnitario());
            pst.setBoolean(3, item.getEntregado());
            pst.setString(4, item.getPedidoId());
            pst.setString(5, item.getProductoId());
            pst.setString(6, item.getId());
            pst.executeUpdate();
            pst.close();
            Logger logger = Logger.getLogger(LineaPedidoDao.class.getName());
            logger.info(() ->
                    "Ejecutando SQL: " + update +
                            " | Params: [1]=" + item.getCantidad() +
                            ", [2]=" + item.getPrecioUnitario() +
                            ", [3]=" + item.getEntregado() +
                            ", [4]=" + item.getPedidoId() +
                            ", [5]=" + item.getProductoId() +
                            ", [6]=" + item.getId() +
                            "]"
            );
        } catch (final SQLException ex) {
            Logger.getLogger(LineaPedidoDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

    }

    @Override
    public void delete(final LineaPedido item) {
        try {
            final PreparedStatement pst =
                    conn.getConnection().prepareStatement(deletebyid);
            pst.setString(1, item.getId());
            pst.executeUpdate();
            pst.close();
            Logger logger = Logger.getLogger(LineaPedidoDao.class.getName());
            logger.info("Ejecutando SQL: " + deletebyid + " | Parametros: [id=" + item.getId() + "]");

        } catch (final SQLException ex) {
            Logger.getLogger(LineaPedidoDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    @Override
    public void insert(final LineaPedido item) {

        final PreparedStatement pst;
        try {
            pst = conn.getConnection().prepareStatement(insert,
                    Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, item.getCantidad());
            pst.setString(2, item.getPrecioUnitario());
            pst.setBoolean(3, item.getEntregado());
            pst.setString(4, item.getPedidoId());
            pst.setString(5, item.getProductoId());
            pst.setString(6, item.getId());

            pst.executeUpdate();
            pst.close();
            Logger logger = Logger.getLogger(LineaPedidoDao.class.getName());
            logger.info(() ->
                    "Ejecutando SQL: " + update +
                            " | Params: [1]=" + item.getCantidad() +
                            ", [2]=" + item.getPrecioUnitario() +
                            ", [3]=" + item.getEntregado() +
                            ", [4]=" + item.getPedidoId() +
                            ", [5]=" + item.getProductoId() +
                            ", [6]=" + item.getId() +
                            "]"
            );

        } catch (final SQLException ex) {
            Logger.getLogger(LineaPedidoDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    //pasar de registro a objeeto
    private LineaPedido registerToObject(final ResultSet r) {
        LineaPedido sc =null;

        try {

            sc=new LineaPedido(
                    r.getString("ID"),
                    r.getInt("CANTIDAD"),
                    r.getString("PRECIO_UNITARIO"),
                    r.getBoolean("ENTREGADO"),
                    r.getString("PEDIDO_ID"),
                    r.getString("PRODUCTO_ID")
            );
            return sc;
        } catch (final SQLException ex) {
            Logger.getLogger(LineaPedidoDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return sc;
    }
}
