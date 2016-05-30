/**
 * GetFileEsitoFatturaPAResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class GetFileEsitoFatturaPAResponse  implements java.io.Serializable {
    private java.lang.Object[] getFileEsitoFatturaPAResult;

    public GetFileEsitoFatturaPAResponse() {
    }

    public GetFileEsitoFatturaPAResponse(
           java.lang.Object[] getFileEsitoFatturaPAResult) {
           this.getFileEsitoFatturaPAResult = getFileEsitoFatturaPAResult;
    }


    /**
     * Gets the getFileEsitoFatturaPAResult value for this GetFileEsitoFatturaPAResponse.
     * 
     * @return getFileEsitoFatturaPAResult
     */
    public java.lang.Object[] getGetFileEsitoFatturaPAResult() {
        return getFileEsitoFatturaPAResult;
    }


    /**
     * Sets the getFileEsitoFatturaPAResult value for this GetFileEsitoFatturaPAResponse.
     * 
     * @param getFileEsitoFatturaPAResult
     */
    public void setGetFileEsitoFatturaPAResult(java.lang.Object[] getFileEsitoFatturaPAResult) {
        this.getFileEsitoFatturaPAResult = getFileEsitoFatturaPAResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetFileEsitoFatturaPAResponse)) return false;
        GetFileEsitoFatturaPAResponse other = (GetFileEsitoFatturaPAResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getFileEsitoFatturaPAResult==null && other.getGetFileEsitoFatturaPAResult()==null) || 
             (this.getFileEsitoFatturaPAResult!=null &&
              java.util.Arrays.equals(this.getFileEsitoFatturaPAResult, other.getGetFileEsitoFatturaPAResult())));
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
        if (getGetFileEsitoFatturaPAResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetFileEsitoFatturaPAResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetFileEsitoFatturaPAResult(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetFileEsitoFatturaPAResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetFileEsitoFatturaPAResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getFileEsitoFatturaPAResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetFileEsitoFatturaPAResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "anyType"));
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
