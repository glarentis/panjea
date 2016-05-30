package it.eurotn.panjea.ordini.rich.forms.areaordine;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.AreaDocumentoAreaRateVisiblePropertyChange;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.AreaDocumentoCodiceIvaAlternativoPropertyChange;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.AreaDocumentoListiniEnabledPropertyChange;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.util.RcpSupport;

public class AreaOrdineAltroForm extends PanjeaAbstractForm {

	private class VisibleAgentePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			TipoAreaOrdine tipoAreaOrdine = (TipoAreaOrdine) getFormModel().getValueModel("areaOrdine.tipoAreaOrdine")
					.getValue();

			boolean visibleAgente = tipoAreaOrdine != null
					&& tipoAreaOrdine.getTipoDocumento().getTipoEntita() == TipoEntita.CLIENTE;

			for (int i = 0; i < agenteComponents.length; i++) {
				agenteComponents[i].setVisible(visibleAgente);
			}

		}

	}

	public static final String FORM_ID = "areaOrdineAltroForm";

	private JComponent[] codicePagamentoComponents = null;
	private JComponent[] componentListino;
	private JComponent[] componentListinoAlternativo;
	private JComponent[] codiceIvaAlternativoComponents;

	private VisibleAgentePropertyChange visibleAgentePropertyChange = new VisibleAgentePropertyChange();

	private final String column1Size = "170";
	private final String column2Size = "180";

	private JComponent[] agenteComponents;

	private PluginManager pluginManager = null;

	/**
	 * Costruttore di default.
	 * 
	 * @param formModel
	 *            form model associato al form
	 */
	public AreaOrdineAltroForm(final FormModel formModel) {
		super(formModel, FORM_ID);

		pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text degli agenti mi selezioni solo agenti e
		// non altri tipi entità
		List<TipoEntita> tipiEntitaAgente = new ArrayList<TipoEntita>();
		tipiEntitaAgente.add(TipoEntita.AGENTE);

		ValueModel tipiEntitaAgenteValueModel = new ValueHolder(tipiEntitaAgente);
		DefaultFieldMetadata tipiEntitaAgenteData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaAgenteValueModel), List.class, true, null);
		getFormModel().add("tipiEntitaAgente", tipiEntitaAgenteValueModel, tipiEntitaAgenteData);
	}

	/**
	 * Aggiunge un {@link PropertyChangeListener} sulla proprietà enabled ai metadata degli attributi del FormModel<br>
	 * per interagire sui componenti.
	 */
	private void addListeners() {

		getFormModel().getFieldMetadata("areaRate").addPropertyChangeListener(FormModel.ENABLED_PROPERTY,
				new AreaDocumentoAreaRateVisiblePropertyChange(getFormModel(), codicePagamentoComponents, null));

		getFormModel().getFieldMetadata("areaOrdine.listino").addPropertyChangeListener(
				FormModel.ENABLED_PROPERTY,
				new AreaDocumentoListiniEnabledPropertyChange(getFormModel(), componentListino,
						componentListinoAlternativo));

		// getFormModel().getValueModel("areaOrdine.tipoAreaOrdine").addValueChangeListener(
		// new TipoAreaMagazzinoPropertyChange());

		getFormModel().getValueModel("areaOrdine.tipologiaCodiceIvaAlternativo").addValueChangeListener(
				new AreaDocumentoCodiceIvaAlternativoPropertyChange(getFormModel(), getFormModel().getValueModel(
						"areaOrdine.tipologiaCodiceIvaAlternativo"), codiceIvaAlternativoComponents));

		getFormModel().getFormObjectHolder().addValueChangeListener(
				new AreaDocumentoCodiceIvaAlternativoPropertyChange(getFormModel(), getFormModel().getValueModel(
						"areaOrdine.tipologiaCodiceIvaAlternativo"), codiceIvaAlternativoComponents));

		if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
			getFormModel().getValueModel("areaOrdine.tipoAreaOrdine").addValueChangeListener(
					visibleAgentePropertyChange);
			addFormObjectChangeListener(visibleAgentePropertyChange);
		}
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		codicePagamentoComponents = builder
				.add(bf.createBoundSearchText("areaRate.codicePagamento", new String[] { "codicePagamento",
						"descrizione" }), "align=left");
		((SearchPanel) (codicePagamentoComponents[1])).getTextFields().get("codicePagamento").setColumns(8);
		((SearchPanel) (codicePagamentoComponents[1])).getTextFields().get("descrizione").setColumns(35);
		builder.row();
		componentListinoAlternativo = builder.add(getListinoBinding(bf, "areaOrdine.listinoAlternativo"),
				"align=left colSpec=" + column1Size + "dlu");
		builder.row();
		componentListino = builder.add(getListinoBinding(bf, "areaOrdine.listino"), "align=left colSpec=" + column2Size
				+ "dlu");
		builder.row();

		if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
			agenteComponents = builder.add(bf.createBoundSearchText("areaOrdine.agente", new String[] { "codice",
					"anagrafica.denominazione" }, new String[] { "tipiEntitaAgente" },
					new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class));

			((SearchPanel) agenteComponents[1]).getTextFields().get("codice").setColumns(5);
			((SearchPanel) agenteComponents[1]).getTextFields().get("anagrafica.denominazione").setColumns(20);

			builder.row();
		}

		ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo = (ETipologiaCodiceIvaAlternativo) getFormModel()
				.getValueModel("areaOrdine.tipologiaCodiceIvaAlternativo").getValue();

		codiceIvaAlternativoComponents = builder.add(bf.createBoundSearchText("areaOrdine.codiceIvaAlternativo",
				new String[] { "codice" }));
		SearchPanel searchPanel = (SearchPanel) codiceIvaAlternativoComponents[1];
		searchPanel.getTextFields().get("codice").setColumns(6);

		if (tipologiaCodiceIvaAlternativo != ETipologiaCodiceIvaAlternativo.ESENZIONE) {
			setCodiceIvaAlternativoVisible(tipologiaCodiceIvaAlternativo != ETipologiaCodiceIvaAlternativo.NESSUNO);
		}
		builder.row();

		addListeners();

		return builder.getForm();
	}

	@Override
	public void dispose() {
		super.dispose();

		if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
			getFormModel().getValueModel("areaOrdine.tipoAreaOrdine").removeValueChangeListener(
					visibleAgentePropertyChange);
			removeFormObjectChangeListener(visibleAgentePropertyChange);
			visibleAgentePropertyChange = null;
		}
	}

	/**
	 * 
	 * @param bf
	 *            binding factory
	 * @param proprietaListino
	 *            listino o listinoAlternativo. Utilizzo un unico metodo per creare i due binding
	 * @return binding per il listino o listino alternativo
	 */
	private Binding getListinoBinding(PanjeaSwingBindingFactory bf, String proprietaListino) {
		Binding bindingListino = bf.createBoundSearchText(proprietaListino, new String[] { "codice" });
		((SearchPanel) bindingListino.getControl()).getTextFields().get("codice").setColumns(10);
		return bindingListino;
	}

	/**
	 * Aggiorna lo stato dei componenti del codice iva alternativo.
	 * 
	 * @param visible
	 *            <code>true</code> per visualizzare i componenti
	 */
	private void setCodiceIvaAlternativoVisible(boolean visible) {
		for (JComponent component : codiceIvaAlternativoComponents) {
			component.setVisible(visible);
		}
	}
}
