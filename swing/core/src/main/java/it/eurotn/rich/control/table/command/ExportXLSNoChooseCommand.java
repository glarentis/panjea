package it.eurotn.rich.control.table.command;

import java.awt.Desktop;
import java.io.File;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 *
 * Comando che utilizza la classe {@link ExportXLSCommand} per esportare la tabella in formato XLS senza però chiederne
 * il percorso di salvataggio del file. Il file verrà quindi generato nella directory temporanea dell'utente e
 * successivamente aperto.
 *
 * @author fattazzo
 * @version 1.0, 06/giu/2012
 *
 */
public class ExportXLSNoChooseCommand extends ActionCommand {

    public static final String COMMAND_ID = "exportXLSNoChooseCommand";

    private ExportXLSCommand exportXLSCommand;

    /**
     * Costruttore.
     * 
     * @param exportXLSCommand
     *            comando di esportazione XLS della tabella
     */
    public ExportXLSNoChooseCommand(final ExportXLSCommand exportXLSCommand) {
        super(COMMAND_ID);
        RcpSupport.configure(this);
        this.exportXLSCommand = exportXLSCommand;
    }

    @Override
    protected void doExecuteCommand() {
        PanjeaSwingUtil.lockScreen("Esportazione tabella in corso...");

        AsyncWorker.post(new AsyncTask() {

            @Override
            public void failure(Throwable arg0) {
                PanjeaSwingUtil.unlockScreen();
                PanjeaSwingUtil.checkAndThrowException(arg0);
            }

            @Override
            public Object run() throws Exception {
                File fileToExport = null;
                fileToExport = File.createTempFile("export", ".xls");

                if (exportXLSCommand.export(fileToExport)) {
                    return fileToExport;
                }
                return null;
            }

            @Override
            public void success(Object arg0) {
                PanjeaSwingUtil.unlockScreen();
                File fileToExport = (File) arg0;
                try {
                    if (fileToExport != null && Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(fileToExport);
                    }
                } catch (Exception e) {
                    logger.error("--> errore durante l'apertura del file di esportazione " + fileToExport.getName(), e);
                }
            }
        });
    }

}
