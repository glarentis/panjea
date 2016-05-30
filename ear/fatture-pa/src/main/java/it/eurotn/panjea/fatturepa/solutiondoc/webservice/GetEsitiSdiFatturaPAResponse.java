/**
 * GetEsitiSdiFatturaPAResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class GetEsitiSdiFatturaPAResponse implements java.io.Serializable {
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

	private GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult getEsitiSdiFatturaPAResult;

	private java.lang.Object __equalsCalc = null;

	private boolean __hashCodeCalc = false;
	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			GetEsitiSdiFatturaPAResponse.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetEsitiSdiFatturaPAResponse"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("getEsitiSdiFatturaPAResult");
		elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetEsitiSdiFatturaPAResult"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetEsitiSdiFatturaPAResponse>GetEsitiSdiFatturaPAResult"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public GetEsitiSdiFatturaPAResponse() {
	}

	public GetEsitiSdiFatturaPAResponse(
			GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult getEsitiSdiFatturaPAResult) {
		this.getEsitiSdiFatturaPAResult = getEsitiSdiFatturaPAResult;
	}

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof GetEsitiSdiFatturaPAResponse)) {
			return false;
		}
		GetEsitiSdiFatturaPAResponse other = (GetEsitiSdiFatturaPAResponse) obj;
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
		_equals = true && ((this.getEsitiSdiFatturaPAResult == null && other.getGetEsitiSdiFatturaPAResult() == null) || (this.getEsitiSdiFatturaPAResult != null && this.getEsitiSdiFatturaPAResult
				.equals(other.getGetEsitiSdiFatturaPAResult())));
		__equalsCalc = null;
		return _equals;
	}

	/**
	 * Gets the getEsitiSdiFatturaPAResult value for this GetEsitiSdiFatturaPAResponse.
	 * 
	 * @return getEsitiSdiFatturaPAResult
	 */
	public GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult getGetEsitiSdiFatturaPAResult() {
		return getEsitiSdiFatturaPAResult;
	}

	@Override
	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getGetEsitiSdiFatturaPAResult() != null) {
			_hashCode += getGetEsitiSdiFatturaPAResult().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	/**
	 * Sets the getEsitiSdiFatturaPAResult value for this GetEsitiSdiFatturaPAResponse.
	 * 
	 * @param getEsitiSdiFatturaPAResult
	 */
	public void setGetEsitiSdiFatturaPAResult(
			GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult getEsitiSdiFatturaPAResult) {
		this.getEsitiSdiFatturaPAResult = getEsitiSdiFatturaPAResult;
	}

}
