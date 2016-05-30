package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.rich.converter.PanjeaConverter;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;

public class TipoMovimentoConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return TipoMovimento.class;
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
	public String toString(Object arg0, ConverterContext arg1) {

		String result = "";

		if (arg0 != null && arg0 instanceof TipoMovimento) {
			TipoMovimento tipoMovimento = (TipoMovimento) arg0;
			result = RcpSupport.getMessage(tipoMovimento.getClass().getName() + "." + tipoMovimento.name());
		}

		return result;
	}

}
