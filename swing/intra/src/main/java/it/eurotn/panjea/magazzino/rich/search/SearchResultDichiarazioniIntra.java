/**
 *
 */
package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.panjea.rich.pages.AbstractTableSearchResult;

import java.util.Collection;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author fattazzo
 * 
 */
public class SearchResultDichiarazioniIntra extends AbstractTableSearchResult<DichiarazioneIntra> {

	private static final String VIEW_ID = "searchResultDichiarazioniIntra";

	private IIntraBD intraBD;

	@Override
	protected DichiarazioneIntra doDelete(DichiarazioneIntra objectToDelete) {
		intraBD.cancellaDichiarazioneIntra(objectToDelete);
		return objectToDelete;
	}

	@Override
	protected String[] getColumnPropertyNames() {
		return new String[] { "tipoDichiarazione", "anno", "data", "mese", "trimestre", "codice" };
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return getDefaultCommand();
	}

	@Override
	protected Collection<DichiarazioneIntra> getData(Map<String, Object> parameters) {
		return intraBD.caricaDichiarazioniIntra();
	}

	@Override
	public String getId() {
		return VIEW_ID;
	}

	/**
	 * @return Returns the intraBD.
	 */
	public IIntraBD getIntraBD() {
		return intraBD;
	}

	@Override
	protected Class<DichiarazioneIntra> getObjectsClass() {
		return DichiarazioneIntra.class;
	}

	@Override
	protected Map<String, Object> getParameters() {
		return null;
	}

	@Override
	public Object reloadObject(DichiarazioneIntra object) {
		return intraBD.caricaDichiarazioneIntra(object.getId());
	}

	/**
	 * @param intraBD
	 *            The intraBD to set.
	 */
	public void setIntraBD(IIntraBD intraBD) {
		this.intraBD = intraBD;
	}
}
