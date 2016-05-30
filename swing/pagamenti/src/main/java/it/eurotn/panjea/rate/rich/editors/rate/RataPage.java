/**
 * 
 */
package it.eurotn.panjea.rate.rich.editors.rate;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.rich.forms.AbstractAreaRateModel;
import it.eurotn.panjea.rate.rich.forms.rate.RataPartitaForm;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.application.Application;

/**
 * @author Leonardo
 */
public class RataPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "rataPartitaPage";

	private AbstractAreaRateModel areaRateModel = null;

	/**
	 * 
	 * @param aziendaCorrente
	 *            aziendaCorrente
	 */
	public RataPage(final AziendaCorrente aziendaCorrente) {
		super(PAGE_ID, new RataPartitaForm(aziendaCorrente));
	}

	@Override
	protected Object doSave() {
		Rata rataPartita = (Rata) getBackingFormPage().getFormObject();
		return this.areaRateModel.salvaRata(rataPartita);
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
	protected void publishApplicationEvent(String e, Object obj) {
		PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(e, obj, areaRateModel);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	@Override
	public void refreshData() {

	}

	/**
	 * @param areaRateModel
	 *            the areaPartiteModel to set
	 */
	public void setAreaRateModel(AbstractAreaRateModel areaRateModel) {
		this.areaRateModel = areaRateModel;
		((RataPartitaForm) getBackingFormPage()).setAreaRateModel(areaRateModel);
	}

}
