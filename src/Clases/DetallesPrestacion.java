package Clases;

public class DetallesPrestacion {

    private int codPrestacion;
    private String obraSocial;
    private String monotributista;
    private String F931;
    private int contratoFirmado;
    private int codPromotor;

    public DetallesPrestacion(int codPrestacion, String obraSocial, String monotributista, String F931, int contratoFirmado, int codPromotor) {
        this.codPrestacion = codPrestacion;
        this.obraSocial = obraSocial;
        this.monotributista = monotributista;
        this.F931 = F931;
        this.contratoFirmado = contratoFirmado;
        this.codPromotor = codPromotor;
    }

    public DetallesPrestacion(String obraSocial, String monotributista, String F931, int contratoFirmado) {
        this.obraSocial = obraSocial;
        this.monotributista = monotributista;
        this.F931 = F931;
        this.contratoFirmado = contratoFirmado;
    }

    public DetallesPrestacion() {
    }

    public int getCodPrestacion() {
        return codPrestacion;
    }

    public void setCodPrestacion(int codPrestacion) {
        this.codPrestacion = codPrestacion;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public String getMonotributista() {
        return monotributista;
    }

    public void setMonotributista(String monotributista) {
        this.monotributista = monotributista;
    }

    public String getF931() {
        return F931;
    }

    public void setF931(String F931) {
        this.F931 = F931;
    }

    public int getContratoFirmado() {
        return contratoFirmado;
    }

    public void setContratoFirmado(int contratoFirmado) {
        this.contratoFirmado = contratoFirmado;
    }

    public int getCodPromotor() {
        return codPromotor;
    }

    public void setCodPromotor(int codPromotor) {
        this.codPromotor = codPromotor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.codPrestacion;
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
        final DetallesPrestacion other = (DetallesPrestacion) obj;
        if (this.codPrestacion != other.codPrestacion) {
            return false;
        }
        return true;
    }

}
