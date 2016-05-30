package it.eurotn.panjea.giroclienti.rich.editors.scheda.header.copiascheda;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.giroclienti.domain.ModalitaCopiaGiroClienti;
import it.eurotn.panjea.giroclienti.rich.bd.GiroClientiAnagraficaBD;
import it.eurotn.panjea.giroclienti.rich.bd.IGiroClientiAnagraficaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

public class CopiaSchedaCommand extends ActionCommand {

    private final class CopiaSchedaDialog extends PanjeaTitledPageApplicationDialog {

        /**
         *
         * Costruttore.
         *
         * @param form
         *            scheda copia form
         */
        public CopiaSchedaDialog(final Form form) {
            super(form, null);
        }

        @Override
        protected String getTitle() {
            return "Copia scheda";
        }

        @Override
        protected boolean onFinish() {

            FormBackedDialogPage dialogPage = (FormBackedDialogPage) getDialogPage();
            SchedaCopiaFrom schedaCopiaFrom = (SchedaCopiaFrom) dialogPage.getBackingFormPage();

            if (!schedaCopiaFrom.hasErrors() && schedaCopiaFrom.getFormModel().isCommittable()) {
                schedaCopiaFrom.commit();
                SchedaCopiaPM schedaCopiaPM = (SchedaCopiaPM) schedaCopiaFrom.getFormObject();

                giroClientiAnagraficaBD.copiaGiroSedeClienti(schedaCopiaPM.getUtente().getId(),
                        schedaCopiaPM.getGiorno(), schedaCopiaPM.getUtenteDestinazione().getId(),
                        schedaCopiaPM.getGiornoDestinazione(), schedaCopiaPM.getModalitaCopia());

                return true;
            }

            return false;
        }
    }

    public static final String PARAM_UTENTE = "paramUtente";

    public static final String PARAM_GIORNO = "giorno";
    private IGiroClientiAnagraficaBD giroClientiAnagraficaBD;

    /**
     * Costruttore.
     */
    public CopiaSchedaCommand() {
        super("copiaSchedaGiroClienteCommand");
        RcpSupport.configure(this);

        this.giroClientiAnagraficaBD = RcpSupport.getBean(GiroClientiAnagraficaBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        Utente utente = (Utente) getParameter(PARAM_UTENTE, null);
        Giorni giorno = (Giorni) getParameter(PARAM_GIORNO, null);

        if (utente == null || giorno == null) {
            return;
        }

        SchedaCopiaPM schedaCopiaPM = new SchedaCopiaPM();
        schedaCopiaPM.setUtente(utente);
        schedaCopiaPM.setGiorno(giorno);
        schedaCopiaPM.setModalitaCopia(ModalitaCopiaGiroClienti.AGGIUNGI);

        PanjeaTitledPageApplicationDialog page = new CopiaSchedaDialog(new SchedaCopiaFrom(schedaCopiaPM));
        page.showDialog();
        page = null;
    }

}
