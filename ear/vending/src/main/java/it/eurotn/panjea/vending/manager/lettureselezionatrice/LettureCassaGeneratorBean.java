package it.eurotn.panjea.vending.manager.lettureselezionatrice;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.Cassa;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice.StatoLettura;
import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.panjea.vending.domain.RigaLetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.RigaMovimentoCassa;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces.LettureCassaGenerator;
import it.eurotn.panjea.vending.manager.movimenticassa.interfaces.MovimentiCassaManager;

@Stateless(name = "Panjea.LettureCassaGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LettureCassaGenerator")
public class LettureCassaGeneratorBean implements LettureCassaGenerator {

    private static final Logger LOGGER = Logger.getLogger(LettureCassaGeneratorBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private MovimentiCassaManager movimentiCassaManager;

    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void creaMovimentazioneCassa(LetturaSelezionatrice lettura) {

        Query query = panjeaDAO.prepareQuery(
                "select riga from RigaLetturaSelezionatrice riga where riga.letturaSelezionatrice.id = :paramProgressivo");
        query.setParameter("paramProgressivo", lettura.getId());

        List<RigaLetturaSelezionatrice> righe = null;
        try {
            righe = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle righe lettura selezionatrice.", e);
            throw new GenericException("errore durante il caricamento delle righe lettura selezionatrice.", e);
        }

        if (lettura.getStatoLettura() == StatoLettura.TRASFERIMENTO_CASSA) {
            creaMovimentazioneDaLettura(lettura.getCassaOrigine(), lettura.getDataRifornimento(), righe, false);
        }

        creaMovimentazioneDaLettura(lettura.getCassaDestinazione(), lettura.getDataRifornimento(), righe, true);

        try {
            panjeaDAO.delete(lettura);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione della lettura selezionatrice", e);
            throw new GenericException("errore durante la cancellazione della lettura selezionatrice", e);
        }

    }

    private void creaMovimentazioneDaLettura(Cassa cassa, Date data, List<RigaLetturaSelezionatrice> righeLettura,
            boolean isEntrata) {

        MovimentoCassa movimento = new MovimentoCassa();
        movimento.setCassa(cassa);
        movimento.setData(data);
        movimento.setRighe(new TreeSet<>(new MovimentoCassa.RigheMovimentoCassaComparator()));
        for (RigaLetturaSelezionatrice rigaLettura : righeLettura) {
            RigaMovimentoCassa rigaMov = new RigaMovimentoCassa();
            rigaMov.setGettone(rigaLettura.getGettone());
            rigaMov.setMovimentoCassa(movimento);
            rigaMov.setQuantitaEntrata(isEntrata ? rigaLettura.getQuantita() : 0);
            rigaMov.setQuantitaUscita(isEntrata ? 0 : rigaLettura.getQuantita());
            movimento.getRighe().add(rigaMov);
        }
        movimentiCassaManager.salva(movimento);
    }
}
