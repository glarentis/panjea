/**
 * InvioSdiFatturaPAResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class InvioSdiFatturaPAResponse  implements java.io.Serializable {
    private java.lang.Object[] invioSdiFatturaPAResult;

    public InvioSdiFatturaPAResponse() {
    }

    public InvioSdiFatturaPAResponse(
           java.lang.Object[] invioSdiFatturaPAResult) {
           this.invioSdiFatturaPAResult = invioSdiFatturaPAResult;
    }


    /**
     * Gets the invioSdiFatturaPAResult value for this InvioSdiFatturaPAResponse.
     * 
     * @return invioSdiFatturaPAResult
     */
    public java.lang.Object[] getInvioSdiFatturaPAResult() {
        return invioSdiFatturaPAResult;
    }


    /**
     * Sets the invioSdiFatturaPAResult value for this InvioSdiFatturaPAResponse.
     * 
     * @param invioSdiFatturaPAResult
     */
    public void setInvioSdiFatturaPAResult(java.lang.Object[] invioSdiFatturaPAResult) {
        this.invioSdiFatturaPAResult = invioSdiFatturaPAResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InvioSdiFatturaPAResponse)) return false;
        InvioSdiFatturaPAResponse other = (InvioSdiFatturaPAResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.invioSdiFatturaPAResult==null && other.getInvioSdiFatturaPAResult()==null) || 
             (this.invioSdiFatturaPAResult!=null &&
              java.util.Arrays.equals(this.invioSdiFatturaPAResult, other.getInvioSdiFatturaPAResult())));
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
        if (getInvioSdiFatturaPAResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInvioSdiFatturaPAResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInvioSdiFatturaPAResult(), i);
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
        new org.apache.axis.description.TypeDesc(InvioSdiFatturaPAResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">InvioSdiFatturaPAResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invioSdiFatturaPAResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "InvioSdiFatturaPAResult"));
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
