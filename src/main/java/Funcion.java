import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Funcion extends Entidad implements Cancelable, Comparable<Funcion> {

    private final Pelicula pelicula;
    private final Sala sala;
    private LocalDate fecha;
    private LocalTime hora;
    private double valorBase;
    private EstadoFuncion estado;
    private final List<Asiento> asientos;

    public Funcion(String codigo, Pelicula pelicula, Sala sala, LocalDate fecha,
                   LocalTime hora, double valorBase) throws DatoInvalidoException {
        super(codigo);
        Validador.noNulo(pelicula, "una película");
        Validador.noNulo(sala, "una sala");
        this.pelicula = pelicula;
        this.sala = sala;
        actualizar(fecha, hora, valorBase);
        this.estado = EstadoFuncion.PROGRAMADA;
        this.asientos = new ArrayList<>(sala.crearCopiaAsientos());
    }

    public void actualizar(LocalDate fecha, LocalTime hora, double valorBase) throws DatoInvalidoException {
        Validador.noNulo(fecha, "una fecha");
        Validador.noNulo(hora, "una hora");
        Validador.positivo(valorBase, "valor base");
        this.fecha = fecha;
        this.hora = hora;
        this.valorBase = valorBase;
    }

    public Asiento buscarAsiento(String codigoAsiento) throws AsientoNoEncontradoException {
        if (codigoAsiento != null) {
            for (Asiento a : asientos) {
                if (a.getCodigo().equalsIgnoreCase(codigoAsiento.trim())) {
                    return a;
                }
            }
        }
        throw new AsientoNoEncontradoException("El asiento " + codigoAsiento
                + " no existe en la sala " + sala.getNombre());
    }

    public int getTotalAsientos() {
        return asientos.size();
    }

    public int contarDisponibles() {
        int contador = 0;
        for (Asiento a : asientos) {
            if (a.estaDisponible()) {
                contador++;
            }
        }
        return contador;
    }

    public int contarOcupados() {
        return getTotalAsientos() - contarDisponibles();
    }

    public boolean hayAsientosDisponibles() {
        return contarDisponibles() > 0;
    }

    public List<Asiento> getAsientosDisponibles() {
        List<Asiento> libres = new ArrayList<>();
        for (Asiento a : asientos) {
            if (a.estaDisponible()) {
                libres.add(a);
            }
        }
        return libres;
    }

    public boolean estaProgramada() {
        return estado == EstadoFuncion.PROGRAMADA;
    }

    public void finalizar() {
        estado = EstadoFuncion.FINALIZADA;
    }

    @Override
    public void cancelar() {
        estado = EstadoFuncion.CANCELADA;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public double getValorBase() {
        return valorBase;
    }

    public EstadoFuncion getEstado() {
        return estado;
    }

    public List<Asiento> getAsientos() {
        return Collections.unmodifiableList(asientos);
    }

    @Override
    public int compareTo(Funcion otra) {
        int porFecha = fecha.compareTo(otra.fecha);
        return porFecha != 0 ? porFecha : hora.compareTo(otra.hora);
    }

    @Override
    public String toString() {
        return codigo + " - " + pelicula.getNombre() + " | " + sala.getNombre()
                + " | " + fecha + " " + hora;
    }
}
