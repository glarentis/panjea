package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.rich.converter.EnumConverter;

public class TipoListinoConverter extends EnumConverter {

	@Override
	public Class<?> getClasse() {

		return ETipoListino.class;
	}

}
