package it.eurotn.panjea.contabilita.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.RapportoBancario;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.ContiBase;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.querybuilder.RicercaSottoContiSeachObjectQueryBuilder;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.TipoContoBaseEsistenteException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti.ETipoRicercaSottoConto;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Manager per la gestione dei Piano dei Conti roles: cancellaPianoConti, visualizzaPianoConti, modificaPianoConti.
 * 
 * @author adriano
 * @version 1.0, 31/ago/07
 */
@Stateless(name = "Panjea.PianoContiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PianoContiManager")
public class PianoContiManagerBean implements PianoContiManager {

	private static Logger logger = Logger.getLogger(PianoContiManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	/**
	 * metodo per cancellare un conto.
	 * 
	 * @param conto
	 *            {@link Conto}
	 */
	@Override
	public void cancellaConto(Conto conto) {
		logger.debug("--> Enter cancellaConto");
		try {
			panjeaDAO.delete(conto);
		} catch (Exception e) {
			logger.error("--> errore durante la cancellazione del conto " + conto, e);
			throw new RuntimeException("Impossibile eliminare il conto ", e);
		}
		logger.debug("--> Exit cancellaConto");
	}

	/**
	 * cancella un conto base.
	 * 
	 * @param contoBase
	 *            {@link ContoBase}
	 */
	@Override
	public void cancellaContoBase(ContoBase contoBase) {
		logger.debug("--> Enter cancellaContoBase");

		try {
			panjeaDAO.delete(contoBase);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione del contobase. ", e);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit cancellaContoBase");

	}

	/**
	 * cancella mastro.
	 * 
	 * @param mastro
	 *            {@link Mastro}
	 */
	@Override
	public void cancellaMastro(Mastro mastro) {
		logger.debug("--> Enter cancellaMastro");
		try {
			panjeaDAO.delete(mastro);
		} catch (Exception e) {
			logger.error("--> errore durante la cancellazione del mastro " + mastro, e);
			throw new RuntimeException("Impossibile eliminare il mastro", e);
		}
		logger.debug("--> Exit cancellaMastro");
	}

	/**
	 * cancella sottoconto.
	 * 
	 * @param sottoConto
	 *            {@link SottoConto}
	 */
	@Override
	public void cancellaSottoConto(SottoConto sottoConto) {
		logger.debug("--> Enter cancellaSottoConto");
		try {
			panjeaDAO.delete(sottoConto);
		} catch (Exception e) {
			logger.error("--> errore durante la cancellazione del sottoconto " + sottoConto, e);
			throw new RuntimeException("Impossibile eliminare il sottoconto", e);
		}
		logger.debug("--> Exit cancellaSottoConto");

	}

	/**
	 * @return lista {@link ContoBase}
	 * @throws ContabilitaException
	 *             ContabilitaException
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ContoBase> caricaContiBase() throws ContabilitaException {
		logger.debug("--> Enter caricaContiBase");
		List<ContoBase> list = new ArrayList<ContoBase>();
		Query query = panjeaDAO.prepareNamedQuery("ContoBase.caricaAll");
		query.setParameter("paramCodiceAzienda", getAzienda());
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento dei conti base", e);
			throw new ContabilitaException("Errore durante il caricamento dei conti base.", e);
		}

		logger.debug("--> Exit caricaContiBase");
		return list;
	}

	/**
	 * @param idConto
	 *            id del conto a caricare.
	 * @return {@link Conto}
	 * @throws ContabilitaException
	 *             ContabilitaException
	 */
	@Override
	public Conto caricaConto(Integer idConto) throws ContabilitaException {
		logger.debug("--> Enter caricaConto");
		Conto conto;
		try {
			conto = panjeaDAO.load(Conto.class, idConto);
		} catch (ObjectNotFoundException e) {
			logger.error("--> Impossibile recuperare il conto con id = " + idConto, e);
			throw new ContabilitaException("Impossibile recuperare il conto con id = " + idConto, e);
		}
		logger.debug("--> Exit caricaConto");
		return conto;
	}

	@Override
	public SottoConto caricaContoAnticipoFatturePerRapportoBancario(RapportoBancarioAzienda rapportoBancario) {
		return caricaContoSceltoPerRapportoBancario(rapportoBancario, "sottoContoAnticipoFatture");
	}

	/**
	 * @param descrizione
	 *            del conto a caricare
	 * @return {@link ContoBase}
	 * @throws ContabilitaException
	 *             ContabilitaException
	 */
	@Override
	public ContoBase caricaContoBase(String descrizione) throws ContabilitaException {
		logger.debug("--> Enter caricaContoBase");

		Query query = panjeaDAO.prepareNamedQuery("ContoBase.caricaByDescrizione");
		query.setParameter("paramCodiceAzienda", getAzienda());
		query.setParameter("paramDescrizione", descrizione);

		ContoBase contoBase = null;

		try {
			contoBase = (ContoBase) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento del conto base", e);
			throw new ContabilitaException("Errore durante il caricamento del conto base", e);
		}

		logger.debug("--> Exit caricaContoBase");
		return contoBase;
	}

	@Override
	public SottoConto caricaContoEffettiAttiviPerRapportoBancario(RapportoBancarioAzienda rapportoBancario) {
		return caricaContoSceltoPerRapportoBancario(rapportoBancario, "sottoContoEffettiAttivi");
	}

	@Override
	public SottoConto caricaContoPerRapportoBancario(RapportoBancario rapportoBancario)
			throws ContoRapportoBancarioAssenteException {
		logger.debug("--> Enter caricaContoPerRapportoBancario");
		Query query = panjeaDAO.prepareNamedQuery("RapportoBancarioAzienda.caricaSottoContoByRapporto");
		query.setParameter("paramIdRapportoBancario", rapportoBancario.getId());
		SottoConto sottoConto;
		try {
			sottoConto = (SottoConto) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.warn("--> errore ObjectNotFoundException in caricaContoPerRapportoBancario");
			// imposto la sessione per eseguire il rollback dato che eseguo il
			// salvataggio di un documento di pagamento
			// e se passa di qui non devo renderlo persistente
			context.setRollbackOnly();
			throw new ContoRapportoBancarioAssenteException(rapportoBancario);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaContoPerRapportoBancario", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaContoPerRapportoBancario");
		return sottoConto;
	}

	@Override
	public SottoConto caricaContoPerTipoContoBase(ETipoContoBase tipoContoBase) throws ContabilitaException,
			ContiBaseException {
		logger.debug("--> Enter caricaContoPerTipoContoBase");
		try {
			ContiBase map = caricaTipiContoBase();
			if (!map.containsKey(tipoContoBase)) {
				logger.error("--> errore in caricaContoPerTipoContoBase: tipoContoBase assente ");
				throw new ContiBaseException(tipoContoBase);
			}
			logger.debug("--> Exit caricaContoPerTipoContoBase");
			return map.get(tipoContoBase);
		} catch (ContabilitaException e) {
			logger.error("--> errore ContabilitaException in caricaContoPerTipoContoBase", e);
			throw e;
		}
	}

	private SottoConto caricaContoSceltoPerRapportoBancario(RapportoBancarioAzienda rapportoBancario, String fieldScelto) {
		StringBuilder sb = new StringBuilder("select ");
		sb = sb.append(fieldScelto);
		sb = sb.append(" from RapportoBancarioAzienda rb where rb.id = :paramIdRapportoBancario ");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramIdRapportoBancario", rapportoBancario.getId());
		SottoConto sottoConto = null;
		try {
			sottoConto = (SottoConto) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("--> nessun sottoconto effetti attivi impostato per il rapporto bancario");
			}
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaContoPerRapportoBancario", e);
			throw new RuntimeException(e);
		}
		return sottoConto;
	}

	/**
	 * 
	 * @return lista {@link Mastro}
	 * @throws ContabilitaException
	 *             Stardard Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Mastro> caricaMastri() throws ContabilitaException {
		logger.debug("--> Enter caricaMastri");
		List<Mastro> listMastri;
		Query query = panjeaDAO.prepareNamedQuery("Mastro.caricaTuttiMastri");
		query.setParameter("codiceAzienda", getAzienda());
		try {
			listMastri = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore impossibile recuperare la list di Mastro ", e);
			throw new ContabilitaException(e);
		}
		logger.debug("--> Numero risultati query " + listMastri.size());

		logger.debug("--> Exit caricaMastri");
		return listMastri;
	}

	/**
	 * carica un mastro perndendo il codice di questo.
	 * 
	 * @param idMastro
	 *            id del mastro a caricare
	 * @return {@link Mastro} result.
	 * @throws ContabilitaException
	 *             Stardard Exception
	 */
	@Override
	public Mastro caricaMastro(Integer idMastro) throws ContabilitaException {
		logger.debug("--> Enter caricaMastro");
		Mastro mastro;
		try {
			mastro = panjeaDAO.load(Mastro.class, idMastro);
		} catch (ObjectNotFoundException e) {
			logger.error("--> Impossibile recuperare il mastro con id = " + idMastro, e);
			throw new ContabilitaException("Impossibile recuperare il mastro con id = " + idMastro, e);
		}
		logger.debug("--> Exit caricaMastro");
		return mastro;
	}

	/**
	 * carica il mastro prendendo ul codice della azienda e il codice del mastro.
	 * 
	 * @param codiceMastro
	 *            codice del mastro per la ricerca
	 * @param codiceAzienda
	 *            codice della azienda ricerca
	 * @return {@link Mastro} trovato.
	 */
	@Override
	public Mastro caricaMastroByCodice(String codiceMastro, String codiceAzienda) {
		logger.debug("-->Enter caricaMastroByCodice con parametro codiceMastro " + codiceMastro + " codiceAzienda "
				+ codiceAzienda);
		Query queryMastro = panjeaDAO.prepareNamedQuery("Mastro.caricaMastroByCodice");
		queryMastro.setParameter("codiceAzienda", codiceAzienda);
		queryMastro.setParameter("codiceMastro", codiceMastro);
		Mastro mastro = null;
		try {
			mastro = (Mastro) queryMastro.getSingleResult();
		} catch (NoResultException e) {
			logger.debug("-->non ho trovato il mastro con codice " + codiceMastro);
		}
		logger.debug("-->Exit caricaMastroByCodice con mastro " + mastro);
		return mastro;
	}

	/**
	 * carica il sotttoconto prendendo imerico del sottoconto.
	 * 
	 * @param idSottoConto
	 *            del {@link SottoConto} a ricercare
	 * @return {@link SottoConto} trovato.
	 * @throws ContabilitaException
	 *             Stardard Exception
	 */
	@Override
	public SottoConto caricaSottoConto(Integer idSottoConto) throws ContabilitaException {
		logger.debug("--> Enter caricaSottoConto");
		SottoConto sottoConto;
		try {
			sottoConto = panjeaDAO.load(SottoConto.class, idSottoConto);
		} catch (ObjectNotFoundException e) {
			logger.error("--> Impossibile recuperare il sottoconto con id = " + idSottoConto, e);
			throw new ContabilitaException("Impossibile recuperare il sottoconto con id = " + idSottoConto, e);
		}
		logger.debug("--> Exit caricaSottoConto");
		return sottoConto;
	}

	/**
	 * carica il sottoconto perndendo il codice formatato(000000).
	 * 
	 * @param codiceSottoConto
	 *            codice del sottoconto a ricercare
	 * @return {@link SottoConto}
	 * @throws ContabilitaException
	 *             Stardard Exception
	 */
	@Override
	public SottoConto caricaSottoConto(String codiceSottoConto) throws ContabilitaException {
		logger.debug("--> Enter caricaSottoConto");

		String[] codici = codiceSottoConto.split("\\.");

		Query query = panjeaDAO.prepareNamedQuery("SottoConto.caricaByCodici");
		query.setParameter("codiceAzienda", getAzienda());
		query.setParameter("codiceMastro", codici[0]);
		query.setParameter("codiceConto", codici[1]);
		query.setParameter("codiceSottoConto", codici[2]);

		SottoConto sottoConto = null;

		try {
			sottoConto = (SottoConto) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento del sottoconto " + codiceSottoConto, e);
			throw new ContabilitaException("Errore durante il caricamento del sottoconto.", e);
		}

		logger.debug("--> Exit caricaSottoConto");
		return sottoConto;
	}

	/**
	 * @param codiceMastro
	 *            del mastro per la ricerca
	 * @param codiceConto
	 *            del conto per la ricerca
	 * @param codiceImportazioneSottoConto
	 *            codice d'importazione da ricercare
	 * @return {@link SottoConto} trovato.
	 * @throws ContabilitaException
	 *             Stardard Exception
	 */
	@Override
	public SottoConto caricaSottoContoPerCodiceImportazione(String codiceMastro, String codiceConto,
			String codiceImportazioneSottoConto) throws ContabilitaException {
		try {
			logger.debug("--> Enter caricaSottoContoPerCodiceImportazione");
			StringBuffer stringBuffer = new StringBuffer(
					" select distinct sc from SottoConto sc join sc.codiciImportazione i ");
			stringBuffer
					.append(" where sc.conto.mastro.codiceAzienda = :paramCodiceAzienda and sc.conto.mastro.codice = :paramCodiceMastro and ");
			stringBuffer
					.append(" sc.conto.codice = :paramCodiceConto and i.codiceImportazione = :paramCodiceImportazione ");

			Query query = panjeaDAO.prepareQuery(stringBuffer.toString());
			query.setParameter("paramCodiceAzienda", getAzienda());
			query.setParameter("paramCodiceMastro", codiceMastro);
			query.setParameter("paramCodiceConto", codiceConto);
			query.setParameter("paramCodiceImportazione", codiceImportazioneSottoConto);
			Object object = panjeaDAO.getSingleResult(query);
			SottoConto sottoConto = (SottoConto) object;
			logger.debug("--> Exit caricaSottoContoPerCodiceImportazione");
			return sottoConto;
		} catch (ObjectNotFoundException ex) {
			logger.warn("--> nessun risultato ottenuto per Codice Importazione ");
			return null;
		} catch (DAOException ex) {
			logger.error("--> errore, impossibile caricare SottoConto per Codice Importazione ", ex);
			throw new ContabilitaException(ex);
		}

	}

	@Override
	public SottoConto caricaSottoContoPerEntita(EntitaLite entitaLite) throws ContabilitaException {
		logger.debug("--> Enter caricaSottoContoPerEntita");
		SottotipoConto sottotipoConto = null;
		if (entitaLite instanceof ClienteLite) {
			sottotipoConto = SottotipoConto.CLIENTE;
		} else if (entitaLite instanceof FornitoreLite) {
			sottotipoConto = SottotipoConto.FORNITORE;
		} else {
			throw new ContabilitaException("impossibile definire il SottotipoConto per entita");
		}
		SottoConto sottoConto;
		sottoConto = caricaSottoContoPerEntita(sottotipoConto, entitaLite.getCodice());
		logger.debug("--> Exit caricaSottoContoPerEntita");
		return sottoConto;
	}

	/**
	 * @param sottotipoConto
	 *            {@link SottoConto}
	 * @param codiceEntita
	 *            codice della la entita per cui caricare il {@link SottoConto}
	 * @return {@link SottoConto}
	 * @throws ContabilitaException
	 *             ContabilitaException
	 */
	@Override
	public SottoConto caricaSottoContoPerEntita(SottotipoConto sottotipoConto, Integer codiceEntita)
			throws ContabilitaException {
		logger.debug("--> Enter caricaSottoContoPerEntita");
		Query query = panjeaDAO.prepareNamedQuery("SottoConto.caricaByEntita");
		query.setParameter("paramCodiceAzienda", getAzienda());
		String codiceSottoConto = getCodiceSottoConto(codiceEntita);

		query.setParameter("paramCodice", codiceSottoConto);
		query.setParameter("paramSottotipoConto", sottotipoConto);
		SottoConto sottoConto = null;
		try {
			sottoConto = (SottoConto) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			return new SottoConto();
		} catch (DAOException e) {
			logger.error("--> errore in ricerca SottoConto per Entita' ", e);
			throw new ContabilitaException("Errore in ricerca SottoConto per Entita' ", e);
		}
		logger.debug("--> Exit caricaSottoContoPerEntita");
		return sottoConto;
	}

	/**
	 * @return {@link ContiBase}
	 * @throws ContabilitaException
	 *             ContabilitaException
	 */
	@Override
	public ContiBase caricaTipiContoBase() throws ContabilitaException {
		logger.debug("--> Enter caricaTipiContoBase");
		List<ContoBase> contiBaseList = caricaContiBase();
		logger.debug("--> Exit caricaTipiContoBase");
		return new ContiBase(contiBaseList);
	}

	@Override
	public SottoConto creaSottoContoPerEntita(Entita entita) throws ContabilitaException {
		logger.debug("--> Enter creaSottoContoPerEntita");
		/* controlla l'esistenza di un diverso sottoconto */
		SottotipoConto sottotipoConto = Conto.getSottoTipoConto(entita);
		SottoConto sottoContoPresente = caricaSottoContoPerEntita(sottotipoConto, entita.getCodice());
		if (sottoContoPresente.getId() != null) {
			/* disabilito sottoConto esistente */
			sottoContoPresente.setAbilitato(false);
			salvaSottoConto(sottoContoPresente);
		}

		SottoConto sottoConto = null;
		// ricerco i conti cliente o fornitore a seconda del sottotipoconto
		List<Conto> conti = ricercaConti("", sottotipoConto);
		if (conti != null && conti.size() > 0) {
			Conto conto = conti.get(0);
			// crea il sottocoonto per l'entita'
			sottoConto = new SottoConto();
			sottoConto.setConto(conto);
			sottoConto.setAbilitato(entita.isAbilitato());
			sottoConto.setCodice(getCodiceSottoConto(entita.getCodice()));
			sottoConto.setDescrizione(entita.getAnagrafica().getDenominazione());
			sottoConto = salvaSottoConto(sottoConto);
		}
		logger.debug("--> Exit creaSottoContoPerEntita");
		return sottoConto;
	}

	/**
	 * 
	 * @return nome della azienda
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	/**
	 * 
	 * @param codice
	 *            codice autogenerato.
	 * @return codice formatato.
	 */
	private String getCodiceSottoConto(Integer codice) {
		return new DecimalFormat("000000").format(codice.longValue());
	}

	/**
	 * @return lista {@link Conto}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Conto> ricercaConti() {
		logger.debug("--> Enter ricercaConti");
		List<Conto> conti = new ArrayList<Conto>();
		Query query = panjeaDAO.prepareNamedQuery("Conto.caricaAll");
		query.setParameter("paramCodiceAzienda", getAzienda());
		try {
			conti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.warn("--> nessun risultato trovato ");
		}
		logger.debug("--> Exit ricercaConti");
		return conti;
	}

	/**
	 * @param codiceConto
	 *            codice del conto a cercare
	 * @param sottoTipoConto
	 *            {@link SottotipoConto}
	 * @return lista {@link Conto}
	 * @throws ContabilitaException
	 *             ContabilitaException
	 */
	@Override
	public List<Conto> ricercaConti(String codiceConto, SottotipoConto sottoTipoConto) throws ContabilitaException {
		logger.debug("--> Enter ricercaConti");
		String codMastro = null;
		String codConto = null;
		if (codiceConto != null && !codiceConto.isEmpty()) {
			String[] strings = codiceConto.split("\\.");
			for (int i = 0; i < strings.length; i++) {
				switch (i) {
				case 0:
					codMastro = strings[i].trim();
					break;
				case 1:
					codConto = strings[i].trim();
					break;
				default:
					break;
				}
			}
		}
		List<Conto> listConti = ricercaConti(codMastro, codConto, sottoTipoConto);
		logger.debug("--> Exit ricercaSottoConti");
		return listConti;
	}

	/**
	 * @param codiceMastro
	 *            codice mastro del conto
	 * @param codiceConto
	 *            codice del conto
	 * @param sottotipoConto
	 *            {@link SottotipoConto}
	 * @return lista {@link Conto}
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Conto> ricercaConti(String codiceMastro, String codiceConto, SottotipoConto sottotipoConto) {
		logger.debug("--> Enter ricercaConti");
		Map<String, Object> valueParametri = new TreeMap<String, Object>();
		StringBuffer queryHQL = new StringBuffer(" select c from Conto c ");
		StringBuffer whereHQL = new StringBuffer(" where c.codiceAzienda = :paramCodiceAzienda ");
		valueParametri.put("paramCodiceAzienda", getAzienda());

		whereHQL.append(" and c.codice <> :paramDefaultCodice ");
		valueParametri.put("paramDefaultCodice", Conto.DEFAULT_CODICE);

		if (sottotipoConto != null) {
			whereHQL.append(" and c.sottotipoConto = :paramSottoTipoConto ");
			valueParametri.put("paramSottoTipoConto", sottotipoConto);
		}
		if ((codiceMastro != null) && (!codiceMastro.equals(""))) {
			// queryHQL.append(" inner join sc.conto c inner join c.mastro m "
			// );
			whereHQL.append(" and c.mastro.codice like :paramCodiceMastro ");
			valueParametri.put("paramCodiceMastro", "%" + codiceMastro + "%");
			if ((codiceConto != null) && (!codiceConto.equals(""))) {
				whereHQL.append(" and c.codice like :paramCodiceConto ");
				valueParametri.put("paramCodiceConto", "%" + codiceConto + "%");
			}
		}
		Query query = panjeaDAO.prepareQuery(queryHQL.toString() + whereHQL.toString());
		Set<String> set = valueParametri.keySet();
		for (String key : set) {
			query.setParameter(key, valueParametri.get(key));
		}
		List<Conto> conti;
		try {
			conti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore in ricercaConti", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit ricercaSottoConti ");
		return conti;
	}

	/**
	 * @return lista {@link SottoConto}
	 * @throws ContabilitaException
	 *             Stardard Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SottoConto> ricercaSottoConti() throws ContabilitaException {
		logger.debug("--> Enter ricercaSottoConti");
		List<SottoConto> list = new ArrayList<SottoConto>();
		Query query = panjeaDAO.prepareNamedQuery("SottoConto.caricaAll");
		query.setParameter("paramCodiceAzienda", getAzienda());
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore in ricerca SottoConto", e);
			throw new ContabilitaException("Impossibile ottenere la lista di SottoConto ", e);
		}
		return list;
	}

	/**
	 * @param parametriRicercaSottoConti
	 *            per effettuarea la ricerca {@link ParametriRicercaSottoConti}
	 * @return lista {@link SottoConto}
	 * @throws ContabilitaException
	 *             ContabilitaException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SottoConto> ricercaSottoConti(ParametriRicercaSottoConti parametriRicercaSottoConti)
			throws ContabilitaException {
		logger.debug("--> Enter ricercaSottoConti");
		Map<String, Object> valueParametri = new TreeMap<String, Object>();
		StringBuffer queryHQL = new StringBuffer(" select sc from SottoConto sc ");
		StringBuffer whereHQL = new StringBuffer(" where sc.codiceAzienda = :paramCodiceAzienda ");
		valueParametri.put("paramCodiceAzienda", getAzienda());

		if (parametriRicercaSottoConti.getAbilitato() != null) {
			whereHQL.append(" and sc.abilitato = :paramAbilitato ");
			valueParametri.put("paramAbilitato", parametriRicercaSottoConti.getAbilitato());
		}

		String orderBy = " order by sc.descrizione";
		if (parametriRicercaSottoConti.getTipoRicercaSottoConto() == ETipoRicercaSottoConto.CODICE) {
			orderBy = " order by sc.conto.mastro.codice, sc.conto.codice,sc.codice";
		}

		whereHQL.append(" and sc.codice <> :paramDefaultCodice ");
		valueParametri.put("paramDefaultCodice", SottoConto.DEFAULT_CODICE);
		if (parametriRicercaSottoConti.getValoreDaRicercare() != null) {
			switch (parametriRicercaSottoConti.getTipoRicercaSottoConto()) {
			case CODICE:
				/* separazione codiceSottoConto */
				String[] strings = parametriRicercaSottoConti.getValoreDaRicercare().split("\\.");
				String codiceMastro = null;
				String codiceConto = null;
				String codiceSottoConto2 = null;
				for (int i = 0; i < strings.length; i++) {
					switch (i) {
					case 0:
						codiceMastro = strings[i].trim();
						break;
					case 1:
						codiceConto = strings[i].trim();
						break;
					case 2:
						codiceSottoConto2 = strings[i].trim();
						break;
					default:
						break;
					}
				}

				if ((codiceMastro != null) && (!codiceMastro.equals(""))) {
					whereHQL.append(" and sc.conto.mastro.codice like :paramCodiceMastro ");
					valueParametri.put("paramCodiceMastro", "%" + codiceMastro + "%");
					if ((codiceConto != null) && (!"".equals(codiceConto))) {
						whereHQL.append(" and sc.conto.codice like :paramCodiceConto ");
						valueParametri.put("paramCodiceConto", "%" + codiceConto + "%");
						if ((codiceSottoConto2 != null) && (!"".equals(codiceSottoConto2))) {
							whereHQL.append(" and sc.codice like :paramCodiceSottoConto ");
							valueParametri.put("paramCodiceSottoConto", "%" + codiceSottoConto2 + "%");
						}
					}
				}

				break;
			case DESCRIZIONE:
				if (!parametriRicercaSottoConti.getValoreDaRicercare().isEmpty()) {
					whereHQL.append(" and ( sc.descrizione like :paramDescrizioneSottoConto ) ");
					String paramDescrizione = "%" + parametriRicercaSottoConti.getValoreDaRicercare() + "%";
					valueParametri.put("paramDescrizioneSottoConto", paramDescrizione);
				}
				break;
			default:
				logger.error("--> errore, Impossibile definire il tipo di ricerca del sottoconto ");
				throw new ContabilitaException("Impossibile definire il tipo di ricerca del sottoconto ");
			}
		}

		if (parametriRicercaSottoConti.getSottotipoConto() != null) {
			switch (parametriRicercaSottoConti.getSottotipoConto()) {
			case CLIENTE:
			case FORNITORE:
				whereHQL.append(" and sc.conto.sottotipoConto = :paramSottotipoConto ");
				valueParametri.put("paramSottotipoConto", parametriRicercaSottoConti.getSottotipoConto());
				break;
			default:
				break;
			}
		}
		Query query = panjeaDAO.prepareQuery(queryHQL.toString() + whereHQL.toString() + orderBy.toString());
		Set<String> set = valueParametri.keySet();
		for (String key : set) {
			query.setParameter(key, valueParametri.get(key));
		}
		List<SottoConto> sottoConti;
		try {
			sottoConti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile eseguire la ricerca ", e);
			throw new ContabilitaException("Impossibile eseguire la ricerca ", e);
		}

		return sottoConti;
	}

	/**
	 * @param codiceSottoConto
	 *            codice del sottoconto a cercare
	 * @return lista {@link SottoConto}
	 * @throws ContabilitaException
	 *             Stardard Exception
	 */
	@Override
	public List<SottoConto> ricercaSottoConti(String codiceSottoConto) throws ContabilitaException {
		logger.debug("--> Enter caricaSottoConti");
		/* separazione codiceSottoConto */
		String[] strings = codiceSottoConto.split("\\.");
		String codiceMastro = null;
		String codiceConto = null;
		String codiceSottoConto2 = null;
		for (int i = 0; i < strings.length; i++) {
			switch (i) {
			case 0:
				codiceMastro = strings[i].trim();
				break;
			case 1:
				codiceConto = strings[i].trim();
				break;
			case 2:
				codiceSottoConto2 = strings[i].trim();
				break;
			default:
				break;
			}
		}
		List<SottoConto> listSottoConti = ricercaSottoConti(codiceMastro, codiceConto, codiceSottoConto2);
		logger.debug("--> Exit ricercaSottoConti");
		return listSottoConti;
	}

	/**
	 * @param codiceMastro
	 *            codice del mastro per la ricerca
	 * @param codiceConto
	 *            codice del conto per la ricerca
	 * @param codiceSottoConto
	 *            codice del sottoconto per la ricerca
	 * @return lista {@link SottoConto}
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SottoConto> ricercaSottoConti(String codiceMastro, String codiceConto, String codiceSottoConto) {
		logger.debug("--> Enter ricercaSottoConti");
		Map<String, Object> valueParametri = new TreeMap<String, Object>();
		StringBuffer queryHQL = new StringBuffer(" select sc from SottoConto sc ");
		StringBuffer whereHQL = new StringBuffer(" where sc.codiceAzienda = :paramCodiceAzienda ");
		valueParametri.put("paramCodiceAzienda", getAzienda());

		whereHQL.append(" and sc.codice <> :paramDefaultCodice ");
		valueParametri.put("paramDefaultCodice", SottoConto.DEFAULT_CODICE);

		if ((codiceMastro != null) && (!codiceMastro.equals(""))) {
			// queryHQL.append(" inner join sc.conto c inner join c.mastro m "
			// );
			whereHQL.append(" and sc.conto.mastro.codice like :paramCodiceMastro ");
			valueParametri.put("paramCodiceMastro", "%" + codiceMastro + "%");
			if ((codiceConto != null) && (!codiceConto.equals(""))) {
				whereHQL.append(" and sc.conto.codice like :paramCodiceConto ");
				valueParametri.put("paramCodiceConto", "%" + codiceConto + "%");
				if ((codiceSottoConto != null) && (!codiceSottoConto.equals(""))) {
					whereHQL.append(" and sc.codice like :paramCodiceSottoConto ");
					valueParametri.put("paramCodiceSottoConto", "%" + codiceSottoConto + "%");
				}
			}
		}
		Query query = panjeaDAO.prepareQuery(queryHQL.toString() + whereHQL.toString());
		Set<String> set = valueParametri.keySet();
		for (String key : set) {
			query.setParameter(key, valueParametri.get(key));
		}
		List<SottoConto> sottoConti;
		try {
			sottoConti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore in ricercaSottoConti", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit ricercaSottoConti ");
		return sottoConti;
	}

	/**
	 * @return lista {@link SottoConto}
	 * @throws ContabilitaException
	 *             Stardard Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SottoConto> ricercaSottoContiOrdinati() throws ContabilitaException {
		logger.debug("--> Enter ricercaSottoContiOrdinati");
		List<SottoConto> list = new ArrayList<SottoConto>();
		Query query = panjeaDAO.prepareNamedQuery("SottoConto.caricaAllOrdinati");
		query.setParameter("paramCodiceAzienda", getAzienda());
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore in ricerca SottoConto", e);
			throw new ContabilitaException("Impossibile ottenere la lista di SottoConto ", e);
		}
		logger.debug("--> Exit ricercaSottoContiOrdinati");
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SottoConto> ricercaSottoContiSearchObject(ParametriRicercaSottoConti parametriRicercaSottoConti) {
		logger.debug("--> Enter ricercaSottoContiSearchObject");
		List<SottoConto> list = new ArrayList<SottoConto>();

		RicercaSottoContiSeachObjectQueryBuilder queryBuilder = new RicercaSottoContiSeachObjectQueryBuilder();
		String queryString = queryBuilder.createQuery(parametriRicercaSottoConti, getAzienda());

		Query query = panjeaDAO.getEntityManager().createNativeQuery(queryString);
		((org.hibernate.ejb.HibernateQuery) query).getHibernateQuery().setResultTransformer(
				Transformers.aliasToBean(SottoConto.class));
		SQLQuery sqlQuery = ((SQLQuery) ((QueryImpl) query).getHibernateQuery());
		sqlQuery.addScalar("id");
		sqlQuery.addScalar("sottoContoCodice");
		sqlQuery.addScalar("descrizione");
		sqlQuery.addScalar("version");

		try {
			list = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante la ricerca dei sottoconti.", e);
			throw new RuntimeException("errore durante la ricerca dei sottoconti.", e);
		}

		logger.debug("--> Exit ricercaSottoContiSearchObject");
		return list;
	}

	/**
	 * @param conto
	 *            {@link Conto} da salvare
	 * @return {@link Conto} salvato
	 */
	@Override
	public Conto salvaConto(Conto conto) {
		logger.debug("--> Enter salvaConto");
		Conto contoSalvato;

		// se e' un nuovo conto vado a creare un sottoconto con codice 000000
		if (conto.isNew()) {
			conto.setCodiceAzienda(getAzienda());

			SottoConto sottoConto = new SottoConto();
			sottoConto.setCodice("000000");
			sottoConto.setCodiceAzienda(getAzienda());

			conto.setSottoConti(new HashSet<SottoConto>());
			conto.getSottoConti().add(sottoConto);
			sottoConto.setConto(conto);
		}

		try {
			contoSalvato = panjeaDAO.save(conto);
		} catch (Exception e) {
			logger.error("--> errore durante il salvataggio del conto ", e);
			throw new RuntimeException("Impossibile salvare il conto ", e);
		}

		boolean soggettoCentroCosto = conto.isSoggettoCentroCosto();
		CentroCosto centroCosto = conto.getCentroCosto();

		String hql = "update SottoConto s set s.soggettoCentroCosto=:paramSoggettoCentroCosto,s.centroCosto=:paramCentroCosto where s.codice!='000000' and s.conto.id = :paramContoId";
		Query query = panjeaDAO.prepareQuery(hql);
		query.setParameter("paramSoggettoCentroCosto", soggettoCentroCosto);
		query.setParameter("paramCentroCosto", centroCosto);
		query.setParameter("paramContoId", contoSalvato.getId());
		try {
			panjeaDAO.executeQuery(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante l'aggiornamento dei sottoconti per il centro costo", e);
			throw new RuntimeException(e);
		}
		contoSalvato.setSoggettoCentroCosto(conto.isSoggettoCentroCosto());
		contoSalvato.setCentroCosto(conto.getCentroCosto());
		logger.debug("--> Exit salvaConto");

		return contoSalvato;
	}

	/**
	 * @param contoBase
	 *            da salvare
	 * @return {@link ContiBase} salvato
	 * @throws ContabilitaException
	 *             Stardard Exception
	 * @throws TipoContoBaseEsistenteException
	 *             exception per conto base già esistente
	 */
	@Override
	public ContoBase salvaContoBase(ContoBase contoBase) throws TipoContoBaseEsistenteException, ContabilitaException {
		logger.debug("--> Enter salvaContoBase");

		// se il conto non e' di tipo avere e il tipo esiste gia'
		// sollevo un'eccezione
		if (contoBase.getTipoContoBase().compareTo(ETipoContoBase.ALTRO) != 0
				&& verificaPresenzaTipoContoBase(contoBase)) {
			logger.error("--> Tipo conto base gia' presente.");
			throw new TipoContoBaseEsistenteException("Tipo conto base gia' presente.");
		}

		ContoBase contoBase2;

		// se il conto che devo salvare non ha ancora settato il codice azienda
		// setto quello del principal
		if (contoBase.getCodiceAzienda() == null) {
			contoBase.setCodiceAzienda(getAzienda());
		}

		try {
			contoBase2 = panjeaDAO.save(contoBase);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del conto base. ", e);
			throw new RuntimeException("Errore durante il salvataggio del conto base.", e);
		}

		logger.debug("--> Exit salvaContoBase");
		return contoBase2;
	}

	/**
	 * @param mastro
	 *            da salvare
	 * @return {@link Mastro} salvato
	 * 
	 */
	@Override
	public Mastro salvaMastro(Mastro mastro) {
		logger.debug("--> Enter salvaMastro");
		Mastro mastroSalvato;

		mastro.setCodiceAzienda(getAzienda());

		// se e' un nuovo mastro vado a creare un nuovo conto e sottoconto
		if (mastro.isNew()) {

			Conto conto = new Conto();
			conto.setCodice("000");
			conto.setCodiceAzienda(getAzienda());
			conto.setSottoConti(new HashSet<SottoConto>());
			conto.setMastro(mastro);

			SottoConto sottoConto = new SottoConto();
			sottoConto.setCodice("000000");
			sottoConto.setCodiceAzienda(getAzienda());
			sottoConto.setConto(conto);
			conto.getSottoConti().add(sottoConto);

			mastro.setConti(new ArrayList<Conto>());
			mastro.getConti().add(conto);

			conto.setMastro(mastro);
		}

		try {
			mastroSalvato = panjeaDAO.save(mastro);
		} catch (Exception e) {
			logger.error("--> Errore  durante il salvataggio del mastro.", e);
			throw new RuntimeException("Errore durante il salvataggio del mastro.", e);
		}

		boolean soggettoCentroCosto = mastro.isSoggettoCentroCosto();
		CentroCosto centroCosto = mastro.getCentroCosto();

		String hql = "update SottoConto s set s.soggettoCentroCosto=:paramSoggettoCentroCosto,s.centroCosto=:paramCentroCosto where s.codice!='000000' and s.conto.id in (select id from Conto c where c.mastro.id= :paramMastroId)";
		Query query = panjeaDAO.prepareQuery(hql);
		query.setParameter("paramSoggettoCentroCosto", soggettoCentroCosto);
		query.setParameter("paramCentroCosto", centroCosto);
		query.setParameter("paramMastroId", mastroSalvato.getId());
		try {
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("--> Errore durante l'aggiornamento dei sottoconti per il centro costo", e);
			throw new RuntimeException("Errore durante l'aggiornamento dei sottoconti per il centro costo", e);
		}
		mastroSalvato.setSoggettoCentroCosto(mastro.isSoggettoCentroCosto());
		mastroSalvato.setCentroCosto(mastro.getCentroCosto());
		logger.debug("--> Exit salvaMastro");

		return mastroSalvato;
	}

	/**
	 * @param sottoConto
	 *            da salvare
	 * @return {@link SottoConto} salvato
	 * 
	 */
	@Override
	public SottoConto salvaSottoConto(SottoConto sottoConto) {
		logger.debug("--> Enter salvaSottoConto");

		if (sottoConto.isNew()) {
			sottoConto.setCodiceAzienda(getAzienda());
		}

		SottoConto sottoContoSalvato;
		try {
			sottoContoSalvato = panjeaDAO.save(sottoConto);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del sottoconto ", e);
			throw new RuntimeException("Errore durante il salvataggio del sottoconto", e);
		}
		logger.debug("--> Exit salvaSottoConto");
		return sottoContoSalvato;
	}

	/**
	 * 
	 * @param contoBase
	 *            in cui verificare la presenza del tipo conto
	 * @return succeso della verifica.
	 */
	private boolean verificaPresenzaTipoContoBase(ContoBase contoBase) {
		logger.debug("--> Enter verificaPresenzaContoBase");
		boolean contoBasePresente = false;
		List<ContoBase> contiBase = new ArrayList<ContoBase>();
		try {
			contiBase = caricaContiBase();
		} catch (ContabilitaException e) {
			logger.error("--> Errore nella verifica della presenza del tipo conto base", e);
			throw new RuntimeException("Errore nella verifica della presenza del tipo conto base", e);
		}

		for (ContoBase cb : contiBase) {
			// se ho lo stesso tipoContoBase verifico che non sia lo stesso
			// contoBase altimenti devo tornare true
			if (cb.getTipoContoBase().equals(contoBase.getTipoContoBase())) {
				if (contoBase.getId() != null && contoBase.getId().equals(cb.getId())) {
					contoBasePresente = false;
				} else {
					contoBasePresente = true;
				}
			}
		}

		logger.debug("--> Exit verificaPresenzaContoBase " + contoBasePresente);
		return contoBasePresente;
	}
}
