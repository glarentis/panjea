/**
 * EsitoInvioSdiFatturaPA.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class EsitoInvioSdiFatturaPA  implements java.io.Serializable {
    private java.lang.String codiceCliente;

    private byte[] passwordServizi;

    private java.lang.String identificativoSdi;

    private java.lang.String nomeFileFatturaPA;

    public EsitoInvioSdiFatturaPA() {
    }

    public EsitoInvioSdiFatturaPA(
           java.lang.String codiceCliente,
           byte[] passwordServizi,
           java.lang.String identificativoSdi,
           java.lang.String nomeFileFatturaPA) {
           this.codiceCliente = codiceCliente;
           this.passwordServizi = passwordServizi;
           this.identificativoSdi = identificativoSdi;
           this.nomeFileFatturaPA = nomeFileFatturaPA;
    }


    /**
     * Gets the codiceCliente value for this EsitoInvioSdiFatturaPA.
     * 
     * @return codiceCliente
     */
    public java.lang.String getCodiceCliente() {
        return codiceCliente;
    }


    /**
     * Sets the codiceCliente value for this EsitoInvioSdiFatturaPA.
     * 
     * @param codiceCliente
     */
    public void setCodiceCliente(java.lang.String codiceCliente) {
        this.codiceCliente = codiceCliente;
    }


    /**
     * Gets the passwordServizi value for this EsitoInvioSdiFatturaPA.
     * 
     * @return passwordServizi
     */
    public byte[] getPasswordServizi() {
        return passwordServizi;
    }


    /**
     * Sets the passwordServizi value for this EsitoInvioSdiFatturaPA.
     * 
     * @param passwordServizi
     */
    public void setPasswordServizi(byte[] passwordServizi) {
        this.passwordServizi = passwordServizi;
    }


    /**
     * Gets the identificativoSdi value for this EsitoInvioSdiFatturaPA.
     * 
     * @return identificativoSdi
     */
    public java.lang.String getIdentificativoSdi() {
        return identificativoSdi;
    }


    /**
     * Sets the identificativoSdi value for this EsitoInvioSdiFatturaPA.
     * 
     * @param identificativoSdi
     */
    public void setIdentificativoSdi(java.lang.String identificativoSdi) {
        this.identificativoSdi = identificativoSdi;
    }


    /**
     * Gets the nomeFileFatturaPA value for this EsitoInvioSdiFatturaPA.
     * 
     * @return nomeFileFatturaPA
     */
    public java.lang.String getNomeFileFatturaPA() {
        return nomeFileFatturaPA;
    }


    /**
     * Sets the nomeFileFatturaPA value for this EsitoInvioSdiFatturaPA.
     * 
     * @param nomeFileFatturaPA
     */
    public void setNomeFileFatturaPA(java.lang.String nomeFileFatturaPA) {
        this.nomeFileFatturaPA = nomeFileFatturaPA;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EsitoInvioSdiFatturaPA)) return false;
        EsitoInvioSdiFatturaPA other = (EsitoInvioSdiFatturaPA) obj;
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
            ((this.identificativoSdi==null && other.getIdentificativoSdi()==null) || 
             (this.identificativoSdi!=null &&
              this.identificativoSdi.equals(other.getIdentificativoSdi()))) &&
            ((this.nomeFileFatturaPA==null && other.getNomeFileFatturaPA()==null) || 
             (this.nomeFileFatturaPA!=null &&
              this.nomeFileFatturaPA.equals(other.getNomeFileFatturaPA())));
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
        if (getIdentificativoSdi() != null) {
            _hashCode += getIdentificativoSdi().hashCode();
        }
        if (getNomeFileFatturaPA() != null) {
            _hashCode += getNomeFileFatturaPA().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EsitoInvioSdiFatturaPA.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">EsitoInvioSdiFatturaPA"));
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
        elemField.setFieldName("identificativoSdi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "identificativoSdi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeFileFatturaPA");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "nomeFileFatturaPA"));
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
