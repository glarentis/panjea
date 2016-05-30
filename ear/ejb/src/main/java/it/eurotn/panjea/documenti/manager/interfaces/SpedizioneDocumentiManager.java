package it.eurotn.panjea.documenti.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.documenti.util.EtichettaSpedizioneDocumentoDTO;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;

import java.util.List;

import javax.ejb.Local;

@Local
public interface SpedizioneDocumentiManager {

	/**
	 * Cambia lo stato di spedizione del movimento restituendo quello nuovo.
	 *
	 * @param areaDocumento
	 *            area documento di riferimento
	 * @return nuovo stato
	 */
	StatoSpedizione cambiaStatoSpedizioneMovimento(IAreaDocumento areaDocumento);

	/**
	 * Carica le etichette per i documenti indicati.
	 *
	 * @param idDocumenti
	 *            id dei documenti di cui stampare le etichette
	 * @param numeroEtichettaIniziale
	 *            numero di etichetta iniziale
	 * @return etichette create
	 */
	List<EtichettaSpedizioneDocumentoDTO> caricaEtichetteSpedizioneDocumenti(List<Integer> idDocumenti,
			int numeroEtichettaIniziale);

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
