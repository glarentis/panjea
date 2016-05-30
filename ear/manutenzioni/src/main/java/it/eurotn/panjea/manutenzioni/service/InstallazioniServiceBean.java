package it.eurotn.panjea.manutenzioni.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione;
import it.eurotn.panjea.manutenzioni.domain.DepositoInstallazione;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.TipoAreaInstallazione;
import it.eurotn.panjea.manutenzioni.exception.TaiSenzaTamException;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.ParametriRicercaAreeInstallazione;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.interfaces.AreeInstallazioniMagazzinoManager;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.interfaces.AreeInstallazioniManager;
import it.eurotn.panjea.manutenzioni.manager.causaliinstallazione.interfaces.CausaliInstallazioneManager;
import it.eurotn.panjea.manutenzioni.manager.installazioni.ParametriRicercaInstallazioni;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.DepositoInstallazioneManager;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.InstallazioniManager;
import it.eurotn.panjea.manutenzioni.manager.righeinstallazione.interfaces.RigheInstallazioneManager;
import it.eurotn.panjea.manutenzioni.manager.tipiareeinstallazione.interfaces.TipiAreeInstallazioneManager;
import it.eurotn.panjea.manutenzioni.service.interfaces.InstallazioniService;

@Stateless(name = "Panjea.InstallazioniService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.InstallazioniService")
public class InstallazioniServiceBean implements InstallazioniService {

    @EJB
    private InstallazioniManager installazioniManager;

    @EJB
    private TipiAreeInstallazioneManager tipiAreeInstallazioneManager;

    @EJB
    private AreeInstallazioniManager areeInstallazioniManager;

    @EJB
    private RigheInstallazioneManager righeInstallazioneManager;

    @EJB
    private CausaliInstallazioneManager causaliInstallazioneManager;

    @EJB
    private DepositoInstallazioneManager depositoInstallazioneManager;

    @EJB
    private AreeInstallazioniMagazzinoManager areeInstallazioniMagazzinoManager;

    @Override
    public void cancellaAreaInstallazione(Integer id) {
        areeInstallazioniManager.cancella(id);
    }

    @Override
    public void cancellaCausaleInstallazione(Integer id) {
        causaliInstallazioneManager.cancella(id);
    }

    @Override
    public void cancellaInstallazione(Integer idInstallazione) {
        installazioniManager.cancella(idInstallazione);
    }

    @Override
    public void cancellaRigaInstallazione(Integer id) {
        righeInstallazioneManager.cancella(id);
    }

    @Override
    public void cancellaTipoAreaInstallazione(Integer id) {
        tipiAreeInstallazioneManager.cancella(id);
    }

    @Override
    public AreaInstallazione caricaAreaInstallazioneById(Integer id) {
        return areeInstallazioniManager.caricaById(id);
    }

    @Override
    public List<AreaInstallazione> caricaAreeInstallazioni() {
        return areeInstallazioniManager.caricaAll();
    }

    @Override
    public CausaleInstallazione caricaCausaleInstallazioneById(Integer id) {
        return causaliInstallazioneManager.caricaById(id);
    }

    @Override
    public List<CausaleInstallazione> caricaCausaliInstallazione() {
        return causaliInstallazioneManager.caricaAll();
    }

    @Override
    public DepositoInstallazione caricaDeposito(SedeEntita sedeEntita) {
        return depositoInstallazioneManager.caricaDeposito(sedeEntita);
    }

    @Override
    public Installazione caricaInstallazione(Integer id) {
        return installazioniManager.caricaById(id);
    }

    @Override
    public Installazione caricaInstallazioneById(Integer id) {
        return installazioniManager.caricaById(id);
    }

    @Override
    public List<Installazione> caricaInstallazioni() {
        return installazioniManager.caricaAll();
    }

    @Override
    public RigaInstallazione caricaRigaInstallazioneById(Integer id) {
        return righeInstallazioneManager.caricaById(id);
    }

    @Override
    public List<RigaInstallazione> caricaRigheInstallazione() {
        return righeInstallazioneManager.caricaAll();
    }

    @Override
    public List<RigaInstallazione> caricaRigheInstallazioneByAreaInstallazione(Integer idAreaInstallazione) {
        return righeInstallazioneManager.caricaRigheInstallazioneByAreaInstallazione(idAreaInstallazione);
    }

    @Override
    public List<RigaInstallazione> caricaRigheInstallazioneByArticolo(Integer idArticolo) {
        return righeInstallazioneManager.caricaRigheInstallazioneByArticolo(idArticolo);
    }

    @Override
    public List<RigaInstallazione> caricaRigheInstallazioneByInstallazione(Integer idInstallazione) {
        return righeInstallazioneManager.caricaRigheInstallazioneByInstallazione(idInstallazione);
    }

    @Override
    public List<TipoAreaInstallazione> caricaTipiAreeInstallazione() {
        return tipiAreeInstallazioneManager.caricaAll();
    }

    @Override
    public TipoAreaInstallazione caricaTipoAreaInstallazioneById(Integer id) {
        return tipiAreeInstallazioneManager.caricaByTipoDocumento(id);
    }

    @Override
    public TipoAreaInstallazione caricaTipoAreaInstallazioneByTipoDocumento(int idTipoDocumento) {
        return tipiAreeInstallazioneManager.caricaByTipoDocumento(idTipoDocumento);
    }

    @Override
    public int creaAreaMagazzino(int idAreaInstallazione) throws TaiSenzaTamException {
        return areeInstallazioniMagazzinoManager.creaAreaMagazzino(idAreaInstallazione);
    }

    @Override
    public List<AreaInstallazione> ricercaAreeInstallazioni(ParametriRicercaAreeInstallazione parametri) {
        return areeInstallazioniManager.ricercaAreeInstallazioni(parametri);
    }

    @Override
    public List<Installazione> ricercaByEntita(Integer idEntita) {
        return installazioniManager.ricercaByEntita(idEntita);
    }

    @Override
    public List<Installazione> ricercaByParametri(ParametriRicercaInstallazioni parametri) {
        return installazioniManager.ricercaByParametri(parametri);
    }

    @Override
    public List<Installazione> ricercaBySede(Integer idSedeEntita) {
        return installazioniManager.ricercaBySede(idSedeEntita);
    }

    @Override
    public AreaInstallazione salvaAreaInstallazione(AreaInstallazione areaInstallazione) {
        return areeInstallazioniManager.salva(areaInstallazione);
    }

    @Override
    public CausaleInstallazione salvaCausaleInstallazione(CausaleInstallazione causaleInstallazione) {
        return causaliInstallazioneManager.salva(causaleInstallazione);
    }

    @Override
    public Installazione salvaInstallazione(Installazione installazione) {
        return installazioniManager.salva(installazione);
    }

    @Override
    public RigaInstallazione salvaRigaInstallazione(RigaInstallazione rigaInstallazione) {
        return righeInstallazioneManager.salva(rigaInstallazione);
    }

    @Override
    public RigaInstallazione salvaRigaInstallazioneInizializza(RigaInstallazione rigaInstallazione) {
        return righeInstallazioneManager.salvaInizializza(rigaInstallazione);
    }

    @Override
    public TipoAreaInstallazione salvaTipoAreaInstallazione(TipoAreaInstallazione tipoAreaInstallazione) {
        return tipiAreeInstallazioneManager.salva(tipoAreaInstallazione);
    }
}
