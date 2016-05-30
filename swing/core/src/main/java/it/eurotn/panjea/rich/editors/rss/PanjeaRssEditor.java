package it.eurotn.panjea.rich.editors.rss;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.richclient.progress.ProgressMonitor;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.rss.FeedReader;
import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;

public class PanjeaRssEditor extends AbstractEditor {

    private FeedReader feedReader = null;
    private JPanel rootPanel;

    /**
     * Costruttore.
     */
    public PanjeaRssEditor() {
        super();
        feedReader = RcpSupport.getBean("panjeaFeedReader");
    }

    @Override
    public JComponent getControl() {
        if (rootPanel == null) {
            rootPanel = new JPanel(new BorderLayout());
            rootPanel.add(new JScrollPane(feedReader), BorderLayout.CENTER);
        }
        return rootPanel;
    }

    @Override
    public String getId() {
        return "panjeaRssEditor";
    }

    @Override
    public void initialize(Object editorObject) {
        // non faccio niente
    }

    @Override
    public void save(ProgressMonitor arg0) {
        // non faccio niente
    }

}
