package it.eurotn.panjea.anagrafica.rich.statusBarItem;

import it.eurotn.rich.report.ReportManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.dialog.InputApplicationDialog;

import com.jidesoft.status.ButtonStatusBarItem;

/**
 * Importa l'azienda per la sola stampa nel bean ReportManager.
 * 
 * @author giangi
 * 
 */
public class AziendaStampaStatusBarItem extends ButtonStatusBarItem implements InitializingBean {
	private static final long serialVersionUID = 4749128319641989375L;

	private ReportManager reportManager;

	/**
	 * Costruttore.
	 * 
	 */
	public AziendaStampaStatusBarItem() {
		setText("Azienda di stampa non impostata");
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new InputApplicationDialog(reportManager, "aziendaStampa").showDialog();
				AziendaStampaStatusBarItem.this.setText("Azienda per stampa:" + reportManager.getAziendaStampa());
			}

		});
		getComponent().setName("AziendaStampaButton");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(reportManager, "report manager nullo");
	}

	/**
	 * @return the reportManager
	 */
	public ReportManager getReportManager() {
		return reportManager;
	}

	/**
	 * @param reportManager
	 *            the reportManager to set
	 */
	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}
}
