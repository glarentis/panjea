package it.eurotn.panjea.bi.service;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIResult;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.sql.detail.RigaDettaglioAnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.exception.MappaNonPresenteException;
import it.eurotn.panjea.bi.manager.interfaces.AnalisiBIManager;
import it.eurotn.panjea.bi.manager.interfaces.DashBoardManager;
import it.eurotn.panjea.bi.manager.interfaces.JasperReportExport;
import it.eurotn.panjea.bi.manager.interfaces.MappeBiManager;
import it.eurotn.panjea.bi.service.interfaces.BusinessIntelligenceService;
import it.eurotn.panjea.bi.util.Mappa;
import it.eurotn.panjea.magazzino.exception.AnalisiNonPresenteException;
import it.eurotn.panjea.magazzino.exception.AnalisiPresenteInDashBoardException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.BusinessIntelligenceService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.BusinessIntelligenceService")
public class BusinessIntelligenceServiceBean implements BusinessIntelligenceService {

	@EJB
	private AnalisiBIManager analisiBiManager;
	@EJB
	private DashBoardManager dashBoardManager;
	@EJB
	private MappeBiManager mappeBiManager;

	@EJB
	private JasperReportExport jasperReportExport;

	@Override
	public void cancellaAnalisi(int idAnalisiBI) throws AnalisiPresenteInDashBoardException,
	AnalisiNonPresenteException {
		cancellaAnalisi(idAnalisiBI, false);
	}

	@Override
	public void cancellaAnalisi(int idAnalisiBI, boolean removeFromDashboard)
			throws AnalisiPresenteInDashBoardException, AnalisiNonPresenteException {
		AnalisiBi analisiBI = new AnalisiBi();
		analisiBI.setId(idAnalisiBI);
		analisiBiManager.cancellaAnalisi(analisiBI, removeFromDashboard);
	}

	@Override
	public void cancellaDashBoard(String nomeDashBoard) {
		dashBoardManager.cancellaDashBoard(nomeDashBoard);
	}

	@Override
	public AnalisiBi caricaAnalisi(String nomeAnalisi, String categoriaAnalisi) throws AnalisiNonPresenteException {
		return analisiBiManager.caricaAnalisi(nomeAnalisi, categoriaAnalisi);
	}

	@Override
	public DashBoard caricaDashBoard(String nomeDashBoard) {
		return dashBoardManager.caricaDashBoard(nomeDashBoard);
	}

	@Override
	public Map<String, byte[]> caricaFilesMappa(String nomeFileMappa) throws MappaNonPresenteException {
		return mappeBiManager.caricaFilesMappa(nomeFileMappa);
	}

	@Override
	public List<AnalisiBi> caricaListaAnalisi() {
		return analisiBiManager.caricaListaAnalisi();
	}

	@Override
	public List<DashBoard> caricaListaDashBoard() {
		return dashBoardManager.caricaListaDashBoard();
	}

	@Override
	public List<Mappa> caricaMappe() {
		return mappeBiManager.caricaMappe();
	}

	@Override
	public Set<Object> caricaValoriPerColonna(Colonna colonna) {
		return analisiBiManager.caricaValoriPerColonna(colonna);
	}

	@Override
	public void copiaAnalisi(AnalisiBi analisi) {
		analisiBiManager.copiaAnalisi(analisi);
	}

	@Override
	public String creaJrxml(AnalisiBi analisiBi, String template) {
		return jasperReportExport.creaJrxml(analisiBi, template);
	}

	@Override
	public String creaJrxml(DashBoard dashBoard, String template) {
		return jasperReportExport.creaJrxml(dashBoard, template);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public List<RigaDettaglioAnalisiBi> drillThrough(AnalisiBi analisi, Map<Colonna, Object[]> detailFilter,
			Colonna colonnaMisura, int page, int sizeOfPage) {
		return analisiBiManager.drillThrough(analisi, detailFilter, colonnaMisura, page, sizeOfPage);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public AnalisiBIResult eseguiAnalisi(AnalisiBi analisiBi) {
		return analisiBiManager.eseguiAnalisi(analisiBi);
	}

	@Override
	public AnalisiBi salvaAnalisi(AnalisiBi analisiBI) {
		return analisiBiManager.salvaAnalisi(analisiBI);
	}

	@Override
	public DashBoard salvaDashBoard(DashBoard dashBoard) {
		return dashBoardManager.salvaDashBoard(dashBoard);
	}

}
