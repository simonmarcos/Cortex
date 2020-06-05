package DAO;

import Clases.Promotora;
import Clases.TrabajosRealizados;
import Conexion.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrabajosRealizadosDAO implements DAO<TrabajosRealizados> {

    private ConexionDB miConexion;
    private Connection c;

    public TrabajosRealizadosDAO() {
        miConexion = ConexionDB.instanciar();
        c = miConexion.conectar();
    }

    @Override
    public int insertar(TrabajosRealizados o) {

        int r = 0;
        List<TrabajosRealizados> lista = listar();
        if (!lista.contains(o)) {
            c = miConexion.conectar();
            if (c != null) {
                try {
                    String consultaSQL = "INSERT INTO TrabajosRealizados (CodTrabajo,Fecha,Horas,CodMarca,DNI) VALUES (?,?,?,?,?)";
                    PreparedStatement ps = c.prepareStatement(consultaSQL);
                    ps.setInt(1, o.getCodTrabajo());
                    ps.setDate(2, o.getFecha());
                    ps.setInt(3, o.getHoras());
                    ps.setInt(4, o.getCodMarca());
                    ps.setInt(5, o.getDNI());

                    r = ps.executeUpdate();

                    ps.close();
                    return r;
                } catch (SQLException ex) {
                    Logger.getLogger(TrabajosRealizadosDAO.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        c.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(TrabajosRealizadosDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return r;
    }

    @Override
    public List<TrabajosRealizados> consultaFiltrada(int codigo, String nombre
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int modificar(int codigo, TrabajosRealizados o
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TrabajosRealizados> listar() {

        List<TrabajosRealizados> lista = null;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("SELECT * FROM TrabajosRealizados");
                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    TrabajosRealizados tr = new TrabajosRealizados();
                    tr.setCodTrabajo(rs.getInt("CodTrabajo"));
                    tr.setFecha(rs.getDate("Fecha"));
                    tr.setHoras(rs.getInt("Horas"));
                    tr.setDNI(rs.getInt("DNI"));

                    lista.add(tr);
                }

                ps.close();
                rs.close();

                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(PromotoraDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PromotoraDAO.class.getName()).log(Level.SEVERE, null, ex);
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
                PreparedStatement ps = c.prepareStatement("DELETE FROM TrabajosRealizados WHERE CodTrabajo=?");
                ps.setInt(1, codigo);

                r = ps.executeUpdate();
                ps.close();
                return r;
            } catch (SQLException ex) {
                Logger.getLogger(PromotoraDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PromotoraDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return r;
    }

}
