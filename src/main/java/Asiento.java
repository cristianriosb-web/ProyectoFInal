
public class Asiento implements Identificable {

    private final String codigo;
    private final char fila;
    private final int numero;
    private EstadoAsiento estado;

    public Asiento(char fila, int numero) {
        this.fila = Character.toUpperCase(fila);
        this.numero = numero;
        this.codigo = "" + this.fila + numero;
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    public Asiento copiar() {
        return new Asiento(fila, numero);
    }

    public boolean estaDisponible() {
        return estado == EstadoAsiento.DISPONIBLE;
    }

    public void ocupar() throws AsientoOcupadoException {
        if (!estaDisponible()) {
            throw new AsientoOcupadoException("El asiento " + codigo + " ya está ocupado");
        }
        estado = EstadoAsiento.OCUPADO;
    }

    public void liberar() {
        estado = EstadoAsiento.DISPONIBLE;
    }

    @Override
    public String getCodigo() {
        return codigo;
    }

    public char getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return codigo + " " + (estaDisponible() ? "Disponible" : "Ocupado");
    }
}
