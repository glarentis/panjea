package it.eurotn.panjea.contabilita.manager.ivasospesa;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class IvaSospesaPerPercentualeCalculator extends IvaSospesaCalculator {

	@Override
	public List<TotaliCodiceIvaDTO> calcola(Pagamento pagamento, AreaIva areaIva, AreaRate areaRate) {
		// tot parte iva che e' la somma di imposta + imponibile per ogni riga iva presente
		BigDecimal totIva = areaIva.getTotale();
		// totale del pagamento
		BigDecimal totPagamento = pagamento.getImporto().getImportoInValuta();

		// trovo la percentuale pagata rispetto al totale iva
		BigDecimal percentualePagata = totPagamento.divide(totIva, RoundingMode.HALF_UP).multiply(Importo.HUNDRED);

		List<TotaliCodiceIvaDTO> righeCodiciIva = new ArrayList<TotaliCodiceIvaDTO>();

		// per ogni riga iva metto da parte la percentuale pagata di imposta e imponibile
		for (RigaIva rigaIva : areaIva.getRigheIva()) {
			if (rigaIva.getCodiceIva().isIvaSospesa()) {
				BigDecimal imponibile = rigaIva.getImponibile().getImportoInValuta();
				imponibile = imponibile.multiply(percentualePagata).divide(Importo.HUNDRED, 2, RoundingMode.HALF_UP);
				BigDecimal imposta = rigaIva.getImposta().getImportoInValuta();
				imposta = imposta.multiply(percentualePagata).divide(Importo.HUNDRED, 2, RoundingMode.HALF_UP);

				TotaliCodiceIvaDTO totCodIva = new TotaliCodiceIvaDTO();
				totCodIva.setImponibile(imponibile);
				totCodIva.setImposta(imposta);
				totCodIva.setIdCodiceIva(rigaIva.getCodiceIva().getId());
				totCodIva.setCodiceIva(rigaIva.getCodiceIva().getCodice());
				totCodIva.setDescrizioneRegistro(rigaIva.getCodiceIva().getDescrizioneRegistro());
				totCodIva.setPercApplicazione(rigaIva.getCodiceIva().getPercApplicazione());
				totCodIva.setPercIndetraibilita(rigaIva.getCodiceIva().getPercIndetraibilita());
				totCodIva.setConsideraPerLiquidazione(!rigaIva.getCodiceIva().isIvaSospesa());

				righeCodiciIva.add(totCodIva);
			}
		}
		return righeCodiciIva;
	}

}
