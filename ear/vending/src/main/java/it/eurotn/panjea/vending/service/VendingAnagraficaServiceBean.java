package it.eurotn.panjea.vending.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.vending.domain.AnagraficaAsl;
import it.eurotn.panjea.vending.domain.Asl;
import it.eurotn.panjea.vending.domain.Cassa;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.Gettone;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.panjea.vending.domain.SistemaElettronico;
import it.eurotn.panjea.vending.domain.TipoComunicazione;
import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.panjea.vending.domain.VendingSettings;
import it.eurotn.panjea.vending.manager.anagraficheasl.interfaces.AnagraficheAslManager;
import it.eurotn.panjea.vending.manager.asl.interfaces.AslManager;
import it.eurotn.panjea.vending.manager.casse.interfaces.CasseManager;
import it.eurotn.panjea.vending.manager.distributore.ParametriRicercaDistributore;
import it.eurotn.panjea.vending.manager.distributore.interfaces.DistributoreManager;
import it.eurotn.panjea.vending.manager.gettoni.interfaces.GettoniManager;
import it.eurotn.panjea.vending.manager.modello.interfaces.ModelloManager;
import it.eurotn.panjea.vending.manager.movimenticassa.interfaces.MovimentiCassaManager;
import it.eurotn.panjea.vending.manager.prodotticollegati.interfaces.ProdottiCollegatiVendingManager;
import it.eurotn.panjea.vending.manager.sistemielettronici.ParametriRicercaSistemiElettronici;
import it.eurotn.panjea.vending.manager.sistemielettronici.interfaces.SistemiElettroniciManager;
import it.eurotn.panjea.vending.manager.tipicomunicazione.interfaces.TipiComunicazioneManager;
import it.eurotn.panjea.vending.manager.tipimodello.interfaces.TipiModelloManager;
import it.eurotn.panjea.vending.manager.vendingsettings.interfaces.VendingSettingsManager;
import it.eurotn.panjea.vending.service.interfaces.VendingAnagraficaService;

@Stateless(name = "Panjea.VendingAnagraficaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.VendingAnagraficaService")
public class VendingAnagraficaServiceBean implements VendingAnagraficaService {

    @EJB
    private TipiComunicazioneManager tipiComunicazioneManager;

    @EJB
    private TipiModelloManager tipiModelloManager;

    @EJB
    private ModelloManager modelloManager;

    @EJB
    private DistributoreManager distributoreManager;

    @EJB
    private ProdottiCollegatiVendingManager prodottiCollegatiVendingManager;

    @EJB
    private SistemiElettroniciManager sistemiElettroniciManager;

    @EJB
    private GettoniManager gettoniManager;

    @EJB
    private CasseManager casseManager;

    @EJB
    private MovimentiCassaManager movimentiCassaManager;

    @EJB
    private VendingSettingsManager vendingSettingsManager;

    @EJB
    private AnagraficheAslManager anagraficheAslManager;

    @EJB
    private AslManager aslManager;

    @Override
    public void cancellaAnagraficaAsl(Integer id) {
        anagraficheAslManager.cancella(id);
    }

    @Override
    public void cancellaAsl(Integer id) {
        aslManager.cancella(id);
    }

    @Override
    public void cancellaCassa(Integer id) {
        casseManager.cancella(id);
    }

    @Override
    public void cancellaDistributore(Integer idDistributore) {
        distributoreManager.cancella(idDistributore);
    }

    @Override
    public void cancellaGettone(Integer id) {
        gettoniManager.cancella(id);
    }

    @Override
    public void cancellaModello(Integer idModello) {
        modelloManager.cancella(idModello);
    }

    @Override
    public void cancellaMovimentoCassa(Integer id) {
        movimentiCassaManager.cancella(id);
    }

    @Override
    public void cancellaSistemaElettronico(Integer id) {
        sistemiElettroniciManager.cancella(id);
    }

    @Override
    public void cancellaTipoComunicazione(Integer idTipoComunicazione) {
        tipiComunicazioneManager.cancella(idTipoComunicazione);
    }

    @Override
    public void cancellaTipoModello(Integer idTipoModello) {
        tipiModelloManager.cancella(idTipoModello);
    }

    @Override
    public AnagraficaAsl caricaAnagraficaAslById(Integer id) {
        return anagraficheAslManager.caricaById(id);
    }

    @Override
    public List<AnagraficaAsl> caricaAnagraficheAsl() {
        return anagraficheAslManager.caricaAll();
    }

    @Override
    public List<Asl> caricaAsl() {
        return aslManager.caricaAll();
    }

    @Override
    public Asl caricaAslById(Integer id) {
        return aslManager.caricaById(id);
    }

    @Override
    public Cassa caricaCassaById(Integer id) {
        return casseManager.caricaById(id);
    }

    @Override
    public List<Cassa> caricaCasse() {
        return casseManager.caricaAll();
    }

    @Override
    public Distributore caricaDistributore(Integer idDistributore) {
        return distributoreManager.caricaById(idDistributore);
    }

    @Override
    public Gettone caricaGettoneById(Integer id) {
        return gettoniManager.caricaById(id);
    }

    @Override
    public List<Gettone> caricaGettoni() {
        return gettoniManager.caricaAll();
    }

    @Override
    public List<Modello> caricaModelli() {
        return modelloManager.caricaAll();
    }

    @Override
    public List<MovimentoCassa> caricaMovimentiCassa(boolean includiChiusure) {
        return movimentiCassaManager.caricaAll(includiChiusure);
    }

    @Override
    public MovimentoCassa caricaMovimentoCassaById(Integer id) {
        return movimentiCassaManager.caricaById(id);
    }

    @Override
    public List<ProdottoCollegato> caricaProdottiCollegatiByDistributore(Integer idDistributore) {
        return prodottiCollegatiVendingManager.caricaProdottiCollegatiByDistributore(idDistributore);
    }

    @Override
    public List<ProdottoCollegato> caricaProdottiCollegatiByInstallazione(Integer idInstallazione) {
        return prodottiCollegatiVendingManager.caricaProdottiCollegatiByInstallazione(idInstallazione);
    }

    @Override
    public List<ProdottoCollegato> caricaProdottiCollegatiByModello(Integer idModello) {
        return prodottiCollegatiVendingManager.caricaProdottiCollegatiByModello(idModello);
    }

    @Override
    public List<ProdottoCollegato> caricaProdottiCollegatiByTipoModello(Integer idTipoModello) {
        return prodottiCollegatiVendingManager.caricaProdottiCollegatiByTipoModello(idTipoModello);
    }

    @Override
    public SistemaElettronico caricaSistemaElettronicoById(Integer id) {
        return sistemiElettroniciManager.caricaById(id);
    }

    @Override
    public List<SistemaElettronico> caricaSistemiElettronici() {
        return sistemiElettroniciManager.caricaAll();
    }

    @Override
    public List<TipoComunicazione> caricaTipiComunicazione() {
        return tipiComunicazioneManager.caricaAll();
    }

    @Override
    public List<TipoModello> caricaTipiModello() {
        return tipiModelloManager.caricaAll();
    }

    @Override
    public VendingSettings caricaVendingSettings() {
        return vendingSettingsManager.caricaVendingSettings();
    }

    @Override
    public void chiudiCassa(Integer[] casseId) {

        for (int i = 0; i < casseId.length; i++) {
            casseManager.chiudiCassa(casseId[i]);
        }
    }

    @Override
    public List<ArticoloRicerca> ricercaArticoliInstallazione(ParametriRicercaArticolo parametri) {
        return prodottiCollegatiVendingManager.ricercaArticoliInstallazione(parametri);
    }

    @Override
    public List<Cassa> ricercaCasse() {
        return casseManager.ricercaCasse();
    }

    @Override
    public List<Distributore> ricercaDistributori(ParametriRicercaDistributore parametri) {
        return distributoreManager.ricercaDistributori(parametri);
    }

    @Override
    public List<SistemaElettronico> ricercaSistemiElettronici(ParametriRicercaSistemiElettronici parametri) {
        return sistemiElettroniciManager.ricercaSistemiElettronici(parametri);
    }

    @Override
    public AnagraficaAsl salvaAnagraficaAsl(AnagraficaAsl anagraficaAsl) {
        return anagraficheAslManager.salva(anagraficaAsl);
    }

    @Override
    public Asl salvaAsl(Asl asl) {
        return aslManager.salva(asl);
    }

    @Override
    public Cassa salvaCassa(Cassa cassa) {
        return casseManager.salva(cassa);
    }

    @Override
    public Distributore salvaDistributore(Distributore distributore) {
        return distributoreManager.salva(distributore);
    }

    @Override
    public Gettone salvaGettone(Gettone gettone) {
        return gettoniManager.salva(gettone);
    }

    @Override
    public Modello salvaModello(Modello modello) {
        return modelloManager.salva(modello);
    }

    @Override
    public MovimentoCassa salvaMovimentoCassa(MovimentoCassa movimentoCassa) {
        return movimentiCassaManager.salva(movimentoCassa);
    }

    @Override
    public SistemaElettronico salvaSistemaElettronico(SistemaElettronico sistemaElettronico) {
        return sistemiElettroniciManager.salva(sistemaElettronico);
    }

    @Override
    public TipoComunicazione salvaTipoComunicazione(TipoComunicazione tipoComunicazione) {
        return tipiComunicazioneManager.salva(tipoComunicazione);
    }

    @Override
    public TipoModello salvaTipoModello(TipoModello tipoModello) {
        return tipiModelloManager.salva(tipoModello);
    }

    @Override
    public VendingSettings salvaVendingSettings(VendingSettings vendingSettings) {
        return vendingSettingsManager.salvaVendingSettings(vendingSettings);
    }
}
