package it.eurotn.rich.editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.util.GuiStandardUtils;

import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.NullPanel;

import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Estende le funzionalit� della FormBackedDialogPage dando la possibilita di gestire piu form, che si riferiscono allo
 * stesso form model, disposti in una tabbed.
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public abstract class FormsBackedTabbedDialogPageEditor extends FormBackedDialogPageEditor implements ChangeListener {

    private static final Logger LOGGER = Logger.getLogger(FormsBackedTabbedDialogPageEditor.class);

    private JTabbedPane tabbedPane = null;

    private Map<Integer, PanjeaAbstractForm> forms;

    /**
     * Costruttore.
     *
     * @param parentPageId
     *            id della pagina
     * @param backingFormPage
     *            form principale della pagina
     */
    public FormsBackedTabbedDialogPageEditor(final String parentPageId, final Form backingFormPage) {
        super(parentPageId, backingFormPage);

        forms = new HashMap<Integer, PanjeaAbstractForm>();
    }

    /**
     * Aggiunge un forms alla pagina.
     *
     * @param form
     *            form da aggiungere
     */
    public void addForm(PanjeaAbstractForm form) {

        forms.put(tabbedPane.getTabCount(), form);

        Icon iconTab = null;
        try {
            iconTab = getIconSource().getIcon(getId() + ".tab." + form.getId() + ".icon");
        } catch (Exception e1) {
            LOGGER.debug("--> Impossibile trovarte l'icona con chiave " + getId() + ".tab." + form.getId() + ".icon");
        }

        tabbedPane.addTab(getMessageSource().getMessage(getId() + ".tab." + form.getId() + ".title", new Object[] {},
                Locale.getDefault()), iconTab, new NullPanel());

    }

    /**
     *
     * Metodo da sovrascrivere per aggiungere i form secondari.
     */
    public abstract void addForms();

    /**
     * Metodo da sovrascrivere se si vuole personalizzare il tabbedPage.
     *
     * @param paramTabbedPane
     *            tabbedPane creato dalla pagina
     */
    protected void configureTabbedPane(JTabbedPane paramTabbedPane) {

    }

    @Override
    public JComponent createControl() {
        tabbedPane = getComponentFactory().createTabbedPane();
        tabbedPane.setName("tabbedPane_" + getId());
        configureTabbedPane(tabbedPane);

        Icon iconTab = null;
        try {
            iconTab = getIconSource().getIcon(getId() + ".tab." + getId() + ".icon");
        } catch (Exception e1) {
            LOGGER.debug("--> Impossibile trovarte l'icona con chiave " + getId() + ".tab." + getId() + ".icon");
        }

        tabbedPane.addTab(
                getMessageSource().getMessage(getId() + ".tab." + getId() + ".title", new Object[] {},
                        Locale.getDefault()),
                iconTab, GuiStandardUtils.attachBorder(getBackingFormPage().getControl()));
        addForms();

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        JComponent toolbar = createToobar();
        if (toolbar != null) {
            rootPanel.add(toolbar, BorderLayout.PAGE_START);
        }

        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        JComponent headComponent = getHeaderTabbedComponent();

        if (headComponent != null) {
            panel.add(headComponent, BorderLayout.NORTH);
        }

        if (insertControlInScrollPane()) {
            JScrollPane scrollPane = getComponentFactory().createScrollPane(tabbedPane);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            panel.add(scrollPane, BorderLayout.CENTER);
        } else {
            panel.add(tabbedPane, BorderLayout.CENTER);
        }

        rootPanel.add(panel, BorderLayout.CENTER);

        // aggiunge gli errori sul titolo della tabbed dialog page
        initPageValidationReporter();
        getBackingFormPage().getFormModel().validate();

        if (defaultController != null) {
            // setto sempre il formModel , non ho problemi perche' e' sempre lo stesso
            defaultController.setFormModel(this.getForm().getFormModel());
            defaultController.register();
        }

        tabbedPane.addChangeListener(this);

        for (Entry<Integer, PanjeaAbstractForm> entries : forms.entrySet()) {
            tabbedPane.setComponentAt(entries.getKey(), entries.getValue().getControl());
        }

        return rootPanel;
    }

    @Override
    public void dispose() {
        super.dispose();

        tabbedPane.removeChangeListener(this);

        for (PanjeaAbstractForm form : forms.values()) {
            form.getFormModel().removeCommitListener(form);
            form.removeGuarded(this);
            form.dispose();
        }

        for (int i = 0; i < tabbedPane.getTabCount() - 1; i++) {
            tabbedPane.removeTabAt(i);
        }
        tabbedPane.removeAll();
        tabbedPane = new JideTabbedPane();
    }

    /**
     *
     * @return componente da inserire sopra il tab
     */
    public JComponent getHeaderTabbedComponent() {
        return null;
    }

    /**
     * @return the tabbedPane
     */
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
     *
     * @return tabbed selezionata
     */
    public int getTabbedSelected() {
        return tabbedPane.getSelectedIndex();
    }

    @Override
    public void grabFocus() {
        LOGGER.debug("---> Enter componentFocusGained per formBackedTabbedPage " + getId());
        Component componentFocusable = null;
        if (tabbedPane != null && tabbedPane.getTabCount() != 0) {
            componentFocusable = findComponentFocusable(tabbedPane.getComponents());
        }
        if (componentFocusable != null && componentFocusable instanceof JComponent) {
            ((JComponent) componentFocusable).requestFocusInWindow();
            if (componentFocusable instanceof JTextField) {
                ((JTextField) componentFocusable).selectAll();
            }
        }
        LOGGER.debug("---> Exit componentFocusGained per formBackedTabbedPage " + getId());
    }

    @Override
    protected boolean insertControlInScrollPane() {
        return false;
    }

    @Override
    public void onNew() {
        super.onNew();
        setTabForm(0);
    }

    @Override
    public boolean onUndo() {
        for (PanjeaAbstractForm form : forms.values()) {
            form.revert();
        }
        return super.onUndo();
    }

    /**
     * Seleziona una tab.
     *
     * @param numTab
     *            numero del tab da selezionare
     */
    public void setTabForm(int numTab) {
        if (numTab > tabbedPane.getTabCount()) {
            LOGGER.warn("--> impostato un tab che non esiste");
            return;
        }
        tabbedPane.setSelectedIndex(numTab);
        if (forms.get(numTab) instanceof Focussable) {
            ((Focussable) forms.get(numTab)).grabFocus();
        } else {
            grabFocus();
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
    }
}
