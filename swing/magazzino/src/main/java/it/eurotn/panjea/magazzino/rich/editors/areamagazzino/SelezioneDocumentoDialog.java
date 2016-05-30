/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.dialog.DialogPage;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.rich.editors.areamagazzino.SelezioneDocumentoPage.RisultatiDocumentoTablePage;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

/**
 * ApplicationDialog che ingloba la DialogPage {@link SelezioneDocumentoPage} per la selezione di
 * {@link Documento} <br>
 * Si registra alla TablePage {@link RisultatiDocumentoTablePage} per intercettare l'evento di
 * selezione dell'oggetto.
 *
 * @author adriano
 * @version 1.0, 10/set/2008
 */
public class SelezioneDocumentoDialog extends PanjeaTitledPageApplicationDialog implements PropertyChangeListener {

    private static final String DIALOG_ID = "selezioneDocumentoDialog";

    private Documento documentoSelected = null;

    /**
     * @param dialogPage
     *            dialogPage
     */
    public SelezioneDocumentoDialog(final DialogPage dialogPage) {
        super(dialogPage);
        // si aggiunge ai listeners di RisultatiDocumentoTablePage per la selzione dell'oggetto
        ((SelezioneDocumentoPage) getDialogPage()).getRisultatiDocumentoTablePage()
                .addPropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED, this);
        this.setTitle(getMessage(DIALOG_ID + ".title"));
        this.setPreferredSize(new Dimension(960, 400));
    }

    /**
     * @return Returns the documentoSelected.
     */
    public Documento getDocumentoSelected() {
        return documentoSelected;
    }

    @Override
    protected void onAboutToShow() {
        super.onAboutToShow();
        ((SelezioneDocumentoPage) getDialogPage()).grabFocus();

    }

    @Override
    protected void onCancel() {
        documentoSelected = null;
        super.onCancel();
    }

    @Override
    protected boolean onFinish() {
        // recupera il documento selezionato dalla DialogPage
        SelezioneDocumentoPage selezioneDocumentoPage = (SelezioneDocumentoPage) getDialogPage();
        documentoSelected = selezioneDocumentoPage.getRisultatiDocumentoTablePage().getTable().getSelectedObject();
        return (documentoSelected != null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getFinishCommand().execute();
    }

}
