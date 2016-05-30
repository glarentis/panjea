package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.evasione;

import it.eurotn.panjea.contabilita.util.AbstractStateDescriptor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.springframework.richclient.core.TitleConfigurable;
import org.springframework.richclient.util.RcpSupport;

public class EvasioneStepPanel extends JPanel implements TitleConfigurable {

	private static final long serialVersionUID = 4318181397975018161L;

	private Icon playIcon = null;
	private Icon finishIcon = null;
	private static final String PLAY_ICON = "evasioneStepPanel.playIcon";
	private static final String FINISH_ICON = "evasioneStepPanel.finishIcon";

	private boolean enableProgressBar = false;

	private JLabel labelPanel = null;
	private JProgressBar progressBar = null;

	private boolean maxValueSet = false;

	/**
	 * Costruttore.
	 * 
	 * @param idStepPanel
	 *            id del componente
	 * @param enableProgressBar
	 *            se <code>true</code> verrà visualizzata la progress bar
	 */
	public EvasioneStepPanel(final String idStepPanel, final boolean enableProgressBar) {
		super(new BorderLayout(10, 10));
		// this.setBorder(new BottomBorder(Color.BLACK));

		this.enableProgressBar = enableProgressBar;

		playIcon = RcpSupport.getIcon(PLAY_ICON);
		finishIcon = RcpSupport.getIcon(FINISH_ICON);

		createControl();

		RcpSupport.configure(this, idStepPanel);
	}

	/**
	 * Crea i controlli.
	 */
	private void createControl() {

		labelPanel = new JLabel(playIcon, SwingConstants.LEFT);
		labelPanel.setPreferredSize(new Dimension(200, 30));
		this.add(labelPanel, BorderLayout.WEST);

		if (enableProgressBar) {
			progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
			progressBar.setStringPainted(true);
			// progressBar.setPreferredSize(new Dimension(600, 50));
			this.add(progressBar, BorderLayout.CENTER);
		}
	}

	/**
	 * Aggiorna lo stato del panel in base allo state descriptor.
	 * 
	 * @param stateDescriptor
	 *            {@link AbstractStateDescriptor}
	 */
	public void progress(final AbstractStateDescriptor stateDescriptor) {

		Runnable task = new Runnable() {
			@Override
			public void run() {
				if (stateDescriptor.getCurrentJobOperation() == stateDescriptor.getTotalJobOperation()) {
					labelPanel.setIcon(finishIcon);
				}

				if (enableProgressBar) {
					if (!maxValueSet) {
						progressBar.setMaximum(stateDescriptor.getTotalJobOperation());
						maxValueSet = true;
					}
					progressBar.setValue(stateDescriptor.getCurrentJobOperation());
					progressBar.setString(stateDescriptor.getCurrentJobOperation() + " / "
							+ stateDescriptor.getTotalJobOperation());
				}
			}
		};
		if (SwingUtilities.isEventDispatchThread()) {
			task.run();
		} else {
			SwingUtilities.invokeLater(task);
		}
	}

	/**
	 * Resetta tutti i valori dello step panel.
	 */
	public void reset() {
		labelPanel.setIcon(playIcon);

		if (enableProgressBar) {
			progressBar.setValue(0);
			progressBar.setMaximum(0);
			progressBar.setString("0 / 0");

			maxValueSet = false;
		}
	}

	@Override
	public void setTitle(String text) {
		labelPanel.setText(text);
	}
}
