package it.eurotn.panjea.rich.editors.update.step;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.springframework.richclient.util.RcpSupport;

public class PointStepPanel extends AbstractStepPanel {

	public enum PointType {
		CLIENT, SERVER_CLIENT, SERVER_EUROPA
	}

	private static final long serialVersionUID = -4210341432122361874L;
	public static final String POINT_CLIENT_ICON = "aggiornamento.client";
	public static final String POINT_SERVER_CLIENT_ICON = "aggiornamento.servercliente";

	public static final String POINT_SERVER_EUROPA_ICON = "aggiornamento.servereuropa";

	private Icon pointIcon;
	private JLabel pointLabel;

	/**
	 * Costruttore.
	 * 
	 * @param managedSteps
	 *            step gestiti dal pannello
	 * @param pointType
	 *            tipo
	 * 
	 */
	public PointStepPanel(final StepUpdate[] managedSteps, final PointType pointType) {
		super(managedSteps);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(160, 110));

		switch (pointType) {
		case CLIENT:
			pointIcon = RcpSupport.getIcon(POINT_CLIENT_ICON);
			break;
		case SERVER_CLIENT:
			pointIcon = RcpSupport.getIcon(POINT_SERVER_CLIENT_ICON);
			break;
		case SERVER_EUROPA:
			pointIcon = RcpSupport.getIcon(POINT_SERVER_EUROPA_ICON);
			break;
		default:
			pointIcon = RcpSupport.getIcon(POINT_SERVER_EUROPA_ICON);
			break;
		}

		pointLabel = new JLabel(pointIcon);
		pointLabel.setEnabled(false);
		add(pointLabel, BorderLayout.NORTH);
	}

	@Override
	public void update(StepUpdate step) {

		boolean containStep = getManagedSteps().contains(step);

		pointLabel.setEnabled(containStep);

	}

}
