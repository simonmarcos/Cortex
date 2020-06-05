package DAO;

import Clases.BockFoto;
import Clases.Promotora;
import Conexion.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BockFotoDAO implements DAO {

    private Connection c;

    public BockFotoDAO() {
        c = ConexionDB.instanciar().conectar();
    }

    public int insertar(BockFoto bf) {

        int r = 0;

        List<BockFoto> lista = listar();
        c = ConexionDB.instanciar().conectar();
        if (!lista.contains(bf)) {
            if (c != null) {
                try {
                    PreparedStatement ps = c.prepareStatement("INSERT INTO BockFoto (CodPromotor,CodBockFoto,DNI) VALUES (?,?,?)");
                    ps.setInt(1, bf.getCodPromotor());
                    ps.setInt(2, bf.getCodBockFoto());
                    ps.setInt(3, bf.getDni());

                    try {
                        r = ps.executeUpdate();
                    } catch (Exception e) {
                        return 5;
                    }

                    ps.close();
                    return r;

                } catch (SQLException ex) {
                    Logger.getLogger(BockFotoDAO.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        c.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(BockFotoDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return r;
    }

    public int consultarCodBockFoto(int DNI) {

        int codigo = 0;

        if (c != null) {

            try {
                PreparedStatement ps = c.prepareStatement("SELECT CodBockFoto FROM BockFoto WHERE DNI=?");
                ps.setInt(1, DNI);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    codigo = rs.getInt("CodBockFoto");
                }

                ps.close();
                rs.close();

                return codigo;

            } catch (SQLException ex) {
                Logger.getLogger(BockFotoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return codigo;
    }

    public List<Promotora> consultaINNER() {

        List<Promotora> lista = null;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("SELECT Promotora.`Nombre`,Promotora.`DNI`,Promotora.`Apellido`,Promotora.`CodPromotor` FROM Promotora INNER JOIN BockFoto ON Promotora.`CodPromotor`=`bockfoto`.`CodPromotor`");

                ResultSet rs = ps.executeQuery();
                lista = new ArrayList<>();
                while (rs.next()) {
                    Promotora p = new Promotora();
                    p.setCodPromotor(rs.getInt("CodPromotor"));
                    p.setDNI(rs.getInt("DNI"));
                    p.setNombre(rs.getString("Nombre"));
                    p.setApellido(rs.getString("Apellido"));

                    lista.add(p);
                }

                ps.close();
                rs.close();

                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(BockFotoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return lista;
    }

    public List<BockFoto> listar() {

        List<BockFoto> lista = null;

        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("SELECT CodBockFoto,CodPromotor,DNI FROM BockFoto");
                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new BockFoto(rs.getInt("CodBockFoto"), rs.getInt("CodPromotor"), rs.getInt("DNI")));
                }

                ps.close();
                rs.close();
                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(BockFotoDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BockFotoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return lista;
    }

    public int eliminar(int codigo) {
        int r = 0;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("DELETE FROM BockFoto WHERE CodBockFoto = ?");
                ps.setInt(1, codigo);

                r = ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(BockFotoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return r;
    }

    @Override
    public int insertar(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List consultaFiltrada(int codigo, String nombre) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int modificar(int codigo, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
