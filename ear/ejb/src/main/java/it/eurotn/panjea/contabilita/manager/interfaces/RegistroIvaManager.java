/**
 * 
 */
package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.CodiceIva.IndicatoreVolumeAffari;
import it.eurotn.panjea.anagrafica.domain.CodiceIva.TipoCaratteristica;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * Interfaccia manager per la gestione del giornale iva.
 * 
 * @author Leonardo
 * @version 1.0, 22/nov/07
 */
@Local
public interface RegistroIvaManager {

	/**
	 * Dalle informazioni di data,mese e registro iva del giornale iva corrente carico il giornale precedente Se il
	 * giornale e' quello di inizio anno ritorna null Se il giornale e' di tipo riepilogativo prende in considerazione
	 * la periodicità (mensile,trimestrale,annuale), per le altre tipologie e' sempre mensile.
	 * 
	 * @param giornaleCorrente
	 *            giornale da cui recupero i dati per trovare il giornale precedente
	 * @return Giornale iva precedente a quello passato
	 */
	GiornaleIva caricaGiornaleIvaPrecedente(GiornaleIva giornaleCorrente);

	/**
	 * Carica i giornali per l'anno e mese scelti, basandosi sui registri iva presenti nell'anagrafica contabilit�; ogni
	 * registro iva ha in quel mese associato un solo giornale. Se non esiste ancora ne creo uno nuovo.
	 * 
	 * @param anno
	 *            l'anno in cui cercare il giornale
	 * @param mese
	 *            il mese in cui cercare il giornale
	 * @return List<GiornaleIva>
	 */
	List<GiornaleIva> caricaGiornaliIva(Integer anno, Integer mese);

	/**
	 * Carica i giornaliIva riepilogativi (utilizzati per la liquidazione) per l'anno.
	 * 
	 * @param anno
	 *            anno per il giornale iva
	 * @return Lista dei giornali. Lista vuota se non trova nessun giornale
	 */
	List<GiornaleIva> caricaGiornaliIvaRiepilogativi(int anno);

	/**
	 * Carica le righe iva con codice iva volume affari a SI dove il documento ha una entità senza partita IVA impostata
	 * (consumatori finali o privati).<br>
	 * Nota che vengono caricati i valori collegati a registri iva di tipo registro VENDITA e CORRISPETTIVO.
	 * 
	 * @param dataInizio
	 *            data inizio
	 * @param dataFine
	 *            data fine
	 * @return List<TotaliCodiceIvaDTO>
	 */
	List<TotaliCodiceIvaDTO> caricaRigheIvaVolumeAffariPrivati(Date dataInizio, Date dataFine);

	/**
	 * Carica il totale delle righe iva del periodo scelto, cercando le righe con codice iva dove l'indicatore del
	 * volume affari è quello passato come parametro. E' possibile inoltre filtrare il volume affari prescelto per
	 * documenti con entità dove è definita una partita iva (aziende) o dove non è definita (utente finale).<br>
	 * Nota che vengono caricati i valori collegati a registri iva di tipo registro VENDITA e CORRISPETTIVO.
	 * 
	 * @param dataInizio
	 *            la data inizio periodo
	 * @param dataFine
	 *            la data fine periodo
	 * @param indicatoreVolumeAffari
	 *            l'indicatore volume affari
	 * @param conPartitaIva
	 *            se true filtra i documenti per aziende, se false filtra i soli documenti per privati; altrimenti cerca
	 * @param filtraAliquoteIva
	 *            se <code>true</code> filtra i codici iva validi per il quadro VT su tutti i documenti indistintamente
	 * @return TotaliCodiceIvaDTO il totale imponibile e imposta delle righe
	 */
	TotaliCodiceIvaDTO caricaTotaliCodiceIvaByVolumeAffari(Date dataInizio, Date dataFine,
			IndicatoreVolumeAffari indicatoreVolumeAffari, Boolean conPartitaIva, boolean filtraAliquoteIva);

	/**
	 * Carica i <code>TotaliCodiceIvaDTO</code> in base al RegistroIVA. Metodo utilizzato per il calcolo dei Registri
	 * Iva Acquisti e Vendite
	 * 
	 * @param dataInizioPeriodo
	 *            dataInizioPeriodo
	 * @param dataFinePeriodo
	 *            dataFinePeriodo
	 * @param registroIva
	 *            registroIva
	 * @param tipoCaratteristica
	 *            tipoCaratteristica
	 * @param gestioneIva
	 *            gestioneIva
	 * @return List<TotaliCodiceIvaDTO>
	 */
	List<TotaliCodiceIvaDTO> caricaTotaliCodiciIvaByRegistro(Date dataInizioPeriodo, Date dataFinePeriodo,
			RegistroIva registroIva, TipoCaratteristica tipoCaratteristica, GestioneIva gestioneIva);

	/**
	 * Invalida il giornale associato all'area contabile selezionata, recupera da essa mese, anno e registro iva per
	 * caricare il registro iva e i successivi.
	 * 
	 * @param areaContabile
	 *            areaContabile
	 */
	void invalidaGiornaleIva(AreaContabile areaContabile);

	/**
	 * Invalida il giornale associato all'area contabile selezionata, recupera da essa mese, anno e registro iva per
	 * caricare il registro iva e i successivi verifica inoltre la data di registrazione precedente, caso in cui ad un
	 * documento è stata cambiata la data. Non modifica lo stato dei documenti di quel registro iva.
	 * 
	 * @param areaContabile
	 *            area contabile modificata
	 * @param dataRegistrazionePrecedente
	 *            data registrazione prima della modifica
	 */
	void invalidaGiornaleIva(AreaContabile areaContabile, Date dataRegistrazionePrecedente);

	/**
	 * Carica le righe iva basandosi sui parametri ricerca movimento, metodo per la stampa dei registri iva.
	 * 
	 * @param parametriRicerca
	 *            sono tenuti i dati per la ricerca delle righe iva
	 * @return List<TotaliCodiceIvaDTO>
	 */
	List<TotaliCodiceIvaDTO> ricercaRigheIva(ParametriRicercaMovimentiContabili parametriRicerca);

	/**
	 * Carica una lista che presenta il riepilogo delle righe iva raggruppate per codice del codice iva, dove per il
	 * campo imposta e imponibile viene fatta la somma di tutte le righe trovate.
	 * 
	 * @param parametriRicercaMovimentiContabili
	 *            parametri su cui impostare la ricerca righe
	 * @return List<TotaliCodiceIvaDTO>
	 */
	List<TotaliCodiceIvaDTO> ricercaRigheRiepilogoCodiciIva(
			ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili);

	/**
	 * Salva il giornale Iva.
	 * 
	 * @param giornaleIva
	 *            il giornale iva da salvare
	 * @return GiornaleIva
	 */
	GiornaleIva salvaGiornaleIva(GiornaleIva giornaleIva);

}
