package it.eurotn.panjea.fatturepa.rich.bd;

import java.util.List;

import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.AreaNotificaFatturaPA;
import it.eurotn.panjea.fatturepa.manager.exception.XMLCreationException;
import it.eurotn.panjea.fatturepa.util.AreaMagazzinoFatturaPARicerca;
import it.eurotn.panjea.fatturepa.util.AreaNotificaFatturaPADTO;
import it.eurotn.panjea.fatturepa.util.ParametriRicercaFatturePA;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

/**
 * @author fattazzo
 *
 */
public interface IFatturePABD {

    /**
     * Cancella l'XML e l'XML firmato dell'area magazzino. Il prograssivo legato all'XML creato viene mantenuto per
     * essere utilizzato su quello che verrà generato.
     *
     * @param idAreaMagazzino
     *            id area magazzino
     */
    void cancellaXMLFatturaPA(Integer idAreaMagazzino);

    /**
     * Carica la {@link AreaMagazzinoFatturaPA} dell'area magazzino specificata. Ne crea una nuova se non esiste
     *
     * @param idAreaMagazzino
     *            id area magazzino
     * @return area magazzino fattura PA caricata
     */
    AreaMagazzinoFatturaPA caricaAreaMagazzinoFatturaPA(Integer idAreaMagazzino);

    /**
     * Carica l'{@link AreaNotificaFatturaPA} richiesta.
     *
     * @param id
     *            id area
     * @return {@link AreaNotificaFatturaPA} caricata
     */
    AreaNotificaFatturaPA caricaAreaNotificaFatturaPA(Integer id);

    /**
     * Carica tutte le notifiche presenti per l'{@link AreaMagazzinoFatturaPA}.
     *
     * @param idAreaMagazzinoFatturaPA
     *            id dell'area magazzino fattura pa
     * @return notifiche presenti
     */
    List<AreaNotificaFatturaPADTO> caricaAreaNotificheFatturaPA(Integer idAreaMagazzinoFatturaPA);

    /**
     * Dall'XML passato ocme parametro viene fatto l'unmarshalling e creata la fattura elettronica type.
     *
     * @param xmlContent
     *            contenuto dell'xml
     * @return fattura creata
     */
    IFatturaElettronicaType caricaFatturaElettronicaType(String xmlContent);

    /**
     * Crea l'xml della fattura elettronica PA per l'area magazzino. Verrà creato solo se la fattura non è provvisoria e
     * non ne ha uno già creato.
     *
     * @param idAreaMagazzino
     *            id aree
     * @return xml creato
     * @throws XMLCreationException
     *             sollevata se ci sono errori durante la creazione dell'XML
     */
    AreaMagazzinoFatturaPA creaXMLFattura(Integer idAreaMagazzino) throws XMLCreationException;

    /**
     * Crea l'xml della fattura PA dell'area specificata utilizzando solo i dati presenti nell'
     * {@link IFatturaElettronicaType}. L'eventuale xml precedente verrà cancellato.
     *
     * @param idAreaMagazzino
     *            area magazzino di cui creare l'xml
     * @param fatturaElettronicaType
     *            dati con i quali creare l'XML
     * @return area magazzino
     * @throws XMLCreationException
     *             sollevata se ci sono errori durante la creazione dell'XML
     */
    AreaMagazzinoFatturaPA creaXMLFatturaPA(Integer idAreaMagazzino, IFatturaElettronicaType fatturaElettronicaType)
            throws XMLCreationException;

    /**
     * Scarica il file XML firmato con il nome specificato.
     *
     * @param fileName
     *            nome del file xml
     * @return file
     */
    byte[] downloadXMLFirmato(String fileName);

    /**
     * Invia la fattura PA al SdI.
     *
     * @param idAreaMagazzino
     *            id area magazzino
     *
     */
    void invioSdiFatturaPA(Integer idAreaMagazzino);

    /**
     * Esegue la ricerca di tutte le fatture PA con i parametri specificati.
     *
     * @param parametri
     *            parametri di ricerca
     * @return fatture trovate
     */
    List<AreaMagazzinoFatturaPARicerca> ricercaFatturePA(ParametriRicercaFatturePA parametri);

    /**
     * Salva una {@link AreaMagazzinoFatturaPA}.
     *
     * @param areaMagazzinoFatturaPA
     *            area da salvare
     * @return area salvata
     */
    AreaMagazzinoFatturaPA salvaAreaMagazzinoFatturaPA(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA);

    /**
     * Aggiunge la notifica "Inviata" alla fattura PA.
     *
     * @param idAreaMagazzino
     *            id area magazzino
     */
    void salvaFatturaPAComeInviata(Integer idAreaMagazzino);

    /**
     * Salva l'XML firmato associato all'area magazzino.
     *
     * @param idAreaMagazzino
     *            id area
     * @param xmlContent
     *            contenuto dell'xml
     * @param xmlFileName
     *            nome del file xml ( nome + estensione)
     */
    void salvaXMLFatturaFirmato(Integer idAreaMagazzino, byte[] xmlContent, String xmlFileName);
}
