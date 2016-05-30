/**
 * GetStatoProcessoConservazioneResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class GetStatoProcessoConservazioneResponse implements java.io.Serializable {
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

	private GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult getStatoProcessoConservazioneResult;

	private java.lang.Object __equalsCalc = null;

	private boolean __hashCodeCalc = false;
	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			GetStatoProcessoConservazioneResponse.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/",
				">GetStatoProcessoConservazioneResponse"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("getStatoProcessoConservazioneResult");
		elemField
				.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetStatoProcessoConservazioneResult"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetStatoProcessoConservazioneResponse>GetStatoProcessoConservazioneResult"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public GetStatoProcessoConservazioneResponse() {
	}

	public GetStatoProcessoConservazioneResponse(
			GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult getStatoProcessoConservazioneResult) {
		this.getStatoProcessoConservazioneResult = getStatoProcessoConservazioneResult;
	}

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof GetStatoProcessoConservazioneResponse)) {
			return false;
		}
		GetStatoProcessoConservazioneResponse other = (GetStatoProcessoConservazioneResponse) obj;
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
		_equals = true && ((this.getStatoProcessoConservazioneResult == null && other
				.getGetStatoProcessoConservazioneResult() == null) || (this.getStatoProcessoConservazioneResult != null && this.getStatoProcessoConservazioneResult
				.equals(other.getGetStatoProcessoConservazioneResult())));
		__equalsCalc = null;
		return _equals;
	}

	/**
	 * Gets the getStatoProcessoConservazioneResult value for this GetStatoProcessoConservazioneResponse.
	 * 
	 * @return getStatoProcessoConservazioneResult
	 */
	public GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult getGetStatoProcessoConservazioneResult() {
		return getStatoProcessoConservazioneResult;
	}

	@Override
	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getGetStatoProcessoConservazioneResult() != null) {
			_hashCode += getGetStatoProcessoConservazioneResult().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	/**
	 * Sets the getStatoProcessoConservazioneResult value for this GetStatoProcessoConservazioneResponse.
	 * 
	 * @param getStatoProcessoConservazioneResult
	 */
	public void setGetStatoProcessoConservazioneResult(
			GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult getStatoProcessoConservazioneResult) {
		this.getStatoProcessoConservazioneResult = getStatoProcessoConservazioneResult;
	}

}
