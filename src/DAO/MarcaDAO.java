package DAO;

import Clases.Beneficio;
import Clases.Marca;
import Conexion.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarcaDAO implements DAO<Marca> {

    private ConexionDB miConexion;
    private Connection c;

    public MarcaDAO() {
        miConexion = ConexionDB.instanciar();
        c = miConexion.conectar();
    }

    @Override
    public int insertar(Marca o) {
        int r = 0;
        List<Marca> lista = listar();
        if (!lista.contains(o)) {
            c = miConexion.conectar();
            if (c != null) {
                try {
                    String consultaSQL = "INSERT INTO Marca (CodMarca,Nombre) VALUES (?,?)";
                    PreparedStatement ps = c.prepareStatement(consultaSQL);
                    ps.setInt(1, o.getCodMarca());
                    ps.setString(2, o.getNombre());

                    r = ps.executeUpdate();

                    ps.close();
                    return r;
                } catch (SQLException ex) {
                    Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        c.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return r;
    }

    @Override
    public List<Marca> consultaFiltrada(int codigo, String nombre) {
        List<Marca> lista = null;

        if (c != null) {

            try {
                PreparedStatement ps = c.prepareStatement("SELECT CodMarca,Nombre FROM Marca WHERE CodMarca=?");
                ps.setInt(1, codigo);

                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    Marca b = new Marca(rs.getInt("CodMarca"), rs.getString("Nombre"));
                    lista.add(b);
                }

                ps.close();
                rs.close();
                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return lista;
    }

    @Override
    public int modificar(int codigo, Marca o) {
        int r = 0;
        if (c != null) {
            try {
                String consultaSQL = "UPDATE Marca SET Nombre=? WHERE CodMarca=?";
                PreparedStatement ps = c.prepareStatement(consultaSQL);
                ps.setInt(2, codigo);
                ps.setString(1, o.getNombre());

                r = ps.executeUpdate();

                ps.close();
                return r;
            } catch (SQLException ex) {
                Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return r;
    }

    @Override
    public List<Marca> listar() {
        List<Marca> lista = null;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("SELECT * FROM Marca");
                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    Marca m = new Marca();
                    m.setCodMarca(rs.getInt("CodMarca"));
                    m.setNombre(rs.getString("Nombre"));
                    lista.add(m);
                }

                ps.close();
                rs.close();

                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
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
                PreparedStatement ps = c.prepareStatement("DELETE FROM Marca WHERE CodMarca=?");
                ps.setInt(1, codigo);

                r = ps.executeUpdate();
                ps.close();
                return r;

            } catch (SQLException ex) {
                Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return r;
    }

}
