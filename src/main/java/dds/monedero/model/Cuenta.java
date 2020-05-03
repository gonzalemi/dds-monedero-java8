package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class Cuenta {
    private static final int cantidadMaximaDepositosDiarios = 3;
    private static final int maximoExtraccionDiario = 1000;

    private List<Movimiento> movimientos = new ArrayList<>();

    private void validarMontoNegativo(double monto) {
        if (monto <= 0) {
            throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
        }
    }

    private void validarCantidadDeDepositosDiarios() {
        if (getMovimientos().stream().filter(Movimiento::esDeposito).count() >= cantidadMaximaDepositosDiarios) {
            throw new MaximaCantidadDepositosException("Ya excedio los " + cantidadMaximaDepositosDiarios + " depositos diarios");
        }
    }

    private void validarSuficienciaDeSaldo(double monto) {
        double saldo = getSaldo();

        if (saldo - monto < 0) {
            throw new SaldoMenorException("No puede sacar mas de " + saldo + " $");
        }
    }

    private void validarMontoMaximoDeExtraccionDiario(double monto) {
        double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
        double limite = maximoExtraccionDiario - montoExtraidoHoy;

        if (monto > limite) {
            throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + maximoExtraccionDiario
                    + " diarios, límite: " + limite);
        }
    }

    public void poner(double cuanto) {
        validarMontoNegativo(cuanto);
        validarCantidadDeDepositosDiarios();

        agregarMovimiento(LocalDate.now(), cuanto, TipoDeMovimiento.DEPOSITO );
    }

    public void sacar(double cuanto) {
        validarMontoNegativo(cuanto);
        validarSuficienciaDeSaldo(cuanto);
        validarMontoMaximoDeExtraccionDiario(cuanto);

        agregarMovimiento(LocalDate.now(), cuanto, TipoDeMovimiento.EXTRACCION );
    }

    public void agregarMovimiento(LocalDate fecha, double cuanto, TipoDeMovimiento tipo) {
        Movimiento movimiento = new Movimiento(fecha, cuanto, tipo);
        movimientos.add(movimiento);
    }

    public double getMontoExtraidoA(LocalDate fecha) {
        return movimientos.stream()
                .filter(movimiento -> movimiento.esExtraccionEnFecha(fecha))
                .mapToDouble(Movimiento::getMonto)
                .sum();
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public double getSaldo() {
        return movimientos
                .stream()
                .mapToDouble(Movimiento::getMonto)
                .sum();
    }
}
