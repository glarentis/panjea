/**
 * CheckVatServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.anagrafica.rich.editors.entita.builder;

public class CheckVatServiceLocator extends org.apache.axis.client.Service implements it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatService {

    public CheckVatServiceLocator() {
    }


    public CheckVatServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CheckVatServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for checkVatPort
    private java.lang.String checkVatPort_address = "http://ec.europa.eu/taxation_customs/vies/services/checkVatService";

    public java.lang.String getcheckVatPortAddress() {
        return checkVatPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String checkVatPortWSDDServiceName = "checkVatPort";

    public java.lang.String getcheckVatPortWSDDServiceName() {
        return checkVatPortWSDDServiceName;
    }

    public void setcheckVatPortWSDDServiceName(java.lang.String name) {
        checkVatPortWSDDServiceName = name;
    }

    public it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatPortType getcheckVatPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(checkVatPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getcheckVatPort(endpoint);
    }

    public it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatPortType getcheckVatPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatBindingStub _stub = new it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatBindingStub(portAddress, this);
            _stub.setPortName(getcheckVatPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setcheckVatPortEndpointAddress(java.lang.String address) {
        checkVatPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatBindingStub _stub = new it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatBindingStub(new java.net.URL(checkVatPort_address), this);
                _stub.setPortName(getcheckVatPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("checkVatPort".equals(inputPortName)) {
            return getcheckVatPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat", "checkVatService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat", "checkVatPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("checkVatPort".equals(portName)) {
            setcheckVatPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
