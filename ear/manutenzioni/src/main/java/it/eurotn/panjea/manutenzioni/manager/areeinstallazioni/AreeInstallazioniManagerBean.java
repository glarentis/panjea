package it.eurotn.panjea.manutenzioni.manager.areeinstallazioni;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.interfaces.AreeInstallazioniMagazzinoManager;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.interfaces.AreeInstallazioniManager;
import it.eurotn.panjea.manutenzioni.manager.righeinstallazione.interfaces.RigheInstallazioneManager;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.util.AliasToBeanNestedResultTransformer;

@Stateless(name = "Panjea.AreeInstallazioniManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreeInstallazioniManager")
public class AreeInstallazioniManagerBean extends CrudManagerBean<AreaInstallazione>
        implements AreeInstallazioniManager {

    private static final Logger LOGGER = Logger.getLogger(AreeInstallazioniManagerBean.class);

    @EJB
    private DocumentiManager documentiManager;

    @EJB
    @IgnoreDependency
    private AreeInstallazioniMagazzinoManager areeInstallazioniMagazzinoManager;

    @EJB
    @IgnoreDependency
    private RigheInstallazioneManager righeInstallazioneManager;

    @Override
    public void cancella(AreaInstallazione areaInstallazione) {
        // Cancello le righe singolarmente per aggiornare le installazioni dell'articoloMI
        areaInstallazione = caricaById(areaInstallazione.getId());
        for (RigaInstallazione riga : areaInstallazione.getRigheInstallazione()) {
            righeInstallazioneManager.cancella(riga.getId());
        }
        super.cancella(areaInstallazione);
        documentiManager.cancellaDocumento(areaInstallazione.getDocumento());
        areeInstallazioniMagazzinoManager.cancellaAreaMagazzino(areaInstallazione.getIdAreaMagazzino());
    }

    @Override
    protected Class<AreaInstallazione> getManagedClass() {
        return AreaInstallazione.class;
    }

    @Override
    public List<AreaInstallazione> ricercaAreeInstallazioni(ParametriRicercaAreeInstallazione parametri) {
        LOGGER.debug("--> Enter ricercaAreeInstallazioni");
        ProjectionList projections = Projections.projectionList().add(Projections.id().as("id"))
                .add(Projections.property("doc.id").as("documento.id"))
                .add(Projections.property("doc.codice.codice").as("documento.codice.codice"))
                .add(Projections.property("doc.codice.codiceOrder").as("documento.codice.codiceOrder"))
                .add(Projections.property("doc.dataDocumento").as("documento.dataDocumento"))
                .add(Projections.property("tipoDocumento.id").as("documento.tipoDocumento.id"))
                .add(Projections.property("tipoDocumento.codice").as("documento.tipoDocumento.codice"))
                .add(Projections.property("tipoDocumento.descrizione").as("documento.tipoDocumento.descrizione"))
                .add(Projections.property("tipoDocumento.tipoEntita").as("documento.tipoDocumento.tipoEntita"))
                .add(Projections.property("ent.id").as("documento.entita.id"))
                .add(Projections.property("ent.codice").as("documento.entita.codice"))
                .add(Projections.property("anagEntita.denominazione").as("documento.entita.anagrafica.denominazione"))
                .add(Projections.property("sede.id").as("documento.sedeEntita.id"))
                .add(Projections.property("sede.codice").as("documento.sedeEntita.codice"))
                .add(Projections.property("sede.sede").as("documento.sedeEntita.sede"));

        Criteria criteria = ((Session) panjeaDAO.getEntityManager().getDelegate())
                .createCriteria(AreaInstallazione.class).createAlias("documento", "doc")
                .createAlias("documento.entita", "ent").createAlias("documento.tipoDocumento", "tipoDocumento")
                .createAlias("documento.entita.anagrafica", "anagEntita").createAlias("documento.sedeEntita", "sede")
                .setProjection(projections)
                .setResultTransformer(new AliasToBeanNestedResultTransformer(AreaInstallazione.class));
        if (parametri.getPeriodo() != null && parametri.getPeriodo().getTipoPeriodo() != TipoPeriodo.NESSUNO) {
            criteria.add(Restrictions.between("doc.dataDocumento", parametri.getPeriodo().getDataIniziale(),
                    parametri.getPeriodo().getDataFinale()));
        }
        if (parametri.getEntita() != null) {
            criteria.add(Restrictions.eq("doc.entita", parametri.getEntita()));
        }
        if (parametri.getSedeEntita() != null) {
            criteria.add(Restrictions.eq("doc.sedeEntita", parametri.getSedeEntita()));
        }
        LOGGER.debug("--> Exit ricercaAreeInstallazioni");
        @SuppressWarnings("unchecked")
        List<AreaInstallazione> result = criteria.list();
        return result;
    }

    @Override
    public AreaInstallazione salva(AreaInstallazione areaInstallazione) {
        areaInstallazione.getDocumento().setTipoDocumento(areaInstallazione.getTipoAreaDocumento().getTipoDocumento());
        if (areaInstallazione.getDocumento().isNew()) {
            // inizializza il codiceAzienda di documento
            areaInstallazione.getDocumento().setCodiceAzienda(getCodiceAzienda());
        }
        // save di documento
        Documento documento;
        try {
            documento = documentiManager.salvaDocumento(areaInstallazione.getDocumento());
        } catch (DocumentoDuplicateException e) {
            LOGGER.error("--> errore DocumentoDuplicateException in salvaAreaPreventivo", e);
            throw new RuntimeException(e);
        }
        areaInstallazione.setDocumento(documento);
        return super.salva(areaInstallazione);
    }

}