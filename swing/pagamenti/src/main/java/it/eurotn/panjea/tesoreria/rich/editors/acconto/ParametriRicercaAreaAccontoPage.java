package it.eurotn.panjea.tesoreria.rich.editors.acconto;

import it.eurotn.panjea.tesoreria.rich.forms.acconto.ParametriRicercaAreaAccontoForm;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ParametriRicercaAreaAccontoPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "parametriRicercaDocumentoAccontoPage";

	/**
	 * Costruttore.
	 */
	public ParametriRicercaAreaAccontoPage() {
		super(PAGE_ID, new ParametriRicercaAreaAccontoForm());
		installPropertyChange();
	}

	/**
	 * Installa i property change sull'oggetto gestito dal form per rilanciare il cambio dell'oggetto ad ogni
	 * variazione.
	 */
	private void installPropertyChange() {
		getBackingFormPage().getFormModel().getValueModel("statoAcconto")
				.addValueChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						ParametriRicercaAcconti parametriRicercaAcconti = (ParametriRicercaAcconti) getBackingFormPage()
								.getFormObject();
						parametriRicercaAcconti.setEffettuaRicerca(true);
						ParametriRicercaAreaAccontoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED,
								null, parametriRicercaAcconti);
					}
				});

		getBackingFormPage().getFormModel().getValueModel("tipoEntita")
				.addValueChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						ParametriRicercaAcconti parametriRicercaAcconti = (ParametriRicercaAcconti) getBackingFormPage()
								.getFormObject();
						parametriRicercaAcconti.setEffettuaRicerca(true);
						ParametriRicercaAreaAccontoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED,
								null, parametriRicercaAcconti);
					}
				});
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
		loadData();
	}

}
