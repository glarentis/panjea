package it.eurotn.panjea.tesoreria.rich.forms.acconto;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.components.ImportoTextField;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class AreaAccontoForm extends PanjeaAbstractForm {

	private class CodiceValutaPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (getFormModel().isReadOnly()) {
				return;
			}

			String codiceValuta = (String) evt.getNewValue();

			if (codiceValuta != null && !codiceValuta.isEmpty()) {
				Importo importo = new Importo();
				importo.setCodiceValuta(codiceValuta);
				importo.setImportoInValuta(BigDecimal.ZERO);
				importo.setImportoInValutaAzienda(BigDecimal.ZERO);
				importo.setTassoDiCambio(BigDecimal.ONE);

				getFormModel().getValueModel("documento.totale").setValue(importo);
			}
		}

	}

	private class TipoEntitaChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			TipoEntita tipoEntita = (TipoEntita) evt.getNewValue();
			if (tipoEntita != null) {
				boolean visible = !tipoEntita.equals(TipoEntita.AZIENDA);

				labelEntita.setVisible(visible);
				searchPanelEntita.setVisible(visible);
				labelSedeEntita.setVisible(visible);
				searchPanelSede.setVisible(visible);
			}
		}
	}

	public static final String FORM_ID = "areaAccontoForm";

	public static final String FORMMODEL_ID = "areaAccontoForm";
	private SearchPanel searchPanelEntita;

	private JLabel labelEntita;
	private JLabel labelSedeEntita;
	private SearchPanel searchPanelSede;

	/**
	 * Costruttore.
	 * 
	 */
	public AreaAccontoForm() {
		super(PanjeaFormModelHelper.createFormModel(new AreaAcconto(), false, FORMMODEL_ID), FORM_ID);

		// Aggiungo al formModel il value model dinamino per il tipo entita
		ValueModel tipoEntitaValueModel = new ValueHolder(TipoEntita.CLIENTE);
		DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(), new FormModelMediatingValueModel(
				tipoEntitaValueModel), TipoEntita.class, true, null);
		getFormModel().add("tipoEntita", tipoEntitaValueModel, metaData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:default,4dlu,left:default, 10dlu, right:default,4dlu,left:default",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);
		builder.addPropertyAndLabel("documento.dataDocumento");
		builder.addLabel("documento.codice", 5);
		builder.addBinding(bf.createBoundLabel("documento.codice.codice"), 7);

		builder.nextRow();
		labelEntita = builder.addLabel("documento.entita", 1, 4);
		Binding bindingEntita = bf.createBoundSearchText("documento.entita", new String[] { "codice",
				"anagrafica.denominazione" }, new String[] { "tipoEntita" },
				new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY });
		searchPanelEntita = (SearchPanel) builder.addBinding(bindingEntita, 3, 4, 5, 1);
		searchPanelEntita.getTextFields().get("codice").setColumns(5);

		labelSedeEntita = builder.addLabel("documento.sedeEntita", 9);
		Binding sedeEntitaBinding = bf.createBoundSearchText("documento.sedeEntita", null,
				new String[] { "documento.entita" }, new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
		searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 11);
		// searchPanelSede.getTextFields().get(null).setColumns(18);
		builder.nextRow();

		builder.nextRow();
		builder.addLabel("rapportoBancarioAzienda", 1, 6);
		Binding bindingBancaAzienda = bf.createBoundSearchText("rapportoBancarioAzienda", new String[] { "numero",
				"descrizione" });
		SearchPanel bancaCodSearchText = (SearchPanel) builder.addBinding(bindingBancaAzienda, 3, 6, 5, 1);
		bancaCodSearchText.getTextFields().get("numero").setColumns(5);

		builder.nextRow();

		builder.addLabel("documento.totale.codiceValuta", 1, 8);
		Binding valutaBinding = bf.createBoundSearchText("documento.totale.codiceValuta", null, ValutaAzienda.class);
		SearchPanel valutaSearchPanel = (SearchPanel) builder.addBinding(valutaBinding, 3, 8);
		valutaSearchPanel.getTextFields().get(null).setColumns(10);

		builder.addLabel("documento.totale", 5, 8);
		Binding totAccontoBinding = bf.createBoundImportoTassoCalcolatoTextField("documento.totale",
				"documento.dataDocumento");
		ImportoTextField importoTextField = (ImportoTextField) builder.addBinding(totAccontoBinding, 7, 8);
		importoTextField.setColumns(10);

		builder.nextRow();
		builder.addPropertyAndLabel("automatico");

		builder.nextRow();
		builder.addPropertyAndLabel("note", 1, 12, 5, 1);

		getValueModel("tipoEntita").addValueChangeListener(new TipoEntitaChangeListener());
		getValueModel("documento.totale.codiceValuta").addValueChangeListener(new CodiceValutaPropertyChangeListener());

		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				AreaAcconto areaAcconto = (AreaAcconto) getFormObject();
				getFormModel().getFieldMetadata("documento.totale.codiceValuta").setReadOnly(false);
				if (areaAcconto != null && areaAcconto.getId() != null) {
					getFormModel().getFieldMetadata("documento.totale.codiceValuta").setReadOnly(true);
				}
			}
		});

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		AreaAcconto areaAcconto = new AreaAcconto();
		TipoEntita tipoEntita = (TipoEntita) getValueModel("tipoEntita").getValue();
		if (tipoEntita.equals(TipoEntita.AZIENDA)) {
			areaAcconto.setNote("Acconto IVA");
		}
		return areaAcconto;
	}

	/**
	 * @param tipoEntita
	 *            setta il tipo di entita
	 */
	public void setTipoEntita(TipoEntita tipoEntita) {
		getValueModel("tipoEntita").setValue(tipoEntita);
	}

}
