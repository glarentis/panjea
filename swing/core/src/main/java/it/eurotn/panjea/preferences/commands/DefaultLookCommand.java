package it.eurotn.panjea.preferences.commands;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.office2003.Office2003Painter;
import com.jidesoft.utils.SystemInfo;

public class DefaultLookCommand extends ApplicationWindowAwareCommand {
    private static final Logger LOGGER = Logger.getLogger(ApplicationWindowAwareCommand.class);
    private static final String COMMAND_ID = "defaultLooksCommand";
    private final int windowsStyle = LookAndFeelFactory.ECLIPSE_STYLE;
    private final int windowsXPStyle = LookAndFeelFactory.OFFICE2003_STYLE;
    private final int linuxStyle = LookAndFeelFactory.XERTO_STYLE;

    private final String linuxLNF = LookAndFeelFactory.METAL_LNF;
    private final String macosxLNF = LookAndFeelFactory.AQUA_LNF;
    private final String windowsLNF = LookAndFeelFactory.WINDOWS_LNF;
    private final String xplatformLNF = LookAndFeelFactory.METAL_LNF;

    private SettingsManager settingsManager;

    /**
     * repristina le preferenze(skin e colori) da panjeauser.properties.
     */
    public DefaultLookCommand() {
        super(COMMAND_ID);
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
        if (SystemInfo.isWindows() && isLookAndFeelInstalled(windowsLNF)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Detected and using windows look and feel " + windowsLNF);
            }
            try {
                UIManager.setLookAndFeel(windowsLNF);
            } catch (Exception e) {

                e.printStackTrace();
            }
            if (SystemInfo.isWindowsXP()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Using Windows XP style " + windowsXPStyle);
                }
                LookAndFeelFactory.installJideExtension(windowsXPStyle);
                Office2003Painter.setNative(true);
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Using Windows style " + windowsStyle);
                }
                LookAndFeelFactory.installJideExtension(windowsStyle);
            }
        } else if (SystemInfo.isLinux() && isLookAndFeelInstalled(linuxLNF)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Detected and using Linux look and feel " + linuxLNF);
            }
            try {
                UIManager.setLookAndFeel(linuxLNF);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LookAndFeelFactory.installJideExtension(linuxStyle);
        } else if (SystemInfo.isMacOSX() && isLookAndFeelInstalled(macosxLNF)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Detected and using MacOS X look and feel " + macosxLNF);
            }
            try {
                UIManager.setLookAndFeel(macosxLNF);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LookAndFeelFactory.installJideExtension();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Using cross platform look and feel " + xplatformLNF);
            }
            try {
                UIManager.setLookAndFeel(xplatformLNF);
            } catch (Exception e) {
                e.printStackTrace();
            }

            LookAndFeelFactory.installJideExtension();
        }

        try {
            settingsManager = (SettingsManager) Application.services().getService(SettingsManager.class);
            settingsManager.getUserSettings().setString("look_and_feel_theme",
                    MetalLookAndFeel.getCurrentTheme().getClass().getCanonicalName());
            settingsManager.getUserSettings().setString("look_and_feel_style",
                    UIManager.getSystemLookAndFeelClassName());
            settingsManager.getUserSettings().save();

        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.updateComponentTreeUI(Application.instance().getActiveWindow().getControl());
    }

    /**
     * @param lookAndFeelName
     *            lookAndFeelName
     * @return is installed
     */
    private boolean isLookAndFeelInstalled(String lookAndFeelName) {

        try {
            Class.forName(lookAndFeelName).newInstance();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Found look and feel " + lookAndFeelName);
            }
            return true;
        } catch (InstantiationException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Could not instantiate " + lookAndFeelName);
            }
            return false;
        } catch (IllegalAccessException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Illegal attempt to access " + lookAndFeelName);
            }
            return false;
        } catch (ClassNotFoundException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Could not find " + lookAndFeelName);
            }
            return false;
        }
    }
}
