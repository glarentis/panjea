/**
 *
 */
package it.eurotn.rich.control.table;

import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;
import java.util.Locale;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JTable;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.MessageDialog;

/**
 * Stampa il contenuto della tabella.
 *
 * @author fattazzo
 * @version 1.0, 02/lug/07
 *
 */
public class PrintCommand extends ActionCommand {

    public static final String COMMAND_ID = "printCommand";

    private static Logger logger = Logger.getLogger(PrintCommand.class);

    private static final String ERROR_MESSAGE_PRINT_TITLE = ".errorMessageTitle";
    private static final String ERROR_MESSAGE_PRINT_MESSAGE = ".errorMessageMessage";
    private static final String PAGE = ".pagina";

    private JXTable table;

    /**
     *
     * Costruttore.
     *
     * @param table
     *            tabella da stampare
     */
    public PrintCommand(final JXTable table) {
        super(COMMAND_ID);
        CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        c.configure(this);
        this.table = table;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.richclient.command.ActionCommand#doExecuteCommand()
     */
    @Override
    protected void doExecuteCommand() {

        MessageSource messageSource = (MessageSource) ApplicationServicesLocator.services()
                .getService(MessageSource.class);

        try {
            // fetch the printable
            Printable printable = table.getPrintable(JTable.PrintMode.FIT_WIDTH, null, new MessageFormat(
                    messageSource.getMessage(COMMAND_ID + PAGE, new Object[] {}, Locale.getDefault()) + " - {0}"));

            // fetch a PrinterJob
            PrinterJob job = PrinterJob.getPrinterJob();

            // set the Printable on the PrinterJob
            job.setPrintable(printable);

            // create an attribute set to store attributes from the print dialog
            PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();

            // display a print dialog and record whether or not the user cancels it
            boolean printAccepted = job.printDialog(attr);

            // if the user didn't cancel the dialog
            if (printAccepted) {
                job.print(attr);
            }
        } catch (PrinterException e) {
            logger.debug("--> Errore durante la stampa della tabella. Controllare lo stato della stampante", e);
            Message message = new DefaultMessage(messageSource.getMessage(COMMAND_ID + ERROR_MESSAGE_PRINT_MESSAGE,
                    new Object[] {}, Locale.getDefault()), Severity.ERROR);
            MessageDialog dialog = new MessageDialog(messageSource.getMessage(COMMAND_ID + ERROR_MESSAGE_PRINT_TITLE,
                    new Object[] {}, Locale.getDefault()), message);
            dialog.setCloseAction(CloseAction.DISPOSE);
            dialog.showDialog();
        }
    }

}
