import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Funcion {
    private String codigo;
    private Pelicula pelicula;
    private Sala sala;
    private LocalDate fecha;
    private LocalTime hora;
    private double precioBase;
    private EstadoFuncion estado;
    private List<Asiento> asientos;

    public Funcion(String codigo, Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora, double precioBase, EstadoFuncion estado, List<Asiento> asientos) {
        this.codigo = codigo;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
        this.precioBase = precioBase;
        this.estado = estado;
      actualizarAsientos(asientos);
    }
    public void actualizarAsientos(List<Asiento> asientos){
       if(this.sala != null){
           this.asientos=this.sala.clonarAsientos();
       }
    }
    public Asiento buscarAsiento(String codigoAsiento){
        for(Asiento asiento:asientos){
        if(asiento.getCodigo().equals(codigoAsiento)){
            return asiento;
        }}
        return null;
    }
    public int contarDisponibles(){
        int disponibles =0;
        for(Asiento asiento:asientos){
            if(asiento.estaDisponible()){
                disponibles++;
            }
        }
        return disponibles;
    }
    public int contarOcupados(){
        return asientos.size()-contarDisponibles();
    }
    public boolean hayAsientosDisponibles(){
        return contarDisponibles()>0;
    }
    public List<Asiento> getAsientosDisponibles(){
        List<Asiento> disponibles = new java.util.ArrayList<>();
        for(Asiento asiento:asientos){
            if(asiento.estaDisponible()){
                disponibles.add(asiento);
            }
        }
        return disponibles;
    }
    public boolean estaProgramada(){
        return this.estado == EstadoFuncion.PROGRAMADA;
    }
    public void cancelar(){
        this.estado = EstadoFuncion.CANCELADA;
    }
    public void finalizar(){
        this.estado = EstadoFuncion.FINALIZADA;
    }
    public int compareTo(Funcion otra){
        return this.fecha.compareTo(otra.fecha);
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
        actualizarAsientos(sala.getAsientos());
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
        return asientos.size();
    }
    @Override
    public String toString() {
        return "Funcion{" + "codigo=" + codigo + ", pelicula=" + pelicula + ", sala=" + sala + ", fecha=" + fecha + ", hora=" + hora + ", precioBase=" + precioBase + ", estado=" + estado + ", asientos=" + asientos + '}';
    }
}
