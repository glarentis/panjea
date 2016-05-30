package it.eurotn.panjea.anagrafica.rich.editors.rapportobancarioentita;

import it.eurotn.panjea.anagrafica.domain.RapportoBancario;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

public class RapportiBancariSedeEntitaTablePage extends AbstractTablePageEditor<RapportoBancarioSedeEntita> {

	private static final String PAGE_ID = "rapportiBancariSedeEntitaTablePage";
	public static final String KEY_MSG_OVERLAY_TABLE = PAGE_ID + ".table.overlay.message";
	private IAnagraficaBD anagraficaBD;
	private SedeEntita sedeEntita;

	/**
	 * costruttore.
	 */
	protected RapportiBancariSedeEntitaTablePage() {
		super(PAGE_ID, new String[] { RapportoBancario.PROP_NUMERO, RapportoBancario.PROP_DESCRIZIONE,
				RapportoBancario.PROP_ABILITATO }, RapportoBancarioSedeEntita.class);
	}

	/**
	 * @return the anagraficaBD
	 */
	public IAnagraficaBD getAnagraficaBD() {
		return anagraficaBD;
	}

	@Override
	public Collection<RapportoBancarioSedeEntita> loadTableData() {
		Collection<RapportoBancarioSedeEntita> rapporti = new ArrayList<RapportoBancarioSedeEntita>();

		if (!sedeEntita.isEreditaRapportiBancari()) {
			rapporti = anagraficaBD.caricaRapportiBancariSedeEntita(sedeEntita.getId());
		}

		return rapporti;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		boolean initializePage = true;
		return initializePage;
	}

	@Override
	public void processTableData(Collection<RapportoBancarioSedeEntita> results) {
		super.processTableData(results);
		if (sedeEntita != null) {
			this.setReadOnly(sedeEntita.isEreditaRapportiBancari());
		}
	}

	@Override
	public Collection<RapportoBancarioSedeEntita> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void saveState(Settings settings) {
		super.saveState(settings);
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public void setFormObject(Object object) {
		this.sedeEntita = (SedeEntita) object;
		// setto la sede entità anche nella pagina di dettaglio
		// per dare la possibilità di settarla in un nuovo rapporto sedeEntità
		((RapportoBancarioSedeEntitaPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME))
		.setSedeEntita(sedeEntita);

		if (sedeEntita != null && sedeEntita.isEreditaRapportiBancari()) {
			getTable().getOverlayTable().setStyledMessage(
					"{" + RcpSupport.getMessage(KEY_MSG_OVERLAY_TABLE) + ":f:gray}");
		} else {
			getTable().getOverlayTable().setMessage(null);
		}
	}
}
