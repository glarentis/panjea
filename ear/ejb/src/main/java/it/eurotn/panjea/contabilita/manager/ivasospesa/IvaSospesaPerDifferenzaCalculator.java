package it.eurotn.panjea.contabilita.manager.ivasospesa;

import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IvaSospesaPerDifferenzaCalculator extends IvaSospesaCalculator {

	private List<TotaliCodiceIvaDTO> totaliPerDifferenza = null;

	@Override
	public List<TotaliCodiceIvaDTO> calcola(Pagamento pagamento, AreaIva areaIva, AreaRate areaRate) {
		List<Pagamento> pagamenti = areaRate.getPagamenti();
		// preparo la lista di righe per codice iva con imponibile e imposta della rigaIva
		initTotaliCodiceIva(areaIva);

		// devo calcolare per percentuale tutti i pagamenti precedendi all'ultimo, quindi sottraggo al totale i nuovi
		// valori percentuali trovati
		IvaSospesaPerPercentualeCalculator calculatorPerc = new IvaSospesaPerPercentualeCalculator();
		for (Pagamento pagamentoCorrente : pagamenti) {
			if (!pagamentoCorrente.equals(pagamento)) {
				List<TotaliCodiceIvaDTO> totaliPerPagamento = calculatorPerc.calcola(pagamentoCorrente, areaIva,
						areaRate);
				sottraiRisultatiDaTotali(totaliPerPagamento);
			}
		}
		return totaliPerDifferenza;
	}

	/**
	 * Inizializza per l'area iva la lista di totali raggruppati per codice iva.
	 * 
	 * @param areaIva
	 *            l'area da cui trovare le righe iva
	 */
	private void initTotaliCodiceIva(AreaIva areaIva) {
		totaliPerDifferenza = new ArrayList<TotaliCodiceIvaDTO>();
		for (RigaIva rigaIva : areaIva.getRigheIva()) {
			if (rigaIva.getCodiceIva().isIvaSospesa()) {
				BigDecimal imponibile = rigaIva.getImponibile().getImportoInValuta();
				BigDecimal imposta = rigaIva.getImposta().getImportoInValuta();

				TotaliCodiceIvaDTO totCodIva = new TotaliCodiceIvaDTO();
				totCodIva.setImponibile(imponibile);
				totCodIva.setImposta(imposta);
				totCodIva.setIdCodiceIva(rigaIva.getCodiceIva().getId());
				totCodIva.setCodiceIva(rigaIva.getCodiceIva().getCodice());
				totCodIva.setDescrizioneRegistro(rigaIva.getCodiceIva().getDescrizioneRegistro());
				totCodIva.setPercApplicazione(rigaIva.getCodiceIva().getPercApplicazione());
				totCodIva.setPercIndetraibilita(rigaIva.getCodiceIva().getPercIndetraibilita());
				totCodIva.setConsideraPerLiquidazione(!rigaIva.getCodiceIva().isIvaSospesa());

				totaliPerDifferenza.add(totCodIva);
			}
		}
	}

	/**
	 * Sottrae dalla lista di totaliCodiceIva di base il valore di imposta e imponibile della lista passata come
	 * parametro per codici iva uguali.
	 * 
	 * @param totaliDaRimuovere
	 *            la lista con i valori per codice iva da rimuovere alla lista base
	 */
	private void sottraiRisultatiDaTotali(List<TotaliCodiceIvaDTO> totaliDaRimuovere) {
		for (TotaliCodiceIvaDTO totCodiceIva : totaliPerDifferenza) {
			for (TotaliCodiceIvaDTO totDaRimuovere : totaliDaRimuovere) {
				if (totCodiceIva.getIdCodiceIva().equals(totDaRimuovere.getIdCodiceIva())) {
					totCodiceIva.setImponibile(totCodiceIva.getImponibile()
							.add(totDaRimuovere.getImponibile().negate()));
					totCodiceIva.setImposta(totCodiceIva.getImposta().add(totDaRimuovere.getImposta().negate()));
				}
			}
		}
	}

}
