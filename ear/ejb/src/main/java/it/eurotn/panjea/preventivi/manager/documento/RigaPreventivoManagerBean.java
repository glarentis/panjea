package it.eurotn.panjea.preventivi.manager.documento;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.PrezzoArticoloCalculator;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.pagamenti.domain.AzioneScontoCommerciale;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;
import it.eurotn.panjea.preventivi.domain.RigaArticoloDistinta;
import it.eurotn.panjea.preventivi.domain.RigaArticoloPadre;
import it.eurotn.panjea.preventivi.domain.RigaNota;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.RigaTestata;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.RigaPreventivoDAO;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.RigaPreventivoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

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
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.RigaPreventivoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaPreventivoManager")
public class RigaPreventivoManagerBean implements RigaPreventivoManager {

	private static Logger logger = Logger.getLogger(RigaPreventivoManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private PrezzoArticoloCalculator prezzoArticoloCalculator;

	@EJB(beanName = "RigaPreventivoDAOBean")
	private RigaPreventivoDAO rigaPreventivoDAO;

	@Override
	public void aggiornaScontoCommerciale(AreaPreventivo areaPreventivo, BigDecimal importoSconto) {
		AzioneScontoCommerciale azioneScontoCommerciale = AzioneScontoCommerciale.INSERISCI;

		if (importoSconto == null || BigDecimal.ZERO.compareTo(importoSconto) == 0) {
			azioneScontoCommerciale = AzioneScontoCommerciale.RIMUOVI;
		}

		List<RigaPreventivo> righe = getDao().caricaRighePreventivo(areaPreventivo);

		for (RigaPreventivo rigaPreventivo : righe) {
			if (rigaPreventivo instanceof RigaArticolo) {
				RigaArticolo rigaDaModificare = (RigaArticolo) rigaPreventivo;
				rigaDaModificare.applicaScontoCommerciale(azioneScontoCommerciale, importoSconto);

				try {
					panjeaDAO.saveWithoutFlush(rigaPreventivo);
				} catch (Exception e) {
					logger.error("--> errore durante il salvataggio della riga articolo", e);
					throw new RuntimeException("errore durante il salvataggio della riga articolo", e);
				}
			}
		}

		panjeaDAO.getEntityManager().flush();

	}

	@Override
	public void collegaTestata(Set<Integer> righePreventivoDaCambiare) {
		logger.debug("--> Enter collegaTestata");
		// Ordino le righe per ordinamento
		Set<RigaPreventivo> righeOrdinate = new TreeSet<RigaPreventivo>(new Comparator<RigaPreventivo>() {

			@Override
			public int compare(RigaPreventivo riga1, RigaPreventivo riga2) {
				return Double.compare(riga1.getOrdinamento(), riga2.getOrdinamento());
			}
		});
		for (Integer idRigaOrdine : righePreventivoDaCambiare) {
			righeOrdinate.add(panjeaDAO.loadLazy(RigaPreventivo.class, idRigaOrdine));
		}

		StringBuilder sb = new StringBuilder("select ro from RigaPreventivo ro ");
		sb.append("where ro.areaPreventivo=:areaPreventivo and ro.ordinamento<=:ordinamento ");
		sb.append("order by ordinamento desc ");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setMaxResults(2);

		for (RigaPreventivo rigaPreventivo : righeOrdinate) {
			// Devo recuperare due righe ordine. Quella da spostare e
			// la precedente per collegarla alla sua riga testata
			try {
				query.setParameter("areaPreventivo", rigaPreventivo.getAreaPreventivo());
				query.setParameter("ordinamento", rigaPreventivo.getOrdinamento());
				@SuppressWarnings("unchecked")
				List<RigaPreventivo> righe = query.getResultList();
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
				rigaPreventivo.setRigaTestataCollegata(rigaTestata);
				rigaPreventivo.setLivello(livello);
				panjeaDAO.save(rigaPreventivo);
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
	public boolean creaRigaNoteAutomatica(AreaPreventivo areaPreventivo, String note) {
		logger.debug("--> Enter creaRigaNoteAutomatica");

		RigaNota rigaNota = new RigaNota();
		rigaNota.setAreaPreventivo(areaPreventivo);
		rigaNota.setLivello(0);
		rigaNota.setNota(note);
		rigaNota.setRigaAutomatica(true);
		rigaNota.setOrdinamento(Calendar.getInstance().getTimeInMillis() * 100);
		rigaNota.setNoteSuDestinazione(areaPreventivo.getTipoAreaPreventivo().isNoteSuDestinazione());

		RigaPreventivo rigaPreventivo = getDao(rigaNota).salvaRigaPreventivo(rigaNota);

		logger.debug("--> Exit creaRigaNoteAutomatica");
		return rigaPreventivo != null;
	}

	@Override
	public RigaPreventivoDAO getDao() {
		return rigaPreventivoDAO;
	}

	@Override
	public RigaPreventivoDAO getDao(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
		RigaPreventivoDAO dao = rigaPreventivoDAO;

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
			throw new RuntimeException("Non implementato");
		}
		return dao;
	}

	@Override
	public RigaPreventivoDAO getDao(RigaPreventivo rigaPreventivo) {
		RigaPreventivoDAO dao = rigaPreventivoDAO;
		if (rigaPreventivo instanceof RigaArticoloPadre) {
			throw new RuntimeException("Non implementato");
		}

		if (rigaPreventivo instanceof RigaArticoloDistinta) {
			throw new RuntimeException("Non implementato");
		}

		return dao;
	}

	@Override
	public void inserisciRaggruppamentoArticoli(Integer idAreaPreventivo, ProvenienzaPrezzo provenienzaPrezzo,
			Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
			Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
			Integer idZonaGeografica, boolean noteSuDestinazione, String codiceValuta, String codiceLingua,
			Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
			BigDecimal percentualeScontoCommerciale) {

		// Carico il riferimento all'area magazzino
		AreaPreventivo areaPreventivo = panjeaDAO.loadLazy(AreaPreventivo.class, idAreaPreventivo);

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

		// Per ogni riga del raggruppamento crea la riga articolo, la associo all'area magazzino e la salvo
		for (RigaRaggruppamentoArticoli righeRaggruppamento : raggruppamentoArticoli.getRigheRaggruppamentoArticoli()) {
			parametri.setIdArticolo(righeRaggruppamento.getArticolo().getId());
			parametri.setProvenienzaPrezzoArticolo(righeRaggruppamento.getArticolo().getProvenienzaPrezzoArticolo());
			RigaArticolo rigaArticolo = creaRigaArticolo(parametri);
			// Setto la qta. Ricalcolo il prezzo perchè il prezzo potrebbe dipendere dalla qta.
			rigaArticolo.setQta(righeRaggruppamento.getQta());
			rigaArticolo.setQtaMagazzino(righeRaggruppamento.getQta());
			rigaArticolo.applicaPoliticaPrezzo();
			rigaArticolo.setAreaPreventivo(areaPreventivo);
			areaPreventivo = rigaArticolo.getAreaPreventivo();
			getDao(rigaArticolo).salvaRigaPreventivo(rigaArticolo);
		}
	}

	@Override
	public RigaPreventivo ricalcolaPrezziRigaArticolo(RigaArticolo rigaArticolo, CodicePagamento codicePagamento) {
		ArticoloLite articolo = rigaArticolo.getArticolo();
		AreaPreventivo areaPreventivo = rigaArticolo.getAreaPreventivo();
		Integer idListino = null;
		Integer idListinoAlternativo = null;
		Integer idSedeEntita = null;
		Integer idTipoMezzo = null;
		BigDecimal percentualeScontoCommerciale = null;
		Integer idAgente = null;

		if (areaPreventivo.getListino() != null) {
			idListino = areaPreventivo.getListino().getId();
		}

		if (areaPreventivo.getListinoAlternativo() != null) {
			idListinoAlternativo = areaPreventivo.getListinoAlternativo().getId();
		}

		if (areaPreventivo.getDocumento().getSedeEntita() != null) {
			idSedeEntita = areaPreventivo.getDocumento().getSedeEntita().getId();
		}

		if (codicePagamento != null) {
			percentualeScontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
		}

		ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(articolo.getId(), areaPreventivo
				.getDocumento().getDataDocumento(), idListino, idListinoAlternativo, null, idSedeEntita, null, null,
				ProvenienzaPrezzo.LISTINO, idTipoMezzo, null, articolo.getProvenienzaPrezzoArticolo(), areaPreventivo
				.getDocumento().getTotale().getCodiceValuta(), idAgente, percentualeScontoCommerciale);
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
