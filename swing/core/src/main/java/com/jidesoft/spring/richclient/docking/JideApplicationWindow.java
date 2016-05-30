/*
 * Copyright 2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jidesoft.spring.richclient.docking;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.application.config.ApplicationWindowConfigurer;
import org.springframework.richclient.application.support.AbstractApplicationWindow;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.docking.DefaultDockableHolder;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.status.StatusBar;

import it.eurotn.panjea.rich.commands.AboutCommand;
import it.eurotn.panjea.rich.commands.PanjeaWikiCommand;

/**
 * An implementation of the Spring RCP ApplicationWindow that uses the JIDE docking framework as the
 * underlying manager. This class adds the ability to specify the active page from the collection of
 * configured pages, and to reload the layout data for a specific page and perspective.
 *
 * @author Tom Corbin
 * @author Jonny Wray
 *
 */
public class JideApplicationWindow extends AbstractApplicationWindow {

    private static Logger logger = Logger.getLogger(JideApplicationWindow.class);
    private DefaultDockableHolder dockableHolder;

    private OpenEditorSearchControl searchControl;
    private StatusBar statusBar;

    /**
     *
     * Costruttore.
     *
     */
    public JideApplicationWindow() {
        super(0);
        this.dockableHolder = new DefaultDockableHolder();
        this.dockableHolder.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.dockableHolder.getDockingManager().setProfileKey("PanjeaLayout");
        this.dockableHolder.getDockingManager().setRearrangable(true);
        this.dockableHolder.getDockingManager().setResizable(true);
        this.dockableHolder.getDockingManager().setContinuousLayout(true);
        this.dockableHolder.getDockingManager().setSensitiveAreaSize(20);
        this.dockableHolder.getDockingManager().setOutlineMode(0);
        this.dockableHolder.getDockingManager().setGroupAllowedOnSidePane(true);
        this.dockableHolder.getDockingManager().setEasyTabDock(true);
        this.dockableHolder.getDockingManager().setShowGripper(true);
        this.dockableHolder.getDockingManager().setShowTitleBar(true);
        this.dockableHolder.getDockingManager().setDoubleClickAction(DockingManager.DOUBLE_CLICK_TO_AUTOHIDE);
        this.dockableHolder.getDockingManager().setHeavyweightComponentEnabled(true);
        this.dockableHolder.getDockingManager().setShowWorkspace(true);
        this.dockableHolder.getDockingManager().setFloatable(true);
    }

    /**
     * attiva la casella per aprire la ricerca globale
     */
    public void activateOpenEditorSearch() {
        searchControl.uninstallListeners();
        searchControl.getTextField().setText("");
        searchControl.getTextField().requestFocusInWindow();
        searchControl.installListeners();
    }

    protected void applyCompactLayout() {
        ApplicationWindowConfigurer configurer = getWindowConfigurer();
        getControl().setTitle(configurer.getTitle());
        getControl().setIconImage(configurer.getImage());
        getControl().setJMenuBar(null);
        statusBar.add(createToolBarControl(), statusBar.getComponentCount() - 1);
        JPanel mainPanel = (JPanel) getControl().getContentPane();
        mainPanel.remove(((BorderLayout) mainPanel.getLayout()).getLayoutComponent(BorderLayout.NORTH));
    }

    protected void applyStandardLayout() {
        ApplicationWindowConfigurer configurer = getWindowConfigurer();
        getControl().setTitle(configurer.getTitle());
        getControl().setIconImage(configurer.getImage());
        getControl().setJMenuBar(createMenuBarControl());
        statusBar.remove(statusBar.getComponentCount() - 1);
        JPanel mainPanel = (JPanel) getControl().getContentPane();
        mainPanel.add(createToolBarControl(), BorderLayout.NORTH);
    }

    /**
     * Overrides the applyStandardLayout by removing the setting of the layout manager and the
     * insertion of the center part of the frame. The JIDE docking framework actually sets these,
     * via the DefaultDockableHolder.
     *
     * @param windowControl
     *            frame che contiene la pagina
     * @param configurer
     *            configurer dell'applicazione
     */
    @Override
    protected void applyStandardLayout(JFrame windowControl, ApplicationWindowConfigurer configurer) {
        logger.info("Applying standard layout");
        windowControl.setTitle(configurer.getTitle());
        windowControl.setIconImage(configurer.getImage());
        windowControl.setJMenuBar(createMenuBarControl());
        windowControl.getContentPane().add(createToolBarControl(), BorderLayout.NORTH);
        windowControl.getContentPane().add(createStatusBarControl(), BorderLayout.SOUTH);
    }

    @Override
    protected JFrame createNewWindowControl() {
        return dockableHolder;
    }

    @Override
    protected JComponent createStatusBarControl() {
        statusBar = (StatusBar) super.createStatusBarControl();
        // statusBar.add(new OpenEditorSearchControl(), 0);
        return statusBar;
    }

    @Override
    protected JComponent createToolBarControl() {
        searchControl = new OpenEditorSearchControl();
        JPanel panelToolbar = new JPanel(new BorderLayout());
        panelToolbar.add(super.createToolBarControl(), BorderLayout.CENTER);

        JPanel eastPanel = new JPanel(new HorizontalLayout(5));
        eastPanel.add(searchControl);
        PanjeaWikiCommand panjeaWikiCommand = RcpSupport.getCommand(PanjeaWikiCommand.COMMAND_ID);
        eastPanel.add(panjeaWikiCommand.createButton());
        AboutCommand aboutCommand = RcpSupport.getCommand(AboutCommand.ID);
        eastPanel.add(aboutCommand.createButton());

        panelToolbar.add(eastPanel, BorderLayout.EAST);
        panelToolbar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        return panelToolbar;
    }

    @Override
    protected JComponent createWindowContentPane() {
        return null;
    }

    /**
     *
     * @return docking manager dell'applicazione.
     */
    public DockingManager getDockingManager() {
        return dockableHolder.getDockingManager();
    }

    @Override
    protected void setActivePage(ApplicationPage page) {
        logger.debug("Setting active page to " + page.getId());
        getPage().getControl();
        ((JideApplicationPage) getPage()).updateShowViewCommands();
    }

    /**
     *
     * @param compactMode
     *            visualizza panjea in compactMode
     */
    public void setCompactMode(boolean compactMode) {
        if (compactMode) {
            applyCompactLayout();
        }
    }
}
