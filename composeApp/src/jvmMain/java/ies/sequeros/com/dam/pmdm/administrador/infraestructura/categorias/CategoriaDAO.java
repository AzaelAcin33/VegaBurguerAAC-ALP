package ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias;

import ies.sequeros.com.dam.pmdm.administrador.infraestructura.dependientes.DependienteDao;
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.IDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoriaDAO implements IDao<Categoria> {

    private DataBaseConnection conn;
    private final String table_name = "CATEGORIA";
    private final String selectall = "select * from " + table_name;
    private final String selectbyid = "select * from " + table_name + " where id=?";
    private final String findbyname = "select * from " + table_name + " where name=?";
    private final String deletedbyid = "delete from " + table_name + " where id=?";
    private final String insert = "INSERT INTO " + table_name + "(id,name,imagePath,description,enabled) VALUES (?,?,?,?,?)";
    private final String update = "UPDATE " + table_name + " SET id=?, name=?, imagePath=?, description=?, enabled=? WHERE id=?";

    public CategoriaDAO() {
    }

    public DataBaseConnection getConn() {
        return this.conn;
    }
    public void setConn(final DataBaseConnection conn) {
        this.conn = conn;
    }

    @Override
    public Categoria getById(String id) {
        Categoria cat = null;
        try{
            final PreparedStatement pst = conn.getConnection().prepareStatement(selectbyid);
            pst.setString(1,id);
            final ResultSet rs = pst.executeQuery();
            while(rs.next()){
                cat = registerToObject(rs);
            }
            pst.close();
            Logger logger = Logger.getLogger(DependienteDao.class.getName());
            logger.info("Ejecutando SQL: " + selectbyid + " | Parametros: [id=" + id + "]");
            return cat;
        } catch (final SQLException ex) {
            Logger.getLogger(DependienteDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return cat;
    }

    @Override
    public List<Categoria> getAll() {
        return List.of();
    }

    @Override
    public void update(Categoria item) {

    }

    @Override
    public void delete(Categoria item) {

    }

    @Override
    public void insert(Categoria item) {

    }

    private Categoria registerToObject(ResultSet rs) {
        return null;
        //rellenar mas tarde
    }
}