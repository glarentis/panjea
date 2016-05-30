package it.eurotn.panjea.magazzino.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.TipiDepositoManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediAziendaManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.manager.interfaces.MezziTrasportoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.MezziTrasportoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MezziTrasportoManager")
public class MezziTrasportoManagerBean implements MezziTrasportoManager {

    private static final Logger LOGGER = Logger.getLogger(MezziTrasportoManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    @EJB
    private SediAziendaManager sediAziendaManager;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private DepositiManager depositiManager;

    @EJB
    private TipiDepositoManager tipiDepositoManager;

    @Override
    public void cancellaMezzoTrasporto(MezzoTrasporto mezzoTrasporto) {
        LOGGER.debug("--> Enter cancellaMezzoTrasporto");

        try {
            panjeaDAO.delete(mezzoTrasporto);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante la cancellazione del mezzo di trasporto " + mezzoTrasporto.getId(), e);
            throw new GenericException(
                    "--> Errore durante la cancellazione del mezzo di trasporto " + mezzoTrasporto.getId(), e);
        }

        LOGGER.debug("--> Exit cancellaMezzoTrasporto");
    }

    @Override
    public void cancellaTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto) {
        LOGGER.debug("--> Enter cancellaTipoMezzoTrasporto");

        try {
            panjeaDAO.delete(tipoMezzoTrasporto);
        } catch (Exception e) {
            LOGGER.error(
                    "--> Errore durante la cancellazione del tipo mezzo di trasporto " + tipoMezzoTrasporto.getId(), e);
            throw new GenericException(
                    "--> Errore durante la cancellazione del tipo mezzo di trasporto " + tipoMezzoTrasporto.getId(), e);
        }

        LOGGER.debug("--> Exit cancellaTipoMezzoTrasporto");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MezzoTrasporto> caricaMezziTrasporto(String targa, boolean abilitato, EntitaLite entita,
            boolean senzaCaricatore) {
        LOGGER.debug("--> Enter caricaMezziTrasporto");
        List<MezzoTrasporto> list = new ArrayList<MezzoTrasporto>();
        StringBuilder sb = new StringBuilder(
                "select mt from MezzoTrasporto mt where mt.codiceAzienda=:paramCodiceAzienda ");
        if (abilitato) {
            sb.append(" and mt.abilitato=true ");
        }
        if (targa != null) {
            sb.append(" and targa like ").append(PanjeaEJBUtil.addQuote(targa));
        }
        if (entita != null) {
            sb.append(" and  (entita=").append(entita.getId()).append(" or entita is null)");
        }
        sb.append(" order by targa");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramCodiceAzienda", getAzienda());
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento dei mezzi di trasporto", e);
            throw new GenericException("Errore durante il caricamento dei mezzi di trasporto", e);
        }
        LOGGER.debug("--> Exit caricaMezziTrasporto");

        if (senzaCaricatore) {
            SQLQuery queryOperatori = panjeaDAO.prepareNativeQuery(
                    "select mezzoTrasporto_id from manu_operatori WHERE mezzoTrasporto_id is not null group by mezzoTrasporto_id");
            List<Integer> mezziOccupatiIds = queryOperatori.list();
            for (Iterator<MezzoTrasporto> iterator = list.iterator(); iterator.hasNext();) {
                MezzoTrasporto mezzoTrasporto = iterator.next();
                if (mezziOccupatiIds.contains(mezzoTrasporto.getId())) {
                    iterator.remove();
                }
            }
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TipoMezzoTrasporto> caricaTipiMezzoTrasporto() {
        LOGGER.debug("--> Enter caricaTipiMezziTrasporto");
        List<TipoMezzoTrasporto> list = new ArrayList<TipoMezzoTrasporto>();
        Query query = panjeaDAO.prepareNamedQuery("TipoMezzoTrasporto.caricaAll");
        query.setParameter("paramCodiceAzienda", getAzienda());
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento dei tipi mezzi di trasporto", e);
            throw new GenericException("Errore durante il caricamento dei tipi mezzi di trasporto", e);
        }
        LOGGER.debug("--> Exit caricaTipiMezziTrasporto");
        return list;
    }

    @Override
    public MezzoTrasporto creaNuovoDepositoMezzoDiTrasporto(MezzoTrasporto mezzoTrasporto, String codiceDeposito,
            String descrizioneDeposito) {
        LOGGER.debug("--> Enter creaNuovoDepositoMezzoDiTrasporto");

        Deposito deposito = new Deposito();
        deposito.setAttivo(true);
        deposito.setCodice(codiceDeposito);
        deposito.setCodiceAzienda(getAzienda());
        deposito.setDescrizione(descrizioneDeposito);
        deposito.setPrincipale(false);
        deposito.setTipoDeposito(
                tipiDepositoManager.caricaOCreaByCodice(mezzoTrasporto.getTipoMezzoTrasporto().getCodice()));

        try {
            Azienda azienda = aziendeManager.caricaAziendaByCodice(getAzienda());
            deposito.setSedeDeposito(sediAziendaManager.caricaSedePrincipaleAzienda(azienda));
            deposito = depositiManager.salvaDeposito(deposito);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio del nuovo deposito per il mezzo di trasporto", e);
            throw new GenericException("errore durante il salvataggio del nuovo deposito per il mezzo di trasporto", e);
        }

        // Per un bug di hibernate se salvo il messo di trasporto per associare il deposito
        // prova ad inserire nell'audit 2 record con stessa idrev. Devo fare un update SQL
        try {
            Query query = panjeaDAO.prepareQuery("update MezzoTrasporto m set m.deposito.id=" + deposito.getId()
                    + " where m.id=" + mezzoTrasporto.getId());
            panjeaDAO.executeQuery(query);
            mezzoTrasporto = panjeaDAO.load(MezzoTrasporto.class, mezzoTrasporto.getId());
        } catch (DAOException e) {
            LOGGER.error("-->errore nell'associare il deposito al mezzo di trasporto", e);
            throw new GenericException("-->errore nell'associare il deposito al mezzo di trasporto", e);
        }

        // mezzoTrasporto = salvaMezzoTrasporto(mezzoTrasporto);
        LOGGER.debug("--> Exit creaNuovoDepositoMezzoDiTrasporto");
        return mezzoTrasporto;
    }

    /**
     * @return codice dell'azienda loggata
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    @Override
    public MezzoTrasporto rimuoviDepositoDaMezzoDiTrasporto(MezzoTrasporto mezzoTrasporto) {
        LOGGER.debug("--> Enter rimuoviDepositoDaMezzoDiTrasporto");

        MezzoTrasporto mezzoLoad = null;
        try {
            mezzoLoad = panjeaDAO.load(MezzoTrasporto.class, mezzoTrasporto.getId());
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento del mezzo di trasporto.", e);
            throw new GenericException("errore durante il caricamento del mezzo di trasporto.", e);
        }

        DepositoLite depositoToDelete = mezzoLoad.getDeposito();
        if (depositoToDelete != null && !depositoToDelete.isNew()) {

            mezzoLoad.setDeposito(null);
            mezzoLoad = salvaMezzoTrasporto(mezzoLoad);

            try {
                depositiManager.cancellaDeposito(depositoToDelete.creaProxy());
            } catch (Exception e) {
                Deposito deposito = depositiManager.caricaDeposito(depositoToDelete.getId());
                deposito.setAttivo(false);
                depositiManager.salvaDeposito(deposito);
            }
        }

        LOGGER.debug("--> Enter rimuoviDepositoDaMezzoDiTrasporto");
        return mezzoLoad;
    }

    @Override
    public MezzoTrasporto salvaMezzoTrasporto(MezzoTrasporto mezzoTrasporto) {
        LOGGER.debug("--> Enter salvaMezzoTrasporto");

        if (mezzoTrasporto.isNew()) {
            mezzoTrasporto.setCodiceAzienda(getAzienda());
        }

        MezzoTrasporto mezzoTrasportoSalvato = null;
        try {
            mezzoTrasportoSalvato = panjeaDAO.save(mezzoTrasporto);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio del mezzo di trasporto", e);
            throw new GenericException("Errore durante il salvataggio del mezzo di trasporto", e);
        }

        LOGGER.debug("--> Exit salvaMezzoTrasporto");
        return mezzoTrasportoSalvato;
    }

    @Override
    public TipoMezzoTrasporto salvaTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto) {
        LOGGER.debug("--> Enter salvaTipoMezzoTrasporto");

        if (tipoMezzoTrasporto.isNew()) {
            tipoMezzoTrasporto.setCodiceAzienda(getAzienda());
        }

        TipoMezzoTrasporto tipoMezzoTrasportoSalvato = null;
        try {
            tipoMezzoTrasportoSalvato = panjeaDAO.save(tipoMezzoTrasporto);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio del tipo mezzo di trasporto", e);
            throw new GenericException("Errore durante il salvataggio del tipo mezzo di trasporto", e);
        }

        LOGGER.debug("--> Exit salvaTipoMezzoTrasporto");
        return tipoMezzoTrasportoSalvato;
    }

}
