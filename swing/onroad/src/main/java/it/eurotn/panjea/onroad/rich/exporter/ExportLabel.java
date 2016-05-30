package it.eurotn.panjea.onroad.rich.exporter;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.util.RcpSupport;

public class ExportLabel extends JPanel {

	private static final long serialVersionUID = 6936318346194054711L;

	private JCheckBox exportCheckBox;
	private JLabel exportLabel;

	private static Icon iconDaImportare = RcpSupport.getIcon("daImportare");
	private static Icon iconInCorso = RcpSupport.getIcon("importazioneInCorso");
	private static Icon iconImportato = RcpSupport.getIcon("importato");

	/**
	 * @param keyMessage
	 *            keyMessage
	 */
	public ExportLabel(final String keyMessage) {
		super(new BorderLayout(5, 0));

		exportCheckBox = new JCheckBox();
		exportCheckBox.setSelected(true);
		add(exportCheckBox, BorderLayout.WEST);

		exportLabel = new JLabel(RcpSupport.getMessage(keyMessage));
		exportLabel.setIcon(iconDaImportare);
		add(exportLabel, BorderLayout.CENTER);
	}

	/**
	 *
	 */
	public void changeToImportato() {
		exportLabel.setIcon(iconImportato);
	}

	/**
	 *
	 */
	public void changeToInCorso() {
		exportLabel.setIcon(iconInCorso);
	}

	/**
	 * @return <code>true</code> se selezionato
	 */
	public boolean isChecked() {
		return exportCheckBox.isSelected();
	}

}
