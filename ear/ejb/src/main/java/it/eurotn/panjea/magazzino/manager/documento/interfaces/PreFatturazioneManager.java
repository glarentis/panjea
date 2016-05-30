package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import it.eurotn.panjea.magazzino.util.MovimentoPreFatturazioneDTO;

import java.util.List;

import javax.ejb.Local;

@Local
public interface PreFatturazioneManager {

	/**
	 * Carica tutti i documenti di prefatturazione.
	 *
	 * @param utente
	 *            utente di riferimento
	 * @return movimenti
	 */
	List<MovimentoPreFatturazioneDTO> caricaMovimentPreFatturazione(String utente);

}
