/**
 *
 */
package it.eurotn.panjea.magazzino.rich.search;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.rich.search.AbstractSearchObject;

/**
 * SearchObject per il caricamento di {@link TipoAreaMagazzino} da utilizzare nelle ricerche.
 *
 * @author adriano
 * @version 1.0, 29/ago/2008
 */
public class TipoAreaMagazzinoSearchObject extends AbstractSearchObject {

    private static Logger logger = Logger.getLogger(TipoAreaMagazzinoSearchObject.class);

    private static final String SEARCH_OBJECT_ID = "tipoAreaMagazzinoSearchObject";
    public static final String PARAMETRO_TIPO_AREA_PRODUZIONE = "tipoAreaProduzione";
    public static final String PARAMETRO_GESTIONE_VENDING = "gestioneVending";
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    /**
     * Costruttore.
     */
    public TipoAreaMagazzinoSearchObject() {
        super(SEARCH_OBJECT_ID);
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return null;
    }

    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        logger.debug("--> Enter getData");
        List<TipoAreaMagazzino> tipiAreaMagazzino = magazzinoDocumentoBD.caricaTipiAreaMagazzino(fieldSearch,
                valueSearch, false);

        Map<String, Object> parameters = searchPanel.getMapParameters();

        Iterator<TipoAreaMagazzino> it = tipiAreaMagazzino.iterator();
        boolean tipoProduzione = parameters.containsKey(PARAMETRO_TIPO_AREA_PRODUZIONE);
        Boolean gestioneVending = (Boolean) parameters.get(PARAMETRO_GESTIONE_VENDING);
        if (gestioneVending == null) {
            gestioneVending = false;
        }

        while (it.hasNext()) {
            TipoAreaMagazzino tipoAreaMagazzino = it.next();
            if (tipoProduzione && !TipoMovimento.CARICO_PRODUZIONE.equals(tipoAreaMagazzino.getTipoMovimento())) {
                it.remove();
                continue;
            }
            if (gestioneVending && !tipoAreaMagazzino.isGestioneVending()) {
                it.remove();
                continue;
            }
        }

        logger.debug("--> Exit getData");

        System.out.println("LUNG. " + tipiAreaMagazzino.size());
        return tipiAreaMagazzino;
    }

    @Override
    public Object getValueObject(Object object) {
        TipoAreaMagazzino tam = (TipoAreaMagazzino) object;
        if (tam.getId() != null) {
            tam = magazzinoDocumentoBD.caricaTipoAreaMagazzino(tam.getId());
        }
        return tam;
    }

    @Override
    public void openEditor(Object object) {
        TipoAreaMagazzino tipoAreaMagazzino = (TipoAreaMagazzino) object;
        TipoDocumento tipoDocumento = tipoAreaMagazzino.getTipoDocumento();
        LifecycleApplicationEvent event = new OpenEditorEvent(tipoDocumento);
        Application.instance().getApplicationContext().publishEvent(event);
    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

}
