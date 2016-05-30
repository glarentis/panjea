package it.eurotn.rich.binding.value.support;

import java.lang.ref.WeakReference;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class WeakApplicationListener implements ApplicationListener {

    private static final Logger LOGGER = Logger.getLogger(WeakApplicationListener.class);

    WeakReference<RefreshableValueHolderApplicationEvent> listenerRef;

    /**
     *
     * Costruttore.
     *
     * @param refreshableValueHolderApplicationEvent
     *            refreshableValueHolderApplicationEvent
     */
    public WeakApplicationListener(
            final RefreshableValueHolderApplicationEvent refreshableValueHolderApplicationEvent) {
        listenerRef = new WeakReference<RefreshableValueHolderApplicationEvent>(refreshableValueHolderApplicationEvent);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent arg0) {
        LOGGER.debug("--> Ricevuto evento " + arg0);
        LOGGER.debug("listenerRef" + listenerRef.get());
    }

}
