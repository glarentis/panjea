package it.eurotn.panjea.vending.manager.evadts.importazioni;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.InstallazioniManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.EvaDtsImportFolder.EvaDtsFieldIDContent;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.manager.arearifornimento.interfaces.AreaRifornimentoAggiornaManager;
import it.eurotn.panjea.vending.manager.evadts.exception.ImportazioneEvaDtsException;
import it.eurotn.panjea.vending.manager.evadts.importazioni.interfaces.ImportazioniEvaDtsRifornimentoManager;
import it.eurotn.panjea.vending.manager.vendingsettings.interfaces.VendingSettingsManager;

@Stateless(name = "Panjea.ImportazioniEvaDtsRifornimentoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ImportazioniEvaDtsRifornimentoManager")
public class ImportazioniEvaDtsRifornimentoManagerBean implements ImportazioniEvaDtsRifornimentoManager {

    private static final Logger LOGGER = Logger.getLogger(ImportazioniEvaDtsRifornimentoManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private InstallazioniManager installazioniManager;

    @EJB
    private AreaRifornimentoAggiornaManager areaRifornimentoAggiornaManager;

    @EJB
    private VendingSettingsManager vendingSettingsManager;

    @EJB
    @IgnoreDependency
    private AreaMagazzinoManager areaMagazzinoManager;

    @SuppressWarnings("unchecked")
    private List<AreaRifornimento> caricaAreaRifornimento(Installazione installazione, Date data,
            Map<ImportazioniQueryType, Query> queries) throws ImportazioneEvaDtsException {
        Query query = queries.get(ImportazioniQueryType.RIF_INSTALLAZIONE_E_DATA);
        query.setParameter("paramData", data);
        query.setParameter("paramIdInstallazione", installazione.getId());

        List<AreaRifornimento> rifornimenti;
        try {
            rifornimenti = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento dei rifornimenti per data e installazione.", e);
            throw new ImportazioneEvaDtsException("Errore durante il caricamento dei rifornimenti", e);
        }
        return rifornimenti;
    }

    private AreaRifornimento caricaAreaRifornimento(RilevazioneEvaDts rilevazioneEvaDts, Installazione installazione,
            Distributore distributore, Map<ImportazioniQueryType, Query> queries,
            List<AreaRifornimento> rifornimentiGiaAssegnati) throws ImportazioneEvaDtsException {

        // carico i rifornimenti in base a installazione e data
        List<AreaRifornimento> rifornimenti = caricaAreaRifornimento(installazione, rilevazioneEvaDts.getEa302(),
                queries);
        rifornimenti.removeAll(rifornimentiGiaAssegnati);
        if (rifornimenti.size() == 1) {
            return rifornimenti.get(0);
        } else if (rifornimenti.isEmpty()) {
            return creaRifornimento(rilevazioneEvaDts, installazione, distributore);
        }

        // carico i rifornimenti con incasso simile (+/- 2€) ordinati per ora
        BigDecimal valoreIncassato = rilevazioneEvaDts.getCa302().add(
                ObjectUtils.defaultIfNull(rilevazioneEvaDts.getCa309(), BigDecimal.ZERO).compareTo(BigDecimal.ZERO) != 0
                        ? rilevazioneEvaDts.getCa309() : rilevazioneEvaDts.getCa304());
        rifornimenti = caricaAreaRifornimentoIncassoSimile(installazione, rilevazioneEvaDts.getEa302(), valoreIncassato,
                queries);
        rifornimenti.removeAll(rifornimentiGiaAssegnati);
        if (rifornimenti.size() == 1) {
            return rifornimenti.get(0);
        } else if (rifornimenti.size() > 1) {
            return getRifornimentoConOraPiuVicina(rifornimenti, rilevazioneEvaDts.getEa302());
        }

        // carico i rifornimenti senza incasso con l'orario simile (+/- 20 minuti) ordinati per orario
        rifornimenti = caricaAreaRifornimentoNoIncassoOraSimile(installazione, rilevazioneEvaDts.getEa302(), queries);
        rifornimenti.removeAll(rifornimentiGiaAssegnati);
        if (!rifornimenti.isEmpty()) {
            // prendo quello fatto per primo, essendo ordinati per data asc prendo il primo
            return rifornimenti.get(0);
        } else {
            return creaRifornimento(rilevazioneEvaDts, installazione, distributore);
        }
    }

    @SuppressWarnings("unchecked")
    private List<AreaRifornimento> caricaAreaRifornimentoIncassoSimile(Installazione installazione, Date data,
            BigDecimal incasso, Map<ImportazioniQueryType, Query> queries) throws ImportazioneEvaDtsException {
        Query query = queries.get(ImportazioniQueryType.RIF_INCASSO_SIMILE);
        query.setParameter("paramData", data);
        query.setParameter("paramIdInstallazione", installazione.getId());
        query.setParameter("paramIncassoMin", incasso.subtract(new BigDecimal(2)));
        query.setParameter("paramIncassoMax", incasso.add(new BigDecimal(2)));

        List<AreaRifornimento> rifornimenti;
        try {
            rifornimenti = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento dei rifornimenti con incasso simile.", e);
            throw new ImportazioneEvaDtsException("Errore durante il caricamento dei rifornimenti", e);
        }
        return rifornimenti;
    }

    @SuppressWarnings("unchecked")
    private List<AreaRifornimento> caricaAreaRifornimentoNoIncassoOraSimile(Installazione installazione, Date data,
            Map<ImportazioniQueryType, Query> queries) throws ImportazioneEvaDtsException {
        Query query = queries.get(ImportazioniQueryType.RIF_SENZA_INCASSO);
        query.setParameter("paramIdInstallazione", installazione.getId());
        Calendar calendarMin = Calendar.getInstance();
        calendarMin.setTime(data);
        calendarMin.add(Calendar.HOUR_OF_DAY, -2);
        query.setParameter("paramDataMin", calendarMin.getTime());
        Calendar calendarMax = Calendar.getInstance();
        calendarMax.setTime(data);
        calendarMax.add(Calendar.HOUR_OF_DAY, 2);
        query.setParameter("paramDataMax", calendarMax.getTime());

        List<AreaRifornimento> rifornimenti;
        try {
            rifornimenti = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento dei rifornimenti con incasso simile.", e);
            throw new ImportazioneEvaDtsException("Errore durante il caricamento dei rifornimenti", e);
        }
        return rifornimenti;
    }

    private Distributore caricaDistributore(String identificativo, EvaDtsFieldIDContent fieldIDContent,
            Map<ImportazioniQueryType, Query> queries) throws ImportazioneEvaDtsException {
        Distributore distributore;

        Query query;
        switch (fieldIDContent) {
        case IDEVADTS:
            query = queries.get(ImportazioniQueryType.DISTRIBUTORE_BY_IDEVADTS);
            query.setParameter("paramIdEvaDts", identificativo);
            try {
                distributore = (Distributore) panjeaDAO.getSingleResult(query);
            } catch (Exception e) {
                LOGGER.error("--> Errore durante il caricamento del distributore per l'EVA DTS " + identificativo, e);
                throw new ImportazioneEvaDtsException(
                        "Errore durante il caricamento del distributore per l'ID EVA DTS " + identificativo, e);
            }
            break;
        case MATRICOLA:
            query = queries.get(ImportazioniQueryType.DISTRIBUTORE_BY_CODICE);
            query.setParameter("paramCodice", identificativo);
            try {
                distributore = (Distributore) panjeaDAO.getSingleResult(query);
            } catch (Exception e) {
                LOGGER.error("--> Errore durante il caricamento del distributore per il codice " + identificativo, e);
                throw new ImportazioneEvaDtsException(
                        "Errore durante il caricamento del distributore per il codice " + identificativo, e);
            }
            break;
        case CODICE_INSTALLAZIONE:
            Installazione installazione = installazioniManager.caricaByCodice(identificativo);
            if (installazione == null) {
                LOGGER.error("--> Errore durante il caricamento del distributore per l'installazione con codice "
                        + identificativo);
                throw new ImportazioneEvaDtsException(
                        "Errore durante il caricamento del distributore per l'installazione con codice "
                                + identificativo);
            }
            distributore = (Distributore) installazione.getArticolo();
            break;
        default:
            distributore = null;
            break;
        }

        return distributore;
    }

    private Installazione caricaInstallazione(Distributore distributore, String identificativo,
            EvaDtsFieldIDContent fieldIDContent, Map<ImportazioniQueryType, Query> queries)
                    throws ImportazioneEvaDtsException {
        Installazione installazione;

        switch (fieldIDContent) {
        case IDEVADTS:
        case MATRICOLA:
            try {
                installazione = installazioniManager.caricaByArticolo(distributore.getId());
            } catch (Exception e) {
                LOGGER.error("--> Errore durante il caricamento dell'installazione per il distributore.", e);
                throw new ImportazioneEvaDtsException(
                        "Errore durante il caricamento dell'installazione per il distributore.", e);
            }

            // se non trovo l'installazione significa che il distributore è stato ritirato quindi cerco l'installazione
            // dell'ultimo movimento di ritiro
            if (installazione == null) {
                Query query = queries.get(ImportazioniQueryType.INSTALLAZIONE_ULTIMO_RITIRO);
                query.setParameter("paramIdDistributore", distributore.getId());
                try {
                    installazione = (Installazione) panjeaDAO.getSingleResult(query);
                } catch (Exception e) {
                    LOGGER.error(
                            "--> Errore durante il caricamento dell'installazione dall''ultimo ritiro per il distributore "
                                    + distributore.getCodice(),
                            e);
                    throw new ImportazioneEvaDtsException(
                            "Errore durante il caricamento dell'installazione dall''ultimo ritiro per il distributore "
                                    + distributore.getCodice(),
                            e);
                }
            }
            break;
        case CODICE_INSTALLAZIONE:
            installazione = installazioniManager.caricaByCodice(identificativo);
            break;
        default:
            installazione = null;
            break;
        }

        return installazione;
    }

    private AreaRifornimento creaRifornimento(RilevazioneEvaDts rilevazioneEvaDts, Installazione installazione,
            Distributore distributore) {

        AreaRifornimento areaRifornimento = new AreaRifornimento();
        areaRifornimento.getDatiVendingArea().getBattute();
        areaRifornimento.getDatiVendingArea().getLettureContatore();

        TipoAreaMagazzino tamImportazione = vendingSettingsManager.caricaVendingSettings()
                .getEvadtsTipoDocumentoImportazione();

        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setTipoAreaMagazzino(tamImportazione);
        areaMagazzino.setDataRegistrazione(rilevazioneEvaDts.getEa302());
        areaMagazzino.getDocumento().setDataDocumento(rilevazioneEvaDts.getEa302());
        areaMagazzino.setAnnoMovimento(DateUtils.toCalendar(rilevazioneEvaDts.getEa302()).get(Calendar.YEAR));
        areaRifornimento.setAreaMagazzino(areaMagazzino);

        areaRifornimento = areaRifornimentoAggiornaManager.aggiornaDatiInstallazione(areaRifornimento, installazione);
        areaMagazzino = areaMagazzinoManager.aggiornaDatiSede(areaRifornimento.getAreaMagazzino(),
                areaRifornimento.getDocumento().getSedeEntita());
        areaRifornimento.setAreaMagazzino(areaMagazzino);

        if (installazione.getArticolo() == null || installazione.getArticolo().isNew()) {
            areaRifornimento.setDistributore(distributore);
        }
        return areaRifornimento;
    }

    @Override
    public AreaRifornimento getAreaRifornimento(RilevazioneEvaDts rilevazioneEvaDts,
            EvaDtsFieldIDContent fieldIDContent, Map<ImportazioniQueryType, Query> queries,
            List<AreaRifornimento> rifornimentiGiaAssegnati) throws ImportazioneEvaDtsException {
        LOGGER.debug("--> Enter getAreaRifornimento");

        Distributore distributore = caricaDistributore(rilevazioneEvaDts.getIdentificativo(), fieldIDContent, queries);
        Installazione installazione = caricaInstallazione(distributore, rilevazioneEvaDts.getIdentificativo(),
                fieldIDContent, queries);

        // se l'installazione non ha articolo significa che c'è stato un ritiro quindi creo il rifornimento
        if (installazione.getArticolo() == null) {
            return creaRifornimento(rilevazioneEvaDts, installazione, distributore);
        }

        LOGGER.debug("--> Exit getAreaRifornimento");
        return caricaAreaRifornimento(rilevazioneEvaDts, installazione, distributore, queries,
                rifornimentiGiaAssegnati);
    }

    private AreaRifornimento getRifornimentoConOraPiuVicina(List<AreaRifornimento> rifornimenti, Date data) {
        long timeRif = data.getTime();
        AreaRifornimento areaVicina = null;

        long diff = timeRif;
        for (AreaRifornimento area : rifornimenti) {
            long timeArea = area.getDatiVendingArea().getDataInizioIntervento().getTime();
            if (Math.abs(timeRif - timeArea) < diff) {
                diff = Math.abs(timeRif - timeArea);
                areaVicina = area;
            }
        }

        return areaVicina;
    }

}
