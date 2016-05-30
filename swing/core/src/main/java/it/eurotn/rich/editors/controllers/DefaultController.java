package it.eurotn.rich.editors.controllers;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.Form;

import it.eurotn.rich.form.FormModelPropertyChangeListeners;

/**
 * Controller di default per associare i controlli, implementati da {@link FormModelPropertyChangeListeners}, ai
 * componenti creati dal {@link Form}.<br>
 * Il controller riceve il FormModel del Form corrente e crea per ogni elemento della Map propertiesChange un
 * ValueChangeListeners per il valore proprietà contenuto nella key <br>
 *
 * Il metodo register scatena la registrazione dei {@link PropertyChangeListener} <br>
 *
 * Esempio di configurazione all'interno del context):
 *
 * <bean id="defaultController" class="it.eurotn.panjea.magazzino.rich.forms.areamagazzino.DefaultController" singleton=
 * "false" lazy-init="true"><br>
 * <property name="propertiesChange"><br>
 * <map><br>
 * <entry key="oggetto1.proprieta"><br>
 * <list><br>
 * <ref bean="oggetto1PropertyChange"></ref><br>
 * </list><br>
 * </entry><br>
 * <entry key="oggetto2.proprieta"><ref bean="oggetto2PropertyChange"></ref></entry><br>
 * </map><br>
 * </property><br>
 * </bean> <br>
 * <br>
 * <bean id="oggetto1PropertyChange" <br>
 * class="foo.oggetto1PropertyChange" singleton="false"<br>
 * lazy-init="true"><br>
 * </bean>
 *
 * <bean id="oggetto2PropertyChange" <br>
 * class="foo.oggetto2PropertyChange" singleton="false" <br>
 * lazy-init="true"><br>
 * <property name="oggettoBD"> <ref bean="oggettoBD"></ref> </property><br>
 * </bean> <br>
 *
 * Importante mappare il bean del DefaultController come singleton=false e lazy-init=true
 *
 *
 *
 * @author adriano
 * @version 1.0, 04/set/2008
 *
 */
public class DefaultController {

    private static final Logger LOGGER = Logger.getLogger(DefaultController.class);

    private boolean registered;
    private FormModel formModel;
    private Map<String, List<FormModelPropertyChangeListeners>> propertiesChange = new HashMap<String, List<FormModelPropertyChangeListeners>>();

    /**
     *
     */
    public DefaultController() {
        super();
        this.registered = false;
    }

    /**
     * Aggiunge un {@link PropertyChangeListener} al formModel per l'attributo propertyName
     *
     * @param propertyName
     *            property name
     * @param formModelPropertyChangeListeners
     *            listener
     */
    public void addPropertyChange(String propertyName,
            FormModelPropertyChangeListeners formModelPropertyChangeListeners) {
        LOGGER.debug("--> Enter addPropertyChange");
        org.springframework.util.Assert.notNull(formModel);
        this.formModel.getValueModel(propertyName).addValueChangeListener(formModelPropertyChangeListeners);
        LOGGER.debug("--> Exit addPropertyChange");
    }

    /**
     * Disabilita i listeners per la proprietà richiesta<br>
     * E' possibile riabilitarli con {@link DefaultController#enableListener(String)}
     *
     * @param propertyName
     *            property name
     */
    public void disableListener(String propertyName) {
        List<FormModelPropertyChangeListeners> listeners = propertiesChange.get(propertyName);
        for (FormModelPropertyChangeListeners formModelPropertyChangeListeners : listeners) {
            formModel.getValueModel(propertyName).removeValueChangeListener(formModelPropertyChangeListeners);
        }
    }

    /**
     * Riabilita i listeners disabilitati da {@link DefaultController#disableListener(String)} per la proprietà
     * richiesta.<br>
     *
     *
     * @param propertyName
     *            property name
     */
    public void enableListener(String propertyName) {
        List<FormModelPropertyChangeListeners> listeners = propertiesChange.get(propertyName);
        for (FormModelPropertyChangeListeners formModelPropertyChangeListeners : listeners) {
            formModel.getValueModel(propertyName).addValueChangeListener(formModelPropertyChangeListeners);
        }
    }

    /**
     * Esegue la registrazione di tutti i {@link FormModelPropertyChangeListeners} contenuti nella Map propertiesChange
     *
     */
    public void register() {
        LOGGER.debug("--> Enter register");
        if (registered) {
            return;
        }
        org.springframework.util.Assert.notNull(formModel);
        Set<String> propertiesName = propertiesChange.keySet();
        List<FormModelPropertyChangeListeners> propertyChangeList;
        for (String propertyName : propertiesName) {
            LOGGER.debug("--> aggiunto propertyChange alla proprietà " + propertyName);
            propertyChangeList = propertiesChange.get(propertyName);
            for (FormModelPropertyChangeListeners formModelPropertyChangeListeners : propertyChangeList) {
                formModelPropertyChangeListeners.setFormModel(formModel);
                formModel.getValueModel(propertyName).addValueChangeListener(formModelPropertyChangeListeners);
            }
        }
        registered = true;
        LOGGER.debug("--> Exit register");
    }

    /**
     * @param formModel
     *            The formModel to set.
     */
    public void setFormModel(FormModel formModel) {
        LOGGER.debug("--> Enter setFormModel");
        this.formModel = formModel;
        LOGGER.debug("--> Exit setFormModel");
    }

    /**
     * @param propertiesChange
     *            The propertiesChange to set.
     */
    public void setPropertiesChange(Map<String, List<FormModelPropertyChangeListeners>> propertiesChange) {
        this.propertiesChange = propertiesChange;
    }

    /**
     * Deregistra tutti i {@link FormModelPropertyChangeListeners} aggiunti al FormModel.
     */
    public void unregistrer() {
        LOGGER.debug("--> Enter unregistrer");
        org.springframework.util.Assert.notNull(formModel);
        Set<String> propertiesName = propertiesChange.keySet();
        List<FormModelPropertyChangeListeners> propertyChangeList;
        for (String propertyName : propertiesName) {
            LOGGER.debug("--> aggiunto propertyChange alla proprietà " + propertyName);
            propertyChangeList = propertiesChange.get(propertyName);
            for (FormModelPropertyChangeListeners formModelPropertyChangeListeners : propertyChangeList) {
                formModel.getValueModel(propertyName).removeValueChangeListener(formModelPropertyChangeListeners);
                formModelPropertyChangeListeners.setFormModel(null);
            }
        }
        registered = false;
    }

}
