/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.bd;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileConFigli;
import it.eurotn.panjea.beniammortizzabili2.domain.CriteriaRicercaBeniAmmortizzabili;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.panjea.beniammortizzabili2.domain.VenditaBene;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.List;

/**
 * Interfaccia Business Delegate per ReportBeniAmmortizzabiliService.
 * 
 * @author adriano
 * @version 1.0, 24/nov/06
 * 
 */
public interface IReportBeniAmmortizzabiliBD {

	@AsyncMethodInvocation
	List<BeneAmmortizzabileConFigli> ricercaBeniAcquistati(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);

	@AsyncMethodInvocation
	List<QuotaAmmortamentoFiscale> ricercaQuoteAmmortamento(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);

	@AsyncMethodInvocation
	List<BeneAmmortizzabileConFigli> ricercaRubricaBeni(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);

	@AsyncMethodInvocation
	List<VenditaBene> ricercaVenditeBeni(CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);
}
