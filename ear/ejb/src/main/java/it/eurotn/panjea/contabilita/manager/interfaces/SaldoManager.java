package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.util.SaldoConti;
import it.eurotn.panjea.contabilita.util.SaldoConto;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * Calcola i saldi per un conto.
 *
 * @author giangi
 *
 */
@Local
public interface SaldoManager {

	/**
	 * Calcola il Saldo di inizio anno.<br/>
	 * Per saldo di Inizio Anno si intende il valore dei movimenti all'apertura dell'anno di esercizio.<br/>
	 *
	 * Per trovare il saldo cerca tutti i movimenti di apertura/chiusura fino all'anno richiesto ordinati per data di
	 * registrazione in modalità descrescente.<br/>
	 * Se il primo movimento trovato è un'apertura nell'anno richiesto prendo il valore dei saldi direttamente dal
	 * documento.<br.>
	 * Se è una chiusura dell'anno prima, prendo il valore girando i conti (diventa quindi un'apertura nell'anno di
	 * esercizio richiesto).<br/>
	 * Se è un'apertura non è nell'anno prendo il saldo dell'apertura e sommo tutti i movimenti in stato confermato o
	 * provvisorio fino all'anno esercizio richiesto.<br/>
	 * Il saldo contiene i movimenti in stato {@link StatoAreaContabile#CONFERMATO} e
	 * {@link StatoAreaContabile#VERIFICATO per tutti i {@link TipoDocumento}.<br>
	 * / Se ho settato nella {@link ContabilitaSettings#getAnnoInizioCalcoloSaldo()}=annoEsercizio non calcolo i saldi
	 * di inizio anno. Questo viene utilizzato <br>
	 * . quando devo aprire l'anno in panjea ed ho movimenti importati da Europa. Per poter aprire le partite devo
	 * creare cliente per cliente ( o fornitore) dei documenti che creano le partite.<br>
	 * In questo modo però apro anche i conti patrimoniali, quindi non devo calcolare il saldo di inizio anno altrimenti
	 * li conterei due volte.
	 *
	 * @param tipoConto
	 *            tipo di conto per il quale si vuole i saldi. Se è presente il parametro sottoConto il valore viene
	 *            sovrascritto dal tipoConto del sottoconto passato come parametro.<br/>
	 *            Se sottoconto è valorizzato può essere <code>null</code>.
	 * @param sottoConto
	 *            null per calcolare i saldi di tutti i conti con tipoconto uguale al parametro Tipoconto. <b>NB.</b> Se
	 *            sottoConto=null tipoConto deve essere valorizzato.
	 * @param centroCosto
	 *            centroCosto per trovare il saldo del centro di costo. Se sottoConto e tipoConto==null calcola i saldi
	 *            per tutti i sottoConti.
	 * @param annoEsercizio
	 *            anno di esercizio cercato.
	 * @param aziendaLite
	 *            utilizzato per recuperare le date di inizio attività contabile
	 * @return saldi dei conti. Se ho passato come parametro sottoConto il saldo contiene un solo conto.
	 * @throws TipoDocumentoBaseException
	 *             Se non ho configurato i {@link TipoDocumentoBase} per le aperture e chiusure.
	 * @throws ContabilitaException
	 *             Lanciata su errori generici
	 */
	SaldoConti calcolaSaldiInizioAnno(TipoConto tipoConto, SottoConto sottoConto, CentroCosto centroCosto,
			Integer annoEsercizio, AziendaLite aziendaLite) throws TipoDocumentoBaseException, ContabilitaException;

	/**
	 * Calcolo il saldo dei movimenti contabili per tutti i conti<br/>
	 * E' una semplice sommatoria dare/avere dei movimenti nel periodo.<br/>
	 * Se nel range dei movimenti trovati ho movimenti di apertura/chiusura questi vengono tolti.
	 *
	 * @param dataInizio
	 *            data iniziale calcolo
	 * @param dataFine
	 *            data finale calcolo
	 * @param annoEsercizio
	 *            anno di esercizio
	 * @param aziendaLite
	 *            aziendaLite
	 * @param listaStatiAree
	 *            stati da considerare. Se null considera tutti gli stati.
	 * @param esclusiEconomici
	 *            esclude i conti economici dal calcolo del saldo
	 * @return lista di {@link SaldoConto} per ogni conto presente nel periodo richiesto
	 * @throws TipoDocumentoBaseException
	 *             Se non ho configurato i {@link TipoDocumentoBase} per le aperture e chiusure.
	 * @throws ContabilitaException
	 *             Lanciata su errori generici
	 */
	List<SaldoConto> calcoloSaldi(Date dataInizio, Date dataFine, Integer annoEsercizio, AziendaLite aziendaLite,
			List<StatoAreaContabile> listaStatiAree, boolean esclusiEconomici) throws TipoDocumentoBaseException,
			ContabilitaException;

	/**
	 * Calcola dei saldi da una data di inizio ad una data di fine per l'anno contabile. Se Data Inizio e Data Fine sono
	 * null considera solamente l' anno di esercizio
	 *
	 * @param sottoConto
	 *            {@link SottoConto} interessato al saldo
	 * @param centroCosto
	 *            centroCosto
	 * @param dataInizio
	 *            data Inizio del calcolo. Se null considero solo annoEsercizio
	 * @param dataFine
	 *            data fine calcolo. Se null consideo solo l'annoEsercizio
	 * @param annoEsercizio
	 *            anno interessato al calcolo
	 * @param aziendaLite
	 *            aziendaLite.
	 * @param listaStatiAree
	 *            stati da filtrare
	 * @param esclusiEconomici
	 *            esclude o meno i conti economici
	 * @return lista di saldoConto contenente i saldi di ogni conto presente nei movimenti
	 * @throws TipoDocumentoBaseException
	 *             lanciato quando mancano i {@link TipoDocumentoBase}
	 * @throws ContabilitaException
	 *             lanciato su un errore generico
	 */
	List<SaldoConto> calcoloSaldi(SottoConto sottoConto, CentroCosto centroCosto, Date dataInizio, Date dataFine,
			Integer annoEsercizio, AziendaLite aziendaLite, List<StatoAreaContabile> listaStatiAree,
			boolean esclusiEconomici) throws TipoDocumentoBaseException, ContabilitaException;

	/**
	 * Calcola dei saldi da una data di inizio ad una data di fine per l'anno contabile. Se Data Inizio e Data Fine sono
	 * null considera solamente l' anno di esercizio
	 *
	 * @param sottoConto
	 *            {@link SottoConto} interessato al saldo
	 * @param centroCosto
	 *            centroCosto
	 * @param dataInizio
	 *            data Inizio del calcolo. Se null considero solo annoEsercizio
	 * @param dataFine
	 *            data fine calcolo. Se null consideo solo l'annoEsercizio
	 * @param annoEsercizio
	 *            anno interessato al calcolo
	 * @param aziendaLite
	 *            aziendaLite.
	 * @param listaStatiAree
	 *            stati da filtrare
	 * @param esclusiEconomici
	 *            esclude o meno i conti economici
	 * @param caricaCentriCosto
	 *            include le righe centro costo nella ricerca per visualizzare un ulteriore livello
	 * @return lista di saldoConto contenente i saldi di ogni conto presente nei movimenti
	 * @throws TipoDocumentoBaseException
	 *             lanciato quando mancano i {@link TipoDocumentoBase}
	 * @throws ContabilitaException
	 *             lanciato su un errore generico
	 */
	List<SaldoConto> calcoloSaldi(SottoConto sottoConto, CentroCosto centroCosto, Date dataInizio, Date dataFine,
			Integer annoEsercizio, AziendaLite aziendaLite, List<StatoAreaContabile> listaStatiAree,
			boolean esclusiEconomici, boolean caricaCentriCosto) throws TipoDocumentoBaseException,
			ContabilitaException;

	/**
	 * Calcolo il saldo dei movimenti contabili fra due date per un determinato conto.<br/>
	 * E' una semplice sommatoria dare/avere dei movimenti nel periodo.<br/>
	 * Se nel range dei movimenti trovati ho movimenti di apertura/chiusura questi vengono tolti.
	 *
	 * @param sottoConto
	 *            sottoConto interessato per il calcolo del saldo
	 * @param centroCosto
	 *            centroCosto
	 * @param dataInizio
	 *            data iniziale calcolo
	 * @param dataFine
	 *            data finale calcolo
	 * @param annoEsercizio
	 *            anno di esercizio
	 * @param aziendaLite
	 *            aziendaLite
	 * @param listaStatiAree
	 *            stati da considerare. Se null considera tutti gli stati.
	 * @param esclusiEconomici
	 *            esclude i conti economici
	 * @return il saldo del conto. NULL se non trovo nessun movimento.
	 * @throws TipoDocumentoBaseException
	 *             Se non ho configurato i {@link TipoDocumentoBase} per le aperture e chiusure.
	 * @throws ContabilitaException
	 *             Lanciata su errori generici
	 */
	SaldoConto calcoloSaldo(SottoConto sottoConto, CentroCosto centroCosto, Date dataInizio, Date dataFine,
			Integer annoEsercizio, AziendaLite aziendaLite, List<StatoAreaContabile> listaStatiAree,
			boolean esclusiEconomici) throws TipoDocumentoBaseException, ContabilitaException;

	/**
	 * Calcola il saldo ad una determinata data.<br/>
	 * Viene calcolato il saldo di inizio anno e aggiunto il saldo dalla data di inizio esercizio nell'anno richiesto
	 * fino al giorno prima. <br/>
	 * Considera solamente i movimenti in stato {@link StatoAreaContabile#CONFERMATO} e
	 * {@link StatoAreaContabile#VERIFICATO}
	 *
	 * @param sottoConto
	 *            sottoConto del saldo
	 * @param data
	 *            data nella quale si vuole il saldo
	 * @param annoEsercizio
	 *            anno di esercizio da considerare
	 * @param aziendaLite
	 *            aziendaLite
	 * @param centroCosto
	 *            centro di costo per il quale calcolare il saldo
	 * @return saldo del conto
	 * @throws TipoDocumentoBaseException
	 *             Se non ho configurato i {@link TipoDocumentoBase} per le aperture e chiusure.
	 * @throws ContabilitaException
	 *             Lanciata su errori generici
	 */
	SaldoConto calcoloSaldo(SottoConto sottoConto, CentroCosto centroCosto, Date data, Integer annoEsercizio,
			AziendaLite aziendaLite) throws TipoDocumentoBaseException, ContabilitaException;
}
