package it.eurotn.panjea.agenti.rich.editors.entita;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.rich.converter.PanjeaConverter;
import it.eurotn.util.PanjeaEJBUtil;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

import com.jidesoft.converter.ConverterContext;

public class AgentePMConverter extends PanjeaConverter {

	public class AgentePMToAgenteConverter extends AbstractConverter {

		@SuppressWarnings("rawtypes")
		@Override
		protected Object doConvert(Object obj, Class class1, ConversionContext conversioncontext) throws Exception {
			Agente agente = new Agente();
			Entita entita = ((AgenteCollegatoPM) obj).getAgente();
			PanjeaEJBUtil.copyProperties(agente, entita);
			return agente;
		}

		@Override
		public Class<?>[] getSourceClasses() {
			return new Class[] { AgenteCollegatoPM.class };
		}

		@Override
		public Class<?>[] getTargetClasses() {
			return new Class[] { Agente.class };
		}

	}

	/**
	 *
	 * Costruttore.
	 */
	public AgentePMConverter() {
		this.addSpringConverter(new AgentePMToAgenteConverter());
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return AgenteCollegatoPM.class;
	}

	@Override
	public boolean supportFromString(String arg0, ConverterContext arg1) {
		return false;
	}

	@Override
	public boolean supportToString(Object arg0, ConverterContext arg1) {
		return false;
	}

	@Override
	public String toString(Object arg0, ConverterContext arg1) {
		return null;
	}

}
