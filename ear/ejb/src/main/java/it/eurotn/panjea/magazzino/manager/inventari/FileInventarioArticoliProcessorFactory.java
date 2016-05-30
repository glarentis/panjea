package it.eurotn.panjea.magazzino.manager.inventari;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.exception.GenericException;

public final class FileInventarioArticoliProcessorFactory {

    private static final Logger LOGGER = Logger.getLogger(FileInventarioArticoliProcessorFactory.class);

    private static List<FileInventarioArticoliProcessor> processors;

    static {
        processors = new ArrayList<FileInventarioArticoliProcessor>();
        processors.add(new FileInventarioTerminaleCOBAProcessor());
        processors.add(new FileInventarioCharSeparatorProcessor("\t"));
        processors.add(new FileInventarioCharSeparatorProcessor("#"));
    }

    /**
     * Costruttore.
     */
    private FileInventarioArticoliProcessorFactory() {
        super();
    }

    /**
     * Processo da utilizzare per gestire il file.
     *
     * @param fileImport
     *            file da importare
     * @return processor
     */
    public static FileInventarioArticoliProcessor getProcessor(File fileImport) {

        String line = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileImport))) {
            line = br.readLine();
        } catch (IOException e) {
            throw new GenericException("Errore durante la lettura del file");
        }

        if (line != null) {
            for (FileInventarioArticoliProcessor processor : processors) {
                if (processor.test(line)) {
                    return processor;
                }
            }
        }

        return null;
    }
}
