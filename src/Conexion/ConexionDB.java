package Conexion;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConexionDB {

    private static ConexionDB conexion;
    private Connection c;

    private ConexionDB() {
    }

    public static ConexionDB instanciar() {
        if (conexion == null) {
            conexion = new ConexionDB();
        }
        return conexion;
    }

    public Connection conectar() {

        try {
            try {
                deleteFilesXAMPP();
                new ProcessBuilder("C:/xampp/xampp_start.exe").start();
            } catch (IOException ex) {
                Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
            }
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/wally", "root", "");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo obtener información de la base de datos.", "Eror Base de Datos", 0);
            System.exit(0);
        }
        return c;
    }

    public void cerrarConexion() {
        try {
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Eliminamos los archivos que nos impiden poder conectarnos a MYSQL
    private void deleteFilesXAMPP() {
        String folder = "C:/xampp/mysql/data/";
        String listFilesDelete[] = {folder + "aria_log.00000001", folder + "aria_log_control", folder + "ib_logfile0", folder
            + "ib_logfile1", folder + "multi-master.info", folder + "mysql.pid", folder + "mysql_error.log"};

        for (int i = 0; i < listFilesDelete.length; i++) {
            File file = new File(listFilesDelete[i]);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
