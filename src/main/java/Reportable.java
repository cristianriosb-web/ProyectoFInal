import java.time.LocalDate;
import java.util.List;

public interface Reportable {
    public int getTotalEntradasVendidas();
    double getTotalIngresos();
    Pelicula getPeliculaMasVendida();
    List<Funcion> getFuncionesProgramadasPorFecha(LocalDate fecha);
}
