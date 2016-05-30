package it.eurotn.panjea.rich.editors;

import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.dialog.TabbedCompositeDialogPage;
import it.eurotn.rich.editors.AbstractEditorDialogPage;

import org.apache.log4j.Logger;

public class TabbedEditor extends AbstractEditorDialogPage {

    private static Logger logger = Logger.getLogger(DockedEditor.class);

    /**
     * Costruttore.
     * 
     */
    public TabbedEditor() {
        super();
        logger.debug("---> Enter DockedEditor");
    }

    @Override
    protected JecCompositeDialogPage createCompositeDialogPage() {
        return new TabbedCompositeDialogPage(getDialogPageId());
    }

}
