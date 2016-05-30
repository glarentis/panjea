package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.DefaultGroupTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;

public class SelectRowListener extends MouseAdapter {

	private JideTableWidget<SedeListiniPM> table;
	private DefaultBeanTableModel<SedeListiniPM> tableModel;
	private DefaultGroupTableModel groupTableModel;
	private long lastEvent;

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private final ListinoSediCollegatePage listinoSediCollegatePage;

	/**
	 * Costruttore.
	 * 
	 * @param listinoSediCollegatePage
	 *            pagina dei listini.
	 * 
	 * @param table
	 *            tabella da agganciare al listener
	 * @param magazzinoAnagraficaBD
	 *            magazzinoAnagraficaBD
	 */
	@SuppressWarnings("unchecked")
	public SelectRowListener(final ListinoSediCollegatePage listinoSediCollegatePage,
			final JideTableWidget<SedeListiniPM> table, final IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		super();
		this.listinoSediCollegatePage = listinoSediCollegatePage;
		this.table = table;
		table.getTable().addMouseListener(this);
		tableModel = (DefaultBeanTableModel<SedeListiniPM>) TableModelWrapperUtils.getActualTableModel(table.getTable()
				.getModel());
		groupTableModel = (DefaultGroupTableModel) TableModelWrapperUtils.getActualTableModel(table.getTable()
				.getModel(), DefaultGroupTableModel.class);

		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	@Override
	public void mouseClicked(MouseEvent mouseevent) {
		// Non so perchè l'evento viene chiamato due volte. (ved. MouseClicked
		// su eventMulticaster).
		// controllo che l'evento sia generato in tempi diversi

		if (mouseevent.getWhen() == lastEvent) {
			return;
		} else {
			lastEvent = mouseevent.getWhen();
		}

		int rowIndex = table.getTable().rowAtPoint(mouseevent.getPoint());
		int columnIndex = table.getTable().columnAtPoint(mouseevent.getPoint());
		int columnIndexConvert = table.getTable().convertRowIndexToModel(
				table.getTable().convertColumnIndexToModel(columnIndex));
		int rowIndexConvert = table.getTable()
				.convertRowIndexToModel(table.getTable().convertRowIndexToModel(rowIndex));
		Object value = table.getTable().getModel().getValueAt(rowIndexConvert, columnIndexConvert);

		int groupCols = groupTableModel.getGroupColumnCount();

		if (value instanceof DefaultGroupRow || (columnIndexConvert + groupCols) < 2) {
			return;
		}

		SedeListiniPM sedeListiniPM = tableModel.getObject(TableModelWrapperUtils.getActualRowAt(table.getTable()
				.getModel(), rowIndex));

		if (!sedeListiniPM.isListinoAlternativoAssociato() && !sedeListiniPM.isListinoAssociato()) {
			// HO LISTINI ASSOCIATI SOLAMENTE PERCHE' SONO EREDITATI...ESCO
			return;
		}

		SedeMagazzino sedeMagazzino = null;
		switch (columnIndexConvert + groupCols) {
		case 2:
			// Rimuovo l'associazione del listino
			// sedeListiniPM.setListinoAssociato(!sedeListiniPM.isListinoAssociato());
			sedeMagazzino = magazzinoAnagraficaBD.caricaSedeMagazzinoBySedeEntita(sedeListiniPM.getSedeRiepilogo()
					.getSedeEntita(), true);
			sedeMagazzino.setListino(null);
			magazzinoAnagraficaBD.salvaSedeMagazzino(sedeMagazzino);
			break;
		case 3:
			sedeMagazzino = magazzinoAnagraficaBD.caricaSedeMagazzinoBySedeEntita(sedeListiniPM.getSedeRiepilogo()
					.getSedeEntita(), true);
			sedeMagazzino.setListinoAlternativo(null);
			magazzinoAnagraficaBD.salvaSedeMagazzino(sedeMagazzino);
		default:
			break;
		}

		listinoSediCollegatePage.loadData();
		table.getTable().setColumnSelectionInterval(columnIndex, columnIndex);
	}
}
