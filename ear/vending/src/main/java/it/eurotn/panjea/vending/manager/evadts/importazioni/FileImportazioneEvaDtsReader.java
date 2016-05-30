package it.eurotn.panjea.vending.manager.evadts.importazioni;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.manager.evadts.exception.ImportazioneEvaDtsException;
import it.eurotn.panjea.vending.manager.evadts.importazioni.handler.DXSEvaDtsHandler;
import it.eurotn.panjea.vending.manager.evadts.importazioni.handler.EvaDtsStandardHandler;
import it.eurotn.panjea.vending.manager.evadts.importazioni.parser.LetturaEvaDtsParser;

public class FileImportazioneEvaDtsReader {

    private static final Logger LOGGER = Logger.getLogger(FileImportazioneEvaDtsReader.class);

    private byte[] fileContent;
    private String fileName;
    private EvaDtsImportFolder evaDtsImportFolder;

    private File file;

    /**
     * Costruttore.
     *
     * @param fileName
     *            nome del file
     * @param fileContent
     *            contenuto del file
     * @param evaDtsImportFolder
     *            import folder
     */
    public FileImportazioneEvaDtsReader(final String fileName, final byte[] fileContent,
            final EvaDtsImportFolder evaDtsImportFolder) {
        super();
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.evaDtsImportFolder = evaDtsImportFolder;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    private List<LetturaEvaDtsParser> getParser(String[] righe) throws ImportazioneEvaDtsException {
        List<LetturaEvaDtsParser> parsers = new ArrayList<>();

        EvaDtsStandardHandler evaDtsHandler = new DXSEvaDtsHandler();
        LetturaEvaDtsParser parser = null;
        do {
            int rigaInizio = parser != null ? parser.getUltimaRiga() + 1 : 0;
            parser = evaDtsHandler.handleRighe(righe, rigaInizio);

            if (parser != null) {
                parser.setEvaDtsImportFolder(evaDtsImportFolder);
                parsers.add(parser);
            }
        } while (parser != null && ((parser.getUltimaRiga() + 1) < righe.length));

        return parsers;
    }

    /**
     * Rilevazioni EVA DTS generate.
     *
     * @return rilevazioni
     * @throws ImportazioneEvaDtsException
     *             sollevata in caso di errore di creazione delle rilevazioni
     */
    public RilevazioneEvaDts[] getRilevazioniEvaDts() throws ImportazioneEvaDtsException {

        String[] righeFile = readFile();
        List<LetturaEvaDtsParser> parsers = getParser(righeFile);
        RilevazioneEvaDts[] rilevazioni = new RilevazioneEvaDts[] {};

        for (LetturaEvaDtsParser parser : parsers) {
            RilevazioneEvaDts rilevazione = parser.getRilevazioneEvaDts();
            rilevazioni = ArrayUtils.add(rilevazioni, rilevazione);
        }

        return rilevazioni;
    }

    @SuppressWarnings("unchecked")
    private String[] readFile() throws ImportazioneEvaDtsException {
        List<String> lines = null;
        file = null;
        try {
            file = File.createTempFile("letturaEvaDts", ".tmp");
            FileUtils.writeByteArrayToFile(file, fileContent);

            lines = FileUtils.readLines(file);
            // tolgo tutte le tighe vuote
            lines.removeAll(Collections.singletonList(""));
        } catch (Exception e) {
            LOGGER.error("--> Errore durante la lettura delle rilevazioni EVA DTS del file " + fileName, e);
            throw new ImportazioneEvaDtsException(
                    "Errore durante la lettura delle rilevazioni EVA DTS del file " + fileName, e);
        }

        return lines.toArray(new String[lines.size()]);
    }

}
