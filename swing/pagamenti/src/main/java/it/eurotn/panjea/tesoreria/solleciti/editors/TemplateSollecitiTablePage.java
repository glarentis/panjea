package it.eurotn.panjea.tesoreria.solleciti.editors;

import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.solleciti.TemplateSolleciti;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

public class TemplateSollecitiTablePage extends AbstractTablePageEditor<TemplateSolleciti> {
	public static final String PAGE_ID = "templateSollecitoPage";
	private ITesoreriaBD tesoreriaBD;

	/**
	 * costruttore.
	 */
	public TemplateSollecitiTablePage() {
		super(PAGE_ID, new String[] { "descrizione" }, TemplateSolleciti.class);
	}

	/**
	 * @return the tesoreriaBD
	 */
	public ITesoreriaBD getTesoreriaBD() {
		return tesoreriaBD;
	}

	@Override
	public Collection<TemplateSolleciti> loadTableData() {
		return tesoreriaBD.caricaTemplateSolleciti();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<TemplateSolleciti> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

}
