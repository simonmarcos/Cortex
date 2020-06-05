package DAO;

import Clases.Foto;
import Conexion.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FotoDAO implements DAO<Foto> {

    private ConexionDB miConexion;
    private Connection c;

    public FotoDAO() {
        miConexion = ConexionDB.instanciar();
        c = miConexion.conectar();
    }

    @Override
    public int insertar(Foto o) {
        int r = 0;

        List<Foto> lista = listar();
        c = ConexionDB.instanciar().conectar();

        if (!lista.contains(o)) {
            if (c != null && lista.size() < 3) {
                try {
                    PreparedStatement ps = c.prepareStatement("INSERT INTO Foto (CodFoto,CodBockFoto) VALUES (?,?)");
                    ps.setInt(1, o.getCodFoto());
                    ps.setInt(2, o.getCodBockFoto());

                    r = ps.executeUpdate();

                    ps.close();
                    return r;

                } catch (SQLException ex) {
                    Logger.getLogger(FotoDAO.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        c.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(FotoDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return r;
    }

    @Override
    public List<Foto> consultaFiltrada(int codigo, String nombre) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int modificar(int codigo, Foto o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Foto> listar() {
        List<Foto> lista = null;

        if (c != null) {

            try {
                PreparedStatement ps = c.prepareStatement("SELECT * FROM Foto");
                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new Foto(rs.getInt("CodFoto"), rs.getInt("CodBockFoto")));
                }

                ps.close();
                rs.close();
                return lista;
            } catch (SQLException ex) {
                Logger.getLogger(FotoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return lista;
    }

    @Override
    public int eliminar(int codigo) {
        int r = 0;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("DELETE FROM `foto` WHERE `CodFoto` = ?");
                System.out.println("DELETE FROM `foto` WHERE `CodFoto` = " + codigo);
                ps.setInt(1, codigo);

                r = ps.executeUpdate();
                System.out.println(r);
                ps.close();
                return r;

            } catch (SQLException ex) {
                Logger.getLogger(FotoDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FotoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return r;
    }

}
