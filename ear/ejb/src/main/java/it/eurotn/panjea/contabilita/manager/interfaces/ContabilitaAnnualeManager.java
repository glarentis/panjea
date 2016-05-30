package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.service.exception.AperturaEsistenteException;
import it.eurotn.panjea.contabilita.service.exception.ChiusuraAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ChiusuraEsistenteException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.DocumentiNonStampatiGiornaleException;
import it.eurotn.panjea.contabilita.service.exception.GiornaliNonValidiException;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * 
 * Manager incaricato di eseguire le operazioni contabili annuali.<br/>
 * 
 * @author adriano
 * @version 1.0, 29/ago/07
 * 
 */
@Local
public interface ContabilitaAnnualeManager {

	/**
	 * Restituisce la lista delle aree contabili di apertura/chisura ordinate per data registrazione.<BR/>
	 * La lista contiene le aree contabili create fino all'anno cercato. <br/>
	 * Questo mi permette di controllare quali sono le aperture o chiusure precedenti o prendere solamente il primo
	 * elemento della lista e controllare se si tratta di un'apertura o chiusura
	 * 
	 * @param annoEsercizio
	 *            ultimo anno contabile per cercare le aree <br>
	 *            (es: anno contabile=2008 cerco tutte le aperture/chiusure fino al 2008 compreso)
	 * @param tipoConto
	 *            tipologie di conto di chiusura/apertura da caricare.
	 * @return lista di aree di apertura/chiusura con anno di esercizio<= al parametro annoEsercizio.<br/>
	 *         Se non esistono movimenti ritorna una lista vuota
	 * @throws TipoDocumentoBaseException
	 *             lanciata quando non sono presenti tutti i documenti base per le chiusure
	 * @throws ContabilitaException
	 *             rilanciata durante un'errore generico
	 */
	List<AreaContabile> caricaAreeContabiliAperturaChiusura(Integer annoEsercizio, TipoConto tipoConto)
			throws TipoDocumentoBaseException, ContabilitaException;

	/**
	 * Apre i conti annuali.<br>
	 * . Le aperture vengono fatte solamente per i conti ordine e patrimoniali.
	 * 
	 * @param annoEsercizio
	 *            anno di esercizio per aprire i conti
	 * @param dataDocumentoChiusura
	 *            data del documento da associareal documento creato.
	 * @throws AperturaEsistenteException
	 *             rilanciata se esite un documento di apertura nell'anno contabile
	 * @throws ChiusuraAssenteException
	 *             rilanciata se non ho un documento di chiusura dell'anno precedente ad annoEsercizio
	 * @throws TipoDocumentoBaseException
	 *             lanciata quando non sono presenti tutti i documenti base per le chiusure
	 * @throws ContiBaseException
	 *             lanciata quando non sono presenti i conti base per la chiusura interessata dal conto.
	 * */
	void eseguiAperturaConti(Integer annoEsercizio, Date dataDocumentoChiusura) throws ContiBaseException,
			AperturaEsistenteException, ChiusuraAssenteException, TipoDocumentoBaseException;

	/**
	 * Esecuzione delle operazioni di chiusura conti patrimoniale, economico e d'ordine.
	 * 
	 * @param annoEsercizio
	 *            anno di chiusura dei conti
	 * @param dataMovimento
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
	void eseguiChiusuraConti(Integer annoEsercizio, Date dataMovimento) throws ChiusuraEsistenteException,
			DocumentiNonStampatiGiornaleException, ContiBaseException, TipoDocumentoBaseException,
			GiornaliNonValidiException;

}
