package it.eurotn.panjea.rate.rich.editors.calendarirate;

import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.RigaCalendarioRate;
import it.eurotn.panjea.rate.rich.bd.ICalendariRateBD;
import it.eurotn.panjea.rate.rich.forms.calendarirate.RigaCalendarioRateForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class RigaCalendarioRatePage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "rigaCalendarioRatePage";

	private CalendarioRate calendarioRate;

	private ICalendariRateBD calendariRateBD;

	/**
	 * Costruttore.
	 * 
	 */
	public RigaCalendarioRatePage() {
		super(PAGE_ID, new RigaCalendarioRateForm());
	}

	@Override
	protected Object doDelete() {
		RigaCalendarioRate riga = (RigaCalendarioRate) getBackingFormPage().getFormObject();

		calendariRateBD.cancellaRigaCalendarioRate(riga);
		return riga;
	}

	@Override
	protected Object doSave() {
		RigaCalendarioRate rigaCalendario = (RigaCalendarioRate) getBackingFormPage().getFormObject();
		rigaCalendario.setCalendarioRate(calendarioRate);
		rigaCalendario = calendariRateBD.salvaRigaCalendarioRate(rigaCalendario);

		return rigaCalendario;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
				toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
		return commands;
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
		// TODO Auto-generated method stub

	}

	/**
	 * @param calendarioRate
	 *            the calendarioRate to set
	 */
	public void setCalendarioRate(CalendarioRate calendarioRate) {
		this.calendarioRate = calendarioRate;
	}

	/**
	 * @param calendariRateBD
	 *            the calendariRateBD to set
	 */
	public void setCalendariRateBD(ICalendariRateBD calendariRateBD) {
		this.calendariRateBD = calendariRateBD;
	}

}
