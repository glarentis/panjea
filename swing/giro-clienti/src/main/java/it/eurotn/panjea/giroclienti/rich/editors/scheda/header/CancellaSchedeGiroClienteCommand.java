package it.eurotn.panjea.giroclienti.rich.editors.scheda.header;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.giroclienti.rich.bd.ISchedeGiroClientiBD;
import it.eurotn.panjea.giroclienti.rich.bd.SchedeGiroClientiBD;
import it.eurotn.panjea.sicurezza.domain.Utente;

public class CancellaSchedeGiroClienteCommand extends ActionCommand {

    public static final String PARAM_UTENTE = "paramUtente";

    private boolean schedeCancellate = false;

    private ISchedeGiroClientiBD schedeGiroClientiBD;

    /**
     * Costruttore.
     */
    public CancellaSchedeGiroClienteCommand() {
        super("cancellaSchedeGiroClienteCommand");
        RcpSupport.configure(this);
        this.schedeGiroClientiBD = RcpSupport.getBean(SchedeGiroClientiBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        schedeCancellate = false;

        final Utente utente = (Utente) getParameter(PARAM_UTENTE, null);

        if (utente != null) {
            new ConfirmationDialog("ATTENZIONE", "Cancellare le schede dell'utente " + utente.getUserName() + "?") {

                @Override
                protected void onConfirm() {
                    schedeGiroClientiBD.cancellaSchede(utente);
                    schedeCancellate = true;
                }

            }.showDialog();

        }
    }

    /**
     * @return the schedeCancellate
     */
    public boolean isSchedeCancellate() {
        return schedeCancellate;
    }

}
