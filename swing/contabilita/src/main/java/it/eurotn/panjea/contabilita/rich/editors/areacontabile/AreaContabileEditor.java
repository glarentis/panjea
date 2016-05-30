package it.eurotn.panjea.contabilita.rich.editors.areacontabile;

import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.dialog.DialogPage;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractEditorDialogPage;

public class AreaContabileEditor extends AbstractEditorDialogPage implements IEditorListener {

    public class AreaContabileDockingCompositeDialogPage extends DockingCompositeDialogPage {

        private static final String DOCKING_COMPOSITE_DIALOG_PAGE_ID = "areaContabileDockingCompositePageID";
        private final AreaContabileEditorController areaContabileEditorController;

        /**
         * Costruttore.
         */
        public AreaContabileDockingCompositeDialogPage() {
            super(DOCKING_COMPOSITE_DIALOG_PAGE_ID);
            areaContabileEditorController = new AreaContabileEditorController(this);
        }

        @Override
        public void addPage(DialogPage page) {
            super.addPage(page);
            areaContabileEditorController.addPage(page);
        }
    }

    private IContabilitaBD contabilitaBD;

    private AreaContabileDockingCompositeDialogPage areaContabileDockingCompositeDialogPage = null;

    @Override
    protected JecCompositeDialogPage createCompositeDialogPage() {
        if (areaContabileDockingCompositeDialogPage == null) {
            areaContabileDockingCompositeDialogPage = new AreaContabileDockingCompositeDialogPage();
        }
        return areaContabileDockingCompositeDialogPage;
    }

    /**
     * Gestisce l'evento LifecycleApplicationEvent.DELETED.
     * 
     * @param areaContabileFullDTO
     *            l'areaContabileFullDTO dell'editor
     * @param sourceObject
     *            l'oggetto ricevuto dall'evento
     */
    private void handleDeleteEvent(AreaContabileFullDTO areaContabileFullDTO, Object sourceObject) {
        boolean chiudiEditor = false;
        if (sourceObject instanceof AreaTesoreria) {
            AreaTesoreria areaTesoreria = (AreaTesoreria) sourceObject;
            chiudiEditor = areaContabileFullDTO.getAreaTesoreria() != null
                    && areaContabileFullDTO.getAreaTesoreria().getId().equals(areaTesoreria.getId());
        } else
            if (sourceObject instanceof AreaMagazzinoFullDTO && areaContabileFullDTO.getAreaMagazzinoLite() != null) {
            AreaMagazzino areaMagazzino = ((AreaMagazzinoFullDTO) sourceObject).getAreaMagazzino();
            chiudiEditor = areaContabileFullDTO.getAreaMagazzinoLite().getId().equals(areaMagazzino.getId());
        } else if (sourceObject instanceof AreaMagazzino && areaContabileFullDTO.getAreaMagazzinoLite() != null) {
            AreaMagazzino areaMagazzino = (AreaMagazzino) sourceObject;
            chiudiEditor = areaContabileFullDTO.getAreaMagazzinoLite().getId().equals(areaMagazzino.getId());
        }
        if (chiudiEditor) {
            PanjeaLifecycleApplicationEvent deleteEvent = new PanjeaLifecycleApplicationEvent(
                    LifecycleApplicationEvent.DELETED, areaContabileFullDTO.getAreaContabile(), this);
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
        AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) getEditorInput();
        if (LifecycleApplicationEvent.DELETED.equals(evtName)) {
            handleDeleteEvent(areaContabileFullDTO, panjeaEvent.getSource());
        } else if (LifecycleApplicationEvent.MODIFIED.equals(evtName) && exteranlEvent) {
            handleModifyEvent(areaContabileFullDTO, panjeaEvent.getSource());
        }
    }

    /**
     * Gestisce l'evento LifecycleApplicationEvent.MODIFIED.
     * 
     * @param areaContabileFullDTO
     *            l'areaContabileFullDTO dell'editor
     * @param sourceObject
     *            l'oggetto ricevuto dall'evento
     */
    private void handleModifyEvent(AreaContabileFullDTO areaContabileFullDTO, Object sourceObject) {
        Documento docCorrente = areaContabileFullDTO.getAreaContabile().getDocumento();
        if (sourceObject instanceof Documento && sourceObject.equals(docCorrente)) {
            initialize(areaContabileFullDTO.getAreaContabile().getAreaContabileLite());
        }
    }

    @Override
    public void initialize(Object editorObject) {
        if (editorObject instanceof AreaContabileLite) {
            editorObject = contabilitaBD.caricaAreaContabileFullDTO(((AreaContabileLite) editorObject).getId());
        }
        super.initialize(editorObject);
    }

    @Override
    public void onEditorEvent(ApplicationEvent event) {
        // Se viene cancellata l'area magazzino legata a quest'area contabile mi chiudo.
        // L'area magazzino cancella sempre l'ìarea contabile.
        PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
        handleEvent(panjeaEvent);
    }

    /**
     * @param contabilitaBD
     *            the contabilitaBD to set
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }
}
