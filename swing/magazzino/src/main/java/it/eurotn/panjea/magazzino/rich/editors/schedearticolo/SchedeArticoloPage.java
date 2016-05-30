/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.schedearticolo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.StyledLabel;
import com.jidesoft.swing.StyledLabelBuilder;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoSchedeArticoloBD;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.SituazioneSchedaArticoloDTO;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * @author fattazzo
 *
 */
public class SchedeArticoloPage extends AbstractDialogPage implements IPageLifecycleAdvisor, Observer {

    private class RefreshCommand extends ApplicationWindowAwareCommand {

        public static final String COMMAND_ID = "refreshCommand";

        /**
         * Costruttore.
         */
        public RefreshCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            // salvo la situazione selezionata per poi riselezionarla
            SituazioneSchedaArticoloDTO situazioneSelezionata = mesiTable.getSelectedObject();

            mesiTable.setRows(
                    magazzinoSchedeArticoloBD.caricaSituazioneSchedeArticolo((Integer) spinnerAnno.getValue()));
            mesiTable.selectRowObject(situazioneSelezionata, null);
        }

    }

    public static final String PAGE_ID = "schedeArticoloPage";

    private AziendaCorrente aziendaCorrente;

    private JSpinner spinnerAnno;

    private IMagazzinoSchedeArticoloBD magazzinoSchedeArticoloBD;

    private JideTableWidget<SituazioneSchedaArticoloDTO> mesiTable;

    private StyledLabel numeroSchedeArticoloInElaborazioneLabel;

    private JideTableWidget<ArticoloRicerca> articoliStampatiTable;
    private JideTableWidget<ArticoloRicerca> articoliNonValidiTable;
    private JideTableWidget<ArticoloRicerca> articoliRimanentiTable;

    private CreaSchedeArticoloCommand creaNonValidiSelezionatiCommand;
    private CreaSchedeArticoloCommand creaRimanentiSelezionatiCommand;
    private StampaSchedeArticoloSelezionateCommand stampaSchedeArticoloSelezionateCommand;

    private RefreshCommand refreshCommand;

    {
        articoliStampatiTable = new JideTableWidget<ArticoloRicerca>("articoliStampatiTable",
                new ArticoliSchedeTableModel());
        articoliNonValidiTable = new JideTableWidget<ArticoloRicerca>("articoliNonValidiTable",
                new ArticoliSchedeTableModel());
        articoliRimanentiTable = new JideTableWidget<ArticoloRicerca>("articoliRimanentiTable",
                new ArticoliSchedeTableModel());

        numeroSchedeArticoloInElaborazioneLabel = new StyledLabel();
    }

    /**
     * Costruttore.
     */
    protected SchedeArticoloPage() {
        super(PAGE_ID);
    }

    /**
     * Aggiorna i controlli che visualizzano il numero di schede articolo che sono attualmente in
     * elaborazione.
     */
    private void aggiornaSchedeArticoloInElaborazioneControl() {

        int schede = magazzinoSchedeArticoloBD.caricaNumeroSchedeArticoloInCodaDiElaborazione();

        numeroSchedeArticoloInElaborazioneLabel.clearStyleRanges();

        String style = "{Nessuna scheda articolo in elaborazione:b}";
        if (schede > 0) {
            style = "{" + schede + " schede articolo ancora in elaborazione:b,f:red}";
        }

        StyledLabelBuilder.setStyledText(numeroSchedeArticoloInElaborazioneLabel, style);
    }

    @Override
    protected JComponent createControl() {
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        JPanel panelData = getComponentFactory().createPanel(new BorderLayout());
        panel.add(panelData, BorderLayout.CENTER);
        panelData.add(createHeaderControl(), BorderLayout.NORTH);
        // panelData.add(createButtonBar(), BorderLayout.SOUTH);
        JPanel panelTable = getComponentFactory().createPanel(new BorderLayout());
        panelTable.add(createMesiControl(), BorderLayout.WEST);
        panelTable.add(createDetailComponent(), BorderLayout.CENTER);
        panelData.add(panelTable, BorderLayout.CENTER);
        Border padding = BorderFactory.createEmptyBorder(20, 20, 5, 20);
        panel.setBorder(padding);

        aggiornaSchedeArticoloInElaborazioneControl();

        return panel;
    }

    /**
     * @return crea i componenti per la visualizzazione dei dettagli del mese selezionato
     */
    private JComponent createDetailComponent() {
        JPanel panel = getComponentFactory().createPanel(new GridLayout(1, 3));

        JPanel panelArticoliStampati = getComponentFactory().createPanel(new BorderLayout());
        panelArticoliStampati.setBorder(BorderFactory.createTitledBorder("Stampate"));
        panelArticoliStampati.add(articoliStampatiTable.getComponent(), BorderLayout.CENTER);
        panelArticoliStampati.add(getStampaSchedeArticoloSelezionateCommand().createButton(), BorderLayout.PAGE_END);
        panel.add(panelArticoliStampati);

        JPanel panelArticoliNonValidi = getComponentFactory().createPanel(new BorderLayout());
        panelArticoliNonValidi.setBorder(BorderFactory.createTitledBorder("Non valide"));
        panelArticoliNonValidi.add(articoliNonValidiTable.getComponent(), BorderLayout.CENTER);
        panelArticoliNonValidi.add(getCreaNonValidiSelezionatiCommand().createButton(), BorderLayout.PAGE_END);
        panel.add(panelArticoliNonValidi);

        JPanel panelArticoliRimanenti = getComponentFactory().createPanel(new BorderLayout());
        panelArticoliRimanenti.setBorder(BorderFactory.createTitledBorder("Da stampare"));
        panelArticoliRimanenti.add(articoliRimanentiTable.getComponent(), BorderLayout.CENTER);
        panelArticoliRimanenti.add(getCreaRimanentiSelezionatiCommand().createButton(), BorderLayout.PAGE_END);
        panel.add(panelArticoliRimanenti);

        return panel;
    }

    /**
     * @return crea i controlli che verranno riportati in alto nella pagina
     */
    private JComponent createHeaderControl() {
        JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        panel.add(new JLabel("Anno"));

        SpinnerNumberModel model = new SpinnerNumberModel(new Integer(aziendaCorrente.getAnnoMagazzino()),
                new Integer(0), new Integer(aziendaCorrente.getAnnoMagazzino() + 100), new Integer(1));
        spinnerAnno = new JSpinner(model);
        spinnerAnno.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                mesiTable.setRows(
                        magazzinoSchedeArticoloBD.caricaSituazioneSchedeArticolo((Integer) spinnerAnno.getValue()));
            }
        });
        JPanel panelSpinner = getComponentFactory().createPanel(new BorderLayout());
        panelSpinner.add(spinnerAnno, BorderLayout.WEST);

        panel.add(panelSpinner);
        panel.add(getRefreshCommand().createButton());
        panel.add(numeroSchedeArticoloInElaborazioneLabel);

        Border padding = BorderFactory.createEmptyBorder(0, 0, 5, 0);
        panel.setBorder(padding);

        return panel;
    }

    /**
     * @return controlli per la visualizzazione dei mesi delle schede articolo
     */
    private JComponent createMesiControl() {
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Periodo"));

        mesiTable = new JideTableWidget<SituazioneSchedaArticoloDTO>("mesiTable", new String[] { "mese" },
                SituazioneSchedaArticoloDTO.class);
        mesiTable.getComponent();
        mesiTable.getTable().getColumnModel().getColumn(0).setCellRenderer(new MeseSchedeArticoloCellRenderer());
        mesiTable.setRows(magazzinoSchedeArticoloBD.caricaSituazioneSchedeArticolo((Integer) spinnerAnno.getValue()));
        // mesiTable.getTable().clearSelection();
        mesiTable.addSelectionObserver(this);
        mesiTable.selectRow(1, this);
        mesiTable.setDelayForSelection(400);
        mesiTable.selectRow(0, null);

        panel.add(mesiTable.getTable(), BorderLayout.NORTH);
        panel.setPreferredSize(new Dimension(200, 200));

        return panel;
    }

    @Override
    public void dispose() {
        articoliNonValidiTable.dispose();
        articoliRimanentiTable.dispose();
        articoliStampatiTable.dispose();
    }

    /**
     * @return the creaNonValidiSelezionatiCommand
     */
    private CreaSchedeArticoloCommand getCreaNonValidiSelezionatiCommand() {
        if (creaNonValidiSelezionatiCommand == null) {
            creaNonValidiSelezionatiCommand = new CreaSchedeArticoloCommand();
            creaNonValidiSelezionatiCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
                @Override
                public boolean preExecution(ActionCommand command) {
                    List<ArticoloRicerca> articoli = articoliNonValidiTable.getSelectedObjects();
                    command.addParameter(CreaSchedeArticoloCommand.PARAM_ANNO, mesiTable.getSelectedObject().getAnno());
                    command.addParameter(CreaSchedeArticoloCommand.PARAM_MESE, mesiTable.getSelectedObject().getMese());
                    command.addParameter(CreaSchedeArticoloCommand.PARAM_ARTICOLI, articoli);

                    return !articoli.isEmpty();
                }
            });
        }

        return creaNonValidiSelezionatiCommand;
    }

    /**
     * @return the creaRimanentiSelezionatiCommand
     */
    private CreaSchedeArticoloCommand getCreaRimanentiSelezionatiCommand() {
        if (creaRimanentiSelezionatiCommand == null) {
            creaRimanentiSelezionatiCommand = new CreaSchedeArticoloCommand();
            creaRimanentiSelezionatiCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
                @Override
                public boolean preExecution(ActionCommand command) {
                    List<ArticoloRicerca> articoli = articoliRimanentiTable.getSelectedObjects();
                    command.addParameter(CreaSchedeArticoloCommand.PARAM_ANNO, mesiTable.getSelectedObject().getAnno());
                    command.addParameter(CreaSchedeArticoloCommand.PARAM_MESE, mesiTable.getSelectedObject().getMese());
                    command.addParameter(CreaSchedeArticoloCommand.PARAM_ARTICOLI, articoli);

                    return !articoli.isEmpty();
                }
            });
        }

        return creaRimanentiSelezionatiCommand;
    }

    /**
     * @return the refreshCommand
     */
    private RefreshCommand getRefreshCommand() {
        if (refreshCommand == null) {
            refreshCommand = new RefreshCommand();
        }

        return refreshCommand;
    }

    /**
     * @return the stampaSchedeArticoloSelezionateCommand
     */
    private StampaSchedeArticoloSelezionateCommand getStampaSchedeArticoloSelezionateCommand() {
        if (stampaSchedeArticoloSelezionateCommand == null) {
            stampaSchedeArticoloSelezionateCommand = new StampaSchedeArticoloSelezionateCommand();
            stampaSchedeArticoloSelezionateCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
                @Override
                public boolean preExecution(ActionCommand command) {
                    List<ArticoloRicerca> articoli = articoliStampatiTable.getSelectedObjects();
                    command.addParameter(StampaSchedeArticoloSelezionateCommand.PARAM_ARTICOLI, articoli);
                    command.addParameter(StampaSchedeArticoloSelezionateCommand.PARAM_ANNO,
                            mesiTable.getSelectedObject().getAnno());
                    command.addParameter(StampaSchedeArticoloSelezionateCommand.PARAM_MESE,
                            mesiTable.getSelectedObject().getMese());
                    return true;
                }
            });
        }

        return stampaSchedeArticoloSelezionateCommand;
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

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    @Override
    public void setFormObject(Object object) {
    }

    /**
     * @param magazzinoSchedeArticoloBD
     *            the magazzinoSchedeArticoloBD to set
     */
    public void setMagazzinoSchedeArticoloBD(IMagazzinoSchedeArticoloBD magazzinoSchedeArticoloBD) {
        this.magazzinoSchedeArticoloBD = magazzinoSchedeArticoloBD;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

    @Override
    public void update(Observable o, Object arg) {
        SituazioneSchedaArticoloDTO situazioneSchedaArticoloDTO = (SituazioneSchedaArticoloDTO) arg;

        Integer anno = situazioneSchedaArticoloDTO.getAnno();
        Integer mese = situazioneSchedaArticoloDTO.getMese();

        articoliStampatiTable.setRows(magazzinoSchedeArticoloBD.caricaArticoliStampati(anno, mese));
        articoliNonValidiTable.setRows(magazzinoSchedeArticoloBD.caricaArticoliNonValidi(anno, mese));
        articoliRimanentiTable.setRows(magazzinoSchedeArticoloBD.caricaArticoliRimanenti(anno, mese));

        aggiornaSchedeArticoloInElaborazioneControl();
    }

}
