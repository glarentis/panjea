package it.eurotn.rich.report.editor.export.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.application.Application;

public class FileChooserExporter extends AbstractFileExporter {

    private Map<String, FileFilter> extensionFileFilter = null;

    {
        extensionFileFilter = new HashMap<String, FileFilter>();
        extensionFileFilter.put("pdf", new PDFFileFilter());
        extensionFileFilter.put("xls", new XLSFileFilter());
        extensionFileFilter.put("doc", new ODTFileFilter());
    }

    /**
     * Costruttore.
     *
     * @param exportFileName
     *            nome del file
     * @param exportExtension
     *            estensione del file
     */
    public FileChooserExporter(final String exportFileName, final String exportExtension) {
        super(exportFileName, exportExtension);
    }

    @Override
    public File getFile() {

        File fileWithExt = null;

        final JFileChooser fileChooser = new JFileChooser();
        // fileChooser.setSelectedFile(PanjeaSwingUtil.getHome().resolve(exportFileName).toFile());
        fileChooser.setDialogTitle("Seleziona il file per l'esportazione");
        fileChooser.addChoosableFileFilter(extensionFileFilter.get(exportExtension));
        if (!StringUtils.isBlank(exportFileName)) {
            fileChooser.setSelectedFile(new File(exportFileName + "." + exportExtension));
        }
        int returnVal = fileChooser.showOpenDialog(Application.instance().getActiveWindow().getControl());

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File localFile = fileChooser.getSelectedFile();
            fileWithExt = new File(localFile.getPath());

            if (!localFile.getName().endsWith(exportExtension)) {
                if (localFile.getName().endsWith("/.")) {
                    fileWithExt = new File(localFile.getPath() + exportExtension);
                } else {
                    fileWithExt = new File(localFile.getPath() + "." + exportExtension);
                }
            }
        }

        return fileWithExt;
    }

}
