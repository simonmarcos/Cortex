package Vistas;

import Clases.DetallesPrestacion;
import Clases.Promotora;
import DAO.DAO;
import DAO.DetallesPrestacionDAO;
import DAO.PromotoraDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.text.Caret;

public class MenuDetallesPrestacion extends javax.swing.JDialog {

    private List<Promotora> listaPromotorByDNI = null;
    //Variable que me determinará si creamos o modificamos 
    private int estado = 0;

    public MenuDetallesPrestacion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setResizable(false);
        setLocationRelativeTo(null);
        this.setTitle("DETALLES DE PRESTACION");
        llenarListaPromotora();
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

    public JLabel getLabelDNIPromotor() {
        return labelDNIPromotor;
    }

    public void setLabelDNIPromotor(String labelDNIPromotor) {
        this.labelDNIPromotor.setText(labelDNIPromotor);
    }

    private void limpiarCampos() {
        txtObraSocial.setText("");
        checkPrestacion.setSelected(false);
        checkTemporal.setSelected(false);
        labelDNIPromotor.setText("");
        areaSeguro.setText("");
        areaF931.setText("");
    }

    private void llenarListaPromotora() {
        DAO dao = new PromotoraDAO();
        List<Promotora> lista = dao.listar();

        Collections.sort(lista);
        for (Promotora p : lista) {
            listaPromotores.addItem(p.getApellido() + ", " + p.getNombre());
        }
    }

    private void datosParaSeguro() {
        DAO dao = new PromotoraDAO();
        listaPromotorByDNI = dao.consultaFiltrada(Integer.parseInt(labelDNIPromotor.getText()), "");

        for (Promotora p : listaPromotorByDNI) {
            areaSeguro.setText("Nombre: " + p.getNombre() + "\nApellido: " + p.getApellido() + "\nDNI: " + p.getDNI() + "\nFecha nacimiento: " + p.getFechaNacimiento().toString());
        }
    }

    private void datosF931() {
        DAO dao = new DetallesPrestacionDAO();
        List<DetallesPrestacion> lista = dao.consultaFiltrada(Integer.parseInt(lblCodPromotor.getText()), "");

        String textAreaF931 = "";

        for (DetallesPrestacion p : lista) {

            String contrato = "";
            //Determinamos el tipo de contrato firmado y le asignamos el nombre del contrato.
            if (p.getContratoFirmado() == 1) {
                contrato = "Prestacion de servicio";
            } else if (p.getContratoFirmado() == 2) {
                contrato = "Temporal (RD)";
            } else if (p.getContratoFirmado() == 3) {
                contrato = "Prestacion de servicio - Temporal (RD)";
            }
            textAreaF931 += "Formulario: " + p.getF931() + "\nMonotributista: " + p.getMonotributista() + "\nObra Social: " + p.getObraSocial() + "\nContrato Firmado: " + contrato;
        }
        areaF931.setText(areaSeguro.getText() + "\n" + textAreaF931);
    }

    private void copiarTextoSeguro() {
        Caret seleccion = areaSeguro.getCaret();
        int posicion = 0;
        if (seleccion.getDot() != seleccion.getMark()) {
            posicion = seleccion.getDot();
        }
    }

    public void llenarCamposConDatos() {
        lblCodPromotor.setText(String.valueOf(new PromotoraDAO().buscarCodPromotor(Integer.parseInt(labelDNIPromotor.getText()))));
        DAO dao = new DetallesPrestacionDAO();
        List<DetallesPrestacion> lista = dao.consultaFiltrada(Integer.parseInt(lblCodPromotor.getText().trim()), "");

        if (lista.size() == 0) {
            btnCrear.setEnabled(true);
            btnModificar.setEnabled(false);
            lblDatos.setText("NO SE ENCONTRARON DATOS");
            areaSeguro.setText("");
        } else {
            btnModificar.setEnabled(true);
            btnCrear.setEnabled(false);
            lblDatos.setText("");

            for (DetallesPrestacion d : lista) {
                txtObraSocial.setText(d.getObraSocial());

                String monot = d.getMonotributista();
                if (monot.equalsIgnoreCase("SI")) {
                    listaMonotributista.setSelectedIndex(0);
                } else {
                    listaMonotributista.setSelectedIndex(1);
                }

                int contrato = d.getContratoFirmado();
                if (contrato == 1) {
                    checkPrestacion.setSelected(true);
                } else if (contrato == 2) {
                    checkTemporal.setSelected(true);
                } else if (contrato == 3) {
                    checkPrestacion.setSelected(true);
                    checkTemporal.setSelected(true);
                }

                String f931 = d.getF931();
                if (f931.equalsIgnoreCase("ALTA")) {
                    listaF931.setSelectedIndex(0);
                } else {
                    listaF931.setSelectedIndex(1);
                }
            }

            datosParaSeguro();
            datosF931();
        }
    }

    private void buscarDNIPromotor() {

        String elemento = listaPromotores.getSelectedItem().toString();
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
            labelDNIPromotor.setText(String.valueOf(promotora.getDNI()));
        }

    }

    private void registrarDetallePrestacion() {

        String monotributista = listaMonotributista.getSelectedItem().toString();
        String F931 = listaF931.getSelectedItem().toString();
        int codPromotor = Integer.parseInt(lblCodPromotor.getText());
        //ESTA VARIABLE HACE REFERENCIAS A CONTRATOSFIRMADOS. SI EL ESTADO DEL TRATO ES 1, SIGNIFICA QUE LA OPCION
        //DE CONTRATO DE PRESTACION ESTA SELECCIONADA, SI ES 2 SIGNIFICA QUE SE SELECCIONO TEMPORAL
        //SI ES 3 SIGNIFICA QUE SE SELECCIONO AMBAS
        int contrato = 0;

        if (checkPrestacion.isSelected() && !checkTemporal.isSelected()) {
            contrato = 1;
        } else if (!checkPrestacion.isSelected() && checkTemporal.isSelected()) {
            contrato = 2;
        } else if (checkPrestacion.isSelected() && checkTemporal.isSelected()) {
            contrato = 3;
        }

        DetallesPrestacion dp = new DetallesPrestacion();
        dp.setCodPromotor(codPromotor);
        dp.setObraSocial(txtObraSocial.getText());
        dp.setMonotributista(monotributista);
        dp.setF931(F931);
        dp.setContratoFirmado(contrato);

        DAO dao = new DetallesPrestacionDAO();
        int r = dao.insertar(dp);

        if (r == 1) {
            JOptionPane.showMessageDialog(this, "Se registró correctamente.");
            limpiarCampos();
        }
    }

    private void modificarDetallePrestacion() {

        String monotributista = listaMonotributista.getSelectedItem().toString();
        String F931 = listaF931.getSelectedItem().toString();
        int codPromotor = Integer.parseInt(lblCodPromotor.getText());
        //ESTA VARIABLE HACE REFERENCIAS A CONTRATOSFIRMADOS. SI EL ESTADO DEL TRATO ES 1, SIGNIFICA QUE LA OPCION
        //DE CONTRATO DE PRESTACION ESTA SELECCIONADA, SI ES 2 SIGNIFICA QUE SE SELECCIONO TEMPORAL
        //SI ES 3 SIGNIFICA QUE SE SELECCIONO AMBAS
        int contrato = 0;

        if (checkPrestacion.isSelected() && !checkTemporal.isSelected()) {
            contrato = 1;
        } else if (!checkPrestacion.isSelected() && checkTemporal.isSelected()) {
            contrato = 2;
        } else if (checkPrestacion.isSelected() && checkTemporal.isSelected()) {
            contrato = 3;
        }

        DetallesPrestacion dp = new DetallesPrestacion();
        dp.setCodPromotor(codPromotor);
        dp.setObraSocial(txtObraSocial.getText());
        dp.setMonotributista(monotributista);
        dp.setF931(F931);
        dp.setContratoFirmado(contrato);

        DAO dao = new DetallesPrestacionDAO();
        int r = dao.modificar(codPromotor, dp);

        if (r == 1) {
            JOptionPane.showMessageDialog(this, "Se modificó correctamente.");
            this.limpiarCampos();
            listaPromotores.setSelectedIndex(0);
        }
    }

    private void habilitarCampos() {
        txtObraSocial.setEnabled(true);
        listaMonotributista.setEnabled(true);
        listaF931.setEnabled(true);
        checkPrestacion.setEnabled(true);
        checkTemporal.setEnabled(true);
        btnGuardar.setEnabled(true);
    }

    private void deshabilitarCampos() {
        txtObraSocial.setEnabled(false);
        listaMonotributista.setEnabled(false);
        listaF931.setEnabled(false);
        checkPrestacion.setEnabled(false);
        checkTemporal.setEnabled(false);
        btnGuardar.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radioMonotributista = new javax.swing.ButtonGroup();
        radioF931 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        listaPromotores = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        labelDNIPromotor = new javax.swing.JLabel();
        btnCrear = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        lblDatos = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        checkPrestacion = new javax.swing.JCheckBox();
        checkTemporal = new javax.swing.JCheckBox();
        txtObraSocial = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        listaMonotributista = new javax.swing.JComboBox<>();
        listaF931 = new javax.swing.JComboBox<>();
        lblCodPromotor = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        areaSeguro = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        areaF931 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("PROMOTORES:");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        listaPromotores.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaPromotores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -" }));
        listaPromotores.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaPromotoresItemStateChanged(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText("DNI:");
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        labelDNIPromotor.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        labelDNIPromotor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDNIPromotor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

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

        lblDatos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblDatos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(listaPromotores, 0, 200, Short.MAX_VALUE)
                            .addComponent(labelDNIPromotor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(lblDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(listaPromotores, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelDNIPromotor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel3.setText("Obra Social:");
        jLabel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel4.setText("Monotributista:");
        jLabel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel5.setText("F931:");
        jLabel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel5.setEnabled(false);

        jLabel6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel6.setText("Contrato Firmado:");
        jLabel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        checkPrestacion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        checkPrestacion.setText("Prestacion de servicio");
        checkPrestacion.setEnabled(false);

        checkTemporal.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        checkTemporal.setText("Temporal (RD)");
        checkTemporal.setEnabled(false);

        txtObraSocial.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtObraSocial.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtObraSocial.setEnabled(false);

        jLabel9.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("FORMULARIO");

        listaMonotributista.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SI", "NO" }));
        listaMonotributista.setEnabled(false);

        listaF931.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALTA", "BAJA" }));
        listaF931.setEnabled(false);

        lblCodPromotor.setForeground(new java.awt.Color(153, 153, 153));
        lblCodPromotor.setEnabled(false);
        lblCodPromotor.setOpaque(true);

        btnGuardar.setBackground(new java.awt.Color(0, 153, 51));
        btnGuardar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("GUARDAR");
        btnGuardar.setEnabled(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        btnGuardar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGuardarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblCodPromotor)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtObraSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(checkPrestacion)
                                    .addGap(18, 18, 18)
                                    .addComponent(checkTemporal, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(listaMonotributista, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(listaF931, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(54, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(lblCodPromotor)
                .addGap(11, 11, 11)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtObraSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(listaMonotributista)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(listaF931))
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkPrestacion)
                    .addComponent(checkTemporal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("DATOS PARA EL SEGURO");

        areaSeguro.setEditable(false);
        areaSeguro.setColumns(20);
        areaSeguro.setRows(5);
        areaSeguro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                areaSeguroMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(areaSeguro);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(156, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(46, 46, 46)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(21, Short.MAX_VALUE)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("DATOS PARA F931");

        areaF931.setColumns(20);
        areaF931.setRows(5);
        jScrollPane1.setViewportView(areaF931);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        estado = 1;
        habilitarCampos();
        //registrarDetallePrestacion();
    }//GEN-LAST:event_btnCrearActionPerformed

    private void btnCrearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCrearKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCrearKeyPressed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        estado = 2;
        habilitarCampos();
        //modificarDetallePrestacion();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnModificarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnModificarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnModificarKeyPressed

    private void listaPromotoresItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaPromotoresItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            limpiarCampos();
            String elemento = listaPromotores.getSelectedItem().toString();
            if (elemento.equals("- - -")) {
                btnModificar.setEnabled(false);
                btnCrear.setEnabled(false);
            } else {
                buscarDNIPromotor();
                llenarCamposConDatos();
            }

        }
    }//GEN-LAST:event_listaPromotoresItemStateChanged

    private void areaSeguroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_areaSeguroMouseClicked

    }//GEN-LAST:event_areaSeguroMouseClicked

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (estado == 1) {
            registrarDetallePrestacion();
            deshabilitarCampos();
        } else if (estado == 2) {
            modificarDetallePrestacion();
            deshabilitarCampos();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnGuardarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGuardarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaF931;
    private javax.swing.JTextArea areaSeguro;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JCheckBox checkPrestacion;
    private javax.swing.JCheckBox checkTemporal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel labelDNIPromotor;
    private javax.swing.JLabel lblCodPromotor;
    private javax.swing.JLabel lblDatos;
    private javax.swing.JComboBox<String> listaF931;
    private javax.swing.JComboBox<String> listaMonotributista;
    private javax.swing.JComboBox<String> listaPromotores;
    private javax.swing.ButtonGroup radioF931;
    private javax.swing.ButtonGroup radioMonotributista;
    private javax.swing.JTextField txtObraSocial;
    // End of variables declaration//GEN-END:variables
}
