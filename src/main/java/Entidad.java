public abstract class Entidad implements Identificable{
    private String codigo;
    public Entidad(String codigo) {
        this.codigo = codigo;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public boolean equals(Object o) {
        return codigo.equals(((Entidad) o).getCodigo());
    }
    public int hashCode() {
        return codigo.hashCode();
    }
}
