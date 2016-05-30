package it.eurotn.panjea.cauzioni.rich.editors.situazionecauzioni;

import it.eurotn.panjea.cauzioni.rich.bd.ICauzioniBD;
import it.eurotn.panjea.cauzioni.util.parametriricerca.ParametriRicercaSituazioneCauzioni;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniDTO;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.RcpSupport;

public class RisultatiRicercaSituazioneCauzioniTablePage extends AbstractTablePageEditor<SituazioneCauzioniDTO> {

	private class ShowDetailCommand extends JideToggleCommand {

		public static final String COMMAND_ID = "showDetailCommand";

		/**
		 * Costruttore.
		 */
		public ShowDetailCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void onDeselection() {
			RisultatiRicercaSituazioneCauzioniTablePage.this.firePropertyChange(
					RisultatiRicercaSituazioneCauzioniTablePage.SHOW_DETAIL_CHANGE, null, false);
			super.onDeselection();
		}

		@Override
		protected void onSelection() {
			RisultatiRicercaSituazioneCauzioniTablePage.this.firePropertyChange(
					RisultatiRicercaSituazioneCauzioniTablePage.SHOW_DETAIL_CHANGE, null, true);
			super.onSelection();
		}

	}

	public static final String PAGE_ID = "risultatiRicercaSituazioneCauzioniTablePage";
	public static final String SELECTED_OBJECT_CHANGE = "selectedObjectChange";
	public static final String SHOW_DETAIL_CHANGE = "showDetailChange";

	private ParametriRicercaSituazioneCauzioni parametriRicerca;

	private ShowDetailCommand showDetailCommand;
	private ICauzioniBD cauzioniBD;

	/**
	 * 
	 * Costruttore.
	 */
	protected RisultatiRicercaSituazioneCauzioniTablePage() {
		super(PAGE_ID, new RisultatiRicercaSituazioneCauzioniTableModel());
		getTable().setDelayForSelection(500);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getShowDetailCommand() };
	}

	/**
	 * @return the ShowDetailCommand
	 */
	public ShowDetailCommand getShowDetailCommand() {
		if (showDetailCommand == null) {
			showDetailCommand = new ShowDetailCommand();
		}

		return showDetailCommand;
	}

	@Override
	public Collection<SituazioneCauzioniDTO> loadTableData() {
		List<SituazioneCauzioniDTO> situazioni = new ArrayList<SituazioneCauzioniDTO>();

		if (parametriRicerca.isEffettuaRicerca()) {
			situazioni = cauzioniBD.caricaSituazioneCauzioni(parametriRicerca);
		}

		return situazioni;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<SituazioneCauzioniDTO> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param cauzioniBD
	 *            the cauzioniBD to set
	 */
	public void setCauzioniBD(ICauzioniBD cauzioniBD) {
		this.cauzioniBD = cauzioniBD;
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof ParametriRicercaSituazioneCauzioni) {
			parametriRicerca = (ParametriRicercaSituazioneCauzioni) object;
		} else {
			parametriRicerca = new ParametriRicercaSituazioneCauzioni();
		}
	}

	@Override
	public void update(Observable observable, Object obj) {
		super.update(observable, obj);

		List<SituazioneCauzioniDTO> situazioni = getTable().getSelectedObjects();

		if (situazioni != null && !situazioni.isEmpty()) {

			Set<Integer> entita = new TreeSet<Integer>();
			Set<Integer> sediEntita = new TreeSet<Integer>();
			Set<Integer> articoli = new TreeSet<Integer>();

			for (SituazioneCauzioniDTO situazione : situazioni) {
				entita.add(situazione.getEntitaDocumento().getId());
				sediEntita.add(situazione.getSedeEntita().getId());
				articoli.add(situazione.getArticolo().getId());
			}

			ParametriRicercaDettaglioMovimentazioneCauzioni parametri = new ParametriRicercaDettaglioMovimentazioneCauzioni(
					entita, sediEntita, articoli);

			RisultatiRicercaSituazioneCauzioniTablePage.this.firePropertyChange(
					RisultatiRicercaSituazioneCauzioniTablePage.SELECTED_OBJECT_CHANGE, null, parametri);
		}
	}
}
