package it.eurotn.panjea.vending.manager.lettureselezionatrice;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.RigaLetturaSelezionatrice;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces.LettureCassaGenerator;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces.LettureRifornimentoGenerator;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces.LettureSelezionatriceManager;

@Stateless(name = "Panjea.LettureSelezionatriceManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LettureSelezionatriceManager")
public class LettureSelezionatriceManagerBean extends CrudManagerBean<LetturaSelezionatrice>
        implements LettureSelezionatriceManager {

    private static final Logger LOGGER = Logger.getLogger(LettureSelezionatriceManagerBean.class);

    @EJB
    private LettureRifornimentoGenerator lettureRifornimentoGenerator;

    @EJB
    private LettureCassaGenerator lettureCassaGenerator;

    @Override
    public void cancella(Integer id) {
        super.cancella(id);
    }

    @Override
    public void cancella(LetturaSelezionatrice object) {
        super.cancella(object);
    }

    @Override
    public LetturaSelezionatrice caricaById(Integer id) {
        LetturaSelezionatrice lettura = super.caricaById(id);
        Hibernate.initialize(lettura.getRifornimento());
        Hibernate.initialize(lettura.getInstallazione());
        Hibernate.initialize(lettura.getDistributore());
        Hibernate.initialize(lettura.getCaricatore());
        Hibernate.initialize(lettura.getCassaDestinazione());
        Hibernate.initialize(lettura.getCassaOrigine());
        Hibernate.initialize(lettura.getRighe());

        return lettura;
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    public RisultatiChiusuraLettureDTO chiudiLettureSelezionatrice(List<LetturaSelezionatrice> letture) {
        LOGGER.debug("--> Enter chiudiLettureSelezionatrice");

        RisultatiChiusuraLettureDTO risultatiChiusura = new RisultatiChiusuraLettureDTO();

        for (LetturaSelezionatrice lettura : letture) {

            switch (lettura.getStatoLettura()) {
            case CASSA:
            case TRASFERIMENTO_CASSA:
                lettureCassaGenerator.creaMovimentazioneCassa(lettura);
                risultatiChiusura.addLetturaCassaGenerata(lettura);
                break;
            case RIFORNIMENTO_ASSOCIATO:
                try {
                    lettureRifornimentoGenerator.associaRifornimento(lettura);
                    lettureCassaGenerator.creaMovimentazioneCassa(lettura);
                    risultatiChiusura.addLetturaRifornimentoAssociato(lettura);
                } catch (Exception e) {
                    risultatiChiusura.addLetturaRifornimentoNonAssociato(lettura);
                }
                break;
            case RIFORNIMENTO_DA_CREARE:
                try {
                    lettureRifornimentoGenerator.creaRifornimento(lettura);
                    lettureCassaGenerator.creaMovimentazioneCassa(lettura);
                    risultatiChiusura.addLetturaRifornimentoCreato(lettura);
                } catch (Exception e) {
                    risultatiChiusura.addLetturaRifornimentoNonCreato(lettura);
                }
                break;
            case NON_VALIDA:
                risultatiChiusura.addLetturaNonValida(lettura);
                break;
            default:
                LOGGER.debug("--> Lettura non gestita. Stato: " + lettura.getStatoLettura().name());
                break;
            }
        }

        LOGGER.debug("--> Exit ricercaLettureSelezionatrice");
        return risultatiChiusura;
    }

    @Override
    protected Class<LetturaSelezionatrice> getManagedClass() {
        return LetturaSelezionatrice.class;
    }

    private String getRicercaLettureHQL(boolean addFiltroLetturaId) {
        StringBuilder sb = new StringBuilder(200);
        sb.append("select ls.id as id, ");
        sb.append("ls.data as data, ");
        sb.append("ls.dataRifornimento as dataRifornimento, ");
        sb.append("ls.progressivo as progressivo, ");
        sb.append("ls.numeroSacchetto as numeroSacchetto, ");
        sb.append("rif.id as rifornimento$id, ");
        sb.append("rif.version as rifornimento$version, ");
        sb.append("doc.codice as rifornimento$documento$codice, ");
        sb.append("doc.dataDocumento as rifornimento$documento$dataDocumento, ");
        sb.append("tipoDoc.codice as rifornimento$documento$tipoDocumento$codice, ");
        sb.append("inst.id as installazione$id, ");
        sb.append("inst.codice as installazione$codice, ");
        sb.append("inst.descrizione as installazione$descrizione, ");
        sb.append("dist.id as distributore$id, ");
        sb.append("dist.version as distributore$version, ");
        sb.append("dist.codice as distributore$codice, ");
        sb.append("dist.descrizioneLinguaAziendale as distributore$descrizione, ");
        sb.append("car.id as caricatore$id, ");
        sb.append("car.version as caricatore$version, ");
        sb.append("car.codice as caricatore$codice, ");
        sb.append("car.nome as caricatore$nome, ");
        sb.append("car.cognome as caricatore$cognome, ");
        sb.append("cassaOri.id as cassaOrigine$id, ");
        sb.append("cassaOri.version as cassaOrigine$version, ");
        sb.append("cassaOri.codice as cassaOrigine$codice, ");
        sb.append("cassaOri.descrizione as cassaOrigine$descrizione, ");
        sb.append("cassaDest.id as cassaDestinazione$id, ");
        sb.append("cassaDest.version as cassaDestinazione$version, ");
        sb.append("cassaDest.codice as cassaDestinazione$codice, ");
        sb.append("cassaDest.descrizione as cassaDestinazione$descrizione, ");
        sb.append("coalesce(ls.reso,0) as reso, ");
        sb.append("sum(rls.quantita*gett.valore) as importo ");
        sb.append("from LetturaSelezionatrice ls left join ls.righe rls ");
        sb.append("left join ls.rifornimento rif ");
        sb.append("left join rif.documento doc ");
        sb.append("left join doc.tipoDocumento tipoDoc ");
        sb.append("left join ls.installazione inst ");
        sb.append("left join ls.distributore dist ");
        sb.append("left join ls.caricatore car ");
        sb.append("left join ls.cassaOrigine cassaOri ");
        sb.append("left join ls.cassaDestinazione cassaDest ");
        sb.append("left join rls.gettone gett ");
        if (addFiltroLetturaId) {
            sb.append(" where ls.id = :paramLetturaId ");
        }
        sb.append("group by ls.id");

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LetturaSelezionatrice> ricercaLettureSelezionatrice(Integer idLettura) {
        LOGGER.debug("--> Enter ricercaLettureSelezionatrice");
        updateLettureSelezionatriceDati();

        Query query = panjeaDAO.prepareQuery(getRicercaLettureHQL(idLettura != null), LetturaSelezionatrice.class,
                null);
        if (idLettura != null) {
            query.setParameter("paramLetturaId", idLettura);
        }

        List<LetturaSelezionatrice> letture = null;
        try {
            letture = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle letture della selezionatrice.", e);
            throw new GenericException("errore durante il caricamento delle letture della selezionatrice.", e);
        }

        LOGGER.debug("--> Exit ricercaLettureSelezionatrice");
        return letture;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaLetturaSelezionatrice> ricercaRigheLetturaSelezionatrice(Integer progressivo) {
        LOGGER.debug("--> Enter ricercaRigheLetturaSelezionatrice");

        Query query = panjeaDAO.prepareQuery(
                "select riga from RigaLetturaSelezionatrice riga where riga.letturaSelezionatrice.id = :paramProgressivo");
        query.setParameter("paramProgressivo", progressivo);

        List<RigaLetturaSelezionatrice> righe = null;
        try {
            righe = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle righe lettura selezionatrice.", e);
            throw new GenericException("errore durante il caricamento delle righe lettura selezionatrice.", e);
        }

        LOGGER.debug("--> Exit ricercaRigheLetturaSelezionatrice");
        return righe;
    }

    @Override
    public LetturaSelezionatrice salva(LetturaSelezionatrice object) {
        LetturaSelezionatrice lettura = super.salva(object);

        updateLettureSelezionatriceDati();
        Query query = panjeaDAO.prepareQuery(getRicercaLettureHQL(true), LetturaSelezionatrice.class, null);
        query.setParameter("paramLetturaId", lettura.getId());

        try {
            lettura = (LetturaSelezionatrice) panjeaDAO.getSingleResult(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento della lettura della selezionatrice.", e);
            throw new GenericException("errore durante il caricamento della lettura della selezionatrice.", e);
        }

        return lettura;
    }

    /**
     * Aggiorna tutti i dati delle letture.
     */
    private void updateLettureSelezionatriceDati() {
        LOGGER.debug("--> Enter updateLettureSelezionatriceDati");

        // aggiornamento installazioni
        panjeaDAO
                .prepareNativeQuery(
                        "update vend_letture_selezionatrice ls left join manu_installazioni inst on ls.codiceInstallazione = inst.codice set ls.installazione_id = inst.id where ls.installazione_id is null")
                .executeUpdate();

        // aggiornamento distributori
        panjeaDAO
                .prepareNativeQuery(
                        "update vend_letture_selezionatrice ls left join maga_articoli art on art.tipo = 'DI' and ls.codiceDistributore = art.codice set ls.distributore_id = art.id where ls.distributore_id is null")
                .executeUpdate();

        // aggiornamento caricatori
        panjeaDAO
                .prepareNativeQuery(
                        "update vend_letture_selezionatrice ls left join manu_operatori operat on operat.caricatore = true and ls.codiceCaricatore = operat.codice set ls.caricatore_id = operat.id where ls.caricatore_id is null")
                .executeUpdate();

        // agigornamento casse
        panjeaDAO
                .prepareNativeQuery(
                        "update vend_letture_selezionatrice ls left join vend_casse cassa on ls.codiceCassaOrigine = cassa.codice set ls.cassaOrigine_id = cassa.id where ls.cassaOrigine_id is null")
                .executeUpdate();
        panjeaDAO
                .prepareNativeQuery(
                        "update vend_letture_selezionatrice ls left join vend_casse cassa on ls.codiceCassaDestinazione = cassa.codice set ls.cassaDestinazione_id = cassa.id where ls.cassaDestinazione_id is null")
                .executeUpdate();

        // aggiornamento gettoni
        panjeaDAO
                .prepareNativeQuery(
                        "update vend_letture_selezionatrice_righe rls left join vend_gettoni gettone on rls.codiceGettone = gettone.codice set rls.gettone_id = gettone.id")
                .executeUpdate();

        LOGGER.debug("--> Exit updateLettureSelezionatriceDati");
    }

}