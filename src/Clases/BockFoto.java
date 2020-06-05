package Clases;

public class BockFoto {

    private int codBockFoto;
    private int codPromotor;
    private int dni;

    public BockFoto() {
    }

    public BockFoto(int codBockFoto, int codPromotor, int dni) {
        this.codBockFoto = codBockFoto;
        this.codPromotor = codPromotor;
        this.dni = dni;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public int getCodPromotor() {
        return codPromotor;
    }

    public void setCodPromotor(int codPromotor) {
        this.codPromotor = codPromotor;
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
        hash = 53 * hash + this.codBockFoto;
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
        final BockFoto other = (BockFoto) obj;
        if (this.codBockFoto != other.codBockFoto) {
            return false;
        }
        return true;
    }

}
