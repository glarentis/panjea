package it.eurotn.codice.generator;

/**
 * @author fattazzo
 *
 */
public class CodiceProtocollo {

  private String codice;

  private Integer valoreProtocollo;

  /**
   * Costruttore.
   * 
   * @param codice
   *          codice generato
   * @param valoreProtocollo
   *          valore del protocollo
   */
  public CodiceProtocollo(final String codice, final Integer valoreProtocollo) {
    super();
    this.codice = codice;
    this.valoreProtocollo = valoreProtocollo;
  }

  /**
   * @return the codice
   */
  public String getCodice() {
    return codice;
  }

  /**
   * @return the valoreProtocollo
   */
  public Integer getValoreProtocollo() {
    return valoreProtocollo;
  }

}
