package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.manager.interfaces.ValutaManager;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAnticipoFattureManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAssegnoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaChiusureManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaEffettiManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaPagamentiManager;
import it.eurotn.panjea.tesoreria.service.exception.EntitaRateNonCoerentiException;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreaChiusure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AreaChiusureManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaChiusureManager")
public class AreaChiusureManagerBean implements AreaChiusureManager {

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected AreaEffettiManager areaEffettiManager;

	@EJB
	private ValutaManager valutaManager;

	@EJB
	protected AreaPagamentiManager areaPagamentiManager;

	@EJB
	protected AreaAnticipoFattureManager areaAnticipoFattureManager;

	@EJB
	protected AreaAssegnoManager areaAssegnoManager;

	private static Logger logger = Logger.getLogger(AreaChiusureManagerBean.class.getName());

	@Override
	public void cancellaAreaChiusure(AreaChiusure areaChiusure) {
		logger.debug("--> Enter cancellaAreaChiusure");
		try {
			panjeaDAO.delete(areaChiusure);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaAreaChiusure");

	}

	@Override
	public AreaChiusure caricaAreaChisura(Integer id) {
		logger.debug("--> Enter caricaAreaChisura");
		AreaChiusure result = null;
		try {
			result = panjeaDAO.load(AreaChiusure.class, id);
		} catch (ObjectNotFoundException e) {
			logger.error("-->errore nel caricare l'area effetti", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaAreaChisura");
		return result;

	}

	@Override
	public AreaChiusure caricaAreaChiusura(Documento documento) {
		logger.debug("--> Enter caricaAreaChiusura");

		javax.persistence.Query query = panjeaDAO
				.prepareQuery("select ach from AreaChiusure ach  inner join ach.documento doc where doc.id = :paramIdDocumento");
		query.setParameter("paramIdDocumento", documento.getId());

		AreaChiusure areaChiusure = null;
		try {
			areaChiusure = (AreaChiusure) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> AreaChiusure non trovata, restituisco una nuova istanza di AreaChiusure");
			areaChiusure = new AreaChiusure();
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dell'area chiusure", e);
			throw new RuntimeException("Errore durante il caricamento dell'area chiusure", e);
		}

		logger.debug("--> Exit caricaAreaChiusura");
		return areaChiusure;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pagamento> caricaPagamenti(Rata rata) {
		logger.debug("--> Enter caricaPagamenti");

		javax.persistence.Query query = panjeaDAO
				.prepareQuery("select p from Pagamento p inner join fetch p.areaChiusure left join fetch p.effetto where p.rata.id = :paramRataId order by dataPagamento");

		query.setParameter("paramRataId", rata.getId());

		List<Pagamento> listResult = new ArrayList<Pagamento>();
		try {
			listResult = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dei pagamenti della rata", e);
			throw new RuntimeException("Errore durante il caricamento dei pagamenti della rata", e);
		}

		logger.debug("--> Exit caricaPagamenti");
		return listResult;
	}

	@Override
	@TransactionTimeout(value = 14400)
	public List<AreaChiusure> creaAreaChiusure(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			List<Pagamento> pagamenti) throws DocumentoDuplicateException, CambioNonPresenteException,
			EntitaRateNonCoerentiException {
		logger.debug("--> Enter creaAreaChiusure");
		if (pagamenti.isEmpty() || parametriCreazioneAreaChiusure.getTipoAreaPartita() == null) {
			throw new IllegalArgumentException("Pagamenti e/o tipo area partita non presnti");
		}

		String codiceValutaPagamento = pagamenti.get(0).getRata().getImporto().getCodiceValuta();
		BigDecimal tassoDiCambio = valutaManager.caricaCambioValuta(codiceValutaPagamento,
				parametriCreazioneAreaChiusure.getDataDocumento()).getTasso();
		ValutaAzienda valutaAziendale = valutaManager.caricaValutaAziendaCorrente();
		// riassocio la rata del pagamento alla sessione
		for (Pagamento pagamento : pagamenti) {
			try {
				pagamento.setRata(panjeaDAO.load(Rata.class, pagamento.getRata().getId()));
			} catch (ObjectNotFoundException e) {
				throw new RuntimeException(e);
			}
			// calcolo l'importo settando il codice valuta e il tasso di cambio
			pagamento.getImporto().setCodiceValuta(pagamento.getRata().getImporto().getCodiceValuta());
			pagamento.getImporto().setTassoDiCambio(tassoDiCambio);
			pagamento.getImporto().calcolaImportoValutaAzienda(valutaAziendale.getNumeroDecimali());

			// inizializzo l'importo forzato settando il codice valuta, il tasso di cambio e l'importo a zero
			pagamento.getImportoForzato().setCodiceValuta(pagamento.getRata().getImporto().getCodiceValuta());
			pagamento.getImportoForzato().setTassoDiCambio(tassoDiCambio);
			pagamento.getImportoForzato().setImportoInValuta(BigDecimal.ZERO);
			pagamento.getImporto().calcolaImportoValutaAzienda(valutaAziendale.getNumeroDecimali());

			// Se l'importo corrisponde all'importo per lo sconto finanziario e la data
			// è inferiore ai num_giorni validi forzo la chiusura della rata e la setto come scontofinanziario
			boolean isImportoScontoFinanziario = pagamento.getRata().getImportoInValutaScontoFinanziario()
					.compareTo(pagamento.getImporto().getImportoInValuta()) == 0;
			boolean isDataScontoFinanziarioValid = parametriCreazioneAreaChiusure.getDataDocumento().before(
					pagamento.getRata().getAreaRate().getDataScadenzaScontoFinanziario());
			if (isImportoScontoFinanziario && isDataScontoFinanziarioValid) {
				pagamento.setChiusuraForzataRata(true);
				pagamento.setScontoFinanziario(true);
				// l'importo forzato viene calcolato dopo perchè la rata viene forzata.
			}

			// se la chiusura deve essere forzata trovo il valore togliendo dal residuo della rata l'importo del
			// pagamento
			if (pagamento.getChiusuraForzataRata()) {
				BigDecimal importoForzatoValuta = pagamento.getRata().getResiduo().getImportoInValuta()
						.subtract(pagamento.getImporto().getImportoInValuta());
				pagamento.getImportoForzato().setImportoInValuta(importoForzatoValuta);
				pagamento.getImportoForzato().calcolaImportoValutaAzienda(valutaAziendale.getNumeroDecimali());
			}

		}

		List<AreaChiusure> areeChiusure = new ArrayList<AreaChiusure>();

		/*
		 * Crea le aree di chiusura per i pagamenti richiamando la creazione dell'area nel manager corretto. 1.Se il
		 * tipo operazione non prevede la distinta verrà richiamato il manager dell'area pagamenti. 2.Se il tipo
		 * operazione è GESTIONE_DISTINTA, se il tipo partita è attivo verrà richiamato il manager dell'area effetti,
		 * altrimenti quello dell'area pagamenti.
		 */
		switch (parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoOperazione()) {
		case GESTIONE_ASSEGNO:
			TipoPartita tipoPartitaAssegno = parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoPartita();
			if (tipoPartitaAssegno == TipoPartita.ATTIVA) {
				areeChiusure.add(areaAssegnoManager.creaAreaAssegno(parametriCreazioneAreaChiusure, pagamenti));
			} else if (tipoPartitaAssegno == TipoPartita.PASSIVA) {
				throw new RuntimeException("Decidere che fare");
			} else {
				if (logger.isDebugEnabled()) {
					logger.error("--> Errore, tipo partita non gestita");
				}
				throw new UnsupportedOperationException("Tipo partita non gestita");
			}
			break;
		case GESTIONE_DISTINTA:
			// Controllo sul tipo partita. Se il tipo partita di una GESTIONE_DISTINTA è ATTIVA devo crare degli effetti
			// che andranno a creare una sola area effetti, altrimenti se è PASSIVA verrà creata una sola area pagamenti
			TipoPartita tipoPartitaDistinta = parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoPartita();
			if (tipoPartitaDistinta == TipoPartita.ATTIVA) {
				areeChiusure.add(areaEffettiManager.creaAreaEffetti(parametriCreazioneAreaChiusure, pagamenti));
			} else if (tipoPartitaDistinta == TipoPartita.PASSIVA) {
				areeChiusure.addAll(areaPagamentiManager.creaAreaPagamenti(parametriCreazioneAreaChiusure, pagamenti));
			} else {
				if (logger.isDebugEnabled()) {
					logger.error("--> Errore, tipo partita non gestita");
				}
				throw new UnsupportedOperationException("Tipo partita non gestita");
			}
			break;
		case CHIUSURA:
			// Nel caso di un tipo operazione CHIUSURA verranno sempre create delle aree pagamenti
			areeChiusure.addAll(areaPagamentiManager.creaAreaPagamenti(parametriCreazioneAreaChiusure, pagamenti));
			break;
		case ANTICIPO_FATTURE:
			TipoPartita tipoPartitaAnticipo = parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoPartita();
			if (tipoPartitaAnticipo == TipoPartita.ATTIVA) {
				areeChiusure.add(areaAnticipoFattureManager.creaAreaAnticipoFatture(parametriCreazioneAreaChiusure,
						pagamenti));
			} else {
				if (logger.isDebugEnabled()) {
					logger.error("--> Errore, tipo partita non gestita");
				}
				throw new UnsupportedOperationException("Tipo partita non gestita");
			}
			break;
		case CHIUSURA_ANTICIPO_FATTURE:
			TipoPartita tipoPartitaChiusuraAnticipo = parametriCreazioneAreaChiusure.getTipoAreaPartita()
					.getTipoPartita();
			if (tipoPartitaChiusuraAnticipo == TipoPartita.ATTIVA) {
				areeChiusure.add(areaAnticipoFattureManager.creaChiusuraAreaAnticipoFatture(
						parametriCreazioneAreaChiusure, pagamenti));
			} else {
				if (logger.isDebugEnabled()) {
					logger.error("--> Errore, tipo partita non gestita");
				}
				throw new UnsupportedOperationException("Tipo partita non gestita");
			}
			break;
		default:
			logger.error("--> Errore, tipo operazione non gestita.");
			throw new UnsupportedOperationException("Tipo operazione non gestita.");
		}

		logger.debug("--> Exit creaAreaChiusure");
		return areeChiusure;
	}

	@Override
	public List<AreaChiusure> ricercaAreeChiusure(ParametriRicercaAreaChiusure parametriRicercaAreaChiusure) {
		logger.debug("--> Enter ricercaAreeChiusure");

		logger.debug("--> Exit ricercaAreeChiusure");
		return null;
	}

}
