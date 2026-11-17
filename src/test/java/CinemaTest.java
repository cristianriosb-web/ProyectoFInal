import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class CinemaTest {

    private static final LocalDate FECHA = LocalDate.of(2026, 11, 20);

    private Cinema cinema;
    private Cliente ana;

    @BeforeEach
    void setUp() throws CinemaException, SalaNoDisponibleException {
        cinema = new Cinema("CINEMAUQ");
        cinema.registrarPelicula(new Pelicula("P1", "Interestelar", "Ciencia ficción", 169, ClasificacionPelicula.MAYORES_12));
        cinema.registrarPelicula(new Pelicula("P2", "El Último Algoritmo", "Suspenso", 100, ClasificacionPelicula.MAYORES_15));
        cinema.registrarSala(new Sala("S1", "Sala 1", 2, 2)); // 4 asientos
        cinema.registrarSala(new Sala("S2", "Sala 2", 3, 3));
        cinema.programarFuncion("F1", "P1", "S1", FECHA, LocalTime.of(14, 0), 15000);
        cinema.programarFuncion("F2", "P2", "S2", FECHA, LocalTime.of(18, 0), 10000);
        ana = new Cliente("1", "Ana", TipoCliente.GENERAL);
    }

    // ---------- Películas y salas ----------

    @Test
    void peliculaConCodigoRepetidoLanzaExcepcion() {
        assertThrows(CodigoDuplicadoException.class, () ->
                cinema.registrarPelicula(new Pelicula("p1", "Otra", "Drama", 90, ClasificacionPelicula.TODOS)));
    }

    @Test
    void salaConCodigoRepetidoLanzaExcepcion() {
        assertThrows(CodigoDuplicadoException.class, () ->
                cinema.registrarSala(new Sala("S1", "Otra", 2, 2)));
    }

    @Test
    void modificarYDeshabilitarPelicula() throws CinemaException {
        cinema.modificarPelicula("P1", "Interstellar", "Sci-fi", 170, ClasificacionPelicula.MAYORES_7);
        assertEquals("Interstellar", cinema.buscarPelicula("P1").getNombre());
        cinema.deshabilitarPelicula("P1");
        assertEquals(1, cinema.getPeliculasDisponibles().size());
    }

    @Test
    void buscarPeliculaInexistenteLanzaExcepcion() {
        assertThrows(PeliculaNoEncontradaException.class, () -> cinema.buscarPelicula("X"));
    }

    // ---------- Funciones ----------

    @Test
    void noSeProgramaFuncionEnSalaFueraDeServicio() throws CinemaException {
        cinema.deshabilitarSala("S2");
        assertThrows(SalaNoDisponibleException.class, () ->
                cinema.programarFuncion("F3", "P1", "S2", FECHA, LocalTime.of(22, 0), 15000));
    }

    @Test
    void noSeProgramaFuncionDePeliculaDeshabilitada() throws CinemaException {
        cinema.deshabilitarPelicula("P2");
        assertThrows(PeliculaNoDisponibleException.class, () ->
                cinema.programarFuncion("F3", "P2", "S1", FECHA, LocalTime.of(22, 0), 15000));
    }

    @Test
    void noSeProgramanFuncionesCruzadasEnLaMismaSala() {
        // F1 va de 14:00 a 16:49 en S1
        assertThrows(SalaNoDisponibleException.class, () ->
                cinema.programarFuncion("F3", "P2", "S1", FECHA, LocalTime.of(16, 0), 15000));
    }

    @Test
    void funcionConCodigoRepetidoLanzaExcepcion() {
        assertThrows(CodigoDuplicadoException.class, () ->
                cinema.programarFuncion("F1", "P2", "S2", FECHA.plusDays(1), LocalTime.NOON, 15000));
    }

    @Test
    void funcionesProgramadasPorFecha() throws CinemaException, FuncionNoEncontradaException, SalaNoDisponibleException {
        cinema.programarFuncion("F3", "P2", "S1", FECHA.plusDays(1), LocalTime.NOON, 15000);
        assertEquals(2, cinema.getFuncionesProgramadasPorFecha(FECHA).size());
        cinema.cancelarFuncion("F2");
        assertEquals(1, cinema.getFuncionesProgramadasPorFecha(FECHA).size());
    }

    // ---------- Ventas ----------

    @Test
    void venderEntradaOcupaAsientoYCalculaValor() throws CinemaException, FuncionNoEncontradaException {
        Venta v = cinema.venderEntrada("F1", ana, TipoCliente.ESTUDIANTE, "A1");
        assertEquals("V0001", v.getCodigo());
        assertEquals(12000, v.getValorPagado(), 0.01);
        assertFalse(cinema.buscarFuncion("F1").buscarAsiento("A1").estaDisponible());
        assertNotNull(v.getFechaHoraVenta());
    }

    @Test
    void venderParaFuncionInexistenteLanzaExcepcion() {
        assertThrows(FuncionNoEncontradaException.class, () ->
                cinema.venderEntrada("NO", ana, TipoCliente.GENERAL, "A1"));
    }

    @Test
    void venderAsientoOcupadoLanzaExcepcion() throws CinemaException, FuncionNoEncontradaException {
        cinema.venderEntrada("F1", ana, TipoCliente.GENERAL, "A1");
        assertThrows(AsientoOcupadoException.class, () ->
                cinema.venderEntrada("F1", ana, TipoCliente.GENERAL, "A1"));
    }

    @Test
    void venderSinAsientosDisponiblesLanzaExcepcion() throws CinemaException, FuncionNoEncontradaException {
        for (String codigo : new String[]{"A1", "A2", "B1", "B2"}) {
            cinema.venderEntrada("F1", ana, TipoCliente.GENERAL, codigo);
        }
        assertThrows(SinAsientosDisponiblesException.class, () ->
                cinema.venderEntrada("F1", ana, TipoCliente.GENERAL, "A1"));
    }

    @Test
    void venderParaFuncionCanceladaLanzaExcepcion() throws CinemaException, FuncionNoEncontradaException {
        cinema.cancelarFuncion("F1");
        assertThrows(FuncionNoDisponibleException.class, () ->
                cinema.venderEntrada("F1", ana, TipoCliente.GENERAL, "A1"));
    }

    @Test
    void cancelarVentaLiberaAsiento() throws CinemaException, FuncionNoEncontradaException {
        Venta v = cinema.venderEntrada("F1", ana, TipoCliente.GENERAL, "A2");
        cinema.cancelarVenta(v.getCodigo());
        assertTrue(cinema.buscarFuncion("F1").buscarAsiento("A2").estaDisponible());
        assertEquals(0, cinema.getTotalEntradasVendidas());
    }

    @Test
    void cancelarVentaInexistenteLanzaExcepcion() {
        assertThrows(VentaNoEncontradaException.class, () -> cinema.cancelarVenta("V9999"));
    }

    // ---------- Tarifas ----------

    @Test
    void calculaValorSegunDescuento() throws CinemaException, FuncionNoEncontradaException {
        Funcion f1 = cinema.buscarFuncion("F1");
        assertEquals(15000, cinema.calcularValorEntrada(f1, TipoCliente.GENERAL), 0.01);
        assertEquals(10500, cinema.calcularValorEntrada(f1, TipoCliente.INFANTE), 0.01);
        cinema.establecerDescuento(TipoCliente.INFANTE, 50);
        assertEquals(7500, cinema.calcularValorEntrada(f1, TipoCliente.INFANTE), 0.01);
    }

    @Test
    void descuentoFueraDeRangoLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class, () -> cinema.establecerDescuento(TipoCliente.GENERAL, 120));
    }

    // ---------- Reportes ----------

    @Test
    void reportesDeVentas() throws CinemaException, FuncionNoEncontradaException {
        cinema.venderEntrada("F1", ana, TipoCliente.GENERAL, "A1");     // 15000
        cinema.venderEntrada("F2", ana, TipoCliente.GENERAL, "A1");     // 10000
        cinema.venderEntrada("F2", ana, TipoCliente.ESTUDIANTE, "A2");  // 8000
        Venta cancelada = cinema.venderEntrada("F1", ana, TipoCliente.GENERAL, "A2");
        cinema.cancelarVenta(cancelada.getCodigo());

        assertEquals(3, cinema.getTotalEntradasVendidas());
        assertEquals(33000, cinema.getTotalIngresos(), 0.01);
        assertEquals("P2", cinema.getPeliculaMasVendida().getCodigo());
        assertEquals(7, cinema.buscarFuncion("F2").contarDisponibles());
        assertEquals(2, cinema.buscarFuncion("F2").contarOcupados());
    }

    @Test
    void peliculaMasVendidaSinVentasEsNull() {
        assertNull(cinema.getPeliculaMasVendida());
    }
}
