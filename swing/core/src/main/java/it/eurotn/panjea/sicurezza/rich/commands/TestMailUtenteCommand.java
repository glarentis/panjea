/**
 *
 */
package it.eurotn.panjea.sicurezza.rich.commands;

import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.rich.services.IMailService;
import it.eurotn.rich.services.MailLocalService;

import java.awt.Dimension;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author Leonardo
 */
public class TestMailUtenteCommand extends ApplicationWindowAwareCommand {

    private static Logger logger = Logger.getLogger(TestMailUtenteCommand.class);

    public static final String ID = "testMailUtenteCommand";
    private IMailService mailService = null;
    private Utente utente = null;
    private DatiMail datiMail = null;

    /**
     * Costruttore.
     */
    public TestMailUtenteCommand() {
        super(ID);
        RcpSupport.configure(this);
        mailService = RcpSupport.getBean(MailLocalService.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {
        logger.debug("Testo la connessione con il server mail");
        try {
            boolean isOk = mailService.testConnection(utente, datiMail);
            if (isOk) {
                MessageDialog message = new MessageDialog("Dati per invio email",
                        new DefaultMessage("Connessione riuscita", Severity.INFO));
                message.setPreferredSize(new Dimension(300, 80));
                message.showDialog();
            }
        } catch (Exception e) {
            MessageDialog messageDialog = new MessageDialog("Errore invio mail",
                    new DefaultMessage(
                            "Errore nella spedizione della mail. Errore ricevuto dal server\n " + e.getMessage(),
                            Severity.ERROR));
            messageDialog.showDialog();
        }
    }

    /**
     * @param datiMail
     *            the datiMail to set
     */
    public void setDatiMail(DatiMail datiMail) {
        this.datiMail = datiMail;
    }

    /**
     * @param utente
     *            the utente to set
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

}
