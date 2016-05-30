package it.eurotn.panjea.giroclienti.rich.editors.entita;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.giroclienti.domain.GiroSedeCliente;
import it.eurotn.panjea.rich.converter.DateConverter;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;
import it.eurotn.rich.control.table.renderer.DateContextSensitiveCellRenderer;

public class GiroClientiEntitaTableModel extends DefaultBeanEditableTableModel<GiroSedeCliente> {

    private static final long serialVersionUID = 6489379847416773925L;

    private static final SearchContext UTENTE_EDITOR_CONTEXT = new SearchContext("userName", "utente");

    private Entita entita;

    private IAnagraficaBD anagraficaBD;

    /**
     * Costruttore.
     */
    public GiroClientiEntitaTableModel() {
        super("GiroClientiEntitaTableModel", new String[] { "utente", "giorno", "ora" }, GiroSedeCliente.class);
    }

    @Override
    protected GiroSedeCliente createNewObject() {
        GiroSedeCliente giroSedeCliente = new GiroSedeCliente();

        if (entita != null) {
            SedeEntita sedeEntita = getAnagraficaBD().caricaSedePrincipaleEntita(entita);
            giroSedeCliente.setSedeEntita(sedeEntita);
        }

        return giroSedeCliente;
    }

    private IAnagraficaBD getAnagraficaBD() {
        if (anagraficaBD == null) {
            this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
        }

        return anagraficaBD;
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int col) {
        if (col == 2) {
            return DateConverter.ORA_CONTEXT;
        }
        return super.getConverterContextAt(row, col);
    }

    @Override
    public EditorContext getEditorContextAt(int row, int col) {
        switch (col) {
        case 0:
            return UTENTE_EDITOR_CONTEXT;
        case 2:
            return DateContextSensitiveCellRenderer.ORA_CONTEXT;
        default:
            break;
        }
        return super.getEditorContextAt(row, col);
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(Entita entita) {
        this.entita = entita;
    }

}
