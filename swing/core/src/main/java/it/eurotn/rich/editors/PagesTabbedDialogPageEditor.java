package it.eurotn.rich.editors;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.DialogPage;

import it.eurotn.locking.ILock;
import it.eurotn.rich.dialog.TabbedCompositeDialogPage;
import it.eurotn.rich.form.PanjeaAbstractForm;

public class PagesTabbedDialogPageEditor extends TabbedCompositeDialogPage
        implements IFormPageEditor, IToolbarPageCommands {

    /**
     *
     * @param pageId
     *            id pagina
     */
    public PagesTabbedDialogPageEditor(final String pageId) {
        super(pageId);
    }

    @Override
    public void componentFocusGained() {
        // tabbedPane.setSelectedIndex(pagesTab.get(getActivePage().getId()));
    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        return getFirstPage().getEditorDeleteCommand();
    }

    @Override
    public AbstractCommand getEditorLockCommand() {
        return getFirstPage().getEditorLockCommand();
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getFirstPage().getEditorNewCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getFirstPage().getEditorSaveCommand();
    }

    @Override
    public AbstractCommand getEditorUndoCommand() {
        return getFirstPage().getEditorUndoCommand();
    }

    @Override
    public AbstractCommand[] getExternalCommandStart() {
        return null;
    }

    /**
     * @return restituisce la prima pagina configurata nella tabbed.
     */
    private IFormPageEditor getFirstPage() {
        return ((IFormPageEditor) getPages().get(0));
    }

    @Override
    public PanjeaAbstractForm getForm() {
        return getFirstPage().getForm();
    }

    @Override
    public String getPageEditorId() {
        return null;
    }

    @Override
    public Object getPageObject() {
        return getForm().getFormObject();
    }

    @Override
    public String getPageSecurityEditorId() {
        return null;
    }

    @Override
    public void grabFocus() {
    }

    @Override
    public boolean isCommittable() {
        return getFirstPage().isCommittable();
    }

    @Override
    public void loadData() {
    }

    @Override
    protected void objectChange(Object domainObject, DialogPage pageSource) {
        super.objectChange(domainObject, pageSource);
        if (domainObject != null) {
            firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, domainObject);
        }
    }

    @Override
    public Object onDelete() {
        return getFirstPage().onDelete();
    }

    @Override
    public ILock onLock() {
        return getFirstPage().onLock();
    }

    @Override
    public void onNew() {
        getFirstPage().onNew();
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public boolean onSave() {
        return getFirstPage().onSave();
    }

    @Override
    public boolean onUndo() {
        return getFirstPage().onUndo();
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void setExternalCommandAppend(AbstractCommand[] externalCommandAppend) {
        ((IToolbarPageCommands) getPages().get(0)).setExternalCommandAppend(externalCommandAppend);
    }

    @Override
    public void setExternalCommandLineEnd(AbstractCommand[] externalCommandLineEnd) {
        ((IToolbarPageCommands) getPages().get(0)).setExternalCommandLineEnd(externalCommandLineEnd);
    }

    @Override
    public void setExternalCommandStart(AbstractCommand[] externalCommandStart) {
        ((IToolbarPageCommands) getPages().get(0)).setExternalCommandStart(externalCommandStart);
    }

    @Override
    public void setFormObject(Object object) {
        setCurrentObject(object);
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

}
