/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.statistiche.movimentazione;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione;
import it.eurotn.panjea.magazzino.util.RigaMovimentazione;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * TablePage per la visualizzazione dei risultati della ricerca della movimentazione magazzino.
 *
 * @author Leonardo
 */
public class RisultatiRicercaMovimentazioneMagazzinoTablePage extends AbstractTablePageEditor<RigaMovimentazione>
        implements InitializingBean {

    private class OpenAreaMagazzinoEditorCommand extends ApplicationWindowAwareCommand {

        private static final String COMMAND_ID = "openAreaMagazzinoCommand";

        private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;

        /**
         * Costruisce il comando per aprire l'area di magazzino selezionata.
         * 
         * @param magazzinoDocumentoBD
         *            BD per caricare i dati dal service
         */
        public OpenAreaMagazzinoEditorCommand(final IMagazzinoDocumentoBD magazzinoDocumentoBD) {
            super(COMMAND_ID);
            RcpSupport.configure(this);
            this.magazzinoDocumentoBD = magazzinoDocumentoBD;
        }

        @Override
        protected void doExecuteCommand() {
            RigaMovimentazione rigaMovimentazione = getTable().getSelectedObject();

            if (rigaMovimentazione != null) {
                AreaMagazzino areaMagazzino = new AreaMagazzino();
                areaMagazzino.setId(rigaMovimentazione.getAreaMagazzinoId());
                AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD
                        .caricaAreaMagazzinoFullDTO(areaMagazzino);
                LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
                Application.instance().getApplicationContext().publishEvent(event);
            }
        }
    }

    public static final String PAGE_ID = "risultatiRicercaMovimentazioneMagazzinoTablePage";
    private static final int SIZEPAGE = 20000;
    private ParametriRicercaMovimentazione parametriRicercaMovimentazione;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;
    private OpenAreaMagazzinoEditorCommand openAreaMagazzinoEditorCommand;

    /**
     * Costruttore di default.
     */
    public RisultatiRicercaMovimentazioneMagazzinoTablePage() {
        super(PAGE_ID, new MovimentazioneBeanTableModel(PAGE_ID));
        getTable().setAggregatedColumns(new String[] { "dataRegistrazione" });
        getTable().getOverlayTable().setEnableCancelAction(true);
        getTable().setSummaryCalculatorFactory(new MovimentazioneSummaryCalculator());
        ((JideTable) getTable().getTable()).setRowAutoResizes(true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        getTable().setPropertyCommandExecutor(getOpenAreaMagazzinoEditor());
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getOpenAreaMagazzinoEditor() };
    }

    /**
     * @return Command per aprire l'area di magazzino selezionata
     */
    private OpenAreaMagazzinoEditorCommand getOpenAreaMagazzinoEditor() {
        if (openAreaMagazzinoEditorCommand == null) {
            openAreaMagazzinoEditorCommand = new OpenAreaMagazzinoEditorCommand(this.magazzinoDocumentoBD);
        }
        return openAreaMagazzinoEditorCommand;
    }

    @Override
    public Collection<RigaMovimentazione> loadTableData() {
        return null;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void processTableData(Collection<RigaMovimentazione> results) {
        getTable().getOverlayTable().startSearch();
        boolean fine = false;
        int page = 1;
        List<RigaMovimentazione> righeMovimentazioneResult = new ArrayList<RigaMovimentazione>();
        getTable().getOverlayTable().setCancel(false);
        MovimentazioneBeanTableModel movimentazioneBeanTableModel = (MovimentazioneBeanTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel());
        movimentazioneBeanTableModel.setNumeroDecimaliPrezzo(6);
        movimentazioneBeanTableModel.setNumeroDecimaliQta(6);
        setRows(new ArrayList<RigaMovimentazione>());
        if (parametriRicercaMovimentazione != null && parametriRicercaMovimentazione.isEffettuaRicerca()) {
            try {
                do {
                    // Runtime.getRuntime().gc();
                    // if (!fine) {
                    List<RigaMovimentazione> righeMovimentazione = magazzinoDocumentoBD
                            .caricaMovimentazione(parametriRicercaMovimentazione, page, SIZEPAGE);
                    fine = righeMovimentazione.size() < SIZEPAGE;
                    righeMovimentazioneResult.addAll(righeMovimentazione);
                    page++;
                    // }
                    // se chiude la pagina la getTable va a null.
                } while (getTable() != null && !getTable().getOverlayTable().isCancel() && !fine);
                if (getTable() != null) {
                    setRows(righeMovimentazioneResult);
                }
            } catch (Exception e) {
                righeMovimentazioneResult.clear();
                PanjeaSwingUtil.checkAndThrowException(e);
            } finally {
                if (getTable() != null) {
                    getTable().getOverlayTable().stopSearch();
                }
            }
        }
    }

    @Override
    public Collection<RigaMovimentazione> refreshTableData() {
        return Collections.emptyList();
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof ParametriRicercaMovimentazione) {
            this.parametriRicercaMovimentazione = (ParametriRicercaMovimentazione) object;
        } else {
            this.parametriRicercaMovimentazione = new ParametriRicercaMovimentazione();
        }
        // NPE da mail. controllo il null
        if (getTable() != null) {
            getTable().setTableHeader(parametriRicercaMovimentazione);
        }
    }

    /**
     * setter per magazzinoDocumentoBD.
     * 
     * @param magazzinoDocumentoBD
     *            magazzinoDocumentoBD
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }
}
