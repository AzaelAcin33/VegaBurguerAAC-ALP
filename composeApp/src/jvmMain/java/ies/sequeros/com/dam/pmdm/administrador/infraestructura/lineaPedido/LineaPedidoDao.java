package ies.sequeros.com.dam.pmdm.administrador.infraestructura.lineaPedido;

import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.IDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LineaPedidoDao implements IDao<LineaPedido> {
    private DataBaseConnection conn;
    private final String table_name = "linea_pedido";

    // SQL Statements
    private final String insert = "INSERT INTO " + table_name +
            " (id, cantidad, precioUnitario, entregado, pedidoId, productoId) VALUES (?, ?, ?, ?, ?, ?)";

    private final String selectByPedidoId = "SELECT * FROM " + table_name + " WHERE pedidoId = ?";
    private final String deleteByPedidoId = "DELETE FROM " + table_name + " WHERE pedidoId = ?";

    public void setConn(DataBaseConnection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(LineaPedido item) {
        try {
            PreparedStatement pst = conn.getConnection().prepareStatement(insert);
            pst.setString(1, item.getId());
            pst.setInt(2, item.getCantidad());
            pst.setString(3, item.getPrecioUnitario());
            pst.setBoolean(4, item.getEntregado()); // Java maneja el booleano a TINYINT automáticamente
            //pst.setString(5, item.getPedidoId());
            pst.setString(5, item.getProductoId());

            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(LineaPedidoDao.class.getName()).log(Level.SEVERE, "Error insertando linea: " + ex.getMessage(), ex);
        }
    }

    // Método específico para recuperar las líneas de un pedido concreto
    public List<LineaPedido> getLineasByPedidoId(String pedidoId) {
        List<LineaPedido> lista = new ArrayList<>();
        try {
            PreparedStatement pst = conn.getConnection().prepareStatement(selectByPedidoId);
            pst.setString(1, pedidoId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                lista.add(new LineaPedido(
                        rs.getString("id"),
                        rs.getInt("cantidad"),
                        rs.getString("precioUnitario"),
                        rs.getBoolean("entregado"),
                        //rs.getString("pedidoId"),
                        rs.getString("productoId")
                ));
            }
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(LineaPedidoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    // Métodos obligatorios de IDao que quizás no uses ahora mismo pero debes implementar
    @Override
    public LineaPedido getById(String id) { return null; }
    @Override
    public List<LineaPedido> getAll() { return new ArrayList<>(); }
    @Override
    public void update(LineaPedido item) { }
    @Override
    public void delete(LineaPedido item) { }
}