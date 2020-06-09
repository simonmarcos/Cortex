package Vistas;

import Clases.Beneficio;
import Clases.Marca;
import Clases.Orientacion;
import Clases.Promotora;
import DAO.BeneficioDAO;
import DAO.DAO;
import DAO.MarcaDAO;
import DAO.OrientacionDAO;
import DAO.PromotoraDAO;
import Estilos.HeaderManagement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import Estilos.RenderTablaConsultaPromotoras;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.table.JTableHeader;

public class MenuPromotora extends javax.swing.JDialog {

    private DefaultTableModel dftDatos = null;
    private DefaultTableModel dftApariencia = null;

    //Variable global para que al limpiar la tabla de los datos tmb eliminar datos de la lista
    private List<Promotora> listaTablaDatos = null;
    private List<Promotora> listaTablaPorEstado = null;
    private List<Orientacion> listaOrientacion = null;
    private List<Marca> listaMarca = null;
    private List<Beneficio> listaBeneficios = null;

    //Variable que me guardara la lista de todos los clientes al filtrarlos
    private List<Promotora> listaTablaDatosFiltrado = null;
    private List<Promotora> listaTablaAparienciaFiltrado = null;

    public MenuPromotora(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        pestañas.setFocusable(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Menú Promotores");
        setearFechaNacimiento();
        eventoFechaNacimiento();
        eventoFechaNacimientoModif();
        setearTablasConsultaDatos();
        setearTablasConsultaApariencia();
        eventoCambiarPestañasConsultar();
        eventoCambiarPestañas();
        lblShorcut();
        setearPopup();
        eventoPopup();
        fechaRegistro();
        llenarTablaDatos();
        getListaOrientacion();
        getListaMarca();
        getListaBeneficios();
        setearEstiloTablaDatos();
        setearEstiloTablaApariencia();
        updateTableDatos();
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

    //ACTUALIZAREMOS LA TABLE DE DATOS CADA VEZ QUE HAGAMOS CLICK EN LA PESTAÑA DE CONSULTA
    private void updateTableDatos() {
        pestañas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (pestañas.getSelectedIndex() == 0) {
                    limpiarTablaDatos();
                    llenarTablaDatos();
                }

            }
        });
    }

    private void fechaRegistro() {

        LocalDate ld = LocalDate.now();
        lblFechaRegistro.setText(ld.format(DateTimeFormatter.ISO_DATE));
    }

    //METODOS SOBRE EL POPUP MENU
    private void setearPopup() {

        menuModificar.setText("Modificar");
        menuEliminar.setText("Eliminar");
        menuBook.setText("Book Fotos");
    }

    //Eventos al hacer clic derecho sobre la tabla
    private void eventoPopup() {

        menuModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarRapido("Personales");
            }
        });

        menuEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarRapido("Personales");
            }
        });

        menuBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookFotoRapido("Personales");
            }
        });
    }

    //Evento que se generara al cambiar de pestañas de CONSULTAR
    private void eventoCambiarPestañasConsultar() {
        pestañasConsultar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (pestañasConsultar.getSelectedIndex() == 0) {
                    deshabilitarConsultaApariencia();
                    limpiarTablaApariencia();
                } else if (pestañasConsultar.getSelectedIndex() == 1) {
                    fillListOfOrientacion("Consultar");
                }
            }
        });
    }

    private void eventoCambiarPestañas() {
        pestañas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (pestañas.getSelectedIndex() == 1) {
                    fillListOfOrientacion("Registrar");
                    listarMarcasRegistrar();
                    listarBeneficiosRegistrar();
                }
            }
        });
    }

    //------------------------------------------------------------------------------------------------------------
    //METODOS PARA MODIFICAR Y ELIMINAR y BOOK FOTO PROMOTOR DESDE EL POPUP MENU O DESDE EL EVENTO DE TECLADO
    private void modificarRapido(String nombreTabla) {

        if (nombreTabla.equals("Personales")) {
            try {
                int fila = tablaConsultaDatos.getSelectedRow();
                String dni = (String) tablaConsultaDatos.getValueAt(fila, 2);

                pestañas.setSelectedIndex(2);
                txtDNIModificar.setText(dni);
                if (!txtDNIModificar.getText().equals("")) {
                    buscarPromotora();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return;
        }
        if (nombreTabla.equals("Apariencia")) {
            try {
                int fila = tablaConsultaApariencia.getSelectedRow();
                String dni = (String) tablaConsultaApariencia.getValueAt(fila, 0);

                pestañas.setSelectedIndex(2);
                txtDNIModificar.setText(dni);
                if (!txtDNIModificar.getText().equals("")) {
                    buscarPromotora();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return;
        }
    }

    private void eliminarRapido(String nombreTabla) {

        if (nombreTabla.equals("Personales")) {
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea eliminarlo?", "Eliminar promotor", JOptionPane.YES_NO_OPTION);
            if (respuesta == 0) {
                int fila = tablaConsultaDatos.getSelectedRow();
                String dni = (String) tablaConsultaDatos.getValueAt(fila, 2);
                String nombre = (String) tablaConsultaDatos.getValueAt(fila, 1);

                PromotoraDAO dao = new PromotoraDAO();
                int r = dao.eliminarBookFotoYPromotor(Integer.parseInt(dni));
                if (r == 1) {
                    listaTablaDatos.removeIf(c -> String.valueOf(c.getDNI()).equalsIgnoreCase(dni));
                    GaleriaFotos.deleteBockPhotoFolder(dni);
                    limpiarTablaDatos();
                    fillTableListPromotor(listaTablaDatos);
                    txtBusquedaDatos.setText("");
                    JOptionPane.showMessageDialog(null, "El promotor " + nombre.toUpperCase() + " fué eliminado.");

                }
            }
            return;
        }
        if (nombreTabla.equals("Apariencia")) {
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea eliminarlo?", "Eliminar promotor", JOptionPane.YES_NO_OPTION);
            if (respuesta == 0) {
                int fila = tablaConsultaApariencia.getSelectedRow();
                String dni = (String) tablaConsultaApariencia.getValueAt(fila, 2);
                String nombre = (String) tablaConsultaApariencia.getValueAt(fila, 1);

                PromotoraDAO dao = new PromotoraDAO();
                int r = dao.eliminarBookFotoYPromotor(Integer.parseInt(dni));
                if (r == 1) {
                    listaTablaAparienciaFiltrado.removeIf(c -> String.valueOf(c.getDNI()).equalsIgnoreCase(dni));
                    listaTablaDatos.removeIf(c -> String.valueOf(c.getDNI()).equalsIgnoreCase(dni));
                    GaleriaFotos.deleteBockPhotoFolder(dni);
                    limpiarTablaApariencia();
                    //fillTableListPromotorAparence(listaTablaAparienciaFiltrado);
                    listaConsultaCabello.setSelectedIndex(0);
                    listaConsultaOjos.setSelectedIndex(0);
                    listaConsultaOrientacion.setSelectedIndex(0);
                    JOptionPane.showMessageDialog(null, "El promotor " + nombre.toUpperCase() + " fué eliminado.");

                }
            }
        }
    }

    private void bookFotoRapido(String nombreTabla) {

        if (nombreTabla.equals(("Personales"))) {
            try {
                GaleriaFotos gf = new GaleriaFotos(null, true);

                int fila = tablaConsultaDatos.getSelectedRow();
                String elemento = "";

                elemento = (String) tablaConsultaDatos.getValueAt(fila, 2);
                gf.setDniPromotor(elemento);
                gf.buscarBookFoto();
                gf.setVisible(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return;
        }

        if (nombreTabla.equals(("Apariencia"))) {
            try {
                GaleriaFotos gf = new GaleriaFotos(null, true);

                int fila = tablaConsultaApariencia.getSelectedRow();
                String elemento = "";

                elemento = (String) tablaConsultaApariencia.getValueAt(fila, 0);
                gf.setDniPromotor(elemento);
                gf.buscarBookFoto();
                gf.setVisible(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void detallePrestacionRapido(String nombreTabla) {

        if (nombreTabla.equals(("Personales"))) {
            try {
                MenuDetallesPrestacion gf = new MenuDetallesPrestacion(null, true);

                int fila = tablaConsultaDatos.getSelectedRow();
                String elemento = "";

                elemento = (String) tablaConsultaDatos.getValueAt(fila, 2);
                gf.setLabelDNIPromotor(elemento);
                gf.llenarCamposConDatos();
                gf.setVisible(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return;
        }

        if (nombreTabla.equals(("Apariencia"))) {
            try {
                GaleriaFotos gf = new GaleriaFotos(null, true);

                int fila = tablaConsultaApariencia.getSelectedRow();
                String elemento = "";

                elemento = (String) tablaConsultaApariencia.getValueAt(fila, 0);
                gf.setDniPromotor(elemento);
                gf.buscarBookFoto();
                gf.setVisible(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void antecedentesRapido(String nombreTabla) {

        if (nombreTabla.equals(("Personales"))) {
            try {
                MenuAntecedentes gf = new MenuAntecedentes(null, true);

                int fila = tablaConsultaDatos.getSelectedRow();
                String elemento = "";
                elemento = (String) tablaConsultaDatos.getValueAt(fila, 2);

                String nombres = (String) tablaConsultaDatos.getValueAt(fila, 0) + " " + (String) tablaConsultaDatos.getValueAt(fila, 1);

                gf.set_NombrePromotor(nombres);
                gf.set_CodPromotor(elemento);
                gf.setVisible(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return;
        }

        if (nombreTabla.equals(("Apariencia"))) {
            try {
                MenuAntecedentes gf = new MenuAntecedentes(null, true);

                int fila = tablaConsultaApariencia.getSelectedRow();
                String elemento = "";
                elemento = (String) tablaConsultaApariencia.getValueAt(fila, 2);

                String nombres = (String) tablaConsultaApariencia.getValueAt(fila, 0) + " " + (String) tablaConsultaApariencia.getValueAt(fila, 1);

                gf.set_NombrePromotor(nombres);
                gf.set_CodPromotor(elemento);
                gf.setVisible(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------
    //--------------------------------------- LISTAR ORIENTACION ---------------------------------------
    private void getListaOrientacion() {
        listaOrientacion = new ArrayList<>();

        DAO dao = new OrientacionDAO();
        listaOrientacion = dao.listar();
    }

    private void getListaMarca() {
        limpiarListaMarcas();

        DAO dao = new MarcaDAO();
        listaMarca = dao.listar();
    }

    private void getListaBeneficios() {
        limpiarListaBeneficios();

        DAO dao = new BeneficioDAO();
        listaBeneficios = dao.listar();
    }

    //Completamos las listas correspondientes con la informacion obtenida de la base de datos
    private void fillListOfOrientacion(String nombreLista) {
        limpiarListaOrientacionRegistrar();
        for (Orientacion orientacion : listaOrientacion) {
            if (orientacion.getCodOrientacion() != 0) {
                if (nombreLista.equals("Registrar")) {
                    listaOrientacionRegistrar.addItem(orientacion.getCodOrientacion() + "  -  " + orientacion.getNombre());
                }
                if (nombreLista.equals("Consultar")) {
                    listaConsultaOrientacion.addItem(orientacion.getCodOrientacion() + "  -  " + orientacion.getNombre());
                }
                if (nombreLista.equals("Modificar")) {
                    listaOrientacionModif.addItem(orientacion.getCodOrientacion() + "  -  " + orientacion.getNombre());
                }
            }
        }
    }

    private void listarMarcasRegistrar() {
        limpiarListaMarcas();
        for (Marca m : listaMarca) {
            if (m.getCodMarca() != 0) {
                listaMarcaRegistrar.addItem(m.getCodMarca() + "  -  " + m.getNombre());
            }
        }
    }

    private void listarBeneficiosRegistrar() {
        limpiarListaBeneficios();
        for (Beneficio b : listaBeneficios) {
            if (b.getCodBeneficio() != 0) {
                listaBeneficiosRegistrar.addItem(b.getCodBeneficio() + "  -  " + b.getNombre());
            }
        }
    }

    private void listarOrientacionModif() {

        List<Orientacion> lista = null;
        limpiarListaOrientacionModif();

        DAO dao = new OrientacionDAO();
        lista = dao.listar();

        for (Orientacion orientacion : lista) {
            if (orientacion.getCodOrientacion() != 0) {
                listaOrientacionModif.addItem(orientacion.getCodOrientacion() + "  -  " + orientacion.getNombre());
            }

        }
    }

    private void listarMarcasModif() {

        List<Marca> lista = null;
        limpiarListaMarcasModif();

        DAO dao = new MarcaDAO();
        lista = dao.listar();

        for (Marca m : lista) {
            if (m.getCodMarca() != 0) {
                listaMarcaModif.addItem(m.getCodMarca() + "  -  " + m.getNombre());
            }
        }
    }

    private void listarBeneficiosModif() {

        List<Beneficio> lista = null;
        limpiarListaBeneficiosModif();

        DAO dao = new BeneficioDAO();
        lista = dao.listar();

        for (Beneficio b : lista) {
            if (b.getCodBeneficio() != 0) {
                listaBeneficiosModif.addItem(b.getCodBeneficio() + "  -  " + b.getNombre());
            }

        }
    }

    private void limpiarListaMarcas() {
        int elementos = listaMarcaRegistrar.getItemCount();
        for (int i = 0; i < elementos; i++) {
            listaMarcaRegistrar.removeItemAt(0);
        }
        listaMarcaRegistrar.addItem("- - -");
    }

    private void limpiarListaOrientacionRegistrar() {
        int elementos = listaOrientacionRegistrar.getItemCount();
        for (int i = 0; i < elementos; i++) {
            listaOrientacionRegistrar.removeItemAt(0);
        }
        listaOrientacionRegistrar.addItem("- - -");
    }

    private void limpiarListaConsultaOrientacion() {
        int elementos = listaConsultaOrientacion.getItemCount();
        for (int i = 0; i < elementos; i++) {
            listaConsultaOrientacion.removeItemAt(0);
        }
        listaConsultaOrientacion.addItem("- - -");
    }

    private void limpiarListaBeneficios() {
        int elementos = listaBeneficiosRegistrar.getItemCount();
        for (int i = 0; i < elementos; i++) {
            listaBeneficiosRegistrar.removeItemAt(0);
        }
        listaBeneficiosRegistrar.addItem("- - -");
    }

    private void limpiarListaOrientacionModif() {
        int elementos = listaOrientacionModif.getItemCount();
        for (int i = 0; i < elementos; i++) {
            listaOrientacionModif.removeItemAt(0);
        }
        listaOrientacionModif.addItem("- - -");
    }

    private void limpiarListaMarcasModif() {
        int elementos = listaMarcaModif.getItemCount();
        for (int i = 0; i < elementos; i++) {
            listaMarcaModif.removeItemAt(0);
        }
        listaMarcaModif.addItem("- - -");
    }

    private void limpiarListaBeneficiosModif() {
        int elementos = listaBeneficiosModif.getItemCount();
        for (int i = 0; i < elementos; i++) {
            listaBeneficiosModif.removeItemAt(0);
        }
        listaBeneficiosModif.addItem("- - -");
    }

    //DAR FORMATO A LA FECHA QUE SE INGRESARA EN EL JDATECHOOSER
    private void setearFechaNacimiento() {
        fechaNacimiento.setDateFormatString("dd-MM-yyyy");
        fechaNacimiento.setDateFormatString("dd-MM-yyyy");
    }

    //METODO PARA CALCULAR LA EDAD A PARTIR DE LA FECHA DE NACIMIENTO QUE SE INGRESA
    private int calcularEdad(String fecha) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNac = LocalDate.parse(fecha, fmt);
        LocalDate ahora = LocalDate.now();

        Period periodo = Period.between(fechaNac, ahora);
        return periodo.getYears();
    }

    //EVENTO AL HACER CLICK SOBRE EL JDATECHOOSER
    private void eventoFechaNacimiento() {
        fechaNacimiento.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    txtEdad.setText(String.valueOf(calcularEdad(new java.sql.Date(fechaNacimiento.getDate().getTime()).toString())));
                } catch (Exception e) {
                }
            }
        });
    }

    private void eventoFechaNacimientoModif() {
        fechaNacimientoModif.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    txtEdadModif.setText(String.valueOf(calcularEdad(new java.sql.Date(fechaNacimientoModif.getDate().getTime()).toString())));
                } catch (Exception e) {
                }
            }
        });
    }

    private void deshabilitarConsultaApariencia() {
        listaConsultaCabello.setSelectedIndex(0);
        listaConsultaOjos.setSelectedIndex(0);
        listaConsultaOrientacion.setSelectedIndex(0);
        limpiarListaConsultaOrientacion();
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDNI.setText("");
        txtDomicilio.setText("");
        txtEdad.setText("");
        txtTel.setText("");
        txtFacebook.setText("");
        txtInstragram.setText("");
        fechaNacimiento.setCalendar(null);

        limpiarListaBeneficios();
        limpiarListaMarcas();
    }

    private void limpiarCamposModif() {

        txtDNIModificar.setText("");
        txtNombreModif.setText("");
        txtApellidoModif.setText("");
        txtDNIModif.setText("");
        txtDomicilioModif.setText("");
        txtEdadModif.setText("");
        txtTelModif.setText("");
        txtFacebookModif.setText("");
        txtInstragramModif.setText("");
        fechaNacimientoModif.setCalendar(null);

        numAlturaModif.setText("");
        numBustosModif.setText("");
        numCinturaModif.setText("");
        numColaModif.setText("");
        numCalzadoModif.setText("");

        lblFechaRegistroModif.setText("");
        limpiarListaBeneficiosModif();
        limpiarListaMarcasModif();
        limpiarListaOrientacionModif();

    }

    private void setearTablasConsultaDatos() {

        dftDatos = new DefaultTableModel();
        String[] columnas = {"Apellido", "Nombre", "DNI", "Fecha Nacimiento", "Edad", "Dirección", "Tel", "Orientacion", "Marca", "Estado", "Movilidad"};
        dftDatos.setColumnIdentifiers(columnas);
        tablaConsultaDatos.setModel(dftDatos);
    }

    private void setearTablasConsultaApariencia() {

        dftApariencia = new DefaultTableModel();
        String[] columnas = {"DNI", "Apellido", "Nombre", "Edad", "Instagram", "Facebook", "Cabello", "Ojos", "Altura", "Medidas"};
        dftApariencia.setColumnIdentifiers(columnas);
        tablaConsultaApariencia.setModel(dftApariencia);

    }

    private void llenarTablaDatos() {

        DAO dao = new PromotoraDAO();
        listaTablaDatos = new ArrayList<>();
        listaTablaDatos = dao.listar();
        labelContador.setText("");
        labelContador.setText("Promotores: " + listaTablaDatos.size());

        if (listaTablaDatos != null) {
            String fila[] = new String[11];
            Collections.sort(listaTablaDatos);
            listaTablaDatos.stream().sorted().forEach(p -> {
                fila[0] = p.getApellido();
                fila[1] = p.getNombre();
                fila[2] = String.valueOf(p.getDNI());
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                fila[3] = format.format(p.getFechaNacimiento());
                fila[4] = String.valueOf(p.getEdad());
                fila[5] = p.getDomicilio();
                fila[6] = p.getTelefono();

                int codOrientacion = p.getOrientacion();
                DAO dOrientacion = new OrientacionDAO();
                List<Orientacion> l = dOrientacion.consultaFiltrada(codOrientacion, "");
                String orientacion = "";
                for (Orientacion o : l) {
                    orientacion += o.getNombre();
                }
                fila[7] = orientacion;

                int codMarca = p.getCodMarca();
                DAO dMarca = new MarcaDAO();
                List<Marca> lMarca = dMarca.consultaFiltrada(codMarca, "");
                String marca = "";
                for (Marca o : lMarca) {
                    marca += o.getNombre();
                }

                fila[8] = marca;
                fila[9] = p.getEstado();
                fila[10] = p.getMovilidad();

                dftDatos.addRow(fila);
            });
        }

        tablaConsultaDatos.setModel(dftDatos);
    }

    //Metodo que me filtrara la tabla, segun lo tipeado (nombre, apellido)
    private void filterTablePromotor(String text) {
        listaTablaDatosFiltrado = new ArrayList<>();
        if (listaTablaDatos != null) {
            if (listaTablaDatos.size() > 0) {
                listaTablaDatos.stream().forEach(p -> {
                    String name = p.getNombre().toLowerCase();
                    String lastName = p.getApellido().toLowerCase();
                    String dni = String.valueOf(p.getDNI());
                    String estado = p.getEstado().toLowerCase();
                    if (name.contains(text.toLowerCase()) || lastName.contains(text.toLowerCase()) || dni.contains(text) || estado.contains(text.toLowerCase())) {
                        Promotora promotor = new Promotora();
                        promotor.setCodPromotor(p.getCodPromotor());
                        promotor.setNombre(name);
                        promotor.setApellido(lastName);
                        promotor.setDNI(p.getDNI());
                        promotor.setTelefono(p.getTelefono());
                        promotor.setFechaNacimiento(p.getFechaNacimiento());
                        promotor.setFechaNacimiento(p.getFechaNacimiento());
                        promotor.setEdad(p.getEdad());
                        promotor.setDomicilio(p.getDomicilio());
                        promotor.setTelefono(p.getTelefono());
                        promotor.setInstagram(p.getInstagram());
                        promotor.setFacebook(p.getFacebook());
                        promotor.setOrientacion(p.getOrientacion());
                        promotor.setEstado(p.getEstado());
                        promotor.setMovilidad(p.getMovilidad());
                        listaTablaDatosFiltrado.add(promotor);
                    }
                });
            }
        }
        this.setearTablasConsultaDatos();
        this.fillTableListPromotor(listaTablaDatosFiltrado);
        labelContador.setText("Promotores: " + listaTablaDatosFiltrado.size());
    }

    //Metodo que me rellena la tabla, donde recibe una lista con los promotores.
    private void fillTableListPromotor(List<Promotora> list) {
        labelContador.setText("Promotores: " + list.size());
        String[] fila = new String[11];
        list.stream().sorted().forEach(p -> {
            fila[0] = p.getApellido().substring(0, 1).toUpperCase() + p.getApellido().substring(1);
            fila[1] = p.getNombre().substring(0, 1).toUpperCase() + p.getNombre().substring(1);
            fila[2] = String.valueOf(p.getDNI());
            if (p.getFechaNacimiento() != null) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                fila[3] = String.valueOf(format.format(p.getFechaNacimiento()));
            } else {
                fila[3] = "";
            }
            fila[4] = String.valueOf(p.getEdad());
            fila[5] = p.getDomicilio();
            fila[6] = p.getTelefono();
            fila[7] = String.valueOf(p.getOrientacion());
            fila[8] = String.valueOf(p.getCodMarca());
            fila[9] = p.getEstado();
            fila[10] = p.getMovilidad();

            dftDatos.addRow(fila);
        });
        tablaConsultaDatos.setModel(dftDatos);
    }

    //Metodo que me rellena la tabla de consulta por apariencia, donde recibe una lista con los promotores.
    private void fillTableListPromotorAparence(List<Promotora> list) {

        if (list != null) {
            if (list.size() > 0) {
                labelContadorApariencia.setText("Promotores: " + list.size());
                String[] fila = new String[10];
                list.stream().sorted().forEach(p -> {
                    fila[0] = String.valueOf(p.getDNI());
                    fila[1] = p.getApellido();
                    fila[2] = p.getNombre();
                    fila[3] = String.valueOf(p.getEdad());
                    fila[4] = p.getInstagram();
                    fila[5] = p.getFacebook();
                    fila[6] = p.getCabello();
                    fila[7] = p.getOjos();
                    fila[8] = String.valueOf(p.getAltura());
                    fila[9] = p.getPechos() + "-" + p.getCintura() + "-" + p.getCola();
                    dftApariencia.addRow(fila);
                });
                tablaConsultaApariencia.setModel(dftApariencia);
            }
        }
    }

    private void setearEstiloTablaDatos() {
        RenderTablaConsultaPromotoras rt = new RenderTablaConsultaPromotoras();
        DefaultTableModel dt;
        tablaConsultaDatos.setDefaultRenderer(Object.class, rt);

        //Codigo para aplicarle el formato personalizado al encabezado
        JTableHeader jth = tablaConsultaDatos.getTableHeader();
        jth.setDefaultRenderer(new HeaderManagement());
        tablaConsultaDatos.setTableHeader(jth);

        //Codigo para aplicarme los formatos personalizados
        tablaConsultaDatos.getColumnModel().getColumn(0).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaDatos.getColumnModel().getColumn(1).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaDatos.getColumnModel().getColumn(2).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaDatos.getColumnModel().getColumn(3).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaDatos.getColumnModel().getColumn(4).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaDatos.getColumnModel().getColumn(5).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaDatos.getColumnModel().getColumn(6).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaDatos.getColumnModel().getColumn(7).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaDatos.getColumnModel().getColumn(8).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaDatos.getColumnModel().getColumn(9).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaDatos.getColumnModel().getColumn(10).setCellRenderer(new RenderTablaConsultaPromotoras());

        //Codigo para especificar el tamaño de las celdas
        tablaConsultaDatos.setRowHeight(20);
        //Codigo para no poder escribir las celdas
        //tablaConsultaDatos.setDefaultEditor(Object.class, null);
    }

    private void setearEstiloTablaApariencia() {
        RenderTablaConsultaPromotoras rt = new RenderTablaConsultaPromotoras();
        DefaultTableModel dt;
        tablaConsultaApariencia.setDefaultRenderer(Object.class, rt);

        //Codigo para aplicarle el formato personalizado al encabezado
        JTableHeader jth = tablaConsultaApariencia.getTableHeader();
        jth.setDefaultRenderer(new HeaderManagement());
        tablaConsultaApariencia.setTableHeader(jth);

        //Codigo para aplicarme los formatos personalizados
        tablaConsultaApariencia.getColumnModel().getColumn(0).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaApariencia.getColumnModel().getColumn(1).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaApariencia.getColumnModel().getColumn(2).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaApariencia.getColumnModel().getColumn(3).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaApariencia.getColumnModel().getColumn(4).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaApariencia.getColumnModel().getColumn(5).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaApariencia.getColumnModel().getColumn(6).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaApariencia.getColumnModel().getColumn(7).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaApariencia.getColumnModel().getColumn(8).setCellRenderer(new RenderTablaConsultaPromotoras());
        tablaConsultaApariencia.getColumnModel().getColumn(9).setCellRenderer(new RenderTablaConsultaPromotoras());

        //Codigo para especificar el tamaño de las celdas
        tablaConsultaApariencia.setRowHeight(20);
        //Codigo para no poder escribir las celdas
        //tablaConsultaApariencia.setDefaultEditor(Object.class, null);
    }

    private void limpiarTablaDatos() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tablaConsultaDatos.getModel();
            int filas = tablaConsultaDatos.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }

    private void limpiarTablaApariencia() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tablaConsultaApariencia.getModel();
            int filas = tablaConsultaApariencia.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelo.removeRow(0);
            }
            listaTablaAparienciaFiltrado = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }

    //Metodo que me devolvera solamente numeros de un String
    private int obtenerCodigoListas(String nombre) {
        int nro = 0;
        Matcher m = Pattern.compile("[0-9]+").matcher(nombre);
        while (m.find()) {
            nro = Integer.parseInt(m.group().trim());
        }
        return nro;
    }

    private void registrarPromotora() {

        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String DNI = txtDNI.getText();

        java.sql.Date fechaNac = null;
        try {
            fechaNac = new java.sql.Date(fechaNacimiento.getDate().getTime());
        } catch (Exception e) {
        }

        String edad = txtEdad.getText();
        String direccion = txtDomicilio.getText();
        String telefono = txtTel.getText();
        String facebook = txtFacebook.getText();
        String instagram = txtInstragram.getText();

        String cabello = listaCabello.getSelectedItem().toString();
        String ojos = listaOjos.getSelectedItem().toString();

        String pechos = numBustos.getText();
        String cintura = numCintura.getText();
        String cola = numCola.getText();
        String altura = numAltura.getText();
        String calzado = numCalzado.getText();

        String estado = listaEstado.getSelectedItem().toString();
        String fechaRegistro = lblFechaRegistro.getText();

        Promotora p = null;
        PromotoraDAO dao = null;
        int r = 0;

        if (!nombre.equals("") && !apellido.equals("") && !DNI.equals("") && !edad.equals("")) {

            String orientacion = listaOrientacionRegistrar.getSelectedItem().toString();
            String beneficio = listaBeneficiosRegistrar.getSelectedItem().toString();
            String marca = listaMarcaRegistrar.getSelectedItem().toString();
            String movilidad = listaMovilidad.getSelectedItem().toString();

            p = new Promotora();
            p.setNombre(nombre);
            p.setApellido(apellido);
            p.setDNI(Integer.parseInt(DNI));
            p.setFechaNacimiento(fechaNac);
            p.setEdad(Integer.parseInt(edad));
            p.setDomicilio(direccion);
            p.setTelefono(telefono);
            p.setFacebook(facebook);
            p.setInstagram(instagram);

            p.setCabello(cabello);
            p.setOjos(ojos);

            if (!numBustos.getText().equals("")) {
                p.setPechos(Integer.parseInt(pechos));
            } else {
                p.setPechos(0);
            }
            if (!numCintura.getText().equals("")) {
                p.setCintura(Integer.parseInt(cintura));
            } else {
                p.setCintura(0);
            }
            if (!numCola.getText().equals("")) {
                p.setCola(Integer.parseInt(cola));
            } else {
                p.setCola(0);
            }
            if (!numAltura.getText().equals("")) {
                p.setAltura(Integer.parseInt(altura));
            } else {
                p.setAltura(0);
            }
            if (!numCalzado.getText().equals("")) {
                p.setCalzado(Integer.parseInt(calzado));
            } else {
                p.setCalzado(0);
            }

            p.setFechaRegistracion(fechaRegistro);

            if (!orientacion.equals("- - -")) {
                int codOrientacion = obtenerCodigoListas(orientacion);
                p.setOrientacion(codOrientacion);
            } else {
                p.setOrientacion(0);
            }
            if (!beneficio.equals("- - -")) {
                int codMarca = obtenerCodigoListas(marca);
                p.setCodMarca(codMarca);

            } else {
                p.setCodMarca(0);
            }
            if (!marca.equals("- - -")) {
                int codBeneficio = obtenerCodigoListas(beneficio);
                p.setCodBeneficio(codBeneficio);
            } else {
                p.setCodMarca(0);
            }

            if (!movilidad.equals("- - -")) {
                p.setMovilidad(movilidad);
            } else {
                p.setMovilidad("");
            }

            p.setEstado(estado);

            //TENEMOS QUE LLENAR LOS CAMPOS DE ORIENTACION, BENEECIOS Y MARCAS
            //VAMOS A HACER UNA CONSULTA A LA TABLA CORRESPONDIENTE PASANDOLE EL NOMBRE Y ASI PODER OBTENER EL CODIGO
            dao = new PromotoraDAO();
            r = dao.registrar(p, 1);

            if (r == 1) {
                JOptionPane.showMessageDialog(this, "Se registró correctamente.");
                limpiarCampos();
            } else if (r == 2) {
                JOptionPane.showMessageDialog(this, "No se pudo registrar.");
            } else if (r == 4) {
                JOptionPane.showMessageDialog(this, "La promotora con ese DNI ya se encuentra registrada.");
                txtDNI.setFocusable(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Algunos campos son requeridos.");
        }
    }

    private void buscarPromotora() {

        if (!txtDNIModificar.getText().equals("")) {

            DAO dao = new PromotoraDAO();
            List<Promotora> lista = dao.consultaFiltrada(Integer.parseInt(txtDNIModificar.getText()), "");

            if (lista.size() > 0) {
                listarBeneficiosModif();
                listarOrientacionModif();
                listarMarcasModif();

                for (Promotora p : lista) {
                    txtNombreModif.setText(p.getNombre());
                    txtApellidoModif.setText(p.getApellido());
                    txtDNIModif.setText(String.valueOf(p.getDNI()));
                    txtEdadModif.setText(String.valueOf(p.getEdad()));
                    txtTelModif.setText(p.getTelefono());
                    txtDomicilioModif.setText(p.getDomicilio());
                    txtFacebookModif.setText(p.getFacebook());
                    txtInstragramModif.setText(p.getInstagram());
                    numBustosModif.setText(String.valueOf(p.getPechos()));
                    numCinturaModif.setText(String.valueOf(p.getCintura()));
                    numColaModif.setText(String.valueOf(p.getCola()));
                    numAlturaModif.setText(String.valueOf(p.getAltura()));
                    numCalzadoModif.setText(String.valueOf(p.getCalzado()));
                    fechaNacimientoModif.setDate(p.getFechaNacimiento());
                    lblFechaRegistroModif.setText(p.getFechaRegistracion());

                    //METODOS QUE ME COMPARAN LOS DATOS OBTENIDOS DE LA LISTA Y LOS ELEMENTOS DE LA LISTA DE CABELLO Y OJOS
                    //PARA VER SI COINCIDEN Y EN EL CASO DE QUE COINCIDAN, ENLISTARLOS.
                    if (!listaOjosModif.getSelectedItem().toString().equals("")) {
                        listaOjosModif.setSelectedItem(p.getOjos());
                    }
                    if (!listaCabelloModif.getSelectedItem().toString().equals("")) {
                        listaCabelloModif.setSelectedItem(p.getCabello());
                    }
                    if (!listaMovilidadModif.getSelectedItem().toString().equals("")) {
                        listaMovilidadModif.setSelectedItem(p.getMovilidad());
                    }

                    int orientacion = listaOrientacionModif.getItemCount();
                    for (int i = 0; i < orientacion; i++) {
                        String elemento = listaOrientacionModif.getItemAt(i);
                        String nro = "";
                        for (int j = 0; j < elemento.length(); j++) {
                            if (j < 5) {
                                nro += elemento.charAt(j);
                            }
                        }
                        if (String.valueOf(p.getOrientacion()).equalsIgnoreCase(nro)) {
                            listaOrientacionModif.setSelectedIndex(i);
                        }
                    }
                    int beneficio = listaBeneficiosModif.getItemCount();
                    for (int i = 0; i < beneficio; i++) {
                        String elemento = listaBeneficiosModif.getItemAt(i);
                        String nro = "";
                        for (int j = 0; j < elemento.length(); j++) {
                            if (j < 5) {
                                nro += elemento.charAt(j);
                            }
                        }
                        if (String.valueOf(p.getCodBeneficio()).equalsIgnoreCase(nro)) {
                            listaBeneficiosModif.setSelectedIndex(i);
                        }
                    }

                    int marca = listaMarcaModif.getItemCount();
                    for (int i = 0; i < marca; i++) {
                        String elemento = listaMarcaModif.getItemAt(i);
                        String nro = "";
                        for (int j = 0; j < elemento.length(); j++) {
                            if (j < 5) {
                                nro += elemento.charAt(j);
                            }
                        }
                        if (String.valueOf(p.getCodMarca()).equalsIgnoreCase(nro)) {
                            listaMarcaModif.setSelectedIndex(i);
                        }
                    }

                    //--------------------------------------------------------------------------------------------------
                }
            }

        }
    }

    private void modificarPromotora() {

        String nombre = txtNombreModif.getText();
        String apellido = txtApellidoModif.getText();
        String DNI = txtDNIModif.getText();

        java.sql.Date fechaNac = null;
        try {
            fechaNac = new java.sql.Date(fechaNacimientoModif.getDate().getTime());
        } catch (Exception e) {
        }

        String edad = txtEdadModif.getText();
        String direccion = txtDomicilioModif.getText();
        String telefono = txtTelModif.getText();
        String facebook = txtFacebookModif.getText();
        String instagram = txtInstragramModif.getText();

        String cabello = listaCabelloModif.getSelectedItem().toString();
        String ojos = listaOjosModif.getSelectedItem().toString();

        String pechos = numBustosModif.getText();
        String cintura = numCinturaModif.getText();
        String cola = numColaModif.getText();
        String altura = numAlturaModif.getText();
        String calzado = numCalzadoModif.getText();

        String estado = listaEstadoModif.getSelectedItem().toString();

        Promotora p = null;
        PromotoraDAO dao = null;
        int r = 0;

        if (!nombre.equals("") && !apellido.equals("") && !DNI.equals("") && !edad.equals("")) {

            p = new Promotora();
            p.setNombre(nombre);
            p.setApellido(apellido);
            p.setDNI(Integer.parseInt(DNI));
            p.setFechaNacimiento(fechaNac);
            p.setEdad(Integer.parseInt(edad));
            p.setDomicilio(direccion);
            p.setTelefono(telefono);
            p.setFacebook(facebook);
            p.setInstagram(instagram);

            p.setCabello(cabello);
            p.setOjos(ojos);
            p.setEstado(estado);

            if (!numBustosModif.getText().equals("")) {
                p.setPechos(Integer.parseInt(pechos));
            } else {
                p.setPechos(0);
            }
            if (!numCinturaModif.getText().equals("")) {
                p.setCintura(Integer.parseInt(cintura));
            } else {
                p.setCintura(0);
            }
            if (!numColaModif.getText().equals("")) {
                p.setCola(Integer.parseInt(cola));
            } else {
                p.setCola(0);
            }
            if (!numAlturaModif.getText().equals("")) {
                p.setAltura(Integer.parseInt(altura));
            } else {
                p.setAltura(0);
            }
            if (!numCalzadoModif.getText().equals("")) {
                p.setCalzado(Integer.parseInt(calzado));
            } else {
                p.setCalzado(0);
            }

            String orientacion = listaOrientacionModif.getSelectedItem().toString();
            String beneficio = listaBeneficiosModif.getSelectedItem().toString();
            String marca = listaMarcaModif.getSelectedItem().toString();
            String movilidad = listaMovilidadModif.getSelectedItem().toString();

            if (!orientacion.equals("- - -")) {
                int codOrientacion = obtenerCodigoListas(orientacion);
                p.setOrientacion(codOrientacion);
            }
            if (!marca.equals("- - -")) {
                int codMarca = obtenerCodigoListas(marca);
                p.setCodMarca(codMarca);

            }
            if (!beneficio.equals("- - -")) {
                int codBeneficio = obtenerCodigoListas(beneficio);
                p.setCodBeneficio(codBeneficio);
            }
            if (!movilidad.equals("- - -")) {
                p.setMovilidad(movilidad);
            } else {
                p.setMovilidad("");
            }

            //TENEMOS QUE LLENAR LOS CAMPOS DE ORIENTACION, BENEECIOS Y MARCAS
            //VAMOS A HACER UNA CONSULTA A LA TABLA CORRESPONDIENTE PASANDOLE EL NOMBRE Y ASI PODER OBTENER EL CODIGO
            dao = new PromotoraDAO();
            r = dao.modificar(Integer.parseInt(txtDNIModificar.getText()), p);
        } else {
            JOptionPane.showMessageDialog(this, "Algunos campos son requeridos.");
        }

        if (r == 1) {
            JOptionPane.showMessageDialog(this, "Se modificó correctamente.");
            limpiarCamposModif();
        } else if (r == 2) {
            JOptionPane.showMessageDialog(this, "No se pudo modificó.");
        } else if (r == 4) {
            JOptionPane.showMessageDialog(this, "La promotora con ese DNI ya se encuentra registrada.");
            txtDNI.setFocusable(true);
        }
    }

    //Metodo para consultar a la bd segun Apariencias Físicas
    private void consultarApariencia() {

        listaTablaAparienciaFiltrado = new ArrayList<>();
        if (listaConsultaCabello.getSelectedItem().equals(("- - -")) && listaConsultaOjos.getSelectedItem().equals(("- - -")) && listaConsultaOrientacion.getSelectedItem().equals(("- - -")) && listaConsultaAltura.getSelectedItem().equals(("- - -"))) {
            labelContadorApariencia.setText("");
            return;
        }

        //En el caso de que solamente esté seleccionado la busqueda por color de cabello
        if (!listaConsultaCabello.getSelectedItem().equals(("- - -")) && listaConsultaOjos.getSelectedItem().equals(("- - -")) && listaConsultaOrientacion.getSelectedItem().equals(("- - -")) && listaConsultaAltura.getSelectedItem().equals(("- - -"))) {
            if (listaTablaDatos != null) {
                if (listaTablaDatos.size() > 0) {
                    String nombreOpcionSelecionada_Cabello = listaConsultaCabello.getSelectedItem().toString();
                    listaTablaDatos.stream().forEach(p -> {
                        if (p.getCabello().toUpperCase().equals(nombreOpcionSelecionada_Cabello.toUpperCase())) {
                            //Llamamos al metodo pasandole el promotor para que me agregue a la tabla filtrada
                            fillListTableAparenceFilter(p);
                        }
                    });
                }
            }
        }

        //En el caso de que solamente esté seleccionado la busqueda por color de ojos
        if (listaConsultaCabello.getSelectedItem().equals(("- - -")) && !listaConsultaOjos.getSelectedItem().equals(("- - -")) && listaConsultaOrientacion.getSelectedItem().equals(("- - -")) && listaConsultaAltura.getSelectedItem().equals(("- - -"))) {
            if (listaTablaDatos != null) {
                if (listaTablaDatos.size() > 0) {

                    String nombreOpcionSelecionada_Ojos = listaConsultaOjos.getSelectedItem().toString();
                    listaTablaDatos.stream().forEach(p -> {
                        if (p.getOjos().toUpperCase().equals(nombreOpcionSelecionada_Ojos.toUpperCase())) {
                            //Llamamos al metodo pasandole el promotor para que me agregue a la tabla filtrada
                            fillListTableAparenceFilter(p);
                        }
                    });
                }
            }
        }

        //En el caso de que solamente esté seleccionado la busqueda por orientación
        if (listaConsultaCabello.getSelectedItem().equals(("- - -")) && listaConsultaOjos.getSelectedItem().equals(("- - -")) && !listaConsultaOrientacion.getSelectedItem().equals(("- - -")) && listaConsultaAltura.getSelectedItem().equals(("- - -"))) {
            if (listaTablaDatos != null) {
                if (listaTablaDatos.size() > 0) {
                    String nombreOpcionSeleccionada = listaConsultaOrientacion.getSelectedItem().toString();
                    int nroOrientacion = obtenerCodigoListas(nombreOpcionSeleccionada);
                    listaTablaDatos.stream().forEach(p -> {
                        if (nroOrientacion == p.getOrientacion()) {
                            //Llamamos al metodo pasandole el promotor para que me agregue a la tabla filtrada
                            fillListTableAparenceFilter(p);
                        }
                    });
                }
            }
        }

        //En el caso de que solamente esté seleccionado la busqueda por altura
        if (listaConsultaCabello.getSelectedItem().equals(("- - -")) && listaConsultaOjos.getSelectedItem().equals(("- - -")) && listaConsultaOrientacion.getSelectedItem().equals(("- - -")) && !listaConsultaAltura.getSelectedItem().equals(("- - -"))) {
            if (listaTablaDatos != null) {
                if (listaTablaDatos.size() > 0) {
                    int nombreOpcionSeleccionada = Integer.parseInt(listaConsultaAltura.getSelectedItem().toString());
                    listaTablaDatos.stream().forEach(p -> {
                        if (p.getAltura() >= nombreOpcionSeleccionada) {
                            //Llamamos al metodo pasandole el promotor para que me agregue a la tabla filtrada
                            fillListTableAparenceFilter(p);
                        }
                    });
                }
            }
        }

        //En el caso de que solamente esté seleccionado la busqueda por altura y orientacion
        if (listaConsultaCabello.getSelectedItem().equals(("- - -")) && listaConsultaOjos.getSelectedItem().equals(("- - -")) && !listaConsultaOrientacion.getSelectedItem().equals(("- - -")) && !listaConsultaAltura.getSelectedItem().equals(("- - -"))) {
            if (listaTablaDatos != null) {
                if (listaTablaDatos.size() > 0) {
                    int nombreOpcionSeleccionada = Integer.parseInt(listaConsultaAltura.getSelectedItem().toString());
                    String nombreOpcionSeleccionada_Orientacion = listaConsultaOrientacion.getSelectedItem().toString();
                    int nroOrientacion = obtenerCodigoListas(nombreOpcionSeleccionada_Orientacion);
                    listaTablaDatos.stream().forEach(p -> {
                        if (nroOrientacion == p.getOrientacion() && p.getAltura() >= nombreOpcionSeleccionada) {
                            //Llamamos al metodo pasandole el promotor para que me agregue a la tabla filtrada
                            fillListTableAparenceFilter(p);
                        }
                    });
                }
            }
        }

        //En el caso de que esté seleccionado por color de cabello y ojos
        if (!listaConsultaCabello.getSelectedItem().equals(("- - -")) && !listaConsultaOjos.getSelectedItem().equals(("- - -")) && listaConsultaOrientacion.getSelectedItem().equals(("- - -")) && listaConsultaAltura.getSelectedItem().equals(("- - -"))) {
            if (listaTablaDatos != null) {
                if (listaTablaDatos.size() > 0) {

                    String nombreOpcionSelecionada_Ojos = listaConsultaOjos.getSelectedItem().toString();
                    String nombreOpcionSelecionada_Cabello = listaConsultaCabello.getSelectedItem().toString();
                    listaTablaDatos.stream().forEach(p -> {
                        if (p.getOjos().toUpperCase().equals(nombreOpcionSelecionada_Ojos.toUpperCase()) && p.getCabello().toUpperCase().equals(nombreOpcionSelecionada_Cabello.toUpperCase())) {
                            //Llamamos al metodo pasandole el promotor para que me agregue a la tabla filtrada
                            fillListTableAparenceFilter(p);
                        }
                    });
                }
            }
        }

        //En el caso de que esté seleccionado por color de cabello, ojos y orientacion
        if (!listaConsultaCabello.getSelectedItem().equals(("- - -")) && !listaConsultaOjos.getSelectedItem().equals(("- - -")) && !listaConsultaOrientacion.getSelectedItem().equals(("- - -")) && listaConsultaAltura.getSelectedItem().equals(("- - -"))) {
            if (listaTablaDatos != null) {
                if (listaTablaDatos.size() > 0) {
                    String nombreOpcionSelecionada_Ojos = listaConsultaOjos.getSelectedItem().toString();
                    String nombreOpcionSelecionada_Cabello = listaConsultaCabello.getSelectedItem().toString();
                    String nombreOpcionSeleccionada = listaConsultaOrientacion.getSelectedItem().toString();
                    int nroOrientacion = obtenerCodigoListas(nombreOpcionSeleccionada);
                    listaTablaDatos.stream().forEach(p -> {
                        if (nroOrientacion == p.getOrientacion() && p.getOjos().toUpperCase().equals(nombreOpcionSelecionada_Ojos.toUpperCase()) && p.getCabello().toUpperCase().equals(nombreOpcionSelecionada_Cabello.toUpperCase())) {
                            //Llamamos al metodo pasandole el promotor para que me agregue a la tabla filtrada
                            fillListTableAparenceFilter(p);
                        }
                    });
                }
            }
        }

        this.setearTablasConsultaApariencia();
        this.fillTableListPromotorAparence(listaTablaAparienciaFiltrado);
        labelContadorApariencia.setText("Promotores: " + listaTablaAparienciaFiltrado.size());

    }

    //Metodo para llenarme la listaTablaAparienciaFiltado
    private void fillListTableAparenceFilter(Promotora p) {
        p.setCodPromotor(p.getCodPromotor());
        p.setNombre(p.getNombre());
        p.setApellido(p.getApellido());
        p.setDNI(p.getDNI());
        p.setFechaNacimiento(p.getFechaNacimiento());
        p.setEdad(p.getEdad());
        p.setDomicilio(p.getDomicilio());
        p.setTelefono(p.getTelefono());
        p.setInstagram(p.getInstagram());
        p.setFacebook(p.getFacebook());
        p.setCabello(p.getCabello());
        p.setOjos(p.getOjos());
        p.setAltura(p.getAltura());
        p.setPechos(p.getPechos());
        p.setCintura(p.getCintura());
        p.setCola(p.getCola());
        listaTablaAparienciaFiltrado.add(p);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------
    //Metodo para consultar en la bd segun el estado (activo, inactivo)
    private void consultaEstado(String text) {

        limpiarTablaApariencia();
        listaTablaPorEstado = new ArrayList<>();
        if (listaTablaDatos != null) {
            if (listaTablaDatos.size() > 0) {
                listaTablaDatos.stream().forEach(p -> {
                    String estado = p.getEstado().toLowerCase();

                    if (estado.equals(text.toLowerCase())) {
                        Promotora promotor = new Promotora();
                        promotor.setCodPromotor(p.getCodPromotor());
                        promotor.setNombre(p.getNombre());
                        promotor.setApellido(p.getApellido());
                        promotor.setDNI(p.getDNI());
                        promotor.setTelefono(p.getTelefono());
                        promotor.setFechaNacimiento(p.getFechaNacimiento());
                        promotor.setFechaNacimiento(p.getFechaNacimiento());
                        promotor.setEdad(p.getEdad());
                        promotor.setDomicilio(p.getDomicilio());
                        promotor.setTelefono(p.getTelefono());
                        promotor.setInstagram(p.getInstagram());
                        promotor.setFacebook(p.getFacebook());
                        promotor.setOrientacion(p.getOrientacion());
                        promotor.setEstado(p.getEstado());
                        listaTablaPorEstado.add(promotor);
                    }
                });
            }
        }
        this.setearTablasConsultaDatos();
        this.fillTableListPromotor(listaTablaPorEstado);

    }

    private void lblShorcut() {

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        busquedaDatos = new javax.swing.ButtonGroup();
        popMenuTabla = new javax.swing.JPopupMenu();
        menuModificar = new javax.swing.JMenuItem();
        menuEliminar = new javax.swing.JMenuItem();
        menuBook = new javax.swing.JMenuItem();
        pestañas = new javax.swing.JTabbedPane();
        pestañaConsultar = new javax.swing.JPanel();
        pestañasConsultar = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaConsultaDatos = new javax.swing.JTable();
        labelContador = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        txtBusquedaDatos = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        listaEstadoDatos = new javax.swing.JComboBox<>();
        jLabel48 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaConsultaApariencia = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        listaConsultaOjos = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        listaConsultaCabello = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        listaConsultaOrientacion = new javax.swing.JComboBox<>();
        jLabel63 = new javax.swing.JLabel();
        listaConsultaAltura = new javax.swing.JComboBox<>();
        labelContadorApariencia = new javax.swing.JLabel();
        pestañaRegistrar = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtDNI = new javax.swing.JTextField();
        txtEdad = new javax.swing.JTextField();
        txtTel = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtDomicilio = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtFacebook = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtInstragram = new javax.swing.JTextField();
        fechaNacimiento = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        listaOrientacionRegistrar = new javax.swing.JComboBox<>();
        listaBeneficiosRegistrar = new javax.swing.JComboBox<>();
        listaMarcaRegistrar = new javax.swing.JComboBox<>();
        jLabel49 = new javax.swing.JLabel();
        listaEstado = new javax.swing.JComboBox<>();
        lblFechaRegistro = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        listaMovilidad = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        listaCabello = new javax.swing.JComboBox<>();
        listaOjos = new javax.swing.JComboBox<>();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        numCalzado = new javax.swing.JTextField();
        numCola = new javax.swing.JTextField();
        numAltura = new javax.swing.JTextField();
        numCintura = new javax.swing.JTextField();
        numBustos = new javax.swing.JTextField();
        btnRegistrar = new javax.swing.JButton();
        pestañaRegistrar1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtNombreModif = new javax.swing.JTextField();
        txtApellidoModif = new javax.swing.JTextField();
        txtDNIModif = new javax.swing.JTextField();
        txtEdadModif = new javax.swing.JTextField();
        txtTelModif = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        txtDomicilioModif = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtFacebookModif = new javax.swing.JTextField();
        txtInstragramModif = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        fechaNacimientoModif = new com.toedter.calendar.JDateChooser();
        jPanel8 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        listaOrientacionModif = new javax.swing.JComboBox<>();
        listaBeneficiosModif = new javax.swing.JComboBox<>();
        listaMarcaModif = new javax.swing.JComboBox<>();
        jLabel55 = new javax.swing.JLabel();
        listaEstadoModif = new javax.swing.JComboBox<>();
        lblFechaRegistroModif = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        listaMovilidadModif = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        listaCabelloModif = new javax.swing.JComboBox<>();
        listaOjosModif = new javax.swing.JComboBox<>();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        numCalzadoModif = new javax.swing.JTextField();
        numColaModif = new javax.swing.JTextField();
        numBustosModif = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        numAlturaModif = new javax.swing.JTextField();
        numCinturaModif = new javax.swing.JTextField();
        btnRegistrar1 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        txtDNIModificar = new javax.swing.JTextField();

        menuModificar.setText("jMenuItem1");
        popMenuTabla.add(menuModificar);

        menuEliminar.setText("jMenuItem1");
        popMenuTabla.add(menuEliminar);

        menuBook.setText("jMenuItem1");
        popMenuTabla.add(menuBook);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pestañas.setForeground(new java.awt.Color(0, 102, 51));
        pestañas.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel36.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(102, 102, 102));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("DATOS PERSONALES");

        tablaConsultaDatos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tablaConsultaDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaConsultaDatos.setComponentPopupMenu(popMenuTabla);
        tablaConsultaDatos.setOpaque(false);
        tablaConsultaDatos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaConsultaDatosKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tablaConsultaDatos);

        labelContador.setFont(new java.awt.Font("Arial", 2, 16)); // NOI18N
        labelContador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jPanel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel10.setOpaque(false);

        txtBusquedaDatos.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        txtBusquedaDatos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBusquedaDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBusquedaDatosMouseClicked(evt);
            }
        });
        txtBusquedaDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusquedaDatosActionPerformed(evt);
            }
        });
        txtBusquedaDatos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusquedaDatosKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBusquedaDatosKeyTyped(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel47.setText("Ingrese el dato a buscar:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBusquedaDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBusquedaDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel11.setOpaque(false);

        jLabel32.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel32.setText("Estado:");

        listaEstadoDatos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaEstadoDatos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -", "Activo", "Inactivo" }));
        listaEstadoDatos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaEstadoDatosItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listaEstadoDatos, 0, 187, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaEstadoDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel48.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(102, 102, 102));
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("Accesos rápidos");
        jLabel48.setEnabled(false);
        jLabel48.setOpaque(true);

        jLabel58.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(102, 102, 102));
        jLabel58.setText("Acontecimientos: Tab");
        jLabel58.setEnabled(false);
        jLabel58.setOpaque(true);

        jLabel59.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(102, 102, 102));
        jLabel59.setText("Eliminar: Supr");
        jLabel59.setEnabled(false);
        jLabel59.setOpaque(true);

        jLabel60.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(102, 102, 102));
        jLabel60.setText("Galería: Espacio");
        jLabel60.setEnabled(false);
        jLabel60.setOpaque(true);

        jLabel61.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(102, 102, 102));
        jLabel61.setText("Modificar: Enter");
        jLabel61.setEnabled(false);
        jLabel61.setOpaque(true);

        jLabel62.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(102, 102, 102));
        jLabel62.setText("Prestaciones: Alt");
        jLabel62.setEnabled(false);
        jLabel62.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(235, 235, 235)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel61, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel59, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(14, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelContador, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(119, 119, 119))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel48)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel58)
                            .addComponent(jLabel59))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel60)
                            .addComponent(jLabel61))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel62)
                        .addGap(18, 18, 18)
                        .addComponent(labelContador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pestañasConsultar.addTab("Datos Personales", jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setPreferredSize(new java.awt.Dimension(550, 460));

        jLabel37.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(102, 102, 102));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("APARIENCIA FISICA");

        tablaConsultaApariencia.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tablaConsultaApariencia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaConsultaApariencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaConsultaAparienciaKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tablaConsultaApariencia);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        listaConsultaOjos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaConsultaOjos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -", "Azules", "Celestes", "Negros", "Marrones", "Verdes" }));
        listaConsultaOjos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaConsultaOjosItemStateChanged(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel33.setText("Cabello:");

        jLabel34.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel34.setText("Ojos:");

        listaConsultaCabello.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaConsultaCabello.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -", "Castaño", "Colorado", "Morocho", "Negro", "Rubio" }));
        listaConsultaCabello.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaConsultaCabelloItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listaConsultaOjos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaConsultaCabello, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaConsultaCabello, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(listaConsultaOjos, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel38.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel38.setText("Orientanción:");

        listaConsultaOrientacion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaConsultaOrientacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -" }));
        listaConsultaOrientacion.setPreferredSize(new java.awt.Dimension(56, 25));
        listaConsultaOrientacion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaConsultaOrientacionItemStateChanged(evt);
            }
        });

        jLabel63.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel63.setText("Altura mínima:");

        listaConsultaAltura.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaConsultaAltura.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -", "150", "151", "152", "153", "154", "155", "156", "157", "158", "159", "160", "161", "162", "163", "164", "165", "166", "167", "168", "169", "170", "171", "172", "173", "174", "175", "176", "177", "178", "179", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "190" }));
        listaConsultaAltura.setPreferredSize(new java.awt.Dimension(56, 25));
        listaConsultaAltura.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaConsultaAlturaItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listaConsultaOrientacion, 0, 174, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listaConsultaAltura, 0, 174, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaConsultaOrientacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaConsultaAltura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );

        labelContadorApariencia.setFont(new java.awt.Font("Arial", 2, 16)); // NOI18N
        labelContadorApariencia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(264, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(441, 441, 441))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelContadorApariencia, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1136, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(labelContadorApariencia, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(315, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addGap(0, 189, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pestañasConsultar.addTab("Apariencia Física", jPanel2);

        javax.swing.GroupLayout pestañaConsultarLayout = new javax.swing.GroupLayout(pestañaConsultar);
        pestañaConsultar.setLayout(pestañaConsultarLayout);
        pestañaConsultarLayout.setHorizontalGroup(
            pestañaConsultarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pestañaConsultarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pestañasConsultar)
                .addContainerGap())
        );
        pestañaConsultarLayout.setVerticalGroup(
            pestañaConsultarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pestañasConsultar, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pestañas.addTab("Consultar", pestañaConsultar);

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setPreferredSize(new java.awt.Dimension(320, 430));
        jPanel4.setRequestFocusEnabled(false);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText("Nombre:");
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel3.setText("Apellido:");
        jLabel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("DNI:");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel4.setText("Fecha Nacimiento:");
        jLabel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel5.setText("Edad:");
        jLabel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel6.setText("Teléfono:");
        jLabel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("DATOS PERSONALES");

        txtNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombre.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        txtApellido.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtApellido.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtApellido.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtApellidoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
        });

        txtDNI.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDNI.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDNI.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtDNI.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDNIKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDNIKeyTyped(evt);
            }
        });

        txtEdad.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtEdad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEdad.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtEdad.setEnabled(false);

        txtTel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtTel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtTel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelKeyTyped(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel41.setText("Domicilio:");
        jLabel41.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtDomicilio.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDomicilio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDomicilio.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel39.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel39.setText("Facebook:");
        jLabel39.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtFacebook.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtFacebook.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFacebook.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel40.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel40.setText("Instagram:");
        jLabel40.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtInstragram.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtInstragram.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInstragram.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        fechaNacimiento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fechaNacimientoKeyPressed(evt);
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
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombre))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtApellido))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDNI))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaNacimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEdad))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDomicilio))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFacebook))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtInstragram)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fechaNacimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEdad, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDomicilio, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFacebook, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInstragram, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.setPreferredSize(new java.awt.Dimension(320, 430));
        jPanel5.setRequestFocusEnabled(false);

        jLabel8.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("DATOS LABORALES");

        jLabel15.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel15.setText("Orientación:");
        jLabel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel16.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel16.setText("Marca:");
        jLabel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setText("Beneficios:");
        jLabel17.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        listaOrientacionRegistrar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaOrientacionRegistrar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -" }));

        listaBeneficiosRegistrar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaBeneficiosRegistrar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -" }));

        listaMarcaRegistrar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaMarcaRegistrar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -" }));

        jLabel49.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel49.setText("Estado:");
        jLabel49.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel49.setPreferredSize(new java.awt.Dimension(67, 19));

        listaEstado.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        lblFechaRegistro.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblFechaRegistro.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFechaRegistro.setEnabled(false);

        jLabel56.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel56.setText("Movilidad:");
        jLabel56.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel56.setPreferredSize(new java.awt.Dimension(67, 19));

        listaMovilidad.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaMovilidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -", "Propia", "Ocasionalmente", "NO" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblFechaRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(listaOrientacionRegistrar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(listaMarcaRegistrar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listaBeneficiosRegistrar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listaEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 33, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listaMovilidad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaOrientacionRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaMarcaRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaBeneficiosRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaMovilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(85, 85, 85)
                .addComponent(lblFechaRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel6.setRequestFocusEnabled(false);

        jLabel9.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("APARIENCIA FÍSICA");

        jLabel10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel10.setText("Color Cabello:");
        jLabel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel11.setText("Color Ojos:");
        jLabel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel12.setText("Medida Bustos:");
        jLabel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel13.setText("Medida Cintura:");
        jLabel13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel14.setText("Medida Cadera:");
        jLabel14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        listaCabello.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaCabello.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Castaño", "Colorado", "Morocho", "Negro", "Rubio" }));

        listaOjos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaOjos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Azules", "Celestes", "Negros", "Marrones", "Verdes" }));

        jLabel45.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel45.setText("Altura (cm):");
        jLabel45.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel46.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel46.setText("Calzado");
        jLabel46.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        numCalzado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numCalzadoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numCalzadoKeyTyped(evt);
            }
        });

        numCola.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numColaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numColaKeyTyped(evt);
            }
        });

        numAltura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numAlturaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numAlturaKeyTyped(evt);
            }
        });

        numCintura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numCinturaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numCinturaKeyTyped(evt);
            }
        });

        numBustos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numBustosKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numBustosKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(123, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listaCabello, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listaOjos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numCola, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 266, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numCintura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numCalzado, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numBustos, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numAltura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaCabello, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaOjos, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numBustos, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numAltura, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(numCintura, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(numCalzado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numCola, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnRegistrar.setBackground(new java.awt.Color(0, 153, 51));
        btnRegistrar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setText("REGISTRAR");
        btnRegistrar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pestañaRegistrarLayout = new javax.swing.GroupLayout(pestañaRegistrar);
        pestañaRegistrar.setLayout(pestañaRegistrarLayout);
        pestañaRegistrarLayout.setHorizontalGroup(
            pestañaRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pestañaRegistrarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pestañaRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pestañaRegistrarLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pestañaRegistrarLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        pestañaRegistrarLayout.setVerticalGroup(
            pestañaRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pestañaRegistrarLayout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addGroup(pestañaRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pestañaRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pestañas.addTab("Registrar", pestañaRegistrar);

        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel7.setPreferredSize(new java.awt.Dimension(320, 430));
        jPanel7.setRequestFocusEnabled(false);

        jLabel18.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel18.setText("Nombre:");
        jLabel18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel19.setText("Apellido:");
        jLabel19.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel20.setText("DNI:");
        jLabel20.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel21.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel21.setText("Fecha Nacimiento:");
        jLabel21.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel22.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel22.setText("Edad:");
        jLabel22.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel23.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel23.setText("Teléfono:");
        jLabel23.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel24.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(102, 102, 102));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("DATOS PERSONALES");

        txtNombreModif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNombreModif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombreModif.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtNombreModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreModifKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreModifKeyTyped(evt);
            }
        });

        txtApellidoModif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtApellidoModif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtApellidoModif.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtApellidoModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtApellidoModifKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoModifKeyTyped(evt);
            }
        });

        txtDNIModif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDNIModif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDNIModif.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtDNIModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDNIModifKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDNIModifKeyTyped(evt);
            }
        });

        txtEdadModif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtEdadModif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEdadModif.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtEdadModif.setEnabled(false);

        txtTelModif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtTelModif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTelModif.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtTelModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelModifKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelModifKeyTyped(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel42.setText("Domicilio:");
        jLabel42.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtDomicilioModif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDomicilioModif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDomicilioModif.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel43.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel43.setText("Facebook:");
        jLabel43.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtFacebookModif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtFacebookModif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFacebookModif.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtInstragramModif.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtInstragramModif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInstragramModif.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel44.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel44.setText("Instagram:");
        jLabel44.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        fechaNacimientoModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fechaNacimientoModifKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreModif))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtApellidoModif))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDNIModif))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaNacimientoModif, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEdadModif))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelModif))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDomicilioModif))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFacebookModif))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtInstragramModif)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellidoModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDNIModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fechaNacimientoModif, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEdadModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDomicilioModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFacebookModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInstragramModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel8.setPreferredSize(new java.awt.Dimension(320, 430));
        jPanel8.setRequestFocusEnabled(false);

        jLabel25.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("DATOS LABORALES");

        jLabel26.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel26.setText("Orientación:");
        jLabel26.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel27.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel27.setText("Marca:");
        jLabel27.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel28.setText("Beneficios:");
        jLabel28.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        listaOrientacionModif.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaOrientacionModif.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -" }));

        listaBeneficiosModif.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaBeneficiosModif.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -" }));

        listaMarcaModif.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaMarcaModif.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -" }));

        jLabel55.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel55.setText("Estado:");
        jLabel55.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        listaEstadoModif.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaEstadoModif.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        lblFechaRegistroModif.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblFechaRegistroModif.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFechaRegistroModif.setEnabled(false);

        jLabel57.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel57.setText("Movilidad:");
        jLabel57.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        listaMovilidadModif.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaMovilidadModif.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - -", "Propia", "Ocasionalmente", "NO" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblFechaRegistroModif, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 40, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(listaOrientacionModif, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(listaMarcaModif, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listaBeneficiosModif, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listaEstadoModif, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listaMovilidadModif, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaOrientacionModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaMarcaModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaBeneficiosModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaEstadoModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaMovilidadModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addComponent(lblFechaRegistroModif, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel9.setPreferredSize(new java.awt.Dimension(422, 277));
        jPanel9.setRequestFocusEnabled(false);

        jLabel29.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(102, 102, 102));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("APARIENCIA FÍSICA");

        jLabel30.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel30.setText("Color Cabello:");
        jLabel30.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel31.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel31.setText("Color Ojos:");
        jLabel31.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        listaCabelloModif.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaCabelloModif.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Castaño", "Colorado", "Morocho", "Negro", "Rubio" }));

        listaOjosModif.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        listaOjosModif.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Azules", "Celestes", "Negros", "Marrones", "Verdes" }));

        jLabel50.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel50.setText("Medida Bustos:");
        jLabel50.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel51.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel51.setText("Medida Cadera:");
        jLabel51.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel52.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel52.setText("Calzado");
        jLabel52.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        numCalzadoModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numCalzadoModifKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numCalzadoModifKeyTyped(evt);
            }
        });

        numColaModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numColaModifKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numColaModifKeyTyped(evt);
            }
        });

        numBustosModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numBustosModifKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numBustosModifKeyTyped(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel53.setText("Medida Cintura:");
        jLabel53.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel54.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel54.setText("Altura (cm):");
        jLabel54.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        numAlturaModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numAlturaModifKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numAlturaModifKeyTyped(evt);
            }
        });

        numCinturaModif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numCinturaModifKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numCinturaModifKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(listaCabelloModif, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(listaOjosModif, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(numBustosModif, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(numCinturaModif, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(numAlturaModif, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(numCalzadoModif, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numColaModif, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaCabelloModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listaOjosModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numBustosModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numCinturaModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numAlturaModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(numCalzadoModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numColaModif, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnRegistrar1.setBackground(new java.awt.Color(0, 153, 51));
        btnRegistrar1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnRegistrar1.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrar1.setText("GUARDAR");
        btnRegistrar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRegistrar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrar1ActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel35.setText("INGRESE DNI DE LA PROMOTORA:");

        txtDNIModificar.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        txtDNIModificar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDNIModificar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtDNIModificar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDNIModificarKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDNIModificarKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout pestañaRegistrar1Layout = new javax.swing.GroupLayout(pestañaRegistrar1);
        pestañaRegistrar1.setLayout(pestañaRegistrar1Layout);
        pestañaRegistrar1Layout.setHorizontalGroup(
            pestañaRegistrar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pestañaRegistrar1Layout.createSequentialGroup()
                .addGroup(pestañaRegistrar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pestañaRegistrar1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(pestañaRegistrar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRegistrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pestañaRegistrar1Layout.createSequentialGroup()
                        .addGap(281, 281, 281)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDNIModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        pestañaRegistrar1Layout.setVerticalGroup(
            pestañaRegistrar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pestañaRegistrar1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pestañaRegistrar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDNIModificar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pestañaRegistrar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(btnRegistrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pestañas.addTab("Modificar", pestañaRegistrar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pestañas)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pestañas)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        registrarPromotora();
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        validarCamposTextos(evt);
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped
        validarCamposTextos(evt);
    }//GEN-LAST:event_txtApellidoKeyTyped

    private void txtDNIKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDNIKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_txtDNIKeyTyped

    private void txtTelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_txtTelKeyTyped

    private void txtBusquedaDatosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaDatosKeyPressed

    }//GEN-LAST:event_txtBusquedaDatosKeyPressed

    private void txtBusquedaDatosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaDatosKeyTyped
        filterTablePromotor(txtBusquedaDatos.getText());
    }//GEN-LAST:event_txtBusquedaDatosKeyTyped

    private void txtDNIModificarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDNIModificarKeyPressed
        txtDNIModificar.setTransferHandler(null);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarPromotora();
        }

    }//GEN-LAST:event_txtDNIModificarKeyPressed

    private void txtDNIModificarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDNIModificarKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_txtDNIModificarKeyTyped

    private void btnRegistrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrar1ActionPerformed
        modificarPromotora();
    }//GEN-LAST:event_btnRegistrar1ActionPerformed

    private void tablaConsultaDatosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaConsultaDatosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            modificarRapido("Personales");
        } else if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            eliminarRapido("Personales");
        } else if (evt.getKeyCode() == 32) {
            bookFotoRapido("Personales");
        } else if (evt.getKeyCode() == KeyEvent.VK_ALT) {
            detallePrestacionRapido("Personales");
        } else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            antecedentesRapido("Personales");
        }
    }//GEN-LAST:event_tablaConsultaDatosKeyPressed

    private void listaEstadoDatosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaEstadoDatosItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String elemento = listaEstadoDatos.getSelectedItem().toString();
            limpiarTablaDatos();
            consultaEstado(elemento);
        }
    }//GEN-LAST:event_listaEstadoDatosItemStateChanged

    private void listaConsultaOrientacionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaConsultaOrientacionItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            consultarApariencia();
        }
    }//GEN-LAST:event_listaConsultaOrientacionItemStateChanged

    private void listaConsultaOjosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaConsultaOjosItemStateChanged
        consultarApariencia();
    }//GEN-LAST:event_listaConsultaOjosItemStateChanged

    private void numBustosModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numBustosModifKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_numBustosModifKeyTyped

    private void numColaModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numColaModifKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_numColaModifKeyTyped

    private void numCalzadoModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numCalzadoModifKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_numCalzadoModifKeyTyped

    private void numCinturaModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numCinturaModifKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_numCinturaModifKeyTyped

    private void numAlturaModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numAlturaModifKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_numAlturaModifKeyTyped

    private void txtNombreModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreModifKeyTyped
        validarCamposTextos(evt);
    }//GEN-LAST:event_txtNombreModifKeyTyped

    private void txtApellidoModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoModifKeyTyped
        validarCamposTextos(evt);
    }//GEN-LAST:event_txtApellidoModifKeyTyped

    private void txtDNIModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDNIModifKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_txtDNIModifKeyTyped

    private void txtTelModifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelModifKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_txtTelModifKeyTyped

    private void txtBusquedaDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaDatosActionPerformed
        limpiarTablaDatos();
        fillTableListPromotor(listaTablaDatos);
    }//GEN-LAST:event_txtBusquedaDatosActionPerformed

    private void txtBusquedaDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBusquedaDatosMouseClicked
        limpiarTablaDatos();
        fillTableListPromotor(listaTablaDatos);
    }//GEN-LAST:event_txtBusquedaDatosMouseClicked

    private void tablaConsultaAparienciaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaConsultaAparienciaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            modificarRapido("Apariencia");
        } else if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            eliminarRapido("Apariencia");
        } else if (evt.getKeyCode() == 32) {
            bookFotoRapido("Apariencia");
        } else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            antecedentesRapido("Apariencia");
        }
    }//GEN-LAST:event_tablaConsultaAparienciaKeyPressed

    private void txtDNIKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDNIKeyPressed
        //txtDNI.setTransferHandler(null);
    }//GEN-LAST:event_txtDNIKeyPressed

    private void txtNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyPressed
        //txtNombre.setTransferHandler(null);
    }//GEN-LAST:event_txtNombreKeyPressed

    private void txtApellidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyPressed
        //txtApellido.setTransferHandler(null);
    }//GEN-LAST:event_txtApellidoKeyPressed

    private void txtTelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelKeyPressed
        //txtTel.setTransferHandler(null);
    }//GEN-LAST:event_txtTelKeyPressed

    private void numBustosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numBustosKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_numBustosKeyTyped

    private void numColaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numColaKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_numColaKeyTyped

    private void numCalzadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numCalzadoKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_numCalzadoKeyTyped

    private void numBustosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numBustosKeyPressed
        //numBustos.setTransferHandler(null);
    }//GEN-LAST:event_numBustosKeyPressed

    private void numCalzadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numCalzadoKeyPressed
        //numCalzado.setTransferHandler(null);
    }//GEN-LAST:event_numCalzadoKeyPressed

    private void numColaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numColaKeyPressed
        //numCola.setTransferHandler(null);
    }//GEN-LAST:event_numColaKeyPressed

    private void numCinturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numCinturaKeyPressed
        // numCintura.setTransferHandler(null);
    }//GEN-LAST:event_numCinturaKeyPressed

    private void numAlturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numAlturaKeyPressed
        //numAltura.setTransferHandler(null);
    }//GEN-LAST:event_numAlturaKeyPressed

    private void numCinturaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numCinturaKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_numCinturaKeyTyped

    private void numAlturaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numAlturaKeyTyped
        validarCamposNumericos(evt);
    }//GEN-LAST:event_numAlturaKeyTyped

    private void txtNombreModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreModifKeyPressed
        //txtNombre.setTransferHandler(null);
    }//GEN-LAST:event_txtNombreModifKeyPressed

    private void txtApellidoModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoModifKeyPressed
        //txtApellidoModif.setTransferHandler(null);
    }//GEN-LAST:event_txtApellidoModifKeyPressed

    private void txtDNIModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDNIModifKeyPressed
        //txtDNIModif.setTransferHandler(null);
    }//GEN-LAST:event_txtDNIModifKeyPressed

    private void txtTelModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelModifKeyPressed
        //txtTelModif.setTransferHandler(null);
    }//GEN-LAST:event_txtTelModifKeyPressed

    private void numBustosModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numBustosModifKeyPressed
        //numBustos.setTransferHandler(null);
    }//GEN-LAST:event_numBustosModifKeyPressed

    private void numColaModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numColaModifKeyPressed
        //numCola.setTransferHandler(null);
    }//GEN-LAST:event_numColaModifKeyPressed

    private void numCalzadoModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numCalzadoModifKeyPressed
        //numCalzado.setTransferHandler(null);
    }//GEN-LAST:event_numCalzadoModifKeyPressed

    private void numCinturaModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numCinturaModifKeyPressed
        //numCintura.setTransferHandler(null);
    }//GEN-LAST:event_numCinturaModifKeyPressed

    private void numAlturaModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numAlturaModifKeyPressed
        //numAltura.setTransferHandler(null);
    }//GEN-LAST:event_numAlturaModifKeyPressed

    private void fechaNacimientoModifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fechaNacimientoModifKeyPressed
        //fechaNacimientoModif.setTransferHandler(null);
    }//GEN-LAST:event_fechaNacimientoModifKeyPressed

    private void fechaNacimientoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fechaNacimientoKeyPressed
        //fechaNacimiento.setTransferHandler(null);
    }//GEN-LAST:event_fechaNacimientoKeyPressed

    private void listaConsultaCabelloItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaConsultaCabelloItemStateChanged
        consultarApariencia();
    }//GEN-LAST:event_listaConsultaCabelloItemStateChanged

    private void listaConsultaAlturaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaConsultaAlturaItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            consultarApariencia();
        }
    }//GEN-LAST:event_listaConsultaAlturaItemStateChanged

    private void validarCamposTextos(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (Character.isDigit(c)) {
            getToolkit().beep();
            evt.consume();
        }
    }

    private void validarCamposNumericos(KeyEvent evt) {
        char c = evt.getKeyChar();
        if ((c < '0' || c > '9')) {
            evt.consume();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnRegistrar1;
    private javax.swing.ButtonGroup busquedaDatos;
    private com.toedter.calendar.JDateChooser fechaNacimiento;
    private com.toedter.calendar.JDateChooser fechaNacimientoModif;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelContador;
    private javax.swing.JLabel labelContadorApariencia;
    private javax.swing.JLabel lblFechaRegistro;
    private javax.swing.JLabel lblFechaRegistroModif;
    private javax.swing.JComboBox<String> listaBeneficiosModif;
    private javax.swing.JComboBox<String> listaBeneficiosRegistrar;
    private javax.swing.JComboBox<String> listaCabello;
    private javax.swing.JComboBox<String> listaCabelloModif;
    private javax.swing.JComboBox<String> listaConsultaAltura;
    private javax.swing.JComboBox<String> listaConsultaCabello;
    private javax.swing.JComboBox<String> listaConsultaOjos;
    private javax.swing.JComboBox<String> listaConsultaOrientacion;
    private javax.swing.JComboBox<String> listaEstado;
    private javax.swing.JComboBox<String> listaEstadoDatos;
    private javax.swing.JComboBox<String> listaEstadoModif;
    private javax.swing.JComboBox<String> listaMarcaModif;
    private javax.swing.JComboBox<String> listaMarcaRegistrar;
    private javax.swing.JComboBox<String> listaMovilidad;
    private javax.swing.JComboBox<String> listaMovilidadModif;
    private javax.swing.JComboBox<String> listaOjos;
    private javax.swing.JComboBox<String> listaOjosModif;
    private javax.swing.JComboBox<String> listaOrientacionModif;
    private javax.swing.JComboBox<String> listaOrientacionRegistrar;
    private javax.swing.JMenuItem menuBook;
    private javax.swing.JMenuItem menuEliminar;
    private javax.swing.JMenuItem menuModificar;
    private javax.swing.JTextField numAltura;
    private javax.swing.JTextField numAlturaModif;
    private javax.swing.JTextField numBustos;
    private javax.swing.JTextField numBustosModif;
    private javax.swing.JTextField numCalzado;
    private javax.swing.JTextField numCalzadoModif;
    private javax.swing.JTextField numCintura;
    private javax.swing.JTextField numCinturaModif;
    private javax.swing.JTextField numCola;
    private javax.swing.JTextField numColaModif;
    private javax.swing.JPanel pestañaConsultar;
    private javax.swing.JPanel pestañaRegistrar;
    private javax.swing.JPanel pestañaRegistrar1;
    private javax.swing.JTabbedPane pestañas;
    private javax.swing.JTabbedPane pestañasConsultar;
    private javax.swing.JPopupMenu popMenuTabla;
    private javax.swing.JTable tablaConsultaApariencia;
    private javax.swing.JTable tablaConsultaDatos;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtApellidoModif;
    private javax.swing.JTextField txtBusquedaDatos;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtDNIModif;
    private javax.swing.JTextField txtDNIModificar;
    private javax.swing.JTextField txtDomicilio;
    private javax.swing.JTextField txtDomicilioModif;
    private javax.swing.JTextField txtEdad;
    private javax.swing.JTextField txtEdadModif;
    private javax.swing.JTextField txtFacebook;
    private javax.swing.JTextField txtFacebookModif;
    private javax.swing.JTextField txtInstragram;
    private javax.swing.JTextField txtInstragramModif;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombreModif;
    private javax.swing.JTextField txtTel;
    private javax.swing.JTextField txtTelModif;
    // End of variables declaration//GEN-END:variables
}
