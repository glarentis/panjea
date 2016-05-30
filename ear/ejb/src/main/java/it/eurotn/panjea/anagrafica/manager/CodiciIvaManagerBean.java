package it.eurotn.panjea.anagrafica.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.CodiceIvaRicorsivoException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.manager.interfaces.CodiciIvaManager;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;

@Stateless(name = "Panjea.CodiciIvaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CodiciIvaManager")
public class CodiciIvaManagerBean extends CrudManagerBean<CodiceIva>implements CodiciIvaManager {
    private static final Logger LOGGER = Logger.getLogger(CodiciIvaManagerBean.class);

    @Override
    public CodiceIva caricaById(Integer id) {
        LOGGER.debug("-->Enter caricaCodiceIva con id: " + id);
        CodiceIva codiceIva = null;
        try {
            codiceIva = panjeaDAO.load(CodiceIva.class, id);
        } catch (Exception e) {
            LOGGER.error("--> Errore nella carica codice pagamento " + id, e);
            throw new RuntimeException("--> Errore nella carica codice pagamento " + id, e);
        }
        // chiamo la getCodcieIvaSostituzioneVentilazione per inizializzare il
        // riferimento che e' lazy
        if (codiceIva.getCodiceIvaSostituzioneVentilazione() != null) {
            codiceIva.getCodiceIvaSostituzioneVentilazione().getId();
        }
        if (codiceIva.getCodiceIvaCollegato() != null) {
            codiceIva.getCodiceIvaCollegato().getId();
        }
        if (codiceIva.getCodiceIvaSplitPayment() != null) {
            codiceIva.getCodiceIvaSplitPayment().getId();
        }

        LOGGER.debug("-->codice iva caricato " + codiceIva);
        return codiceIva;
    }

    @Override
    public CodiceIva caricaCodiceIva(String codiceEuropa) throws ObjectNotFoundException {
        LOGGER.debug("--> Enter caricaCodiceIva");

        CodiceIva codiceIva = null;

        Query query = panjeaDAO.prepareNamedQuery("CodiceIva.caricaByCodiceEUROPA");
        query.setParameter("codiceAzienda", getCodiceAzienda());
        query.setParameter("paramCodiceEuropa", codiceEuropa);
        try {
            codiceIva = (CodiceIva) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            LOGGER.debug("--> codice iva non trovato");
            throw e;
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento del codice iva", e);
            throw new RuntimeException("--> Errore durante il caricamento del codice iva", e);
        }

        LOGGER.debug("--> Exit caricaCodiceIva");
        return codiceIva;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CodiceIva> caricaCodiciIva(String codice) {
        LOGGER.debug("--> Enter caricaCodiciIva con codice " + codice);
        List<CodiceIva> listCodiciIva = null;
        if (codice == null) {
            codice = "%";
        }
        Query query = panjeaDAO.prepareNamedQuery("CodiceIva.caricaByCodice");
        query.setParameter("codiceAzienda", getCodiceAzienda());
        query.setParameter("paramCodice", codice);
        try {
            listCodiciIva = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> Errore impossibile recuperare la list di CodiceIva", e);
            throw new RuntimeException("--> Errore impossibile recuperare la list di CodiceIva", e);
        }
        LOGGER.debug("--> Numero risultati query : " + listCodiciIva.size());
        return listCodiciIva;
    }

    @Override
    public Map<Double, CodiceIva> caricaCodiciIvaPalmare() {
        LOGGER.debug("--> Enter caricaCodiciIvaPalmare");
        List<CodiceIva> codiciIva = caricaAll();
        Map<Double, CodiceIva> result = new HashMap<>();
        for (CodiceIva codiceIva : codiciIva) {
            if (codiceIva.isPalmareAbilitato()) {
                result.put(codiceIva.getPercApplicazione().doubleValue(), codiceIva);
            }
        }
        LOGGER.debug("--> Exit caricaCodiciIvaPalmare");
        return result;
    }

    /**
     * lancia una runtimeException se il check trova un codice iva uguale al codiceiva di origine.
     *
     * @param codiceIvaSostituto
     *            codiceiva sostituito
     * @param codiceIvaOrigine
     *            codice iva di origine
     * @throws CodiceIvaRicorsivoException
     *             rilanciata quando ho dei codici sostitutuvi legati tra di loro
     */
    private void checkCodiceSostitutivo(CodiceIva codiceIvaSostituto, CodiceIva codiceIvaOrigine)
            throws CodiceIvaRicorsivoException {
        LOGGER.debug("--> Enter checkCodiceSostitutivo " + codiceIvaSostituto + " del codice iva " + codiceIvaOrigine);
        if (codiceIvaSostituto != null) {
            LOGGER.debug("--> codice iva sostituto " + codiceIvaSostituto.getCodice());
            if (codiceIvaSostituto.getCodice().equals(codiceIvaOrigine.getCodice())) {

                CodiceIvaRicorsivoException exception = new CodiceIvaRicorsivoException();
                LOGGER.error(exception.getMessage(), exception);
                throw exception;
            } else {
                LOGGER.debug("--> check di " + codiceIvaSostituto.getCodiceIvaSostituzioneVentilazione());
                checkCodiceSostitutivo(codiceIvaSostituto.getCodiceIvaSostituzioneVentilazione(), codiceIvaOrigine);
            }
        } else {
            LOGGER.debug("--> non ho un codiceIva sostituto a quello di origine " + codiceIvaOrigine.getCodice());
            return;
        }
        LOGGER.debug("--> Exit checkCodiceSostitutivo");
    }

    @Override
    protected Class<CodiceIva> getManagedClass() {
        return CodiceIva.class;
    }

    @Override
    public CodiceIva salva(CodiceIva object) {
        throw new UnsupportedOperationException("Utilizzare la salvaCodiceIva");
    }

    @Override
    public CodiceIva salvaCodiceIva(CodiceIva codiceIva) throws CodiceIvaRicorsivoException {
        LOGGER.debug("--> Enter salvaCodiceIva ");
        CodiceIva codiceIvaSalvata = null;
        /* Inizializza il codice azienda in caso di nuova entita */
        if (codiceIva.isNew()) {
            codiceIva.setCodiceAzienda(getCodiceAzienda());
        }

        checkCodiceSostitutivo(codiceIva.getCodiceIvaSostituzioneVentilazione(), codiceIva);

        try {
            codiceIvaSalvata = panjeaDAO.save(codiceIva);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio del codice iva", e);
            throw new RuntimeException("--> Errore durante il salvataggio del codice iva", e);
        }
        // chiamo la getCodcieIvaSostituzioneVentilazione per inizializzare il
        // riferimento che e' lazy
        if (codiceIvaSalvata.getCodiceIvaSostituzioneVentilazione() != null) {
            codiceIvaSalvata.getCodiceIvaSostituzioneVentilazione().getId();
        }
        if (codiceIvaSalvata.getCodiceIvaCollegato() != null) {
            codiceIvaSalvata.getCodiceIvaCollegato().getId();
        }
        if (codiceIvaSalvata.getCodiceIvaSplitPayment() != null) {
            codiceIvaSalvata.getCodiceIvaSplitPayment().getId();
        }

        LOGGER.debug("--> Exit salvaCodiceIva " + codiceIvaSalvata);
        return codiceIvaSalvata;
    }

}
