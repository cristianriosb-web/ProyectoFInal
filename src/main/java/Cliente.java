public class Cliente extends Persona {
    private TipoCliente tipoCliente;

    public Cliente(String identificacion, String nombre, TipoCliente tipoCliente) throws DatoInvalidoException {
        super(identificacion, nombre);
        Validador.noNulo(tipoCliente, "tipo de cliente");
        this.tipoCliente = tipoCliente;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }
}