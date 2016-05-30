package it.eurotn.panjea.aton.exporter.service;

import it.eurotn.panjea.aton.exporter.ALIIVAExporter;
import it.eurotn.panjea.aton.exporter.ARTICOExporter;
import it.eurotn.panjea.aton.exporter.ATTRIBExporter;
import it.eurotn.panjea.aton.exporter.CHIAVIExporter;
import it.eurotn.panjea.aton.exporter.CLIENTExporter;
import it.eurotn.panjea.aton.exporter.CONDIZExporter;
import it.eurotn.panjea.aton.exporter.GIACENExporter;
import it.eurotn.panjea.aton.exporter.PARTITExporter;
import it.eurotn.panjea.aton.exporter.TABELLExporter;
import it.eurotn.panjea.aton.exporter.UNIVENExporter;
import it.eurotn.panjea.aton.exporter.UTENTIExporter;
import it.eurotn.panjea.aton.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.aton.exporter.service.interfaces.AtonExporterService;
import it.eurotn.panjea.exporter.exception.FileCreationException;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.AtonExporterService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.AtonExporterService")
@TransactionTimeout(value = 7200)
public class AtonExporterServiceBean implements AtonExporterService {

	private static Logger logger = Logger.getLogger(AtonExporterServiceBean.class);

	@Resource
	private SessionContext context;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@TransactionTimeout(value = 7200)
	public void esporta() {
		esportaCodiciIva();
		esportaArticoli();
		esportaAttributiArticoli();
		esportaClienti();
		esportaGiacenze();
		esportaTabelle();
		esportaRate();
		esportaUtenti();
		esportaCondiz();
		esportaUm();
	}

	@Override
	public void esportaArticoli() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(ARTICOExporter.BEAN_NAME);
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
		DataExporter exporter = (DataExporter) context.lookup(ATTRIBExporter.BEAN_NAME);
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
		DataExporter exporter = (DataExporter) context.lookup(CLIENTExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void esportaCodiciIva() {
		System.setProperty("line.separator", "\r\n");
		DataExporter exporter = (DataExporter) context.lookup(ALIIVAExporter.BEAN_NAME);
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
		DataExporter exporter = (DataExporter) context.lookup(CONDIZExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
		DataExporter exporterChiavi = (DataExporter) context.lookup(CHIAVIExporter.BEAN_NAME);
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
		DataExporter exporter = (DataExporter) context.lookup(GIACENExporter.BEAN_NAME);
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
		DataExporter exporter = (DataExporter) context.lookup(PARTITExporter.BEAN_NAME);
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
		DataExporter exporter = (DataExporter) context.lookup(TABELLExporter.BEAN_NAME);
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
		DataExporter exporter = (DataExporter) context.lookup(UNIVENExporter.BEAN_NAME);
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
		DataExporter exporter = (DataExporter) context.lookup(UTENTIExporter.BEAN_NAME);
		try {
			exporter.esporta();
		} catch (FileCreationException e) {
			logger.error("--> errore durante la creazione del file", e);
			throw new RuntimeException("errore durante la creazione del file", e);
		}
	}
}
