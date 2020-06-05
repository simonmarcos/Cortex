package DAO;

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

public class PromotoraDAO implements DAO<Promotora> {

    private ConexionDB miConexion;
    private Connection c;

    public PromotoraDAO() {
        miConexion = ConexionDB.instanciar();
        c = miConexion.conectar();
    }

    public int registrar(Promotora o, int estado) {
        List<Promotora> lista = listar();
        int r = 0;
        if (!lista.contains(o)) {
            c = miConexion.conectar();
            if (c != null) {

                String consultaSQL = "";
                PreparedStatement ps = null;

                try {
                    if (estado == 1) {
                        consultaSQL = "INSERT INTO Promotora (DNI,Nombre,Apellido,FechaNacimiento,Edad,Domicilio,Telefono"
                                + ",Facebook,Instagram,CodOrientacion,Cabello,Ojos,Pechos,Cintura,Cola,Altura,Calzado,CodBeneficio,"
                                + "CodMarca,FechaRegistracion,Estado,Movilidad) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        ps = c.prepareStatement(consultaSQL);
                        ps.setInt(1, o.getDNI());
                        ps.setString(2, o.getNombre());
                        ps.setString(3, o.getApellido());
                        ps.setDate(4, o.getFechaNacimiento());
                        ps.setInt(5, o.getEdad());
                        ps.setString(6, o.getDomicilio());
                        ps.setString(7, o.getTelefono());
                        ps.setString(8, o.getFacebook());
                        ps.setString(9, o.getInstagram());
                        ps.setInt(10, o.getOrientacion());
                        ps.setString(11, o.getCabello());
                        ps.setString(12, o.getOjos());
                        ps.setInt(13, o.getPechos());
                        ps.setInt(14, o.getCintura());
                        ps.setInt(15, o.getCola());
                        ps.setInt(16, o.getAltura());
                        ps.setInt(17, o.getCalzado());
                        ps.setInt(18, o.getCodBeneficio());
                        ps.setInt(19, o.getCodMarca());
                        ps.setString(20, o.getFechaRegistracion());
                        ps.setString(21, o.getEstado());
                        ps.setString(22, o.getMovilidad());

                    } else if (estado == 2) {
                        consultaSQL = "INSERT INTO Promotora (DNI,Nombre,Apellido,FechaNacimiento,Edad,Domicilio,Telefono"
                                + ",Facebook,Instagram,Cabello,Ojos,Pechos,Cintura,Cola,Altura,Calzado,FechaRegistracion) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                        ps = c.prepareStatement(consultaSQL);
                        ps.setInt(1, o.getDNI());
                        ps.setString(2, o.getNombre());
                        ps.setString(3, o.getApellido());
                        ps.setDate(4, o.getFechaNacimiento());
                        ps.setInt(5, o.getEdad());
                        ps.setString(6, o.getDomicilio());
                        ps.setString(7, o.getTelefono());
                        ps.setString(8, o.getFacebook());
                        ps.setString(9, o.getInstagram());
                        ps.setString(10, o.getCabello());
                        ps.setString(11, o.getOjos());
                        ps.setInt(12, o.getPechos());
                        ps.setInt(13, o.getCintura());
                        ps.setInt(14, o.getCola());
                        ps.setInt(15, o.getAltura());
                        ps.setInt(16, o.getCalzado());
                        ps.setString(17, o.getFechaRegistracion());
                    }

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
        } else {
            return 4;
        }

        return r;
    }

    @Override
    public int insertar(Promotora o) {
        return 0;
    }

    public List<Promotora> consultaLike(int DNI, String nombre, String apellido) {

        List<Promotora> lista = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String consultaSQl = "";

        if (!nombre.equals("") && apellido.equals("")) {
            consultaSQl = "SELECT Nombre,Apellido,DNI,Telefono,Domicilio,Instagram,Facebook,Edad,FechaNacimiento,CodOrientacion,Estado FROM Promotora WHERE Nombre LIKE'" + nombre + "%'";
        } else if (nombre.equals("") && !apellido.equals("")) {
            consultaSQl = "SELECT Nombre,Apellido,DNI,Telefono,Domicilio,Instagram,Facebook,Edad,FechaNacimiento,CodOrientacion,Estado FROM Promotora WHERE Apellido LIKE'" + apellido + "%'";
        }
        try {
            ps = c.prepareStatement(consultaSQl);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            while (rs.next()) {
                Promotora p = new Promotora();
                p.setDNI(rs.getInt("DNI"));
                p.setNombre(rs.getString("Nombre"));
                p.setApellido(rs.getString("Apellido"));
                p.setFechaNacimiento(rs.getDate("FechaNacimiento"));
                p.setEdad(rs.getInt("Edad"));
                p.setDomicilio(rs.getString("Domicilio"));
                p.setTelefono(rs.getString("Telefono"));
                p.setFacebook(rs.getString("Facebook"));
                p.setInstagram(rs.getString("Instagram"));
                p.setOrientacion(rs.getInt("CodOrientacion"));
                p.setEstado(rs.getString("Estado"));

                lista.add(p);
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

        return lista;
    }

    public List<Promotora> consultaEstado(String estado) {

        List<Promotora> lista = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String consultaSQl = "";

        try {
            consultaSQl = "SELECT Nombre,Apellido,DNI,Telefono,Domicilio,Instagram,Facebook,Edad,FechaNacimiento,CodOrientacion,Estado FROM Promotora WHERE Estado=?";
            ps = c.prepareStatement(consultaSQl);
            ps.setString(1, estado);
        } catch (SQLException ex) {
            Logger.getLogger(PromotoraDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            rs = ps.executeQuery();
            lista = new ArrayList<>();

            while (rs.next()) {
                Promotora p = new Promotora();
                p.setDNI(rs.getInt("DNI"));
                p.setNombre(rs.getString("Nombre"));
                p.setApellido(rs.getString("Apellido"));
                p.setFechaNacimiento(rs.getDate("FechaNacimiento"));
                p.setEdad(rs.getInt("Edad"));
                p.setDomicilio(rs.getString("Domicilio"));
                p.setTelefono(rs.getString("Telefono"));
                p.setFacebook(rs.getString("Facebook"));
                p.setInstagram(rs.getString("Instagram"));
                p.setOrientacion(rs.getInt("CodOrientacion"));
                p.setEstado(rs.getString("Estado"));

                lista.add(p);
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

        return lista;

    }

    public List<Promotora> consultaOrientacion(int orientacion) {

        List<Promotora> lista = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String consultaSQl = "";

        try {
            consultaSQl = "SELECT Promotora.Nombre,Promotora.Apellido,Promotora.DNI,Promotora.Telefono,Promotora.Domicilio,Promotora.Instagram,Promotora.Facebook,Promotora.Edad,Promotora.FechaNacimiento,Promotora.Cabello,Promotora.Ojos,Promotora.Pechos,Promotora.Cintura,Promotora.Cola,Promotora.Altura FROM Promotora INNER JOIN Orientacion ON Promotora.`CodOrientacion`=`orientacion`.`CodOrientacion` WHERE Promotora.`CodOrientacion`=?";
            ps = c.prepareStatement(consultaSQl);
            ps.setInt(1, orientacion);

            rs = ps.executeQuery();
            lista = new ArrayList<>();

            while (rs.next()) {
                Promotora p = new Promotora();
                p.setDNI(rs.getInt("DNI"));
                p.setNombre(rs.getString("Nombre"));
                p.setApellido(rs.getString("Apellido"));
                p.setFechaNacimiento(rs.getDate("FechaNacimiento"));
                p.setEdad(rs.getInt("Edad"));
                p.setDomicilio(rs.getString("Domicilio"));
                p.setTelefono(rs.getString("Telefono"));
                p.setFacebook(rs.getString("Facebook"));
                p.setInstagram(rs.getString("Instagram"));
                p.setCabello(rs.getString("Cabello"));
                p.setOjos(rs.getString("Ojos"));
                p.setAltura(rs.getInt("Altura"));
                p.setPechos(rs.getInt("Pechos"));
                p.setCintura(rs.getInt("Cintura"));
                p.setCola(rs.getInt("Cola"));

                lista.add(p);
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
        return lista;
    }

    public List<Promotora> consultaApariencia(String cabello, String ojos, int codOrientacion) {

        List<Promotora> lista = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String consultaSQl = "";

        try {

            if (!cabello.equals("") && !ojos.equals("") && codOrientacion == 0) {
                consultaSQl = "SELECT Nombre,Apellido,DNI,Telefono,Domicilio,Instagram,Facebook,Edad,FechaNacimiento,Cabello,Ojos,Pechos,Cintura,Cola,Altura FROM Promotora WHERE Cabello = ? AND Ojos = ?";
                ps = c.prepareStatement(consultaSQl);
                ps.setString(1, cabello);
                ps.setString(2, ojos);
            } else if (!cabello.equals("") && ojos.equals("") && codOrientacion == 0) {
                consultaSQl = "SELECT Nombre,Apellido,DNI,Telefono,Domicilio,Instagram,Facebook,Edad,FechaNacimiento,Cabello,Ojos,Pechos,Cintura,Cola,Altura FROM Promotora WHERE Cabello = ?";
                ps = c.prepareStatement(consultaSQl);
                ps.setString(1, cabello);
            } else if (cabello.equals("") && !ojos.equals("") && codOrientacion == 0) {
                consultaSQl = "SELECT Nombre,Apellido,DNI,Telefono,Domicilio,Instagram,Facebook,Edad,FechaNacimiento,Cabello,Ojos,Pechos,Cintura,Cola,Altura FROM Promotora WHERE Ojos = ?";
                ps = c.prepareStatement(consultaSQl);
                ps.setString(1, ojos);
            } else if (!cabello.equals("") && !ojos.equals("") && codOrientacion != 0) {
                consultaSQl = "SELECT Nombre,Apellido,DNI,Telefono,Domicilio,Instagram,Facebook,Edad,FechaNacimiento,Cabello,Ojos,Pechos,Cintura,Cola,Altura FROM Promotora WHERE Cabello = ? AND Ojos = ? AND CodOrientacion=?";
                ps = c.prepareStatement(consultaSQl);
                ps.setString(1, cabello);
                ps.setString(2, ojos);
                ps.setInt(3, codOrientacion);
            } else if (cabello.equals("") && ojos.equals("") && codOrientacion != 0) {
                consultaSQl = "SELECT Nombre,Apellido,DNI,Telefono,Domicilio,Instagram,Facebook,Edad,FechaNacimiento,Cabello,Ojos,Pechos,Cintura,Cola,Altura FROM Promotora WHERE CodOrientacion=?";
                ps = c.prepareStatement(consultaSQl);
                ps.setInt(1, codOrientacion);
            } else if (!cabello.equals("") && ojos.equals("") && codOrientacion != 0) {
                consultaSQl = "SELECT Nombre,Apellido,DNI,Telefono,Domicilio,Instagram,Facebook,Edad,FechaNacimiento,Cabello,Ojos,Pechos,Cintura,Cola,Altura FROM Promotora WHERE Cabello = ? AND CodOrientacion=?";
                ps = c.prepareStatement(consultaSQl);
                ps.setString(1, cabello);
                ps.setInt(2, codOrientacion);
            } else if (cabello.equals("") && !ojos.equals("") && codOrientacion != 0) {
                consultaSQl = "SELECT Nombre,Apellido,DNI,Telefono,Domicilio,Instagram,Facebook,Edad,FechaNacimiento,Cabello,Ojos,Pechos,Cintura,Cola,Altura FROM Promotora WHERE Ojos = ? AND CodOrientacion=?";
                ps = c.prepareStatement(consultaSQl);
                ps.setString(1, ojos);
                ps.setInt(2, codOrientacion);
            }

            rs = ps.executeQuery();
            lista = new ArrayList<>();

            while (rs.next()) {
                Promotora p = new Promotora();
                p.setDNI(rs.getInt("DNI"));
                p.setNombre(rs.getString("Nombre"));
                p.setApellido(rs.getString("Apellido"));
                p.setFechaNacimiento(rs.getDate("FechaNacimiento"));
                p.setEdad(rs.getInt("Edad"));
                p.setDomicilio(rs.getString("Domicilio"));
                p.setTelefono(rs.getString("Telefono"));
                p.setFacebook(rs.getString("Facebook"));
                p.setInstagram(rs.getString("Instagram"));
                p.setCabello(rs.getString("Cabello"));
                p.setOjos(rs.getString("Ojos"));
                p.setAltura(rs.getInt("Altura"));
                p.setPechos(rs.getInt("Pechos"));
                p.setCintura(rs.getInt("Cintura"));
                p.setCola(rs.getInt("Cola"));

                lista.add(p);
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
        return lista;
    }

    @Override
    public List<Promotora> consultaFiltrada(int codigo, String nombre) {

        List<Promotora> lista = null;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("SELECT * FROM Promotora WHERE DNI=?");
                ps.setInt(1, codigo);
                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    Promotora p = new Promotora();
                    p.setCodPromotor(rs.getInt("CodPromotor"));
                    p.setDNI(rs.getInt("DNI"));
                    p.setNombre(rs.getString("Nombre"));
                    p.setApellido(rs.getString("Apellido"));
                    p.setFechaNacimiento(rs.getDate("FechaNacimiento"));
                    p.setEdad(rs.getInt("Edad"));
                    p.setDomicilio(rs.getString("Domicilio"));
                    p.setTelefono(rs.getString("Telefono"));
                    p.setFacebook(rs.getString("Facebook"));
                    p.setInstagram(rs.getString("Instagram"));
                    p.setOrientacion(rs.getInt("CodOrientacion"));
                    p.setCodBeneficio(rs.getInt("CodBeneficio"));
                    p.setCodMarca(rs.getInt("CodMarca"));
                    p.setCabello(rs.getString("Cabello"));
                    p.setOjos(rs.getString("Ojos"));
                    p.setPechos(rs.getInt("Pechos"));
                    p.setCintura(rs.getInt("Cintura"));
                    p.setCola(rs.getInt("Cola"));
                    p.setAltura(rs.getInt("Altura"));
                    p.setCalzado(rs.getInt("Calzado"));
                    p.setFechaRegistracion(rs.getString("FechaRegistracion"));
                    p.setMovilidad(rs.getString("Movilidad"));

                    lista.add(p);
                }

                ps.close();
                rs.close();

                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(PromotoraDAO.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();

                } catch (SQLException ex) {
                    Logger.getLogger(PromotoraDAO.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return lista;
    }

    public List<Promotora> consultaNombres(String apellido, String nombre) {
        List<Promotora> lista = null;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("SELECT DNI FROM Promotora WHERE Nombre=? && Apellido=?");
                ps.setString(1, nombre);
                ps.setString(2, apellido);

                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    Promotora p = new Promotora();
                    p.setDNI(rs.getInt("DNI"));

                    lista.add(p);
                }

                ps.close();
                rs.close();

                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(PromotoraDAO.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();

                } catch (SQLException ex) {
                    Logger.getLogger(PromotoraDAO.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return lista;
    }

    @Override
    public int modificar(int codigo, Promotora o) {

        int r = 0;
        if (c != null) {
            try {
                String consultaSQL = "UPDATE Promotora SET DNI=?,Nombre=?,Apellido=?,FechaNacimiento=?,Edad=?,Telefono=?,Facebook=?,Instagram=?,CodOrientacion=?"
                        + ",Cabello=?,Ojos=?,Pechos=?,Cintura=?,Cola=?,Altura=?,Calzado=?,CodBeneficio=?"
                        + ",CodMarca=?,Domicilio=?,Estado=?,Movilidad=? WHERE DNI=?";
                PreparedStatement ps = c.prepareStatement(consultaSQL);
                ps.setInt(1, o.getDNI());
                ps.setString(2, o.getNombre());
                ps.setString(3, o.getApellido());
                ps.setDate(4, o.getFechaNacimiento());
                ps.setInt(5, o.getEdad());
                ps.setString(6, o.getTelefono());
                ps.setString(7, o.getFacebook());
                ps.setString(8, o.getInstagram());
                ps.setInt(9, o.getOrientacion());
                ps.setString(10, o.getCabello());
                ps.setString(11, o.getOjos());
                ps.setInt(12, o.getPechos());
                ps.setInt(13, o.getCintura());
                ps.setInt(14, o.getCola());
                ps.setInt(15, o.getAltura());
                ps.setInt(16, o.getCalzado());
                ps.setInt(17, o.getCodBeneficio());
                ps.setInt(18, o.getCodMarca());
                ps.setString(19, o.getDomicilio());
                ps.setString(20, o.getEstado());
                ps.setString(21, o.getMovilidad());
                ps.setInt(22, codigo);

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

    @Override
    public List<Promotora> listar() {

        List<Promotora> lista = null;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("SELECT * FROM Promotora");
                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    Promotora p = new Promotora();
                    p.setCodPromotor(rs.getInt("CodPromotor"));
                    p.setDNI(rs.getInt("DNI"));
                    p.setNombre(rs.getString("Nombre"));
                    p.setApellido(rs.getString("Apellido"));
                    p.setFechaNacimiento(rs.getDate("FechaNacimiento"));
                    p.setEdad(rs.getInt("Edad"));
                    p.setDomicilio(rs.getString("Domicilio"));
                    p.setTelefono(rs.getString("Telefono"));
                    p.setFacebook(rs.getString("Facebook"));
                    p.setInstagram(rs.getString("Instagram"));
                    p.setOrientacion(rs.getInt("CodOrientacion"));
                    p.setCodBeneficio(rs.getInt("CodBeneficio"));
                    p.setCodMarca(rs.getInt("CodMarca"));
                    p.setCabello(rs.getString("Cabello"));
                    p.setOjos(rs.getString("Ojos"));
                    p.setPechos(rs.getInt("Pechos"));
                    p.setCintura(rs.getInt("Cintura"));
                    p.setCola(rs.getInt("Cola"));
                    p.setAltura(rs.getInt("Altura"));
                    p.setCalzado(rs.getInt("Calzado"));
                    p.setEstado(rs.getString("Estado"));
                    p.setFechaRegistracion(rs.getString("FechaRegistracion"));
                    p.setMovilidad(rs.getString("Movilidad"));
                    //20 12607448 5
                    lista.add(p);
                }

                ps.close();
                rs.close();

                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(PromotoraDAO.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();

                } catch (SQLException ex) {
                    Logger.getLogger(PromotoraDAO.class
                            .getName()).log(Level.SEVERE, null, ex);
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
                PreparedStatement ps = c.prepareStatement("DELETE FROM Promotora WHERE DNI=?");
                ps.setInt(1, codigo);

                r = ps.executeUpdate();
                ps.close();
                return r;

            } catch (SQLException ex) {
                Logger.getLogger(PromotoraDAO.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();

                } catch (SQLException ex) {
                    Logger.getLogger(PromotoraDAO.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return r;
    }

    //Metodo en el cual eliminaremos el book de fotos y el promotor
    public int eliminarBookFotoYPromotor(int dni) {
        int r = 0;
        if (c != null) {
            try {
                c.setAutoCommit(false);
                PreparedStatement psBook = c.prepareStatement("DELETE FROM bockfoto WHERE DNI=?");
                psBook.setInt(1, dni);
                psBook.executeUpdate();

                PreparedStatement psPromotor = c.prepareStatement("DELETE FROM promotora WHERE DNI=?");
                psPromotor.setInt(1, dni);
                psPromotor.executeUpdate();

                c.commit();
                return 1;

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(BockFotoDAO.class.getName()).log(Level.SEVERE, null, ex);
                    c.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(BockFotoDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    c.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BockFotoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return r;
    }

    public int buscarCodPromotor(int DNI) {
        int codPromotor = 0;
        if (c != null) {
            try {
                PreparedStatement ps = c.prepareStatement("SELECT CodPromotor FROM Promotora WHERE DNI=?");
                ps.setInt(1, DNI);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    codPromotor = rs.getInt("CodPromotor");
                }

                ps.close();
                rs.close();
                return codPromotor;

            } catch (SQLException ex) {
                Logger.getLogger(PromotoraDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return codPromotor;
    }

    public List<Promotora> listarPromotoresBook() {
        List<Promotora> lista = null;
        if (c != null) {
            try {
                String consultaSQL = "SELECT Promotora.`Nombre`,Promotora.`Apellido`,Promotora.`CodPromotor` FROM Promotora INNER JOIN BockFoto ON `promotora`.`CodPromotor`=`bockfoto`.`CodPromotor`";

                PreparedStatement ps = c.prepareStatement(consultaSQL);
                ResultSet rs = ps.executeQuery();

                lista = new ArrayList<>();
                while (rs.next()) {
                    Promotora p = new Promotora();
                    p.setCodPromotor(rs.getInt("CodPromotor"));
                    p.setNombre(rs.getString("Nombre"));
                    p.setApellido(rs.getString("Apellido"));

                    lista.add(p);
                }

                ps.close();
                rs.close();

                return lista;

            } catch (SQLException ex) {
                Logger.getLogger(PromotoraDAO.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    c.close();

                } catch (SQLException ex) {
                    Logger.getLogger(PromotoraDAO.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return lista;
    }

}
