package it.eurotn.rich.dialog;

import java.beans.PropertyChangeListener;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.form.FormGuard;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.IFormPageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

/**
 * Dialog di modifica dei record tabella.
 *
 * @author Leonardo
 */
public class DefaultTitledPageApplicationDialog extends PanjeaTitledPageApplicationDialog {

    private boolean commit = false;
    private DialogPage owner = null;

    /**
     * Costruttore.
     *
     * @param object
     *            oggetto
     * @param owner
     *            owner
     * @param dialogPage
     *            pagina
     */
    public DefaultTitledPageApplicationDialog(final IDefProperty object, final DialogPage owner,
            final IPageEditor dialogPage) {
        super();

        org.springframework.util.Assert.notNull(dialogPage, "dialogPage cannot be null!");

        dialogPage.addPropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED, (PropertyChangeListener) owner);

        // esegue la creazione dei controlli prima che venga inizializzato il FormObject
        this.setDialogPage((DialogPage) dialogPage);
        this.owner = owner;
        if (!isControlCreated()) {
            createDialog();
        }

        /*
         * Se nel costruttore della TitlePageApplicationDialog l'oggetto che mi viene passato � nullo allora ne creo uno
         * nuovo con il NewFormObjectCommand del Form della dialogPage, altrimenti setto al form l'oggetto che mi viene
         * passato
         */
        dialogPage.onPrePageOpen();
        if (object != null) {
            dialogPage.setFormObject(object);
        } else {
            dialogPage.onNew();
        }
        dialogPage.onPostPageOpen();
    }

    @Override
    protected String getTitle() {
        MessageSource messageSource = (MessageSource) ApplicationServicesLocator.services()
                .getService(MessageSource.class);
        String property = getDialogPage().getId() + "Dialog.title";
        return messageSource.getMessage(property, new Object[] {}, Locale.getDefault());
    }

    /**
     * @return Returns the commit.
     */
    public boolean isCommit() {
        return commit;
    }

    @Override
    protected void onAboutToShow() {
        IFormPageEditor page = (IFormPageEditor) this.getDialogPage();
        // bug : se non risetto il command sulla base di isCommittable trovo il finish abilitato
        // nella condizione in cui il form non e' committable.
        IDefProperty object = (IDefProperty) page.getForm().getFormObject();

        // devo impostare il guard su dirty e committable, (dirty ha un problema al momento quindi lo disattivo)
        // + FormGuard.ON_ISDIRTY
        new PanjeaFormGuard(page.getForm().getFormModel(), this.getFinishCommand(), FormGuard.ON_NOERRORS);

        if (object.getId() != null) {
            page.onLock();
        }

        // chiamata per prevenire problemi di visualizzazione del contenuto del dialogo quando
        // essso viene cambiato (aggiunta o rimozione di componenti); esegue dialog.pack() se i controlli
        // sono stati creati
        this.componentsChanged();
    }

    @Override
    protected void onCancel() {
        IFormPageEditor page = (IFormPageEditor) this.getDialogPage();
        page.onUndo();
        if (owner != null && owner instanceof PropertyChangeListener) {
            page.removePropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED, (PropertyChangeListener) owner);
        }
        super.onCancel();
    }

    @Override
    protected boolean onFinish() {
        IFormPageEditor page = (IFormPageEditor) this.getDialogPage();
        boolean finished = page.onSave();

        if (finished && owner != null && owner instanceof PropertyChangeListener) {
            page.removePropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED, (PropertyChangeListener) owner);
        }
        commit = true;
        return finished;
    }

}