public enum TipoCliente {
    GENERAL(17000.0),
    ESTUDIANTE(14000.0),
    INFANTE(12000.0),
    ADULTO_MAYOR(12000.0);

   private double tarifaFija;
   TipoCliente(double tarifaFija){
       this.tarifaFija = tarifaFija;
    }
}
