package it.eurotn.panjea.manutenzioni.rich.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.manutenzioni.domain.UbicazioneInstallazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class UbicazioneInstallazioneSearchObject extends AbstractSearchObject {
    private IManutenzioniBD manutenzioniBD;

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @Override
    public List<?> getData(final String fieldSearch, final String valueSearch) {
        List<UbicazioneInstallazione> ubicazioni = manutenzioniBD.caricaUbicazioniInstallazione();
        if (valueSearch == null) {
            return ubicazioni;
        }
        final String valueSearchFinal = valueSearch.substring(0, valueSearch.length() - 1).toLowerCase();
        CollectionUtils.filter(ubicazioni, new Predicate<UbicazioneInstallazione>() {

            @Override
            public boolean evaluate(UbicazioneInstallazione ubicazione) {
                return ubicazione.getDescrizione().toLowerCase().startsWith(valueSearchFinal);
            }
        });
        return ubicazioni;
    }

    /**
     * @param manutenzioniBD
     *            The manutenzioniBD to set.
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

}
