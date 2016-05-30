package it.eurotn.codice.generator.interfaces;

import javax.ejb.Local;

/**
 * @author fattazzo
 *
 */
@Local
public interface LastCodiceGenerator {

  /**
   * Restituisce il codice successivo rispetto a quello più alto presente.
   * 
   * @param classEntity
   *          classe dell'entità
   * @param codiceAziendaPath
   *          path della proprietà del codice azienda
   * @return nuovo codice
   */
  Integer nextCodice(Class<?> classEntity, String codiceAziendaPath);
}
