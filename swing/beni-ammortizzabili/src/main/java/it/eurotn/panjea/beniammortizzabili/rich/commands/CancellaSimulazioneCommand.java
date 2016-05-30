/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.commands;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;

import java.awt.Component;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.selection.dialog.ListSelectionDialog;

/**
 * 
 * @author Aracno
 * @version 1.0, 20/nov/06
 * 
 */
public class CancellaSimulazioneCommand extends ApplicationWindowAwareCommand {

	private class SimulazioniSelectionDialog extends ListSelectionDialog {

		private Simulazione simulazioneSelected = null;

		/**
		 * Costruttore che richiede il titolo, la parent window e la lista delle simulazioni da selezionare.
		 * 
		 * @param title
		 *            tilolo del dialog
		 * @param parent
		 *            parent window
		 * @param list
		 *            lista di simulaizoni
		 */
		public SimulazioniSelectionDialog(final String title, final Window parent, final List<?> list) {
			super(title, parent, list);
			setRenderer(new DefaultListCellRenderer() {
				/**
				 * Comment for <code>serialVersionUID</code>
				 */
				private static final long serialVersionUID = -6600465552132434943L;

				@Override
				public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3,
						boolean arg4) {
					JLabel label = (JLabel) super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
					label.setText(((Simulazione) arg1).getData() + " - " + ((Simulazione) arg1).getDescrizione());
					return label;
				}
			});
		}

		/**
		 * @return la simulazione selezionata
		 */
		public Simulazione getSimulazioneSelected() {
			return simulazioneSelected;
		}

		@Override
		protected void onSelect(Object selection) {
			simulazioneSelected = (Simulazione) selection;
		}

	}

	private String idCommand;

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	@Override
	public void afterPropertiesSet() {
		org.springframework.util.Assert.notNull(idCommand, "idCommand must be set");
		org.springframework.util.Assert.notNull(beniAmmortizzabiliBD, "beniAmmortizzabiliBD must be set");
		setId(idCommand);
		super.afterPropertiesSet();
	}

	@Override
	protected void doExecuteCommand() {
		// carico la lista di tutte le simulaizoni
		List<Simulazione> listSimulazioni;
		listSimulazioni = beniAmmortizzabiliBD.caricaSimulazioni();

		// creo una lista si sole simulazione non consolidate perch� quelle
		// consolidate non si possono cancellare
		List<Simulazione> listSimulazioniNonConsolidate = new ArrayList<Simulazione>();
		for (Simulazione simulazione : listSimulazioni) {
			if (!simulazione.isConsolidata()) {
				listSimulazioniNonConsolidate.add(simulazione);
			}
		}

		MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
				.getService(MessageSourceAccessor.class);
		if (listSimulazioniNonConsolidate.size() > 0) {
			String titolo = messageSourceAccessor.getMessage("selezioneSimulazioneDialog.title", new Object[] {},
					Locale.getDefault());

			SimulazioniSelectionDialog selectionDialog = new SimulazioniSelectionDialog(titolo, null,
					listSimulazioniNonConsolidate);
			selectionDialog.setCloseAction(CloseAction.HIDE);
			selectionDialog.showDialog();
			if (selectionDialog.getSimulazioneSelected() != null) {
				Simulazione simulazioneSelected = selectionDialog.getSimulazioneSelected();
				beniAmmortizzabiliBD.cancellaSimulazione(simulazioneSelected);
			}
		} else {
			String titolo = messageSourceAccessor.getMessage("cancellaSimulazione.noSimulazioni.title",
					new Object[] {}, Locale.getDefault());
			String messaggio = messageSourceAccessor.getMessage("cancellaSimulazione.noSimulazioni.message",
					new Object[] {}, Locale.getDefault());

			MessageDialog dialog = new MessageDialog(titolo, messaggio);
			dialog.setModal(true);
			dialog.setCloseAction(CloseAction.DISPOSE);
			dialog.showDialog();
		}
	}

	/**
	 * @param beniAmmortizzabiliBD
	 *            the beniAmmortizzabiliBD to set
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	/**
	 * @param idCommand
	 *            the idCommand to set
	 */
	public void setIdCommand(String idCommand) {
		this.idCommand = idCommand;
	};
}
