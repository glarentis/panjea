package it.eurotn.panjea.rate.rich.editors.calendarirate;

import it.eurotn.panjea.rate.rich.forms.calendarirate.RiepilogoCalendariRateForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class RiepilogoCalendariRatePage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "riepilogoCalendariRatePage";

	/**
	 * Costruttore.
	 * 
	 */
	public RiepilogoCalendariRatePage() {
		super(PAGE_ID, new RiepilogoCalendariRateForm());
	}

	@Override
	public boolean isDirty() {
		return false;
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

}
