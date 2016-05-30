/**
 * 
 */
package it.eurotn.panjea.conai.manager.interfaces;

import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.interfaces.IGeneratoreRigheArticolo;

import javax.ejb.Local;

/**
 * Crea le righe del conai per gli articoli assogettati alla tassa per i tipi area magazzino che generano valori
 * fatturato.(ad es. sul ddt gestisco il conai e quindi creo le righe conai componente, ma non genero le righe conai che
 * riporto invece in fattura)<br/>
 * Se l'area magazzino contiene già delle righe articolo per il conai, queste vengono cancellate e generate nuovamente<br>
 * <b>NB:</b>Le righe restituite sono già persistenti.
 * 
 * @author leonardo
 */
@Local
public interface RigheConaiBuilder extends IGeneratoreRigheArticolo {

}
