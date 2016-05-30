package it.eurotn.panjea.sicurezza.rich.editors;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.sicurezza.rich.pm.UtentePM;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * @author Leonardo
 */
public class UtentePage extends FormsBackedTabbedDialogPageEditor {

    private static Logger logger = Logger.getLogger(UtentePage.class);
    private static final String ID_PAGE = "utentePage";
    private ISicurezzaBD sicurezzaBD;

    private UtenteMenuDiableCommandsForm utenteMenuDiableCommandsForm;

    /**
     * Costruttore.
     *
     * @param form
     *            form che la pagina gestisce
     */
    public UtentePage(final PanjeaAbstractForm form) {
        super(ID_PAGE, form);
    }

    @Override
    public void addForms() {
        utenteMenuDiableCommandsForm = new UtenteMenuDiableCommandsForm(getBackingFormPage().getFormModel());
        addForm(utenteMenuDiableCommandsForm);
    }

    @Override
    protected Object doSave() {
        UtentePM utentePM = (UtentePM) getBackingFormPage().getFormModel().getFormObject();
        Utente utenteDaSalvare = utentePM.getUtente();

        logger.debug("---> doSave utente " + utenteDaSalvare);
        Utente utente = sicurezzaBD.salvaUtente(utenteDaSalvare, utentePM.getRuoliDaAggiungere(),
                utentePM.getRuoliDaRimuovere());
        logger.debug("---> currentObject " + utente);

        if (utente == null) {
            return false;
        }

        utentePM.setUtente(utente);
        utentePM.getRuoliDaAggiungere().clear();
        utentePM.getRuoliDaRimuovere().clear();
        getBackingFormPage().setFormObject(utentePM);

        return utente;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
                toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand() };
        return abstractCommands;
    }

    /**
     * @return the sicurezzaBD
     */
    public ISicurezzaBD getSicurezzaBD() {
        return sicurezzaBD;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void setFormObject(Object object) {
        super.setFormObject(new UtentePM((Utente) object));
    }

    /**
     * @param sicurezzaBD
     *            the sicurezzaBD to set
     */
    public void setSicurezzaBD(ISicurezzaBD sicurezzaBD) {
        this.sicurezzaBD = sicurezzaBD;
    }

}
