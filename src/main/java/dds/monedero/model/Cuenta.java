package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dds.monedero.exceptions.MaximaCantidadDepositosDiariosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class Cuenta {
    private static final int cantidadMaximaDepositosDiarios = 3;
    private static final int maximoExtraccionDiario = 1000;

    private final List<Movimiento> movimientos = new ArrayList<>();

    private void validarMontoNegativo(double monto) {
        if (monto <= 0) {
            throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
        }
    }

    private void validarCantidadDeDepositosDiarios() {

        if (cantidadDeDepositosA(LocalDate.now()) >= cantidadMaximaDepositosDiarios) {
            throw new MaximaCantidadDepositosDiariosException("Ya excedio los " + cantidadMaximaDepositosDiarios + " depositos diarios");
        }
    }

    private void validarSuficienciaDeSaldo(double monto) {
        double saldo = getSaldo();

        if (saldo - monto < 0) {
            throw new SaldoMenorException("No puede sacar mas de " + saldo + " $");
        }
    }

    private void validarMontoMaximoDeExtraccionDiario(double monto) {
        double limite = maximoExtraccionDiario - getMontoExtraidoA(LocalDate.now());

        if (monto > limite) {
            throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + maximoExtraccionDiario
                    + " diarios, límite: " + limite);
        }
    }

    public void poner(double montoDeposito) {
        validarMontoNegativo(montoDeposito);
        validarCantidadDeDepositosDiarios();

        Movimiento movimiento = new Movimiento(LocalDate.now(), montoDeposito, TipoDeMovimiento.DEPOSITO);
        agregarMovimiento(movimiento);
    }

    public void sacar(double montoExtraccion) {
        validarMontoNegativo(montoExtraccion);
        validarSuficienciaDeSaldo(montoExtraccion);
        validarMontoMaximoDeExtraccionDiario(montoExtraccion);

        Movimiento movimiento = new Movimiento(LocalDate.now(), -montoExtraccion, TipoDeMovimiento.EXTRACCION);
        agregarMovimiento(movimiento);
    }

    public void agregarMovimiento(Movimiento movimiento) {
        movimientos.add(movimiento);
    }

    private List<Movimiento> getMovimientosSegun(LocalDate fecha, TipoDeMovimiento tipo) {
        return movimientos.stream()
                .filter(movimiento -> movimiento.esDeTipoYFecha(tipo, fecha))
                .collect(Collectors.toList());
    }

    private double getMontoExtraidoA(LocalDate fecha) {
        return getMovimientosSegun(fecha, TipoDeMovimiento.EXTRACCION)
                .stream()
                .mapToDouble(Movimiento::getMonto)
                .sum();
    }

    private int cantidadDeDepositosA(LocalDate fecha) {
        return getMovimientosSegun(fecha, TipoDeMovimiento.DEPOSITO)
                .size();
    }

    public double getSaldo() {
        return movimientos
                .stream()
                .mapToDouble(Movimiento::getMonto)
                .sum();
    }
}
