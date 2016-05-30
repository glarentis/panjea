package it.eurotn.rich.binding.searchtext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

/**
 * @author giangi
 * @version 1.0, 10/gen/2011
 */
public class SearchTextBinder extends AbstractBinder {

    private static Logger logger = Logger.getLogger(SearchTextBinder.class);

    public static final String RENDERER_KEY = "rendered";
    public static final String FILTERPROPERTYPATH_KEY = "filterPropertyPath";
    public static final String FILTERNAME_KEY = "filterName";
    public static final String SEARCH_TEXT_CLASS_KEY = "searchTextClass";
    public static final String SEARCH_OBJECT_CLASS_KEY = "searchObjectClassKey";

    private String[] renderProperties = null;
    private String searchTextClass = null;

    /**
     * Costruttore.
     */
    public SearchTextBinder() {
        this(JTextField.class);
    }

    /**
     * @param requiredSourceClass
     *            classe gestista dalla search
     */
    public SearchTextBinder(final Class<?> requiredSourceClass) {
        super(requiredSourceClass, new String[] { RENDERER_KEY, FILTERPROPERTYPATH_KEY, FILTERNAME_KEY,
                SEARCH_TEXT_CLASS_KEY, SEARCH_OBJECT_CLASS_KEY });
    }

    /**
     * 
     * @param requiredSourceClass
     *            .
     * @param supportedContextKeys
     *            .
     */
    public SearchTextBinder(final Class<?> requiredSourceClass, final String[] supportedContextKeys) {
        super(requiredSourceClass, supportedContextKeys);
    }

    @Override
    protected JComponent createControl(@SuppressWarnings("rawtypes") Map context) {
        logger.debug("--> Enter createControl");
        final SearchPanel panel = new SearchPanel();
        logger.debug("--> Exit createControl");
        return panel;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath,
            @SuppressWarnings("rawtypes") Map context) {
        logger.debug("--> Enter doBind");
        org.springframework.util.Assert.isInstanceOf(JPanel.class, control, "Control must be an instance of JPanel.");
        if (context == Collections.EMPTY_MAP) {
            context = new HashMap<String, Object>();
        }
        if (!context.containsKey(RENDERER_KEY)) {
            context.put(RENDERER_KEY, renderProperties);
        }
        if (searchTextClass != null) {
            context.put(SEARCH_TEXT_CLASS_KEY, searchTextClass);
        }

        SearchTextBinding searchTextBinding = new SearchTextBinding((SearchPanel) control, formModel, formPropertyPath,
                context);
        logger.debug("--> Exit doBind");
        return searchTextBinding;
    }

    /**
     * @param renderProperties
     *            The renderProperties to set.
     * @uml.property name="renderProperties"
     */
    public void setRenderProperties(String[] renderProperties) {
        this.renderProperties = renderProperties;
    }

    /**
     * @param searchTextClass
     *            The searchTextClass to set.
     * @uml.property name="searchTextClass"
     */
    public void setSearchTextClass(String searchTextClass) {
        this.searchTextClass = searchTextClass;
    }

}
