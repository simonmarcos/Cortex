package Clases;

public class Marca implements Comparable<Marca> {

    private int codMarca;
    private String nombre;

    public Marca(int codMarca, String nombre) {
        this.codMarca = codMarca;
        this.nombre = nombre;
    }

    public Marca() {
    }

    public int getCodMarca() {
        return codMarca;
    }

    public void setCodMarca(int codMarca) {
        this.codMarca = codMarca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.codMarca;
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
        final Marca other = (Marca) obj;
        if (this.codMarca != other.codMarca) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Marca o) {
        return this.nombre.compareTo(o.getNombre());
    }

}
