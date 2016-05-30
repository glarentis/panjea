package it.eurotn.panjea.vending.manager.distributore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficaTabelleManager;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.manutenzioni.manager.articolimi.interfaces.ArticoliMIManager;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.manager.distributore.interfaces.DistributoreManager;

@Stateless(name = "Panjea.DistributoreManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DistributoreManager")
public class DistributoreManagerBean extends CrudManagerBean<Distributore>implements DistributoreManager {

    private static final String UMDISTR = "UMDISTR";
    private static final Logger LOGGER = Logger.getLogger(DistributoreManagerBean.class);

    @EJB
    private ArticoliMIManager articoliMIManager;

    @EJB
    private AnagraficaTabelleManager anagraficaTabelleManager;

    @EJB
    private PreferenceService preferenceService;

    @Override
    public Distributore caricaById(Integer id) {
        Distributore dist = (Distributore) articoliMIManager.caricaByIdConInstallazione(id);
        Hibernate.initialize(dist.getDatiVending().getSistemaPagamento());
        Hibernate.initialize(dist.getDatiVending().getGettoniera());
        Hibernate.initialize(dist.getDatiVending().getLettoreBanconote());
        return dist;
    }

    @Override
    protected Class<Distributore> getManagedClass() {
        return Distributore.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Distributore> ricercaDistributori(ParametriRicercaDistributore parametri) {
        LOGGER.debug("--> Enter caricaDistributori");
        Map<String, Object> paramsMap = new HashMap<>();

        List<Distributore> distributori = new ArrayList<>();

        StringBuilder queryString = new StringBuilder(
                "select dist.id as id,dist.version as version,dist.codice as codice,dist.descrizioneLinguaAziendale as descrizioneLinguaAziendale,");
        queryString.append(" dist.proprietaCliente as proprietaCliente,");
        queryString.append(
                " modello.id as datiVending$modello$id,modello.version as datiVending$modello$version,modello.codice as datiVending$modello$codice,modello.descrizione as datiVending$modello$descrizione");
        if (parametri.isSoloDisponibili() || parametri.getIdCliente() != null || parametri.getIdSedeCliente() != null) {
            queryString.append(" from Installazione inst ");
            queryString.append(" right join inst.articolo dist ");
            queryString.append(" right join dist.datiVending vend");
            queryString.append(" right join vend.modello modello");
            queryString.append(" right join inst.deposito deposito ");
            queryString.append(" right join deposito.sedeEntita sedeEntita ");
            queryString.append("where  dist.class = it.eurotn.panjea.vending.domain.Distributore  ");
            queryString.append("and dist.codiceAzienda = :paramCodiceAzienda ");
        } else {
            queryString.append(" from Distributore dist ");
            queryString.append("inner join dist.datiVending vend ");
            queryString.append("inner join vend.modello modello ");
            queryString.append("where dist.codiceAzienda = :paramCodiceAzienda ");
        }

        if (parametri.isSoloDisponibili()) {
            queryString.append(" and inst is null ");
        }

        if (parametri.getIdCliente() != null && parametri.getIdSedeCliente() == null) {
            queryString.append(" and sedeEntita.entita.id=:idEntita");
            paramsMap.put("idEntita", parametri.getIdCliente());
        }

        if (parametri.getIdSedeCliente() != null) {
            queryString.append(" and sedeEntita.id=:idSedeEntita");
            paramsMap.put("idSedeEntita", parametri.getIdSedeCliente());
        }

        if (!StringUtils.isEmpty(parametri.getCodice())) {
            queryString.append(" and dist.codice like :paramCodice");
            paramsMap.put("paramCodice", parametri.getCodice() + "%");
        }
        if (!StringUtils.isEmpty(parametri.getDescrizione())) {
            queryString.append(" and dist.descrizioneLinguaAziendale like :paramDescrizione");
            paramsMap.put("paramDescrizione", parametri.getDescrizione() + "%");
        }
        if (parametri.isSoloProprietaCliente()) {
            queryString.append(" and dist.proprietaCliente = :paramProprietaCliente ");
            paramsMap.put("paramProprietaCliente", parametri.isSoloProprietaCliente());
        }
        if (parametri.getIdModello() != null && parametri.getIdModello().compareTo(-1) != 0) {
            queryString.append(" and dist.datiVending.modello.id = :paramIdModello ");
            paramsMap.put("paramIdModello", parametri.getIdModello().intValue());
        }
        if (parametri.getIdTipoModello() != null && parametri.getIdTipoModello().compareTo(-1) != 0) {
            queryString.append(" and dist.datiVending.modello.tipoModello.id = :paramIdTipoModello ");
            paramsMap.put("paramIdTipoModello", parametri.getIdTipoModello().intValue());
        }
        if (StringUtils.isNotBlank(parametri.getDescrizioneModello())) {
            queryString.append(" and (dist.datiVending.modello.codice like :paramDescModello ");
            queryString.append(" or dist.datiVending.modello.descrizione like :paramDescModello ) ");
            paramsMap.put("paramDescModello", '%' + parametri.getDescrizioneModello() + "%");
        }

        Query query = panjeaDAO.prepareQuery(queryString.toString(), Distributore.class, null);
        query.setParameter("paramCodiceAzienda", getCodiceAzienda());
        for (Entry<String, Object> entry : paramsMap.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        try {
            distributori = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la ricerca dei distributori per modello.", e);
            throw new GenericException("errore durante la ricerca dei distributori per modello.", e);
        }

        LOGGER.debug("--> Exit caricaDistributori");
        return distributori;
    }

    @Override
    public Distributore salva(Distributore distributore) {
        if (distributore.getUnitaMisura() == null) {
            String codiceUnitaMisura;
            try {
                codiceUnitaMisura = preferenceService.caricaPreference(UMDISTR).getValore();
                List<UnitaMisura> um = anagraficaTabelleManager.caricaUnitaMisura(codiceUnitaMisura);
                if (CollectionUtils.isEmpty(um)) {
                    throw new GenericException(
                            "Impostare la variabile UMDISTR nei settings per l'um di default del distr");
                }
                distributore.setUnitaMisura(um.get(0));
            } catch (PreferenceNotFoundException e) {
                throw new GenericException("Impostare la variabile UMDISTR nei settings per l'um di default del distr");
            }
        }
        Distributore distributoreSalvato = super.salva(distributore);
        return caricaById(distributoreSalvato.getId());
    }

}
