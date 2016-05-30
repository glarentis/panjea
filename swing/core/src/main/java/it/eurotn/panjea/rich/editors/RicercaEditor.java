package it.eurotn.panjea.rich.editors;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.DialogPage;

import it.eurotn.rich.command.ParametriRicercaCommand;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * @author fattazzo
 *
 */
public class RicercaEditor extends DockedEditor {

    public class RicercaDockingCompositeDialogPage extends DockingCompositeDialogPage {

        /**
         * Costruttore.
         *
         * @param idPage
         *            id
         */
        public RicercaDockingCompositeDialogPage(final String idPage) {
            super(idPage);
        }

        @Override
        public void addPage(DialogPage page) {
            super.addPage(page);

            // la prima pagina è quella dei parametri di ricerca mentre la seconda è quella dei
            // risultati, tutte le
            // altre non le considero ai fini della ricerca
            if (parametriRicercaPage == null) {
                parametriRicercaPage = (FormBackedDialogPageEditor) page;
            } else if (parametriRicercaCommand == null) {
                // solo se di tratta di una AbstractTablePageEditor viene gestito il layout della
                // tabella
                AbstractTablePageEditor<?> risultatiRicercaPage = null;
                if (page instanceof AbstractTablePageEditor) {
                    risultatiRicercaPage = (AbstractTablePageEditor<?>) page;
                }

                parametriRicercaCommand = new ParametriRicercaCommand(parametriRicercaPage, risultatiRicercaPage);
                // alla pagina di ricerca aggiungo come primo command quello dei parametri di
                // ricerca
                AbstractCommand[] oldExtrenalCommandStart = parametriRicercaPage.getExternalCommandStart();
                if (oldExtrenalCommandStart == null) {
                    oldExtrenalCommandStart = new ActionCommand[] { parametriRicercaCommand };
                } else {
                    List<AbstractCommand> list = new LinkedList<AbstractCommand>(
                            Arrays.asList(oldExtrenalCommandStart));
                    list.add(0, parametriRicercaCommand);
                    oldExtrenalCommandStart = list.toArray(new ActionCommand[list.size()]);
                }
                parametriRicercaPage.setExternalCommandStart(oldExtrenalCommandStart);
            }
        }

    }

    private FormBackedDialogPageEditor parametriRicercaPage = null;

    private RicercaDockingCompositeDialogPage compositeDialogPage;

    private ParametriRicercaCommand parametriRicercaCommand;

    @Override
    protected JecCompositeDialogPage createCompositeDialogPage() {
        if (compositeDialogPage == null) {
            compositeDialogPage = new RicercaDockingCompositeDialogPage(getDialogPageId());
        }
        return compositeDialogPage;
    }

}
