import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Funcion extends Entidad implements Comparable<Funcion>,Cancelable{
    private Pelicula pelicula;
    private Sala sala;
    private LocalDate fecha;
    private LocalTime hora;
    private double precioBase;
    private EstadoFuncion estado;
    private List<Asiento> asientos;

    public Funcion( String codigo,Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora, double precioBase) throws DatoInvalidoException {
        super(codigo);
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
        this.precioBase = precioBase;
        this.estado = EstadoFuncion.PROGRAMADA;
        this.asientos = sala.crearCopiaAsientos();
    }
    public void actualizar(LocalDate fecha, LocalTime hora, double precioBase) throws DatoInvalidoException {
        Validador.noNulo(fecha, "una fecha");
        Validador.noNulo(hora, "una hora");
        Validador.positivo(precioBase, "valor base");
        this.fecha = fecha;
        this.hora = hora;
        this.precioBase = precioBase;
    }
    public Asiento buscarAsiento(String codigo) throws AsientoNoEncontradoException {
        if (codigo == null || codigo.isBlank()) {
            throw new AsientoNoEncontradoException("El código del asiento no puede ser nulo o vacío.");
        }
        for (Asiento asiento : asientos) {
            if (asiento.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return asiento;
            }
        }
        throw new AsientoNoEncontradoException("No se encontró el asiento con código: " + codigo);
    }
    public int contarDisponibles(){
        int disponibles =0;
        for(Asiento asiento:this.asientos){
            if(asiento.estaDisponible()){
                disponibles++;
            }
        }
        return disponibles;
    }
    public int contarOcupados(){
        return this.asientos.size()-contarDisponibles();
    }
    public boolean hayAsientosDisponibles(){
        return contarDisponibles()>0;
    }
    public List<Asiento> getAsientosDisponibles(){
        List<Asiento> disponibles = new ArrayList<>();
        for(Asiento asiento:this.asientos){
            if(asiento.estaDisponible()){
                disponibles.add(asiento);
            }
        }
        return disponibles;
    }
    public boolean estaProgramada(){
        return this.estado == EstadoFuncion.PROGRAMADA;
    }
    public void finalizar(){
        this.estado = EstadoFuncion.FINALIZADA;
    }
    @Override
    public void cancelar(){
        this.estado = EstadoFuncion.CANCELADA;
    }
    @Override
    public int compareTo(Funcion otra){
        int comparacionFecha= this.fecha.compareTo(otra.fecha);
        if(comparacionFecha!=0){
            return comparacionFecha;
        }
        return this.hora.compareTo(otra.hora);
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public Pelicula getPelicula() {
        return pelicula;
    }
    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }
    public Sala getSala() {
        return sala;
    }
   public void setSala(Sala sala) {
        this.sala = sala;
   }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public LocalTime getHora() {
        return hora;
    }
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    public double getPrecioBase() {
        return precioBase;
    }
    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }
    public EstadoFuncion getEstado() {
        return estado;
    }
    public void setEstado(EstadoFuncion estado) {
        this.estado = estado;
    }
    public List<Asiento> getAsientos() {
        return asientos;
    }
    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }
    public int getTotalAsientos(){
        return this.asientos.size();
    }
    @Override
    public String toString() {
        return "Funcion{" + "codigo=" + codigo + ", pelicula=" + pelicula + ", sala=" + sala + ", fecha=" + fecha + ", hora=" + hora + ", precioBase=" + precioBase + ", estado=" + estado + ", asientos=" + asientos + '}';
    }
}
