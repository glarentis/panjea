package it.eurotn.panjea.cosaro.sync;

import java.util.List;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

import it.eurotn.panjea.cosaro.CosaroSettings;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;

public class CosaroDistintaCaricoSyncInterceptor {
    private static final Logger LOGGER = Logger.getLogger(CosaroDistintaCaricoSyncInterceptor.class);

    @EJB(beanName = "Panjea.EsportaDistintaBilance")
    private EsportaDistinta esportaDistintaBilance;

    @EJB(beanName = "Panjea.EsportaDistintaGammaMeat")
    private EsportaDistinta esportaDistintaGammaMeat;

    @EJB
    private CosaroSettings cosaroSettings;

    /**
     * Esporta le distinte di carico a Gulliver.
     *
     * @param ctx
     *            context dell'interceptor
     * @return risultato della catena degli interceptor
     * @throws Exception
     *             eccezione generica
     */
    @AroundInvoke
    public Object esporta(InvocationContext ctx) throws Exception {
        @SuppressWarnings("unchecked")
        List<DistintaCarico> distinteSelezionate = (List<DistintaCarico>) ctx.proceed();
        if (cosaroSettings.isGammaMeatEnable()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Esporto distinte per gamma meat");
            }
            esportaDistintaGammaMeat.esporta(distinteSelezionate);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Esporto distinte per le bilance");        
            }
            esportaDistintaBilance.esporta(distinteSelezionate);
        }
        return distinteSelezionate;

    }
}