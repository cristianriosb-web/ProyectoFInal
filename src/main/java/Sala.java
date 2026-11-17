import javax.swing.text.Element;
import java.util.ArrayList;
import java.util.List;

public class Sala extends ElementoCatalogo implements Habilitable{
    private int filas;
    private int asientosPorFila;
    private EstadoSala estado;
    private List<Asiento> asientos;

    public Sala(String codigo,String nombre,int filas,int asientosPorFila) throws DatoInvalidoException {
        super(codigo,nombre);
        this.filas = filas;
        this.asientosPorFila = asientosPorFila;
        this.estado = EstadoSala.DISPONIBLE;
        this.asientos = new ArrayList<>();
        generarAsientos(getFilas(),getAsientosPorFila());
    }
    public void actualizar(String nombre, int filas, int asientosPorFila) throws DatoInvalidoException {
        setNombre(nombre);
        this.filas = filas;
        this.asientosPorFila = asientosPorFila;
        generarAsientos(getFilas(),getAsientosPorFila());
    }
    public void generarAsientos(int filas, int asientosPorFila){
       this.asientos.clear();
       for(int i=0;i<this.filas;i++){
           char letra = (char)('A'+i);
           for(int num =1;num<=this.asientosPorFila;num++){
            this.asientos.add(new Asiento(letra,num));
           }
       }
    }
    public List<Asiento> crearCopiaAsientos(){
        List<Asiento> copia = new ArrayList<>();
        for(Asiento asiento:this.asientos){
            copia.add(asiento.copiar());
        }
        return copia;
    }
    public int getCapacidad(){
        return filas*asientosPorFila;
    }

    @Override
    public boolean estaDisponible() {
        return this.estado==EstadoSala.DISPONIBLE;
    }

    @Override
    public void habilitar() {
        this.estado= EstadoSala.DISPONIBLE;

    }

    @Override
    public void deshabilitar() {
        this.estado= EstadoSala.FUERA_DE_SERVICIO;

    }
    public int getFilas() {
        return filas;
    }
    public void setFilas(int filas) {
        this.filas = filas;
    }
    public int getAsientosPorFila() {
        return asientosPorFila;
    }
    public void setAsientosPorFila(int asientosPorFila) {
        this.asientosPorFila = asientosPorFila;
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
        return "Sala{" +   ", estado=" + estado + ", asientos=" + asientos + '}';
    }

}
