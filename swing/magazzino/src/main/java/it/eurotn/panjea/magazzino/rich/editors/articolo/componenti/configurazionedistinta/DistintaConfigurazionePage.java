package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.SortableTreeTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TreeTable;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.articolo.alternativi.ArticoliAlternativiDialog;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.RicercaAvanzataArticoliCommand;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.rich.control.table.style.FocusCellStyle;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class DistintaConfigurazionePage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    private class CancellaComponenteCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public CancellaComponenteCommand() {
            super("cancellaComponenteCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            // actual row se cambia l'ordinamento non restituisce l'indice
            // corretto
            // int actualIdx = table.getActualRowAt(idx);

            int[] selectedIndexes = table.getSelectedRows();
            if (selectedIndexes.length == 0) {
                return;
            }

            List<Componente> componentiDaCancellare = new ArrayList<>();
            List<FaseLavorazioneArticolo> fasiArticoloDaCancellare = new ArrayList<>();
            ConfigurazioneDistinta configurazioneDistinta = ((ArticoloConfigurazioneDistinta) articoloConfigurazioneDistintaValueHolder
                    .getValue()).getConfigurazioneDistinta();

            for (int currentIndex : selectedIndexes) {
                ComponenteRow selectedRow = (ComponenteRow) table.getRowAt(currentIndex);
                if (selectedRow instanceof FaseLavorazioneRow) {
                    fasiArticoloDaCancellare.add(((FaseLavorazioneRow) selectedRow).getFase());
                } else {
                    Componente componenteSelezionato = selectedRow.getComponente();
                    componenteSelezionato.setConfigurazioneComponente(configurazioneDistinta);
                    componentiDaCancellare.add(componenteSelezionato);
                }
            }

            if (componentiDaCancellare.size() > 0) {
                magazzinoAnagraficaBD.cancellaComponentiConfigurazioneDistinta(componentiDaCancellare);
            }

            if (fasiArticoloDaCancellare.size() > 0) {
                magazzinoAnagraficaBD.cancellaFasiLavorazioneArticolo(configurazioneDistinta, fasiArticoloDaCancellare);
            }

            // treeModel.setAdjusting(true);
            for (int currentIndex : selectedIndexes) {
                treeModel.removeRow(currentIndex);
            }
            // treeModel.setAdjusting(false);

            if (selectedIndexes[0] > (treeModel.getRowCount() - 1)) {
                selectedIndexes[0]--;
            }

            articoloConfigurazioneDistintaValueHolder
                    .setValue(magazzinoAnagraficaBD.caricaArticoloConfigurazioneDistinta(configurazioneDistinta));

            table.getSelectionModel().setSelectionInterval(selectedIndexes[0], selectedIndexes[0]);
        }
    }

    private class InserisciComponenteButton extends ActionCommand {

        /**
         * Costruttore.
         */
        public InserisciComponenteButton() {
            super("inserisciComponenteButton");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ComponenteRow rigaSelezionata = (ComponenteRow) table.getRowAt(table.getSelectedRow());
            // bloccare o forse se non presente aggiungerlo al root ?
            if (rigaSelezionata == null) {
                return;
            }

            RicercaAvanzataArticoliCommand ricercaCommand = new RicercaAvanzataArticoliCommand();
            ricercaCommand.execute();

            ConfigurazioneDistinta configurazioneDistinta = ((ArticoloConfigurazioneDistinta) articoloConfigurazioneDistintaValueHolder
                    .getValue()).getConfigurazioneDistinta();
            List<ArticoloRicerca> articoliDaInserire = ricercaCommand.getArticoliSelezionati();
            for (ArticoloRicerca articoloRicerca : articoliDaInserire) {
                ArticoloLite articoloDaInserire = articoloRicerca.createProxyArticoloLite();// magazzinoAnagraficaBD.caricaArticoloLite(articoloRicerca.getId());
                // può essere null se elimino tutte le righe della
                // configurazione/personalizzazione
                Componente componente = magazzinoAnagraficaBD.aggiungiComponenteAConfigurazione(configurazioneDistinta,
                        rigaSelezionata.getComponente(), articoloDaInserire.creaProxyArticolo());
                ComponenteRow rigaDaAggiungere = treeModel.creaRiga(componente);
                rigaSelezionata.addChild(rigaDaAggiungere);
                treeModel.expandAll();
                articoloConfigurazioneDistintaValueHolder
                        .setValue(magazzinoAnagraficaBD.caricaArticoloConfigurazioneDistinta(configurazioneDistinta));
            }
        }

    }

    private class InserisciComponenteRootCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public InserisciComponenteRootCommand() {
            super("inserisciComponenteRootCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            RicercaAvanzataArticoliCommand ricercaCommand = new RicercaAvanzataArticoliCommand();
            ricercaCommand.execute();
            ConfigurazioneDistinta configurazioneDistinta = ((ArticoloConfigurazioneDistinta) articoloConfigurazioneDistintaValueHolder
                    .getValue()).getConfigurazioneDistinta();
            List<ArticoloRicerca> articoliDaInserire = ricercaCommand.getArticoliSelezionati();
            ComponenteRow rigaDaAggiungere = null;
            for (ArticoloRicerca articoloRicerca : articoliDaInserire) {
                ArticoloLite articoloDaInserire = articoloRicerca.createProxyArticoloLite();
                Componente componente = magazzinoAnagraficaBD.aggiungiComponenteAConfigurazione(configurazioneDistinta,
                        null, articoloDaInserire.creaProxyArticolo());
                rigaDaAggiungere = treeModel.creaRiga(componente);
                treeModel.addRow(rigaDaAggiungere);
            }
            articoloConfigurazioneDistintaValueHolder
                    .setValue(magazzinoAnagraficaBD.caricaArticoloConfigurazioneDistinta(configurazioneDistinta));
            treeModel.expandAll();
            table.requestFocusInWindow();
            table.setSelectedRow(rigaDaAggiungere);
        }
    }

    private class InserisciFasiButton extends ActionCommand {

        /**
         * Costruttore.
         */
        public InserisciFasiButton() {
            super("inserisciFasiButton");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ComponenteRow rigaSelezionata = (ComponenteRow) table.getRowAt(table.getSelectedRow());
            // bloccare o forse se non presente aggiungerlo al root ?
            if (rigaSelezionata == null || rigaSelezionata instanceof FaseLavorazioneRow) {
                return;
            }

            ConfigurazioneDistinta configurazioneDistinta = ((ArticoloConfigurazioneDistinta) articoloConfigurazioneDistintaValueHolder
                    .getValue()).getConfigurazioneDistinta();
            Componente componente = rigaSelezionata.getComponente();

            InserimentoFasePageApplicationDialog dialog = new InserimentoFasePageApplicationDialog(componente,
                    configurazioneDistinta, magazzinoAnagraficaBD);
            dialog.setCallingCommand(this);
            dialog.showDialog();
            Set<FaseLavorazioneArticolo> fasiAggiunte = dialog.getFasiAggiunte();
            dialog = null;
            for (FaseLavorazioneArticolo faseLavorazioneArticolo : fasiAggiunte) {
                rigaSelezionata.addChild(new FaseLavorazioneRow(faseLavorazioneArticolo, magazzinoAnagraficaBD));
            }
            treeModel.expandRow(rigaSelezionata, true);
        }
    }

    private class InserisciFasiRootButton extends ActionCommand {

        /**
         * Costruttore.
         */
        public InserisciFasiRootButton() {
            super("inserisciFasiRootButton");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ConfigurazioneDistinta configurazioneDistinta = ((ArticoloConfigurazioneDistinta) articoloConfigurazioneDistintaValueHolder
                    .getValue()).getConfigurazioneDistinta();

            InserimentoFasePageApplicationDialog dialog = new InserimentoFasePageApplicationDialog(null,
                    configurazioneDistinta, magazzinoAnagraficaBD);
            dialog.setCallingCommand(this);
            dialog.showDialog();
            Set<FaseLavorazioneArticolo> fasiAggiunte = dialog.getFasiAggiunte();
            dialog = null;
            FaseLavorazioneRow faseLavorazioneRow = null;
            for (FaseLavorazioneArticolo faseLavorazioneArticolo : fasiAggiunte) {
                faseLavorazioneRow = new FaseLavorazioneRow(faseLavorazioneArticolo, magazzinoAnagraficaBD);
                treeModel.addRow(faseLavorazioneRow);
            }
            treeModel.expandAll();
            table.requestFocusInWindow();
            table.setSelectedRow(faseLavorazioneRow);
        }
    }

    private class SostituisciComponenteCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public SostituisciComponenteCommand() {
            super("sostituisciComponenteCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            int selectedIndex = table.getSelectedRow();
            if (selectedIndex == -1) {
                return;
            }

            ComponenteRow rigaSelezionata = ((ComponenteRow) table.getRowAt(selectedIndex));
            if (rigaSelezionata instanceof FaseLavorazioneRow) {
                return;
            }

            Componente componenteSelezionato = rigaSelezionata.getComponente();
            ArticoloLite articoloSelezionato = componenteSelezionato.getArticolo();
            Articolo articolo = magazzinoAnagraficaBD.caricaArticolo(articoloSelezionato.creaProxyArticolo(), false);
            ConfigurazioneDistinta configurazioneDistinta = ((ArticoloConfigurazioneDistinta) articoloConfigurazioneDistintaValueHolder
                    .getValue()).getConfigurazioneDistinta();

            componenteSelezionato.setConfigurazioneComponente(configurazioneDistinta);

            ArticoliAlternativiDialog articoliAlternativiDialog = new ArticoliAlternativiDialog(articolo);
            articoliAlternativiDialog.showDialog();

            ArticoloRicerca articoloSostitutivoRicerca = articoliAlternativiDialog.getArticoloSelezionato();
            if (articoloSostitutivoRicerca != null) {

                // Inserisco nel db il nuovo componente
                // Recupero il componente padre
                ComponenteRow rigaPadre = rigaSelezionata.getParent() instanceof ComponenteRow
                        ? (ComponenteRow) rigaSelezionata.getParent() : null;
                Componente componentePadre = rigaPadre != null ? rigaPadre.getComponente() : null;

                // Aggiungo il nuovo articolo al padre
                Articolo articoloSostitutivo = new Articolo();
                articoloSostitutivo.setId(articoloSostitutivoRicerca.getId());
                articoloSostitutivo.setVersion(articoloSostitutivoRicerca.getVersion());
                // Componente componente =
                // magazzinoAnagraficaBD.sostituisciComponenteAConfigurazione(
                // configurazioneDistinta, componentePadre,
                // componenteSelezionato, articoloSostitutivo);

                magazzinoAnagraficaBD.sostituisciComponenteAConfigurazione(configurazioneDistinta, componentePadre,
                        componenteSelezionato, articoloSostitutivo);

                // Aggiorno la gui
                // ComponenteRow rigaDaAggiungere =
                // treeModel.creaRiga(componente);

                // // Aggiungo la riga al parent
                // rigaSelezionata.getParent().addChild(rigaDaAggiungere);
                // // Rimuovo la selezionata
                // treeModel.removeRow(rigaSelezionata);
                // treeModel.expandAll();

                articoloConfigurazioneDistintaValueHolder
                        .setValue(magazzinoAnagraficaBD.caricaArticoloConfigurazioneDistinta(configurazioneDistinta));
                table.requestFocusInWindow();
                table.getSelectionModel().setSelectionInterval(selectedIndex, selectedIndex);
            }
        }
    }

    protected static FocusCellStyle focusCellStyle;

    static {
        focusCellStyle = new FocusCellStyle();
    }

    // private ArticoloConfigurazioneDistinta articoloConfigurazioneDistinta;
    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
    private ConfigurazioneDistintaTableModel treeModel;
    private TreeTable table;

    private ValueHolder articoloConfigurazioneDistintaValueHolder;

    private ActionCommand inserisciComponenteCommand;
    private ActionCommand inserisciComponenteRootCommand;
    private ActionCommand sostituisciComponenteCommand;
    private ActionCommand cancellaComponenteCommand;
    private ActionCommand inserisciFasiButton;
    private ActionCommand inserisciFasiRootButton;

    /**
     * Costruttore.
     */
    public DistintaConfigurazionePage() {
        super("distintaConfigurazionePage");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JComponent createControl() {

        table = new TreeTable();
        table.expandAll();
        table.setRowHeight(25);
        table.setFocusCellStyle(focusCellStyle);

        treeModel = new ConfigurazioneDistintaTableModel(new ArticoloConfigurazioneDistinta());
        table.setModel(treeModel);
        SortableTreeTableModel<ComponenteRow> sortableTableModel = (SortableTreeTableModel<ComponenteRow>) TableModelWrapperUtils
                .getActualTableModel(table.getModel(), SortableTreeTableModel.class);
        sortableTableModel.sortColumn(3);

        inserisciComponenteCommand = new InserisciComponenteButton();
        inserisciComponenteRootCommand = new InserisciComponenteRootCommand();
        sostituisciComponenteCommand = new SostituisciComponenteCommand();
        cancellaComponenteCommand = new CancellaComponenteCommand();
        inserisciFasiButton = new InserisciFasiButton();
        inserisciFasiRootButton = new InserisciFasiRootButton();

        JScrollPane sp = new JScrollPane(table);

        table.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_INSERT:
                    if (e.getModifiers() == KeyEvent.CTRL_MASK) {
                        inserisciComponenteCommand.execute();
                    } else {
                        inserisciComponenteRootCommand.execute();
                    }
                    break;
                case KeyEvent.VK_S:
                    if (e.getModifiers() == KeyEvent.CTRL_MASK) {
                        sostituisciComponenteCommand.execute();
                    }
                    break;
                case KeyEvent.VK_DELETE:
                    cancellaComponenteCommand.execute();
                    break;
                case KeyEvent.VK_F:
                    if (e.getModifiers() == KeyEvent.CTRL_MASK) {
                        inserisciFasiButton.execute();
                    } else {
                        inserisciFasiRootButton.execute();
                    }
                    break;
                default:
                    break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });

        CommandGroup group = new CommandGroup();
        group.add(inserisciComponenteRootCommand);
        group.add(inserisciComponenteCommand);
        group.add(sostituisciComponenteCommand);
        group.addSeparator();
        group.add(inserisciFasiRootButton);
        group.add(inserisciFasiButton);
        group.addSeparator();
        group.add(cancellaComponenteCommand);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(sp, BorderLayout.CENTER);
        rootPanel.add(group.createToolBar(), BorderLayout.SOUTH);
        return rootPanel;
    }

    @Override
    public void dispose() {
    }

    /**
     * @return Returns the magazzinoAnagraficamagazzinoAnagraficaBD
     */
    public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
        return magazzinoAnagraficaBD;
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void restoreState(Settings arg0) {

    }

    @Override
    public void saveState(Settings arg0) {
    }

    @Override
    public void setFormObject(Object object) {
        articoloConfigurazioneDistintaValueHolder = (ValueHolder) object;
        articoloConfigurazioneDistintaValueHolder.addValueChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                treeModel.setArticoloConfigurazioneDistinta((ArticoloConfigurazioneDistinta) evt.getNewValue());
                table.expandAll();
            }
        });
    }

    /**
     * @param magazzinoAnagraficaBD
     *            The magazzinoAnagraficaBD to set.
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

}