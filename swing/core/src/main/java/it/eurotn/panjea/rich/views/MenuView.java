package it.eurotn.panjea.rich.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.progress.BusyIndicator;
import org.springframework.richclient.security.ClientSecurityEvent;
import org.springframework.richclient.security.LoginEvent;
import org.springframework.richclient.security.LogoutEvent;

import com.jidesoft.pane.OutlookTabbedPane;
import com.jidesoft.spring.richclient.docking.view.JideAbstractView;
import com.jidesoft.swing.JideButton;

import it.eurotn.panjea.rich.AbstractMenuPanel;

/**
 * Classe che rappresenta il menu generale dell'applicazione e un secondo gruppo di pulsanti per accedere alle funzioni
 * del software, divisi in pannelli espandibili. <br>
 *
 * @author Leonardo
 */
public class MenuView extends JideAbstractView implements ApplicationListener {

    protected static Logger logger = Logger.getLogger(MenuView.class);
    private static final String ID_VIEW = "menuView";
    private List<AbstractMenuPanel> pannelli;
    // il tab principale contenente le funzioni principali del software
    private OutlookTabbedPane tabbedPane;

    /**
     * Inserisce nel menu un pannello.
     *
     * @param tabPannelli
     *            . .
     */
    public void addTabPanel(AbstractMenuPanel tabPannelli) {
        pannelli.add(tabPannelli);
    }

    /**
     *
     * @param tabPannelli
     *            pannelli da inserire
     */
    public void addTabPanel(List<AbstractMenuPanel> tabPannelli) {
        pannelli.addAll(tabPannelli);
    }

    /**
     * Crea il controllo della vista MenuView.
     *
     * @return componente della vista
     */
    @Override
    protected JComponent createControl() {
        // tab di outlook
        tabbedPane = createTabbedPane();
        tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                // JTree treeMenu = (JTree) ((JPanel) (((JScrollPane) tabbedPane.getSelectedComponent()).getViewport())
                // .getView()).getComponent(0);
                // treeMenu.setSelectionRow(0);
                // treeMenu.requestFocusInWindow();
            }
        });
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.add(tabbedPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea il menu ( Outlook like menu ) aggiungendo i pulsanti per le varie sezioni.
     *
     * @return JTabbedPane
     */
    private OutlookTabbedPane createTabbedPane() {
        tabbedPane = new OutlookTabbedPane() {

            @Override
            protected void customizeButton(AbstractButton paramAbstractButton) {
                paramAbstractButton.setOpaque(true);
                paramAbstractButton.setHorizontalAlignment(10);
                // paramAbstractButton.setMargin(new Insets(3, 6, 4, 6));
                if ((A == 0) && (!(paramAbstractButton instanceof JideButton))) {
                    return;
                }
                // ((JideButton) paramAbstractButton)
                // .setButtonStyle(UIDefaultsLookup.getInt("OutlookTabbedPane.buttonStyle"));
                ((JideButton) paramAbstractButton).setButtonStyle(JideButton.FLAT_STYLE);
                paramAbstractButton.setPreferredSize(new Dimension(200, 24));
            }
        };

        tabbedPane.setPreferredSize(new Dimension(200, 600));
        tabbedPane.setAutoscrolls(true);
        tabbedPane.setName("MenuPrincipale");
        reloadTabs(pannelli);
        return tabbedPane;
    }

    /**
     *
     */
    private void doLogin() {
        logger.debug("--> pannelli.size() " + pannelli.size());
        logger.debug("--> pannelli.size() " + pannelli.hashCode());
        // for (AbstractMenuPanel abstractMenuPanel : pannelli) {
        // abstractMenuPanel.
        // }
        reloadTabs(pannelli);
    }

    /**
     *
     */
    private void doLogout() {
        reloadTabs(new ArrayList<AbstractMenuPanel>());
    }

    @Override
    public String getId() {
        return ID_VIEW;
    }

    /**
     * metodo observer dell'autenticazione che esegue l'aggiornamento dei Tabbed.
     *
     * @param event
     *            evento
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ClientSecurityEvent) {
            if (event instanceof LoginEvent) {
                doLogin();
            } else if (event instanceof LogoutEvent) {
                doLogout();
            }
        }

    }

    /**
     * metodo incaricato di ricaricare i pannelli all'interno della tabbedPane.
     *
     * @param paramPannelli
     *            pannelli
     */
    private void reloadTabs(List<AbstractMenuPanel> paramPannelli) {
        BusyIndicator.showAt(Application.instance().getActiveWindow().getControl());
        try {
            if (tabbedPane == null) {
                return;
            }
            // rimuovi eventuali pannelli esistenti
            tabbedPane.removeAll();
            // Aggiungo i pannelli
            if (pannelli != null) {
                for (AbstractMenuPanel pannello : paramPannelli) {
                    if (pannello.hasElements()) {
                        tabbedPane.addTab(pannello.getTitle(), pannello.getIcon(), pannello.getControl(),
                                pannello.getDescription());
                    }
                }
            }
        } finally {
            BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());
        }

    }

    /**
     *
     * @param tabPannelli
     *            pannelli
     */
    public void setTabPanel(List<AbstractMenuPanel> tabPannelli) {
        if (pannelli == null) {
            pannelli = new ArrayList<AbstractMenuPanel>();
        }
        pannelli.addAll(tabPannelli);
    }
}
