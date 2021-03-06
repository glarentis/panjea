/**
 * GetIdUnivocoFatturaPA.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class GetIdUnivocoFatturaPA  implements java.io.Serializable {
    private java.lang.String codiceCliente;

    private byte[] passwordServizi;

    private java.lang.String codicePaese;

    private java.lang.String identificativoTrasmittente;

    public GetIdUnivocoFatturaPA() {
    }

    public GetIdUnivocoFatturaPA(
           java.lang.String codiceCliente,
           byte[] passwordServizi,
           java.lang.String codicePaese,
           java.lang.String identificativoTrasmittente) {
           this.codiceCliente = codiceCliente;
           this.passwordServizi = passwordServizi;
           this.codicePaese = codicePaese;
           this.identificativoTrasmittente = identificativoTrasmittente;
    }


    /**
     * Gets the codiceCliente value for this GetIdUnivocoFatturaPA.
     * 
     * @return codiceCliente
     */
    public java.lang.String getCodiceCliente() {
        return codiceCliente;
    }


    /**
     * Sets the codiceCliente value for this GetIdUnivocoFatturaPA.
     * 
     * @param codiceCliente
     */
    public void setCodiceCliente(java.lang.String codiceCliente) {
        this.codiceCliente = codiceCliente;
    }


    /**
     * Gets the passwordServizi value for this GetIdUnivocoFatturaPA.
     * 
     * @return passwordServizi
     */
    public byte[] getPasswordServizi() {
        return passwordServizi;
    }


    /**
     * Sets the passwordServizi value for this GetIdUnivocoFatturaPA.
     * 
     * @param passwordServizi
     */
    public void setPasswordServizi(byte[] passwordServizi) {
        this.passwordServizi = passwordServizi;
    }


    /**
     * Gets the codicePaese value for this GetIdUnivocoFatturaPA.
     * 
     * @return codicePaese
     */
    public java.lang.String getCodicePaese() {
        return codicePaese;
    }


    /**
     * Sets the codicePaese value for this GetIdUnivocoFatturaPA.
     * 
     * @param codicePaese
     */
    public void setCodicePaese(java.lang.String codicePaese) {
        this.codicePaese = codicePaese;
    }


    /**
     * Gets the identificativoTrasmittente value for this GetIdUnivocoFatturaPA.
     * 
     * @return identificativoTrasmittente
     */
    public java.lang.String getIdentificativoTrasmittente() {
        return identificativoTrasmittente;
    }


    /**
     * Sets the identificativoTrasmittente value for this GetIdUnivocoFatturaPA.
     * 
     * @param identificativoTrasmittente
     */
    public void setIdentificativoTrasmittente(java.lang.String identificativoTrasmittente) {
        this.identificativoTrasmittente = identificativoTrasmittente;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetIdUnivocoFatturaPA)) return false;
        GetIdUnivocoFatturaPA other = (GetIdUnivocoFatturaPA) obj;
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
            ((this.codicePaese==null && other.getCodicePaese()==null) || 
             (this.codicePaese!=null &&
              this.codicePaese.equals(other.getCodicePaese()))) &&
            ((this.identificativoTrasmittente==null && other.getIdentificativoTrasmittente()==null) || 
             (this.identificativoTrasmittente!=null &&
              this.identificativoTrasmittente.equals(other.getIdentificativoTrasmittente())));
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
        if (getCodicePaese() != null) {
            _hashCode += getCodicePaese().hashCode();
        }
        if (getIdentificativoTrasmittente() != null) {
            _hashCode += getIdentificativoTrasmittente().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetIdUnivocoFatturaPA.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetIdUnivocoFatturaPA"));
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
        elemField.setFieldName("codicePaese");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "codicePaese"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificativoTrasmittente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "identificativoTrasmittente"));
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
