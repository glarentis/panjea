package it.eurotn.panjea.fatturepa.interceptor;

import java.util.List;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.jboss.annotation.IgnoreDependency;

import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.fatturepa.exception.FatturaPAPresenteException;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturePAManager;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

/**
 * Interceptor che si occupa di cancellare l'eventuale area della fattura PA.
 *
 */
public class AreaMagazzinoFatturaPACancellaInterceptor {

    @EJB
    @IgnoreDependency
    protected FatturePAManager fatturePAManager;

    private void checkCanEdit(Integer idAreaMagazzino) {

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePAManager.caricaAreaMagazzinoFatturaPA(idAreaMagazzino);

        boolean canEdit = areaMagazzinoFatturaPA == null || areaMagazzinoFatturaPA.getNotificaCorrente() == null
                || areaMagazzinoFatturaPA.getNotificaCorrente().getStatoFattura() == StatoFatturaPA._DI
                || !areaMagazzinoFatturaPA.getNotificaCorrente().isEsitoPositivo();

        if (!canEdit) {
            throw new FatturaPAPresenteException();
        }
    }

    /**
     * PointCut.
     *
     * @param ctx
     *            contesto della chiamata
     * @return return per il metodo
     * @throws Exception
     *             eventuale exception
     */
    @SuppressWarnings("unchecked")
    @AroundInvoke
    public Object execute(InvocationContext ctx) throws Exception {
        for (Object parametro : ctx.getParameters()) {
            if (parametro != null && parametro instanceof AreaMagazzino) {
                Integer idAreaMagazzino = ((AreaMagazzino) parametro).getId();
                checkCanEdit(idAreaMagazzino);

                if (ctx.getMethod().getName().contains("cancella")) {
                    fatturePAManager.cancellaAreaMagazzinoFatturaPA(idAreaMagazzino);
                } else if (ctx.getMethod().getName().contains("cambiaStatoDaConfermatoInProvvisorio")) {
                    fatturePAManager.cancellaXMLFatturaPA(((AreaMagazzino) parametro).getId());
                }
            } else if (parametro != null && parametro instanceof List) {
                for (AreaMagazzino area : (List<AreaMagazzino>) parametro) {
                    checkCanEdit(area.getId());
                    fatturePAManager.cancellaAreaMagazzinoFatturaPA(area.getId());
                }
            }
        }
        return ctx.proceed();
    }
}
