package it.eurotn.rich.control.table.command;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;
import javax.swing.SwingWorker;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;

import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.components.messagealert.ProgressBarMessageAlert;
import it.eurotn.rich.control.table.JecTablePrintable;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.report.JecReport;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;

public class OpenTablePreviewTask extends SwingWorker<Void, Integer> {

    private ProgressBarMessageAlert exportMessageAlert;

    private JTable table;
    private ITableHeader tableHeader;

    private PageFormat pageFormat;

    private int numberOfPages = 0;

    private IndexColorModel indexColorModel;

    /**
     * Costruttore.
     *
     * @param table
     *            tabella di cui fare il preview
     * @param tableHeader
     *            header della tabella
     */
    public OpenTablePreviewTask(final JTable table, final ITableHeader tableHeader) {
        super();
        this.table = table;
        this.tableHeader = tableHeader;

        init();

        // tolgo il focus alla tabella altrimenti usando le frecce è possibile navigarci e espandere/collassare i nodi
        table.setFocusable(false);
        PanjeaSwingUtil.lockScreen("Stampa tabella in corso...");

        exportMessageAlert = new ProgressBarMessageAlert("Stampa tabella in corso....");
        exportMessageAlert.setDescrizioneOperazione("Generazione della stampa");
        exportMessageAlert.showAlert();
    }

    @Override
    protected Void doInBackground() throws Exception {

        if (numberOfPages == -1) {
            return null;
        }

        try {
            JasperPrint jasperPrint = new JasperPrint();
            jasperPrint.setName("Stampa tabella");
            jasperPrint.setPageWidth(842);
            jasperPrint.setPageHeight(595);
            jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);

            JecTablePrintable printable = new JecTablePrintable(table, PrintMode.FIT_WIDTH, tableHeader);

            Dimension size = new Dimension((int) pageFormat.getWidth(), (int) pageFormat.getHeight());
            publish(0);
            for (int i = 0; i < numberOfPages; i++) {
                BufferedImage bufferimage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
                Graphics gr = bufferimage.getGraphics();
                gr.setColor(Color.WHITE);
                gr.fillRect(0, 0, (int) pageFormat.getWidth(), (int) pageFormat.getHeight());
                try {
                    printable.print(gr, pageFormat, i);
                } catch (Exception ex) {
                    // non mi darà mai errore perchè si fà la stessa cosa per il calcolo del numero di pagine quindi se
                    // avesse dato errore non sarei a questo punto ma il preview si sarebbe arrestato prima
                    throw ex;
                } finally {
                    gr.dispose();
                }

                JRPrintImage image = new JRBasePrintImage(jasperPrint.getDefaultStyleProvider());
                image.setX(0);
                image.setY(0);
                image.setWidth(842);
                image.setHeight(595);
                image.setScaleImage(ScaleImageEnum.CLIP);

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageOutputStream stream = new MemoryCacheImageOutputStream(baos);) {
                    ImageIO.write(bufferimage, "png", stream);
                    baos.flush();
                    image.setRenderable(JRImageRenderer.getInstance(baos.toByteArray()));
                }

                JRPrintPage page = new JRBasePrintPage();
                page.addElement(image);
                jasperPrint.addPage(page);

                if (i % 5 == 0) {
                    publish(i);
                }
            }
            publish(numberOfPages);

            JecReport jecReport = new JecReport(jasperPrint);
            jecReport.setReportName("Tabella");
            jecReport.execute();

        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        return null;
    }

    @Override
    protected void done() {
        try {
            get();
        } catch (Exception e) {
            // chiudo l'alert della generazione e visualizzo quello dell'arrore
            exportMessageAlert.closeAlert();
            MessageAlert errorMessageAlert = new MessageAlert(
                    new DefaultMessage("Errore durante la stampa della tabella", Severity.ERROR), 3000);
            errorMessageAlert.showAlert();

            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            exportMessageAlert.closeAlert();
            table.setFocusable(true);
            PanjeaSwingUtil.unlockScreen();
        }
    }

    /**
     * Inizializza tutte le proprietà necessarie.
     */
    private void init() {

        // creo il page format da utilizzare
        double a4HeightInch = 8.26771654; // Equals 210mm
        double a4WidthInch = 11.6929134; // Equals 297mm
        Paper paper = new Paper();
        paper.setSize(a4WidthInch * 72, a4HeightInch * 72); // 72 DPI
        paper.setImageableArea(5, 5, a4WidthInch * 72 - 10, a4HeightInch * 72 - 5); // 1 inch margins
        pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(PageFormat.PORTRAIT);

        // creo il modello di colore da utilizzare
        byte ff = (byte) 0xff;
        byte[] red = { ff, 0, 0, ff, 0 };
        byte[] green = { 0, 0x66, 0, ff, 0 };
        byte[] blue = { 0, 0, ff, ff, 0 };
        indexColorModel = new IndexColorModel(3, 5, red, green, blue);

        // calcolo il numero di pagine
        JecTablePrintable printable = new JecTablePrintable(table, PrintMode.FIT_WIDTH, tableHeader);
        Graphics graphics = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_INDEXED, indexColorModel).getGraphics();
        numberOfPages = 0;
        try {
            while (printable.print(graphics, pageFormat, numberOfPages) == Printable.PAGE_EXISTS) {
                numberOfPages++;
            }
        } catch (Exception ex) {
            numberOfPages = -1;
        } finally {
            graphics.dispose();
        }
    }

    @Override
    protected void process(List<Integer> chunks) {
        for (Integer progr : chunks) {
            if (progr.intValue() == 0) {
                // stò iniziando a processare le pagine quindi cambio il messaggio e setto il numero totale di pagine
                exportMessageAlert.setDescrizioneOperazione("Generazione pagine");
                exportMessageAlert.setNumeroOperazioni(numberOfPages);
            }
            exportMessageAlert.setOperazione(progr);
        }
    }

}