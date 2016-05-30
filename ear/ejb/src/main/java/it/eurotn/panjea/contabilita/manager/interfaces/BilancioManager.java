package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancio;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancioConfronto;
import it.eurotn.panjea.contabilita.util.SaldoConti;
import it.eurotn.panjea.contabilita.util.SaldoContoConfronto;

import java.util.List;

import javax.ejb.Local;

/**
 * 
 * Gestisce i bilanci aziendali.<br/>
 * L'estratto conto è considerato un bilancio per il conto quindi viene gestito da questo manager.
 * 
 * @author adriano
 * @version 1.0, 29/ago/07
 * 
 */
@Local
public interface BilancioManager {

	/**
	 * Carica il bilancio aziendale.<BR/>
	 * Se la data di inizio bilancio è uguale alla data di inizio Esercizio il bilancio compende il saldo inizio anno.<br/>
	 * Se la data di inizio bilancio è uguale alla data di inizio Esercizio il bilancio non calcola il saldo inizio anno <br/>
	 * ma fa solamente la somma dei movimenti.
	 * 
	 * @param parametriRicercaBilancio
	 *            parametri per il calcolo del bilancio
	 * @throws ContabilitaException
	 *             ContabilitaException
	 * @throws TipoDocumentoBaseException
	 *             TipoDocumentoBaseException
	 */
	SaldoConti caricaBilancio(ParametriRicercaBilancio parametriRicercaBilancio) throws TipoDocumentoBaseException,
			ContabilitaException;

	/**
	 * Carica il bilancio annuale per tutti i conti, tipiDocumento e stati documento
	 * {@link StatoAreaContabile#VERIFICATO} e {@link StatoAreaContabile#CONFERMATO}.
	 * 
	 * @param annoEsercizio
	 * @return
	 * @throws ContabilitaException
	 * @throws TipiDocumentoBaseException
	 */
	SaldoConti caricaBilancioAnnuale(Integer annoEsercizio) throws TipoDocumentoBaseException, ContabilitaException;

	/**
	 * Metodo per caricare il bilancio a confronto, vengono caricati i bilanci di due periodi temporali e viene generata
	 * la riga di confronto tra i due che riporta saldo iniziale, saldo finale differenza saldi e percentuale differenza
	 * saldi.
	 * 
	 * @param parametriRicercaBilancioConfronto
	 *            i parametri contenenti le date su cui caricare i bilanci
	 * @return List<ConfrontoSottocontiDTO> righeBilancioConfronto
	 * @throws ContabilitaException
	 */
	List<SaldoContoConfronto> caricaBilancioConfronto(
			ParametriRicercaBilancioConfronto parametriRicercaBilancioConfronto) throws TipoDocumentoBaseException,
			ContabilitaException;
}
