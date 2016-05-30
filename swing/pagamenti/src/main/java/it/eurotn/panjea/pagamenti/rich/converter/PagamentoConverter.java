package it.eurotn.panjea.pagamenti.rich.converter;

import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.rich.converter.PanjeaConverter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;

public class PagamentoConverter extends PanjeaConverter {

	@Override
	public Object fromString(String string, ConverterContext context) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return Pagamento.class;
	}

	@Override
	public boolean supportFromString(String string, ConverterContext context) {
		return false;
	}

	@Override
	public boolean supportToString(Object object, ConverterContext context) {
		return true;
	}

	@Override
	public String toString(Object object, ConverterContext context) {

		String result = "";

		if (object != null && object instanceof Pagamento) {
			Pagamento pagamento = (Pagamento) object;

			StringBuilder sb = new StringBuilder();
			sb.append(RcpSupport.getMessage("importo"));
			sb.append(": ");
			sb.append(new DecimalFormat("#,##0.00 €").format(pagamento.getImporto().getImportoInValutaAzienda()));
			sb.append(" data: ");
			String data = pagamento.getDataPagamento() != null ? new SimpleDateFormat("dd/MM/yyyy").format(pagamento
					.getDataPagamento()) : "in lavorazione";
			sb.append(data);

			Rata rata = pagamento.getRata();
			if (rata != null && rata.getId() != null) {
				sb.append(RcpSupport.getMessage(Rata.class.getName()));
				sb.append(" n. ");
				sb.append(rata.getNumeroRata());
			}

			result = sb.toString();
		}

		return result;
	}

}
