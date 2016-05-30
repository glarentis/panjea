package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import java.util.Date;

import javax.ejb.Local;

@Local
public interface ConfermaMovimentoInFatturazioneManager {

	/**
	 * Conferma il movimento in fatturazione temporanea.
	 * 
	 * @param areaMagazzino
	 *            movimento da confermare
	 * @param dataCreazione
	 *            dataDi creazione utilizzata per i dati generazione del movimento confermato
	 * 
	 */
	void confermaMovimentoInFatturazione(AreaMagazzino areaMagazzino, Date dataCreazione);
}
