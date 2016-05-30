package it.eurotn.panjea.magazzino.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.magazzino.domain.AttributoCategoria;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.manager.interfaces.CategorieManager;
import it.eurotn.panjea.magazzino.manager.sqlbuilder.CategoriaSincronizzazioneAttributiQueryBuilder;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.Comparator;
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
import org.hibernate.Session;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.CategorieManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CategorieManager")
public class CategorieManagerBean implements CategorieManager {
	private static Logger logger = Logger.getLogger(CategorieManagerBean.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext sessionContext;

	@Override
	public void cancellaCategoria(Integer idCategoria) {
		logger.debug("--> Enter cancellaCategoria");

		Categoria categoria = new Categoria();
		try {
			categoria.setId(idCategoria);

			panjeaDAO.delete(categoria);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione della categoria " + categoria.getId(), e);
			throw new RuntimeException("Errore durante la cancellazione della categoria " + categoria.getId(), e);
		}
		logger.debug("--> Exit cancellaCategoria");
	}

	/**
	 *
	 * @param categoria
	 *            categoria per la quale caricare gli attributi
	 * @return lista degli attributi per la categoria
	 */
	@SuppressWarnings("unchecked")
	private List<AttributoCategoria> caricaAttributiCategoria(Categoria categoria) {

		List<AttributoCategoria> attributi = new ArrayList<AttributoCategoria>();

		if (categoria.getId() != null) {
			CategoriaLite categoriaPadre = new CategoriaLite(categoria);
			List<CategoriaLite> categorieDaSincronizzare = caricaCategorieFiglie(categoria.getId());
			categorieDaSincronizzare.add(categoriaPadre);

			List<Categoria> categorie = new ArrayList<Categoria>();
			for (CategoriaLite categoriaLite : categorieDaSincronizzare) {
				Categoria cat = categoriaLite.createCategoria();
				categorie.add(cat);
			}

			Query query = panjeaDAO.prepareNamedQuery("AttributoCategoria.caricaByCategorie");
			query.setParameter("categorie", categorie);
			try {
				attributi = panjeaDAO.getResultList(query);
			} catch (Exception e) {
				logger.error("--> errore il caricamento degli attributi delle categorie", e);
				throw new RuntimeException("errore il caricamento degli attributi delle categorie", e);
			}
		}

		return attributi;
	}

	@Override
	public Categoria caricaCategoria(Categoria categoria, boolean initializeLazy) {
		logger.debug("--> Enter caricaCategoria");
		Categoria categoriaResult = null;
		try {
			categoriaResult = panjeaDAO.load(Categoria.class, categoria.getId());

			if (initializeLazy) {
				logger.debug("--> Inizializzo le collezioni");
				categoriaResult.getAttributiCategoria().size();
				logger.debug("-->collezioni inizializzate");
			}
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore nel caricare la categoria " + categoria, e);
			throw new RuntimeException(e);
		} catch (RuntimeException e) {
			logger.error("--> errore nel caricare la categoria " + categoria, e);
			throw e;
		}
		logger.debug("--> Exit caricaCategoria");
		return categoriaResult;
	}

	@Override
	public List<CategoriaLite> caricaCategorie() {
		logger.debug("--> Enter caricaCategorie");

		Map<Integer, CategoriaLite> categorieMap = creaAlbero();

		List<CategoriaLite> categorieResult = new ArrayList<CategoriaLite>();

		for (Entry<Integer, CategoriaLite> categoriaEntry : categorieMap.entrySet()) {
			if (categoriaEntry.getValue().isRoot()) {
				categorieResult.add(categoriaEntry.getValue());
			}
		}

		EventList<CategoriaLite> categorieEventList = new BasicEventList<CategoriaLite>();
		categorieEventList.addAll(categorieResult);

		SortedList<CategoriaLite> categorieSortedList = new SortedList<CategoriaLite>(categorieEventList,
				new Comparator<CategoriaLite>() {

			@Override
			public int compare(CategoriaLite o1, CategoriaLite o2) {
				return o1.getCodice().compareTo(o2.getCodice());
			}
		});

		categorieResult.clear();
		categorieResult.addAll(categorieSortedList);
		logger.debug("--> Exit caricaCategorie");
		return categorieResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Categoria> caricaCategorieCodiceDescrizione(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter cercaCategoria");
		StringBuilder sb = new StringBuilder(
				"select DISTINCT c.id as id,c.version as version,c.codice as codice, c.descrizioneLinguaAziendale as descrizioneLinguaAziendale from Categoria c where c.codiceAzienda = :paramCodiceAzienda ");

		// devo farmi per forza l'if perchè se viene usata la descrizione per l'sql devo usare il nome colonna
		// "descrizioneLinguaAziendale"
		if ("descrizione".equals(fieldSearch)) {
			fieldSearch = "descrizioneLinguaAziendale";
		}

		if (valueSearch != null) {
			sb.append(" and c.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		sb.append(" order by c.").append(fieldSearch);
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramCodiceAzienda", getAzienda());

		List<Categoria> listResult = new ArrayList<Categoria>();

		try {
			((org.hibernate.ejb.HibernateQuery) query).getHibernateQuery().setResultTransformer(
					Transformers.aliasToBean(Categoria.class));
			listResult = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento degli articoli lite per l'azienda", e);
			throw new RuntimeException("Errore durante il caricamento degli articoli lite per l'azienda", e);
		}
		logger.debug("--> Exit cercaCategoria");
		return listResult;
	}

	@Override
	public List<CategoriaLite> caricaCategorieFiglie(Integer idCategoria) {
		logger.debug("--> Enter caricaCategorieFiglie");
		Map<Integer, CategoriaLite> alberoCategorie = creaAlbero();
		CategoriaLite categoriaLite = alberoCategorie.get(idCategoria);
		List<CategoriaLite> categorieFiglie = visiteCategoriaLite(categoriaLite);
		logger.debug("--> Exit caricaCategorieFiglie");
		return categorieFiglie;
	}

	/**
	 * carica in una mappa tutte le categorie e inizializza le categorie figlie.
	 *
	 * @return mappa con le categorie. La chiave della mappa è il codice categoria.
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, CategoriaLite> creaAlbero() {
		logger.debug("--> Enter creaAlbero");
		List<Categoria> list = new ArrayList<Categoria>();
		Query query = panjeaDAO.prepareNamedQuery("Categoria.caricaAll");
		query.setParameter("paramCodiceAzienda", getAzienda());
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento delle categorie.", e);
			throw new RuntimeException("Errore durante il caricamento delle categorie.", e);
		}

		// creo tutti i DTO
		Map<Integer, CategoriaLite> mapCategorie = new HashMap<Integer, CategoriaLite>();
		for (Categoria categoria : list) {
			CategoriaLite categoriaLite = new CategoriaLite(categoria,
					((JecPrincipal) sessionContext.getCallerPrincipal()).getLingua());
			mapCategorie.put(categoriaLite.getId(), categoriaLite);
		}

		// scorro di nuovo la lista delle categorie per costruire l'albero
		for (Categoria categoria2 : list) {
			if (categoria2.getPadre() != null) {
				CategoriaLite categoriaLiteFiglio = mapCategorie.get(categoria2.getId());
				CategoriaLite categoriaLitePadre = mapCategorie.get(categoria2.getPadre().getId());

				categoriaLitePadre.addCategoriaFiglia(categoriaLiteFiglio);
			}
		}
		logger.debug("--> Exit creaAlbero");
		return mapCategorie;
	}

	@Override
	public Categoria creaCategoria(Integer idCategoriaPadre) {
		logger.debug("--> Enter creaCategoria");

		Categoria categoriaNew = new Categoria();
		if (idCategoriaPadre != null) {
			Categoria categoriaPadre = new Categoria();
			categoriaPadre.setId(idCategoriaPadre);
			categoriaPadre = caricaCategoria(categoriaPadre, true);
			categoriaNew.setPadre(categoriaPadre);
			for (AttributoCategoria attributoCategoria : categoriaPadre.getAttributiCategoria()) {
				AttributoCategoria attributo = new AttributoCategoria(attributoCategoria);
				attributo.setCategoria(categoriaNew);
				categoriaNew.getAttributiCategoria().add(attributo);
			}
		}

		logger.debug("--> Exit creaCategoria");
		return categoriaNew;
	}

	/**
	 *
	 * @return codice dell'azienda loggata.
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

	/**
	 *
	 * @return utente loggato
	 */
	private JecPrincipal getPrincipal() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal());
	}

	@Override
	public Categoria salvaCategoria(Categoria categoria) {
		logger.debug("--> Enter salvaCategoria");

		List<AttributoCategoria> attributiCategorie = caricaAttributiCategoria(categoria);

		List<AttributoCategoria> attributiInseriti = new ArrayList<AttributoCategoria>();
		Map<AttributoCategoria, AttributoCategoria> attributiModificati = new HashMap<AttributoCategoria, AttributoCategoria>();
		List<AttributoCategoria> attributiCancellati = new ArrayList<AttributoCategoria>();

		Categoria categoriaOld = null;

		if (categoria.isNew()) {
			categoria.setCodiceAzienda(getAzienda());
			for (AttributoCategoria attributoCategoria : categoria.getAttributiCategoria()) {
				attributoCategoria.setCategoria(categoria);
			}
			attributiInseriti.addAll(categoria.getAttributiCategoria());
		} else {
			try {

				categoriaOld = panjeaDAO.load(Categoria.class, categoria.getId());
				categoriaOld.getAttributiCategoria().size();
				((Session) panjeaDAO.getEntityManager().getDelegate()).evict(categoriaOld);

				// carico tutti gli attributi della categoria padre e figlie che serviranno poi nell'aggiornamento degli
				// attributi articoli

			} catch (Exception e) {
				logger.error("--> errore durante il caricamento della categoria.", e);
				throw new RuntimeException("errore durante il caricamento della categoria.", e);
			}

			if (categoriaOld.getAttributiCategoria() != null) {
				attributiCancellati.addAll(categoriaOld.getAttributiCategoria());
				attributiCancellati.removeAll(categoria.getAttributiCategoria());
			}

			if (categoria.getAttributiCategoria() != null) {
				attributiInseriti.addAll(categoria.getAttributiCategoria());
				attributiInseriti.removeAll(categoriaOld.getAttributiCategoria());
			}
		}

		Categoria categoriaResult = null;

		try {
			categoriaResult = panjeaDAO.save(categoria);

			logger.debug("--> Inizializzo le collezioni");
			categoriaResult.getDescrizioniLinguaEstesa().size();
			if (categoriaResult.getAttributiCategoria() != null) {
				categoriaResult.getAttributiCategoria().size();
			}
			logger.debug("-->collezioni inizializzate");
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio della categoria", e);
			throw new RuntimeException("Errore durante il salvataggio della categoria", e);
		}

		if (categoriaOld != null) {
			for (AttributoCategoria attributoCategoria : categoriaResult.getAttributiCategoria()) {
				int idx = categoriaOld.getAttributiCategoria().indexOf(attributoCategoria);

				if (idx != -1) {
					if (attributoCategoria.getVersion() != 0
							&& categoriaOld.getAttributiCategoria().get(idx).getVersion().intValue() != attributoCategoria
							.getVersion().intValue()) {
						attributiModificati.put(attributoCategoria, categoriaOld.getAttributiCategoria().get(idx));
					}
				}
			}
		}

		// se ci sono attributi inseriti li prendo dalla categoria salvata per avere gli attributi salvati e non con id
		// nulli
		if (!attributiInseriti.isEmpty()) {
			List<AttributoCategoria> tmp = new ArrayList<AttributoCategoria>();
			tmp.addAll(categoriaResult.getAttributiCategoria());
			tmp.retainAll(attributiInseriti);
			attributiInseriti = tmp;
		}

		sincronizzaAttributiCategorie(categoriaResult, attributiInseriti, attributiModificati, attributiCancellati);
		sincronizzaAttributiArticoli(categoriaResult, attributiCategorie);

		logger.debug("--> Exit salvaCategoria");
		return categoriaResult;
	}

	@Override
	public void sincronizzaAttributiArticoli(Categoria categoria, List<AttributoCategoria> attributiOld) {

		CategoriaSincronizzazioneAttributiQueryBuilder queryBuilder = new CategoriaSincronizzazioneAttributiQueryBuilder();
		Query queryInserimento = panjeaDAO.getEntityManager().createNativeQuery(
				queryBuilder.getSqlInsertAttributiArticoliMancanti());
		Query queryDelete = panjeaDAO.getEntityManager().createNativeQuery(queryBuilder.getSqlDeleteAttributi());
		Query queryOverrideAll = panjeaDAO.getEntityManager().createNativeQuery(
				queryBuilder.getSQLOverrideAttributiArticoli());

		CategoriaLite categoriaPadre = new CategoriaLite(categoria);
		List<CategoriaLite> categorieDaSincronizzare = caricaCategorieFiglie(categoria.getId());
		categorieDaSincronizzare.add(categoriaPadre);

		// aggiorno gli attributi di ogni categoria
		for (CategoriaLite categoriaLite : categorieDaSincronizzare) {

			Categoria categoriaCorrente = panjeaDAO.loadLazy(Categoria.class, categoriaLite.getId());

			try {
				// cancello gli eventuali attributi articolo degli attributi cancellati nella categoria
				queryDelete.setParameter(1, categoriaCorrente.getId());
				panjeaDAO.executeQuery(queryDelete);

				for (AttributoCategoria attributoCategoria : categoriaCorrente.getAttributiCategoria()) {

					switch (attributoCategoria.getTipoAggiornamento()) {
					case TUTTI:
						queryOverrideAll.setParameter(1, categoriaCorrente.getId());
						queryOverrideAll.setParameter(2, attributoCategoria.getTipoAttributo().getId());
						panjeaDAO.executeQuery(queryOverrideAll);
						break;
					case UGUALI:
						int idx = attributiOld.indexOf(attributoCategoria);

						if (idx != -1) {
							AttributoCategoria attributoOld = attributiOld.get(idx);

							boolean insInRigaChanged = attributoCategoria.getInserimentoInRiga() != attributoOld
									.getInserimentoInRiga();
							boolean ordineChanged = !attributoCategoria.getOrdine().equals(attributoOld.getOrdine());
							boolean rigaChanged = !attributoCategoria.getRiga().equals(attributoOld.getRiga());
							boolean stampaChanged = attributoCategoria.getStampa() != attributoOld.getStampa();
							boolean formulaChanged = attributoCategoria.isFormulaChanged(attributoOld);
							boolean obbligatorioChanged = attributoCategoria.getObbligatorio() != attributoOld
									.getObbligatorio();
							boolean updatedChanged = attributoCategoria.getUpdatable() != attributoOld.getUpdatable();

							boolean ricalcolaInEvasioneChanged = attributoCategoria.getObbligatorio() != attributoOld
									.getRicalcolaInEvasione();

							if (updatedChanged) {
								Query queryOverride = panjeaDAO.getEntityManager().createNativeQuery(
										queryBuilder.getSQLOverrideUpdatableAttributiArticoli());
								queryOverride.setParameter(1, categoriaCorrente.getId());
								queryOverride.setParameter(2, attributoCategoria.getTipoAttributo().getId());
								queryOverride.setParameter(3, attributoOld.getRicalcolaInEvasione());
								panjeaDAO.executeQuery(queryOverride);
							}

							if (ricalcolaInEvasioneChanged) {
								Query queryOverride = panjeaDAO.getEntityManager().createNativeQuery(
										queryBuilder.getSQLRicalcolaInEvasioneAttributiArticoli());
								queryOverride.setParameter(1, categoriaCorrente.getId());
								queryOverride.setParameter(2, attributoCategoria.getTipoAttributo().getId());
								queryOverride.setParameter(3, attributoOld.getRicalcolaInEvasione());
								panjeaDAO.executeQuery(queryOverride);
							}

							if (insInRigaChanged) {
								Query queryOverride = panjeaDAO.getEntityManager().createNativeQuery(
										queryBuilder.getSQLOverrideInsInRigaAttributiArticoli());
								queryOverride.setParameter(1, categoriaCorrente.getId());
								queryOverride.setParameter(2, attributoCategoria.getTipoAttributo().getId());
								queryOverride.setParameter(3, attributoOld.getInserimentoInRiga());
								panjeaDAO.executeQuery(queryOverride);
							}

							if (ordineChanged) {
								Query queryOverride = panjeaDAO.getEntityManager().createNativeQuery(
										queryBuilder.getSQLOverrideOrdineAttributiArticoli());
								queryOverride.setParameter(1, categoriaCorrente.getId());
								queryOverride.setParameter(2, attributoCategoria.getTipoAttributo().getId());
								queryOverride.setParameter(3, attributoOld.getOrdine());
								panjeaDAO.executeQuery(queryOverride);
							}

							if (rigaChanged) {
								Query queryOverride = panjeaDAO.getEntityManager().createNativeQuery(
										queryBuilder.getSQLOverrideRigaAttributiArticoli());
								queryOverride.setParameter(1, categoriaCorrente.getId());
								queryOverride.setParameter(2, attributoCategoria.getTipoAttributo().getId());
								queryOverride.setParameter(3, attributoOld.getRiga());
								panjeaDAO.executeQuery(queryOverride);
							}

							if (stampaChanged) {
								Query queryOverride = panjeaDAO.getEntityManager().createNativeQuery(
										queryBuilder.getSQLOverrideStampaAttributiArticoli());
								queryOverride.setParameter(1, categoriaCorrente.getId());
								queryOverride.setParameter(2, attributoCategoria.getTipoAttributo().getId());
								queryOverride.setParameter(3, attributoOld.getStampa());
								panjeaDAO.executeQuery(queryOverride);
							}

							if (formulaChanged) {
								Query queryOverride = panjeaDAO.getEntityManager().createNativeQuery(
										queryBuilder.getSQLOverrideFormulaAttributiArticoli(attributoOld.getFormula()));
								queryOverride.setParameter(1, categoriaCorrente.getId());
								queryOverride.setParameter(2, attributoCategoria.getTipoAttributo().getId());
								if (attributoOld.getFormula() != null) {
									queryOverride.setParameter(3, attributoOld.getFormula());
								}
								panjeaDAO.executeQuery(queryOverride);
							}

							if (obbligatorioChanged) {
								Query queryOverride = panjeaDAO.getEntityManager().createNativeQuery(
										queryBuilder.getSQLOverrideObbligatorioAttributiArticoli());
								queryOverride.setParameter(1, categoriaCorrente.getId());
								queryOverride.setParameter(2, attributoCategoria.getTipoAttributo().getId());
								queryOverride.setParameter(3, attributoOld.getObbligatorio());
								panjeaDAO.executeQuery(queryOverride);
							}
						}

						break;
					case NESSUNO:
						// in questo caso non aggiorno o inserisco nessun attributo
						break;
					default:
						break;
					}
				}

				if (!categoriaCorrente.getAttributiCategoria().isEmpty()) {
					// inserisco gli attributi non presenti negli articoli della categoria
					queryInserimento.setParameter(1, categoriaCorrente.getId());
					queryInserimento.setParameter(2, getPrincipal().getUserName());
					panjeaDAO.executeQuery(queryInserimento);
				}
			} catch (Exception e) {
				logger.error("--> errore durante la sincronizzazione degli attributi articoli.", e);
				throw new RuntimeException("errore durante la sincronizzazione degli attributi articoli.", e);
			}

		}
	}

	/**
	 * Sincronizza gli attributi su tutte le categorie figlie.
	 *
	 * @param categoriaPadre
	 *            categoria padre
	 * @param attributiInseriti
	 *            attributi inseriti
	 * @param attributiModificati
	 *            attributi modificati
	 * @param attributiCancellati
	 *            attributi cancellati
	 */
	private void sincronizzaAttributiCategorie(Categoria categoriaPadre, List<AttributoCategoria> attributiInseriti,
			Map<AttributoCategoria, AttributoCategoria> attributiModificati,
			List<AttributoCategoria> attributiCancellati) {

		CategoriaSincronizzazioneAttributiQueryBuilder queryBuilder = new CategoriaSincronizzazioneAttributiQueryBuilder();

		List<CategoriaLite> categorieLiteFiglie = caricaCategorieFiglie(categoriaPadre.getId());

		// se non ci sono categorie figlie non faccio niente
		if (categorieLiteFiglie.isEmpty()) {
			return;
		}

		List<Categoria> categorieFiglie = new ArrayList<Categoria>();
		for (CategoriaLite categoriaLite : categorieLiteFiglie) {
			Categoria categoria = categoriaLite.createCategoria();
			categorieFiglie.add(categoria);
		}

		// se è stato cancellato un attributo lo cancello da tutte le sue categorie figlie
		if (!attributiCancellati.isEmpty()) {
			Query query = panjeaDAO
					.prepareQuery("delete AttributoCategoria ac where ac.tipoAttributo in (:paramTipiAttributo) and ac.categoria in (:paramCategorie)");
			List<TipoAttributo> tipiAttributo = new ArrayList<TipoAttributo>();
			for (AttributoCategoria attributoCategoria : attributiCancellati) {
				tipiAttributo.add(attributoCategoria.getTipoAttributo());
			}
			query.setParameter("paramTipiAttributo", tipiAttributo);
			query.setParameter("paramCategorie", categorieFiglie);
			try {
				panjeaDAO.executeQuery(query);
			} catch (Exception e) {
				logger.error("--> errore durante la cancellazione degli attributi delle categorie figlie.", e);
				throw new RuntimeException("errore durante la cancellazione degli attributi delle categorie figlie.", e);
			}
		}

		// modifica deli attributi esistenti
		for (Entry<AttributoCategoria, AttributoCategoria> entry : attributiModificati.entrySet()) {
			AttributoCategoria attributoNuovo = entry.getKey();
			AttributoCategoria attributoVecchio = entry.getValue();

			// solo gli attributi che considerano le sotto categorie verranno aggiornati
			if (attributoNuovo.isConsideraSottoCategorie()) {

				boolean valoreChanged = attributoNuovo.isValoreAttributoChanged(attributoVecchio);
				boolean insInRigaChanged = attributoNuovo.getInserimentoInRiga() != attributoVecchio
						.getInserimentoInRiga();
				boolean ordineChanged = !attributoNuovo.getOrdine().equals(attributoVecchio.getOrdine());
				boolean rigaChanged = !attributoNuovo.getRiga().equals(attributoVecchio.getRiga());
				boolean stampaChanged = attributoNuovo.getStampa() != attributoVecchio.getStampa();
				boolean consSottoCategoriaChanged = attributoNuovo.isConsideraSottoCategorie() != attributoVecchio
						.isConsideraSottoCategorie();
				boolean tipoAggChanged = attributoNuovo.getTipoAggiornamento() != attributoVecchio
						.getTipoAggiornamento();
				boolean formulaChanged = attributoNuovo.isFormulaChanged(attributoVecchio);
				boolean obbligatorioChanged = attributoNuovo.getObbligatorio() != attributoVecchio.getObbligatorio();
				boolean updatableChanged = attributoNuovo.getUpdatable() != attributoVecchio.getUpdatable();

				String queryString = queryBuilder.getHQLUpdateAttributiCategoria(valoreChanged, insInRigaChanged,
						ordineChanged, rigaChanged, stampaChanged, consSottoCategoriaChanged, tipoAggChanged,
						formulaChanged, obbligatorioChanged, updatableChanged);

				Query queryAggiornamento = panjeaDAO.prepareQuery(queryString);
				((QueryImpl) queryAggiornamento).getHibernateQuery().setProperties(attributoNuovo);
				queryAggiornamento.setParameter("categorie", categorieFiglie);
				try {
					panjeaDAO.executeQuery(queryAggiornamento);
				} catch (Exception e) {
					logger.error("--> errore durante l'aggiornamento dell'attributo "
							+ attributoNuovo.getTipoAttributo().getNome(), e);
					throw new RuntimeException("errore durante l'aggiornamento dell'attributo "
							+ attributoNuovo.getTipoAttributo().getNome(), e);
				}
			}
		}

		// Inserimento attributi nuovi
		for (AttributoCategoria attributoCategoria : attributiInseriti) {
			// solo gli attributi che considerano le sotto categorie verranno inseriti
			if (attributoCategoria.isConsideraSottoCategorie()) {
				for (Categoria categoria : categorieFiglie) {
					Query queryInserimento = panjeaDAO.getEntityManager().createNativeQuery(
							queryBuilder.getSqlInsertAttributiCategoria());
					queryInserimento.setParameter(1, categoria.getId());
					queryInserimento.setParameter(2, attributoCategoria.getId());
					try {
						panjeaDAO.executeQuery(queryInserimento);
					} catch (Exception e) {
						logger.error(
								"--> errore durante l'inserimento dell'attributo sulla categoria " + categoria.getId(),
								e);
						throw new RuntimeException("errore durante l'inserimento dell'attributo sulla categoria "
								+ categoria.getId(), e);
					}
				}
			}
		}
	}

	/**
	 * Metodo ricorsivo che carica le categorie figlie (e quelle ereditate) di categoriaLite in una lista.
	 *
	 * @param categoriaLite
	 *            categoria padre
	 * @return lista lista di {@link CategoriaLite}. le figlie di una categoria (anche i figli ereditati)
	 */
	private List<CategoriaLite> visiteCategoriaLite(CategoriaLite categoriaLite) {

		List<CategoriaLite> listCategorie = new ArrayList<CategoriaLite>();

		for (CategoriaLite categoriaFiglia : categoriaLite.getCategorieFiglie()) {
			listCategorie.add(categoriaFiglia);
			if (categoriaFiglia.getCategorieFiglie() != null) {
				listCategorie.addAll(visiteCategoriaLite(categoriaFiglia));
			}
		}

		return listCategorie;
	}
}
