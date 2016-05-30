package it.eurotn.panjea.magazzino.rich.search;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

/**
 * @author Leonardo
 */
public class MezzoTrasportoSearchObject extends AbstractSearchObject {

    public static final String SENZA_CARICATORE_KEY = "mezziSenzaCaricatore";

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

    /**
     * Costruttore di default.
     */
    public MezzoTrasportoSearchObject() {
        super("mezzoTrasportoSearchObject");
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return null;
    }

    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        Map<String, Object> parameters = searchPanel.getMapParameters();
        boolean senzaCaricatore = ObjectUtils.defaultIfNull((Boolean) parameters.get(SENZA_CARICATORE_KEY), false);
        List<MezzoTrasporto> mezzi = magazzinoAnagraficaBD.caricaMezziTrasporto(valueSearch, true,
                (EntitaLite) parameters.get("entita"), senzaCaricatore);
        return mezzi;
    }

    /**
     * @param magazzinoAnagraficaBD
     *            The magazzinoAnagraficaBD to set.
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

}
