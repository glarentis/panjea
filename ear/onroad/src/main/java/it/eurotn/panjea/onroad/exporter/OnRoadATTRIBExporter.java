package it.eurotn.panjea.onroad.exporter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.Query;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;
import it.eurotn.panjea.onroad.domain.wrapper.ArticoloOnRoad;
import it.eurotn.panjea.onroad.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateful(mappedName = "Panjea.OnRoadATTRIBExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnRoadATTRIBExporter")
public class OnRoadATTRIBExporter extends AbstractDataExporter implements DataExporter {

    public static final String BEAN_NAME = "Panjea.OnRoadATTRIBExporter";

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private GiacenzaManager giacenzaManager;

    @Resource
    private SessionContext sessionContext;

    @EJB
    private DepositiManager depositiManager;

    @Override
    public void esporta() throws FileCreationException {
        Query query = panjeaDAO.prepareQuery(
                "select art from Articolo art left join fetch art.descrizioniLingua inner join fetch art.codiceIva where art.abilitato=true");
        @SuppressWarnings("unchecked")
        List<Articolo> articoli = query.getResultList();

        Deposito deposito = depositiManager.caricaDepositoPrincipale();

        Date currentDate = Calendar.getInstance().getTime();
        Map<ArticoloLite, Double> giacenze = giacenzaManager.calcolaGiacenze(deposito.getDepositoLite(), currentDate);

        StreamFactory factory = StreamFactory.newInstance();
        factory.load(getFilePathForTemplate());
        BeanWriter out = factory.createWriter("attributi", getFileForExport());

        for (Articolo articolo : articoli) {
            ArticoloLite articoloLite = new ArticoloLite(articolo);
            Double giacenza = giacenze.get(articoloLite);
            ArticoloOnRoad articoloAton = new ArticoloOnRoad(articolo, giacenza);
            out.write(articoloAton);
        }
        out.flush();
        out.close();
    }
}
