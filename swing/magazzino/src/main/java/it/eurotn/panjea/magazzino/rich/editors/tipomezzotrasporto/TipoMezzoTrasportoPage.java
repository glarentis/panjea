package it.eurotn.panjea.magazzino.rich.editors.tipomezzotrasporto;

import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.tipomezzotrasporto.TipoMezzoTrasportoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author Leonardo
 */
public class TipoMezzoTrasportoPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "tipoMezzoTrasportoPage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

	/**
	 * Costruttore di default,inizializza un nuovo form.
	 */
	public TipoMezzoTrasportoPage() {
		super(PAGE_ID, new TipoMezzoTrasportoForm());
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaTipoMezzoTrasporto((TipoMezzoTrasporto) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		TipoMezzoTrasporto tipoMezzoTrasporto = (TipoMezzoTrasporto) this.getForm().getFormObject();
		return magazzinoAnagraficaBD.salvaTipoMezzoTrasporto(tipoMezzoTrasporto);
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
	 * Set magazzinoAnagraficaBD.
	 * 
	 * @param magazzinoAnagraficaBD
	 *            il bd da utilizzare per accedere alle operazioni sui tipi mezzo trasporto
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
