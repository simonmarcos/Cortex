package Clases;

public class Orientacion implements Comparable<Orientacion> {

    private int codOrientacion;
    private String nombre;

    public Orientacion() {
    }

    public Orientacion(int codOrientacion, String nombre) {
        this.codOrientacion = codOrientacion;
        this.nombre = nombre;
    }

    public int getCodOrientacion() {
        return codOrientacion;
    }

    public void setCodOrientacion(int codOrientacion) {
        this.codOrientacion = codOrientacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.codOrientacion;
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
        final Orientacion other = (Orientacion) obj;
        if (this.codOrientacion != other.codOrientacion) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Orientacion o) {
        return this.nombre.compareTo(o.getNombre());
    }

}
