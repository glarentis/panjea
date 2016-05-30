package it.eurotn.panjea.aton.exporter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.aton.domain.wrapper.GiacenzaAton;
import it.eurotn.panjea.aton.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;

@Stateful(mappedName = "Panjea.GIACENExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.GIACENExporter")
public class GIACENExporter extends AbstractDataExporter implements DataExporter {
    public static final String BEAN_NAME = "Panjea.GIACENExporter";

    @EJB
    private GiacenzaManager giacenzaManager;

    @EJB
    private ArticoloManager articoloManager;

    @EJB
    private DepositiManager depositiManager;

    @Override
    public void esporta() throws FileCreationException {

        List<GiacenzaAton> giacenzeAton = new ArrayList<GiacenzaAton>();

        List<DepositoLite> depositi = depositiManager.caricaDepositiAzienda();
        for (DepositoLite depositoLite : depositi) {
            Date currentDate = Calendar.getInstance().getTime();
            List<ArticoloRicerca> articoliRicerca = articoloManager.caricaArticoli();
            List<ArticoloLite> articoli = new ArrayList<ArticoloLite>();
            for (ArticoloRicerca articoloRicerca : articoliRicerca) {
                articoli.add(articoloRicerca.createProxyArticoloLite());
            }

            Map<Integer, ArticoloLite> articoliMap = new HashMap<Integer, ArticoloLite>();
            for (ArticoloLite articoloLite : articoli) {
                articoliMap.put(articoloLite.getId(), articoloLite);
            }

            Map<ArticoloLite, Double> giacenze = giacenzaManager.calcolaGiacenze(depositoLite, currentDate);

            for (Entry<ArticoloLite, Double> giacenza : giacenze.entrySet()) {
                giacenzeAton.add(new GiacenzaAton(articoliMap.get(giacenza.getKey().getId()), depositoLite,
                        BigDecimal.valueOf(giacenza.getValue())));
            }

        }

        StreamFactory factory = StreamFactory.newInstance();
        factory.load(getFilePathForTemplate());
        BeanWriter out = factory.createWriter("giacenze", getFileForExport());

        for (GiacenzaAton giacenza : giacenzeAton) {
            out.write(giacenza);
        }
        out.flush();
        out.close();
    }
}
