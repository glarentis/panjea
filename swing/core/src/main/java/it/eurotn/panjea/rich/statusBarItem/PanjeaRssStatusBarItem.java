package it.eurotn.panjea.rich.statusBarItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.rss.FeedEvent;
import com.jidesoft.rss.FeedEventListener;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.status.ButtonStatusBarItem;

/**
 * @author fattazzo
 *
 */
public class PanjeaRssStatusBarItem extends ButtonStatusBarItem implements FeedEventListener {

    private static final long serialVersionUID = -1444671737131901142L;

    private PanjeaFeedReader feedReader;

    /**
     * Costruttore.
     */
    public PanjeaRssStatusBarItem() {
        super();
        setIcon(RcpSupport.getIcon("rssUpdateCommand.icon"));

        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                OpenEditorEvent event = new OpenEditorEvent("panjeaRssEditor");
                Application.instance().getApplicationContext().publishEvent(event);
            }
        });

        feedReader = RcpSupport.getBean("panjeaFeedReader");
        feedReader.addFeedEventListener(this);
    }

    @Override
    public void eventHappened(FeedEvent paramFeedEvent) {
        String description = feedReader.getUnReadItemsCountDescription();
        if (description.startsWith("0")) {
            description = "";
        }
        setText(description);
        setToolTip(feedReader.getNewsDescription());
    }

}
