package it.eurotn.panjea.bi.rich.editors.analisi.converter;

import it.eurotn.rich.converter.PanjeaConverter;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jidesoft.converter.ConverterContext;

public class ResourceDescriptorConverter extends PanjeaConverter<ResourceDescriptor> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return "";
	}

	@Override
	public Class<ResourceDescriptor> getClasse() {
		return ResourceDescriptor.class;
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
		return ((ResourceDescriptor) arg0).getLabel();
	}

}
