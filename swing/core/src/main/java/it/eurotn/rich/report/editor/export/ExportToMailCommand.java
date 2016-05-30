package it.eurotn.rich.report.editor.export;

import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.rich.control.mail.IPanjeaMailClient;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class ExportToMailCommand extends ActionCommand {

    private ParametriMail parametri;

    private AbstractExportCommand[] exportCommands;

    private static final String SAVE_TO_MAIL_ACTION = "JecJRViewer.exportToMail";

    private IPanjeaMailClient panjeaMailClient;

    {
        panjeaMailClient = RcpSupport.getBean(IPanjeaMailClient.BEAN_ID);
    }

    /**
     * Costruttore.
     *
     * @param exportCommands
     *            comandi per il tipo di allegati possibili
     */
    public ExportToMailCommand(final AbstractExportCommand[] exportCommands) {
        super(SAVE_TO_MAIL_ACTION);
        this.exportCommands = exportCommands;
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        if (parametri == null) {
            parametri = new ParametriMail();
        } else {
            parametri.getAttachments().clear();
        }
        panjeaMailClient.show(parametri, exportCommands);
    }

    /**
     * @param parametri
     *            The parametri to set.
     */
    public void setParametri(ParametriMail parametri) {
        this.parametri = parametri;
    }
}
