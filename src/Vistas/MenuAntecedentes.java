package Vistas;

import Clases.Marca;
import Clases.TrabajosRealizados;
import DAO.DAO;
import DAO.MarcaDAO;
import DAO.PromotoraDAO;
import DAO.TrabajosRealizadosDAO;
import Estilos.HeaderManagement;
import Estilos.RenderTablaConsultaPromotoras;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class MenuAntecedentes extends javax.swing.JDialog {

    private List<Marca> listMarcas = null;
    private List<TrabajosRealizados> listAntecedentes = null;
    private DefaultTableModel dtm = null;
    private int codPromotor;

    public MenuAntecedentes(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("MENU ANTECEDENTES");
        addEscapeListener(this);
        setearTablasConsultaDatos();
        setearFechaTrabajada();
        setearEstiloTablaAntecedentes();
        setListMarcas();
        eventClickPestaña();
    }

    //GETTER AND SETTER
    private void setearFechaTrabajada() {
        fechaTrabajada.setDateFormatString("dd-MM-yyyy");
        fechaTrabajada.setDate(new java.util.Date());
        fechaTrabajada_Modif.setDateFormatString("dd-MM-yyyy");
        fechaTrabajada_Modif.setDate(new java.util.Date());
    }

    private void setearEstiloTablaAntecedentes() {
        RenderTablaConsultaPromotoras rt = new RenderTablaConsultaPromotoras();
        DefaultTableModel dt;
        tableAcontecimientos.setDefaultRenderer(Object.class, rt);

        //Codigo para aplicarle el formato personalizado al encabezado
        JTableHeader jth = tableAcontecimientos.getTableHeader();
        jth.setDefaultRenderer(new HeaderManagement());
        tableAcontecimientos.setTableHeader(jth);

        //Codigo para aplicarme los formatos personalizados
        tableAcontecimientos.getColumnModel().getColumn(0).setCellRenderer(new RenderTablaConsultaPromotoras());
        tableAcontecimientos.getColumnModel().getColumn(1).setCellRenderer(new RenderTablaConsultaPromotoras());
        tableAcontecimientos.getColumnModel().getColumn(2).setCellRenderer(new RenderTablaConsultaPromotoras());
        tableAcontecimientos.getColumnModel().getColumn(3).setCellRenderer(new RenderTablaConsultaPromotoras());
        tableAcontecimientos.getColumnModel().getColumn(4).setCellRenderer(new RenderTablaConsultaPromotoras());

        //Codigo para especificar el tamaño de las celdas
        tableAcontecimientos.setRowHeight(20);

        TableColumn column_Descripcion = tableAcontecimientos.getColumn("Descripcion");
        column_Descripcion.setMinWidth(350);

        TableColumn column_Horas = tableAcontecimientos.getColumn("Horas");
        column_Horas.setMaxWidth(50);

        //Codigo para no poder escribir las celdas
        //tableAcontecimientos.setDefaultEditor(Object.class, null);
    }

    public void set_NombrePromotor(String nombre) {
        lbl_NombrePromotor_Consulta.setText(nombre.toUpperCase());
    }

    public void set_CodPromotor(String DNI) {

        PromotoraDAO dao = new PromotoraDAO();
        codPromotor = dao.buscarCodPromotor(Integer.parseInt(DNI));

        fillTableListAcontecimientos();
    }

    private void eventClickPestaña() {
        pestañas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (pestañas.getSelectedIndex() == 0) {
                    limpiarTablaDatos();
                    fillTableListAcontecimientos();
                }
                if (pestañas.getSelectedIndex() == 1) {
                    setearFechaTrabajada();
                }
            }
        });
    }

    private int generarCodAntecedente() {

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

    private void setearTablasConsultaDatos() {

        dtm = new DefaultTableModel();
        String[] columnas = {"Código", "Marca", "Fecha", "Horas", "Descripcion"};
        dtm.setColumnIdentifiers(columnas);
        tableAcontecimientos.setModel(dtm);
    }

    private void getListMarcas() {
        listMarcas = new ArrayList<>();
        DAO dao = new MarcaDAO();
        listMarcas = dao.listar();
    }

    private void setListMarcas() {
        getListMarcas();
        listMarcas.stream().forEach(m -> {
            list_Marcas.addItem(m.getCodMarca() + "  -  " + m.getNombre());
            list_Marcas_Modif.addItem(m.getCodMarca() + "  -  " + m.getNombre());
        });
    }

    private void getListAntecedentes() {
        DAO dao = new TrabajosRealizadosDAO();
        listAntecedentes = dao.listar();
    }

    private void getListByCodPromotor() {
        DAO dao = new TrabajosRealizadosDAO();
        listAntecedentes = dao.consultaFiltrada(codPromotor, "");
    }

    private void fillTableListAcontecimientos() {
        getListByCodPromotor();
        String filas[] = new String[5];
        listAntecedentes.stream().sorted().forEach(ant -> {
            filas[0] = String.valueOf(ant.getCodTrabajo());
            filas[1] = getNombreMarcaByCodMarca(ant.getCodMarca());
            filas[2] = ant.getFecha().toString();
            if (ant.getFecha() != null) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                filas[2] = String.valueOf(format.format(ant.getFecha()));
            } else {
                filas[2] = "";
            }
            filas[3] = String.valueOf(ant.getHoras());
            filas[4] = ant.getDescripcion();

            dtm.addRow(filas);
        });
        tableAcontecimientos.setModel(dtm);
    }

    private String getNombreMarcaByCodMarca(int codMarca) {
        DAO dao = new MarcaDAO();
        List<Marca> lMarca = dao.consultaFiltrada(codMarca, "");
        String nombreMarca = "";

        for (Marca marca : lMarca) {
            nombreMarca = marca.getNombre();
            break;
        }
        return nombreMarca;
    }

    private void saveNewAntecedente() {

        int codAntecedente = generarCodAntecedente();
        String codPromotor = String.valueOf(this.codPromotor);
        Date fecha = new java.sql.Date(fechaTrabajada.getDate().getTime());
        int horas = 0;
        if (!txt_HorasTrabajadas.getText().equals("")) {
            horas = Integer.parseInt(txt_HorasTrabajadas.getText());
        }

        int codMarca = Integer.parseInt(list_Marcas.getSelectedItem().toString().replaceAll("[^\\.0123456789]", ""));
        String descripcion = txt_Descripcion.getText();

        DAO dao = null;
        TrabajosRealizados tr = null;

        if (!codPromotor.equals("")) {
            dao = new TrabajosRealizadosDAO();
            tr = new TrabajosRealizados(codAntecedente, fecha, horas, codMarca, Integer.parseInt(codPromotor), descripcion);

            int r = dao.insertar(tr);
            if (r == 1) {
                JOptionPane.showMessageDialog(this, "El antecedente se cargó correctamente");
                this.limpiarCamposRegistrar();
            }
        }
    }

    private void updateAntecedente() {
        DAO dao = new TrabajosRealizadosDAO();

        TrabajosRealizados tr = new TrabajosRealizados();
        tr.setCodTrabajo(Integer.parseInt(text_CodAntecedente_Modif.getText()));
        tr.setCodPromotor(codPromotor);
        tr.setFecha(new java.sql.Date(fechaTrabajada_Modif.getDate().getTime()));
        tr.setHoras(Integer.parseInt(txt_HorasTrabajadas_Modif.getText()));
        tr.setCodMarca(Integer.parseInt(list_Marcas_Modif.getSelectedItem().toString().replaceAll("[^\\.0123456789]", "")));
        tr.setDescripcion(txt_Descripcion_Modif.getText());

        int r = dao.modificar(Integer.parseInt(text_CodAntecedente_Modif.getText()), tr);
        if (r == 1) {
            JOptionPane.showMessageDialog(this, "El antecedente se actualizó correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo realizar la modificación. Cierre e intente nuevamente.");
        }

    }

    private void limpiarCamposRegistrar() {
        fechaTrabajada.setDateFormatString("");
        txt_HorasTrabajadas.setText("");
        txt_Descripcion.setText("");
        list_Marcas.setSelectedIndex(0);
    }

    private void modificarRapido() {
        try {

            pestañas.setSelectedIndex(2);

            int fila = tableAcontecimientos.getSelectedRow();
            String codAcontecimiento = (String) tableAcontecimientos.getValueAt(fila, 0);
            String nombreMarca = ((String) tableAcontecimientos.getValueAt(fila, 1)).replace(" ", "");
            String fecha = (String) tableAcontecimientos.getValueAt(fila, 2);
            String horas = (String) tableAcontecimientos.getValueAt(fila, 3);
            String descripcion = (String) tableAcontecimientos.getValueAt(fila, 4);

            text_CodAntecedente_Modif.setText(codAcontecimiento);
            fechaTrabajada_Modif.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(fecha));
            txt_HorasTrabajadas_Modif.setText(horas);
            txt_Descripcion_Modif.setText(descripcion);

            int orientacion = list_Marcas_Modif.getItemCount();
            for (int i = 0; i < orientacion; i++) {
                String elemento = list_Marcas_Modif.getItemAt(i).replaceAll("[^a-zA-Z]", "");
                if (elemento.equalsIgnoreCase(nombreMarca)) {
                    list_Marcas_Modif.setSelectedIndex(i);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return;
    }

    private void eliminarRapido() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea eliminarlo?", "Eliminar antecedente", JOptionPane.YES_NO_OPTION);
        if (respuesta == 0) {
            int fila = tableAcontecimientos.getSelectedRow();
            String codAntecedente = (String) tableAcontecimientos.getValueAt(fila, 0);

            DAO dao = new TrabajosRealizadosDAO();
            int r = dao.eliminar(Integer.parseInt(codAntecedente));
            if (r == 1) {
                limpiarTablaDatos();
                // listAntecedentes.removeIf(c -> String.valueOf(c.getCodTrabajo()).trim().equals(String.valueOf(codAntecedente).trim()));
                fillTableListAcontecimientos();
                JOptionPane.showMessageDialog(null, "El antecedente fué eliminado correctamente.");

            }
        }
        return;
    }

    private void limpiarTablaDatos() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tableAcontecimientos.getModel();
            int filas = tableAcontecimientos.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
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

        pestañas = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAcontecimientos = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        lbl_NombrePromotor_Consulta = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        list_Marcas = new javax.swing.JComboBox<>();
        btnGuardar = new javax.swing.JButton();
        txt_HorasTrabajadas = new javax.swing.JTextField();
        fechaTrabajada = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_Descripcion = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        list_Marcas_Modif = new javax.swing.JComboBox<>();
        btnModificar = new javax.swing.JButton();
        txt_HorasTrabajadas_Modif = new javax.swing.JTextField();
        fechaTrabajada_Modif = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txt_Descripcion_Modif = new javax.swing.JTextArea();
        jLabel15 = new javax.swing.JLabel();
        text_CodAntecedente_Modif = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pestañas.setForeground(new java.awt.Color(0, 102, 51));
        pestañas.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        jPanel1.setPreferredSize(new java.awt.Dimension(788, 380));
        jPanel1.setVerifyInputWhenFocusTarget(false);

        tableAcontecimientos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableAcontecimientos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableAcontecimientos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableAcontecimientosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tableAcontecimientos);

        lbl_NombrePromotor_Consulta.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lbl_NombrePromotor_Consulta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_NombrePromotor_Consulta, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_NombrePromotor_Consulta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pestañas.addTab("Consultar", jPanel1);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText("Fecha:");
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel3.setText("Horas:");
        jLabel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel4.setText("Marca:");
        jLabel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        list_Marcas.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        list_Marcas.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnGuardar.setBackground(new java.awt.Color(0, 153, 51));
        btnGuardar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("GUARDAR");
        btnGuardar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        txt_HorasTrabajadas.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txt_HorasTrabajadas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_HorasTrabajadas.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_HorasTrabajadas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_HorasTrabajadasKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_HorasTrabajadasKeyTyped(evt);
            }
        });

        fechaTrabajada.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaTrabajada.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        fechaTrabajada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fechaTrabajadaKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel6.setText("Descripción:");
        jLabel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txt_Descripcion.setColumns(20);
        txt_Descripcion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txt_Descripcion.setRows(5);
        txt_Descripcion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jScrollPane2.setViewportView(txt_Descripcion);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(list_Marcas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fechaTrabajada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_HorasTrabajadas, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fechaTrabajada, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_HorasTrabajadas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(list_Marcas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        pestañas.addTab("Registrar", jPanel2);

        jLabel11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel11.setText("Fecha:");
        jLabel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel12.setText("Horas:");
        jLabel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel13.setText("Marca:");
        jLabel13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        list_Marcas_Modif.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        list_Marcas_Modif.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnModificar.setBackground(new java.awt.Color(0, 153, 51));
        btnModificar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setText("MODIFICAR");
        btnModificar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        txt_HorasTrabajadas_Modif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txt_HorasTrabajadas_Modif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_HorasTrabajadas_Modif.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_HorasTrabajadas_Modif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_HorasTrabajadas_ModifKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_HorasTrabajadas_ModifKeyTyped(evt);
            }
        });

        fechaTrabajada_Modif.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fechaTrabajada_Modif.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        fechaTrabajada_Modif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fechaTrabajada_ModifKeyPressed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel14.setText("Descripción:");
        jLabel14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txt_Descripcion_Modif.setColumns(20);
        txt_Descripcion_Modif.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txt_Descripcion_Modif.setRows(5);
        txt_Descripcion_Modif.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jScrollPane4.setViewportView(txt_Descripcion_Modif);

        jLabel15.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel15.setText("Código:");
        jLabel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        text_CodAntecedente_Modif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        text_CodAntecedente_Modif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        text_CodAntecedente_Modif.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        text_CodAntecedente_Modif.setEnabled(false);
        text_CodAntecedente_Modif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                text_CodAntecedente_ModifKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                text_CodAntecedente_ModifKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(text_CodAntecedente_Modif, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(list_Marcas_Modif, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fechaTrabajada_Modif, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_HorasTrabajadas_Modif, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(146, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_CodAntecedente_Modif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fechaTrabajada_Modif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_HorasTrabajadas_Modif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(list_Marcas_Modif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(119, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 788, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 428, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pestañas.addTab("Modificar", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_HorasTrabajadasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_HorasTrabajadasKeyPressed

    }//GEN-LAST:event_txt_HorasTrabajadasKeyPressed

    private void txt_HorasTrabajadasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_HorasTrabajadasKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_txt_HorasTrabajadasKeyTyped

    private void fechaTrabajadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fechaTrabajadaKeyPressed
        //fechaNacimiento.setTransferHandler(null);
    }//GEN-LAST:event_fechaTrabajadaKeyPressed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        saveNewAntecedente();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        updateAntecedente();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void txt_HorasTrabajadas_ModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_HorasTrabajadas_ModifKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_HorasTrabajadas_ModifKeyPressed

    private void txt_HorasTrabajadas_ModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_HorasTrabajadas_ModifKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_HorasTrabajadas_ModifKeyTyped

    private void fechaTrabajada_ModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fechaTrabajada_ModifKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaTrabajada_ModifKeyPressed

    private void tableAcontecimientosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableAcontecimientosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            modificarRapido();
        } else if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            eliminarRapido();
        }
    }//GEN-LAST:event_tableAcontecimientosKeyPressed

    private void text_CodAntecedente_ModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_CodAntecedente_ModifKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_CodAntecedente_ModifKeyPressed

    private void text_CodAntecedente_ModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_CodAntecedente_ModifKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_text_CodAntecedente_ModifKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private com.toedter.calendar.JDateChooser fechaTrabajada;
    private com.toedter.calendar.JDateChooser fechaTrabajada_Modif;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbl_NombrePromotor_Consulta;
    private javax.swing.JComboBox<String> list_Marcas;
    private javax.swing.JComboBox<String> list_Marcas_Modif;
    private javax.swing.JTabbedPane pestañas;
    private javax.swing.JTable tableAcontecimientos;
    private javax.swing.JTextField text_CodAntecedente_Modif;
    private javax.swing.JTextArea txt_Descripcion;
    private javax.swing.JTextArea txt_Descripcion_Modif;
    private javax.swing.JTextField txt_HorasTrabajadas;
    private javax.swing.JTextField txt_HorasTrabajadas_Modif;
    // End of variables declaration//GEN-END:variables
}
