package it.eurotn.panjea.dms.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import com.logicaldoc.webservice.document.WSDocument;

import it.eurotn.panjea.dms.domain.DmsSettings;
import it.eurotn.panjea.dms.exception.DMSLoginException;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.dms.manager.interfaces.DMSAllegatoManager;
import it.eurotn.panjea.dms.manager.interfaces.DMSSettingsManager;
import it.eurotn.panjea.dms.service.interfaces.DMSService;
import it.eurotn.panjea.dms.service.interfaces.RubricaExporterService;

@Stateless(name = "Panjea.LogicalDocDMSService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.LogicalDocDMSService")
public class LogicalDocDMSServiceBean implements DMSService, RubricaExporterService {

    @EJB
    private DMSSettingsManager dmsSettingManager;

    @EJB(beanName = "DMSAllegatoManagerBean")
    private DMSAllegatoManager allegatoManager;

    @Override
    public void addAllegato(String folderPath, WSDocument documento) throws DMSLoginException {
        allegatoManager.addAllegato(folderPath, documento);
    }

    @Override
    public void addAllegato(String uuid, WSDocument documento, AllegatoDMS allegatoDMS) throws DMSLoginException {
        allegatoManager.addAllegato(uuid, documento, allegatoDMS);
    }

    @Override
    public DmsSettings caricaDmsSettings() {
        return dmsSettingManager.caricaDmsSettings();
    }

    @Override
    public void deleteAllegato(long idAllegato) throws DMSLoginException {
        allegatoManager.deleteAllegato(idAllegato);
    }

    @Override
    public List<WSDocument> getAllegati(AllegatoDMS allegatoDMS) throws DMSLoginException {
        return allegatoManager.getAllegati(allegatoDMS);
    }

    @Override
    public byte[] getAllegatoTile(long idAllegato) throws DMSLoginException {
        return allegatoManager.getAllegatoTile(idAllegato);
    }

    @Override
    public DmsSettings salvaDmsSettings(DmsSettings dmsSettings) {
        return dmsSettingManager.salvaDmsSettings(dmsSettings);
    }

}
