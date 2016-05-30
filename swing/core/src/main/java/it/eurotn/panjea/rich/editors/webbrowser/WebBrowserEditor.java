package it.eurotn.panjea.rich.editors.webbrowser;

import java.awt.BorderLayout;
import java.net.URL;
import java.util.UUID;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.progress.ProgressMonitor;

import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;

/**
 * Editor per visualizzare un {@link URL}.
 *
 * @author Leo
 */
public class WebBrowserEditor extends AbstractEditor implements InitializingBean {

    private static Logger logger = Logger.getLogger(WebBrowserEditor.class);

    private WebView browser;
    private final AbstractControlFactory factory = new AbstractControlFactory() {

        @Override
        public JComponent createControl() {
            final JFXPanel fxPanel = new JFXPanel();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Platform.setImplicitExit(false);
                    fxPanel.setScene(createScene());
                }
            });
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(fxPanel, BorderLayout.CENTER);
            return panel;
        }
    };

    private PanjeaUrl url = null;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void componentOpened() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                url.setEngine(browser.getEngine());
                browser.getEngine().load(url.getUrl().toExternalForm());
            }
        });
        super.componentOpened();
    }

    /**
     *
     * @return scena contenente il browser
     */
    private Scene createScene() {
        browser = new WebView();
        java.net.CookieManager manager = new java.net.CookieManager();
        java.net.CookieHandler.setDefault(manager);
        browser.getEngine().getLoadWorker().workDoneProperty().addListener(url);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, Color.BLACK);
        root.setCenter(browser);
        return (scene);
    }

    @Override
    public void dispose() {
        url.setEngine(null);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                browser.getEngine().getLoadWorker().workDoneProperty().removeListener(url);
            }
        });
        super.dispose();
    }

    @Override
    public JComponent getControl() {
        return factory.getControl();
    }

    @Override
    public String getDisplayName() {
        return url.getDisplayName();
    }

    @Override
    public String getId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void initialize(Object editorObject) {
        if (editorObject != null && editorObject instanceof PanjeaUrl) {
            url = ((PanjeaUrl) editorObject);
            if (logger.isDebugEnabled()) {
                logger.debug("Url da raggiungere " + url.getUrl());
            }
        }
    }

    @Override
    public boolean isEnableCache() {
        return false;
    }

    @Override
    public void save(ProgressMonitor arg0) {
    }

}
