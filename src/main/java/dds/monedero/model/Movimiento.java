package dds.monedero.model;

import java.time.LocalDate;

public class Movimiento {
    private LocalDate fecha;
    //En ningún lenguaje de programación usen jamás doubles para modelar dinero en el mundo real
    //siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares
    private double monto;
    private TipoDeMovimiento tipo;

    public Movimiento(LocalDate fecha, double monto, TipoDeMovimiento tipo) {
        this.fecha = fecha;
        this.monto = monto;
        this.tipo = tipo;
    }

    public double getMonto() {
        return monto;
    }

    public boolean esDeLaFecha(LocalDate fecha) {
        return this.fecha.equals(fecha);
    }

    public boolean esDelTipo(TipoDeMovimiento tipo) {
        return this.tipo == tipo;
    }

    public boolean esDeTipoYFecha(TipoDeMovimiento tipo, LocalDate fecha){
        return esDelTipo(tipo) && esDeLaFecha(fecha);
    }
}
