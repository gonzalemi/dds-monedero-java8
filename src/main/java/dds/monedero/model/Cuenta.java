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

    public void poner(double cuanto) {
        if (cuanto <= 0) {
            throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
        }

        if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= cantidadMaximaDepositosDiarios) {
            throw new MaximaCantidadDepositosException("Ya excedio los " + cantidadMaximaDepositosDiarios + " depositos diarios");
        }

        new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
    }

    public void sacar(double cuanto) {
        if (cuanto <= 0) {
            throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
        }
        if (getSaldo() - cuanto < 0) {
            throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
        }
        double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
        double limite = maximoExtraccionDiario - montoExtraidoHoy;
        if (cuanto > limite) {
            throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + maximoExtraccionDiario
                    + " diarios, límite: " + limite);
        }
        new Movimiento(LocalDate.now(), -cuanto, false).agregateA(this);
    }

    public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
        Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
        movimientos.add(movimiento);
    }

    public double getMontoExtraidoA(LocalDate fecha) {
        return movimientos.stream()
                .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
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
