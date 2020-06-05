package DAO;

import Clases.Beneficio;
import Conexion.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BeneficioDAO implements DAO<Beneficio> {

    private ConexionDB miConexion;
    private Connection c;

    public BeneficioDAO() {
        miConexion = ConexionDB.instanciar();
        c = miConexion.conectar();
    }

    @Override
    public int insertar(Beneficio o) {

        List<Beneficio> lista = listar();
        int r = 0;

        if (!lista.contains(o)) {
            c = ConexionDB.instanciar().conectar();
            if (c != null) {

                try {
                    PreparedStatement ps = c.prepareStatement("INSERT INTO Beneficio (CodBeneficio,Nombre) VALUES (?,?)");
                    ps.setInt(1, o.getCodBeneficio());
                    ps.setString(2, o.getNombre());

                    r = ps.executeUpdate();

                    ps.close();
                    return r;
                } catch (SQLException ex) {
                    Logger.getLogger(BeneficioDAO.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        c.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(BeneficioDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } else {
            r = 4;
        }

        return r;
    }

    @Override
    public List<Beneficio> consultaFiltrada(int codigo, String nombre) {
        List<Beneficio> lista = null;

        if (c != null) {

            try {
                PreparedStatement ps = c.prepareStatement("SELECT CodBeneficio,Nombre FROM Beneficio WHERE CodBeneficio=?");
                ps.setInt(1, codigo);

                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    Beneficio b = new Beneficio(rs.getInt("CodBeneficio"), rs.getString("Nombre"));
                    lista.add(b);
                }

                ps.close();
                rs.close();
                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(BeneficioDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BeneficioDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return lista;
    }

    @Override
    public int modificar(int codigo, Beneficio o) {

        int r = 0;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("UPDATE Beneficio SET Nombre=? WHERE CodBeneficio=?");
                ps.setString(1, o.getNombre());
                ps.setInt(2, codigo);

                r = ps.executeUpdate();

                ps.close();
                return r;

            } catch (SQLException ex) {
                Logger.getLogger(BeneficioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return r;
    }

    @Override
    public List<Beneficio> listar() {
        List<Beneficio> lista = null;

        if (c != null) {

            try {
                PreparedStatement ps = c.prepareStatement("SELECT CodBeneficio,Nombre FROM Beneficio");
                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    Beneficio b = new Beneficio(rs.getInt("CodBeneficio"), rs.getString("Nombre"));
                    lista.add(b);
                }

                ps.close();
                rs.close();
                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(BeneficioDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BeneficioDAO.class.getName()).log(Level.SEVERE, null, ex);
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
                PreparedStatement ps = c.prepareStatement("DELETE FROM Beneficio WHERE CodBeneficio=?");
                ps.setInt(1, codigo);

                r = ps.executeUpdate();
                ps.close();
                return r;

            } catch (SQLException ex) {
                Logger.getLogger(BeneficioDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BeneficioDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return r;
    }

}
