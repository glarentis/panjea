package it.eurotn.panjea.rate.manager;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.StatoAreaContabileManager;
import it.eurotn.panjea.contabilita.manager.liquidazione.interfaces.LiquidazionePagamentiManager;
import it.eurotn.panjea.contabilita.service.exception.StatoAreaContabileNonValidoException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.AreaRateManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaRateManager")
public class AreaRateManagerBean implements AreaRateManager {
    private static Logger logger = Logger.getLogger(AreaRateManagerBean.class.getName());

    @Resource
    private SessionContext context;

    /**
     * @uml.property name="panjeaDAO"
     * @uml.associationEnd
     */
    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    @IgnoreDependency
    private AreaContabileManager areaContabileManager;

    @EJB
    @IgnoreDependency
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private StatoAreaContabileManager statoAreaContabileManager;

    @EJB
    @IgnoreDependency
    private LiquidazionePagamentiManager liquidazionePagamentiManager;

    /**
     * @uml.property name="tipiAreaPartitaManager"
     * @uml.associationEnd
     */
    @EJB
    private TipiAreaPartitaManager tipiAreaPartitaManager;

    @Override
    public void cancellaAreaRate(Documento documento) {
        logger.debug("--> Enter cancellaAreaRate");
        AreaRate areaRate = caricaAreaRate(documento);
        if (areaRate.getId() != null) {
            liquidazionePagamentiManager.cancellaPagamentoLiquidazionePrecedente(documento);
            try {
                panjeaDAO.delete(areaRate);
            } catch (Exception e) {
                logger.error("Errore nel cancellare l'areaRate", e);
                throw new RuntimeException(e);
            }
        }
        logger.debug("--> Exit cancellaAreaRate");
    }

    @Override
    public AreaRate caricaAreaRate(Documento documento) {
        logger.debug("--> Enter caricaAreaRate");
        Query query = panjeaDAO.prepareNamedQuery("AreaRate.ricercaByDocumento");
        query.setParameter("paramIdDocumento", documento.getId());
        AreaRate areaRate;
        try {
            areaRate = (AreaRate) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            logger.debug("--> AreaRate non trovata, restituisco una nuova istanza di AreaRate");
            areaRate = new AreaRate();
            areaRate.setDocumento(documento);
        } catch (DAOException e) {
            logger.error("--> errore DAO in caricaAreaRate ByDocumento", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaAreaRate");
        return areaRate;
    }

    @Override
    public AreaRate caricaAreaRate(Integer idAreaRate) {
        logger.debug("--> Enter caricaAreaRate");
        AreaRate areaRate;
        try {
            areaRate = panjeaDAO.load(AreaRate.class, idAreaRate);
        } catch (ObjectNotFoundException e) {
            logger.error("ObjectNotFoundException per caricaAreaRate");
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("Errore nel caricare l'areaRate", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaAreaRate");
        return areaRate;
    }

    @Override
    public boolean checkCambioScontoCommerciale(AreaRate areaRate) {

        boolean result = Boolean.FALSE;

        if (areaRate != null && !areaRate.isNew()) {
            AreaRate areaRateOld = caricaAreaRate(areaRate.getId());

            // Controllo se il codice pagamento è cambiato e se è cambiata la percentuale di sconto
            // commerciale. In
            // questo
            // caso devo aggiungerla sulle righe dell'area.
            CodicePagamento codicePagamento = areaRate.getCodicePagamento();
            BigDecimal scontoComm = (codicePagamento != null) ? codicePagamento.getPercentualeScontoCommerciale()
                    : BigDecimal.ZERO;
            CodicePagamento codicePagamentoOld = areaRateOld.getCodicePagamento();
            BigDecimal scontoCommOld = (codicePagamentoOld != null)
                    ? codicePagamentoOld.getPercentualeScontoCommerciale() : BigDecimal.ZERO;

            result = scontoComm.compareTo(scontoCommOld) != 0;
        }

        return result;
    }

    @Override
    public AreaRate checkInvalidaAreaRate(AreaRate areaRate, Boolean cambiaStatoDocumento) {
        logger.debug("--> Enter checkInvalidaAreaRate");
        if (areaRate.getDatiValidazione().isValid()) {
            areaRate.getDatiValidazione().invalida();
            areaRate = salvaAreaRate(areaRate);
        }
        Documento documento = areaRate.getDocumento();
        AreaContabile areaContabile = areaContabileManager.caricaAreaContabileByDocumento(documento.getId());
        if (areaContabile != null) {
            try {
                if (areaContabile.getStatoAreaContabile() != StatoAreaContabile.PROVVISORIO) {
                    areaContabile = statoAreaContabileManager.cambioStatoDaConfermatoInProvvisorio(areaContabile);
                }
            } catch (StatoAreaContabileNonValidoException ex) {
                logger.error("--> Errore durante il cambio della stato dell'area contabile.", ex);
                throw new RuntimeException("Errore durante il cambio della stato dell'area contabile.", ex);
            } catch (Exception e) {
                logger.error("--> errore nel salvataggio dell' area contabile in changeStatoAreaContabile", e);
                throw new RuntimeException(
                        "--> errore nel salvataggio dell' area contabile in changeStatoAreaContabile", e);
            }
        } else {
            AreaMagazzino areaMagazzino = areaMagazzinoManager.caricaAreaMagazzinoByDocumento(documento);
            if (areaMagazzino != null && areaMagazzino.getStatoAreaMagazzino() != StatoAreaMagazzino.PROVVISORIO) {
                // lo riporto a provvisorio
                logger.debug("--> Porto lo stato dell'area magazzino a provvisorio ");
                areaMagazzinoManager.cambiaStatoInProvvisorio(areaMagazzino);
            }
        }

        logger.debug("--> Exit checkInvalidaAreaRate");
        return areaRate;
    }

    /**
     * recupera {@link JecPrincipal} dal {@link SessionContext}.
     *
     * @return {@link JecPrincipal}
     */
    private JecPrincipal getJecPrincipal() {
        return (JecPrincipal) context.getCallerPrincipal();
    }

    @Override
    public AreaRate salvaAreaRate(AreaRate areaRate) {
        if (logger.isDebugEnabled()) {
            logger.debug("--> Enter salvaAreaRate " + areaRate);
        }
        AreaRate areaRateSalvata = null;
        // devo sincronizzare il tipo documento del documento con il tipo
        // documento dell'area rate
        TipoAreaPartita tipoAreaPartita = tipiAreaPartitaManager
                .caricaTipoAreaPartitaPerTipoDocumento(areaRate.getDocumento().getTipoDocumento());

        if (tipoAreaPartita.isNew()) {
            throw new RuntimeException("Area partita non prevista per il documento con id "
                    + areaRate.getDocumento().getTipoDocumento().getId());
        }
        // allinea il valore caricato di tipoAreaPartita con l'attributo di
        // AreaPartita
        areaRate.setTipoAreaPartita(tipoAreaPartita);
        try {
            areaRateSalvata = panjeaDAO.save(areaRate);
        } catch (Exception e) {
            logger.error("Errore nel salvataggio dell'area partite", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit salvaAreaRate");
        return areaRateSalvata;
    }

    @Override
    public AreaRate validaAreaRate(AreaRate areaRate, AreaContabile areaContabile) {
        logger.debug("--> Enter validaAreaRate");
        areaRate.getDatiValidazione().valida(getJecPrincipal().getUserName());
        AreaRate areaRateSalvata = salvaAreaRate(areaRate);
        if (areaContabile != null) {
            try {
                statoAreaContabileManager.cambioStatoDaProvvisorioInConfermato(areaContabile);
            } catch (StatoAreaContabileNonValidoException e) {
                logger.error(
                        "--> errore StatoAreaContabileNonValido in validaRatePartita per il cambio stato del documento ",
                        e);
                throw new RuntimeException(e);
            }
        }
        logger.debug("--> Exit validaAreaRate");
        return areaRateSalvata;
    }

    @Override
    public AreaRate validaAreaRate(AreaRate areaRate, AreaMagazzino areaMagazzino) {
        logger.debug("--> Enter validaAreaRate");
        areaRate.getDatiValidazione().valida(getJecPrincipal().getUserName());
        AreaRate areaRateSalvata = salvaAreaRate(areaRate);
        if (areaMagazzino.getStatoAreaMagazzino() == AreaMagazzino.StatoAreaMagazzino.PROVVISORIO) {
            areaMagazzinoManager.cambiaStatoDaProvvisorioInConfermato(areaMagazzino);
        }
        logger.debug("--> Exit validaAreaRate");
        return areaRateSalvata;
    }

}
