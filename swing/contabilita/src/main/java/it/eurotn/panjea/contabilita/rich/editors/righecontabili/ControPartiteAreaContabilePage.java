/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.ETipologiaContoControPartita;
import it.eurotn.panjea.contabilita.rich.editors.tabelle.contropartiteprimanota.DescrizioneTableCellRenderer;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.dialog.CloseAction;

import com.jidesoft.grid.SortableTable;

/**
 * Pagina che gestisce le contro partite della struttura contabile dell'area contabile.
 * 
 * @author fattazzo
 * @version 1.0, 04/set/07
 */
public class ControPartiteAreaContabilePage extends AbstractDialogPage {

	private class OpenContropartiteContoCommand extends ApplicationWindowAwareCommand {

		@Override
		protected void doExecuteCommand() {
			ControPartiteAreaContabilePage.this.openControPartiteContoDialog();
		}

	}

	private static final String PAGE_ID = "controPartiteAreaContabilePage";

	/**
	 * La mappa contiene tutte le contropartite definite nella struttura contabile con l'importo avvalorato. La chiave
	 * della mappa e' la contropartita. Il valore e' una lista che contiene come primo valore la contropartita stessa e
	 * se questa e' di tipologia CONTO gli altri valori saranno le contropartite di tipologia SOTTOCONTO.
	 */
	private final Map<ControPartita, List<ControPartita>> mapControPartite;

	private JideTableWidget<ControPartita> table;

	/**
	 * Costruttore.
	 * 
	 * @param list
	 *            List<ControPartita>
	 */
	public ControPartiteAreaContabilePage(final List<ControPartita> list) {
		super(PAGE_ID);
		this.mapControPartite = createMapControPartite(list);
	}

	@Override
	protected JComponent createControl() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(getComponentFactory().createScrollPane(createTable()), BorderLayout.CENTER);
		return panel;
	}

	/**
	 * Crea una mappa contenente come chiave la contropartita e come valore una lista che contiene la contropartita.
	 * 
	 * @param list
	 *            List<ControPartita>
	 * @return Map<ControPartita, List<ControPartita>>
	 */
	private Map<ControPartita, List<ControPartita>> createMapControPartite(List<ControPartita> list) {
		Map<ControPartita, List<ControPartita>> map = new HashMap<ControPartita, List<ControPartita>>();

		for (ControPartita controPartita : list) {
			List<ControPartita> listControPartite = new ArrayList<ControPartita>();

			// Moltiplico il valore dell'ordine per 100 perchè nelle contropartite conto verranno inserite
			// tutte le contropartite sottoconto relative
			controPartita.setOrdine(controPartita.getOrdine() * 100);

			listControPartite.add(controPartita);
			map.put(controPartita, listControPartite);
		}

		return map;
	}

	/**
	 * Crea la tabella per visualizzare e modificare le contropartite.
	 * 
	 * @return tabella creata
	 */
	private JComponent createTable() {
		List<ControPartita> listAllControPartite = new ArrayList<ControPartita>();
		for (List<ControPartita> listControPartita : mapControPartite.values()) {
			listAllControPartite.addAll(listControPartita);
		}

		// ordino le contropartite perche' la lista ottenuta da mapControPartite.values()
		// non e' ordinata secondo la propriata' ordine
		Collections.sort(listAllControPartite, new Comparator<ControPartita>() {

			@Override
			public int compare(ControPartita o1, ControPartita o2) {
				return o1.getOrdine().compareTo(o2.getOrdine());
			}
		});

		table = new JideTableWidget<ControPartita>(PAGE_ID, new ControPartiteAreaContabileTableModel(PAGE_ID));
		((SortableTable) table.getTable()).setSortable(false);
		table.setRows(listAllControPartite);
		table.setPropertyCommandExecutor(new OpenContropartiteContoCommand());
		table.getTable().getColumnModel().getColumn(0).setCellRenderer(new DescrizioneTableCellRenderer());

		return table.getComponent();
	}

	/**
	 * restituisce la lista di tutte le contropartite presenti nella tabella.
	 * 
	 * @return List<ControPartita>
	 */
	public List<ControPartita> getControPartiteAll() {
		List<ControPartita> list = new ArrayList<ControPartita>();

		for (List<ControPartita> listControPartita : mapControPartite.values()) {

			for (ControPartita controPartita : listControPartita) {
				list.add(controPartita);
			}
		}
		return list;
	}

	/**
	 * Restituisce la lista di tutte le contropartite che andranno a generare le righe contabili scartando quelle di
	 * tipologia CONTO perche' rimpiazzate da tutte quelle di tipologia SOTTOCONTO che ne hanno ripartito l'importo.
	 * 
	 * @return List<ControPartita>
	 */
	public List<ControPartita> getControPartiteCompilate() {
		List<ControPartita> list = new ArrayList<ControPartita>();

		for (List<ControPartita> listControPartita : mapControPartite.values()) {

			for (ControPartita controPartita : listControPartita) {
				if (controPartita.getTipologiaContoControPartita() != ETipologiaContoControPartita.CONTO) {
					list.add(controPartita);
				}
			}
		}
		return list;
	}

	/**
	 * Apre la finestra di dialogo per poter ripartire l'importo della controparita CONTO selezionata e inserisce o
	 * toglie, una volta chiuso, tutte le contropartite SOTTOCONTO generate o tolte.
	 */
	private void openControPartiteContoDialog() {
		// Ottengo la contropartita selezionata
		ControPartita controPartita = table.getSelectedObject();

		// controllo che sia di tipologia CONTO
		if (controPartita.getTipologiaContoControPartita() == ETipologiaContoControPartita.CONTO) {

			// carico la lista della contropartite della mappa
			List<ControPartita> list = mapControPartite.get(controPartita);

			// apro la dialog
			ControPartiteContoAreaContabileDialog dialog = new ControPartiteContoAreaContabileDialog(list);
			dialog.setCloseAction(CloseAction.HIDE);
			dialog.showDialog();

			// controllo che la dialog sia stata chiusa con successo, altrimenti non faccio niente
			if (dialog.isFinish()) {
				List<ControPartita> listControPartiteSottoConti = dialog.getControPartiteConImporto();

				// sostituisco nella mappa le contropartite compilate dall'utente
				ControPartiteAreaContabilePage.this.mapControPartite.put(controPartita, listControPartiteSottoConti);

				List<ControPartita> listAllControPartite = new ArrayList<ControPartita>();
				for (List<ControPartita> listControPartita : mapControPartite.values()) {
					listAllControPartite.addAll(listControPartita);
				}

				Collections.sort(listAllControPartite, new Comparator<ControPartita>() {

					@Override
					public int compare(ControPartita o1, ControPartita o2) {
						return o1.getOrdine().compareTo(o2.getOrdine());
					}
				});

				table.setRows(listAllControPartite);
				table.getTable().getColumnModel().getColumn(0).setCellRenderer(new DescrizioneTableCellRenderer());
			}

			dialog = null;
		}
	}

}
