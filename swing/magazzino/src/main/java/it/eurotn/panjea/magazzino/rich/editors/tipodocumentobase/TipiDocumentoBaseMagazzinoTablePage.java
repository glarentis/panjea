/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.tipodocumentobase;

import it.eurotn.panjea.magazzino.domain.TipoDocumentoBaseMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author Leonardo
 * 
 */
public class TipiDocumentoBaseMagazzinoTablePage extends AbstractTablePageEditor<TipoDocumentoBaseMagazzino> implements
		InitializingBean {

	private static final String PAGE_ID = "tipiDocumentoBaseMagazzinoTablePage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	protected TipiDocumentoBaseMagazzinoTablePage() {
		super(PAGE_ID, new String[] { "tipoOperazione", "tipoAreaMagazzino.tipoDocumento.codice",
				"tipoAreaMagazzino.tipoDocumento.descrizione" }, TipoDocumentoBaseMagazzino.class);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public Collection<TipoDocumentoBaseMagazzino> loadTableData() {
		return magazzinoAnagraficaBD.caricaTipiDocumentoBase();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<TipoDocumentoBaseMagazzino> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
