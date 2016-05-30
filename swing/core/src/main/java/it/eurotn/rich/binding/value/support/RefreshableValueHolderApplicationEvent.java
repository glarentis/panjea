package it.eurotn.rich.binding.value.support;

import java.util.ArrayList;

import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.richclient.application.Application;
import org.springframework.rules.closure.Closure;

/**
 * @author fattazzo
 *
 */
public class RefreshableValueHolderApplicationEvent extends RefreshableValueHolder {

    private final Class<?> objectClass;

    /**
     *
     * Costruttore.
     *
     * @param objectClass
     *            object class
     */
    public RefreshableValueHolderApplicationEvent(final Class<?> objectClass) {
        super(new Closure() {

            @Override
            public Object call(Object arg0) {
                return new ArrayList<Object>();
            }

        }, false, false);
        this.objectClass = objectClass;
        getApplicationEventMulticaster().addApplicationListener(new WeakApplicationListener(this));
    }

    /**
     *
     * Costruttore.
     *
     * @param refreshFunction
     *            closure
     * @param objectClass
     *            object class
     * @param refresh
     *            refresh
     */
    public RefreshableValueHolderApplicationEvent(final Closure refreshFunction, final Class<?> objectClass,
            final boolean refresh) {
        super(refreshFunction, false, refresh);
        this.objectClass = objectClass;
        getApplicationEventMulticaster().addApplicationListener(new WeakApplicationListener(this));
    }

    /**
     * @return application multicaster
     */
    public ApplicationEventMulticaster getApplicationEventMulticaster() {
        if (Application.instance().getApplicationContext() != null) {
            final String beanName = AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME;
            if (Application.instance().getApplicationContext().containsBean(beanName)) {
                return (ApplicationEventMulticaster) Application.instance().getApplicationContext().getBean(beanName);
            }
        }
        return null;
    }

    /**
     * @return object class
     */
    public Class<?> getObjectClass() {
        return objectClass;
    }
}
