package it.eurotn.codice.generator.interfaces;

import java.util.Map;

import javax.ejb.Local;

/**
 * @author fattazzo
 *
 */
@Local
public interface CodicePatternManager {

  /**
   * Genera il codice in base al pattern specificato e usando i valori delle variabili contenute
   * nella mappa.
   * 
   * @param pattern
   *          pattern di generazione
   * @param mapVariabili
   *          mappa delle variabili
   * @return codice generato
   */
  String genera(String pattern, Map<String, String> mapVariabili);

}
