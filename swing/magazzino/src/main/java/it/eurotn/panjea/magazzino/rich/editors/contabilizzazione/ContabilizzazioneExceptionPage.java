package it.eurotn.panjea.magazzino.rich.editors.contabilizzazione;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiDuplicatiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.AreeContabiliDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContiEntitaAssentiException;
import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.service.exception.SottoContiContabiliAssentiException;
import it.eurotn.panjea.magazzino.service.exception.SottoContoContabileAssenteException;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class ContabilizzazioneExceptionPage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    public static final String PAGE_ID = "contabilizzazioneExceptionPage";

    private ContabilizzazioneException contabilizzazioneException;

    private JPanel documentiDuplicatiPanel;
    private JideTableWidget<DocumentoDuplicateException> documentiDuplicatiTable;

    private JPanel areeContabiliDuplicatePanel;
    private JideTableWidget<AreaContabileDuplicateException> areeContabiliDuplicateTable;

    private JPanel contiEntitaAssentiPanel;
    private JideTableWidget<ContoEntitaAssenteException> contiEntitaAssentiTable;

    private JPanel sottoContiContabiliAssentiPanel;
    private JideTableWidget<SottoContoContabileAssenteException> sottoContiContabiliAssentiTable;

    /**
     * Costruttore.
     * 
     */
    protected ContabilizzazioneExceptionPage() {
        super(PAGE_ID);
    }

    /**
     * Crea i controlli per visualizzare le informazioni contenute nell'eccezione
     * {@link AreeContabiliDuplicateException}.
     * 
     * @return controlli creati
     */
    private JPanel createAreeContabiliDuplicateExceptionComponent() {

        // exceptionTable.setRows(areeException.getAreeContabiliDuplicateException());
        areeContabiliDuplicateTable.setPropertyCommandExecutor(new ActionCommandExecutor() {

            @Override
            public void execute() {
                if (areeContabiliDuplicateTable.getSelectedObject() != null) {
                    LifecycleApplicationEvent event = new OpenEditorEvent(areeContabiliDuplicateTable
                            .getSelectedObject().getAreaContabileEsistente().getAreaContabileLite());
                    Application.instance().getApplicationContext().publishEvent(event);
                }
            }
        });

        areeContabiliDuplicatePanel = getComponentFactory().createPanel(new BorderLayout());
        areeContabiliDuplicatePanel.setVisible(false);
        areeContabiliDuplicatePanel.add(areeContabiliDuplicateTable.getComponent(), BorderLayout.CENTER);
        areeContabiliDuplicatePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), getMessage("areaContabileDuplicate.title")));
        areeContabiliDuplicatePanel.setPreferredSize(new Dimension(800, 200));

        return areeContabiliDuplicatePanel;
    }

    /**
     * Crea i controlli per visualizzare le informazioni contenute nell'eccezione.
     * {@link ContiEntitaAssentiException}.
     * 
     * @return il component con la lista dei conti entità assenti
     */
    private JPanel createContiEntitaAssentiExceptionComponent() {

        // exceptionTable.setRows(documentiDuplicatiException.getDocumentiDuplicatiException());
        contiEntitaAssentiTable.setPropertyCommandExecutor(new ActionCommandExecutor() {

            @Override
            public void execute() {
                if (contiEntitaAssentiTable.getSelectedObject() != null) {
                    IAnagraficaBD anagraficaBD = RcpSupport.getBean("anagraficaBD");
                    LifecycleApplicationEvent event = new OpenEditorEvent(anagraficaBD
                            .caricaEntita(contiEntitaAssentiTable.getSelectedObject().getEntitaLite(), false));
                    Application.instance().getApplicationContext().publishEvent(event);
                }
            }
        });

        contiEntitaAssentiPanel = getComponentFactory().createPanel(new BorderLayout());
        contiEntitaAssentiPanel.setVisible(false);
        contiEntitaAssentiPanel.add(contiEntitaAssentiTable.getComponent(), BorderLayout.CENTER);
        contiEntitaAssentiPanel.setPreferredSize(new Dimension(800, 200));
        contiEntitaAssentiPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                getMessage("contiEntitaAssenti.title")));

        return contiEntitaAssentiPanel;
    }

    @Override
    protected JComponent createControl() {
        JPanel rootPanel = getComponentFactory().createPanel(new VerticalLayout(10));

        sottoContiContabiliAssentiTable = new JideTableWidget<SottoContoContabileAssenteException>(PAGE_ID,
                new String[] { "articolo", "deposito", "sedeEntita", "categoriaContabileArticolo",
                        "categoriaContabileDeposito", "categoriaContabileSedeMagazzino" },
                SottoContoContabileAssenteException.class);
        sottoContiContabiliAssentiPanel = createSottoContiContabiliAssentiExceptionComponent();
        rootPanel.add(sottoContiContabiliAssentiPanel);

        contiEntitaAssentiTable = new JideTableWidget<ContoEntitaAssenteException>(PAGE_ID, new String[] { "entita" },
                ContoEntitaAssenteException.class);
        contiEntitaAssentiPanel = createContiEntitaAssentiExceptionComponent();
        rootPanel.add(contiEntitaAssentiPanel);

        areeContabiliDuplicateTable = new JideTableWidget<AreaContabileDuplicateException>(
                "areaContabileDuplicateTable", new String[] { "areaContabileEsistente.codice",
                        "areaContabileEsistente.documento.entita", "areaContabileEsistente.documento.dataDocumento" },
                AreaContabileDuplicateException.class);
        areeContabiliDuplicatePanel = createAreeContabiliDuplicateExceptionComponent();
        rootPanel.add(areeContabiliDuplicatePanel);

        documentiDuplicatiTable = new JideTableWidget<DocumentoDuplicateException>("documentiDuplicatiTable",
                new String[] { "documento.codice", "documento.entita", "documento.dataDocumento",
                        "documento.tipoDocumento" },
                DocumentoDuplicateException.class);
        documentiDuplicatiPanel = createDocumentiDuplicatiExceptionComponent();
        rootPanel.add(documentiDuplicatiPanel);

        GuiStandardUtils.attachBorder(rootPanel);

        JScrollPane scroll = getComponentFactory().createScrollPane(rootPanel);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    /**
     * Crea i controlli per visualizzare le informazioni contenute nell'eccezione.
     * {@link DocumentiDuplicatiException}.
     * 
     * @return il component con la lista di documenti duplicati
     */
    private JPanel createDocumentiDuplicatiExceptionComponent() {
        // exceptionTable.setRows(documentiDuplicatiException.getDocumentiDuplicatiException());

        documentiDuplicatiPanel = getComponentFactory().createPanel(new BorderLayout());
        documentiDuplicatiPanel.setVisible(false);
        documentiDuplicatiPanel.add(documentiDuplicatiTable.getComponent(), BorderLayout.CENTER);
        documentiDuplicatiPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                getMessage("documentoDuplicate.title")));
        documentiDuplicatiPanel.setPreferredSize(new Dimension(800, 200));

        return documentiDuplicatiPanel;
    }

    /**
     * Crea i controlli per visualizzare le informazioni contenute nell'eccezione.
     * {@link SottoContiContabiliAssentiException}.
     * 
     * @return il component con la lista dei sotto conto contabili
     */
    private JPanel createSottoContiContabiliAssentiExceptionComponent() {

        // exceptionTable.setRows(documentiDuplicatiException.getDocumentiDuplicatiException());

        sottoContiContabiliAssentiPanel = getComponentFactory().createPanel(new BorderLayout());
        sottoContiContabiliAssentiPanel.setVisible(false);

        sottoContiContabiliAssentiPanel.add(sottoContiContabiliAssentiTable.getComponent(), BorderLayout.CENTER);
        sottoContiContabiliAssentiPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), getMessage("sottoContiContabiliAssenti.title")));
        sottoContiContabiliAssentiPanel.setPreferredSize(new Dimension(800, 200));

        return sottoContiContabiliAssentiPanel;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void loadData() {
        if (contabilizzazioneException.getSottoContiContabiliAssentiException() != null) {
            sottoContiContabiliAssentiPanel.setVisible(true);
            sottoContiContabiliAssentiTable.setRows(contabilizzazioneException.getSottoContiContabiliAssentiException()
                    .getAllSottoContoContabileAssenteExceptions());
        } else {
            sottoContiContabiliAssentiPanel.setVisible(false);
        }

        if (contabilizzazioneException.getContiEntitaAssentiException() != null) {
            contiEntitaAssentiPanel.setVisible(true);
            contiEntitaAssentiTable
                    .setRows(contabilizzazioneException.getContiEntitaAssentiException().getContiEntitaExceptions());
        } else {
            contiEntitaAssentiPanel.setVisible(false);
        }

        if (contabilizzazioneException.getAreeContabiliDuplicateException() != null) {
            areeContabiliDuplicatePanel.setVisible(true);
            areeContabiliDuplicateTable.setRows(contabilizzazioneException.getAreeContabiliDuplicateException()
                    .getAreeContabiliDuplicateException());
        } else {
            areeContabiliDuplicatePanel.setVisible(false);
        }

        if (contabilizzazioneException.getDocumentiDuplicatiException() != null) {
            documentiDuplicatiPanel.setVisible(true);
            documentiDuplicatiTable.setRows(
                    contabilizzazioneException.getDocumentiDuplicatiException().getDocumentiDuplicatiException());
        } else {
            documentiDuplicatiPanel.setVisible(false);
        }
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
        loadData();
    }

    @Override
    public void restoreState(Settings settings) {
        // areeContabiliDuplicateTable.restoreState(settings);
        // contiEntitaAssentiTable.restoreState(settings);
        // documentiDuplicatiTable.restoreState(settings);
        // sottoContiContabiliAssentiTable.restoreState(settings);

    }

    @Override
    public void saveState(Settings settings) {
        areeContabiliDuplicateTable.saveState(settings);
        contiEntitaAssentiTable.saveState(settings);
        documentiDuplicatiTable.saveState(settings);
        sottoContiContabiliAssentiTable.saveState(settings);

    }

    @Override
    public void setFormObject(Object object) {
        this.contabilizzazioneException = (ContabilizzazioneException) object;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Metodo non implementato");
    }

}
