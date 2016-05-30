package it.eurotn.panjea.anagrafica.rich.editors.documento;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.dialog.DialogPage;

import it.eurotn.rich.dialog.ButtonCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractEditorDialogPage;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class TipoDocumentoEditor extends AbstractEditorDialogPage {

    private class TipoDocumentoCompositeDialogPage extends ButtonCompositeDialogPage {

        /**
         * Costruttore.
         *
         */
        public TipoDocumentoCompositeDialogPage() {
            super(getDialogPageId(), enabledOnOpen);
        }

        /*
         *
         * @see it.eurotn.rich.dialog.JecCompositeDialogPage#createControl()
         */
        @Override
        protected JComponent createControl() {
            JComponent controlli = super.createControl();
            // chiamo una objectChange per scatenare il lifecycle della pagina.
            // sulla setCurrentObject lancio la object change solamente se i
            // componenti sono creati
            // perchè nel caso di una tablePage verrebbe chiamata una LoadData e
            // facendo setData() la JTable
            // deve ancora esistere e darebbe una npe.
            // Quindi una volta creati i controlli lancio una objectChange così
            // lancio il lifecycle per il primo oggetto inserito
            // nella pagina
            objectChange(currentObject, null);
            return controlli;
        }

        /**
         * Sovrascrivo la objectChange perchè l'imlementazione standard è lazy,<BR>
         * quindi l'editor non chiamerebbe il lifeCycle della pagina finchè non si preme il pulsante
         * per attivarla. <br>
         * In questo editor la pagina deve essere notificata subito per<br>
         * <li>poter disabilitarsi se la classe documento non prevede l'area gestita dalla pagina
         * </li>
         * <li>cambiare il commandFace del pulsante che la attiva se l'area può essere gestita ma
         * non è ancora stata creata</li> <BR>
         *
         * @param domainObject
         *            :il nuovo oggetto di dominio
         * @param pageSource
         *            :la pagina che ha generato l'evento
         */
        @Override
        protected void objectChange(Object domainObject, DialogPage pageSource) {
            logger.debug("--> Enter OBJECTCHANGE");
            for (DialogPage page : getDialogPages()) {
                if (pageSource == null || !page.getId().equals(pageSource.getId())) {
                    // pagesDirty.put(page.getId(), true);
                    try {
                        logger.debug("--> CHIAMO SETFORMOBJECT e LOADDATA per page " + page.getId());
                        IPageLifecycleAdvisor lifecycle = ((IPageLifecycleAdvisor) page);
                        lifecycle.preSetFormObject(domainObject);
                        lifecycle.setFormObject(domainObject);
                        lifecycle.postSetFormObject(domainObject);
                        ((IPageLifecycleAdvisor) page).loadData();
                    } catch (Exception e) {
                        logger.error("--> errore nel settare il domainObject nella pagina " + page.getId(), e);
                    }
                }
            }
            if (pageSource == null) {
                pagesLoaded.clear();
                showPage(getPage(getPagesLinkedList().get(0)));
            }
        }
    }

    private Logger logger = Logger.getLogger(TipoDocumentoEditor.class);

    @Override
    protected JecCompositeDialogPage createCompositeDialogPage() {
        return new TipoDocumentoCompositeDialogPage();
    }
}
