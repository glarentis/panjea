package it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione1;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione1Intra;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.intra.rich.editors.dichiarazione.DichiarazioneIntraSezioneChangeListener;
import it.eurotn.panjea.intra.rich.search.ServizioSearchObject;
import it.eurotn.panjea.magazzino.domain.Articolo.ETipoArticolo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Font;
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

public class Sezione1BeniForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "sezione1BeniForm";

	private DichiarazioneIntraSezioneChangeListener dichiarazioneIntraChangeListener = null;

	private DichiarazioneIntra dichiarazioneIntra = null;

	/**
	 * Costruttore.
	 * 
	 * @param rigaSezione1Intra
	 *            per init
	 */
	public Sezione1BeniForm(final RigaSezione1Intra rigaSezione1Intra) {
		super(PanjeaFormModelHelper.createFormModel(rigaSezione1Intra, false, FORM_ID), FORM_ID);

		// aggiungo sul form model il tipo articolo per avere a disposizione solo le nomenclature nella searchtext del
		// servizio/nomenclatura
		ValueModel tipoArticoloValueModel = new ValueHolder(ETipoArticolo.FISICO);
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

		builder.addPropertyAndLabel("naturaTransazione", 1, 8, 5, 1);

		// proprietà della classe rigaSezione1
		builder.nextRow();
		builder.addHorizontalSeparator(9);
		builder.nextRow();

		builder.addLabel("nomenclatura", 1);
		Binding bindingNomenclatura = bf.createBoundSearchText("nomenclatura",
				new String[] { "codice", "descrizione" }, new String[] { "tipoArticolo" },
				new String[] { ServizioSearchObject.PARAM_TIPO_ARTICOLO });
		SearchPanel searchPanelNomenclatura = (SearchPanel) builder.addBinding(bindingNomenclatura, 3, 12, 5, 1);
		searchPanelNomenclatura.getTextFields().get("codice").setColumns(12);
		builder.nextRow();

		JTextField fieldMassa = (JTextField) builder.addPropertyAndLabel("massaNetta")[1];
		fieldMassa.setColumns(14);

		builder.addLabel("um", 5);
		Binding bindingUM = bf.createBoundSearchText("um", null, UnitaMisura.class);
		SearchPanel searchPanelUnitaMisura = (SearchPanel) builder.addBinding(bindingUM, 7);
		searchPanelUnitaMisura.getTextFields().get(null).setColumns(12);
		builder.nextRow();

		JTextField fieldMassaSuppl = (JTextField) builder.addPropertyAndLabel("massaSupplementare")[1];
		fieldMassaSuppl.setColumns(14);

		builder.addLabel("umsupplementare", 5);
		JLabel labelUmSuppl = (JLabel) builder.addBinding(bf.createBoundLabel("nomenclatura.umsupplementare"), 7);
		labelUmSuppl.setFont(labelUmSuppl.getFont().deriveFont(Font.BOLD));
		builder.nextRow();

		builder.addLabel("valoreStatisticoEuro");
		JTextField fieldValoreStatistico = (JTextField) builder.addBinding(
				bf.createBoundFormattedTextField("valoreStatisticoEuro", getFactory(0)), 3);
		fieldValoreStatistico.setColumns(14);

		builder.addPropertyAndLabel("gruppoCondizioneConsegna", 5);
		builder.nextRow();

		builder.addPropertyAndLabel("modalitaTrasporto", 1);

		JLabel labelPaeseProv = builder.addLabel("paese", 5);
		SearchPanel searchPanelPaeseProv = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("paese", null, Nazione.class), 7);
		componentsAcquisti.add(labelPaeseProv);
		componentsAcquisti.add(searchPanelPaeseProv);
		builder.nextRow();

		JLabel labelProvDest = builder.addLabel("provincia", 1);
		SearchPanel searchPanelProvDest = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("provincia", null, LivelloAmministrativo2.class), 3);
		searchPanelProvDest.getTextFields().get(null).setColumns(12);

		JLabel labelPaeseOrigine = builder.addLabel("paeseOrigineArticolo", 5);
		SearchPanel searchPanelPaeseOrigine = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("paeseOrigineArticolo", null, Nazione.class), 7);
		searchPanelPaeseOrigine.getTextFields().get(null).setColumns(12);

		builder.nextRow();

		getDichiarazioneIntraChangeListener().setComponents(componentsAcquisti);
		getValueModel("dichiarazione").addValueChangeListener(getDichiarazioneIntraChangeListener());

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		RigaSezioneIntra rigaSezioneIntra = new RigaSezione1Intra();
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
