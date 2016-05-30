/**
 * SolutionDOC_HubLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class SolutionDOC_HubLocator extends org.apache.axis.client.Service implements SolutionDOC_Hub {

	// Use to get a proxy class for SolutionDOC_HubSoap
	private java.lang.String SolutionDOC_HubSoap_address = "http://localhost/SolutionUpdate/SolutionDOC_Hub.asmx";

	// The WSDD service name defaults to the port name.
	private java.lang.String SolutionDOC_HubSoapWSDDServiceName = "SolutionDOC_HubSoap";

	private java.util.HashSet ports = null;

	public SolutionDOC_HubLocator() {
	}

	public SolutionDOC_HubLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName)
			throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	public SolutionDOC_HubLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	/**
	 * For the given interface, get the stub implementation. If this service has no port for the given interface, then
	 * ServiceException is thrown.
	 */
	@Override
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (SolutionDOC_HubSoap.class.isAssignableFrom(serviceEndpointInterface)) {
				SolutionDOC_HubSoapStub _stub = new SolutionDOC_HubSoapStub(new java.net.URL(
						SolutionDOC_HubSoap_address), this);
				_stub.setPortName(getSolutionDOC_HubSoapWSDDServiceName());
				return _stub;
			}
		} catch (java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  "
				+ (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	/**
	 * For the given interface, get the stub implementation. If this service has no port for the given interface, then
	 * ServiceException is thrown.
	 */
	@Override
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface)
			throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		java.lang.String inputPortName = portName.getLocalPart();
		if ("SolutionDOC_HubSoap".equals(inputPortName)) {
			return getSolutionDOC_HubSoap();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	@Override
	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "SolutionDOC_HubSoap"));
		}
		return ports.iterator();
	}

	@Override
	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("http://tempuri.org/", "SolutionDOC_Hub");
	}

	public SolutionDOC_HubSoap getSolutionDOC_HubSoap() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(SolutionDOC_HubSoap_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getSolutionDOC_HubSoap(endpoint);
	}

	public SolutionDOC_HubSoap getSolutionDOC_HubSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			SolutionDOC_HubSoapStub _stub = new SolutionDOC_HubSoapStub(portAddress, this);
			_stub.setPortName(getSolutionDOC_HubSoapWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public java.lang.String getSolutionDOC_HubSoapAddress() {
		return SolutionDOC_HubSoap_address;
	}

	public java.lang.String getSolutionDOC_HubSoapWSDDServiceName() {
		return SolutionDOC_HubSoapWSDDServiceName;
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName, java.lang.String address)
			throws javax.xml.rpc.ServiceException {

		if ("SolutionDOC_HubSoap".equals(portName)) {
			setSolutionDOC_HubSoapEndpointAddress(address);
		} else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address)
			throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}

	public void setSolutionDOC_HubSoapEndpointAddress(java.lang.String address) {
		SolutionDOC_HubSoap_address = address;
	}

	public void setSolutionDOC_HubSoapWSDDServiceName(java.lang.String name) {
		SolutionDOC_HubSoapWSDDServiceName = name;
	}

}
