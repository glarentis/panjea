package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.service.exception.StatoAreaContabileNonValidoException;
import it.eurotn.panjea.contabilita.service.exception.VariazioneStatoAreaContabileNonValidoPerStampaLibroGiornale;

import javax.ejb.Local;

@Local
public interface StatoAreaContabileManager {

	/**
	 * Esegue il cambio stato in Provvisorio dallo stato di Confermato, non salva l'area contabile
	 * 
	 * @param areaContabile
	 * @return
	 * @throws StatoAreaContabileNonValidoException
	 */
	public AreaContabile cambioStatoDaConfermatoInProvvisorio(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException;

	/**
	 * Esegue il cambio stato da Confermato in Verificato, non salva l'area contabile
	 * 
	 * @param areaContabile
	 * @return
	 * @throws StatoAreaContabileNonValidoException
	 */
	public AreaContabile cambioStatoDaConfermatoInVerificato(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException;

	/**
	 * Esegue il cambio stato da Provvisorio a Confermato invalida libro giornale e registro iva se presenti
	 * 
	 * @param areaContabile
	 * @return
	 * @throws StatoAreaContabileNonValidoException
	 */
	public AreaContabile cambioStatoDaProvvisorioInConfermato(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException;

	/**
	 * Esegue il cambio stato in Simulato, non salva l'area contabile
	 * 
	 * @param areaContabile
	 * @return
	 * @throws StatoAreaContabileNonValidoException
	 */
	public AreaContabile cambioStatoDaProvvisorioInSimulato(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException;

	/**
	 * Esegue il cambio stato in Provvisorio dallo stato di Simulato, non salva l'area contabile
	 * 
	 * @param areaContabile
	 * @return
	 * @throws StatoAreaContabileNonValidoException
	 */
	public AreaContabile cambioStatoDaSimulatoInProvvisorio(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException;

	/**
	 * Esegue il cambio stato da Verificato a Confermato, non salva l'area contabile
	 * 
	 * @param areaContabile
	 * @return
	 * @throws StatoAreaContabileNonValidoException
	 */
	public AreaContabile cambioStatoDaVerificatoInConfermato(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException;

	/**
	 * Esegue il cambio stato in confermato, non salva l'area contabile ma invalida registro iva e libro giornale se
	 * presenti
	 * 
	 * @param areaContabile
	 * @return
	 * @throws StatoAreaContabileNonValidoException
	 */
	public AreaContabile cambioStatoInConfermato(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException;

	/**
	 * Esegue il cambio stato in provvisorio, non salva l'area contabile
	 * 
	 * @param areaContabile
	 * @return
	 * @throws StatoAreaContabileNonValidoException
	 */
	public AreaContabile cambioStatoInProvvisorio(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException;

	/**
	 * Esegue il cambio stato da Provvisorio a Confermato per l'esecuzione della conferma righe contabili, non salva
	 * l'area contabile
	 * 
	 * @param areaContabile
	 * @return
	 * @throws StatoAreaContabileNonValidoException
	 */
	public AreaContabile cambioStatoPerConfermaRigheContabili(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException;

	/**
	 * Esegue il cambio stato da Confermato a Verificato per l'esecuzione della stampa del Libro Giornale, non salva
	 * l'area contabile
	 * 
	 * @param areaContabile
	 * @return
	 * @throws VariazioneStatoAreaContabileNonValidoPerStampaLibroGiornale
	 */
	public AreaContabile cambioStatoPerStampaLibroGiornale(AreaContabile areaContabile)
			throws VariazioneStatoAreaContabileNonValidoPerStampaLibroGiornale;

}
