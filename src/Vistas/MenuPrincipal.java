package Vistas;

import Clases.Promotora;
import DAO.DAO;
import DAO.PromotoraDAO;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class MenuPrincipal extends javax.swing.JFrame {

    private DefaultTableModel dft = null;
    private int contador = 0;

    public MenuPrincipal() {
        initComponents();
        this.setResizable(false);
        setLocationRelativeTo(null);
        this.setTitle("SANCHEZ LORIA");
        imgFondo();
        this.setFocusable(true);
        buscarCumpleneras();
    }

    private void ajustarPantalla() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        int ancho = (int) d.getWidth();
        int alto = (int) d.getHeight();
        System.out.println(alto);
        this.setSize(ancho, (int) (alto - (alto * 0.05)));
    }

    private void imgFondo() {

        Image imagenUser = new ImageIcon(getClass().getResource("/Img/img.png")).getImage();
        Icon iconoUser = new ImageIcon(imagenUser.getScaledInstance(imgLogo.getWidth(), imgLogo.getHeight(), Image.SCALE_DEFAULT));

        imgLogo.setIcon(iconoUser);
    }

    private void setearTabla() {
        dft = new DefaultTableModel();
        String[] fila = {"Nombre", "Apellido", "Edad", "Teléfono", "Facebook", "Dirección"};
        dft.setColumnIdentifiers(fila);
        tablaCumpleanos.setModel(dft);
    }

    private void buscarCumpleneras() {

        DAO dao = new PromotoraDAO();
        List<Promotora> lista = dao.listar();

        Calendar c = GregorianCalendar.getInstance();
        int mesActual = c.get(Calendar.MONTH) + 1;
        int diaActual = c.get(Calendar.DAY_OF_MONTH);

        if (lista != null) {
            for (Promotora p : lista) {
                String[] fila = new String[6];
                try {
                    LocalDate fecha = new java.sql.Date(p.getFechaNacimiento().getTime()).toLocalDate();

                    int dia = fecha.getDayOfMonth();
                    int mes = fecha.getMonthValue();

                    if (mesActual == mes && diaActual == dia) {
                        //Lo que hacemos con este contador, es que al entrar por esta condicion quiere decir que hay una persona
                        //que cumple años en el dia de hoy, por lo cual vamos a setar la tabla para que se agrege la fila con las campos
                        contador = 1;
                        if (contador == 1) {
                            setearTabla();
                        }
                        fila[1] = p.getApellido();
                        fila[0] = p.getNombre();
                        fila[2] = String.valueOf(p.getEdad());
                        fila[3] = String.valueOf(p.getTelefono());
                        fila[4] = p.getFacebook();
                        fila[5] = p.getDomicilio();

                        dft.addRow(fila);
                    }
                } catch (Exception e) {
                }

            }
        }
    }

    private void lookAndFeel(String name) {
        try {
            UIManager.setLookAndFeel(name);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void backupDB(String namePathBackup) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("C:/xampp/mysql/bin/mysqldump -u root wally -r " + namePathBackup);
            int processOK = process.waitFor();
            if (processOK == 0) {
                JOptionPane.showMessageDialog(this, "La base de datos se guardó correctamente.");
            }

        } catch (Exception ex) {
            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadBackupDB(String namePathLoadBackup) {
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("C:/xampp/mysql/bin/mysql.exe - u root wally  < " + namePathLoadBackup);
            //JOptionPane.showMessageDialog(this, "La base de datos se cargó correctamente.");
        } catch (IOException ex) {
            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendMailMessage() {

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        miLamina = new javax.swing.JPanel();
        imgLogo = new javax.swing.JLabel();
        btnGuardarBaseDatos = new javax.swing.JButton();
        miLamina2 = new javax.swing.JPanel();
        btnPromotora = new javax.swing.JButton();
        btnGaleria = new javax.swing.JButton();
        btnDetallePrestacion = new javax.swing.JButton();
        btnBeneficioMarca = new javax.swing.JButton();
        btnAntecedentes = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCumpleanos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        miLamina.setBackground(new java.awt.Color(255, 255, 255));
        miLamina.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        imgLogo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(0, 102, 51), new java.awt.Color(102, 102, 102), new java.awt.Color(153, 153, 153)));

        btnGuardarBaseDatos.setBackground(new java.awt.Color(0, 153, 51));
        btnGuardarBaseDatos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnGuardarBaseDatos.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarBaseDatos.setText("GUARDAR BASE DE DATOS");
        btnGuardarBaseDatos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGuardarBaseDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarBaseDatosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout miLaminaLayout = new javax.swing.GroupLayout(miLamina);
        miLamina.setLayout(miLaminaLayout);
        miLaminaLayout.setHorizontalGroup(
            miLaminaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, miLaminaLayout.createSequentialGroup()
                .addComponent(imgLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardarBaseDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        miLaminaLayout.setVerticalGroup(
            miLaminaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(miLaminaLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(imgLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(miLaminaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardarBaseDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        miLamina2.setBackground(new java.awt.Color(255, 255, 255));
        miLamina2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnPromotora.setBackground(new java.awt.Color(0, 102, 51));
        btnPromotora.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        btnPromotora.setForeground(new java.awt.Color(255, 255, 255));
        btnPromotora.setText("PROMOTORES");
        btnPromotora.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPromotora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPromotoraActionPerformed(evt);
            }
        });

        btnGaleria.setBackground(new java.awt.Color(0, 102, 51));
        btnGaleria.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        btnGaleria.setForeground(new java.awt.Color(255, 255, 255));
        btnGaleria.setText("GALERIA");
        btnGaleria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGaleria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGaleriaActionPerformed(evt);
            }
        });

        btnDetallePrestacion.setBackground(new java.awt.Color(0, 102, 51));
        btnDetallePrestacion.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        btnDetallePrestacion.setForeground(new java.awt.Color(255, 255, 255));
        btnDetallePrestacion.setText("DETALLE DE PRESTACION");
        btnDetallePrestacion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDetallePrestacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetallePrestacionActionPerformed(evt);
            }
        });

        btnBeneficioMarca.setBackground(new java.awt.Color(0, 102, 51));
        btnBeneficioMarca.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        btnBeneficioMarca.setForeground(new java.awt.Color(255, 255, 255));
        btnBeneficioMarca.setText("CONFIGURACIONES");
        btnBeneficioMarca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBeneficioMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBeneficioMarcaActionPerformed(evt);
            }
        });

        btnAntecedentes.setBackground(new java.awt.Color(0, 102, 51));
        btnAntecedentes.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        btnAntecedentes.setForeground(new java.awt.Color(255, 255, 255));
        btnAntecedentes.setText("ANTECEDENTES");
        btnAntecedentes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAntecedentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAntecedentesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout miLamina2Layout = new javax.swing.GroupLayout(miLamina2);
        miLamina2.setLayout(miLamina2Layout);
        miLamina2Layout.setHorizontalGroup(
            miLamina2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, miLamina2Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(miLamina2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBeneficioMarca, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDetallePrestacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGaleria, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPromotora, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAntecedentes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22))
        );
        miLamina2Layout.setVerticalGroup(
            miLamina2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, miLamina2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPromotora, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGaleria, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAntecedentes, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDetallePrestacion, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBeneficioMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Arial", 3, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CUMPLEAÑOS");

        tablaCumpleanos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tablaCumpleanos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaCumpleanos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(48, 48, 48)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(miLamina, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(miLamina2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(miLamina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(miLamina2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnPromotoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPromotoraActionPerformed
        MenuPromotora mp = new MenuPromotora(null, true);
        mp.setVisible(true);
    }//GEN-LAST:event_btnPromotoraActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            MenuPromotora mp = new MenuPromotora(null, true);
            mp.setVisible(true);
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            MenuBMO mmbo = new MenuBMO(null, true);
            mmbo.setVisible(true);
        }
    }//GEN-LAST:event_formKeyPressed

    private void btnGuardarBaseDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarBaseDatosActionPerformed

        String nameBackup = "Respaldo_BaseDatos.sql";

        JFileChooser fch = new JFileChooser();
        fch.setCurrentDirectory(new File("."));
        fch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fch.setAcceptAllFileFilterUsed(false);
        int optionSelected = fch.showDialog(this, "Seleccionar");
        if (optionSelected == 0) {
            String namePathSaveBackup = fch.getSelectedFile() + "\\" + nameBackup;
            System.out.println("La ruta donde se guardará es: " + namePathSaveBackup);
            backupDB(namePathSaveBackup);

        }
    }//GEN-LAST:event_btnGuardarBaseDatosActionPerformed

    private void btnGaleriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGaleriaActionPerformed
        GaleriaFotos gf = new GaleriaFotos(null, true);
        gf.setVisible(true);
    }//GEN-LAST:event_btnGaleriaActionPerformed

    private void btnAntecedentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAntecedentesActionPerformed
        /*MenuAntecedentes ma = new MenuAntecedentes(null, true);
        ma.setVisible(true);*/
        JOptionPane.showMessageDialog(this, "Este menú solamente puede abrirse desde el menu de promotores.");
    }//GEN-LAST:event_btnAntecedentesActionPerformed

    private void btnBeneficioMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBeneficioMarcaActionPerformed
        MenuBMO mmbo = new MenuBMO(null, true);
        mmbo.setVisible(true);
    }//GEN-LAST:event_btnBeneficioMarcaActionPerformed

    private void btnDetallePrestacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetallePrestacionActionPerformed
        MenuDetallesPrestacion ma = new MenuDetallesPrestacion(null, true);
        ma.setVisible(true);
    }//GEN-LAST:event_btnDetallePrestacionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAntecedentes;
    private javax.swing.JButton btnBeneficioMarca;
    private javax.swing.JButton btnDetallePrestacion;
    private javax.swing.JButton btnGaleria;
    private javax.swing.JButton btnGuardarBaseDatos;
    private javax.swing.JButton btnPromotora;
    private javax.swing.JLabel imgLogo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel miLamina;
    private javax.swing.JPanel miLamina2;
    private javax.swing.JTable tablaCumpleanos;
    // End of variables declaration//GEN-END:variables
}
