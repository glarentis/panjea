package it.eurotn.panjea.giroclienti.rich.editors.scheda.header;

import java.util.Calendar;
import java.util.Date;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.giroclienti.domain.GiroSedeCliente;
import it.eurotn.panjea.giroclienti.rich.bd.GiroClientiAnagraficaBD;
import it.eurotn.panjea.giroclienti.rich.bd.IGiroClientiAnagraficaBD;
import it.eurotn.panjea.giroclienti.rich.editors.scheda.GiroSedeClienteForm;
import it.eurotn.panjea.giroclienti.rich.editors.scheda.GiroSedeClientePM;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

public class AggiungiGiroClienteCommand extends ActionCommand {

    public static final String PARAM_UTENTE = "paramUtente";
    public static final String PARAM_GIORNO = "paramGiorno";
    public static final String PARAM_ORA = "paramOra";

    private IGiroClientiAnagraficaBD giroClientiAnagraficaBD;

    private boolean giroAggiunto = false;

    /**
     * Costruttore.
     */
    public AggiungiGiroClienteCommand() {
        super("aggiungiGiroClienteCommand");
        RcpSupport.configure(this);
        this.giroClientiAnagraficaBD = RcpSupport.getBean(GiroClientiAnagraficaBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        giroAggiunto = false;

        Utente utente = (Utente) getParameter(PARAM_UTENTE, null);
        Giorni giorno = (Giorni) getParameter(PARAM_GIORNO, Giorni.LUNEDI);
        Date ora = (Date) getParameter(PARAM_ORA, null);
        if (ora == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(0);
            ora = calendar.getTime();
        }

        GiroSedeCliente giroSedeCliente = new GiroSedeCliente();
        giroSedeCliente.setGiorno(giorno);
        giroSedeCliente.setUtente(utente);
        giroSedeCliente.setOra(ora);

        GiroSedeClientePM giroSedeClientePM = new GiroSedeClientePM();
        giroSedeClientePM.setGiroSedeCliente(giroSedeCliente);

        PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(
                new GiroSedeClienteForm(giroSedeClientePM), Application.instance().getActiveWindow().getControl()) {

            @Override
            protected boolean onFinish() {
                FormBackedDialogPage page = (FormBackedDialogPage) getDialogPage();

                GiroSedeClientePM giroSedeClientePM = (GiroSedeClientePM) page.getBackingFormPage().getFormObject();

                // salvo il giro e non chiudo il dialog per fare in modo che ne possa essere inserito un'altro al volo
                giroClientiAnagraficaBD.salvaGiroSedeCliente(giroSedeClientePM.getGiroSedeCliente());
                giroAggiunto = true;

                giroSedeClientePM.setEntita(null);
                giroSedeClientePM.getGiroSedeCliente().setSedeEntita(null);
                page.getBackingFormPage().setFormObject(giroSedeClientePM);
                ((Focussable) page.getBackingFormPage()).grabFocus();
                return false;
            }
        };
        dialog.showDialog();

    }

    /**
     * @return the giroAggiunto
     */
    public boolean isGiroAggiunto() {
        return giroAggiunto;
    }

}
