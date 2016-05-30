package it.eurotn.panjea.bi.rich.commands;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiForm;
import it.eurotn.panjea.bi.rich.editors.analisi.style.AnalisiOpenDialogTableStyleProvider;
import it.eurotn.rich.command.OpenEditorCommand;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.grid.AbstractTableCellEditorRenderer;
import com.jidesoft.grid.CellRolloverSupport;
import com.jidesoft.grid.CellStyleTable;
import com.jidesoft.grid.GroupTable;
import com.jidesoft.grid.GroupTableHeader;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.swing.JideButton;

public class OpenDashBoardEditorCommand extends OpenEditorCommand {

	/**
	 *
	 * Renderer per il pulsante di delete.
	 *
	 * @author giangi
	 * @version 1.0, 23/lug/2012
	 *
	 */
	class ButtonsCellEditorRenderer extends AbstractTableCellEditorRenderer implements CellRolloverSupport {
		private static final long serialVersionUID = -1559557761862573989L;

		/**
		 * @param table
		 *            .
		 * @param editorRendererComponent
		 *            .
		 * @param forRenderer
		 *            .
		 * @param value
		 *            .
		 * @param isSelected
		 *            .
		 * @param hasFocus
		 *            .
		 * @param row
		 *            .
		 * @param column
		 *            .
		 */
		@Override
		public void configureTableCellEditorRendererComponent(final JTable table, Component editorRendererComponent,
				boolean forRenderer, final Object value, boolean isSelected, boolean hasFocus, final int row, int column) {
			JLabel label = (JLabel) (((JPanel) editorRendererComponent).getComponent(0));
			label.setText("");
			label.setBorder(null);

			if (value != null) {
				label.setText(value.toString());
			}
			label.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
			label.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

			JButton editButton = (JButton) (((JPanel) editorRendererComponent).getComponent(1));
			editButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String nomeDashBoard = (String) table.getModel().getValueAt(row, 0);
					final DashBoard dashBoard = businessIntelligenceBD.caricaDashBoard(nomeDashBoard);
					AnalisiBi analisiBi = new AnalisiBi();
					analisiBi.setDescrizione(dashBoard.getDescrizione());
					analisiBi.setCategoria(dashBoard.getCategoria());
					analisiBi.setNome(dashBoard.getNome());

					final AnalisiBiForm analisiBiForm = new AnalisiBiForm(analisiBi);
					PanjeaTitledPageApplicationDialog inputDialog = new PanjeaTitledPageApplicationDialog(
							analisiBiForm, null) {

						@SuppressWarnings("unchecked")
						@Override
						protected boolean onFinish() {
							AnalisiBi analisiBiToStore = (AnalisiBi) analisiBiForm.getFormObject();
							dashBoard.setNome(analisiBiToStore.getNome());
							dashBoard.setCategoria(analisiBiToStore.getCategoria());
							dashBoard.setDescrizione(analisiBiToStore.getDescrizione());
							((DefaultBeanTableModel<DashBoard>) TableModelWrapperUtils.getActualTableModel(table
									.getModel())).removeObject(dashBoard);
							DashBoard dashBoardSalvata = businessIntelligenceBD.salvaDashBoard(dashBoard);
							((DefaultBeanTableModel<DashBoard>) TableModelWrapperUtils.getActualTableModel(table
									.getModel())).addObject(dashBoardSalvata);
							return true;
						}
					};
					inputDialog.showDialog();
				}
			});
			JButton removeButton = (JButton) (((JPanel) editorRendererComponent).getComponent(2));
			removeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String nomeDashBoard = (String) table.getModel().getValueAt(row, 0);
					ConfirmationDialog confirmation = new ConfirmationDialog("Conferma cancellazione",
							"Confermi la cancellazione della dashboard " + nomeDashBoard) {

						@SuppressWarnings("unchecked")
						@Override
						protected void onConfirm() {
							String nomeDashBoard = (String) table.getModel().getValueAt(row, 0);
							businessIntelligenceBD.cancellaDashBoard(nomeDashBoard);
							int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
							DashBoard result = ((DefaultBeanTableModel<DashBoard>) TableModelWrapperUtils
									.getActualTableModel(table.getModel())).getElementAt(actualRow);
							((DefaultBeanTableModel<DashBoard>) TableModelWrapperUtils.getActualTableModel(table
									.getModel())).removeObject(result);

						}
					};
					confirmation.showDialog();
				}
			});
			editorRendererComponent.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
			editorRendererComponent.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
		}

		/**
		 *
		 * @param icon
		 *            icona edl pulsante
		 * @return button per la tabella
		 */
		private JButton createButton(Icon icon) {
			JButton button = new JideButton(icon);
			button.setOpaque(false);
			button.setContentAreaFilled(false);
			// button.setPreferredSize(new Dimension(9, 9));
			// button.setMaximumSize(new Dimension(9, 9));
			// button.setMinimumSize(new Dimension(9, 9));
			button.setContentAreaFilled(false);
			button.setFocusPainted(false);
			button.setFocusable(false);
			button.setRequestFocusEnabled(false);
			button.setHorizontalAlignment(SwingConstants.CENTER);
			return button;
		}

		/**
		 * Creao il renderer per i pulsanti e la label.
		 *
		 * @param table
		 *            .
		 * @param row
		 *            .
		 * @param column
		 *            .
		 * @return .
		 */
		@Override
		public Component createTableCellEditorRendererComponent(JTable table, int row, int column) {
			FormLayout layout = new FormLayout("max(p;5dlu):grow, right:max(p;5dlu), right:max(p;5dlu)", "default");
			JPanel panel = new JPanel(layout);
			JLabel label = new JLabel();
			panel.add(label, new CellConstraints("1, 1, 1, 1, default, default"));
			panel.add(createButton(RcpSupport.getIcon("edit")), new CellConstraints("2, 1, 1, 1, default, default"));
			panel.add(createButton(RcpSupport.getIcon("deleteCommand.icon")), new CellConstraints(
					"3, 1, 1, 1, default, default"));
			return panel;
		}

		/**
		 * Valore della cella per l'editor sempre a null.
		 *
		 * @return .
		 */
		@Override
		public Object getCellEditorValue() {
			return null;
		}

		/**
		 * Rollover = true.
		 *
		 * @param table
		 *            .
		 * @param e
		 *            .
		 * @param row
		 *            .
		 * @param column
		 *            .
		 * @return .
		 */
		@Override
		public boolean isRollover(JTable table, MouseEvent e, int row, int column) {
			return true;
		}
	}

	private IBusinessIntelligenceBD businessIntelligenceBD;

	@Override
	protected void doExecuteCommand() {
		final JideTableWidget<DashBoard> table = new JideTableWidget<DashBoard>("listaDashBoard", new String[] {
				"nome", "descrizione", "categoria" }, DashBoard.class);
		table.setTableType(TableType.GROUP);
		table.setAggregatedColumns(new String[] { "categoria" });
		((GroupTable) table.getTable()).setCellRendererManagerEnabled(false);
		((CellStyleTable) table.getTable()).setTableStyleProvider(new AnalisiOpenDialogTableStyleProvider());
		// table.getTable().getColumnModel().getColumn(1).setCellEditor(new ButtonsCellEditorRenderer());
		// table.getTable().getColumnModel().getColumn(1).setCellRenderer(new ButtonsCellEditorRenderer());

		final ApplicationDialog dialog = new ApplicationDialog() {

			@Override
			protected JComponent createDialogContentPane() {
				List<DashBoard> listaDashBoard = businessIntelligenceBD.caricaListaDashBoard();
				((GroupTableHeader) table.getTable().getTableHeader()).setGroupHeaderEnabled(false);
				table.setRows(listaDashBoard);
				table.getTable().getColumnModel().getColumn(1).setCellEditor(new ButtonsCellEditorRenderer());
				table.getTable().getColumnModel().getColumn(1).setCellRenderer(new ButtonsCellEditorRenderer());
				table.getTable().getTableHeader().setPreferredSize(new Dimension(0, 0));
				((GroupTable) table.getTable()).setExpandIconVisible(false);
				table.setPropertyCommandExecutor(new ActionCommandExecutor() {

					@Override
					public void execute() {
						getFinishCommand().execute();
					}
				});
				JPanel rootPanel = new JPanel(new BorderLayout());
				JScrollPane scroll = new JScrollPane(table.getTable());
				table.getTable().setBorder(null);
				scroll.setBorder(null);
				rootPanel.add(scroll, BorderLayout.CENTER);
				table.getTable().requestFocusInWindow();
				return rootPanel;
			}

			@Override
			protected void disposeDialogContentPane() {
				super.disposeDialogContentPane();
				table.setPropertyCommandExecutor(null);
			}

			@Override
			protected boolean onFinish() {
				DashBoard dashBoard = null;
				if (table.getSelectedObject() != null) {
					dashBoard = businessIntelligenceBD.caricaDashBoard(table.getSelectedObject().getNome());
					LifecycleApplicationEvent event = new OpenEditorEvent(dashBoard);
					Application.instance().getApplicationContext().publishEvent(event);
				}
				return true;
			}
		};
		dialog.showDialog();
	}

	/**
	 * @param businessIntelligenceBD
	 *            The businessIntelligenceBD to set.
	 */
	public void setBusinessIntelligenceBD(IBusinessIntelligenceBD businessIntelligenceBD) {
		this.businessIntelligenceBD = businessIntelligenceBD;
	}

}
