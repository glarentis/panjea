package it.eurotn.rich.weaklistener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public class WeakFocusListener implements FocusListener {

    private WeakReference listenerRef;
    private Object obj;

    /**
     * Costruttore.
     * 
     * @param listenerRef
     * @param obj
     */
    public WeakFocusListener(FocusListener listener, Object obj) {
        super();
        this.listenerRef = new WeakReference(listener);
        this.obj = obj;
    }

    @Override
    public void focusGained(FocusEvent e) {
        FocusListener listener = (FocusListener) listenerRef.get();

        if (listener == null) {
            removeListener();
        } else {
            listener.focusGained(e);
        }

    }

    @Override
    public void focusLost(FocusEvent e) {
        FocusListener listener = (FocusListener) listenerRef.get();

        if (listener == null) {
            removeListener();
        } else {
            listener.focusLost(e);
        }

    }

    private void removeListener() {
        try {
            Method method = obj.getClass().getMethod("removeFocusListener", new Class[] { FocusListener.class });
            method.invoke(obj, new Object[] { this });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
