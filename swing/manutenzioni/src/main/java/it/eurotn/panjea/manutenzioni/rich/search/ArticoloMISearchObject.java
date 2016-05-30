package it.eurotn.panjea.manutenzioni.rich.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;
import it.eurotn.panjea.manutenzioni.manager.articolimi.interfaces.ParametriRicercaArticoliMI;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class ArticoloMISearchObject extends AbstractSearchObject {

    public static final String PROPRIETA_CLIENTE_PARAM_KEY = "proprietaClienteParam";
    // Con disponibili si indicano quelli che non sono ancora associati ad una installazione
    public static final String SOLO_DISPONIBILI_PARAM_KEY = "soloDisponibiliParam";

    private IManutenzioniBD manutenzioniBD;

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        final ParametriRicercaArticoliMI parametriRicerca = new ParametriRicercaArticoliMI();
        switch (fieldSearch) {
        case "codice":
            parametriRicerca.setCodice(valueSearch);
            break;
        case "descrizione":
            parametriRicerca.setDescrizione(valueSearch);
            break;
        default:
            throw new UnsupportedOperationException("ricerca per campo " + fieldSearch + " non suportato");
        }

        final Map<String, Object> parametri = searchPanel.getMapParameters();
        if (parametri.get(PROPRIETA_CLIENTE_PARAM_KEY) != null) {
            parametriRicerca.setSoloProprietaCliente((boolean) parametri.get(PROPRIETA_CLIENTE_PARAM_KEY));
        }
        if (parametri.get(SOLO_DISPONIBILI_PARAM_KEY) != null) {
            parametriRicerca.setSoloDisponibili((boolean) parametri.get(SOLO_DISPONIBILI_PARAM_KEY));
        }

        return manutenzioniBD.ricercaArticoloMI(parametriRicerca);
    }

    /**
     * @return Returns the manutenzioniBD.
     */
    public IManutenzioniBD getManutenzioniBD() {
        return manutenzioniBD;
    }

    @Override
    public void openDialogPage(Object object) {
        dialogPageId = "";
        final ArticoloMI articoloParam = (ArticoloMI) object;
        if (articoloParam.getId() == null) {
            return;
        }
        final ArticoloMI articolo = manutenzioniBD.caricaArticoloMIById(articoloParam.getId());
        articolo.setVersion(null);

        final LifecycleApplicationEvent event = new OpenEditorEvent(articolo);
        Application.instance().getApplicationContext().publishEvent(event);

    }

    /**
     * @param manutenzioniBD
     *            The manutenzioniBD to set.
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }
}
