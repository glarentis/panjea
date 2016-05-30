/**
 * 
 */
package it.eurotn.panjea.rich;

import org.springframework.richclient.application.event.LifecycleApplicationEvent;

/**
 * @author Leonardo
 * 
 */
public class PanjeaLifecycleApplicationEvent extends LifecycleApplicationEvent {

    private static final long serialVersionUID = 2853280063241782892L;

    public static final String SELECTED = "lifecycleEvent.selected";

    public static final String CLEAR_CACHE = "lifecycleEvent.clear..cache";

    public static final String CLOSED = "lifecycleEvent.closed";

    public static final String REFRESHED = "lifecycleEvent.refreshed";

    public static final String CLOSE_APP = "lifecycleEvent.closeApplication";

    public static final String NEXT_EDITOR = "lifecycleEvent.nextEditor";

    public static final String PREV_EDITOR = "lifecycleEvent.prevEditor";

    public static final String START_METHOD = "lifecycleEvent.startMethod";

    public static final String END_METHOD = "lifecycleEvent.endMethod";

    private Object sourceContainer = null;

    /**
     * Costruttore.
     * 
     * @param eventType
     *            tipo di evento
     * @param source
     *            valore contenuto nell'evento
     */
    public PanjeaLifecycleApplicationEvent(final String eventType, final Object source) {
        super(eventType, source);
    }

    /**
     * Costruttore.
     * 
     * @param eventType
     *            tipo di evento
     * @param source
     *            valore contenuto nell'evento
     * @param sourceContainer
     *            il container che gestisce l'oggetto dell'evento, può essere null
     */
    public PanjeaLifecycleApplicationEvent(final String eventType, final Object source, final Object sourceContainer) {
        super(eventType, source);
        this.sourceContainer = sourceContainer;
    }

    /**
     * @return the sourceContainer
     */
    public Object getSourceContainer() {
        return sourceContainer;
    }
}
