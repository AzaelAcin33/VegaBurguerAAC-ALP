package ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.IDao;

import java.util.List;

public class CategoriaDAO implements IDao<Categoria> {
    @Override
    public Categoria getById(String id) {
        return null;
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
}
