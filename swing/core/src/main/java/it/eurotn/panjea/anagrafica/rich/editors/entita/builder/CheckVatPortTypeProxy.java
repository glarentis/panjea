package it.eurotn.panjea.anagrafica.rich.editors.entita.builder;

public class CheckVatPortTypeProxy implements it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatPortType {
	private String _endpoint = null;
	private it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatPortType checkVatPortType = null;

	public CheckVatPortTypeProxy() {
		_initCheckVatPortTypeProxy();
	}

	public CheckVatPortTypeProxy(String endpoint) {
		_endpoint = endpoint;
		_initCheckVatPortTypeProxy();
	}

	private void _initCheckVatPortTypeProxy() {
		try {
			checkVatPortType = (new it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatServiceLocator())
					.getcheckVatPort();
			if (checkVatPortType != null) {
				if (_endpoint != null) {
					((javax.xml.rpc.Stub) checkVatPortType)._setProperty("javax.xml.rpc.service.endpoint.address",
							_endpoint);
				} else {
					_endpoint = (String) ((javax.xml.rpc.Stub) checkVatPortType)
							._getProperty("javax.xml.rpc.service.endpoint.address");
				}
			}

		} catch (javax.xml.rpc.ServiceException serviceException) {
		}
	}

	public void checkVat(javax.xml.rpc.holders.StringHolder countryCode, javax.xml.rpc.holders.StringHolder vatNumber,
			org.apache.axis.holders.DateHolder requestDate, javax.xml.rpc.holders.BooleanHolder valid,
			javax.xml.rpc.holders.StringHolder name, javax.xml.rpc.holders.StringHolder address)
			throws java.rmi.RemoteException {
		if (checkVatPortType == null) {
			_initCheckVatPortTypeProxy();
		}
		checkVatPortType.checkVat(countryCode, vatNumber, requestDate, valid, name, address);
	}

	public void checkVatApprox(javax.xml.rpc.holders.StringHolder countryCode,
			javax.xml.rpc.holders.StringHolder vatNumber, javax.xml.rpc.holders.StringHolder traderName,
			javax.xml.rpc.holders.StringHolder traderCompanyType, javax.xml.rpc.holders.StringHolder traderStreet,
			javax.xml.rpc.holders.StringHolder traderPostcode, javax.xml.rpc.holders.StringHolder traderCity,
			java.lang.String requesterCountryCode, java.lang.String requesterVatNumber,
			org.apache.axis.holders.DateHolder requestDate, javax.xml.rpc.holders.BooleanHolder valid,
			javax.xml.rpc.holders.StringHolder traderAddress, MatchCodeHolder traderNameMatch,
			MatchCodeHolder traderCompanyTypeMatch, MatchCodeHolder traderStreetMatch,
			MatchCodeHolder traderPostcodeMatch, MatchCodeHolder traderCityMatch,
			javax.xml.rpc.holders.StringHolder requestIdentifier) throws java.rmi.RemoteException {
		if (checkVatPortType == null) {
			_initCheckVatPortTypeProxy();
		}
		checkVatPortType.checkVatApprox(countryCode, vatNumber, traderName, traderCompanyType, traderStreet,
				traderPostcode, traderCity, requesterCountryCode, requesterVatNumber, requestDate, valid,
				traderAddress, traderNameMatch, traderCompanyTypeMatch, traderStreetMatch, traderPostcodeMatch,
				traderCityMatch, requestIdentifier);
	}

	public it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatPortType getCheckVatPortType() {
		if (checkVatPortType == null) {
			_initCheckVatPortTypeProxy();
		}
		return checkVatPortType;
	}

	public String getEndpoint() {
		return _endpoint;
	}

	public void setEndpoint(String endpoint) {
		_endpoint = endpoint;
		if (checkVatPortType != null) {
			((javax.xml.rpc.Stub) checkVatPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
		}

	}

}