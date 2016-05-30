package it.eurotn.panjea.dms.manager.transfer;

import java.io.File;

import javax.ejb.Local;

@Local
public interface LogicalDocEjbTransferFile {

    /**
     * @param idDoc
     *            id documento logicaldoc
     * @return contenuto del file
     */
    File getContent(long idDoc);

    /**
     * @param filePath
     *            nome del file da pubblicare
     * @return uuid utilizzato per questo upload
     */
    String putContent(String filePath);

    /**
     * @param fileName
     *            nome del file da pubblicare
     * @param fileContent
     *            file content
     * @return uuid utilizzato per questo upload
     */
    String putContent(String fileName, byte[] fileContent);
}
