package it.eurotn.panjea.rich.stampe;

import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager.TipoLayoutPrefefinito;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;

import java.util.List;
import java.util.Properties;

public interface ILayoutStampeManager {

    /**
     * Carica tutti i layout di stampa batch per il tipo area specificato. Se viene specificata anche l'entità verranno
     * aggiunti anche i layout bach personalizzati per l'entità.
     *
     * @param tipoAreaDocumento
     *            tipo area specificato
     * @param entita
     *            entita
     * @param sedeEntita
     *            sede entità
     * @return layouts caricati
     */
    List<LayoutStampaDocumento> caricaLayoutStampaBatch(ITipoAreaDocumento tipoAreaDocumento, EntitaLite entita,
            SedeEntita sedeEntita);

    /**
     *
     * @return lista di layoutManager associati a dei documenti
     */
    List<LayoutStampaDocumento> caricaLayoutStampaPerDocumenti();

    /**
     * Carica tutti i {@link LayoutStampa} definiti.<br>
     *
     * @return layout caricati
     */
    List<LayoutStampa> caricaLayoutStampe();

    /**
     * Carica tutti i {@link LayoutStampa} personalizzati per l'entità.<br>
     *
     * @param idEntita
     *            id entità
     * @return layout caricati
     */
    List<LayoutStampaDocumento> caricaLayoutStampe(Integer idEntita);

    /**
     *
     * @param tipoAreaDocumento
     *            tipoAreaDocumento
     * @param entita
     *            entita
     * @param sedeEntita
     *            sede
     * @return layout caricati
     */
    List<LayoutStampaDocumento> caricaLayoutStampe(ITipoAreaDocumento tipoAreaDocumento, EntitaLite entita,
            SedeEntita sedeEntita);

    /**
     *
     * @param reportPath
     *            path del report
     * @return layoutDiStampa per il report
     */
    LayoutStampa caricaLayoutStampe(String reportPath);

    /**
     * Restituisce il layout predefinito fra quelli passati come parametro. I layouts devono essere ordinati secondo
     * l'ordine di default per sede entità, entita e predefinito.
     *
     * @param layouts
     *            layouts
     * @param tipoLayoutPrefefinito
     *            tipo di layout predefinito da utilizzare
     * @return layout predefinito, <code>null</code> se non esiste
     */
    LayoutStampaDocumento getLayoutStampaPredefinito(List<LayoutStampaDocumento> layouts,
            TipoLayoutPrefefinito tipoLayoutPrefefinito);

    /**
     * Carica il contenuto del file di configurazione delle stampanti per l'utente.
     *
     * @return file di configurazione caricato
     */
    Properties loadPrinterConfigFile();

    /**
     * Salva sul file di configurazione l'associazione fre il layout e stampante specificati.
     *
     * @param nomeLayout
     *            nome del layout
     * @param nomeStampante
     *            nome della stampante
     */
    void salvaAssociazioneStampante(String nomeLayout, String nomeStampante);

    /**
     * Setta il layout di stampa come predefinito. Restituisce tutti i layout configurati per il tipo area di
     * riferimento. (Oltre al layout impostato come predefinito anche il predefinito precedente viene modificato)
     *
     * @param layoutStampa
     *            layout da impostare come predefinito
     * @return layouts
     */
    List<LayoutStampaDocumento> setLayoutAsDefault(LayoutStampaDocumento layoutStampa);

    /**
     * Setta il layout di stampa come predefinito per l'invio delle email. Restituisce tutti i layout configurati per il
     * tipo area di riferimento. (Oltre al layout impostato come predefinito anche il predefinito precedente viene
     * modificato)
     *
     * @param layoutStampa
     *            layout da impostare come predefinito per l'invio email
     * @return layouts
     */
    List<LayoutStampaDocumento> setLayoutForInvioMail(LayoutStampaDocumento layoutStampa);

    /**
     * Setta il layout di stampa come ad uso interno. Restituisce tutti i layout configurati per il tipo area di
     * riferimento. (Oltre al layout impostato come predefinito anche il predefinito precedente viene modificato)
     *
     * @param layoutStampa
     *            layout da impostare ad uso interno
     * @param usoInterno
     *            <code>true</code> per settarlo come uso interno, <code>false</code> altrimenti
     * @return layouts
     */
    List<LayoutStampaDocumento> setLayoutForUsoInterno(LayoutStampaDocumento layoutStampa, boolean usoInterno);
}
