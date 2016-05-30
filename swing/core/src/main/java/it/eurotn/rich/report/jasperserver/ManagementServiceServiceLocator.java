/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 */

/**
 * ManagementServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.rich.report.jasperserver;

public class ManagementServiceServiceLocator extends org.apache.axis.client.Service
        implements ManagementServiceService {

    /**
     * 
     */
    private static final long serialVersionUID = -6165858402411876798L;

    // Use to get a proxy class for repository
    private java.lang.String repository_address = "http://127.0.0.1:8080/jasperserver/services/repository";

    // The WSDD service name defaults to the port name.
    private java.lang.String repositoryWSDDServiceName = "repository";

    private java.util.HashSet ports = null;

    public ManagementServiceServiceLocator() {
    }

    public ManagementServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName)
            throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    public ManagementServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    /**
     * For the given interface, get the stub implementation. If this service has no port for the given interface, then
     * ServiceException is thrown.
     */
    @Override
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ManagementService.class.isAssignableFrom(serviceEndpointInterface)) {
                RepositorySoapBindingStub _stub = new RepositorySoapBindingStub(new java.net.URL(repository_address),
                        this);
                _stub.setPortName(getrepositoryWSDDServiceName());
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
        if ("repository".equals(inputPortName)) {
            return getrepository();
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
            ports.add(new javax.xml.namespace.QName("http://127.0.0.1:8080/jasperserver/services/repository",
                    "repository"));
        }
        return ports.iterator();
    }

    @Override
    public ManagementService getrepository() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(repository_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getrepository(endpoint);
    }

    @Override
    public ManagementService getrepository(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            RepositorySoapBindingStub _stub = new RepositorySoapBindingStub(portAddress, this);
            _stub.setPortName(getrepositoryWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    @Override
    public java.lang.String getrepositoryAddress() {
        return repository_address;
    }

    public java.lang.String getrepositoryWSDDServiceName() {
        return repositoryWSDDServiceName;
    }

    @Override
    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://127.0.0.1:8080/jasperserver/services/repository",
                "ManagementServiceService");
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address)
            throws javax.xml.rpc.ServiceException {

        if ("repository".equals(portName)) {
            setrepositoryEndpointAddress(address);
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

    public void setrepositoryEndpointAddress(java.lang.String address) {
        repository_address = address;
    }

    public void setrepositoryWSDDServiceName(java.lang.String name) {
        repositoryWSDDServiceName = name;
    }

}
