package it.eurotn.panjea.auvend.rich.forms;

import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.rich.editors.importazioni.ArticoliMancantiComponent;
import it.eurotn.panjea.auvend.util.ParametriRecuperoCarichiRifornimenti;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class ImportazioneCarichiRifornimentiForm extends PanjeaAbstractForm {
	private class ImportaCommand extends ApplicationWindowAwareCommand {

		private static final int MAX_GIORNI = 62;

		public static final String COMMAND_ID = "importaAuvendCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public ImportaCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			IAuVendBD auVendBD = RcpSupport.getBean(IAuVendBD.BEAN_ID);
			Calendar calendarioInioOperazioni = Calendar.getInstance();
			ParametriRecuperoCarichiRifornimenti parametri = (ParametriRecuperoCarichiRifornimenti) getFormObject();
			Date dataInizio = parametri.getDataInizio();
			Date dataFine = parametri.getDataFine();
			try {
				logArea.getDocument().remove(0, logArea.getDocument().getLength());
				articoliMancantiFactory.getControl().setVisible(false);
				logArea.insert("Verifica dati", 0);
				StatisticaImportazione result = auVendBD.verificaCarichi(dataInizio, dataFine);
				if (!result.getArticoliMancanti().isEmpty()) {
					logArea.insert("\nArticoli non presenti in panjea. Inserire gli articoli mancanti", logArea
							.getDocument().getLength());
					articoliMancantiFactory.aggiungiArticoliMancanti(result.getArticoliMancanti());
					articoliMancantiFactory.getControl().setVisible(true);
				} else {

					// Importo un giorno alla volta.

					Calendar calendario = Calendar.getInstance();
					Calendar calendarioFine = Calendar.getInstance();
					calendario.setTime(dataInizio);
					calendarioFine.setTime(dataFine);
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy EEE");

					int ii = 0;
					for (; ii < MAX_GIORNI && !calendario.after(calendarioFine); calendario.add(Calendar.DATE, 1), ii++) {
						logArea.insert("\nImportazione " + dateFormat.format(calendario.getTime()), logArea
								.getDocument().getLength());
						Calendar inizio = Calendar.getInstance();
						auVendBD.importaCarichiERifornimenti(calendario.getTime(), calendario.getTime());
						Calendar fine = Calendar.getInstance();
						long secondi = (fine.getTimeInMillis() - inizio.getTimeInMillis()) / 1000;
						logArea.insert(" (" + secondi + " sec)", logArea.getDocument().getLength());
					}

					if (ii >= MAX_GIORNI) {
						logArea.insert("\nRaggiunto il numero massimo di giorni importabili.", logArea.getDocument()
								.getLength());
					}

					getFormModel().commit();
					logArea.insert(
							"\nImportazione terminata con successo.\nRicordarsi di sincronizzare il data warehouse.",
							logArea.getDocument().getLength());
				}
			} catch (Exception e) {
				logger.error("-->errore nell'importazione dei movimenti", e);
				logArea.insert("\nImportazione terminata con errori. Errore ricervuto:\n" + e, logArea.getDocument()
						.getLength());
			}

			Calendar calendarioFineOperazioni = Calendar.getInstance();
			long secondi = (calendarioFineOperazioni.getTimeInMillis() - calendarioInioOperazioni.getTimeInMillis()) / 1000;
			logArea.insert("\nTempo impiegato in totale: " + secondi + " sec", logArea.getDocument().getLength());
		}
	}

	private static final String FORM_ID = "importazioneMovimentiForm";

	private ApplicationWindowAwareCommand importaCommand;

	private JTextArea logArea;

	private ArticoliMancantiComponent articoliMancantiFactory;

	/**
	 * .
	 * 
	 * Costruttore.
	 */
	public ImportazioneCarichiRifornimentiForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRecuperoCarichiRifornimenti(), false, FORM_ID),
				FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref,4dlu,50dlu,10dlu,left:pref,4dlu,50dlu,50dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.addPropertyAndLabel("dataInizio");
		builder.addPropertyAndLabel("dataFine", 5);
		builder.addComponent(getImportaCommand().createButton(), 8);
		builder.nextRow();
		logArea = new JTextArea();
		logArea.setRows(4);
		logArea.setBackground(UIManager.getColor("Panel.background"));
		builder.addComponent(logArea, 1, 3, 5, 1);
		builder.nextRow();
		articoliMancantiFactory = new ArticoliMancantiComponent();
		builder.addComponent(logArea, 1, 5, 8, 1);
		builder.nextRow();
		builder.addComponent(articoliMancantiFactory.getControl(), 1, 7, 8, 1);
		articoliMancantiFactory.getControl().setVisible(false);
		return builder.getPanel();
	}

	/**
	 * 
	 * @return command per l'importazione
	 */
	private ApplicationWindowAwareCommand getImportaCommand() {
		if (importaCommand == null) {
			importaCommand = new ImportaCommand();
		}
		return importaCommand;
	}

}
