package it.eurotn.panjea.protocolli.service;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.protocolli.domain.ProtocolloAnno;
import it.eurotn.panjea.protocolli.domain.ProtocolloValore;
import it.eurotn.panjea.protocolli.service.exception.ProtocolliException;
import it.eurotn.panjea.protocolli.service.interfaces.ProtocolliService;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.util.SqlExecuter;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.ProtocolliService")
@SecurityDomain("PanjeaLoginModule")
@RemoteBinding(jndiBinding = "Panjea.ProtocolliService")
@PermitAll
public class ProtocolliServiceBean implements ProtocolliService {

	private static Logger logger = Logger.getLogger(ProtocolliServiceBean.class);
	@EJB
	protected PanjeaDAO panjeaDAO;
	@Resource
	protected SessionContext context;
	@EJB
	protected AziendeManager aziendeManager;

	@Override
	public void cancellaProtocollo(Protocollo protocollo) throws ProtocolliException {
		logger.debug("--> Enter cancellaProtocollo");
		try {
			panjeaDAO.delete(protocollo);
		} catch (VincoloException e) {
			logger.error("--> errore VincoloException in cancellazione protocollo", e);
			throw new ProtocolliException(e);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in cancellazione protocollo", e);
			throw new ProtocolliException(e);
		}
		logger.debug("--> Exit cancellaProtocollo");

	}

	@Override
	@SuppressWarnings("unchecked")
	public void cancellaProtocolloAnno(ProtocolloAnno protocolloAnno) throws ProtocolliException {
		logger.debug("--> Enter cancellaProtocolloAnno");
		Protocollo protocollo = protocolloAnno.getProtocollo();
		try {
			panjeaDAO.delete(protocolloAnno);
		} catch (VincoloException e) {
			logger.error("--> errore VincoloEception in cancellaProtocolloAnno", e);
			throw new ProtocolliException(e);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in cancellaProtocolloAnno", e);
			throw new ProtocolliException(e);
		}
		/*
		 * verifica l'esistenza di ProtocolloAnno per il ProtocolloAnnuale corrente, nell'eventualit� che non ne
		 * esistano si provedde alla sua cancellazione
		 */
		Query query = panjeaDAO.prepareNamedQuery("ProtocolloAnno.caricaByProtocollo");
		query.setParameter("paramId", protocollo.getId());
		List<ProtocolloAnno> protocolliAnno = query.getResultList();
		if (protocolliAnno.size() == 0) {
			try {
				panjeaDAO.delete(protocollo);
			} catch (VincoloException e) {
				logger.error("--> errore VincoloException in cancellazione ProtocolloAnnuale", e);
				throw new RuntimeException(e);
			} catch (DAOException e) {
				logger.error("--> errore DAOException in cancellazione ProtocolloAnnuale ", e);
				throw new RuntimeException(e);
			}
		}
		logger.debug("--> Exit cancellaProtocolloAnno");
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Protocollo> caricaProtocolli(String filter) {
		logger.debug("--> Enter caricaProtocolli");
		JecPrincipal jecPrincipal = getPrincipal();
		List<Protocollo> protocolli;
		String namedQuery = "Protocollo.caricaAll";
		if (filter != null) {
			namedQuery = "Protocollo.caricaByCodice";
		}
		Query query = panjeaDAO.prepareNamedQuery(namedQuery);
		query.setParameter("paramCodiceAzienda", jecPrincipal.getCodiceAzienda());
		if (filter != null) {
			query.setParameter("paramCodice", filter);
		}
		protocolli = query.getResultList();
		logger.debug("--> Exit caricaProtocolli find# " + protocolli.size());
		return protocolli;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProtocolloAnno> caricaProtocolliAnno(Integer anno, String filter) {
		logger.debug("--> Enter caricaProtocolliAnno");
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		StringBuffer queryString = new StringBuffer("select pa from ProtocolloAnno pa inner join pa.protocollo p ");
		queryString.append("where p.codiceAzienda = :paramCodiceAzienda ");
		if ((filter != null) && (!"".equals(filter))) {
			queryString.append(" and p.codice=:paramCodice ");
		}
		if (anno != null) {
			queryString.append(" and pa.anno=:paramAnno ");
		}
		Query query = panjeaDAO.prepareQuery(queryString.toString());
		query.setParameter("paramCodiceAzienda", jecPrincipal.getCodiceAzienda());
		if (anno != null) {
			query.setParameter("paramAnno", anno);
		}
		if ((filter != null) && (!"".equals(filter))) {
			query.setParameter("paramCodice", filter);
		}
		List<ProtocolloAnno> list = query.getResultList();
		logger.debug("--> Exit caricaProtocolliAnno find# " + list.size());
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProtocolloValore> caricaProtocolliValore(String filter) {
		logger.debug("--> Enter caricaProtocolliValore");
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		List<ProtocolloValore> protocolli;
		String namedQuery = "ProtocolloValore.caricaAll";
		if (filter != null) {
			namedQuery = "ProtocolloValore.caricaByCodice";
		}
		Query query = panjeaDAO.prepareNamedQuery(namedQuery);
		query.setParameter("paramCodiceAzienda", jecPrincipal.getCodiceAzienda());
		if (filter != null) {
			query.setParameter("paramCodice", filter);
		}
		protocolli = query.getResultList();
		logger.debug("--> Exit caricaProtocolliValore find# " + protocolli.size());
		return protocolli;
	}

	@Override
	public Protocollo caricaProtocollo(Integer id) throws ProtocolliException {
		logger.debug("--> Enter caricaProtocollo");
		Protocollo protocollo;
		try {
			protocollo = panjeaDAO.load(Protocollo.class, id);
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore ObjectNotFoundException in caricaProtocollo", e);
			throw new ProtocolliException(e);
		}
		logger.debug("--> Enter caricaProtocolloValore");
		return protocollo;
	}

	@Override
	public ProtocolloAnno caricaProtocolloAnno(Integer id) throws ProtocolliException {
		logger.debug("--> Enter caricaProtocolloAnno");
		ProtocolloAnno protocolloAnno;
		try {
			protocolloAnno = panjeaDAO.load(ProtocolloAnno.class, id);
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore ObjectNotFoundException in caricaProtocolloAnno", e);
			throw new ProtocolliException(e);
		}
		logger.debug("--> Exit caricaProtocolloAnno");
		return protocolloAnno;
	}

	@Override
	public void creaProtocolliPerAnno(int anno) {
		logger.debug("--> Enter creaProtocolliPerAnno");

		StringBuilder sb = new StringBuilder();
		sb.append("insert into code_protocolli_anno(id,version,userInsert,anno,valore,protocollo_id,timeStamp) ");
		sb.append("select null,0,");
		sb.append(PanjeaEJBUtil.addQuote(getPrincipal().getUserName()));
		sb.append(",");
		sb.append(anno);
		sb.append(",0,p.protocollo_id,UNIX_TIMESTAMP()*1000 ");
		sb.append("from code_protocolli_anno p left join code_protocolli_anno pesistenti on p.protocollo_id = pesistenti.protocollo_id and pesistenti.anno = ");
		sb.append(anno);
		sb.append(" where p.anno = (select distinct pa.anno from code_protocolli_anno pa where pa.anno < ");
		sb.append(anno);
		sb.append(" order by pa.anno desc limit 1) and pesistenti.id is null ");
		SqlExecuter executer = new SqlExecuter();
		executer.setSql(sb.toString());
		((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);

		logger.debug("--> Exit creaProtocolliPerAnno");
	}

	/**
	 * @return {@link JecPrincipal}
	 */
	private JecPrincipal getPrincipal() {
		return (JecPrincipal) this.context.getCallerPrincipal();
	}

	@Override
	public ProtocolloValore salvaProtocollo(ProtocolloValore protocollo) throws ProtocolliException {
		logger.debug("--> Enter salvaProtocollo");
		ProtocolloValore protocollo2;
		if (protocollo.isNew()) {
			protocollo.setCodiceAzienda(getPrincipal().getCodiceAzienda());
		}
		try {
			protocollo2 = panjeaDAO.save(protocollo);
		} catch (DAOException e) {
			throw new ProtocolliException(e);
		}
		logger.debug("--> Exit salvaProtocollo");
		return protocollo2;
	}

	@Override
	public ProtocolloAnno salvaProtocolloAnno(ProtocolloAnno protocolloAnno) throws ProtocolliException {
		logger.debug("--> Enter salvaProtocolloAnno");
		ProtocolloAnno protocolloAnno2;
		if (protocolloAnno.getProtocollo().isNew()) {
			protocolloAnno.getProtocollo().setCodiceAzienda(getPrincipal().getCodiceAzienda());
		}

		try {
			protocolloAnno2 = panjeaDAO.save(protocolloAnno);
		} catch (DAOException e) {
			throw new ProtocolliException(e);
		}
		logger.debug("--> Exit salvaProtocolloAnno");
		return protocolloAnno2;
	}
}
