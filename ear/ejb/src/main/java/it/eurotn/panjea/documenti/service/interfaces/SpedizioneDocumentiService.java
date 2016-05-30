package it.eurotn.panjea.documenti.service.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.documenti.util.EtichettaSpedizioneDocumentoDTO;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface SpedizioneDocumentiService {

	/**
	 * Carica le etichette per i documenti indicati.
	 *
	 * @param params
	 *            parametri
	 * @return etichette caricate
	 */
	List<EtichettaSpedizioneDocumentoDTO> caricaEtichetteSpedizioneDocumenti(Map<Object, Object> params);

	/**
	 * Carica tutti i movimenti per la spedizione.
	 *
	 * @param areaDocumentoClass
	 *            classe dei documenti da spedire
	 * @param idDocumenti
	 *            id documenti
	 * @return movimenti caricati
	 */
	List<MovimentoSpedizioneDTO> caricaMovimentiPerSpedizione(Class<? extends IAreaDocumento> areaDocumentoClass,
			List<Integer> idDocumenti);
}
