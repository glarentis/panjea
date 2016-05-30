package it.eurotn.panjea.dms.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import com.logicaldoc.webservice.document.WSDocument;

import it.eurotn.panjea.dms.domain.DmsSettings;
import it.eurotn.panjea.dms.exception.DMSLoginException;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;

@Remote
public interface DMSService {

    /**
     *
     * @param folderPath
     *            cartella di logicaldoc di destinazione
     * @param documento
     *            metadati del documento da pubblicare. il filename indica il nome del file
     *            precedentemente scaricato con l'uploadServlet
     * @throws DMSLoginException
     *             sollevata se viene generato un problema durante il login
     */
    void addAllegato(String folderPath, WSDocument documento) throws DMSLoginException;

    /**
     *
     * @param uuid
     *            uuid utilizzato per pubblicare nella servlet uploadServlet
     * @param documento
     *            metadati del documento da pubblicare. il filename indica il nome del file
     *            precedentemente scaricato con l'uploadServlet
     * @param allegatoDms
     *            attributi dell'allegato
     * @throws DMSLoginException
     *             sollevata se viene generato un problema durante il login
     */
    void addAllegato(String uuid, WSDocument documento, AllegatoDMS allegatoDms) throws DMSLoginException;

    /**
     * Carica i settings presenti.
     *
     * @return {@link DmsSettings}
     */
    DmsSettings caricaDmsSettings();

    /**
     * Cancella un documento dal dms.
     *
     * @param idAllegato
     *            id documento da cancellare
     * @throws DMSLoginException
     *             sollevata se viene generato un problema durante il login
     */
    void deleteAllegato(long idAllegato) throws DMSLoginException;

    /**
     *
     * @param allegatoDMS
     *            attributi dell'allegato
     * @return allegati del documento
     * @throws DMSLoginException
     *             sollevata se viene generato un problema durante il login
     */
    List<WSDocument> getAllegati(AllegatoDMS allegatoDMS) throws DMSLoginException;

    /**
     *
     * @param idAllegato
     *            id documento di logicalDoc
     * @return immagine tile
     * @throws DMSLoginException
     *             sollevata se viene generato un problema durante il login
     */
    byte[] getAllegatoTile(long idAllegato) throws DMSLoginException;

    /**
     * Salva un {@link DmsSettings}.
     *
     * @param dmsSettings
     *            settings da salvare
     * @return settings salvate
     */
    DmsSettings salvaDmsSettings(DmsSettings dmsSettings);

}
