package DAO;

import Clases.Orientacion;
import Conexion.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrientacionDAO implements DAO<Orientacion> {

    private ConexionDB miConexion;
    private Connection c;

    public OrientacionDAO() {
        miConexion = ConexionDB.instanciar();
        c = miConexion.conectar();
    }

    @Override
    public int insertar(Orientacion o) {
        List<Orientacion> lista = listar();
        int r = 0;

        if (!lista.contains(o)) {
            c = ConexionDB.instanciar().conectar();
            if (c != null) {

                try {
                    PreparedStatement ps = c.prepareStatement("INSERT INTO Orientacion (CodOrientacion,Nombre) VALUES (?,?)");
                    ps.setInt(1, o.getCodOrientacion());
                    ps.setString(2, o.getNombre());

                    r = ps.executeUpdate();

                    ps.close();
                    return r;
                } catch (SQLException ex) {
                    Logger.getLogger(OrientacionDAO.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        c.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(OrientacionDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } else {
            r = 4;
        }

        return r;
    }

    @Override
    public List<Orientacion> consultaFiltrada(int codigo, String nombre) {
        List<Orientacion> lista = null;

        if (c != null) {

            try {
                PreparedStatement ps = c.prepareStatement("SELECT CodOrientacion,Nombre FROM Orientacion WHERE CodOrientacion=?");
                ps.setInt(1, codigo);

                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    Orientacion b = new Orientacion(rs.getInt("CodOrientacion"), rs.getString("Nombre"));
                    lista.add(b);
                }

                ps.close();
                rs.close();
                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(OrientacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(OrientacionDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return lista;
    }

    @Override
    public int modificar(int codigo, Orientacion o) {
        int r = 0;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("UPDATE Orientacion SET Nombre=? WHERE CodOrientacion=?");
                ps.setString(1, o.getNombre());
                ps.setInt(2, codigo);

                r = ps.executeUpdate();

                ps.close();
                return r;

            } catch (SQLException ex) {
                Logger.getLogger(OrientacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return r;
    }

    @Override
    public List<Orientacion> listar() {
        List<Orientacion> lista = null;

        if (c != null) {

            try {
                PreparedStatement ps = c.prepareStatement("SELECT CodOrientacion,Nombre FROM Orientacion");
                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    Orientacion b = new Orientacion(rs.getInt("CodOrientacion"), rs.getString("Nombre"));
                    lista.add(b);
                }

                ps.close();
                rs.close();
                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(OrientacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(OrientacionDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return lista;
    }

    @Override
    public int eliminar(int codigo) {
        int r = 0;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("DELETE FROM Orientacion WHERE CodOrientacion=?");
                ps.setInt(1, codigo);

                r = ps.executeUpdate();
                ps.close();
                return r;

            } catch (SQLException ex) {
                Logger.getLogger(OrientacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(OrientacionDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return r;
    }

}
