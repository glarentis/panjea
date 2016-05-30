/**
 * SolutionDOC_HubSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class SolutionDOC_HubSoapStub extends org.apache.axis.client.Stub implements SolutionDOC_HubSoap {
	private static void _initOperationDesc1() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("ContattoHub");
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "ContattoHubResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[0] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("CheckClienteFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "CheckClienteFatturaPAResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[1] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("GetUrlLoginUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"urlClient"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"username"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetUrlLoginUserResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[2] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("GetIdUnivocoFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codicePaese"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"identificativoTrasmittente"), org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class,
				false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetIdUnivocoFatturaPAResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[3] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("GetNomeFileZipFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetNomeFileZipFatturaPAResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[4] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("UploadFileFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"nomeFile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"buffer"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"offset"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "UploadFileFatturaPAResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[5] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("InvioSdiFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"nomeFile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codicePaese"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"identificativoTrasmittente"), org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class,
				false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"rinominaFile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"firmaTerzoIntermediario"), org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false,
				false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType"));
		oper.setReturnClass(java.lang.Object[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "InvioSdiFatturaPAResult"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "anyType"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[6] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("EsitoInvioSdiFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"identificativoSdi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"nomeFileFatturaPA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType"));
		oper.setReturnClass(java.lang.Object[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "EsitoInvioSdiFatturaPAResult"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "anyType"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[7] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("ImportaFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"nomeFileZip"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"nomeFileRiepilogo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType"));
		oper.setReturnClass(java.lang.Object[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "ImportaFatturaPAResult"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "anyType"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[8] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("GetTransazioniSdiFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"identificativoSdi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"nomeFileZip"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"nomeFileFatturaPA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"Data_Da"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"Data_A"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetTransazioniSdiFatturaPAResponse>GetTransazioniSdiFatturaPAResult"));
		oper.setReturnClass(GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult.class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetTransazioniSdiFatturaPAResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[9] = oper;

	}

	private static void _initOperationDesc2() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("GetEsitiSdiFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"idTransazione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"identificativoSdi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"nomeFileFatturaPA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetEsitiSdiFatturaPAResponse>GetEsitiSdiFatturaPAResult"));
		oper.setReturnClass(GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult.class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetEsitiSdiFatturaPAResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[10] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("GetLogFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"identificativoSdi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"nomeFileZip"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"nomeServizio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"messaggio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"Data_Da"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"Data_A"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetLogFatturaPAResponse>GetLogFatturaPAResult"));
		oper.setReturnClass(GetLogFatturaPAResponseGetLogFatturaPAResult.class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetLogFatturaPAResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[11] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("GetFileFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"idTransazione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"estrazioneP7M"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType"));
		oper.setReturnClass(java.lang.Object[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetFileFatturaPAResult"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "anyType"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[12] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("GetFileEsitoFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"idEsito"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType"));
		oper.setReturnClass(java.lang.Object[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetFileEsitoFatturaPAResult"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "anyType"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[13] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("SetFirmatarioFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"username"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"urlFirma"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"usernameFirma"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordFirma"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"nome"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"cognome"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"societa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"note"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"email"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"emailPec"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"consenso"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"revoca"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"soggettoGiuridico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceFiscalePaese"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceFiscaleCodice"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"partitaIvaPaese"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"partitaIvaCodice"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"anagraficaTitolo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"anagraficaCodiceEORI"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "SetFirmatarioFatturaPAResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[14] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("SetPacchettoDiVersamentoFatturaPA");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"username"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"isFattura"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"urlServizio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"usernameServizio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"intervalloRdV"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"intervalloPdA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"numPacchettiRdV"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"numPacchettiPdA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"sendEmail"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"idCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"numMaxFiles"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType"));
		oper.setReturnClass(java.lang.Object[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/",
				"SetPacchettoDiVersamentoFatturaPAResult"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "anyType"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[15] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("GetStatoProcessoConservazione");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"identificativoSdi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetStatoProcessoConservazioneResponse>GetStatoProcessoConservazioneResult"));
		oper.setReturnClass(GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult.class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetStatoProcessoConservazioneResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[16] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("GetRapportoVersamento");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"listaIdRdV"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://tempuri.org/", "ArrayOfString"), java.lang.String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "string"));
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
		oper.setReturnClass(byte[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetRapportoVersamentoResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[17] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("GetPacchettoDistribuzione");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceCliente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"passwordServizi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"listaIdFattura"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://tempuri.org/", "ArrayOfString"), java.lang.String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "string"));
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"listaIdEsito"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://tempuri.org/", "ArrayOfString"), java.lang.String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "string"));
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"nomeUtente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"cognomeUtente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceFiscalePaese"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"codiceFiscale"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"partitaIvaPaese"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/",
				"partitaIva"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
				"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
		oper.setReturnClass(byte[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "GetPacchettoDistribuzioneResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[18] = oper;

	}

	private java.util.Vector cachedSerClasses = new java.util.Vector();
	private java.util.Vector cachedSerQNames = new java.util.Vector();

	private java.util.Vector cachedSerFactories = new java.util.Vector();

	private java.util.Vector cachedDeserFactories = new java.util.Vector();

	static org.apache.axis.description.OperationDesc[] _operations;

	static {
		_operations = new org.apache.axis.description.OperationDesc[19];
		_initOperationDesc1();
		_initOperationDesc2();
	}

	public SolutionDOC_HubSoapStub() throws org.apache.axis.AxisFault {
		this(null);
	}

	public SolutionDOC_HubSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service)
			throws org.apache.axis.AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public SolutionDOC_HubSoapStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}
		((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
		java.lang.Class cls;
		javax.xml.namespace.QName qName;
		javax.xml.namespace.QName qName2;
		java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
		java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
		java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
		java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
		java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
		java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
		java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
		java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
		java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
		java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
		qName = new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetEsitiSdiFatturaPAResponse>GetEsitiSdiFatturaPAResult");
		cachedSerQNames.add(qName);
		cls = GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">>GetLogFatturaPAResponse>GetLogFatturaPAResult");
		cachedSerQNames.add(qName);
		cls = GetLogFatturaPAResponseGetLogFatturaPAResult.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetStatoProcessoConservazioneResponse>GetStatoProcessoConservazioneResult");
		cachedSerQNames.add(qName);
		cls = GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/",
				">>GetTransazioniSdiFatturaPAResponse>GetTransazioniSdiFatturaPAResult");
		cachedSerQNames.add(qName);
		cls = GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">CheckClienteFatturaPA");
		cachedSerQNames.add(qName);
		cls = CheckClienteFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">CheckClienteFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = CheckClienteFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">ContattoHub");
		cachedSerQNames.add(qName);
		cls = ContattoHub.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">ContattoHubResponse");
		cachedSerQNames.add(qName);
		cls = ContattoHubResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">EsitoInvioSdiFatturaPA");
		cachedSerQNames.add(qName);
		cls = EsitoInvioSdiFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">EsitoInvioSdiFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = EsitoInvioSdiFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetEsitiSdiFatturaPA");
		cachedSerQNames.add(qName);
		cls = GetEsitiSdiFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetEsitiSdiFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = GetEsitiSdiFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetFileEsitoFatturaPA");
		cachedSerQNames.add(qName);
		cls = GetFileEsitoFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetFileEsitoFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = GetFileEsitoFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetFileFatturaPA");
		cachedSerQNames.add(qName);
		cls = GetFileFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetFileFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = GetFileFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetIdUnivocoFatturaPA");
		cachedSerQNames.add(qName);
		cls = GetIdUnivocoFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetIdUnivocoFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = GetIdUnivocoFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetLogFatturaPA");
		cachedSerQNames.add(qName);
		cls = GetLogFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetLogFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = GetLogFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetNomeFileZipFatturaPA");
		cachedSerQNames.add(qName);
		cls = GetNomeFileZipFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetNomeFileZipFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = GetNomeFileZipFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetPacchettoDistribuzione");
		cachedSerQNames.add(qName);
		cls = GetPacchettoDistribuzione.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetPacchettoDistribuzioneResponse");
		cachedSerQNames.add(qName);
		cls = GetPacchettoDistribuzioneResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetRapportoVersamento");
		cachedSerQNames.add(qName);
		cls = GetRapportoVersamento.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetRapportoVersamentoResponse");
		cachedSerQNames.add(qName);
		cls = GetRapportoVersamentoResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetStatoProcessoConservazione");
		cachedSerQNames.add(qName);
		cls = GetStatoProcessoConservazione.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetStatoProcessoConservazioneResponse");
		cachedSerQNames.add(qName);
		cls = GetStatoProcessoConservazioneResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetTransazioniSdiFatturaPA");
		cachedSerQNames.add(qName);
		cls = GetTransazioniSdiFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetTransazioniSdiFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = GetTransazioniSdiFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetUrlLoginUser");
		cachedSerQNames.add(qName);
		cls = GetUrlLoginUser.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">GetUrlLoginUserResponse");
		cachedSerQNames.add(qName);
		cls = GetUrlLoginUserResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">ImportaFatturaPA");
		cachedSerQNames.add(qName);
		cls = ImportaFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">ImportaFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = ImportaFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">InvioSdiFatturaPA");
		cachedSerQNames.add(qName);
		cls = InvioSdiFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">InvioSdiFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = InvioSdiFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">SetFirmatarioFatturaPA");
		cachedSerQNames.add(qName);
		cls = SetFirmatarioFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">SetFirmatarioFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = SetFirmatarioFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">SetPacchettoDiVersamentoFatturaPA");
		cachedSerQNames.add(qName);
		cls = SetPacchettoDiVersamentoFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">SetPacchettoDiVersamentoFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = SetPacchettoDiVersamentoFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">UploadFileFatturaPA");
		cachedSerQNames.add(qName);
		cls = UploadFileFatturaPA.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", ">UploadFileFatturaPAResponse");
		cachedSerQNames.add(qName);
		cls = UploadFileFatturaPAResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfAnyType");
		cachedSerQNames.add(qName);
		cls = java.lang.Object[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType");
		qName2 = new javax.xml.namespace.QName("http://tempuri.org/", "anyType");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("http://tempuri.org/", "ArrayOfString");
		cachedSerQNames.add(qName);
		cls = java.lang.String[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
		qName2 = new javax.xml.namespace.QName("http://tempuri.org/", "string");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

	}

	public java.lang.String checkClienteFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[1]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/CheckClienteFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "CheckClienteFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.String) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public java.lang.String contattoHub() throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/ContattoHub");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "ContattoHub"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.String) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
		try {
			org.apache.axis.client.Call _call = super._createCall();
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}
			java.util.Enumeration keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				java.lang.String key = (java.lang.String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}
			// All the type mapping information is registered
			// when the first call is made.
			// The type mapping information is actually registered in
			// the TypeMappingRegistry of the service, which
			// is the reason why registration is only needed for the first call.
			synchronized (this) {
				if (firstCall()) {
					// must set encoding style before registering serializers
					_call.setEncodingStyle(null);
					for (int i = 0; i < cachedSerFactories.size(); ++i) {
						java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
						javax.xml.namespace.QName qName = (javax.xml.namespace.QName) cachedSerQNames.get(i);
						java.lang.Object x = cachedSerFactories.get(i);
						if (x instanceof Class) {
							java.lang.Class sf = (java.lang.Class) cachedSerFactories.get(i);
							java.lang.Class df = (java.lang.Class) cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						} else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
							org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories
									.get(i);
							org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory) cachedDeserFactories
									.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
					}
				}
			}
			return _call;
		} catch (java.lang.Throwable _t) {
			throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
		}
	}

	public java.lang.Object[] esitoInvioSdiFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String identificativoSdi, java.lang.String nomeFileFatturaPA) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[7]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/EsitoInvioSdiFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "EsitoInvioSdiFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi,
					identificativoSdi, nomeFileFatturaPA });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.Object[]) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.Object[]) org.apache.axis.utils.JavaUtils
							.convert(_resp, java.lang.Object[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult getEsitiSdiFatturaPA(java.lang.String codiceCliente,
			byte[] passwordServizi, java.lang.String idTransazione, java.lang.String identificativoSdi,
			java.lang.String nomeFileFatturaPA) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[10]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetEsitiSdiFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "GetEsitiSdiFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi,
					idTransazione, identificativoSdi, nomeFileFatturaPA });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult) _resp;
				} catch (java.lang.Exception _exception) {
					return (GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult) org.apache.axis.utils.JavaUtils
							.convert(_resp, GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public java.lang.Object[] getFileEsitoFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String idEsito) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[13]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetFileEsitoFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "GetFileEsitoFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi, idEsito });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.Object[]) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.Object[]) org.apache.axis.utils.JavaUtils
							.convert(_resp, java.lang.Object[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public java.lang.Object[] getFileFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String idTransazione, boolean estrazioneP7M) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[12]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetFileFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "GetFileFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi,
					idTransazione, new java.lang.Boolean(estrazioneP7M) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.Object[]) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.Object[]) org.apache.axis.utils.JavaUtils
							.convert(_resp, java.lang.Object[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public java.lang.String getIdUnivocoFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String codicePaese, java.lang.String identificativoTrasmittente) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[3]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetIdUnivocoFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "GetIdUnivocoFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi, codicePaese,
					identificativoTrasmittente });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.String) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public GetLogFatturaPAResponseGetLogFatturaPAResult getLogFatturaPA(java.lang.String codiceCliente,
			byte[] passwordServizi, java.lang.String identificativoSdi, java.lang.String nomeFileZip,
			java.lang.String nomeServizio, java.lang.String messaggio, java.util.Calendar data_Da,
			java.util.Calendar data_A) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[11]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetLogFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "GetLogFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi,
					identificativoSdi, nomeFileZip, nomeServizio, messaggio, data_Da, data_A });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (GetLogFatturaPAResponseGetLogFatturaPAResult) _resp;
				} catch (java.lang.Exception _exception) {
					return (GetLogFatturaPAResponseGetLogFatturaPAResult) org.apache.axis.utils.JavaUtils.convert(
							_resp, GetLogFatturaPAResponseGetLogFatturaPAResult.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public java.lang.String getNomeFileZipFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[4]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetNomeFileZipFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "GetNomeFileZipFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.String) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public byte[] getPacchettoDistribuzione(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String[] listaIdFattura, java.lang.String[] listaIdEsito, java.lang.String nomeUtente,
			java.lang.String cognomeUtente, java.lang.String codiceFiscalePaese, java.lang.String codiceFiscale,
			java.lang.String partitaIvaPaese, java.lang.String partitaIva) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[18]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetPacchettoDistribuzione");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "GetPacchettoDistribuzione"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi,
					listaIdFattura, listaIdEsito, nomeUtente, cognomeUtente, codiceFiscalePaese, codiceFiscale,
					partitaIvaPaese, partitaIva });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (byte[]) _resp;
				} catch (java.lang.Exception _exception) {
					return (byte[]) org.apache.axis.utils.JavaUtils.convert(_resp, byte[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public byte[] getRapportoVersamento(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String[] listaIdRdV) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[17]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetRapportoVersamento");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "GetRapportoVersamento"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call
					.invoke(new java.lang.Object[] { codiceCliente, passwordServizi, listaIdRdV });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (byte[]) _resp;
				} catch (java.lang.Exception _exception) {
					return (byte[]) org.apache.axis.utils.JavaUtils.convert(_resp, byte[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult getStatoProcessoConservazione(
			java.lang.String codiceCliente, byte[] passwordServizi, java.lang.String identificativoSdi)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[16]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetStatoProcessoConservazione");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "GetStatoProcessoConservazione"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi,
					identificativoSdi });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult) _resp;
				} catch (java.lang.Exception _exception) {
					return (GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult) org.apache.axis.utils.JavaUtils
							.convert(_resp,
									GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult getTransazioniSdiFatturaPA(
			java.lang.String codiceCliente, byte[] passwordServizi, java.lang.String identificativoSdi,
			java.lang.String nomeFileZip, java.lang.String nomeFileFatturaPA, java.util.Calendar data_Da,
			java.util.Calendar data_A) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[9]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetTransazioniSdiFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "GetTransazioniSdiFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi,
					identificativoSdi, nomeFileZip, nomeFileFatturaPA, data_Da, data_A });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult) _resp;
				} catch (java.lang.Exception _exception) {
					return (GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult) org.apache.axis.utils.JavaUtils
							.convert(_resp, GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public java.lang.String getUrlLoginUser(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String urlClient, java.lang.String username, byte[] password) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[2]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetUrlLoginUser");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "GetUrlLoginUser"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi, urlClient,
					username, password });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.String) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public java.lang.Object[] importaFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String nomeFileZip, java.lang.String nomeFileRiepilogo) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[8]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/ImportaFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "ImportaFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi, nomeFileZip,
					nomeFileRiepilogo });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.Object[]) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.Object[]) org.apache.axis.utils.JavaUtils
							.convert(_resp, java.lang.Object[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public java.lang.Object[] invioSdiFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String nomeFile, java.lang.String codicePaese, java.lang.String identificativoTrasmittente,
			boolean rinominaFile, boolean firmaTerzoIntermediario) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[6]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/InvioSdiFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "InvioSdiFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi, nomeFile,
					codicePaese, identificativoTrasmittente, new java.lang.Boolean(rinominaFile),
					new java.lang.Boolean(firmaTerzoIntermediario) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.Object[]) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.Object[]) org.apache.axis.utils.JavaUtils
							.convert(_resp, java.lang.Object[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public java.lang.String setFirmatarioFatturaPA(java.lang.String username, byte[] password,
			java.lang.String urlFirma, java.lang.String usernameFirma, java.lang.String passwordFirma,
			java.lang.String nome, java.lang.String cognome, java.lang.String societa, java.lang.String note,
			java.lang.String email, java.lang.String emailPec, java.lang.String codiceCliente, boolean consenso,
			boolean revoca, boolean soggettoGiuridico, java.lang.String codiceFiscalePaese,
			java.lang.String codiceFiscaleCodice, java.lang.String partitaIvaPaese, java.lang.String partitaIvaCodice,
			java.lang.String anagraficaTitolo, java.lang.String anagraficaCodiceEORI) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[14]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/SetFirmatarioFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "SetFirmatarioFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { username, password, urlFirma, usernameFirma,
					passwordFirma, nome, cognome, societa, note, email, emailPec, codiceCliente,
					new java.lang.Boolean(consenso), new java.lang.Boolean(revoca),
					new java.lang.Boolean(soggettoGiuridico), codiceFiscalePaese, codiceFiscaleCodice, partitaIvaPaese,
					partitaIvaCodice, anagraficaTitolo, anagraficaCodiceEORI });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.String) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public java.lang.Object[] setPacchettoDiVersamentoFatturaPA(java.lang.String username, byte[] password,
			boolean isFattura, java.lang.String urlServizio, java.lang.String usernameServizio,
			byte[] passwordServizio, java.lang.String intervalloRdV, java.lang.String intervalloPdA,
			int numPacchettiRdV, int numPacchettiPdA, boolean sendEmail, java.lang.String idCliente, int numMaxFiles)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[15]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/SetPacchettoDiVersamentoFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "SetPacchettoDiVersamentoFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { username, password,
					new java.lang.Boolean(isFattura), urlServizio, usernameServizio, passwordServizio, intervalloRdV,
					intervalloPdA, new java.lang.Integer(numPacchettiRdV), new java.lang.Integer(numPacchettiPdA),
					new java.lang.Boolean(sendEmail), idCliente, new java.lang.Integer(numMaxFiles) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.Object[]) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.Object[]) org.apache.axis.utils.JavaUtils
							.convert(_resp, java.lang.Object[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public java.lang.String uploadFileFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String nomeFile, byte[] buffer, long offset) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[5]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/UploadFileFatturaPA");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "UploadFileFatturaPA"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { codiceCliente, passwordServizi, nomeFile,
					buffer, new java.lang.Long(offset) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.String) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

}
