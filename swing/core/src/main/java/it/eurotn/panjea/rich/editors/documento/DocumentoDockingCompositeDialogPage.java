package it.eurotn.panjea.rich.editors.documento;

import it.eurotn.rich.dialog.DockingCompositeDialogPage;

import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.settings.SettingsManager;

/**
 *
 * Gestisce le pagine delle varie aree documento in un editor. Di default alla conferma di una pagina viene passato il
 * focus alla pagina successiva. Per settare la squenza delle pagine usare la proprietà
 * flow:idPagina:tabPagina,idPagina2,etc etc
 *
 * @author giangi
 * @version 1.0, 13/gen/2015
 *
 */
public class DocumentoDockingCompositeDialogPage extends DockingCompositeDialogPage {
    private class PageController {
        private int startTab;
        private String id;
        private int position;

        public PageController(final int position, String id, int startTab) {
            super();
            this.position = position;
            this.startTab = startTab;
            this.id = id;
        }

        public PageController(String id) {
            super();
            this.id = id;
            this.startTab = -1;
        }

    }

    private PageController[] pages;
    private String pagesFlow;

    /**
     * Costruttore.
     *
     * @param pageId
     *            id della pagina
     * @param settingsManager
     *            {@link SettingsManager}
     */
    public DocumentoDockingCompositeDialogPage(final String pageId, final SettingsManager settingsManager) {
        super(pageId);
    }

    @Override
    public void addPage(DialogPage page) {
        super.addPage(page);

        String[] pagesFlowToken = pagesFlow.split("\\,");
        for (String pagesFlowTab : pagesFlowToken) {
            // String[] pageFlowSequenceAndTab = pagesFlowTab.split("\\:");
        }

    }

    /**
     * @return Returns the pagesFlow.
     */
    public String getPagesFlow() {
        return pagesFlow;
    }

    /**
     * @param pagesFlow
     *            The pagesFlow to set.
     */
    public void setPagesFlow(String pagesFlow) {
        this.pagesFlow = pagesFlow;
    }

}