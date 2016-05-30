package it.eurotn.panjea.magazzino.rich.editors.tipodocumentobase;

import it.eurotn.panjea.magazzino.domain.TipoDocumentoBaseMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.tipodocumentobase.TipoDocumentoBaseMagazzinoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;

public class TipoDocumentoBaseMagazzinoPage extends FormBackedDialogPageEditor implements InitializingBean {

	private static final String PAGE_ID = "tipoDocumentoBaseMagazzinoPage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public TipoDocumentoBaseMagazzinoPage() {
		super(PAGE_ID, new TipoDocumentoBaseMagazzinoForm());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(magazzinoAnagraficaBD, "magazzinoAnagraficaBD cannot be null!");
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaTipoDocumentoBase((TipoDocumentoBaseMagazzino) getBackingFormPage()
				.getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		TipoDocumentoBaseMagazzino tipoDocumentoBase = (TipoDocumentoBaseMagazzino) getBackingFormPage()
				.getFormObject();
		TipoDocumentoBaseMagazzino tipoDocumentoMagazzinoSalvato = magazzinoAnagraficaBD
				.salvaTipoDocumentoBase(tipoDocumentoBase);
		return tipoDocumentoMagazzinoSalvato;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
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
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
