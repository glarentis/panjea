package it.eurotn.panjea.tesoreria.rich.forms;

import foxtrot.Task;
import foxtrot.Worker;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.etichetta.ParametriStampaEtichetteArticolo;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaSituazioneRata;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.eurotn.rich.report.ReportManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.util.RcpSupport;

public class ParametriRicercaSituazioneRateForm extends PanjeaAbstractForm {

	private class EntitaPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (getFormModel().isReadOnly()) {
				return;
			}

			EntitaLite entita = (EntitaLite) evt.getNewValue();

			if (entita == null) {
				getFormModel().getFieldMetadata("categoriaEntita").setReadOnly(false);
			} else {
				getFormModel().getFieldMetadata("categoriaEntita").setReadOnly(true);
				getFormModel().getValueModel("categoriaEntita").setValue(new CategoriaEntita());
			}
		}

	}

	private class TipoPartitaChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {

			TipoEntita tipoEntita = TipoEntita.CLIENTE;
			EntitaLite entitaLite = new ClienteLite();

			if (((TipoPartita) event.getNewValue()) == TipoPartita.ATTIVA) {
				tipoEntita = TipoEntita.CLIENTE;
				entitaLite = new ClienteLite();
			} else {
				tipoEntita = TipoEntita.FORNITORE;
				entitaLite = new FornitoreLite();
			}

			getFormModel().getValueModel("tipoEntita").setValue(tipoEntita);
			getFormModel().getValueModel("entitaLite").setValue(entitaLite);
		}

	}

	public static final String FORM_ID = "parametriRicercaSituazioneRateForm";

	private EntitaPropertyChange entitaPropertyChange = null;
	private TipoPartitaChangeListener tipoPartitaChangeListener = null;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaSituazioneRateForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaSituazioneRata(), false, FORM_ID), FORM_ID);
	}

	/**
	 * Carica da jasperReport tutti i layout presenti per le etichette.<br/>
	 * i layout sono nella cartella di jasper {@link ParametriStampaEtichetteArticolo#FOLDER_REPORT_PATH}
	 * 
	 * @return valueHolder con i nomi dei report.
	 */
	private ValueHolder caricaLayout() {
		final ReportManager reportManager = RcpSupport.getBean(ReportManager.BEAN_ID);
		try {
			return (ValueHolder) Worker.post(new Task() {

				@Override
				public Object run() throws Exception {
					return new ValueHolder(reportManager.listReport("Tesoreria/ScadenzeAperte"));
				}
			});
		} catch (Exception e) {
			logger.error("-->errore nel caricare la lista dei report Tesoreria/ScadenzeAperte", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		builder.add("dataIniziale", "align=left");
		builder.add("dataFinale", "align=left");
		builder.row();
		builder.add("tipoPartita", "align=left");
		builder.row();
		Binding entitaBinding = getEntitaBinding(bf);
		builder.add(entitaBinding, "align=left");
		builder.row();
		builder.add(bf.createBoundSearchText("categoriaEntita", new String[] { "descrizione" }), "align=left");
		builder.row();
		Binding reportListBinding = bf.createBoundComboBox("reportName", caricaLayout());
		builder.add(reportListBinding, "align=left");
		builder.row();

		getFormModel().getValueModel("entitaLite").addValueChangeListener(getEntitaPropertyChange());
		getFormModel().getValueModel("tipoPartita").addValueChangeListener(getTipoPartitaChangeListener());
		return builder.getForm();
	}

	/**
	 * crea e restituisce il SearchTextBinding di Entita.
	 * 
	 * @param bf
	 *            PanjeaSwingBindingFactory
	 * @return binding per l'entità
	 */
	private Binding getEntitaBinding(PanjeaSwingBindingFactory bf) {
		Binding bindingEntita = bf.createBoundSearchText("entitaLite", new String[] { "codice",
				"anagrafica.denominazione" }, new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY },
				new String[] { "tipoEntita" });
		SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
		searchPanel.getTextFields().get("codice").setColumns(5);
		searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(20);
		return bindingEntita;
	}

	/**
	 * @return Returns the entitaPropertyChange.
	 */
	private EntitaPropertyChange getEntitaPropertyChange() {
		if (entitaPropertyChange == null) {
			entitaPropertyChange = new EntitaPropertyChange();
		}
		return entitaPropertyChange;
	}

	/**
	 * @return Returns the entitaPropertyChange.
	 */
	private TipoPartitaChangeListener getTipoPartitaChangeListener() {
		if (tipoPartitaChangeListener == null) {
			tipoPartitaChangeListener = new TipoPartitaChangeListener();
		}
		return tipoPartitaChangeListener;
	}
}
