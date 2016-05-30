package it.eurotn.panjea.manutenzioni.manager.areeinstallazioni;

import java.util.Calendar;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoCancellaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.TipiAreaMagazzinoManager;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.exception.TaiSenzaTamException;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.interfaces.AreeInstallazioniMagazzinoManager;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.interfaces.RigheMagazzinoBuilder;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.DepositoInstallazioneManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.AreeInstallazioniMagazzinoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreeInstallazioniMagazzinoManager")
public class AreeInstallazioniMagazzinoManagerBean implements AreeInstallazioniMagazzinoManager {
    private static final Logger LOGGER = Logger.getLogger(AreeInstallazioniMagazzinoManagerBean.class);
    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;
    @EJB
    @IgnoreDependency
    private AreaMagazzinoCancellaManager areaMagazzinoCancellaManager;
    @EJB
    private PanjeaDAO panjeaDAO;
    @EJB
    private TipiAreaMagazzinoManager tipiAreaMagazzinoManager;
    @EJB
    private DepositoInstallazioneManager depositoInstallazioneManager;
    @EJB
    private RigheMagazzinoBuilder righeMagazzinoBuilder;

    @Override
    public void cancellaAreaMagazzino(Integer idAreaMagazzino) {
        LOGGER.debug("--> Enter cancellaAreaMagazzino");
        if (idAreaMagazzino == null) {
            return;
        }
        try {
            AreaMagazzino areaMagazzino = panjeaDAO.loadLazy(AreaMagazzino.class, idAreaMagazzino);
            areaMagazzinoCancellaManager.cancellaAreaMagazzino(areaMagazzino);
        } catch (DocumentiCollegatiPresentiException | TipoDocumentoBaseException | AreeCollegatePresentiException e) {
            LOGGER.error("-->errore nel cancellare l'area di magazzino id" + idAreaMagazzino, e);
            throw new GenericException("-->errore nel cancellare l'area di magazzino id" + idAreaMagazzino, e);
        }
        LOGGER.debug("--> Exit cancellaAreaMagazzino");
    }

    @Override
    public int creaAreaMagazzino(int idAreaInstallazione) throws TaiSenzaTamException {
        LOGGER.debug("--> Enter creaAreaMagazzino2");
        AreaInstallazione ai = panjeaDAO.loadLazy(AreaInstallazione.class, idAreaInstallazione);
        AreaMagazzino areaMagazzino = creaTestataMagazzino(ai);
        righeMagazzinoBuilder.creaRigheMagazzino(ai, areaMagazzino);
        try {
            areaMagazzinoManager.validaRigheMagazzino(areaMagazzino, null, false, true);
        } catch (TotaleDocumentoNonCoerenteException e) {
            throw new IllegalStateException("Eccezione non prevista", e);
        }
        LOGGER.debug("--> Exit creaAreaMagazzino");
        return areaMagazzino.getId();
    }

    /**
     *
     * @param ai
     *            area installazione
     * @return testata area magazzino SALVATA creata da area installazione
     * @throws TaiSenzaTamException
     */
    private AreaMagazzino creaTestataMagazzino(AreaInstallazione ai) throws TaiSenzaTamException {
        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setBloccata(true);
        areaMagazzino.setDocumento(ai.getDocumento());
        TipoAreaMagazzino tam = tipiAreaMagazzinoManager
                .caricaTipoAreaMagazzinoPerTipoDocumento(ai.getDocumento().getTipoDocumento().getId());
        if (tam.isNew()) {
            LOGGER.error("-->Un tipoAreaInstallazione deve avere un tipoareaMagazzino ");
            throw new TaiSenzaTamException();
        }
        areaMagazzino.setTipoAreaDocumento(tam);
        try {
            areaMagazzino.setDataRegistrazione(ai.getDocumento().getDataDocumento());
            areaMagazzino.setDepositoOrigine(ai.getDepositoOrigine());
            DepositoLite depositoDestinazione = depositoInstallazioneManager
                    .caricaOCreaDeposito(ai.getDocumento().getSedeEntita(), null).creaLite();
            areaMagazzino.setDepositoDestinazione(depositoDestinazione);
            areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.PROVVISORIO);
            Calendar calendarData = Calendar.getInstance();
            calendarData.setTime(ai.getDocumento().getDataDocumento());
            areaMagazzino.setAnnoMovimento(calendarData.get(Calendar.YEAR));
            areaMagazzino = areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, false);
        } catch (DocumentoAssenteAvvisaException | DocumentoAssenteBloccaException
                | DocumentiEsistentiPerAreaMagazzinoException e) {
            LOGGER.error("-->errore nel salvare l'area magazzino creata da areainstallazione", e);
            throw new GenericException("-->errore nel salvare l'area magazzino creata da areainstallazione", e);
        }
        return areaMagazzino;
    }

}
