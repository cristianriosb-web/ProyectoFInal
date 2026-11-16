public final class Validador {

    private Validador() {
    }

    public static void texto(String valor, String campo) throws DatoInvalidoException {
        if (valor == null || valor.isBlank()) {
            throw new DatoInvalidoException("El campo " + campo + " es obligatorio");
        }
    }

    public static void positivo(double valor, String campo) throws DatoInvalidoException {
        if (valor <= 0) {
            throw new DatoInvalidoException("El campo " + campo + " debe ser mayor que cero");
        }
    }

    public static void noNulo(Object valor, String campo) throws DatoInvalidoException {
        if (valor == null) {
            throw new DatoInvalidoException("Debe seleccionar " + campo);
        }
    }
}
