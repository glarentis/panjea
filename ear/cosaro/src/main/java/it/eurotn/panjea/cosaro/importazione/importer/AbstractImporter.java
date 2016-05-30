package it.eurotn.panjea.cosaro.importazione.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.beanio.BeanReaderErrorHandler;
import org.beanio.BeanReaderException;

public abstract class AbstractImporter implements ImporterCosaro {
    protected class FileReaderErrorHandler implements BeanReaderErrorHandler {

        /**
         * Costruttore.
         */
        public FileReaderErrorHandler() {
            super();
        }

        @Override
        public void handleError(BeanReaderException ex) throws Exception {
            logger.error("-->errore nel leggere il file ", ex);
            throw new RuntimeException(ex);
        }
    }

    private static Logger logger = Logger.getLogger(AbstractImporter.class);

    protected static final String CODICE_ATTRIBUTO_CASSE = "Casse";
    protected static final String CODICE_ATTRIBUTO_PEZZI = "Pezzi";

    /**
     * Legge la prima riga del file.
     * 
     * @param file
     *            file da leggere
     * @return prima riga del file
     */
    protected String leggiPrimaRiga(File file) {
        BufferedReader in = null;
        try {
            FileReader reader = new FileReader(file);
            in = new BufferedReader(reader);
            String linea = in.readLine();
            if (linea == null) {
                linea = "";
            }
            return linea;
        } catch (Exception e) {
            logger.error("-->errore nel leggere la prima riga del file " + file.getAbsolutePath(), e);
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("-->errore nel chiudere il file " + file.getAbsolutePath(), e);
                }
            }
        }
    }
}
