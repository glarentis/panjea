/**
 * GetPacchettoDistribuzione.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class GetPacchettoDistribuzione  implements java.io.Serializable {
    private java.lang.String codiceCliente;

    private byte[] passwordServizi;

    private java.lang.String[] listaIdFattura;

    private java.lang.String[] listaIdEsito;

    private java.lang.String nomeUtente;

    private java.lang.String cognomeUtente;

    private java.lang.String codiceFiscalePaese;

    private java.lang.String codiceFiscale;

    private java.lang.String partitaIvaPaese;

    private java.lang.String partitaIva;

    public GetPacchettoDistribuzione() {
    }

    public GetPacchettoDistribuzione(
           java.lang.String codiceCliente,
           byte[] passwordServizi,
           java.lang.String[] listaIdFattura,
           java.lang.String[] listaIdEsito,
           java.lang.String nomeUtente,
           java.lang.String cognomeUtente,
           java.lang.String codiceFiscalePaese,
           java.lang.String codiceFiscale,
           java.lang.String partitaIvaPaese,
           java.lang.String partitaIva) {
           this.codiceCliente = codiceCliente;
           this.passwordServizi = passwordServizi;
           this.listaIdFattura = listaIdFattura;
           this.listaIdEsito = listaIdEsito;
           this.nomeUtente = nomeUtente;
           this.cognomeUtente = cognomeUtente;
           this.codiceFiscalePaese = codiceFiscalePaese;
           this.codiceFiscale = codiceFiscale;
           this.partitaIvaPaese = partitaIvaPaese;
           this.partitaIva = partitaIva;
    }


    /**
     * Gets the codiceCliente value for this GetPacchettoDistribuzione.
     * 
     * @return codiceCliente
     */
    public java.lang.String getCodiceCliente() {
        return codiceCliente;
    }


    /**
     * Sets the codiceCliente value for this GetPacchettoDistribuzione.
     * 
     * @param codiceCliente
     */
    public void setCodiceCliente(java.lang.String codiceCliente) {
        this.codiceCliente = codiceCliente;
    }


    /**
     * Gets the passwordServizi value for this GetPacchettoDistribuzione.
     * 
     * @return passwordServizi
     */
    public byte[] getPasswordServizi() {
        return passwordServizi;
    }


    /**
     * Sets the passwordServizi value for this GetPacchettoDistribuzione.
     * 
     * @param passwordServizi
     */
    public void setPasswordServizi(byte[] passwordServizi) {
        this.passwordServizi = passwordServizi;
    }


    /**
     * Gets the listaIdFattura value for this GetPacchettoDistribuzione.
     * 
     * @return listaIdFattura
     */
    public java.lang.String[] getListaIdFattura() {
        return listaIdFattura;
    }


    /**
     * Sets the listaIdFattura value for this GetPacchettoDistribuzione.
     * 
     * @param listaIdFattura
     */
    public void setListaIdFattura(java.lang.String[] listaIdFattura) {
        this.listaIdFattura = listaIdFattura;
    }


    /**
     * Gets the listaIdEsito value for this GetPacchettoDistribuzione.
     * 
     * @return listaIdEsito
     */
    public java.lang.String[] getListaIdEsito() {
        return listaIdEsito;
    }


    /**
     * Sets the listaIdEsito value for this GetPacchettoDistribuzione.
     * 
     * @param listaIdEsito
     */
    public void setListaIdEsito(java.lang.String[] listaIdEsito) {
        this.listaIdEsito = listaIdEsito;
    }


    /**
     * Gets the nomeUtente value for this GetPacchettoDistribuzione.
     * 
     * @return nomeUtente
     */
    public java.lang.String getNomeUtente() {
        return nomeUtente;
    }


    /**
     * Sets the nomeUtente value for this GetPacchettoDistribuzione.
     * 
     * @param nomeUtente
     */
    public void setNomeUtente(java.lang.String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }


    /**
     * Gets the cognomeUtente value for this GetPacchettoDistribuzione.
     * 
     * @return cognomeUtente
     */
    public java.lang.String getCognomeUtente() {
        return cognomeUtente;
    }


    /**
     * Sets the cognomeUtente value for this GetPacchettoDistribuzione.
     * 
     * @param cognomeUtente
     */
    public void setCognomeUtente(java.lang.String cognomeUtente) {
        this.cognomeUtente = cognomeUtente;
    }


    /**
     * Gets the codiceFiscalePaese value for this GetPacchettoDistribuzione.
     * 
     * @return codiceFiscalePaese
     */
    public java.lang.String getCodiceFiscalePaese() {
        return codiceFiscalePaese;
    }


    /**
     * Sets the codiceFiscalePaese value for this GetPacchettoDistribuzione.
     * 
     * @param codiceFiscalePaese
     */
    public void setCodiceFiscalePaese(java.lang.String codiceFiscalePaese) {
        this.codiceFiscalePaese = codiceFiscalePaese;
    }


    /**
     * Gets the codiceFiscale value for this GetPacchettoDistribuzione.
     * 
     * @return codiceFiscale
     */
    public java.lang.String getCodiceFiscale() {
        return codiceFiscale;
    }


    /**
     * Sets the codiceFiscale value for this GetPacchettoDistribuzione.
     * 
     * @param codiceFiscale
     */
    public void setCodiceFiscale(java.lang.String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }


    /**
     * Gets the partitaIvaPaese value for this GetPacchettoDistribuzione.
     * 
     * @return partitaIvaPaese
     */
    public java.lang.String getPartitaIvaPaese() {
        return partitaIvaPaese;
    }


    /**
     * Sets the partitaIvaPaese value for this GetPacchettoDistribuzione.
     * 
     * @param partitaIvaPaese
     */
    public void setPartitaIvaPaese(java.lang.String partitaIvaPaese) {
        this.partitaIvaPaese = partitaIvaPaese;
    }


    /**
     * Gets the partitaIva value for this GetPacchettoDistribuzione.
     * 
     * @return partitaIva
     */
    public java.lang.String getPartitaIva() {
        return partitaIva;
    }


    /**
     * Sets the partitaIva value for this GetPacchettoDistribuzione.
     * 
     * @param partitaIva
     */
    public void setPartitaIva(java.lang.String partitaIva) {
        this.partitaIva = partitaIva;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetPacchettoDistribuzione)) return false;
        GetPacchettoDistribuzione other = (GetPacchettoDistribuzione) obj;
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
            ((this.listaIdFattura==null && other.getListaIdFattura()==null) || 
             (this.listaIdFattura!=null &&
              java.util.Arrays.equals(this.listaIdFattura, other.getListaIdFattura()))) &&
            ((this.listaIdEsito==null && other.getListaIdEsito()==null) || 
             (this.listaIdEsito!=null &&
              java.util.Arrays.equals(this.listaIdEsito, other.getListaIdEsito()))) &&
            ((this.nomeUtente==null && other.getNomeUtente()==null) || 
             (this.nomeUtente!=null &&
              this.nomeUtente.equals(other.getNomeUtente()))) &&
            ((this.cognomeUtente==null && other.getCognomeUtente()==null) || 
             (this.cognomeUtente!=null &&
              this.cognomeUtente.equals(other.getCognomeUtente()))) &&
            ((this.codiceFiscalePaese==null && other.getCodiceFiscalePaese()==null) || 
             (this.codiceFiscalePaese!=null &&
              this.codiceFiscalePaese.equals(other.getCodiceFiscalePaese()))) &&
            ((this.codiceFiscale==null && other.getCodiceFiscale()==null) || 
             (this.codiceFiscale!=null &&
              this.codiceFiscale.equals(other.getCodiceFiscale()))) &&
            ((this.partitaIvaPaese==null && other.getPartitaIvaPaese()==null) || 
             (this.partitaIvaPaese!=null &&
              this.partitaIvaPaese.equals(other.getPartitaIvaPaese()))) &&
            ((this.partitaIva==null && other.getPartitaIva()==null) || 
             (this.partitaIva!=null &&
              this.partitaIva.equals(other.getPartitaIva())));
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
        if (getListaIdFattura() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getListaIdFattura());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getListaIdFattura(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getListaIdEsito() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getListaIdEsito());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getListaIdEsito(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNomeUtente() != null) {
            _hashCode += getNomeUtente().hashCode();
        }
        if (getCognomeUtente() != null) {
            _hashCode += getCognomeUtente().hashCode();
        }
        if (getCodiceFiscalePaese() != null) {
            _hashCode += getCodiceFiscalePaese().hashCode();
        }
        if (getCodiceFiscale() != null) {
            _hashCode += getCodiceFiscale().hashCode();
        }
        if (getPartitaIvaPaese() != null) {
            _hashCode += getPartitaIvaPaese().hashCode();
        }
        if (getPartitaIva() != null) {
            _hashCode += getPartitaIva().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetPacchettoDistribuzione.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetPacchettoDistribuzione"));
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
        elemField.setFieldName("listaIdFattura");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "listaIdFattura"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listaIdEsito");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "listaIdEsito"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeUtente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "nomeUtente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cognomeUtente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "cognomeUtente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceFiscalePaese");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "codiceFiscalePaese"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceFiscale");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "codiceFiscale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("partitaIvaPaese");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "partitaIvaPaese"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("partitaIva");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "partitaIva"));
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
