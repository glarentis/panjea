package it.eurotn.panjea.mrp.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import commonj.work.WorkItem;
import commonj.work.WorkManager;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.manager.interfaces.MrpBomExplosionManager;
import it.eurotn.panjea.mrp.manager.interfaces.MrpManager;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;

@Stateless(name = "Panjea.MrpBomExplosionManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.MrpBomExplosionManager")
@PermitAll
public class MrpBomExplosionManagerBean implements MrpBomExplosionManager {

    private static Logger logger = Logger.getLogger(MrpBomExplosionManagerBean.class);

    @EJB
    private MrpManager mrpManager;

    @Resource(mappedName = "java:worker/mrpWorker")
    private WorkManager workManager;

    /**
     * Esplodi l'adjacent list.
     *
     * @param adjacentListReadOnly
     *            adjacentListReadOnly
     * @return Map<String, Bom>
     */
    private Map<ArticoloConfigurazioneKey, Bom> esplodi(List<Object[]> adjacentListReadOnly) {
        final BomResult result = new BomResult();
        int page = 100;
        if (page > adjacentListReadOnly.size()) {
            page = adjacentListReadOnly.size();
        }
        int start = 0;
        int end = page;
        List<WorkItem> workers = new ArrayList<WorkItem>();
        try {
            while (end <= adjacentListReadOnly.size()) {
                // Ogni task può contenere varie distinte. L'ultima distinta però deve essere
                // completa per non dover
                // fare dei merge dei risultati dei vari task
                while (end < adjacentListReadOnly.size() - 1 && adjacentListReadOnly.get(end)[COLUMN_ID_DISTINTA]
                        .equals(adjacentListReadOnly.get(end + 1)[COLUMN_ID_DISTINTA])) {
                    end++;
                }
                // devo incrementare di uno visto che la lista viene letta da start a end-1, ma non
                // quando sono alla
                // fine della lista, altrimenti viene sollevata una AIOOBE
                if (end < adjacentListReadOnly.size()) {
                    end++;
                }

                BomExplosionWork work = new BomExplosionWork(adjacentListReadOnly, start, end, result);
                workers.add(workManager.schedule(work));
                start = end;
                if (end == adjacentListReadOnly.size()) {
                    break;
                }
                end = (end + page) > adjacentListReadOnly.size() ? adjacentListReadOnly.size() : end + page;
            }
            workManager.waitForAll(workers, 200000);
        } catch (Exception ex) {
            logger.error("-->errore nellìesplodere la distinta ", ex);
            throw new RuntimeException(ex);
        }
        logger.debug("--> Exit esplodiBoms");
        return result.getResult();
    }

    @Override
    public Map<ArticoloConfigurazioneKey, Bom> esplodiBoms() {
        logger.debug("--> Enter esplodiBoms");
        List<Object[]> adjacentListReadOnly = Collections.unmodifiableList(mrpManager.caricaBomBase());
        if (logger.isDebugEnabled()) {
            logger.debug("--> Righe nella adjacentList " + adjacentListReadOnly.size());
        }
        // divido la lista in diversi task.
        Map<ArticoloConfigurazioneKey, Bom> esplosi = esplodi(adjacentListReadOnly);
        return esplosi;
    }

    @Override
    public Map<ArticoloConfigurazioneKey, Bom> esplodiBoms(Set<Integer> configurazioniUtilizzate) {
        Map<ArticoloConfigurazioneKey, Bom> boms = new HashMap<>();
        Map<ArticoloConfigurazioneKey, Bom> bomsBase = esplodiBoms();
        boms.putAll(bomsBase);
        if (configurazioniUtilizzate != null && configurazioniUtilizzate.size() > 0) {
            List<Object[]> bomsConfigurazioni = mrpManager.caricaBomConfigurazioni(configurazioniUtilizzate);
            Map<ArticoloConfigurazioneKey, Bom> esplosi = esplodi(bomsConfigurazioni);
            boms.putAll(esplosi);
        }

        return boms;
    }
}
