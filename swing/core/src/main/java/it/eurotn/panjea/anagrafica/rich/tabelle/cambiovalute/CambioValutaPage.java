package it.eurotn.panjea.anagrafica.rich.tabelle.cambiovalute;

import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.rich.bd.IValutaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.util.Calendar;

import org.springframework.richclient.command.AbstractCommand;

public class CambioValutaPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "cambioValutaPage";

	private IValutaBD valutaBD;

	private ValutaAzienda valuta;

	/**
	 * Costruttore.
	 */
	public CambioValutaPage() {
		super(PAGE_ID, new CambioValutaForm());
	}

	@Override
	protected Object doDelete() {
		valutaBD.cancellaCambioValuta((CambioValuta) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		CambioValuta cambioValuta = (CambioValuta) getBackingFormPage().getFormObject();
		cambioValuta = valutaBD.salvaCambioValuta(cambioValuta);
		valuta = cambioValuta.getValuta();
		return cambioValuta;
	}

	@Override
	public AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	@Override
	protected Object getNewEditorObject() {
		CambioValuta cambioValuta = new CambioValuta();
		cambioValuta.setValuta(valuta);
		cambioValuta.setData(Calendar.getInstance().getTime());
		setFormObject(cambioValuta);
		return cambioValuta;
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
		super.postSetFormObject(object);
	}

	@Override
	public void preSetFormObject(Object object) {

		super.preSetFormObject(object);
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param valuta
	 *            The valuta to set.
	 */
	public void setValuta(ValutaAzienda valuta) {
		this.valuta = valuta;
	}

	/**
	 * @param valutaBD
	 *            The valutaBD to set.
	 */
	public void setValutaBD(IValutaBD valutaBD) {
		this.valutaBD = valutaBD;
	}
}
