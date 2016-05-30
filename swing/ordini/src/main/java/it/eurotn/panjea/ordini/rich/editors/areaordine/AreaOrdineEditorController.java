package it.eurotn.panjea.ordini.rich.editors.areaordine;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.dialog.DialogPage;

import it.eurotn.panjea.ordini.rich.editors.righeinserimento.RigheInserimentoPage;
import it.eurotn.panjea.ordini.rich.editors.righeordine.RigheOrdineTablePage;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.RigaArticoloDTO;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;

/**
 * La classe gestisce l'iterazione fra le varie pagine dell'editor (e della compositePage).<br/>
 *
 * @author fattazzo
 *
 */
public class AreaOrdineEditorController {

    /**
     * PropertyChange chiamato quando la testata (area ordine) viene validata, in questo caso quando viene salvata.
     */
    private class AreaOrdineValidataChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (AreaOrdinePage.VALIDA_AREA_ORDINE.equals(evt.getPropertyName())) {
                AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) areaOrdinePage.getForm().getFormObject();
                if (areaOrdineFullDTO.getVersion() != null && areaOrdineFullDTO.getVersion() == 0) {
                    compositePage.setActivePage(righeOrdineTablePage);

                    righeOrdineTablePage.getEditFrame().setCurrentPage(new RigaArticoloDTO());
                    righeOrdineTablePage.getEditFrame().getQuickInsertCommand().setSelected(true);
                }
            }
        }
    }

    private class RigheInserimentoChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (RigheInserimentoPage.ATTIVAZIONE_RIGHE_INSERIMENTO.equals(evt.getPropertyName())) {

                boolean attivaInserimento = (boolean) evt.getNewValue();
                righeInserimentoPage.setVisible(attivaInserimento);
                righeOrdineTablePage.setVisible(!attivaInserimento);
                if (!attivaInserimento) {
                    righeOrdineTablePage.loadData();
                }
            }
        }

    }

    public class RigheOrdineValidataChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (RigheOrdineTablePage.VALIDA_RIGHE_ORDINE.equals(evt.getPropertyName())) {

                Boolean nuovaArea = null;

                nuovaArea = righeOrdineTablePage.getAzioneDopoConfermaCommand().isSelected();

                if (nuovaArea && evt.getOldValue() != null) {
                    areaOrdinePage.setTabForm(0);
                    areaOrdinePage.getEditorNewCommand().execute();
                    compositePage.setActivePage(areaOrdinePage);
                } else {
                    areaOrdinePage.setTabForm(3);
                }
            }
        }
    }

    private AreaOrdinePage areaOrdinePage;
    private RigheOrdineTablePage righeOrdineTablePage;
    private RigheInserimentoPage righeInserimentoPage;

    private DockingCompositeDialogPage compositePage = null;

    private AreaOrdineValidataChangeListener areaOrdineValidataChangeListener = null;
    private RigheOrdineValidataChangeListener righeOrdineValidataChangeListener = null;
    private RigheInserimentoChangeListener righeInserimentoChangeListener = null;

    /**
     * Costruttore.
     *
     * @param compositePage
     *            pagina contenuta nell'editor
     */
    public AreaOrdineEditorController(final DockingCompositeDialogPage compositePage) {
        areaOrdineValidataChangeListener = new AreaOrdineValidataChangeListener();
        righeOrdineValidataChangeListener = new RigheOrdineValidataChangeListener();
        righeInserimentoChangeListener = new RigheInserimentoChangeListener();
        this.compositePage = compositePage;
    }

    /**
     * aggiunge una pagina al controller.
     *
     * @param page
     *            pagina da aggiungere
     */
    public void addPage(DialogPage page) {
        if (AreaOrdinePage.PAGE_ID.equals(page.getId())) {
            areaOrdinePage = (AreaOrdinePage) page;
            areaOrdinePage.addPropertyChangeListener(AreaOrdinePage.VALIDA_AREA_ORDINE,
                    areaOrdineValidataChangeListener);

        } else if (RigheOrdineTablePage.PAGE_ID.equals(page.getId())) {
            righeOrdineTablePage = (RigheOrdineTablePage) page;
            righeOrdineTablePage.addPropertyChangeListener(RigheOrdineTablePage.VALIDA_RIGHE_ORDINE,
                    righeOrdineValidataChangeListener);
            righeOrdineTablePage.addPropertyChangeListener(RigheInserimentoPage.ATTIVAZIONE_RIGHE_INSERIMENTO,
                    righeInserimentoChangeListener);

        } else if (RigheInserimentoPage.PAGE_ID.equals(page.getId())) {
            righeInserimentoPage = (RigheInserimentoPage) page;
            righeInserimentoPage.addPropertyChangeListener(RigheInserimentoPage.ATTIVAZIONE_RIGHE_INSERIMENTO,
                    righeInserimentoChangeListener);
        }
    }

    /**
     * @param attivaInserimento
     *            attiva l'inserimento massivo delle righe
     */
    public void attivaInserimentoRighe(boolean attivaInserimento) {
        righeInserimentoPage.setVisible(attivaInserimento);
        righeOrdineTablePage.setVisible(!attivaInserimento);
        if (!attivaInserimento) {
            righeOrdineTablePage.loadData();
        }
    }

    /**
     * dispose del controller.
     */
    public void dispose() {
        righeOrdineTablePage.removePropertyChangeListener(RigheOrdineTablePage.VALIDA_RIGHE_ORDINE,
                righeOrdineValidataChangeListener);
        areaOrdinePage.removePropertyChangeListener(AreaOrdinePage.VALIDA_AREA_ORDINE,
                areaOrdineValidataChangeListener);
    }

    /**
     * @return the righeInserimentoPage
     */
    public RigheInserimentoPage getRigheInserimentoPage() {
        return righeInserimentoPage;
    }
}
