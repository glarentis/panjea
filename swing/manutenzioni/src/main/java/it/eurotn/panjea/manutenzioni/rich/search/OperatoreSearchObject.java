package it.eurotn.panjea.manutenzioni.rich.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.manager.operatori.ParametriRicercaOperatori;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class OperatoreSearchObject extends AbstractSearchObject {

    public static final String CARICATORE_PARAM_KEY = "caricatoreParam";
    public static final String TECNICO_PARAM_KEY = "tecnicoParam";

    private IManutenzioniBD manutenzioniBD;

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {

        final ParametriRicercaOperatori parametri = new ParametriRicercaOperatori();
        switch (fieldSearch) {
        case "codice":
            parametri.setCodice(valueSearch);
            break;
        case "nome":
        case "cognome":
        case "denominazione":
            parametri.setDenominazione(valueSearch);
            break;
        default:
            throw new UnsupportedOperationException("ricerca per campo " + fieldSearch + " non suportato");
        }

        if (searchPanel.getMapParameters().containsKey(TECNICO_PARAM_KEY)) {
            parametri.setTecnico((boolean) searchPanel.getMapParameters().get(TECNICO_PARAM_KEY));
        }
        if (searchPanel.getMapParameters().containsKey(CARICATORE_PARAM_KEY)) {
            parametri.setCaricatore((boolean) searchPanel.getMapParameters().get(CARICATORE_PARAM_KEY));
        }

        return manutenzioniBD.ricercaOperatori(parametri);
    }

    /**
     * @return Returns the manutenzioniBD.
     */
    public IManutenzioniBD getManutenzioniBD() {
        return manutenzioniBD;
    }

    @Override
    public void openEditor(Object object) {
        Operatore operatore = (Operatore) object;
        if (!operatore.isNew()) {
            operatore = manutenzioniBD.caricaOperatoreById(operatore.getId());
        }
        final LifecycleApplicationEvent event = new OpenEditorEvent(operatore);
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
