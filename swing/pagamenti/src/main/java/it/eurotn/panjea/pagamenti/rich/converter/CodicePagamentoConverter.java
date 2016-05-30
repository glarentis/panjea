package it.eurotn.panjea.pagamenti.rich.converter;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class CodicePagamentoConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return CodicePagamento.class;
	}

	@Override
	public boolean supportFromString(String arg0, ConverterContext arg1) {
		return false;
	}

	@Override
	public boolean supportToString(Object arg0, ConverterContext arg1) {
		return true;
	}

	@Override
	public String toString(Object value, ConverterContext arg1) {

		String result = "";

		if (value != null && value instanceof CodicePagamento) {

			CodicePagamento pagamento = (CodicePagamento) value;
			result = (pagamento.getCodicePagamento() == null) ? "" : pagamento.getCodicePagamento() + " - ";
			result = (pagamento.getDescrizione() == null) ? "" : pagamento.getDescrizione();
		}

		return result;
	}
}
