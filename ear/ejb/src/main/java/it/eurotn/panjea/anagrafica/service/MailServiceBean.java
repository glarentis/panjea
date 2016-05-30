package it.eurotn.panjea.anagrafica.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.ImportazioneMailManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.MailManager;
import it.eurotn.panjea.anagrafica.service.interfaces.MailService;
import it.eurotn.panjea.anagrafica.util.MailDTO;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaMail;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.MailService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.MailService")
public class MailServiceBean implements MailService {

    @EJB
    protected MailManager mailManager;

    @EJB
    private ImportazioneMailManager importazioneMailManager;

    @Override
    public List<MailDTO> caricaMails(ParametriRicercaMail parametriRicercaMail) {
        return mailManager.caricaMails(parametriRicercaMail);
    }

    @Override
    public Integer caricaNumeroMailDaImportare() {
        return importazioneMailManager.caricaNumeroMailDaImportare();
    }

    @Override
    public void importaMail() {
        importazioneMailManager.importaMail();
    }

    @Override
    public void salvaMail(byte[] emlFile, EntitaLite entita, boolean inviata) {
        mailManager.salvaMail(emlFile, entita, inviata);
    }
}
