package ies.sequeros.com.dam.pmdm.administrador.infraestructura.lineaPedido;

import java.sql.SQLException;
import java.util.List;

import ies.sequeros.com.dam.pmdm.administrador.infraestructura.lineaPedido.LineaPedidoDao;
import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
public class BBDDRepositorioLineaPedidoJava {
    private final DataBaseConnection db;
    private LineaPedidoDao dao;
    public BBDDRepositorioLineaPedidoJava(DataBaseConnection connection) throws Exception { //  (String path)

        super();
        this.db = connection;
        dao = new LineaPedidoDao();
        dao.setConn(this.db);


    }
    public void add(LineaPedido item){
        this.dao.insert(item);
    }
    public boolean remove(LineaPedido item){
        this.dao.delete(item);
        return true;
    }
    public boolean remove(String id){
        var item=this.dao.getById(id);
        if(item!=null){
            this.remove(item);
            return true;
        }
        return false;
    }
    public boolean  update(LineaPedido item){
        this.dao.update(item);
        return true;
    }
    public List<LineaPedido> getAll() {
        return this.dao.getAll();
    }
    public LineaPedido findByName(String name){

        return null;
    }
    public LineaPedido  getById(String id){
        return this.dao.getById(id);

    }

    public List<LineaPedido> findByIds(List<String> ids){
        return null;
    }
    public void close(){
        try {
            this.db.close();
            //no hace caso de esta excepción
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
