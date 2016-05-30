/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.contratto;

import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.List;

/**
 * @author leonardo
 * 
 */
public class ContrattiSpesometroTablePage extends AbstractTablePageEditor<ContrattoSpesometro> {

	public static final String PAGE_ID = "contrattiSpesometroTablePage";
	private IAnagraficaTabelleBD anagraficaTabelleBD = null;
	private EntitaLite entita = null;

	/**
	 * Costruttore.
	 */
	protected ContrattiSpesometroTablePage() {
		super(PAGE_ID, new String[] { "codice", "dataInizio", "dataFine", "entita" }, ContrattoSpesometro.class);
		getTable().setDelayForSelection(200);
	}

	/**
	 * @return the anagraficaTabelleBD
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
	}

	@Override
	public List<ContrattoSpesometro> loadTableData() {
		return anagraficaTabelleBD.caricaContratti(entita);
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<ContrattoSpesometro> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	@Override
	public void setFormObject(Object object) {
		entita = null;
		if (object instanceof Entita) {
			entita = ((Entita) object).getEntitaLite();
		}
		ContrattoSpesometroPage contrattoSpesometroPage = (ContrattoSpesometroPage) getEditPages().get(
				EditFrame.DEFAULT_OBJECT_CLASS_NAME);
		contrattoSpesometroPage.setEntita(entita);
	}

}
