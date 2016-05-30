package it.eurotn.panjea.dms.manager.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.logicaldoc.webservice.document.WSDocument;

import it.eurotn.panjea.dms.exception.DMSLoginException;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;

@Local
public interface DMSAllegatoManager {

    /**
     *
     * @param uuid
     *            uuid utilizzato per pubblicare nella servlet uploadServlet
     * @param documento
     *            metadati del documento da pubblicare. il filename indica il nome del file
     *            precedentemente scaricato con l'uploadServlet
     * @throws DMSLoginException
     *             sollevata se viene generato un problema durante il login
     */
    void addAllegato(String uuid, WSDocument documento) throws DMSLoginException;

    /**
     *
     * @param uuid
     *            uuid utilizzato per pubblicare nella servlet uploadServlet
     * @param documento
     *            metadati del documento da pubblicare. il filename indica il nome del file
     *            precedentemente scaricato con l'uploadServlet
     * @param allegatoDMS
     *            attributi da associare all'allegato.
     * @throws DMSLoginException
     *             sollevata se viene generato un problema durante il login
     */
    void addAllegato(String uuid, WSDocument documento, AllegatoDMS allegatoDMS) throws DMSLoginException;

    /**
     * Cancella un documento dal dms.
     *
     * @param idAllegato
     *            id allegato da cancellare
     * @throws DMSLoginException
     *             sollevata se viene generato un problema durante il login
     */
    void deleteAllegato(long idAllegato) throws DMSLoginException;

    /**
     *
     * @param attributiAllegatoDMS
     *            attributi dell'allegato
     * @return allegati del documento
     * @throws DMSLoginException
     *             sollevata se viene generato un problema durante il login
     */
    List<WSDocument> getAllegati(AllegatoDMS attributiAllegatoDMS) throws DMSLoginException;

    /**
     *
     * @param idAllegato
     *            id allegato di logicalDoc
     * @return immagine tile
     * @throws DMSLoginException
     *             sollevata se viene generato un problema durante il login
     */
    byte[] getAllegatoTile(long idAllegato) throws DMSLoginException;

    void updateDocuments(List<WSDocument> documentiDaAggiornare, AllegatoDMS allegatoDms, String folder, Date data)
            throws DMSLoginException;

}
