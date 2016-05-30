package it.eurotn.panjea.sicurezza.rich.editors;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class UtenteDatiMailPage extends FormBackedDialogPageEditor {

    private ISicurezzaBD sicurezzaBD;

    private DatiMail datiMail;

    /**
     * Costruttore.
     */
    public UtenteDatiMailPage() {
        super("utenteDatiMailPage", new UtenteDatiEmailForm());
    }

    @Override
    protected Object doDelete() {
        DatiMail dati = (DatiMail) getBackingFormPage().getFormObject();
        sicurezzaBD.cancellaDatiMail(dati);
        return dati;
    }

    @Override
    protected Object doSave() {
        DatiMail datiMailSave = (DatiMail) getForm().getFormObject();
        datiMail = sicurezzaBD.salvaDatiMail(datiMailSave);
        return datiMail;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return toolbarPageEditor.getDefaultCommand(true);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public void refreshData() {
        loadData();
    }

    @Override
    public void setFormObject(Object object) {
        datiMail = (DatiMail) object;
        datiMail = sicurezzaBD.caricaDatiMail(datiMail);
        super.setFormObject(datiMail);
    }

    /**
     * @param sicurezzaBD
     *            the sicurezzaBD to set
     */
    public void setSicurezzaBD(ISicurezzaBD sicurezzaBD) {
        this.sicurezzaBD = sicurezzaBD;
    }

    /**
     * @param utente
     *            the utente to set
     */
    public void setUtente(Utente utente) {
        ((UtenteDatiEmailForm) getForm()).setUtente(utente);
    }

}
