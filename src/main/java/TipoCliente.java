public enum TipoCliente {
    GENERAL(17000.0),
    ESTUDIANTE(14000.0),
    INFANTE(12000.0),
    ADULTO_MAYOR(12000.0);

    private final double descuentoPorDefecto;

    TipoCliente(double descuentoPorDefecto) {
        this.descuentoPorDefecto = descuentoPorDefecto;
    }

    /** Porcentaje de descuento por defecto (0 a 100). */
    public double getDescuentoPorDefecto() {
        return descuentoPorDefecto;
    }

}
