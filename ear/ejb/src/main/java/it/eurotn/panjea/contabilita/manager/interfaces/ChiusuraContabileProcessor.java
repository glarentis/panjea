package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.service.exception.ChiusuraEsistenteException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.DocumentiNonStampatiGiornaleException;
import it.eurotn.panjea.contabilita.service.exception.GiornaliNonValidiException;
import it.eurotn.panjea.contabilita.util.SaldoConti;

import java.util.Date;

import javax.ejb.Local;

@Local
public interface ChiusuraContabileProcessor {

	/**
	 * Crea il documento di chiusura<br/>
	 * . Dal bilancio carico le righe e scrivo sul documento di chiusura le righe con conto girato (essendo una
	 * chiusura).<br/>
	 * Ogni classe che implementa la chiusura può personalizzare il documento di chiusura con il metodo
	 * completadocumentoChisuraApertura Ricevo direttamente il bilancio per eseguirlo per non doverlo ricreare ogni
	 * volta.<br/>
	 * Il bilancio non viene controllato, quindi deve essere calcolato con i parametri corretti.<br/>
	 * Dal punto di vista architetturale non è una buona soluzione ma calcolare per 3 volte il bilancio (per ogni
	 * tipoconto)<br/>
	 * degrada le prestazioni in maniera considerevole.
	 * 
	 * @param annoEsercizio
	 *            anno di chiusura dei conti
	 * @param bilancio
	 *            Bilancio per l'anno di chiusura.
	 * @param dataDocumentoChiusura
	 *            data di chiusura da associare al documento creato.
	 * @throws ChiusuraEsistenteException
	 *             lanciata quando esiste già una cihusura
	 * @throws DocumentiNonStampatiGiornaleException
	 *             lanciata quando ci sono dei movimenti non non stampati a giornale
	 * @throws TipoDocumentoBaseException
	 *             lanciata quando non sono presenti tutti i documenti base per le chiusure
	 * @throws ContiBaseException
	 *             lanciata quando non sono presenti i conti base per la chiusura interessata dal conto.
	 * @throws GiornaliNonValidiException
	 *             lanciata quando esistono dei giornali in stato non valido
	 */
	void eseguiChiusura(Integer annoEsercizio, SaldoConti bilancio, Date dataDocumentoChiusura)
			throws ChiusuraEsistenteException, DocumentiNonStampatiGiornaleException, TipoDocumentoBaseException,
			ContiBaseException, GiornaliNonValidiException;

	/**
	 * Verifica se ci sono dei documenti di chiusura presenti nell'anno di esercizio richiesto.
	 * 
	 * @param annoEsercizio
	 *            anno di esercizio per la verifica dei documenti
	 * @throws ChiusuraEsistenteException
	 *             rilanciata quando esiste il documento di chiusura per l'anno
	 */
	void verificaDocumentiChisuraPresenti(Integer annoEsercizio) throws ChiusuraEsistenteException;

	/**
	 * Controlla che tutti i movimenti contabili siano stampati a giornale e che tutti i giornali siano in stato valido<br/>
	 * Tutti i documenti nel periodo interessati alla chiusura devono essere stampati a giornale.<br/>
	 * . Quindi tutti i documenti devono essere in stato verificato e i giornali validati.<br/>
	 * 
	 * @param annoEsercizio
	 *            anno di esercizio per la verifica dei documenti
	 * @throws DocumentiNonStampatiGiornaleException
	 *             lanciata quando ci sono dei movimenti non non stampati a giornale
	 * @throws GiornaliNonValidiException
	 *             lanciata quando esistono dei giornali in stato non valido
	 */
	void verificaStampaGiornaleMovimenti(Integer annoEsercizio) throws DocumentiNonStampatiGiornaleException,
			GiornaliNonValidiException;

	/**
	 * Verifica l'esistenza dei tipi conto per la chiusura.
	 * 
	 * @throws ContiBaseException
	 *             lanciata quando non sono presenti i conti base per la chiusura interessata dal conto.
	 */
	void verificaTipiContiChiusura() throws ContiBaseException;

	/**
	 * Verifica l'esistenza del TipoDocumentoBase per la chiusura contabile.<br/>
	 * .
	 * 
	 * @throws TipoDocumentoBaseException
	 *             lanciata quando non sono presenti tutti i documenti base per le chiusure
	 */
	void verificaTipoDocumentoBaseChiusura() throws TipoDocumentoBaseException;

}