package it.eurotn.rich.dialog;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.docking.event.DockableFrameAdapter;
import com.jidesoft.docking.event.DockableFrameEvent;

import it.eurotn.rich.dialog.docking.HolderPanel;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Visualizza le pagine contenute in un AbstractEditorPage in modalit? Docked. l'icona e la caption
 * per ogni frame viene definita come il pulsante nella ButtonCompositeDialogPage
 *
 * @author giangi
 */
public class DockingCompositeDialogPage extends JecCompositeDialogPage {

    private final class ActivateFrameAdapter extends DockableFrameAdapter {

        @Override
        public void dockableFrameActivated(DockableFrameEvent dockableframeevent) {
            setActivePage(dockableframeevent.getDockableFrame().getKey());
        }

        @Override
        public void dockableFrameAutohidden(DockableFrameEvent paramDockableFrameEvent) {
            super.dockableFrameAutohidden(paramDockableFrameEvent);
            DialogPage page = getPage(paramDockableFrameEvent.getDockableFrame().getKey());
            // Rimuovo il changeListener altrimenti la setVisible sulla pagina lancia il propery
            // change sul visible e il frame viene nascosto
            page.removePropertyChangeListener(pagePropertyChange);
            page.setVisible(false);
            page.addPropertyChangeListener(pagePropertyChange);
        }

        @Override
        public void dockableFrameAutohideShowing(DockableFrameEvent paramDockableFrameEvent) {
            super.dockableFrameAutohideShowing(paramDockableFrameEvent);
            DialogPage page = getPage(paramDockableFrameEvent.getDockableFrame().getKey());
            // Rimuovo il changeListener altrimenti la setVisible sulla pagina lancia il propery
            // change sul visible e il frame viene nascosto
            page.removePropertyChangeListener(pagePropertyChange);
            page.setVisible(true);
            page.addPropertyChangeListener(pagePropertyChange);
        }

        @Override
        public void dockableFrameDocked(DockableFrameEvent paramDockableFrameEvent) {
            super.dockableFrameDocked(paramDockableFrameEvent);
            DialogPage page = getPage(paramDockableFrameEvent.getDockableFrame().getKey());
            // Rimuovo il changeListener altrimenti la setVisible sulla pagina lancia il propery
            // change sul visible e il frame viene nascosto
            page.removePropertyChangeListener(pagePropertyChange);
            page.setVisible(true);
            page.addPropertyChangeListener(pagePropertyChange);
        }

    }

    private final class PagePropertyChange implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("visible".equals(evt.getPropertyName())) {
                holderPanel.showFrame(((DialogPage) evt.getSource()), (Boolean) evt.getNewValue());
            }
        }
    }

    private static final Logger LOGGER = Logger.getLogger(DockingCompositeDialogPage.class);

    public static final String PAGE_PROPERTY_NAME = "pageObject";
    protected HolderPanel holderPanel = null;
    private boolean isControlCreating = false;

    private ActivateFrameAdapter activateFrameAdapter = null;
    private final PagePropertyChange pagePropertyChange = new PagePropertyChange();

    /**
     * Costruttore.
     *
     * @param pageId
     *            id della composite
     */
    public DockingCompositeDialogPage(final String pageId) {
        super(pageId);
    }

    /**
     * Per ogni pagina configurata, crea un frame e lo aggiunge.
     */
    protected void addFrames() {

        LOGGER.debug("--> Enter addFrames");
        if (getPages().isEmpty()) {
            LOGGER.warn("--> Non ho pagine da caricare in questa dockingCompositePage " + getId());
            return;
        }

        for (final DialogPage page : getDialogPages()) {
            LOGGER.debug("--> addFrame per page " + page.getId());
            addPageAndFrame(page);
        }
        LOGGER.debug("--> activePage di pages(0) " + getPages().get(0));
        setActivePage((DialogPage) getPages().get(0));
        LOGGER.debug("--> Exit addFrames");
    }

    /**
     * @param page
     *            aggiunge una pagina e crea subito il frame.
     */
    public void addPageAndFrame(DialogPage page) {
        // Se la pagina non è già stata aggiunta la aggiungo alla lista delle pagine
        if (!getPages().contains(page)) {
            addPage(page);
        }
        page.addPropertyChangeListener(pagePropertyChange);
        // Recupero l'icona del frame.
        Icon icon = RcpSupport.getIcon(page.getId() + "Command.icon");
        if (icon == null) {
            icon = RcpSupport.getIcon("tileWindow.icon");
        }

        // Il titolo del frame coincide con la label del command
        String titleCommand = page.getId() + "Command.label";
        String title = getMessage(titleCommand);
        if (title.equals(titleCommand)) {
            title = getMessage(this.getId() + "." + page.getId() + "Command.label");
            if (title.endsWith("Command.label")) {
                System.out.println(this.getId() + "." + page.getId() + "Command.label=");
            }
        }

        final DockableFrame frame = createDockableFrame(title, icon, page);
        frame.setBorder(BorderFactory.createEmptyBorder());
        frame.setTabTitle(title);
        frame.setFrameIcon(icon);
        frame.setKey(page.getId());
        frame.addDockableFrameListener(activateFrameAdapter);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(getDockingManager().getAllFrames().size());
        JComponent controlPage = page.getControl();
        if (!(page instanceof FormBackedDialogPageEditor)) {
            controlPage.setBorder(BorderFactory.createLineBorder(Color.gray));
        }
        frame.getContentPane().add(controlPage);
        ((JPanel) frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder());
        frame.setAvailableButtons(DockableFrame.BUTTON_AUTOHIDE);
        frame.putClientProperty(PAGE_PROPERTY_NAME, page);

        LOGGER.debug("Titolo della pagina " + page.getTitle());
        LOGGER.debug("--> aggiungo il frame al docking manager ");
        holderPanel.getDockingManager().addFrame(frame);

        configureFrame(frame);

        LOGGER.debug("--> addFrame completato ");

    }

    /**
     * Metodo da sovrascrivere per poter configurare ogni singolo frame creato.
     *
     * @param frame
     *            frame
     */
    protected void configureFrame(DockableFrame frame) {
        LOGGER.debug("--> Enter configureFrame");
    }

    /**
     * Crea il frame da utilizzare.
     *
     * @param title
     *            titolo del frame
     * @param icon
     *            icona del frame
     * @param page
     *            pagina associata al frame
     * @return frame creato
     */
    public DockableFrame createDockableFrame(String title, Icon icon, DialogPage page) {
        // page qui non viene usata. Ma se devo sovrascrivere la creazione del frame potrebbe
        // servire
        return new DockableFrame(title, icon);
    }

    @Override
    protected JComponent createPageControl() {
        LOGGER.debug("-->ENTER createPageControl");
        isControlCreating = true;
        holderPanel = new HolderPanel(getActiveWindow().getControl());
        activateFrameAdapter = new ActivateFrameAdapter();
        addFrames();

        if (!getPages().isEmpty()) {
            setActivePage((DialogPage) getPages().get(0));
        }
        isControlCreating = false;

        return holderPanel;
    }

    @Override
    public void dispose() {
        LOGGER.debug("-->ENTER dispose");
        super.dispose();
        for (DialogPage page : getDialogPages()) {
            if (holderPanel != null) {
                if (holderPanel.getFrame(page.getId()) != null) {
                    holderPanel.getFrame(page.getId()).putClientProperty(PAGE_PROPERTY_NAME, null);
                    holderPanel.getFrame(page.getId()).removeDockableFrameListener(activateFrameAdapter);
                    holderPanel.getDockingManager().removeFrame(page.getId());
                    ((IPageLifecycleAdvisor) page).dispose();
                }
                try {
                    page.removePropertyChangeListener(pagePropertyChange);
                } catch (Exception e) {
                    LOGGER.warn("Errore nella dispose", e);
                }
            }
        }

        for (DockableFrame frame : getFrames()) {
            frame.dispose();
        }

        activateFrameAdapter = null;

        if (holderPanel != null) {
            WindowListener[] listeners = ((Window) holderPanel.getDockingManager().getRootPaneContainer())
                    .getWindowListeners();
            for (WindowListener listener : listeners) {
                ((Window) holderPanel.getDockingManager().getRootPaneContainer()).removeWindowListener(listener);
            }
            holderPanel.getDockingManager().removeAllFrames();
            holderPanel.getDockingManager().dispose();
            holderPanel.removeAll();
            holderPanel.dispose();
            holderPanel = null;
        }

        LOGGER.debug("-->ENTER dispose");
    }

    /**
     * Ritorna il docking manager della pagina.
     *
     * @return DockingManager docking manager
     */
    public DockingManager getDockingManager() {
        return holderPanel.getDockingManager();
    }

    /**
     * Ritorna il DockableFrame associato alla key.
     *
     * @param key
     *            l'id della page
     * @return DockableFrame
     */
    protected DockableFrame getFrame(String key) {
        return holderPanel.getFrame(key);
    }

    /**
     * @return restituisce tutti i frame creati
     */
    protected Collection<DockableFrame> getFrames() {

        Collection<DockableFrame> dockableFrames = new ArrayList<DockableFrame>();

        if (holderPanel != null) {
            Collection<String> dokframes = holderPanel.getDockingManager().getAllFrames();
            for (String id : dokframes) {
                DockableFrame dockFrame = holderPanel.getFrame(id);
                dockableFrames.add(dockFrame);
            }
        }
        return dockableFrames;
    }

    /**
     * Rende il frame non accesibile.
     *
     * @param key
     *            il frame rendere inaccessibile
     */
    public void hideFrame(String key) {
        // NPE viste diverse mail dove holderPanel è null, controllo anche questa
        if (holderPanel != null) {
            holderPanel.showFrame(key, false);
        }
    }

    @Override
    public boolean isLayoutSupported() {
        return true;
    }

    @Override
    public boolean isLockLayout() {
        if (holderPanel != null) {
            return holderPanel.isLockLayout();
        }
        return false;
    }

    @Override
    public void lockLayout(boolean lock) {
        // NPE mail
        if (holderPanel != null) {
            holderPanel.lock(lock);
        }
    }

    @Override
    protected void objectChange(Object domainObject, DialogPage pageSource) {
        LOGGER.debug("--> Enter objectChange");
        // Se sto creando le pagine e sto ricevendo un objectchange non devo considerarlo
        // perchè è lancianto da qualche evento sulla setFormObject di una pagina, ma in creazione
        // delle pagine non serve perchè l'oggetto viene sicuramente aggiornato successivamente
        if (pageSource == null) {
            pagesLoaded.clear();
        }
        for (DialogPage page : getDialogPages()) {
            if (isControlCreating && !((AbstractDialogPage) page).isControlCreated()) {
                continue;
            }
            // verifica se null nel caso in cui l'object changed viene dall'esterno
            // (aggiorno l'oggetto dell'editor e di conseguenza della composite contenuta che
            // avverte ogni pagina)
            // e non da una dialog page della jecComposite
            if (pageSource == null || !page.getId().equals(pageSource.getId())) {
                // Sporco e processo le pagine nella composite, esclusa la pagina che ha lanciato
                // l'evento
                pagesDirty.put(page.getId(), true);
                processDialogPage(page);
            }
        }
        LOGGER.debug("--> Exit objectChange");
    }

    /**
     * Rimuove tutti i frame e ricarica le pagine.
     */
    public void reloadPage() {
        getDockingManager().removeAllFrames();
        addFrames();
    }

    /**
     * Rimuove la pagine ed il relativo frame dalla composite.
     *
     * @param page
     *            pagine da rimuovere
     */
    public void removePage(DialogPage page) {
        getPages().remove(page);
        getPagesLinkedList().remove(page.getId());
        pagesDirty.remove(page.getId());
        pagesLoaded.remove(page.getId());
        getDockingManager().removeFrame(page.getId());
    }

    @Override
    public void restoreLayout() {
        // NPE viste diverse mail dove holderPanel è null, controllo anche questa
        if (holderPanel != null) {
            try {
                DockingLayoutManager.restoreCurrentLayout(holderPanel.getDockingManager(), this.getId());
            } catch (Exception ex) {
                holderPanel.getDockingManager().resetToDefault();
                LOGGER.error("-->errore nel restore del layout.NV", ex);
            }
        }
    }

    /**
     * Nel caso di una docking composite devo ripristinare il layout del dockingmanager e le
     * impostazioni per le pagine contenute.
     *
     * @param settings
     *            settings
     */
    @Override
    public void restoreState(Settings settings) {
        // restore anche delle pagine contenute, visto che sulla docking sono tutte visualizzate
        // non mi preoccupo di pagine attive o pagine lazy.
        if (holderPanel != null) {
            holderPanel.getDockingManager().beginLoadLayoutData();
            try {
                holderPanel.getDockingManager().setUseFrameBounds(false);
                holderPanel.getDockingManager().setUseFrameState(false);
                DockingLayoutManager.restoreLayout(holderPanel.getDockingManager(), this.getId());
            } catch (Exception e) {
                LOGGER.error("--> Errore restore DOCKING MANAGER", e);
            }
        }

        for (DialogPage page : getDialogPages()) {
            IPageLifecycleAdvisor pageEditor = (IPageLifecycleAdvisor) page;
            if (isPageLoaded(page.getId())) {
                pageEditor.restoreState(settings);
            }

            if (holderPanel != null) {
                DockableFrame frame = holderPanel.getFrame(page.getId());
                // la pagina che deriva pu? personalizzarsi il frame
                configureFrame(frame);

                // richiamo la funzione per visualizzare o nascondere il frame perchè la restore
                // state viene chiamata dopo la refreshData o loadData della pagina e quindi
                // sovrascriverebbe le
                // impostazioni
                // del frame in base al layout salvato sul file di configurazione
                // checkAndHideFrame(page);
            }
        }
    }

    /**
     * Salvo il layout su un file per ogni editor docking trovato; questo per un bug che si presenta
     * quando salvo il layout di default indifferenziato in una unica sorgente. Inoltre mi preoccupo
     * di salvare le impostazioni di tutte le pagine contenute.
     *
     * @param settings
     *            settings
     */
    @Override
    public void saveState(Settings settings) {
        if (holderPanel != null) {
            try {
                DockingLayoutManager.saveLayout(holderPanel.getDockingManager(), this.getId());
            } catch (Exception e) {
                LOGGER.error("--> Errore restore DOCKING MANAGER", e);
            }
        }
        // e salvo anche le impostazioni delle pagine contenute
        super.saveState(settings);
    }

    @Override
    public void setActivePage(DialogPage activePage) {
        LOGGER.debug("--> Enter setActivePage " + activePage);
        super.setActivePage(activePage);

        /*
         * per evitare un loop devo verificare di non attivare il frame che e' gia' attivo; attivare
         * il frame infatti richiama ActivateFrameAdapter che risetta la pagina attiva e entra in
         * loop
         */

        // NPE Mail
        if (holderPanel != null) {
            for (String keyFrame : getDockingManager().getAllFrames()) {
                if (getDockingManager().getFrame(keyFrame).getClientProperty(PAGE_PROPERTY_NAME) == activePage) {
                    holderPanel.getDockingManager().activateFrame(getDockingManager().getFrame(keyFrame).getKey());
                }
            }
        }
    }

    /**
     * Non utilizzata perchè tutte le pagine in una docked sono visibili.
     *
     * @param page
     *            pagina da visualizzare
     */
    @Override
    protected void showComponentPage(DialogPage page) {
        LOGGER.debug("--> Enter showComponentPage");
    }

    /**
     * Rende il frame accessibile.
     *
     * @param key
     *            il frame da rendere accessibile
     */
    public void showFrame(String key) {
        // NPE viste diverse mail dove holderPanel è null, controllo anche questa
        if (holderPanel != null) {
            holderPanel.getDockingManager().setFrameAvailable(key);
        }
    }

    @Override
    protected void showInitialPage() {
        LOGGER.debug("--> Enter showInitialPage");
        super.showInitialPage();
        LOGGER.debug("--> Exit showInitialPage");
    }
}
