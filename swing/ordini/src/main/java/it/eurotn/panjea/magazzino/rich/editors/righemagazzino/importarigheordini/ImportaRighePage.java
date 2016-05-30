package it.eurotn.panjea.magazzino.rich.editors.righemagazzino.importarigheordini;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.AbstractTableFilter;
import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.FilterableTableModel;
import com.jidesoft.grid.GroupTable;
import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico.StatoRiga;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.forms.evasione.ParametriRicercaEvasioneForm;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaEvasione;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class ImportaRighePage extends AbstractTablePageEditor<RigaDistintaCarico>implements Focussable {

    private class CercaCommand extends ActionCommand {

        /**
         *
         * Costruttore.
         *
         */
        public CercaCommand() {
            super("openRicercaAreaOrdineCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            refreshData();
        }

    }

    private class FiltraRigheDaEvadereCommand extends JideToggleCommand {

        public static final String COMMAND_ID = "filtraDistintePronteCommand";

        /**
         * Costruttore.
         */
        public FiltraRigheDaEvadereCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void onDeselection() {
            super.onDeselection();
            filtroRigheDaEvadere.setEnabled(false);
            FilterableTableModel tm = (FilterableTableModel) TableModelWrapperUtils
                    .getActualTableModel(getTable().getTable().getModel(), FilterableTableModel.class);
            tm.setFiltersApplied(false);
            tm.refresh();
        }

        @Override
        protected void onSelection() {
            super.onSelection();
            filtroRigheDaEvadere.setEnabled(true);
            FilterableTableModel tm = (FilterableTableModel) TableModelWrapperUtils
                    .getActualTableModel(getTable().getTable().getModel(), FilterableTableModel.class);
            tm.setFiltersApplied(true);
            tm.refresh();
        }
    }

    private final class FiltroRigheDaEvadere extends AbstractTableFilter<Object> {
        private static final long serialVersionUID = 8902060457200249359L;

        /**
         *
         * Costruttore.
         */
        private FiltroRigheDaEvadere() {
            super("filtroRigheDaEvadere");
            setEnabled(false);
        }

        @Override
        public boolean isValueFiltered(Object value) {
            if (value instanceof DefaultGroupRow) {
                return true;
            }
            return ((StatoRiga) value == StatoRiga.SELEZIONABILE);
        }
    }

    public static final String PAGE_ID = "ImportaRighePage";
    private AreaMagazzinoFullDTO areaMagazzinoFullDTO;
    private IOrdiniDocumentoBD ordiniDocumentoBD;
    private ParametriRicercaEvasioneForm parametriRicercaEvasioneForm;
    private FiltroRigheDaEvadere filtroRigheDaEvadere;
    private FiltraRigheDaEvadereCommand filtraRigheDaEvadereCommand;
    private CercaCommand cercaCommand;

    /**
     *
     * Costruttore.
     *
     * @param ordiniDocumentoBD
     *            bd ordini
     *
     */
    public ImportaRighePage(final IOrdiniDocumentoBD ordiniDocumentoBD) {
        super(PAGE_ID, new ImportaRigheOrdiniTableModel());
        this.ordiniDocumentoBD = ordiniDocumentoBD;
        parametriRicercaEvasioneForm = new ParametriRicercaEvasioneForm();

        parametriRicercaEvasioneForm.getFormModel().getFieldMetadata("entita").setReadOnly(true);
        parametriRicercaEvasioneForm.setOrdiniDocumentoBD(ordiniDocumentoBD);
        getTable().setTableType(TableType.GROUP);
        filtroRigheDaEvadere = new FiltroRigheDaEvadere();
        FilterableTableModel filterableTableModel = (FilterableTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel(), FilterableTableModel.class);
        filterableTableModel.addFilter(0, filtroRigheDaEvadere);
        new SelectRowListener((GroupTable) getTable().getTable());
    }

    /**
     * @return command per cercare le righe
     */
    private CercaCommand getCercaCommand() {
        if (cercaCommand == null) {
            cercaCommand = new CercaCommand();
        }
        return cercaCommand;
    }

    /**
     * @return command per filtrare le distinte pronte
     */
    private FiltraRigheDaEvadereCommand getFiltraRigheDaEvadereCommand() {
        if (filtraRigheDaEvadereCommand == null) {
            filtraRigheDaEvadereCommand = new FiltraRigheDaEvadereCommand();
        }
        return filtraRigheDaEvadereCommand;
    }

    @Override
    public JComponent getHeaderControl() {
        JPanel headerControl = new JPanel(new BorderLayout());
        headerControl.add(parametriRicercaEvasioneForm.getControl(), BorderLayout.CENTER);
        parametriRicercaEvasioneForm.setScambioVisualizzazioneTipiDocVisible(false);

        JPanel buttonBar = getComponentFactory().createPanel(new VerticalLayout(5));
        buttonBar.add(getCercaCommand().createButton());
        buttonBar.add(getFiltraRigheDaEvadereCommand().createButton());
        headerControl.add(buttonBar, BorderLayout.EAST);
        return headerControl;
    }

    /**
     *
     * @return righe da evadere <br/>
     *         <b>NB</b>!!!:Le righe da evadere devono essere righe di distinta quindi ci si aspetta di avere la
     *         qtaEvasa come qta. In tabella si utilizza QtaDaEvadere, che viene copiata nel campo qtaEvasa
     */
    public List<RigaDistintaCarico> getRigheDaEvadere() {
        filtroRigheDaEvadere.setEnabled(true);

        FilterableTableModel filterableTableModel = (FilterableTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel(), FilterableTableModel.class);
        filterableTableModel.setFiltersApplied(true);
        filterableTableModel.refresh();
        ImportaRigheOrdiniTableModel baseTableModel = (ImportaRigheOrdiniTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel());
        List<RigaDistintaCarico> result = new ArrayList<RigaDistintaCarico>();
        for (int i = 0; i < filterableTableModel.getRowCount(); i++) {
            RigaDistintaCarico riga = baseTableModel.getElementAt(filterableTableModel.getActualRowAt(i));
            riga.setQtaEvasa(riga.getQtaDaEvadere());
            result.add(riga);
        }
        return result;
    }

    @Override
    public void grabFocus() {
        getTable().getTable().requestFocusInWindow();
    }

    @Override
    public Collection<RigaDistintaCarico> loadTableData() {
        // Carico tutti gli ordini aperti del fornitore definito.
        final ParametriRicercaEvasione parametri = new ParametriRicercaEvasione();
        EntitaLite entita = areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getEntita();
        TipoDocumento tipoDocumento = areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getTipoDocumento();
        parametri.setEntita(entita);
        parametri.setTipoEntita(tipoDocumento.getTipoEntita());
        Periodo periodoDataRegistrazione = new Periodo();
        periodoDataRegistrazione.setDataFinale(areaMagazzinoFullDTO.getAreaMagazzino().getDataRegistrazione());
        parametri.setDataRegistrazione(periodoDataRegistrazione);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                parametriRicercaEvasioneForm.setFormObject(parametri);
            }
        });

        // ParametriRicercaEvasione parametri = (ParametriRicercaEvasione) parametriRicercaEvasioneForm.getFormObject();
        List<RigaDistintaCarico> result = ordiniDocumentoBD.caricaRigheEvasione(parametri);
        return result;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return super.onPrePageOpen();
    }

    @Override
    public Collection<RigaDistintaCarico> refreshTableData() {
        ParametriRicercaEvasione parametri = (ParametriRicercaEvasione) parametriRicercaEvasioneForm.getFormObject();
        List<RigaDistintaCarico> result = ordiniDocumentoBD.caricaRigheEvasione(parametri);
        return result;
    }

    @Override
    public void setFormObject(Object object) {
        areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) object;
    }

}
