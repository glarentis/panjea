package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti;

import it.eurotn.panjea.rich.editors.documentograph.GraphLayoutsFactory;
import it.eurotn.rich.command.JECCommandGroup;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.action.CommandBar;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.view.mxGraph;

public class ToolbarComponentiGraph extends AbstractControlFactory {
	private class GraphZoomAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			@SuppressWarnings("unchecked")
			String zoom = ((JComboBox<String>) e.getSource()).getSelectedItem().toString();

			try {
				zoom = zoom.replace("%", "");
				double scale = Math.min(16, Math.max(0.01, Double.parseDouble(zoom) / 100));
				graphComponent.zoomTo(scale, graphComponent.isCenterZoom());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(graphComponent, ex.getMessage());
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
			mxGraph graph = graphComponent.getGraph();

			mxGraphLayout layout = GraphLayoutsFactory.getLayout(layoutName, graph);
			graphComponent.applyLayout(layout);
		}

	}

	private ArticoloComponentiGraphComponent graphComponent;
	private ArticoloComponentiGraphOutline graphOutline;

	private JComboBox<String> zoomCombo;

	/**
	 * Costruttore.
	 *
	 * @param articoloComponentiGraphComponent
	 *            graph component
	 * @param mxGraphOutline
	 *            graphOutline
	 *
	 */
	public ToolbarComponentiGraph(final ArticoloComponentiGraphComponent articoloComponentiGraphComponent,
			final ArticoloComponentiGraphOutline mxGraphOutline) {
		super();
		this.graphComponent = articoloComponentiGraphComponent;
		this.graphOutline = mxGraphOutline;
	}

	@Override
	protected JComponent createControl() {

		JPanel rootPanel = getComponentFactory().createPanel(new HorizontalLayout(10));

		// combobox per lo zoom
		zoomCombo = new JComboBox<String>(new String[] { "400%", "200%", "150%", "100%", "75%", "50%" });
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
				// graphOutline.setVisible(true);
			}
		});
		rootPanel.add(label);

		JECCommandGroup commandGroup = new JECCommandGroup();

		// layouts command
		for (int i = 0; i < GraphLayoutsFactory.getLayoutsName().length; i++) {
			String layoutName = GraphLayoutsFactory.getLayoutsName()[i];
			commandGroup.add(new LayoutCommand(layoutName));
		}

		CommandBar toolbar = (CommandBar) commandGroup.createToolBar();
		rootPanel.add(toolbar);

		return rootPanel;
	}

}
