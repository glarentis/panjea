package it.eurotn.panjea.contabilita.rich.editors.tabelle.strutturacontabile;

import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.contabilita.rich.bd.ContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class EntitaPMForm extends PanjeaAbstractForm {

    private class FilterEntitaCommand extends JideToggleCommand {

        /**
         * Costruttore.
         */
        public FilterEntitaCommand() {
            super("filterEntitaCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void onDeselection() {
            getValueModel("entitaConStruttura").setValue(null);
            getValueModel("entita").setValue(null);
        }

        @Override
        protected void onSelection() {
            getValueModel("entitaConStruttura")
                    .setValue(contabilitaAnagraficaBD.caricaEntitaConStrutturaContabile(tipoDocumento));
            getValueModel("entita").setValue(null);
        }
    }

    private TipoDocumento tipoDocumento;

    private final IContabilitaAnagraficaBD contabilitaAnagraficaBD;

    /**
     * Costruttore.
     *
     * @param tipoDocumento
     *            tipo documento di riferimento
     */
    public EntitaPMForm(final TipoDocumento tipoDocumento) {
        super(PanjeaFormModelHelper.createFormModel(new EntitaPM(TipoEntita.FORNITORE), false,
                "strutturaContabileControPartitePageEntitaSearch"));
        this.contabilitaAnagraficaBD = RcpSupport.getBean(ContabilitaAnagraficaBD.BEAN_ID);
        this.tipoDocumento = tipoDocumento;

        List<EntitaLite> entitaConStruttura = null;
        ValueModel entitaConStrutturaValueModel = new ValueHolder(entitaConStruttura);
        DefaultFieldMetadata entitaConStrutturaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(entitaConStrutturaValueModel), List.class, false, null);
        getFormModel().add("entitaConStruttura", entitaConStrutturaValueModel, entitaConStrutturaData);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("fill:p:g,fill:pref", "default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        Binding searchBinding = bf
                .createBoundSearchText("entita",
                        new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipoEntita",
                                "entitaConStruttura" },
                new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY, EntitaByTipoSearchObject.RETAIN_LIST_KEY });
        builder.addBinding(searchBinding);
        SearchPanel searchPanelEntita = (SearchPanel) searchBinding.getControl();
        searchPanelEntita.getTextFields().get("codice").setColumns(4);
        searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(20);

        FilterEntitaCommand filterEntitaCommand = new FilterEntitaCommand();
        builder.addComponent(filterEntitaCommand.createButton(), 2);

        return builder.getPanel();
    }

}
