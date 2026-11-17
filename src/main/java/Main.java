import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final Cinema cinema = new Cinema("CINEMAUQ");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("H:mm");

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n========== CINEMAUQ ==========");
            System.out.println("1.  Venta de entrada");
            System.out.println("2.  Cancelar venta");
            System.out.println("3.  Consultar películas");
            System.out.println("4.  Consultar funciones");
            System.out.println("5.  Consultar disponibilidad");
            System.out.println("6.  Administrar películas");
            System.out.println("7.  Administrar funciones y tarifas");
            System.out.println("8.  Administrar salas");
            System.out.println("9.  Generar reportes");
            System.out.println("10. Salir");
            opcion = leerEntero("Seleccione una opción: ");

            try {
                switch (opcion) {
                    case 1:
                        venderEntrada();
                        break;
                    case 2:
                        cancelarVenta();
                        break;
                    case 3:
                        mostrarPeliculas(cinema.getPeliculasDisponibles());
                        break;
                    case 4:
                        mostrarFunciones(cinema.getFuncionesProgramadas());
                        break;
                    case 5:
                        consultarDisponibilidad();
                        break;
                    case 6:
                        menuPeliculas();
                        break;
                    case 7:
                        menuFunciones();
                        break;
                    case 8:
                        menuSalas();
                        break;
                    case 9:
                        menuReportes();
                        break;
                    case 10:
                        System.out.println("¡Gracias por usar CINEMAUQ!");
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (CinemaException | FuncionNoEncontradaException | SinAsientosDisponiblesException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        } while (opcion != 10);
    }

    private static void venderEntrada() throws CinemaException, FuncionNoEncontradaException, SinAsientosDisponiblesException {
        List<Funcion> funciones = cinema.getFuncionesProgramadas();
        if (funciones.isEmpty()) {
            System.out.println("No hay funciones programadas.");
            return;
        }
        mostrarFunciones(funciones);
        Funcion funcion = cinema.buscarFuncion(leerTexto("Código de la función: "));
        mostrarAsientos(funcion);

        String codigoAsiento = leerTexto("Código del asiento (ej. A1): ");
        String identificacion = leerTexto("Identificación del cliente: ");
        String nombre = leerTexto("Nombre del cliente: ");
        TipoCliente tipoCliente = elegirTipoCliente("Tipo de cliente");
        TipoCliente tipoEntrada = elegirTipoCliente("Tipo de entrada");

        Cliente cliente = new Cliente(identificacion, nombre, tipoCliente);
        Venta venta = cinema.venderEntrada(funcion.getCodigo(), cliente, tipoEntrada, codigoAsiento);
        System.out.println("Venta registrada");
        System.out.println(venta.generarTiquete());
    }

    private static void cancelarVenta() throws CinemaException {
        mostrarVentas(cinema.getVentas());
        String codigo = leerTexto("Código de la venta a cancelar: ");
        cinema.cancelarVenta(codigo);
        System.out.println("Venta " + codigo + " cancelada. El asiento quedó disponible.");
    }

    private static void consultarDisponibilidad() throws CinemaException, FuncionNoEncontradaException {
        mostrarFunciones(cinema.getFunciones());
        Funcion f = cinema.buscarFuncion(leerTexto("Código de la función: "));
        System.out.println("Función: " + f.getPelicula().getNombre());
        System.out.println("Sala: " + f.getSala().getNombre());
        System.out.println("Hora: " + f.getHora().format(FORMATO_HORA));
        mostrarAsientos(f);
        for (Asiento a : f.getAsientos()) {
            System.out.println(a);
        }
    }

    private static void menuPeliculas() {
        int opcion;
        do {
            System.out.println("\n--- ADMINISTRAR PELÍCULAS ---");
            System.out.println("1. Registrar película");
            System.out.println("2. Consultar películas");
            System.out.println("3. Modificar película");
            System.out.println("4. Deshabilitar película");
            System.out.println("5. Habilitar película");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione una opción: ");

            try {
                switch (opcion) {
                    case 1:
                        String codigo = leerTexto("Código: ");
                        String nombre = leerTexto("Nombre: ");
                        String genero = leerTexto("Género: ");
                        int duracion = leerEntero("Duración en minutos: ");
                        ClasificacionPelicula clasificacion = elegirClasificacion();
                        cinema.registrarPelicula(new Pelicula(codigo, nombre, genero, duracion, clasificacion));
                        System.out.println("Película registrada");
                        break;
                    case 2:
                        mostrarPeliculas(cinema.getPeliculas());
                        break;
                    case 3:
                        Pelicula p = cinema.buscarPelicula(leerTexto("Código de la película: "));
                        String nuevoNombre = leerTexto("Nuevo nombre: ");
                        String nuevoGenero = leerTexto("Nuevo género: ");
                        int nuevaDuracion = leerEntero("Nueva duración en minutos: ");
                        ClasificacionPelicula nuevaClasificacion = elegirClasificacion();
                        cinema.modificarPelicula(p.getCodigo(), nuevoNombre, nuevoGenero, nuevaDuracion, nuevaClasificacion);
                        System.out.println("Película modificada");
                        break;
                    case 4:
                        cinema.deshabilitarPelicula(leerTexto("Código de la película: "));
                        System.out.println("Película deshabilitada");
                        break;
                    case 5:
                        cinema.habilitarPelicula(leerTexto("Código de la película: "));
                        System.out.println("Película habilitada");
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (CinemaException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void menuFunciones() {
        int opcion;
        do {
            System.out.println("\n--- ADMINISTRAR FUNCIONES Y TARIFAS ---");
            System.out.println("1. Programar función");
            System.out.println("2. Consultar todas las funciones");
            System.out.println("3. Modificar función");
            System.out.println("4. Cancelar función");
            System.out.println("5. Finalizar función");
            System.out.println("6. Consultar tarifas");
            System.out.println("7. Modificar tarifa");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione una opción: ");

            try {
                switch (opcion) {
                    case 1:
                        String codigo = leerTexto("Código de la función: ");
                        mostrarPeliculas(cinema.getPeliculasDisponibles());
                        String codigoPelicula = leerTexto("Código de la película: ");
                        mostrarSalas(cinema.getSalas());
                        String codigoSala = leerTexto("Código de la sala: ");
                        LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD): ");
                        LocalTime hora = leerHora("Hora (HH:mm): ");
                        double valor = leerDecimal("Valor base de la entrada: ");
                        cinema.programarFuncion(codigo, codigoPelicula, codigoSala, fecha, hora, valor);
                        System.out.println("Función programada");
                        break;
                    case 2:
                        mostrarFunciones(cinema.getFunciones());
                        break;
                    case 3:
                        String codigoFuncion = leerTexto("Código de la función: ");
                        LocalDate nuevaFecha = leerFecha("Nueva fecha (AAAA-MM-DD): ");
                        LocalTime nuevaHora = leerHora("Nueva hora (HH:mm): ");
                        double nuevoValor = leerDecimal("Nuevo valor base: ");
                        cinema.modificarFuncion(codigoFuncion, nuevaFecha, nuevaHora, nuevoValor);
                        System.out.println("Función modificada");
                        break;
                    case 4:
                        cinema.cancelarFuncion(leerTexto("Código de la función: "));
                        System.out.println("Función cancelada");
                        break;
                    case 5:
                        cinema.finalizarFuncion(leerTexto("Código de la función: "));
                        System.out.println("Función finalizada");
                        break;
                    case 6:
                        mostrarTarifas();
                        break;
                    case 7:
                        TipoCliente tipo = elegirTipoCliente("Tipo de entrada");
                        double descuento = leerDecimal("Porcentaje de descuento (0 a 100): ");
                        cinema.establecerDescuento(tipo, descuento);
                        System.out.println("Tarifa actualizada");
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (CinemaException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (SalaNoDisponibleException e) {
                throw new RuntimeException(e);
            } catch (FuncionNoEncontradaException e) {
                throw new RuntimeException(e);
            }
        } while (opcion != 0);
    }

    private static void menuSalas() {
        int opcion;
        do {
            System.out.println("\n--- ADMINISTRAR SALAS ---");
            System.out.println("1. Registrar sala");
            System.out.println("2. Consultar salas");
            System.out.println("3. Modificar sala");
            System.out.println("4. Poner sala fuera de servicio");
            System.out.println("5. Habilitar sala");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione una opción: ");

            try {
                switch (opcion) {
                    case 1:
                        String codigo = leerTexto("Código: ");
                        String nombre = leerTexto("Nombre o número: ");
                        int filas = leerEntero("Número de filas (máx. 26): ");
                        int porFila = leerEntero("Asientos por fila: ");
                        cinema.registrarSala(new Sala(codigo, nombre, filas, porFila));
                        System.out.println("Sala registrada con " + (filas * porFila) + " asientos");
                        break;
                    case 2:
                        mostrarSalas(cinema.getSalas());
                        break;
                    case 3:
                        String codigoSala = leerTexto("Código de la sala: ");
                        String nuevoNombre = leerTexto("Nuevo nombre: ");
                        int nuevasFilas = leerEntero("Nuevo número de filas: ");
                        int nuevosPorFila = leerEntero("Nuevos asientos por fila: ");
                        cinema.modificarSala(codigoSala, nuevoNombre, nuevasFilas, nuevosPorFila);
                        System.out.println("Sala modificada");
                        break;
                    case 4:
                        cinema.deshabilitarSala(leerTexto("Código de la sala: "));
                        System.out.println("Sala fuera de servicio");
                        break;
                    case 5:
                        cinema.habilitarSala(leerTexto("Código de la sala: "));
                        System.out.println("Sala habilitada");
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (CinemaException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void menuReportes() {
        int opcion;
        do {
            System.out.println("\n--- REPORTES ---");
            System.out.println("1. Total de entradas vendidas");
            System.out.println("2. Total de ingresos");
            System.out.println("3. Película con más entradas vendidas");
            System.out.println("4. Ocupación de una función");
            System.out.println("5. Funciones programadas en una fecha");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione una opción: ");

            try {
                switch (opcion) {
                    case 1:
                        System.out.println("Total de entradas vendidas: " + cinema.getTotalEntradasVendidas());
                        break;
                    case 2:
                        System.out.println("Total de ingresos: $" + cinema.getTotalIngresos());
                        break;
                    case 3:
                        Pelicula mas = cinema.getPeliculaMasVendida();
                        if (mas == null) {
                            System.out.println("Aún no hay ventas.");
                        } else {
                            System.out.println("Película más vendida: " + mas.getNombre());
                            for (Map.Entry<Pelicula, Integer> e : cinema.getEntradasPorPelicula().entrySet()) {
                                System.out.println("  " + e.getKey().getNombre() + ": " + e.getValue());
                            }
                        }
                        break;
                    case 4:
                        Funcion f = cinema.buscarFuncion(leerTexto("Código de la función: "));
                        System.out.println("Asientos disponibles: " + f.contarDisponibles());
                        System.out.println("Asientos ocupados: " + f.contarOcupados());
                        break;
                    case 5:
                        LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD): ");
                        List<Funcion> lista = cinema.getFuncionesProgramadasPorFecha(fecha);
                        if (lista.isEmpty()) {
                            System.out.println("No hay funciones programadas para esa fecha.");
                        } else {
                            mostrarFunciones(lista);
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (FuncionNoEncontradaException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void mostrarPeliculas(List<Pelicula> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay películas.");
            return;
        }
        for (Pelicula p : lista) {
            System.out.println(p.getCodigo() + " | " + p.getNombre() + " | " + p.getGenero() + " | "
                    + p.getDuracionMinutos() + " min | " + p.getClasificacion() + " | " + p.getEstado());
        }
    }

    private static void mostrarSalas(List<Sala> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay salas.");
            return;
        }
        for (Sala s : lista) {
            System.out.println(s.getCodigo() + " | " + s.getNombre() + " | " + s.getFilas() + " filas x "
                    + s.getAsientosPorFila() + " = " + s.getCapacidad() + " asientos | " + s.getEstado());
        }
    }

    private static void mostrarFunciones(List<Funcion> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay funciones.");
            return;
        }
        for (Funcion f : lista) {
            System.out.println(f.getCodigo() + " | " + f.getPelicula().getNombre() + " | " + f.getSala().getNombre()
                    + " | " + f.getFecha() + " " + f.getHora().format(FORMATO_HORA) + " | $" + f.getPrecioBase()
                    + " | Disponibles: " + f.contarDisponibles() + "/" + f.getTotalAsientos() + " | " + f.getEstado());
        }
    }

    private static void mostrarVentas(List<Venta> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay ventas.");
            return;
        }
        for (Venta v : lista) {
            System.out.println(v.getCodigo() + " | " + v.getCliente().getNombre() + " | "
                    + v.getFuncion().getPelicula().getNombre() + " | Asiento " + v.getAsiento().getCodigo()
                    + " | $" + v.getValorPagado() + " | " + v.getEstado());
        }
    }

    private static void mostrarAsientos(Funcion f) {
        System.out.println("---------- PANTALLA ----------");
        char filaActual = ' ';
        for (Asiento a : f.getAsientos()) {
            if (a.getFila() != filaActual) {
                System.out.println();
                filaActual = a.getFila();
            }
            System.out.print(a.estaDisponible() ? "[" + a.getCodigo() + "] " : "[X] ");
        }
        System.out.println("\n[X] = ocupado");
        System.out.println("Total: " + f.getTotalAsientos() + "  Disponibles: " + f.contarDisponibles()
                + "  Ocupados: " + f.contarOcupados());
    }

    private static void mostrarTarifas() {
        for (TipoCliente t : TipoCliente.values()) {
            System.out.println(t + ": " + cinema.getDescuento(t) + "% de descuento");
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número entero");
            }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje));
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Ejemplo: 2026-11-20");
            }
        }
    }

    private static LocalTime leerHora(String mensaje) {
        while (true) {
            try {
                return LocalTime.parse(leerTexto(mensaje), FORMATO_HORA);
            } catch (DateTimeParseException e) {
                System.out.println("Hora inválida. Ejemplo: 18:30");
            }
        }
    }

    private static TipoCliente elegirTipoCliente(String titulo) {
        System.out.println(titulo + ":");
        System.out.println("1. GENERAL");
        System.out.println("2. ESTUDIANTE");
        System.out.println("3. NINO");
        System.out.println("4. ADULTO_MAYOR");
        while (true) {
            int opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1:
                    return TipoCliente.GENERAL;
                case 2:
                    return TipoCliente.ESTUDIANTE;
                case 3:
                    return TipoCliente.INFANTE;
                case 4:
                    return TipoCliente.ADULTO_MAYOR;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    private static ClasificacionPelicula elegirClasificacion() {
        System.out.println("Clasificación:");
        System.out.println("1. TODOS");
        System.out.println("2. MAYORES_7");
        System.out.println("3. MAYORES_12");
        System.out.println("4. MAYORES_15");
        System.out.println("5. MAYORES_18");
        while (true) {
            int opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1:
                    return ClasificacionPelicula.TODOS;
                case 2:
                    return ClasificacionPelicula.MAYORES_7;
                case 3:
                    return ClasificacionPelicula.MAYORES_12;
                case 4:
                    return ClasificacionPelicula.MAYORES_15;
                case 5:
                    return ClasificacionPelicula.MAYORES_18;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }
}
