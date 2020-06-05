package Vistas;

import Clases.Promotora;
import DAO.BockFotoDAO;
import DAO.DAO;
import DAO.FotoDAO;
import DAO.PromotoraDAO;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class GaleriaFotos extends javax.swing.JDialog {

    private Map<Integer, List<String>> listaArchivos = null;
    private int contador = 1;
    private File[] arrayFotos = null;
    private boolean btnSeleccionado = false;
    //Variable que me contendra la cantidad de fotos que se encuentran guardados en la base de datos
    private int countPhotosPromotora = 0;

    public GaleriaFotos(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("GALERIA");
        llenarListaPromotora();
        addEscapeListener(this);
    }

    public GaleriaFotos() {
    }

    //METODO PARA CERRAR CON ESCAPE EL JDIALOG
    public static void addEscapeListener(final JDialog dialog) {
        ActionListener escListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        };

        dialog.getRootPane().registerKeyboardAction(escListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

    }

    public JTextField getTxtDNIPromotor() {
        return txtDNIPromotor;
    }

    public void setTxtDNIPromotor(JTextField txtDNIPromotor) {
        this.txtDNIPromotor = txtDNIPromotor;
    }

    public void setDniPromotor(String dniPromotor) {
        txtDNIPromotor.setText(dniPromotor);
    }

    //METODO QUE ME BUSCARA EL DNI DEL PROMOTOR SEGUN EL NOMBRE Y APELLIDO
    private void buscarDNIPromotor() {

        String elemento = listaPromotoras.getSelectedItem().toString();
        String apellido = "";
        String nombre = "";
        for (int i = 0; i < elemento.length(); i++) {
            int r = elemento.indexOf(",");
            if (i < r) {
                apellido += elemento.charAt(i);
            } else if (i > r) {
                nombre += elemento.charAt(i);
            }
        }

        PromotoraDAO dao = new PromotoraDAO();
        List<Promotora> lista = dao.consultaNombres(apellido.trim(), nombre.trim());

        for (Promotora promotora : lista) {
            txtDNIPromotor.setText(String.valueOf(promotora.getDNI()));
        }

    }

    private void llenarListaPromotora() {
        DAO dao = new PromotoraDAO();
        List<Promotora> lista = dao.listar();

        Collections.sort(lista);
        for (Promotora p : lista) {
            listaPromotoras.addItem(p.getApellido() + ", " + p.getNombre());
        }
    }

    private void limpiarPanelFotos() {
        fotoPrincipal_1.setIcon(null);
        fotoPrincipal_2.setIcon(null);
        fotoPrincipal_3.setIcon(null);
        lblPathFoto_1.setText("");
        lblPathFoto_2.setText("");
        lblPathFoto_3.setText("");

        btnCrear.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    public void buscarBookFoto() {
        BockFotoDAO dao = new BockFotoDAO();
        int codBookFoto = dao.consultarCodBockFoto(Integer.parseInt(txtDNIPromotor.getText()));
        if (codBookFoto != 0) {
            txtCodBockFotos.setText(String.valueOf(codBookFoto));
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);
            btnCrear.setEnabled(false);
            buscarFoto(txtDNIPromotor.getText());
        } else {
            btnModificar.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnCrear.setEnabled(true);
            txtCodBockFotos.setText("");
            fotoPrincipal_1.setIcon(null);
            fotoPrincipal_2.setIcon(null);
            fotoPrincipal_3.setIcon(null);
            lblPathFoto_1.setIcon(null);
            lblPathFoto_2.setIcon(null);
            lblPathFoto_3.setIcon(null);
        }
    }

    public void buscarFoto(String dniPromotor) {

        contador = 0;
        listaArchivos = null;
        arrayFotos = null;
        listaArchivos = new HashMap<>();

        File file = new File("C:/xampp/mysql/data/wally/BookFotos/" + dniPromotor);
        countPhotosPromotora = file.listFiles().length;

        //CON ESTA INSTRUCCION GUARDAREMOS EN UN ARRAY TODOS LOS ARCHIVOS(FOTOS) DENTRO DE ESA CARPETA CON ESE CODIGO
        arrayFotos = file.listFiles();
        //RECORREMOS ESE ARRAY PARA OBTENER CADA FOTO
        if (arrayFotos != null) {
            for (int i = 0; i < arrayFotos.length; i++) {

                //OBTENEMOS LA RUTA DE LA PRIMERA FOTO
                String ruta = String.valueOf((arrayFotos[i]));

                //GUARDAMOS EN UNA LISTA LA DIRECCION DE LA RUTA
                List<String> listaRuta = new ArrayList<>();
                listaRuta.add(ruta);

                //GUARDAMOS EN UN MAP EL VALOR Y LA LISTA CON SU STRING CORRESPONDIENTE
                listaArchivos.put(i, listaRuta);

                ImageIcon image = new ImageIcon(String.valueOf((arrayFotos[i])));
                Icon icono = new ImageIcon(image.getImage().getScaledInstance((int) Math.floor(image.getIconWidth() / 4), (int) Math.floor(image.getIconHeight() / 4), Image.SCALE_DEFAULT));
                switch (i) {
                    case 0:
                        fotoPrincipal_1.setIcon(icono);
                        lblPathFoto_1.setText(ruta);
                        break;
                    case 1:
                        fotoPrincipal_2.setIcon(icono);
                        lblPathFoto_2.setText(ruta);
                        break;
                    case 2:
                        fotoPrincipal_3.setIcon(icono);
                        lblPathFoto_3.setText(ruta);
                        break;
                    default:
                        break;
                }
            }
        }

    }

    private void eliminarBookFoto() {
        if (!txtDNIPromotor.getText().isEmpty()) {
            DAO dao = new BockFotoDAO();
            int r = dao.eliminar(Integer.parseInt(txtCodBockFotos.getText()));
            if (r == 1) {
                deleteBockPhotoFolder(txtDNIPromotor.getText());
                JOptionPane.showMessageDialog(null, "Se eliminó correctamente el bock de fotos.");
                limpiarPanelFotos();
            } else {
                JOptionPane.showMessageDialog(null, "No se puedo eliminar el bock de fotos. Cierre la ventana e intente nuevamente.");
            }
        }
    }

    private void deletePhoto(int photo) {
        File file = null;
        String namePhoto = "";
        DAO dao = new FotoDAO();
        int r = 0;
        switch (photo) {
            case 1:
                file = new File(lblPathFoto_1.getText());
                System.out.println(lblPathFoto_1.getText());
                namePhoto = file.getName().replace(".jpg", "").replace(".png", "").trim();
                r = dao.eliminar(Integer.parseInt(namePhoto));
                if (r == 1) {
                    fotoPrincipal_1.setIcon(null);
                    file.delete();

                    lblPathFoto_1.setText("");
                    countPhotosPromotora = countPhotosPromotora - 1;
                }
                break;
            case 2:
                file = new File(lblPathFoto_2.getText());
                namePhoto = file.getName().replace(".jpg", "").replace(".png", "").trim();
                r = dao.eliminar(Integer.parseInt(namePhoto));
                if (r == 1) {
                    fotoPrincipal_2.setIcon(null);
                    file.delete();

                    lblPathFoto_2.setText("");
                    countPhotosPromotora = countPhotosPromotora - 1;
                }
                break;
            case 3:
                file = new File(lblPathFoto_3.getText());
                namePhoto = file.getName().replace(".jpg", "").replace(".png", "").trim();
                r = dao.eliminar(Integer.parseInt(namePhoto));
                if (r == 1) {
                    fotoPrincipal_3.setIcon(null);
                    file.delete();

                    lblPathFoto_3.setText("");
                    countPhotosPromotora = countPhotosPromotora - 1;
                }
                break;
            default:
                break;
        }
    }

    //Eliminamos el bock de fotos del disco
    public static void deleteBockPhotoFolder(String codBockPhoto) {
        String pathFolder = "C:/xampp/mysql/data/wally/BookFotos/" + codBockPhoto;
        File files = new File(pathFolder);
        String[] listFiles = files.list();
        if (listFiles != null) {

            for (String lf : listFiles) {
                String pathFile = "C:/xampp/mysql/data/wally/BookFotos/" + codBockPhoto + "/" + lf;
                File file = new File(pathFile);
                file.delete();
            }
            files.delete();
        }
    }

    private void openImageWithDefaultApplication(int button) {
        switch (button) {
            case 1: {
                try {
                    Desktop.getDesktop().open(new File(lblPathFoto_1.getText()));
                } catch (IOException ex) {
                    Logger.getLogger(GaleriaFotos.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case 2: {
                try {
                    Desktop.getDesktop().open(new File(lblPathFoto_2.getText()));
                } catch (IOException ex) {
                    Logger.getLogger(GaleriaFotos.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case 3: {
                try {
                    Desktop.getDesktop().open(new File(lblPathFoto_3.getText()));
                } catch (IOException ex) {
                    Logger.getLogger(GaleriaFotos.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            default:
                break;
        }
    }

    private void validarCamposNumericos(KeyEvent evt) {
        char c = evt.getKeyChar();
        if ((c < '0' || c > '9')) {
            evt.consume();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        listaPromotoras = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtCodBockFotos = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDNIPromotor = new javax.swing.JTextField();
        btnCrear = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        fotoPrincipal_1 = new javax.swing.JLabel();
        fotoPrincipal_3 = new javax.swing.JLabel();
        fotoPrincipal_2 = new javax.swing.JLabel();
        lblPathFoto_1 = new javax.swing.JLabel();
        lblPathFoto_2 = new javax.swing.JLabel();
        lblPathFoto_3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("PROMOTOR:");

        listaPromotoras.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaPromotoras.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -" }));
        listaPromotoras.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaPromotorasItemStateChanged(evt);
            }
        });
        listaPromotoras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                listaPromotorasKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText("CODIGO BOOK DE FOTOS:");

        txtCodBockFotos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtCodBockFotos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodBockFotos.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel3.setText("DNI PROMOTOR:");

        txtDNIPromotor.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtDNIPromotor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDNIPromotor.setEnabled(false);
        txtDNIPromotor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDNIPromotorKeyTyped(evt);
            }
        });

        btnCrear.setBackground(new java.awt.Color(0, 153, 51));
        btnCrear.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnCrear.setForeground(new java.awt.Color(255, 255, 255));
        btnCrear.setText("CREAR");
        btnCrear.setEnabled(false);
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });
        btnCrear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCrearKeyPressed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(0, 153, 51));
        btnModificar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setText("MODIFICAR");
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        btnModificar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnModificarKeyPressed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(0, 153, 51));
        btnEliminar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("ELIMINAR BOOK");
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        btnEliminar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEliminarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDNIPromotor))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(listaPromotoras, 0, 237, Short.MAX_VALUE)
                            .addComponent(txtCodBockFotos))))
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCrear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(347, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(listaPromotoras, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDNIPromotor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodBockFotos, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        fotoPrincipal_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fotoPrincipal_1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(0, 153, 51), null, null));
        fotoPrincipal_1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fotoPrincipal_1MouseClicked(evt);
            }
        });

        fotoPrincipal_3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fotoPrincipal_3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(0, 153, 51), null, null));
        fotoPrincipal_3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fotoPrincipal_3MouseClicked(evt);
            }
        });

        fotoPrincipal_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fotoPrincipal_2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(0, 153, 51), null, null));
        fotoPrincipal_2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fotoPrincipal_2MouseClicked(evt);
            }
        });

        lblPathFoto_1.setFont(new java.awt.Font("Tahoma", 0, 1)); // NOI18N
        lblPathFoto_1.setForeground(new java.awt.Color(255, 255, 255));
        lblPathFoto_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        lblPathFoto_2.setFont(new java.awt.Font("Tahoma", 0, 1)); // NOI18N
        lblPathFoto_2.setForeground(new java.awt.Color(255, 255, 255));
        lblPathFoto_2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        lblPathFoto_3.setFont(new java.awt.Font("Tahoma", 0, 1)); // NOI18N
        lblPathFoto_3.setForeground(new java.awt.Color(255, 255, 255));
        lblPathFoto_3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoPrincipal_1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPathFoto_1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoPrincipal_2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPathFoto_2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoPrincipal_3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPathFoto_3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoPrincipal_3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fotoPrincipal_2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fotoPrincipal_1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblPathFoto_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPathFoto_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPathFoto_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void listaPromotorasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaPromotorasItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String elemento = listaPromotoras.getSelectedItem().toString();
            if (elemento.equals("- - -")) {
                btnModificar.setEnabled(false);
                btnCrear.setEnabled(false);
                txtCodBockFotos.setText("");
                txtDNIPromotor.setText("");
            } else {
                //A PARTIR DEL NOMBRE Y APELLIDO DE LA LISTA SELECCIONADA BUSCAMOS EL PROMOTOR
                buscarDNIPromotor();
                //SI SE ENCONTRO EL DNI DEL PROMOTOR BUSCAMOS SI TIENE BOOK DE FOTO CREADO YA
                if (!txtDNIPromotor.getText().isEmpty()) {
                    buscarBookFoto();
                }
            }

        }
    }//GEN-LAST:event_listaPromotorasItemStateChanged

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        SubirFotos sb = new SubirFotos(null, true, 1, countPhotosPromotora);
        sb.setTxtCodBockFoto(String.valueOf(sb.generarNroBockFoto()));
        sb.setTxtDNIPromotor(txtDNIPromotor.getText());
        sb.buscarCodPromotor();
        sb.setVisible(true);
    }//GEN-LAST:event_btnCrearActionPerformed

    private void btnCrearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCrearKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCrearKeyPressed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        if (countPhotosPromotora < 3) {
            SubirFotos sb = new SubirFotos(null, true, 2, countPhotosPromotora);
            sb.setTxtCodBockFoto(String.valueOf(txtCodBockFotos.getText()));
            sb.setTxtDNIPromotor(txtDNIPromotor.getText().trim());
            sb.buscarCodPromotor();
            sb.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "El bock de fotos está completo. Si desea cargar nuevas fotos, debe eliminar el bock actual y crear uno nuevo.");
        }

    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnModificarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnModificarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnModificarKeyPressed

    private void listaPromotorasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaPromotorasKeyPressed

    }//GEN-LAST:event_listaPromotorasKeyPressed

    private void txtDNIPromotorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDNIPromotorKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_txtDNIPromotorKeyTyped

    private void fotoPrincipal_1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fotoPrincipal_1MouseClicked
        if (!lblPathFoto_1.getText().isEmpty()) {
            openImageWithDefaultApplication(1);
        }

    }//GEN-LAST:event_fotoPrincipal_1MouseClicked

    private void fotoPrincipal_2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fotoPrincipal_2MouseClicked
        if (!lblPathFoto_2.getText().isEmpty()) {
            openImageWithDefaultApplication(2);
        }
    }//GEN-LAST:event_fotoPrincipal_2MouseClicked

    private void fotoPrincipal_3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fotoPrincipal_3MouseClicked
        if (!lblPathFoto_3.getText().isEmpty()) {
            openImageWithDefaultApplication(3);
        }
    }//GEN-LAST:event_fotoPrincipal_3MouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminarBookFoto();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnEliminarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEliminarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JLabel fotoPrincipal_1;
    private javax.swing.JLabel fotoPrincipal_2;
    private javax.swing.JLabel fotoPrincipal_3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblPathFoto_1;
    private javax.swing.JLabel lblPathFoto_2;
    private javax.swing.JLabel lblPathFoto_3;
    private javax.swing.JComboBox<String> listaPromotoras;
    private javax.swing.JTextField txtCodBockFotos;
    public javax.swing.JTextField txtDNIPromotor;
    // End of variables declaration//GEN-END:variables

}
