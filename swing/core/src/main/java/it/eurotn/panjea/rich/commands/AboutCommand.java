package it.eurotn.panjea.rich.commands;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.util.Assert;

import it.eurotn.panjea.plugin.Plugin;
import it.eurotn.panjea.plugin.PluginManager;

public class AboutCommand extends ApplicationWindowAwareCommand implements InitializingBean {

    private class AboutDialog extends MessageDialog {

        /**
         * Costruttore.
         */
        public AboutDialog() {
            super("About", null, "message");

            setPreferredSize(new Dimension(600, 500));
        }

        @Override
        protected JComponent createDialogContentPane() {
            JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

            Image logo = getLogoImage();
            if (logo != null) {
                JLabel logoLabel = new JLabel(new ImageIcon(logo));
                rootPanel.add(logoLabel, BorderLayout.NORTH);
            }

            JTextPane pluginTextPane = new JTextPane();
            pluginTextPane.setOpaque(false);
            ((DefaultCaret) pluginTextPane.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
            pluginTextPane.setContentType("text/html");
            pluginTextPane.setText(getPluginsText());
            pluginTextPane.setEditable(false);
            pluginTextPane.setFocusable(false);

            JScrollPane scrollPane = new JScrollPane(pluginTextPane);
            scrollPane.setBorder(null);
            rootPanel.add(scrollPane, BorderLayout.CENTER);

            return rootPanel;
        }

        /**
         * @return Immagine configurata per il logo di Panjea
         */
        private Image getLogoImage() {
            Image image = null;
            try {
                image = loadImage(imageResourcePath);
            } catch (IOException e) {
                logger.error("Unable to load image from resource " + imageResourcePath, e);
            }
            return image;
        }

        /**
         * Scrive in html la lista dei plugins trovati da pluginManager presentandoli in una tabella.
         *
         * @return html generato
         */
        private String getPluginsText() {
            logger.debug("--> Enter getPluginsText");
            List<Plugin> plugins = pluginManager.getPlugins();
            logger.debug("--> plugin trovati " + plugins.size());
            StringBuffer buf = new StringBuffer();
            if (plugins.size() > 0) {
                buf.append("<span style='font-weight:bold;'>Plugin</span>");

                buf.append("<ul>");
                for (Plugin plugin : plugins) {
                    buf.append("<li>" + plugin.getDescrizione() + "</li>");
                }
                buf.append("</ul>");
            }
            return buf.toString();
        }

        private Image loadImage(Resource path) throws IOException {
            URL url = path.getURL();
            if (url == null) {
                logger.warn("Unable to locate splash screen in classpath at: " + path);
                return null;
            }
            return Toolkit.getDefaultToolkit().createImage(url);
        }

    }

    public static final String ID = "aboutCommand";

    private AboutBox aboutBox = new AboutBox();

    private AboutDialog aboutDialog;

    private PluginManager pluginManager;

    private Resource imageResourcePath;

    /**
     * Costruttore.
     */
    public AboutCommand() {
        super(ID);
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        aboutDialog = new AboutDialog();
    }

    @Override
    protected void doExecuteCommand() {
        aboutDialog.showDialog();
    }

    /**
     *
     * @param path
     *            path testo about
     */
    public void setAboutTextPath(Resource path) {
        // crea una Resource da una stringa
        // Resource res = new ByteArrayResource(getPluginsText().getBytes());
        this.aboutBox.setAboutTextPath(path);
    }

    /**
     * 
     * @param path
     *            set path image
     */
    public void setImageResourcePath(Resource path) {
        Assert.notNull(path, "The splash screen image resource path is required");
        this.imageResourcePath = path;
    }

    /**
     * @param pluginManager
     *            the pluginManager to set
     */
    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }
}