/**
 * SolutionDOC_HubSoapSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class SolutionDOC_HubSoapSkeleton implements SolutionDOC_HubSoap, org.apache.axis.wsdl.Skeleton {
	/**
	 * Returns List of OperationDesc objects with this name
	 */
	public static java.util.List getOperationDescByName(java.lang.String methodName) {
		return (java.util.List) _myOperations.get(methodName);
	}

	/**
	 * Returns Collection of OperationDescs
	 */
	public static java.util.Collection getOperationDescs() {
		return _myOperationsList;
	}

	private SolutionDOC_HubSoap impl;

	private static java.util.Map _myOperations = new java.util.Hashtable();

	private static java.util.Collection _myOperationsList = new java.util.ArrayList();

	static {
		org.apache.axis.description.OperationDesc _oper;
		org.apache.axis.description.FaultDesc _fault;
		org.apache.axis.description.ParameterDesc[] _params;
		_params = new org.apache.axis.description.ParameterDesc[] {};
		_oper = new org.apache.axis.description.OperationDesc("contattoHub", _params, new javax.xml.namespace.QName(
				"http://tempuri.org/", "ContattoHubResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "ContattoHub"));
		_oper.setSoapAction("http://tempuri.org/ContattoHub");
		_myOperationsList.add(_oper);
		if (_myOperations.get("contattoHub") == null) {
			_myOperations.put("contattoHub", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("contattoHub")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("checkClienteFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "CheckClienteFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "CheckClienteFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/CheckClienteFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("checkClienteFatturaPA") == null) {
			_myOperations.put("checkClienteFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("checkClienteFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"urlClient"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"username"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("getUrlLoginUser", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "GetUrlLoginUserResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetUrlLoginUser"));
		_oper.setSoapAction("http://tempuri.org/GetUrlLoginUser");
		_myOperationsList.add(_oper);
		if (_myOperations.get("getUrlLoginUser") == null) {
			_myOperations.put("getUrlLoginUser", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("getUrlLoginUser")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codicePaese"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"identificativoTrasmittente"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("getIdUnivocoFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "GetIdUnivocoFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetIdUnivocoFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/GetIdUnivocoFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("getIdUnivocoFatturaPA") == null) {
			_myOperations.put("getIdUnivocoFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("getIdUnivocoFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("getNomeFileZipFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "GetNomeFileZipFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetNomeFileZipFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/GetNomeFileZipFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("getNomeFileZipFatturaPA") == null) {
			_myOperations.put("getNomeFileZipFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("getNomeFileZipFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"nomeFile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"buffer"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"offset"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("uploadFileFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "UploadFileFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "UploadFileFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/UploadFileFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("uploadFileFatturaPA") == null) {
			_myOperations.put("uploadFileFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("uploadFileFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"nomeFile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codicePaese"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"identificativoTrasmittente"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"rinominaFile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"firmaTerzoIntermediario"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class,
						false, false), };
		_oper = new org.apache.axis.description.OperationDesc("invioSdiFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "InvioSdiFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "InvioSdiFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/InvioSdiFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("invioSdiFatturaPA") == null) {
			_myOperations.put("invioSdiFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("invioSdiFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"identificativoSdi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"nomeFileFatturaPA"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("esitoInvioSdiFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "EsitoInvioSdiFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "EsitoInvioSdiFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/EsitoInvioSdiFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("esitoInvioSdiFatturaPA") == null) {
			_myOperations.put("esitoInvioSdiFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("esitoInvioSdiFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"nomeFileZip"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"nomeFileRiepilogo"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("importaFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "ImportaFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "ImportaFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/ImportaFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("importaFatturaPA") == null) {
			_myOperations.put("importaFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("importaFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"identificativoSdi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"nomeFileZip"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"nomeFileFatturaPA"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"Data_Da"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"Data_A"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("getTransazioniSdiFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "GetTransazioniSdiFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetTransazioniSdiFatturaPAResponse>GetTransazioniSdiFatturaPAResult"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetTransazioniSdiFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/GetTransazioniSdiFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("getTransazioniSdiFatturaPA") == null) {
			_myOperations.put("getTransazioniSdiFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("getTransazioniSdiFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"idTransazione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"identificativoSdi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"nomeFileFatturaPA"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("getEsitiSdiFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "GetEsitiSdiFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetEsitiSdiFatturaPAResponse>GetEsitiSdiFatturaPAResult"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetEsitiSdiFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/GetEsitiSdiFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("getEsitiSdiFatturaPA") == null) {
			_myOperations.put("getEsitiSdiFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("getEsitiSdiFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"identificativoSdi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"nomeFileZip"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"nomeServizio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"messaggio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"Data_Da"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"Data_A"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("getLogFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "GetLogFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetLogFatturaPAResponse>GetLogFatturaPAResult"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetLogFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/GetLogFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("getLogFatturaPA") == null) {
			_myOperations.put("getLogFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("getLogFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"idTransazione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"estrazioneP7M"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("getFileFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "GetFileFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetFileFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/GetFileFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("getFileFatturaPA") == null) {
			_myOperations.put("getFileFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("getFileFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"idEsito"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("getFileEsitoFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "GetFileEsitoFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetFileEsitoFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/GetFileEsitoFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("getFileEsitoFatturaPA") == null) {
			_myOperations.put("getFileEsitoFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("getFileEsitoFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"username"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"urlFirma"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"usernameFirma"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordFirma"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"nome"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"cognome"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"societa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"note"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"email"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"emailPec"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"consenso"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"revoca"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"soggettoGiuridico"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class,
						false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceFiscalePaese"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceFiscaleCodice"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"partitaIvaPaese"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"partitaIvaCodice"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"anagraficaTitolo"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"anagraficaCodiceEORI"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("setFirmatarioFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "SetFirmatarioFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "SetFirmatarioFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/SetFirmatarioFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("setFirmatarioFatturaPA") == null) {
			_myOperations.put("setFirmatarioFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("setFirmatarioFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"username"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"isFattura"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"urlServizio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"usernameServizio"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizio"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"intervalloRdV"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"intervalloPdA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"numPacchettiRdV"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false,
						false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"numPacchettiPdA"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false,
						false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"sendEmail"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"idCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"numMaxFiles"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("setPacchettoDiVersamentoFatturaPA", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "SetPacchettoDiVersamentoFatturaPAResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "SetPacchettoDiVersamentoFatturaPA"));
		_oper.setSoapAction("http://tempuri.org/SetPacchettoDiVersamentoFatturaPA");
		_myOperationsList.add(_oper);
		if (_myOperations.get("setPacchettoDiVersamentoFatturaPA") == null) {
			_myOperations.put("setPacchettoDiVersamentoFatturaPA", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("setPacchettoDiVersamentoFatturaPA")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"identificativoSdi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("getStatoProcessoConservazione", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "GetStatoProcessoConservazioneResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetStatoProcessoConservazioneResponse>GetStatoProcessoConservazioneResult"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetStatoProcessoConservazione"));
		_oper.setSoapAction("http://tempuri.org/GetStatoProcessoConservazione");
		_myOperationsList.add(_oper);
		if (_myOperations.get("getStatoProcessoConservazione") == null) {
			_myOperations.put("getStatoProcessoConservazione", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("getStatoProcessoConservazione")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"listaIdRdV"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://tempuri.org/", "ArrayOfString"), java.lang.String[].class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("getRapportoVersamento", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "GetRapportoVersamentoResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetRapportoVersamento"));
		_oper.setSoapAction("http://tempuri.org/GetRapportoVersamento");
		_myOperationsList.add(_oper);
		if (_myOperations.get("getRapportoVersamento") == null) {
			_myOperations.put("getRapportoVersamento", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("getRapportoVersamento")).add(_oper);
		_params = new org.apache.axis.description.ParameterDesc[] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"passwordServizi"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"),
						byte[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"listaIdFattura"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://tempuri.org/", "ArrayOfString"), java.lang.String[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"listaIdEsito"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://tempuri.org/", "ArrayOfString"), java.lang.String[].class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"nomeUtente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"cognomeUtente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceFiscalePaese"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"codiceFiscale"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"partitaIvaPaese"), org.apache.axis.description.ParameterDesc.IN,
						new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false),
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
						"partitaIva"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), };
		_oper = new org.apache.axis.description.OperationDesc("getPacchettoDistribuzione", _params,
				new javax.xml.namespace.QName("http://tempuri.org/", "GetPacchettoDistribuzioneResult"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetPacchettoDistribuzione"));
		_oper.setSoapAction("http://tempuri.org/GetPacchettoDistribuzione");
		_myOperationsList.add(_oper);
		if (_myOperations.get("getPacchettoDistribuzione") == null) {
			_myOperations.put("getPacchettoDistribuzione", new java.util.ArrayList());
		}
		((java.util.List) _myOperations.get("getPacchettoDistribuzione")).add(_oper);
	}

	public SolutionDOC_HubSoapSkeleton() {
		this.impl = new SolutionDOC_HubSoapImpl();
	}

	public SolutionDOC_HubSoapSkeleton(SolutionDOC_HubSoap impl) {
		this.impl = impl;
	}

	public java.lang.String checkClienteFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi)
			throws java.rmi.RemoteException {
		java.lang.String ret = impl.checkClienteFatturaPA(codiceCliente, passwordServizi);
		return ret;
	}

	public java.lang.String contattoHub() throws java.rmi.RemoteException {
		java.lang.String ret = impl.contattoHub();
		return ret;
	}

	public java.lang.Object[] esitoInvioSdiFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String identificativoSdi, java.lang.String nomeFileFatturaPA) throws java.rmi.RemoteException {
		java.lang.Object[] ret = impl.esitoInvioSdiFatturaPA(codiceCliente, passwordServizi, identificativoSdi,
				nomeFileFatturaPA);
		return ret;
	}

	public GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult getEsitiSdiFatturaPA(java.lang.String codiceCliente,
			byte[] passwordServizi, java.lang.String idTransazione, java.lang.String identificativoSdi,
			java.lang.String nomeFileFatturaPA) throws java.rmi.RemoteException {
		GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult ret = impl.getEsitiSdiFatturaPA(codiceCliente,
				passwordServizi, idTransazione, identificativoSdi, nomeFileFatturaPA);
		return ret;
	}

	public java.lang.Object[] getFileEsitoFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String idEsito) throws java.rmi.RemoteException {
		java.lang.Object[] ret = impl.getFileEsitoFatturaPA(codiceCliente, passwordServizi, idEsito);
		return ret;
	}

	public java.lang.Object[] getFileFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String idTransazione, boolean estrazioneP7M) throws java.rmi.RemoteException {
		java.lang.Object[] ret = impl.getFileFatturaPA(codiceCliente, passwordServizi, idTransazione, estrazioneP7M);
		return ret;
	}

	public java.lang.String getIdUnivocoFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String codicePaese, java.lang.String identificativoTrasmittente) throws java.rmi.RemoteException {
		java.lang.String ret = impl.getIdUnivocoFatturaPA(codiceCliente, passwordServizi, codicePaese,
				identificativoTrasmittente);
		return ret;
	}

	public GetLogFatturaPAResponseGetLogFatturaPAResult getLogFatturaPA(java.lang.String codiceCliente,
			byte[] passwordServizi, java.lang.String identificativoSdi, java.lang.String nomeFileZip,
			java.lang.String nomeServizio, java.lang.String messaggio, java.util.Calendar data_Da,
			java.util.Calendar data_A) throws java.rmi.RemoteException {
		GetLogFatturaPAResponseGetLogFatturaPAResult ret = impl.getLogFatturaPA(codiceCliente, passwordServizi,
				identificativoSdi, nomeFileZip, nomeServizio, messaggio, data_Da, data_A);
		return ret;
	}

	public java.lang.String getNomeFileZipFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi)
			throws java.rmi.RemoteException {
		java.lang.String ret = impl.getNomeFileZipFatturaPA(codiceCliente, passwordServizi);
		return ret;
	}

	public byte[] getPacchettoDistribuzione(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String[] listaIdFattura, java.lang.String[] listaIdEsito, java.lang.String nomeUtente,
			java.lang.String cognomeUtente, java.lang.String codiceFiscalePaese, java.lang.String codiceFiscale,
			java.lang.String partitaIvaPaese, java.lang.String partitaIva) throws java.rmi.RemoteException {
		byte[] ret = impl.getPacchettoDistribuzione(codiceCliente, passwordServizi, listaIdFattura, listaIdEsito,
				nomeUtente, cognomeUtente, codiceFiscalePaese, codiceFiscale, partitaIvaPaese, partitaIva);
		return ret;
	}

	public byte[] getRapportoVersamento(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String[] listaIdRdV) throws java.rmi.RemoteException {
		byte[] ret = impl.getRapportoVersamento(codiceCliente, passwordServizi, listaIdRdV);
		return ret;
	}

	public GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult getStatoProcessoConservazione(
			java.lang.String codiceCliente, byte[] passwordServizi, java.lang.String identificativoSdi)
			throws java.rmi.RemoteException {
		GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult ret = impl
				.getStatoProcessoConservazione(codiceCliente, passwordServizi, identificativoSdi);
		return ret;
	}

	public GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult getTransazioniSdiFatturaPA(
			java.lang.String codiceCliente, byte[] passwordServizi, java.lang.String identificativoSdi,
			java.lang.String nomeFileZip, java.lang.String nomeFileFatturaPA, java.util.Calendar data_Da,
			java.util.Calendar data_A) throws java.rmi.RemoteException {
		GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult ret = impl.getTransazioniSdiFatturaPA(
				codiceCliente, passwordServizi, identificativoSdi, nomeFileZip, nomeFileFatturaPA, data_Da, data_A);
		return ret;
	}

	public java.lang.String getUrlLoginUser(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String urlClient, java.lang.String username, byte[] password) throws java.rmi.RemoteException {
		java.lang.String ret = impl.getUrlLoginUser(codiceCliente, passwordServizi, urlClient, username, password);
		return ret;
	}

	public java.lang.Object[] importaFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String nomeFileZip, java.lang.String nomeFileRiepilogo) throws java.rmi.RemoteException {
		java.lang.Object[] ret = impl.importaFatturaPA(codiceCliente, passwordServizi, nomeFileZip, nomeFileRiepilogo);
		return ret;
	}

	public java.lang.Object[] invioSdiFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String nomeFile, java.lang.String codicePaese, java.lang.String identificativoTrasmittente,
			boolean rinominaFile, boolean firmaTerzoIntermediario) throws java.rmi.RemoteException {
		java.lang.Object[] ret = impl.invioSdiFatturaPA(codiceCliente, passwordServizi, nomeFile, codicePaese,
				identificativoTrasmittente, rinominaFile, firmaTerzoIntermediario);
		return ret;
	}

	public java.lang.String setFirmatarioFatturaPA(java.lang.String username, byte[] password,
			java.lang.String urlFirma, java.lang.String usernameFirma, java.lang.String passwordFirma,
			java.lang.String nome, java.lang.String cognome, java.lang.String societa, java.lang.String note,
			java.lang.String email, java.lang.String emailPec, java.lang.String codiceCliente, boolean consenso,
			boolean revoca, boolean soggettoGiuridico, java.lang.String codiceFiscalePaese,
			java.lang.String codiceFiscaleCodice, java.lang.String partitaIvaPaese, java.lang.String partitaIvaCodice,
			java.lang.String anagraficaTitolo, java.lang.String anagraficaCodiceEORI) throws java.rmi.RemoteException {
		java.lang.String ret = impl.setFirmatarioFatturaPA(username, password, urlFirma, usernameFirma, passwordFirma,
				nome, cognome, societa, note, email, emailPec, codiceCliente, consenso, revoca, soggettoGiuridico,
				codiceFiscalePaese, codiceFiscaleCodice, partitaIvaPaese, partitaIvaCodice, anagraficaTitolo,
				anagraficaCodiceEORI);
		return ret;
	}

	public java.lang.Object[] setPacchettoDiVersamentoFatturaPA(java.lang.String username, byte[] password,
			boolean isFattura, java.lang.String urlServizio, java.lang.String usernameServizio,
			byte[] passwordServizio, java.lang.String intervalloRdV, java.lang.String intervalloPdA,
			int numPacchettiRdV, int numPacchettiPdA, boolean sendEmail, java.lang.String idCliente, int numMaxFiles)
			throws java.rmi.RemoteException {
		java.lang.Object[] ret = impl.setPacchettoDiVersamentoFatturaPA(username, password, isFattura, urlServizio,
				usernameServizio, passwordServizio, intervalloRdV, intervalloPdA, numPacchettiRdV, numPacchettiPdA,
				sendEmail, idCliente, numMaxFiles);
		return ret;
	}

	public java.lang.String uploadFileFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String nomeFile, byte[] buffer, long offset) throws java.rmi.RemoteException {
		java.lang.String ret = impl.uploadFileFatturaPA(codiceCliente, passwordServizi, nomeFile, buffer, offset);
		return ret;
	}

}
