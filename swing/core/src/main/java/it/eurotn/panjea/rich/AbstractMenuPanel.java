/**
 *
 */
package it.eurotn.panjea.rich;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.config.ApplicationObjectConfigurer;
import org.springframework.richclient.core.DescriptionConfigurable;
import org.springframework.richclient.core.TitleConfigurable;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.image.config.IconConfigurable;

/**
 * Pannello utilizzato per inserire nel menu' generale.
 *
 * @author giangi
 * @version 1.0, 19/ott/06
 *
 */
public abstract class AbstractMenuPanel extends AbstractControlFactory
        implements TitleConfigurable, IconConfigurable, DescriptionConfigurable, InitializingBean {

    private String title;
    private Icon icon;
    private String id = null;
    private String description;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("--> ID: " + id);
        if (id != null) {
            ApplicationObjectConfigurer configurer = (ApplicationObjectConfigurer) ApplicationServicesLocator.services()
                    .getService(ApplicationObjectConfigurer.class);
            configurer.configure(this, id);
        }
    }

    @Override
    protected abstract JComponent createControl();

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return the icon
     */
    public Icon getIcon() {
        return this.icon;
    }

    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * specifica se il MenuPanel ha elementi al suo interno.
     *
     * @return <code>true</code> se ha elementi
     */
    public abstract boolean hasElements();

    @Override
    public void setCaption(String shortDescription) {
        this.title = shortDescription;
    }

    @Override
    public void setDescription(String longDescription) {
        this.description = longDescription;
    }

    @Override
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }
}
