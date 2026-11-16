
public class Pelicula extends ElementoCatalogo {

    private String genero;
    private int duracionMinutos;
    private ClasificacionPelicula clasificacion;
    private EstadoPelicula estado;

    public Pelicula(String codigo, String nombre, String genero, int duracionMinutos,
                    ClasificacionPelicula clasificacion) throws DatoInvalidoException {
        super(codigo, nombre);
        actualizar(nombre, genero, duracionMinutos, clasificacion);
        this.estado = EstadoPelicula.DISPONIBLE;
    }

    public void actualizar(String nombre, String genero, int duracionMinutos,
                           ClasificacionPelicula clasificacion) throws DatoInvalidoException {
        Validador.texto(genero, "género");
        Validador.positivo(duracionMinutos, "duración");
        Validador.noNulo(clasificacion, "clasificación");
        setNombre(nombre);
        this.genero = genero.trim();
        this.duracionMinutos = duracionMinutos;
        this.clasificacion = clasificacion;
    }

    @Override
    public boolean estaDisponible() {
        return estado == EstadoPelicula.DISPONIBLE;
    }

    @Override
    public void deshabilitar() {
        estado = EstadoPelicula.NO_DISPONIBLE;
    }
    @Override
    public void habilitar() {
        estado = EstadoPelicula.DISPONIBLE;
    }

    public String getGenero() {
        return genero;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public ClasificacionPelicula getClasificacion() {
        return clasificacion;
    }

    public EstadoPelicula getEstado() {
        return estado;
    }
}
