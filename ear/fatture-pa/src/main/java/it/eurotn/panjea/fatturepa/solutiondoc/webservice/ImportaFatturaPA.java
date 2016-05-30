/**
 * ImportaFatturaPA.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class ImportaFatturaPA  implements java.io.Serializable {
    private java.lang.String codiceCliente;

    private byte[] passwordServizi;

    private java.lang.String nomeFileZip;

    private java.lang.String nomeFileRiepilogo;

    public ImportaFatturaPA() {
    }

    public ImportaFatturaPA(
           java.lang.String codiceCliente,
           byte[] passwordServizi,
           java.lang.String nomeFileZip,
           java.lang.String nomeFileRiepilogo) {
           this.codiceCliente = codiceCliente;
           this.passwordServizi = passwordServizi;
           this.nomeFileZip = nomeFileZip;
           this.nomeFileRiepilogo = nomeFileRiepilogo;
    }


    /**
     * Gets the codiceCliente value for this ImportaFatturaPA.
     * 
     * @return codiceCliente
     */
    public java.lang.String getCodiceCliente() {
        return codiceCliente;
    }


    /**
     * Sets the codiceCliente value for this ImportaFatturaPA.
     * 
     * @param codiceCliente
     */
    public void setCodiceCliente(java.lang.String codiceCliente) {
        this.codiceCliente = codiceCliente;
    }


    /**
     * Gets the passwordServizi value for this ImportaFatturaPA.
     * 
     * @return passwordServizi
     */
    public byte[] getPasswordServizi() {
        return passwordServizi;
    }


    /**
     * Sets the passwordServizi value for this ImportaFatturaPA.
     * 
     * @param passwordServizi
     */
    public void setPasswordServizi(byte[] passwordServizi) {
        this.passwordServizi = passwordServizi;
    }


    /**
     * Gets the nomeFileZip value for this ImportaFatturaPA.
     * 
     * @return nomeFileZip
     */
    public java.lang.String getNomeFileZip() {
        return nomeFileZip;
    }


    /**
     * Sets the nomeFileZip value for this ImportaFatturaPA.
     * 
     * @param nomeFileZip
     */
    public void setNomeFileZip(java.lang.String nomeFileZip) {
        this.nomeFileZip = nomeFileZip;
    }


    /**
     * Gets the nomeFileRiepilogo value for this ImportaFatturaPA.
     * 
     * @return nomeFileRiepilogo
     */
    public java.lang.String getNomeFileRiepilogo() {
        return nomeFileRiepilogo;
    }


    /**
     * Sets the nomeFileRiepilogo value for this ImportaFatturaPA.
     * 
     * @param nomeFileRiepilogo
     */
    public void setNomeFileRiepilogo(java.lang.String nomeFileRiepilogo) {
        this.nomeFileRiepilogo = nomeFileRiepilogo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ImportaFatturaPA)) return false;
        ImportaFatturaPA other = (ImportaFatturaPA) obj;
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
            ((this.nomeFileZip==null && other.getNomeFileZip()==null) || 
             (this.nomeFileZip!=null &&
              this.nomeFileZip.equals(other.getNomeFileZip()))) &&
            ((this.nomeFileRiepilogo==null && other.getNomeFileRiepilogo()==null) || 
             (this.nomeFileRiepilogo!=null &&
              this.nomeFileRiepilogo.equals(other.getNomeFileRiepilogo())));
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
        if (getNomeFileZip() != null) {
            _hashCode += getNomeFileZip().hashCode();
        }
        if (getNomeFileRiepilogo() != null) {
            _hashCode += getNomeFileRiepilogo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ImportaFatturaPA.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">ImportaFatturaPA"));
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
        elemField.setFieldName("nomeFileZip");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "nomeFileZip"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeFileRiepilogo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "nomeFileRiepilogo"));
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
