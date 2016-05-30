/**
 * ContattoHubResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class ContattoHubResponse  implements java.io.Serializable {
    private java.lang.String contattoHubResult;

    public ContattoHubResponse() {
    }

    public ContattoHubResponse(
           java.lang.String contattoHubResult) {
           this.contattoHubResult = contattoHubResult;
    }


    /**
     * Gets the contattoHubResult value for this ContattoHubResponse.
     * 
     * @return contattoHubResult
     */
    public java.lang.String getContattoHubResult() {
        return contattoHubResult;
    }


    /**
     * Sets the contattoHubResult value for this ContattoHubResponse.
     * 
     * @param contattoHubResult
     */
    public void setContattoHubResult(java.lang.String contattoHubResult) {
        this.contattoHubResult = contattoHubResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ContattoHubResponse)) return false;
        ContattoHubResponse other = (ContattoHubResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.contattoHubResult==null && other.getContattoHubResult()==null) || 
             (this.contattoHubResult!=null &&
              this.contattoHubResult.equals(other.getContattoHubResult())));
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
        if (getContattoHubResult() != null) {
            _hashCode += getContattoHubResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ContattoHubResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">ContattoHubResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contattoHubResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ContattoHubResult"));
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
