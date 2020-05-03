package dds.monedero.model;
import org.junit.Before;
import org.junit.Test;

import dds.monedero.exceptions.MaximaCantidadDepositosDiariosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import static org.junit.Assert.assertEquals;

public class MonederoTest {
  private Cuenta cuenta;

  @Before
  public void init() {
    cuenta = new Cuenta();
  }

  @Test
  public void Poner() {
    cuenta.poner(1500);
    assertEquals(1500, cuenta.getSaldo(),0);
  }


  @Test(expected = MontoNegativoException.class)
  public void PonerMontoNegativo() {
    cuenta.poner(-1500);
  }

  @Test
  public void RealizarUnaCantidadDepositosSinExcederElMaximo() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(3856, cuenta.getSaldo(),0);
  }

  @Test(expected = MaximaCantidadDepositosDiariosException.class)
  public void RealizarUnaCantidadDeDepositosMayorAlMaximoDiario() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    cuenta.poner(245);
  }

  @Test(expected = SaldoMenorException.class)
  public void ExtraerMasQueElSaldo() {
    cuenta.poner(90);
    cuenta.sacar(1001);
  }

  @Test(expected = SaldoMenorException.class)
  public void ExtraerMasQueElSaldoValorLimiteSuperior() {
    cuenta.poner(90);
    cuenta.sacar(91);
  }

  @Test()
  public void ExtraerMasQueElSaldoValorLimiteInferior() {
    cuenta.poner(90);
    cuenta.sacar(89);
    assertEquals(1, cuenta.getSaldo(),0);
  }

  @Test(expected = MaximoExtraccionDiarioException.class)
  public void ExtraerMasDineroDelLimiteDiario() {
    cuenta.poner(5000);
    cuenta.sacar(1001);
  }

  @Test()
  public void ExtraerDineroValorLimiteDiario() {
    cuenta.poner(1000);
    cuenta.sacar(1000);
    assertEquals(0, cuenta.getSaldo(),0);
  }

  @Test(expected = MontoNegativoException.class)
  public void ExtraerMontoNegativo() { cuenta.sacar(-500); }

  @Test()
  public void ElSaldoSeCalculaCorrectamenteLuegoDeRealizarDepositosYExtracciones() {
    cuenta.poner(100);
    cuenta.sacar(75);
    assertEquals(25, cuenta.getSaldo(),0);

  }

}