package it.eurotn.panjea.ordini.manager.documento.produzione.interfaces;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoProduzione;
import it.eurotn.panjea.ordini.util.ParametriRicercaProduzione;

import java.util.List;

import javax.ejb.Local;

@Local
public interface OrdiniProduzioneManager {

	/**
	 * @param parametriRicercaProduzione
	 *            parametriRicercaProduzione
	 * @return lista di righe ordine produzione
	 */
	List<RigaDistintaCaricoProduzione> caricaRigheEvasioneProduzione(
			ParametriRicercaProduzione parametriRicercaProduzione);

}
