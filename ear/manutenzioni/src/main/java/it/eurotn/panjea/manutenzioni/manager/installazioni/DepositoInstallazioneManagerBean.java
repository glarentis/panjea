package it.eurotn.panjea.manutenzioni.manager.installazioni;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.TipiDepositoManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.manutenzioni.domain.DepositoInstallazione;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.DepositoInstallazioneManager;

@Stateless(name = "Panjea.DepositoInstallazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DepositoInstallazioneManager")
public class DepositoInstallazioneManagerBean extends CrudManagerBean<DepositoInstallazione>
        implements DepositoInstallazioneManager {
    private static final Logger LOGGER = Logger.getLogger(DepositoInstallazioneManagerBean.class);
    private static final String CODICE_TIPO_DEPOSITO_INSTALLAZIONE = "DI";

    @EJB
    private TipiDepositoManager tipiDepositoManager;

    @Override
    public DepositoInstallazione caricaDeposito(SedeEntita sedeEntita) {
        LOGGER.debug("--> Enter caricaDeposito");

        Query query = panjeaDAO.prepareNamedQuery("DepositoInstallazione.caricaDepositoPerSedeEntita");
        query.setParameter("paramSede", sedeEntita);
        DepositoInstallazione result = null;
        try {
            result = (DepositoInstallazione) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) { // NOSONAR
            result = null;
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare il deposito per la sedeEntita con id " + sedeEntita.getId(), e);
            throw new GenericException(
                    "-->errore nel caricare il deposito per la sedeEntita con id " + sedeEntita.getId(), e);
        }

        return result;
    }

    @Override
    public DepositoInstallazione caricaOCreaDeposito(SedeEntita sedeEntita, SedeAzienda sedeAzienda) {
        LOGGER.debug("--> Enter creaDeposito");

        sedeEntita = (SedeEntita) ((Session) panjeaDAO.getEntityManager().getDelegate()).merge(sedeEntita);
        DepositoInstallazione result = caricaDeposito(sedeEntita);
        if (result == null) {
            // Se non trovo il deposito lo creo
            result = creaDepositoInstallazione(sedeEntita, sedeAzienda);
        }

        LOGGER.debug("--> Exit creaDeposito");
        return result;
    }

    private DepositoInstallazione creaDepositoInstallazione(SedeEntita sedeEntita, SedeAzienda sedeAzienda) {
        DepositoInstallazione result = new DepositoInstallazione();
        result.setCodice("DINST");
        result.setDescrizione("Deposito installazione");
        result.setAttivo(false);
        result.setEntita(sedeEntita.getEntita().getEntitaLite());
        result.setSedeEntita(sedeEntita);
        result.setTipoDeposito(tipiDepositoManager.caricaOCreaByCodice(CODICE_TIPO_DEPOSITO_INSTALLAZIONE));
        result.setCodiceAzienda(getCodiceAzienda());
        result.setSedeDeposito(sedeAzienda);
        return salva(result);
    }

    @Override
    protected Class<DepositoInstallazione> getManagedClass() {
        return DepositoInstallazione.class;
    }

}
