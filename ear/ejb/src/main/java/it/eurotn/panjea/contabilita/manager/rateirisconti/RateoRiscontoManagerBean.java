package it.eurotn.panjea.contabilita.manager.rateirisconti;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabile.EContoInsert;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaContabileRateiRisconti;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoAnno;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRiscontoAnno;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.rateirisconti.interfaces.AreaChiusuraRateiManager;
import it.eurotn.panjea.contabilita.manager.rateirisconti.interfaces.RateoRiscontoManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.util.RigaElencoRiscontiDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.RateoRiscontoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RateoRiscontoManager")
public class RateoRiscontoManagerBean implements RateoRiscontoManager {

    private static final Logger LOGGER = Logger.getLogger(RateoRiscontoManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private AreaChiusuraRateiManager areaChiusuraRateiManager;

    @EJB
    private PianoContiManager pianoContiManager;

    private void aggiornaRigaContabileImportoRateoRisconto(RigaContabile rigaContabile) {

        cancellaRigaContabileImportoRateoRisconto(rigaContabile);

        RigaContabile rigaContabileImportoRR = RigaContabile.creaRigaContabile(rigaContabile.getAreaContabile(),
                rigaContabile.getConto(), rigaContabile.getContoInsert() != EContoInsert.DARE,
                rigaContabile.getImporto().subtract(rigaContabile.getImportoCostoRateoRiscontoAnnoDocumento()), null,
                true);

        try {
            panjeaDAO.saveWithoutFlush(rigaContabileImportoRR);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel salvare la riga contabile", e);
            throw new RuntimeException(e);
        }
    }

    private RigaContabile aggiornaRigaContabileRateoCollegata(RigaContabile rigaContabile, BigDecimal importoRateo)
            throws ContabilitaException, ContiBaseException, TipoDocumentoBaseException {
        RigaContabileRateiRisconti rigaContabileRateo = rigaContabile.getRigaRateoRiscontoDocumento();

        SottoConto contoRateo = null;
        if (rigaContabileRateo == null) {
            boolean isDare = rigaContabile.getAreaContabile().getDocumento().getTipoDocumento()
                    .getTipoEntita() == TipoEntita.FORNITORE;
            if (rigaContabile.getAreaContabile().getDocumento().getTipoDocumento()
                    .getTipoEntita() == TipoEntita.CLIENTE) {
                contoRateo = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.RATEO_PASSIVO);
            } else {
                contoRateo = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.RATEO_ATTIVO);
            }
            rigaContabileRateo = (RigaContabileRateiRisconti) RigaContabile.creaRigaContabile(
                    new RigaContabileRateiRisconti(), rigaContabile.getAreaContabile(), contoRateo, isDare,
                    importoRateo, null, false);
        }
        rigaContabileRateo.setOrdinamento(rigaContabile.getOrdinamento());
        rigaContabileRateo.setImporto(importoRateo);

        try {
            rigaContabileRateo = panjeaDAO.saveWithoutFlush(rigaContabileRateo);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel salvare la rigaContabilerisconto", e);
            throw new RuntimeException(e);
        }

        for (RigaRateoRisconto rateoRisconto : rigaContabile.getRigheRateoRisconto()) {
            rateoRisconto.getRateiRiscontiAnno().get(1).setRigaContabile(rigaContabileRateo);
        }

        return areaChiusuraRateiManager.creaAreaContabileChiusura(rigaContabile, importoRateo, contoRateo);
    }

    private RigaContabile aggiornaRigaContabileRiscontoCollegata(RigaContabile rigaContabile,
            BigDecimal importoRigariscontoRisconto) throws ContabilitaException, ContiBaseException {
        RigaContabile rigaContabilerisconto = rigaContabile.getRigaRateoRiscontoDocumento();

        if (rigaContabilerisconto == null) {
            // carico il conto
            SottoConto contorisconto = null;
            boolean isDare = rigaContabile.getAreaContabile().getDocumento().getTipoDocumento()
                    .getTipoEntita() == TipoEntita.FORNITORE;

            if (rigaContabile.getAreaContabile().getDocumento().getTipoDocumento()
                    .getTipoEntita() == TipoEntita.CLIENTE) {
                contorisconto = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.RISCONTO_PASSIVO);
            } else {
                contorisconto = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.RISCONTO_ATTIVO);
            }

            rigaContabilerisconto = RigaContabile.creaRigaContabile(new RigaContabileRateiRisconti(),
                    rigaContabile.getAreaContabile(), contorisconto, isDare, importoRigariscontoRisconto, null, false);
        }

        rigaContabilerisconto.setOrdinamento(rigaContabile.getOrdinamento());
        rigaContabilerisconto.setImporto(importoRigariscontoRisconto);

        try {
            rigaContabilerisconto = panjeaDAO.saveWithoutFlush(rigaContabilerisconto);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel salvare la rigaContabilerisconto", e);
            throw new RuntimeException(e);
        }
        for (RigaRateoRisconto rigariscontoRisconto : rigaContabile.getRigheRateoRisconto()) {
            rigariscontoRisconto.getRateiRiscontiAnno().get(0)
                    .setRigaContabile((RigaContabileRateiRisconti) rigaContabilerisconto);
        }
        return rigaContabile;
    }

    private RigaContabile cancellaRiferimentiRateo(RigaContabile rigaContabile) {
        boolean cancellaRighe = false;
        for (RigaRateoRisconto rigaRateoRisconto : rigaContabile.getRigheRateoRisconto()) {
            List<RigaRiscontoAnno> righeRateoRiscontiAnno = rigaRateoRisconto.getRateiRiscontiAnno();
            if (righeRateoRiscontiAnno.size() > 1) { // teoricamente sempre
                RigaContabile rigaContabileRateoAnnoPrecedente = righeRateoRiscontiAnno.get(0).getRigaContabile();
                if (rigaContabileRateoAnnoPrecedente != null) {
                    throw new RuntimeException(new VincoloException("Cancellare il documento del rateo collegato"));
                }

                RigaRiscontoAnno rigaRateoAnno = righeRateoRiscontiAnno.get(1);
                RigaContabile rigaContabileCollegata = rigaRateoAnno.getRigaContabile();
                cancellaRighe = true;
                if (rigaContabileCollegata != null) {
                    rigaRateoAnno.setRigaContabile(null);
                    try {
                        rigaRateoAnno = panjeaDAO.save(rigaRateoAnno);
                    } catch (DAOException e) {
                        LOGGER.error("-->errore nel settare a null la rigaContabileRateo collegata", e);
                        throw new RuntimeException(e);
                    }
                    try {
                        panjeaDAO.delete(rigaContabileCollegata);
                    } catch (DAOException e) {
                        LOGGER.error("-->errore nel cancellare la riga Rateo collegata", e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        if (cancellaRighe) {
            rigaContabile.getRigheRateoRisconto().clear();
        }
        return rigaContabile;
    }

    @Override
    public RigaContabile cancellaRiferimentiRateoRisconto(RigaContabile rigaContabile) {
        rigaContabile = panjeaDAO.getEntityManager().merge(rigaContabile);

        cancellaRigaContabileImportoRateoRisconto(rigaContabile);

        if (!rigaContabile.getRigheRateoRisconto().isEmpty() && rigaContabile.getRigheRateoRisconto().get(0)
                .getRateiRiscontiAnno().get(0) instanceof RigaRateoAnno) {
            return cancellaRiferimentiRateo(rigaContabile);
        }
        return cancellaRiferimentiRisconto(rigaContabile);
    }

    private RigaContabile cancellaRiferimentiRisconto(RigaContabile rigaContabile) {
        LOGGER.debug("--> Enter cancellaRiferimentiRateo");
        // Se ho già creato ratei/risconti per l'anno successivo esco

        boolean cancellaRighe = false;

        for (RigaRateoRisconto rigaRateoRisconto : rigaContabile.getRigheRateoRisconto()) {
            List<RigaRiscontoAnno> righeRateoRiscontiAnno = rigaRateoRisconto.getRateiRiscontiAnno();
            if (righeRateoRiscontiAnno.size() > 1) { // teoricamente sempre
                if (righeRateoRiscontiAnno.get(1).getRigaContabile() == null) {
                    cancellaRighe = true;
                    // cancello la riga contabile collegata
                    RigaRiscontoAnno rigaRateoAnno = righeRateoRiscontiAnno.get(0);
                    RigaContabileRateiRisconti rigaContabileCollegata = rigaRateoAnno.getRigaContabile();
                    if (rigaContabileCollegata != null) {
                        rigaRateoAnno.setRigaContabile(null);
                        try {
                            rigaRateoAnno = panjeaDAO.save(rigaRateoAnno);
                        } catch (DAOException e) {
                            LOGGER.error("-->errore nel settare a null la rigaContabileRateo collegata", e);
                            throw new RuntimeException(e);
                        }
                        try {
                            panjeaDAO.delete(rigaContabileCollegata);
                        } catch (DAOException e) {
                            LOGGER.error("-->errore nel cancellare la riga Rateo collegata", e);
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        }
        if (cancellaRighe) {
            rigaContabile.getRigheRateoRisconto().clear();
        }
        LOGGER.debug("--> Exit cancellaRiferimentiRateo");
        return rigaContabile;
    }

    private void cancellaRigaContabileImportoRateoRisconto(RigaContabile rigaContabile) {
        try {
            Query query = panjeaDAO.prepareQuery(
                    "delete from RigaContabile r where r.areaContabile.id=:idAreaContabile and r.conto.id=:idSottoConto and r.id !=:idRiga and r.automatica = true");
            query.setParameter("idAreaContabile", rigaContabile.getAreaContabile().getId());
            query.setParameter("idSottoConto", rigaContabile.getConto().getId());
            query.setParameter("idRiga", rigaContabile.getId());
            panjeaDAO.executeQuery(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione della riga contabile", e);
            throw new RuntimeException("errore durante la cancellazione della riga contabile", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Documento> caricaDocumentiCollegatiRisconto(RigaContabileRateiRisconti rigaContabile) {
        LOGGER.debug("--> Enter caricaDocumentiCollegatiRisconto");
        StringBuilder sb = new StringBuilder(500);
        sb.append("select distinct ");
        sb.append("          doc.id as id, ");
        sb.append("          doc.dataDocumento as dataDocumento, ");
        sb.append("          doc.codice as codice, ");
        sb.append("          tipoDoc.codice as codiceTipoDocumento ");
        sb.append(
                "from RigaRiscontoAnno rrra inner join rrra.rigaRateoRisconto rrr left join rrr.rigaContabile rc left join rc.areaContabile ac ");
        sb.append(
                "                                                       left join ac.documento doc left join doc.tipoDocumento tipoDoc ");
        sb.append("where rrra.rigaContabile=:rigaContabile");
        Query query = panjeaDAO.prepareQuery(sb.toString(), Documento.class, null);
        query.setParameter("rigaContabile", rigaContabile);
        List<Documento> documenti = new ArrayList<>();
        try {
            documenti = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare i documenti collegati al risconto", e);
            throw new RuntimeException(e);
        }
        LOGGER.debug("--> Exit caricaDocumentiCollegatiRisconto");
        return documenti;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaElencoRiscontiDTO> caricaElencoRisconti(int anno, Class<? extends RigaRiscontoAnno> clazz) {

        StringBuilder sb = new StringBuilder(500);
        sb.append("select rrr.id as idRateoRisconto, ");
        sb.append("          rrr.importo as importo, ");
        sb.append("          rrr.inizio as inizio, ");
        sb.append("          rrr.fine as fine, ");
        sb.append("          rrr.nota as nota, ");
        sb.append("          rrra.anno as anno, ");
        sb.append("          rrra.importo as importoAnno, ");
        sb.append("          rrra.giorni as giorniAnno, ");
        sb.append("          rrra.importoSuccessivo as importoSuccessivoAnno, ");
        sb.append("          rrra.giorniSuccessivo as giorniSuccessivoAnno, ");
        sb.append("          doc.id as idDocumentoAnno, ");
        sb.append("          doc.dataDocumento as dataDocumentoAnno, ");
        sb.append("          doc.codice as codiceDocumentoAnno, ");
        sb.append("          tipoDoc.codice as tipoDocumentoCodiceAnno ");
        sb.append(
                "from RigaRiscontoAnno rrra inner join rrra.rigaRateoRisconto rrr left join rrra.rigaContabile rc left join rc.areaContabile ac ");
        sb.append(
                "                                                       left join ac.documento doc left join doc.tipoDocumento tipoDoc ");
        sb.append("where rrra.anno = :anno ");
        if (clazz != null) {
            sb.append("and rrra.class = ");
            sb.append(clazz.getName());
        }
        sb.append(" and rrr.rigaContabile.areaContabile.statoAreaContabile in (:statiArea) ");

        Query query = panjeaDAO.prepareQuery(sb.toString(), RigaElencoRiscontiDTO.class, null);
        query.setParameter("anno", anno);
        query.setParameter("statiArea", Arrays.asList(StatoAreaContabile.CONFERMATO, StatoAreaContabile.VERIFICATO));

        List<RigaElencoRiscontiDTO> elenco = new ArrayList<RigaElencoRiscontiDTO>();

        try {
            elenco = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento dell'elenco risconti per l'anno " + anno, e);
            throw new RuntimeException("Errore durante il caricamento dell'elenco risconti per l'anno " + anno, e);
        }

        return elenco;
    }

    @Override
    public RigaRateoRisconto caricaRigaRateoRisconto(Integer idRigaRateoRisconto) {
        LOGGER.debug("--> Enter caricaRigaRateoRisconto");

        RigaRateoRisconto rigaRateoRisconto = null;
        try {
            rigaRateoRisconto = panjeaDAO.load(RigaRateoRisconto.class, idRigaRateoRisconto);
            for (RigaRiscontoAnno rigaRiscontoAnno : rigaRateoRisconto.getRateiRiscontiAnno()) {
                if (rigaRiscontoAnno.getRigaContabile() != null) {
                    rigaRiscontoAnno.getRigaContabile().getAreaContabile().getDocumento().getId();
                }
            }
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento della riga rateo risconto.", e);
            throw new RuntimeException("errore durante il caricamento della riga rateo risconto.", e);
        }

        LOGGER.debug("--> Exit caricaRigaRateoRisconto");
        return rigaRateoRisconto;
    }

    @Override
    public RigaContabile salvaRigaContabileRateoRisconto(RigaContabile rigaContabile)
            throws ContabilitaException, ContiBaseException, TipoDocumentoBaseException {
        LOGGER.debug("--> Enter salvaRigaContabileRateoRisconto");
        if (!rigaContabile.getRigheRateoRisconto().isEmpty()) {

            if (rigaContabile.getRigheRateoRisconto().get(0).getRateiRiscontiAnno().get(0) instanceof RigaRateoAnno) {
                rigaContabile = aggiornaRigaContabileRateoCollegata(rigaContabile,
                        rigaContabile.getImportoSuccessivoFromRateoPrimoAnno());
            } else {
                rigaContabile = aggiornaRigaContabileRiscontoCollegata(rigaContabile,
                        rigaContabile.getImportoSuccessivoFromRateoPrimoAnno());
            }
            aggiornaRigaContabileImportoRateoRisconto(rigaContabile);
        }

        LOGGER.debug("--> Exit salvaRigaContabileRateoRisconto");
        return rigaContabile;
    }
}
