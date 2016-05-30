/**
 * UploadFileFatturaPAResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class UploadFileFatturaPAResponse  implements java.io.Serializable {
    private java.lang.String uploadFileFatturaPAResult;

    public UploadFileFatturaPAResponse() {
    }

    public UploadFileFatturaPAResponse(
           java.lang.String uploadFileFatturaPAResult) {
           this.uploadFileFatturaPAResult = uploadFileFatturaPAResult;
    }


    /**
     * Gets the uploadFileFatturaPAResult value for this UploadFileFatturaPAResponse.
     * 
     * @return uploadFileFatturaPAResult
     */
    public java.lang.String getUploadFileFatturaPAResult() {
        return uploadFileFatturaPAResult;
    }


    /**
     * Sets the uploadFileFatturaPAResult value for this UploadFileFatturaPAResponse.
     * 
     * @param uploadFileFatturaPAResult
     */
    public void setUploadFileFatturaPAResult(java.lang.String uploadFileFatturaPAResult) {
        this.uploadFileFatturaPAResult = uploadFileFatturaPAResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UploadFileFatturaPAResponse)) return false;
        UploadFileFatturaPAResponse other = (UploadFileFatturaPAResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.uploadFileFatturaPAResult==null && other.getUploadFileFatturaPAResult()==null) || 
             (this.uploadFileFatturaPAResult!=null &&
              this.uploadFileFatturaPAResult.equals(other.getUploadFileFatturaPAResult())));
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
        if (getUploadFileFatturaPAResult() != null) {
            _hashCode += getUploadFileFatturaPAResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UploadFileFatturaPAResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">UploadFileFatturaPAResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uploadFileFatturaPAResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "UploadFileFatturaPAResult"));
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
