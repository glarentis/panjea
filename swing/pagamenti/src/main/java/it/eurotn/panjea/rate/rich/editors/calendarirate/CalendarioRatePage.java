package it.eurotn.panjea.rate.rich.editors.calendarirate;

import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.rich.bd.ICalendariRateBD;
import it.eurotn.panjea.rate.rich.forms.calendarirate.CalendarioRateForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class CalendarioRatePage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "calendarioRatePage";

	private ICalendariRateBD calendariRateBD;

	/**
	 * Costruttore.
	 */
	public CalendarioRatePage() {
		super(PAGE_ID, new CalendarioRateForm());
	}

	@Override
	protected Object doDelete() {
		CalendarioRate calendarioRate = (CalendarioRate) getBackingFormPage().getFormObject();
		calendariRateBD.cancellaCalendarioRate(calendarioRate);
		return calendarioRate;
	}

	@Override
	protected Object doSave() {
		CalendarioRate calendarioRate = (CalendarioRate) getBackingFormPage().getFormObject();
		calendarioRate = calendariRateBD.salvaCalendarioRate(calendarioRate);
		return calendarioRate;
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
	}

	/**
	 * @param calendariRateBD
	 *            the calendariRateBD to set
	 */
	public void setCalendariRateBD(ICalendariRateBD calendariRateBD) {
		this.calendariRateBD = calendariRateBD;
	}

}
