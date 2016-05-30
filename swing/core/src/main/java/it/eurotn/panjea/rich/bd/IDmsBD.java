package it.eurotn.panjea.rich.bd;

import java.io.File;
import java.util.List;

import com.logicaldoc.webservice.document.WSDocument;

import it.eurotn.panjea.dms.domain.DmsSettings;
import it.eurotn.panjea.dms.exception.DMSLoginException;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;

public interface IDmsBD {

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
     *            id allegato da cancellare
     */
    void deleteAllegato(long idAllegato);

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
     *            id allegato di logicalDoc
     * @return immagine tile
     */
    byte[] getAllegatoTile(long idAllegato);

    /**
     * @param idAllegato
     *            id allegato
     * @return file con il contenuto del documento
     */
    File getContent(long idAllegato);

    /**
     * @param idAllegato
     *            id allegato
     * @return file
     */
    byte[] getContentByte(long idAllegato);

    /**
     *
     * @param folderPath
     *            cartella di logicaldoc di destinazione
     * @param fileName
     *            nome del file da pubblicare
     * @param title
     *            title del documento da pubblicare.
     */
    void pubblicaAllegato(String folderPath, String fileName, String title);

    /**
     *
     * @param folderPath
     *            cartella di logicaldoc di destinazione
     * @param fileName
     *            nome del file da pubblicare
     * @param titolo
     *            title del documento da pubblicare.
     * @param allegatoDms
     *            allegato dell'articolo da pubblciare
     */
    void pubblicaAllegato(String folderPath, String fileName, String titolo, AllegatoDMS allegatoDms);

    /**
     * Salva un {@link DmsSettings}.
     *
     * @param dmsSettings
     *            settings da salvare
     * @return settings salvate
     */
    DmsSettings salvaDmsSettings(DmsSettings dmsSettings);
}
