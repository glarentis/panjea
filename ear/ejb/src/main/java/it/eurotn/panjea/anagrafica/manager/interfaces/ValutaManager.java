package it.eurotn.panjea.anagrafica.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface ValutaManager {

	/**
	 * Aggiorna tutti i tassi di cambio per le valute aziendali..
	 * 
	 * @param byteArray
	 *            array contenente il file per l'aggiornamento delle valute
	 */
	void aggiornaTassi(byte[] byteArray);

	/**
	 * Cancella un cambio valuta. <br/>
	 * Se la valuta non ha nessun cambio al cancella.
	 * 
	 * @param cambioValuta
	 *            cambio da cancellare
	 */
	void cancellaCambioValuta(CambioValuta cambioValuta);

	/**
	 * Cancella una valuta azienda e lo storico dei cambi.
	 * 
	 * @param valutaAzienda
	 *            valuta da cancellare.
	 */
	void cancellaValutaAzienda(ValutaAzienda valutaAzienda);

	/**
	 * Carica una valuta con il cambio valido per la data di riferimento.<br/>
	 * Se a quella data non esiste il cambio viene presa la più vicina
	 * 
	 * @param codiceValuta
	 *            codice valuta richiesta
	 * @param date
	 *            data di riferimento
	 * @return cambio della valuta alla data richiesta.
	 * @throws CambioNonPresenteException
	 *             lanciata quando non è presente il cambio
	 */
	CambioValuta caricaCambioValuta(String codiceValuta, Date date) throws CambioNonPresenteException;

	/**
	 * Carica le valute con il cambio valido per la data di riferimento.<br/>
	 * Se a quella data non esiste il cambio viene presa la più vicina
	 * 
	 * @param date
	 *            data di riferimento
	 * @return cambiValuta alla data richiesta per tutte le valute aziendali
	 */
	List<CambioValuta> caricaCambiValute(Date date);

	/**
	 * Carica tutti i cambi della valuta per l'anno di riferimento.
	 * 
	 * @param codiceValuta
	 *            codice valuta
	 * @param anno
	 *            anno di riferimento
	 * @return storico dei cambi per l'anno e la valuta voluti.
	 */
	List<CambioValuta> caricaCambiValute(String codiceValuta, int anno);

	/**
	 * 
	 * @return codici delle valute abilitate per l'azienda
	 */
	List<String> caricaCodiciValuteAzienda();

	/**
	 * Carica {@link ValutaAzienda} attraverso il suo codice valuta.
	 * 
	 * @param codiceValuta
	 *            codice della valuta da caricare
	 * @return {@link ValutaAzienda} identificata da codiceValuta
	 */
	ValutaAzienda caricaValutaAzienda(String codiceValuta);

	/**
	 * 
	 * @return valuta dell'azienda corrente
	 */
	ValutaAzienda caricaValutaAziendaCorrente();

	/**
	 * 
	 * @return carica le valute dell'azienda.
	 */
	List<ValutaAzienda> caricaValuteAzienda();

	/**
	 * Salva un cambio valuta.
	 * 
	 * @param cambioValuta
	 *            cambio da salvare
	 * @return cambio salvato
	 */
	CambioValuta salvaCambioValuta(CambioValuta cambioValuta);

	/**
	 * Salva una valuta azienda.
	 * 
	 * @param valutaAzienda
	 *            valuta azienda da salvare
	 * @return valutaazienda salvata
	 */
	ValutaAzienda salvaValutaAzienda(ValutaAzienda valutaAzienda);
}
