import java.util.ArrayList;
import java.util.List;

public class Sala {
    private String codigo;
    private String nombre;
    private int capacidad;
    private EstadoSala estado;
    private List<Asiento> asientos;

    public Sala(String codigo, String nombre, int capacidad, EstadoSala estado, List<Asiento> asientos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.estado = estado;
        this.asientos = new ArrayList<>();
        generarAsientos();
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    public EstadoSala getEstado() {
        return estado;
    }
    public void setEstado(EstadoSala estado) {
        this.estado = estado;
    }
    public List<Asiento> getAsientos() {
        return asientos;
    }
    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }
    @Override
    public String toString() {
        return "Sala{" + "codigo=" + codigo + ", nombre=" + nombre + ", capacidad=" + capacidad + ", estado=" + estado + ", asientos=" + asientos + '}';
    }
    public void generarAsientos(){
        this.asientos.clear();
        int asientosPorFila=12;
        char fila='A';
        int numeroFila=1;
        for(int i=0;i<this.capacidad;i++){
            String codigoAsiento = ""+fila+numeroFila;
            Asiento asiento = new Asiento(codigoAsiento, fila, numeroFila, EstadoAsiento.DISPONIBLE);
            this.asientos.add(asiento);
            numeroFila++;
            if(numeroFila>asientosPorFila){
                numeroFila=1;
                fila++;
            }
        }
    }
    public List<Asiento> clonarAsientos() {
        List<Asiento> copia = new ArrayList<>();
        for (Asiento a : this.asientos) {
            copia.add(new Asiento(a.getCodigo(), a.getFila(), a.getNumero(), EstadoAsiento.DISPONIBLE));
        }
        return copia;
    }
}
