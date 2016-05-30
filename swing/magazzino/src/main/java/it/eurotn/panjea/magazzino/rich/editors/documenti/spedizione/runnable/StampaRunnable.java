package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.runnable;

import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.panjea.rich.stampe.ILayoutStampeManager;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager.TipoLayoutPrefefinito;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.report.JecReport;
import it.eurotn.rich.report.JecReportDocumento;
import it.eurotn.rich.report.JecReports;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.util.RcpSupport;

public class StampaRunnable extends SpedizioneRunnable {

	protected ILayoutStampeManager layoutStampeManager;

	/**
	 *
	 * Costruttore.
	 *
	 * @param movimento
	 *            movimento
	 * @param tableWidget
	 *            tabella dei movimenti
	 * @param layoutInterno
	 *            indica se deve essere preferito il layout interno per la stampa
	 */
	public StampaRunnable(final MovimentoSpedizioneDTO movimento,
			final JideTableWidget<MovimentoSpedizioneDTO> tableWidget, final boolean layoutInterno) {
		super(movimento, tableWidget, layoutInterno);

		layoutStampeManager = RcpSupport.getBean(LayoutStampeManager.BEAN_ID);
	}

	@Override
	protected EsitoSpedizione doRun() {
		List<JecReport> reportsDocumenti = new ArrayList<JecReport>();
		JecReport reportDocumento = new JecReportDocumento(getMovimento().getAreaDocumento(),
				layoutInterno ? TipoLayoutPrefefinito.INTERNO : TipoLayoutPrefefinito.PREDEFINITO);
		reportsDocumenti.add(reportDocumento);

		boolean spedizioneOk = false;
		StringBuilder sbEsito = new StringBuilder(500);
		try {
			JecReports.stampaReports(reportsDocumenti, true, false, true);

			sbEsito.append("Stampa generata correttamente");
			spedizioneOk = true;
		} catch (Exception e) {
			sbEsito.append("Errore durante la stampa:\n " + StringUtils.defaultString(e.getMessage()));
		}

		return new EsitoSpedizione(spedizioneOk, sbEsito.toString());
	}
}
