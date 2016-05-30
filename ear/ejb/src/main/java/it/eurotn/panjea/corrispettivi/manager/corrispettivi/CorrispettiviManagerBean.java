package it.eurotn.panjea.corrispettivi.manager.corrispettivi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.contabilita.domain.CodiceIvaCorrispettivo;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.corrispettivi.domain.CalendarioCorrispettivo;
import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.corrispettivi.domain.GiornoCorrispettivo;
import it.eurotn.panjea.corrispettivi.domain.RigaCorrispettivo;
import it.eurotn.panjea.corrispettivi.manager.corrispettivi.interfaces.CorrispettiviManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;

@Stateless(name = "Panjea.CorrispettiviManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CorrispettiviManager")
public class CorrispettiviManagerBean extends CrudManagerBean<Corrispettivo>implements CorrispettiviManager {

    private static final Logger LOGGER = Logger.getLogger(CorrispettiviManagerBean.class);

    @EJB
    private ContabilitaSettingsManager contabilitaSettingsManager;

    @Override
    public void aggiornaCodiceIva(Corrispettivo corrispettivo, TipoDocumento tipoDocumento) {
        ArrayList<RigaCorrispettivo> listRigheNuoveCorrispettivo = new ArrayList<RigaCorrispettivo>();
        List<CodiceIvaCorrispettivo> listCodiceIvaCorrispettivo = contabilitaSettingsManager
                .caricaCodiciIvaCorrispettivo(tipoDocumento);
        for (CodiceIvaCorrispettivo codiceIvaCorrispettivo : listCodiceIvaCorrispettivo) {
            boolean codiceIvaPresente = false;
            for (RigaCorrispettivo rigaCorrispettivo : corrispettivo.getRigheCorrispettivo()) {
                if (rigaCorrispettivo.getCodiceIva().equals(codiceIvaCorrispettivo.getCodiceIva())) {
                    codiceIvaPresente = true;
                    break;
                }
            }
            if (!codiceIvaPresente) {
                RigaCorrispettivo rigaCorrispettivo = new RigaCorrispettivo();
                rigaCorrispettivo.setCodiceIva(codiceIvaCorrispettivo.getCodiceIva());
                rigaCorrispettivo.setImporto(BigDecimal.ZERO);
                listRigheNuoveCorrispettivo.add(rigaCorrispettivo);
            }
        }
        if (!listRigheNuoveCorrispettivo.isEmpty()) {
            corrispettivo.getRigheCorrispettivo().addAll(listRigheNuoveCorrispettivo);
            salva(corrispettivo);
        }
    }

    @Override
    public void cancellaCorrispettivi(CalendarioCorrispettivo calendarioCorrispettivo) {
        LOGGER.debug("--> Enter cancellaCorrispettivi");

        for (GiornoCorrispettivo giornoCorrispettivo : calendarioCorrispettivo.getGiorniCorrispettivo()) {
            cancella(giornoCorrispettivo.getCorrispettivo());
        }
        LOGGER.debug("--> Exit cancellaCorrispettivi");
    }

    @Override
    public void cancellaCorrispettivo(Date data) {
        LOGGER.debug("--> Enter cancellaCorrispettivo");

        StringBuilder sb = new StringBuilder(100);
        sb.append("select c from Corrispettivo c ");
        sb.append("where c.data = :paramData ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramData", data);

        try {
            @SuppressWarnings("unchecked")
            List<Corrispettivo> corrispettivi = panjeaDAO.getResultList(query);
            for (Corrispettivo corrispettivo : corrispettivi) {
                cancella(corrispettivo);
            }
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione del corrispettivo.", e);
            throw new GenericException("errore durante la cancellazione del corrispettivo.", e);
        }

        LOGGER.debug("--> Exit cancellaCorrispettivo");
    }

    @Override
    public Corrispettivo caricaCorrispettivo(Date data, TipoDocumento tipoDocumento, boolean createNew) {

        Corrispettivo corrispettivo = null;
        // il tipo documento non puo' essere null
        if (tipoDocumento == null) {
            LOGGER.error("--> Tipo documento non puo' essere null");
            throw new IllegalArgumentException("Tipo documento non puo' essere null");
        }

        try {
            LOGGER.debug("--> Enter caricaCorrispettivo");

            Query query = panjeaDAO.prepareNamedQuery("Corrispettivo.caricaCorrispettivo");
            query.setParameter("paramCodiceAzienda", getCodiceAzienda());
            query.setParameter("paramData", data);
            query.setParameter("paramTipoDocumentoId", tipoDocumento.getId());

            @SuppressWarnings("unchecked")
            List<Corrispettivo> corrispettivi = panjeaDAO.getResultList(query);
            if (corrispettivi.size() == 1) {
                corrispettivo = corrispettivi.get(0);
            }
            if (corrispettivi.size() == 0 && createNew) {
                corrispettivo = creaCorrispettivo(data, tipoDocumento);
            } else if (corrispettivi.size() > 1) {
                for (Corrispettivo corrispettivoDaValutare : corrispettivi) {
                    if (corrispettivoDaValutare.getRigheCorrispettivo().get(0).getImporto() == null) {
                        panjeaDAO.delete(corrispettivoDaValutare);
                    } else {
                        corrispettivo = corrispettivoDaValutare;
                    }
                }
            }

            LOGGER.debug("--> Exit caricaCorrispettivo");
        } catch (ObjectNotFoundException ex) {
            LOGGER.warn("--> Non e' stato trovato nessun corrispettivo nella data " + data + " con tipo documento "
                    + tipoDocumento.getCodice() + ". Ne creo uno nuovo.");
            if (createNew) {
                corrispettivo = creaCorrispettivo(data, tipoDocumento);
            }
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento del corrispettivo", e);
            throw new GenericException("errore durante il caricamento del corrispettivo", e);
        }
        return corrispettivo;
    }

    /**
     * Crea un nuovo corrispettivo.
     *
     * @param date
     *            data del corrispettivo
     * @param tipoDocumento
     *            tipoDocumento del corrispettivo
     * @return nuovo corrispettivo
     * @throws CorrispettivoPresenteException
     *             CorrispettivoPresenteException
     */
    private Corrispettivo creaCorrispettivo(Date date, TipoDocumento tipoDocumento) {

        Corrispettivo corrispettivo = new Corrispettivo();
        corrispettivo.setCodiceAzienda(getCodiceAzienda());
        corrispettivo.setData(date);
        corrispettivo.setTipoDocumento(tipoDocumento);
        corrispettivo.setRigheCorrispettivo(creaRigheCorrispettivoContabilita(tipoDocumento));
        corrispettivo = salva(corrispettivo);

        return corrispettivo;
    }

    /**
     * Crea le righe del corrispettivo in base ai codici iva definiti per il tipo documento.
     *
     * @param tipoDocumento
     *            tipoDocumento definito
     * @return righe del corrispettivo.
     */
    private List<RigaCorrispettivo> creaRigheCorrispettivoContabilita(TipoDocumento tipoDocumento) {

        List<RigaCorrispettivo> listRigheCorrispettivo = new ArrayList<RigaCorrispettivo>();
        List<CodiceIvaCorrispettivo> listCodiceIvaCorrispettivo = contabilitaSettingsManager
                .caricaCodiciIvaCorrispettivo(tipoDocumento);

        for (CodiceIvaCorrispettivo codiceIvaCorrispettivo : listCodiceIvaCorrispettivo) {
            RigaCorrispettivo rigaCorrispettivo = new RigaCorrispettivo();
            rigaCorrispettivo.setCodiceIva(codiceIvaCorrispettivo.getCodiceIva());

            listRigheCorrispettivo.add(rigaCorrispettivo);
        }

        return listRigheCorrispettivo;
    }

    @Override
    protected Class<Corrispettivo> getManagedClass() {
        return Corrispettivo.class;
    }

    @Override
    public Corrispettivo salva(Corrispettivo corrispettivo) {
        LOGGER.debug("--> Enter salvaCorrispettivo");

        Corrispettivo corrispettivoEsistente = caricaCorrispettivo(corrispettivo.getData(),
                corrispettivo.getTipoDocumento(), false);

        // se esiste e voglio salvare un nuovo giornale associo id e version al nuovo giornale in modo da aggiornare
        // il record esistente e non salvarne uno nuovo che causerebbe errore (NonUniqueResultException)
        if (corrispettivoEsistente != null && corrispettivoEsistente.getId() != null && corrispettivo.getId() == null) {
            corrispettivo.setId(corrispettivoEsistente.getId());
            corrispettivo.setVersion(corrispettivoEsistente.getVersion());
        }

        Corrispettivo corrispettivoSalvato = null;
        try {
            corrispettivoSalvato = panjeaDAO.save(corrispettivo);
        } catch (Exception ex) {
            LOGGER.error("--> Errore durante il salvataggio del corrispettivo.", ex);
            throw new GenericException("Errore durante il salvataggio del corrispettivo.", ex);
        }
        LOGGER.debug("--> Exit salvaCorrispettivo");
        return corrispettivoSalvato;
    }

}