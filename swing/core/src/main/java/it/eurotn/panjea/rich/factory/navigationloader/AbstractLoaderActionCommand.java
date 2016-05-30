/**
 * 
 */
package it.eurotn.panjea.rich.factory.navigationloader;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public abstract class AbstractLoaderActionCommand extends ActionCommand implements ILoaderActionCommand {

    public static final String PARAM_LOADER_OBJECT = "paramLoaderObject";
    public static final String PARAM_LOADER_CONTEXT_OBJECT = "paramLoaderContextObject";

    private NavigationLoaderContext navigationLoaderContext;

    /**
     * Costruttore.
     * 
     * @param commandId
     *            command id
     */
    public AbstractLoaderActionCommand(final String commandId) {
        super(commandId);
        RcpSupport.configure(this);
        this.navigationLoaderContext = NavigationLoaderContext.DEFAULT_CONTEXT;
    }

    /**
     * @return the navigationLoaderContext
     */
    @Override
    public NavigationLoaderContext getNavigationLoaderContext() {
        return navigationLoaderContext;
    }

    /**
     * @param navigationLoaderContext
     *            the navigationLoaderContext to set
     */
    public void setNavigationLoaderContext(NavigationLoaderContext navigationLoaderContext) {
        this.navigationLoaderContext = navigationLoaderContext;
    }

}
