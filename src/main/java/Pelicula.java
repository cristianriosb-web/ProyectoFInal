public class Pelicula extends ElementoCatalogo implements Habilitable{
    private String genero;
    private int duracionMinutos;
    private ClasificacionPelicula clasificacion;
    private EstadoPelicula estado;

    public Pelicula(String codigo,String nombre, String genero, int duracionMinutos, ClasificacionPelicula clasificacion) {
        super(codigo,nombre);
        this.genero = genero;
        this.duracionMinutos = duracionMinutos;
        this.clasificacion = clasificacion;
        this.estado = EstadoPelicula.DISPONIBLE;

    }
    public void actualizar(String nombre, String genero, int duracionMinutos, ClasificacionPelicula clasificacion){
        setNombre(nombre);
        this.genero = genero;
        this.duracionMinutos = duracionMinutos;
        this.clasificacion = clasificacion;
    }
    @Override
    public boolean estaDisponible() {
        return this.estado.equals(EstadoPelicula.DISPONIBLE);
    }
    @Override
    public void habilitar(){
        this.estado = EstadoPelicula.DISPONIBLE;
    }
    @Override
    public void deshabilitar(){
        this.estado = EstadoPelicula.NO_DISPONIBLE;
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
        return "Pelicula{" + ", genero=" + genero + ", duracionMinutos=" + duracionMinutos + ", clasificacion=" + clasificacion + ", estado=" + estado + '}';
    }
}
