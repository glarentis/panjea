package it.eurotn.panjea.beniammortizzabili.rich.forms.simulazione;

import it.eurotn.panjea.beniammortizzabili.exception.AreeContabiliSimulazioneException;
import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.editors.simulazioni.SimulazionePage;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class CalcolaSimulazioneCommand extends ApplicationWindowAwareCommand {

	private class AreeContabiliSimulazioneExceptionDialog extends ConfirmationDialog {

		private AreeContabiliSimulazioneException exception;
		private Simulazione simulazione;

		/**
		 * Costruttore.
		 */
		public AreeContabiliSimulazioneExceptionDialog() {
			super("Documenti contabili presenti", "_");
		}

		@Override
		protected JComponent createDialogContentPane() {
			FormLayout layout = new FormLayout(
					"left:80dlu,4dlu,left:default,right:200dlu",
					"default,default,10dlu,default,default,default,default,default,default,default,default,default,default,default,default,default");
			PanelBuilder builder = new PanelBuilder(layout);
			CellConstraints cc = new CellConstraints();

			builder.addLabel("Attenzione, si stà cercando di ricalcolare una simulazione già contabilizzata.",
					cc.xyw(1, 1, 4));
			builder.addLabel("Sono presenti i seguenti documenti:", cc.xyw(1, 2, 4));

			builder.addLabel("Stato", cc.xy(1, 4));
			builder.addLabel("Numero documenti", cc.xy(3, 4));
			builder.addSeparator("", cc.xyw(1, 5, 3));

			int row = 6;
			for (Entry<StatoAreaContabile, Integer> entry : exception.getNumeroAreePerStato().entrySet()) {
				builder.addLabel(entry.getValue().toString(), cc.xy(3, row));

				Icon icon = RcpSupport.getIcon(entry.getKey().getClass().getName() + "#" + entry.getKey().name());
				JLabel statoLabel = new JLabel(ObjectConverterManager.toString(entry.getKey()), icon,
						SwingConstants.LEFT);
				builder.add(statoLabel, cc.xy(1, row));

				row++;
			}

			builder.addLabel(" ", cc.xy(1, row++));
			builder.addLabel("Il ricalcolo eliminarà i movimenti, continuare?", cc.xyw(1, row++, 4));

			return builder.getPanel();
		}

		@Override
		protected void onConfirm() {
			simulazione = beniAmmortizzabiliBD.salvaSimulazione(simulazione, true);
			simulazione = beniAmmortizzabiliBD.caricaSimulazione(simulazione);
			LifecycleApplicationEvent event = new OpenEditorEvent(simulazione);
			Application.instance().getApplicationContext().publishEvent(event);
		}

		/**
		 * @param exception
		 *            the exception to set
		 */
		public void setException(AreeContabiliSimulazioneException exception) {
			this.exception = exception;
		}

		/**
		 * @param simulazione
		 *            the simulazione to set
		 */
		public void setSimulazione(Simulazione simulazione) {
			this.simulazione = simulazione;
		}

	}

	public static final String COMMAND_ID = "calcolaSimulazioneCommand";

	private final SimulazionePage simulazionePage;

	private final MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	private AreeContabiliSimulazioneExceptionDialog exceptionDialog;

	/**
	 * Costruttore di default.
	 *
	 * @param paramSimulazionePage
	 *            Page della simulaizone
	 * @param paramBeniAmmortizzabiliBD
	 *            BD per le operazioni di storage
	 */
	public CalcolaSimulazioneCommand(final SimulazionePage paramSimulazionePage,
			final IBeniAmmortizzabiliBD paramBeniAmmortizzabiliBD) {
		super(COMMAND_ID);
		setSecurityControllerId(COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		c.configure(this);
		this.simulazionePage = paramSimulazionePage;
		this.beniAmmortizzabiliBD = paramBeniAmmortizzabiliBD;

		exceptionDialog = new AreeContabiliSimulazioneExceptionDialog();
	}

	@Override
	protected void doExecuteCommand() {
		// non posso ricalcolare una simulazione gi� consolidata
		if (((Simulazione) this.simulazionePage.getBackingFormPage().getFormObject()).isConsolidata()) {
			String title = messageSource.getMessage("calcolaSimulazione.alreadyConsolidata.title", new Object[] {},
					Locale.getDefault());
			String message = messageSource.getMessage("calcolaSimulazione.alreadyConsolidata.message", new Object[] {},
					Locale.getDefault());

			MessageDialog dialog = new MessageDialog(title, message);
			dialog.setModal(true);
			dialog.setPreferredSize(new Dimension(300, 70));
			dialog.setCloseAction(CloseAction.DISPOSE);
			dialog.showDialog();
		} else {
			try {
				simulazionePage.setReadOnly(true);

				Simulazione simulazioneDaSalvare = (Simulazione) CalcolaSimulazioneCommand.this.simulazionePage
						.getBackingFormPage().getFormObject();

				List<PoliticaCalcolo> listPoliticheDirty = new ArrayList<PoliticaCalcolo>();

				// al metodo salva simulazione passo solo le potiche di calcolo che risultano
				// essere dirty e se seleziono solo la politicaCalcoloBene allora verra' generata
				// una exception nella creazione del treetable nel wizard simulazione a causa del fatto che
				// nel tree non ho una root(Gruppo) a cui attaccare leafs(Speci e sottospecie e bene).
				// per evitare questo sul business quando creo una nuova simulazione tutte le politiche
				// vengono messe a dirty true.
				for (PoliticaCalcolo politicaCalcolo : simulazioneDaSalvare.getPoliticheCalcolo()) {
					if (politicaCalcolo.isDirty()) {
						listPoliticheDirty.add(politicaCalcolo);
					}
				}
				simulazioneDaSalvare.getPoliticheCalcolo().clear();
				simulazioneDaSalvare.setPoliticheCalcolo(listPoliticheDirty);

				Simulazione simulazioneSalvata;
				try {
					simulazioneSalvata = beniAmmortizzabiliBD.salvaSimulazione(simulazioneDaSalvare);
					Simulazione simulazione = beniAmmortizzabiliBD.caricaSimulazione(simulazioneSalvata);
					LifecycleApplicationEvent event = new OpenEditorEvent(simulazione);
					Application.instance().getApplicationContext().publishEvent(event);
				} catch (AreeContabiliSimulazioneException e) {
					exceptionDialog.setException(e);
					exceptionDialog.setSimulazione(simulazioneDaSalvare);
					exceptionDialog.showDialog();
				}
			} finally {
				simulazionePage.setReadOnly(false);
			}
		}
	}

}
