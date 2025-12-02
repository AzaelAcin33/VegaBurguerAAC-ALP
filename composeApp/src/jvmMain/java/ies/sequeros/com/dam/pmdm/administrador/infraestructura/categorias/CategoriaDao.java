package ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.IDao;

import javax.smartcardio.CardTerminal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoriaDao implements IDao<Categoria> {

    private DataBaseConnection conn;
    private final String table_name = "categorias";
    private final String selectall = "select * from " + table_name;
    private final String selectbyid = "select * from " + table_name + " where id=?";
    private final String findbyname = "select * from " + table_name + " where name=?";
    private final String deletebyid = "delete from " + table_name + " where id=?";
    private final String insert = "INSERT INTO " + table_name + "(id,name,imagePath,description,enabled) VALUES (?,?,?,?,?)";
    private final String update = "UPDATE " + table_name + " SET id=?, name=?, imagePath=?, description=?, enabled=? WHERE id=?";

    public CategoriaDao() {
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
            Logger logger = Logger.getLogger(CategoriaDao.class.getName());
            logger.info("Ejecutando SQL: " + selectbyid + " | Parametros: [id=" + id + "]");
            return cat;
        } catch (final SQLException ex) {
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return cat;
    }

    public Categoria findByName(final String name) {
        Categoria sp = null;
        try {
            final PreparedStatement pst = conn.getConnection().prepareStatement(findbyname);
            pst.setString(1, name);
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                sp = registerToObject(rs);
            }
            pst.close();
            Logger logger = Logger.getLogger(CategoriaDao.class.getName());
            logger.info("Ejecutando SQL: " + findbyname + " | Parametros: [name=" + name + "]");

            return sp;
        } catch (final SQLException ex) {
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return sp;
    }

    @Override
    public List<Categoria> getAll() {
        final ArrayList<Categoria> listCat = new ArrayList<>();
        Categoria tempo;
        PreparedStatement pst = null;
        try {
            try {
                pst = conn.getConnection().prepareStatement(selectall);
            }catch (SQLException ex){
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE,
                    null, ex);
            }
            assert pst != null;
            final ResultSet res = pst.executeQuery();
            while(res.next()){
                tempo = registerToObject(res);
                listCat.add(tempo);
            }
            pst.close();
            Logger logger = Logger.getLogger(CategoriaDao.class.getName());
            logger.info("Ejecutando SQL: " + selectall + " | Parametros: ");
        } catch (final SQLException ex) {
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return  listCat;
    }

    @Override
    public void update(final Categoria item) {
        try{
            final PreparedStatement pst = conn.getConnection().prepareStatement(update);
            pst.setString(1,item.getId());
            pst.setString(2,item.getDescription());
            pst.setString(3,item.getName());
            pst.setString(4,item.getImagePath());
            pst.setString(5,item.getDescription());
            pst.setBoolean(6,item.getEnabled());
            pst.executeUpdate();
            pst.close();
            Logger logger = Logger.getLogger(CategoriaDao.class.getName());
            logger.info(() ->
                    "Ejecutando SQL: " + update +
                            " | Params: [1]=" + item.getId() +
                            ", [2]=" + item.getName()+
                            ", [3]=" + item.getImagePath()+
                            ", [4]=" + item.getDescription()+
                            ", [5]=" + item.getEnabled()+
                            "]"
            );
        }catch(final SQLException ex){
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    @Override
    public void delete(final Categoria item) {
        try {
            final PreparedStatement pst =
                    conn.getConnection().prepareStatement(deletebyid);
            pst.setString(1, item.getId());
            pst.executeUpdate();
            pst.close();
            Logger logger = Logger.getLogger(CategoriaDao.class.getName());
            logger.info("Ejecutando SQL: " + deletebyid + " | Parametros: [id=" + item.getId() + "]");

        } catch (final SQLException ex) {
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    @Override
    public void insert(final Categoria item) {
        final PreparedStatement pst;
        try {
            pst = conn.getConnection().prepareStatement(insert,
                    Statement.RETURN_GENERATED_KEYS);
            pst.setString(1,item.getId());
            pst.setString(2,item.getDescription());
            pst.setString(3,item.getName());
            pst.setString(4,item.getImagePath());
            pst.setString(5,item.getDescription());
            pst.setBoolean(6,item.getEnabled());

            pst.executeUpdate();
            pst.close();
            Logger logger = Logger.getLogger(CategoriaDao.class.getName());
            logger.info(() ->
                    "Ejecutando SQL: " + insert +
                            " | Params: [1]=" + item.getId() +
                            ", [2]=" + item.getName()+
                            ", [3]=" + item.getImagePath()+
                            ", [4]=" + item.getDescription()+
                            ", [5]=" + item.getEnabled()+
                            "]"
            );

        } catch (final SQLException ex) {
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    private Categoria registerToObject(final ResultSet rs) {
        Categoria cat = null;
        try{
            cat=new Categoria(
                    rs.getString("ID"),
                    rs.getString("NAME"),
                    rs.getString("IMAGE_PATH"),
                    rs.getString("DESCRIPTION"),
                    rs.getBoolean("ENABLED")
            );
            return cat;
        }catch(final SQLException ex){
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return cat;
    }
}