package it.eurotn.panjea.beniammortizzabili.rich.editors.beni.bene;

import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;

import java.util.Collection;
import java.util.List;

public class QuoteFiscaliNonConsolidateTablePage extends QuoteFiscaliTablePage {

	public static final String PAGE_ID = "quoteFiscaliNonConsolidateTablePage";

	/**
	 * Costruttore.
	 * 
	 */
	public QuoteFiscaliNonConsolidateTablePage() {
		super(PAGE_ID);
	}

	@Override
	public Collection<QuotaAmmortamentoFiscale> loadTableData() {
		List<QuotaAmmortamentoFiscale> listQuote = null;
		if (beneAmmortizzabile != null) {
			listQuote = getBeniAmmortizzabiliBD().caricaQuoteAmmortamentoFiscaliNonConsolidate(beneAmmortizzabile);
		}

		return listQuote;
	}
}
