package it.eurotn.panjea.contabilita.rich.editors.tabelle.strutturacontabile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.CodiceIvaPrevalente;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class StrutturaContabileControPartitePage extends AbstractDialogPage implements PropertyChangeListener {

    private static final String PAGE_ID = "strutturaContabileControPartitePage";

    private final TipoAreaContabile tipoAreaContabile;
    private final IContabilitaAnagraficaBD contabilitaAnagraficaBD;

    private FormModel codiceIvaPrevalenteFormModel;

    private StruttureContabiliTablePage struttureContabiliTablePage;

    private ControPartitaTablePage controPartitaTablePage;

    /**
     * Costruttore.
     *
     * @param tipoAreaContabile
     *            tipo area contabile di riferimento
     * @param contabilitaAnagraficaBD
     *            contabilitaAnagraficaBD
     */
    public StrutturaContabileControPartitePage(final TipoAreaContabile tipoAreaContabile,
            final IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        super(PAGE_ID);
        this.tipoAreaContabile = tipoAreaContabile;
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    /**
     * Crea i controlli per la gestione del codice iva prevalente.
     *
     * @return controlli creati
     */
    private JComponent createCodiceIvaPrevalenteControl() {
        loadDataCodiceIvaPrevalente(null);

        PanjeaSwingBindingFactory bf = null;
        bf = (PanjeaSwingBindingFactory) ((BindingFactoryProvider) getService(BindingFactoryProvider.class))
                .getBindingFactory(codiceIvaPrevalenteFormModel);
        TableFormBuilder builder = new TableFormBuilder(bf);
        builder.setLabelAttributes("colSpec=right:pref:");
        builder.row();
        builder.add(bf.createBoundSearchText("codiceIva", new String[] { "codice" }), "align=left");

        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.add(builder.getForm(), BorderLayout.CENTER);

        return GuiStandardUtils.attachBorder(panel, BorderFactory.createEmptyBorder(1, 1, 5, 1));
    }

    /**
     * Crea i controlli, nel caso in cui il tipo documento ha settato un tipo entità cliente o fornitore crea due radio
     * button (default e entita button); in tutti gli altri casi ritorno un panel vuoto.
     *
     * @return Jcomponent
     */
    private JComponent createComboEntita() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // creo i controlli per selezionare l'entita' per caricare le strutture contabili
        if (tipoAreaContabile.getTipoDocumento().getTipoEntita() == TipoEntita.CLIENTE
                || tipoAreaContabile.getTipoDocumento().getTipoEntita() == TipoEntita.FORNITORE) {

            JLabel entitaLabel = new JLabel(
                    RcpSupport.getMessage(tipoAreaContabile.getTipoDocumento().getClass().getName() + "$TipoEntita."
                            + tipoAreaContabile.getTipoDocumento().getTipoEntita().name()));
            panel.add(entitaLabel, BorderLayout.WEST);
            JPanel panelEntita = new JPanel(new BorderLayout());
            panel.add(panelEntita, BorderLayout.CENTER);

            final EntitaPMForm entitaPMForm = new EntitaPMForm(tipoAreaContabile.getTipoDocumento());
            panelEntita.add(entitaPMForm.getControl(), BorderLayout.CENTER);

            // value change su entita', quindi quando viene inserita una entita' selezionando il relativo radio button
            // ho la necessita' di ricaricare i dati delle dialog page di struttura contabile e contropartita
            entitaPMForm.getFormModel().getValueModel("entita").addValueChangeListener(new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    EntitaLite entitaLite = (EntitaLite) evt.getNewValue();
                    reloadData(entitaLite);

                    saveOrDeleteCodiceIvaPrevalente();
                    loadDataCodiceIvaPrevalente(entitaLite);
                }
            });
        }
        return panel;
    }

    @Override
    protected JComponent createControl() {
        JPanel panelHeader = getComponentFactory().createPanel(new BorderLayout());
        panelHeader.add(createComboEntita(), BorderLayout.NORTH);
        panelHeader.add(createCodiceIvaPrevalenteControl(), BorderLayout.CENTER);

        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(10, 10));
        JPanel tablePanel = getComponentFactory().createPanel(new BorderLayout());

        panel.add(panelHeader, BorderLayout.NORTH);

        tablePanel.add(createStrutturaContabileComponent(), BorderLayout.NORTH);
        tablePanel.add(createControPartitaComponent(), BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crea i controlli delle righe delle contro partite.
     *
     * @return controlli creati
     */
    private JComponent createControPartitaComponent() {

        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Contro partite"));
        controPartitaTablePage = new ControPartitaTablePage(tipoAreaContabile.getTipoDocumento(),
                contabilitaAnagraficaBD);
        controPartitaTablePage.loadData();
        panel.add(controPartitaTablePage.getControl(), BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crea i controlli delle righe della struttura contabile..
     *
     * @return controlli creati
     */
    private JComponent createStrutturaContabileComponent() {

        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Strutture contabili"));
        struttureContabiliTablePage = new StruttureContabiliTablePage(tipoAreaContabile.getTipoDocumento(),
                contabilitaAnagraficaBD);
        struttureContabiliTablePage.loadData();
        struttureContabiliTablePage
                .addPropertyChangeListener(StruttureContabiliTablePage.CONTROPARTITA_STRUTTURA_CONTABILE_CHANGE, this);
        panel.add(struttureContabiliTablePage.getControl(), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(100, 200));
        panel.setMinimumSize(new Dimension(100, 200));
        return panel;
    }

    /**
     * Carica il codice iva prevalente.
     *
     * @param entitaLite
     *            entità di riferimento
     */
    private void loadDataCodiceIvaPrevalente(EntitaLite entitaLite) {
        CodiceIvaPrevalente codiceIvaPrevalente = contabilitaAnagraficaBD.caricaCodiceIvaPrevalente(tipoAreaContabile,
                entitaLite);

        if (codiceIvaPrevalente == null) {
            codiceIvaPrevalente = new CodiceIvaPrevalente();
            codiceIvaPrevalente.setTipoAreaContabile(tipoAreaContabile);
            if (entitaLite != null && entitaLite.isNew()) {
                codiceIvaPrevalente.setEntita(null);
            } else {
                codiceIvaPrevalente.setEntita(entitaLite);
            }
        }

        if (codiceIvaPrevalenteFormModel == null) {
            codiceIvaPrevalenteFormModel = PanjeaFormModelHelper.createFormModel(codiceIvaPrevalente, false,
                    "codiceIvaPrevalenteForm");
        } else {
            codiceIvaPrevalenteFormModel.setFormObject(codiceIvaPrevalente);
        }

        boolean isNew = entitaLite != null && entitaLite.isNew();
        codiceIvaPrevalenteFormModel.getFieldMetadata("codiceIva").setEnabled(!isNew);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        // quando cambia la struttura contabile ricarico le contro partite
        controPartitaTablePage.loadData();
    }

    private void reloadData(EntitaLite entita) {

        struttureContabiliTablePage.setEntita(entita);
        struttureContabiliTablePage.loadData();

        controPartitaTablePage.setEntita(entita);
        controPartitaTablePage.loadData();
    }

    /**
     * Controlla se il formModel del codice iva prevalente e' dirty. In questo caso prende il codice iva prevalente
     * contenuto e lo salva o se il codice iva e' impostato a null viene cancellato il codice iva prevalente.
     */
    public void saveOrDeleteCodiceIvaPrevalente() {
        if (codiceIvaPrevalenteFormModel.isDirty()) {
            codiceIvaPrevalenteFormModel.commit();
            CodiceIvaPrevalente codiceIvaPrevalente = (CodiceIvaPrevalente) codiceIvaPrevalenteFormModel
                    .getFormObject();

            if (codiceIvaPrevalente.getCodiceIva() == null) {
                if (!codiceIvaPrevalente.isNew()) {
                    contabilitaAnagraficaBD.cancellaCodiceIvaPrevalente(codiceIvaPrevalente);
                }
            } else {
                CodiceIvaPrevalente codiceIvaCaricato = contabilitaAnagraficaBD
                        .caricaCodiceIvaPrevalente(tipoAreaContabile, codiceIvaPrevalente.getEntita());
                if (codiceIvaCaricato != null) {
                    codiceIvaCaricato.setCodiceIva(codiceIvaPrevalente.getCodiceIva());
                    contabilitaAnagraficaBD.salvaCodiceIvaPrevalente(codiceIvaCaricato);
                } else {
                    contabilitaAnagraficaBD.salvaCodiceIvaPrevalente(codiceIvaPrevalente);
                }

            }
        }
    }

}
