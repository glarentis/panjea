package it.eurotn.panjea.manutenzioni.manager.righeinstallazione;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.interfaces.AreeInstallazioniManager;
import it.eurotn.panjea.manutenzioni.manager.installazioni.ParametriRicercaInstallazioni;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.InstallazioniManager;
import it.eurotn.panjea.manutenzioni.manager.righeinstallazione.interfaces.RigheInstallazioneManager;
import it.eurotn.util.KeyFromValueProvider;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.RigheInstallazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheInstallazioneManager")
public class RigheInstallazioneManagerBean extends CrudManagerBean<RigaInstallazione>
        implements RigheInstallazioneManager {

    private static final Logger LOGGER = Logger.getLogger(RigheInstallazioneManagerBean.class);

    @EJB
    private AreeInstallazioniManager areeInstallazioniManager;

    @EJB
    private InstallazioniManager installazioniManager;

    @Override
    public void cancella(RigaInstallazione riga) {
        riga = panjeaDAO.loadLazy(RigaInstallazione.class, riga.getId());
        super.cancella(riga);
        installazioniManager.aggiornaArticoloInstallato(riga.getInstallazione().getId());
    }

    @SuppressWarnings("unchecked")
    private List<RigaInstallazione> caricaRigheInstallazione(String where) {
        String hql = "select r from RigaInstallazione r  join fetch r.areaInstallazione ai join fetch ai.documento  left join fetch r.installazione i left join fetch i.articolo arti left join fetch i.ubicazione left join fetch r.articoloInstallazione left join fetch r.causaleInstallazione left join fetch r.articoloRitiro left join fetch r.causaleRitiro where "
                + where;
        List<RigaInstallazione> righe;
        try {
            righe = panjeaDAO.getResultList(panjeaDAO.prepareQuery(hql));
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare le righe di installazione con where " + where, e);
            throw new GenericException("-->errore nel caricare le righe di installazione con la where " + where, e);
        }
        return righe;
    }

    @Override
    public List<RigaInstallazione> caricaRigheInstallazioneByAreaInstallazione(Integer idAreaInstallazione) {
        LOGGER.debug("--> Enter caricaRigheInstallazioneByAreaInstallazione");
        AreaInstallazione areaInstallazione = areeInstallazioniManager.caricaById(idAreaInstallazione);
        List<RigaInstallazione> collectionRighe = caricaRigheInstallazione(
                "r.areaInstallazione.id=" + idAreaInstallazione);
        Map<Integer, RigaInstallazione> righeArea = PanjeaEJBUtil.collectionToMap(collectionRighe,
                new KeyFromValueProvider<RigaInstallazione, Integer>() {

                    @Override
                    public Integer keyFromValue(RigaInstallazione elem) {
                        return elem.getInstallazione().getId();
                    }
                });
        ParametriRicercaInstallazioni parametri = new ParametriRicercaInstallazioni();
        parametri.setIdSedeEntita(areaInstallazione.getDocumento().getSedeEntita().getId());
        parametri.setIncludeEmpty(true);
        List<Installazione> installazioni = installazioniManager.ricercaByParametri(parametri);

        List<RigaInstallazione> righe = new ArrayList<>();
        for (Installazione installazione : installazioni) {
            RigaInstallazione riga = righeArea.get(installazione.getId());
            if (riga == null) {
                riga = new RigaInstallazione(installazione);
            }
            righe.add(riga);
        }
        LOGGER.debug("--> Exit caricaRigheInstallazioneByAreaInstallazione");
        return righe;
    }

    @Override
    public List<RigaInstallazione> caricaRigheInstallazioneByArticolo(Integer idArticolo) {
        return caricaRigheInstallazione(
                " r.articoloRitiro.id=" + idArticolo + " or r.articoloInstallazione.id=" + idArticolo);
    }

    @Override
    public List<RigaInstallazione> caricaRigheInstallazioneByInstallazione(Integer idInstallazione) {
        return caricaRigheInstallazione("r.installazione.id=" + idInstallazione);
    }

    @Override
    protected Class<RigaInstallazione> getManagedClass() {
        return RigaInstallazione.class;
    }

    @Override
    public RigaInstallazione salva(RigaInstallazione object) {
        RigaInstallazione ri = super.salva(object);
        installazioniManager.aggiornaArticoloInstallato(ri.getInstallazione().getId());
        return ri;
    }

    @Override
    public RigaInstallazione salvaInizializza(RigaInstallazione rigaInstallazione) {
        LOGGER.debug("--> Enter salvaInizializza");
        RigaInstallazione rigaSalvata = salva(rigaInstallazione);
        Hibernate.initialize(rigaSalvata.getInstallazione());
        Hibernate.initialize(rigaSalvata.getInstallazione().getUbicazione());
        Hibernate.initialize(rigaSalvata.getInstallazione().getArticolo());
        Hibernate.initialize(rigaSalvata.getAreaInstallazione());
        Hibernate.initialize(rigaSalvata.getArticoloInstallazione());
        Hibernate.initialize(rigaSalvata.getArticoloRitiro());
        Hibernate.initialize(rigaSalvata.getCausaleInstallazione());
        Hibernate.initialize(rigaSalvata.getCausaleRitiro());
        LOGGER.debug("--> Exit salvaInizializza");
        return rigaSalvata;
    }

}