package it.eurotn.codice.generator.interfaces;

import javax.ejb.Local;

/**
 * @author fattazzo
 *
 */
@Local
public interface ProtocolloGenerator {

  /**
   * Restituisce il codice da utilizzare per il codice protocollo specificato.
   * 
   * @param codiceProtocollo
   *          codice protocollo
   * @param maxValue
   *          valore massimo
   * @return codice
   */
  String nextCodice(String codiceProtocollo, Integer maxValue);
}
