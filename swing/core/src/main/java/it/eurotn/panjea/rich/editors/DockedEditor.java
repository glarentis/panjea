package it.eurotn.panjea.rich.editors;

import it.eurotn.rich.dialog.DockingCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractEditorDialogPage;

import org.apache.log4j.Logger;

public class DockedEditor extends AbstractEditorDialogPage {

    private static Logger logger = Logger.getLogger(DockedEditor.class);

    private DockingCompositeDialogPage compositeDialogPage;

    /**
     * Costruttore.
     */
    public DockedEditor() {
        super();
        logger.debug("---> Enter DockedEditor");
    }

    @Override
    protected JecCompositeDialogPage createCompositeDialogPage() {
        return getCompositeDialogPage();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * @return the compositeDialogPage
     */
    public DockingCompositeDialogPage getCompositeDialogPage() {
        if (compositeDialogPage == null) {
            compositeDialogPage = new DockingCompositeDialogPage(getDialogPageId());
        }

        return compositeDialogPage;
    }

}
