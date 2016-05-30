package it.eurotn.panjea.vending.manager.arearifornimento;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.manutenzioni.domain.DatiInstallazione;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.InstallazioniManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.manager.arearifornimento.interfaces.AreaRifornimentoAggiornaManager;

@Stateless(name = "Panjea.AreaRifornimentoAggiornaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaRifornimentoAggiornaManager")
public class AreaRifornimentoAggiornaManagerBean implements AreaRifornimentoAggiornaManager {

    private static final Logger LOGGER = Logger.getLogger(AreaRifornimentoAggiornaManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;
    @EJB
    private SediEntitaManager sediEntitaManager;
    @EJB
    private InstallazioniManager installazioniManager;
    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    private AreaMagazzino aggiornaAreaMagazzino(AreaRifornimento areaRifornimento) {

        AreaMagazzino am = areaRifornimento.getAreaMagazzino();

        if (am.getTipoAreaMagazzino() == null && areaRifornimento.getInstallazione() != null
                && areaRifornimento.getInstallazione().getTipoAreaMagazzino() != null) {
            TipoAreaMagazzino tam = null;
            try {
                tam = panjeaDAO.load(TipoAreaMagazzino.class,
                        areaRifornimento.getInstallazione().getTipoAreaMagazzino().getId());
            } catch (ObjectNotFoundException e1) {
                e1.printStackTrace();
            }
            am.setTipoAreaMagazzino(tam);
            if (am.getTipoAreaMagazzino().isDataDocLikeDataReg()) {
                am.getDocumento().setDataDocumento(am.getDataRegistrazione());
            }

            am.setDocumentoCreatoDaAreaMagazzino(false);
            am.setDocumentoForzatoDaAreaMagazzino(false);
        }
        return am;
    }

    @Override
    public AreaRifornimento aggiornaDatiInstallazione(AreaRifornimento areaRifornimento, Installazione installazione) {
        LOGGER.debug("--> Enter aggiornaDatiInstallazione");

        AreaRifornimento areaRifornimentoAggiornata = areaRifornimento;
        areaRifornimentoAggiornata.setDistributore(null);
        areaRifornimentoAggiornata.setInstallazione(null);
        areaRifornimentoAggiornata.setOperatore(null);
        areaRifornimentoAggiornata.setDepositoOrigine(null);
        areaRifornimentoAggiornata.setCodiceIvaAlternativo(null);
        areaRifornimentoAggiornata.setTipologiaCodiceIvaAlternativo(null);
        areaRifornimentoAggiornata.getDocumento().setEntita(null);
        areaRifornimentoAggiornata.getDocumento().setSedeEntita(null);

        if (installazione != null) {
            installazione = caricaInstallazione(installazione);
            areaRifornimentoAggiornata.getDocumento()
                    .setEntita(installazione.getDeposito().getSedeEntita().getEntita().getEntitaLite());
            areaRifornimentoAggiornata.getDocumento().setSedeEntita(installazione.getDeposito().getSedeEntita());
            try {
                areaRifornimentoAggiornata
                        .setDistributore(panjeaDAO.load(Distributore.class, installazione.getArticolo().getId()));
            } catch (Exception e) {
                areaRifornimentoAggiornata.setDistributore(null);
            }

            areaRifornimentoAggiornata.setInstallazione(installazione);

            DatiInstallazione datiInstallazione = installazione.getDatiInstallazione();
            Operatore caricatore = datiInstallazione.getCaricatore();
            caricatore = caricatore != null && !caricatore.isNew() ? caricatore : null;
            areaRifornimentoAggiornata.setOperatore(caricatore);

            if (datiInstallazione.getCaricatore() != null
                    && datiInstallazione.getCaricatore().getMezzoTrasporto() != null) {
                areaRifornimentoAggiornata
                        .setDepositoOrigine(datiInstallazione.getCaricatore().getMezzoTrasporto().getDeposito());
            }

            if (datiInstallazione.getIvaSomministrazione() != null) {
                areaRifornimentoAggiornata
                        .setTipologiaCodiceIvaAlternativo(ETipologiaCodiceIvaAlternativo.SOMMINISTRAZIONE);
                try {
                    areaRifornimentoAggiornata.setCodiceIvaAlternativo(
                            panjeaDAO.load(CodiceIva.class, datiInstallazione.getIvaSomministrazione().getId()));
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }

            DepositoLite deposito = areaRifornimento.getInstallazione().getDeposito().creaLite();
            try {
                deposito = panjeaDAO.load(DepositoLite.class, deposito.getId());
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
            areaRifornimentoAggiornata.setDepositoDestinazione(deposito);

            Listino listino = null;
            if (areaRifornimento.getInstallazione().getListino() != null) {
                try {
                    listino = panjeaDAO.load(Listino.class, areaRifornimento.getInstallazione().getListino().getId());
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
            areaRifornimentoAggiornata.setListino(listino);

            Listino listinoAlternativo = null;
            if (areaRifornimento.getInstallazione() != null
                    && areaRifornimento.getInstallazione().getListinoAlternativo() != null) {
                try {
                    listinoAlternativo = panjeaDAO.load(Listino.class,
                            areaRifornimento.getInstallazione().getListinoAlternativo().getId());
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
            areaRifornimentoAggiornata.setListinoAlternativo(listinoAlternativo);

        }

        LOGGER.debug("--> Exit aggiornaDatiInstallazione");
        if (areaRifornimentoAggiornata.getAreaMagazzino() != null) {
            areaRifornimentoAggiornata.setAreaMagazzino(aggiornaAreaMagazzino(areaRifornimentoAggiornata));
        }
        return areaRifornimentoAggiornata;
    }

    @Override
    public AreaRifornimento aggiornaDistributore(AreaRifornimento areaRifornimento, Integer idDistributore) {
        LOGGER.debug("--> Enter aggiornaArticolo");

        AreaRifornimento areaRifornimentoAggiornata = areaRifornimento;
        areaRifornimentoAggiornata.setDistributore(null);
        areaRifornimentoAggiornata.setInstallazione(null);
        areaRifornimentoAggiornata.setOperatore(null);
        areaRifornimentoAggiornata.setDepositoOrigine(null);
        if (idDistributore != null) {
            Installazione installazione = installazioniManager.caricaByArticolo(idDistributore);
            if (installazione != null) {
                areaRifornimentoAggiornata = aggiornaDatiInstallazione(areaRifornimentoAggiornata, installazione);
            }
        }

        LOGGER.debug("--> Exit aggiornaArticolo");
        return areaRifornimentoAggiornata;
    }

    @Override
    public AreaRifornimento aggiornaEntita(AreaRifornimento areaRifornimento, Integer idEntita) {
        LOGGER.debug("--> Enter aggiornaEntita");

        AreaRifornimento areaRifornimentoAggiornata = areaRifornimento;
        areaRifornimentoAggiornata.getDocumento().setEntita(null);
        areaRifornimentoAggiornata.getDocumento().setSedeEntita(null);
        areaRifornimentoAggiornata.setDistributore(null);
        areaRifornimentoAggiornata.setInstallazione(null);
        areaRifornimentoAggiornata.setOperatore(null);
        areaRifornimentoAggiornata.setDepositoOrigine(null);
        if (idEntita != null) {
            try {
                areaRifornimento.getDocumento().setEntita(panjeaDAO.load(EntitaLite.class, idEntita));
            } catch (Exception e1) {
                LOGGER.error("--> errore durante il caricamento dell'entita", e1);
                throw new GenericException("errore durante il caricamento dell'entita", e1);
            }
            SedeEntita sedePrincipale = null;
            try {
                sedePrincipale = sediEntitaManager.caricaSedePrincipaleEntita(idEntita);
            } catch (AnagraficaServiceException e) {
                sedePrincipale = null;
            }
            if (sedePrincipale != null) {
                areaRifornimentoAggiornata = aggiornaSedeEntita(areaRifornimentoAggiornata, sedePrincipale);
            }
        }

        LOGGER.debug("--> Exit aggiornaEntita");
        return areaRifornimentoAggiornata;
    }

    @Override
    public AreaRifornimento aggiornaSedeEntita(AreaRifornimento areaRifornimento, SedeEntita sedeEntita) {
        LOGGER.debug("--> Enter aggiornaSedeEntita");

        AreaRifornimento areaRifornimentoAggiornata = areaRifornimento;
        areaRifornimentoAggiornata.setDistributore(null);
        areaRifornimentoAggiornata.setInstallazione(null);
        areaRifornimentoAggiornata.setOperatore(null);
        areaRifornimentoAggiornata.getDocumento().setSedeEntita(null);
        areaRifornimentoAggiornata.setDepositoOrigine(null);
        if (sedeEntita != null && !sedeEntita.isNew()) {
            areaRifornimentoAggiornata.getDocumento().setSedeEntita(sedeEntita);
            List<Installazione> installazioni = installazioniManager.ricercaBySede(sedeEntita.getId());
            if (installazioni != null && installazioni.size() == 1) {
                Installazione installazioneSede = installazioniManager.caricaById(installazioni.get(0).getId());
                areaRifornimentoAggiornata = aggiornaDatiInstallazione(areaRifornimentoAggiornata, installazioneSede);
            }
        }

        LOGGER.debug("--> Exit aggiornaSedeEntita");
        return areaRifornimentoAggiornata;
    }

    Installazione caricaInstallazione(Installazione installazione) {
        try {
            installazione = panjeaDAO.load(Installazione.class, installazione.getId());
        } catch (ObjectNotFoundException e1) {
            e1.printStackTrace();
        }
        return installazione;
    }
}
