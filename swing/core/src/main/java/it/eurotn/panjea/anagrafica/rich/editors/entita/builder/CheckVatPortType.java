/**
 * CheckVatPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.anagrafica.rich.editors.entita.builder;

public interface CheckVatPortType extends java.rmi.Remote {
	public void checkVat(javax.xml.rpc.holders.StringHolder countryCode, javax.xml.rpc.holders.StringHolder vatNumber,
			org.apache.axis.holders.DateHolder requestDate, javax.xml.rpc.holders.BooleanHolder valid,
			javax.xml.rpc.holders.StringHolder name, javax.xml.rpc.holders.StringHolder address)
			throws java.rmi.RemoteException;

	public void checkVatApprox(javax.xml.rpc.holders.StringHolder countryCode,
			javax.xml.rpc.holders.StringHolder vatNumber, javax.xml.rpc.holders.StringHolder traderName,
			javax.xml.rpc.holders.StringHolder traderCompanyType, javax.xml.rpc.holders.StringHolder traderStreet,
			javax.xml.rpc.holders.StringHolder traderPostcode, javax.xml.rpc.holders.StringHolder traderCity,
			java.lang.String requesterCountryCode, java.lang.String requesterVatNumber,
			org.apache.axis.holders.DateHolder requestDate, javax.xml.rpc.holders.BooleanHolder valid,
			javax.xml.rpc.holders.StringHolder traderAddress, MatchCodeHolder traderNameMatch,
			MatchCodeHolder traderCompanyTypeMatch, MatchCodeHolder traderStreetMatch,
			MatchCodeHolder traderPostcodeMatch, MatchCodeHolder traderCityMatch,
			javax.xml.rpc.holders.StringHolder requestIdentifier) throws java.rmi.RemoteException;
}
