package it.eurotn.panjea.manutenzioni.rich.bd;

import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;
import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione;
import it.eurotn.panjea.manutenzioni.domain.DepositoInstallazione;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.ManutenzioneSettings;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.domain.UbicazioneInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.TipoAreaInstallazione;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.ParametriRicercaAreeInstallazione;
import it.eurotn.panjea.manutenzioni.manager.articolimi.interfaces.ParametriRicercaArticoliMI;
import it.eurotn.panjea.manutenzioni.manager.installazioni.ParametriRicercaInstallazioni;
import it.eurotn.panjea.manutenzioni.manager.operatori.ParametriRicercaOperatori;
import it.eurotn.panjea.manutenzioni.service.interfaces.InstallazioniService;
import it.eurotn.panjea.manutenzioni.service.interfaces.ManutenzioneAnagraficaService;
import it.eurotn.panjea.manutenzioni.service.interfaces.OperatoriService;
import it.eurotn.panjea.manutenzioni.service.interfaces.ProdottiCollegatiService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class ManutenzioniBD extends AbstractBaseBD implements IManutenzioniBD {

    private static final Logger LOGGER = Logger.getLogger(ManutenzioniBD.class);

    public static final String BEAN_ID = "manutenzioniBD";

    private OperatoriService operatoriService;
    private ProdottiCollegatiService prodottiCollegatiService;

    private ManutenzioneAnagraficaService manutenzioneAnagraficaService;

    private InstallazioniService installazioniService;

    @Override
    public void cancellaAreaInstallazione(Integer id) {
        LOGGER.debug("--> Enter cancellaAreaInstallazione");
        start("cancellaAreaInstallazione");
        try {
            installazioniService.cancellaAreaInstallazione(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAreaInstallazione");
        }
        LOGGER.debug("--> Exit cancellaAreaInstallazione ");
    }

    @Override
    public void cancellaArticoloMI(Integer id) {
        LOGGER.debug("--> Enter cancellaArticoloMI");
        start("cancellaArticoloMI");
        try {
            manutenzioneAnagraficaService.cancellaArticoloMI(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaArticoloMI");
        }
        LOGGER.debug("--> Exit cancellaArticoloMI ");
    }

    @Override
    public void cancellaCausaleInstallazione(Integer id) {
        LOGGER.debug("--> Enter cancellaCausaleInstallazione");
        start("cancellaCausaleInstallazione");
        try {
            installazioniService.cancellaCausaleInstallazione(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaCausaleInstallazione");
        }
        LOGGER.debug("--> Exit cancellaCausaleInstallazione ");
    }

    @Override
    public void cancellaInstallazione(Integer id) {
        LOGGER.debug("--> Enter cancellaInstallazione");
        start("cancellaInstallazione");
        try {
            installazioniService.cancellaInstallazione(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaInstallazione");
        }
        LOGGER.debug("--> Exit cancellaInstallazione ");
    }

    @Override
    public void cancellaOperatore(Integer id) {
        LOGGER.debug("--> Enter cancellaOperatore");
        start("cancellaOperatore");
        try {
            operatoriService.cancellaOperatore(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaOperatore");
        }
        LOGGER.debug("--> Exit cancellaOperatore ");
    }

    @Override
    public void cancellaProdottoCollegato(Integer id) {
        LOGGER.debug("--> Enter cancellaProdottoCollegato");
        start("cancellaProdottoCollegato");
        try {
            prodottiCollegatiService.cancellaProdottoCollegato(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaProdottoCollegato");
        }
        LOGGER.debug("--> Exit cancellaProdottoCollegato ");
    }

    @Override
    public void cancellaRigaInstallazione(Integer id) {
        LOGGER.debug("--> Enter cancellaRigaInstallazione");
        start("cancellaRigaInstallazione");
        try {
            installazioniService.cancellaRigaInstallazione(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigaInstallazione");
        }
        LOGGER.debug("--> Exit cancellaRigaInstallazione ");
    }

    @Override
    public void cancellaTipoAreaInstallazione(Integer id) {
        LOGGER.debug("--> Enter cancellaTipoAreaInstallazione");
        start("cancellaTipoAreaInstallazione");
        try {
            installazioniService.cancellaTipoAreaInstallazione(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTipoAreaInstallazione");
        }
        LOGGER.debug("--> Exit cancellaTipoAreaInstallazione ");
    }

    @Override
    public void cancellaUbicazioneInstallazione(Integer id) {
        LOGGER.debug("--> Enter cancellaUbicazioneInstallazione");
        start("cancellaUbicazioneInstallazione");
        try {
            manutenzioneAnagraficaService.cancellaUbicazioneInstallazione(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaUbicazioneInstallazione");
        }
        LOGGER.debug("--> Exit cancellaUbicazioneInstallazione ");
    }

    @Override
    public AreaInstallazione caricaAreaInstallazioneById(Integer id) {
        LOGGER.debug("--> Enter caricaAreaInstallazioneById");
        start("caricaAreaInstallazioneById");

        AreaInstallazione object = null;
        try {
            object = installazioniService.caricaAreaInstallazioneById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaInstallazioneById");
        }
        LOGGER.debug("--> Exit caricaAreaInstallazioneById ");
        return object;
    }

    @Override
    public List<AreaInstallazione> caricaAreeInstallazioni() {
        LOGGER.debug("--> Enter caricaAreeInstallazioni");
        start("caricaAreeInstallazioni");

        List<AreaInstallazione> result = null;
        try {
            result = installazioniService.caricaAreeInstallazioni();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreeInstallazioni");
        }
        LOGGER.debug("--> Exit caricaAreeInstallazioni ");
        return result;
    }

    @Override
    public List<ArticoloMI> caricaArticoliMI() {
        LOGGER.debug("--> Enter caricaArticoliMI");
        start("caricaArticoliMI");

        List<ArticoloMI> result = null;
        try {
            result = manutenzioneAnagraficaService.caricaArticoliMI();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaArticoliMI");
        }
        LOGGER.debug("--> Exit caricaArticoliMI ");
        return result;
    }

    @Override
    public ArticoloMI caricaArticoloByIdConInstallazione(Integer id) {
        LOGGER.debug("--> Enter caricaByIdConInstallazione");
        start("caricaByIdConInstallazione");
        ArticoloMI result = null;
        try {
            result = manutenzioneAnagraficaService.caricaArticoloByIdConInstallazione(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaByIdConInstallazione");
        }
        LOGGER.debug("--> Exit caricaByIdConInstallazione ");
        return result;
    }

    @Override
    public ArticoloMI caricaArticoloMIById(Integer id) {
        LOGGER.debug("--> Enter caricaArticoloMIById");
        start("caricaArticoloMIById");

        ArticoloMI object = null;
        try {
            object = manutenzioneAnagraficaService.caricaArticoloMIById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaArticoloMIById");
        }
        LOGGER.debug("--> Exit caricaArticoloMIById ");
        return object;
    }

    @Override
    public CausaleInstallazione caricaCausaleInstallazioneById(Integer id) {
        LOGGER.debug("--> Enter caricaCausaleInstallazioneById");
        start("caricaCausaleInstallazioneById");

        CausaleInstallazione object = null;
        try {
            object = installazioniService.caricaCausaleInstallazioneById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCausaleInstallazioneById");
        }
        LOGGER.debug("--> Exit caricaCausaleInstallazioneById ");
        return object;
    }

    @Override
    public List<CausaleInstallazione> caricaCausaliInstallazione() {
        LOGGER.debug("--> Enter caricaCausaliInstallazione");
        start("caricaCausaliInstallazione");

        List<CausaleInstallazione> result = null;
        try {
            result = installazioniService.caricaCausaliInstallazione();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCausaliInstallazione");
        }
        LOGGER.debug("--> Exit caricaCausaliInstallazione ");
        return result;
    }

    @Override
    public DepositoInstallazione caricaDeposito(SedeEntita sedeEntita) {
        LOGGER.debug("--> Enter caricaDeposito");
        start("caricaDeposito");

        DepositoInstallazione deposito = null;
        try {
            deposito = installazioniService.caricaDeposito(sedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDeposito");
        }
        LOGGER.debug("--> Exit caricaDeposito ");
        return deposito;
    }

    @Override
    public Installazione caricaInstallazioneById(Integer id) {
        LOGGER.debug("--> Enter caricaInstallazioneById");
        start("caricaInstallazioneById");

        Installazione object = null;
        try {
            object = installazioniService.caricaInstallazioneById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaInstallazioneById");
        }
        LOGGER.debug("--> Exit caricaInstallazioneById ");
        return object;
    }

    @Override
    public List<Installazione> caricaInstallazioni() {
        LOGGER.debug("--> Enter caricaInstallazioni");
        start("caricaInstallazioni");

        List<Installazione> result = null;
        try {
            result = installazioniService.caricaInstallazioni();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaInstallazioni");
        }
        LOGGER.debug("--> Exit caricaInstallazioni ");
        return result;
    }

    @Override
    public ManutenzioneSettings caricaManutenzioniSettings() {
        LOGGER.debug("--> Enter caricaManutenzioniSettings");
        start("caricaManutenzioniSettings");

        ManutenzioneSettings manutenzioneSettings = null;
        try {
            manutenzioneSettings = manutenzioneAnagraficaService.caricaManutenzioniSettings();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaManutenzioniSettings");
        }
        LOGGER.debug("--> Exit caricaManutenzioniSettings ");
        return manutenzioneSettings;
    }

    @Override
    public Operatore caricaOperatoreById(Integer id) {
        LOGGER.debug("--> Enter caricaOperatoreById");
        start("caricaOperatoreById");

        Operatore object = null;
        try {
            object = operatoriService.caricaOperatoreById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaOperatoreById");
        }
        LOGGER.debug("--> Exit caricaOperatoreById ");
        return object;
    }

    @Override
    public List<Operatore> caricaOperatori() {
        LOGGER.debug("--> Enter caricaOperatori");
        start("caricaOperatori");

        List<Operatore> result = null;
        try {
            result = operatoriService.caricaOperatori();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaOperatori");
        }
        LOGGER.debug("--> Exit caricaOperatori ");
        return result;
    }

    @Override
    public List<ProdottoCollegato> caricaProdottiCollegati() {
        LOGGER.debug("--> Enter caricaProdottiCollegati");
        start("caricaProdottiCollegati");

        List<ProdottoCollegato> result = null;
        try {
            result = prodottiCollegatiService.caricaProdottiCollegati();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaProdottiCollegati");
        }
        LOGGER.debug("--> Exit caricaProdottiCollegati ");
        return result;
    }

    @Override
    public ProdottoCollegato caricaProdottoCollegatoById(Integer id) {
        LOGGER.debug("--> Enter caricaProdottoCollegatoById");
        start("caricaProdottoCollegatoById");

        ProdottoCollegato object = null;
        try {
            object = prodottiCollegatiService.caricaProdottoCollegatoById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaProdottoCollegatoById");
        }
        LOGGER.debug("--> Exit caricaProdottoCollegatoById ");
        return object;
    }

    @Override
    public RigaInstallazione caricaRigaInstallazioneById(Integer id) {
        LOGGER.debug("--> Enter caricaRigaInstallazioneById");
        start("caricaRigaInstallazioneById");

        RigaInstallazione object = null;
        try {
            object = installazioniService.caricaRigaInstallazioneById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigaInstallazioneById");
        }
        LOGGER.debug("--> Exit caricaRigaInstallazioneById ");
        return object;
    }

    @Override
    public List<RigaInstallazione> caricaRigheInstallazioneByAreaInstallazione(Integer idAreaInstallazione) {
        LOGGER.debug("--> Enter caricaRigheInstallazioneByAreaInstallazione");
        start("caricaRigheInstallazione");

        List<RigaInstallazione> result = null;
        try {
            result = installazioniService.caricaRigheInstallazioneByAreaInstallazione(idAreaInstallazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheInstallazioneByAreaInstallazione");
        }
        LOGGER.debug("--> Exit caricaRigheInstallazione ");
        return result;
    }

    @Override
    public List<RigaInstallazione> caricaRigheInstallazioneByArticolo(Integer idArticolo) {
        LOGGER.debug("--> Enter caricaRigheInstallazioneByArticolo");
        start("caricaRigheInstallazioneByArticolo");
        List<RigaInstallazione> result = null;
        ;
        try {
            result = installazioniService.caricaRigheInstallazioneByArticolo(idArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheInstallazioneByArticolo");
        }
        LOGGER.debug("--> Exit caricaRigheInstallazioneByArticolo ");
        return result;
    }

    @Override
    public List<RigaInstallazione> caricaRigheInstallazioneByInstallazione(Integer idInstallazione) {
        LOGGER.debug("--> Enter caricaRigheInstallazioneByInstallazione");
        start("caricaRigheInstallazioneByInstallazione");
        List<RigaInstallazione> result = null;
        try {
            result = installazioniService.caricaRigheInstallazioneByInstallazione(idInstallazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheInstallazioneByInstallazione");
        }
        LOGGER.debug("--> Exit caricaRigheInstallazioneByInstallazione ");
        return result;
    }

    @Override
    public List<TipoAreaInstallazione> caricaTipiAreeInstallazione() {
        LOGGER.debug("--> Enter caricaTipiAreeInstallazione");
        start("caricaTipiAreeInstallazione");

        List<TipoAreaInstallazione> result = null;
        try {
            result = installazioniService.caricaTipiAreeInstallazione();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiAreeInstallazione");
        }
        LOGGER.debug("--> Exit caricaTipiAreeInstallazione ");
        return result;
    }

    @Override
    public TipoAreaInstallazione caricaTipoAreaInstallazioneById(Integer id) {
        LOGGER.debug("--> Enter caricaTipoAreaInstallazioneById");
        start("caricaTipoAreaInstallazioneById");

        TipoAreaInstallazione object = null;
        try {
            object = installazioniService.caricaTipoAreaInstallazioneById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoAreaInstallazioneById");
        }
        LOGGER.debug("--> Exit caricaTipoAreaInstallazioneById ");
        return object;
    }

    @Override
    public TipoAreaInstallazione caricaTipoAreaInstallazioneByTipoDocumento(int idTipoDocumento) {
        LOGGER.debug("--> Enter caricaTipoAreaInstallazioneByTipoDocumento");
        start("caricaTipoAreaInstallazioneByTipoDocumento");
        TipoAreaInstallazione result = null;
        try {
            result = installazioniService.caricaTipoAreaInstallazioneById(idTipoDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoAreaInstallazioneByTipoDocumento");
        }
        LOGGER.debug("--> Exit caricaTipoAreaInstallazioneByTipoDocumento ");
        return result;
    }

    @Override
    public UbicazioneInstallazione caricaUbicazioneInstallazioneById(Integer id) {
        LOGGER.debug("--> Enter caricaUbicazioneInstallazioneById");
        start("caricaUbicazioneInstallazioneById");

        UbicazioneInstallazione object = null;
        try {
            object = manutenzioneAnagraficaService.caricaUbicazioneInstallazioneById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaUbicazioneInstallazioneById");
        }
        LOGGER.debug("--> Exit caricaUbicazioneInstallazioneById ");
        return object;
    }

    @Override
    public List<UbicazioneInstallazione> caricaUbicazioniInstallazione() {
        LOGGER.debug("--> Enter caricaUbicazioniInstallazione");
        start("caricaUbicazioniInstallazione");

        List<UbicazioneInstallazione> result = null;
        try {
            result = manutenzioneAnagraficaService.caricaUbicazioniInstallazione();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaUbicazioniInstallazione");
        }
        LOGGER.debug("--> Exit caricaUbicazioniInstallazione ");
        return result;
    }

    @Override
    public int creaAreaMagazzino(int idAreaInstallazione) {
        LOGGER.debug("--> Enter creaAreaMagazzino");
        start("creaAreaMagazzino");
        int result = 0;
        try {
            result = installazioniService.creaAreaMagazzino(idAreaInstallazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaAreaMagazzino");
        }
        LOGGER.debug("--> Exit creaAreaMagazzino ");
        return result;
    }

    @Override
    public List<AreaInstallazione> ricercaAreeInstallazioni(ParametriRicercaAreeInstallazione parametri) {
        LOGGER.debug("--> Enter ricercaAreeInstallazioni");
        start("ricercaAreeInstallazioni");
        List<AreaInstallazione> result = null;
        try {
            result = installazioniService.ricercaAreeInstallazioni(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaAreeInstallazioni");
        }
        LOGGER.debug("--> Exit ricercaAreeInstallazioni ");
        return result;
    }

    @Override
    public List<ArticoloMI> ricercaArticoloMI(ParametriRicercaArticoliMI parametriRicerca) {
        LOGGER.debug("--> Enter ricercaArticoloMI");
        start("ricercaArticoloMI");
        List<ArticoloMI> result = null;
        try {
            result = manutenzioneAnagraficaService.ricercaArticoloMI(parametriRicerca);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaArticoloMI");
        }
        LOGGER.debug("--> Exit ricercaArticoloMI ");
        return result;
    }

    @Override
    public List<Installazione> ricercaInstallazioniByEntita(Integer idEntita) {

        LOGGER.debug("--> Enter ricercaInstallazioniByEntita");
        start("ricercaInstallazioniByEntita");
        List<Installazione> result = null;
        try {
            result = installazioniService.ricercaByEntita(idEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaInstallazioniByEntita");
        }
        LOGGER.debug("--> Exit ricercaInstallazioniByEntita ");

        return result;
    }

    @Override
    public List<Installazione> ricercaInstallazioniByParametri(ParametriRicercaInstallazioni parametri) {
        LOGGER.debug("--> Enter ricercaInstallazioniByParametri");
        start("ricercaInstallazioniByParametri");
        List<Installazione> result = null;
        try {
            result = installazioniService.ricercaByParametri(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaInstallazioniByParametri");
        }
        LOGGER.debug("--> Exit ricercaInstallazioniByParametri ");
        return result;
    }

    @Override
    public List<Installazione> ricercaInstallazioniBySede(Integer idSedeEntita) {
        LOGGER.debug("--> Enter ricercaInstallazioniBySede");
        start("ricercaInstallazioniBySede");
        List<Installazione> result = null;
        try {
            result = installazioniService.ricercaBySede(idSedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaInstallazioniBySede");
        }
        LOGGER.debug("--> Exit ricercaInstallazioniBySede");
        return result;
    }

    @Override
    public List<Operatore> ricercaOperatori(ParametriRicercaOperatori parametri) {
        LOGGER.debug("--> Enter ricercaOperatori");
        start("ricercaOperatori");
        List<Operatore> result = null;
        try {
            result = operatoriService.ricercaOperatori(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaOperatori");
        }
        LOGGER.debug("--> Exit ricercaOperatori ");
        return result;
    }

    @Override
    public AreaInstallazione salvaAreaInstallazione(AreaInstallazione areaInstallazione) {
        LOGGER.debug("--> Enter salvaAreaInstallazione");
        start("salvaAreaInstallazione");

        AreaInstallazione object = null;
        try {
            object = installazioniService.salvaAreaInstallazione(areaInstallazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAreaInstallazione");
        }
        LOGGER.debug("--> Exit salvaAreaInstallazione ");
        return object;
    }

    @Override
    public ArticoloMI salvaArticoloMI(ArticoloMI articoloMI) {
        LOGGER.debug("--> Enter salvaArticoloMI");
        start("salvaArticoloMI");

        ArticoloMI object = null;
        try {
            object = manutenzioneAnagraficaService.salvaArticoloMI(articoloMI);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaArticoloMI");
        }
        LOGGER.debug("--> Exit salvaArticoloMI ");
        return object;
    }

    @Override
    public CausaleInstallazione salvaCausaleInstallazione(CausaleInstallazione causaleInstallazione) {
        LOGGER.debug("--> Enter salvaCausaleInstallazione");
        start("salvaCausaleInstallazione");

        CausaleInstallazione object = null;
        try {
            object = installazioniService.salvaCausaleInstallazione(causaleInstallazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCausaleInstallazione");
        }
        LOGGER.debug("--> Exit salvaCausaleInstallazione ");
        return object;
    }

    @Override
    public Installazione salvaInstallazione(Installazione installazione) {
        LOGGER.debug("--> Enter salvaInstallazione");
        start("salvaInstallazione");

        Installazione object = null;
        try {
            object = installazioniService.salvaInstallazione(installazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaInstallazione");
        }
        LOGGER.debug("--> Exit salvaInstallazione ");
        return object;
    }

    @Override
    public ManutenzioneSettings salvaManutenzioneSettings(ManutenzioneSettings manutenzioneSettings) {
        LOGGER.debug("--> Enter salvaManutenzioneSettings");
        start("salvaManutenzioneSettings");

        ManutenzioneSettings manutenzioneSettingsSave = null;
        try {
            manutenzioneSettingsSave = manutenzioneAnagraficaService.salvaManutenzioneSettings(manutenzioneSettings);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaManutenzioneSettings");
        }
        LOGGER.debug("--> Exit salvaManutenzioneSettings ");
        return manutenzioneSettingsSave;
    }

    @Override
    public Operatore salvaOperatore(Operatore operatore) {
        LOGGER.debug("--> Enter salvaOperatore");
        start("salvaOperatore");

        Operatore object = null;
        try {
            object = operatoriService.salvaOperatore(operatore);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaOperatore");
        }
        LOGGER.debug("--> Exit salvaOperatore ");
        return object;
    }

    @Override
    public ProdottoCollegato salvaProdottoCollegato(ProdottoCollegato prodottoCollegato) {
        LOGGER.debug("--> Enter salvaProdottoCollegato");
        start("salvaProdottoCollegato");

        ProdottoCollegato object = null;
        try {
            object = prodottiCollegatiService.salvaProdottoCollegato(prodottoCollegato);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaProdottoCollegato");
        }
        LOGGER.debug("--> Exit salvaProdottoCollegato ");
        return object;
    }

    @Override
    public RigaInstallazione salvaRigaInstallazione(RigaInstallazione rigaInstallazione) {
        LOGGER.debug("--> Enter salvaRigaInstallazione");
        start("salvaRigaInstallazione");

        RigaInstallazione object = null;
        try {
            object = installazioniService.salvaRigaInstallazione(rigaInstallazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigaInstallazione");
        }
        LOGGER.debug("--> Exit salvaRigaInstallazione ");
        return object;
    }

    @Override
    public RigaInstallazione salvaRigaInstallazioneInizializza(RigaInstallazione rigaInstallazione) {
        LOGGER.debug("--> Enter salvaInizializza");
        start("salvaInizializza");
        RigaInstallazione result = null;
        try {
            result = installazioniService.salvaRigaInstallazioneInizializza(rigaInstallazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaInizializza");
        }
        LOGGER.debug("--> Exit salvaInizializza ");
        return result;
    }

    @Override
    public TipoAreaInstallazione salvaTipoAreaInstallazione(TipoAreaInstallazione tipoAreaInstallazione) {
        LOGGER.debug("--> Enter salvaTipoAreaInstallazione");
        start("salvaTipoAreaInstallazione");

        TipoAreaInstallazione object = null;
        try {
            object = installazioniService.salvaTipoAreaInstallazione(tipoAreaInstallazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTipoAreaInstallazione");
        }
        LOGGER.debug("--> Exit salvaTipoAreaInstallazione ");
        return object;
    }

    @Override
    public UbicazioneInstallazione salvaUbicazioneInstallazione(UbicazioneInstallazione ubicazioneInstallazione) {
        LOGGER.debug("--> Enter salvaUbicazioneInstallazione");
        start("salvaUbicazioneInstallazione");

        UbicazioneInstallazione object = null;
        try {
            object = manutenzioneAnagraficaService.salvaUbicazioneInstallazione(ubicazioneInstallazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaUbicazioneInstallazione");
        }
        LOGGER.debug("--> Exit salvaUbicazioneInstallazione ");
        return object;
    }

    /**
     * @param installazioniService
     *            the installazioniService to set
     */
    public void setInstallazioniService(InstallazioniService installazioniService) {
        this.installazioniService = installazioniService;
    }

    /**
     * @param manutenzioneAnagraficaService
     *            the manutenzioneAnagraficaService to set
     */
    public void setManutenzioneAnagraficaService(ManutenzioneAnagraficaService manutenzioneAnagraficaService) {
        this.manutenzioneAnagraficaService = manutenzioneAnagraficaService;
    }

    /**
     * @param operatoriService
     *            the operatoriService to set
     */
    public void setOperatoriService(OperatoriService operatoriService) {
        this.operatoriService = operatoriService;
    }

    /**
     * @param prodottiCollegatiService
     *            The prodottiCollegatiService to set.
     */
    public void setProdottiCollegatiService(ProdottiCollegatiService prodottiCollegatiService) {
        this.prodottiCollegatiService = prodottiCollegatiService;
    }

    @Override
    public void sostituisciOperatore(Integer idOperatoreDaSostituire, Integer idOperatore, boolean sostituisciTecnico,
            boolean sostituisciCaricatore) {
        LOGGER.debug("--> Enter sostituisciOperatore");
        start("sostituisciOperatore");
        try {
            operatoriService.sostituisciOperatore(idOperatoreDaSostituire, idOperatore, sostituisciTecnico,
                    sostituisciCaricatore);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("sostituisciOperatore");
        }
        LOGGER.debug("--> Exit sostituisciOperatore ");
    }
}
