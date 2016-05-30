package it.eurotn.panjea.anagrafica.rich.editors.azienda.depositi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.DatiGeograficiBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class DepositoSedeAziendaForm extends PanjeaAbstractForm {

    private class EntitaPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            if (getFormModel().isReadOnly()) {
                return;
            }

            EntitaLite entita = (EntitaLite) evt.getNewValue();

            if (entita == null) {
                getValueModel("sedeEntita").setValue(null);
            } else {
                getValueModel("sedeEntita").setValue(anagraficaBD.caricaSedePrincipaleEntita(entita.creaProxyEntita()));
            }
        }

    }

    private static final String FORM_ID = "depositoSedeAziendaForm";
    private final IAnagraficaTabelleBD anagraficaTabelleBD;

    private IAnagraficaBD anagraficaBD;

    private EntitaPropertyChangeListener entitaPropertyChangeListener;
    private List<SedeAzienda> sediAzienda;

    private RefreshableValueHolder sediValueHolder;

    /**
     * Costruttore.
     *
     * @param deposito
     *            deposito da gestire *
     * @param anagraficaTabelleBD
     *            bd dell'tabelle
     */
    public DepositoSedeAziendaForm(final Deposito deposito, final IAnagraficaTabelleBD anagraficaTabelleBD) {
        super(PanjeaFormModelHelper.createFormModel(deposito, false, "depositoSedeAziendaForm"), FORM_ID);
        this.anagraficaTabelleBD = anagraficaTabelleBD;
        this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);

        TipoEntita tipiEntitaCliente = TipoEntita.CLIENTE;
        ValueModel tipiEntitaClienteValueModel = new ValueHolder(tipiEntitaCliente);
        DefaultFieldMetadata tipiEntitaCLienteData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(tipiEntitaClienteValueModel), TipoEntita.class, true, null);
        getFormModel().add("tipiEntitaCliente", tipiEntitaClienteValueModel, tipiEntitaCLienteData);

        entitaPropertyChangeListener = new EntitaPropertyChangeListener();
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "70dlu,4dlu,left:default, 10dlu, right:pref,4dlu,fill:default, fill:default:grow", "3dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.nextRow();
        builder.setRow(2);

        ((JTextField) builder.addPropertyAndLabel("codice", 1)[1]).setColumns(10);
        builder.addPropertyAndLabel("descrizione", 5);
        builder.nextRow();

        builder.addLabel("sedeDeposito", 1);

        sediValueHolder = new RefreshableValueHolder(new Closure() {

            @Override
            public Object call(Object paramObject) {
                return ObjectUtils.defaultIfNull(sediAzienda, new ArrayList<SedeAzienda>());
            }
        });
        sediValueHolder.refresh();

        builder.setComponentAttributes("f,c");
        ComboBoxBinding sedeComboBinding = (ComboBoxBinding) bf.createBoundComboBox("sedeDeposito", sediValueHolder,
                "descrizione");
        builder.addBinding(sedeComboBinding, 3);
        builder.nextRow();
        builder.setComponentAttributes("l,c");

        DatiGeograficiBinding bindingDatiGeografici = (DatiGeograficiBinding) bf
                .createDatiGeograficiBinding("datiGeografici", "right:70dlu");
        builder.addBinding(bindingDatiGeografici, 1, 6, 8, 1);
        builder.nextRow();

        builder.addPropertyAndLabel("indirizzo", 1);
        builder.addPropertyAndLabel("attivo", 5);
        builder.nextRow();

        builder.addLabel("tipoDeposito", 1);
        builder.addBinding(bf.createBoundComboBox("tipoDeposito",
                new ValueHolder(anagraficaTabelleBD.caricaTipiDepositi()), "codice"), 3);
        builder.addPropertyAndLabel("principale", 5);
        builder.nextRow();

        builder.addLabel("entita", 1);
        Binding bindingEntita = bf.createBoundSearchText("entita",
                new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaCliente" },
                new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY });
        SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(20);
        builder.addBinding(bindingEntita, 3);

        builder.addLabel("sedeEntita", 5);
        Binding sedeEntitaBinding = bf.createBoundSearchText("sedeEntita", null, new String[] { "entita" },
                new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
        builder.addBinding(sedeEntitaBinding, 7);

        getValueModel("entita").addValueChangeListener(entitaPropertyChangeListener);

        return builder.getPanel();
    }

    @Override
    public void dispose() {

        getValueModel("entita").removeValueChangeListener(entitaPropertyChangeListener);
        entitaPropertyChangeListener = null;

        super.dispose();
    }

    /**
     * @param sediAzienda
     *            the sediAzienda to set
     */
    public void setSediAzienda(List<SedeAzienda> sediAzienda) {
        this.sediAzienda = sediAzienda;

        sediValueHolder.refresh();
    }

}
