package it.eurotn.panjea.vending.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.RigaLetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.manager.arearifornimento.ParametriRicercaAreeRifornimento;
import it.eurotn.panjea.vending.manager.arearifornimento.interfaces.AreaRifornimentoAggiornaManager;
import it.eurotn.panjea.vending.manager.arearifornimento.interfaces.AreaRifornimentoManager;
import it.eurotn.panjea.vending.manager.evadts.importazioni.ImportazioneFileEvaDtsResult;
import it.eurotn.panjea.vending.manager.evadts.importazioni.interfaces.ImportazioneEvaDtsManager;
import it.eurotn.panjea.vending.manager.evadts.rilevazioni.ParametriRicercaRilevazioniEvaDts;
import it.eurotn.panjea.vending.manager.evadts.rilevazioni.interfaces.RilevazioniEvaDtsManager;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.RisultatiChiusuraLettureDTO;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces.LettureSelezionatriceManager;
import it.eurotn.panjea.vending.service.interfaces.VendingDocumentoService;

@Stateless(name = "Panjea.VendingDocumentoService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.VendingDocumentoService")
public class VendingDocumentoServiceBean implements VendingDocumentoService {

    @EJB
    private AreaRifornimentoManager areaRifornimentoManager;

    @EJB
    private AreaRifornimentoAggiornaManager areaRifornimentoAggiornaManager;

    @EJB
    private LettureSelezionatriceManager lettureSelezionatriceManager;

    @EJB
    private RilevazioniEvaDtsManager rilevazioniEvaDtsManager;

    @EJB
    private ImportazioneEvaDtsManager importazioneEvaDtsManager;

    @Override
    public AreaRifornimento aggiornaDatiInstallazione(AreaRifornimento areaRifornimento, Installazione installazione) {
        return areaRifornimentoAggiornaManager.aggiornaDatiInstallazione(areaRifornimento, installazione);
    }

    @Override
    public AreaRifornimento aggiornaDistributore(AreaRifornimento areaRifornimento, Integer idDistributore) {
        return areaRifornimentoAggiornaManager.aggiornaDistributore(areaRifornimento, idDistributore);
    }

    @Override
    public AreaRifornimento aggiornaEntita(AreaRifornimento areaRifornimento, Integer idEntita) {
        return areaRifornimentoAggiornaManager.aggiornaEntita(areaRifornimento, idEntita);
    }

    @Override
    public AreaRifornimento aggiornaSedeEntita(AreaRifornimento areaRifornimento, SedeEntita sedeEntita) {
        return areaRifornimentoAggiornaManager.aggiornaSedeEntita(areaRifornimento, sedeEntita);
    }

    @Override
    public void cancellaAreaRifornimento(Integer id) {
        areaRifornimentoManager.cancella(id);
    }

    @Override
    public void cancellaLetturaSelezionatrice(Integer id) {
        lettureSelezionatriceManager.cancella(id);
    }

    @Override
    public void cancellaRilevazioneEvaDts(Integer id) {
        rilevazioniEvaDtsManager.cancella(id);
    }

    @Override
    public List<AreaRifornimento> caricaAreaRifornimento() {
        return areaRifornimentoManager.caricaAll();
    }

    @Override
    public AreaRifornimento caricaAreaRifornimentoById(Integer id) {
        return areaRifornimentoManager.caricaById(id);
    }

    @Override
    public LetturaSelezionatrice caricaLetturaSelezionatriceById(Integer id) {
        return lettureSelezionatriceManager.caricaById(id);
    }

    @Override
    public RilevazioneEvaDts caricaRilevazioneEvaDtsById(Integer id) {
        return rilevazioniEvaDtsManager.caricaById(id);
    }

    @Override
    public List<RilevazioneEvaDts> caricaRilevazioniEvaDts() {
        return rilevazioniEvaDtsManager.caricaAll();
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    public RisultatiChiusuraLettureDTO chiudiLettureSelezionatrice(List<LetturaSelezionatrice> letture) {
        return lettureSelezionatriceManager.chiudiLettureSelezionatrice(letture);
    }

    @Override
    public ImportazioneFileEvaDtsResult importaEvaDts(String fileName, byte[] fileContent,
            EvaDtsImportFolder evaDtsImportFolder) {
        return importazioneEvaDtsManager.importaEvaDts(fileName, fileContent, evaDtsImportFolder);
    }

    @Override
    public ImportazioneFileEvaDtsResult importaEvaDts(String fileName, byte[] fileContent,
            EvaDtsImportFolder evaDtsImportFolder, boolean forzaImportazione) {
        return importazioneEvaDtsManager.importaEvaDts(fileName, fileContent, evaDtsImportFolder, forzaImportazione);
    }

    @Override
    public List<AreaRifornimento> ricercaAreeRifornimento(ParametriRicercaAreeRifornimento parametri) {
        return areaRifornimentoManager.ricercaAreeRifornimento(parametri);
    }

    @Override
    public List<LetturaSelezionatrice> ricercaLettureSelezionatrice(Integer idLettura) {
        return lettureSelezionatriceManager.ricercaLettureSelezionatrice(idLettura);
    }

    @Override
    public List<RigaLetturaSelezionatrice> ricercaRigheLetturaSelezionatrice(Integer progressivo) {
        return lettureSelezionatriceManager.ricercaRigheLetturaSelezionatrice(progressivo);
    }

    @Override
    public List<RilevazioneEvaDts> ricercaRilevazioniEvaDts(ParametriRicercaRilevazioniEvaDts parametri) {
        return rilevazioniEvaDtsManager.ricercaRilevazioniEvaDts(parametri);
    }

    @Override
    public AreaRifornimento salvaAreaRifornimento(AreaRifornimento areaRifornimento) {
        return areaRifornimentoManager.salva(areaRifornimento);
    }

    @Override
    public LetturaSelezionatrice salvaLetturaSelezionatrice(LetturaSelezionatrice letturaSelezionatrice) {
        return lettureSelezionatriceManager.salva(letturaSelezionatrice);
    }

    @Override
    public RilevazioneEvaDts salvaRilevazioneEvaDts(RilevazioneEvaDts rilevazioneEvaDts) {
        return rilevazioniEvaDtsManager.salva(rilevazioneEvaDts);
    }
}
