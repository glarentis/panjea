/**
 * 
 */
package it.eurotn.panjea.rich.editors;

import it.eurotn.rich.dialog.ButtonCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractEditorDialogPage;

import org.springframework.richclient.progress.ProgressMonitor;

/**
 * Editor di default.
 * 
 * @author Aracno,Leonardo
 * @version 1.0, 04/ago/06
 * 
 */
public class DefaultEditor extends AbstractEditorDialogPage {

    /**
     * Costruttore.
     */
    public DefaultEditor() {
        super();
    }

    @Override
    protected JecCompositeDialogPage createCompositeDialogPage() {
        return new ButtonCompositeDialogPage(getDialogPageId(), enabledOnOpen);
    }

    @Override
    public void save(ProgressMonitor saveProgressTracker) {
    }
}
