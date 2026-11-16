import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Cinema implements Reportable {

    private final String nombre;
    private final List<Pelicula> peliculas = new ArrayList<>();
    private final List<Sala> salas = new ArrayList<>();
    private final List<Funcion> funciones = new ArrayList<>();
    private final List<Venta> ventas = new ArrayList<>();
    private final Map<TipoCliente, Double> descuentos = new EnumMap<>(TipoCliente.class);
    private int consecutivoVentas = 0;

    public Cinema(String nombre) {
        this.nombre = nombre;
        for (TipoCliente tipo : TipoCliente.values()) {
            descuentos.put(tipo, tipo.getTarifaCliente());
        }
    }

    private static <T extends Identificable> T buscarPorCodigo(List<T> lista, String codigo) {
        for (T elemento : lista) {
            if (elemento.getCodigo().equalsIgnoreCase(codigo)) {
                return elemento;
            }
        }
        return null;
    }

    private static <T extends Habilitable> List<T> filtrarDisponibles(List<T> lista) {
        List<T> resultado = new ArrayList<>();
        for (T elemento : lista) {
            if (elemento.estaDisponible()) {
                resultado.add(elemento);
            }
        }
        return resultado;
    }

    public void registrarPelicula(Pelicula pelicula) throws CodigoDuplicadoException, DatoInvalidoException {
        Validador.noNulo(pelicula, "una película");
        if (existePelicula(pelicula.getCodigo())) {
            throw new CodigoDuplicadoException("Ya existe una película con el código " + pelicula.getCodigo());
        }
        peliculas.add(pelicula);
    }

    public boolean existePelicula(String codigo) {
        return buscarPorCodigo(peliculas, codigo) != null;
    }

    public Pelicula buscarPelicula(String codigo) throws PeliculaNoEncontradaException {
        Pelicula encontrado = buscarPorCodigo(peliculas, codigo);
        if (encontrado != null) {
            return encontrado;
        }
        throw new PeliculaNoEncontradaException("No existe una película con el código " + codigo);
    }

    public void modificarPelicula(String codigo, String nombre, String genero, int duracion,
                                  ClasificacionPelicula clasificacion) throws CinemaException {
        buscarPelicula(codigo).actualizar(nombre, genero, duracion, clasificacion);
    }

    public void deshabilitarPelicula(String codigo) throws PeliculaNoEncontradaException {
        buscarPelicula(codigo).deshabilitar();
    }

    public void habilitarPelicula(String codigo) throws PeliculaNoEncontradaException {
        buscarPelicula(codigo).habilitar();
    }

    public List<Pelicula> getPeliculas() {
        return Collections.unmodifiableList(peliculas);
    }

    public List<Pelicula> getPeliculasDisponibles() {
        return filtrarDisponibles(peliculas);
    }

    public List<Sala> getSalasDisponibles() {
        return filtrarDisponibles(salas);
    }

    public void registrarSala(Sala sala) throws CodigoDuplicadoException, DatoInvalidoException {
        Validador.noNulo(sala, "una sala");
        if (existeSala(sala.getCodigo())) {
            throw new CodigoDuplicadoException("Ya existe una sala con el código " + sala.getCodigo());
        }
        salas.add(sala);
    }

    public boolean existeSala(String codigo) {
        return buscarPorCodigo(salas, codigo) != null;
    }

    public Sala buscarSala(String codigo) throws SalaNoEncontradaException {
        Sala encontrado = buscarPorCodigo(salas, codigo);
        if (encontrado != null) {
            return encontrado;
        }
        throw new SalaNoEncontradaException("No existe una sala con el código " + codigo);
    }

    public void modificarSala(String codigo, String nombre, int filas, int asientosPorFila) throws CinemaException {
        buscarSala(codigo).actualizar(nombre, filas, asientosPorFila);
    }

    public void deshabilitarSala(String codigo) throws SalaNoEncontradaException {
        buscarSala(codigo).deshabilitar();
    }

    public void habilitarSala(String codigo) throws SalaNoEncontradaException {
        buscarSala(codigo).habilitar();
    }

    public List<Sala> getSalas() {
        return Collections.unmodifiableList(salas);
    }

    public Funcion programarFuncion(String codigo, String codigoPelicula, String codigoSala,
                                    LocalDate fecha, LocalTime hora, double valorBase) throws CinemaException, SalaNoDisponibleException {
        if (existeFuncion(codigo)) {
            throw new CodigoDuplicadoException("Ya existe una función con el código " + codigo);
        }
        Pelicula pelicula = buscarPelicula(codigoPelicula);
        Sala sala = buscarSala(codigoSala);
        if (!pelicula.estaDisponible()) {
            throw new PeliculaNoDisponibleException("La película " + pelicula.getNombre() + " no está disponible");
        }
        if (!sala.estaDisponible()) {
            throw new SalaNoDisponibleException("La sala " + sala.getNombre() + " está fuera de servicio");
        }
        Funcion nueva = new Funcion(codigo, pelicula, sala, fecha, hora, valorBase);
        validarCruceHorario(nueva);
        funciones.add(nueva);
        return nueva;
    }

    private void validarCruceHorario(Funcion nueva) throws SalaNoDisponibleException {
        LocalDateTime inicioNueva = LocalDateTime.of(nueva.getFecha(), nueva.getHora());
        LocalDateTime finNueva = inicioNueva.plusMinutes(nueva.getPelicula().getDuracionMinutos());
        for (Funcion f : funciones) {
            if (f != nueva && f.estaProgramada() && f.getSala() == nueva.getSala()) {
                LocalDateTime inicio = LocalDateTime.of(f.getFecha(), f.getHora());
                LocalDateTime fin = inicio.plusMinutes(f.getPelicula().getDuracionMinutos());
                if (inicioNueva.isBefore(fin) && inicio.isBefore(finNueva)) {
                    throw new SalaNoDisponibleException("La sala " + nueva.getSala().getNombre()
                            + " ya tiene la función " + f.getCodigo() + " en ese horario");
                }
            }
        }
    }

    public boolean existeFuncion(String codigo) {
        return buscarPorCodigo(funciones, codigo) != null;
    }

    public Funcion buscarFuncion(String codigo) throws FuncionNoEncontradaException {
        Funcion encontrado = buscarPorCodigo(funciones, codigo);
        if (encontrado != null) {
            return encontrado;
        }
        throw new FuncionNoEncontradaException("No existe una función con el código " + codigo);
    }

    public void modificarFuncion(String codigo, LocalDate fecha, LocalTime hora, double valorBase)
            throws CinemaException, FuncionNoEncontradaException, SalaNoDisponibleException {
        Funcion f = buscarFuncion(codigo);
        LocalDate fechaAnterior = f.getFecha();
        LocalTime horaAnterior = f.getHora();
        double valorAnterior = f.getValorBase();
        f.actualizar(fecha, hora, valorBase);
        try {
            validarCruceHorario(f);
        } catch (SalaNoDisponibleException e) {
            f.actualizar(fechaAnterior, horaAnterior, valorAnterior); // deshace el cambio
            throw e;
        }
    }

    public void cancelarFuncion(String codigo) throws FuncionNoEncontradaException {
        buscarFuncion(codigo).cancelar();
    }

    public void finalizarFuncion(String codigo) throws FuncionNoEncontradaException {
        buscarFuncion(codigo).finalizar();
    }

    public List<Funcion> getFunciones() {
        return Collections.unmodifiableList(funciones);
    }

    @Override
    public List<Funcion> getFuncionesProgramadasPorFecha(LocalDate fecha) {
        List<Funcion> resultado = new ArrayList<>();
        for (Funcion f : funciones) {
            if (f.estaProgramada() && f.getFecha().equals(fecha)) {
                resultado.add(f);
            }
        }
        Collections.sort(resultado); // usa Funcion.compareTo (Comparable)
        return resultado;
    }

    public List<Funcion> getFuncionesProgramadas() {
        List<Funcion> resultado = new ArrayList<>();
        for (Funcion f : funciones) {
            if (f.estaProgramada()) {
                resultado.add(f);
            }
        }
        return resultado;
    }

    public double getDescuento(TipoCliente tipo) {
        return descuentos.get(tipo);
    }

    public void establecerDescuento(TipoCliente tipo, double porcentaje) throws DatoInvalidoException {
        Validador.noNulo(tipo, "un tipo de entrada");
        if (porcentaje < 0 || porcentaje > 100) {
            throw new DatoInvalidoException("El descuento debe estar entre 0 y 100");
        }
        descuentos.put(tipo, porcentaje);
    }

    public double calcularValorEntrada(Funcion funcion, TipoCliente tipo) {
        return Math.round(funcion.getValorBase() * (1 - getDescuento(tipo) / 100.0));
    }

    public Venta venderEntrada(String codigoFuncion, Cliente cliente, TipoCliente tipoEntrada,
                               String codigoAsiento) throws CinemaException, FuncionNoEncontradaException, SinAsientosdisponiblesException {
        Validador.noNulo(cliente, "un cliente");
        Validador.noNulo(tipoEntrada, "un tipo de entrada");
        Funcion funcion = buscarFuncion(codigoFuncion);
        if (!funcion.estaProgramada()) {
            throw new FuncionNoDisponibleException("La función " + codigoFuncion + " está " + funcion.getEstado());
        }
        if (!funcion.hayAsientosDisponibles()) {
            throw new SinAsientosdisponiblesException("La función " + codigoFuncion + " no tiene asientos disponibles");
        }
        Asiento asiento = funcion.buscarAsiento(codigoAsiento);
        asiento.ocupar(); // lanza AsientoOcupadoException si ya está ocupado

        double valor = calcularValorEntrada(funcion, tipoEntrada);
        consecutivoVentas++;
        String codigoVenta = String.format("V%04d", consecutivoVentas);
        Venta venta = new Venta(codigoVenta, cliente, funcion, asiento, tipoEntrada, LocalDateTime.now(), valor);
        ventas.add(venta);
        return venta;
    }

    public Venta buscarVenta(String codigo) throws VentaNoEncontradaException {
        Venta encontrado = buscarPorCodigo(ventas, codigo);
        if (encontrado != null) {
            return encontrado;
        }
        throw new VentaNoEncontradaException("No existe una venta con el código " + codigo);
    }

    public void cancelarVenta(String codigo) throws VentaNoEncontradaException, VentaYaCanceladaException {
        buscarVenta(codigo).cancelar();
    }

    public List<Venta> getVentas() {
        return Collections.unmodifiableList(ventas);
    }

    @Override
    public int getTotalEntradasVendidas() {
        int total = 0;
        for (Venta v : ventas) {
            if (v.estaActiva()) {
                total++;
            }
        }
        return total;
    }

    @Override
    public double getTotalIngresos() {
        double total = 0;
        for (Venta v : ventas) {
            if (v.estaActiva()) {
                total += v.getValorPagado();
            }
        }
        return total;
    }

    public Map<Pelicula, Integer> getEntradasPorPelicula() {
        Map<Pelicula, Integer> conteo = new LinkedHashMap<>();
        for (Venta v : ventas) {
            if (v.estaActiva()) {
                Pelicula p = v.getFuncion().getPelicula();
                conteo.put(p, conteo.getOrDefault(p, 0) + 1);
            }
        }
        return conteo;
    }

    @Override
    public Pelicula getPeliculaMasVendida() {
        Pelicula mayor = null;
        int maximo = 0;
        for (Map.Entry<Pelicula, Integer> e : getEntradasPorPelicula().entrySet()) {
            if (e.getValue() > maximo) {
                maximo = e.getValue();
                mayor = e.getKey();
            }
        }
        return mayor;
    }

    public String getNombre() {
        return nombre;
    }
}
