package it.eurotn.panjea.anagrafica.rich.editors.rubrica;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.table.renderer.RubricaDenominazioneCellRenderer;
import it.eurotn.panjea.anagrafica.util.RubricaDTO;
import it.eurotn.rich.control.table.AutoResizePopupMenuCustomizer;
import it.eurotn.rich.control.table.command.ExportXLSCommand;
import it.eurotn.rich.control.table.style.DefaultCellStyleProvider;
import it.eurotn.rich.services.TableLayoutCache;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.progress.ProgressMonitor;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.Memento;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.AutoFilterTableHeader;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.FilterableTreeTableModel;
import com.jidesoft.grid.QuickTableFilterField;
import com.jidesoft.grid.RootExpandableRow;
import com.jidesoft.grid.SortableTreeTableModel;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.grid.TableColumnChooserPopupMenuCustomizer;
import com.jidesoft.grid.TableHeaderPopupMenuInstaller;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.grid.TreeTable;
import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;

public class RubricaEditor extends AbstractEditor implements Memento {

	private class DefaultAction extends ActionCommand {

		@Override
		protected void doExecuteCommand() {
			try {
				RubricaRow rigaSelezionata = getRigaSelezionata();
				if (rigaSelezionata != null) {
					String className = rigaSelezionata.getRubricaDTO().getRowClass().getName();
					if (className.endsWith("Cliente") || className.endsWith("Vettore")
							|| className.endsWith("Fornitore")) {
						Entita entita = (Entita) rigaSelezionata.getRubricaDTO().getRowClass().newInstance();
						entita.setId(rigaSelezionata.getRubricaDTO().getId());
						entita = anagraficaBD.caricaEntita(entita);
						LifecycleApplicationEvent event = new OpenEditorEvent(entita);
						Application.instance().getApplicationContext().publishEvent(event);
					}
				}
			} catch (Exception ex) {
				logger.error("-->errore ", ex);
			}
		}

	}

	private class KeyListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				defaultAction.execute();
				e.consume();
			}
		}

	}

	private class MouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			if (e.getClickCount() == 2) {
				defaultAction.execute();
			}
		}
	}

	private class RefreshCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "refreshCommand";

		/**
		 * Costruttore.
		 */
		public RefreshCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			List<RubricaDTO> rubrica = anagraficaBD.caricaAnagraficheFull();
			List<RubricaRow> rubricaRows = new ArrayList<RubricaRow>();

			for (RubricaDTO rubricaDTO : rubrica) {
				rubricaRows.add(creaRigaRubrica(rubricaDTO));
			}
			RootExpandableRow root = (RootExpandableRow) ((RubricaTableModel) filterField.getTableModel()).getRoot();
			root.removeAllChildren();
			((RubricaTableModel) filterField.getTableModel()).addRows(rubricaRows);
			((RubricaTableModel) filterField.getTableModel()).fireTableDataChanged();

			treeTable.expandAll();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(EDITOR_ID + "." + COMMAND_ID);
		}
	}

	@SuppressWarnings("rawtypes")
	private class StripeSortableTreeTableModel extends SortableTreeTableModel implements StyleModel {
		private static final long serialVersionUID = 7707484364461990477L;
		private CellStyle cellStyle = new CellStyle();

		/**
		 * Costruttore.
		 * 
		 * @param model
		 *            treeModel della rubrica
		 */
		public StripeSortableTreeTableModel(final TableModel model) {
			super(model);
		}

		@Override
		public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
			cellStyle.setHorizontalAlignment(-1);
			cellStyle.setForeground(Color.BLACK);
			if (rowIndex % 2 == 0) {
				cellStyle.setBackground(DefaultCellStyleProvider.EVEN_COLOR);
			} else {
				cellStyle.setBackground(DefaultCellStyleProvider.ODD_COLOR);
			}
			return cellStyle;
		}

		@Override
		public boolean isCellStyleOn() {
			return true;
		}
	}

	public static final String EDITOR_ID = "rubricaEditor";

	private IAnagraficaBD anagraficaBD;

	private RubricaTableModel tableModel;

	private TreeTable treeTable;

	private TableLayoutCache tableLayoutCache;

	private MouseListener mouseListener;

	private ActionCommand defaultAction;

	private KeyListener keyListener;

	private JPanel rootPanel;

	private QuickTableFilterField filterField;

	/**
	 * 
	 * @param rubrica
	 *            della rubrica con i "figli"
	 * @return riga da inserire nella tree
	 */
	private RubricaRow creaRigaRubrica(RubricaDTO rubrica) {
		RubricaRow rubriche = new RubricaRow(rubrica);
		for (RubricaDTO rubricaDTO : rubrica.getRubricaDTO()) {
			rubriche.addChild(creaRigaRubrica(rubricaDTO));
		}
		return rubriche;
	}

	@Override
	public void dispose() {
		super.dispose();
		treeTable.removeMouseListener(mouseListener);
		treeTable.removeKeyListener(keyListener);
	}

	@Override
	public JComponent getControl() {
		if (rootPanel == null) {
			JPanel quickSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
			filterField = new QuickTableFilterField(tableModel);
			filterField.setObjectConverterManagerEnabled(true);
			filterField.getTextField().setName(getId() + ".ricercaTextField");
			quickSearchPanel.add(filterField);
			quickSearchPanel.setBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED,
					PartialSide.NORTH), "Chiave di ricerca", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP));

			JPanel tablePanel = new JPanel(new BorderLayout(2, 2));

			@SuppressWarnings("unchecked")
			SortableTreeTableModel<RubricaRow> sortableTreeTableModel = new StripeSortableTreeTableModel(
					new FilterableTreeTableModel<RubricaRow>(filterField.getDisplayTableModel()));
			sortableTreeTableModel.setDefaultSortableOption(SortableTreeTableModel.SORTABLE_LEAF_LEVEL);
			sortableTreeTableModel.setSortableOption(0, SortableTreeTableModel.SORTABLE_ROOT_LEVEL);
			treeTable = new TreeTable(sortableTreeTableModel) {
				private static final long serialVersionUID = 2021621226432784777L;

				@Override
				public TableCellRenderer getActualCellRenderer(int i, int j) {
					if (j == 0) {
						return new RubricaDenominazioneCellRenderer();
					}
					return super.getActualCellRenderer(i, j);
				}

			};

			TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(treeTable);
			installer.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
			installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());

			// do not clear selection when filtering
			treeTable.setClearSelectionOnTableDataChanges(false);
			treeTable.setRowResizable(false);
			treeTable.setVariousRowHeights(false);
			treeTable.setShowTreeLines(false);
			treeTable.setAutoCreateColumnsFromModel(false);
			treeTable.expandAll();
			treeTable.setPreferredScrollableViewportSize(new Dimension(700, 400));
			treeTable.setName(EDITOR_ID + "TableWidget");

			AutoFilterTableHeader header = new AutoFilterTableHeader(treeTable);
			header.setShowFilterIcon(false);
			header.setAutoFilterEnabled(true);
			header.setUseNativeHeaderRenderer(true);
			header.setAllowMultipleValues(true);
			header.setFont(header.getFont().deriveFont(Font.BOLD));
			treeTable.setTableHeader(header);
			treeTable.setRowHeight(22);
			mouseListener = new MouseListener();
			keyListener = new KeyListener();
			treeTable.addMouseListener(mouseListener);
			treeTable.addKeyListener(keyListener);

			if (defaultAction == null) {
				defaultAction = new DefaultAction();
			}
			filterField.setTable(treeTable);
			JScrollPane scrollPane = new JScrollPane(treeTable);

			tablePanel.add(scrollPane);

			rootPanel = new JPanel(new BorderLayout(3, 3));
			rootPanel.add(tablePanel);

			JPanel topPanel = new JPanel(new BorderLayout());
			topPanel.add(quickSearchPanel, BorderLayout.WEST);

			JPanel buttonPanel = getComponentFactory().createPanel(new HorizontalLayout(5));
			buttonPanel.add(new RefreshCommand().createButton());
			AbstractButton exportButton = new ExportXLSCommand(treeTable).createButton();
			exportButton.setText("Esporta");
			buttonPanel.add(exportButton);

			JPanel bottomPanel = getComponentFactory().createPanel(new BorderLayout());
			bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

			topPanel.add(bottomPanel, BorderLayout.EAST);
			rootPanel.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);
		}
		return rootPanel;
	}

	@Override
	public String getId() {
		return EDITOR_ID;
	}

	/**
	 * @return Returns the rigaSelezionata.
	 */
	public RubricaRow getRigaSelezionata() {
		RubricaTableModel rubricaTableModel = (RubricaTableModel) TableModelWrapperUtils.getActualTableModel(
				treeTable.getModel(), RubricaTableModel.class);
		int actualRow = TableModelWrapperUtils.getActualRowAt(treeTable.getModel(), treeTable.getSelectedRow());
		RubricaRow rigaSelezionata = rubricaTableModel.getRowAt(actualRow);
		return rigaSelezionata;
	}

	/**
	 * @return the tableLayoutCache
	 */
	private TableLayoutCache getTableLayoutCache() {
		if (tableLayoutCache == null) {
			tableLayoutCache = (TableLayoutCache) Application.services().getService(TableLayoutCache.class);
		}

		return tableLayoutCache;
	}

	/**
	 * 
	 * @return treeTable utilizzato per visualizzare la rubrica.
	 */
	public TreeTable getTreeTable() {
		return treeTable;
	}

	@Override
	public void initialize(Object editorObject) {
		List<RubricaDTO> rubrica = anagraficaBD.caricaAnagraficheFull();
		List<RubricaRow> rubricaRows = new ArrayList<RubricaRow>();

		for (RubricaDTO rubricaDTO : rubrica) {
			rubricaRows.add(creaRigaRubrica(rubricaDTO));
		}
		tableModel = new RubricaTableModel(rubricaRows);
	}

	@Override
	public void restoreState(Settings arg0) {
		List<TableLayout> layouts = getTableLayoutCache().caricaTableLayout("rubricaTable");
		if (layouts.size() == 1) {
			TableUtils.setTablePreferenceByName(treeTable, layouts.get(0).getData());
		}
	}

	@Override
	public void save(ProgressMonitor arg0) {
		throw new UnsupportedOperationException("SAVE....operazione non supportata.");
	}

	@Override
	public void saveState(Settings arg0) {
		String tablePref = TableUtils.getTablePreferenceByName(treeTable);
		TableLayout layout = new TableLayout();
		layout.setChiave("rubricaTable");
		layout.setGlobal(false);
		layout.setData(tablePref);
		getTableLayoutCache().salva(layout);
	}

	/**
	 * @param anagraficaBD
	 *            The anagraficaBD to set.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @param defaultAction
	 *            The defaultAction to set.
	 */
	public void setDefaultAction(ActionCommand defaultAction) {
		this.defaultAction = defaultAction;
	}

	/**
	 * Filtro da applicare alla rubrica.
	 * 
	 * @param filter
	 *            filtro
	 */
	public void setFilter(String filter) {
		filterField.setText(filter);
	}

}
