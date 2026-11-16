public abstract class ElementoCatalogo extends Entidad{
    private String nombre;

    public ElementoCatalogo(String codigo, String nombre) {
        super(codigo);
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return codigo+""+nombre;
    }
}
