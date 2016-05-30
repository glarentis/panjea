package it.eurotn.panjea.intra.rich.editors.dichiarazione;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;
import it.eurotn.panjea.intra.domain.DichiarazioneIntraVendite;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.panjea.intra.rich.bd.IntraBD;
import it.eurotn.rich.command.SeparatorActionCommand;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.util.Arrays;
import java.util.Calendar;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.selection.dialog.ListSelectionDialog;
import org.springframework.richclient.util.RcpSupport;

public class IntestazionePage extends FormBackedDialogPageEditor {

	private class GeneraDichiarazioneCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public GeneraDichiarazioneCommand() {
			super("generaDichiarazioneCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			DichiarazioneIntra dichiarazione = (DichiarazioneIntra) getForm().getFormObject();
			if (dichiarazione.getId() != null) {
				IIntraBD intrabd = RcpSupport.getBean(IntraBD.BEAN_ID);
				dichiarazione = intrabd.compilaDichiarazioneIntra(dichiarazione);

				setFormObject(dichiarazione);
				IntestazionePage.this.firePropertyChange(OBJECT_CHANGED, null, dichiarazione);
			}
		}
	}

	private IIntraBD intraBD;

	/**
	 * Costruttore.
	 */
	public IntestazionePage() {
		super("intestazionePage", new IntestazioneForm(new DichiarazioneIntraVendite()));
	}

	@Override
	protected Object doDelete() {
		DichiarazioneIntra dichiarazioneIntra = (DichiarazioneIntra) getForm().getFormObject();
		intraBD.cancellaDichiarazioneIntra(dichiarazioneIntra);
		return dichiarazioneIntra;
	}

	@Override
	protected Object doSave() {
		DichiarazioneIntra dichiarazioneIntra = (DichiarazioneIntra) getForm().getFormObject();
		return intraBD.salvaDichiarazioneIntra(dichiarazioneIntra);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] defaultCommand = toolbarPageEditor.getDefaultCommand(true);
		AbstractCommand[] commands = Arrays.copyOf(defaultCommand, defaultCommand.length + 2);
		commands[defaultCommand.length] = new SeparatorActionCommand();
		commands[defaultCommand.length + 1] = new GeneraDichiarazioneCommand();
		return commands;
	}

	/**
	 * @return Returns the intraBD.
	 */
	public IIntraBD getIntraBD() {
		return intraBD;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onNew() {
		super.onNew();
		ListSelectionDialog nuovaDichiarazioneDialog = new ListSelectionDialog("Tipo dichiarazione", null,
				Arrays.asList(TipoDichiarazione.values())) {

			@Override
			protected void onSelect(Object selection) {
				TipoDichiarazione tipologiaSelezionata = (TipoDichiarazione) selection;
				if (tipologiaSelezionata != null) {
					DichiarazioneIntra dichiarazioneIntra = intraBD.creaDichiarazioneIntra(tipologiaSelezionata);

					setFormObject(dichiarazioneIntra);
					// Avviso le altre pagine dell'editor che ho aggiornato l'oggetto
					// altrimenti rimangono con l'oggetto ricevuto all'apertura dell'editor
					IntestazionePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
							dichiarazioneIntra);
				}
			}
		};
		nuovaDichiarazioneDialog.showDialog();

		// devo abilitare il form al salvataggio nel caso in cui preparo un nuovo oggetto nel form;
		// dato che l'oggetto viene preparato sul server, per sporcare il form reimposto la data della dichiarazione
		if (!getBackingFormPage().getFormModel().isReadOnly()) {
			getBackingFormPage().getFormModel().getValueModel("data").setValue(Calendar.getInstance().getTime());
		}
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public void refreshData() {
	}

	@Override
	public void setFormObject(final Object object) {
		final DichiarazioneIntra intra = (DichiarazioneIntra) object;
		if (intra.getAnno() == null) {
			onNew();
		} else {
			super.setFormObject(object);
		}
	}

	/**
	 * @param intraBD
	 *            The intraBD to set.
	 */
	public void setIntraBD(IIntraBD intraBD) {
		this.intraBD = intraBD;
	}

}
