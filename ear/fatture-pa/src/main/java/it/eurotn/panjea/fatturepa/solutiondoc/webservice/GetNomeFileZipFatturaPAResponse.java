/**
 * GetNomeFileZipFatturaPAResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class GetNomeFileZipFatturaPAResponse  implements java.io.Serializable {
    private java.lang.String getNomeFileZipFatturaPAResult;

    public GetNomeFileZipFatturaPAResponse() {
    }

    public GetNomeFileZipFatturaPAResponse(
           java.lang.String getNomeFileZipFatturaPAResult) {
           this.getNomeFileZipFatturaPAResult = getNomeFileZipFatturaPAResult;
    }


    /**
     * Gets the getNomeFileZipFatturaPAResult value for this GetNomeFileZipFatturaPAResponse.
     * 
     * @return getNomeFileZipFatturaPAResult
     */
    public java.lang.String getGetNomeFileZipFatturaPAResult() {
        return getNomeFileZipFatturaPAResult;
    }


    /**
     * Sets the getNomeFileZipFatturaPAResult value for this GetNomeFileZipFatturaPAResponse.
     * 
     * @param getNomeFileZipFatturaPAResult
     */
    public void setGetNomeFileZipFatturaPAResult(java.lang.String getNomeFileZipFatturaPAResult) {
        this.getNomeFileZipFatturaPAResult = getNomeFileZipFatturaPAResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetNomeFileZipFatturaPAResponse)) return false;
        GetNomeFileZipFatturaPAResponse other = (GetNomeFileZipFatturaPAResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getNomeFileZipFatturaPAResult==null && other.getGetNomeFileZipFatturaPAResult()==null) || 
             (this.getNomeFileZipFatturaPAResult!=null &&
              this.getNomeFileZipFatturaPAResult.equals(other.getGetNomeFileZipFatturaPAResult())));
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
        if (getGetNomeFileZipFatturaPAResult() != null) {
            _hashCode += getGetNomeFileZipFatturaPAResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetNomeFileZipFatturaPAResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetNomeFileZipFatturaPAResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getNomeFileZipFatturaPAResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetNomeFileZipFatturaPAResult"));
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
