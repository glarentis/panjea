package it.eurotn.panjea.vending.rest.service;

import java.io.OutputStream;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ArticoliSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.BollaSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.BollaTestataSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.CaricatoriSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.CauzioniSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ChiamateCaricatoreSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ChiaviDepositoSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ChiaviTipiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ClientiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.DistributoriInConsegnaSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.DistributoriSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.EccezioniProdottiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.InstallazioniRitiriDistributoriSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ListiniNuovaInstallazioneSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ListiniSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ManutenzioniPianificateSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ModelliManutenzioniSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ModelliProdottiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ModelliSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.NuoveInstallazioniDettaglioSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.PocketVendingParameterSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ProdottiChiamateCaricatoreSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.RaccoltaPuntiPremiClientiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.RaccoltaPuntiPremiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.RaccoltaPuntiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.SospesiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.TabellaGenericaSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.TipiChiamataSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.TipiManutenzioneSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.TipiModelliProdottiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.exception.ImportazioneException;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.PalmariImportaManager;
import it.eurotn.panjea.vending.rest.service.interfaces.VendingPalmariRestService;

@Stateless(name = "Panjea.VendingPalmariRestService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.VendingPalmariRestService")
public class VendingPalmariRestServiceBean implements VendingPalmariRestService {

    @EJB
    private ArticoliSyncBuilder articoliSyncBuilder;

    @EJB
    private BollaSyncBuilder bollaSyncBuilder;

    @EJB
    private BollaTestataSyncBuilder bollaTestataSyncBuilder;

    @EJB
    private CaricatoriSyncBuilder caricatoriSyncBuilder;

    @EJB
    private CauzioniSyncBuilder cauzioniSyncBuilder;

    @EJB
    private ChiamateCaricatoreSyncBuilder chiamateCaricatoreSyncBuilder;

    @EJB
    private ChiaviDepositoSyncBuilder chiaviDepositoSyncBuilder;

    @EJB
    private ChiaviTipiSyncBuilder chiaviTipiSyncBuilder;

    @EJB
    private ClientiSyncBuilder clientiSyncBuilder;

    @EJB
    private DistributoriInConsegnaSyncBuilder distributoriInConsegnaSyncBuilder;

    @EJB
    private DistributoriSyncBuilder distributoriSyncBuilder;

    @EJB
    private EccezioniProdottiSyncBuilder eccezioniProdottiSyncBuilder;

    @EJB
    private InstallazioniRitiriDistributoriSyncBuilder installazioniRitiriDistributoriSyncBuilder;

    @EJB
    private ListiniNuovaInstallazioneSyncBuilder listiniNuovaInstallazioneSyncBuilder;

    @EJB
    private ListiniSyncBuilder listiniSyncBuilder;

    @EJB
    private ManutenzioniPianificateSyncBuilder manutenzioniPianificateSyncBuilder;

    @EJB
    private ModelliManutenzioniSyncBuilder modelliManutenzioniSyncBuilder;

    @EJB
    private ModelliProdottiSyncBuilder modelliProdottiSyncBuilder;

    @EJB
    private ModelliSyncBuilder modelliSyncBuilder;

    @EJB
    private NuoveInstallazioniDettaglioSyncBuilder nuoveInstallazioniDettaglioSyncBuilder;

    @EJB
    private PalmariImportaManager palmariImportaManager;

    @EJB
    private PocketVendingParameterSyncBuilder pocketVendingParameterSyncBuilder;

    @EJB
    private ProdottiChiamateCaricatoreSyncBuilder prodottiChiamateCaricatoreSyncBuilder;

    @EJB
    private RaccoltaPuntiPremiClientiSyncBuilder raccoltaPuntiPremiClientiSyncBuilder;

    @EJB
    private RaccoltaPuntiPremiSyncBuilder raccoltaPuntiPremiSyncBuilder;

    @EJB
    private RaccoltaPuntiSyncBuilder raccoltaPuntiSyncBuilder;

    @EJB
    private SospesiSyncBuilder sospesiSyncBuilder;

    @EJB
    private TabellaGenericaSyncBuilder tabellaGenericaSyncBuilder;

    @EJB
    private TipiChiamataSyncBuilder tipiChiamataSyncBuilder;

    @EJB
    private TipiManutenzioneSyncBuilder tipiManutenzioneSyncBuilder;

    @EJB
    private TipiModelliProdottiSyncBuilder tipiModelliProdottiSyncBuilder;

    @Override
    public void esporta(OutputStream output, String codiceOperatore) {
        final StringBuilder sb = new StringBuilder(8000);
        articoliSyncBuilder.esporta(output, codiceOperatore);
        // sb.append(pocketVendingParameterSyncBuilder.esporta(codiceOperatore));
        // sb.append(caricatoriSyncBuilder.esporta(codiceOperatore));
        // sb.append(clientiSyncBuilder.esporta(codiceOperatore));
        // sb.append(articoliSyncBuilder.esporta(codiceOperatore));
        // sb.append(modelliSyncBuilder.esporta(output, codiceOperatore));
        // sb.append(modelliProdottiSyncBuilder.esporta(codiceOperatore));
        // sb.append(tipiModelliProdottiSyncBuilder.esporta(codiceOperatore));
        // sb.append(distributoriSyncBuilder.esporta(codiceOperatore));
        // sb.append(eccezioniProdottiSyncBuilder.esporta(codiceOperatore));
        // sb.append(listiniSyncBuilder.esporta(codiceOperatore));
        // sb.append(bollaTestataSyncBuilder.esporta(codiceOperatore));
        // sb.append(bollaSyncBuilder.esporta(codiceOperatore));
        // sb.append(cauzioniSyncBuilder.esporta(codiceOperatore));
        // sb.append(chiamateCaricatoreSyncBuilder.esporta(codiceOperatore));
        // sb.append(chiaviDepositoSyncBuilder.esporta(codiceOperatore));
        // sb.append(chiaviTipiSyncBuilder.esporta(codiceOperatore));
        // sb.append(distributoriInConsegnaSyncBuilder.esporta(codiceOperatore));
        // sb.append(installazioniRitiriDistributoriSyncBuilder.esporta(codiceOperatore));
        // sb.append(listiniNuovaInstallazioneSyncBuilder.esporta(codiceOperatore));
        // sb.append(manutenzioniPianificateSyncBuilder.esporta(codiceOperatore));
        // sb.append(modelliManutenzioniSyncBuilder.esporta(codiceOperatore));
        // sb.append(nuoveInstallazioniDettaglioSyncBuilder.esporta(codiceOperatore));
        // sb.append(prodottiChiamateCaricatoreSyncBuilder.esporta(codiceOperatore));
        // sb.append(raccoltaPuntiPremiClientiSyncBuilder.esporta(codiceOperatore));
        // sb.append(raccoltaPuntiPremiSyncBuilder.esporta(codiceOperatore));
        // sb.append(raccoltaPuntiSyncBuilder.esporta(codiceOperatore));
        // sb.append(sospesiSyncBuilder.esporta(codiceOperatore));
        // sb.append(tabellaGenericaSyncBuilder.esporta(codiceOperatore));
        // sb.append(tipiChiamataSyncBuilder.esporta(codiceOperatore));
        // sb.append(tipiManutenzioneSyncBuilder.esporta(codiceOperatore));
        // return sb.toString();
    }

    @Override
    public String importa(String codiceOperatore, byte[] contenutoFile) throws ImportazioneException {
        return palmariImportaManager.importa(codiceOperatore, contenutoFile);
    }

}
