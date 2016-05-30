package it.eurotn.panjea.rich.bd;

import java.util.Date;
import java.util.List;

import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;

public interface IValutaBD {

    /**
     * Aggiorna tutti i tassi di cambio per le valute aziendali.
     *
     * @param byteArray
     *            contenuto del file di aggiornamento
     */
    @AsyncMethodInvocation
    void aggiornaTassi(byte[] byteArray);

    /**
     * Cancella un cambio valuta.
     *
     * @param cambioValuta
     *            cambio da cancellare
     */
    void cancellaCambioValuta(CambioValuta cambioValuta);

    /**
     * Cancella una valuta azienda.
     *
     * @param valutaAzienda
     *            valuta da cancellare
     */
    void cancellaValutaAzienda(ValutaAzienda valutaAzienda);

    /**
     * Carica una valuta con il cambio valido per la data di riferimento.<br/>
     * Se a quella data non esiste il cambio viene presa la più vicina.<br/>
     * Tiene i risultati in cache per 5 sec per evitare di caricare il cambio più volte.
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
     * Carica tutti i cambi valuta di quella data. Se non esiste un cambio valuta nella data specificata viene caricato
     * quello con la data minore più prossimo.
     *
     * @param date
     *            data di riferimento
     * @return cambio per quella valuta
     */
    List<CambioValuta> caricaCambiValute(Date date);

    /**
     * Carica tutti i cambi della valuta data per l'anno di riferimento.
     *
     * @param codiceValuta
     *            codice della valuta
     * @param anno
     *            anno di riferimento
     * @return storico dei cambi per l'anno e la valuta di riferimento
     */
    List<CambioValuta> caricaCambiValute(String codiceValuta, int anno);

    /**
     *
     * @param codiceValuta
     *            codice della valuta da caricare
     * @return valuta caricata
     */
    ValutaAzienda caricaValutaAzienda(String codiceValuta);

    /**
     *
     * @return valuta dell'azienda corrente
     */
    @AsyncMethodInvocation
    ValutaAzienda caricaValutaAziendaCorrente();

    /**
     * Carica tutte le valute dell'azienda.
     *
     * @return lista delle valute azienda
     */
    @AsyncMethodInvocation
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
     *            valuta da salavre
     * @return valuta salvata
     */
    ValutaAzienda salvaValutaAzienda(ValutaAzienda valutaAzienda);

}