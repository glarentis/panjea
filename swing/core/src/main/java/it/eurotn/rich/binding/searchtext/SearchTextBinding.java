package it.eurotn.rich.binding.searchtext;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.support.AbstractBinding;

/**
 *
 * @author giangi
 * @version 1.0, 10/gen/2011
 */
public class SearchTextBinding extends AbstractBinding {
    /**
     * @uml.property name="searchPanel"
     * @uml.associationEnd
     */
    private SearchPanel searchPanel;

    /**
     *
     * @param panel
     *            searchPanel che contiene le searchText
     * @param formModel
     *            formModel del binding
     * @param formPropertyPath
     *            property path legato al binding
     * @param context
     *            contesto del binding
     */
    public SearchTextBinding(final SearchPanel panel, final FormModel formModel, final String formPropertyPath,
            final Map<String, Object> context) {
        super(formModel, formPropertyPath, null);
        this.searchPanel = panel;
        applyContext(context);
    }

    /**
     *
     * @param context
     *            applica i parametri contenuti nel context.
     */
    private void applyContext(Map<String, Object> context) {
        Map<String, String> parameters = new HashMap<String, String>();
        String[] stringsParameter = (String[]) context.get(SearchTextBinder.FILTERNAME_KEY);
        String[] stringsParameterPath = (String[]) context.get(SearchTextBinder.FILTERPROPERTYPATH_KEY);
        if ((stringsParameter != null) && (stringsParameterPath != null)) {
            for (int i = 0; i < stringsParameter.length; i++) {
                parameters.put(stringsParameter[i], stringsParameterPath[i]);
            }
        }
        searchPanel.configure(this.getProperty(), context, this.getFormModel(), parameters);
        new SearchTextController(searchPanel, parameters);
    }

    @Override
    protected JComponent doBindControl() {
        return searchPanel;
    }

    @Override
    protected void enabledChanged() {
        searchPanel.setEnabled(isEnabled());
        // searchPanel.setReadOnly(!isEnabled());
    }

    @Override
    protected void readOnlyChanged() {
        searchPanel.setReadOnly(isReadOnly());
    }
}