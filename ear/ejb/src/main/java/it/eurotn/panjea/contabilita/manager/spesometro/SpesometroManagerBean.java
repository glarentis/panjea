package it.eurotn.panjea.contabilita.manager.spesometro;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.SpesometroRecordBuilder;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.SpesometroRecordBuilderFactory;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.analitico.SpesometroAnaliticoBuilder;
import it.eurotn.panjea.contabilita.manager.spesometro.interfaces.SpesometroManager;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.SpesometroManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SpesometroManager")
public class SpesometroManagerBean implements SpesometroManager {

    private static final Logger LOGGER = Logger.getLogger(SpesometroManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    @EJB
    private AziendeManager aziendeManager;

    @Override
    public List<DocumentoSpesometro> caricaDocumentiSpesometro(ParametriCreazioneComPolivalente params) {

        AziendaAnagraficaDTO azienda = null;
        try {
            azienda = aziendeManager.caricaAziendaAnagrafica(getPrincipal().getCodiceAzienda());
        } catch (AnagraficaServiceException e) {
            LOGGER.error("-->errore nel caricare l'azienda corrente", e);
            throw new RuntimeException("errore nel caricare l'azienda corrente", e);
        }

        // all'utente faccio vedere sempre i documenti in forma analitica
        SpesometroAnaliticoBuilder builder = new SpesometroAnaliticoBuilder(panjeaDAO, azienda, params);

        return builder.getDocumenti();
    }

    @Override
    public byte[] genera(ParametriCreazioneComPolivalente params) {
        AziendaAnagraficaDTO azienda = null;
        try {
            azienda = aziendeManager.caricaAziendaAnagrafica(getPrincipal().getCodiceAzienda());
        } catch (AnagraficaServiceException e) {
            LOGGER.error("-->errore nel caricare l'azienda corrente", e);
            throw new RuntimeException("errore nel caricare l'azienda corrente", e);
        }

        SpesometroRecordBuilderFactory factory = new SpesometroRecordBuilderFactory();
        SpesometroRecordBuilder builder = factory.getBuilder(panjeaDAO, azienda, params);

        StringBuilder sb = new StringBuilder(10000);
        sb.append(builder.getRecordA());
        sb.append(builder.getRecordB());
        String recordC = builder.getRecordC();
        if (recordC != null) {
            sb.append(recordC);
        }
        String recordD = builder.getRecordD();
        if (recordD != null) {
            sb.append(recordD);
        }
        String recordE = builder.getRecordE();
        if (recordE != null) {
            sb.append(recordE);
        }
        sb.append(builder.getRecordZ());

        return sb.toString().getBytes();

    }

    /**
     * @return principal
     */
    private JecPrincipal getPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }
}
