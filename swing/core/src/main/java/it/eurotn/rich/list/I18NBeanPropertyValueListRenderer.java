package it.eurotn.rich.list;

import java.util.Locale;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.MessageSource;
import org.springframework.richclient.list.BeanPropertyValueListRenderer;

/**
 * Come <code>BeanPropertyValueListRenderer</code> si incarica di wrappare l'oggetto ma il metodo
 * <code>getTextValue</code> retituisce il valore caricato tramite il <code>MessageSource</code> usando come chiave il
 * valore della proprietà.
 *
 * Se la proprietà non è di tipo stringa non viene lanciato un errore ma viene restituito il suo velore.
 *
 * @author fattazzo
 *
 */
public class I18NBeanPropertyValueListRenderer extends BeanPropertyValueListRenderer {

    private static final long serialVersionUID = 7965739215683611777L;

    private BeanWrapperImpl beanWrapper;

    private final MessageSource messageSource;

    /**
     * Costruttore di default.
     *
     * @param propertyName
     *            la proprietà da renderizzare
     * @param messageSource
     *            il messageSource da cui trovare il messaggio internazionalizzato
     */
    public I18NBeanPropertyValueListRenderer(final String propertyName, final MessageSource messageSource) {
        super(propertyName);
        this.messageSource = messageSource;
    }

    @Override
    protected String getTextValue(Object value) {
        if (value == null) {
            return "";
        }
        if (beanWrapper == null) {
            beanWrapper = new BeanWrapperImpl(value);
        } else {
            beanWrapper.setWrappedInstance(value);
        }

        // controllo l'istanza della proprietà
        if (beanWrapper.getPropertyValue(getPropertyName()) instanceof String) {
            // se è stringa carico il valore dal message source
            return messageSource.getMessage(String.valueOf(beanWrapper.getPropertyValue(getPropertyName())),
                    new Object[] {}, Locale.getDefault());
        } else {
            // altrimenti restituisco il suo valore
            return String.valueOf(beanWrapper.getPropertyValue(getPropertyName()));
        }
    }

}
