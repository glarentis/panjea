package it.eurotn.panjea.rich.lock.pm;

import it.eurotn.locking.DefaultLock;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.ApplicationServicesLocator;

public class LockPM {

    private DefaultLock lock;

    private String classLock;

    private MessageSource messageSource;

    /**
     * Costruttore.
     */
    public LockPM() {
        super();
    }

    /**
     * 
     * Costruttore.
     * 
     * @param lock
     *            lock
     */
    public LockPM(final DefaultLock lock) {
        this.lock = lock;
        this.classLock = getMessageSource().getMessage(this.lock.getClassObj(), new Object[] {},
                this.lock.getClassObj(), Locale.getDefault());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LockPM other = (LockPM) obj;
        if (lock == null) {
            if (other.lock != null) {
                return false;
            }
        } else if (!lock.equals(other.lock)) {
            return false;
        }
        return true;
    }

    /**
     * @return the classLock
     */
    public String getClassLock() {
        return classLock;
    }

    /**
     * @return the lock
     */
    public DefaultLock getLock() {
        return lock;
    }

    /**
     * @return message source
     */
    protected MessageSource getMessageSource() {
        if (messageSource == null) {
            messageSource = (MessageSource) ApplicationServicesLocator.services().getService(MessageSource.class);
        }
        return messageSource;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lock == null) ? 0 : lock.hashCode());
        return result;
    }
}
