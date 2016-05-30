/**
 * GetLogFatturaPAResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class GetLogFatturaPAResponse implements java.io.Serializable {
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

	private GetLogFatturaPAResponseGetLogFatturaPAResult getLogFatturaPAResult;

	private java.lang.Object __equalsCalc = null;

	private boolean __hashCodeCalc = false;
	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			GetLogFatturaPAResponse.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetLogFatturaPAResponse"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("getLogFatturaPAResult");
		elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetLogFatturaPAResult"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetLogFatturaPAResponse>GetLogFatturaPAResult"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public GetLogFatturaPAResponse() {
	}

	public GetLogFatturaPAResponse(GetLogFatturaPAResponseGetLogFatturaPAResult getLogFatturaPAResult) {
		this.getLogFatturaPAResult = getLogFatturaPAResult;
	}

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof GetLogFatturaPAResponse)) {
			return false;
		}
		GetLogFatturaPAResponse other = (GetLogFatturaPAResponse) obj;
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
		_equals = true && ((this.getLogFatturaPAResult == null && other.getGetLogFatturaPAResult() == null) || (this.getLogFatturaPAResult != null && this.getLogFatturaPAResult
				.equals(other.getGetLogFatturaPAResult())));
		__equalsCalc = null;
		return _equals;
	}

	/**
	 * Gets the getLogFatturaPAResult value for this GetLogFatturaPAResponse.
	 * 
	 * @return getLogFatturaPAResult
	 */
	public GetLogFatturaPAResponseGetLogFatturaPAResult getGetLogFatturaPAResult() {
		return getLogFatturaPAResult;
	}

	@Override
	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getGetLogFatturaPAResult() != null) {
			_hashCode += getGetLogFatturaPAResult().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	/**
	 * Sets the getLogFatturaPAResult value for this GetLogFatturaPAResponse.
	 * 
	 * @param getLogFatturaPAResult
	 */
	public void setGetLogFatturaPAResult(GetLogFatturaPAResponseGetLogFatturaPAResult getLogFatturaPAResult) {
		this.getLogFatturaPAResult = getLogFatturaPAResult;
	}

}
