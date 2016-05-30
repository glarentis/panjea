/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.noteautomatiche;

import java.util.Collection;

import org.springframework.richclient.settings.Settings;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.INoteAutomaticheBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.table.EditFrame;

/**
 * @author fattazzo
 *
 */
public class GestioneNoteAutomatichePage extends AbstractTablePageEditor<NotaAutomatica> {

    private INoteAutomaticheBD noteAutomaticheBD;

    private Integer idEntita;
    private Integer idSedeEntita;

    /**
     * Costruttore.
     *
     * @param pageId
     * @param columnsName
     * @param classe
     */
    protected GestioneNoteAutomatichePage() {
        super("gestioneNoteAutomatichePage", new GestioneNoteAutomaticheTableModel());
        getTable().setAggregatedColumns(new String[] { "classeTipoDocumento", "tipoDocumento" });

        // getTable().getTable().getTableHeader().setReorderingAllowed(false);

        // tolgo la possibilità di vedere le opzioni della tabella visto che non mi servono
        // getTable().getOverlayTable().getOptionsPanel().setVisible(false);
        // ((JideScrollPane) ((JPanel)
        // getTable().getOverlayTable().getActualComponent()).getComponent(1)).getCorner(
        // JScrollPane.UPPER_RIGHT_CORNER).setVisible(false);
    }

    @Override
    public Collection<NotaAutomatica> loadTableData() {
        return noteAutomaticheBD.caricaNoteAutomatiche(idEntita, idSedeEntita);
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public Collection<NotaAutomatica> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void restoreState(Settings settings) {
    }

    @Override
    public void saveState(Settings settings) {
    }

    @Override
    public void setFormObject(Object object) {
        idEntita = null;
        idSedeEntita = null;

        IPageEditor page = getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME);
        if (object instanceof SedeEntita) {
            SedeEntita sedeEntita = (SedeEntita) object;
            idSedeEntita = sedeEntita.getId();
            idEntita = sedeEntita.getId();
            ((NotaAutomaticaTipoDocForm) ((NotaAutomaticaTipoDocPage) page).getForm())
                    .setSedeEntitaPredefinita(sedeEntita);
        } else if (object instanceof Entita) {
            Entita entita = (Entita) object;
            idEntita = entita.getId();
            ((NotaAutomaticaTipoDocForm) ((NotaAutomaticaTipoDocPage) page).getForm())
                    .setEntitaPredefinita(entita.getEntitaLite());
        }
    }

    /**
     * @param noteAutomaticheBD
     *            the noteAutomaticheBD to set
     */
    public void setNoteAutomaticheBD(INoteAutomaticheBD noteAutomaticheBD) {
        this.noteAutomaticheBD = noteAutomaticheBD;
    }

}
