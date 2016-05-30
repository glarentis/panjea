/**
 *
 */
package it.eurotn.panjea.magazzino.manager.documento;

import java.util.Collections;
import java.util.Comparator;
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

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.intra.manager.interfaces.AreaIntraManager;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaCancellaManager;
import it.eurotn.panjea.lotti.manager.interfaces.LottiManager;
import it.eurotn.panjea.lotti.manager.verifica.interfaces.LottiVerificaManager;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaTestata;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoCancellaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces.MagazzinoControlloSchedeArticolo;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author Leonardo
 */
@Stateless(name = "Panjea.AreaMagazzinoCancellaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaMagazzinoCancellaManager")
public class AreaMagazzinoCancellaManagerBean implements AreaMagazzinoCancellaManager {

    /**
     * Comparator che ordina le righe magazzino mantenendo in fondo quelle di tipo {@link RigaTestata}.
     *
     * @author fattazzo
     * @version 1.0, 17/set/2012
     */
    private class CancellazioneRigheComparator implements Comparator<RigaMagazzino> {

        @Override
        public int compare(RigaMagazzino o1, RigaMagazzino o2) {
            if (o1 instanceof RigaTestata && o2 instanceof RigaTestata) {
                return Integer.valueOf(o2.getLivello()).compareTo(Integer.valueOf(o1.getLivello()));
            } else if (o1 instanceof RigaTestata) {
                return 1;
            } else if (o2 instanceof RigaTestata) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private static Logger logger = Logger.getLogger(AreaMagazzinoCancellaManagerBean.class);

    @Resource
    private SessionContext sessionContext;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private DocumentiManager documentiManager;

    @IgnoreDependency
    @EJB
    private AreaRateManager areaRateManager;

    @IgnoreDependency
    @EJB
    private AreaContabileCancellaManager areaContabileCancellaManager;

    @EJB
    private AreaIvaCancellaManager areaIvaCancellaManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private LottiManager lottiManager;

    @EJB
    private LottiVerificaManager lottiVerificaManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private AreaIntraManager areaIntraManager;

    @EJB
    private MagazzinoControlloSchedeArticolo magazzinoControlloSchedeArticolo;

    @Override
    public void cancellaAreaMagazzino(AreaMagazzino areaMagazzino)
            throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException, AreeCollegatePresentiException {
        logger.debug("--> Enter cancellaAreaMagazzino");
        cancellaAreaMagazzino(areaMagazzino, true);
        logger.debug("--> Exit cancellaAreaMagazzino");
    }

    @Override
    public void cancellaAreaMagazzino(AreaMagazzino areaMagazzino, boolean deleteAreeCollegate)
            throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException, AreeCollegatePresentiException {
        logger.debug("--> Enter cancellaAreaMagazzino");
        cancellaAreaMagazzino(areaMagazzino, deleteAreeCollegate, false);
        logger.debug("--> Exit cancellaAreaMagazzino");
    }

    @Override
    public void cancellaAreaMagazzino(AreaMagazzino areaMagazzino, boolean deleteAreeCollegate,
            boolean forceDeleteAreeCollegate) throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException,
                    AreeCollegatePresentiException {
        logger.debug("--> Enter cancellaAreaMagazzino");
        areaMagazzino = areaMagazzinoManager.caricaAreaMagazzino(areaMagazzino);
        Documento documento = areaMagazzino.getDocumento();

        int numAreeCollegate = documentiManager.caricaNumeroAreeCollegate(areaMagazzino.getDocumento());
        boolean isLastAreaToDelete = numAreeCollegate == 1;

        if (deleteAreeCollegate || isLastAreaToDelete) {
            areaRateManager.cancellaAreaRate(documento);
            areaIvaCancellaManager.cancellaAreaIva(documento);
            areaContabileCancellaManager.cancellaAreaContabile(documento, forceDeleteAreeCollegate);
            areaIntraManager.cancellaAreaIntra(documento);
        }

        magazzinoControlloSchedeArticolo.checkInvalidaSchedeArticolo(areaMagazzino);

        cancellaRigheMagazzino(areaMagazzino);
        cancellaAreaMagazzinoNoCheck(areaMagazzino);

        if (deleteAreeCollegate || isLastAreaToDelete) {
            documentiManager.cancellaDocumento(documento);
        }
        logger.debug("--> Exit cancellaAreaMagazzino");
    }

    /**
     * Cancella un'area magazzino senza verifiche.
     *
     * @param areaMagazzino
     *            area da cancellare
     */
    private void cancellaAreaMagazzinoNoCheck(AreaMagazzino areaMagazzino) {
        logger.debug("--> Enter cancellaAreaMagazzinoNoCheck");
        try {
            panjeaDAO.delete(areaMagazzino);
        } catch (Exception e) {
            logger.error("--> errore in cancellaAreaMagazzino", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaAreaMagazzinoNoCheck");
    }

    @Override
    public AreaMagazzino cancellaRigaMagazzino(RigaMagazzino rigaMagazzino) {
        return rigaMagazzinoManager.getDao(rigaMagazzino).cancellaRigaMagazzino(rigaMagazzino);
    }

    @Override
    public void cancellaRigheMagazzino(AreaMagazzino areaMagazzino) {
        logger.debug("--> Enter cancellaRigheMagazzino");

        // prima di cancellare le righe recupero i codici articolo perchè mi serviranno poi per la verifica dei lotti
        List<String> articoliRighe = null;
        Query queryArticoli = panjeaDAO.prepareQuery(
                "select distinct rm.articolo.codice from RigaArticolo rm where rm.areaMagazzino=:paramAreaMagazzino");
        queryArticoli.setParameter("paramAreaMagazzino", areaMagazzino);
        try {
            articoliRighe = panjeaDAO.getResultList(queryArticoli);
        } catch (DAOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Devo mettere a null tutte le righe che puntano a righeTestate collegate
        Query queryRigheTestateCollegate = panjeaDAO.prepareQuery(
                "update RigaMagazzino rm set rm.rigaTestataMagazzinoCollegata=null where rm.areaMagazzino=:paramAreaMagazzino");
        queryRigheTestateCollegate.setParameter("paramAreaMagazzino", areaMagazzino);
        queryRigheTestateCollegate.executeUpdate();

        // cancella prima i componenti e poi le righe per evitare di incorrere nel vincolo RigaComponente->RigaDistinta
        // metto i vincoli dei padri a null per poterli cancellare
        Query queryAzzeraPadriComponenti = panjeaDAO
                .prepareNamedQuery("RigaMagazzino.azzeraPadriRigheComponentiByAreaMagazzino");
        queryAzzeraPadriComponenti.setParameter("paramAreaMagazzino", areaMagazzino.getId());
        queryAzzeraPadriComponenti.executeUpdate();

        Query queryComponenti = panjeaDAO.prepareNamedQuery("RigaMagazzino.cancellaRigheComponentiByAreaMagazzino");
        queryComponenti.setParameter("paramAreaMagazzino", areaMagazzino.getId());

        // cancello le righe dell'area
        Query query = panjeaDAO.prepareNamedQuery("RigaMagazzino.cancellaByAreaMagazzino");
        query.setParameter("paramAreaMagazzino", areaMagazzino.getId());
        try {
            panjeaDAO.executeQuery(queryComponenti);
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        // verifico i lotti solo se si sta cancellando un movimento di carico
        switch (areaMagazzino.getTipoAreaMagazzino().getTipoMovimento()) {
        case CARICO:
        case INVENTARIO:
        case TRASFERIMENTO:
        case CARICO_PRODUZIONE:
            try {
                lottiVerificaManager.verifica(articoliRighe, areaMagazzino.getDepositoOrigine());
            } catch (Exception e) {
                throw new RuntimeException("eccezione dopo la cancellazione delle righe magazzino", e);
            }
        default:
            // non faccio niente perchè i movimenti di scarico liberano quantità
            // nel lotto.
        }

        lottiManager.cancellaLottiNonUtilizzati();

        logger.debug("--> Exit cancellaRigheMagazzino");
    }

    @Override
    public AreaMagazzino cancellaRigheMagazzino(List<RigaMagazzino> righeMagazzino) {

        Collections.sort(righeMagazzino, new CancellazioneRigheComparator());

        AreaMagazzino areaMagazzino = null;
        for (RigaMagazzino rigaMagazzino : righeMagazzino) {
            areaMagazzino = cancellaRigaMagazzino(rigaMagazzino);
        }
        return areaMagazzino;
    }
}
