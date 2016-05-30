package it.eurotn.panjea.rich.editors.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumentoDTO;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.editors.AbstractEditorDialogPage;

import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.settings.SettingsManager;

/**
 * Editor personalizzato per l'area magazzino.
 *
 * @author adriano
 * @version 1.0, 05/nov/2008
 *
 */
public abstract class AreaDocumentoEditor<T extends IAreaDocumento, S extends IAreaDocumentoDTO>
        extends AbstractEditorDialogPage implements IEditorListener {

    private static Logger logger = Logger.getLogger(AreaDocumentoEditor.class);

    private IAreaDocumentoBD<T> areaDocumentoBD;

    private SettingsManager settingsManager = null;

    /**
     * Costruttore.
     */
    public AreaDocumentoEditor() {
        logger.debug("--> Enter costruttore AreaDocumentoEditor");
        logger.debug("--> Exit costruttore AreaDocumentoEditor");
    }

    // @Override
    // protected JecCompositeDialogPage createCompositeDialogPage() {
    // return new AreaMagazzinoDockingCompositeDialogPage(getDialogPageId(), settingsManager);
    // }

    /**
     * Gestisce l'evento LifecycleApplicationEvent.DELETED.
     *
     * @param areaMagazzinoFullDTO
     *            l'areaMagazzinoFullDTO dell'editor
     * @param sourceObject
     *            l'oggetto ricevuto dall'evento
     */
    protected void handleDeleteEvent(IAreaDocumento areaDocumento) {
        @SuppressWarnings("unchecked")
        S areaDocumentoDTO = (S) getEditorInput();

        // boolean chiudiEditor = !areaDocumento.isNew() && !areaDocumentoDTO.getAreaDocumento().isNew()
        // && areaDocumento.getId().equals(areaDocumentoDTO.getAreaDocumento().getId());
        //
        // controllo se è stata cancellata un'area contabile. In questo caso devo toglierla dal full dto dell'area
        // magazzino se ce l'ha.
        // if (sourceObject instanceof AreaContabileFullDTO && areaMagazzinoFullDTO != null
        // && areaMagazzinoFullDTO.getAreaContabileLite() != null) {
        // Integer idAreaContabileCancellata = ((AreaContabileFullDTO) sourceObject).getId();
        // if (areaMagazzinoFullDTO.getAreaContabileLite().getId() != null
        // && areaMagazzinoFullDTO.getAreaContabileLite().getId().equals(idAreaContabileCancellata)) {
        // areaMagazzinoFullDTO.setAreaContabileLite(null);
        // compositeDialogPage.setCurrentObject(currentObject);
        // }
        // }
        //
        // if (chiudiEditor) {
        // PanjeaLifecycleApplicationEvent deleteEvent = new PanjeaLifecycleApplicationEvent(
        // LifecycleApplicationEvent.DELETED, areaDocumentoDTO.getAreaDocumento(), this);
        // Application.instance().getApplicationContext().publishEvent(deleteEvent);
        // }
    }

    /**
     * Gestisce l'evento LifecycleApplicationEvent.MODIFIED.
     *
     * @param areaMagazzinoFullDTO
     *            l'areaMagazzinoFullDTO dell'editor
     * @param sourceObject
     *            l'oggetto ricevuto dall'evento
     */
    private void handleModifyEvent(IAreaDocumento areaDocumento) {
        // @SuppressWarnings("unchecked")
        // T areaDocumentoDTO = (T) getEditorInput();
        // Documento docCorrente = areaDocumentoDTO.getAreaDocumento().getDocumento();
        // if (sourceObject instanceof Documento && sourceObject.equals(docCorrente)) {
        // initialize(areaMagazzinoFullDTO.getAreaMagazzino());
        // }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(Object editorObject) {
        logger.debug("--> Enter initialize" + Objects.toString(editorObject, "oggetto nullo"));
        if ((editorObject instanceof IAreaDocumento)) {
            editorObject = areaDocumentoBD.caricaAreaDocumento((T) editorObject);
        } else {
            throw new IllegalArgumentException("L'editor object deve implementare IAreaDocumento");
        }
        super.initialize(editorObject);
    }

    @Override
    public void onEditorEvent(ApplicationEvent event) {
        logger.debug("--> Enter onEditorEvent . HashCode editor " + this.hashCode());

        PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;

        if (panjeaEvent.getSource() instanceof IAreaDocumento) {
            IAreaDocumento areaDocumento = (IAreaDocumento) panjeaEvent.getSource();
            String evtName = panjeaEvent.getEventType();
            // Se non ho il sourceContainer è un evento che proviene esternamente all'editor.
            boolean externalEvent = getDialogPages().indexOf(panjeaEvent.getSourceContainer()) == -1;

            if (PanjeaLifecycleApplicationEvent.DELETED.equals(evtName)) {
                handleDeleteEvent(areaDocumento);
            } else if (PanjeaLifecycleApplicationEvent.MODIFIED.equals(evtName) && externalEvent) {
                handleModifyEvent(areaDocumento);
            }
            logger.debug("--> Exit onEditorEvent");
        }
    }

    /**
     * @param areaDocumentoBD
     *            The areaDocumentoBD to set.
     */
    public void setAreaDocumentoBD(IAreaDocumentoBD areaDocumentoBD) {
        this.areaDocumentoBD = areaDocumentoBD;
    }

    /**
     * @param settingsManager
     *            the settingsManager to set
     */
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }
}
