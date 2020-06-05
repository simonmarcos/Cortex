package Vistas;

import Clases.BockFoto;
import Clases.Foto;
import Clases.Promotora;
import DAO.BockFotoDAO;
import DAO.DAO;
import DAO.FotoDAO;
import DAO.PromotoraDAO;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class SubirFotos extends javax.swing.JDialog {

    private int dni;
    private int estado; //ESTA VARIABLE ESTADO ME INDICARA SI SE CREARA O MODIFICARA UN BOOK DE FOTO
    private int countPhotos;

    /*VARIABLES QUE ME SERVIRAN PARA ALMACENAR LOS BYTE DE CADA FOTO(CADA FOTO TIENE SU RESPECTIVA VARIABLE LIST)*/
    List<Integer> lista1 = null;
    List<Integer> lista2 = null;
    List<Integer> lista3 = null;
    List<Integer> lista4 = null;
    List<Integer> lista5 = null;

    /*ESTA VARIABLE SERA UN LIST DONDE DENTRO DE ESTE LIST HABRA OTRO LIST QUE ME CONTENDRA TODOS LOS 
    BYTE DE EL ARCHIVO. PARA ASI PODER RECORRERLO A UNO POR UNO EN EL CASO DE HABER MUCHOS ARCHIVOS DENTRO
    DEL LIST PRINCIPAL.*/
    List<List<Integer>> contenedorListas = new ArrayList<>();

    /*ESTA VARIABLE FUE CREADA PARA CUANDO GUARDAR UNA CLAVE Y UN VALOR. EN DONDE LA CLAVE SERA LA FOTO1, FOTO2
    Y EL VALOR SERA LA LISTA QUE ME CONTENDRA LOS BYTE DE CADA ARCHIO.*/
    Map<Integer, List<Integer>> contenedorLista = new HashMap<>();

    public SubirFotos(java.awt.Frame parent, boolean modal, int estado, int countPhotos) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.estado = estado;
        this.countPhotos = countPhotos;
        ocultarBtnEliminar();
        addEscapeListener(this);
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

    //----------------------------------GETERS AND SETTERS---------------------------------------
    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public void setTxtDNIPromotor(String txtDNIPromotor) {
        this.txtDNIPromotor.setText(txtDNIPromotor);
    }

    public void setTxtCodBockFoto(String txtCodBockFoto) {
        this.txtCodBockFoto.setText(txtCodBockFoto);
    }

    //--------------------------------------------------------------------------------------------
    private void nullListas() {
        lista1 = null;
        lista2 = null;
        lista3 = null;
        lista4 = null;
        lista5 = null;
    }

    private void nullLabelFotos() {
        foto1.setIcon(null);
        foto2.setIcon(null);
        foto3.setIcon(null);

    }

    private void ocultarBtnEliminar() {
        btnEliminar1.setVisible(false);
        btnEliminar2.setVisible(false);
        btnEliminar3.setVisible(false);

    }

    public int generarNroBockFoto() {
        String nroFinal = "";

        for (int i = 0; i < 8; i++) {
            double nro = Math.floor(Math.random() * 10);
            int n = (int) nro;
            nroFinal += n;
        }

        return Integer.parseInt(nroFinal);
    }

    private int generarNroAleatorio() {
        String nroFinal = "";

        for (int i = 0; i < 9; i++) {
            double nro = Math.floor(Math.random() * 10);
            int n = (int) nro;
            if (n == 0) {
                n += 1;
            }
            nroFinal += n;
        }

        return Integer.parseInt(nroFinal);
    }

    public void buscarCodPromotor() {
        if (!txtCodBockFoto.getText().isEmpty() && !txtDNIPromotor.getText().isEmpty()) {
            DAO dao = new PromotoraDAO();
            List<Promotora> lista = dao.consultaFiltrada(Integer.parseInt(txtDNIPromotor.getText()), "");
            System.out.println(lista.size());
            for (Promotora promotora : lista) {
                txtCodPromotor.setText(String.valueOf(promotora.getCodPromotor()));
            }
        }
    }

    // ------------------------------------- METODOS CREAR BOOK FOTO-------------------------------------
    private void deshabilitarCamposCrear() {
        txtCodBockFoto.setEnabled(false);
        txtCodBockFoto.setText("");

        txtCodPromotor.setEnabled(false);
        txtCodPromotor.setText("");
        txtDNIPromotor.setText("");

        //limpiarListaCrear();
    }

    private void listarPromotoresCrear() {

        List<Promotora> lista = null;
        // limpiarListaCrear();

        //VAMOS A LISTAR TODAS LAS PROMOTORAS QUE TENEMOS REGISTRADAS
        DAO d = new PromotoraDAO();
        List<Promotora> l = d.listar();

        //VAMOS A LISTAR TODAS LAS PROMOTORAS CON BOOK DE FOTOS QUE TENGAS
        PromotoraDAO dao = new PromotoraDAO();
        lista = dao.listarPromotoresBook();

        if (lista != null) {
            //CON RESPECTO A LA LISTA DE LAS PROMOTORAS CON BOOK DE FOTOS CORROBORAMOS QUE SI YA TIENEN BOOK DE FOTOS
            //ELIMINARLA DE LA LISTA 
            for (Promotora p : lista) {
                if (l.contains(p)) {
                    l.remove(p);
                }
            }

            //RECORREMOS LA LISTA DE LAS PROMOTORAS CON LAS PROMOTORAS YA ELIMINADAS Y LA ENLISTAMOS
            for (Promotora p : l) {
                //listaPromotorCrear.addItem(p.getCodPromotor() + "  -  " + p.getApellido() + " " + p.getNombre());
            }
        }
    }

    /* private void limpiarListaCrear() {
        int elementos = listaPromotorCrear.getItemCount();
        for (int i = 0; i < elementos; i++) {
            listaPromotorCrear.removeItemAt(0);
        }
    }*/
    private int insertarBockFotoCrear() {

        int r = 0;

        BockFotoDAO dao = new BockFotoDAO();

        BockFoto bf = new BockFoto();
        bf.setCodBockFoto(Integer.parseInt(txtCodBockFoto.getText()));
        bf.setCodPromotor(Integer.parseInt(txtCodPromotor.getText()));
        bf.setDni(Integer.parseInt(txtDNIPromotor.getText()));

        r = dao.insertar(bf);
        return r;
    }

    private void insertarFoto(int estado) {

        int r = 0;
        //Variable que me determina la cantidad de fotos permitidas.
        int fotosPermitidas = 3 - countPhotos;
        if (contenedorLista.size() > fotosPermitidas) {
            JOptionPane.showMessageDialog(this, "El bock de fotos no puede tener más de 3 fotos.");
            return;
        }

        for (Map.Entry<Integer, List<Integer>> l : contenedorLista.entrySet()) {

            int codFoto = generarNroAleatorio();
            FotoDAO dao = new FotoDAO();
            Foto f = null;
            f = new Foto();
            f.setCodFoto(codFoto);
            f.setCodBockFoto(Integer.parseInt(txtCodBockFoto.getText()));

            r = dao.insertar(f);
            if (r == 1) {
                guardarImagen(estado, codFoto, l);
            } else if (r == 2) {
                JOptionPane.showMessageDialog(this, "No se pudo registrar.");
            }
        }
        JOptionPane.showMessageDialog(this, "Se registró correctamente.");
        nullListas();
        nullLabelFotos();
        deshabilitarCamposCrear();
        deshabilitarCamposModif();
        ocultarBtnEliminar();
        btnGuardar.setEnabled(false);
        btnLimpiarLabel.setEnabled(false);
        this.dispose();

    }

    private boolean insertarImagenCrear(JLabel foto, List<Integer> listaFoto, int key) {
        /*EN ESTE METODO RECIBIMOS EL LABEL(IDENTIFICADOR DEL RECUADRO) LA VARIABLE DONDE SE GUARDARAN LOS BYTE DE LA FOTO,
        Y UN KEY, QUE SERA EL IDENTIFICADOR DE ESA FOTO QUE NOS SERVIRA PARA LUEGO MANIPULARLA(ELIMINARLA EN EL CASO QUE SE DESEE).*/

        listaFoto = new ArrayList<>();

        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);

        String ruta = "";
        try {
            ruta += chooser.getSelectedFile().getPath();
        } catch (Exception e) {
        }

        if (!ruta.isEmpty() && (ruta.contains(".jpg") || ruta.contains(".png"))) {
            try {
                FileInputStream fis = new FileInputStream(new File(ruta));

                int r = fis.read();
                while (r != -1) {
                    listaFoto.add(r);
                    r = fis.read();
                }

                contenedorLista.put(key, listaFoto);

                ImageIcon image = new ImageIcon(ruta);
                Icon icono = new ImageIcon(image.getImage().getScaledInstance(foto1.getWidth() + 20, foto1.getHeight(), Image.SCALE_DEFAULT));
                foto.setIcon(icono);

                fis.close();
                return true;

            } catch (FileNotFoundException ex) {
                Logger.getLogger(SubirFotos.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SubirFotos.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "El formato que desea subir no es permitido.");
        }

        return false;
    }

    private void guardarImagen(int estado, int codFoto, Map.Entry<Integer, List<Integer>> l) {

        FileOutputStream fos = null;
        try {
            File file = null;
            if (estado == 1) {
                file = new File("C:/xampp/mysql/data/wally/BookFotos/" + txtDNIPromotor.getText());
                file.mkdirs();
                fos = new FileOutputStream(new File("C:/xampp/mysql/data/wally/BookFotos/" + txtDNIPromotor.getText() + "/" + codFoto + ".jpg"));
            } else if (estado == 2) {
                fos = new FileOutputStream(new File("C:/xampp/mysql/data/wally/BookFotos/" + txtDNIPromotor.getText() + "/" + codFoto + ".jpg"));
            }

            for (Integer integer : l.getValue()) {
                fos.write(integer);
            }

            fos.close();

        } catch (IOException ex) {
            Logger.getLogger(SubirFotos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ------------------------------------- METODOS SUBIR NUEVAS FOTOS -------------------------------------
    private void habilitarCamposModif() {

        listarPromotoresModif();
        //limpiarListaCrear();
    }

    private void deshabilitarCamposModif() {
        //limpiarListaModif();
    }

    private void listarPromotoresModif() {

        List<Promotora> lista = null;
        //limpiarListaModif();

        PromotoraDAO dao = new PromotoraDAO();
        lista = dao.listarPromotoresBook();

        if (lista != null) {
            for (Promotora p : lista) {
                //listaPromotorModif.addItem(p.getCodPromotor() + "  -  " + p.getApellido() + " " + p.getNombre());
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        miLamina = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCodBockFoto = new javax.swing.JTextField();
        txtCodPromotor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDNIPromotor = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        foto2 = new javax.swing.JLabel();
        foto3 = new javax.swing.JLabel();
        foto1 = new javax.swing.JLabel();
        btnEliminar1 = new javax.swing.JButton();
        btnEliminar2 = new javax.swing.JButton();
        btnEliminar3 = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnLimpiarLabel = new javax.swing.JButton();

        miLamina.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("DNI PROMOTOR:");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText("CODIGO BOCK DE FOTOS:");
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtCodBockFoto.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtCodBockFoto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodBockFoto.setEnabled(false);

        txtCodPromotor.setFont(new java.awt.Font("Arial", 1, 1)); // NOI18N
        txtCodPromotor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodPromotor.setBorder(null);
        txtCodPromotor.setEnabled(false);
        txtCodPromotor.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("CREAR NUEVO BOOK:");

        txtDNIPromotor.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtDNIPromotor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDNIPromotor.setEnabled(false);

        javax.swing.GroupLayout miLaminaLayout = new javax.swing.GroupLayout(miLamina);
        miLamina.setLayout(miLaminaLayout);
        miLaminaLayout.setHorizontalGroup(
            miLaminaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(miLaminaLayout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addGroup(miLaminaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, miLaminaLayout.createSequentialGroup()
                        .addGroup(miLaminaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(miLaminaLayout.createSequentialGroup()
                                .addGap(174, 174, 174)
                                .addComponent(txtCodPromotor, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(miLaminaLayout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDNIPromotor, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(miLaminaLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodBockFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(72, 72, 72))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, miLaminaLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(167, 167, 167))))
        );
        miLaminaLayout.setVerticalGroup(
            miLaminaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(miLaminaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(miLaminaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodBockFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(miLaminaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDNIPromotor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodPromotor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        foto2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        foto2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        foto2.setText("FOTO");
        foto2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        foto2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foto2MouseClicked(evt);
            }
        });

        foto3.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        foto3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        foto3.setText("FOTO");
        foto3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        foto3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foto3MouseClicked(evt);
            }
        });

        foto1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        foto1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        foto1.setText("FOTO");
        foto1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        foto1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foto1MouseClicked(evt);
            }
        });

        btnEliminar1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnEliminar1.setText("X");
        btnEliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminar1ActionPerformed(evt);
            }
        });

        btnEliminar2.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnEliminar2.setText("X");
        btnEliminar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminar2ActionPerformed(evt);
            }
        });

        btnEliminar3.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnEliminar3.setText("X");
        btnEliminar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminar3ActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(0, 153, 51));
        btnGuardar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.setEnabled(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnLimpiarLabel.setBackground(new java.awt.Color(0, 153, 51));
        btnLimpiarLabel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnLimpiarLabel.setText("LIMPIAR");
        btnLimpiarLabel.setEnabled(false);
        btnLimpiarLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarLabelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnEliminar1)
                            .addComponent(foto1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(85, 85, 85)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(foto2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminar2))
                        .addGap(79, 79, 79)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(foto3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminar3)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(btnLimpiarLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 79, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(btnEliminar3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1)
                            .addComponent(foto3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(btnEliminar2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1)
                            .addComponent(foto2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnEliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(foto1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(77, 77, 77)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiarLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(miLamina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(miLamina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        //SI EL ESTADO ES 1, SIGNIFICA QUE DEBEMOS CREAR UN NUEVO BOCK
        if (estado == 1) {
            if (!txtCodBockFoto.getText().equals("") && !txtCodPromotor.getText().equals("")) {
                int r = insertarBockFotoCrear();
                if (r == 1) {
                    insertarFoto(1);
                } else if (r == 2) {
                    JOptionPane.showMessageDialog(this, "No se pudo registrar.");
                } else if (r == 5) {
                    JOptionPane.showMessageDialog(this, "Ingrese el código de promotora correcto.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe completar los campos requeridos.");
            }
            //SI EL ESTADO ES 2, SIGNIFICA QUE DEBEMOS MODIFICAR EL BOCK
        } else if (estado == 2) {
            if (!txtCodBockFoto.getText().equals("") && !txtCodPromotor.getText().equals("")) {
                insertarFoto(2);
            } else {
                JOptionPane.showMessageDialog(this, "Debe completar los campos requeridos.");
            }
        }

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void validarCamposNumericos(KeyEvent evt) {
        char c = evt.getKeyChar();
        if ((c < '0' || c > '9')) {
            evt.consume();
        }
    }

    private void foto1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_foto1MouseClicked
        /*AL INSERTAR LA IMAGEN NOS DEVOLVERA UNA VARIABLE BOOLEAN QUE NOS INDICARA TRUE SI SE GUARDARON LOS BYTE
        CORRECTAMENTE EN LA LISTA QUE LE PASAMOS POR ARGUMENTO O FALSE EN CASO CONTRARIO.*/
        boolean r = insertarImagenCrear(foto1, lista1, 0);
        //SI ES TRUE PONEMOS LA VARIABLE F1 EN TRUE, HACIENDO REFERENCIA A QUE EL CUADRO DE LA FOTO SE ENCUENTRA OCUPADO
        if (r) {
            btnEliminar1.setVisible(true);
            btnGuardar.setEnabled(true);
            btnLimpiarLabel.setEnabled(true);
        }
    }//GEN-LAST:event_foto1MouseClicked

    private void foto2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_foto2MouseClicked
        boolean r = insertarImagenCrear(foto2, lista2, 1);
        if (r) {
            btnEliminar2.setVisible(true);
            btnGuardar.setEnabled(true);
            btnLimpiarLabel.setEnabled(true);
        }
        //insertarImagen(foto2, lista2);
    }//GEN-LAST:event_foto2MouseClicked

    private void foto3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_foto3MouseClicked
        boolean r = insertarImagenCrear(foto3, lista3, 2);
        if (r) {
            btnEliminar3.setVisible(true);
            btnGuardar.setEnabled(true);
            btnLimpiarLabel.setEnabled(true);
        }
    }//GEN-LAST:event_foto3MouseClicked

    private void eliminarFotoRecuadro(JButton btn, JLabel foto, List<Integer> lista, int keyPhoto) {

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                foto.setIcon(null);
                btn.setVisible(false);
                //Eliminamos del Map este elemento cuyo key se pasa por parametro.
                contenedorLista.remove(keyPhoto);
            }
        });
        lista = null;
    }


    private void btnEliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminar1ActionPerformed
        eliminarFotoRecuadro(btnEliminar1, foto1, lista1, 1);
    }//GEN-LAST:event_btnEliminar1ActionPerformed

    private void btnEliminar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminar3ActionPerformed
        eliminarFotoRecuadro(btnEliminar3, foto3, lista3, 2);
    }//GEN-LAST:event_btnEliminar3ActionPerformed

    private void btnEliminar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminar2ActionPerformed
        eliminarFotoRecuadro(btnEliminar2, foto2, lista2, 0);
    }//GEN-LAST:event_btnEliminar2ActionPerformed

    private void btnLimpiarLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarLabelActionPerformed
        nullLabelFotos();
        ocultarBtnEliminar();
    }//GEN-LAST:event_btnLimpiarLabelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar1;
    private javax.swing.JButton btnEliminar2;
    private javax.swing.JButton btnEliminar3;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiarLabel;
    private javax.swing.JLabel foto1;
    private javax.swing.JLabel foto2;
    private javax.swing.JLabel foto3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel miLamina;
    private javax.swing.JTextField txtCodBockFoto;
    private javax.swing.JTextField txtCodPromotor;
    private javax.swing.JTextField txtDNIPromotor;
    // End of variables declaration//GEN-END:variables
}
