/**
 * 
 */
package it.eurotn.panjea.rich.commands;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.report.ReportManager;

import java.awt.Desktop;
import java.net.URI;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class OpenJasperServerUrlCommand extends ApplicationWindowAwareCommand {

	private ISicurezzaBD sicurezzaBD;

	private ReportManager reportManager;

	/**
	 * Costruttore.
	 */
	public OpenJasperServerUrlCommand() {
		super("openJasperServerUrlCommand");
		sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
		reportManager = RcpSupport.getBean("reportManager");
	}

	private String buildJasperServerURL() {

		Utente utente = sicurezzaBD.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());

		StringBuilder sb = new StringBuilder(100);
		sb.append(reportManager.getServerBaseAddress());
		sb.append("flow.html?_flowId=searchFlow&mode=library&j_username=");
		sb.append(utente.getDatiJasperServer().getUsername());
		sb.append("&j_password=");
		sb.append(utente.getDatiJasperServer().getPassword());

		return sb.toString();
	}

	@Override
	protected void doExecuteCommand() {

		String url = buildJasperServerURL();

		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop() != null) {
				// Windows
				Desktop.getDesktop().browse(new URI(url));
			} else {
				// Linux ( tento di aprire il link con firefox )
				Runtime runtime = Runtime.getRuntime();
				runtime.exec("/usr/bin/firefox -new-window " + url);
			}
		} catch (Exception e) {
			new MessageDialog("ATTENZIONE", new DefaultMessage("Sistema operativo non supportato.", Severity.INFO))
					.showDialog();
		}

	}

}
