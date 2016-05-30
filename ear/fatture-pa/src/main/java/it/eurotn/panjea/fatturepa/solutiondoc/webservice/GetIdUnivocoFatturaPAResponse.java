/**
 * GetIdUnivocoFatturaPAResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class GetIdUnivocoFatturaPAResponse  implements java.io.Serializable {
    private java.lang.String getIdUnivocoFatturaPAResult;

    public GetIdUnivocoFatturaPAResponse() {
    }

    public GetIdUnivocoFatturaPAResponse(
           java.lang.String getIdUnivocoFatturaPAResult) {
           this.getIdUnivocoFatturaPAResult = getIdUnivocoFatturaPAResult;
    }


    /**
     * Gets the getIdUnivocoFatturaPAResult value for this GetIdUnivocoFatturaPAResponse.
     * 
     * @return getIdUnivocoFatturaPAResult
     */
    public java.lang.String getGetIdUnivocoFatturaPAResult() {
        return getIdUnivocoFatturaPAResult;
    }


    /**
     * Sets the getIdUnivocoFatturaPAResult value for this GetIdUnivocoFatturaPAResponse.
     * 
     * @param getIdUnivocoFatturaPAResult
     */
    public void setGetIdUnivocoFatturaPAResult(java.lang.String getIdUnivocoFatturaPAResult) {
        this.getIdUnivocoFatturaPAResult = getIdUnivocoFatturaPAResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetIdUnivocoFatturaPAResponse)) return false;
        GetIdUnivocoFatturaPAResponse other = (GetIdUnivocoFatturaPAResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getIdUnivocoFatturaPAResult==null && other.getGetIdUnivocoFatturaPAResult()==null) || 
             (this.getIdUnivocoFatturaPAResult!=null &&
              this.getIdUnivocoFatturaPAResult.equals(other.getGetIdUnivocoFatturaPAResult())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getGetIdUnivocoFatturaPAResult() != null) {
            _hashCode += getGetIdUnivocoFatturaPAResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetIdUnivocoFatturaPAResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetIdUnivocoFatturaPAResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getIdUnivocoFatturaPAResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetIdUnivocoFatturaPAResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
