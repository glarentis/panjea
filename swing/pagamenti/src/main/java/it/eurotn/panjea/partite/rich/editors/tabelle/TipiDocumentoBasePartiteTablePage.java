/**
 * 
 */
package it.eurotn.panjea.partite.rich.editors.tabelle;

import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author Leonardo
 * 
 */
public class TipiDocumentoBasePartiteTablePage extends AbstractTablePageEditor<TipoDocumentoBasePartite> implements
		InitializingBean {

	private static final String PAGE_ID = "tipiDocumentoBasePartiteTablePage";
	private IPartiteBD partiteBD = null;

	/**
	 * Costruttore.
	 */
	protected TipiDocumentoBasePartiteTablePage() {
		super(PAGE_ID, new String[] { "tipoOperazione", "tipoAreaPartita.tipoDocumento.codice",
				"tipoAreaPartita.tipoDocumento.descrizione" }, TipoDocumentoBasePartite.class);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public Collection<TipoDocumentoBasePartite> loadTableData() {
		return partiteBD.caricaTipiDocumentoBase();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<TipoDocumentoBasePartite> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param partiteBD
	 *            partiteBD to set
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

}
