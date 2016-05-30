package it.eurotn.panjea.onroad.exporter.service;

import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.onroad.exporter.OnRoadALIIVAExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadARTICOExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadASSORTExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadATTRIBExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadCHIAVIExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadCLIENTExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadCONCESExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadCONDIZExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadGIACENExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadPAGAMEExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadPARTITExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadTABELLExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadUNIVENExporter;
import it.eurotn.panjea.onroad.exporter.OnRoadUTENTIExporter;
import it.eurotn.panjea.onroad.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.onroad.exporter.service.interfaces.OnRoadExporterService;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.OnRoadExporterService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.OnRoadExporterService")
@TransactionTimeout(value = 7200)
public class OnRoadExporterServiceBean implements OnRoadExporterService {

	private static Logger logger = Logger.getLogger(OnRoadExporterServiceBean.class);

	@Resource
	private SessionContext context;

	@Override
	public void esporta() {
		esportaCodiciIva(); // ALIIVA
		esportaArticoli(); // ARTICO
		esportaAttributiArticoli(); // ATTRIB
		esportaAssortimentoArticoli(); // ASSORT assortimento articoli
		esportaClienti(); // CLIENT
		esportaClientiCessionari(); // CONCES legami cliente-cessionario
		esportaCondiz(); // CONDIZ
		esportaCodiciPagamento(); // PAGAME
		esportaRate(); // PARTIT
		esportaTabelle(); // TABELL

		// esportaUtenti(); //UTENTI
		// esportaGiacenze(); //non serve
		// esportaUm();
	}

	@Override
	public void esportaArticoli() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadARTICOExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

	@Override
	public void esportaAssortimentoArticoli() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadASSORTExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

	@Override
	public void esportaAttributiArticoli() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadATTRIBExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

	@Override
	public void esportaClienti() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadCLIENTExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

	@Override
	public void esportaClientiCessionari() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadCONCESExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

	@Override
	public void esportaCodiciIva() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadALIIVAExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

	@Override
	public void esportaCodiciPagamento() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadPAGAMEExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

	@Override
	public void esportaCondiz() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadCONDIZExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}

		DataExporter exporterChiavi = (DataExporter) context.lookup(OnRoadCHIAVIExporter.BEAN_NAME);
		try {
			exporterChiavi.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

	@Override
	public void esportaGiacenze() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadGIACENExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

	@Override
	public void esportaRate() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadPARTITExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

	@Override
	public void esportaTabelle() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadTABELLExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

	@Override
	public void esportaUm() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadUNIVENExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

	@Override
	public void esportaUtenti() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(OnRoadUTENTIExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}

}
