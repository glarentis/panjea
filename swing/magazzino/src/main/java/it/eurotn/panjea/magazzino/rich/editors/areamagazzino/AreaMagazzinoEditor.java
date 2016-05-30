/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.settings.SettingsManager;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractEditorDialogPage;

/**
 * Editor personalizzato per l'area magazzino.
 *
 * @author adriano
 * @version 1.0, 05/nov/2008
 *
 */
public class AreaMagazzinoEditor extends AbstractEditorDialogPage implements IEditorListener {

    private static Logger logger = Logger.getLogger(AreaMagazzinoEditor.class);

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private SettingsManager settingsManager = null;

    /**
     * Costruttore.
     */
    public AreaMagazzinoEditor() {
        logger.debug("--> costruita area magazzino editor");
    }

    @Override
    protected JecCompositeDialogPage createCompositeDialogPage() {
        return new AreaMagazzinoDockingCompositeDialogPage(getDialogPageId(), settingsManager);
    }

    /**
     * Gestisce l'evento LifecycleApplicationEvent.DELETED.
     * 
     * @param areaMagazzinoFullDTO
     *            l'areaMagazzinoFullDTO dell'editor
     * @param sourceObject
     *            l'oggetto ricevuto dall'evento
     */
    private void handleDeleteEvent(AreaMagazzinoFullDTO areaMagazzinoFullDTO, Object sourceObject) {
        boolean chiudiEditor = (sourceObject instanceof AreaMagazzino && areaMagazzinoFullDTO != null
                && ((AreaMagazzino) sourceObject).getId() != null
                && areaMagazzinoFullDTO.getAreaMagazzino().getId() != null
                && ((AreaMagazzino) sourceObject).getId().equals(areaMagazzinoFullDTO.getAreaMagazzino().getId()));
        // controllo se è stata cancellata un'area contabile. In questo caso devo toglierla dal full dto dell'area
        // magazzino se ce l'ha.
        if (sourceObject instanceof AreaContabileFullDTO && areaMagazzinoFullDTO != null
                && areaMagazzinoFullDTO.getAreaContabileLite() != null) {
            Integer idAreaContabileCancellata = ((AreaContabileFullDTO) sourceObject).getId();
            if (areaMagazzinoFullDTO.getAreaContabileLite().getId() != null
                    && areaMagazzinoFullDTO.getAreaContabileLite().getId().equals(idAreaContabileCancellata)) {
                areaMagazzinoFullDTO.setAreaContabileLite(null);
                compositeDialogPage.setCurrentObject(getEditorInput());
            }
        }

        if (chiudiEditor) {
            PanjeaLifecycleApplicationEvent deleteEvent = new PanjeaLifecycleApplicationEvent(
                    LifecycleApplicationEvent.DELETED, areaMagazzinoFullDTO.getAreaMagazzino(), this);
            Application.instance().getApplicationContext().publishEvent(deleteEvent);
        }
    }

    /**
     * Gestisce l'evento PanjeaLifecycleApplicationEvent ricevuto.
     * 
     * @param panjeaEvent
     *            l'evento da gestire
     */
    private void handleEvent(PanjeaLifecycleApplicationEvent panjeaEvent) {
        String evtName = panjeaEvent.getEventType();
        boolean exteranlEvent = getDialogPages().indexOf(panjeaEvent.getSourceContainer()) == -1;
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = ((AreaMagazzinoFullDTO) getEditorInput());
        if (PanjeaLifecycleApplicationEvent.DELETED.equals(evtName)) {
            handleDeleteEvent(areaMagazzinoFullDTO, panjeaEvent.getSource());
        } else if (PanjeaLifecycleApplicationEvent.MODIFIED.equals(evtName) && exteranlEvent) {
            handleModifyEvent(areaMagazzinoFullDTO, panjeaEvent.getSource());
        }
    }

    /**
     * Gestisce l'evento LifecycleApplicationEvent.MODIFIED.
     * 
     * @param areaMagazzinoFullDTO
     *            l'areaMagazzinoFullDTO dell'editor
     * @param sourceObject
     *            l'oggetto ricevuto dall'evento
     */
    private void handleModifyEvent(AreaMagazzinoFullDTO areaMagazzinoFullDTO, Object sourceObject) {
        Documento docCorrente = areaMagazzinoFullDTO.getAreaMagazzino().getDocumento();
        if (sourceObject instanceof Documento && sourceObject.equals(docCorrente)) {
            initialize(areaMagazzinoFullDTO.getAreaMagazzino());
        }
    }

    @Override
    public void initialize(Object editorObject) {
        if (editorObject instanceof AreaMagazzino) {
            editorObject = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO((AreaMagazzino) editorObject);
        }
        super.initialize(editorObject);
    }

    @Override
    public void onEditorEvent(ApplicationEvent event) {
        logger.debug("--> Enter onEditorEvent   editor " + this.hashCode());
        // Controllo se l'evento è una cancellazione dell'area contabile
        // (che lancia un close editor event)
        PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
        handleEvent(panjeaEvent);
        logger.debug("--> Exit onEditorEvent");
    }

    /**
     * setter per magazzinoDocumentoBD.
     * 
     * @param magazzinoDocumentoBD
     *            magazzino documento bd
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    /**
     * @param settingsManager
     *            the settingsManager to set
     */
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }
}
