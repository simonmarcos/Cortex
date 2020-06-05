package Clases;

import java.sql.Date;

public class Promotora implements Comparable<Promotora> {

    private int codPromotor;
    private int DNI;
    private String nombre;
    private String apellido;
    private String domicilio;
    private java.sql.Date fechaNacimiento;
    private int edad;
    private String telefono;
    private String facebook;
    private String instagram;
    private int orientacion;
    private String cabello;
    private String ojos;
    private int pechos;
    private int cintura;
    private int cola;
    private int codBeneficio;
    private int codMarca;
    private int altura;
    private int calzado;
    private String fechaRegistracion;
    private String estado;
    private String movilidad;

    public Promotora(int codPromotor, int DNI, String nombre, String apellido, String domicilio, Date fechaNacimiento, int edad, String telefono, String facebook, String instagram, int orientacion, String cabello, String ojos, int pechos, int cintura, int cola, int codBeneficio, int codMarca, int altura, int calzado, String fechaRegistracion, String estado, String movilidad) {
        this.codPromotor = codPromotor;
        this.DNI = DNI;
        this.nombre = nombre;
        this.apellido = apellido;
        this.domicilio = domicilio;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.telefono = telefono;
        this.facebook = facebook;
        this.instagram = instagram;
        this.orientacion = orientacion;
        this.cabello = cabello;
        this.ojos = ojos;
        this.pechos = pechos;
        this.cintura = cintura;
        this.cola = cola;
        this.codBeneficio = codBeneficio;
        this.codMarca = codMarca;
        this.altura = altura;
        this.calzado = calzado;
        this.fechaRegistracion = fechaRegistracion;
        this.estado = estado;
        this.movilidad = movilidad;
    }

    public Promotora() {
    }

    public int getCodPromotor() {
        return codPromotor;
    }

    public void setCodPromotor(int codPromotor) {
        this.codPromotor = codPromotor;
    }

    public int getDNI() {
        return DNI;
    }

    public void setDNI(int DNI) {
        this.DNI = DNI;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getFechaRegistracion() {
        return fechaRegistracion;
    }

    public void setFechaRegistracion(String fechaRegistracion) {
        this.fechaRegistracion = fechaRegistracion;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getCalzado() {
        return calzado;
    }

    public void setCalzado(int calzado) {
        this.calzado = calzado;
    }

    public int getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(int orientacion) {
        this.orientacion = orientacion;
    }

    public String getCabello() {
        return cabello;
    }

    public void setCabello(String cabello) {
        this.cabello = cabello;
    }

    public String getOjos() {
        return ojos;
    }

    public void setOjos(String ojos) {
        this.ojos = ojos;
    }

    public int getPechos() {
        return pechos;
    }

    public void setPechos(int pechos) {
        this.pechos = pechos;
    }

    public int getCintura() {
        return cintura;
    }

    public void setCintura(int cintura) {
        this.cintura = cintura;
    }

    public int getCola() {
        return cola;
    }

    public void setCola(int cola) {
        this.cola = cola;
    }

    public int getCodBeneficio() {
        return codBeneficio;
    }

    public void setCodBeneficio(int codBeneficio) {
        this.codBeneficio = codBeneficio;
    }

    public int getCodMarca() {
        return codMarca;
    }

    public void setCodMarca(int codMarca) {
        this.codMarca = codMarca;
    }

    public String getMovilidad() {
        return movilidad;
    }

    public void setMovilidad(String movilidad) {
        this.movilidad = movilidad;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.codPromotor;
        hash = 79 * hash + this.DNI;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Promotora other = (Promotora) obj;
        if ((this.codPromotor != other.codPromotor) && (this.DNI != other.DNI)) {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(Promotora o) {
        if (this.apellido.toUpperCase().compareTo(o.getApellido().toUpperCase()) == 0) {
            if (this.nombre.toUpperCase().compareTo(o.getNombre().toUpperCase()) == 0) {
                return 0;
            }
            return 0;
        }
        return this.apellido.toUpperCase().compareTo(o.getApellido().toUpperCase());
    }
}
