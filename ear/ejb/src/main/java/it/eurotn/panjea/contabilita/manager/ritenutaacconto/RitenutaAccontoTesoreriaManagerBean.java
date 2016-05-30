/**
 *
 */
package it.eurotn.panjea.contabilita.manager.ritenutaacconto;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.manager.interfaces.ValutaManager;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RataRitenutaAccontoCalculator;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoContabilitaManager;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoTesoreriaManager;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipologiaPartita;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.manager.interfaces.RateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.RitenutaRitenutaAccontoTesoreriaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RitenutaAccontoTesoreriaManager")
public class RitenutaAccontoTesoreriaManagerBean implements RitenutaAccontoTesoreriaManager {

	private static Logger logger = Logger.getLogger(RitenutaAccontoTesoreriaManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private RitenutaAccontoContabilitaManager ritenutaAccontoContabilitaManager;

	@EJB
	private RateManager rateManager;

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	private ValutaManager valutaManager;

	@EJB
	private RataRitenutaAccontoCalculator rataRitenutaAccontoCalculator;

	@Override
	public void aggiornaRataRitenutaAcconto(Pagamento pagamento) {

		// verifico se il documento gestisce le ritenute d'acconto e se il papgamento non è quello di una rata di
		// ritenuta (in quel caso non serve ricrearla perchè non cambia niente)
		AreaContabile areaContabile = areaContabileManager.caricaAreaContabileByDocumento(pagamento.getRata()
				.getAreaRate().getDocumento().getId());
		if (areaContabile == null || pagamento.getRata().isRitenutaAcconto()
				|| !areaContabile.getDatiRitenutaAccontoAreaContabile().isCausaleRitenutaPresente()) {
			return;
		}

		// verifico se il pagamento è già associato ad una rata e in caso positivo la cancello
		Rata rataRitenuta = caricaRataRitenutaAcconto(pagamento);
		if (rataRitenuta != null) {
			try {
				rateManager.cancellaRata(rataRitenuta);
			} catch (DocumentiCollegatiPresentiException e) {
				logger.warn("Non posso cancellare la rata della ritenuta d'acconto perchè ha dei documenti collegati.");
				throw new RuntimeException(e);
			}
		}

		// cancello la rata della ritenuta senza data
		cancellaRataRitenutaSenzaDataScadenza(pagamento.getRata().getAreaRate());

		// creo la rata acconto legata al pagamento
		Importo importo = rataRitenutaAccontoCalculator.calcolaImportoRata(pagamento);
		Date dataScadenza = rataRitenutaAccontoCalculator.calcolaDataScadenza(pagamento.getDataPagamento());
		String tributo = areaContabile.getDatiRitenutaAccontoAreaContabile().getTributo();
		String sezione = areaContabile.getDatiRitenutaAccontoAreaContabile().getSezione();
		Integer numeroRata = getMaxNumeroRata(pagamento.getRata().getAreaRate()) + 1;

		Rata rata = creaNuovaRataRA(pagamento.getRata().getAreaRate(), numeroRata, dataScadenza, importo, tributo,
				sezione);
		rata.getDatiRitenutaAccontoRata().setPagamentoRiferimento(pagamento);
		rateManager.salvaRataNoCheck(rata);

		// creo la rata con data nulla per la rimanenza
		numeroRata = numeroRata + 1;
		Rata rataDataNull = creaRataRitenutaAcconto(areaContabile, pagamento.getRata().getAreaRate(), numeroRata);
		if (rataDataNull != null) {
			rateManager.salvaRataNoCheck(rataDataNull);
		}
	}

	@Override
	public void cancellaRataRitenutaAccontoAssociata(AreaPagamenti areaPagamenti) {

		for (Pagamento pagamento : areaPagamenti.getPagamenti()) {

			// verifico se il pagamento è già associato ad una rata e in caso positivo la cancello
			Rata rataRitenuta = caricaRataRitenutaAcconto(pagamento);
			if (rataRitenuta != null) {
				try {
					rateManager.cancellaRata(rataRitenuta);
				} catch (DocumentiCollegatiPresentiException e) {
					logger.warn("Non posso cancellare la rata della ritenuta d'acconto perchè ha dei documenti collegati.");
					throw new RuntimeException(e);
				}

				// cancello la rata della ritenuta senza data per ricrearla con l'importo esatto
				cancellaRataRitenutaSenzaDataScadenza(pagamento.getRata().getAreaRate());

				// ricreo la rata con l'importo aggiornato
				AreaContabile areaContabile = areaContabileManager.caricaAreaContabileByDocumento(pagamento.getRata()
						.getAreaRate().getDocumento().getId());
				Integer numeroRata = getMaxNumeroRata(pagamento.getRata().getAreaRate()) + 1;
				Rata rata = creaRataRitenutaAcconto(areaContabile, pagamento.getRata().getAreaRate(), numeroRata);
				if (rata != null) {
					rateManager.salvaRataNoCheck(rata);
				}
			}
		}

	}

	/**
	 * Cancella la rata creata per la ritenuta d'acconto senza la data di scadenza.
	 *
	 * @param areaRate
	 *            area rate
	 */
	private void cancellaRataRitenutaSenzaDataScadenza(AreaRate areaRate) {
		logger.debug("--> Enter cancellaRataRitenutaSenzaDataScadenza");

		StringBuilder sb = new StringBuilder();
		sb.append("select r from Rata r where r.areaRate = :paramAreaRate and r.ritenutaAcconto = true and r.dataScadenza is null ");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramAreaRate", areaRate);

		Rata rata = null;
		try {
			rata = (Rata) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			// non c'è nessuna rata
			rata = null;
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento della rata.", e);
			throw new RuntimeException("errore durante il caricamento della rata.", e);
		}

		if (rata != null) {
			try {
				rateManager.cancellaRata(rata);
			} catch (DocumentiCollegatiPresentiException e) {
				logger.warn("Non posso cancellare la rata della ritenuta d'acconto perchè ha dei documenti collegati.");
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Carica l'importo di tutte le rate della ritenuta d'acconto che hanno la data di scadenza.
	 *
	 * @param areaContabile
	 *            area contabile
	 * @return importo
	 */
	private BigDecimal caricaImportoRateRitenutaConData(AreaContabile areaContabile) {
		logger.debug("--> Enter caricaImportoRateRitenutaConData");

		BigDecimal importo = BigDecimal.ZERO;

		StringBuilder sb = new StringBuilder();
		sb.append("select coalesce(sum(r.importoInValutaAzienda),0) from part_rate r inner join part_area_partite ap on r.areaRate_id = ap.id ");
		sb.append("										    inner join docu_documenti doc on doc.id = ap.documento_id ");
		sb.append("where r.dataScadenza is not null and r.ritenutaAcconto = true and doc.id = :paramIdDoc ");

		Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
		query.setParameter("paramIdDoc", areaContabile.getDocumento().getId());

		try {
			importo = (BigDecimal) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento del importo delle rate ritenuta d'acconto presenti.", e);
			throw new RuntimeException(
					"errore durante il caricamento del importo delle rate ritenuta d'acconto presenti.", e);
		}

		return importo;
	}

	@Override
	public Rata caricaRataRitenutaAcconto(Pagamento pagamentoRiferimento) {
		logger.debug("--> Enter caricaRataRitenutaAcconto");

		Rata rata = null;

		StringBuilder sb = new StringBuilder();
		sb.append("select r from Rata r where r.datiRitenutaAccontoRata.pagamentoRiferimento = :paramPag ");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramPag", pagamentoRiferimento);

		try {
			rata = (Rata) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			// il pagamento non fa ancora riferimento a nessuna rata
			rata = null;
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento della rata ritenuta d'acconto.", e);
			throw new RuntimeException("errore durante il caricamento della rata ritenuta d'acconto.", e);
		}

		return rata;
	}

	/**
	 * Crea la rata relativa alla ritenuta d'acconto.
	 *
	 * @param areaRate
	 *            area rate
	 * @param numero
	 *            numero rata
	 * @param dataScadenza
	 *            data scadenza
	 * @param importo
	 *            importo
	 * @param tributo
	 *            tributo
	 * @param sezione
	 *            sezione
	 * @return rata creata
	 */
	private Rata creaNuovaRataRA(AreaRate areaRate, Integer numero, Date dataScadenza, Importo importo, String tributo,
			String sezione) {

		Rata rata = new Rata();
		rata.setAreaRate(areaRate);
		rata.setDataScadenza(dataScadenza);
		rata.getDatiRitenutaAccontoRata().setTributo(tributo);
		rata.getDatiRitenutaAccontoRata().setSezione(sezione);
		rata.setImporto(importo);
		rata.setNumeroRata(numero);
		rata.setRitenutaAcconto(true);
		rata.setTipologiaPartita(TipologiaPartita.NORMALE);
		rata.setTipoPagamento(TipoPagamento.F24);

		return rata;
	}

	@Override
	public Rata creaRataRitenutaAcconto(AreaContabile areaContabile, AreaRate areaRate, Integer numeroRata) {

		Rata rataGenerata = null;

		if (areaContabile.getDatiRitenutaAccontoAreaContabile().isCausaleRitenutaPresente()) {

			// carico l'importo della riga dell'erario temporaneo per ottenere l'importo della rata da creare
			BigDecimal importoRiga = ritenutaAccontoContabilitaManager.getImportoRitenutaAcconto(areaContabile
					.getDocumento());
			BigDecimal importoRighePresenti = caricaImportoRateRitenutaConData(areaContabile);

			Importo importoRata = new Importo(valutaManager.caricaValutaAziendaCorrente().getCodiceValuta(),
					BigDecimal.ONE);
			importoRata.setImportoInValuta(importoRiga.subtract(importoRighePresenti));
			importoRata.calcolaImportoValutaAzienda(2);

			if (importoRata.getImportoInValuta().compareTo(BigDecimal.ZERO) != 0) {

				String tributo = areaContabile.getDatiRitenutaAccontoAreaContabile().getTributo();
				String sezione = areaContabile.getDatiRitenutaAccontoAreaContabile().getSezione();

				// creo la rata della ritenuta d'acconto.
				rataGenerata = creaNuovaRataRA(areaRate, numeroRata, null, importoRata, tributo, sezione);
			}
		}

		return rataGenerata;
	}

	/**
	 * Restituisce il più alto numero di rata dell'area.
	 *
	 * @param areaRate
	 *            area arate
	 * @return numero rata
	 */
	private Integer getMaxNumeroRata(AreaRate areaRate) {

		StringBuilder sb = new StringBuilder();
		sb.append("select max(r.numeroRata) from Rata r where r.areaRate = :paramAreaRate ");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramAreaRate", areaRate);

		Integer numMax = new Integer(0);
		try {
			numMax = (Integer) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento del numero rate", e);
			throw new RuntimeException("errore durante il caricamento del numero rate", e);
		}

		return numMax;
	}

}
