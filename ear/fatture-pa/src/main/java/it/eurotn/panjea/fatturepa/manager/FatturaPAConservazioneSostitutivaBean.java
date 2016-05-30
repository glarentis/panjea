package it.eurotn.panjea.fatturepa.manager;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.fatturepa.manager.interfaces.FatturaPAConservazioneSostitutiva;
import it.eurotn.panjea.fatturepa.solutiondoc.manager.interfaces.SolutionDocFatturaPAConservazioneManager;

@Stateless(name = "Panjea.FatturaPAConservazioneSostitutiva")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.FatturaPAConservazioneSostitutiva")
public class FatturaPAConservazioneSostitutivaBean implements FatturaPAConservazioneSostitutiva {

    @EJB
    private SolutionDocFatturaPAConservazioneManager solutionDocFatturaPAConservazioneManager;

    @Override
    public void conservaXMLFatturePA() {

        solutionDocFatturaPAConservazioneManager.conservaXMLFatturePA();
    }

}
