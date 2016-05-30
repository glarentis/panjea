package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.rich.converter.PanjeaConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class ConfigurazioneDistintaConverter extends PanjeaConverter<ConfigurazioneDistinta> implements
		Comparator<ConfigurazioneDistinta> {

	@Override
	public int compare(ConfigurazioneDistinta o1, ConfigurazioneDistinta o2) {
		return ConfigurazioneDistintaConverter.this.toString(o1, null).compareTo(
				ConfigurazioneDistintaConverter.this.toString(o2, null));
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<ConfigurazioneDistinta> getClasse() {
		return ConfigurazioneDistinta.class;
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
		String nome = "";
		if (arg0 != null && arg0 instanceof ConfigurazioneDistinta) {
			nome = ((ConfigurazioneDistinta) arg0).getNome();
		}
		return nome;
	}

}
