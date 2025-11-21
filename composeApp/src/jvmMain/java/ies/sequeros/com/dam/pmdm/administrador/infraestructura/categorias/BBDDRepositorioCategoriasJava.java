package ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias;

import java.sql.SQLException;
import java.util.List;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;

public class BBDDRepositorioCategoriasJava {
    private final DataBaseConnection db;
    private CategoriaDao dao;

    public BBDDRepositorioCategoriasJava(DataBaseConnection connection) throws Exception {
        super();
        this.db = connection;
        dao = new CategoriaDao();
        dao.setConn(this.db);
    }

    public void add(Categoria item) {
        this.dao.insert(item);
    }

    public boolean remove(Categoria item) {
        this.dao.delete(item);
        return true;
    }

    public boolean remove(String id) {
        var item = this.dao.getById(id);
        if (item != null) {
            this.remove(item);
            return true;
        }
        return false;
    }

    public boolean update(Categoria item) {
        this.dao.update(item);
        return true;
    }

    public List<Categoria> getAll() {
        return this.dao.getAll();
    }

    public Categoria findByName(String name) {
        return this.dao.findByName(name);
    }

    public Categoria getById(String id) {
        return this.dao.getById(id);
    }

    public void close() {
        try {
            this.db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
