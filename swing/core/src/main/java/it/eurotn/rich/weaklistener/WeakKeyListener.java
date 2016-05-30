package it.eurotn.rich.weaklistener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public class WeakKeyListener implements KeyListener {

    private WeakReference listenerRef;
    private Object obj;

    /**
     * Costruttore.
     * 
     * @param listenerRef
     * @param obj
     */
    public WeakKeyListener(KeyListener listener, Object obj) {
        super();
        this.listenerRef = new WeakReference(listener);
        this.obj = obj;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        KeyListener listener = (KeyListener) listenerRef.get();

        if (listener == null) {
            removeListener();
        } else {
            listener.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        KeyListener listener = (KeyListener) listenerRef.get();

        if (listener == null) {
            removeListener();
        } else {
            listener.keyReleased(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        KeyListener listener = (KeyListener) listenerRef.get();

        if (listener == null) {
            removeListener();
        } else {
            listener.keyTyped(e);
        }

    }

    private void removeListener() {
        try {
            Method method = obj.getClass().getMethod("removeKeyListener", new Class[] { KeyListener.class });
            method.invoke(obj, new Object[] { this });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
