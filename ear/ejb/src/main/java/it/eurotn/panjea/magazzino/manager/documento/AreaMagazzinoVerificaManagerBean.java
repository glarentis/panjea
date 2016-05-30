package it.eurotn.panjea.magazzino.manager.documento;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoVerificaManager;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteNonAvvisareException;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 *
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(mappedName = "Panjea.AreaMagazzinoVerificaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaMagazzinoVerificaManager")
public class AreaMagazzinoVerificaManagerBean implements AreaMagazzinoVerificaManager {

    private static Logger logger = Logger.getLogger(AreaMagazzinoVerificaManagerBean.class);

    @Resource
    private SessionContext sessionContext;

    /**
     * @uml.property name="panjeaDAO"
     * @uml.associationEnd
     */
    @EJB
    private PanjeaDAO panjeaDAO;

    /**
     * @uml.property name="areaMagazzinoManager"
     * @uml.associationEnd
     */
    @IgnoreDependency
    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    /**
     * @uml.property name="areaRateManager"
     * @uml.associationEnd
     */
    @EJB
    private AreaRateManager areaRateManager;

    @Override
    public boolean checkCambioStato(AreaMagazzino areaMagazzino, AreaRate areaRate) {
        logger.debug("--> Enter checkCambioStato");

        if (areaRate != null && !areaRate.isNew()
                && areaMagazzino.getStatoAreaMagazzino() != StatoAreaMagazzino.PROVVISORIO) {

            AreaRate areaPartiteOld = areaRateManager.caricaAreaRate(areaRate.getId());
            AreaMagazzino areaMagazzinoOld = areaMagazzinoManager.caricaAreaMagazzino(areaMagazzino);

            CodicePagamento codicePagamentoOld = areaPartiteOld.getCodicePagamento();
            CodicePagamento codicePagamentoNew = areaRate.getCodicePagamento();
            boolean perScontoCommCambiata = codicePagamentoOld == null || codicePagamentoOld.isNew()
                    || codicePagamentoNew.getPercentualeScontoCommerciale()
                            .compareTo(codicePagamentoOld.getPercentualeScontoCommerciale()) != 0;

            boolean codicePagamentoCambiato = codicePagamentoOld == null || codicePagamentoOld.isNew()
                    || !codicePagamentoOld.equals(codicePagamentoNew);

            BigDecimal speseIncassoOld = ObjectUtils.defaultIfNull(areaPartiteOld.getSpeseIncasso(), BigDecimal.ZERO);
            BigDecimal speseIncassoNew = ObjectUtils.defaultIfNull(areaRate.getSpeseIncasso(), BigDecimal.ZERO);
            boolean speseIncassoCambiate = speseIncassoNew.compareTo(speseIncassoOld) != 0;

            boolean addebitoSpeseCambiate = areaMagazzino.isAddebitoSpeseIncasso() != areaMagazzinoOld
                    .isAddebitoSpeseIncasso();

            boolean dataDocumentoCambiata = !areaMagazzino.getDocumento().getDataDocumento()
                    .equals(areaMagazzinoOld.getDocumento().getDataDocumento());

            boolean mezzoTrasportoCambiato = false;
            if (areaMagazzino.getMezzoTrasporto() == null && areaMagazzinoOld.getMezzoTrasporto() != null) {
                mezzoTrasportoCambiato = true;
            } else if (areaMagazzino.getMezzoTrasporto() != null && areaMagazzinoOld.getMezzoTrasporto() == null) {
                mezzoTrasportoCambiato = true;
            } else if (areaMagazzino.getMezzoTrasporto() != null && areaMagazzinoOld.getMezzoTrasporto() != null
                    && areaMagazzinoOld.getMezzoTrasporto().equals(areaMagazzino.getMezzoTrasporto())) {
                mezzoTrasportoCambiato = true;
            }

            // controllo che le spese incasso o sconto commerciale dell'area partite non siano
            // cambiate
            if (addebitoSpeseCambiate || speseIncassoCambiate || perScontoCommCambiata || codicePagamentoCambiato
                    || dataDocumentoCambiata || mezzoTrasportoCambiato) {
                return true;
            }
        }

        logger.debug("--> Exit checkCambioStato");
        return false;
    }

    @Override
    public AreaMagazzino checkInvalidaAreaMagazzino(AreaMagazzino areaMagazzino) {
        boolean saveArea = false;
        if (areaMagazzino.getDatiValidazioneRighe().isValid()) {
            // invalido le righe magazzino
            areaMagazzino.getDatiValidazioneRighe().invalida();
            saveArea = true;
        }
        if (areaMagazzino.getStatoAreaMagazzino() != StatoAreaMagazzino.PROVVISORIO) {
            // Cambia stato in provvisiorio salva l'area contabile
            areaMagazzino = areaMagazzinoManager.cambiaStatoInProvvisorio(areaMagazzino);
            saveArea = true;
        }

        if (saveArea) {
            try {
                areaMagazzino = panjeaDAO.save(areaMagazzino);
            } catch (Exception e) {
                logger.error("--> errore nel salvare l'area magazzino durante l'invalidazione", e);
                throw new RuntimeException("--> errore nel salvare l'area magazzino durante l'invalidazione", e);
            }
        }
        return areaMagazzino;
    }

    /**
     * Restituisce JecPrincipal dal {@link SessionContext}.
     *
     * @return JecPrincipal
     */
    private JecPrincipal getJecPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Documento> ricercaDocumentiSenzaAreaMagazzino(Documento documento, boolean soloAttributiNotNull) {
        logger.debug("--> Enter ricercaDocumentiSenzaAreaMagazzino");
        StringBuffer selectString = new StringBuffer(
                "select d from AreaMagazzino a right join a.documento d where d.codiceAzienda = :paramCodiceAzienda and a.id is null ");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("paramCodiceAzienda", getJecPrincipal().getCodiceAzienda());
        // aggiungo la condizione che il tipoDocumento presenti un
        // tipoAreaMagazzino. La condizione viene sviluppata
        // attraverso un SubQuery che recupera tutti i tipoDocumento con
        // TipoAreaMagazzino
        selectString.append(
                " and d.tipoDocumento in (select tipoAreaMagazzino.tipoDocumento from TipoAreaMagazzino tipoAreaMagazzino where tipoAreaMagazzino.tipoDocumento.codiceAzienda = :paramCodiceAziendaTipoDocumento ) ");
        parameters.put("paramCodiceAziendaTipoDocumento", getJecPrincipal().getCodiceAzienda());
        if (!soloAttributiNotNull) {
            // verifica tutti gli attributi interessati di documento ignorando
            // che questi siano a null
            selectString.append(" and d.codice = :paramCodice ");
            parameters.put("paramCodice", documento.getCodice());
            selectString.append(" and d.dataDocumento = :paramDataDocumento ");
            parameters.put("paramDataDocumento", documento.getDataDocumento());
            selectString.append(" and d.tipoDocumento = :paramTipoDocumento ");
            parameters.put("paramTipoDocumento", documento.getTipoDocumento());

            if (TipoEntita.CLIENTE.equals(documento.getTipoDocumento().getTipoEntita())
                    || TipoEntita.FORNITORE.equals(documento.getTipoDocumento().getTipoEntita())) {
                selectString.append(" and d.entita = :paramEntita ");
                parameters.put("paramEntita", documento.getEntita());
            } else {
                selectString.append(" and d.entita is null ");
            }

            if (TipoEntita.BANCA.equals(documento.getTipoDocumento().getTipoEntita())) {
                selectString.append(" and d.rapportoBancarioAzienda = :paramRapportoBancario ");
                parameters.put("paramRapportoBancario", documento.getRapportoBancarioAzienda());
            } else {
                selectString.append(" and d.rapportoBancarioAzienda is null ");
            }
        } else {
            // crea le condizioni di ricerca solo per gli attributi not null
            if (documento.getCodice() != null) {
                selectString.append(" and d.codice = :paramCodice ");
                parameters.put("paramCodice", documento.getCodice());
            }
            if (documento.getDataDocumento() != null) {
                selectString.append(" and d.dataDocumento = :paramDataDocumento ");
                parameters.put("paramDataDocumento", documento.getDataDocumento());
            }
            if (documento.getTipoDocumento() != null) {
                selectString.append(" and d.tipoDocumento = :paramTipoDocumento ");
                parameters.put("paramTipoDocumento", documento.getTipoDocumento());
            }

            if (documento.getEntita() != null) {
                selectString.append(" and d.entita = :paramEntita ");
                parameters.put("paramEntita", documento.getEntita());
            }
            if (documento.getRapportoBancarioAzienda() != null) {
                selectString.append(" and d.rapportoBancarioAzienda = :paramRapportoBancario ");
                parameters.put("paramRapportoBancario", documento.getRapportoBancarioAzienda());
            }

        }
        Query query = panjeaDAO.prepareQuery(selectString.toString());
        for (String parameterName : parameters.keySet()) {
            query.setParameter(parameterName, parameters.get(parameterName));
        }
        List<Documento> documenti = null;
        try {
            documenti = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore DAO in ricercaDocumentiSenzaAreaMagazzino", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit ricercaDocumentiSenzaAreaMagazzino");
        return documenti;
    }

    @Override
    public void verificaAssociazioneConDocumento(AreaMagazzino areaMagazzino)
            throws DocumentoAssenteNonAvvisareException, DocumentoAssenteAvvisaException,
            DocumentoAssenteBloccaException, DocumentiEsistentiPerAreaMagazzinoException {
        logger.debug("--> Enter verificaAssociazioneConDocumento");
        List<Documento> documenti = ricercaDocumentiSenzaAreaMagazzino(areaMagazzino.getDocumento(), true);
        if (documenti.isEmpty()) {
            // valuta l'azione da compiere in base al valore presente in
            // TipoAreaMagazzino se Documento non esiste
            switch (areaMagazzino.getTipoAreaMagazzino().getOperazioneAreaContabileNonTrovata()) {
            case NONAVVISARE:
                logger.debug("--> documento assente: non avvisare ");
                throw new DocumentoAssenteNonAvvisareException("il Documento non esiste");
            case AVVISA:
                logger.debug("--> documento assente: avvisare ");
                throw new DocumentoAssenteAvvisaException("il Documento non esiste");
            case BLOCCA:
                logger.debug("--> documento assente: blocca l'inserimento ");
                throw new DocumentoAssenteBloccaException("il Documento non esiste");
            default:
                break;
            }
        }
        // rimuove dai documenti trovati quello esistente e ne ricontrolla la
        // size
        documenti.remove(areaMagazzino.getDocumento());
        if (!documenti.isEmpty()) {
            // rilanciare un eccezione perchè esistono più documenti con le
            // stesse chiavi di dominio
            throw new DocumentiEsistentiPerAreaMagazzinoException(documenti);
        }
        logger.debug("--> Exit verificaAssociazioneConDocumento");
    }
}
