package it.eurotn.rich.control.table.command;

import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JTable;
import javax.swing.JTable.PrintMode;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import it.eurotn.rich.control.table.JecTablePrintable;

public class ExportPDFCommand extends AbstractExportTableCommand {

    public static final String COMMAND_ID = "exportPDFCommand";

    /**
     * Costruttore.
     * 
     * @param table
     *            tabella da esportare
     */
    public ExportPDFCommand(final JTable table) {
        super(COMMAND_ID, "pdf", table);
    }

    @Override
    public boolean export(File fileToExport) {
        int orientation = PageFormat.PORTRAIT;
        JecTablePrintable tp = new JecTablePrintable(getTable(), PrintMode.FIT_WIDTH, getTableHeader());
        JecTablePrintable tpClone = new JecTablePrintable(getTable(), PrintMode.FIT_WIDTH, getTableHeader());

        Rectangle pageSize = PageSize.A4.rotate();

        // We will count the number of pages that have to be printed
        int numberOfPages = 0;

        try {
            // We will print the document twice, once to count the number of pages and once for real
            for (int i = 0; i < 2; i++) {

                // First time, use the printable, second time, use the clone
                Printable usedPrintable = i == 0 ? tp : tpClone;

                FileOutputStream bos = new FileOutputStream(fileToExport);

                // This determines the size of the pdf document
                Document document = new Document(pageSize);
                PdfWriter writer = PdfWriter.getInstance(document, bos);
                document.open();
                PdfContentByte contentByte = writer.getDirectContent();

                // These lines do not influence the pdf document, but are there to tell the Printable how to print
                double a4HeightInch = 8.26771654; // Equals 210mm
                double a4WidthInch = 11.6929134; // Equals 297mm
                Paper paper = new Paper();
                paper.setSize(a4WidthInch * 72, a4HeightInch * 72); // 72 DPI
                paper.setImageableArea(20, 20, a4WidthInch * 72 - 40, a4HeightInch * 72 - 20); // 1 inch margins
                PageFormat pageFormat = new PageFormat();
                pageFormat.setPaper(paper);
                pageFormat.setOrientation(orientation);

                float width = (float) pageFormat.getWidth();
                float height = (float) pageFormat.getHeight();

                // First time, don't use numberOfPages, since we don't know it yet
                for (int j = 0; j < numberOfPages || i == 0; j++) {
                    @SuppressWarnings("deprecation")
                    Graphics2D g2d = contentByte.createGraphics(width, height);
                    int pageReturn = usedPrintable.print(g2d, pageFormat, j);
                    g2d.dispose();

                    // The page that we just printed, actually existed; we only know this afterwards
                    if (pageReturn == Printable.PAGE_EXISTS) {
                        // We have to create a newPage for the next page, even if we don't yet know if it exists, hence
                        // the second run where we do know
                        document.newPage();
                        // First run, count the pages
                        if (i == 0) {
                            numberOfPages++;
                        }
                    } else {
                        break;
                    }
                }
                document.close();
                writer.close();
            }
        } catch (Exception e) {
            // We expect no Exceptions, so any Exception that occurs is a technical one and should be a
            // RuntimeException
            return false;
        }

        return true;
    }
}
