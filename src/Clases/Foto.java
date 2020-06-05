package Clases;

public class Foto {

    private int codFoto;
    private int codBockFoto;

    public Foto(int codFoto, int codBockFoto) {
        this.codFoto = codFoto;
        this.codBockFoto = codBockFoto;
    }

    public Foto() {
    }

    public int getCodFoto() {
        return codFoto;
    }

    public void setCodFoto(int codFoto) {
        this.codFoto = codFoto;
    }

    public int getCodBockFoto() {
        return codBockFoto;
    }

    public void setCodBockFoto(int codBockFoto) {
        this.codBockFoto = codBockFoto;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.codFoto;
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
        final Foto other = (Foto) obj;
        if (this.codFoto != other.codFoto) {
            return false;
        }
        return true;
    }

}
