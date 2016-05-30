/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.statistiche.movimentazione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ESezioneTipoMovimento;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazioneArticolo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form per l'input dei parametri di ricerca della movimentazione articolo.
 * 
 * @author fattazzo
 */
public class ParametriRicercaMovimentazioneArticoloForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriRicercaMovimentazioneArticoloForm";
	public static final String FORMMODEL_ID = "parametriRicercaMovimentazioneArticoloFormModel";

	private AziendaCorrente aziendaCorrente = null;
	private EntitaPropertyChange entitaPropertyChange;

	/**
	 * Costruttore di default.
	 * 
	 * @param parametriRicercaMovimentazioneArticolo
	 *            parametri di ricerca iniziale
	 */
	public ParametriRicercaMovimentazioneArticoloForm(
			final ParametriRicercaMovimentazioneArticolo parametriRicercaMovimentazioneArticolo) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaMovimentazioneArticolo, false, FORMMODEL_ID),
				FORM_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text dell'entità mi selezioni solo clienti e
		// fornitori
		List<TipoEntita> tipiEntita = new ArrayList<TipoEntita>();
		tipiEntita.add(TipoEntita.CLIENTE);
		tipiEntita.add(TipoEntita.FORNITORE);

		ValueModel tipiEntitaValueModel = new ValueHolder(tipiEntita);
		DefaultFieldMetadata tipiEntitaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaValueModel), List.class, true, null);
		getFormModel().add("tipiEntita", tipiEntitaValueModel, tipiEntitaData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,40dlu,10dlu,right:pref,4dlu,200dlu,left:pref,4dlu,20dlu",
				"4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.addPropertyAndLabel("periodo", 1, 2, 8, 1);

		builder.addLabel("articoloLite", 1, 4);
		Binding articoloBinding = bf.createBoundSearchText("articoloLite", new String[] { "codice", "descrizione" });
		SearchPanel articoloSearchPanel = (SearchPanel) builder.addBinding(articoloBinding, 3, 4, 5, 1);
		articoloSearchPanel.getTextFields().get("codice").setColumns(5);

		builder.addLabel("unitaMisura", 8, 4);
		final JTextField unitaMisuraLabel = (JTextField) builder.addProperty("articoloLite.unitaMisura.codice", 10, 4);
		unitaMisuraLabel.setFocusable(false);

		builder.addLabel("entitaLite", 1, 6);
		Binding entiBinding = bf.createBoundSearchText("entitaLite", new String[] { "codice",
				"anagrafica.denominazione" }, new String[] { "tipiEntita" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY });
		SearchPanel searchPanelEntita = (SearchPanel) builder.addBinding(entiBinding, 3, 6, 8, 1);
		searchPanelEntita.getTextFields().get("codice").setColumns(5);

		builder.addLabel("sedeEntita", 1, 8);
		Binding sedeEntitaBinding = bf.createBoundSearchText("sedeEntita", new String[] { "sede.descrizione" },
				new String[] { "entitaLite" }, new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
		builder.addBinding(sedeEntitaBinding, 3, 8, 8, 1);

		builder.addLabel("depositoLite", 1, 10);
		Binding depositoBinding = bf.createBoundSearchText("depositoLite", new String[] { "codice", "descrizione" });
		SearchPanel searchPanelDeposito = (SearchPanel) builder.addBinding(depositoBinding, 3, 10, 8, 1);
		searchPanelDeposito.getTextFields().get("codice").setColumns(5);

		builder.addLabel("sezioniTipoMovimento", 1, 12);
		builder.addBinding(bf.createBoundEnumCheckBoxList("sezioniTipoMovimento", ESezioneTipoMovimento.class,
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP), 3, 12, 8, 1);

		entitaPropertyChange = new EntitaPropertyChange(getFormModel());
		getValueModel("entitaLite").addValueChangeListener(entitaPropertyChange);

		addFormObjectChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				unitaMisuraLabel.setEditable(false);
			}
		});

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		ParametriRicercaMovimentazioneArticolo parametriRicercaMovimentazioneArticolo = new ParametriRicercaMovimentazioneArticolo();
		Calendar cal = Calendar.getInstance();
		cal.set(aziendaCorrente.getAnnoMagazzino(), 0, 1, 0, 0, 0);
		parametriRicercaMovimentazioneArticolo.getPeriodo().setDataIniziale(cal.getTime());
		cal.set(aziendaCorrente.getAnnoMagazzino(), 11, 31, 23, 59, 59);
		parametriRicercaMovimentazioneArticolo.getPeriodo().setDataFinale(cal.getTime());
		return parametriRicercaMovimentazioneArticolo;
	}

	@Override
	public void dispose() {
		super.dispose();
		getValueModel("entitaLite").removeValueChangeListener(entitaPropertyChange);
	}

	/**
	 * 
	 * @param aziendaCorrente
	 *            setta l'azienda corrente
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

}
