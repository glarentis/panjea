package it.eurotn.panjea.contabilita.rich.editors;

import it.eurotn.panjea.contabilita.util.RigaContabileDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.settings.support.PanjeaTableMemento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.settings.SettingsManager;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.HierarchicalTable;
import com.jidesoft.grid.HierarchicalTableComponentFactory;

public class ControlloMovimentiHierarchicalTableComponentFactory implements HierarchicalTableComponentFactory {

	private class TableChildComponent extends JPanel {

		private static final long serialVersionUID = -85380947682090389L;

		private JideTableWidget<RigaContabileDTO> tableWidget;

		/**
		 * Costruttore.
		 * 
		 * @param righeContabili
		 *            righe contabili
		 * 
		 */
		public TableChildComponent(final List<RigaContabileDTO> righeContabili) {
			super(new BorderLayout());
			setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

			tableWidget = new JideTableWidget<RigaContabileDTO>("controlloMovimentiHierarchicalTable",
					new TableChildTableModel());
			tableWidget.setRows(righeContabili);

			// altezza tabelle
			tableWidget.getComponent().setPreferredSize(
					new Dimension(200, tableWidget.getTable().getRowHeight()
							* righeContabili.size()
							+ new Double(tableWidget.getTable().getTableHeader().getPreferredSize().getHeight())
									.intValue()));

			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setPreferredSize(new Dimension(60, 20));

			add(tableWidget.getComponent(), BorderLayout.CENTER);
			add(panel, BorderLayout.WEST);
		}

		/**
		 * @return the tableWidget
		 */
		public JideTableWidget<RigaContabileDTO> getTableWidget() {
			return tableWidget;
		}
	}

	private class TableChildTableModel extends DefaultBeanTableModel<RigaContabileDTO> {

		private static final long serialVersionUID = 3573772139677406763L;

		private final ConverterContext importoContext;

		{
			importoContext = new NumberWithDecimalConverterContext();
			importoContext.setUserObject(2);
		}

		/**
		 * Costruttore.
		 */
		public TableChildTableModel() {
			super("controlloMovimentiHierarchicalTableModel", new String[] { "codiceSottoConto",
					"descrizioneSottoConto", "note", "importoDare", "importoAvere" }, RigaContabileDTO.class);
		}

		@Override
		public ConverterContext getConverterContextAt(int row, int j) {

			switch (j) {
			case 3:
			case 4:
				return importoContext;
			default:
				return null;
			}
		}
	}

	private SettingsManager settingsManager;

	private static Logger logger = Logger.getLogger(ControlloMovimentiHierarchicalTableComponentFactory.class);

	@Override
	public Component createChildComponent(HierarchicalTable arg0, Object values, int arg2) {

		@SuppressWarnings("unchecked")
		List<RigaContabileDTO> righeContabili = (List<RigaContabileDTO>) values;

		TableChildComponent tableChildComponent = new TableChildComponent(righeContabili);

		// eseguo il restore delle settings per la tabella
		try {
			PanjeaTableMemento tableMemento = new PanjeaTableMemento(tableChildComponent.getTableWidget().getTable(),
					"controlloMovimentiHierarchicalTable");
			tableMemento.restoreState(getSettingsManager().getUserSettings());
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento delle settings per la tabella", e);
		}

		return tableChildComponent;
	}

	@Override
	public void destroyChildComponent(HierarchicalTable arg0, Component component, int arg2) {
		TableChildComponent tableChildComponent = (TableChildComponent) component;

		// eseguo il salvataggio delle settings per la tabella
		try {
			PanjeaTableMemento tableMemento = new PanjeaTableMemento(tableChildComponent.getTableWidget().getTable(),
					"controlloMovimentiHierarchicalTable");
			tableMemento.saveState(getSettingsManager().getUserSettings());
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento delle settings per la tabella", e);
		}

		tableChildComponent.removeAll();
		tableChildComponent.getTableWidget().dispose();
		tableChildComponent = null;
	}

	/**
	 * @return Returns the settingsManager.
	 */
	private SettingsManager getSettingsManager() {
		if (settingsManager == null) {
			settingsManager = (SettingsManager) Application.services().getService(SettingsManager.class);
		}

		return settingsManager;
	}

}
