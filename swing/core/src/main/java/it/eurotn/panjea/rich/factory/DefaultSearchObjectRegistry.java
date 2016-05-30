/**
 * 
 */
package it.eurotn.panjea.rich.factory;

import it.eurotn.rich.search.AbstractSearchObject;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

/**
 * 
 * Implementazione di edfault di {@link SearchObjectRegistry}.
 * 
 * @author adriano
 * @version 1.0, 19/ott/06
 * 
 */
public class DefaultSearchObjectRegistry implements SearchObjectRegistry, InitializingBean {

    private static Logger logger = Logger.getLogger(DefaultSearchObjectRegistry.class);

    private Map<Object, Object> searchObjectMap;
    private SettingsManager settingsManager;

    /**
     * chiamato dopo aver settato le proprità del bean.
     * 
     * @throws Exception
     *             mai rilanciata.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        org.springframework.util.Assert.notNull(settingsManager, "Setting Manager nullo");
    }

    @Override
    public AbstractSearchObject getSearchObject(Class<?> propertySearch) {
        logger.debug("--> Enter getSearchObject");
        String searchObjectBeanId = (String) searchObjectMap.get(propertySearch);
        org.springframework.util.Assert.notNull(searchObjectBeanId, "SearchObject assente per " + propertySearch);
        AbstractSearchObject searchObject = RcpSupport.getBean(searchObjectBeanId);
        try {
            searchObject.setSettings(settingsManager.getUserSettings());
        } catch (SettingsException e) {
            logger.error("--> errore nel caricare user settings", e);
        }
        logger.debug("--> Exit getSearchObject");
        return searchObject;
    }

    @Override
    public AbstractSearchObject getSearchObject(String formPropertyPath) {
        logger.debug("--> Enter getSearchObject");
        String searchObjectBeanId = (String) searchObjectMap.get(formPropertyPath);
        org.springframework.util.Assert.notNull(searchObjectBeanId,
                "SearchObject assente per il property path " + formPropertyPath);
        AbstractSearchObject searchObject = RcpSupport.getBean(searchObjectBeanId);
        try {
            searchObject.setSettings(settingsManager.getUserSettings());
        } catch (SettingsException e) {
            logger.error("--> errore nel caricare user settings", e);
        }
        logger.debug("--> Exit getSearchObject");
        return searchObject;
    }

    /**
     * @param searchObjectMap
     *            The searchObjectMap to set.
     */
    public void setSearchObjectMap(Map<Object, Object> searchObjectMap) {
        this.searchObjectMap = searchObjectMap;
    }

    /**
     * 
     * @param settingsManager
     *            manager per gestire le preferenze dell'applicazione
     */
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }
}
