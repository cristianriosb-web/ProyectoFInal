public class Asiento {
    private String codigo;
    private char fila;
    private int numero;
    private EstadoAsiento estado;

    public Asiento(String codigo, char fila, int numero, EstadoAsiento estado) {
        this.codigo = codigo;
        this.fila = fila;
        this.numero = numero;
        this.estado = estado;
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
    public String ocupar(){
        this.estado = EstadoAsiento.OCUPADO;
        return "Asiento ocupado";
    }
    public String librerar(){
        this.estado = EstadoAsiento.DISPONIBLE;
        return "Asiento liberado";
    }
    public boolean estaDisponible(){
        return this.estado == EstadoAsiento.DISPONIBLE;
    }
}
