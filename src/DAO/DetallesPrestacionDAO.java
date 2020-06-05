package DAO;

import Clases.DetallesPrestacion;
import Conexion.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DetallesPrestacionDAO implements DAO<DetallesPrestacion> {

    private ConexionDB miConexion;
    private Connection c;

    public DetallesPrestacionDAO() {
        miConexion = ConexionDB.instanciar();
        c = miConexion.conectar();
    }

    @Override
    public int insertar(DetallesPrestacion o) {
        List<DetallesPrestacion> lista = listar();
        int r = 0;

        if (!lista.contains(o)) {
            c = ConexionDB.instanciar().conectar();
            if (c != null) {
                try {
                    PreparedStatement ps = c.prepareStatement("INSERT INTO DetallesPrestacion (ObraSocial,Monotributista,F931,ContratoFirmado,CodPromotor) VALUES (?,?,?,?,?)");
                    ps.setString(1, o.getObraSocial());
                    ps.setString(2, o.getMonotributista());
                    ps.setString(3, o.getF931());
                    ps.setInt(4, o.getContratoFirmado());
                    ps.setInt(5, o.getCodPromotor());

                    r = ps.executeUpdate();

                    ps.close();
                    return r;
                } catch (SQLException ex) {
                    Logger.getLogger(DetallesPrestacionDAO.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        c.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DetallesPrestacionDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } else {
            r = 4;
        }

        return r;
    }

    @Override
    public List<DetallesPrestacion> consultaFiltrada(int codigo, String nombre) {

        List<DetallesPrestacion> lista = null;

        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("SELECT Promotora.`CodPromotor`, `DetallesPrestacion`.`ObraSocial`,`DetallesPrestacion`.`Monotributista`,`DetallesPrestacion`.`F931`,`DetallesPrestacion`.`ContratoFirmado`,`DetallesPrestacion`.`ObraSocial` FROM DetallesPrestacion INNER JOIN Promotora ON `DetallesPrestacion`.`CodPromotor`=Promotora.`CodPromotor` WHERE `detallesprestacion`.`CodPromotor` = ?");
                ps.setInt(1, codigo);

                ResultSet rs = ps.executeQuery();
                lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new DetallesPrestacion(rs.getString("ObraSocial"), rs.getString("Monotributista"), rs.getString("F931"), rs.getInt("ContratoFirmado")));
                }

                ps.close();
                rs.close();
                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(DetallesPrestacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return lista;
    }

    @Override
    public int modificar(int codigo, DetallesPrestacion o) {
        int r = 0;
        if (c != null) {
            try {
                String consultaSQL = "UPDATE detallesprestacion SET ObraSocial=?,Monotributista=?,F931=?,ContratoFirmado=? WHERE CodPromotor = ?";
                PreparedStatement ps = c.prepareStatement(consultaSQL);
                ps.setString(1, o.getObraSocial());
                ps.setString(2, o.getMonotributista());
                ps.setString(3, o.getF931());
                ps.setInt(4, o.getContratoFirmado());
                ps.setInt(5, codigo);

                r = ps.executeUpdate();

                ps.close();
                return r;

            } catch (SQLException ex) {
                Logger.getLogger(DetallesPrestacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();

                } catch (SQLException ex) {
                    Logger.getLogger(DetallesPrestacionDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return r;
    }

    @Override
    public List<DetallesPrestacion> listar() {
        List<DetallesPrestacion> lista = null;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("SELECT CodPrestacion,ObraSocial,Monotributista,F931,ContratoFirmado,CodPromotor FROM DetallesPrestacion");
                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    DetallesPrestacion p = new DetallesPrestacion();
                    p.setCodPrestacion(rs.getInt("CodPrestacion"));
                    p.setObraSocial(rs.getString("ObraSocial"));
                    p.setMonotributista(rs.getString("Monotributista"));
                    p.setF931(rs.getString("F931"));
                    p.setContratoFirmado(rs.getInt("ContratoFirmado"));
                    p.setCodPromotor(rs.getInt("CodPromotor"));
                    lista.add(p);
                }

                ps.close();
                rs.close();

                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(DetallesPrestacionDAO.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();

                } catch (SQLException ex) {
                    Logger.getLogger(DetallesPrestacionDAO.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return lista;
    }

    @Override
    public int eliminar(int codigo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
