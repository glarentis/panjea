package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.updateultimocosto;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.table.TableColumn;

import org.springframework.richclient.dialog.ApplicationDialog;

import com.jidesoft.grid.TableColumnChooser;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.listino.exception.RigheListinoListiniCollegatiExceptionDialog;
import it.eurotn.panjea.magazzino.service.exception.RigheListinoListiniCollegatiException;
import it.eurotn.panjea.rich.bd.BusyIndicator;
import it.eurotn.rich.control.table.JideTableWidget;

public class DialogAggiornamento extends ApplicationDialog {

    public static final String DIALOG_ID = "dialogAggiornamentoUltimoCosto";
    private final JideTableWidget<RigaListinoAggioramento> table;
    private final ArrayList<RigaListinoAggioramento> righeListinoAggiornamento;
    private final IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

    /**
     * costruttore.
     *
     * @param righe
     *            righe da aggiornare
     * @param listino
     *            listino di riferimento
     * @param magazzinoAnagraficaBD
     *            bd anagrafica magazzino
     * @param importiArticoli
     *            importi articoli
     */
    public DialogAggiornamento(final List<RigaListino> righe, final Listino listino,
            final IMagazzinoAnagraficaBD magazzinoAnagraficaBD, final Map<ArticoloLite, Importo> importiArticoli) {
        this.righeListinoAggiornamento = new ArrayList<RigaListinoAggioramento>();
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
        setTitle(getMessage(DIALOG_ID + ".title"));
        table = new JideTableWidget<RigaListinoAggioramento>(DIALOG_ID, new RigaListinoAggiornamentoTableModel());

        table.restoreState(null);

        table.setAggregatedColumns(new String[] { "rigaListino.articolo" });
        if (importiArticoli.size() == 1) {
            TableColumnChooser.hideColumn(table.getTable(), 0);
        }
        List<RigaListinoAggioramento> rigaListinoAggiornamentoCorrente = new ArrayList<RigaListinoAggioramento>();
        for (RigaListino rigaListino : righe) {
            RigaListinoAggioramento rigaListinoAggioramento = new RigaListinoAggioramento();
            rigaListinoAggioramento
                    .setPrezzoDaAggiornare(importiArticoli.get(rigaListino.getArticolo()).getImportoInValutaAzienda());
            rigaListinoAggioramento.setNumeroDecimali(rigaListino.getNumeroDecimaliPrezzo());
            rigaListinoAggioramento.setRigaListino(rigaListino);
            // Aggiungo solamente le righe listino che non sono del listino su area magazzino.
            // la riga del listino su area magazzino al aggiungo dopo con indice = 0 per inseirla
            // all'inizio della
            // lista.
            if (rigaListino.getVersioneListino().getListino().equals(listino)) {
                rigaListinoAggiornamentoCorrente.add(rigaListinoAggioramento);
            } else {
                righeListinoAggiornamento.add(rigaListinoAggioramento);
            }
        }

        // aggiungo ora la riga listino del listino corrente.
        if (!rigaListinoAggiornamentoCorrente.isEmpty()) {
            righeListinoAggiornamento.addAll(0, rigaListinoAggiornamentoCorrente);
        }

        table.setRows(righeListinoAggiornamento);

        int dialogWidth = 0;
        for (int i = 0; i < table.getTable().getColumnModel().getColumnCount(); i++) {
            TableColumn colonna = table.getTable().getColumnModel().getColumn(i);
            dialogWidth += colonna.getWidth();
        }
        if (dialogWidth == 0) {
            dialogWidth = 300;
        }
        dialogWidth = table.getTable().getColumnModel().getTotalColumnWidth();
        getDialog().setPreferredSize(new Dimension(dialogWidth, 250));
        getDialog().setSize(new Dimension(dialogWidth, 250));
    }

    @Override
    protected JComponent createDialogContentPane() {
        return table.getComponent();
    }

    @Override
    protected boolean onFinish() {
        // se non ho editato alcun valore della tabella il cellEditor è null!
        if ((table.getTable()).getCellEditor() != null) {
            (table.getTable()).getCellEditor().stopCellEditing();
        }
        table.saveState(null);
        List<RigaListinoAggioramento> righeListino = table.getRows();
        List<RigaListino> result = new ArrayList<RigaListino>();
        try {
            BusyIndicator.showAt();
            for (RigaListinoAggioramento rigaListinoAggioramento : righeListino) {
                if (rigaListinoAggioramento.getAggiorna()) {
                    rigaListinoAggioramento.getRigaListino().setPrezzo(rigaListinoAggioramento.getPrezzoDaAggiornare());
                    result.add(rigaListinoAggioramento.getRigaListino());
                }
            }
            try {
                magazzinoAnagraficaBD.salvaPrezzoRigheListino(result);
            } catch (RigheListinoListiniCollegatiException e) {
                RigheListinoListiniCollegatiExceptionDialog dialog = new RigheListinoListiniCollegatiExceptionDialog(
                        result, e);
                dialog.showDialog();
                Map<Boolean, List<RigaListino>> mapRighe = dialog.getRigheDaAggiornare();

                // salvo le righe che devono aggiornare i prezzi
                if (mapRighe.get(true) != null) {
                    magazzinoAnagraficaBD.salvaPrezzoRigheListino(mapRighe.get(true), true);
                }

                // salvo le righe che non devono aggiornare i prezzi
                if (mapRighe.get(false) != null) {
                    magazzinoAnagraficaBD.salvaPrezzoRigheListino(mapRighe.get(false), false);
                }
            }
        } finally {
            BusyIndicator.clearAt();
        }
        return true;
    }

}