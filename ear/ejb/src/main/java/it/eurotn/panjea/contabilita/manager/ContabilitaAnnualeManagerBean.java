package it.eurotn.panjea.contabilita.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.TipiDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AperturaContabileProcessor;
import it.eurotn.panjea.contabilita.manager.interfaces.BilancioManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ChiusuraContabileProcessor;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaAnnualeManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.AperturaEsistenteException;
import it.eurotn.panjea.contabilita.service.exception.ChiusuraAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ChiusuraEsistenteException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.DocumentiNonStampatiGiornaleException;
import it.eurotn.panjea.contabilita.service.exception.GiornaliNonValidiException;
import it.eurotn.panjea.contabilita.util.SaldoConti;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.ContabilitaAnnualeManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ContabilitaAnnualeManager")
public class ContabilitaAnnualeManagerBean implements ContabilitaAnnualeManager {

	private static Logger logger = Logger.getLogger(ContabilitaAnnualeManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private TipiAreaContabileManager tipiAreaContabileManager;

	@IgnoreDependency
	@EJB(beanName = "Panjea.ChiusuraContabileOrdineProcessor")
	private ChiusuraContabileProcessor chiusuraContabileOrdineProcessor;

	@IgnoreDependency
	@EJB(beanName = "Panjea.ChisuraContabilePatrimonialeProcessor")
	private ChiusuraContabileProcessor chiusuraContabilePatrimonialeProcessor;

	@IgnoreDependency
	@EJB(beanName = "Panjea.ChiusuraContabileEconomicaProcessor")
	private ChiusuraContabileProcessor chiusuraContabileEconomicaProcessor;

	@IgnoreDependency
	@EJB(beanName = "Panjea.AperturaContabileOrdineProcessor")
	private AperturaContabileProcessor aperturaContabileOrdineProcessor;

	@IgnoreDependency
	@EJB(beanName = "Panjea.AperturaContabilePatrimonialeProcessor")
	private AperturaContabileProcessor aperturaContabilePatrimonialeProcessor;

	@IgnoreDependency
	@EJB
	private BilancioManager bilancioManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaContabile> caricaAreeContabiliAperturaChiusura(Integer annoContabile, TipoConto tipoConto)
			throws TipoDocumentoBaseException, ContabilitaException {
		// Carico i documenti base per apertura/chiusura
		logger.debug("--> Enter caricaAreeContabiliAperturaChiusura");
		List<TipoAreaContabile> tipiDocumentoAperturaChiusura = new ArrayList<TipoAreaContabile>();
		try {
			TipiDocumentoBase tipiDocumentoBase = tipiAreaContabileManager.caricaTipiOperazione();
			tipiDocumentoAperturaChiusura = tipiDocumentoBase.getTipiDocumentiAperturaChisuraPerTipoConto(tipoConto);
		} catch (ContabilitaException e) {
			logger.error("--> errore nel caricare i tipiContiBase", e);
			throw new RuntimeException(e);
		}

		// Recupero i movimenti di apertura/chiusura
		Query query = panjeaDAO.prepareNamedQuery("AreaContabile.ricercaByTipiAreaContabileAnnoContabile");
		query.setParameter("paramTipiAreaContabile", tipiDocumentoAperturaChiusura);
		query.setParameter("paramAnno", annoContabile);
		List<AreaContabile> areeContabiliAperturaChiusura;
		try {
			areeContabiliAperturaChiusura = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile recuperare il movimento di apertura ", e);
			throw new ContabilitaException("impossibile recuperare il movimento di apertura", e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> Aree di apertura chiusura trovate " + areeContabiliAperturaChiusura);
			logger.debug("--> Exit caricaAreeContabiliAperturaChiusura");
		}
		return areeContabiliAperturaChiusura;
	}

	@Override
	public void eseguiAperturaConti(Integer annoEsercizio, Date dataDocumentoChiusura) throws ContiBaseException,
			AperturaEsistenteException, ChiusuraAssenteException, TipoDocumentoBaseException {

		aperturaContabileOrdineProcessor.verificaTipiContiApertura();
		aperturaContabilePatrimonialeProcessor.verificaTipiContiApertura();

		aperturaContabileOrdineProcessor.verificaDocumentiAperturaPresenti(annoEsercizio);
		aperturaContabilePatrimonialeProcessor.verificaDocumentiAperturaPresenti(annoEsercizio);

		aperturaContabileOrdineProcessor.verificaTipoDocumentoBaseApertura();
		aperturaContabilePatrimonialeProcessor.verificaTipoDocumentoBaseApertura();

		aperturaContabilePatrimonialeProcessor.eseguiApertura(annoEsercizio, dataDocumentoChiusura);
		aperturaContabileOrdineProcessor.eseguiApertura(annoEsercizio, dataDocumentoChiusura);
	}

	@Override
	public void eseguiChiusuraConti(Integer annoEsercizio, Date dataDocumentoChiusura)
			throws ChiusuraEsistenteException, DocumentiNonStampatiGiornaleException, ContiBaseException,
			TipoDocumentoBaseException, GiornaliNonValidiException {
		// verifico le condizioni
		chiusuraContabileEconomicaProcessor.verificaDocumentiChisuraPresenti(annoEsercizio);
		chiusuraContabilePatrimonialeProcessor.verificaDocumentiChisuraPresenti(annoEsercizio);
		chiusuraContabileOrdineProcessor.verificaDocumentiChisuraPresenti(annoEsercizio);

		// Verifico solamente su un processor perchè le verificaStampaGiornaleMovimenti verificano che TUTTI i movimenti
		// siano stampati.
		// Quindi con una verifica sono sicuro. Viene implementata in tutte le classi che implementano la abstract per
		// sicurezza.
		// Se venissero chiamate da sole cmq la verifica verrebbe fatta.
		chiusuraContabileEconomicaProcessor.verificaStampaGiornaleMovimenti(annoEsercizio);

		chiusuraContabileEconomicaProcessor.verificaTipiContiChiusura();
		chiusuraContabilePatrimonialeProcessor.verificaTipiContiChiusura();
		chiusuraContabileOrdineProcessor.verificaTipiContiChiusura();

		chiusuraContabileEconomicaProcessor.verificaTipoDocumentoBaseChiusura();
		chiusuraContabilePatrimonialeProcessor.verificaTipoDocumentoBaseChiusura();
		chiusuraContabileOrdineProcessor.verificaTipoDocumentoBaseChiusura();

		// Carico il bilancio
		try {
			SaldoConti bilancio = bilancioManager.caricaBilancioAnnuale(annoEsercizio);
			chiusuraContabileEconomicaProcessor.eseguiChiusura(annoEsercizio, bilancio, dataDocumentoChiusura);
			chiusuraContabilePatrimonialeProcessor.eseguiChiusura(annoEsercizio, bilancio, dataDocumentoChiusura);
			chiusuraContabileOrdineProcessor.eseguiChiusura(annoEsercizio, bilancio, dataDocumentoChiusura);
		} catch (ContabilitaException e) {
			logger.error("--> errore nel calcolare il bilancio", e);
			throw new RuntimeException(e);
		}
	}
}
