import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Venta extends Entidad implements Cancelable {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Cliente cliente;
    private final Funcion funcion;
    private final Asiento asiento;
    private final TipoCliente tipoEntrada;
    private final LocalDateTime fechaHoraVenta;
    private final double valorPagado;
    private EstadoVenta estado;

    public Venta(String codigo, Cliente cliente, Funcion funcion, Asiento asiento,
                 TipoCliente tipoEntrada, LocalDateTime fechaHoraVenta, double valorPagado)
            throws DatoInvalidoException {
        super(codigo);
        this.cliente = cliente;
        this.funcion = funcion;
        this.asiento = asiento;
        this.tipoEntrada = tipoEntrada;
        this.fechaHoraVenta = fechaHoraVenta;
        this.valorPagado = valorPagado;
        this.estado = EstadoVenta.ACTIVA;
    }

    public boolean estaActiva() {
        return estado == EstadoVenta.ACTIVA;
    }

    @Override
    public void cancelar() {
        if (!estaActiva()) {
            String codigo = "";
            throw new VentaYaCanceladaException("La venta " + codigo + " ya estaba cancelada");
        }
        asiento.liberar();
        estado = EstadoVenta.CANCELADA;
    }

    public String generarTiquete() {
        String codigo = "";
        return "========== CINEMAUQ ==========\n"
                + "Venta:     " + codigo + "\n"
                + "Cliente:   " + cliente + "\n"
                + "Película:  " + funcion.getPelicula().getNombre() + "\n"
                + "Sala:      " + funcion.getSala().getNombre() + "\n"
                + "Función:   " + funcion.getFecha() + " " + funcion.getHora() + "\n"
                + "Asiento:   " + asiento.getCodigo() + "\n"
                + "Entrada:   " + tipoEntrada + "\n"
                + "Valor:     $" + String.format("%,.0f", valorPagado) + "\n"
                + "Vendida:   " + fechaHoraVenta.format(FORMATO) + "\n"
                + "==============================";
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public TipoCliente getTipoEntrada() {
        return tipoEntrada;
    }

    public LocalDateTime getFechaHoraVenta() {
        return fechaHoraVenta;
    }

    public double getValorPagado() {
        return valorPagado;
    }

    public EstadoVenta getEstado() {
        return estado;
    }
}
