package it.eurotn.panjea.vending.rest.manager.palmari.importazione;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.importer.util.RigaImport;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.RigaImportazioneBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.RifornimentiProdotti;

@Stateless(name = "Panjea.RigaImportazioneBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaImportazioneBuilder")
public class RigaImportazioneBuilderBean implements RigaImportazioneBuilder {

    private static final Logger LOGGER = Logger.getLogger(RigaImportazioneBuilderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    private RigaImport creaRiga(RifornimentiProdotti riga) {
        RigaImport rigaImport = new RigaImport();
        rigaImport.setIdArticolo(getIdArticolo(riga.getProdotto()));
        rigaImport.setCodiceArticolo(null);
        rigaImport.setQta(riga.getQuantita());
        rigaImport.setPrezzoUnitario(riga.getPrezzo());
        return rigaImport;
    }

    @Override
    public Collection<RigaImport> creaRigheImport(Collection<RifornimentiProdotti> righeArticolo) {
        Collection<RigaImport> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(righeArticolo)) {
            return result;
        }
        for (RifornimentiProdotti riga : righeArticolo) {
            RigaImport rigaImport = creaRiga(riga);
            result.add(rigaImport);
            if (riga.getOmaggio() > 0) {
                RigaImport rigaImportOmaggio = creaRiga(riga);
                rigaImportOmaggio.setQta(riga.getOmaggio());
                rigaImportOmaggio.setOmaggio(true);
                result.add(rigaImportOmaggio);
            }
        }
        return result;
    }

    private Integer getIdArticolo(String codArticolo) {
        Query query = panjeaDAO.prepareQuery("select a.id from Articolo a where a.codice=:codice");
        query.setParameter("codice", codArticolo);
        Integer idArticolo = null;
        try {
            idArticolo = (Integer) panjeaDAO.getSingleResult(query);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare l'articolo con codice " + codArticolo, e);
            throw new GenericException("-->errore nel caricare l'articolo con codice ", e);
        }
        return idArticolo;
    }
}
