package it.eurotn.panjea.magazzino.manager.documento;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.PreFatturazioneManager;
import it.eurotn.panjea.magazzino.util.MovimentoPreFatturazioneDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import commonj.work.Work;
import commonj.work.WorkItem;

import de.myfoo.commonj.util.ThreadPool;
import de.myfoo.commonj.work.FooWorkManager;

@Stateless(mappedName = "Panjea.PreFatturazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PreFatturazioneManager")
public class PreFatturazioneManagerBean implements PreFatturazioneManager {

	public class AreeCollegateWork implements Work {

		private List<AreaMagazzinoLite> areaCollegate = new ArrayList<AreaMagazzinoLite>();
		private AreaMagazzinoManager areaMagazzinoManager;

		private MovimentoPreFatturazioneDTO movimento;

		/**
		 *
		 * @param areaMagazzinoManager
		 *            manager per il caricamento delle aree collegate
		 * @param movimento
		 *            movimento di cui caricare i documenti collegati
		 */
		public AreeCollegateWork(final AreaMagazzinoManager areaMagazzinoManager,
				final MovimentoPreFatturazioneDTO movimento) {
			this.areaMagazzinoManager = areaMagazzinoManager;
			this.movimento = movimento;
		}

		/**
		 * @return the areaCollegate
		 */
		public List<AreaMagazzinoLite> getAreaCollegate() {
			return areaCollegate;
		}

		/**
		 * @return the movimento
		 */
		public MovimentoPreFatturazioneDTO getMovimento() {
			return movimento;
		}

		@Override
		public boolean isDaemon() {
			return false;
		}

		@Override
		public void release() {
			areaCollegate = null;
		}

		@Override
		public void run() {
			List<AreaMagazzino> aree = new ArrayList<AreaMagazzino>();
			aree.add(movimento.getAreaMagazzino());

			areaCollegate = areaMagazzinoManager.caricaAreeMagazzinoCollegate(aree);
		}

	}

	private static Logger logger = Logger.getLogger(PreFatturazioneManagerBean.class);

	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private AreaMagazzinoManager areaMagazzinoManager;

	private List<MovimentoPreFatturazioneDTO> caricaDocumentiCollegati(List<MovimentoPreFatturazioneDTO> movimenti) {

		// create the thread pool for this work manager
		ThreadPool pool = new ThreadPool(10, 10, 1000);

		// create the work manager
		FooWorkManager workManager = new FooWorkManager(pool, 0);

		List<WorkItem> movimentiWorkItem = new ArrayList<WorkItem>();

		for (MovimentoPreFatturazioneDTO mov : movimenti) {
			try {
				movimentiWorkItem.add(workManager.schedule(new AreeCollegateWork(areaMagazzinoManager, mov)));
			} catch (Exception e) {
				logger.error("--> errore nel lanciare il task per il caricamento dei documenti collegati all'AM "
						+ mov.getAreaMagazzino().getId(), e);
				throw new RuntimeException(
						"errore nel lanciare il task per il caricamento dei documenti collegati all'AM "
								+ mov.getAreaMagazzino().getId(), e);
			}
		}

		try {
			workManager.waitForAll(movimentiWorkItem, 120000);
			for (WorkItem workItem : movimentiWorkItem) {
				AreeCollegateWork areeCollegateWork = (AreeCollegateWork) workItem.getResult();
				// aggiorno le aree collegate del movimento
				int movIdx = movimenti.indexOf(areeCollegateWork.getMovimento());
				if (movIdx != -1) {
					movimenti.get(movIdx).setAreeCollegate(areeCollegateWork.getAreaCollegate());
				}
			}
		} catch (Exception e) {
			logger.error("-->errore nell'aspettare i processi per la valorizzazione dei depositi", e);
			throw new RuntimeException("-->errore nell'aspettare i processi per la valorizzazione dei depositi ", e);
		} finally {
			workManager.shutdown();
		}

		return movimenti;
	}

	@SuppressWarnings("unchecked")
	private List<MovimentoPreFatturazioneDTO> caricaMovimenti(String utente) {
		StringBuffer hqlQuery = new StringBuffer(1000);
		hqlQuery.append("select am.id as idAreaMagazzino, ");
		hqlQuery.append("am.version as versionAreaMagazzino, ");
		hqlQuery.append("partite.id as idAreaRate, ");
		hqlQuery.append("partite.codicePagamento.id as idCodicePagamento, ");
		hqlQuery.append("partite.codicePagamento.codicePagamento as pagamentoCodice, ");
		hqlQuery.append("partite.codicePagamento.descrizione as pagamentoDescrizione, ");
		hqlQuery.append("(select min(sp.tipoPagamento) from CodicePagamento cp join cp.strutturePartita sp where cp.id=partite.codicePagamento.id) as tipoPagamento, ");
		hqlQuery.append("am.tipoAreaMagazzino.id as idTipoAreaMagazzino, ");
		hqlQuery.append("tipoDocumento.id as idTipoDocumento, ");
		hqlQuery.append("doc.entita.id as entitaId, ");
		hqlQuery.append("doc.entita.version as entitaVersion, ");
		hqlQuery.append("doc.entita.codice as entitaCodice, ");
		hqlQuery.append("doc.entita.anagrafica.denominazione as entitaDenominazione, ");
		hqlQuery.append("doc.entita.anagrafica.sedeAnagrafica.indirizzoMail as indirizzoMail, ");
		hqlQuery.append("doc.entita.anagrafica.sedeAnagrafica.indirizzoPEC as indirizzoPec, ");
		hqlQuery.append("tipoDocumento.descrizione as tipoDocumentoDescrizione, ");
		hqlQuery.append("tipoDocumento.codice as tipoDocumentoCodice, ");
		hqlQuery.append("doc.codice as numeroDocumento, ");
		hqlQuery.append("doc.dataDocumento as dataDocumento, ");
		hqlQuery.append("doc.id as idDocumento, ");
		hqlQuery.append("doc.totale as totaleDocumento, ");
		hqlQuery.append("sede.id as idSede, ");
		hqlQuery.append("sede.sede.descrizione as descrizioneSede, ");
		hqlQuery.append("sede.sede.indirizzo as indirizzoSede, ");
		hqlQuery.append("(select zona from ZonaGeografica zona where zona.id=am.idZonaGeografica) as zonaGeografica, ");
		hqlQuery.append("agente.id as idAgente, ");
		hqlQuery.append("agente.codice as codiceAgente, ");
		hqlQuery.append("agenteAnagrafica.denominazione as agenteDenominazione ");
		hqlQuery.append("from AreaMagazzino am join am.documento doc join doc.tipoDocumento tipoDocumento ");
		hqlQuery.append("join doc.sedeEntita sede left join sede.agente agente left join agente.anagrafica agenteAnagrafica , AreaPartite partite ");
		hqlQuery.append("where partite.documento = doc and doc.codiceAzienda = :paramCodiceAzienda ");
		hqlQuery.append(" and am.datiGenerazione.utente = :paramUtente ");
		hqlQuery.append(" and am.statoAreaMagazzino = 3 ");
		hqlQuery.append("order by am.documento.codice.codiceOrder ");

		Query query = panjeaDAO.prepareQuery(hqlQuery.toString(), MovimentoPreFatturazioneDTO.class, null);
		query.setParameter("paramCodiceAzienda", getCodiceAzienda());
		query.setParameter("paramUtente", utente);

		List<MovimentoPreFatturazioneDTO> listMovimenti = new ArrayList<MovimentoPreFatturazioneDTO>();

		try {
			listMovimenti = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dei movimenti in fatturazione.", e);
			throw new RuntimeException("Errore durante il caricamento dei movimenti in fatturazione.", e);
		}

		logger.debug("--> Exit caricaMovimentiInFatturazione");
		return listMovimenti;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<MovimentoPreFatturazioneDTO> caricaMovimentPreFatturazione(String utente) {
		logger.debug("--> Enter caricaMovimentiInFatturazione");

		// carico i documenti presenti per l'utente specificato
		List<MovimentoPreFatturazioneDTO> listMovimenti = caricaMovimenti(utente);

		// carico i documenti collegati
		listMovimenti = caricaDocumentiCollegati(listMovimenti);

		return listMovimenti;
	}

	/**
	 * @return codice azienda loggata
	 */
	private String getCodiceAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}
}
