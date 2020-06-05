package DAO;

import java.util.List;

public interface DAO<Object> {

    public int insertar(Object o);

    public List<Object> consultaFiltrada(int codigo, String nombre);

    public int modificar(int codigo, Object o);

    public List<Object> listar();

    public int eliminar(int codigo);
}
