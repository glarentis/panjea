package it.eurotn.panjea.beniammortizzabili.rich.editors.beni.bene;

import it.eurotn.rich.dialog.DockingCompositeDialogPage;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class QuoteFiscaliCompositePage extends DockingCompositeDialogPage implements IPageLifecycleAdvisor {

	public static final String PAGE_ID = "quoteFiscaliCompositePage";

	/**
	 * Costruttore.
	 * 
	 */
	public QuoteFiscaliCompositePage() {
		super(PAGE_ID);
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
	public void postSetFormObject(Object object) {
	}

	@Override
	public void preSetFormObject(Object object) {
	}

	@Override
	public void refreshData() {
	}

	@Override
	public void setFormObject(Object object) {
		setCurrentObject(object);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
	}

}
