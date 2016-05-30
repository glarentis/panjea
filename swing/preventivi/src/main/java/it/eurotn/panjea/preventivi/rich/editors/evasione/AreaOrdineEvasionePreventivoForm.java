/**
 * 
 */
package it.eurotn.panjea.preventivi.rich.editors.evasione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.validation.support.RulesValidator;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.Rules;
import org.springframework.rules.factory.Constraints;
import org.springframework.rules.support.DefaultRulesSource;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;

/**
 * @author fattazzo
 * 
 */
public class AreaOrdineEvasionePreventivoForm extends PanjeaAbstractForm {

	private class TipoAreaOrdinePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			TipoAreaOrdine tipoAreaOrdine = (TipoAreaOrdine) evt.getNewValue();

			// controllo che il tipo entita dell'ordine sia uguale a quello del preventivo
			if (tipoAreaOrdine != null && tipoAreaOrdine.getTipoDocumento().getTipoEntita() != tipoEntitaPreventivo) {
				StringBuilder sbTitle = new StringBuilder("Il tipo entità del tipo documento scelto per l'ordine ( <b>");
				sbTitle.append(ObjectConverterManager.toString(tipoAreaOrdine.getTipoDocumento().getTipoEntita()));
				sbTitle.append("</b> )<br> è diverso da quello presente sul preventivo ( <b>");
				sbTitle.append(ObjectConverterManager.toString(tipoEntitaPreventivo));
				sbTitle.append("</b> )");
				MessageDialog dialog = new MessageDialog("ATTENZIONE", sbTitle.toString());
				dialog.showDialog();
				getFormModel().getValueModel("tipoAreaOrdine").setValue(null);
			}
		}

	}

	public static final String FORM_ID = "areaOrdineEvasionePreventivoForm";

	private PluginManager pluginManager;

	private TipoEntita tipoEntitaPreventivo;

	private TipoAreaOrdinePropertyChange tipoAreaOrdinePropertyChange;

	/**
	 * Costruttore.
	 */
	public AreaOrdineEvasionePreventivoForm() {
		super(PanjeaFormModelHelper.createFormModel(new AreaOrdine(), false, FORM_ID), FORM_ID);
		pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		tipoAreaOrdinePropertyChange = new TipoAreaOrdinePropertyChange();

		// aggiungo la finta proprietà tipiEntita per far si che la search text degli agenti mi selezioni solo agenti e
		// non altri tipi entità
		List<TipoEntita> tipiEntitaAgente = new ArrayList<TipoEntita>();
		tipiEntitaAgente.add(TipoEntita.AGENTE);

		ValueModel tipiEntitaAgenteValueModel = new ValueHolder(tipiEntitaAgente);
		DefaultFieldMetadata tipiEntitaAgenteData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaAgenteValueModel), List.class, true, null);
		getFormModel().add("tipiEntitaAgente", tipiEntitaAgenteValueModel, tipiEntitaAgenteData);

		DefaultRulesSource rulesSource = new DefaultRulesSource();
		Rules rules = new Rules(AreaOrdine.class) {

			@Override
			protected void initRules() {
				add("tipoAreaOrdine", Constraints.instance().required());
				add("depositoOrigine", Constraints.instance().required());
				add("documento.dataDocumento", Constraints.instance().required());
				add("dataConsegna", Constraints.instance().required());
				if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
					add("agente", Constraints.instance().required());
				}
			}
		};
		rulesSource.addRules(rules);
		getFormModel().setValidator(new RulesValidator(getFormModel(), rulesSource));
		getFormModel().validate();
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,fill:150dlu, 10dlu, right:pref,4dlu,fill:pref,10dlu, right:pref,4dlu,fill:pref",
				"4dlu,default,4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addLabel("tipoAreaOrdine", 1);
		Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaOrdine", new String[] { "tipoDocumento.codice",
				"tipoDocumento.descrizione" });
		SearchPanel tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDoc, 3);
		tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
		tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(23);

		builder.addPropertyAndLabel("documento.dataDocumento", 5);

		builder.addPropertyAndLabel("dataConsegna", 9);

		builder.nextRow();

		builder.addLabel("areaOrdine.depositoOrigine", 1);
		Binding bindDepOrigine = bf.createBoundSearchText("depositoOrigine", new String[] { "codice", "descrizione" });
		SearchPanel searchPanelDepositoOrigine = (SearchPanel) builder.addBinding(bindDepOrigine, 3);
		searchPanelDepositoOrigine.getTextFields().get("codice").setColumns(5);
		searchPanelDepositoOrigine.getTextFields().get("descrizione").setColumns(20);

		if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
			builder.addLabel("agente", 5);
			Binding bindingAgente = bf.createBoundSearchText("agente", new String[] { "codice",
					"anagrafica.denominazione" }, new String[] { "tipiEntitaAgente" },
					new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class);
			SearchPanel agenteSearchPanel = (SearchPanel) builder.addBinding(bindingAgente, 7, 4, 5, 1);
			agenteSearchPanel.getTextFields().get("codice").setColumns(5);
			agenteSearchPanel.getTextFields().get("anagrafica.denominazione").setColumns(20);
		}

		getFormModel().getValueModel("tipoAreaOrdine").addValueChangeListener(tipoAreaOrdinePropertyChange);

		return builder.getPanel();
	}

	@Override
	public void dispose() {
		getFormModel().getValueModel("tipoAreaOrdine").removeValueChangeListener(tipoAreaOrdinePropertyChange);
		super.dispose();
	}

	/**
	 * @param tipoEntitaPreventivo
	 *            the tipoEntitaPreventivo to set
	 */
	public void setTipoEntitaPreventivo(TipoEntita tipoEntitaPreventivo) {
		this.tipoEntitaPreventivo = tipoEntitaPreventivo;
	}

}
