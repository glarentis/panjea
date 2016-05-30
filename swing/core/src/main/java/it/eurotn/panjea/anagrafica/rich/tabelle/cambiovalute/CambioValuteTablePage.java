package it.eurotn.panjea.anagrafica.rich.tabelle.cambiovalute;

import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.rich.bd.IValutaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Calendar;
import java.util.List;

public class CambioValuteTablePage extends AbstractTablePageEditor<CambioValuta> {

	private ValutaAzienda valuta = null;
	private IValutaBD valutaBD;

	/**
	 * Costruttore.
	 */
	public CambioValuteTablePage() {
		super("cambioValutaTablePage", new CambioValutaTableModel());
	}

	/**
	 * @return Returns the valuta.
	 */
	public ValutaAzienda getValuta() {
		return valuta;
	}

	@Override
	public List<CambioValuta> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<CambioValuta> refreshTableData() {
		List<CambioValuta> cambi = null;

		if (valuta.getCodiceValuta() != null) {
			cambi = valutaBD.caricaCambiValute(valuta.getCodiceValuta(), Calendar.getInstance().get(Calendar.YEAR));
		}

		return cambi;
	}

	@Override
	public void setFormObject(Object object) {
		valuta = (ValutaAzienda) object;
		((CambioValutaPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setValuta(valuta);
	}

	/**
	 * @param valutaBD
	 *            The valutaBD to set.
	 */
	public void setValutaBD(IValutaBD valutaBD) {
		this.valutaBD = valutaBD;
	}
}
