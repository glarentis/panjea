package it.eurotn.panjea.magazzino.manager.sincronizzazione;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.FatturazioneManager;
import it.eurotn.panjea.magazzino.manager.sincronizzazione.interfaces.SincronizzazioneManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.SincronizzazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SincronizzazioneManager")
public class SincronizzazioneManagerBean implements SincronizzazioneManager {

    public static final String URL_DESTINAZIONE_SETTINGS = "urlDestinazioneSyncDDT";

    @Resource
    private SessionContext sessionContext;

    /**
     * @uml.property name="preferenceService"
     * @uml.associationEnd
     */
    @EJB
    private PreferenceService preferenceService;

    @EJB
    private PanjeaDAO panjeaDAO;

    /**
     * @uml.property name="areaMagazzinoManager"
     * @uml.associationEnd
     */
    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    /**
     * @uml.property name="fatturazioneManager"
     * @uml.associationEnd
     */
    @EJB
    private FatturazioneManager fatturazioneManager;

    private static Logger logger = Logger.getLogger(SincronizzazioneManagerBean.class);

    /**
     * 
     * @return nome azienda loggata
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    @Override
    public String sincronizzaDDT(List<DatiGenerazione> datiGenerazione) {
        logger.debug("--> Enter sincronizzaDDT");
        String urlDestinazione;
        try {
            urlDestinazione = preferenceService.caricaPreference(URL_DESTINAZIONE_SETTINGS).getValore();
        } catch (Exception e) {
            throw new IllegalArgumentException("Chiave " + URL_DESTINAZIONE_SETTINGS
                    + " nella tabella di configurazione del server mancante (http://{ip}:{port}/panjeasync/importaDDT)");
        }

        if (urlDestinazione.isEmpty()) {
            throw new IllegalArgumentException("Chiave " + URL_DESTINAZIONE_SETTINGS
                    + " nella tabella di configurazione del server mancante (http://{ip}:{port}/panjeasync/importaDDT)");
        }

        // Recupero i DDT associati dai dati di fatturazione

        List<AreaMagazzino> fatture = new ArrayList<AreaMagazzino>();
        for (DatiGenerazione fatturazione : datiGenerazione) {
            List<AreaMagazzinoLite> fattureLite = fatturazioneManager
                    .caricaMovimentiPerFatturazione(fatturazione.getDataCreazione());
            for (AreaMagazzinoLite fatturaLite : fattureLite) {
                AreaMagazzino fattura = new AreaMagazzino();
                fattura.setId(fatturaLite.getId());
                fattura.setVersion(fatturaLite.getVersion());
                fatture.add(fattura);
            }
        }

        // List<AreaMagazzino> ddtCollegati = areaMagazzinoManager.caricaAreeMagazzinoCollegate(fatture);
        // StringBuilder sbDdt = new StringBuilder();
        // for (AreaMagazzino ddt : ddtCollegati) {
        // sbDdt.append(ddt.getId());
        // sbDdt.append(",");
        // }

        String[] context = new String[5];

        context[0] = "--context=Default";
        context[1] = "--context_param panjeaConnection=" + panjeaDAO.getJndiCurrentDataSource();
        context[2] = "--context_param azienda=" + getAzienda();
        context[3] = "--context_param destinazioneUrl=" + urlDestinazione;
        // context[4] = "--context_param ddts=" + sbDdt.substring(0, sbDdt.length() - 1);

        if (logger.isDebugEnabled()) {
            logger.debug("--> Lancio il task EsportaDDT con il seguente contesto");
        }
        // esportaddt.esportaddt_0_1.EsportaDDT esportaDDT = new esportaddt.esportaddt_0_1.EsportaDDT();
        // esportaDDT.runJob(context);

        logger.debug("--> Exit sincronizzaDDT");
        // return esportaDDT.getContext().getServletResponse();
        return null;
    }
}
