package it.eurotn.panjea.ordini.manager.documento;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.DistintaBaseManager;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.PrezzoArticoloCalculator;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaArticoloDistinta;
import it.eurotn.panjea.ordini.domain.RigaNota;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.RigaTestata;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineDAO;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineManager;
import it.eurotn.panjea.pagamenti.domain.AzioneScontoCommerciale;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 */
@Stateless(name = "Panjea.RigaOrdineManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaOrdineManager")
public class RigaOrdineManagerBean implements RigaOrdineManager {

	private static Logger logger = Logger.getLogger(RigaOrdineManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private DistintaBaseManager distintaBaseManager;

	@EJB
	private PrezzoArticoloCalculator prezzoArticoloCalculator;

	@EJB(beanName = "RigaOrdineDAOBean")
	private RigaOrdineDAO rigaOrdineDAO;

	@EJB(beanName = "RigaOrdineDistintaDAO")
	private RigaOrdineDAO rigaOrdineDistintaDAO;

	@IgnoreDependency
	@EJB
	protected AreaOrdineManager areaOrdineManager;

	@Override
	public void aggiornaDataConsegna(AreaOrdine areaOrdine, Date dataRiferimento) {
		List<RigaOrdine> righe = getDao().caricaRigheOrdine(areaOrdine);

		for (RigaOrdine rigaOrdine : righe) {
			if (!(rigaOrdine instanceof RigaArticolo)) {
				continue;
			}
			RigaArticolo rigaArticolo = (RigaArticolo) rigaOrdine;

			Date dataConsegnaRiga = rigaArticolo.getDataConsegna();
			if (((dataRiferimento != null && dataRiferimento.equals(dataConsegnaRiga)) || dataRiferimento == null)
					&& !rigaArticolo.isEvasa()) {
				rigaArticolo.setDataConsegna(areaOrdine.getDataConsegna());

				try {
					panjeaDAO.saveWithoutFlush(rigaArticolo);
				} catch (Exception e) {
					logger.error("--> errore durante il salvataggio della riga articolo", e);
					throw new RuntimeException("errore durante il salvataggio della riga articolo", e);
				}
			}
		}
		panjeaDAO.getEntityManager().flush();
	}

	@Override
	public void aggiornaScontoCommerciale(AreaOrdine areaOrdine, BigDecimal importoSconto) {
		AzioneScontoCommerciale azioneScontoCommerciale = AzioneScontoCommerciale.INSERISCI;

		if (importoSconto == null || BigDecimal.ZERO.compareTo(importoSconto) == 0) {
			azioneScontoCommerciale = AzioneScontoCommerciale.RIMUOVI;
		}

		List<RigaOrdine> righe = getDao().caricaRigheOrdine(areaOrdine);

		for (RigaOrdine rigaOrdine : righe) {

			// Solo una riga articolo può avere gli sconti quindi salto tutte
			// quelle che non lo sono o che hanno righe
			// collegate.
			if (!(rigaOrdine instanceof RigaArticolo)) {
				continue;
			}

			RigaArticolo rigaDaModificare = (RigaArticolo) rigaOrdine;
			rigaDaModificare.applicaScontoCommerciale(azioneScontoCommerciale, importoSconto);

			try {
				panjeaDAO.saveWithoutFlush(rigaOrdine);
			} catch (Exception e) {
				logger.error("--> errore durante il salvataggio della riga articolo", e);
				throw new RuntimeException("errore durante il salvataggio della riga articolo", e);
			}
		}

		panjeaDAO.getEntityManager().flush();
	}

	@Override
	public RigaArticolo associaConfigurazioneDistintaARigaOrdine(RigaArticolo rigaArticolo,
			ConfigurazioneDistinta configurazioneDistintaDaAssociare) {
		// Se alla riga avevo associato una configurazione personalizzata ne
		// associo una di anagrafica devo cancellare
		// quella personalizzata
		RigaArticolo rigaOrdineSalvata = getDao(rigaArticolo).associaConfigurazioneDistintaARigaOrdine(rigaArticolo,
				configurazioneDistintaDaAssociare);
		return rigaOrdineSalvata;
	}

	@Override
	public void collegaTestata(Set<Integer> righeOrdineDaCambiare) {
		logger.debug("--> Enter collegaTestata");
		// Ordino le righe per ordinamento
		Set<RigaOrdine> righeOrdinate = new TreeSet<RigaOrdine>(new Comparator<RigaOrdine>() {

			@Override
			public int compare(RigaOrdine o1, RigaOrdine o2) {
				return Double.compare(01, 02);
			}
		});
		for (Integer idRigaOrdine : righeOrdineDaCambiare) {
			righeOrdinate.add(panjeaDAO.loadLazy(RigaOrdine.class, idRigaOrdine));
		}

		StringBuilder sb = new StringBuilder("select ro from RigaOrdine ro ");
		sb.append("where ro.areaOrdine=:areaOrdine and ro.ordinamento<=:ordinamento ");
		sb.append("order by ordinamento desc ");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setMaxResults(2);

		for (RigaOrdine rigaOrdine : righeOrdinate) {
			// Devo recuperare due righe ordine. Quella da spostare e
			// la precedente per collegarla alla sua riga testata
			try {
				query.setParameter("areaOrdine", rigaOrdine.getAreaOrdine());
				query.setParameter("ordinamento", rigaOrdine.getOrdinamento());
				@SuppressWarnings("unchecked")
				List<RigaOrdine> righe = query.getResultList();
				// Recupero la riga testata alla quale devo collegare la riga
				RigaTestata rigaTestata = null;
				int livello = 0;
				if (righe.size() == 2) {
					if (righe.get(1) instanceof RigaTestata) {
						rigaTestata = (RigaTestata) righe.get(1);
						livello = rigaTestata.getLivello();
						livello++;
					} else if (righe.get(1).getRigaTestataCollegata() != null) {
						rigaTestata = righe.get(1).getRigaTestataCollegata();
						livello = rigaTestata.getLivello();
						livello++;
					}
				}
				rigaOrdine.setRigaTestataCollegata(rigaTestata);
				rigaOrdine.setLivello(livello);
				panjeaDAO.save(rigaOrdine);
			} catch (DAOException e) {
				throw new RuntimeException(e);
			}
		}
		logger.debug("--> Exit collegaTestata");
	}

	@Override
	public RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
		return (RigaArticolo) getDao(parametriCreazioneRigaArticolo).creaRigaArticolo(parametriCreazioneRigaArticolo);
	}

	@Override
	public boolean creaRigaNoteAutomatica(AreaOrdine areaOrdine, String note) {
		logger.debug("--> Enter creaRigaNoteAutomatica");

		RigaNota rigaNota = new RigaNota();
		rigaNota.setAreaOrdine(areaOrdine);
		rigaNota.setLivello(0);
		rigaNota.setNota(note);
		rigaNota.setRigaAutomatica(true);
		rigaNota.setOrdinamento(Calendar.getInstance().getTimeInMillis() * 100);
		rigaNota.setNoteSuDestinazione(areaOrdine.getTipoAreaOrdine().isNoteSuDestinazione());

		RigaOrdine rigaOrdine = getDao(rigaNota).salvaRigaOrdine(rigaNota);

		logger.debug("--> Exit creaRigaNoteAutomatica");
		return rigaOrdine != null;
	}

	@Override
	public void dividiRiga(Integer rigaOriginale, List<RigaArticolo> righeDivise) {
		logger.debug("--> Enter dividiRiga");
		if (righeDivise.size() < 1) {
			return;
		}
		RigaArticolo riga = new RigaArticolo();
		riga.setId(rigaOriginale);
		riga = (RigaArticolo) rigaOrdineDAO.caricaRigaOrdine(riga);
		riga.setQta(righeDivise.get(0).getQta());
		riga.setDataConsegna(righeDivise.get(0).getDataConsegna());
		try {
			riga = panjeaDAO.save(riga);
		} catch (DAOException e) {
			logger.error("-->errore nel salvare la riga da dividere", e);
			throw new RuntimeException("-->errore nel salvare la riga da dividere", e);
		}
		for (int i = 1; i < righeDivise.size(); i++) {
			RigaArticolo rigaConDati = righeDivise.get(i);
			RigaArticolo nuovaRiga = PanjeaEJBUtil.cloneObject(riga);
			nuovaRiga.setId(null);
			nuovaRiga.setVersion(null);
			nuovaRiga.setQta(rigaConDati.getQta());
			nuovaRiga.setQtaMagazzino(rigaConDati.getQta());
			nuovaRiga.setDataConsegna(rigaConDati.getDataConsegna());
			try {
				panjeaDAO.save(nuovaRiga);
			} catch (DAOException e) {
				logger.error("-->errore nel salvare una nuova riga divisa", e);
				throw new RuntimeException("-->errore nel salvare una nuova riga divisa", e);
			}
		}
		logger.debug("--> Exit dividiRiga");
	}

	@Override
	public void forzaRigheOrdine(List<Integer> righe) {
		logger.debug("--> Enter forzaRigheOrdine");

		for (Integer idRiga : righe) {
			RigaOrdine rigaOrdine = panjeaDAO.loadLazy(RigaOrdine.class, idRiga);
			rigaOrdine.setEvasioneForzata(true);
			try {
				panjeaDAO.saveWithoutFlush(rigaOrdine);
			} catch (Exception e) {
				logger.error("--> errore durante il salvataggio della riga ordine.", e);
				throw new RuntimeException("errore durante il salvataggio della riga ordine.", e);
			}
		}

		logger.debug("--> Exit forzaRigheOrdine");
	}

	@Override
	public RigaOrdineDAO getDao() {
		return rigaOrdineDAO;
	}

	@Override
	public RigaOrdineDAO getDao(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
		RigaOrdineDAO dao = rigaOrdineDAO;

		Articolo articolo;
		// Carico l'articolo
		try {
			articolo = panjeaDAO.load(Articolo.class, parametriCreazioneRigaArticolo.getIdArticolo());
		} catch (ObjectNotFoundException e) {
			logger.error("--> articolo non trovato: idArticolo " + parametriCreazioneRigaArticolo.getIdArticolo(), e);
			throw new RuntimeException(e);
		}

		// in base all'articolo ed ai parametri seleziono il dao.
		if (parametriCreazioneRigaArticolo.isGestioneArticoloDistinta() && articolo.isDistinta()) {
			dao = rigaOrdineDistintaDAO;
		}
		return dao;
	}

	@Override
	public RigaOrdineDAO getDao(RigaOrdine rigaOrdine) {
		RigaOrdineDAO dao = rigaOrdineDAO;
		if (rigaOrdine.getClass() == RigaArticoloDistinta.class) {
			dao = rigaOrdineDistintaDAO;
		}
		return dao;
	}

	@Override
	public void inserisciRaggruppamentoArticoli(Integer idAreaOrdine, ProvenienzaPrezzo provenienzaPrezzo,
			Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
			Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
			Integer idZonaGeografica, boolean noteSuDestinazione, String codiceValuta, String codiceLingua,
			Date dataConsegna, Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
			BigDecimal percentualeScontoCommerciale) throws RimanenzaLottiNonValidaException {
		// Carico il riferimento all'area magazzino
		AreaOrdine areaOrdine = panjeaDAO.loadLazy(AreaOrdine.class, idAreaOrdine);

		// Carico il raggruppamento articoli
		RaggruppamentoArticoli raggruppamentoArticoli;
		try {
			raggruppamentoArticoli = panjeaDAO.load(RaggruppamentoArticoli.class, idRaggruppamentoArticoli);
		} catch (ObjectNotFoundException e) {
			logger.error("-->errore. Raggruppamento articolìi non trovato. Id :" + idRaggruppamentoArticoli, e);
			throw new RuntimeException(e);
		}

		ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
		parametri.setProvenienzaPrezzo(provenienzaPrezzo);
		parametri.setData(data);
		parametri.setIdSedeEntita(idSedeEntita);
		parametri.setIdListinoAlternativo(idListinoAlternativo);
		parametri.setIdListino(idListino);
		parametri.setImporto(importo);
		parametri.setCodiceIvaAlternativo(codiceIvaAlternativo);
		parametri.setIdTipoMezzo(idTipoMezzo);
		parametri.setIdZonaGeografica(idZonaGeografica);
		parametri.setNoteSuDestinazione(noteSuDestinazione);
		parametri.setTipoMovimento(TipoMovimento.NESSUNO);
		parametri.setCodiceValuta(codiceValuta);
		parametri.setCodiceLingua(codiceLingua);
		parametri.setIdAgente(idAgente);
		parametri.setTipologiaCodiceIvaAlternativo(tipologiaCodiceIvaAlternativo);
		parametri.setPercentualeScontoCommerciale(percentualeScontoCommerciale);

		// Per ogni riga del raggruppamento crea la riga articolo, la associo
		// all'area magazzino e la salvo
		for (RigaRaggruppamentoArticoli righeRaggruppamento : raggruppamentoArticoli.getRigheRaggruppamentoArticoli()) {
			parametri.setIdArticolo(righeRaggruppamento.getArticolo().getId());
			parametri.setProvenienzaPrezzoArticolo(righeRaggruppamento.getArticolo().getProvenienzaPrezzoArticolo());
			RigaArticolo rigaArticolo = creaRigaArticolo(parametri);
			rigaArticolo.setDataConsegna(dataConsegna);
			// Setto la qta. Ricalcolo il prezzo perchè il prezzo potrebbe
			// dipendere dalla qta.
			rigaArticolo.setQta(righeRaggruppamento.getQta());
			rigaArticolo.setQtaMagazzino(righeRaggruppamento.getQta());
			rigaArticolo.applicaPoliticaPrezzo();
			rigaArticolo.setAreaOrdine(areaOrdine);
			areaOrdine = rigaArticolo.getAreaOrdine();
			getDao(rigaArticolo).salvaRigaOrdine(rigaArticolo);
		}

	}

	@Override
	public RigaOrdine ricalcolaPrezziRigaArticolo(RigaArticolo rigaArticolo, CodicePagamento codicePagamento) {

		ArticoloLite articolo = rigaArticolo.getArticolo();
		AreaOrdine areaOrdine = rigaArticolo.getAreaOrdine();
		Integer idListino = null;
		Integer idListinoAlternativo = null;
		Integer idSedeEntita = null;
		Integer idTipoMezzo = null;
		BigDecimal percentualeScontoCommerciale = null;
		Integer idAgente = null;

		if (areaOrdine.getListino() != null) {
			idListino = areaOrdine.getListino().getId();
		}

		if (areaOrdine.getListinoAlternativo() != null) {
			idListinoAlternativo = areaOrdine.getListinoAlternativo().getId();
		}

		if (areaOrdine.getDocumento().getSedeEntita() != null) {
			idSedeEntita = areaOrdine.getDocumento().getSedeEntita().getId();
		}

		if (areaOrdine.getAgente() != null) {
			idAgente = areaOrdine.getAgente().getId();
		}

		if (codicePagamento != null) {
			percentualeScontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
		}

		ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(articolo.getId(),
				areaOrdine.getDocumento().getDataDocumento(), idListino, idListinoAlternativo, null, idSedeEntita, null,
				null, ProvenienzaPrezzo.LISTINO, idTipoMezzo, null, articolo.getProvenienzaPrezzoArticolo(),
				areaOrdine.getDocumento().getTotale().getCodiceValuta(), idAgente, percentualeScontoCommerciale);
		PoliticaPrezzo politicaPrezzo = prezzoArticoloCalculator.calcola(parametriCalcoloPrezzi);

		rigaArticolo.setPoliticaPrezzo(politicaPrezzo);
		rigaArticolo.applicaPoliticaPrezzo();

		try {
			rigaArticolo = panjeaDAO.save(rigaArticolo);
		} catch (Exception e) {
			logger.error("--> errore durante il salvataggio della riga articolo", e);
			throw new RuntimeException("errore durante il salvataggio della riga articolo", e);
		}

		return rigaArticolo;
	}
}
