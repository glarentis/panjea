package it.eurotn.codice.generator.interfaces;

import javax.ejb.Local;

import it.eurotn.codice.generator.CodiceProtocollo;
import it.eurotn.entity.EntityBase;

/**
 * @author fattazzo
 *
 */
@Local
public interface ProtocolloAnnoGenerator {

  /**
   * Restituisce il codice da utilizzare in base al protocollo e anno dell'entity di riferimento.
   * 
   * @param entity
   *          entity
   * @return codice
   */
  CodiceProtocollo nextCodice(EntityBase entity);

  /**
   * Restituisce il codice da utilizzare in base al protocollo e anno dell'entity di riferimento.
   * 
   * @param entity
   *          entity
   * @param registroProtocollo
   *          registro protocollo
   * @return codice
   */
  CodiceProtocollo nextCodice(EntityBase entity, String registroProtocollo);

  /**
   * Se il valore protocollo associato all'entity è l'ultimo valore del protocollo, quest'ultimo
   * viene decrementato.
   * 
   * @param entity
   *          entity
   */
  void restoreCodice(EntityBase entity);

  /**
   * Se il valore protocollo associato all'entity è l'ultimo valore del protocollo, quest'ultimo
   * viene decrementato.
   * 
   * @param entity
   *          entity
   * @param registroProtocollo
   *          registro protocollo
   */
  void restoreCodice(EntityBase entity, String registroProtocollo);
}
