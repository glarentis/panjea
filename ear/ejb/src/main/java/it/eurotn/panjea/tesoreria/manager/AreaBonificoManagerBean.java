package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaBonifico;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaBonificoContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaBonificoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author leonardo
 */
@Stateless(name = "Panjea.AreaBonificoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaBonificoManager")
public class AreaBonificoManagerBean implements AreaBonificoManager {

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private TipiAreaPartitaManager tipiAreaPartitaManager;

	@EJB
	private AreaTesoreriaManager areaTesoreriaManager;

	@EJB
	private AreaBonificoContabilitaManager areaBonificoContabilitaManager;

	@EJB
	private DocumentiManager documentiManager;

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	private AreaContabileCancellaManager areaContabileCancellaManager;

	private static Logger logger = Logger.getLogger(AreaBonificoManagerBean.class.getName());

	@Override
	public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate) {
		logger.debug("--> Enter cancellaAreaTesoreria");

		AreaBonifico areaBonifico = (AreaBonifico) areaTesoreria;

		try {
			if (deleteAreeCollegate) {
				// cancello l'area contabile se esiste
				AreaContabileLite areaContabileLite = areaContabileManager
						.caricaAreaContabileLiteByDocumento(areaBonifico.getDocumento());
				if (areaContabileLite != null) {
					areaContabileCancellaManager.cancellaAreaContabile(areaBonifico.getDocumento(), true);
				}
			}

			// cancello l'area distinta
			panjeaDAO.delete(areaBonifico);

			if (deleteAreeCollegate) {
				// cancello il documento
				documentiManager.cancellaDocumento(areaBonifico.getDocumento());
			}
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione dell'area bonifico.", e);
			throw new RuntimeException("Errore durante la cancellazione dell'area bonifico.", e);
		}

		logger.debug("--> Exit cancellaAreaTesoreria");
	}

	@Override
	public AreaBonifico caricaAreaBonifico(AreaPagamenti areaPagamenti) {
		logger.debug("--> Enter caricaAreaBonifico");

		Query query = panjeaDAO.prepareNamedQuery("AreaBonifico.caricaByAreaCollegata");
		query.setParameter("paramIdAreaPagamentiCollegata", areaPagamenti.getId());

		AreaBonifico areaDistinta = null;
		try {
			areaDistinta = (AreaBonifico) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.warn("L'area bonifico non esiste per questa area pagamenti");
		} catch (Exception e) {
			logger.error("-->errore durante il caricamento del bonifico collegato all'area pagamenti", e);
			throw new RuntimeException("errore durante il caricamento del bonifico collegato all'area pagamenti", e);
		}

		logger.debug("--> Exit caricaAreaBonifico");
		return areaDistinta;
	}

	@Override
	public AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException {
		logger.debug("--> Enter caricaAreaTesoreria");

		AreaBonifico result = null;
		Query query = panjeaDAO.prepareNamedQuery("AreaBonifico.carica");
		query.setParameter("paramIdAreaBonifico", idAreaTesoreria);
		try {
			result = (AreaBonifico) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.error("--> area bonifico non trovata id " + idAreaTesoreria);
			throw e;
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dell'area bonifico", e);
			throw new RuntimeException("Errore durante il caricamento dell'area bonifico", e);
		}

		logger.debug("--> Exit caricaAreaTesoreria");
		return result;
	}

	@Override
	public AreaBonifico creaAreaBonifico(Date dataDocumento, String numeroDocumento, AreaPagamenti areaPagamenti,
			BigDecimal spese, Set<Pagamento> pagamenti) throws TipoDocumentoBaseException {
		logger.debug("--> Enter creaAreaDistintaBancaria");
		TipoDocumentoBasePartite tipoDocumentoBase = tipiAreaPartitaManager
				.caricaTipoDocumentoBase(TipoDocumentoBasePartite.TipoOperazione.BONIFICO_FORNITORE);

		areaPagamenti = updateAreaPagamentiCollegata(areaPagamenti, dataDocumento, pagamenti);

		// documento
		Documento doc = new Documento();
		doc.setCodiceAzienda(areaPagamenti.getDocumento().getCodiceAzienda());
		doc.setDataDocumento(dataDocumento);
		doc.getCodice().setCodice(numeroDocumento);
		doc.setTipoDocumento(tipoDocumentoBase.getTipoAreaPartita().getTipoDocumento());
		doc.setEntita(null);
		doc.setRapportoBancarioAzienda(areaPagamenti.getDocumento().getRapportoBancarioAzienda());
		doc.setTotale(areaPagamenti.getDocumento().getTotale());
		doc.setContrattoSpesometro(null);
		Documento docSalvato;
		try {
			docSalvato = documentiManager.salvaDocumento(doc);
		} catch (DocumentoDuplicateException e1) {
			throw new RuntimeException(e1);
		}

		// area partite
		AreaBonifico areaBonifico = new AreaBonifico();
		areaBonifico.setDocumento(docSalvato);
		areaBonifico.setTipoAreaPartita(tipoDocumentoBase.getTipoAreaPartita());
		areaBonifico.setCodicePagamento(areaPagamenti.getCodicePagamento());
		areaBonifico.setAreaPagamentiCollegata(areaPagamenti);
		areaBonifico.setSpeseIncasso(ObjectUtils.defaultIfNull(spese, BigDecimal.ZERO));
		areaBonifico = (AreaBonifico) areaTesoreriaManager.salvaAreaTesoreria(areaBonifico);

		// ricarico la distinta per ricaricare gli effetti con le modifiche apportate
		try {
			areaBonifico = (AreaBonifico) caricaAreaTesoreria(areaBonifico.getId());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// creo l'area contabile
		try {
			areaBonificoContabilitaManager.creaAreaContabileBonifico(areaBonifico);
		} catch (ContoRapportoBancarioAssenteException e) {
			logger.error("--> Errore durante la creazione dell'area contabile dell'area bonifico.", e);
			throw new RuntimeException("Errore durante la creazione dell'area contabile dell'area bonifico.", e);
		}

		logger.debug("--> Exit creaAreaDistintaBancaria");
		return areaBonifico;
	}

	@Override
	public List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria) {
		return new ArrayList<AreaEffetti>();
	}

	/**
	 * Cancella i pagamenti non considerati per il bonifico, aggiorna la data pagamento di quelli considerati con la
	 * data del bonifico fornitore e aggiorna il totale documento.
	 * 
	 * @param areaPagamenti
	 *            areaPagamenti
	 * @param dataDocumento
	 *            dataDocumento
	 * @param pagamenti
	 *            pagamenti
	 * @return AreaPagamenti
	 */
	private AreaPagamenti updateAreaPagamentiCollegata(AreaPagamenti areaPagamenti, Date dataDocumento,
			Set<Pagamento> pagamenti) {
		Set<Pagamento> pagamentiOrigine = areaPagamenti.getPagamenti();

		// se i pagamenti per l'areaBonifico sono diversi da quelli originali, devo cancellare i pagamenti
		// dell'areaPagamenti e aggiornare il totale documento
		// e devo impostare la data pagamento per chiudere la rata
		boolean deletePagamentiNonAccettati = pagamentiOrigine.size() != pagamenti.size();

		// CALCOLO IL TOTALE DEL DOCUMENTO
		Importo totaleDocumento = new Importo();
		// Estraggo il primo pagamento per utilizzare i dati di codiceValuta e tassoDi cambio che
		// sono ugulali per tutti i pagamenti
		Pagamento pagamentoRiferimento = pagamenti.iterator().next();
		totaleDocumento.setCodiceValuta(pagamentoRiferimento.getImporto().getCodiceValuta());
		totaleDocumento.setTassoDiCambio(pagamentoRiferimento.getImporto().getTassoDiCambio());

		for (Pagamento pagamentoOrigine : pagamentiOrigine) {

			// cancello i pagamenti non considerati per il bonifico fornitore
			if (deletePagamentiNonAccettati && !pagamenti.contains(pagamentoOrigine)) {
				try {
					panjeaDAO.delete(pagamentoOrigine);
				} catch (DAOException e) {
					logger.error(
							"Errore nel cancellare il pagamento per l'area pagamento collegata all'area bonifico da creare",
							e);
					throw new RuntimeException(
							"Errore nel cancellare il pagamento per l'area pagamento collegata all'area bonifico da creare",
							e);
				}
			} else {
				// imposto e salvo la data pagamento con la data bonifico fornitore
				pagamentoOrigine.setDataPagamento(dataDocumento);
				try {
					pagamentoOrigine = panjeaDAO.save(pagamentoOrigine);
				} catch (DAOException e) {
					logger.error(
							"Errore nel salvare il pagamento con nuova data per l'area pagamento collegata all'area bonifico da creare",
							e);
					throw new RuntimeException(
							"Errore nel salvare il pagamento con nuova data per l'area pagamento collegata all'area bonifico da creare",
							e);
				}

				// Sommo l'importo in valuta
				totaleDocumento.setImportoInValuta(totaleDocumento.getImportoInValuta().add(
						pagamentoOrigine.getImporto().getImportoInValuta()));
				// All'importo sommo anche l'importo forzato
				totaleDocumento.setImportoInValuta(totaleDocumento.getImportoInValuta().add(
						pagamentoOrigine.getImportoForzato().getImportoInValuta()));
				// Sommo l'importo in valuta azienda
				totaleDocumento.setImportoInValutaAzienda(totaleDocumento.getImportoInValutaAzienda().add(
						pagamentoOrigine.getImporto().getImportoInValutaAzienda()));
				// All'importo sommo anche l'importo in valuta forzato
				totaleDocumento.setImportoInValutaAzienda(totaleDocumento.getImportoInValutaAzienda().add(
						pagamentoOrigine.getImportoForzato().getImportoInValutaAzienda()));
			}
		}

		// imposto e salvo il totale calcolato con i nuovi pagamenti
		areaPagamenti.getDocumento().setTotale(totaleDocumento);
		areaPagamenti.setPagamenti(pagamenti);

		Documento documento = null;
		try {
			documento = panjeaDAO.save(areaPagamenti.getDocumento());
			areaPagamenti.setDocumento(documento);
		} catch (DAOException e) {
			logger.error(
					"Errore nel salvare il nuovo totale documento per l'area pagamento collegata all'area bonifico da creare",
					e);
			throw new RuntimeException(
					"Errore nel salvare il nuovo totale documento per l'area pagamento collegata all'area bonifico da creare",
					e);
		}
		return areaPagamenti;
	}

}
