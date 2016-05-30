/**
 * GetLogFatturaPA.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class GetLogFatturaPA  implements java.io.Serializable {
    private java.lang.String codiceCliente;

    private byte[] passwordServizi;

    private java.lang.String identificativoSdi;

    private java.lang.String nomeFileZip;

    private java.lang.String nomeServizio;

    private java.lang.String messaggio;

    private java.util.Calendar data_Da;

    private java.util.Calendar data_A;

    public GetLogFatturaPA() {
    }

    public GetLogFatturaPA(
           java.lang.String codiceCliente,
           byte[] passwordServizi,
           java.lang.String identificativoSdi,
           java.lang.String nomeFileZip,
           java.lang.String nomeServizio,
           java.lang.String messaggio,
           java.util.Calendar data_Da,
           java.util.Calendar data_A) {
           this.codiceCliente = codiceCliente;
           this.passwordServizi = passwordServizi;
           this.identificativoSdi = identificativoSdi;
           this.nomeFileZip = nomeFileZip;
           this.nomeServizio = nomeServizio;
           this.messaggio = messaggio;
           this.data_Da = data_Da;
           this.data_A = data_A;
    }


    /**
     * Gets the codiceCliente value for this GetLogFatturaPA.
     * 
     * @return codiceCliente
     */
    public java.lang.String getCodiceCliente() {
        return codiceCliente;
    }


    /**
     * Sets the codiceCliente value for this GetLogFatturaPA.
     * 
     * @param codiceCliente
     */
    public void setCodiceCliente(java.lang.String codiceCliente) {
        this.codiceCliente = codiceCliente;
    }


    /**
     * Gets the passwordServizi value for this GetLogFatturaPA.
     * 
     * @return passwordServizi
     */
    public byte[] getPasswordServizi() {
        return passwordServizi;
    }


    /**
     * Sets the passwordServizi value for this GetLogFatturaPA.
     * 
     * @param passwordServizi
     */
    public void setPasswordServizi(byte[] passwordServizi) {
        this.passwordServizi = passwordServizi;
    }


    /**
     * Gets the identificativoSdi value for this GetLogFatturaPA.
     * 
     * @return identificativoSdi
     */
    public java.lang.String getIdentificativoSdi() {
        return identificativoSdi;
    }


    /**
     * Sets the identificativoSdi value for this GetLogFatturaPA.
     * 
     * @param identificativoSdi
     */
    public void setIdentificativoSdi(java.lang.String identificativoSdi) {
        this.identificativoSdi = identificativoSdi;
    }


    /**
     * Gets the nomeFileZip value for this GetLogFatturaPA.
     * 
     * @return nomeFileZip
     */
    public java.lang.String getNomeFileZip() {
        return nomeFileZip;
    }


    /**
     * Sets the nomeFileZip value for this GetLogFatturaPA.
     * 
     * @param nomeFileZip
     */
    public void setNomeFileZip(java.lang.String nomeFileZip) {
        this.nomeFileZip = nomeFileZip;
    }


    /**
     * Gets the nomeServizio value for this GetLogFatturaPA.
     * 
     * @return nomeServizio
     */
    public java.lang.String getNomeServizio() {
        return nomeServizio;
    }


    /**
     * Sets the nomeServizio value for this GetLogFatturaPA.
     * 
     * @param nomeServizio
     */
    public void setNomeServizio(java.lang.String nomeServizio) {
        this.nomeServizio = nomeServizio;
    }


    /**
     * Gets the messaggio value for this GetLogFatturaPA.
     * 
     * @return messaggio
     */
    public java.lang.String getMessaggio() {
        return messaggio;
    }


    /**
     * Sets the messaggio value for this GetLogFatturaPA.
     * 
     * @param messaggio
     */
    public void setMessaggio(java.lang.String messaggio) {
        this.messaggio = messaggio;
    }


    /**
     * Gets the data_Da value for this GetLogFatturaPA.
     * 
     * @return data_Da
     */
    public java.util.Calendar getData_Da() {
        return data_Da;
    }


    /**
     * Sets the data_Da value for this GetLogFatturaPA.
     * 
     * @param data_Da
     */
    public void setData_Da(java.util.Calendar data_Da) {
        this.data_Da = data_Da;
    }


    /**
     * Gets the data_A value for this GetLogFatturaPA.
     * 
     * @return data_A
     */
    public java.util.Calendar getData_A() {
        return data_A;
    }


    /**
     * Sets the data_A value for this GetLogFatturaPA.
     * 
     * @param data_A
     */
    public void setData_A(java.util.Calendar data_A) {
        this.data_A = data_A;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetLogFatturaPA)) return false;
        GetLogFatturaPA other = (GetLogFatturaPA) obj;
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
            ((this.nomeFileZip==null && other.getNomeFileZip()==null) || 
             (this.nomeFileZip!=null &&
              this.nomeFileZip.equals(other.getNomeFileZip()))) &&
            ((this.nomeServizio==null && other.getNomeServizio()==null) || 
             (this.nomeServizio!=null &&
              this.nomeServizio.equals(other.getNomeServizio()))) &&
            ((this.messaggio==null && other.getMessaggio()==null) || 
             (this.messaggio!=null &&
              this.messaggio.equals(other.getMessaggio()))) &&
            ((this.data_Da==null && other.getData_Da()==null) || 
             (this.data_Da!=null &&
              this.data_Da.equals(other.getData_Da()))) &&
            ((this.data_A==null && other.getData_A()==null) || 
             (this.data_A!=null &&
              this.data_A.equals(other.getData_A())));
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
        if (getNomeFileZip() != null) {
            _hashCode += getNomeFileZip().hashCode();
        }
        if (getNomeServizio() != null) {
            _hashCode += getNomeServizio().hashCode();
        }
        if (getMessaggio() != null) {
            _hashCode += getMessaggio().hashCode();
        }
        if (getData_Da() != null) {
            _hashCode += getData_Da().hashCode();
        }
        if (getData_A() != null) {
            _hashCode += getData_A().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetLogFatturaPA.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetLogFatturaPA"));
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
        elemField.setFieldName("nomeFileZip");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "nomeFileZip"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeServizio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "nomeServizio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messaggio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "messaggio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data_Da");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Data_Da"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data_A");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Data_A"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
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
