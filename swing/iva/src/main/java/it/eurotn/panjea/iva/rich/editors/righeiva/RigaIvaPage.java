package it.eurotn.panjea.iva.rich.editors.righeiva;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.rich.forms.righeiva.AbstractAreaIvaModel;
import it.eurotn.panjea.iva.rich.forms.righeiva.RigaIvaForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

public class RigaIvaPage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(RigaIvaPage.class);
	public static final String PAGE_ID = "rigaIvaPage";
	private AbstractAreaIvaModel areaIvaModel = null;

	/**
	 * Costruttore di default.
	 */
	public RigaIvaPage() {
		super(PAGE_ID, new RigaIvaForm());

	}

	@Override
	protected Object doDelete() {
		RigaIva ri = (RigaIva) getBackingFormPage().getFormObject();
		areaIvaModel.cancellaRigaIva(ri);
		return ri;
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Salvo la riga iva.");
		RigaIva rigaIva = (RigaIva) getBackingFormPage().getFormObject();

		try {
			rigaIva = this.areaIvaModel.salvaRigaIva(rigaIva);
		} catch (CodiceIvaCollegatoAssenteException e) {
			// per lo split playment non è obbligatorio l'iva collegata
			if (logger.isDebugEnabled()) {
				logger.debug("--> codice iva collegato non presente");
			}
			// throw new RuntimeException(e);
		}

		logger.debug("--> Riga iva salvata: " + rigaIva);
		return rigaIva;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onNew() {
		super.onNew();
		// TIPSSSSSSSSSSS: Devo abilitare il salva perchè la rigaIva se ho il codice prevalente è già compilata.
		CodiceIva codiceIva = ((RigaIva) getForm().getFormModel().getFormObject()).getCodiceIva();
		getForm().getValueModel("codiceIva").setValue(null);
		getForm().getValueModel("codiceIva").setValue(codiceIva);
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
		((RigaIvaForm) getBackingFormPage()).activateListeners();
	}

	@Override
	public void preSetFormObject(Object object) {
		((RigaIvaForm) getBackingFormPage()).deactivateListeners();
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param areaIvaModel
	 *            The areaIvaModel to set.
	 */
	public void setAreaIvaModel(AbstractAreaIvaModel areaIvaModel) {
		this.areaIvaModel = areaIvaModel;
		((RigaIvaForm) getBackingFormPage()).setAreaIvaModel(areaIvaModel);
	}

}
