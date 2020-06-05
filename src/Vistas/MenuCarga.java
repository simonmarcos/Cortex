package Vistas;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.UIManager;

public class MenuCarga extends javax.swing.JFrame {

    private int i = 0;
    private Timer timer;

    public static void main(String[] args) {
        //Desktop.getDesktop().open(new File("C:/xampp/mysql/data/wally/BookFotos/46032049/561212639.jpg"));
        MenuCarga mc = new MenuCarga();
        mc.setVisible(true);

        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public MenuCarga() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        iniciarCarga();

        addImagen();
    }

    private void addImagen() {
        Image imagenUser = new ImageIcon(getClass().getResource("/Img/img.png")).getImage();
        Icon iconoUser = new ImageIcon(imagenUser.getScaledInstance(imgLogo.getWidth(), imgLogo.getHeight(), Image.SCALE_DEFAULT));

        imgLogo.setIcon(iconoUser);
    }

    private void iniciarCarga() {

        timer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                i += 1;
                barraProgreso.setValue(i);
                barraProgreso.setStringPainted(true);

                if (i == 100) {
                    timer.stop();
                    dispose();
                    //JOptionPane.showMessageDialog(null, "Bienvenido Walter");
                    MenuPrincipal mp = new MenuPrincipal();
                    mp.setVisible(true);
                }

                if (i <= 25) {
                    txtCargaDatos.setText("Cargando interfáz");
                } else if (i > 25 && i <= 50) {
                    txtCargaDatos.setText("Cargando archivos externos");
                } else if (i > 50 && i <= 80) {
                    txtCargaDatos.setText("Cargando base de datos");
                } else if (i > 80 && i < 100) {
                    txtCargaDatos.setText("Iniciando aplicación");
                }

            }
        });
        timer.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        imgLogo = new javax.swing.JLabel();
        barraProgreso = new javax.swing.JProgressBar();
        txtCargaDatos = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        barraProgreso.setBackground(new java.awt.Color(255, 255, 255));
        barraProgreso.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        barraProgreso.setForeground(new java.awt.Color(0, 0, 0));
        barraProgreso.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtCargaDatos.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtCargaDatos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imgLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(barraProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
            .addComponent(txtCargaDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(imgLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(barraProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCargaDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel1.setBackground(new java.awt.Color(102, 102, 102));
        jLabel1.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barraProgreso;
    private javax.swing.JLabel imgLogo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel txtCargaDatos;
    // End of variables declaration//GEN-END:variables
}
