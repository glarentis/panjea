package it.eurotn.rich.report.editor.export.file;

import java.io.File;

public class XLSFileFilter extends javax.swing.filechooser.FileFilter {
    @Override
    public boolean accept(File file) {

        if (file.isDirectory()) {
            return true;
        }

        String filename = file.getName();
        return filename.endsWith(".xls");
    }

    @Override
    public String getDescription() {
        return "*.xls";
    }
}