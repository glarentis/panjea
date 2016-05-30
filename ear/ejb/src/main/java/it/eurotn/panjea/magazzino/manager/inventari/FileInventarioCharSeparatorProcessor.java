package it.eurotn.panjea.magazzino.manager.inventari;

public class FileInventarioCharSeparatorProcessor extends FileInventarioArticoliProcessor {

    private String charSeparator = "\t";

    /**
     * Costruttore.
     *
     * @param separator
     *            separatore
     */
    public FileInventarioCharSeparatorProcessor(final String separator) {
        super();
        this.charSeparator = separator;
    }

    @Override
    public String getBarcodeArticolo(String lineFile) {
        return null;
    }

    @Override
    public String getCodiceArticolo(String lineFile) {
        return lineFile.split(charSeparator)[0];
    }

    @Override
    public String getImporto(String lineFile) {

        // l'importo potrebbe anche non essere presente
        String[] split = lineFile.split(charSeparator);

        return split.length > 2 ? split[2] : null;
    }

    @Override
    public String getQuantita(String lineFile) {
        return lineFile.split(charSeparator)[1];
    }

    @Override
    public boolean test(String rigaFile) {
        return rigaFile != null && rigaFile.contains(charSeparator);
    }

}
