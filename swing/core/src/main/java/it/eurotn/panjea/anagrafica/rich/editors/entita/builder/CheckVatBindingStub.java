/**
 * CheckVatBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.anagrafica.rich.editors.entita.builder;

public class CheckVatBindingStub extends org.apache.axis.client.Stub implements
		it.eurotn.panjea.anagrafica.rich.editors.entita.builder.CheckVatPortType {
	private java.util.Vector cachedSerClasses = new java.util.Vector();
	private java.util.Vector cachedSerQNames = new java.util.Vector();
	private java.util.Vector cachedSerFactories = new java.util.Vector();
	private java.util.Vector cachedDeserFactories = new java.util.Vector();

	static org.apache.axis.description.OperationDesc[] _operations;

	static {
		_operations = new org.apache.axis.description.OperationDesc[2];
		_initOperationDesc1();
	}

	private static void _initOperationDesc1() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("checkVat");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "countryCode"),
				org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "vatNumber"),
				org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "requestDate"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "date"), java.util.Date.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "valid"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "name"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "address"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[0] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("checkVatApprox");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "countryCode"),
				org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "vatNumber"),
				org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderName"),
				org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderCompanyType"),
				org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName(
						"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "companyTypeCode"),
				java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderStreet"),
				org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderPostcode"),
				org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderCity"),
				org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "requesterCountryCode"),
				org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "requesterVatNumber"),
				org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "requestDate"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "date"), java.util.Date.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "valid"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderAddress"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderNameMatch"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "matchCode"), MatchCode.class, false,
				false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderCompanyTypeMatch"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "matchCode"), MatchCode.class, false,
				false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderStreetMatch"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "matchCode"), MatchCode.class, false,
				false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderPostcodeMatch"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "matchCode"), MatchCode.class, false,
				false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderCityMatch"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "matchCode"), MatchCode.class, false,
				false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(
				"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "requestIdentifier"),
				org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[1] = oper;

	}

	public CheckVatBindingStub() throws org.apache.axis.AxisFault {
		this(null);
	}

	public CheckVatBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service)
			throws org.apache.axis.AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public CheckVatBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
		qName = new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types", "companyTypeCode");
		cachedSerQNames.add(qName);
		cls = java.lang.String.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(
				org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
		cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(
				org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

		qName = new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types", "matchCode");
		cachedSerQNames.add(qName);
		cls = MatchCode.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(enumsf);
		cachedDeserFactories.add(enumdf);

	}

	public void checkVat(javax.xml.rpc.holders.StringHolder countryCode, javax.xml.rpc.holders.StringHolder vatNumber,
			org.apache.axis.holders.DateHolder requestDate, javax.xml.rpc.holders.BooleanHolder valid,
			javax.xml.rpc.holders.StringHolder name, javax.xml.rpc.holders.StringHolder address)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
				"checkVat"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { countryCode.value, vatNumber.value });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				java.util.Map _output;
				_output = _call.getOutputParams();
				try {
					countryCode.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "countryCode"));
				} catch (java.lang.Exception _exception) {
					countryCode.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"countryCode")), java.lang.String.class);
				}
				try {
					vatNumber.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "vatNumber"));
				} catch (java.lang.Exception _exception) {
					vatNumber.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"vatNumber")), java.lang.String.class);
				}
				try {
					requestDate.value = (java.util.Date) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "requestDate"));
				} catch (java.lang.Exception _exception) {
					requestDate.value = (java.util.Date) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"requestDate")), java.util.Date.class);
				}
				try {
					valid.value = ((java.lang.Boolean) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "valid"))).booleanValue();
				} catch (java.lang.Exception _exception) {
					valid.value = ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"valid")), boolean.class)).booleanValue();
				}
				try {
					name.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "name"));
				} catch (java.lang.Exception _exception) {
					name.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"name")), java.lang.String.class);
				}
				try {
					address.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "address"));
				} catch (java.lang.Exception _exception) {
					address.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"address")), java.lang.String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public void checkVatApprox(javax.xml.rpc.holders.StringHolder countryCode,
			javax.xml.rpc.holders.StringHolder vatNumber, javax.xml.rpc.holders.StringHolder traderName,
			javax.xml.rpc.holders.StringHolder traderCompanyType, javax.xml.rpc.holders.StringHolder traderStreet,
			javax.xml.rpc.holders.StringHolder traderPostcode, javax.xml.rpc.holders.StringHolder traderCity,
			java.lang.String requesterCountryCode, java.lang.String requesterVatNumber,
			org.apache.axis.holders.DateHolder requestDate, javax.xml.rpc.holders.BooleanHolder valid,
			javax.xml.rpc.holders.StringHolder traderAddress, MatchCodeHolder traderNameMatch,
			MatchCodeHolder traderCompanyTypeMatch, MatchCodeHolder traderStreetMatch,
			MatchCodeHolder traderPostcodeMatch, MatchCodeHolder traderCityMatch,
			javax.xml.rpc.holders.StringHolder requestIdentifier) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[1]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
				"checkVatApprox"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { countryCode.value, vatNumber.value,
					traderName.value, traderCompanyType.value, traderStreet.value, traderPostcode.value,
					traderCity.value, requesterCountryCode, requesterVatNumber });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				java.util.Map _output;
				_output = _call.getOutputParams();
				try {
					countryCode.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "countryCode"));
				} catch (java.lang.Exception _exception) {
					countryCode.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"countryCode")), java.lang.String.class);
				}
				try {
					vatNumber.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "vatNumber"));
				} catch (java.lang.Exception _exception) {
					vatNumber.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"vatNumber")), java.lang.String.class);
				}
				try {
					traderName.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderName"));
				} catch (java.lang.Exception _exception) {
					traderName.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"traderName")), java.lang.String.class);
				}
				try {
					traderCompanyType.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderCompanyType"));
				} catch (java.lang.Exception _exception) {
					traderCompanyType.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"traderCompanyType")), java.lang.String.class);
				}
				try {
					traderStreet.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderStreet"));
				} catch (java.lang.Exception _exception) {
					traderStreet.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"traderStreet")), java.lang.String.class);
				}
				try {
					traderPostcode.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderPostcode"));
				} catch (java.lang.Exception _exception) {
					traderPostcode.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"traderPostcode")), java.lang.String.class);
				}
				try {
					traderCity.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderCity"));
				} catch (java.lang.Exception _exception) {
					traderCity.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"traderCity")), java.lang.String.class);
				}
				try {
					requestDate.value = (java.util.Date) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "requestDate"));
				} catch (java.lang.Exception _exception) {
					requestDate.value = (java.util.Date) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"requestDate")), java.util.Date.class);
				}
				try {
					valid.value = ((java.lang.Boolean) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "valid"))).booleanValue();
				} catch (java.lang.Exception _exception) {
					valid.value = ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"valid")), boolean.class)).booleanValue();
				}
				try {
					traderAddress.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderAddress"));
				} catch (java.lang.Exception _exception) {
					traderAddress.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"traderAddress")), java.lang.String.class);
				}
				try {
					traderNameMatch.value = (MatchCode) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderNameMatch"));
				} catch (java.lang.Exception _exception) {
					traderNameMatch.value = (MatchCode) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"traderNameMatch")), MatchCode.class);
				}
				try {
					traderCompanyTypeMatch.value = (MatchCode) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderCompanyTypeMatch"));
				} catch (java.lang.Exception _exception) {
					traderCompanyTypeMatch.value = (MatchCode) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"traderCompanyTypeMatch")), MatchCode.class);
				}
				try {
					traderStreetMatch.value = (MatchCode) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderStreetMatch"));
				} catch (java.lang.Exception _exception) {
					traderStreetMatch.value = (MatchCode) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"traderStreetMatch")), MatchCode.class);
				}
				try {
					traderPostcodeMatch.value = (MatchCode) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderPostcodeMatch"));
				} catch (java.lang.Exception _exception) {
					traderPostcodeMatch.value = (MatchCode) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"traderPostcodeMatch")), MatchCode.class);
				}
				try {
					traderCityMatch.value = (MatchCode) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderCityMatch"));
				} catch (java.lang.Exception _exception) {
					traderCityMatch.value = (MatchCode) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"traderCityMatch")), MatchCode.class);
				}
				try {
					requestIdentifier.value = (java.lang.String) _output.get(new javax.xml.namespace.QName(
							"urn:ec.europa.eu:taxud:vies:services:checkVat:types", "requestIdentifier"));
				} catch (java.lang.Exception _exception) {
					requestIdentifier.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output
							.get(new javax.xml.namespace.QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types",
									"requestIdentifier")), java.lang.String.class);
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

}
