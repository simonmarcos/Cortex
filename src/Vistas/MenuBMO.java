package Vistas;

import Clases.Beneficio;
import Clases.Marca;
import Clases.Orientacion;
import DAO.BeneficioDAO;
import DAO.DAO;
import DAO.MarcaDAO;
import DAO.OrientacionDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MenuBMO extends javax.swing.JDialog {

    public MenuBMO(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        txtNombreOrientacionModif.setEditable(true);
        txtNombreMarcaModif.setEditable(true);
        txtNombreBenefModif.setEditable(true);
        listarOrientacion();
        pestañaPrincipal.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switch (pestañaPrincipal.getSelectedIndex()) {
                    case 2:
                        listarBeneficios();
                        limpiarCamposMarca();
                        limpiarCamposOrientacion();
                        break;
                    case 1:
                        listarMarcas();
                        limpiarCamposBeneficio();
                        break;
                    case 0:
                        listarOrientacion();
                        limpiarCamposMarca();
                        limpiarCamposBeneficio();
                        break;
                    default:
                        break;
                }

            }
        });
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

    private int generarNumAleatorio() {

        String num = "";
        for (int i = 0; i < 5; i++) {
            int nro = (int) Math.floor(Math.random() * 10);
            if (nro == 0) {
                nro += 1;
            }
            num += nro;
        }
        return Integer.parseInt(num);
    }

    //----------------------------------------- METODOS BENEFICIOS -----------------------------------------
    private void limpiarCamposBeneficio() {
        txtCodBeneficio.setText("");
        txtNombreBeneficio.setText("");
        txtCodBenefModif.setText("");
        txtNombreBenefModif.setText("");
        //txtNombreBenefModif.setEnabled(false);
    }

    private void listarBeneficios() {

        List<Beneficio> lista = null;
        limpiarListaBeneficio();

        DAO dao = new BeneficioDAO();
        lista = dao.listar();

        Collections.sort(lista);
        if (lista != null) {
            for (Beneficio b : lista) {
                if (b.getCodBeneficio() != 0) {
                    listaBeneficios.addItem(b.getCodBeneficio() + " - " + b.getNombre());
                }
            }
        }
    }

    private void registrarBeneficio() {

        if (!txtCodBeneficio.getText().equals("") && !txtNombreBeneficio.getText().equals("")) {
            DAO dao = new BeneficioDAO();
            Beneficio b = new Beneficio(generarNumAleatorio(), txtNombreBeneficio.getText());
            int r = dao.insertar(b);

            if (r == 1) {
                JOptionPane.showMessageDialog(this, "Se registró correctamente.");
                limpiarCamposBeneficio();
                listarBeneficios();
            } else if (r == 2) {
                JOptionPane.showMessageDialog(this, "No se pudo registrar.");
            } else if (r == 4) {
                JOptionPane.showMessageDialog(this, "El registro con ese código ya existe.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe completar los campos.");
        }

    }

    private String buscarBeneficio() {
        String nombre = "";
        if (!txtCodBenefModif.getText().equals("")) {
            List<Beneficio> lista = null;

            DAO dao = new BeneficioDAO();
            lista = dao.consultaFiltrada(Integer.parseInt(txtCodBenefModif.getText()), "");
            if (lista != null) {
                for (Beneficio beneficio : lista) {
                    nombre += beneficio.getNombre();
                    break;
                }
            }
        }
        return nombre;
    }

    private void modificarBeneficio() {

        DAO dao = new BeneficioDAO();
        Beneficio b = new Beneficio();
        b.setNombre(txtNombreBenefModif.getText());
        int r = dao.modificar(Integer.parseInt(txtCodBenefModif.getText()), b);

        if (r == 1) {
            JOptionPane.showMessageDialog(this, "Se modificó correctamente.");
            limpiarCamposBeneficio();
            listarBeneficios();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo modificar.");
        }

    }

    private void eliminarBeneficio() {

        DAO dao = new BeneficioDAO();
        int r = dao.eliminar(Integer.parseInt(txtCodBenefModif.getText()));
        if (r == 1) {
            JOptionPane.showMessageDialog(this, "Se elimó correctamente.");
            limpiarCamposBeneficio();
            listarBeneficios();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar.");
        }

    }

    private void limpiarListaBeneficio() {
        int elementos = listaBeneficios.getItemCount();
        for (int i = 0; i < elementos; i++) {
            listaBeneficios.removeItemAt(0);
        }
    }

    //----------------------------------------- METODOS MARCAS -----------------------------------------
    private void limpiarCamposMarca() {
        txtCodMarca.setText("");
        txtNombreMarca.setText("");
        txtCodMarcaModif.setText("");
        txtNombreMarcaModif.setText("");
        //txtNombreMarcaModif.setEnabled(false);
    }

    private void listarMarcas() {

        List<Marca> lista = null;
        limpiarListaMarca();

        DAO dao = new MarcaDAO();
        lista = dao.listar();

        Collections.sort(lista);
        if (lista != null) {
            limpiarListaBeneficio();
            for (Marca b : lista) {
                if (b.getCodMarca() != 0) {
                    listaMarca.addItem(b.getCodMarca() + " - " + b.getNombre());
                }

            }
        }
    }

    private void registrarMarca() {

        if (!txtCodMarca.getText().equals("") && !txtNombreMarca.getText().equals("")) {
            DAO dao = new MarcaDAO();
            Marca b = new Marca(generarNumAleatorio(), txtNombreMarca.getText());
            int r = dao.insertar(b);

            if (r == 1) {
                JOptionPane.showMessageDialog(this, "Se registró correctamente.");
                limpiarCamposMarca();
                listarMarcas();
            } else if (r == 2) {
                JOptionPane.showMessageDialog(this, "No se pudo registrar.");
            } else if (r == 4) {
                JOptionPane.showMessageDialog(this, "El registro con ese código ya existe.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe completar los campos.");
        }

    }

    private String buscarMarca() {
        String nombre = "";
        if (!txtCodMarcaModif.getText().equals("")) {
            List<Marca> lista = null;

            DAO dao = new MarcaDAO();
            lista = dao.consultaFiltrada(Integer.parseInt(txtCodMarcaModif.getText()), "");
            if (lista != null) {
                for (Marca m : lista) {
                    nombre += m.getNombre();
                    break;
                }
            }
        }
        return nombre;
    }

    private void modificarMarca() {

        DAO dao = new MarcaDAO();
        Marca b = new Marca();
        b.setNombre(txtNombreMarcaModif.getText());
        int r = dao.modificar(Integer.parseInt(txtCodMarcaModif.getText()), b);

        if (r == 1) {
            JOptionPane.showMessageDialog(this, "Se modificó correctamente.");
            limpiarCamposMarca();
            listarMarcas();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo modificar.");
        }
    }

    private void eliminarMarca() {
        DAO dao = new MarcaDAO();
        int r = dao.eliminar(Integer.parseInt(txtCodMarcaModif.getText()));
        if (r == 1) {
            JOptionPane.showMessageDialog(this, "Se elimó correctamente.");
            limpiarCamposMarca();
            listarMarcas();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar.");
        }
    }

    private void limpiarListaMarca() {
        int elementos = listaMarca.getItemCount();
        for (int i = 0; i < elementos; i++) {
            listaMarca.removeItemAt(0);
        }
    }

    //----------------------------------------- METODOS ORIENTACION -----------------------------------------
    private void limpiarCamposOrientacion() {
        txtCodOrientacion.setText("");
        txtNombreOrientacion.setText("");
        txtCodOrientacionModif.setText("");
        txtNombreOrientacionModif.setText("");
    }

    private void listarOrientacion() {

        List<Orientacion> lista = null;
        limpiarListaOrientacion();

        DAO dao = new OrientacionDAO();
        lista = dao.listar();

        Collections.sort(lista);
        if (lista != null) {
            for (Orientacion b : lista) {
                if (b.getCodOrientacion() != 0) {
                    listaOrientacion.addItem(b.getCodOrientacion() + " - " + b.getNombre());
                }

            }
        }
    }

    private void registrarOrientacion() {

        if (!txtCodOrientacion.getText().equals("") && !txtNombreOrientacion.getText().equals("")) {
            DAO dao = new OrientacionDAO();
            Orientacion b = new Orientacion(generarNumAleatorio(), txtNombreOrientacion.getText());
            int r = dao.insertar(b);

            if (r == 1) {
                JOptionPane.showMessageDialog(this, "Se registró correctamente.");
                limpiarCamposOrientacion();
                listarOrientacion();
            } else if (r == 2) {
                JOptionPane.showMessageDialog(this, "No se pudo registrar.");
            } else if (r == 4) {
                JOptionPane.showMessageDialog(this, "El registro con ese código ya existe.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe completar los campos.");
        }

    }

    private void modificarOrientacion() {

        DAO dao = new OrientacionDAO();
        Orientacion b = new Orientacion();
        b.setNombre(txtNombreOrientacionModif.getText());
        int r = dao.modificar(Integer.parseInt(txtCodOrientacionModif.getText()), b);

        if (r == 1) {
            JOptionPane.showMessageDialog(this, "Se modificó correctamente.");
            limpiarCamposOrientacion();
            listarOrientacion();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo modificar.");
        }

    }

    //Metodo que me buscara la orientacion segun el código
    private String buscarOrientacion() {
        String nombre = "";
        if (!txtCodOrientacionModif.getText().equals("")) {
            List<Orientacion> lista = null;

            DAO dao = new OrientacionDAO();
            lista = dao.consultaFiltrada(Integer.parseInt(txtCodOrientacionModif.getText()), "");
            if (lista != null) {
                for (Orientacion o : lista) {
                    nombre += o.getNombre();
                    break;
                }
            }
        }
        return nombre;
    }

    private void eliminarOrientacion() {
        DAO dao = new OrientacionDAO();
        int r = dao.eliminar(Integer.parseInt(txtCodOrientacionModif.getText()));
        if (r == 1) {
            JOptionPane.showMessageDialog(this, "Se elimó correctamente.");
            limpiarCamposOrientacion();
            listarOrientacion();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar.");
        }
    }

    private void limpiarListaOrientacion() {
        int elementos = listaOrientacion.getItemCount();
        for (int i = 0; i < elementos; i++) {
            listaOrientacion.removeItemAt(0);
        }
    }

    private void validarCamposTextos(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (Character.isDigit(c)) {
            getToolkit().beep();
            evt.consume();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pestañaPrincipal = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCodOrientacion = new javax.swing.JTextField();
        txtNombreOrientacion = new javax.swing.JTextField();
        btnRegistrarOrientacion = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        listaOrientacion = new javax.swing.JComboBox<>();
        jPanel23 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtNombreOrientacionModif = new javax.swing.JTextField();
        txtCodOrientacionModif = new javax.swing.JLabel();
        btnModificarOrientacion = new javax.swing.JButton();
        btnEliminarOrientacion = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCodMarca = new javax.swing.JTextField();
        txtNombreMarca = new javax.swing.JTextField();
        btnRegistrarMarca = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        listaMarca = new javax.swing.JComboBox<>();
        jPanel16 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtNombreMarcaModif = new javax.swing.JTextField();
        txtCodMarcaModif = new javax.swing.JLabel();
        btnModificarMarca = new javax.swing.JButton();
        btnEliminarMarca = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCodBeneficio = new javax.swing.JTextField();
        txtNombreBeneficio = new javax.swing.JTextField();
        btnRegistrarBenef = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        listaBeneficios = new javax.swing.JComboBox<>();
        jPanel13 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNombreBenefModif = new javax.swing.JTextField();
        txtCodBenefModif = new javax.swing.JLabel();
        btnModificarBeneficio = new javax.swing.JButton();
        btnEliminarBeneficio = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(400, 325));

        pestañaPrincipal.setForeground(new java.awt.Color(0, 102, 51));
        pestañaPrincipal.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        jTabbedPane4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel10.setText("Código Orientación:");
        jLabel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel11.setText("Nombre:");
        jLabel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtCodOrientacion.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCodOrientacion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodOrientacion.setEnabled(false);
        txtCodOrientacion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtCodOrientacionMousePressed(evt);
            }
        });

        txtNombreOrientacion.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNombreOrientacion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombreOrientacion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtNombreOrientacionMousePressed(evt);
            }
        });
        txtNombreOrientacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreOrientacionKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreOrientacionKeyTyped(evt);
            }
        });

        btnRegistrarOrientacion.setBackground(new java.awt.Color(0, 153, 51));
        btnRegistrarOrientacion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnRegistrarOrientacion.setText("REGISTRAR");
        btnRegistrarOrientacion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRegistrarOrientacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarOrientacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRegistrarOrientacion, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCodOrientacion, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreOrientacion)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap(68, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodOrientacion, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreOrientacion, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addComponent(btnRegistrarOrientacion, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 374, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 239, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane4.addTab("Registrar", jPanel11);

        jPanel22.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        listaOrientacion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaOrientacion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaOrientacionItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listaOrientacion, 0, 350, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listaOrientacion, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel23.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel18.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel18.setText("Código:");
        jLabel18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel19.setText("Nombre:");
        jLabel19.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtNombreOrientacionModif.setEditable(false);
        txtNombreOrientacionModif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNombreOrientacionModif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombreOrientacionModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreOrientacionModifKeyTyped(evt);
            }
        });

        txtCodOrientacionModif.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtCodOrientacionModif.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtCodOrientacionModif.setEnabled(false);

        btnModificarOrientacion.setBackground(new java.awt.Color(0, 153, 51));
        btnModificarOrientacion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnModificarOrientacion.setText("MODIFICAR");
        btnModificarOrientacion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificarOrientacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarOrientacionActionPerformed(evt);
            }
        });

        btnEliminarOrientacion.setBackground(new java.awt.Color(0, 153, 51));
        btnEliminarOrientacion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnEliminarOrientacion.setText("ELIMINAR");
        btnEliminarOrientacion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarOrientacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarOrientacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(btnEliminarOrientacion, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnModificarOrientacion, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodOrientacionModif, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel23Layout.createSequentialGroup()
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombreOrientacionModif, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 30, Short.MAX_VALUE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodOrientacionModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreOrientacionModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificarOrientacion, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarOrientacion, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 379, Short.MAX_VALUE)
            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel20Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 239, Short.MAX_VALUE)
            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel20Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 374, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 239, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane4.addTab("Modificar / Eliminar", jPanel8);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4)
        );

        pestañaPrincipal.addTab("Orientación", jPanel3);

        jTabbedPane3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel3.setText("Código Marca:");
        jLabel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel4.setText("Nombre:");
        jLabel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtCodMarca.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCodMarca.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodMarca.setEnabled(false);
        txtCodMarca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCodMarcaMouseClicked(evt);
            }
        });
        txtCodMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodMarcaActionPerformed(evt);
            }
        });

        txtNombreMarca.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNombreMarca.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombreMarca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNombreMarcaMouseClicked(evt);
            }
        });
        txtNombreMarca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreMarcaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreMarcaKeyTyped(evt);
            }
        });

        btnRegistrarMarca.setBackground(new java.awt.Color(0, 153, 51));
        btnRegistrarMarca.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnRegistrarMarca.setText("REGISTRAR");
        btnRegistrarMarca.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRegistrarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarMarcaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRegistrarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCodMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreMarca)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(69, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addComponent(btnRegistrarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 374, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 239, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane3.addTab("Registrar", jPanel6);

        jPanel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        listaMarca.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaMarca.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaMarcaItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listaMarca, 0, 350, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listaMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel13.setText("Código:");
        jLabel13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel14.setText("Nombre:");
        jLabel14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtNombreMarcaModif.setEditable(false);
        txtNombreMarcaModif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNombreMarcaModif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombreMarcaModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreMarcaModifKeyTyped(evt);
            }
        });

        txtCodMarcaModif.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtCodMarcaModif.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtCodMarcaModif.setEnabled(false);

        btnModificarMarca.setBackground(new java.awt.Color(0, 153, 51));
        btnModificarMarca.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnModificarMarca.setText("MODIFICAR");
        btnModificarMarca.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarMarcaActionPerformed(evt);
            }
        });

        btnEliminarMarca.setBackground(new java.awt.Color(0, 153, 51));
        btnEliminarMarca.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnEliminarMarca.setText("ELIMINAR");
        btnEliminarMarca.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarMarcaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(btnEliminarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnModificarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodMarcaModif, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombreMarcaModif, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 30, Short.MAX_VALUE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodMarcaModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreMarcaModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 374, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 239, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane3.addTab("Modificar / Eliminar", jPanel7);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );

        pestañaPrincipal.addTab("Marca", jPanel2);

        jTabbedPane2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Código Beneficio:");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText("Nombre:");
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtCodBeneficio.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCodBeneficio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodBeneficio.setEnabled(false);
        txtCodBeneficio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtCodBeneficioMousePressed(evt);
            }
        });

        txtNombreBeneficio.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNombreBeneficio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombreBeneficio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtNombreBeneficioMousePressed(evt);
            }
        });
        txtNombreBeneficio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreBeneficioKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreBeneficioKeyTyped(evt);
            }
        });

        btnRegistrarBenef.setBackground(new java.awt.Color(0, 153, 51));
        btnRegistrarBenef.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnRegistrarBenef.setText("REGISTRAR");
        btnRegistrarBenef.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRegistrarBenef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarBenefActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRegistrarBenef, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCodBeneficio, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreBeneficio)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(66, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodBeneficio, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreBeneficio, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addComponent(btnRegistrarBenef, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Registro", jPanel4);

        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        listaBeneficios.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaBeneficios.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaBeneficiosItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listaBeneficios, 0, 350, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listaBeneficios, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel8.setText("Código:");
        jLabel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel9.setText("Nombre:");
        jLabel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtNombreBenefModif.setEditable(false);
        txtNombreBenefModif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNombreBenefModif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombreBenefModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreBenefModifKeyTyped(evt);
            }
        });

        txtCodBenefModif.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtCodBenefModif.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtCodBenefModif.setEnabled(false);

        btnModificarBeneficio.setBackground(new java.awt.Color(0, 153, 51));
        btnModificarBeneficio.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnModificarBeneficio.setText("MODIFICAR");
        btnModificarBeneficio.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificarBeneficio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarBeneficioActionPerformed(evt);
            }
        });

        btnEliminarBeneficio.setBackground(new java.awt.Color(0, 153, 51));
        btnEliminarBeneficio.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnEliminarBeneficio.setText("ELIMINAR");
        btnEliminarBeneficio.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarBeneficio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarBeneficioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(btnEliminarBeneficio, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnModificarBeneficio, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodBenefModif, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombreBenefModif, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 30, Short.MAX_VALUE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodBenefModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreBenefModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificarBeneficio, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarBeneficio, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Modificar / Eliminar", jPanel5);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        pestañaPrincipal.addTab("Beneficio", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pestañaPrincipal)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pestañaPrincipal)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodBeneficioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodBeneficioMousePressed
        txtCodBeneficio.setText(String.valueOf(generarNumAleatorio()));
        //txtCodBeneficio.setEnabled(false);
    }//GEN-LAST:event_txtCodBeneficioMousePressed

    private void btnRegistrarBenefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarBenefActionPerformed
        registrarBeneficio();
    }//GEN-LAST:event_btnRegistrarBenefActionPerformed

    private void txtNombreBeneficioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreBeneficioKeyPressed
        if (evt.getKeyCode() == 10) {
            registrarBeneficio();
        }
    }//GEN-LAST:event_txtNombreBeneficioKeyPressed

    private void btnRegistrarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarMarcaActionPerformed
        registrarMarca();
    }//GEN-LAST:event_btnRegistrarMarcaActionPerformed

    private void txtCodMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodMarcaActionPerformed

    }//GEN-LAST:event_txtCodMarcaActionPerformed

    private void txtCodMarcaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodMarcaMouseClicked
        txtCodMarca.setText(String.valueOf(generarNumAleatorio()));
        //txtCodMarca.setEnabled(false);
    }//GEN-LAST:event_txtCodMarcaMouseClicked

    private void btnRegistrarOrientacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarOrientacionActionPerformed
        registrarOrientacion();
    }//GEN-LAST:event_btnRegistrarOrientacionActionPerformed

    private void listaBeneficiosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaBeneficiosItemStateChanged

        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String elemento = elemento = listaBeneficios.getSelectedItem().toString();
            String nro = "";
            for (int i = 0; i < elemento.length(); i++) {
                if (i < 5) {
                    nro += elemento.charAt(i);
                }
            }
            txtCodBenefModif.setText(nro);
            txtNombreBenefModif.setText(buscarBeneficio());
        }
    }//GEN-LAST:event_listaBeneficiosItemStateChanged

    private void listaMarcaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaMarcaItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String elemento = elemento = listaMarca.getSelectedItem().toString();
            String nro = "";
            for (int i = 0; i < elemento.length(); i++) {
                if (i < 5) {
                    nro += elemento.charAt(i);
                }
            }
            txtCodMarcaModif.setText(nro);
            txtNombreMarcaModif.setText(buscarMarca());
        }
    }//GEN-LAST:event_listaMarcaItemStateChanged

    private void btnEliminarBeneficioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarBeneficioActionPerformed
        eliminarBeneficio();
    }//GEN-LAST:event_btnEliminarBeneficioActionPerformed

    private void btnEliminarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarMarcaActionPerformed
        eliminarMarca();
    }//GEN-LAST:event_btnEliminarMarcaActionPerformed

    private void listaOrientacionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaOrientacionItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String elemento = elemento = listaOrientacion.getSelectedItem().toString();
            String nro = "";
            for (int i = 0; i < elemento.length(); i++) {
                if (i < 5) {
                    nro += elemento.charAt(i);
                }
            }
            txtCodOrientacionModif.setText(nro);
            txtNombreOrientacionModif.setText(buscarOrientacion());
        }
    }//GEN-LAST:event_listaOrientacionItemStateChanged

    private void btnEliminarOrientacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarOrientacionActionPerformed
        eliminarOrientacion();
    }//GEN-LAST:event_btnEliminarOrientacionActionPerformed

    private void btnModificarBeneficioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarBeneficioActionPerformed
        modificarBeneficio();
    }//GEN-LAST:event_btnModificarBeneficioActionPerformed

    private void btnModificarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarMarcaActionPerformed
        modificarMarca();
    }//GEN-LAST:event_btnModificarMarcaActionPerformed

    private void btnModificarOrientacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarOrientacionActionPerformed
        modificarOrientacion();
    }//GEN-LAST:event_btnModificarOrientacionActionPerformed

    private void txtNombreOrientacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreOrientacionKeyPressed
        if (evt.getKeyCode() == 10) {
            if (!txtCodOrientacion.getText().equals("") && !txtNombreOrientacion.getText().equals("")) {
                registrarOrientacion();
            }
        }
    }//GEN-LAST:event_txtNombreOrientacionKeyPressed

    private void txtCodOrientacionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodOrientacionMousePressed
        txtCodOrientacion.setText(String.valueOf(generarNumAleatorio()));
    }//GEN-LAST:event_txtCodOrientacionMousePressed

    private void txtNombreBeneficioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreBeneficioKeyTyped
        validarCamposTextos(evt);
    }//GEN-LAST:event_txtNombreBeneficioKeyTyped

    private void txtNombreMarcaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreMarcaKeyTyped
        validarCamposTextos(evt);
    }//GEN-LAST:event_txtNombreMarcaKeyTyped

    private void txtNombreMarcaModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreMarcaModifKeyTyped
        validarCamposTextos(evt);
    }//GEN-LAST:event_txtNombreMarcaModifKeyTyped

    private void txtNombreBenefModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreBenefModifKeyTyped
        validarCamposTextos(evt);
    }//GEN-LAST:event_txtNombreBenefModifKeyTyped

    private void txtNombreOrientacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreOrientacionKeyTyped
        validarCamposTextos(evt);
    }//GEN-LAST:event_txtNombreOrientacionKeyTyped

    private void txtNombreOrientacionModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreOrientacionModifKeyTyped
        validarCamposTextos(evt);
    }//GEN-LAST:event_txtNombreOrientacionModifKeyTyped

    private void txtNombreMarcaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreMarcaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            registrarMarca();
        }
    }//GEN-LAST:event_txtNombreMarcaKeyPressed

    private void txtNombreBeneficioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreBeneficioMousePressed
        txtCodBeneficio.setText(String.valueOf(generarNumAleatorio()));
    }//GEN-LAST:event_txtNombreBeneficioMousePressed

    private void txtNombreMarcaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreMarcaMouseClicked
        txtCodMarca.setText(String.valueOf(generarNumAleatorio()));
    }//GEN-LAST:event_txtNombreMarcaMouseClicked

    private void txtNombreOrientacionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreOrientacionMousePressed
        txtCodOrientacion.setText(String.valueOf(generarNumAleatorio()));
    }//GEN-LAST:event_txtNombreOrientacionMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminarBeneficio;
    private javax.swing.JButton btnEliminarMarca;
    private javax.swing.JButton btnEliminarOrientacion;
    private javax.swing.JButton btnModificarBeneficio;
    private javax.swing.JButton btnModificarMarca;
    private javax.swing.JButton btnModificarOrientacion;
    private javax.swing.JButton btnRegistrarBenef;
    private javax.swing.JButton btnRegistrarMarca;
    private javax.swing.JButton btnRegistrarOrientacion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JComboBox<String> listaBeneficios;
    private javax.swing.JComboBox<String> listaMarca;
    private javax.swing.JComboBox<String> listaOrientacion;
    private javax.swing.JTabbedPane pestañaPrincipal;
    private javax.swing.JLabel txtCodBenefModif;
    private javax.swing.JTextField txtCodBeneficio;
    private javax.swing.JTextField txtCodMarca;
    private javax.swing.JLabel txtCodMarcaModif;
    private javax.swing.JTextField txtCodOrientacion;
    private javax.swing.JLabel txtCodOrientacionModif;
    private javax.swing.JTextField txtNombreBenefModif;
    private javax.swing.JTextField txtNombreBeneficio;
    private javax.swing.JTextField txtNombreMarca;
    private javax.swing.JTextField txtNombreMarcaModif;
    private javax.swing.JTextField txtNombreOrientacion;
    private javax.swing.JTextField txtNombreOrientacionModif;
    // End of variables declaration//GEN-END:variables
}
