public abstract class Persona {
    private String identificacion;
    private String nombre;

    public Persona(String identificacion, String nombre) throws DatoInvalidoException {
        Validador.texto(identificacion, "identificación");
        Validador.texto(nombre, "nombre");
        this.identificacion = identificacion.trim();
        this.nombre = nombre.trim();
    }
    public String getIdentificacion() {
        return identificacion;
    }
    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    @Override
    public String toString() {
        return "Persona{" + "identificacion=" + identificacion + ", nombre=" + nombre + '}';
    }

}
