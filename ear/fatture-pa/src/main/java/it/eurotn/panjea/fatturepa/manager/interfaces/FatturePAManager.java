package it.eurotn.panjea.fatturepa.manager.interfaces;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.AreaNotificaFatturaPA;
import it.eurotn.panjea.fatturepa.manager.exception.XMLCreationException;
import it.eurotn.panjea.fatturepa.util.AreaMagazzinoFatturaPARicerca;
import it.eurotn.panjea.fatturepa.util.AreaNotificaFatturaPADTO;
import it.eurotn.panjea.fatturepa.util.ParametriRicercaFatturePA;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

@Local
public interface FatturePAManager {

    /**
     * Cancella l' {@link AreaMagazzinoFatturaPA} dell'area magazzino.
     *
     * @param idAreaMagazzino
     *            id area magazzino
     */
    void cancellaAreaMagazzinoFatturaPA(Integer idAreaMagazzino);

    /**
     * Cancella l'XML e l'XML firmato dell'area magazzino. Il prograssivo legato all'XML creato viene mantenuto per
     * essere utilizzato su quello che verrà generato.
     *
     * @param idAreaMagazzino
     *            id area magazzino
     * @throws PreferenceNotFoundException
     *             sollevata se la preferenza per la directory di salvataggio degli allegati non è configurata
     */
    void cancellaXMLFatturaPA(Integer idAreaMagazzino) throws PreferenceNotFoundException;

    /**
     * Carica l'area magazzino fattura PA.
     * 
     * @param identificativoSdI
     *            identificativo SDI
     * @param xmlFileName
     *            nome file xml
     * @return area caricata
     */
    AreaMagazzinoFatturaPA caricaAreaMagazzinoFatturaPA(BigInteger identificativoSdI, String xmlFileName);

    /**
     * Carica la {@link AreaMagazzinoFatturaPA} dell'area magazzino specificata. Ne crea una nuova se non esiste
     *
     * @param idAreaMagazzino
     *            id area magazzino
     * @return area magazzino fattura PA caricata
     */
    AreaMagazzinoFatturaPA caricaAreaMagazzinoFatturaPA(Integer idAreaMagazzino);

    /**
     * Carica tutte le {@link AreaMagazzinoFatturaPA} che non hanno ancora eseguito la conservazione sostitutiva.
     *
     * @return aree
     */
    List<AreaMagazzinoFatturaPA> caricaAreaMagazzinoFatturaPANonConservate();

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
     * Carica tutte le fatture PA in cui ancora non esiste un esito di chiusura ( notifica di scarto, ecc.. ).
     *
     * @return lista delle {@link AreaMagazzinoFatturaPA} aperte
     */
    List<AreaMagazzinoFatturaPA> caricaFatturePAAperte();

    /**
     * Verifica se esiste una notifica generata da una mail con l'id specificato.
     *
     * @param messageID
     *            id messaggio
     * @return <code>true</code> se esiste
     */
    boolean checkEmailMessageIDNotifica(String messageID);

    /**
     * Crea l'xml della fattura PA dell'area specificata.
     *
     * @param idAreaMagazzino
     *            area magazzino di cui creare l'xml
     * @return xml creato
     * @throws XMLCreationException
     *             sollevata se ci sono errori durante la creazione dell'XML
     */
    AreaMagazzinoFatturaPA creaXMLFatturaPA(Integer idAreaMagazzino) throws XMLCreationException;

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
     * @throws PreferenceNotFoundException
     *             sollevata se la preferenza per la directory di salvataggio degli allegati non è configurata
     */
    AreaMagazzinoFatturaPA creaXMLFatturaPA(Integer idAreaMagazzino, IFatturaElettronicaType fatturaElettronicaType)
            throws XMLCreationException, PreferenceNotFoundException;

    /**
     * Scarica il file XML firmato con il nome specificato.
     *
     * @param fileName
     *            nome del file xml
     * @return file
     * @throws PreferenceNotFoundException
     *             sollevata se la preferenza per la directory di salvataggio degli allegati non è configurata
     */
    byte[] downloadXMLFirmato(String fileName) throws PreferenceNotFoundException;

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
     * @throws PreferenceNotFoundException
     *             sollevata se la preferenza per la directory di salvataggio degli allegati non è configurata
     */
    void salvaXMLFatturaFirmato(Integer idAreaMagazzino, byte[] xmlContent, String xmlFileName)
            throws PreferenceNotFoundException;
}
