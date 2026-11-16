public abstract class ElementoCatalogo extends Entidad implements Habilitable {

    protected String nombre;

    protected ElementoCatalogo(String codigo, String nombre) throws DatoInvalidoException {
        super(codigo);
        setNombre(nombre);
    }

    protected void setNombre(String nombre) throws DatoInvalidoException {
        Validador.texto(nombre, "nombre");
        this.nombre = nombre.trim();
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
