package it.eurotn.panjea.partite.rich.forms.ricercarate;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.partite.rich.search.TipoAreaPartitaSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.rich.commands.OpenPreviewAssegnoCommand;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Parametri creazione Area partita form, presenta i dati necessari alla creazione di un documento di pagamento.
 * 
 * @author Leonardo
 */
public class ParametriCreazioneAreaChiusureForm extends PanjeaAbstractForm {

	public class AssegnoCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand actioncommand) {
		}

		@Override
		public boolean preExecution(ActionCommand actioncommand) {
			ParametriCreazioneAreaChiusure parametri = (ParametriCreazioneAreaChiusure) getFormObject();
			actioncommand.addParameter(OpenPreviewAssegnoCommand.IMMAGINE_ASSEGNO_PARAMETER, parametri.getImmagine());
			return true;
		}

	}

	private class GeneraSingoloEffettoValueChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			getFormModel().getFieldMetadata("dataEffetto").setReadOnly(!(Boolean) evt.getNewValue());
		}

	}

	/**
	 * Property change associato al tipoAreaContabile che visualizza il campo rapporto bancario se il tipo documento ha
	 * TipoEntita.BANCA.
	 * 
	 * @author Leonardo
	 */
	private class TipoAreaPartitaValueChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			ParametriCreazioneAreaChiusureForm.logger.debug("--> Enter TipoAreaPartitaValueChangeListener");

			((JTextField) componentsNoteContabili[1]).setText("");

			for (JComponent effettoComponent : componentRaggruppamentoEffetto) {
				effettoComponent.setVisible(false);
			}
			for (JComponent compAssegno : componentsAssegno) {
				compAssegno.setVisible(false);
			}
			for (JComponent compAnticipoFatture : componentsAnticipoFatture) {
				compAnticipoFatture.setVisible(false);
			}
			for (JComponent bancaComponent : componentsBanca) {
				bancaComponent.setVisible(false);
			}

			for (JComponent noteComponent : componentsNoteContabili) {
				noteComponent.setVisible(false);
			}

			if (evt != null) {
				TipoAreaPartita tipoAreaPartita = (TipoAreaPartita) evt.getNewValue();

				if (tipoAreaPartita != null) {
					TipoDocumento tipoDocumento = tipoAreaPartita.getTipoDocumento();
					for (JComponent effettoComponent : componentRaggruppamentoEffetto) {
						effettoComponent
								.setVisible(tipoAreaPartita.getTipoOperazione() == TipoOperazione.GESTIONE_DISTINTA);
					}

					for (JComponent compAssegno : componentsAssegno) {
						compAssegno.setVisible(tipoAreaPartita.getTipoOperazione() == TipoOperazione.GESTIONE_ASSEGNO);
					}

					for (JComponent compAnticipoFatture : componentsAnticipoFatture) {
						compAnticipoFatture
								.setVisible(tipoAreaPartita.getTipoOperazione() == TipoOperazione.ANTICIPO_FATTURE);
					}

					for (JComponent noteComponent : componentsNoteContabili) {
						noteComponent.setVisible(tipoAreaPartita.getTipoOperazione() == TipoOperazione.CHIUSURA
								|| tipoAreaPartita.getTipoOperazione() == TipoOperazione.GESTIONE_DISTINTA);
					}

					getFormModel().getValueModel("speseIncasso").setValue(BigDecimal.ZERO);
					getFormModel().getFieldMetadata("speseIncasso").setEnabled(tipoAreaPartita.isSpeseIncasso());

					for (JComponent bancaComponent : componentsBanca) {
						bancaComponent.setVisible(tipoDocumento.getTipoEntita() == TipoEntita.BANCA);
					}
				}
			}
			ParametriCreazioneAreaChiusureForm.logger.debug("--> Exit TipoAreaPartitaValueChangeListener");
		}
	}

	private static Logger logger = Logger.getLogger(ParametriCreazioneAreaChiusureForm.class);
	public static final String FORM_ID = "parametriCreazioneAreaPartitaForm";
	private final JComponent[] componentsBanca = new JComponent[2];
	private final JComponent[] componentsAssegno = new JComponent[7];
	private JComponent[] componentsAnticipoFatture = new JComponent[2];
	private JComponent[] componentsNoteContabili = new JComponent[2];
	private JComponent[] componentSpeseIncasso;
	private JComponent[] componentRaggruppamentoEffetto;
	private OpenPreviewAssegnoCommand assegnoCommand;

	/**
	 * Costruttore.
	 */
	public ParametriCreazioneAreaChiusureForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriCreazioneAreaChiusure(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Creo controlli per il form");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"left:p,4dlu,left:pref, 10dlu, left:pref,4dlu,left:pref, 10dlu, left:pref,4dlu,left:pref, 10dlu, left:pref,4dlu,left:pref, 10dlu, left:pref,4dlu,left:pref, 10dlu, left:pref,4dlu,left:pref",
				"default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);

		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.setLabelAttributes("r, c");

		builder.addPropertyAndLabel("dataDocumento", 1);

		builder.addLabel("tipoAreaPartita", 5);
		Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaPartita", new String[] { "tipoDocumento.codice",
				"tipoDocumento.descrizione" }, new String[] { "tipoPartita" },
				new String[] { TipoAreaPartitaSearchObject.PARAM_TIPO_PARTITA });
		SearchPanel componentTipoAreaContabile = (SearchPanel) builder.addBinding(bindingTipoDoc, 7);
		componentTipoAreaContabile.getTextFields().get("tipoDocumento.codice").setColumns(7);
		componentTipoAreaContabile.getTextFields().get("tipoDocumento.descrizione").setColumns(14);

		// rapporto bancario azienda
		componentsBanca[0] = builder.addLabel("rapportoBancarioAzienda", 9);
		Binding bindingBanca = bf.createBoundSearchText("rapportoBancarioAzienda", new String[] { "numero",
				"descrizione" }, new String[] {}, new String[] {});
		SearchPanel searchBanca = (SearchPanel) bindingBanca.getControl();
		searchBanca.getTextFields().get("numero").setColumns(5);
		searchBanca.getTextFields().get("descrizione").setColumns(14);
		componentsBanca[1] = builder.addBinding(bindingBanca, 11);

		builder.nextRow();

		componentSpeseIncasso = builder.addPropertyAndLabel("speseIncasso", 1);
		((JTextField) componentSpeseIncasso[1]).setColumns(7);

		componentsAnticipoFatture = builder.addPropertyAndLabel("dataScadenzaAnticipoFatture", 5);

		JPanel raggruppamentoEffetto = getComponentFactory().createPanel(new HorizontalLayout(10));
		raggruppamentoEffetto.add(bf.createBinding("generaSingoloEffetto").getControl());
		raggruppamentoEffetto.add(bf.createBinding("dataEffetto").getControl());

		componentRaggruppamentoEffetto = new JComponent[2];
		componentRaggruppamentoEffetto[0] = builder.addLabel("generaSingoloEffetto", 9);

		builder.addComponent(raggruppamentoEffetto, 11);
		componentRaggruppamentoEffetto[1] = raggruppamentoEffetto;
		getFormModel().getFieldMetadata("dataEffetto").setReadOnly(true);

		JComponent[] numDoc = builder.addPropertyAndLabel("numeroAssegno", 5);
		assegnoCommand = new OpenPreviewAssegnoCommand();
		AbstractButton assegnoButton = assegnoCommand.createButton();
		builder.addComponent(assegnoButton, 8);
		assegnoCommand.addCommandInterceptor(new AssegnoCommandInterceptor());

		((JTextField) numDoc[1]).setColumns(9);
		componentsAssegno[0] = numDoc[0];
		componentsAssegno[1] = numDoc[1];
		JComponent[] abiDoc = builder.addPropertyAndLabel("abi", 9);
		((JTextField) abiDoc[1]).setColumns(4);
		componentsAssegno[2] = abiDoc[0];
		componentsAssegno[3] = abiDoc[1];
		JComponent[] cabDoc = builder.addPropertyAndLabel("cab", 13);
		((JTextField) cabDoc[1]).setColumns(4);
		componentsAssegno[4] = cabDoc[0];
		componentsAssegno[5] = cabDoc[1];
		componentsAssegno[6] = assegnoButton;

		componentsNoteContabili = builder.addPropertyAndLabel("noteContabili", 5, 3, 1, 1);

		// value change per visualizzare o meno il rapporto bancario a seconda
		// del tipo entita' del tipo documento
		TipoAreaPartitaValueChangeListener tipoAreaPartitaValueChangeListener = new TipoAreaPartitaValueChangeListener();
		this.addFormValueChangeListener("tipoAreaPartita", tipoAreaPartitaValueChangeListener);
		this.addFormValueChangeListener("generaSingoloEffetto", new GeneraSingoloEffettoValueChangeListener());
		tipoAreaPartitaValueChangeListener.propertyChange(null);

		return builder.getPanel(); // new FormDebugPanel(layout);
	}
}