package it.eurotn.panjea.magazzino.rich.editors.prefatturazione;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.util.MovimentoPreFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.magazzino.util.PreFatturazioneDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Visualizza tutti i movimenti generati da una pre fatturazione.
 *
 * @author fattazzo
 */
public class MovimentiPreFatturazioneTablePage extends AbstractTablePageEditor<MovimentoPreFatturazioneDTO> implements
Observer {

	public static final String PAGE_ID = "movimentiPreFatturazioneTablePage";

	private PreFatturazioneDTO preFatturazioneDTO;

	private MovimentiInFatturaTablePage movimentiInFatturaTablePage;

	/**
	 * Costruttore.
	 */
	protected MovimentiPreFatturazioneTablePage() {
		super(PAGE_ID, new MovimentiPreFatturazioneTableModel());
		getTable().setDelayForSelection(500);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public Collection<MovimentoPreFatturazioneDTO> loadTableData() {
		return preFatturazioneDTO.getMovimenti();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<MovimentoPreFatturazioneDTO> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		preFatturazioneDTO = (PreFatturazioneDTO) object;
	}

	/**
	 * @param movimentiInFatturaTablePage
	 *            the movimentiInFatturaTablePage to set
	 */
	public void setMovimentiInFatturaTablePage(MovimentiInFatturaTablePage movimentiInFatturaTablePage) {
		this.movimentiInFatturaTablePage = movimentiInFatturaTablePage;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);

		getTable().getTable().setEnabled(!readOnly);
	}

	@Override
	public void update(Observable observable, Object obj) {
		super.update(observable, obj);

		List<MovimentoPreFatturazioneDTO> movimenti = getTable().getSelectedObjects();

		if (movimenti != null && !movimenti.isEmpty()) {

			List<Integer> idAreeDaCercare = new ArrayList<Integer>();
			for (MovimentoPreFatturazioneDTO movimento : movimenti) {
				for (AreaMagazzinoLite area : movimento.getAreeCollegate()) {
					idAreeDaCercare.add(area.getId());
				}
			}

			ParametriRicercaAreaMagazzino parametri = new ParametriRicercaAreaMagazzino();
			parametri.setAnnoCompetenza(null);
			parametri.setIdAreeDaRicercare(idAreeDaCercare);
			parametri.setEffettuaRicerca(true);

			movimentiInFatturaTablePage.setFormObject(parametri);
			movimentiInFatturaTablePage.refreshData();
		}
	}

}
