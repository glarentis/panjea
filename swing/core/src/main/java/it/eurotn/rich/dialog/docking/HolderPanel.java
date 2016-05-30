package it.eurotn.rich.dialog.docking;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.RootPaneContainer;

import org.springframework.richclient.dialog.DialogPage;

import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockableHolderPanel;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.plaf.basic.BasicJideTabbedPaneUI;
import com.jidesoft.swing.JideTabbedPane;

public class HolderPanel extends DockableHolderPanel {

    private static final long serialVersionUID = -8676366614389300464L;
    private boolean lockLayout = true;

    /**
     * @param rootContainer
     *            container
     */
    public HolderPanel(final RootPaneContainer rootContainer) {
        super(rootContainer);
        getDockingManager().setFloatable(true);
        getDockingManager().resetToDefault();
        getDockingManager().setAutohideAllTabs(true);
        getDockingManager().setEasyTabDock(true);
        getDockingManager().setShowGripper(true);
        getDockingManager().setShowDividerGripper(true);
        getDockingManager().setSensitiveAreaSize(20);
        getDockingManager().setDoubleClickAction(DockingManager.DOUBLE_CLICK_TO_MAXIMIZE);
        getDockingManager().getWorkspace().setVisible(false);
        getDockingManager().setRearrangable(false);
        getDockingManager().setAutohideAllTabs(true);
        setBorder(BorderFactory.createEmptyBorder());
        getDockingManager().setOutlineMode(DockingManager.FULL_OUTLINE_MODE);

        getDockingManager().setTabbedPaneCustomizer(new DockingManager.TabbedPaneCustomizer() {
            @Override
            public void customize(JideTabbedPane tabbedPane) {
                tabbedPane.setContentBorderInsets(new Insets(0, 0, 0, 0));
                tabbedPane.setUI(new BasicJideTabbedPaneUI() {
                    @Override
                    protected void paintContentBorder(Graphics graphics, int tabPlacement, int selectedIndex) {
                        // non disegno i bordi
                    }
                });
            }
        });

    }

    /**
     *
     * @param key
     *            key of frame
     * @return frame
     */
    public DockableFrame getFrame(String key) {
        return getDockingManager().getFrame(key);
    }

    /**
     *
     * @return true se il layout dei frame è bloccato
     */
    public boolean isLockLayout() {
        return lockLayout;
    }

    /**
     * blocca/sblocca il layout dei frame
     *
     * @param lock
     *            true per blocacre, false per sbloccare
     */
    public void lock(boolean lock) {
        lockLayout = lock;
        getDockingManager().setRearrangable(!lock);
    }

    /**
     *
     * @param dialogPage
     *            dialogPage legata al frame
     * @param show
     *            true visualizza il frame, false lo rimuove
     */
    public void showFrame(DialogPage dialogPage, Boolean show) {
        showFrame(dialogPage.getId(), show);
    }

    /**
     *
     * @param key
     *            chiave
     * @param show
     *            visualizza/nasconde il farme
     */
    public void showFrame(String key, Boolean show) {
        if (show) {
            getDockingManager().showFrame(key);
            getDockingManager().setFrameAvailable(key);
        } else {
            getDockingManager().setFrameUnavailable(key);
            getDockingManager().hideFrame(key);
        }
    }

}
