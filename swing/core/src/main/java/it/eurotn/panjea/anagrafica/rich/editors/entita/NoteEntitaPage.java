package it.eurotn.panjea.anagrafica.rich.editors.entita;

import java.util.Locale;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.MessageDialog;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class NoteEntitaPage extends FormBackedDialogPageEditor implements InitializingBean {

    private static final String PAGE_ID = "noteEntitaPage";

    private IAnagraficaBD anagraficaBD;
    private Entita entita;

    /**
     * Costruttore di default.
     *
     * @param entita
     *            l'entita' da modificare
     */
    public NoteEntitaPage(final Entita entita) {
        super(PAGE_ID, new NoteEntitaForm(entita));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    protected Object doSave() {
        Entita entitaDaSalvare = ((Entita) getBackingFormPage().getFormObject());
        Entita entitaResult = null;

        if (isHtmlTextEmpty(entitaDaSalvare.getNote())) {
            entitaDaSalvare.setNote(null);
        }
        if (isHtmlTextEmpty(entitaDaSalvare.getNoteContabilita())) {
            entitaDaSalvare.setNoteContabilita(null);
        }
        if (isHtmlTextEmpty(entitaDaSalvare.getNoteMagazzino())) {
            entitaDaSalvare.setNoteMagazzino(null);
        }

        try {
            entitaResult = anagraficaBD.salvaEntita(entitaDaSalvare);

            // TODO verificare la necessità di questo setformobj
            getBackingFormPage().setFormObject(entitaResult);
        } catch (AnagraficheDuplicateException e) {
            MessageDialog messagio = new MessageDialog("ERRORE", "partita  iva o  codice fiscale duplicato...." + e);
            messagio.showDialog();
            getNewCommand().execute();
            entitaResult = (Entita) getForm().getFormObject();
        }

        return entitaResult;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] commands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
                toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand() };
        return commands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return null;
    }

    @Override
    protected boolean insertControlInScrollPane() {
        return false;
    }

    /**
     * prende un testo un formato html e controla se è vuoto.
     *
     * @param testoHtml
     *            in formato html
     * @return boolean
     */
    public boolean isHtmlTextEmpty(String testoHtml) {
        boolean result = true;
        if (testoHtml != null) {
            String testo = testoHtml.replaceAll("\\<.*?>", "").replaceAll("\n", "");
            if (!testo.trim().isEmpty()) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public void loadData() {
        // Non utilizzato
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        boolean initializePage = true;
        if (entita.isNew()) {
            initializePage = false;
            MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                    .getService(MessageSourceAccessor.class);
            String titolo = messageSourceAccessor.getMessage("entita.null.messageDialog.title", new Object[] {},
                    Locale.getDefault());
            String messaggio = messageSourceAccessor.getMessage(
                    "entita.null.messageDialog.message", new Object[] { messageSourceAccessor
                            .getMessage(entita.getDomainClassName(), new Object[] {}, Locale.getDefault()) },
                    Locale.getDefault());
            new MessageDialog(titolo, messaggio).showDialog();
        }
        return initializePage;
    }

    @Override
    public void refreshData() {
        loadData();
    }

    /**
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
        entita = (Entita) object;
        super.setFormObject(object);
    }
}
