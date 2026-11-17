import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void creaCliente() throws DatoInvalidoException {
        Cliente c = new Cliente("123", "Ana", TipoCliente.ESTUDIANTE);
        assertEquals(TipoCliente.ESTUDIANTE, c.getTipoCliente());
    }

    @Test
    void datosVaciosLanzanExcepcion() {
        assertThrows(DatoInvalidoException.class, () -> new Cliente("", "Ana", TipoCliente.GENERAL));
        assertThrows(DatoInvalidoException.class, () -> new Cliente("1", "Ana", null));
    }
}
