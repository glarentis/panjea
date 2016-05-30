package it.eurotn.panjea.preferences;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jidesoft.spring.richclient.components.JideLookAndFeelConfigurer;

import it.eurotn.panjea.preferences.commands.DefaultLookCommand;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class AspettoPage extends AbstractDialogPage
        implements IPageLifecycleAdvisor, InitializingBean, ListSelectionListener {

    private static final String PAGE_ID = "aspettoPage";
    private static final String APPLY_LABEL_ID = PAGE_ID + ".applyLabelID";
    private static final String APPLY_LABEL_ICON = PAGE_ID + ".applyLabelIcon";

    private List<LookAndFeel> lookAndFeelList;
    private DefaultListModel themeListModel;
    private SettingsManager settingsManager;
    private JList skinListComponent;
    private String currentSkinName;
    private String currentThemeName;
    private String currentTheme;
    private String currentSkin;

    private JList themeListComponent;

    private JideLookAndFeelConfigurer lookAndFeelConfigurer;

    /**
     * Costruttore.
     */
    protected AspettoPage() {
        super(PAGE_ID);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lookAndFeelList = new ArrayList<LookAndFeel>();
        lookAndFeelList.add(new MetalLookAndFeel());
        lookAndFeelList.add(new Plastic3DLookAndFeel());
        lookAndFeelList.add(new PlasticLookAndFeel());
        lookAndFeelList.add(new PlasticXPLookAndFeel());
    }

    @Override
    protected JComponent createControl() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        DefaultLookCommand defaultCommand = new DefaultLookCommand();
        buttonPanel.add(defaultCommand.createButton(), new FlowLayout(FlowLayout.RIGHT));
        defaultCommand.addCommandInterceptor(new ActionCommandInterceptor() {

            @Override
            public void postExecution(ActionCommand actioncommand) {
                for (LookAndFeel lookAndFeelElement : lookAndFeelList) {
                    if (lookAndFeelElement.getClass().getCanonicalName()
                            .equals(UIManager.getSystemLookAndFeelClassName())) {
                        skinListComponent.setSelectedIndex(lookAndFeelList.indexOf(lookAndFeelElement));
                    }
                }
                themeListComponent.setSelectedIndex(((DefaultListModel) themeListComponent.getModel())
                        .indexOf(MetalLookAndFeel.getCurrentTheme().getName()));

            }

            @Override
            public boolean preExecution(ActionCommand actioncommand) {
                return true;
            }
        });

        JPanel listPanel = new JPanel(new HorizontalLayout(20));
        listPanel.add(createSkinListComponent());
        listPanel.add(createThemeSelectionComponent());

        JPanel topPanel = getComponentFactory().createPanel(new VerticalLayout(20));
        JLabel attentionLabel = getComponentFactory().createLabel(APPLY_LABEL_ID);
        attentionLabel.setIcon(RcpSupport.getIcon(APPLY_LABEL_ICON));
        topPanel.add(attentionLabel);
        topPanel.add(listPanel);

        rootPanel.add(topPanel, BorderLayout.NORTH);
        rootPanel.add(buttonPanel, BorderLayout.CENTER);

        skinListComponent.addListSelectionListener(this);
        themeListComponent.addListSelectionListener(this);
        return rootPanel;
    }

    /**
     *
     * @return jList degli skins presenti
     */
    private JList createSkinListComponent() {
        DefaultListModel skinListModel = new DefaultListModel();

        skinListComponent = getComponentFactory().createList();
        try {
            currentSkin = getSettingsManagerLocal().getUserSettings().getString("look_and_feel_style");
        } catch (SettingsException e) {
            e.printStackTrace();
        }
        for (LookAndFeel skin : lookAndFeelList) {
            skinListModel.addElement(skin.getName());
            if (skin.getClass().getCanonicalName().contains(currentSkin)) {
                currentSkinName = skin.getName();
            }
        }
        skinListComponent.setModel(skinListModel);

        skinListComponent.setOpaque(false);
        skinListComponent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        skinListComponent.setBorder(BorderFactory.createTitledBorder("Skins"));
        if (currentSkin != null && !currentSkin.isEmpty()) {
            skinListComponent
                    .setSelectedIndex(((DefaultListModel) skinListComponent.getModel()).indexOf(currentSkinName));
        }

        return skinListComponent;

    }

    /**
     *
     * @return jList dei temi temi presenti.
     */
    private JList createThemeSelectionComponent() {
        try {
            currentTheme = getSettingsManagerLocal().getUserSettings().getString("look_and_feel_theme");
        } catch (SettingsException e) {
            e.printStackTrace();
        }
        if (themeListComponent == null) {
            themeListModel = new DefaultListModel();
            themeListComponent = getComponentFactory().createList();
            themeListComponent.setModel(themeListModel);
            themeListComponent.setOpaque(false);
            themeListComponent.setBorder(BorderFactory.createTitledBorder("Themes"));
            themeListComponent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            List<DefaultMetalTheme> listaTemi = getPlasticThemeList();
            for (DefaultMetalTheme theme : listaTemi) {
                themeListModel.addElement(theme.getName());
                if (theme.getClass().getCanonicalName().contains(currentTheme)) {
                    currentThemeName = theme.getName();
                }
            }
            if (currentTheme != null && !currentTheme.isEmpty()) {
                themeListComponent
                        .setSelectedIndex(((DefaultListModel) themeListComponent.getModel()).indexOf(currentThemeName));

            }
        }
        if (skinListComponent.getSelectedIndex() > -1
                && !(lookAndFeelList.get(skinListComponent.getSelectedIndex()) instanceof PlasticLookAndFeel)) {
            themeListComponent.setVisible(false);
        }
        return themeListComponent;
    }

    @Override
    public void dispose() {
    }

    /**
     *
     * @return lista per i temi plastic
     */
    @SuppressWarnings("unchecked")
    private List<DefaultMetalTheme> getPlasticThemeList() {
        List<DefaultMetalTheme> temi = LookUtils.getInstalledThemes(new Plastic3DLookAndFeel());
        return temi;

    }

    /**
     *
     * @return settingManager.
     */
    private SettingsManager getSettingsManagerLocal() {
        if (settingsManager == null) {
            settingsManager = (SettingsManager) Application.services().getService(SettingsManager.class);
        }
        return settingsManager;
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return false;
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void restoreState(Settings arg0) {
    }

    @Override
    public void saveState(Settings settings) {
    }

    @Override
    public void setFormObject(Object object) {
    }

    /**
     * @param lookAndFeelConfigurer
     *            the lookAndFeelConfigurer to set
     */
    public void setLookAndFeelConfigurer(JideLookAndFeelConfigurer lookAndFeelConfigurer) {
        this.lookAndFeelConfigurer = lookAndFeelConfigurer;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

    @Override
    public void valueChanged(ListSelectionEvent listselectionevent) {

        if (listselectionevent.getValueIsAdjusting()) {
            return;
        }

        themeListComponent.setVisible(false);
        skinListComponent.setVisible(false);

        try {
            if (themeListComponent.getSelectedValue() == null) {
                themeListComponent.setSelectedIndex(0);
            }

            String skinName = null;
            String themeName = null;
            if (lookAndFeelList.get(skinListComponent.getSelectedIndex()) instanceof PlasticLookAndFeel) {
                themeListComponent.setVisible(true);
                themeName = new StringBuilder("com.jgoodies.looks.plastic.theme.")
                        .append(themeListComponent.getSelectedValue()).toString();

                themeName = themeName.replaceAll(" ", "");
                getSettingsManagerLocal().getUserSettings().setString("look_and_feel_theme", themeName);
            } else {
                themeListComponent.setVisible(false);
            }
            skinName = lookAndFeelList.get(skinListComponent.getSelectedIndex()).getClass().getName();
            getSettingsManagerLocal().getUserSettings().setString("look_and_feel_style", skinName);

            lookAndFeelConfigurer.applyLAF(skinName, themeName);
            getSettingsManagerLocal().getUserSettings().save();

            Application.instance().getActiveWindow().getControl().repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }

        themeListComponent.setVisible(true);
        skinListComponent.setVisible(true);
    }
}