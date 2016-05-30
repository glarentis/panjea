package com.jidesoft.spring.richclient.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.statusbar.StatusBar;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.progress.ProgressMonitor;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.status.MemoryStatusBarItem;
import com.jidesoft.status.ProgressStatusBarItem;
import com.jidesoft.status.ResizeStatusBarItem;
import com.jidesoft.status.StatusBarItem;
import com.jidesoft.status.TimeStatusBarItem;
import com.jidesoft.swing.JideBoxLayout;

import it.eurotn.panjea.anagrafica.rich.statusBarItem.ChangeFontSizeStatusBarItem;
import it.eurotn.panjea.anagrafica.rich.statusBarItem.PanjeaUpdateStatusBarItem;

public class JecStatusBar implements StatusBar {

    private static Logger logger = Logger.getLogger(JecStatusBar.class);

    private static final String ERROR_ICON = "statusbar.error";
    private static final String MESSAGE_ICON = "statusbar.message";
    private com.jidesoft.status.StatusBar statusBar;
    private ProgressStatusBarItem progressItem;
    private ProgressStatusBarAdapter progressMonitor;
    private List<StatusBarItem> statusBarItems;
    private List<StatusBarItem> additionalStatusBarItems;
    private SettingsManager settingsManager;

    /**
     *
     * Costruttore.
     *
     */
    public JecStatusBar() {
        super();

    }

    @Override
    public void clear() {
        updateStatusItem(null, null, Color.BLACK);
    }

    @Override
    public JComponent getControl() {
        if (statusBar == null) {
            initStatusBarItems();
            statusBar = new com.jidesoft.status.StatusBar();
            Iterator<StatusBarItem> it = statusBarItems.iterator();
            StatusBarItem item;
            while (it.hasNext()) {
                item = it.next();
                if (item.getClass().getName()
                        .equals("it.eurotn.panjea.anagrafica.rich.statusBarItem.AziendaStampaStatusBarItem")) {
                    // aggiungo questo solamente se in modalità debug
                    try {
                        if (!settingsManager.getUserSettings().getBoolean("debugMode")) {
                            continue;
                        }
                    } catch (Exception e) {
                        logger.error("--> errore nel leggere la chiave debugMode dai setting. Imposto a false");
                        continue;
                    }
                }
                logger.debug("-->Aggiungo un item nella statusBar " + it.getClass());
                String posizione = JideBoxLayout.FIX;
                if (item instanceof ProgressStatusBarItem) {
                    posizione = JideBoxLayout.VARY;
                }
                statusBar.add(item, posizione);
            }
            progressItem = getProgressStatusBarItem();

            // passo come secondo parametro il valore false perchè non voglio
            // vedere il pulsante Annulla
            progressMonitor = new ProgressStatusBarAdapter(progressItem, false);

        }
        return statusBar;
    }

    private ImageIcon getImageIcon(String key) {
        IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
        Icon icon = iconSource.getIcon(key);
        return (ImageIcon) icon;
    }

    /**
     * Restituisce l'item con il nome specificato.
     *
     * @param name
     *            nome item
     * @return item trovato
     */
    public StatusBarItem getItemByName(String name) {
        return statusBar.getItemByName(name);
    }

    @Override
    public ProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    /**
     * Ritorno il bean per la progressBar.
     *
     * @return {@link ProgressStatusBarItem}
     */
    protected ProgressStatusBarItem getProgressStatusBarItem() {
        ProgressStatusBarItem item = (ProgressStatusBarItem) getItemByName("Status");
        item.setDefaultStatus(null);
        return item;
    }

    /**
     * @return the settingsManager
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    /**
     * Inizializza tutti gli item di default e gli eventuali additionalStatusBarItem configurati su
     * xml.
     */
    private void initStatusBarItems() {
        statusBarItems = new ArrayList<StatusBarItem>();
        statusBarItems.add((StatusBarItem) RcpSupport.getBean("aziendaStatusBarItem"));
        statusBarItems.add((StatusBarItem) RcpSupport.getBean("aziendaStampaStatusBarItem"));
        statusBarItems.add((StatusBarItem) RcpSupport.getBean("userStatusBarItem"));
        statusBarItems.add(new PanjeaUpdateStatusBarItem());
        ProgressStatusBarItem barItem = new ProgressStatusBarItem();
        barItem.getComponent(0).setName("progressStatusBarLabel");
        statusBarItems.add(barItem);
        statusBarItems.add((StatusBarItem) RcpSupport.getBean("panjeaRssStatusBarItem"));
        statusBarItems.add(new ChangeFontSizeStatusBarItem());
        statusBarItems.add(new MemoryStatusBarItem());
        statusBarItems.add(new TimeStatusBarItem());
        for (StatusBarItem addionalStatusBarItem : additionalStatusBarItems) {
            statusBarItems.add(addionalStatusBarItem);
        }
        statusBarItems.add(new ResizeStatusBarItem());
    }

    /**
     * @param additionalStatusBarItems
     *            the additionalStatusBarItems to set
     */
    public void setAdditionalStatusBarItems(List<StatusBarItem> additionalStatusBarItems) {
        this.additionalStatusBarItems = additionalStatusBarItems;
    }

    @Override
    public void setCancelEnabled(boolean enabled) {
    }

    @Override
    public void setErrorMessage(Message errorMessage) {
        setErrorMessage(errorMessage == null ? null : errorMessage.getMessage());
    }

    @Override
    public void setErrorMessage(String message) {
        updateStatusItem(message, getImageIcon(ERROR_ICON), Color.RED);
    }

    @Override
    public void setMessage(Message message) {
        setMessage(message == null ? null : message.getMessage());
    }

    @Override
    public void setMessage(String message) {
        updateStatusItem(message, getImageIcon(MESSAGE_ICON), Color.BLACK);
    }

    /**
     * @param settingsManager
     *            settings manager
     */
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @Override
    public void setVisible(boolean visible) {
        statusBar.setVisible(visible);
    }

    private void updateStatusItem(String message, Icon icon, Color color) {
        // Implementare se si usa la statusBar per ricevere dei messaggi
    }
}