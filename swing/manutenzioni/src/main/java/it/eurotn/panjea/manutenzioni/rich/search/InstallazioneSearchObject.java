package it.eurotn.panjea.manutenzioni.rich.search;

import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.manutenzioni.manager.installazioni.ParametriRicercaInstallazioni;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class InstallazioneSearchObject extends AbstractSearchObject {
    public static final String PROPRIETA_CLIENTE_LITE_PARAM_KEY = "clienteLite";
    public static final String PROPRIETA_CLIENTE_PARAM_KEY = "cliente";
    public static final String PROPRIETA_SEDE_PARAM_KEY = "sede";
    private IManutenzioniBD manutenzioniBD;

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return null;
    }

    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        final ParametriRicercaInstallazioni parametriRicerca = new ParametriRicercaInstallazioni();

        switch (fieldSearch) {
        case "codice":
            parametriRicerca.setCodice(valueSearch);
            break;
        case "descrizione":
            parametriRicerca.setDescrizione(valueSearch);
            break;
        default:
            throw new UnsupportedOperationException();
        }
        final Map<String, Object> parametri = searchPanel.getMapParameters();
        if (parametri.get(PROPRIETA_CLIENTE_PARAM_KEY) != null) {
            parametriRicerca.setIdEntita(((Entita) parametri.get(PROPRIETA_CLIENTE_PARAM_KEY)).getId());
        }
        if (parametri.get(PROPRIETA_CLIENTE_LITE_PARAM_KEY) != null) {
            final EntitaLite entitaLite = ((EntitaLite) parametri.get(PROPRIETA_CLIENTE_LITE_PARAM_KEY));
            parametriRicerca.setIdEntita(entitaLite != null ? entitaLite.getId() : null);
        }
        if (parametri.get(PROPRIETA_SEDE_PARAM_KEY) != null) {
            final SedeEntita sedeEntita = ((SedeEntita) parametri.get(PROPRIETA_SEDE_PARAM_KEY));
            parametriRicerca.setIdSedeEntita(sedeEntita != null ? sedeEntita.getId() : null);
        }
        return manutenzioniBD.ricercaInstallazioniByParametri(parametriRicerca);
    }

    /**
     * @param manutenzioniBD
     *            The manutenzioniBD to set.
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

}
