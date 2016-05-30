package it.eurotn.panjea.anagrafica.manager.depositi;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.TipiDepositoManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;

@Stateless(name = "Panjea.TipiDepositoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipiDepositoManager")
public class TipiDepositoManagerBean extends CrudManagerBean<TipoDeposito>implements TipiDepositoManager {

    private static final Logger LOGGER = Logger.getLogger(TipiDepositoManagerBean.class);

    @Override
    public TipoDeposito caricaByCodice(String codice) {
        LOGGER.debug("--> Enter caricaByCodice");
        Query query = panjeaDAO.prepareNamedQuery("TipoDeposito.caricaByCodice");
        query.setParameter("codice", codice);
        TipoDeposito tipoDeposito = null;
        try {
            @SuppressWarnings("unchecked")
            List<TipoDeposito> result = panjeaDAO.getResultList(query);
            if (!result.isEmpty()) {
                tipoDeposito = result.get(0);
            }
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare il tipo deposito con codice " + codice, e);
            throw new GenericException("-->errore nel caricare il tipo deposito con codice " + codice, e);
        }
        LOGGER.debug("--> Exit caricaByCodice");
        return tipoDeposito;
    }

    @Override
    public TipoDeposito caricaOCreaByCodice(String codice) {
        TipoDeposito tipoDeposito = caricaByCodice(codice);
        if (tipoDeposito == null) {
            TipoDeposito nuovoTipoDeposito = new TipoDeposito();
            nuovoTipoDeposito.setCodice(codice);
            tipoDeposito = salva(nuovoTipoDeposito);
        }
        return tipoDeposito;
    }

    @Override
    protected Class<TipoDeposito> getManagedClass() {
        return TipoDeposito.class;
    }

}
