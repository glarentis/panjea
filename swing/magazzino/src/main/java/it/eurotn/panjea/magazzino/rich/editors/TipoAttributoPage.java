/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoVariante;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.TipoAttributoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author fattazzo
 */
public class TipoAttributoPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "tipoAttributoPage";

	private TipoAttributo tipoAttributo;
	private final IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private final IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 * 
	 * @param magazzinoAnagraficaBD
	 *            magazzinoAnagraficaBD
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD
	 */
	public TipoAttributoPage(final IMagazzinoAnagraficaBD magazzinoAnagraficaBD,
			final IAnagraficaTabelleBD anagraficaTabelleBD) {

		super(PAGE_ID, new TipoAttributoForm(anagraficaTabelleBD));
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaTipoAttributo((TipoAttributo) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		tipoAttributo = (TipoAttributo) getBackingFormPage().getFormModel().getFormObject();
		tipoAttributo = magazzinoAnagraficaBD.salvaTipoAttributo(tipoAttributo);
		return tipoAttributo;
	}

	/**
	 * @return the anagraficaTabelleBD
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	@Override
	protected Object getNewEditorObject() {
		TipoAttributo tipoAttrib = (TipoAttributo) super.getNewEditorObject();
		return tipoAttrib;
	}

	/**
	 * @return the tipoAttributo
	 */
	public TipoAttributo getTipoAttributo() {
		return tipoAttributo;
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

	@Override
	public void setFormObject(Object object) {
		if (!((IDefProperty) object).isNew()) {
			// Ricarico l'attributo per inizializzare le prop. lazy (ad.esempio le descizioni in lingua
			if (object instanceof TipoVariante) {
				object = magazzinoAnagraficaBD.caricaTipoVariante((TipoVariante) object);
			} else {
				object = magazzinoAnagraficaBD.caricaTipoAttributo((TipoAttributo) object);
			}
		}
		super.setFormObject(object);
	}

	/**
	 * @param tipoAttributo
	 *            the tipoAttributo to set
	 */
	public void setTipoAttributo(TipoAttributo tipoAttributo) {
		this.tipoAttributo = tipoAttributo;
	}
}
