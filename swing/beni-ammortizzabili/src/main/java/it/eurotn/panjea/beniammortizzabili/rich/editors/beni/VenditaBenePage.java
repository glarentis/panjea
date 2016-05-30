/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.beniammortizzabili.exception.VenditaInAnnoConsolidatoException;
import it.eurotn.panjea.beniammortizzabili.exception.VenditaInAnnoSimulatoException;
import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.VenditaBene;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.MessageDialog;

/**
 * 
 * @author Aracno,Leonardo
 * @version 1.0, 12/ott/06
 * 
 */
public class VenditaBenePage extends FormBackedDialogPageEditor {

	private class AskRicalcolaDialog extends ConfirmationDialog {

		private VenditaBene venditaBene = null;

		/**
		 * Costruttore.
		 * 
		 * @param title
		 *            titolo
		 * @param message
		 *            messaggio
		 */
		public AskRicalcolaDialog(final String title, final String message) {
			super(title, message);
		}

		/**
		 * @return vendita bene
		 */
		public VenditaBene getVenditaBene() {
			return venditaBene;
		}

		@Override
		protected void onCancel() {
			venditaBene = null;
			super.onCancel();
		}

		@Override
		protected void onConfirm() {

			ApplicationDialog dialog = new ApplicationDialog(getMessage("ricalcoloSimulazione.title"), null) {

				@Override
				protected JComponent createDialogContentPane() {
					JPanel panel = new JPanel(new BorderLayout());
					JLabel label = new JLabel(getMessage("ricalcoloSimulazione.message", null));
					label.setIcon(getIconSource().getIcon("wait"));
					panel.add(label, BorderLayout.CENTER);
					panel.setPreferredSize(new Dimension(300, 100));
					return panel;
				}

				@Override
				protected Object[] getCommandGroupMembers() {
					return new Object[] {};
				}

				@Override
				protected void onAboutToShow() {
					AsyncWorker.post(new AsyncTask() {

						@Override
						public void failure(Throwable arg0) {
						}

						@Override
						public void finish() {
							dispose();
						}

						@Override
						public Object run() throws Exception {
							venditaBene = beniAmmortizzabiliBD.salvaVenditaBene(venditaBene, true);
							return null;
						}

						@Override
						public void success(Object arg0) {
						}
					});
				}

				@Override
				protected boolean onFinish() {
					return true;
				}
			};

			dialog.setModal(true);
			dialog.setCloseAction(CloseAction.DISPOSE);
			dialog.showDialog();
		}

		/**
		 * @param venditaBene
		 *            the venditaBene to set
		 */
		public void setVenditaBene(VenditaBene venditaBene) {
			this.venditaBene = venditaBene;
		}

	}

	private static Logger logger = Logger.getLogger(VenditaBenePage.class);
	private static final String PAGE_ID = "venditeBeneTablePage.venditaBenePage";
	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	private BeneAmmortizzabile beneAmmortizzabile;

	/**
	 * Costruttore.
	 * 
	 * @param beneAmmortizzabile
	 *            bene
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public VenditaBenePage(final BeneAmmortizzabile beneAmmortizzabile, final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(PAGE_ID, new VenditaBeneForm(beneAmmortizzabile, beniAmmortizzabiliBD));
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
		this.beneAmmortizzabile = beneAmmortizzabile;
		VenditaBene venditaBene = new VenditaBene();
		venditaBene.setId(-1);
		this.setFormObject(venditaBene);
	}

	/**
	 * @param venditaBene
	 *            vendita
	 * @return vendita
	 */
	private Object askRicalcolaSimulazione(VenditaBene venditaBene) {
		String title = getMessage("askRicalcolaSimulazioneDialog.title");
		String messagge = getMessage("askRicalcolaSimulazioneDialog.message");
		AskRicalcolaDialog askRicalcolaDialog = new AskRicalcolaDialog(title, messagge);
		askRicalcolaDialog.setVenditaBene(venditaBene);
		askRicalcolaDialog.showDialog();
		return askRicalcolaDialog.getVenditaBene();
	}

	@Override
	protected Object doDelete() {
		VenditaBene vendita = (VenditaBene) getBackingFormPage().getFormObject();

		try {
			beniAmmortizzabiliBD.cancellaVenditaBene(vendita);
		} catch (VenditaInAnnoSimulatoException e) {
			new MessageDialog("Attenzione",
					"Non è possibile cancellare la vendita perchè è presente una simulazione nell'anno.").showDialog();
			return null;
		} catch (VenditaInAnnoConsolidatoException e) {
			new MessageDialog("Attenzione",
					"Non è possibile cancellare la vendita perchè è presente una simulazione consolidata nell'anno.")
					.showDialog();
			return null;
		}
		return vendita;
	}

	@Override
	protected Object doSave() {
		VenditaBene venditaBene = null;
		try {
			VenditaBene venditaDaSalvare = (VenditaBene) getBackingFormPage().getFormObject();

			venditaDaSalvare.setBene(beneAmmortizzabile);
			logger.debug("--> mi accingo al salvataggio della vendita " + venditaDaSalvare + " associata al bene "
					+ beneAmmortizzabile);
			venditaBene = beniAmmortizzabiliBD.salvaVenditaBene(venditaDaSalvare);
			logger.debug("--> ho salvato la vendita " + venditaDaSalvare + " associata al bene " + beneAmmortizzabile);
			// risetto la TipologiaEliminazione a nuovo oggetto nella
			// setFormObject
		} catch (VenditaInAnnoSimulatoException e) {
			logger.debug("--> vendita in anno simulato ! devo ricalcolare la simulazione ? chiedo all'utente.");
			return askRicalcolaSimulazione((VenditaBene) getBackingFormPage().getFormObject());
		}
		return venditaBene;
	}

	/**
	 * @return the beneAmmortizzabile
	 */
	public BeneAmmortizzabile getBeneAmmortizzabile() {
		return beneAmmortizzabile;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
				toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
		return abstractCommands;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {

	}

	/**
	 * @param beneAmmortizzabile
	 *            the beneAmmortizzabile to set
	 */
	public void setBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		this.beneAmmortizzabile = beneAmmortizzabile;
		toolbarPageEditor.getNewCommand().execute();
		((VenditaBeneForm) this.getBackingFormPage()).setBeneAmmortizzabile(beneAmmortizzabile);
	}

	@Override
	public void setFormObject(Object object) {
		VenditaBene venditaBene = ((VenditaBene) object);
		super.setFormObject(venditaBene);
	}
}
