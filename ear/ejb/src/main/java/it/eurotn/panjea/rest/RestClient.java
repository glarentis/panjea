package it.eurotn.panjea.rest;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RestClient {

	protected static Logger log = LoggerFactory.getLogger(RestClient.class);

	protected String endpoint;

	protected HttpClient client = new HttpClient();

	/**
	 * Constructor.
	 *
	 * @param endpoint
	 *            Connection URL
	 */
	public RestClient(final String endpoint) {
		this.endpoint = endpoint;
		configureSSL();
	}

	/**
	 * Constructor.
	 *
	 * @param endpoint
	 *            Connection URL
	 * @param timeout
	 *            Timeout for the RESTful requests
	 */
	public RestClient(final String endpoint, final int timeout) {
		this(endpoint);
		configureTimeout(timeout);
	}

	/**
	 * Configures the SSL environment.
	 */
	protected void configureSSL() {
		if (!endpoint.toLowerCase().startsWith("https")) {
			return;
		}

		Protocol https = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);

		try {
			URL url = new URL(endpoint);

			int port = url.getPort() > 0 ? url.getPort() : 443;
			String host = url.getHost();

			client.getHostConfiguration().setHost(host, port, https);
		} catch (MalformedURLException e) {
			log.error("Malformed URL " + endpoint);
		}
	}

	/**
	 * Configures the timeout (in seconds).
	 */
	protected void configureTimeout(int timeout) {
		if (timeout <= 0) {
			return;
		}
		HttpConnectionParams params = client.getHttpConnectionManager().getParams();
		params.setConnectionTimeout(timeout * 1000);
		params.setSoTimeout(timeout * 1000);
	}

	protected PostMethod preparePostMethod(String url) {
		PostMethod request = null;

		if (url.toLowerCase().startsWith("https")) {
			try {
				URL urlNew = new URL(url);
				request = new PostMethod(urlNew.getPath());
			} catch (MalformedURLException e) {
				log.error("Malformed URL " + url);
				request = null;
			}
		} else {
			request = new PostMethod(url);
		}
		if (request != null) {
			request.setRequestHeader("User-Agent", "LogicalDOC");
		}
		return request;
	}
}