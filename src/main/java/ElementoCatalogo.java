public abstract class ElementoCatalogo extends Entidad{
    private String nombre;

    public ElementoCatalogo(String codigo, String nombre) throws DatoInvalidoException {
        super(codigo);
        Validador.texto(nombre, "nombre");
        this.nombre = nombre.trim();
    }

    public void setNombre(String nombre) throws DatoInvalidoException {
        Validador.texto(nombre, "nombre");
        this.nombre = nombre.trim();
    }
    public String getNombre() {
        return nombre;
    }


    @Override
    public String toString() {
        return codigo+""+nombre;
    }
}
