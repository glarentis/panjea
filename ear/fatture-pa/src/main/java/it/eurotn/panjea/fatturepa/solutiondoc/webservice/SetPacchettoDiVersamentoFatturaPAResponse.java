/**
 * SetPacchettoDiVersamentoFatturaPAResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class SetPacchettoDiVersamentoFatturaPAResponse  implements java.io.Serializable {
    private java.lang.Object[] setPacchettoDiVersamentoFatturaPAResult;

    public SetPacchettoDiVersamentoFatturaPAResponse() {
    }

    public SetPacchettoDiVersamentoFatturaPAResponse(
           java.lang.Object[] setPacchettoDiVersamentoFatturaPAResult) {
           this.setPacchettoDiVersamentoFatturaPAResult = setPacchettoDiVersamentoFatturaPAResult;
    }


    /**
     * Gets the setPacchettoDiVersamentoFatturaPAResult value for this SetPacchettoDiVersamentoFatturaPAResponse.
     * 
     * @return setPacchettoDiVersamentoFatturaPAResult
     */
    public java.lang.Object[] getSetPacchettoDiVersamentoFatturaPAResult() {
        return setPacchettoDiVersamentoFatturaPAResult;
    }


    /**
     * Sets the setPacchettoDiVersamentoFatturaPAResult value for this SetPacchettoDiVersamentoFatturaPAResponse.
     * 
     * @param setPacchettoDiVersamentoFatturaPAResult
     */
    public void setSetPacchettoDiVersamentoFatturaPAResult(java.lang.Object[] setPacchettoDiVersamentoFatturaPAResult) {
        this.setPacchettoDiVersamentoFatturaPAResult = setPacchettoDiVersamentoFatturaPAResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SetPacchettoDiVersamentoFatturaPAResponse)) return false;
        SetPacchettoDiVersamentoFatturaPAResponse other = (SetPacchettoDiVersamentoFatturaPAResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.setPacchettoDiVersamentoFatturaPAResult==null && other.getSetPacchettoDiVersamentoFatturaPAResult()==null) || 
             (this.setPacchettoDiVersamentoFatturaPAResult!=null &&
              java.util.Arrays.equals(this.setPacchettoDiVersamentoFatturaPAResult, other.getSetPacchettoDiVersamentoFatturaPAResult())));
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
        if (getSetPacchettoDiVersamentoFatturaPAResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSetPacchettoDiVersamentoFatturaPAResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSetPacchettoDiVersamentoFatturaPAResult(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SetPacchettoDiVersamentoFatturaPAResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">SetPacchettoDiVersamentoFatturaPAResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("setPacchettoDiVersamentoFatturaPAResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "SetPacchettoDiVersamentoFatturaPAResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "anyType"));
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
