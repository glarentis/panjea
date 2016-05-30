package it.eurotn.panjea.manutenzioni.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;
import it.eurotn.panjea.manutenzioni.domain.ManutenzioneSettings;
import it.eurotn.panjea.manutenzioni.domain.UbicazioneInstallazione;
import it.eurotn.panjea.manutenzioni.manager.articolimi.interfaces.ArticoliMIManager;
import it.eurotn.panjea.manutenzioni.manager.articolimi.interfaces.ParametriRicercaArticoliMI;
import it.eurotn.panjea.manutenzioni.manager.manutenzionisettings.interfaces.ManutenzioniSettingsManager;
import it.eurotn.panjea.manutenzioni.manager.ubicazioniinstallazione.interfaces.UbicazioniInstallazioneManager;
import it.eurotn.panjea.manutenzioni.service.interfaces.ManutenzioneAnagraficaService;

@Stateless(name = "Panjea.ManutenzioneAnagraficaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.ManutenzioneAnagraficaService")
public class ManutenzioneAnagraficaServiceBean implements ManutenzioneAnagraficaService {

    @EJB
    private ArticoliMIManager articoliMIManager;

    @EJB
    private UbicazioniInstallazioneManager ubicazioniInstallazioneManager;

    @EJB
    private ManutenzioniSettingsManager manutenzioniSettingsManager;

    @Override
    public void cancellaArticoloMI(Integer id) {
        articoliMIManager.cancella(id);
    }

    @Override
    public void cancellaUbicazioneInstallazione(Integer id) {
        ubicazioniInstallazioneManager.cancella(id);
    }

    @Override
    public List<ArticoloMI> caricaArticoliMI() {
        return articoliMIManager.caricaAll();
    }

    @Override
    public ArticoloMI caricaArticoloByIdConInstallazione(Integer id) {
        return articoliMIManager.caricaByIdConInstallazione(id);
    }

    @Override
    public ArticoloMI caricaArticoloMIById(Integer id) {
        return articoliMIManager.caricaByIdConInstallazione(id);
    }

    @Override
    public ManutenzioneSettings caricaManutenzioniSettings() {
        return manutenzioniSettingsManager.caricaManutenzioniSettings();
    }

    @Override
    public UbicazioneInstallazione caricaUbicazioneInstallazioneById(Integer id) {
        return ubicazioniInstallazioneManager.caricaById(id);
    }

    @Override
    public List<UbicazioneInstallazione> caricaUbicazioniInstallazione() {
        return ubicazioniInstallazioneManager.caricaAll();
    }

    @Override
    public List<ArticoloMI> ricercaArticoloMI(ParametriRicercaArticoliMI parametriRicerca) {
        return articoliMIManager.ricercaArticoloMI(parametriRicerca);
    }

    @Override
    public ArticoloMI salvaArticoloMI(ArticoloMI articoloMI) {
        return articoliMIManager.salva(articoloMI);
    }

    @Override
    public ManutenzioneSettings salvaManutenzioneSettings(ManutenzioneSettings manutenzioneSettings) {
        return manutenzioniSettingsManager.salvaManutenzioneSettings(manutenzioneSettings);
    }

    @Override
    public UbicazioneInstallazione salvaUbicazioneInstallazione(UbicazioneInstallazione ubicazioneInstallazione) {
        return ubicazioniInstallazioneManager.salva(ubicazioneInstallazione);
    }
}
