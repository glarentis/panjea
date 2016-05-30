package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.TableFormBuilder;

import it.eurotn.panjea.anagrafica.rich.search.ContrattoSpesometroSearchObject;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 *
 * @author fattazzo
 */
public class AreaMagazzinoAltroForm extends PanjeaAbstractForm {

	private class RaggruppamentoVisiblePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			logger.debug("--> Enter propertyChange");
			if (!getFormModel().isEnabled()) {
				return;
			}
			if ("enabled".equals(evt.getPropertyName())) {
				boolean visible = ((Boolean) evt.getNewValue()).booleanValue();
				for (JComponent component : raggrupBolle) {
					component.setVisible(visible);
				}
			}
		}

	}

	private class TipoAreaMagazzinoPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			TipoAreaMagazzino tipoAreaMagazzino = (TipoAreaMagazzino) getFormModel()
					.getValueModel("areaMagazzino.tipoAreaMagazzino").getValue();

			// nascondo o visualizzo il flag raggruppamento bolle in base
			// all'esistenza o meno del documento di
			// destinazione sul tipo area magazzino.
			if (tipoAreaMagazzino != null && tipoAreaMagazzino.getTipoDocumentoPerFatturazione() == null) {
				setRaggrupBolleVisible(false);
			} else {
				setRaggrupBolleVisible(true);
			}

			boolean contrattoVisible = tipoAreaMagazzino != null
					&& tipoAreaMagazzino.getTipoDocumento().isGestioneContratto();
			componentsContratto[0].setVisible(contrattoVisible);
			componentsContratto[1].setVisible(contrattoVisible);
		}

		/**
		 * @param visible
		 *            visualizza o no i controlli del raggruppamento bolle
		 */
		private void setRaggrupBolleVisible(boolean visible) {
			for (JComponent component : raggrupBolle) {
				component.setVisible(visible);
			}
		}

	}

	public static final String FORM_ID = "areaMagazzinoAltroForm";

	private final String column1Size = "170";
	private final String column2Size = "180";

	private JComponent[] codicePagamentoComponents = null;
	private JComponent[] raggrupBolle;
	private JComponent[] componentSpeseIncassos;
	private JComponent[] componentListino;
	private JComponent[] componentListinoAlternativo;
	private JComponent[] codiceIvaAlternativoComponents;
	private JComponent[] componentsContratto;

	private AreaDocumentoAreaRateVisiblePropertyChange areaDocumentoAreaRateVisiblePropertyChange;
	private RaggruppamentoVisiblePropertyChange raggruppamentoVisiblePropertyChange;
	private AreaDocumentoListiniEnabledPropertyChange areaDocumentoListiniEnabledPropertyChange;
	private TipoAreaMagazzinoPropertyChange tipoAreaMagazzinoPropertyChange;
	private AreaDocumentoCodiceIvaAlternativoPropertyChange areaDocumentoCodiceIvaAlternativoPropertyChange;

	/**
	 * Costruttore di default.
	 *
	 * @param formModel
	 *            form model associato al form
	 */
	public AreaMagazzinoAltroForm(final FormModel formModel) {
		super(formModel, FORM_ID);
	}

	/**
	 * Aggiunge un {@link PropertyChangeListener} sulla proprietà enabled ai metadata degli attributi del FormModel<br>
	 * per interagire sui componenti.
	 */
	private void addListeners() {

		areaDocumentoAreaRateVisiblePropertyChange = new AreaDocumentoAreaRateVisiblePropertyChange(getFormModel(),
				codicePagamentoComponents, componentSpeseIncassos);
		getFormModel().getFieldMetadata("areaRate").addPropertyChangeListener(FormModel.ENABLED_PROPERTY,
				areaDocumentoAreaRateVisiblePropertyChange);

		raggruppamentoVisiblePropertyChange = new RaggruppamentoVisiblePropertyChange();
		getFormModel().getFieldMetadata("areaMagazzino.raggruppamentoBolle")
				.addPropertyChangeListener(FormModel.ENABLED_PROPERTY, raggruppamentoVisiblePropertyChange);

		areaDocumentoListiniEnabledPropertyChange = new AreaDocumentoListiniEnabledPropertyChange(getFormModel(),
				componentListino, componentListinoAlternativo);
		getFormModel().getFieldMetadata("areaMagazzino.listino").addPropertyChangeListener(FormModel.ENABLED_PROPERTY,
				areaDocumentoListiniEnabledPropertyChange);

		tipoAreaMagazzinoPropertyChange = new TipoAreaMagazzinoPropertyChange();
		getFormModel().getValueModel("areaMagazzino.tipoAreaMagazzino")
				.addValueChangeListener(tipoAreaMagazzinoPropertyChange);

		areaDocumentoCodiceIvaAlternativoPropertyChange = new AreaDocumentoCodiceIvaAlternativoPropertyChange(
				getFormModel(), getFormModel().getValueModel("areaMagazzino.tipologiaCodiceIvaAlternativo"),
				codiceIvaAlternativoComponents);
		getFormModel().getValueModel("areaMagazzino.tipologiaCodiceIvaAlternativo")
				.addValueChangeListener(areaDocumentoCodiceIvaAlternativoPropertyChange);

		getFormModel().getFormObjectHolder().addValueChangeListener(areaDocumentoCodiceIvaAlternativoPropertyChange);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		codicePagamentoComponents = builder.add(
				bf.createBoundSearchText("areaRate.codicePagamento", new String[] { "codicePagamento", "descrizione" }),
				"align=left");
		((SearchPanel) (codicePagamentoComponents[1])).getTextFields().get("codicePagamento").setColumns(8);
		((SearchPanel) (codicePagamentoComponents[1])).getTextFields().get("descrizione").setColumns(35);
		builder.row();
		componentListinoAlternativo = builder.add(getListinoBinding(bf, "areaMagazzino.listinoAlternativo"),
				"align=left colSpec=" + column1Size + "dlu");
		builder.add(bf.createBoundCheckBox("areaMagazzino.contoTerzi"), "align=left");
		builder.row();
		componentListino = builder.add(getListinoBinding(bf, "areaMagazzino.listino"),
				"align=left colSpec=" + column2Size + "dlu");
		builder.row();
		componentsContratto = builder.add(getContrattiBinding(bf), "align=left colSpec=" + column1Size + "dlu");
		builder.row();
		componentSpeseIncassos = builder.add("areaMagazzino.addebitoSpeseIncasso", "align=left");
		raggrupBolle = builder.add("areaMagazzino.raggruppamentoBolle", "align=left");
		builder.row();

		ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo = (ETipologiaCodiceIvaAlternativo) getFormModel()
				.getValueModel("areaMagazzino.tipologiaCodiceIvaAlternativo").getValue();

		codiceIvaAlternativoComponents = builder.add(
				bf.createBoundSearchText("areaMagazzino.codiceIvaAlternativo", new String[] { "codice" }),
				"align=left");
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

		getFormModel().getFieldMetadata("areaRate").removePropertyChangeListener(FormModel.ENABLED_PROPERTY,
				areaDocumentoAreaRateVisiblePropertyChange);

		getFormModel().getFieldMetadata("areaMagazzino.raggruppamentoBolle")
				.removePropertyChangeListener(FormModel.ENABLED_PROPERTY, raggruppamentoVisiblePropertyChange);

		getFormModel().getFieldMetadata("areaMagazzino.listino")
				.removePropertyChangeListener(FormModel.ENABLED_PROPERTY, areaDocumentoListiniEnabledPropertyChange);

		getFormModel().getValueModel("areaMagazzino.tipoAreaMagazzino")
				.removeValueChangeListener(tipoAreaMagazzinoPropertyChange);

		getFormModel().getValueModel("areaMagazzino.tipologiaCodiceIvaAlternativo")
				.removeValueChangeListener(areaDocumentoCodiceIvaAlternativoPropertyChange);

		getFormModel().getFormObjectHolder().removeValueChangeListener(areaDocumentoCodiceIvaAlternativoPropertyChange);

		super.dispose();
	}

	/**
	 * Crea e restituisce il SearchTextBinding per l' entita aggiungendo il pulsante per la richiesta della situazione
	 * rate.
	 *
	 * @param bf
	 *            il binding factory
	 * @return Binding
	 */
	private Binding getContrattiBinding(PanjeaSwingBindingFactory bf) {
		Binding bindingContratto = bf.createBoundSearchText("areaMagazzino.documento.contrattoSpesometro",
				new String[] { "codice" }, new String[] { "areaMagazzino.documento.entita" },
				new String[] { ContrattoSpesometroSearchObject.ENTITA_KEY });
		return bindingContratto;
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
		Binding bindingListino = bf.createBoundSearchText(proprietaListino, null, Listino.class);
		// ((SearchPanel)
		// bindingListino.getControl()).getTextFields().get("codice").setColumns(10);
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
