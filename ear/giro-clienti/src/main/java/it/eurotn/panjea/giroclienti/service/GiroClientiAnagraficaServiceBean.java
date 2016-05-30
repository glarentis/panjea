package it.eurotn.panjea.giroclienti.service;

import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.giroclienti.domain.GiroClientiSettings;
import it.eurotn.panjea.giroclienti.domain.GiroSedeCliente;
import it.eurotn.panjea.giroclienti.domain.ModalitaCopiaGiroClienti;
import it.eurotn.panjea.giroclienti.manager.interfaces.GiroClientiAnagraficaManager;
import it.eurotn.panjea.giroclienti.manager.interfaces.GiroClientiSettingsManager;
import it.eurotn.panjea.giroclienti.service.interfaces.GiroClientiAnagraficaService;
import it.eurotn.panjea.util.Giorni;

@Stateless(name = "Panjea.GiroClientiAnagraficaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.GiroClientiAnagraficaService")
public class GiroClientiAnagraficaServiceBean implements GiroClientiAnagraficaService {

    @EJB
    private GiroClientiAnagraficaManager giroClientiAnagraficaManager;

    @EJB
    private GiroClientiSettingsManager giroClientiSettingsManager;

    @Override
    public void cancellaGiroSedeCliente(GiroSedeCliente giroSedeCliente) {
        giroClientiAnagraficaManager.cancellaGiroSedeCliente(giroSedeCliente);

    }

    @Override
    public void cancellaGiroSedeCliente(Integer idEntita) {
        giroClientiAnagraficaManager.cancellaGiroSedeCliente(idEntita);
    }

    @Override
    public void cancellaGiroSedeCliente(Integer idSedeEntita, Giorni giorno, Date ora) {
        giroClientiAnagraficaManager.cancellaGiroSedeCliente(idSedeEntita, giorno, ora);
    }

    @Override
    public GiroClientiSettings caricaGiroClientiSettings() {
        return giroClientiSettingsManager.caricaGiroClientiSettings();
    }

    @Override
    public List<GiroSedeCliente> caricaGiroSedeCliente(Integer idEntita) {
        return giroClientiAnagraficaManager.caricaGiroSedeCliente(idEntita);
    }

    @Override
    public void copiaGiroSedeClienti(Integer idUtente, Giorni giorno, Integer idUtenteDestinazione,
            Giorni giornoDestinazione, ModalitaCopiaGiroClienti modalitaCopia) {
        giroClientiAnagraficaManager.copiaGiroSedeClienti(idUtente, giorno, idUtenteDestinazione, giornoDestinazione,
                modalitaCopia);
    }

    @Override
    public GiroClientiSettings salvaGiroClientiSettings(GiroClientiSettings giroClientiSettings) {
        return giroClientiSettingsManager.salvaGiroClientiSettings(giroClientiSettings);
    }

    @Override
    public GiroSedeCliente salvaGiroSedeCliente(GiroSedeCliente giroSedeCliente) {
        return giroClientiAnagraficaManager.salvaGiroSedeCliente(giroSedeCliente);
    }

    @Override
    public void salvaGiroSedeCliente(List<GiroSedeCliente> giri) {
        giroClientiAnagraficaManager.salvaGiroSedeCliente(giri);
    }
}
