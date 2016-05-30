package it.eurotn.panjea.auvend.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.auvend.domain.CodiceIvaAuVend;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione;
import it.eurotn.panjea.auvend.exception.AuVendException;
import it.eurotn.panjea.auvend.exception.TipoDocumentoPerTipoOperazioneNotUniqueException;
import it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager;
import it.eurotn.panjea.auvend.service.interfaces.AuVendExtDAO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * Manager per la gestione delle classi di anagrafica( {@link LetturaFlussoAuVend}, {@link TipoDocumentoBaseAuVend} ).
 * 
 * @author adriano
 * @version 1.0, 23/dic/2008
 * 
 */
@Stateless(name = "Panjea.AnagraficaAuVendManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AnagraficaAuVendManager")
public class AnagraficaAuVendManagerBean implements AnagraficaAuVendManager {

	private static Logger logger = Logger.getLogger(AnagraficaAuVendManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private AuVendExtDAO auVendDAO;

	@Resource
	private SessionContext sessionContext;

	@Override
	public void cancellaCodiceIvaAuVend(Integer id) {
		logger.debug("--> Enter cancellaCodiceIvaAuVend");
		CodiceIvaAuVend codiceIvaAuVend = new CodiceIvaAuVend();
		codiceIvaAuVend.setId(id);
		// codiceIvaAuVend.setVersion(0);
		try {
			panjeaDAO.delete(codiceIvaAuVend);
		} catch (VincoloException e) {
			logger.error("--> errore VincoloException in cancellaCodiceIvaAuVend", e);
			throw new RuntimeException(e);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in cancellaCodiceIvaAuVend", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaCodiceIvaAuVend");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#cancellaLetturaFlussoAuVend(it.eurotn.panjea
	 * .auvend.domain.LetturaFlussoAuVend)
	 */
	@Override
	public void cancellaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend) {
		logger.debug("--> Enter cancellaLetturaFlussoAuVend");
		try {
			panjeaDAO.delete(letturaFlussoAuVend);
		} catch (VincoloException e) {
			logger.error("--> errore VincoloException in cancellaLetturaFlussoAuVend", e);
			throw new RuntimeException(e);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in cancellaLetturaFlussoAuVend", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaLetturaFlussoAuVend");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#cancellaTipoDocumentoBaseAuVend(it.eurotn.
	 * panjea.auvend.domain.TipoDocumentoBaseAuVend)
	 */
	@Override
	public void cancellaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend) {
		logger.debug("--> Enter cancellaTipoDocumentoBaseAuVend");
		try {
			panjeaDAO.delete(tipoDocumentoBaseAuVend);
		} catch (VincoloException e) {
			logger.error("--> errore VincoloException in cancellaTipoDocumentoBaseAuVend", e);
			throw new RuntimeException(e);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in cancellaTipoDocumentoBaseAuVend", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaTipoDocumentoBaseAuVend");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#caricaCaricatori()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Deposito> caricaCaricatori() throws AuVendException {
		logger.debug("--> Enter caricaCaricatori");
		List<Deposito> caricatori = null;
		StringBuffer hql = new StringBuffer(
				"select dep from Deposito dep where dep.sedeDeposito.azienda.codice = :paramCodiceAzienda and dep.principale = false");
		Query query = panjeaDAO.prepareQuery(hql.toString());
		query.setParameter("paramCodiceAzienda", getCodiceAzienda());
		try {
			caricatori = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaCaricatori", e);
			throw new AuVendException(e);
		}
		logger.debug("--> Exit caricaCaricatori");
		return caricatori;
	}

	@Override
	public List<String> caricaCausaliNonAssociateAuvend() {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("* ");
		sb.append("from CausaliMagazzino ");
		sb.append("left join OPENQUERY ");
		sb.append("( ");
		sb.append("   PANJEA,'select causaleAuvend from avend_tipi_documento_base' ");
		sb.append(") ");
		sb.append("as tdb on tdb.causaleAuvend=Causale ");
		sb.append("where tdb.causaleAuvend is null ");
		sb.append("and Causale<>'.'  order by causale ");
		ResultSet resultSet = auVendDAO.executeQuery(sb.toString());
		List<String> causali = new ArrayList<String>();
		try {
			while (resultSet.next()) {
				causali.add(resultSet.getString(1));
			}
		} catch (SQLException e) {
			logger.error("-->errore nel caricare le causali ", e);
		} finally {
			auVendDAO.cleanUp();
		}
		return causali;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#caricaCodiceIvaAuVend(java.lang.Integer)
	 */
	@Override
	public CodiceIvaAuVend caricaCodiceIvaAuVend(Integer id) throws AuVendException {
		logger.debug("--> Enter caricaCodiceIvaAuVend");
		CodiceIvaAuVend codiceIvaAuVend;
		try {
			codiceIvaAuVend = panjeaDAO.load(CodiceIvaAuVend.class, id);
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore ObjectNotFoundException in caricaCodiceIvaAuVend", e);
			throw new AuVendException(e);
		}
		logger.debug("--> Exit caricaCodiceIvaAuVend");
		return codiceIvaAuVend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#caricaCodiciIvaAuVend()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CodiceIvaAuVend> caricaCodiciIvaAuVend() throws AuVendException {
		logger.debug("--> Enter caricaCodiciIvaAuVend");
		List<CodiceIvaAuVend> list = new ArrayList<CodiceIvaAuVend>();
		Query query = panjeaDAO.prepareNamedQuery("CodiceIvaAuVend.caricaAll");
		query.setParameter("paramCodiceAzienda", getCodiceAzienda());
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaCodiciIvaAuVend", e);
			throw new AuVendException(e);
		}
		logger.debug("--> Exit caricaCodiciIvaAuVend");
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#caricaLetturaFlussoAuVend(it.eurotn.panjea
	 * .auvend.domain.LetturaFlussoAuVend)
	 */
	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend)
			throws AuVendException {
		logger.debug("--> Enter caricaLetturaFlussoAuVend");
		LetturaFlussoAuVend letturaFlussoAuVendLoad;
		try {
			letturaFlussoAuVendLoad = panjeaDAO.load(LetturaFlussoAuVend.class, letturaFlussoAuVend.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore ObjectNotFoundException in caricaLetturaFlussoAuVend", e);
			throw new AuVendException(e);
		}
		logger.debug("--> Exit caricaLetturaFlussoAuVend");
		return letturaFlussoAuVendLoad;
	}

	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoAuVend(String codiceDeposito) throws AuVendException {
		List<LetturaFlussoAuVend> letture = caricaLettureFlussoAuVend();
		for (LetturaFlussoAuVend letturaFlussoAuVend : letture) {
			if (letturaFlussoAuVend.getDeposito().getCodice().equals(codiceDeposito)) {
				return letturaFlussoAuVend;
			}
		}
		throw new AuVendException("Lettura per il deposito non trovata. Codice deposito: " + codiceDeposito);
	}

	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoFattuazioneRifornimenti() throws AuVendException {
		LetturaFlussoAuVend letturaFlussoAuVend = getLetturaFlussoSenzaDeposito();
		if (letturaFlussoAuVend == null) {
			letturaFlussoAuVend = new LetturaFlussoAuVend();
			letturaFlussoAuVend.setUltimaLetturaFlussoFatturazioneRifornimenti(Calendar.getInstance().getTime());
			letturaFlussoAuVend.setDeposito(null);
		}
		return letturaFlussoAuVend;
	}

	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoMovimenti() throws AuVendException {
		LetturaFlussoAuVend letturaFlussoAuVend = getLetturaFlussoSenzaDeposito();
		if (letturaFlussoAuVend == null) {
			letturaFlussoAuVend = new LetturaFlussoAuVend();
			letturaFlussoAuVend.setUltimaLetturaFlussoMovimenti(Calendar.getInstance().getTime());
			letturaFlussoAuVend.setDeposito(null);
		}
		return letturaFlussoAuVend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#caricaLettureFlussoAuVend()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LetturaFlussoAuVend> caricaLettureFlussoAuVend() throws AuVendException {
		logger.debug("--> Enter caricaLettureFlussoAuVend");
		Query query = panjeaDAO.prepareNamedQuery("LetturaFlussoAuVend.caricaByAzienda");
		query.setParameter("paramCodiceAzienda", getCodiceAzienda());
		List<LetturaFlussoAuVend> lettureFlussoAuVend = new ArrayList<LetturaFlussoAuVend>();
		try {
			lettureFlussoAuVend = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaLettureFlussoAuVend", e);
			throw new AuVendException(e);
		}
		logger.debug("--> Exit caricaLettureFlussoAuVend");
		return lettureFlussoAuVend;
	}

	/**
	 * 
	 * @return .
	 * @throws AuVendException .
	 */
	public Map<Deposito, LetturaFlussoAuVend> caricaLettureFlussoAuVendInMap() throws AuVendException {
		logger.debug("--> Enter caricaLettureFlussoAuVendInMap");
		List<LetturaFlussoAuVend> depositi = caricaLettureFlussoAuVend();
		Map<Deposito, LetturaFlussoAuVend> depositiMap = new HashMap<Deposito, LetturaFlussoAuVend>();
		for (LetturaFlussoAuVend letturaFlussoAuVend : depositi) {
			depositiMap.put(letturaFlussoAuVend.getDeposito(), letturaFlussoAuVend);
		}
		logger.debug("--> Exit caricaLettureFlussoAuVendInMap");
		return depositiMap;
	}

	@Override
	public Map<Deposito, LetturaFlussoAuVend> caricaLettureFlussoCarichi() throws AuVendException {
		logger.debug("--> Enter caricaLettureFlussoCarichi");

		Map<Deposito, LetturaFlussoAuVend> mapLettureCarichi = new HashMap<Deposito, LetturaFlussoAuVend>();
		Map<Deposito, LetturaFlussoAuVend> mapLetture = caricaLettureFlussoAuVendInMap();

		for (Entry<Deposito, LetturaFlussoAuVend> entry : mapLetture.entrySet()) {
			if (entry.getValue().getUltimaLetturaFlussoCarichi() != null) {
				mapLettureCarichi.put(entry.getKey(), entry.getValue());
			}
		}

		logger.debug("--> Exit caricaLettureFlussoCarichi");
		return mapLettureCarichi;
	}

	@Override
	public LetturaFlussoAuVend caricaLettureFlussoFatturazioneRifornimenti() throws AuVendException {
		Map<Deposito, LetturaFlussoAuVend> mapLetture = caricaLettureFlussoAuVendInMap();
		return mapLetture.get(null);
	}

	@Override
	public Map<Deposito, LetturaFlussoAuVend> caricaLettureFlussoFatture() throws AuVendException {
		logger.debug("--> Enter caricaLettureFlussoFatture");

		Map<Deposito, LetturaFlussoAuVend> mapLettureFatture = new HashMap<Deposito, LetturaFlussoAuVend>();
		Map<Deposito, LetturaFlussoAuVend> mapLetture = caricaLettureFlussoAuVendInMap();

		for (Entry<Deposito, LetturaFlussoAuVend> entry : mapLetture.entrySet()) {
			if (entry.getValue().getUltimaLetturaFlussoFatture() != null) {
				mapLettureFatture.put(entry.getKey(), entry.getValue());
			}
		}

		logger.debug("--> Exit caricaLettureFlussoFatture");
		return mapLettureFatture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#caricaTipiDocumentoBaseAuVend()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDocumentoBaseAuVend> caricaTipiDocumentoBaseAuVend() throws AuVendException {
		logger.debug("--> Enter caricaTipiDocumentoBaseAuVend");
		Query query = panjeaDAO.prepareNamedQuery("TipoDocumentoBaseAuVend.caricaByAzienda");
		query.setParameter("paramCodiceAzienda", getCodiceAzienda());
		List<TipoDocumentoBaseAuVend> tipiDocumentoBaseAuVend = new ArrayList<TipoDocumentoBaseAuVend>();
		try {
			tipiDocumentoBaseAuVend = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaTipiDocumentoBaseAuVend", e);
			throw new AuVendException(e);
		}
		logger.debug("--> Exit caricaTipiDocumentoBaseAuVend");
		return tipiDocumentoBaseAuVend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#caricaTipiDocumentoBaseAuVendPerTipoOperazione
	 * (it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDocumentoBaseAuVend> caricaTipiDocumentoBaseAuVendPerTipoOperazione(TipoOperazione tipoOperazione)
			throws AuVendException {
		logger.debug("--> Enter caricaTipiDocumentoBaseAuVendPerTipoOperazione");
		List<TipoDocumentoBaseAuVend> tipiDocumentoBaseAuVend = null;
		Query query = panjeaDAO.prepareNamedQuery("TipoDocumentoBaseAuVend.caricaByTipoOperazione");
		query.setParameter("paramCodiceAzienda", getCodiceAzienda());
		query.setParameter("paramTipoOperazione", tipoOperazione);
		try {
			tipiDocumentoBaseAuVend = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaTipiDocumentoBaseAuVendPerTipoOperazione", e);
			throw new AuVendException(e);
		}
		logger.debug("--> Exit caricaTipiDocumentoBaseAuVendPerTipoOperazione");
		return tipiDocumentoBaseAuVend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#caricaTipoDocumentoBaseAuVend(it.eurotn.panjea
	 * .auvend.domain.TipoDocumentoBaseAuVend)
	 */
	@Override
	public TipoDocumentoBaseAuVend caricaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend)
			throws AuVendException {
		logger.debug("--> Enter caricaTipoDocumentoBaseAuVend");
		TipoDocumentoBaseAuVend tipoDocumentoBaseAuVendLoad;
		try {
			tipoDocumentoBaseAuVendLoad = panjeaDAO
					.load(TipoDocumentoBaseAuVend.class, tipoDocumentoBaseAuVend.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore ObjectNotFoundException in caricaTipoDocumentoBaseAuVend", e);
			throw new AuVendException(e);
		}
		logger.debug("--> Exit caricaTipoDocumentoBaseAuVend");
		return tipoDocumentoBaseAuVendLoad;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#caricaTipoDocumentoBaseAuVendPerTipoOperazione
	 * (it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione)
	 */
	@Override
	public TipoDocumentoBaseAuVend caricaTipoDocumentoBaseAuVendPerTipoOperazione(TipoOperazione tipoOperazione) {
		logger.debug("--> Enter caricaTipoDocumentoBaseAuVendPerTipoOperazione");
		TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend = null;
		Query query = panjeaDAO.prepareNamedQuery("TipoDocumentoBaseAuVend.caricaByTipoOperazione");
		query.setParameter("paramCodiceAzienda", getCodiceAzienda());
		query.setParameter("paramTipoOperazione", tipoOperazione);
		try {
			tipoDocumentoBaseAuVend = (TipoDocumentoBaseAuVend) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.warn("--> tipo documento base assente per tipo operazione ");
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaTipoDocumentoBaseAuVendPerTipoOperazione", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipoDocumentoBaseAuVendPerTipoOperazione");
		return tipoDocumentoBaseAuVend;
	}

	/**
	 * 
	 * @return codice azienda dell'utente loggato
	 */
	private String getCodiceAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

	/**
	 * 
	 * @return la lettura flusso non associata ad alcun deposito, se esiste. Altrimenti null.
	 */
	private LetturaFlussoAuVend getLetturaFlussoSenzaDeposito() {
		Query query = panjeaDAO
				.prepareQuery("select lettura from LetturaFlussoAuVend lettura where lettura.deposito is null");
		LetturaFlussoAuVend letturaFlussoAuVend = null;
		try {
			letturaFlussoAuVend = (LetturaFlussoAuVend) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			letturaFlussoAuVend = new LetturaFlussoAuVend();
			letturaFlussoAuVend.setUltimaLetturaFlussoFatturazioneRifornimenti(Calendar.getInstance().getTime());
			letturaFlussoAuVend.setUltimaLetturaFlussoMovimenti(Calendar.getInstance().getTime());
			letturaFlussoAuVend = salvaLetturaFlussoAuVend(letturaFlussoAuVend);
		} catch (DAOException e) {
			logger.error("-->errore nel leggere la lettura flusso per i movimenti di magazzino", e);
			throw new RuntimeException(e);
		}

		return letturaFlussoAuVend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#salvaCodiceIvaAuVend(it.eurotn.panjea.auvend
	 * .domain.CodiceIvaAuVend)
	 */
	@Override
	public CodiceIvaAuVend salvaCodiceIvaAuVend(CodiceIvaAuVend codiceIvaAuVend) {
		logger.debug("--> Enter salvaCodiceIvaAuVend");
		CodiceIvaAuVend codiceIvaAuVendSave;
		try {
			codiceIvaAuVendSave = panjeaDAO.save(codiceIvaAuVend);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaCodiceIvaAuVend");
		return codiceIvaAuVendSave;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#salvaLetturaFlussoAuVend(it.eurotn.panjea.
	 * auvend.domain.LetturaFlussoAuVend)
	 */
	@Override
	public LetturaFlussoAuVend salvaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend) {
		logger.debug("--> Enter salvaLetturaFlussoAuVend");
		LetturaFlussoAuVend letturaFlussoAuVendSave;
		try {
			letturaFlussoAuVendSave = panjeaDAO.save(letturaFlussoAuVend);
		} catch (Exception e) {
			logger.error("--> errore DAOException in salvaLetturaFlussoAuVend", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaLetturaFlussoAuVend");
		return letturaFlussoAuVendSave;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager#salvaTipoDocumentoBaseAuVend(it.eurotn.panjea
	 * .auvend.domain.TipoDocumentoBaseAuVend)
	 */
	@Override
	public TipoDocumentoBaseAuVend salvaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend) {
		logger.debug("--> Enter salvaTipoDocumentoBaseAuVend");
		TipoDocumentoBaseAuVend tipoDocumentoBaseAuVendSave;
		try {
			verificaIntegritaTipoDocumentoBaseAuVend(tipoDocumentoBaseAuVend);
		} catch (TipoDocumentoPerTipoOperazioneNotUniqueException e) {
			logger.error("--> errore TipoDocumentoPerTipoOperazioneNotUniqueException in salvaTipoDocumentoBaseAuVend",
					e);
			throw new RuntimeException(e);
		}
		try {
			tipoDocumentoBaseAuVend.setCodiceAzienda(getCodiceAzienda());
			tipoDocumentoBaseAuVendSave = panjeaDAO.save(tipoDocumentoBaseAuVend);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in salvaTipoDocumentoBaseAuVend", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaTipoDocumentoBaseAuVend");
		return tipoDocumentoBaseAuVendSave;
	}

	/**
	 * 
	 * @param tipoDocumentoBaseAuVendToSave
	 *            .
	 * @throws TipoDocumentoPerTipoOperazioneNotUniqueException .
	 */
	private void verificaIntegritaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVendToSave)
			throws TipoDocumentoPerTipoOperazioneNotUniqueException {
		logger.debug("--> Enter verificaIntegritaTipoDocumentoBaseAuVend");

		switch (tipoDocumentoBaseAuVendToSave.getTipoOperazione()) {
		case RECUPERO_FATTURA:
		case RECUPERO_GENERICO:
			// in caso ri recupero fattura o generico posso configurare più tipi documento quindi non controllo
			// l'univocità
			return;
		default:
			// ricerca la presenza di altri tipi documento con lo stesso tipo
			try {
				List<TipoDocumentoBaseAuVend> tipiDocumentoBase = caricaTipiDocumentoBaseAuVendPerTipoOperazione(tipoDocumentoBaseAuVendToSave
						.getTipoOperazione());
				if (tipiDocumentoBase.size() < 1) {
					return;
				}
				if (tipiDocumentoBase.size() > 1) {
					throw new TipoDocumentoPerTipoOperazioneNotUniqueException(
							"tipo documento per tipo operazione non unico ", tipiDocumentoBase.get(0));
				}
				if (!tipiDocumentoBase.get(0).getId().equals(tipoDocumentoBaseAuVendToSave.getId())) {
					throw new TipoDocumentoPerTipoOperazioneNotUniqueException(
							"tipo documento per tipo operazione non unico ", tipiDocumentoBase.get(0));
				}
			} catch (AuVendException e) {
				logger.error("--> errore AuVendException in verificaIntegritaTipoDocumentoBaseAuVend", e);
				throw new TipoDocumentoPerTipoOperazioneNotUniqueException(e, null);
			}
		}

		logger.debug("--> Exit verificaIntegritaTipoDocumentoBaseAuVend");
	}

	/**
	 * 
	 * @return Lettura Flusso Riparazione conto terzi
	 * @throws AuVendException .
	 */
	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoRiparazioneContoTerzi()
			throws AuVendException {
		LetturaFlussoAuVend letturaFlussoAuVend = getLetturaFlussoSenzaDeposito();
		if (letturaFlussoAuVend == null) {
			letturaFlussoAuVend = new LetturaFlussoAuVend();
			letturaFlussoAuVend.setUltimaLetturaFlussoRiparazioneContoTerzi(Calendar.getInstance().getTime());
			letturaFlussoAuVend.setDeposito(null);
		}
		return letturaFlussoAuVend;
	}

}
