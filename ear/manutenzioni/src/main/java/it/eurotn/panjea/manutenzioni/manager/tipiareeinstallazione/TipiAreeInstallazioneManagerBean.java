package it.eurotn.panjea.manutenzioni.manager.tipiareeinstallazione;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.manutenzioni.domain.documento.TipoAreaInstallazione;
import it.eurotn.panjea.manutenzioni.manager.tipiareeinstallazione.interfaces.TipiAreeInstallazioneManager;

@Stateless(name = "Panjea.TipiAreeInstallazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipiAreeInstallazioneManager")
public class TipiAreeInstallazioneManagerBean extends CrudManagerBean<TipoAreaInstallazione>
        implements TipiAreeInstallazioneManager {

    private static final Logger LOGGER = Logger.getLogger(TipiAreeInstallazioneManagerBean.class);

    @Override
    public TipoAreaInstallazione caricaByTipoDocumento(int idTipoDocumento) {
        LOGGER.debug("--> Enter caricaByTipoDocumento");
        TipoAreaInstallazione tai = null;
        Query query = panjeaDAO.prepareQuery(
                "Select tai from TipoAreaInstallazione tai where tai.tipoDocumento.id=" + idTipoDocumento);
        try {
            tai = (TipoAreaInstallazione) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException onf) { // NOSONAR
            return new TipoAreaInstallazione();
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare il tipoareainstallazione per il tipodocumento " + idTipoDocumento, e);
            throw new GenericException("-->errore nel caricare il tipoareainstallazione per il tipodocumento ", e);
        }
        LOGGER.debug("--> Exit caricaByTipoDocumento");
        return tai;
    }

    @Override
    protected Class<TipoAreaInstallazione> getManagedClass() {
        return TipoAreaInstallazione.class;
    }

}