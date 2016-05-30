package it.eurotn.panjea.anagrafica.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.service.exception.LinguaAziendaleDeleteException;

@Local
public interface AnagraficaTabelleManager {

    /**
     * Cancella una conversione unità di musura.
     *
     * @param conversioneUnitaMisura
     *            <code>ConversioneUnitaMisura</code> da cancellare
     */
    void cancellaConversioneUnitaMisura(ConversioneUnitaMisura conversioneUnitaMisura);

    /**
     * Cancella una lingua gestita dall'azienda. Lancia un errore se cerco di cancellare la lingua
     * aziendale
     *
     * @param lingua
     *            lingua da salvare
     * @throws LinguaAziendaleDeleteException
     *             sollevata de sto cercando di cancellare la lingua aziendale
     */
    void cancellaLingua(Lingua lingua) throws LinguaAziendaleDeleteException;

    /**
     * Cancella una {@link NotaAnagrafica}.
     *
     * @param notaAnagrafica
     *            nota da cancellare
     */
    void cancellaNotaAnagrafica(NotaAnagrafica notaAnagrafica);

    /**
     * Cancella una unità di musura.
     *
     * @param unitaMisura
     *            <code>UnitaMisura</code> da cancellare
     */
    void cancellaUnitaMisura(UnitaMisura unitaMisura);

    /**
     * Cancellazione di un record dall'anagrafica zona geografica.
     *
     * @param zonaGeografica
     *            {@link ZonaGeografica} da cancellare
     */
    void cancellaZonaGeografica(ZonaGeografica zonaGeografica);

    /**
     * Carica, se esiste, la conversione unità di misura tra quella di origine e destinazione.
     *
     * @param unitaMisuraOrigine
     *            unita misura origine
     * @param unitaMisuraDestinazione
     *            unita misura destinazione
     * @return la conversioneUnitaMisura o null
     */
    ConversioneUnitaMisura caricaConversioneUnitaMisura(String unitaMisuraOrigine, String unitaMisuraDestinazione);

    /**
     * Carica tutte le conversioni unità di misura.
     *
     * @return {@link List} di {@link ConversioneUnitaMisura} caricate
     */
    List<ConversioneUnitaMisura> caricaConversioniUnitaMisura();

    /**
     * carica una lingua.
     *
     * @param lingua
     *            {@link Lingua} a caricare
     * @return {@link Lingua}
     */
    Lingua caricaLingua(Lingua lingua);

    /**
     * Carica le lingue per l'azienda corrente.
     *
     * @return {@link List} di {@link Lingua}
     */
    List<Lingua> caricaLingue();

    /**
     * Carica tutte le note anagrafica.
     *
     * @return note caricate
     */
    List<NotaAnagrafica> caricaNoteAnagrafica();

    /**
     * Carica tutte le unità di misura.
     *
     * @return {@link list} di {@link UnitaMisura} caricate
     */
    List<UnitaMisura> caricaUnitaMisura();

    /**
     *
     * @param codice
     *            parte del codice
     * @return lista um filtrate per desrizione.
     */
    List<UnitaMisura> caricaUnitaMisura(String codice);

    /**
     * Carica una unità di misura.
     *
     * @param unitaMisura
     *            {@link UnitaMisura} da caricare
     * @return {@link UnitaMisura} caricata
     */
    UnitaMisura caricaUnitaMisura(UnitaMisura unitaMisura);

    /**
     *
     * @param codice
     *            codice della valuta
     * @return unità di misura trovata, NULL se non trovata
     */
    UnitaMisura caricaUnitaMisuraByCodice(String codice);

    /**
     * Carica i {@link ZonaGeografica} presenti nell'anagrafica.<br/>
     *
     * @param valueSearch
     *            .
     * @param fieldSearch
     *            .
     *
     * @return lista dei {@link ZonaGeografica}
     */
    List<ZonaGeografica> caricaZoneGeografiche(String fieldSearch, String valueSearch);

    /**
     * Salva conversioneUnitaMisura.
     *
     * @param conversioneUnitaMisura
     *            la conversioneUnitaMisura da salvare
     * @return conversioneUnitaMisura salvata
     */
    ConversioneUnitaMisura salvaConversioneUnitaMisura(ConversioneUnitaMisura conversioneUnitaMisura);

    /**
     * Salva una lingua gestita dall'azienda.
     *
     * @param lingua
     *            : lingua da salvare
     * @return {@link Lingua}
     */

    Lingua salvaLingua(Lingua lingua);

    /**
     * Salva una {@link NotaAnagrafica}.
     *
     * @param notaAnagrafica
     *            nota da salvare
     * @return nota salvata
     */
    NotaAnagrafica salvaNotaAnagrafica(NotaAnagrafica notaAnagrafica);

    /**
     * Salva una unità di misura.
     *
     * @param unitaMisura
     *            <code>UnitaMisura</code> da salvare
     * @return <code>UnitaMisura</code> salvata
     */
    UnitaMisura salvaUnitaMisura(UnitaMisura unitaMisura);

    /**
     * Salva un record {@link ZonaGeografica}.
     *
     * @param zonaGeografica
     *            oggetto con i nuovi dati
     * @return {@link ZonaGeografica} salvato
     */
    ZonaGeografica salvaZonaGeografica(ZonaGeografica zonaGeografica);

}
