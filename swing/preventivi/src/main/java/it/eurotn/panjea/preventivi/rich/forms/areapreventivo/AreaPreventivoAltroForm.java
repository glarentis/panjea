package it.eurotn.panjea.preventivi.rich.forms.areapreventivo;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.AreaDocumentoAreaRateVisiblePropertyChange;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.AreaDocumentoCodiceIvaAlternativoPropertyChange;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.AreaDocumentoListiniEnabledPropertyChange;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

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

public class AreaPreventivoAltroForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "areaPreventivoAltroForm";

	private PluginManager pluginManager;

	private JComponent[] codicePagamentoComponents = null;

	private JComponent[] componentListino;
	private JComponent[] componentListinoAlternativo;
	private JComponent[] codiceIvaAlternativoComponents;

	private final String column1Size = "170";

	private final String column2Size = "180";

	/**
	 * 
	 * @param formModel
	 *            formmodel
	 */
	public AreaPreventivoAltroForm(final FormModel formModel) {
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

		getFormModel().getFieldMetadata("areaPreventivo.listino").addPropertyChangeListener(
				FormModel.ENABLED_PROPERTY,
				new AreaDocumentoListiniEnabledPropertyChange(getFormModel(), componentListino,
						componentListinoAlternativo));

		// getFormModel().getValueModel("areaOrdine.tipoAreaOrdine").addValueChangeListener(
		// new TipoAreaMagazzinoPropertyChange());

		getFormModel().getValueModel("areaPreventivo.tipologiaCodiceIvaAlternativo").addValueChangeListener(
				new AreaDocumentoCodiceIvaAlternativoPropertyChange(getFormModel(), getFormModel().getValueModel(
						"areaPreventivo.tipologiaCodiceIvaAlternativo"), codiceIvaAlternativoComponents));

		getFormModel().getFormObjectHolder().addValueChangeListener(
				new AreaDocumentoCodiceIvaAlternativoPropertyChange(getFormModel(), getFormModel().getValueModel(
						"areaPreventivo.tipologiaCodiceIvaAlternativo"), codiceIvaAlternativoComponents));
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
		componentListinoAlternativo = builder.add(getListinoBinding(bf, "areaPreventivo.listinoAlternativo"),
				"align=left colSpec=" + column1Size + "dlu");
		builder.row();
		componentListino = builder.add(getListinoBinding(bf, "areaPreventivo.listino"), "align=left colSpec="
				+ column2Size + "dlu");
		builder.row();

		ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo = (ETipologiaCodiceIvaAlternativo) getFormModel()
				.getValueModel("areaPreventivo.tipologiaCodiceIvaAlternativo").getValue();

		codiceIvaAlternativoComponents = builder.add(bf.createBoundSearchText("areaPreventivo.codiceIvaAlternativo",
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
