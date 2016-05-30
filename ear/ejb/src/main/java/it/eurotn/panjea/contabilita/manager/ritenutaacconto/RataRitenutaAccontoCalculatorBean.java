/**
 * 
 */
package it.eurotn.panjea.contabilita.manager.ritenutaacconto;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.manager.interfaces.ValutaManager;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RataRitenutaAccontoCalculator;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoContabilitaManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.RataRitenutaAccontoCalculator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RataRitenutaAccontoCalculator")
public class RataRitenutaAccontoCalculatorBean implements RataRitenutaAccontoCalculator {

	@EJB
	private AreaRateManager areaRateManager;

	@EJB
	private RitenutaAccontoContabilitaManager ritenutaAccontoContabilitaManager;

	@EJB
	private ValutaManager valutaManager;

	@Override
	public Date calcolaDataScadenza(Date dataPagamento) {

		dataPagamento = PanjeaEJBUtil.getDateTimeToZero(dataPagamento);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataPagamento);
		calendar.set(Calendar.DAY_OF_MONTH, 16);
		calendar.add(Calendar.MONTH, 1);

		return calendar.getTime();
	}

	@Override
	public Importo calcolaImportoRata(Pagamento pagamento) {
		// ricarico l'area rate
		AreaRate areaRate = areaRateManager.caricaAreaRate(pagamento.getRata().getAreaRate().getId());
		boolean tutteRatePagate = false;
		BigDecimal totaleRate = BigDecimal.ZERO;
		BigDecimal totaleRateRitenuta = BigDecimal.ZERO;
		for (Rata rata : areaRate.getRate()) {
			if (!rata.isRitenutaAcconto()) {
				totaleRate = totaleRate.add(rata.getImporto().getImportoInValutaAzienda());
				tutteRatePagate = tutteRatePagate
						&& (rata.getStatoRata() == StatoRata.CHIUSA || rata.getStatoRata() == StatoRata.RIEMESSA);
			} else {
				totaleRateRitenuta = totaleRateRitenuta.add(rata.getImporto().getImportoInValutaAzienda());
			}
		}

		BigDecimal importoRitenuta = ritenutaAccontoContabilitaManager.getImportoRitenutaAcconto(areaRate
				.getDocumento());

		Importo importo = new Importo(valutaManager.caricaValutaAziendaCorrente().getCodiceValuta(), BigDecimal.ONE);
		// se tutte le rate sono pagate creo la rata della ritenuta con importo pari a
		// importo ritenuta - importo rate ritenute già presenti
		// altrimenti l'importo sarà in proporzione al pagamento
		if (tutteRatePagate) {
			importo.setImportoInValuta(importoRitenuta.subtract(totaleRateRitenuta));
		} else {

			BigDecimal importoPag = pagamento.getImporto().getImportoInValutaAzienda();
			importoPag = importoPag.add(pagamento.getImportoForzato().getImportoInValutaAzienda());

			// trovo la percentuale
			BigDecimal percPagamento = importoPag.multiply(new BigDecimal(100)).divide(totaleRate, 2,
					BigDecimal.ROUND_HALF_UP);

			BigDecimal impPerc = importoRitenuta.multiply(percPagamento).divide(new BigDecimal(100), 2,
					BigDecimal.ROUND_HALF_UP);

			importo.setImportoInValuta(impPerc);
		}
		importo.calcolaImportoValutaAzienda(2);

		return importo;
	}
}
