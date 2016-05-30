package it.eurotn.panjea.manutenzioni.manager.installazioni;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.UbicazioneInstallazione;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.DepositoInstallazioneManager;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.InstallazioniManager;
import it.eurotn.panjea.manutenzioni.manager.manutenzionisettings.interfaces.ManutenzioniSettingsManager;

@Stateless(name = "Panjea.InstallazioniManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.InstallazioniManager")
public class InstallazioniManagerBean extends CrudManagerBean<Installazione>implements InstallazioniManager {
    private static final Logger LOGGER = Logger.getLogger(InstallazioniManagerBean.class);

    @EJB
    private DepositoInstallazioneManager depositoInstallazioneManager;

    @EJB
    private ManutenzioniSettingsManager manutenzioniSettingsManager;

    @Override
    public void aggiornaArticoloInstallato(int idInstallazione) {
        LOGGER.debug("--> Enter aggiornaArticoloInstallato");
        StringBuilder sb = new StringBuilder();
        sb.append("update manu_installazioni set articolo_id= ");
        sb.append("( ");
        sb.append("   select ");
        sb.append("   r.articoloInstallazione_id ");
        sb.append("   from manu_righe_installazione r ");
        sb.append("   inner join manu_area_installazioni ai on ai.id=r.areaInstallazione_id ");
        sb.append("   inner join docu_documenti doc on doc.id=ai.documento_id ");
        sb.append("   where r.installazione_id= ");
        sb.append(idInstallazione);
        sb.append("   order by doc.dataDocumento desc,r.timeStamp desc limit 1 ");
        sb.append(") ");
        sb.append("where manu_installazioni.id=");
        sb.append(idInstallazione);
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        query.executeUpdate();
        LOGGER.debug("--> Exit aggiornaArticoloInstallato");
    }

    @Override
    public Installazione caricaByArticolo(Integer idArticolo) {
        LOGGER.debug("--> Enter caricaByArticolo");
        Query query = panjeaDAO
                .prepareQuery("select inst.id from Installazione inst where inst.articolo.id=" + idArticolo);
        Installazione installazione = null;
        try {
            @SuppressWarnings("unchecked")
            List<Integer> installazioni = panjeaDAO.getResultList(query);
            if (installazioni.size() > 0) {
                Integer id = installazioni.get(0);
                installazione = caricaById(id);
            }
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare l'installazioen per l'articolo " + idArticolo, e);
            throw new GenericException("-->errore nel caricare l'installazioen per l'articolo " + idArticolo, e);
        }
        LOGGER.debug("--> Exit caricaByArticolo");
        return installazione;
    }

    @Override
    public Installazione caricaByCodice(String codice) {
        LOGGER.debug("--> Enter caricaByCodice");
        LOGGER.debug("--> Enter caricaInstallazioneByCodice");
        Query query = panjeaDAO.prepareQuery("select i.id from Installazione i where i.codice=:codice");
        query.setParameter("codice", codice);
        @SuppressWarnings("unchecked")
        List<Integer> idInstallazioni = query.getResultList();
        Installazione installazione = null;
        if (idInstallazioni.size() > 0) {
            try {
                installazione = panjeaDAO.load(Installazione.class, idInstallazioni.get(0));
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
            return installazione;
        }
        LOGGER.debug("--> Exit caricaInstallazioneByCodice");
        return null;
    }

    @Override
    public Installazione caricaById(Integer id) {
        Installazione installazione = super.caricaById(id);
        Hibernate.initialize(installazione.getDatiInstallazione().getTecnico());
        Hibernate.initialize(installazione.getDatiInstallazione().getCaricatore());
        Hibernate.initialize(installazione.getListino());
        Hibernate.initialize(installazione.getListinoAlternativo());
        Hibernate.initialize(installazione.getDatiInstallazione().getIvaSomministrazione());
        Hibernate.initialize(installazione.getDeposito());
        Hibernate.initialize(installazione.getArticolo());
        Hibernate.initialize(installazione.getUbicazione());
        Hibernate.initialize(installazione.getDeposito().getSedeEntita().getEntita().getAnagrafica());
        Hibernate.initialize(installazione.getTipoAreaMagazzino());
        Hibernate.initialize(installazione.getPozzetto());
        return installazione;
    }

    /**
     * @return hql per la ricerca delle installazioni
     */
    private String getHQLRicercaInstallazioni() {

        StringBuilder sb = new StringBuilder(2000);
        sb.append("select i.id as id, ");
        sb.append("i.version as version, ");
        sb.append("i.codice as codice, ");
        sb.append("i.descrizione as descrizione, ");
        sb.append("tam.id as tipoAreaMagazzino$id, ");
        sb.append("tam.version as tipoAreaMagazzino$version, ");
        sb.append("tipoDoc.id as tipoAreaMagazzino$tipoDocumento$id, ");
        sb.append("tipoDoc.version as tipoAreaMagazzino$tipoDocumento$version, ");
        sb.append("tipoDoc.codice as tipoAreaMagazzino$tipoDocumento$codice, ");
        sb.append("tipoDoc.descrizione as tipoAreaMagazzino$tipoDocumento$descrizione, ");
        sb.append("art.id as articolo$id, ");
        sb.append("art.version as articolo$version, ");
        sb.append("art.codice as articolo$codice, ");
        sb.append("art.descrizioneLinguaAziendale as articolo$descrizioneLinguaAziendale, ");
        sb.append("dep.id as deposito$id, ");
        sb.append("dep.version as deposito$version, ");
        sb.append("sedeEnt.id as deposito$sedeEntita$id, ");
        sb.append("sedeEnt.version as deposito$sedeEntita$version, ");
        sb.append("sedeEnt.codice as deposito$sedeEntita$codice, ");
        sb.append("ent.id as deposito$sedeEntita$entita$id, ");
        sb.append("ent.version as deposito$sedeEntita$entita$version, ");
        sb.append("ent.codice as deposito$sedeEntita$entita$codice, ");
        sb.append("ent.tipoAnagrafica as deposito$sedeEntita$entita$tipoAnagrafica, ");
        sb.append("anagEnt.denominazione as deposito$sedeEntita$entita$anagrafica$denominazione, ");
        sb.append("sedeAnag.descrizione as deposito$sedeEntita$sede$descrizione, ");
        sb.append("ubic as ubicazione, ");
        sb.append("tecnico.id as datiInstallazione$tecnico$id, ");
        sb.append("tecnico.version as datiInstallazione$tecnico$version, ");
        sb.append("tecnico.codice as datiInstallazione$tecnico$codice, ");
        sb.append("tecnico.nome as datiInstallazione$tecnico$nome, ");
        sb.append("tecnico.cognome as datiInstallazione$tecnico$cognome, ");
        sb.append("caricatore.id as datiInstallazione$caricatore$id, ");
        sb.append("caricatore.version as datiInstallazione$caricatore$version, ");
        sb.append("caricatore.codice as datiInstallazione$caricatore$codice, ");
        sb.append("caricatore.nome as datiInstallazione$caricatore$nome, ");
        sb.append("caricatore.cognome as datiInstallazione$caricatore$cognome, ");
        sb.append("ivaSomm.id as datiInstallazione$ivaSomministrazione$id, ");
        sb.append("ivaSomm.version as datiInstallazione$ivaSomministrazione$version, ");
        sb.append("ivaSomm.descrizioneInterna as datiInstallazione$ivaSomministrazione$descrizioneInterna, ");
        sb.append("ivaSomm.codice as datiInstallazione$ivaSomministrazione$codice, ");
        sb.append("list.id as listino$id, ");
        sb.append("list.version as listino$version, ");
        sb.append("list.codice as listino$codice, ");
        sb.append("list.descrizione as listino$descrizione, ");
        sb.append("listAlt.id as listinoAlternativo$id, ");
        sb.append("listAlt.version as listinoAlternativo$version, ");
        sb.append("listAlt.codice as listinoAlternativo$codice, ");
        sb.append("listAlt.descrizione as listinoAlternativo$descrizione, ");
        sb.append("carDep.id as datiInstallazione$caricatore$mezzoTrasporto$deposito$id, ");
        sb.append("carDep.codice as datiInstallazione$caricatore$mezzoTrasporto$deposito$codice, ");
        sb.append("carDep.descrizione as datiInstallazione$caricatore$mezzoTrasporto$deposito$descrizione, ");
        sb.append("carDep.version as datiInstallazione$caricatore$mezzoTrasporto$deposito$version, ");
        sb.append("pozz.id as pozzetto$id, ");
        sb.append("pozz.version as pozzetto$version, ");
        sb.append("pozz.codice as pozzetto$codice, ");
        sb.append("pozz.descrizione as pozzetto$descrizione ");
        sb.append("from Installazione i ");
        sb.append("left join i.articolo art ");
        sb.append("inner join i.deposito as dep ");
        sb.append("inner join dep.sedeEntita as sedeEnt ");
        sb.append("inner join sedeEnt.entita as ent ");
        sb.append("inner join ent.anagrafica as anagEnt ");
        sb.append("inner join sedeEnt.sede as sedeAnag ");
        sb.append("left join i.ubicazione as ubic ");
        sb.append("left join i.listino as list ");
        sb.append("left join i.listinoAlternativo as listAlt ");
        sb.append("left join i.datiInstallazione.tecnico as tecnico ");
        sb.append("left join i.datiInstallazione.caricatore as caricatore ");
        sb.append("left join i.datiInstallazione.ivaSomministrazione as ivaSomm ");
        sb.append("left join caricatore.mezzoTrasporto carTrasp ");
        sb.append("left join carTrasp.deposito as carDep ");
        sb.append("left join i.tipoAreaMagazzino tam ");
        sb.append("left join tam.tipoDocumento tipoDoc ");
        sb.append("left join i.pozzetto pozz ");

        return sb.toString();
    }

    @Override
    protected Class<Installazione> getManagedClass() {
        return Installazione.class;
    }

    private Integer getNextCodiceInstallazione() {
        Integer nextCodice = 0;

        SQLQuery query = panjeaDAO.prepareNativeQuery("select MAX(CAST(codice AS Int)) from manu_installazioni");
        BigInteger maxCodice = ((BigInteger) query.uniqueResult());
        if (maxCodice != null) {
            nextCodice = maxCodice.intValue();
        }

        nextCodice++;

        return nextCodice;
    }

    @Override
    public List<Installazione> ricercaByEntita(Integer idEntita) {
        ParametriRicercaInstallazioni parametri = new ParametriRicercaInstallazioni();
        parametri.setIdEntita(idEntita);
        parametri.setIncludeEmpty(true);
        return ricercaByParametri(parametri);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Installazione> ricercaByParametri(ParametriRicercaInstallazioni parametri) {
        StringBuilder sbQuery = new StringBuilder(getHQLRicercaInstallazioni());
        sbQuery.append(" where 1=1 ");
        if (!StringUtils.isEmpty(parametri.getCodice())) {
            sbQuery.append(" and i.codice like '%" + parametri.getCodice() + "'");
        }
        if (!StringUtils.isEmpty(parametri.getDescrizione())) {
            sbQuery.append(" and i.descrizione like '%" + parametri.getDescrizione() + "'");
        }
        if (parametri.getIdEntita() != null) {
            sbQuery.append(" and ent.id = " + parametri.getIdEntita());
        }
        if (parametri.getIdSedeEntita() != null) {
            sbQuery.append(" and sedeEnt.id = " + parametri.getIdSedeEntita());
        }
        if (!parametri.isIncludeEmpty()) {
            sbQuery.append(" and art.id is not null ");
        }
        if (parametri.getIdTecnico() != null && parametri.getIdCaricatore() != null) {
            sbQuery.append(" and (i.datiInstallazione.tecnico.id = " + parametri.getIdTecnico());
            sbQuery.append(" or i.datiInstallazione.caricatore.id = " + parametri.getIdCaricatore() + ") ");
        } else {
            if (parametri.getIdTecnico() != null) {
                sbQuery.append(" and i.datiInstallazione.tecnico.id = " + parametri.getIdTecnico());
            }

            if (parametri.getIdCaricatore() != null) {
                sbQuery.append(" and i.datiInstallazione.caricatore.id = " + parametri.getIdCaricatore());
            }
        }
        Query query = panjeaDAO.prepareQuery(sbQuery.toString(), Installazione.class, null);
        List<Installazione> result = null;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare le installazioni per i parametri " + parametri, e);
            throw new GenericException("-->errore nel caricare le installazioni per i parametri " + parametri, e);
        }

        return result;
    }

    @Override
    public List<Installazione> ricercaBySede(Integer idSedeEntita) {
        ParametriRicercaInstallazioni parametri = new ParametriRicercaInstallazioni();
        parametri.setIdSedeEntita(idSedeEntita);
        parametri.setIncludeEmpty(true);
        return ricercaByParametri(parametri);
    }

    @Override
    public Installazione salva(Installazione installazione) {
        if (installazione.getDeposito() == null) {
            throw new InvalidParameterException("Installazione è senza deposito");
        }
        if (installazione.getDeposito().isNew()) {
            installazione.setDeposito(depositoInstallazioneManager.caricaOCreaDeposito(
                    installazione.getDeposito().getSedeEntita(), installazione.getDeposito().getSedeDeposito()));
        }
        if (StringUtils.isBlank(installazione.getCodice())) {
            installazione.setCodice(String.valueOf(getNextCodiceInstallazione()));
        }
        if (installazione.getUbicazione() == null || installazione.getUbicazione().isNew()) {
            UbicazioneInstallazione ubicazionePredefinita = manutenzioniSettingsManager.caricaManutenzioniSettings()
                    .getUbicazionePredefinita();
            if (ubicazionePredefinita == null || ubicazionePredefinita.isNew()) {
                throw new GenericException(
                        "Ubicazione non presente. Selezionarne una predefinita nelle preferenze per assegnarla in automatico.");
            }
            installazione.setUbicazione(ubicazionePredefinita);
        }
        return super.salva(installazione);
    }

}
