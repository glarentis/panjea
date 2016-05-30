package it.eurotn.panjea.rich.editors.update.step;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;

import java.awt.BorderLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.richclient.util.RcpSupport;

public class ArrowStepPanel extends AbstractStepPanel {

	private static Logger logger = Logger.getLogger(ArrowStepPanel.class);

	public static final String FRECCIA_SX_ICON = "aggiornamento.freccia.sx";
	public static final String FRECCIA_DX_ICON = "aggiornamento.freccia.dx";

	private static final long MEGABYTE = 1024L * 1024L;
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.0");

	private static final long serialVersionUID = -8007858737656564461L;

	private Icon frecciaIcon;
	private JLabel frecciaLabel;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param managedSteps
	 *            step gestiti dal pannello
	 * @param orientation
	 *            orientamento della freccia nel pannello. See {@link SwingConstants}.
	 */
	public ArrowStepPanel(final StepUpdate[] managedSteps, final int orientation) {
		super(managedSteps);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

		switch (orientation) {
		case SwingConstants.LEFT:
			frecciaIcon = RcpSupport.getIcon(FRECCIA_SX_ICON);
			frecciaLabel = new JLabel(frecciaIcon);
			add(frecciaLabel, BorderLayout.SOUTH);
			break;
		case SwingConstants.RIGHT:
			frecciaIcon = RcpSupport.getIcon(FRECCIA_DX_ICON);
			frecciaLabel = new JLabel(frecciaIcon);
			add(frecciaLabel, BorderLayout.NORTH);
			break;
		default:
			logger.error("-->orientamento freccia non gestito.");
			break;
		}

		frecciaLabel.setEnabled(false);
		frecciaLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		setVisible(false);

	}

	@Override
	public void update(StepUpdate step) {

		boolean containStep = getManagedSteps().contains(step);

		setVisible(containStep);
		frecciaLabel.setEnabled(containStep);

		if (!containStep) {
			frecciaLabel.setText("");
		}
	}

	/**
	 * Visualizza all'interno della freccia i byte passati come parametro.
	 * 
	 * @param bytes
	 *            byte da visualizzare
	 */
	public void updateByte(Long bytes) {

		if (frecciaLabel.isEnabled()) {
			final long mb = bytes / MEGABYTE;

			if (!SwingUtilities.isEventDispatchThread()) {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {

						@Override
						public void run() {
							frecciaLabel.setText(DECIMAL_FORMAT.format(mb) + " Mb");
						}
					});
				} catch (Exception e) {
					logger.error("-->errore durante l'aggiornamento dei byte ricevuti.", e);
					throw new PanjeaRuntimeException(e);
				}
			} else {
				frecciaLabel.setText(DECIMAL_FORMAT.format(mb) + " Mb");
			}
		}
	}
}
