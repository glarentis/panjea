package it.gov.fatturapa.sdi.fatturapa.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.StringUtils;

public class EmptyStringAdapter extends XmlAdapter<String, String> {

	@Override
	public String marshal(String arg0) throws Exception {

		if (StringUtils.isBlank(arg0)) {
			return null;
		}

		return arg0;
	}

	@Override
	public String unmarshal(String arg0) throws Exception {
		return arg0;
	}

}
