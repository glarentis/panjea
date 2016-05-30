package it.eurotn.panjea.auvend.rich.forms;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.rich.editors.importazioni.ArticoliMancantiComponent;
import it.eurotn.panjea.auvend.rich.editors.importazioni.ClientiDaVerificareComponent;
import it.eurotn.panjea.auvend.util.ParametriRecuperoRiparazioniContoTerzi;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class ImportazioneRiparazioniContoTerziForm extends PanjeaAbstractForm {
	private class ImportaCommand extends ApplicationWindowAwareCommand {

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
			ParametriRecuperoRiparazioniContoTerzi parametri = (ParametriRecuperoRiparazioniContoTerzi) getFormObject();
			try {
				logArea.getDocument().remove(0, logArea.getDocument().getLength());
				articoliMancantiFactory.getControl().setVisible(false);
				logArea.insert("Importazione in corso...", 0);
				StatisticaImportazione result = auVendBD.verificaRiparazioniContoTerzi(parametri.getDataInizio(),
						parametri.getDataFine());

				boolean datiValidi = true;

				if (!result.getArticoliMancanti().isEmpty()) {
					datiValidi = false;
					logArea.insert("\nArticoli non presenti in panjea. Inserire gli articoli mancanti.", logArea
							.getDocument().getLength());
				}

				if (!result.getClientiDaVerificare().isEmpty()) {
					datiValidi = false;
					logArea.insert("\nClienti non presenti in panjea. Inserire i clienti mancanti.", logArea
							.getDocument().getLength());
				}
				articoliMancantiFactory.aggiungiArticoliMancanti(result.getArticoliMancanti());
				articoliMancantiFactory.getControl().setVisible(!datiValidi);
				clientiDaVerificareComponent.aggiungiClientiDaVerificare(result.getClientiDaVerificare());
				clientiDaVerificareComponent.getControl().setVisible(!datiValidi);

				if (datiValidi) {
					auVendBD.importaRiparazioniContoTerzi(parametri.getDataInizio(), parametri.getDataFine());
					getFormModel().commit();
					logArea.insert("\nImportazione terminata con successo.", logArea.getDocument().getLength());
				}

			} catch (Exception e) {
				logger.error("-->errore nell'importazione dei movimenti", e);
				logArea.insert("\nImportazione terminata con errori. Errore ricervuto:\n" + e, logArea.getDocument()
						.getLength());
			}
		}
	}

	private static final String FORM_ID = "importazioneRiparazioniContoTerziForm";

	private ApplicationWindowAwareCommand importaCommand;

	private JTextArea logArea;

	private ArticoliMancantiComponent articoliMancantiFactory;
	private ClientiDaVerificareComponent clientiDaVerificareComponent;

	/**
	 * .
	 * 
	 * Costruttore.
	 */
	public ImportazioneRiparazioniContoTerziForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRecuperoRiparazioniContoTerzi(), false, FORM_ID),
				FORM_ID);
		new PanjeaFormGuard(getFormModel(), getImportaCommand());
	}

	@Override
	protected JComponent createFormControl() {
		IAnagraficaBD anagraficaBD = RcpSupport.getBean(IAnagraficaBD.BEAN_ID);
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref,4dlu,50dlu,10dlu,left:pref,4dlu,50dlu,50dlu,250dlu", "default");
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
		clientiDaVerificareComponent = new ClientiDaVerificareComponent(anagraficaBD);
		builder.addComponent(logArea, 1, 5, 8, 1);

		builder.nextRow();
		builder.addComponent(articoliMancantiFactory.getControl(), 1, 7, 8, 1);
		builder.addComponent(clientiDaVerificareComponent.getControl(), 9, 7, 1, 1);
		articoliMancantiFactory.getControl().setVisible(false);
		clientiDaVerificareComponent.getControl().setVisible(false);
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
