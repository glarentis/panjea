package it.eurotn.rich.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.JideTabbedPane;

import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class TabbedCompositeDialogPage extends JecCompositeDialogPage {

    private final Map<String, Integer> pagesTab = new HashMap<String, Integer>();

    protected JideTabbedPane tabbedPane;

    protected final List<DialogPage> pages;
    private boolean loadingPage = false;

    /**
     * Costruttore.
     *
     * @param pageId
     *            id delal pagina
     */
    public TabbedCompositeDialogPage(final String pageId) {
        super(pageId);
        pages = new ArrayList<DialogPage>();
    }

    @Override
    public void addPage(String idPage) {

        if (!idPage.startsWith("group:")) {
            DialogPage pagina = (DialogPage) Application.instance().getApplicationContext().getBean(idPage);
            if (pagina != null) {
                pages.add(pagina);
                addPage(pagina);
            }
        }
    }

    /**
     * Abilita o disabilita il tab relativo alla pagina richiesta.
     *
     * @param pageId
     *            id della pagina
     * @param enable
     *            enable
     */
    protected void changeEnableTab(String pageId, boolean enable) {
        if (pagesTab.containsKey(pageId)) {
            tabbedPane.setEnabledAt(pagesTab.get(pageId), enable);
        }
    }

    @Override
    public void componentFocusGained() {

        tabbedPane.setSelectedIndex(pagesTab.get(getActivePage().getId()));

        if (getActivePage() instanceof IPageEditor) {
            IPageEditor page = (IPageEditor) getActivePage();
            page.grabFocus();
        } else {
            if (getActivePage().getControl() != null) {
                getActivePage().getControl().requestFocusInWindow();
            }
        }
    }

    @Override
    protected JComponent createPageControl() {

        createTabbedPane();
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                DialogPage page = pages.get(((JTabbedPane) event.getSource()).getSelectedIndex());
                if (loadingPage) {
                    return;
                }
                try {
                    loadingPage = true;
                    if (!isPageLoaded(page.getId())) {
                        tabbedPane.setComponentAt(((JTabbedPane) event.getSource()).getSelectedIndex(),
                                page.getControl());
                    }
                    showPage(page);
                    TabbedCompositeDialogPage.this.componentFocusGained();
                } finally {
                    loadingPage = false;
                }
            }
        });

        Integer tabindex = 0;

        try {
            loadingPage = true;
            for (DialogPage page : pages) {

                String idTab = TabbedCompositeDialogPage.this.getId() + "." + page.getId() + "Command";
                pagesTab.put(page.getId(), tabindex);

                String caption = RcpSupport.getMessage(idTab + ".label");
                if (caption.isEmpty()) {
                    caption = idTab + ".label";
                }
                if (tabindex == 0) {
                    tabbedPane.addTab(caption, RcpSupport.getIcon(idTab + ".icon"), page.getControl());
                    showPage(page);
                    TabbedCompositeDialogPage.this.componentFocusGained();
                } else {
                    tabbedPane.addTab(caption, RcpSupport.getIcon(idTab + ".icon"), new JPanel());
                }
                tabindex++;
            }
        } finally {
            loadingPage = false;
        }

        return tabbedPane;
    }

    /**
     *
     */
    protected void createTabbedPane() {
        tabbedPane = (JideTabbedPane) getComponentFactory().createTabbedPane();
        tabbedPane.setTabShape(JideTabbedPane.SHAPE_VSNET);
        tabbedPane.setTabPlacement(SwingConstants.TOP);
        tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_VSNET);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
        tabbedPane.setName(getId());
    }

    /**
     * Disabilita tutti i tab.
     *
     */
    protected void disableAllTabs() {
        for (Entry<String, Integer> entry : pagesTab.entrySet()) {
            tabbedPane.setEnabledAt(entry.getValue(), false);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Object opagina : getPages()) {
            ((IPageLifecycleAdvisor) opagina).dispose();
        }
    }

    /**
     * Abilita tutti i tab.
     *
     */
    protected void enableAllTabs() {
        for (Entry<String, Integer> entry : pagesTab.entrySet()) {
            tabbedPane.setEnabledAt(entry.getValue(), true);
        }
    }

    @Override
    protected void objectChange(Object domainObject, DialogPage pageSource) {
        for (DialogPage page : getDialogPages()) {
            if (pageSource == null || !page.getId().equals(pageSource.getId())) {
                pagesDirty.put(page.getId(), true);
            }
        }
        if (pageSource == null) {
            pagesLoaded.clear();
            showPage(getPage(getPagesLinkedList().get(0)));
        }
    }

    /**
     * Il metodo si occupa di aggiungere tutte le pagine da gestire.
     *
     * @param idPages
     *            <code>List</code> contenente l'id delle pagine da gestire.
     */
    public void setIdPages(List<String> idPages) {
        for (String pagId : idPages) {
            this.addPage(pagId);
        }
    }

    @Override
    protected void showComponentPage(DialogPage page) {
        if (pagesTab.get(page.getId()) != null) {
            tabbedPane.setSelectedIndex(pagesTab.get(page.getId()));
        }
    }
}
