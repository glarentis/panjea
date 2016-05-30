package it.eurotn.panjea.rich.editors.documentograph.toolbar.graph;

import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.rich.editors.documentograph.DocumentoGraphEditor;
import it.eurotn.panjea.rich.editors.documentograph.GraphLayoutsFactory;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.command.JideToggleCommand;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.action.CommandBar;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;

public class ToolbarDocumentoGraph extends AbstractControlFactory {

	private class ExpandCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "expandGraphCommand";

		private static final String EXPAND_STATE = "expand";
		private static final String COLLAPSE_STATE = "collapse";

		private CommandFaceDescriptor expandDescriptor;
		private CommandFaceDescriptor collapseDescriptor;

		private boolean collapseState = Boolean.TRUE;

		/**
		 * Costruttore.
		 */
		public ExpandCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);

			String toExpandLabel = RcpSupport.getMessage(COMMAND_ID + "." + EXPAND_STATE);
			String toCollapseLabel = RcpSupport.getMessage(COMMAND_ID + "." + COLLAPSE_STATE);
			Icon toExpandIcon = RcpSupport.getIcon(COMMAND_ID + "." + EXPAND_STATE);
			Icon toCollapseIcon = RcpSupport.getIcon(COMMAND_ID + "." + COLLAPSE_STATE);
			collapseDescriptor = new CommandFaceDescriptor(toCollapseLabel, toCollapseIcon, null);
			expandDescriptor = new CommandFaceDescriptor(toExpandLabel, toExpandIcon, null);
			applyDefaultFaceDescriptor();
		}

		/**
		 * Setta al command il face descriptor di defautl.
		 */
		public void applyDefaultFaceDescriptor() {
			setFaceDescriptor(expandDescriptor);
		}

		@Override
		protected void doExecuteCommand() {

			if (resetControl) {
				return;
			}

			collapseState = !collapseState;

			setFaceDescriptor(collapseState ? expandDescriptor : collapseDescriptor);

			mxGraph graph = documentoGraphEditor.getGraphComponent().getGraph();

			Object[] cells = graph.getChildVertices(graph.getDefaultParent());
			for (int i = 0; i < cells.length; i++) {

				Object cell = cells[i];
				if (cell instanceof mxCell && ((mxCell) cell).getValue() instanceof DocumentoNode) {
					((DocumentoNode) ((mxCell) cell).getValue()).setFullDescription(!collapseState);
				}
			}

			updateGraph();
		}

	}

	private class GraphZoomAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraphComponent graphComponent = documentoGraphEditor.getGraphComponent();

			String zoom = ((JComboBox) e.getSource()).getSelectedItem().toString();

			try {
				zoom = zoom.replace("%", "");
				double scale = Math.min(16, Math.max(0.01, Double.parseDouble(zoom) / 100));
				graphComponent.zoomTo(scale, graphComponent.isCenterZoom());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(documentoGraphEditor.getGraphComponent(), ex.getMessage());
			}

		}
	}

	private class LayoutCommand extends ApplicationWindowAwareCommand {

		private String layoutName;

		/**
		 * Costruttore.
		 * 
		 * @param paramLayoutName
		 *            nome del layout da gestire.
		 */
		public LayoutCommand(final String paramLayoutName) {
			super(paramLayoutName + "Command");
			RcpSupport.configure(this);
			this.layoutName = paramLayoutName;
		}

		@Override
		protected void doExecuteCommand() {

			mxGraph graph = documentoGraphEditor.getGraphComponent().getGraph();

			mxGraphLayout layout = GraphLayoutsFactory.getLayout(layoutName, graph);

			documentoGraphEditor.setLayout(layout);

			updateGraph();
		}

	}

	private class LoadAllNodesCommand extends JideToggleCommand {

		public static final String COMMAND_ID = "loadAllNodesCommand";

		/**
		 * Costruttore.
		 */
		public LoadAllNodesCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void onDeselection() {
			if (resetControl) {
				return;
			}

			super.onDeselection();
			documentoGraphEditor.setLoadAllNodes(false);
			documentoGraphEditor.createGraph();
		}

		@Override
		protected void onSelection() {
			if (resetControl) {
				return;
			}

			super.onSelection();
			documentoGraphEditor.setLoadAllNodes(true);
			documentoGraphEditor.createGraph();
		}
	}

	private DocumentoGraphEditor documentoGraphEditor;

	private ExpandCommand expandCommand;
	private LoadAllNodesCommand loadAllNodesCommand;

	private JComboBox zoomCombo;

	private boolean resetControl = false;

	/**
	 * Costruttore.
	 * 
	 * @param paramDocumentoGraphEditor
	 *            editor del grafico
	 * 
	 */
	public ToolbarDocumentoGraph(final DocumentoGraphEditor paramDocumentoGraphEditor) {
		super();
		this.documentoGraphEditor = paramDocumentoGraphEditor;
	}

	@Override
	protected JComponent createControl() {

		JPanel rootPanel = getComponentFactory().createPanel(new HorizontalLayout(10));

		// combobox per lo zoom
		zoomCombo = new JComboBox(new Object[] { "400%", "200%", "150%", "100%", "75%", "50%" });
		zoomCombo.setEditable(true);
		zoomCombo.setMinimumSize(new Dimension(75, 0));
		zoomCombo.setPreferredSize(new Dimension(75, 0));
		zoomCombo.setMaximumSize(new Dimension(75, 100));
		zoomCombo.setMaximumRowCount(9);
		zoomCombo.setSelectedIndex(3);
		zoomCombo.addActionListener(new GraphZoomAction());
		rootPanel.add(new JLabel("Zoom:"));
		rootPanel.add(zoomCombo);

		// show navigator label
		JLabel label = new JLabel(RcpSupport.getIcon("graphOutline.icon"));
		label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				documentoGraphEditor.getGraphOutline().setVisible(true);
			}
		});
		rootPanel.add(label);

		JECCommandGroup commandGroup = new JECCommandGroup();

		// layouts command
		for (int i = 0; i < GraphLayoutsFactory.getLayoutsName().length; i++) {
			String layoutName = GraphLayoutsFactory.getLayoutsName()[i];
			commandGroup.add(new LayoutCommand(layoutName));
		}

		// load all nodes command
		loadAllNodesCommand = new LoadAllNodesCommand();
		commandGroup.add(loadAllNodesCommand);

		// espandi / comprimi command
		expandCommand = new ExpandCommand();
		commandGroup.add(expandCommand);

		CommandBar toolbar = (CommandBar) commandGroup.createToolBar();
		rootPanel.add(toolbar);

		return rootPanel;
	}

	/**
	 * Riporta tutti i controlli allo stato iniziale.
	 */
	public void resetControl() {
		resetControl = true;

		expandCommand.applyDefaultFaceDescriptor();

		loadAllNodesCommand.setSelected(false);

		zoomCombo.setSelectedIndex(3);

		resetControl = false;
	}

	/**
	 * Aggiorna il grafico.
	 */
	private void updateGraph() {
		final mxGraph graph = documentoGraphEditor.getGraphComponent().getGraph();

		graph.getModel().beginUpdate();

		Object[] cells = graph.getChildVertices(graph.getDefaultParent());
		for (int i = 0; i < cells.length; i++) {
			graph.updateCellSize(cells[i]);
		}
		documentoGraphEditor.getLayout().execute(graph.getDefaultParent());

		mxMorphing morph = new mxMorphing(documentoGraphEditor.getGraphComponent(), 20, 1.2, 20);
		morph.addListener(mxEvent.DONE, new mxIEventListener() {
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				graph.getModel().endUpdate();
			}
		});
		morph.startAnimation();
	}

}
