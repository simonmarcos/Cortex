package Clases;

public class Beneficio implements Comparable<Beneficio> {

    private int codBeneficio;
    private String nombre;

    public Beneficio(int codBeneficio, String nombre) {
        this.codBeneficio = codBeneficio;
        this.nombre = nombre;
    }

    public Beneficio() {
    }

    public int getCodBeneficio() {
        return codBeneficio;
    }

    public void setCodBeneficio(int codBeneficio) {
        this.codBeneficio = codBeneficio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int compareTo(Beneficio o) {
        return this.nombre.compareTo(o.getNombre());
    }

}
