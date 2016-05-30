package it.eurotn.panjea.intra.rich.editors.dichiarazione;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;
import it.eurotn.panjea.intra.domain.TipoPeriodo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.StyledLabel;
import com.jidesoft.swing.StyledLabelBuilder;

public class IntestazioneForm extends PanjeaAbstractForm {

	private class DichiarazioneChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			DichiarazioneIntra dichiarazione = (DichiarazioneIntra) evt.getNewValue();
			if (dichiarazione != null) {
				if (dichiarazione.getTipoDichiarazione().equals(TipoDichiarazione.ACQUISTI)) {
					titleComponent.setText("PERIODICITA' ELENCO ACQUISTI");
				} else if (dichiarazione.getTipoDichiarazione().equals(TipoDichiarazione.VENDITE)) {
					titleComponent.setText("PERIODICITA' ELENCO VENDITE");
				}
			}
		}
	}

	private class TipoPeriodoChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			TipoPeriodo tipoPeriodo = (TipoPeriodo) evt.getNewValue();
			if (!getFormModel().isReadOnly() && tipoPeriodo != null) {
				// rilegge i valori delle due proprietà e ne aggiorna i valori nei componenti swing
				if (tipoPeriodo.equals(TipoPeriodo.M)) {
					getValueModel("trimestre").setValue(getValue("trimestre"));
				} else {
					getValueModel("mese").setValue(getValue("mese"));
				}
			}
		}

	}

	private static final String FORM_ID = "intestazioneForm";

	private RiassuntoComponent riassuntoComponent = null;
	private JLabel titleComponent = null;

	private DichiarazioneChangeListener dichiarazioneChangeListener = null;
	private TipoPeriodoChangeListener tipoPeriodoChangeListener;

	/**
	 * Costruttore.
	 * 
	 * @param dichiarazioneIntra
	 *            dichiarazione per init.
	 */
	public IntestazioneForm(final DichiarazioneIntra dichiarazioneIntra) {
		super(PanjeaFormModelHelper.createFormModel(dichiarazioneIntra, false, FORM_ID + "Model"), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		JPanel rootPanel = new JPanel(new VerticalLayout(20));
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"left:30dlu, 10dlu, right:pref,4dlu,left:default, 30dlu, right:pref,4dlu,left:default, 30dlu, right:pref,4dlu,left:default, default:grow",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new FormDebugPanel()
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		titleComponent = createLabel("{PERIODICITA' ELENCO:b}");
		titleComponent.setFont(titleComponent.getFont().deriveFont(titleComponent.getFont().getSize() + 4));

		builder.addComponent(titleComponent, 1, 2, 13, 1);
		builder.nextRow();

		builder.addPropertyAndLabel("tipoPeriodo", 3);
		builder.addPropertyAndLabel("data", 7);
		JTextField codiceComp = (JTextField) builder.addPropertyAndLabel("codice", 11)[1];
		codiceComp.setColumns(5);
		builder.nextRow();

		JComponent[] meseComps = builder.addPropertyAndLabel("mese", 3, 6);
		((JTextField) meseComps[1]).setColumns(3);
		JTextField annoComp = (JTextField) builder.addPropertyAndLabel("anno", 7, 6, 1, 1)[1];
		annoComp.setColumns(5);

		JTextField percValStat = (JTextField) builder.addPropertyAndLabel("percValoreStatistico", 11)[1];
		percValStat.setColumns(3);
		builder.nextRow();

		JComponent[] trimestreComps = builder.addPropertyAndLabel("trimestre", 3, 8);
		((JTextField) trimestreComps[1]).setColumns(3);

		builder.nextRow();

		builder.setComponentAttributes("r, c");
		StyledLabel infoRifTrimestreComponent = StyledLabelBuilder
				.createStyledLabel("Le informazioni delle sezioni 1 e/o 3 sono da riferirsi a: @rows:2:2:2");
		infoRifTrimestreComponent.setFont(infoRifTrimestreComponent.getFont().deriveFont(
				new Float(infoRifTrimestreComponent.getFont().getSize() - 2)));
		builder.addComponent(infoRifTrimestreComponent, 6, 8, 2, 1);

		builder.setComponentAttributes("l, c");
		builder.addProperty("periodoRiferimentoTrimestre", 9, 8, 1, 1);
		builder.nextRow();

		JLabel titleSoggettoDelegatoObblicato = createLabel("{SOGGETTO OBBLIGATO:b}");
		titleSoggettoDelegatoObblicato.setFont(titleComponent.getFont().deriveFont(
				titleSoggettoDelegatoObblicato.getFont().getSize() + 4));
		builder.addComponent(titleSoggettoDelegatoObblicato, 1, 12, 6, 1);
		builder.nextRow();

		JTextField piva = (JTextField) builder.addPropertyAndLabel("soggettoObbligato.partitaIva", 3)[1];
		piva.setColumns(20);
		builder.nextRow();

		JLabel titlePersonaFisica = createLabel("{PERSONA FISICA:b}");
		titlePersonaFisica.setFont(titlePersonaFisica.getFont().deriveFont(titlePersonaFisica.getFont().getSize() + 4));
		builder.addComponent(titlePersonaFisica, 1, 16, 6, 1);

		builder.setRow(18);
		JTextField cognome = (JTextField) builder.addPropertyAndLabel("soggettoObbligato.cognome", 3)[1];
		cognome.setColumns(20);
		JTextField nome = (JTextField) builder.addPropertyAndLabel("soggettoObbligato.nome", 7)[1];
		nome.setColumns(20);
		builder.nextRow();

		JLabel titleSoggettoDiverso = createLabel("{SOGGETTO DIVERSO DA PERSONA FISICA:b}");
		titleSoggettoDiverso.setFont(titleSoggettoDiverso.getFont().deriveFont(
				titleSoggettoDiverso.getFont().getSize() + 4));
		builder.addComponent(titleSoggettoDiverso, 1, 20, 6, 1);
		builder.nextRow();

		JTextField soggetto = (JTextField) builder.addPropertyAndLabel("soggettoObbligato.soggetto", 3, 22, 6, 1)[1];
		soggetto.setColumns(20);
		builder.nextRow();

		builder.setRow(24);
		builder.setComponentAttributes("l, c");
		StyledLabel elenchiPrec = StyledLabelBuilder
				.createStyledLabel("Barrare la casella nel caso non siano stati presentati in precedenza elenchi riepologativi (Modd.INTRA-1 od INTRA-2) @rows:3:3:3");
		elenchiPrec.setFont(infoRifTrimestreComponent.getFont().deriveFont(
				new Float(elenchiPrec.getFont().getSize() - 2)));
		builder.addComponent(elenchiPrec, 5);

		builder.setComponentAttributes("r, c");
		builder.addProperty("soggettoObbligato.elenchiPrecedenti", 3);

		builder.setComponentAttributes("l, c");
		StyledLabel varPiva = StyledLabelBuilder
				.createStyledLabel("Barrare la casella in caso di cessazione di attivita’ ovvero di variazione della partita iva @rows:3:3:3");
		varPiva.setFont(infoRifTrimestreComponent.getFont().deriveFont(new Float(varPiva.getFont().getSize() - 2)));
		builder.addComponent(varPiva, 9);

		builder.setComponentAttributes("r, c");
		builder.addProperty("soggettoObbligato.cessazioneAttivita", 7);
		builder.nextRow();

		builder.setComponentAttributes("l, c");
		JLabel titleSoggettoDelegatoComponent = createLabel("{SOGGETTO DELEGATO:b}");
		titleSoggettoDelegatoComponent.setFont(titleSoggettoDelegatoComponent.getFont().deriveFont(
				titleSoggettoDelegatoObblicato.getFont().getSize() + 4));
		builder.addComponent(titleSoggettoDelegatoComponent, 1, 26, 6, 1);
		builder.nextRow();

		JTextField pivaDelegato = (JTextField) builder.addPropertyAndLabel(
				"soggettoDelegato.partitaIvaSoggettoDelegato", 3)[1];
		pivaDelegato.setColumns(20);
		builder.nextRow();

		JTextField denominazioneDelegato = (JTextField) builder.addPropertyAndLabel(
				"soggettoDelegato.denominazioneSoggettoDelegato", 3)[1];
		denominazioneDelegato.setColumns(20);
		builder.nextRow();

		rootPanel.add(builder.getPanel());

		riassuntoComponent = new RiassuntoComponent(bf);
		rootPanel.add(riassuntoComponent.getControl());

		addFormValueChangeListener("tipoPeriodo", getTipoPeriodoChangeListener());
		addFormObjectChangeListener(getDichiarazioneChangeListener());

		return rootPanel;
	}

	/**
	 * @param labelString
	 *            stringa della label e con i parametri dello stile
	 * @return label Styled con border line
	 */
	private JLabel createLabel(String labelString) {
		JLabel result = StyledLabelBuilder.createStyledLabel(labelString);
		return result;
	}

	@Override
	public void dispose() {
		removeFormObjectChangeListener(getDichiarazioneChangeListener());
		removeFormValueChangeListener("tipoPeriodo", getTipoPeriodoChangeListener());
		super.dispose();
	}

	/**
	 * @return dichiarazioneChangeListener
	 */
	private DichiarazioneChangeListener getDichiarazioneChangeListener() {
		if (dichiarazioneChangeListener == null) {
			dichiarazioneChangeListener = new DichiarazioneChangeListener();
		}
		return dichiarazioneChangeListener;
	}

	/**
	 * @return TipoPeriodoChangeListener
	 */
	private TipoPeriodoChangeListener getTipoPeriodoChangeListener() {
		if (tipoPeriodoChangeListener == null) {
			tipoPeriodoChangeListener = new TipoPeriodoChangeListener();
		}
		return tipoPeriodoChangeListener;
	}

	@Override
	public void setFormObject(Object formObject) {
		super.setFormObject(formObject);
		riassuntoComponent.setDichiarazioneIntra((DichiarazioneIntra) formObject);
	}

}
