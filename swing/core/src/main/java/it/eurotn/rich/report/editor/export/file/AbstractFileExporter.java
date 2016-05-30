package it.eurotn.rich.report.editor.export.file;

import java.io.File;

public abstract class AbstractFileExporter {

    protected String exportFileName;
    protected String exportExtension;

    /**
     * Costruttore.
     *
     * @param exportFileName
     *            nome del file
     * @param exportExtension
     *            estensione del file
     */
    public AbstractFileExporter(final String exportFileName, final String exportExtension) {
        super();
        if (exportFileName != null) {
            this.exportFileName = exportFileName.replaceAll(" ", "").replaceAll("/", "_");
        }
        this.exportExtension = exportExtension;
    }

    /**
     * Restituisce il file da esportare.
     *
     * @return file
     */
    public abstract File getFile();
}
