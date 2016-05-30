package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.dialog.DialogPage;

import it.eurotn.panjea.manutenzioni.rich.editors.righeinstallazione.RigheInstallazioneTablePage;
import it.eurotn.panjea.rich.editors.DockedEditor;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;

public class AreaInstallazioneEditor extends DockedEditor {

    public class AreaInstallazioneDockingCompositeDialogPage extends DockingCompositeDialogPage {

        /**
         * Costruttore.
         *
         * @param idPage
         *            id
         */
        public AreaInstallazioneDockingCompositeDialogPage(final String idPage) {
            super(idPage);
        }

        @Override
        public void addPage(DialogPage page) {
            super.addPage(page);

            if (AreaInstallazionePage.PAGE_ID.equals(page.getId())) {
                page.addPropertyChangeListener(AreaInstallazionePage.AREA_MAGAZZINO_PRESENTE,
                        areaInstallazioneListener);
            } else if (RigheInstallazioneTablePage.PAGE_ID.equals(page.getId())) {
                righeInstallazioneTablePage = (RigheInstallazioneTablePage) page;
            }
        }
    }

    public class AreaInstallazioneListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (AreaInstallazionePage.AREA_MAGAZZINO_PRESENTE.equals(evt.getPropertyName())) {
                boolean areaMagazzinoPresente = (boolean) evt.getNewValue();

                if (righeInstallazioneTablePage.isControlCreated()) {
                    righeInstallazioneTablePage.setAreaMagazzinoPresente(areaMagazzinoPresente);
                }
            }
        }
    }

    public RigheInstallazioneTablePage righeInstallazioneTablePage;

    private AreaInstallazioneDockingCompositeDialogPage compositeDialogPage;

    private AreaInstallazioneListener areaInstallazioneListener = new AreaInstallazioneListener();

    @Override
    protected JecCompositeDialogPage createCompositeDialogPage() {
        if (compositeDialogPage == null) {
            compositeDialogPage = new AreaInstallazioneDockingCompositeDialogPage(getDialogPageId());
        }
        return compositeDialogPage;
    }
}
