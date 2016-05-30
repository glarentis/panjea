package it.eurotn.rich.report;

import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseDdt;
import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.articoli.marchice.ArticoloMarchiCEDAO;
import it.eurotn.panjea.magazzino.rich.articoli.marchice.IArticoloMarchiCEDAO;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

public class JecReportDocumentoMagazzino extends JecReportDocumento {

	protected IMagazzinoDocumentoBD magazzinoDocumentoBD;
	protected IArticoloMarchiCEDAO articoloMarchiCEDAO;

	/**
	 * Costruttore.
	 * 
	 * @param areaMagazzino
	 *            areamagazzino da stampare
	 */
	public JecReportDocumentoMagazzino(final AreaMagazzino areaMagazzino) {
		super(areaMagazzino);
		this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
		this.articoloMarchiCEDAO = RcpSupport.getBean(ArticoloMarchiCEDAO.BEAN_ID);
	}

	@Override
	public JecReport call() throws Exception {
		boolean loadMarchiCEE = marchiCEEenable()
				&& ClasseDdt.class.getName().equals(
						areaDocumento.getTipoAreaDocumento().getTipoDocumento().getClasseTipoDocumento());
		if (loadMarchiCEE) {
			List<JecReport> reportsMarchiCEE = creaReportMarchiCEE();
			for (JecReport marchioCee : reportsMarchiCEE) {
				addReport(marchioCee, true);
			}
		}
		return super.call();
	}

	/**
	 * Aggiunge al jasperPrint di riferimento le pagine conteneti i marchi CEE delle righe articolo presenti nell'area
	 * magazzino.
	 * 
	 * @return List di jasperPrint
	 */
	private List<JecReport> creaReportMarchiCEE() {
		List<? extends RigaMagazzino> righeDocumento = magazzinoDocumentoBD
				.caricaRigheMagazzinobyAreaMagazzino((AreaMagazzino) areaDocumento);

		List<JecReport> reportMarchi = new ArrayList<JecReport>();
		for (RigaMagazzino rigaMagazzino : righeDocumento) {
			if (rigaMagazzino instanceof RigaArticolo) {
				String codiceArticolo = ((RigaArticolo) rigaMagazzino).getArticolo().getCodice().toLowerCase();
				String marchioCEPath = articoloMarchiCEDAO.caricaPathMarchioCECorrente(codiceArticolo,
						(((AreaMagazzino) areaDocumento).getDataRegistrazione()));

				if (marchioCEPath != null) {
					Map<Object, Object> parametriMarchio = new HashMap<Object, Object>();
					parametriMarchio.put("pathMarchioCEE", marchioCEPath);
					JecReport reportMarchio = new JecReport("Magazzino/MarchioCEE", parametriMarchio);
					reportMarchi.add(reportMarchio);
				}
			}
		}
		return reportMarchi;
	}

	@Override
	public void execute(Closure postAction) {
		initReportName();
		super.execute(postAction);
	}

	@Override
	public void generaReport(Closure postAction) {
		parameters.put("id", areaDocumento.getId());
		initParametriMail();
		initReportPath();
		boolean loadMarchiCEE = marchiCEEenable()
				&& ClasseDdt.class.getName().equals(
						areaDocumento.getTipoAreaDocumento().getTipoDocumento().getClasseTipoDocumento());
		if (loadMarchiCEE) {
			List<JecReport> reportsMarchiCEE = creaReportMarchiCEE();
			for (JecReport marchioCee : reportsMarchiCEE) {
				addReport(marchioCee, true);
			}
		}
		super.generaReport(postAction);
	}

	/**
	 * @return <code>true</code> se la gestione marchi CE è abilitata
	 */
	private boolean marchiCEEenable() {

		Preference preference = preferenceBD.caricaPreference("marchiCE");

		Boolean gestioneMarchiCE = false;
		try {
			gestioneMarchiCE = new Boolean(preference.getValore());
		} catch (Exception e) {
			throw new RuntimeException("--> Errore durante il recupero della preference marchiCE", e);
		}
		return gestioneMarchiCE;
	}
}
