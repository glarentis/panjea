package it.eurotn.panjea.rich.editors.update;

import it.eurotn.panjea.anagrafica.rich.statusBarItem.AggiornamentoCompletatoDialog;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.jfree.ui.tabbedui.VerticalLayout;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.StyledLabel;
import com.jidesoft.swing.StyledLabelBuilder;

public class ServicesStatusPanel extends JPanel {

	private static Logger logger = Logger.getLogger(ServicesStatusPanel.class);

	private static final long serialVersionUID = 4325746386432634201L;

	private StyledLabel versionLabel;
	private StyledLabel buildIDLabel;
	private JLabel serverLabel;
	private JLabel panjeaLabel;
	private JLabel databaseLabel;

	private String currentState = "started";

	private Map<String, Icon> statesIcon;

	{
		statesIcon = new HashMap<String, Icon>();
		statesIcon.put("started",
				RcpSupport.getIcon("aggiornamento.stato.partito"));
		statesIcon.put("created",
				RcpSupport.getIcon("aggiornamento.stato.inpartenza"));
		statesIcon.put("not started",
				RcpSupport.getIcon("aggiornamento.stato.nonpartito"));
		statesIcon.put("non partito",
				RcpSupport.getIcon("aggiornamento.stato.nonpartito"));
	}

	/**
	 * Costruttore.
	 *
	 */
	public ServicesStatusPanel() {
		super(new VerticalLayout());

		versionLabel = StyledLabelBuilder
				.createStyledLabel("{Versione attuale :bold}");
		buildIDLabel = StyledLabelBuilder.createStyledLabel("{Build ID :bold}");
		serverLabel = new JLabel();
		panjeaLabel = new JLabel();
		panjeaLabel.setName("StatoPanjea");
		databaseLabel = new JLabel();

		add(versionLabel);
		add(buildIDLabel);
		add(StyledLabelBuilder.createStyledLabel("{Stato dei servizi:bold}"));
		add(new JLabel());
		add(serverLabel);
		add(panjeaLabel);
		add(databaseLabel);

		currentState = "started";
	}

	/**
	 * Aggiorna lo stato dei servizi in base allo statusText.
	 *
	 * @param statusText
	 *            stato del server sotto forma di stringa html
	 */
	public void update(final String statusText) {
		if (statusText != null) {

			if (!SwingUtilities.isEventDispatchThread()) {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {

						@Override
						public void run() {
							updateStatus(statusText);
						}
					});
				} catch (Exception e) {
					logger.error("-->errore nel cambiare lo stato ", e);
					throw new PanjeaRuntimeException(e);
				}
			} else {
				updateStatus(statusText);
			}
		}
	}

	/**
	 * @param statusText
	 *            la string che descrive lo stato di panjeaserver
	 */
	private void updateStatus(String statusText) {
		if (statusText.startsWith("<li>")) {
			updateStatus2(statusText);
			return;
		}

		if (statusText.indexOf("HTML") != -1) {

			String newState = "";
			// rimuovo intestazione html, title, body e table
			statusText = statusText.replace(
					"<HTML><TITLE>STATO DEL SERVER</TITLE><BODY><TABLE>", "");

			// rimuovo tr,/td e table, body e html di chiusura
			statusText = statusText.replaceAll("<TR>", "");
			statusText = statusText.replaceAll("</TR>", "");
			statusText = statusText.replace("</TABLE></BODY></HTML>", "");

			String[] status = statusText.split("</TD>");

			serverLabel.setText(status[0].replace("<TD>", ""));
			serverLabel.setIcon(statesIcon.get(status[1].replace("<TD>", "")
					.toLowerCase()));

			if (status.length > 2
					&& !status[2].replace("<TD>", "").equals("......")) {
				newState = status[3].replace("<TD>", "").toLowerCase();
				panjeaLabel.setText(status[2].replace("<TD>", ""));
				panjeaLabel.setIcon(statesIcon.get(status[3]
						.replace("<TD>", "").toLowerCase()));
				((ImageIcon) panjeaLabel.getIcon()).setDescription(status[3]
						.replace("<TD>", "").toLowerCase());

				if (status.length > 4
						&& !status[4].replace("<TD>", "").equals("......")) {
					databaseLabel.setText(status[4].replace("<TD>", ""));
					databaseLabel.setIcon(statesIcon.get(status[5].replace(
							"<TD>", "").toLowerCase()));
				} else {
					databaseLabel.setIcon(statesIcon.get("not started"));
				}
			} else {
				newState = "not started";
				panjeaLabel.setIcon(statesIcon.get("not started"));
				((ImageIcon) panjeaLabel.getIcon()).setDescription("");
				databaseLabel.setIcon(statesIcon.get("not started"));
			}

			if (!currentState.equals("started") && newState.equals("started")) {
				// Notifico la fine dell'aggiornamento
				MessageDialog dialog = new AggiornamentoCompletatoDialog();
				dialog.showDialog();
			}

			currentState = newState;

		} else {
			serverLabel.setText(statusText);
			serverLabel.setIcon(RcpSupport.getIcon("severity.error"));
		}
	}

	/**
	 * Aggiorna lo stato del server con la nuova versione del server
	 *
	 * @param statusText
	 */
	private void updateStatus2(String statusText) {
		serverLabel.setText("<HTML>" + statusText + "</HTML>");
	}

	/**
	 * Aggiorna la versione e build id in base al properties.
	 *
	 * @param properties
	 *            {@link Properties}
	 */
	public void updateVersion(Properties properties) {
		String version = "nd.";
		String buildID = "nd.";

		if (properties != null) {
			version = properties.getProperty("versione");
			buildID = properties.getProperty("buildid");
		}

		StyledLabelBuilder.setStyledText(versionLabel,
				"{Versione attuale:bold}\\: " + version);
		StyledLabelBuilder.setStyledText(buildIDLabel, "{Build ID:bold}\\: "
				+ buildID);
	}
}
