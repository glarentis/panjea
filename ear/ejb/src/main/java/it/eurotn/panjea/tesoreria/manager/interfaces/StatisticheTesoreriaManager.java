package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * Metodi per recuperare statistiche di tesoreria.
 * 
 * @author giangi
 * 
 */
@Local
public interface StatisticheTesoreriaManager {

	/**
	 * 
	 * @param entitaLite
	 *            entita per la quale calcolare l'importo dei documenti aperti.
	 * @return totale documenti imponibile+imposta dei documenti aperti. Il tipo Documento da considerare è selezionato
	 *         nei settings.
	 */
	BigDecimal calcolaImportoDocumentiAperti(EntitaLite entitaLite);

	/**
	 * Calcola l'importo delle rate aperte per l'entità.
	 * 
	 * @param entita
	 *            entità interessata
	 * @return importo ancora aperto per l'entità
	 */
	BigDecimal calcolaImportoRateAperte(EntitaLite entita);

	/**
	 * 
	 * @param idEntita
	 *            id dell'entità per la quale caricare le rate da poter utilizzare per l'acconto.
	 * @param tipoPartita
	 *            tipo partita ATTIVA/PASSIVA
	 * @return list di rate rate da poter pagare con un acconto.
	 */
	List<SituazioneRata> caricaSituazioneRateDaUtilizzarePerAcconto(Integer idEntita, TipoPartita tipoPartita);

	/**
	 * @param tipoPartita
	 *            tipo partita
	 * @param dataInizio
	 *            data inizio statistica
	 * @param dataFine
	 *            data fine statistica
	 * @param idEntita
	 *            filtra per entità, se null non viene considerato
	 * @param stampaDettaglio
	 *            indica se stampo il dettaglio o no; può essere null dato che la scelta influenza solo l'ordinamento
	 *            dei dati
	 * @param idCategoriaEntita
	 *            categoria per l'entità. se null non viene considerato.
	 * @return lista contenente il dettaglio della situazione delle rate ancora aperte escluse F24
	 */
	List<SituazioneRata> caricaSituazioneRateStampa(TipoPartita tipoPartita, Date dataInizio, Date dataFine,
			Integer idEntita, Boolean stampaDettaglio, Integer idCategoriaEntita);
}
