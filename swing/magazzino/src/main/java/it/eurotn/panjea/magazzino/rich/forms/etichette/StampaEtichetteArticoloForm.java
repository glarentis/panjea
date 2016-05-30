package it.eurotn.panjea.magazzino.rich.forms.etichette;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.etichetta.EtichettaArticolo;
import it.eurotn.panjea.magazzino.domain.etichetta.ParametriStampaEtichetteArticolo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.search.ArticoloSearchObject;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.util.RcpSupport;

public class StampaEtichetteArticoloForm extends PanjeaAbstractForm {

	private class ArticoloPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// carico il prezzo e l'iva
			if (getFormModel().isReadOnly()) {
				return;
			}
			if (evt.getNewValue() == null || evt.getNewValue().equals(evt.getOldValue())) {
				return;
			}
			ArticoloLite articoloLite = (ArticoloLite) evt.getNewValue();
			if (articoloLite.getId() != null) {
				// Ricarico articoliLite per avvalorare tutte le propreità
				articoloLite = magazzinoAnagraficaBD.caricaArticoloLite(articoloLite.getId());

				Date data = (Date) formModelParametriStampa.getValueModel("data").getValue();
				Listino listino = (Listino) formModelParametriStampa.getValueModel("listino").getValue();
				Boolean aggiungiIva = (Boolean) formModelParametriStampa.getValueModel("aggiungiIva").getValue();

				getFormModel().getValueModel("percApplicazioneCodiceIva").setValue(
						articoloLite.getCodiceIva().getPercApplicazione());
				getFormModel().getValueModel("aggiungiIva").setValue(aggiungiIva);

				Integer numeroDecimali = 2;
				if (!aggiungiIva && articoloLite.getNumeroDecimaliPrezzo() != null) {
					numeroDecimali = articoloLite.getNumeroDecimaliPrezzo();
				}

				if (listino != null) {
					ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(articoloLite.getId(),
							data, listino.getId(), null, null, null, null, null, ProvenienzaPrezzo.LISTINO, null, null,
							null, valutaAziendaCache.caricaValutaAziendaCorrente().getCodiceValuta(), null, null);
					PoliticaPrezzo politicaPrezzo = magazzinoDocumentoBD.calcolaPrezzoArticolo(parametriCalcoloPrezzi);
					// recupero lo scaglione a 0;
					BigDecimal prezzoNetto = politicaPrezzo.getPrezzoNetto(0, articoloLite.getCodiceIva()
							.getPercApplicazione());
					getFormModel().getValueModel("prezzoNettoCalcolato").setValue(prezzoNetto);
					// setto il numero di decimali nel form per il prezzo.
					if (!aggiungiIva) {
						numeroDecimali = politicaPrezzo.getPrezzi().getRisultatoPrezzo(0.0).getNumeroDecimali();
					} else {
						if (politicaPrezzo.isPrezzoIvato()) {
							prezzoNetto = politicaPrezzo.getPrezzi().getRisultatoPrezzo(0.0).getValue();
							RisultatoPrezzo<Sconto> variazione = politicaPrezzo.getSconti().getRisultatoPrezzo(0.0);
							if (variazione != null) {
								prezzoNetto = variazione.getValue().applica(prezzoNetto, 2);
							}
						} else if (articoloLite.getCodiceIva() != null) {
							prezzoNetto = articoloLite.getCodiceIva().applica(prezzoNetto);
						}
					}

					getFormModel().getValueModel("prezzoNetto").setValue(prezzoNetto);
				}
				getFormModel().getValueModel("numeroDecimali").setValue(numeroDecimali);
			}
		}
	}

	private class EntitaParametriEtichetteChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			EntitaLite entita = (EntitaLite) evt.getNewValue();
			getValueModel("entitaFiltroPerArticoli").setValue(null);
			if (entita != null) {
				getValueModel("entitaFiltroPerArticoli").setValue(entita);
			}
		}

	}

	public class NumeroDecimaliPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getNewValue().equals(evt.getOldValue())) {
				return;
			}
			Integer numeroDecimali = (Integer) evt.getNewValue();
			DefaultFormatterFactory factory = new DefaultNumberFormatterFactory("###,###,###,##0", numeroDecimali,
					BigDecimal.class);
			prezzoControl.setFormatterFactory(factory);

			if (!getFormModel().isReadOnly()) {
				BigDecimal prezzo = (BigDecimal) getValueModel("prezzoNetto").getValue();
				if (prezzo != null) {
					prezzo = prezzo.setScale(numeroDecimali, RoundingMode.HALF_UP);
				}
				getValueModel("prezzoNetto").setValue(prezzo);
			}
		}
	}

	private static final String FORM_ID = "stampaEtichetteArticoloForm";

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private PluginManager pluginManager;
	private ValutaAziendaCache valutaAziendaCache;
	private ParametriStampaEtichetteArticolo parametriStampaEtichetteArticolo;

	private FormModel formModelParametriStampa;
	private JFormattedTextField prezzoControl;

	private List<JComponent> pluginLottiComponents;
	private NumeroDecimaliPropertyChange numeroDecimaliPropertyChange;

	private ArticoloPropertyChange articoloPropertyChange;

	private EntitaParametriEtichetteChangeListener entitaParametriEtichetteChangeListener;

	/**
	 * Costruttore di default, inizializza un nuovo tipo mezzo di trasporto.
	 */
	public StampaEtichetteArticoloForm() {
		super(PanjeaFormModelHelper.createFormModel(new EtichettaArticolo(), false, FORM_ID), FORM_ID);

		this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		this.valutaAziendaCache = RcpSupport.getBean(ValutaAziendaCache.BEAN_ID);

		ValueModel clienteValueModel = new ValueHolder(null);
		DefaultFieldMetadata entitaMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(clienteValueModel), ClienteLite.class, true, null);
		getFormModel().add("entitaFiltroPerArticoli", clienteValueModel, entitaMetaData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();
		SearchPanel searchPanel = (SearchPanel) builder.add(bf.createBoundSearchText("articolo", new String[] {
				"codice", "descrizione" }, new String[] { "entitaFiltroPerArticoli" },
				new String[] { ArticoloSearchObject.ENTITA_KEY }), "align=left")[1];
		searchPanel.getTextFields().get("codice").setColumns(20);
		searchPanel.getTextFields().get("descrizione").setColumns(35);
		builder.row();
		((JTextField) builder.add("numeroCopiePerStampa", "align=left")[1]).setColumns(3);
		builder.row();

		Binding boundFormattedTextFieldBinding = bf.createBoundFormattedTextField("prezzoNetto");
		prezzoControl = (JFormattedTextField) boundFormattedTextFieldBinding.getControl();
		builder.add(boundFormattedTextFieldBinding, "align=left");
		prezzoControl.setColumns(10);
		prezzoControl.setHorizontalAlignment(SwingConstants.RIGHT);

		pluginLottiComponents = new ArrayList<JComponent>();
		if (pluginManager.isPresente(PluginManager.PLUGIN_LOTTI)) {
			builder.row();
			JComponent[] comp = builder.add("quantita", "align=left");
			((JTextField) comp[1]).setColumns(3);
			pluginLottiComponents.add(comp[0]);
			pluginLottiComponents.add(comp[1]);
			builder.row();
			comp = builder.add("dataDocumento", "align=left");
			pluginLottiComponents.add(comp[0]);
			pluginLottiComponents.add(comp[1]);
		}

		getValueModel("numeroDecimali").addValueChangeListener(getNumeroDecimaliPropertyChange());
		// registro il property change sull'articolo all'interno della pagina e non nel default controller perchè
		// utilizzo il formModelParametriStampa che la pagina non può passare al controller
		getFormModel().getValueModel("articolo").addValueChangeListener(getArticoloPropertyChange());

		return builder.getForm();
	}

	@Override
	public void dispose() {
		getValueModel("numeroDecimali").removeValueChangeListener(getNumeroDecimaliPropertyChange());
		getFormModel().getValueModel("articolo").removeValueChangeListener(getArticoloPropertyChange());

		formModelParametriStampa.getValueModel("entita").removeValueChangeListener(
				getEntitaParametriEtichetteChangeListener());
		super.dispose();
	}

	/**
	 * @return ArticoloPropertyChange
	 */
	private ArticoloPropertyChange getArticoloPropertyChange() {
		if (articoloPropertyChange == null) {
			articoloPropertyChange = new ArticoloPropertyChange();
		}
		return articoloPropertyChange;
	}

	/**
	 * @return EntitaParametriEtichetteChangeListener
	 */
	private EntitaParametriEtichetteChangeListener getEntitaParametriEtichetteChangeListener() {
		if (entitaParametriEtichetteChangeListener == null) {
			entitaParametriEtichetteChangeListener = new EntitaParametriEtichetteChangeListener();
		}
		return entitaParametriEtichetteChangeListener;
	}

	/**
	 * @return NumeroDecimaliPropertyChange
	 */
	private NumeroDecimaliPropertyChange getNumeroDecimaliPropertyChange() {
		if (numeroDecimaliPropertyChange == null) {
			numeroDecimaliPropertyChange = new NumeroDecimaliPropertyChange();
		}
		return numeroDecimaliPropertyChange;
	}

	@Override
	public boolean isDirty() {
		return super.isDirty();
	}

	/**
	 * @param formModelParametriStampa
	 *            the formModelParametriStampa to set
	 */
	public void setFormModelParametriStampa(FormModel formModelParametriStampa) {
		this.formModelParametriStampa = formModelParametriStampa;
		this.formModelParametriStampa.getValueModel("entita").addValueChangeListener(
				getEntitaParametriEtichetteChangeListener());
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            the magazzinoDocumentoBD to set
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	/**
	 * @param parametriStampaEtichetteArticolo
	 *            The parametriStampaEtichetteArticolo to set.
	 */
	public void setParametriStampaEtichetteArticolo(ParametriStampaEtichetteArticolo parametriStampaEtichetteArticolo) {
		this.parametriStampaEtichetteArticolo = parametriStampaEtichetteArticolo;

		updatePluginLottiComponents();
	}

	/**
	 * Aggiorna la proprietà visible dei componenti dei lotti in base ai parametri di ricerca.
	 */
	private void updatePluginLottiComponents() {
		for (JComponent component : pluginLottiComponents) {
			component.setVisible(parametriStampaEtichetteArticolo.isGestioneLotti());
		}
	}
}
