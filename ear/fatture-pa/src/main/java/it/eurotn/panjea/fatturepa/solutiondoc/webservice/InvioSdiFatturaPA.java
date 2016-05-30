/**
 * InvioSdiFatturaPA.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class InvioSdiFatturaPA  implements java.io.Serializable {
    private java.lang.String codiceCliente;

    private byte[] passwordServizi;

    private java.lang.String nomeFile;

    private java.lang.String codicePaese;

    private java.lang.String identificativoTrasmittente;

    private boolean rinominaFile;

    private boolean firmaTerzoIntermediario;

    public InvioSdiFatturaPA() {
    }

    public InvioSdiFatturaPA(
           java.lang.String codiceCliente,
           byte[] passwordServizi,
           java.lang.String nomeFile,
           java.lang.String codicePaese,
           java.lang.String identificativoTrasmittente,
           boolean rinominaFile,
           boolean firmaTerzoIntermediario) {
           this.codiceCliente = codiceCliente;
           this.passwordServizi = passwordServizi;
           this.nomeFile = nomeFile;
           this.codicePaese = codicePaese;
           this.identificativoTrasmittente = identificativoTrasmittente;
           this.rinominaFile = rinominaFile;
           this.firmaTerzoIntermediario = firmaTerzoIntermediario;
    }


    /**
     * Gets the codiceCliente value for this InvioSdiFatturaPA.
     * 
     * @return codiceCliente
     */
    public java.lang.String getCodiceCliente() {
        return codiceCliente;
    }


    /**
     * Sets the codiceCliente value for this InvioSdiFatturaPA.
     * 
     * @param codiceCliente
     */
    public void setCodiceCliente(java.lang.String codiceCliente) {
        this.codiceCliente = codiceCliente;
    }


    /**
     * Gets the passwordServizi value for this InvioSdiFatturaPA.
     * 
     * @return passwordServizi
     */
    public byte[] getPasswordServizi() {
        return passwordServizi;
    }


    /**
     * Sets the passwordServizi value for this InvioSdiFatturaPA.
     * 
     * @param passwordServizi
     */
    public void setPasswordServizi(byte[] passwordServizi) {
        this.passwordServizi = passwordServizi;
    }


    /**
     * Gets the nomeFile value for this InvioSdiFatturaPA.
     * 
     * @return nomeFile
     */
    public java.lang.String getNomeFile() {
        return nomeFile;
    }


    /**
     * Sets the nomeFile value for this InvioSdiFatturaPA.
     * 
     * @param nomeFile
     */
    public void setNomeFile(java.lang.String nomeFile) {
        this.nomeFile = nomeFile;
    }


    /**
     * Gets the codicePaese value for this InvioSdiFatturaPA.
     * 
     * @return codicePaese
     */
    public java.lang.String getCodicePaese() {
        return codicePaese;
    }


    /**
     * Sets the codicePaese value for this InvioSdiFatturaPA.
     * 
     * @param codicePaese
     */
    public void setCodicePaese(java.lang.String codicePaese) {
        this.codicePaese = codicePaese;
    }


    /**
     * Gets the identificativoTrasmittente value for this InvioSdiFatturaPA.
     * 
     * @return identificativoTrasmittente
     */
    public java.lang.String getIdentificativoTrasmittente() {
        return identificativoTrasmittente;
    }


    /**
     * Sets the identificativoTrasmittente value for this InvioSdiFatturaPA.
     * 
     * @param identificativoTrasmittente
     */
    public void setIdentificativoTrasmittente(java.lang.String identificativoTrasmittente) {
        this.identificativoTrasmittente = identificativoTrasmittente;
    }


    /**
     * Gets the rinominaFile value for this InvioSdiFatturaPA.
     * 
     * @return rinominaFile
     */
    public boolean isRinominaFile() {
        return rinominaFile;
    }


    /**
     * Sets the rinominaFile value for this InvioSdiFatturaPA.
     * 
     * @param rinominaFile
     */
    public void setRinominaFile(boolean rinominaFile) {
        this.rinominaFile = rinominaFile;
    }


    /**
     * Gets the firmaTerzoIntermediario value for this InvioSdiFatturaPA.
     * 
     * @return firmaTerzoIntermediario
     */
    public boolean isFirmaTerzoIntermediario() {
        return firmaTerzoIntermediario;
    }


    /**
     * Sets the firmaTerzoIntermediario value for this InvioSdiFatturaPA.
     * 
     * @param firmaTerzoIntermediario
     */
    public void setFirmaTerzoIntermediario(boolean firmaTerzoIntermediario) {
        this.firmaTerzoIntermediario = firmaTerzoIntermediario;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InvioSdiFatturaPA)) return false;
        InvioSdiFatturaPA other = (InvioSdiFatturaPA) obj;
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
            ((this.nomeFile==null && other.getNomeFile()==null) || 
             (this.nomeFile!=null &&
              this.nomeFile.equals(other.getNomeFile()))) &&
            ((this.codicePaese==null && other.getCodicePaese()==null) || 
             (this.codicePaese!=null &&
              this.codicePaese.equals(other.getCodicePaese()))) &&
            ((this.identificativoTrasmittente==null && other.getIdentificativoTrasmittente()==null) || 
             (this.identificativoTrasmittente!=null &&
              this.identificativoTrasmittente.equals(other.getIdentificativoTrasmittente()))) &&
            this.rinominaFile == other.isRinominaFile() &&
            this.firmaTerzoIntermediario == other.isFirmaTerzoIntermediario();
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
        if (getNomeFile() != null) {
            _hashCode += getNomeFile().hashCode();
        }
        if (getCodicePaese() != null) {
            _hashCode += getCodicePaese().hashCode();
        }
        if (getIdentificativoTrasmittente() != null) {
            _hashCode += getIdentificativoTrasmittente().hashCode();
        }
        _hashCode += (isRinominaFile() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isFirmaTerzoIntermediario() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InvioSdiFatturaPA.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">InvioSdiFatturaPA"));
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
        elemField.setFieldName("nomeFile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "nomeFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rinominaFile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "rinominaFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firmaTerzoIntermediario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "firmaTerzoIntermediario"));
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
