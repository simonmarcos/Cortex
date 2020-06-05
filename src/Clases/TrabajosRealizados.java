package Clases;

import java.sql.Date;

public class TrabajosRealizados {

    private int codTrabajo;
    private java.sql.Date fecha;
    private int horas;
    private int codMarca;
    private int DNI;

    public TrabajosRealizados(int codTrabajo, Date fecha, int horas, int codMarca, int DNI) {
        this.codTrabajo = codTrabajo;
        this.fecha = fecha;
        this.horas = horas;
        this.codMarca = codMarca;
        this.DNI = DNI;
    }

    public TrabajosRealizados() {
    }

    public int getCodTrabajo() {
        return codTrabajo;
    }

    public void setCodTrabajo(int codTrabajo) {
        this.codTrabajo = codTrabajo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getCodMarca() {
        return codMarca;
    }

    public void setCodMarca(int codMarca) {
        this.codMarca = codMarca;
    }

    public int getDNI() {
        return DNI;
    }

    public void setDNI(int DNI) {
        this.DNI = DNI;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.codTrabajo;
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
        final TrabajosRealizados other = (TrabajosRealizados) obj;
        if (this.codTrabajo != other.codTrabajo) {
            return false;
        }
        return true;
    }

}
