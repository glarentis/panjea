package it.eurotn.panjea.fatturepa.solutiondoc.webservice.manager;

import it.eurotn.panjea.fatturepa.domain.DatiConservazioneSostitutiva;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturaPASettingsManager;
import it.eurotn.panjea.fatturepa.solutiondoc.webservice.GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult;
import it.eurotn.panjea.fatturepa.solutiondoc.webservice.SolutionDOC_HubLocator;
import it.eurotn.panjea.fatturepa.solutiondoc.webservice.SolutionDOC_HubSoap;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.axis.message.MessageElement;
import org.apache.commons.lang3.StringUtils;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.SolutionDocFatturaPAManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.SolutionDocFatturaPAManager")
public class SolutionDocFatturaPAManagerBean implements SolutionDocFatturaPAManager {

	@EJB
	private FatturaPASettingsManager fatturaPASettingsManager;

	@Override
	public String checkClienteFatturaPA() {

		String result = "";

		SolutionDOC_HubLocator locator = new SolutionDOC_HubLocator();
		locator.setSolutionDOC_HubSoapEndpointAddress(getDatiConservazioneSostitutiva().getIndirizzoWeb());

		try {
			SolutionDOC_HubSoap soap = locator.getSolutionDOC_HubSoap();
			result = soap.checkClienteFatturaPA(getDatiConservazioneSostitutiva().getUtente(),
					getDatiConservazioneSostitutiva().getEncryptedPassword());
		} catch (Exception e) {
			result = e.getMessage();
		}

		return result;
	}

	@Override
	public String contattoHub() {

		String result = "";

		SolutionDOC_HubLocator locator = new SolutionDOC_HubLocator();
		locator.setSolutionDOC_HubSoapEndpointAddress(getDatiConservazioneSostitutiva().getIndirizzoWeb());

		try {
			SolutionDOC_HubSoap soap = locator.getSolutionDOC_HubSoap();
			result = soap.contattoHub();
		} catch (Exception e) {
			result = e.getMessage();
		}

		return result;
	}

	private DatiConservazioneSostitutiva getDatiConservazioneSostitutiva() {

		FatturaPASettings fatturaPASettings = fatturaPASettingsManager.caricaFatturaPASettings();

		return fatturaPASettings.getDatiConservazioneSostitutiva();
	}

	@Override
	public Map<StatoFatturaPA, String> getEsitiSdIFatturaPA(String identificativoSdI, String nomeXMLFatturaPA) {

		Map<StatoFatturaPA, String> esiti = new HashMap<StatoFatturaPA, String>();

		SolutionDOC_HubLocator locator = new SolutionDOC_HubLocator();
		locator.setSolutionDOC_HubSoapEndpointAddress(getDatiConservazioneSostitutiva().getIndirizzoWeb());

		try {
			SolutionDOC_HubSoap soap = locator.getSolutionDOC_HubSoap();
			GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult result = soap.getEsitiSdiFatturaPA(
					getDatiConservazioneSostitutiva().getUtente(), getDatiConservazioneSostitutiva()
					.getEncryptedPassword(), "", identificativoSdI, nomeXMLFatturaPA);

			for (MessageElement element : result.get_any()) {
				// recupero tutti gli identificativi degli esiti presenti
				if (element.getElementsByTagName("TipoMessaggio").item(0) != null) {
					MessageElement tipoEsitoElement = (MessageElement) element.getElementsByTagName("TipoMessaggio")
							.item(0);
					String tipoEsito = tipoEsitoElement.getValue();

					MessageElement idEsitoElement = (MessageElement) element.getElementsByTagName("IdSdiEsito").item(0);
					String idEsito = idEsitoElement.getValue();

					esiti.put(StatoFatturaPA.valueOf(tipoEsito), idEsito);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return esiti;
	}

	@Override
	public byte[] getFileEsitoFatturaPA(String idEsito) {

		byte[] result = null;

		SolutionDOC_HubLocator locator = new SolutionDOC_HubLocator();
		locator.setSolutionDOC_HubSoapEndpointAddress(getDatiConservazioneSostitutiva().getIndirizzoWeb());

		Object[] fileEsito;
		try {
			SolutionDOC_HubSoap soap = locator.getSolutionDOC_HubSoap();
			fileEsito = soap.getFileEsitoFatturaPA(getDatiConservazioneSostitutiva().getUtente(),
					getDatiConservazioneSostitutiva().getEncryptedPassword(), idEsito);

			if (StringUtils.startsWith((CharSequence) fileEsito[0], "Errore")) {
				throw new RuntimeException((String) fileEsito[0]);
			}

			result = (byte[]) fileEsito[1];
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	@Override
	public String getNomeFileZipFatturaPA() {

		String result = "";

		SolutionDOC_HubLocator locator = new SolutionDOC_HubLocator();
		locator.setSolutionDOC_HubSoapEndpointAddress(getDatiConservazioneSostitutiva().getIndirizzoWeb());

		try {
			SolutionDOC_HubSoap soap = locator.getSolutionDOC_HubSoap();
			result = soap.getNomeFileZipFatturaPA(getDatiConservazioneSostitutiva().getUtente(),
					getDatiConservazioneSostitutiva().getEncryptedPassword());
		} catch (Exception e) {
			result = e.getMessage();
		}

		return result;
	}

	@Override
	public Object[] importaFatturaPA(String nomeZip, String nomeXmlDefinizioneFiles) {

		Object[] result = new Object[] {};

		SolutionDOC_HubLocator locator = new SolutionDOC_HubLocator();
		locator.setSolutionDOC_HubSoapEndpointAddress(getDatiConservazioneSostitutiva().getIndirizzoWeb());

		try {
			SolutionDOC_HubSoap soap = locator.getSolutionDOC_HubSoap();
			result = soap.importaFatturaPA(getDatiConservazioneSostitutiva().getUtente(),
					getDatiConservazioneSostitutiva().getEncryptedPassword(), nomeZip, nomeXmlDefinizioneFiles);
		} catch (Exception e) {
			result = new String[] { e.getMessage() };
		}

		return result;
	}

	@Override
	public Object[] invioSdiFatturaPA(String nomeZip, String codicePaese, String identificativo, boolean rinomina,
			boolean firmaIntermediario) {
		Object[] result = new Object[] {};

		SolutionDOC_HubLocator locator = new SolutionDOC_HubLocator();
		locator.setSolutionDOC_HubSoapEndpointAddress(getDatiConservazioneSostitutiva().getIndirizzoWeb());

		try {
			SolutionDOC_HubSoap soap = locator.getSolutionDOC_HubSoap();
			result = soap.invioSdiFatturaPA(getDatiConservazioneSostitutiva().getUtente(),
					getDatiConservazioneSostitutiva().getEncryptedPassword(), nomeZip, codicePaese, identificativo,
					rinomina, firmaIntermediario);
		} catch (Exception e) {
			result = new String[] { e.getMessage() };
		}

		return result;
	}

	@Override
	public String uploadFileFatturaPA(String nomeFile, byte[] dataFile) {
		String result = "";

		SolutionDOC_HubLocator locator = new SolutionDOC_HubLocator();
		locator.setSolutionDOC_HubSoapEndpointAddress(getDatiConservazioneSostitutiva().getIndirizzoWeb());

		try {
			SolutionDOC_HubSoap soap = locator.getSolutionDOC_HubSoap();
			result = soap.uploadFileFatturaPA(getDatiConservazioneSostitutiva().getUtente(),
					getDatiConservazioneSostitutiva().getEncryptedPassword(), nomeFile, dataFile, 0);
		} catch (Exception e) {
			result = e.getMessage();
		}

		return result;
	}

}
