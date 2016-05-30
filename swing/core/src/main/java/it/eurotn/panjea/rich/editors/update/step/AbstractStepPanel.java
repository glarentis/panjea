package it.eurotn.panjea.rich.editors.update.step;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

public abstract class AbstractStepPanel extends JPanel {

	private static final long serialVersionUID = 1682344179953884078L;

	private List<StepUpdate> managedSteps;

	/**
	 * Costruttore.
	 * 
	 * @param managedSteps
	 *            step gestiti dal pannello
	 * 
	 */
	public AbstractStepPanel(final StepUpdate[] managedSteps) {
		super(new BorderLayout());
		this.managedSteps = Arrays.asList(managedSteps);
	}

	/**
	 * @return the managedSteps
	 */
	public List<StepUpdate> getManagedSteps() {
		return managedSteps;
	}

	/**
	 * Aggiorna il pannello in base allo step di riferimento.
	 * 
	 * @param step
	 *            step di riferimento
	 */
	public abstract void update(StepUpdate step);

}
