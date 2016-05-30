/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.ritenuteacconto.certificazioni;

import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.report.JecReport;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.form.Form;

/**
 *
 * @author adriano
 * @version 1.0, 04/dic/06
 */
public class CertificazioniCompensiDialogPage extends PanjeaTitledPageApplicationDialog {

	private static Logger logger = Logger.getLogger(CertificazioniCompensiDialogPage.class);
	private Form form = null;

	/**
	 * Costruttore.
	 *
	 */
	public CertificazioniCompensiDialogPage() {
		super(new ReportCertificazioniCompensiForm(), null);
		org.springframework.util.Assert.isInstanceOf(FormBackedDialogPage.class, getDialogPage());
		FormBackedDialogPage dialogPage = (FormBackedDialogPage) getDialogPage();
		form = dialogPage.getBackingFormPage();
	}

	@Override
	protected boolean onFinish() {
		logger.debug("--> Enter onFinish");

		form.commit();
		ParametriCreazioneCertificazioniCompensi parametriCreazione = (ParametriCreazioneCertificazioniCompensi) form
				.getFormObject();

		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("anno", parametriCreazione.getAnno());
		if (parametriCreazione.getEntita() != null) {
			params.put("idEntita", parametriCreazione.getEntita().getId());
		}
		params.put("dataCertificazione",
				new SimpleDateFormat("dd/MM/yyyy").format(parametriCreazione.getDataCertificazione()));

		JecReport jecReport = new JecReport("Contabilita/certificazioneRitenuteAcconto", params);
		jecReport.setReportName("Certificazione ritenute d'acconto");
		jecReport.execute();

		logger.debug("--> Exit onFinish");
		return true;
	}

}
