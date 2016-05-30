package it.eurotn.panjea.giroclienti.rich.editors.scheda;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class GiroSedeClienteForm extends PanjeaAbstractForm implements Focussable {

    private class EntitaPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            EntitaLite entita = (EntitaLite) evt.getNewValue();

            if (entita == null || entita.isNew()) {
                getValueModel("giroSedeCliente.sedeEntita").setValue(null);
            } else {
                SedeEntita sedeEntita = anagraficaBD.caricaSedePrincipaleEntita(entita.creaProxyEntita());
                getValueModel("giroSedeCliente.sedeEntita").setValue(sedeEntita);
            }
        }

    }

    private static final String FORM_ID = "giroSedeClienteForm";

    private IAnagraficaBD anagraficaBD;

    private JComponent oraFocusComponent;

    /**
     * Costruttore.
     *
     * @param giroSedeCliente
     *            giro sede cliente
     */
    public GiroSedeClienteForm(final GiroSedeClientePM giroSedeCliente) {
        super(PanjeaFormModelHelper.createFormModel(giroSedeCliente, false, FORM_ID), FORM_ID);

        this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()

        builder.setRow(2);
        builder.addLabel("ora");
        oraFocusComponent = builder.addBinding(bf.createBoundCalendar("giroSedeCliente.ora", "HH:mm", "##:##"), 3);
        builder.nextRow();

        builder.addLabel("entita");
        Binding bindingEntita = bf.createBoundSearchText("entita",
                new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipoEntita" },
                new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY });
        SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(23);
        builder.addBinding(bindingEntita, 3);
        builder.nextRow();

        builder.addLabel("sedeEntita");
        Binding sedeEntitaBinding = bf.createBoundSearchText("giroSedeCliente.sedeEntita", null,
                new String[] { "entita" }, new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
        SearchPanel searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 3);
        searchPanelSede.getTextFields().get(null).setColumns(30);
        builder.nextRow();

        builder.addLabel("utente");
        JComponent componentsUtente = builder
                .addBinding(bf.createBoundSearchText("giroSedeCliente.utente", new String[] { "userName", "nome" }), 3);
        ((SearchPanel) componentsUtente).getTextFields().get("userName").setColumns(5);
        ((SearchPanel) componentsUtente).getTextFields().get("nome").setColumns(23);
        builder.nextRow();

        builder.addPropertyAndLabel("giroSedeCliente.giorno");
        builder.nextRow();

        getFormModel().getValueModel("giroSedeCliente.sedeEntita");
        getFormModel().getValueModel("entita").addValueChangeListener(new EntitaPropertyChangeListener());

        return builder.getPanel();
    }

    @Override
    public void grabFocus() {
        oraFocusComponent.requestFocusInWindow();
    }

}
