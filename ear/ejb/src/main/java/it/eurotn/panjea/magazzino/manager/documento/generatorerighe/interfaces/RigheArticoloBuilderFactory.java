/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.documento.generatorerighe.interfaces;

import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.RigheArticoloBuilderFactoryBean.EGeneratoreRiga;

import java.util.List;

import javax.ejb.Local;

/**
 * @author leonardo
 */
@Local
public interface RigheArticoloBuilderFactory {

	/**
	 * Crea la lista di generatori righe disponibili.
	 * 
	 * @param generatoreRiga
	 *            il generatore richiesto
	 * @return la lista di generatori righe richiesti, usare EGeneratoreRiga.ALL per avere tutti i generatori
	 */
	List<IGeneratoreRigheArticolo> creaGeneratoriRigheArticolo(EGeneratoreRiga generatoreRiga);

}
