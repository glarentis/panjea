package it.eurotn.panjea.magazzino.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.manager.contratti.sqlbuilder.RigheContrattoCalcoloQueryBuilder;
import it.eurotn.panjea.magazzino.manager.contratti.stampa.sqlbuilder.RigheContrattoStampaQueryBuilder;
import it.eurotn.panjea.magazzino.manager.interfaces.ContrattiManager;
import it.eurotn.panjea.magazzino.service.exception.SedeMagazzinoAssenteException;
import it.eurotn.panjea.magazzino.util.ContrattoProspettoDTO;
import it.eurotn.panjea.magazzino.util.ContrattoStampaDTO;
import it.eurotn.panjea.magazzino.util.ParametriRicercaContratti;
import it.eurotn.panjea.magazzino.util.RigaContrattoCalcolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaStampaContratti;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Implementazione del SessionBean di {@link ContrattiManager} .
 *
 * @author adriano
 * @version 1.0, 17/giu/08
 */
@Stateless(name = "Panjea.ContrattiManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.ContrattiManager")
public class ContrattiManagerBean implements ContrattiManager {

	private static Logger logger = Logger.getLogger(ContrattiManagerBean.class);

	@Resource
	private SessionContext sessionContext;

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public Contratto associaCategoriaSedeContratto(Contratto contratto, CategoriaSedeMagazzino categoriaSedeMagazzino) {
		logger.debug("--> Enter associaCategoriaSedeContratto");

		contratto.getCategorieSediMagazzino().add(categoriaSedeMagazzino);
		Contratto contrattoSalvato = salvaContratto(contratto, false);
		logger.debug("--> Exit associaCategoriaSedeContratto");
		return contrattoSalvato;
	}

	@Override
	public Contratto associaEntitaContratto(Contratto contratto, EntitaLite entitaLite) {
		logger.debug("--> Enter associaEntitaContratto");

		contratto = panjeaDAO.getEntityManager().merge(contratto);

		// controllo che l'entità non sia già associata
		for (EntitaLite entita : contratto.getEntita()) {
			if (entita.getId().equals(entitaLite.getId())) {
				return contratto;
			}
		}

		// rimuovo tutte le eventuali sedi dell'entità legate al contratto
		List<SedeMagazzinoLite> sediDaRimuovere = new ArrayList<SedeMagazzinoLite>();
		for (SedeMagazzinoLite sedeMagazzinoLite : contratto.getSediMagazzino()) {
			if (sedeMagazzinoLite.getSedeEntita().getEntita().getId().equals(entitaLite.getId())) {
				sediDaRimuovere.add(sedeMagazzinoLite);
			}
		}
		contratto.getSediMagazzino().removeAll(sediDaRimuovere);

		contratto.getEntita().add(entitaLite);
		Contratto contrattoSalvato = salvaContratto(contratto, false);

		logger.debug("--> Exit associaEntitaContratto");
		return contrattoSalvato;
	}

	@Override
	public Contratto associaSedeContratto(Contratto contratto, SedeMagazzinoLite sedeMagazzinoLite) {
		logger.debug("--> Enter associaSedeContratto");
		contratto.getSediMagazzino().add(sedeMagazzinoLite);
		Contratto contrattoSalvato = salvaContratto(contratto, false);
		logger.debug("--> Exit associaSedeContratto");
		return contrattoSalvato;
	}

	@Override
	public void cancellaContratto(Contratto contratto) {
		logger.debug("--> Enter cancellaContratto");
		try {
			panjeaDAO.delete(contratto);
		} catch (VincoloException e) {
			logger.error("--> errore VincoloExcetion in cancellazione contratto ", e);
			throw new RuntimeException(e);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in cancellazione contratto ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaContratto");
	}

	@Override
	public void cancellaRigaContratto(RigaContratto rigaContratto) {
		logger.debug("--> Enter cancellaContrattoArticolo");
		try {
			panjeaDAO.delete(rigaContratto);
		} catch (VincoloException e) {
			logger.error("--> errore VincoloException in cancellaContrattoArticolo ", e);
			throw new RuntimeException(e);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in cancellaContrattoArticolo", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaContrattoArticolo");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Contratto> caricaContratti(ParametriRicercaContratti parametriRicercaContratti) {
		logger.debug("--> Enter caricaContratti");
		Query query = panjeaDAO.prepareNamedQuery("Contratto.caricaContratti");
		query.setParameter("paramCodiceAzienda", getJecPrincipal().getCodiceAzienda());
		List<Contratto> contratti;
		try {
			contratti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore DAO in caricaContratti", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaContratti");
		return contratti;
	}

	@Override
	public Contratto caricaContratto(Contratto contratto, boolean loadCollection) {
		logger.debug("--> Enter caricaContratto");
		Contratto contrattoCaricato;
		try {
			contrattoCaricato = panjeaDAO.load(Contratto.class, contratto.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore ObjectNotFoundException in caricaContratto", e);
			throw new RuntimeException(e);
		}
		if (loadCollection) {
			// inizializza le collection
			contrattoCaricato.getSediMagazzino().size();
			contrattoCaricato.getEntita().size();
			contrattoCaricato.getRigheContratto().size();
			contrattoCaricato.getCategorieSediMagazzino().size();

		}
		logger.debug("--> Exit caricaContratto");
		return contrattoCaricato;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContrattoProspettoDTO> caricaContrattoProspetto(Integer idSedeEntita, Date data) {
		StringBuilder sb = new StringBuilder();
		// Righe legate direttamente alle sedi
		sb.append("select c.id as contrattoId, ");
		sb.append("c.descrizione as contrattoDescrizione, ");
		sb.append("c.codice as contrattoCodice, ");
		sb.append("c.dataFine as ContrattoDataFine, ");
		sb.append("c.dataInizio as ContrattoDataInizio, ");
		sb.append("2 as tipoLinkEntitaNum, ");
		sb.append("sedeMagazzino.sedeEntita.entita.class as classEntita, ");
		sb.append("sedeMagazzino.sedeEntita.entita.id as entitaId, ");
		sb.append("sedeMagazzino.sedeEntita.entita.codice as entitaCodice, ");
		sb.append("sedeMagazzino.sedeEntita.entita.anagrafica.denominazione as entitaDescrizione, ");
		sb.append("sedeMagazzino.sedeEntita.id as sedeId, ");
		sb.append("sedeMagazzino.sedeEntita.codice as sedeCodice, ");
		sb.append("sedeMagazzino.sedeEntita.sede.descrizione as sedeDescrizione ");
		sb.append("from  Contratto c ");
		sb.append("join c.sediMagazzino sedeMagazzino ");
		if (idSedeEntita != null) {
			sb.append("where sedeMagazzino.sedeEntita.id=");
			sb.append(idSedeEntita);
		}
		org.hibernate.Query querySede = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(sb
				.toString());
		querySede.setResultTransformer(Transformers.aliasToBean(ContrattoProspettoDTO.class));
		List<ContrattoProspettoDTO> righe = new ArrayList<ContrattoProspettoDTO>();
		righe.addAll(querySede.list());

		// // Righe legate all'entità
		sb = new StringBuilder();
		sb.append("select ");
		sb.append("c.id as contrattoId, ");
		sb.append("c.descrizione as contrattoDescrizione, ");
		sb.append("c.codice as contrattoCodice, ");
		sb.append("c.dataFine as ContrattoDataFine, ");
		sb.append("c.dataInizio as ContrattoDataInizio, ");
		sb.append("4 as tipoLinkEntitaNum, ");
		sb.append("ent.class as classEntita, ");
		sb.append("ent.id as entitaId, ");
		sb.append("ent.codice as entitaCodice, ");
		sb.append("ent.anagrafica.denominazione as entitaDescrizione, ");
		sb.append("sede.id as sedeId, ");
		sb.append("sede.codice as sedeCodice, ");
		sb.append("sede.sede.descrizione as sedeDescrizione ");
		sb.append("from Contratto c,SedeEntita sede ");
		sb.append("join c.entita ent ");
		sb.append("where sede.entita=ent ");
		if (idSedeEntita != null) {
			sb.append("and sede.id=");
			sb.append(idSedeEntita);
		}
		querySede = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(sb.toString());
		querySede.setResultTransformer(Transformers.aliasToBean(ContrattoProspettoDTO.class));
		righe.addAll(querySede.list());

		sb = new StringBuilder();
		// Righe legate alla categoria della sede che non eredita dati commerciali
		sb.append("select c.id as contrattoId, ");
		sb.append("c.descrizione as contrattoDescrizione, ");
		sb.append("c.codice as contrattoCodice, ");
		sb.append("c.dataFine as ContrattoDataFine, ");
		sb.append("c.dataInizio as ContrattoDataInizio, ");
		sb.append("1 as tipoLinkEntitaNum, ");
		sb.append("sede.entita.class as classEntita, ");
		sb.append("sede.entita.id as entitaId, ");
		sb.append("sede.entita.codice as entitaCodice, ");
		sb.append("sede.entita.anagrafica.denominazione as entitaDescrizione, ");
		sb.append("sede.id as sedeId, ");
		sb.append("sede.codice as sedeCodice, ");
		sb.append("sede.sede.descrizione as sedeDescrizione ");
		sb.append("from Contratto c ,SedeMagazzino sm ");
		sb.append("join c.categorieSediMagazzino cat ");
		sb.append("join sm.sedeEntita sede ");
		sb.append("where sm.categoriaSedeMagazzino=cat ");
		sb.append("and sede.ereditaDatiCommerciali=false ");
		if (idSedeEntita != null) {
			sb.append("and sede.id=");
			sb.append(idSedeEntita);
		}
		querySede = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(sb.toString());
		querySede.setResultTransformer(Transformers.aliasToBean(ContrattoProspettoDTO.class));
		righe.addAll(querySede.list());

		// Righe collegate alla categoria ereditata
		sb = new StringBuilder();
		sb.append("select c.id as contrattoId, ");
		sb.append("c.descrizione as contrattoDescrizione, ");
		sb.append("c.codice as contrattoCodice, ");
		sb.append("c.dataFine as ContrattoDataFine, ");
		sb.append("c.dataInizio as ContrattoDataInizio, ");
		sb.append("3 as tipoLinkEntitaNum, ");
		sb.append("sede.entita.class as classEntita, ");
		sb.append("sede.entita.id as entitaId, ");
		sb.append("sede.entita.codice as entitaCodice, ");
		sb.append("sede.entita.anagrafica.denominazione as entitaDescrizione, ");
		sb.append("sede.id as sedeId, ");
		sb.append("sede.codice as sedeCodice, ");
		sb.append("sede.sede.descrizione as sedeDescrizione ");
		sb.append("from Contratto c ,SedeMagazzino sm ,SedeMagazzino smp ");
		sb.append("join c.categorieSediMagazzino cat ");
		sb.append("join sm.sedeEntita sede ");
		sb.append("join smp.sedeEntita sedePrincipale ");
		sb.append("where sedePrincipale.entita=sede.entita ");
		sb.append("and sedePrincipale.tipoSede.sedePrincipale=true ");
		sb.append("and sede.tipoSede.sedePrincipale=false ");
		sb.append("and smp.categoriaSedeMagazzino=cat ");
		sb.append("and sede.ereditaDatiCommerciali=true ");
		if (idSedeEntita != null) {
			sb.append("and sede.id=");
			sb.append(idSedeEntita);
		}
		querySede = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(sb.toString());
		querySede.setResultTransformer(Transformers.aliasToBean(ContrattoProspettoDTO.class));
		righe.addAll(querySede.list());

		// Contratti legati a tutte le categorie e tutti
		sb = new StringBuilder();
		sb.append("select ");
		sb.append("c.id as contrattoId, ");
		sb.append("c.descrizione as contrattoDescrizione, ");
		sb.append("c.codice as contrattoCodice, ");
		sb.append("c.dataFine as ContrattoDataFine, ");
		sb.append("c.dataInizio as ContrattoDataInizio, ");
		sb.append("0 as tipoLinkEntitaNum, ");
		sb.append("sede.entita.class as classEntita, ");
		sb.append("sede.entita.id as entitaId, ");
		sb.append("sede.entita.codice as entitaCodice, ");
		sb.append("sede.entita.anagrafica.denominazione as entitaDescrizione, ");
		sb.append("sede.id as sedeId, ");
		sb.append("sede.codice as sedeCodice, ");
		sb.append("sede.sede.descrizione as sedeDescrizione ");
		sb.append("from Contratto c,SedeEntita sede ");
		sb.append("join sede.sedeMagazzino sm ");
		sb.append("where (c.tutteCategorieSedeMagazzino=true ");
		sb.append("or (c.tutteCategorieSedeMagazzino=false and c.categorieSediMagazzino.size=0 and c.sediMagazzino.size=0 and c.entita.size=0)) ");
		if (idSedeEntita != null) {
			sb.append(" and sm.sedeEntita.id=");
			sb.append(idSedeEntita);
		}
		querySede = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(sb.toString());
		querySede.setResultTransformer(Transformers.aliasToBean(ContrattoProspettoDTO.class));
		righe.addAll(querySede.list());
		return righe;
	}

	@Override
	public RigaContratto caricaRigaContratto(RigaContratto rigaContratto) {
		logger.debug("--> Enter caricaContrattoArticolo");
		try {
			rigaContratto = panjeaDAO.load(RigaContratto.class, rigaContratto.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore ObjectNotFound in caricaContrattoArticolo", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaContrattoArticolo");
		return rigaContratto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaContratto> caricaRigheContratto(Contratto contratto) {
		logger.debug("--> Enter caricaContrattoArticoli");
		List<RigaContratto> righeContratto = new ArrayList<RigaContratto>();
		if (contratto.isNew()) {
			return righeContratto;
		}
		Query query = panjeaDAO.prepareNamedQuery("RigaContratto.caricaByContratto");
		query.setParameter("paramIdContratto", contratto.getId());

		try {
			righeContratto = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaContrattoArticoli", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaContrattoArticoli");
		return righeContratto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaContrattoCalcolo> caricaRigheContrattoCalcolo(Integer idArticolo, Integer idEntita,
			Integer idSedeEntita, Integer idCategoriaSedeMagazzino, Integer idCategoriaCommercialeArticolo, Date data,
			String codiceValuta, Integer idAgente, boolean tutteLeRighe) {
		logger.debug("--> Enter caricaRigheContrattoCalcolo");

		// SedeMagazzino sedeMagazzino = null;
		Integer idSedeMagazzino = null;

		// Per performance eseguo delle query specifiche
		if (idSedeEntita != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("select ");
			sb.append("se.entita_id,sm.id,sm.categoriaSedeMagazzino_id,se.ereditaDatiCommerciali, ");
			sb.append("(select ");
			sb.append("smp.categoriaSedeMagazzino_id ");
			sb.append("from maga_sedi_magazzino smp ");
			sb.append("inner join anag_sedi_entita sep on sep.id=smp.sedeEntita_id ");
			sb.append("inner join anag_tipo_sede_entita ts on ts.id=sep.tipoSede_id ");
			sb.append("where sep.entita_id=ent.id and ts.sede_principale=1) ");
			sb.append("from maga_sedi_magazzino sm ");
			sb.append("inner join anag_sedi_entita se on se.id=sm.sedeEntita_id ");
			sb.append("inner join anag_entita ent on ent.id=se.entita_id ");
			sb.append("where se.id=:paramIdSede ");

			Query querySedeMagazzino = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());

			querySedeMagazzino.setParameter("paramIdSede", idSedeEntita);
			try {
				Object[] result = (Object[]) querySedeMagazzino.getSingleResult();

				idEntita = (Integer) result[0];
				idSedeMagazzino = (Integer) result[1];
				idCategoriaSedeMagazzino = (Integer) result[2];
				if ((Boolean) result[3]) {
					idCategoriaSedeMagazzino = (Integer) result[4];
				}
			} catch (NoResultException e) {
				logger.error("Errore, sede magazzino assente per la sede entita con id " + idSedeEntita, e);
				throw new SedeMagazzinoAssenteException("Errore, sede magazzino assente per la sede entita con id "
						+ idSedeEntita);
			}
		}

		List<Integer> idCategorieSedePerEntita = new ArrayList<Integer>();
		if (idEntita != null && idSedeEntita == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("select ");
			sb.append("sm.categoriaSedeMagazzino_id,se.ereditaDatiCommerciali, ");
			sb.append("(select ");
			sb.append("smp.categoriaSedeMagazzino_id ");
			sb.append("from maga_sedi_magazzino smp ");
			sb.append("inner join anag_sedi_entita sep on sep.id=smp.sedeEntita_id ");
			sb.append("inner join anag_tipo_sede_entita ts on ts.id=sep.tipoSede_id ");
			sb.append("where sep.entita_id=ent.id and ts.sede_principale=1) ");
			sb.append("from maga_sedi_magazzino sm ");
			sb.append("inner join anag_sedi_entita se on se.id=sm.sedeEntita_id ");
			sb.append("inner join anag_entita ent on ent.id=se.entita_id ");
			sb.append("where se.entita_id=:paramIdEntita ");

			Query querySedeMagazzino = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());

			querySedeMagazzino.setParameter("paramIdEntita", idEntita);
			List<Object[]> result = querySedeMagazzino.getResultList();

			Integer idCategoriaSedePerEntita = null;
			for (Object[] objects : result) {
				idCategoriaSedePerEntita = (Integer) objects[0];
				if ((Boolean) objects[1]) {
					idCategoriaSedePerEntita = (Integer) objects[2];
				}
				if (idCategoriaSedePerEntita != null) {
					idCategorieSedePerEntita.add(idCategoriaSedePerEntita);
				}
			}
		}

		Integer idCategoriaCommercialeArticolo2 = null;
		if (idArticolo != null) {
			try {
				Query queryCategoria = panjeaDAO
						.getEntityManager()
						.createNativeQuery(
								"select c.id from maga_categoria_commerciale_articolo c inner join maga_articoli a on a.categoriaCommercialeArticolo_id=c.id where a.id="
										+ idArticolo);
				idCategoriaCommercialeArticolo = (Integer) panjeaDAO.getSingleResult(queryCategoria);
			} catch (Exception e) {
				throw new RuntimeException("CategoriaArticolo non trovata", e);
			}
			try {
				Query queryCategoria = panjeaDAO
						.getEntityManager()
						.createNativeQuery(
								"select c.id from maga_categoria_commerciale_articolo c inner join maga_articoli a on a.categoriaCommercialeArticolo2_id=c.id where a.id="
										+ idArticolo);
				idCategoriaCommercialeArticolo2 = (Integer) panjeaDAO.getSingleResult(queryCategoria);
			} catch (ObjectNotFoundException e) {
				logger.info("L'articolo non ha impostata una categoria commerciale 2");
				// non faccio nulla, la categoria commerciale 2 non è obbligatoria
			} catch (Exception e) {
				throw new RuntimeException("Errore nella ricerca della categoria commerciale 2 articolo", e);
			}
		}

		// INIZIO questa è la vecchia query, la tengo per verifica rispetto alla nuova
		// (RigheContrattoCalcoloQueryBuilder)
		// TODO da cancellare
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append("  contratto.tutteCategorieSedeMagazzino as tutteLeCategorieSediMagazzino, ");
		sb.append("  contratto.codice as codiceContratto, ");
		sb.append("  contratto.dataInizio as dataInizioContratto, ");
		sb.append("  contratto.dataFine as dataFineContratto, ");
		sb.append("  contratto.descrizione as descrizioneContratto, ");
		sb.append("  sediMagazzino.sediMagazzino_id as idSedeMagazzino, ");
		sb.append("  categorieSedi.categorieSediMagazzino_id as idCategoriaSede, ");
		sb.append("  riga.id as id, ");
		sb.append("  riga.articolo_id as idArticolo, ");
		sb.append("  riga.categoriaCommercialeArticolo_id as idCategoriaCommercialeArticolo, ");
		sb.append("  riga.categoriaCommercialeArticolo2_id as idCategoriaCommercialeArticolo2, ");
		sb.append("  riga.contratto_id as idContratto, ");
		sb.append("  riga.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("  riga.azionePrezzo as azionePrezzo, ");
		sb.append("  riga.bloccoPrezzo as bloccoPrezzo, ");
		sb.append("  riga.ignoraBloccoPrezzoPrecedente as ignoraBloccoPrezzoPrecedente, ");
		sb.append("  riga.quantitaSogliaPrezzo as quantitaSogliaPrezzo, ");
		sb.append("  riga.tipoValorePrezzo as tipoValorePrezzo, ");
		sb.append("  riga.valorePrezzo as valorePrezzo, ");
		sb.append("  riga.azioneSconto as azioneSconto, ");
		sb.append("  riga.bloccoSconto as bloccoSconto, ");
		sb.append("  riga.ignoraBloccoScontoPrecedente as ignoraBloccoScontoPrecedente, ");
		sb.append("  riga.quantitaSogliaSconto as quantitaSogliaSconto, ");
		sb.append("  riga.tutteLeCategorie as tutteLeCategorieArticolo, ");

		sb.append("  riga.strategiaPrezzoAbilitata as strategiaPrezzoAbilitata, ");
		sb.append("  riga.strategiaScontoAbilitata as strategiaScontoAbilitata, ");

		sb.append("  sconti.sconto1 as sconto1, ");
		sb.append("  sconti.sconto2 as sconto2, ");
		sb.append("  sconti.sconto3 as sconto3, ");
		sb.append("  sconti.sconto4 as sconto4, ");
		sb.append("  contrattiEntita.entita_id as idEntita, ");
		sb.append("  rigaContrattoAgente.agente_id as idAgente, ");
		sb.append("  rigaContrattoAgente.valoreProvvigione as provvigineAgente, ");
		sb.append("  ifnull(rigaContrattoAgente.blocco,false) as bloccoProvvigione, ");
		sb.append("  ifnull(rigaContrattoAgente.ignoraBloccoPrecedente,false) as ignoraBloccoProvvigionePrecedente, ");
		sb.append("  rigaContrattoAgente.azione as azioneProvvigione ");
		sb.append(" from ");
		sb.append(" maga_righe_contratto riga ");
		sb.append(" left outer join maga_contratti contratto on riga.contratto_id=contratto.id ");
		sb.append(" left outer join maga_contratti_maga_categorie_sedi_magazzino categorieSedi on contratto.id=categorieSedi.maga_contratti_id ");
		sb.append(" left outer join maga_contratti_maga_sedi_magazzino sediMagazzino on contratto.id=sediMagazzino.maga_contratti_id ");
		sb.append(" left outer join maga_sconti sconti  on sconti.id=riga.sconto_id ");
		sb.append(" left outer join maga_righe_contratto_agente rigaContrattoAgente on rigaContrattoAgente.rigaContratto_id = riga.id ");
		sb.append(" left outer join maga_contratti_anag_entita contrattiEntita on contrattiEntita.maga_contratti_id=contratto.id  ");
		if (!tutteLeRighe) {
			sb.append(" where ");
		}
		if (idArticolo != null) {
			sb.append(" (  riga.articolo_id=:idArticolo");
			sb.append("  or riga.categoriaCommercialeArticolo_id=:idCategoriaCommercialeArticolo");
			sb.append(" or riga.tutteLeCategorie=true) ");
			sb.append(" and (");
		}

		if (idSedeMagazzino != null) {
			sb.append("(sediMagazzino.sediMagazzino_id=:idSede ");
			if (idCategoriaSedeMagazzino != null) {
				sb.append(" or categorieSedi.categorieSediMagazzino_id=:idCategoriaSede");
			}
			sb.append(" or ");
			if (idEntita != null) {
				sb.append("contrattiEntita.entita_id=:entitaId or ");
				sb.append(" contratto.tutteCategorieSedeMagazzino=true ");
				sb.append(")");
			}
			sb.append("or (");
		}

		if (!tutteLeRighe) {
			sb.append(" contratto.tutteCategorieSedeMagazzino=false and contrattiEntita.entita_id is null and sediMagazzino.sediMagazzino_id is null and categorieSediMagazzino_id is null");
		}

		if (idSedeMagazzino != null) {
			sb.append(")");
		}

		if (idArticolo != null) {
			sb.append(")");
		}

		sb.append(" and contratto.dataInizio<=:data and contratto.dataFine>=:data ");

		if (codiceValuta != null && !codiceValuta.isEmpty()) {
			sb.append(" and contratto.codiceValuta = :codiceValuta ");
		}

		sb.append(" order by  contratto.dataInizio,riga.quantitaSogliaPrezzo,riga.quantitaSogliaSconto,contratto.id,rigaContrattoAgente.agente_id desc ");
		// FINE questa è la vecchia query, la tengo per verifica rispetto alla nuova (RigheContrattoCalcoloQueryBuilder)

		String sql = RigheContrattoCalcoloQueryBuilder.getSql(idArticolo, idCategoriaCommercialeArticolo,
				idCategoriaCommercialeArticolo2, idCategorieSedePerEntita, idCategoriaSedeMagazzino, idEntita,
				idSedeMagazzino, codiceValuta, tutteLeRighe);

		SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sql);
		query.addScalar("tutteLeCategorieSediMagazzino");
		query.addScalar("codiceContratto");
		query.addScalar("dataInizioContratto");
		query.addScalar("dataFineContratto");
		query.addScalar("descrizioneContratto");
		query.addScalar("idSedeMagazzino");
		query.addScalar("idCategoriaSede");
		query.addScalar("id");
		query.addScalar("idArticolo");
		query.addScalar("idCategoriaCommercialeArticolo", Hibernate.INTEGER);
		query.addScalar("idCategoriaCommercialeArticolo2", Hibernate.INTEGER);
		query.addScalar("idContratto");
		query.addScalar("numeroDecimaliPrezzo");
		query.addScalar("azionePrezzo");
		query.addScalar("bloccoPrezzo");
		query.addScalar("ignoraBloccoPrezzoPrecedente");
		query.addScalar("quantitaSogliaPrezzo");
		query.addScalar("tipoValorePrezzo");
		query.addScalar("valorePrezzo");
		query.addScalar("azioneSconto");
		query.addScalar("bloccoSconto");
		query.addScalar("ignoraBloccoScontoPrecedente");
		query.addScalar("quantitaSogliaSconto");
		query.addScalar("tutteLeCategorieArticolo");
		query.addScalar("strategiaPrezzoAbilitata");
		query.addScalar("strategiaScontoAbilitata");
		query.addScalar("sconto1");
		query.addScalar("sconto2");
		query.addScalar("sconto3");
		query.addScalar("sconto4");
		query.addScalar("idEntita");
		query.addScalar("idAgente");
		query.addScalar("provvigineAgente");
		query.addScalar("bloccoProvvigione", Hibernate.BOOLEAN);
		query.addScalar("ignoraBloccoProvvigionePrecedente", Hibernate.BOOLEAN);
		query.addScalar("azioneProvvigione");

		if (idArticolo != null) {
			query.setParameter("idArticolo", idArticolo);
		}
		if (idCategoriaCommercialeArticolo != null) {
			query.setParameter("idCategoriaCommercialeArticolo", idCategoriaCommercialeArticolo);
		}
		if (idCategoriaCommercialeArticolo2 != null) {
			query.setParameter("idCategoriaCommercialeArticolo2", idCategoriaCommercialeArticolo2);
		}

		if (idEntita != null) {
			query.setParameter("entitaId", idEntita);
		}
		if (!idCategorieSedePerEntita.isEmpty()) {
			query.setParameterList("categorieSedeEntita", idCategorieSedePerEntita);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> articolo : " + idArticolo);
			logger.debug("--> categoria : " + idCategoriaCommercialeArticolo);
			logger.debug("--> data : " + data);
		}
		query.setParameter("data", data);

		if (codiceValuta != null && !codiceValuta.isEmpty()) {
			query.setParameter("codiceValuta", codiceValuta);
		}

		if (idSedeMagazzino != null) {
			query.setParameter("idSede", idSedeMagazzino);
		}
		if (idCategoriaSedeMagazzino != null) {
			query.setParameter("idCategoriaSede", idCategoriaSedeMagazzino);
		}

		List<RigaContrattoCalcolo> righeContrattoCalcolo = new ArrayList<RigaContrattoCalcolo>();
		try {
			query.setResultTransformer(Transformers.aliasToBean(RigaContrattoCalcolo.class));
			righeContrattoCalcolo = query.list();
		} catch (Exception e) {
			logger.error("-->errore nel recuperare le righe contratto", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaRigheContrattoCalcolo con num righe " + righeContrattoCalcolo.size());
		// Devo ordinare le righe contratto con l'ordine di applicazione
		Collections.sort(righeContrattoCalcolo);
		return righeContrattoCalcolo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContrattoStampaDTO> caricaStampaContratti(ParametriRicercaStampaContratti parametri) {
		List<ContrattoStampaDTO> righe = new ArrayList<ContrattoStampaDTO>();

		try {
			// Contratti legati alle sedi
			Object[] sqlObjects = RigheContrattoStampaQueryBuilder.getSediSqlQuery(parametri, getAzienda());
			Query query = panjeaDAO.prepareSQLQuery(sqlObjects[0].toString(), ContrattoStampaDTO.class,
					(List<String>) sqlObjects[1]);
			righe.addAll(panjeaDAO.getResultList(query));

			// Contratti legati alle entità
			sqlObjects = RigheContrattoStampaQueryBuilder.getEntitaSqlQuery(parametri, getAzienda());
			query = panjeaDAO.prepareSQLQuery(sqlObjects[0].toString(), ContrattoStampaDTO.class,
					(List<String>) sqlObjects[1]);
			righe.addAll(panjeaDAO.getResultList(query));

			// Contratti legati alle categorie. Se non viene filtrata l'entità posso caricare tutti i contratti che
			// hanno delle categorie associate ( o tutte le categorie), altrimenti devo caricare tutti i contratti
			// legati alle categorie associate alle sedi ( ereditate e non ) dell'entità.
			if (parametri.getEntita() == null || parametri.getEntita().isNew()) {
				sqlObjects = RigheContrattoStampaQueryBuilder.getCateogorieSediSqlQuery(parametri, getAzienda());
				query = panjeaDAO.prepareSQLQuery(sqlObjects[0].toString(), ContrattoStampaDTO.class,
						(List<String>) sqlObjects[1]);
				righe.addAll(panjeaDAO.getResultList(query));
			} else {
				// Cateogorie non ereditate
				sqlObjects = RigheContrattoStampaQueryBuilder.getCateogorieSediNonEreditateSqlQuery(parametri,
						getAzienda());
				query = panjeaDAO.prepareSQLQuery(sqlObjects[0].toString(), ContrattoStampaDTO.class,
						(List<String>) sqlObjects[1]);
				righe.addAll(panjeaDAO.getResultList(query));

				// categorie ereditate
				sqlObjects = RigheContrattoStampaQueryBuilder.getCateogorieSediEreditateSqlQuery(parametri,
						getAzienda());
				query = panjeaDAO.prepareSQLQuery(sqlObjects[0].toString(), ContrattoStampaDTO.class,
						(List<String>) sqlObjects[1]);
				righe.addAll(panjeaDAO.getResultList(query));
			}

			// tutte le categorie
			sqlObjects = RigheContrattoStampaQueryBuilder.getTutteCateogorieQuery(parametri, getAzienda());
			query = panjeaDAO.prepareSQLQuery(sqlObjects[0].toString(), ContrattoStampaDTO.class,
					(List<String>) sqlObjects[1]);
			righe.addAll(panjeaDAO.getResultList(query));

		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento della stampa contratti.", e);
			throw new RuntimeException("errore durante il caricamento della stampa contratti.", e);
		}

		return righe;
	}

	/**
	 *
	 * @return codice dell'azienda loggata.
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

	/**
	 * recupera dal context il {@link JecPrincipal} loggato.
	 *
	 * @return jecprincipal loggato.
	 */
	private JecPrincipal getJecPrincipal() {
		return (JecPrincipal) sessionContext.getCallerPrincipal();
	}

	@Override
	public Contratto rimuoviCategoriaSedeContratto(Contratto contratto, CategoriaSedeMagazzino categoriaSedeMagazzino) {
		logger.debug("--> Enter rimuoviCategoriaSedeContratto");
		contratto.getCategorieSediMagazzino().remove(categoriaSedeMagazzino);
		Contratto contrattoSalvato = salvaContratto(contratto, false);
		logger.debug("--> Exit rimuoviCategoriaSedeContratto");
		return contrattoSalvato;
	}

	@Override
	public Contratto rimuoviEntitaContratto(Contratto contratto, EntitaLite entitaLite) {
		logger.debug("--> Enter rimuoviEntitaContratto");
		contratto.getEntita().remove(entitaLite);
		Contratto contrattoSalvato = salvaContratto(contratto, false);
		logger.debug("--> Exit rimuoviEntitaContratto");
		return contrattoSalvato;
	}

	@Override
	public Contratto rimuoviSedeContratto(Contratto contratto, SedeMagazzinoLite sedeMagazzinoLite) {
		logger.debug("--> Enter rimuoviSedeContratto");
		contratto.getSediMagazzino().remove(sedeMagazzinoLite);
		Contratto contrattoSalvato = salvaContratto(contratto, false);
		logger.debug("--> Exit rimuoviSedeContratto");
		return contrattoSalvato;
	}

	@Override
	public Contratto salvaContratto(Contratto contratto, boolean loadCollection) {
		logger.debug("--> Enter salvaContratto");
		// inizializzo CodiceAzienda
		contratto.setCodiceAzienda(getJecPrincipal().getCodiceAzienda());

		Contratto contrattoSalvato = null;
		try {
			// Se ho tutte le categorie selezionate devo rimuovere le sedi collegate
			if (contratto.isTutteCategorieSedeMagazzino()) {
				contratto.setCategorieSediMagazzino(new ArrayList<CategoriaSedeMagazzino>());
				contratto.setSediMagazzino(new ArrayList<SedeMagazzinoLite>());
				contratto.setEntita(new ArrayList<EntitaLite>());
			}
			contrattoSalvato = panjeaDAO.save(contratto);
			if (loadCollection) {
				// inizializza le collection
				if (contrattoSalvato.getSediMagazzino() != null) {
					contrattoSalvato.getSediMagazzino().size();
				}
				if (contrattoSalvato.getRigheContratto() != null) {
					contrattoSalvato.getRigheContratto().size();
				}
			}
		} catch (Exception e) {
			logger.error("--> errore in salvaContratto", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaContratto");
		return contrattoSalvato;
	}

	@Override
	public RigaContratto salvaRigaContratto(RigaContratto rigaContratto) {
		logger.debug("--> Enter salvaRigaContratto");
		try {
			rigaContratto = panjeaDAO.save(rigaContratto);
		} catch (Exception e) {
			logger.error("--> errore in salvaContrattoArticolo", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaRigaContratto");
		return rigaContratto;
	}

}
