package it.eurotn.codice.generator.interfaces;

import java.util.Map;

import it.eurotn.entity.EntityBase;

/**
 * @author fattazzo
 *
 */
public interface VariabiliCodiceManager {

  String ANNO4 = "anno4";
  String ANNO2 = "anno2";
  String TIPODOC = "tipoDoc";
  String VALPROT = "valoreProt";

  String VAR_SEPARATOR = "$";

  /**
   * Restituisce le variabili valorizzate in base all'entity di riferimento.
   * 
   * @param entity
   *          entity
   * @return variabili
   */
  Map<String, String> creaMapVariabili(EntityBase entity);

  /**
   * Restituisce tutte le variabili che possono essere utilizzate per la generazione del codice.
   * 
   * @return lista di variabili
   */
  String[] getVariabili();

}
