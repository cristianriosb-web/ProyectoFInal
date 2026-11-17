public class Asiento implements Identificable {
    private String codigo;
    private char fila;
    private int numero;
    private EstadoAsiento estado;

    public Asiento( char fila, int numero) {
        this.fila = Character.toUpperCase(fila);
        this.numero = numero;
        this.codigo = "" + this.fila + numero;
        this.estado = EstadoAsiento.DISPONIBLE;
    }
    public Asiento copiar(){
        return new Asiento(this.fila, this.numero);
    }
    public boolean estaDisponible(){
        return this.estado == EstadoAsiento.DISPONIBLE;
    }
    public void ocupar() throws AsientoOcupadoException {
        if (!estaDisponible()) {
            throw new AsientoOcupadoException("El asiento " + codigo + " ya está ocupado.");
        }
        this.estado = EstadoAsiento.OCUPADO;
    }
    public void liberar(){
        this.estado = EstadoAsiento.DISPONIBLE;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public char getFila() {
        return fila;
    }
    public void setFila(char fila) {
        this.fila = fila;
    }
    public int getNumero() {
        return numero;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }
    public EstadoAsiento getEstado() {
        return estado;
    }
    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }
    @Override
    public String toString() {
        return "Asiento{" + "codigo=" + codigo + ", fila=" + fila + ", numero=" + numero + ", estado=" + estado + '}';
    }

}
