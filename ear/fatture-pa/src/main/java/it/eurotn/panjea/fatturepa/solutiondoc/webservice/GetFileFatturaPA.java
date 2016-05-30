/**
 * GetFileFatturaPA.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class GetFileFatturaPA  implements java.io.Serializable {
    private java.lang.String codiceCliente;

    private byte[] passwordServizi;

    private java.lang.String idTransazione;

    private boolean estrazioneP7M;

    public GetFileFatturaPA() {
    }

    public GetFileFatturaPA(
           java.lang.String codiceCliente,
           byte[] passwordServizi,
           java.lang.String idTransazione,
           boolean estrazioneP7M) {
           this.codiceCliente = codiceCliente;
           this.passwordServizi = passwordServizi;
           this.idTransazione = idTransazione;
           this.estrazioneP7M = estrazioneP7M;
    }


    /**
     * Gets the codiceCliente value for this GetFileFatturaPA.
     * 
     * @return codiceCliente
     */
    public java.lang.String getCodiceCliente() {
        return codiceCliente;
    }


    /**
     * Sets the codiceCliente value for this GetFileFatturaPA.
     * 
     * @param codiceCliente
     */
    public void setCodiceCliente(java.lang.String codiceCliente) {
        this.codiceCliente = codiceCliente;
    }


    /**
     * Gets the passwordServizi value for this GetFileFatturaPA.
     * 
     * @return passwordServizi
     */
    public byte[] getPasswordServizi() {
        return passwordServizi;
    }


    /**
     * Sets the passwordServizi value for this GetFileFatturaPA.
     * 
     * @param passwordServizi
     */
    public void setPasswordServizi(byte[] passwordServizi) {
        this.passwordServizi = passwordServizi;
    }


    /**
     * Gets the idTransazione value for this GetFileFatturaPA.
     * 
     * @return idTransazione
     */
    public java.lang.String getIdTransazione() {
        return idTransazione;
    }


    /**
     * Sets the idTransazione value for this GetFileFatturaPA.
     * 
     * @param idTransazione
     */
    public void setIdTransazione(java.lang.String idTransazione) {
        this.idTransazione = idTransazione;
    }


    /**
     * Gets the estrazioneP7M value for this GetFileFatturaPA.
     * 
     * @return estrazioneP7M
     */
    public boolean isEstrazioneP7M() {
        return estrazioneP7M;
    }


    /**
     * Sets the estrazioneP7M value for this GetFileFatturaPA.
     * 
     * @param estrazioneP7M
     */
    public void setEstrazioneP7M(boolean estrazioneP7M) {
        this.estrazioneP7M = estrazioneP7M;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetFileFatturaPA)) return false;
        GetFileFatturaPA other = (GetFileFatturaPA) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codiceCliente==null && other.getCodiceCliente()==null) || 
             (this.codiceCliente!=null &&
              this.codiceCliente.equals(other.getCodiceCliente()))) &&
            ((this.passwordServizi==null && other.getPasswordServizi()==null) || 
             (this.passwordServizi!=null &&
              java.util.Arrays.equals(this.passwordServizi, other.getPasswordServizi()))) &&
            ((this.idTransazione==null && other.getIdTransazione()==null) || 
             (this.idTransazione!=null &&
              this.idTransazione.equals(other.getIdTransazione()))) &&
            this.estrazioneP7M == other.isEstrazioneP7M();
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
        if (getCodiceCliente() != null) {
            _hashCode += getCodiceCliente().hashCode();
        }
        if (getPasswordServizi() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPasswordServizi());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPasswordServizi(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdTransazione() != null) {
            _hashCode += getIdTransazione().hashCode();
        }
        _hashCode += (isEstrazioneP7M() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetFileFatturaPA.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetFileFatturaPA"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "codiceCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("passwordServizi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "passwordServizi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTransazione");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "idTransazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estrazioneP7M");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "estrazioneP7M"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
