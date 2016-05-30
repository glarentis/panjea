package it.eurotn.panjea.contabilita.manager;

import java.util.List;

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

import it.eurotn.codice.generator.interfaces.ProtocolloAnnoGenerator;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileVerificaManager;
import it.eurotn.panjea.contabilita.manager.rateirisconti.interfaces.RateoRiscontoManager;
import it.eurotn.panjea.contabilita.manager.spesometro.entitacointestazione.interfaces.EntitaCointestazioneManager;
import it.eurotn.panjea.contabilita.service.exception.StatoAreaContabileNonValidoException;
import it.eurotn.panjea.intra.manager.interfaces.AreaIntraManager;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaCancellaManager;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoDocumentoCancellaManager;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;

/**
 * Manager per la gestione della cancellazione dell'area contabile e le sue aree collegate
 * (AreaPartite,AreaMagazzino,AreaIva).
 *
 * @author Leonardo
 */
@Stateless(name = "Panjea.AreaContabileCancellaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaContabileCancellaManager")
public class AreaContabileCancellaManagerBean implements AreaContabileCancellaManager {

    private static Logger logger = Logger.getLogger(AreaContabileCancellaManagerBean.class);

    @Resource
    protected SessionContext context;
    @EJB
    protected AreaContabileVerificaManager areaContabileVerificaManager;
    @EJB
    @IgnoreDependency
    protected AreaIvaManager areaIvaManager;
    @EJB
    protected PanjeaDAO panjeaDAO;
    @EJB(mappedName = "Panjea.DocumentiManager")
    protected DocumentiManager documentiManager;
    @EJB
    @IgnoreDependency
    protected AreaContabileManager areaContabileManager;
    @EJB
    protected AreaIvaCancellaManager areaIvaCancellaManager;
    @IgnoreDependency
    @EJB
    protected AreaRateManager areaRateManager;
    @IgnoreDependency
    @EJB
    protected AreaMagazzinoDocumentoCancellaManager areaMagazzinoDocumentoCancellaManager;
    @EJB
    @IgnoreDependency
    protected AreaTesoreriaManager areaTesoreriaManager;
    @EJB
    private AreaIntraManager areaIntraManager;
    @IgnoreDependency
    @EJB(beanName = "AreaContabileProtocolloAnnoGeneratorBean")
    private ProtocolloAnnoGenerator protocolloAnnoGenerator;

    @EJB
    private RateoRiscontoManager rateoRiscontoManager;

    @EJB
    private EntitaCointestazioneManager entitaCointestazioneManager;

    @Override
    public void cancellaAreaContabile(AreaContabile areaContabile)
            throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException {
        cancellaAreaContabile(areaContabile, true);
    }

    @Override
    public void cancellaAreaContabile(AreaContabile areaContabile, boolean deleteAreeCollegate)
            throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException {
        cancellaAreaContabile(areaContabile, deleteAreeCollegate, false);
    }

    @Override
    public void cancellaAreaContabile(AreaContabile areaContabile, boolean deleteAreeCollegate,
            boolean forceDeleteAreeCollegate)
                    throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException {
        logger.debug("--> Enter cancellaAreaContabile");

        int numAreeCollegate = documentiManager.caricaNumeroAreeCollegate(areaContabile.getDocumento());
        boolean isLastAreaToDelete = numAreeCollegate == 1;
        try {
            if (areaContabile.getStatoAreaContabile().compareTo(StatoAreaContabile.VERIFICATO) == 0) {
                context.setRollbackOnly();
                throw new StatoAreaContabileNonValidoException(
                        "Impossibile cancellare una area contabile in stato verificato!");
            }

            cancellaCollegamentoRigaContabilePagamento(areaContabile, null);

            if (deleteAreeCollegate || isLastAreaToDelete) {
                areaRateManager.cancellaAreaRate(areaContabile.getDocumento());
                areaIvaCancellaManager.cancellaAreaIva(areaContabile.getDocumento());
                areaTesoreriaManager.cancellaAreaTesoreria(areaContabile.getDocumento(), false);
                areaIntraManager.cancellaAreaIntra(areaContabile.getDocumento());
                areaMagazzinoDocumentoCancellaManager.cancellaAreaMagazzino(areaContabile.getDocumento(),
                        forceDeleteAreeCollegate);
            } else {
                AreaIva areaIva = areaIvaManager.caricaAreaIvaByDocumento(areaContabile.getDocumento());
                if (areaIva != null && areaIva.getId() != null) {
                    areaIvaManager.associaAreaContabile(areaIva, null);
                }
            }

            // invalida se le condizioni sono soddisfatte il giornale e il registro iva
            areaContabileVerificaManager.checkInvalidaDocumentiContabilita(null, areaContabile);

        } catch (AreeCollegatePresentiException e) {
            context.setRollbackOnly();
            throw e;
        } catch (DocumentiCollegatiPresentiException e) {
            context.setRollbackOnly();
            throw e;
        } catch (Exception e) {
            logger.error("--> errore in cancellazione AreaContabile ", e);
            throw new RuntimeException(e);
        }
        // cancello qui l'area contabile.Devo cancellarla dopo aver cancellato
        // l'iva altrimenti mi da una
        // VincoloException
        cancellaAreaContabileNoCheck(areaContabile);
        if (deleteAreeCollegate || isLastAreaToDelete) {
            // il documento devo cancellarlo sempre dopo aver cancellato tutte
            // le aree
            documentiManager.cancellaDocumento(areaContabile.getDocumento());
        }

        logger.debug("--> Exit cancellaAreaContabile");
    }

    @Override
    public void cancellaAreaContabile(Documento documento)
            throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException {
        cancellaAreaContabile(documento, false);
    }

    @Override
    public void cancellaAreaContabile(Documento documento, boolean forceDeleteAreeCollegate)
            throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException {
        AreaContabile areaContabile = areaContabileManager.caricaAreaContabileByDocumento(documento.getId());
        if (areaContabile != null && areaContabile.getId() != null && areaContabile.getId().intValue() != -1) {
            if (!forceDeleteAreeCollegate) {
                AreeCollegatePresentiException areeCollegatePresentiException = new AreeCollegatePresentiException();
                areeCollegatePresentiException.addAreaCollegata(areaContabile);
                context.setRollbackOnly();
                throw areeCollegatePresentiException;
            }
            cancellaAreaContabile(areaContabile, false);
        }
    }

    @Override
    public void cancellaAreaContabileNoCheck(AreaContabile areaContabile) {
        logger.debug("--> Enter cancellaAreaContabileNoCheck");
        try {
            // cancello gli eventuali link su simulazioni o politiche di calcolo perchè hibernate non supporta la
            // gestione del vincolo SET NULL sulle foreign key
            panjeaDAO.prepareNativeQuery("update bamm_simulazione set areaContabile_id = null where areaContabile_id = "
                    + areaContabile.getId()).executeUpdate();
            panjeaDAO.prepareNativeQuery(
                    "update bamm_politica_calcolo set areaContabile_id = null where areaContabile_id = "
                            + areaContabile.getId())
                    .executeUpdate();
            // link ai ratei/risconti
            panjeaDAO.prepareNativeQuery(
                    "update cont_righe_rateo_risconto_anno ratanno inner join cont_righe_contabili rc on ratanno.rigaContabile_id = rc.id set ratanno.rigaContabile_id = null where rc.areaContabile_id = "
                            + areaContabile.getId())
                    .executeUpdate();

            // link entità cointestate
            entitaCointestazioneManager.cancellaByAreaContabile(areaContabile.getId());

            panjeaDAO.delete(areaContabile);
            protocolloAnnoGenerator.restoreCodice(areaContabile);

            // se ho gestione diversa da normale su tipoAreaContabile devo avere un protocollo e registro iva collegati
            // e quindi codice e prefisso rispettivi sull'areaContabile
            if (!areaContabile.getTipoAreaContabile().getGestioneIva().equals(GestioneIva.NORMALE)
                    && areaContabile.getCodiceCollegato() != null) {
                protocolloAnnoGenerator.restoreCodice(areaContabile,
                        areaContabile.getTipoAreaContabile().getRegistroProtocolloCollegato());
            }
        } catch (Exception e) {
            logger.error("--> errore in cancellazione AreaContabile ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaAreaContabileNoCheck");
    }

    /**
     * Elimina il link tra rigaContabile e pagamento di tutte le righe se non viene specificata una riga contabile
     * particolare.
     *
     * @param areaContabile
     *            l'area contabile contenente l'id
     * @param rigaContabile
     *            la riga contabile contenente l'id
     */
    private void cancellaCollegamentoRigaContabilePagamento(AreaContabile areaContabile, RigaContabile rigaContabile) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("delete from ");
            sql.append("cont_righe_contabili_part_pagamenti ");
            sql.append("where cont_righe_contabili_id ");
            if (rigaContabile != null) {
                sql.append("= :paramRigaContabileId");
            } else {
                sql.append("in (select id from cont_righe_contabili where areaContabile_id = :paramAreaContabileId)");
            }

            Query query = panjeaDAO.getEntityManager().createNativeQuery(sql.toString());
            if (rigaContabile != null) {
                query.setParameter("paramRigaContabileId", rigaContabile.getId());
            } else {
                query.setParameter("paramAreaContabileId", areaContabile.getId());
            }

            panjeaDAO.executeQuery(query);
        } catch (Exception e) {
            logger.error("--> errore in cancellazione AreaContabile ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public AreaContabile cancellaRigaContabile(RigaContabile rigaContabile) {
        logger.debug("--> Enter cancellaRigaContabile");
        AreaContabile areaContabile;
        try {
            cancellaCollegamentoRigaContabilePagamento(rigaContabile.getAreaContabile(), rigaContabile);
            if (rigaContabile.isRateiRiscontiAttivi()) {
                rigaContabile = rateoRiscontoManager.cancellaRiferimentiRateoRisconto(rigaContabile);
            }

            panjeaDAO.delete(rigaContabile);

            // controllo l'area contabile
            areaContabile = areaContabileManager.caricaAreaContabile(rigaContabile.getAreaContabile().getId());
            areaContabile = areaContabileManager.checkInvalidaAreeCollegate(areaContabile);
        } catch (Exception e) {
            logger.error("--> errore in cancellazione RigaContabile", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaRigaContabile");
        return areaContabile;
    }

    @Override
    public AreaContabile cancellaRigheContabili(AreaContabile areaContabile) {

        List<RigaContabile> righe = areaContabileManager.caricaRigheContabili(areaContabile.getId());
        return cancellaRigheContabili(righe);
    }

    @Override
    public AreaContabile cancellaRigheContabili(List<RigaContabile> righeContabili) {

        AreaContabile areaContabile = null;
        if (righeContabili != null && !righeContabili.isEmpty()) {

            for (RigaContabile rigaContabile : righeContabili) {
                areaContabile = cancellaRigaContabile(rigaContabile);
            }
        }
        return areaContabile;
    }

}
