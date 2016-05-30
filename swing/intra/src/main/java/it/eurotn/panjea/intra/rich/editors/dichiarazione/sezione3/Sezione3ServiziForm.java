package it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione3;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione3Intra;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.intra.rich.editors.dichiarazione.DichiarazioneIntraSezioneChangeListener;
import it.eurotn.panjea.intra.rich.search.ServizioSearchObject;
import it.eurotn.panjea.magazzino.domain.Articolo.ETipoArticolo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class Sezione3ServiziForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "sezione3ServiziForm";

	private DichiarazioneIntraSezioneChangeListener dichiarazioneIntraChangeListener = null;

	private DichiarazioneIntra dichiarazioneIntra = null;

	/**
	 * Costruttore.
	 * 
	 * @param rigaSezione3Intra
	 *            per init
	 */
	public Sezione3ServiziForm(final RigaSezione3Intra rigaSezione3Intra) {
		super(PanjeaFormModelHelper.createFormModel(rigaSezione3Intra, false, FORM_ID), FORM_ID);

		// aggiungo sul form model il tipo articolo per avere a disposizione solo le nomenclature nella searchtext del
		// servizio/nomenclatura
		ValueModel tipoArticoloValueModel = new ValueHolder(ETipoArticolo.SERVIZI);
		DefaultFieldMetadata tipoArticoloMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipoArticoloValueModel), ETipoArticolo.class, true, null);
		getFormModel().add("tipoArticolo", tipoArticoloValueModel, tipoArticoloMetaData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,fill:default",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		List<JComponent> componentsAcquisti = new ArrayList<JComponent>();

		// "progressivo", "fornitoreStato", "fornitorepiva", "importo",
		// "importoInValuta", "numeroFattura", "dataFattura", "servizio",
		// "modalitaErogazione", "modalitaIncasso", "paesePagamento"

		// proprietà della classe base RigaSezioneIntra
		JTextField progressivo = (JTextField) builder.addPropertyAndLabel("progressivo")[1];
		progressivo.setColumns(7);
		builder.nextRow();

		JTextField piva = (JTextField) builder.addPropertyAndLabel("fornitorepiva", 1)[1];
		piva.setColumns(14);

		builder.addLabel("fornitoreStato", 5);
		Binding bindingStato = bf.createBoundSearchText("fornitoreStato", null, Nazione.class);
		SearchPanel searchPanelStato = (SearchPanel) builder.addBinding(bindingStato, 7);
		searchPanelStato.getTextFields().get(null).setColumns(12);
		builder.nextRow();

		builder.addLabel("importo", 1);
		JTextField fieldImporto = (JTextField) builder.addBinding(
				bf.createBoundFormattedTextField("importo", getFactory(0)), 3);
		fieldImporto.setColumns(14);

		JLabel labelImportoInValuta = builder.addLabel("importoInValuta", 5);
		JTextField fieldImportoInValuta = (JTextField) builder.addBinding(
				bf.createBoundFormattedTextField("importoInValuta", getFactory(0)), 7);
		fieldImportoInValuta.setColumns(14);

		componentsAcquisti.add(labelImportoInValuta);
		componentsAcquisti.add(fieldImportoInValuta);
		builder.nextRow();

		// proprietà della classe rigaSezione1
		builder.nextRow();
		builder.addHorizontalSeparator(9);
		builder.nextRow();

		builder.addLabel("servizio", 1);
		Binding bindingServizio = bf.createBoundSearchText("servizio", new String[] { "codice", "descrizione" },
				new String[] { "tipoArticolo" }, new String[] { ServizioSearchObject.PARAM_TIPO_ARTICOLO });
		SearchPanel searchPanelNomenclatura = (SearchPanel) builder.addBinding(bindingServizio, 3, 12, 5, 1);
		searchPanelNomenclatura.getTextFields().get("codice").setColumns(12);
		builder.nextRow();

		JTextField fieldNumFatt = (JTextField) builder.addPropertyAndLabel("numeroFattura", 1)[1];
		fieldNumFatt.setColumns(14);
		builder.addPropertyAndLabel("dataFattura", 5);
		builder.nextRow();

		builder.addLabel("paesePagamento", 1);
		SearchPanel searchPanelPaesePagamento = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("paesePagamento", null, Nazione.class), 3);
		searchPanelPaesePagamento.getTextFields().get(null).setColumns(12);
		builder.nextRow();

		builder.addPropertyAndLabel("modalitaErogazione", 1);
		builder.addPropertyAndLabel("modalitaIncasso", 5);
		builder.nextRow();

		getDichiarazioneIntraChangeListener().setComponents(componentsAcquisti);
		getValueModel("dichiarazione").addValueChangeListener(getDichiarazioneIntraChangeListener());

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		RigaSezioneIntra rigaSezioneIntra = new RigaSezione3Intra();
		rigaSezioneIntra.setDichiarazione(dichiarazioneIntra);
		return rigaSezioneIntra;
	}

	@Override
	public void dispose() {
		getValueModel("dichiarazione").removeValueChangeListener(getDichiarazioneIntraChangeListener());
		super.dispose();
	}

	/**
	 * @return DichiarazioneIntraChangeListener
	 */
	private DichiarazioneIntraSezioneChangeListener getDichiarazioneIntraChangeListener() {
		if (dichiarazioneIntraChangeListener == null) {
			dichiarazioneIntraChangeListener = new DichiarazioneIntraSezioneChangeListener();
		}
		return dichiarazioneIntraChangeListener;
	}

	/**
	 * @param numeroDecimali
	 *            numeroDecimali
	 * @return DefaultFormatterFactory
	 */
	private DefaultFormatterFactory getFactory(int numeroDecimali) {
		DefaultFormatterFactory factory = new DefaultNumberFormatterFactory("###,###,###,##0", numeroDecimali,
				BigDecimal.class);
		return factory;
	}

	/**
	 * @param dichiarazioneIntra
	 *            the dichiarazioneIntra to set
	 */
	public void setDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		this.dichiarazioneIntra = dichiarazioneIntra;
	}

}
