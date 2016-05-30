/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.anagrafica.rich.tabelle.TipoDocumentoPage;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * TablePage di {@link TipoDocumentoPage}.
 * 
 * @author adriano
 * @version 1.0, 27/ago/07
 * 
 */
public class TipiDocumentoBaseTablePage extends AbstractTablePageEditor<TipoDocumentoBase> {

	private static final String PAGE_ID = "tipiDocumentoBaseTablePage";
	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	/**
	 * Costruttore.
	 */
	protected TipiDocumentoBaseTablePage() {
		super(PAGE_ID, new String[] { "tipoOperazione", "tipoAreaContabile.tipoDocumento.codice",
				"tipoAreaContabile.tipoDocumento.descrizione" }, TipoDocumentoBase.class);
	}

	@Override
	public Collection<TipoDocumentoBase> loadTableData() {
		return contabilitaAnagraficaBD.caricaTipiDocumentoBase();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<TipoDocumentoBase> refreshTableData() {
		return null;
	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            contabilitaAnagraficaBD to set
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

	@Override
	public void setFormObject(Object object) {
	}

}
