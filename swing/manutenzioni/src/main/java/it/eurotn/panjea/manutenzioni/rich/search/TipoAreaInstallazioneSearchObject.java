package it.eurotn.panjea.manutenzioni.rich.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.manutenzioni.domain.documento.TipoAreaInstallazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class TipoAreaInstallazioneSearchObject extends AbstractSearchObject {
    private IManutenzioniBD manutenzioniBD;

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @Override
    public List<?> getData(final String fieldSearch, final String valueSearch) {
        List<TipoAreaInstallazione> tai = manutenzioniBD.caricaTipiAreeInstallazione();
        if (valueSearch == null) {
            return tai;
        }
        final String valueSearchWithoutWildCard = valueSearch.replaceAll("%", "");
        CollectionUtils.filter(tai, new Predicate<TipoAreaInstallazione>() {

            @Override
            public boolean evaluate(TipoAreaInstallazione tipoAreaInstallazione) {
                switch (fieldSearch) {
                case "tipoDocumento.codice":
                    return tipoAreaInstallazione.getTipoDocumento().getCodice().toUpperCase()
                            .startsWith(valueSearchWithoutWildCard.toUpperCase());
                case " tipoDocumento.descrizione":
                    return tipoAreaInstallazione.getTipoDocumento().getDescrizione().toUpperCase()
                            .startsWith(valueSearchWithoutWildCard.toUpperCase());
                default:
                    return true;
                }
            }
        });
        return tai;
    }

    @Override
    public boolean isOpenNewObjectManaged() {
        return false;
    }

    @Override
    public void openEditor(Object object) {
        TipoAreaInstallazione tipoAreaInstallazione = (TipoAreaInstallazione) object;
        TipoDocumento tipoDocumento = tipoAreaInstallazione.getTipoDocumento();
        LifecycleApplicationEvent event = new OpenEditorEvent(tipoDocumento);
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
