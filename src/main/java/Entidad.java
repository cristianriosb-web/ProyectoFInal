import java.util.Objects;

public abstract class Entidad implements Identificable{
    protected String codigo;
    public Entidad(String codigo) throws DatoInvalidoException {
        Validador.texto(codigo, "código");
        this.codigo = codigo.trim();
    }
    @Override
    public String getCodigo() {
        return codigo;
    }
    @Override
    public boolean equals(Object o) {
       if(this == o) return true;
       if(o == null || getClass() != o.getClass()) return false;
       Entidad entidad = (Entidad) o;
       return Objects.equals(codigo,entidad.codigo);
    }
    @Override
    public int hashCode() {
        return codigo.hashCode();
    }
}
