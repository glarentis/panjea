/**
 * GetTransazioniSdiFatturaPAResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class GetTransazioniSdiFatturaPAResponse implements java.io.Serializable {
	/**
	 * Get Custom Deserializer
	 */
	public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Serializer
	 */
	public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}

	private GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult getTransazioniSdiFatturaPAResult;

	private java.lang.Object __equalsCalc = null;

	private boolean __hashCodeCalc = false;
	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			GetTransazioniSdiFatturaPAResponse.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetTransazioniSdiFatturaPAResponse"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("getTransazioniSdiFatturaPAResult");
		elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetTransazioniSdiFatturaPAResult"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetTransazioniSdiFatturaPAResponse>GetTransazioniSdiFatturaPAResult"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public GetTransazioniSdiFatturaPAResponse() {
	}

	public GetTransazioniSdiFatturaPAResponse(
			GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult getTransazioniSdiFatturaPAResult) {
		this.getTransazioniSdiFatturaPAResult = getTransazioniSdiFatturaPAResult;
	}

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof GetTransazioniSdiFatturaPAResponse)) {
			return false;
		}
		GetTransazioniSdiFatturaPAResponse other = (GetTransazioniSdiFatturaPAResponse) obj;
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.getTransazioniSdiFatturaPAResult == null && other
				.getGetTransazioniSdiFatturaPAResult() == null) || (this.getTransazioniSdiFatturaPAResult != null && this.getTransazioniSdiFatturaPAResult
				.equals(other.getGetTransazioniSdiFatturaPAResult())));
		__equalsCalc = null;
		return _equals;
	}

	/**
	 * Gets the getTransazioniSdiFatturaPAResult value for this GetTransazioniSdiFatturaPAResponse.
	 * 
	 * @return getTransazioniSdiFatturaPAResult
	 */
	public GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult getGetTransazioniSdiFatturaPAResult() {
		return getTransazioniSdiFatturaPAResult;
	}

	@Override
	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getGetTransazioniSdiFatturaPAResult() != null) {
			_hashCode += getGetTransazioniSdiFatturaPAResult().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	/**
	 * Sets the getTransazioniSdiFatturaPAResult value for this GetTransazioniSdiFatturaPAResponse.
	 * 
	 * @param getTransazioniSdiFatturaPAResult
	 */
	public void setGetTransazioniSdiFatturaPAResult(
			GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult getTransazioniSdiFatturaPAResult) {
		this.getTransazioniSdiFatturaPAResult = getTransazioniSdiFatturaPAResult;
	}

}
