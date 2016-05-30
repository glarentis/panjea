package it.eurotn.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.eurotn.panjea.exception.GenericException;

public class XlsUtils {

    private static final Logger LOGGER = Logger.getLogger(XlsUtils.class);

    private XSSFWorkbook workbook;

    /**
     * Costruttore.
     *
     * @param fileContent
     *            file content
     */
    public XlsUtils(final byte[] fileContent) {
        this(new ByteArrayInputStream(fileContent));
    }

    /**
     * Costruttore.
     *
     * @param file
     *            file
     * @throws FileNotFoundException
     *             sollevata se il file non esiste
     */
    public XlsUtils(final File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    /**
     * Costruttore.
     *
     * @param inputStream
     *            stream del file
     */
    private XlsUtils(final InputStream inputStream) {

        try {
            workbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento del file.", e);
            workbook = null;
        }
    }

    /**
     * Costruttore.
     *
     * @param filePath
     *            file path
     * @throws FileNotFoundException
     *             sollevata se il file non esiste
     */
    public XlsUtils(final String filePath) throws FileNotFoundException {
        this(new File(filePath));
    }

    /**
     * Restituisce il file CSV creato.
     *
     * @param separator
     *            separatore da utilizzare per creare il file
     * @return file creato
     */
    public File getCSV(String separator) {
        if (!isWorkbook()) {
            throw new GenericException("Il file di riferimento non è un workbook!");
        }
        File fileCSV = null;
        try {
            fileCSV = File.createTempFile("xlsToCsv", "csv");
        } catch (IOException e1) {
            LOGGER.error("--> errore durante la creazione del file CSV ", e1);
            throw new GenericException("errore durante la creazione del file CSV ", e1);
        }

        try (FileOutputStream output = new FileOutputStream(fileCSV)) {

            XSSFSheet sheet = workbook.getSheetAt(0);

            StringBuilder rowValue = new StringBuilder(200);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    rowValue.append(cell.getStringCellValue());

                    // switch (cell.getCellType()) {
                    // case Cell.CELL_TYPE_NUMERIC:
                    // if (DateUtil.isCellDateFormatted(cell)) {
                    // rowValue.append(DateFormatUtils.format(cell.getDateCellValue(), "dd/MM/yyyy"));
                    // } else {
                    // rowValue.append(String.valueOf(cell.getNumericCellValue()));
                    // }
                    // break;
                    // case Cell.CELL_TYPE_STRING:
                    // rowValue.append(cell.getStringCellValue());
                    // break;
                    // case Cell.CELL_TYPE_BOOLEAN:
                    // rowValue.append(cell.getBooleanCellValue() ? "Si" : "No");
                    // break;
                    // default:
                    // rowValue.append("");
                    // break;
                    // }

                    if (cellIterator.hasNext()) {
                        rowValue.append(separator);
                    }
                }
                IOUtils.write(rowValue.toString() + (rowIterator.hasNext() ? "\n" : ""), output);
                rowValue.setLength(0);
            }
        } catch (Exception e) {
            LOGGER.error("--> errore durante la scritture del file CSV.", e);
            throw new GenericException("errore durante la scritture del file CSV.", e);
        }

        return fileCSV;
    }

    /**
     * @return <code>true</code> se si tratta di un file xls
     */
    public boolean isWorkbook() {
        return workbook != null;
    }
}
