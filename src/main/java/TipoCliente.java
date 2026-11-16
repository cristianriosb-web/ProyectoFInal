public enum TipoCliente {
    GENERAL(17000.0),
    ESTUDIANTE(14000.0),
    INFANTE(12000.0),
    ADULTO_MAYOR(12000.0);

    private final double tarifaCliente;

    TipoCliente(double tarifaCliente) {
        this.tarifaCliente = tarifaCliente;
    }

    public double getTarifaCliente() {
        return tarifaCliente;
    }

}
