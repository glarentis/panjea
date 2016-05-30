package it.eurotn.panjea.rich.editors.webbrowser;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.Assert;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class WebBrowserCommand extends ApplicationWindowAwareCommand {

    protected static Logger logger = Logger.getLogger(WebBrowserCommand.class);
    private PanjeaUrl url = null;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(url);
    }

    @Override
    protected void doExecuteCommand() {
        LifecycleApplicationEvent event = new OpenEditorEvent(url);
        Application.instance().getApplicationContext().publishEvent(event);
    }

    /**
     * @return the url
     */
    public PanjeaUrl getUrl() {
        return url;
    }

    @Override
    protected void setId(String id) {
        super.setId(id);
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(PanjeaUrl url) {
        this.url = url;
    }

}
