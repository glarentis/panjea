package it.eurotn.panjea.vending.rich.editors.distributore.modelli;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import org.springframework.richclient.dialog.DialogPage;

import it.eurotn.locking.IDefProperty;
import it.eurotn.rich.dialog.DefaultTitledPageApplicationDialog;
import it.eurotn.rich.editors.IFormPageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.PagesTabbedDialogPageEditor;

public class ModelloTipoModelloDialog extends DefaultTitledPageApplicationDialog {

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
    public ModelloTipoModelloDialog(final IDefProperty object, final DialogPage owner, final IPageEditor dialogPage) {
        super(object, owner, dialogPage);
        List<DialogPage> pages = ((PagesTabbedDialogPageEditor) dialogPage).getDialogPages();
        for (DialogPage page : pages) {
            for (Component component : page.getControl().getComponents()) {
                if (component.getName() != null && "toolBar".equals(component.getName())) {
                    page.getControl().remove(component);
                }
            }
        }
    }

    @Override
    protected Dimension getPreferredSize() {
        return new Dimension(900, 600);
    }

    @Override
    protected void onAboutToShow() {
        super.onAboutToShow();

        IFormPageEditor page = (IFormPageEditor) this.getDialogPage();
        IDefProperty object = (IDefProperty) page.getForm().getFormObject();

        if (object.isNew()) {
            page.onLock();
        }
    }

}
