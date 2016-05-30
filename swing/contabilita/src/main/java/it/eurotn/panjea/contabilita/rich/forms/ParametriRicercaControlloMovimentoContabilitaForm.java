/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Page dei parametri ricerca documenti contabili.
 * 
 * @author Leonardo
 */
public class ParametriRicercaControlloMovimentoContabilitaForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(ParametriRicercaControlloMovimentoContabilitaForm.class);
	private static final String FORM_ID = "parametriRicercaControlloMovimentoContabilitaForm";
	private final IContabilitaAnagraficaBD contabilitaAnagraficaBD;
	private IDocumentiBD documentiBD = null;
	private AziendaCorrente aziendaCorrente = null;
	private ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili = null;
	private JComboBox jDateChooser = null;
	private PeriodoParametriChangeListener periodoParametriChangeListener = null;
	private List<TipoAreaContabile> tipiAreaContabile = null;

	/**
	 * Default constructor.
	 * 
	 * @param parametriRicerca
	 *            parametriRicerca
	 * @param contabilitaAnagraficaBD
	 *            contabilitaAnagraficaBD
	 * @param documentiBD
	 *            documentiBD
	 * @param aziendaCorrente
	 *            aziendaCorrente
	 */
	public ParametriRicercaControlloMovimentoContabilitaForm(final ParametriRicercaMovimentiContabili parametriRicerca,
			final IContabilitaAnagraficaBD contabilitaAnagraficaBD, final IDocumentiBD documentiBD,
			final AziendaCorrente aziendaCorrente) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicerca, false, FORM_ID), FORM_ID);
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
		this.documentiBD = documentiBD;
		this.aziendaCorrente = aziendaCorrente;
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Creo controlli per il form");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,60dlu,10dlu,right:pref,4dlu,left:60dlu,10dlu,fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default, fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel()
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);

		((JTextField) builder.addPropertyAndLabel("annoCompetenza")[1]).setColumns(4);

		builder.nextRow();
		JPanel panelDataRegistrazione = (JPanel) builder.addPropertyAndLabel("dataRegistrazione", 1, 4, 5, 1)[1];
		jDateChooser = (JComboBox) panelDataRegistrazione.getComponents()[0];

		builder.nextRow();
		builder.addPropertyAndLabel("dataDocumento", 1, 6, 5, 1);
		builder.nextRow();
		builder.addLabel("daNumeroDocumento", 1);
		builder.addBinding(bf.createBoundCodice("daNumeroDocumento", true, false), 3);
		builder.addLabel("aNumeroDocumento", 5);
		builder.addBinding(bf.createBoundCodice("aNumeroDocumento", true, false), 7);
		builder.nextRow();
		builder.addPropertyAndLabel("escludiMovimentiStampati", 1);

		((JTextField) builder.addPropertyAndLabel("totale", 5)[1]).setColumns(6);

		builder.nextRow();
		builder.addLabel("statiAreaContabile", 1);
		Binding bindingStato = bf.createBoundEnumCheckBoxList("statiAreaContabile", StatoAreaContabile.class,
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
		builder.addBinding(bindingStato, 3, 12, 5, 1);

		periodoParametriChangeListener = new PeriodoParametriChangeListener();
		periodoParametriChangeListener.setPeriodoValueModel(getValueModel("dataRegistrazione"));

		addFormValueChangeListener("annoCompetenza", periodoParametriChangeListener);
		builder.nextRow();

		builder.setComponentAttributes("f, f");
		List<TipoAreaContabile> tipiAreeContabile = getTipiAreaContabile();
		List<TipoDocumento> tipiDocumentoSet = new ArrayList<TipoDocumento>();
		for (TipoAreaContabile tipoAreaContabile : tipiAreeContabile) {
			tipiDocumentoSet.add(tipoAreaContabile.getTipoDocumento());
		}

		builder.addBinding(bf.createBoundCheckBoxTree("tipiDocumento", new String[] { "abilitato",
				"classeTipoDocumento" }, new ValueHolder(tipiDocumentoSet)), 9, 2, 1, 14);

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		parametriRicercaMovimentiContabili = new ParametriRicercaMovimentiContabili();
		List<StatoAreaContabile> stati = Arrays.asList(StatoAreaContabile.values());
		parametriRicercaMovimentiContabili.getStatiAreaContabile().addAll(stati);
		// parametriRicercaMovimentiContabili.setTipiDocumento(getTipiDocumentoFromTipiAreaContabile());
		parametriRicercaMovimentiContabili.setEscludiMovimentiStampati(Boolean.TRUE);
		parametriRicercaMovimentiContabili.setAnnoCompetenza(aziendaCorrente.getAnnoContabile() + "");
		parametriRicercaMovimentiContabili.getDataRegistrazione().setDataIniziale(
				aziendaCorrente.getDataInizioEsercizio());
		parametriRicercaMovimentiContabili.getDataRegistrazione().setDataFinale(aziendaCorrente.getDataFineEsercizio());
		parametriRicercaMovimentiContabili.setNumeroTotaleTipiDocumento(getTipiAreaContabile().size());

		return parametriRicercaMovimentiContabili;
	}

	/**
	 * @return the documentiBD
	 */
	public IDocumentiBD getDocumentiBD() {
		return documentiBD;
	}

	/**
	 * @return List<TipoAreaContabile>
	 */
	private List<TipoAreaContabile> getTipiAreaContabile() {
		if (tipiAreaContabile == null) {
			tipiAreaContabile = contabilitaAnagraficaBD.caricaTipiAreaContabile("tipoDocumento.codice", null, true);
		}
		return tipiAreaContabile;
	}

	public void requestFocusForDate() {
		PanjeaSwingUtil.giveFocusToComponent(new Component[] { jDateChooser });
	}

	/**
	 * @param documentiBD
	 *            the documentiBD to set
	 */
	public void setDocumentiBD(IDocumentiBD documentiBD) {
		this.documentiBD = documentiBD;
	}

}
