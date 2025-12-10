package ies.sequeros.com.dam.pmdm.administrador.infraestructura.dependientes;

import java.sql.SQLException;
import java.util.List;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Dependiente;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;

public  class BBDDRepositorioDependientesJava {
    private final DataBaseConnection db;
    private DependienteDao dao;
    public BBDDRepositorioDependientesJava(DataBaseConnection connection) throws Exception { //  (String path)
/*
        super();
        this.db = new DataBaseConnection();
        this.db.setConfig_path(path);
        this.db.open();
        dao= new DependienteDao();
        dao.setConn(this.db);
*/
        super();
        this.db = connection;
        dao = new DependienteDao();
        dao.setConn(this.db);


    }
    //Caso para añadir
    public void add(Dependiente item){
        this.dao.insert(item);
    }
    public boolean remove(Dependiente item){
        this.dao.delete(item);
        return true;
    }

    //Caso para borrar
    public boolean remove(String id){
        var item=this.dao.getById(id);
        if(item!=null){
            this.remove(item);
            return true;
        }
        return false;
    }

    //Caso para actualizar
    public boolean  update(Dependiente item){
        this.dao.update(item);
        return true;
    }

    //Caso para obtener todos
    public List<Dependiente> getAll() {
        return this.dao.getAll();
    }

    //Caso para buscar por nombre
    public Dependiente findByName(String name){

        return this.dao.findByName(name);
    }

    //Caso para buscar por id
    public Dependiente  getById(String id){
        return this.dao.getById(id);

    }

    public List<Dependiente> findByIds(List<String> ids){
        return null;
    }

    //Cerrar conexión
    public void close(){
        try {
            this.db.close();
        //no hace caso de esta excepción
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
