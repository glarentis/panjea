package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.ETipologiaContoControPartita;
import it.eurotn.panjea.contabilita.rich.bd.ContabilitaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.forms.ControPartitaForm;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * @author fattazzo
 * @version 1.0, 03/set/07
 */
public class ControPartitaPage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "controPartitaPage";

    public static final String STRUTTURA_CONTABILE_VARIABILI_LEGENDA_TITLE = "controPartitaTablePage.variabili.legenda";
    public static final String STRUTTURA_CONTABILE_VARIABILE = "controPartitaTablePage.variabile.";
    private final IContabilitaAnagraficaBD contabilitaAnagraficaBD;
    private final IContabilitaBD contabilitaBD = RcpSupport.getBean(ContabilitaBD.BEAN_ID);

    private EntitaLite entita = null;
    private TipoDocumento tipoDocumento = null;

    /**
     * Costruttore.
     *
     * @param contabilitaAnagraficaBD
     *            contabilitaAnagraficaBD
     */
    public ControPartitaPage(final IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        super(PAGE_ID, new ControPartitaForm(new ControPartita(), contabilitaAnagraficaBD));
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    /**
     * @return crea il pannello che contiene l'elenco delle variabili disponibili
     */
    private JPanel creaLegendaVariabili() {
        // carico l'elenco delle variabili che si possono avere
        List<String> variabili = contabilitaBD.caricaVariabiliFormulaControPartite();

        // se non ce ne sono non metto niente sul piede della tabella
        if (variabili == null || variabili.isEmpty()) {
            return null;
        } else {
            JPanel panelNorth = new JPanel(new BorderLayout());
            panelNorth.setBorder(
                    BorderFactory.createTitledBorder(getMessage(STRUTTURA_CONTABILE_VARIABILI_LEGENDA_TITLE)));

            StringBuilder descrizione = new StringBuilder();
            descrizione.append("<HTML>");
            for (String variabile : variabili) {
                String nomeVariabile = getMessage(STRUTTURA_CONTABILE_VARIABILE + variabile);
                descrizione.append("<B>" + variabile + " : </B>" + nomeVariabile + "<BR>");
            }
            descrizione.append("</HTML>");
            JLabel label = new JLabel(descrizione.toString());
            JScrollPane scrollPane = getComponentFactory().createScrollPane(label);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            panelNorth.add(scrollPane, BorderLayout.NORTH);

            // pannello principale per compattare il box della legenda
            JPanel panel = getComponentFactory().createPanel(new BorderLayout());
            panel.add(panelNorth, BorderLayout.CENTER);
            return panel;
        }
    }

    @Override
    protected Object doDelete() {
        contabilitaAnagraficaBD.cancellaControPartita((ControPartita) getBackingFormPage().getFormObject());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        ControPartita controPartita = (ControPartita) getBackingFormPage().getFormObject();
        controPartita = contabilitaAnagraficaBD.salvaControPartita(controPartita);
        return controPartita;
    }

    @Override
    public AbstractCommand[] getCommand() {
        return null;
    }

    @Override
    public JComponent getControl() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(super.getControl(), BorderLayout.CENTER);
        panel.add(creaLegendaVariabili(), BorderLayout.EAST);
        ((Focussable) getForm()).grabFocus();
        return panel;
    }

    /**
     * Verifica che i dati della contropartita siano validi.
     *
     * @return <code>true</code> se i dati sono validi
     */
    public boolean isControPartitaValid() {
        ControPartita controPartitaForm = (ControPartita) getBackingFormPage().getFormObject();

        ControPartita controPartita = (ControPartita) PanjeaSwingUtil.cloneObject(controPartitaForm);

        if ((controPartita.getAvere() == null || controPartita.getAvere().getId() == -1)
                && (controPartita.getDare() == null || controPartita.getDare().getId() == -1)
                && (controPartita.getContoAvere() == null || controPartita.getContoAvere().getId() == -1)
                && (controPartita.getContoDare() == null || controPartita.getContoDare().getId() == -1)
                && (controPartita.getContoBaseAvere() == null || controPartita.getContoBaseAvere().getId() == -1)
                && (controPartita.getContoBaseDare() == null || controPartita.getContoBaseDare().getId() == -1)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void loadData() {
        // non faccio niente
    }

    @Override
    public ILock onLock() {
        super.onLock();
        return toolbarPageEditor.getLock();
    }

    @Override
    public void onNew() {
        ControPartita controPartita = new ControPartita();
        controPartita.setTipoDocumento(this.tipoDocumento);
        controPartita.setEntita(this.entita);
        controPartita.setTipologiaContoControPartita(ETipologiaContoControPartita.SOTTOCONTO);
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void refreshData() {
        // non faccio niente
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param tipoDocumento
     *            the tipoDocumento to set
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
