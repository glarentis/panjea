package it.eurotn.panjea.magazzino.manager.inventari;

import java.util.regex.Pattern;

public class FileInventarioTerminaleCOBAProcessor extends FileInventarioArticoliProcessor {

    @Override
    public String getBarcodeArticolo(String lineFile) {
        return lineFile.substring(0, 13);
    }

    @Override
    public String getCodiceArticolo(String lineFile) {
        return null;
    }

    @Override
    public String getImporto(String lineFile) {
        return null;
    }

    @Override
    public String getQuantita(String lineFile) {
        return lineFile.substring(13);
    }

    @Override
    public boolean test(String rigaFile) {
        boolean result = rigaFile != null && rigaFile.length() == 18;

        result = result && Pattern.matches("^[a-zA-Z0-9]*$", rigaFile);

        return result;
    }

}
