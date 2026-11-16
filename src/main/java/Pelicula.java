public class Pelicula {
    private String nombre;
    private String genero;
    private int duracionMinutos;
    private ClasificacionPelicula clasificacion;
    private EstadoPelicula estado;

    public Pelicula(String nombre, String genero, int duracionMinutos, ClasificacionPelicula clasificacion, EstadoPelicula estado) {
        this.nombre = nombre;
        this.genero = genero;
        this.duracionMinutos = duracionMinutos;
        this.clasificacion = clasificacion;
        this.estado = estado;

    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public int getDuracionMinutos() {
        return duracionMinutos;
    }
    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }
    public ClasificacionPelicula getClasificacion() {
        return clasificacion;
    }
    public void setClasificacion(ClasificacionPelicula clasificacion) {
        this.clasificacion = clasificacion;
    }
    public EstadoPelicula getEstado() {
        return estado;
    }
    public void setEstado(EstadoPelicula estado) {
        this.estado = estado;
    }
    @Override
    public String toString() {
        return "Pelicula{" + "nombre=" + nombre + ", genero=" + genero + ", duracionMinutos=" + duracionMinutos + ", clasificacion=" + clasificacion + ", estado=" + estado + '}';
    }
    public String habilitar(){
        this.estado = EstadoPelicula.DISPONIBLE;
        return "Pelicula habilitada";
    }
    public String deshabilitar(){
        this.estado = EstadoPelicula.NO_DISPONIBLE;
        return "Pelicula deshabilitada";
    }
}
