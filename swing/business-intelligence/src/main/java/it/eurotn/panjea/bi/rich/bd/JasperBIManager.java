package it.eurotn.panjea.bi.rich.bd;

import it.eurotn.rich.report.rest.ClientConfig;
import it.eurotn.rich.report.rest.ReportRestManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;

public class JasperBIManager {

	private static Logger logger = Logger.getLogger(JasperBIManager.class);
	private ReportRestManager restManager;
	private static final String PATH_TEMPLATE_FILE = "/Standard/Stili/Template";
	private static final String PATH_TEMPLATE_INPUT_CONTROLS = "/Standard/InputControl/Analisi";

	/**
	 *
	 * @param config
	 *            configurazione per il server di analisi
	 */
	public JasperBIManager(final ClientConfig config) {
		restManager = new ReportRestManager(config);
		restManager.login();
	}

	/**
	 *
	 * @param analisiBi
	 *            analisi da pubblicare
	 * @param mainJrxml
	 *            file principale dell'analisi<
	 */
	@SuppressWarnings("unchecked")
	public void putAnalisi(String nome, String categoria, String descrizione, String mainJrxml,
			List<String> inputControls) {
		if (nome.isEmpty()) {
			return;
		}
		nome = StringUtils.deleteWhitespace(nome);
		categoria = StringUtils.deleteWhitespace(categoria);
		if (restManager.isResourcePresent("/test99/" + nome)) {
			restManager.deleteResource("/test99/" + nome);
		}
		ResourceDescriptor rd = new ResourceDescriptor();
		rd.setWsType(ResourceDescriptor.TYPE_REPORTUNIT);
		rd.setName(nome);
		rd.setLabel(nome);
		rd.setDescription(descrizione);
		rd.setUriString("/test99/" + nome);
		rd.setIsNew(true);
		rd.setHasData(false);
		rd.setResourceProperty(ResourceDescriptor.PROP_RU_ALWAYS_PROPMT_CONTROLS, false);
		rd.setResourceProperty(ResourceDescriptor.PROP_RU_CONTROLS_LAYOUT,
				ResourceDescriptor.RU_CONTROLS_LAYOUT_IN_PAGE);

		ResourceDescriptor tmpDataSourceDescriptor = new ResourceDescriptor();
		tmpDataSourceDescriptor.setName("jndiDataSource");
		tmpDataSourceDescriptor.setWsType(ResourceDescriptor.TYPE_DATASOURCE);
		tmpDataSourceDescriptor.setReferenceUri("/Generale/DataSource/panjeajndi");
		tmpDataSourceDescriptor.setIsReference(true);
		rd.getChildren().add(tmpDataSourceDescriptor);

		ResourceDescriptor jrxmlDescriptor = new ResourceDescriptor();
		jrxmlDescriptor.setWsType(ResourceDescriptor.TYPE_JRXML);
		jrxmlDescriptor.setName("main_jrxml");
		jrxmlDescriptor.setLabel("main jrxml");
		jrxmlDescriptor.setDescription("main jrxml");
		jrxmlDescriptor.setIsNew(true);
		jrxmlDescriptor.setHasData(true);
		jrxmlDescriptor.setMainReport(true);
		jrxmlDescriptor.setUriString(rd.getUriString() + "_files/main_jrxml");
		rd.getChildren().add(jrxmlDescriptor);

		// Input control che deve SEMPRE esistere
		ResourceDescriptor aziendaInputControlRd = new ResourceDescriptor();
		aziendaInputControlRd.setWsType(ResourceDescriptor.TYPE_INPUT_CONTROL);
		aziendaInputControlRd.setIsReference(true);
		aziendaInputControlRd.setReferenceUri("/Standard/InputControl/Analisi/AZIENDA");
		aziendaInputControlRd.setIsNew(true);
		rd.getChildren().add(aziendaInputControlRd);

		// InputControl selezionati
		for (String inputControl : inputControls) {
			ResourceDescriptor inputControlRd = new ResourceDescriptor();
			inputControlRd.setWsType(ResourceDescriptor.TYPE_INPUT_CONTROL);
			inputControlRd.setIsReference(true);
			inputControlRd.setReferenceUri("/Standard/InputControl/Analisi/" + inputControl);
			inputControlRd.setIsNew(true);
			rd.getChildren().add(inputControlRd);
		}

		File mainJrxmlfile = null;
		try {
			mainJrxmlfile = File.createTempFile("analisiPanjea", ".jrxml");
			IOUtils.write(mainJrxml, new FileOutputStream(mainJrxmlfile));
			restManager.putResource(rd, mainJrxmlfile, jrxmlDescriptor.getUriString());
		} catch (FileNotFoundException e) {
			logger.error("-->errore file non trovato ...impossibile ...creato alla riga prima !!!!", e);
		} catch (IOException ioe) {
			logger.error("-->errore nello scrivere il file contenente il jrxml", ioe);
			throw new RuntimeException(ioe);
		} catch (Exception ex) {
			logger.error("-->errore nel creare la report unit dall'analisi ", ex);
			throw new RuntimeException(ex);
		} finally {
			if (mainJrxmlfile != null) {
				mainJrxmlfile.delete();
			}
		}
	}

	/**
	 *
	 * @param pathTemplateFiles
	 *            path del file di template dell'analisi
	 * @return stringa con il contenuto del file di template.
	 */
	public String retrieveContentTemplateFile(String pathTemplateFiles) {
		return restManager.retrieveContentResourceAsString(PATH_TEMPLATE_FILE + "/" + pathTemplateFiles + ".jrxml");
	}

	/**
	 *
	 * @return lista di file di template da usare come base per i report di Business
	 */
	public List<ResourceDescriptor> retrieveInputControls() {
		List<ResourceDescriptor> inputControls = restManager.retrieveResources(PATH_TEMPLATE_INPUT_CONTROLS,
				"inputControl");
		List<ResourceDescriptor> result = new ArrayList<ResourceDescriptor>();
		for (ResourceDescriptor resourceDescriptor : inputControls) {
			if (resourceDescriptor.getName().contains("_")) {
				result.add(resourceDescriptor);
			}
		}
		return result;
	}

	/**
	 *
	 * @return lista di file di template da usare come base per i report di Business
	 */
	public Set<String> retrieveTemplateFiles() {
		List<ResourceDescriptor> result = restManager.retrieveResources(PATH_TEMPLATE_FILE, "jrxml");
		Set<String> templates = new TreeSet<String>();
		for (ResourceDescriptor resourceDescriptor : result) {
			templates.add(resourceDescriptor.getName().substring(0, resourceDescriptor.getName().length() - 6));
		}
		return templates;
	}
}
