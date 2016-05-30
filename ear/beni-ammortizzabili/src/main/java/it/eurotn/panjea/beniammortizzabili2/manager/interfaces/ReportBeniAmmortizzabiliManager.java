package it.eurotn.panjea.beniammortizzabili2.manager.interfaces;

import it.eurotn.panjea.beniammortizzabili.exception.BeniAmmortizzabiliException;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileConFigli;
import it.eurotn.panjea.beniammortizzabili2.domain.CriteriaRicercaBeniAmmortizzabili;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.panjea.beniammortizzabili2.util.SituazioneBene;
import it.eurotn.panjea.beniammortizzabili2.util.registrobeni.RegistroBene;
import it.eurotn.panjea.beniammortizzabili2.util.venditeannuali.VenditaAnnualeBene;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface ReportBeniAmmortizzabiliManager {

	/**
	 * Carica il registro dei beni.
	 * 
	 * @param parameters
	 *            parametri
	 * @return beni caricati
	 */
	List<RegistroBene> caricaRegistroBeni(Map<String, Object> parameters);

	/**
	 * Carica la situazione dei beni.
	 * 
	 * @param parameters
	 *            parametri di caricamento.
	 * 
	 * @return situazione caricata
	 */
	List<SituazioneBene> caricaSituazioneBeni(Map<Object, Object> parameters);

	/**
	 * Carica tutti i beni con vendite effettuate nell'anno specificato nei
	 * parametri.
	 * 
	 * @param parameters
	 *            parametri
	 * @return beni caricati
	 */
	List<VenditaAnnualeBene> caricaVenditeAnnualiBeniPadri(Map<Object, Object> parameters);

	/**
	 * recupera la {@link List} {@link BeneAmmortizzabileConFigli} per il report
	 * dei beni acquistati.
	 * 
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            parametri di ricerca
	 * @return beni trovati
	 */
	List<BeneAmmortizzabileConFigli> ricercaBeniAcquistati(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);

	/**
	 * recupera la {@link List} di {@link QuotaAmmortamentoFiscale} per il
	 * report degli ammortamenti.
	 * 
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            parametri di ricerca
	 * @return quote trovate
	 * @throws BeniAmmortizzabiliException
	 *             BeniAmmortizzabiliException
	 */
	List<QuotaAmmortamentoFiscale> ricercaQuoteAmmortamento(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) throws BeniAmmortizzabiliException;

	/**
	 * recupera la {@link List} {@link BeneAmmortizzabileConFigli} per la
	 * rubrica beni.
	 * 
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            parametri di ricerca
	 * @return lista di beni trovati
	 */
	List<BeneAmmortizzabileConFigli> ricercaRubricaBeni(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);
}
