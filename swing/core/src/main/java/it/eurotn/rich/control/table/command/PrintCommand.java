package it.eurotn.rich.control.table.command;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.util.RcpSupport;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.JecTablePrintable;
import it.eurotn.rich.dialog.MessageAlert;

/**
 * Stampa il contenuto della tabella.
 *
 * @author fattazzo
 * @version 1.0, 02/lug/07
 *
 */
public class PrintCommand extends ActionCommand {

    public static final String COMMAND_ID = "printTableCommand";
    public static final String PARAM_HEADER_PARAMETER = "headerParamenter";

    private static final Logger LOGGER = Logger.getLogger(PrintCommand.class);

    private final JTable table;
    private ITableHeader tableHeader;

    /**
     * 
     * @param table
     *            tabella da stampare.
     */
    public PrintCommand(final JTable table) {
        super(COMMAND_ID);
        RcpSupport.configure(this);
        this.table = table;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.richclient.command.ActionCommand#doExecuteCommand()
     */
    @Override
    protected void doExecuteCommand() {

        final HashPrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(MediaSizeName.ISO_A4);
        attr.add(OrientationRequested.LANDSCAPE);
        // set the image area within the paper total area :
        MediaSize mediaSize = MediaSize.ISO.A4;
        float mediaWidth = mediaSize.getX(Size2DSyntax.MM);
        float mediaHeight = mediaSize.getY(Size2DSyntax.MM);
        float imageableX = 10; // mm
        float imageableY = 10; // mm
        float imageableWidth = mediaWidth - (2 * imageableX);
        float imageableHeight = mediaHeight - (2 * imageableY);
        MediaPrintableArea imageableArea = new MediaPrintableArea(imageableX, imageableY, imageableWidth,
                imageableHeight, Size2DSyntax.MM);
        attr.add(imageableArea);

        final PrinterJob job = PrinterJob.getPrinterJob();
        if (job.printDialog(attr)) {

            PanjeaSwingUtil.lockScreen("Stampa tabella in corso...");

            AsyncWorker.post(new AsyncTask() {

                @Override
                public void failure(Throwable error) {
                    Message message = null;
                    message = new DefaultMessage("Stampa della tabella non riuscita", Severity.ERROR);
                    LOGGER.error("-->errore nella stampa della tabella", error);
                    new MessageAlert(message).showAlert();
                }

                @Override
                public Object run() throws Exception {
                    try {
                        job.setPrintable(new JecTablePrintable(table, PrintMode.FIT_WIDTH, tableHeader));
                        job.print(attr);
                    } catch (PrinterException ex) {
                        PanjeaSwingUtil.checkAndThrowException(ex);
                    } finally {
                        PanjeaSwingUtil.unlockScreen();
                    }
                    return true;
                }

                @Override
                public void success(Object arg0) {
                }
            });
        }

    }

    /**
     * @param tableHeader
     *            The tableHeader to set.
     */
    public void setTableHeader(ITableHeader tableHeader) {
        this.tableHeader = tableHeader;
    }
}
