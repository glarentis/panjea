package it.eurotn.panjea.ordini.rich.editors.areaordine;

import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.docking.DockableFrame;

import it.eurotn.rich.dialog.DockingCompositeDialogPage;

public class AreaOrdineDockingCompositeDialogPage extends DockingCompositeDialogPage {

    private final AreaOrdineEditorController areaOrdineEditorController;

    /**
     *
     * Costruttore.
     *
     * @param pageId
     *            id pagina
     */
    public AreaOrdineDockingCompositeDialogPage(final String pageId) {
        super(pageId);
        areaOrdineEditorController = new AreaOrdineEditorController(this);
    }

    @Override
    public void addPage(DialogPage page) {
        super.addPage(page);
        areaOrdineEditorController.addPage(page);
    }

    @Override
    protected void configureFrame(DockableFrame frame) {

    }

    @Override
    public void dispose() {
        areaOrdineEditorController.dispose();
        super.dispose();
    }

    @Override
    public void restoreState(Settings settings) {
        boolean visible = areaOrdineEditorController.getRigheInserimentoPage().isVisible();
        super.restoreState(settings);
        areaOrdineEditorController.attivaInserimentoRighe(visible);
    }
}
