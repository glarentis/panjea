/**
 * SetPacchettoDiVersamentoFatturaPA.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class SetPacchettoDiVersamentoFatturaPA  implements java.io.Serializable {
    private java.lang.String username;

    private byte[] password;

    private boolean isFattura;

    private java.lang.String urlServizio;

    private java.lang.String usernameServizio;

    private byte[] passwordServizio;

    private java.lang.String intervalloRdV;

    private java.lang.String intervalloPdA;

    private int numPacchettiRdV;

    private int numPacchettiPdA;

    private boolean sendEmail;

    private java.lang.String idCliente;

    private java.lang.Integer numMaxFiles;

    public SetPacchettoDiVersamentoFatturaPA() {
    }

    public SetPacchettoDiVersamentoFatturaPA(
           java.lang.String username,
           byte[] password,
           boolean isFattura,
           java.lang.String urlServizio,
           java.lang.String usernameServizio,
           byte[] passwordServizio,
           java.lang.String intervalloRdV,
           java.lang.String intervalloPdA,
           int numPacchettiRdV,
           int numPacchettiPdA,
           boolean sendEmail,
           java.lang.String idCliente,
           java.lang.Integer numMaxFiles) {
           this.username = username;
           this.password = password;
           this.isFattura = isFattura;
           this.urlServizio = urlServizio;
           this.usernameServizio = usernameServizio;
           this.passwordServizio = passwordServizio;
           this.intervalloRdV = intervalloRdV;
           this.intervalloPdA = intervalloPdA;
           this.numPacchettiRdV = numPacchettiRdV;
           this.numPacchettiPdA = numPacchettiPdA;
           this.sendEmail = sendEmail;
           this.idCliente = idCliente;
           this.numMaxFiles = numMaxFiles;
    }


    /**
     * Gets the username value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return username
     */
    public java.lang.String getUsername() {
        return username;
    }


    /**
     * Sets the username value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param username
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }


    /**
     * Gets the password value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return password
     */
    public byte[] getPassword() {
        return password;
    }


    /**
     * Sets the password value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param password
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }


    /**
     * Gets the isFattura value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return isFattura
     */
    public boolean isIsFattura() {
        return isFattura;
    }


    /**
     * Sets the isFattura value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param isFattura
     */
    public void setIsFattura(boolean isFattura) {
        this.isFattura = isFattura;
    }


    /**
     * Gets the urlServizio value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return urlServizio
     */
    public java.lang.String getUrlServizio() {
        return urlServizio;
    }


    /**
     * Sets the urlServizio value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param urlServizio
     */
    public void setUrlServizio(java.lang.String urlServizio) {
        this.urlServizio = urlServizio;
    }


    /**
     * Gets the usernameServizio value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return usernameServizio
     */
    public java.lang.String getUsernameServizio() {
        return usernameServizio;
    }


    /**
     * Sets the usernameServizio value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param usernameServizio
     */
    public void setUsernameServizio(java.lang.String usernameServizio) {
        this.usernameServizio = usernameServizio;
    }


    /**
     * Gets the passwordServizio value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return passwordServizio
     */
    public byte[] getPasswordServizio() {
        return passwordServizio;
    }


    /**
     * Sets the passwordServizio value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param passwordServizio
     */
    public void setPasswordServizio(byte[] passwordServizio) {
        this.passwordServizio = passwordServizio;
    }


    /**
     * Gets the intervalloRdV value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return intervalloRdV
     */
    public java.lang.String getIntervalloRdV() {
        return intervalloRdV;
    }


    /**
     * Sets the intervalloRdV value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param intervalloRdV
     */
    public void setIntervalloRdV(java.lang.String intervalloRdV) {
        this.intervalloRdV = intervalloRdV;
    }


    /**
     * Gets the intervalloPdA value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return intervalloPdA
     */
    public java.lang.String getIntervalloPdA() {
        return intervalloPdA;
    }


    /**
     * Sets the intervalloPdA value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param intervalloPdA
     */
    public void setIntervalloPdA(java.lang.String intervalloPdA) {
        this.intervalloPdA = intervalloPdA;
    }


    /**
     * Gets the numPacchettiRdV value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return numPacchettiRdV
     */
    public int getNumPacchettiRdV() {
        return numPacchettiRdV;
    }


    /**
     * Sets the numPacchettiRdV value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param numPacchettiRdV
     */
    public void setNumPacchettiRdV(int numPacchettiRdV) {
        this.numPacchettiRdV = numPacchettiRdV;
    }


    /**
     * Gets the numPacchettiPdA value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return numPacchettiPdA
     */
    public int getNumPacchettiPdA() {
        return numPacchettiPdA;
    }


    /**
     * Sets the numPacchettiPdA value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param numPacchettiPdA
     */
    public void setNumPacchettiPdA(int numPacchettiPdA) {
        this.numPacchettiPdA = numPacchettiPdA;
    }


    /**
     * Gets the sendEmail value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return sendEmail
     */
    public boolean isSendEmail() {
        return sendEmail;
    }


    /**
     * Sets the sendEmail value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param sendEmail
     */
    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }


    /**
     * Gets the idCliente value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return idCliente
     */
    public java.lang.String getIdCliente() {
        return idCliente;
    }


    /**
     * Sets the idCliente value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param idCliente
     */
    public void setIdCliente(java.lang.String idCliente) {
        this.idCliente = idCliente;
    }


    /**
     * Gets the numMaxFiles value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @return numMaxFiles
     */
    public java.lang.Integer getNumMaxFiles() {
        return numMaxFiles;
    }


    /**
     * Sets the numMaxFiles value for this SetPacchettoDiVersamentoFatturaPA.
     * 
     * @param numMaxFiles
     */
    public void setNumMaxFiles(java.lang.Integer numMaxFiles) {
        this.numMaxFiles = numMaxFiles;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SetPacchettoDiVersamentoFatturaPA)) return false;
        SetPacchettoDiVersamentoFatturaPA other = (SetPacchettoDiVersamentoFatturaPA) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.username==null && other.getUsername()==null) || 
             (this.username!=null &&
              this.username.equals(other.getUsername()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              java.util.Arrays.equals(this.password, other.getPassword()))) &&
            this.isFattura == other.isIsFattura() &&
            ((this.urlServizio==null && other.getUrlServizio()==null) || 
             (this.urlServizio!=null &&
              this.urlServizio.equals(other.getUrlServizio()))) &&
            ((this.usernameServizio==null && other.getUsernameServizio()==null) || 
             (this.usernameServizio!=null &&
              this.usernameServizio.equals(other.getUsernameServizio()))) &&
            ((this.passwordServizio==null && other.getPasswordServizio()==null) || 
             (this.passwordServizio!=null &&
              java.util.Arrays.equals(this.passwordServizio, other.getPasswordServizio()))) &&
            ((this.intervalloRdV==null && other.getIntervalloRdV()==null) || 
             (this.intervalloRdV!=null &&
              this.intervalloRdV.equals(other.getIntervalloRdV()))) &&
            ((this.intervalloPdA==null && other.getIntervalloPdA()==null) || 
             (this.intervalloPdA!=null &&
              this.intervalloPdA.equals(other.getIntervalloPdA()))) &&
            this.numPacchettiRdV == other.getNumPacchettiRdV() &&
            this.numPacchettiPdA == other.getNumPacchettiPdA() &&
            this.sendEmail == other.isSendEmail() &&
            ((this.idCliente==null && other.getIdCliente()==null) || 
             (this.idCliente!=null &&
              this.idCliente.equals(other.getIdCliente()))) &&
            ((this.numMaxFiles==null && other.getNumMaxFiles()==null) || 
             (this.numMaxFiles!=null &&
              this.numMaxFiles.equals(other.getNumMaxFiles())));
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
        if (getUsername() != null) {
            _hashCode += getUsername().hashCode();
        }
        if (getPassword() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPassword());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPassword(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isIsFattura() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getUrlServizio() != null) {
            _hashCode += getUrlServizio().hashCode();
        }
        if (getUsernameServizio() != null) {
            _hashCode += getUsernameServizio().hashCode();
        }
        if (getPasswordServizio() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPasswordServizio());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPasswordServizio(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIntervalloRdV() != null) {
            _hashCode += getIntervalloRdV().hashCode();
        }
        if (getIntervalloPdA() != null) {
            _hashCode += getIntervalloPdA().hashCode();
        }
        _hashCode += getNumPacchettiRdV();
        _hashCode += getNumPacchettiPdA();
        _hashCode += (isSendEmail() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getIdCliente() != null) {
            _hashCode += getIdCliente().hashCode();
        }
        if (getNumMaxFiles() != null) {
            _hashCode += getNumMaxFiles().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SetPacchettoDiVersamentoFatturaPA.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">SetPacchettoDiVersamentoFatturaPA"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("username");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isFattura");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "isFattura"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urlServizio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "urlServizio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usernameServizio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "usernameServizio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("passwordServizio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "passwordServizio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("intervalloRdV");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "intervalloRdV"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("intervalloPdA");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "intervalloPdA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numPacchettiRdV");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "numPacchettiRdV"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numPacchettiPdA");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "numPacchettiPdA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sendEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "sendEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "idCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numMaxFiles");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "numMaxFiles"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
