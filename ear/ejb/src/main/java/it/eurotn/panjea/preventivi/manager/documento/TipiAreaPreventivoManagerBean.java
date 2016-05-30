package it.eurotn.panjea.preventivi.manager.documento;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.TipiAreaPreventivoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.stampe.manager.interfaces.LayoutStampeManager;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.TipiAreaPreventivoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipiAreaPreventivoManager")
public class TipiAreaPreventivoManagerBean implements TipiAreaPreventivoManager {
	private static Logger logger = Logger.getLogger(TipiAreaPreventivoManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext context;

	@EJB
	protected TipiAreaPartitaManager tipiAreaPartita;

	@EJB
	private LayoutStampeManager layoutStampeManager;

	@Override
	public void cancellaTipoAreaPreventivo(TipoAreaPreventivo tipoAreaPreventivo) {
		logger.debug("--> Enter cancellaTipoAreaPreventivo con id " + tipoAreaPreventivo.getId());
		try {
			TipoDocumento tipoDocumento = tipoAreaPreventivo.getTipoDocumento();
			TipoAreaPartita tipoAreaPartita = tipiAreaPartita.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumento);
			if (tipoAreaPartita.getId() != null) {
				tipiAreaPartita.cancellaTipoAreaPartita(tipoAreaPartita);
			}

			layoutStampeManager.cancellaLayoutStampa(tipoAreaPreventivo);

			panjeaDAO.delete(tipoAreaPreventivo);

		} catch (Exception e) {
			logger.error("--> errore nella cancellazione ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaTipoAreaPreventivo");

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoAreaPreventivo> caricaTipiAreaPreventivo(String fieldSearch, String valueSearch,
			boolean loadTipiDocumentoDisabilitati) {
		logger.debug("--> Enter caricaTipiAreaPreventivo");

		StringBuilder sb = new StringBuilder();
		sb.append("select tao from TipoAreaPreventivo tao ");
		sb.append("inner join tao.tipoDocumento td ");
		sb.append("where td.codiceAzienda = :paramCodiceAzienda ");
		if (!loadTipiDocumentoDisabilitati) {
			sb.append("and td.abilitato = true ");
		}
		if (valueSearch != null) {
			sb.append("and tao.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		if (fieldSearch != null) {
			sb.append(" order by ").append("tao.").append(fieldSearch);
		}

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setHint("org.hibernate.cacheable", "true");
		query.setHint("org.hibernate.cacheRegion", "tipoAreaPreventivo");
		query.setParameter("paramCodiceAzienda", getAzienda());
		List<TipoAreaPreventivo> tipiAreaPreventivo;
		try {
			tipiAreaPreventivo = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore impossibile recuperare la list di TipoAreaPreventivo ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipiAreaOrdine");
		return tipiAreaPreventivo;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDocumento> caricaTipiDocumentiPreventivo() {
		logger.debug("--> Enter caricaTipiDocumentiPreventivo");
		Query query = panjeaDAO.prepareNamedQuery("TipoAreaPreventivo.caricaTipiDocumentiPreventivo");
		query.setParameter("paramCodiceAzienda", getAzienda());
		List<TipoDocumento> tipiDocumento = null;
		try {
			tipiDocumento = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore in ricerca tipi area preventivo", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipiDocumentiPreventivo con n° documenti" + tipiDocumento.size());

		return tipiDocumento;

	}

	@Override
	public TipoAreaPreventivo caricaTipoAreaPreventivo(Integer id) {
		logger.debug("--> Enter caricaTipoAreaPreventivo con id " + id);
		try {
			TipoAreaPreventivo result = panjeaDAO.load(TipoAreaPreventivo.class, id);
			logger.debug("--> Exit caricaTipoAreaContabile");
			return result;
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore caricaTipoAreaPreventivo", e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public TipoAreaPreventivo caricaTipoAreaPreventivoPerTipoDocumento(Integer idTipoDocumento) {
		Query query = panjeaDAO.prepareNamedQuery("TipoAreaPreventivo.caricaByTipoDocumento");
		query.setParameter("paramId", idTipoDocumento);
		TipoAreaPreventivo result = null;
		try {
			result = (TipoAreaPreventivo) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> TipoAreaOrdine non trovato");
			result = new TipoAreaPreventivo();
			logger.debug("--> Restituisco tipoAreaOrdine " + result);
		} catch (DAOException e) {
			logger.error("--> errore DAOException ", e);
			throw new RuntimeException(
					"Impossibile restituire TipoAreaOrdine con tipoDocumento con id =" + idTipoDocumento, e);
		}
		return result;
	}

	/**
	 *
	 * @return azienda loggata.
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();

	}

	/**
	 *
	 * @param tipoDocumento
	 *            tipo documento
	 * @throws ModificaTipoAreaConDocumentoException
	 *             eccezione
	 */
	private void salvaTipoAreaPartita(TipoDocumento tipoDocumento) throws ModificaTipoAreaConDocumentoException {
		TipoAreaPartita tipoAreaPartita = tipiAreaPartita.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumento);
		boolean modicheDaSalvare = false;

		if (tipoAreaPartita.getId() == null) {
			tipoAreaPartita.setTipoDocumento(tipoDocumento);
			modicheDaSalvare = true;
		}

		TipoPartita tipoPartita = TipoPartita.ATTIVA;
		if (tipoDocumento.getTipoEntita() != TipoEntita.CLIENTE) {
			tipoPartita = TipoPartita.PASSIVA;
		}

		if (tipoPartita != tipoAreaPartita.getTipoPartita()) {
			tipoAreaPartita.setTipoPartita(tipoPartita);
			modicheDaSalvare = true;
		}

		if (modicheDaSalvare) {
			tipiAreaPartita.salvaTipoAreaPartita(tipoAreaPartita);
		}
	}

	@Override
	public TipoAreaPreventivo salvaTipoAreaPreventivo(TipoAreaPreventivo tipoAreaDaSalvare) {
		logger.debug("--> Enter salvaTipoAreaPreventivo");
		TipoAreaPreventivo tipoAreaSalvata = null;
		try {
			tipoAreaSalvata = panjeaDAO.save(tipoAreaDaSalvare);
			salvaTipoAreaPartita(tipoAreaSalvata.getTipoDocumento());

		} catch (Exception e) {
			logger.error("--> errore nel salvare il TipoAreaPreventivo", e);
			throw new RuntimeException("errore nel salvare il TipoAreaPreventivo", e);
		}
		logger.debug("--> Exit salvaTipoAreaPreventivo");
		return tipoAreaSalvata;
	}

}
