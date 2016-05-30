package it.eurotn.panjea.rich.editors.email;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.rich.bd.IMailBD;
import it.eurotn.panjea.rich.bd.MailBD;
import javafx.concurrent.Task;

public class ImportaMailCommand extends ActionCommand {

    private IMailBD mailBD = null;

    /**
     * Costruttore.
     */
    public ImportaMailCommand() {
        super("importaMailCommand");
        RcpSupport.configure(this);
        mailBD = RcpSupport.getBean(MailBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                mailBD.importaMail();
                return null;
            }
        }).start();

    }

}
