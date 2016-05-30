package it.eurotn.panjea.magazzino.manager.inventari;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public abstract class FileInventarioArticoliProcessor {

    private String decimalSeparator = ".";
    private String fieldSeparator = "|";

    /**
     * Crea la riga formattata correttamente per essere importata nell'inventario.
     *
     * @param codiceArticolo
     *            codice articolo
     * @param barcodeArticolo
     *            barcode articolo
     * @param quantita
     *            quantità
     * @param importo
     *            importo
     * @return riga creata
     */
    protected String createLine(String codiceArticolo, String barcodeArticolo, String quantita, String importo) {
        StringBuilder sb = new StringBuilder();

        if (!StringUtils.isBlank(codiceArticolo)) {
            sb.append(codiceArticolo);
        }
        sb.append(fieldSeparator);

        if (!StringUtils.isBlank(barcodeArticolo)) {
            sb.append(barcodeArticolo);
        }

        sb.append(fieldSeparator);

        // se quantità e importo hanno la virgola la sostituisco con il punto per poterli importare
        sb.append(quantita.replaceAll(",", decimalSeparator));

        sb.append(fieldSeparator);
        if (!StringUtils.isBlank(importo)) {
            sb.append(importo.replaceAll(",", decimalSeparator));
        }

        return sb.toString();
    }

    /**
     * Barcode articolo estratto dalla riga del file.
     *
     * @param lineFile
     *            riga
     * @return barcode
     */
    public abstract String getBarcodeArticolo(String lineFile);

    /**
     * Codice articolo estratto dalla riga del file.
     *
     * @param lineFile
     *            riga
     * @return codice articolo
     */
    public abstract String getCodiceArticolo(String lineFile);

    /**
     * Importo estratto dalla riga del file.
     *
     * @param lineFile
     *            riga
     * @return importo
     */
    public abstract String getImporto(String lineFile);

    /**
     * Quantità estratta dalla riga del file.
     *
     * @param lineFile
     *            riga
     * @return quantità
     */
    public abstract String getQuantita(String lineFile);

    /**
     * Processa il file poter essere importato.
     *
     * @param fileToProcess
     *            file
     */
    public void process(File fileToProcess) {

        List<String> lines = new ArrayList<String>();

        // leggo il file e ricreo le righe
        try (BufferedReader br = new BufferedReader(new FileReader(fileToProcess))) {
            String strLine;
            while ((strLine = br.readLine()) != null) {
                String codiceArticolo = getCodiceArticolo(strLine);
                String barcodeArticolo = getBarcodeArticolo(strLine);
                String quantita = getQuantita(strLine);
                String importo = getImporto(strLine);

                lines.add(createLine(codiceArticolo, barcodeArticolo, quantita, importo));
            }
        } catch (IOException e) {
            throw new RuntimeException("Errore durante la lettura del file di importazione", e);
        }

        // sovrascrivo il file con le righe formattate
        writeFileImportazione(fileToProcess, lines);
    }

    /**
     * Esegue il test per verificare se la riga del file è del formato gestito.
     *
     * @param rigaFile
     *            riga file
     * @return <code>true</code> se la riga può essere gestita
     */
    public abstract boolean test(String rigaFile);

    /**
     * Inserisce tutte le righe nel file specificato.
     *
     * @param file
     *            file
     * @param lines
     *            righe
     */
    private void writeFileImportazione(File file, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.append(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException exception) {
            System.out.println("Errore durante la scrittura del file di importaizone");
        }
    }

}
