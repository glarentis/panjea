/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.entita;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita.TipoSede;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.dialog.TabbedCompositeDialogPage;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * @author fattazzo
 *
 */
public class SedeEntitaCompositePage extends TabbedCompositeDialogPage implements Observer, PropertyChangeListener {

    private static final String PAGE_ID = "sedeEntitaButtonCompositePage";

    /**
     * costruttore.
     */
    public SedeEntitaCompositePage() {
        super(PAGE_ID);

    }

    @Override
    public void addPage(DialogPage page) {
        if (SedeEntitaPage.PAGE_ID.equals(page.getId())) {
            page.addPropertyChangeListener(FormBackedDialogPageEditor.OBJECT_CHANGED, this);
        }
        super.addPage(page);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertychangeevent) {
        update(null, propertychangeevent.getNewValue());
    }

    @Override
    public void setIdPages(List<String> idPages) {
        for (String pagId : idPages) {
            addPage(pagId);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        SedeEntita sedeEntita = (SedeEntita) arg;
        if (sedeEntita != null && sedeEntita.getId() != null) {
            // Carico tutti i valori della sedeEntita
            IAnagraficaBD anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
            sedeEntita = anagraficaBD.caricaSedeEntita(sedeEntita.getId());
        }
        this.setCurrentObject(sedeEntita);
        this.objectChange(sedeEntita, this);

        disableAllTabs();
        changeEnableTab(SedeEntitaPage.PAGE_ID, true);

        if (sedeEntita != null && sedeEntita.getId() != null) {
            if (sedeEntita.getTipoSede().getTipoSede() == TipoSede.INDIRIZZO_SPEDIZIONE
                    || sedeEntita.getTipoSede().getTipoSede() == TipoSede.SERVIZIO) {
                changeEnableTab(NoteSedeEntitaPage.PAGE_ID, true);
            } else {
                enableAllTabs();
            }
        }

    }
}
