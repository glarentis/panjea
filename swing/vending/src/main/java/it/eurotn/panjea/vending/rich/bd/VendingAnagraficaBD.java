package it.eurotn.panjea.vending.rich.bd;

import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
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
import it.eurotn.panjea.vending.manager.distributore.ParametriRicercaDistributore;
import it.eurotn.panjea.vending.manager.sistemielettronici.ParametriRicercaSistemiElettronici;
import it.eurotn.panjea.vending.service.interfaces.VendingAnagraficaService;

public class VendingAnagraficaBD extends AbstractBaseBD implements IVendingAnagraficaBD {

    private static final Logger LOGGER = Logger.getLogger(VendingAnagraficaBD.class);

    public static final String BEAN_ID = "vendingAnagraficaBD";

    private VendingAnagraficaService vendingAnagraficaService;

    @Override
    public void cancellaAnagraficaAsl(Integer id) {
        LOGGER.debug("--> Enter cancellaAnagraficaAsl");
        start("cancellaAnagraficaAsl");
        try {
            vendingAnagraficaService.cancellaAnagraficaAsl(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAnagraficaAsl");
        }
        LOGGER.debug("--> Exit cancellaAnagraficaAsl ");
    }

    @Override
    public void cancellaAsl(Integer id) {
        LOGGER.debug("--> Enter cancellaAsl");
        start("cancellaAsl");
        try {
            vendingAnagraficaService.cancellaAsl(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAsl");
        }
        LOGGER.debug("--> Exit cancellaAsl ");
    }

    @Override
    public void cancellaCassa(Integer id) {
        LOGGER.debug("--> Enter cancellaCassa");
        start("cancellaCassa");
        try {
            vendingAnagraficaService.cancellaCassa(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaCassa");
        }
        LOGGER.debug("--> Exit cancellaCassa ");
    }

    @Override
    public void cancellaDistributore(Integer idDistributore) {
        LOGGER.debug("--> Enter cancellaDistributore");
        start("cancellaDistributore");
        try {
            vendingAnagraficaService.cancellaDistributore(idDistributore);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaDistributore");
        }
        LOGGER.debug("--> Exit cancellaDistributore ");

    }

    @Override
    public void cancellaGettone(Integer id) {
        LOGGER.debug("--> Enter cancellaGettone");
        start("cancellaGettone");
        try {
            vendingAnagraficaService.cancellaGettone(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaGettone");
        }
        LOGGER.debug("--> Exit cancellaGettone ");
    }

    @Override
    public void cancellaModello(Integer idModello) {
        LOGGER.debug("--> Enter cancellaModello");
        start("cancellaModello");
        try {
            vendingAnagraficaService.cancellaModello(idModello);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaModello");
        }
        LOGGER.debug("--> Exit cancellaModello ");
    }

    @Override
    public void cancellaMovimentoCassa(Integer id) {
        LOGGER.debug("--> Enter cancellaMovimentoCassa");
        start("cancellaMovimentoCassa");
        try {
            vendingAnagraficaService.cancellaMovimentoCassa(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaMovimentoCassa");
        }
        LOGGER.debug("--> Exit cancellaMovimentoCassa ");
    }

    @Override
    public void cancellaSistemaElettronico(Integer id) {
        LOGGER.debug("--> Enter cancellaSistemaElettronico");
        start("cancellaSistemaElettronico");
        try {
            vendingAnagraficaService.cancellaSistemaElettronico(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaSistemaElettronico");
        }
        LOGGER.debug("--> Exit cancellaSistemaElettronico ");
    }

    @Override
    public void cancellaTipoComunicazione(Integer idTipoComunicazione) {
        LOGGER.debug("--> Enter cancellaTipoComunicazione");
        start("cancellaTipoComunicazione");
        try {
            vendingAnagraficaService.cancellaTipoComunicazione(idTipoComunicazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTipoComunicazione");
        }
        LOGGER.debug("--> Exit cancellaTipoComunicazione ");
    }

    @Override
    public void cancellaTipoModello(Integer idTipoModello) {
        LOGGER.debug("--> Enter cancellaTipoModello");
        start("cancellaTipoModello");
        try {
            vendingAnagraficaService.cancellaTipoModello(idTipoModello);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTipoModello");
        }
        LOGGER.debug("--> Exit cancellaTipoModello ");
    }

    @Override
    public AnagraficaAsl caricaAnagraficaAslById(Integer id) {
        LOGGER.debug("--> Enter caricaAnagraficaAslById");
        start("caricaAnagraficaAslById");

        AnagraficaAsl object = null;
        try {
            object = vendingAnagraficaService.caricaAnagraficaAslById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAnagraficaAslById");
        }
        LOGGER.debug("--> Exit caricaAnagraficaAslById ");
        return object;
    }

    @Override
    public List<AnagraficaAsl> caricaAnagraficheAsl() {
        LOGGER.debug("--> Enter caricaAnagraficheAsl");
        start("caricaAnagraficheAsl");

        List<AnagraficaAsl> result = null;
        try {
            result = vendingAnagraficaService.caricaAnagraficheAsl();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAnagraficheAsl");
        }
        LOGGER.debug("--> Exit caricaAnagraficheAsl ");
        return result;
    }

    @Override
    public List<Asl> caricaAsl() {
        LOGGER.debug("--> Enter caricaAsl");
        start("caricaAsl");

        List<Asl> result = null;
        try {
            result = vendingAnagraficaService.caricaAsl();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAsl");
        }
        LOGGER.debug("--> Exit caricaAsl ");
        return result;
    }

    @Override
    public Asl caricaAslById(Integer id) {
        LOGGER.debug("--> Enter caricaAslById");
        start("caricaAslById");

        Asl object = null;
        try {
            object = vendingAnagraficaService.caricaAslById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAslById");
        }
        LOGGER.debug("--> Exit caricaAslById ");
        return object;
    }

    @Override
    public Cassa caricaCassaById(Integer id) {
        LOGGER.debug("--> Enter caricaCassaById");
        start("caricaCassaById");

        Cassa object = null;
        try {
            object = vendingAnagraficaService.caricaCassaById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCassaById");
        }
        LOGGER.debug("--> Exit caricaCassaById ");
        return object;
    }

    @Override
    public List<Cassa> caricaCasse() {
        LOGGER.debug("--> Enter caricaCasse");
        start("caricaCasse");

        List<Cassa> result = null;
        try {
            result = vendingAnagraficaService.caricaCasse();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCasse");
        }
        LOGGER.debug("--> Exit caricaCasse ");
        return result;
    }

    @Override
    public Distributore caricaDistributore(Integer idDistributore) {
        LOGGER.debug("--> Enter caricaDistributore");
        start("caricaDistributore");
        Distributore distributore = null;
        try {
            distributore = vendingAnagraficaService.caricaDistributore(idDistributore);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDistributore");
        }
        LOGGER.debug("--> Exit caricaDistributore ");
        return distributore;
    }

    @Override
    public Gettone caricaGettoneById(Integer id) {
        LOGGER.debug("--> Enter caricaGettoneById");
        start("caricaGettoneById");

        Gettone object = null;
        try {
            object = vendingAnagraficaService.caricaGettoneById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaGettoneById");
        }
        LOGGER.debug("--> Exit caricaGettoneById ");
        return object;
    }

    @Override
    public List<Gettone> caricaGettoni() {
        LOGGER.debug("--> Enter caricaGettoni");
        start("caricaGettoni");

        List<Gettone> result = null;
        try {
            result = vendingAnagraficaService.caricaGettoni();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaGettoni");
        }
        LOGGER.debug("--> Exit caricaGettoni ");
        return result;
    }

    @Override
    public List<Modello> caricaModelli() {
        LOGGER.debug("--> Enter caricaModelli");
        start("caricaModelli");

        List<Modello> result = null;
        try {
            result = vendingAnagraficaService.caricaModelli();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaModelli");
        }
        LOGGER.debug("--> Exit caricaModelli ");
        return result;
    }

    @Override
    public List<MovimentoCassa> caricaMovimentiCassa(boolean includiChiusure) {
        LOGGER.debug("--> Enter caricaMovimentiCassa");
        start("caricaMovimentiCassa");

        List<MovimentoCassa> result = null;
        try {
            result = vendingAnagraficaService.caricaMovimentiCassa(includiChiusure);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaMovimentiCassa");
        }
        LOGGER.debug("--> Exit caricaMovimentiCassa ");
        return result;
    }

    @Override
    public MovimentoCassa caricaMovimentoCassaById(Integer id) {
        LOGGER.debug("--> Enter caricaMovimentoCassaById");
        start("caricaMovimentoCassaById");

        MovimentoCassa object = null;
        try {
            object = vendingAnagraficaService.caricaMovimentoCassaById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaMovimentoCassaById");
        }
        LOGGER.debug("--> Exit caricaMovimentoCassaById ");
        return object;
    }

    @Override
    public List<ProdottoCollegato> caricaProdottiCollegatiByDistributore(Integer idDistributore) {
        LOGGER.debug("--> Enter caricaProdottiCollegatiByDistributore");
        start("caricaProdottiCollegatiByDistributore");

        List<ProdottoCollegato> prodotti = null;
        try {
            prodotti = vendingAnagraficaService.caricaProdottiCollegatiByDistributore(idDistributore);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaProdottiCollegatiByDistributore");
        }
        LOGGER.debug("--> Exit caricaProdottiCollegatiByDistributore ");
        return prodotti;
    }

    @Override
    public List<ProdottoCollegato> caricaProdottiCollegatiByInstallazione(Integer idInstallazione) {
        LOGGER.debug("--> Enter caricaProdottiCollegatiByInstallazione");
        start("caricaProdottiCollegatiByInstallazione");

        List<ProdottoCollegato> prodotti = null;
        try {
            prodotti = vendingAnagraficaService.caricaProdottiCollegatiByInstallazione(idInstallazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaProdottiCollegatiByInstallazione");
        }
        LOGGER.debug("--> Exit caricaProdottiCollegatiByInstallazione ");
        return prodotti;
    }

    @Override
    public List<ProdottoCollegato> caricaProdottiCollegatiByModello(Integer idModello) {
        LOGGER.debug("--> Enter caricaProdottiCollegatiByModello");
        start("caricaProdottiCollegatiByModello");

        List<ProdottoCollegato> prodotti = null;
        try {
            prodotti = vendingAnagraficaService.caricaProdottiCollegatiByModello(idModello);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaProdottiCollegatiByModello");
        }
        LOGGER.debug("--> Exit caricaProdottiCollegatiByModello ");
        return prodotti;
    }

    @Override
    public List<ProdottoCollegato> caricaProdottiCollegatiByTipoModello(Integer idTipoModello) {
        LOGGER.debug("--> Enter caricaProdottiCollegatiByTipoModello");
        start("caricaProdottiCollegatiByTipoModello");

        List<ProdottoCollegato> prodotti = null;
        try {
            prodotti = vendingAnagraficaService.caricaProdottiCollegatiByTipoModello(idTipoModello);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaProdottiCollegatiByTipoModello");
        }
        LOGGER.debug("--> Exit caricaProdottiCollegatiByTipoModello ");
        return prodotti;
    }

    @Override
    public SistemaElettronico caricaSistemaElettronicoById(Integer id) {
        LOGGER.debug("--> Enter caricaSistemaElettronicoById");
        start("caricaSistemaElettronicoById");

        SistemaElettronico object = null;
        try {
            object = vendingAnagraficaService.caricaSistemaElettronicoById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSistemaElettronicoById");
        }
        LOGGER.debug("--> Exit caricaSistemaElettronicoById ");
        return object;
    }

    @Override
    public List<SistemaElettronico> caricaSistemiElettronici() {
        LOGGER.debug("--> Enter caricaSistemiElettronici");
        start("caricaSistemiElettronici");

        List<SistemaElettronico> result = null;
        try {
            result = vendingAnagraficaService.caricaSistemiElettronici();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSistemiElettronici");
        }
        LOGGER.debug("--> Exit caricaSistemiElettronici ");
        return result;
    }

    @Override
    public List<TipoComunicazione> caricaTipiComunicazione() {
        LOGGER.debug("--> Enter caricaTipiComunicazione");
        start("caricaTipiComunicazione");

        List<TipoComunicazione> result = null;
        try {
            result = vendingAnagraficaService.caricaTipiComunicazione();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiComunicazione");
        }
        LOGGER.debug("--> Exit caricaTipiComunicazione ");
        return result;
    }

    @Override
    public List<TipoModello> caricaTipiModello() {
        LOGGER.debug("--> Enter caricaTipiModello");
        start("caricaTipiModello");
        List<TipoModello> result = null;
        try {
            result = vendingAnagraficaService.caricaTipiModello();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiModello");
        }
        LOGGER.debug("--> Exit caricaTipiModello ");
        return result;
    }

    @Override
    public VendingSettings caricaVendingSettings() {
        LOGGER.debug("--> Enter caricaVendingSettings");
        start("caricaVendingSettings");

        VendingSettings vendingSettings = null;
        try {
            vendingSettings = vendingAnagraficaService.caricaVendingSettings();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaVendingSettings");
        }
        LOGGER.debug("--> Exit caricaVendingSettings ");
        return vendingSettings;
    }

    @Override
    public void chiudiCassa(Integer[] casseId) {
        LOGGER.debug("--> Enter chiudiCassa");
        start("chiudiCassa");
        try {
            vendingAnagraficaService.chiudiCassa(casseId);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("chiudiCassa");
        }
        LOGGER.debug("--> Exit chiudiCassa ");

    }

    @Override
    public List<ArticoloRicerca> ricercaArticoliSearchObject(ParametriRicercaArticolo parametriRicercaArticolo) {
        LOGGER.debug("--> Enter ricercaArticoliSearchObject");
        start("ricercaArticoliSearchObject");

        List<ArticoloRicerca> articoli = null;
        try {
            articoli = vendingAnagraficaService.ricercaArticoliInstallazione(parametriRicercaArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaArticoliSearchObject");
        }
        LOGGER.debug("--> Exit ricercaArticoliSearchObject ");
        return articoli;
    }

    @Override
    public List<Cassa> ricercaCasse() {
        LOGGER.debug("--> Enter ricercaCasse");
        start("ricercaCasse");

        List<Cassa> casse = null;
        try {
            casse = vendingAnagraficaService.ricercaCasse();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaCasse");
        }
        LOGGER.debug("--> Exit ricercaCasse ");
        return casse;
    }

    @Override
    public List<Distributore> ricercaDistributori(ParametriRicercaDistributore parametri) {
        LOGGER.debug("--> Enter ricercaDistributoriByModello");
        start("ricercaDistributoriByModello");

        List<Distributore> distributori = null;
        try {
            distributori = vendingAnagraficaService.ricercaDistributori(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaDistributoriByModello");
        }
        LOGGER.debug("--> Exit ricercaDistributoriByModello ");
        return distributori;
    }

    @Override
    public List<SistemaElettronico> ricercaSistemiElettronici(ParametriRicercaSistemiElettronici parametri) {
        LOGGER.debug("--> Enter ricercaSistemiElettronici");
        start("ricercaSistemiElettronici");

        List<SistemaElettronico> result = null;
        try {
            result = vendingAnagraficaService.ricercaSistemiElettronici(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaSistemiElettronici");
        }
        LOGGER.debug("--> Exit ricercaSistemiElettronici ");
        return result;
    }

    @Override
    public AnagraficaAsl salvaAnagraficaAsl(AnagraficaAsl anagraficaAsl) {
        LOGGER.debug("--> Enter salvaAnagraficaAsl");
        start("salvaAnagraficaAsl");

        AnagraficaAsl object = null;
        try {
            object = vendingAnagraficaService.salvaAnagraficaAsl(anagraficaAsl);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAnagraficaAsl");
        }
        LOGGER.debug("--> Exit salvaAnagraficaAsl ");
        return object;
    }

    @Override
    public Asl salvaAsl(Asl asl) {
        LOGGER.debug("--> Enter salvaAsl");
        start("salvaAsl");

        Asl object = null;
        try {
            object = vendingAnagraficaService.salvaAsl(asl);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAsl");
        }
        LOGGER.debug("--> Exit salvaAsl ");
        return object;
    }

    @Override
    public Cassa salvaCassa(Cassa cassa) {
        LOGGER.debug("--> Enter salvaCassa");
        start("salvaCassa");

        Cassa object = null;
        try {
            object = vendingAnagraficaService.salvaCassa(cassa);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCassa");
        }
        LOGGER.debug("--> Exit salvaCassa ");
        return object;
    }

    @Override
    public Distributore salvaDistributore(Distributore distributore) {
        LOGGER.debug("--> Enter salvaDistributore");
        start("salvaDistributore");

        Distributore distributoreSave = null;
        try {
            distributoreSave = vendingAnagraficaService.salvaDistributore(distributore);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaDistributore");
        }
        LOGGER.debug("--> Exit salvaDistributore ");
        return distributoreSave;
    }

    @Override
    public Gettone salvaGettone(Gettone gettone) {
        LOGGER.debug("--> Enter salvaGettone");
        start("salvaGettone");

        Gettone object = null;
        try {
            object = vendingAnagraficaService.salvaGettone(gettone);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaGettone");
        }
        LOGGER.debug("--> Exit salvaGettone ");
        return object;
    }

    @Override
    public Modello salvaModello(Modello modello) {
        LOGGER.debug("--> Enter salvaModello");
        start("salvaModello");

        Modello modelloSave = null;
        try {
            modelloSave = vendingAnagraficaService.salvaModello(modello);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaModello");
        }
        LOGGER.debug("--> Exit salvaModello ");
        return modelloSave;
    }

    @Override
    public MovimentoCassa salvaMovimentoCassa(MovimentoCassa movimentoCassa) {
        LOGGER.debug("--> Enter salvaMovimentoCassa");
        start("salvaMovimentoCassa");

        MovimentoCassa object = null;
        try {
            object = vendingAnagraficaService.salvaMovimentoCassa(movimentoCassa);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaMovimentoCassa");
        }
        LOGGER.debug("--> Exit salvaMovimentoCassa ");
        return object;
    }

    @Override
    public SistemaElettronico salvaSistemaElettronico(SistemaElettronico sistemaElettronico) {
        LOGGER.debug("--> Enter salvaSistemaElettronico");
        start("salvaSistemaElettronico");

        SistemaElettronico object = null;
        try {
            object = vendingAnagraficaService.salvaSistemaElettronico(sistemaElettronico);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaSistemaElettronico");
        }
        LOGGER.debug("--> Exit salvaSistemaElettronico ");
        return object;
    }

    @Override
    public TipoComunicazione salvaTipoComunicazione(TipoComunicazione tipoComunicazione) {
        LOGGER.debug("--> Enter salvaTipoComunicazione");
        start("salvaTipoComunicazione");

        TipoComunicazione result = null;
        try {
            result = vendingAnagraficaService.salvaTipoComunicazione(tipoComunicazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTipoComunicazione");
        }
        LOGGER.debug("--> Exit salvaTipoComunicazione ");
        return result;
    }

    @Override
    public TipoModello salvaTipoModello(TipoModello tipoModello) {
        LOGGER.debug("--> Enter salvaTipoModello");
        start("salvaTipoModello");

        TipoModello tipoModelloSave = null;
        try {
            tipoModelloSave = vendingAnagraficaService.salvaTipoModello(tipoModello);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTipoModello");
        }
        LOGGER.debug("--> Exit salvaTipoModello ");
        return tipoModelloSave;
    }

    @Override
    public VendingSettings salvaVendingSettings(VendingSettings vendingSettings) {
        LOGGER.debug("--> Enter salvaVendingSettings");
        start("salvaVendingSettings");

        VendingSettings vendingSettingsSave = null;
        try {
            vendingSettingsSave = vendingAnagraficaService.salvaVendingSettings(vendingSettings);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaVendingSettings");
        }
        LOGGER.debug("--> Exit salvaVendingSettings ");
        return vendingSettingsSave;
    }

    /**
     * @param vendingAnagraficaService
     *            the vendingAnagraficaService to set
     */
    public void setVendingAnagraficaService(VendingAnagraficaService vendingAnagraficaService) {
        this.vendingAnagraficaService = vendingAnagraficaService;
    }
}
