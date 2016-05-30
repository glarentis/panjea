package it.eurotn.rich.control.table;

import java.beans.IntrospectionException;

import org.springframework.binding.form.FieldFaceSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;

import com.jidesoft.grid.Property;
import com.jidesoft.introspector.BeanIntrospector;

/**
 * Crea un beanIntrospector con le prorprietà internazionalizzate.
 * 
 * @author giangi
 */
public final class BeanIntrospectorI18NBuilder {
    /**
     * Restituisce un BeanInstrospector con le proprietà displayName internazionalizzate.
     * 
     * @param id
     *            utilizzato per l'internazionalizzazione. Se trovo il pattern <code>id.nomeproprietà</code> nel
     *            properties utilizzo quella stringa altrimenti utilizzo <code>nomeproprietà</code>
     * @param beanClass
     *            beanClass
     * @param beanProperties
     *            beanProperties
     * @return BeanInstrospector con le proprietà displayName internazionalizzate
     */
    public static BeanIntrospector buildBeanIntrospector(String id, Class beanClass, String[] beanProperties) {
        String[] proprieta = new String[beanProperties.length * 3];
        for (int i = 0; i < beanProperties.length; i++) {
            proprieta[i * 3] = beanProperties[i];
            proprieta[i * 3 + 1] = "";
            proprieta[i * 3 + 2] = "";
        }

        BeanIntrospector beanIntrospector;
        try {
            beanIntrospector = new BeanIntrospector(beanClass, proprieta);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        if (Application.isLoaded()) {
            FieldFaceSource fieldFaceSource = (FieldFaceSource) ApplicationServicesLocator.services()
                    .getService(org.springframework.binding.form.FieldFaceSource.class);

            for (String propertyName : beanIntrospector.getPropertyNames()) {
                Property p = beanIntrospector.getProperty(propertyName);

                p.setDisplayName(fieldFaceSource.getFieldFace(propertyName, id).getLabelInfo().getText());
            }
        }
        return beanIntrospector;
    }

    /**
     * Classe di utilità, quindi metodi solamente statici e costruttore privato.
     */
    private BeanIntrospectorI18NBuilder() {
    }
}
