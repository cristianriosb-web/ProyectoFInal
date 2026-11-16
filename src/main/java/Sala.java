import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sala extends ElementoCatalogo {

    public static final int MAX_FILAS = 26; // letras A..Z

    private int filas;
    private int asientosPorFila;
    private EstadoSala estado;
    private final List<Asiento> asientos = new ArrayList<>();

    public Sala(String codigo, String nombre, int filas, int asientosPorFila) throws DatoInvalidoException {
        super(codigo, nombre);
        actualizar(nombre, filas, asientosPorFila);
        this.estado = EstadoSala.DISPONIBLE;
    }

    public void actualizar(String nombre, int filas, int asientosPorFila) throws DatoInvalidoException {
        Validador.positivo(filas, "filas");
        Validador.positivo(asientosPorFila, "asientos por fila");
        if (filas > MAX_FILAS) {
            throw new DatoInvalidoException("Una sala puede tener máximo " + MAX_FILAS + " filas");
        }
        setNombre(nombre);
        this.filas = filas;
        this.asientosPorFila = asientosPorFila;
        generarAsientos();
    }

    private void generarAsientos() {
        asientos.clear();
        for (int f = 0; f < filas; f++) {
            char letra = (char) ('A' + f);
            for (int n = 1; n <= asientosPorFila; n++) {
                asientos.add(new Asiento(letra, n));
            }
        }
    }

    public List<Asiento> crearCopiaAsientos() {
        List<Asiento> copia = new ArrayList<>();
        for (Asiento a : asientos) {
            copia.add(a.copiar());
        }
        return copia;
    }

    public int getCapacidad() {
        return filas * asientosPorFila;
    }

    @Override
    public boolean estaDisponible() {
        return estado == EstadoSala.DISPONIBLE;
    }

    @Override
    public void deshabilitar() {
        estado = EstadoSala.FUERA_DE_SERVICIO;
    }

    @Override
    public void habilitar() {
        estado = EstadoSala.DISPONIBLE;
    }

    public int getFilas() {
        return filas;
    }

    public int getAsientosPorFila() {
        return asientosPorFila;
    }

    public EstadoSala getEstado() {
        return estado;
    }

    public List<Asiento> getAsientos() {
        return Collections.unmodifiableList(asientos);
    }
}
