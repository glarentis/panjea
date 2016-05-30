package it.eurotn.panjea.rich.editors.documentograph.toolbar.cell;

import it.eurotn.panjea.rich.editors.documentograph.toolbar.AbstractToolbarGraphCommand;
import it.eurotn.rich.command.JECCommandGroup;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.factory.AbstractControlFactory;

import com.jidesoft.action.CommandBar;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;

public class ToolbarDocumentoCellGraph extends AbstractControlFactory {

	private class DefaultToolbarCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public void postExecution(ActionCommand command) {
			ToolbarDocumentoCellGraph.this.getControl().setVisible(false);

			final mxGraph graph = graphComponent.getGraph();

			if (((AbstractToolbarGraphCommand) command).isRelayoutGraph()) {

				graph.getModel().beginUpdate();

				Object[] cells = graph.getChildVertices(graph.getDefaultParent());
				for (int i = 0; i < cells.length; i++) {
					graph.updateCellSize(cells[i]);
				}
				graphLayout.execute(graph.getDefaultParent());

				mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);
				morph.addListener(mxEvent.DONE, new mxIEventListener() {
					@Override
					public void invoke(Object sender, mxEventObject evt) {
						graph.getModel().endUpdate();
					}
				});
				morph.startAnimation();
			}
		}
	}

	private List<AbstractToolbarGraphCommand> commands;

	private mxGraphComponent graphComponent;
	private mxGraphLayout graphLayout;

	private DefaultToolbarCommandInterceptor commandInterceptor;

	/**
	 * Costruttore.
	 * 
	 */
	public ToolbarDocumentoCellGraph() {
		super();
	}

	@Override
	protected JComponent createControl() {

		commandInterceptor = new DefaultToolbarCommandInterceptor();

		commands = new ArrayList<AbstractToolbarGraphCommand>();
		commands.add(new ExpandCommand());
		commands.add(new StepIntoDocumentCommand());
		commands.add(new OpenDocumentCommand());

		JECCommandGroup commandGroup = new JECCommandGroup();
		for (AbstractToolbarGraphCommand command : commands) {
			commandGroup.add(command);
			command.addCommandInterceptor(commandInterceptor);

		}

		CommandBar toolbar = (CommandBar) commandGroup.createToolBar();
		toolbar.setOrientation(SwingConstants.VERTICAL);
		return toolbar;
	}

	/**
	 * Esegue il dispose della toolbar.
	 */
	public void dispose() {
		for (AbstractToolbarGraphCommand command : commands) {
			command.removeCommandInterceptor(commandInterceptor);
			command = null;
		}
		commandInterceptor = null;
	}

	/**
	 * Aggiorna i controlli in base alla cella di riferimento.
	 * 
	 * @param paramCell
	 *            cella di riferimento
	 * @param paramGraphComponent
	 *            graph component
	 * @param paramGraphLayout
	 *            layout utilizzato
	 */
	public void update(mxCell paramCell, mxGraphComponent paramGraphComponent, mxGraphLayout paramGraphLayout) {
		this.graphComponent = paramGraphComponent;
		this.graphLayout = paramGraphLayout;

		for (AbstractToolbarGraphCommand command : commands) {
			command.update(paramCell);
		}
	}

}
