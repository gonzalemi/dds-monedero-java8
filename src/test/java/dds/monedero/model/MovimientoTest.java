package dds.monedero.model;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import static org.junit.Assert.*;

public class MovimientoTest {
    private Movimiento movimiento;
    private final LocalDate fecha = LocalDate.of(2020, 1, 1);

    @Before
    public void init() {
        movimiento = new Movimiento(fecha, 500, TipoDeMovimiento.EXTRACCION);
    }

    @Test
    public void MovimientoEsDelTipoYFechaCuandoNoCoincideElTipo() {
        assertFalse(movimiento.esDeTipoYFecha(TipoDeMovimiento.DEPOSITO, fecha));
    }

    @Test
    public void MovimientoEsDelTipoYFechaCuandoNoCoincideLaFecha() {
        assertFalse(movimiento.esDeTipoYFecha(TipoDeMovimiento.EXTRACCION, LocalDate.of(2020, 1, 2)));
    }

    @Test
    public void MovimientoEsDelTipoYFechaCuandoLaFechaTipoCorrectos() {
        assertTrue(movimiento.esDeTipoYFecha(TipoDeMovimiento.EXTRACCION, fecha));
    }

}