package it.eurotn.panjea.partite.rich.tabelle;

import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.util.Assert;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.CustomEnumComboBoxEditor;
import it.eurotn.rich.binding.CustomEnumListRenderer;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * Form di TipoAreaContabile per l'assegnazione dei suoi attributi: tipoPartita, tipoOperazione.
 *
 * @author adriano
 * @version 1.0, 08/lug/08
 */
public class TipoAreaPartitaForm extends PanjeaAbstractForm {

    /**
     * PropertyChangeListener per intercettare le variazioni della ClasseTipoDocumento all'interno dell'oggetto.
     * {@link TipoDocumento}<br>
     * per aggiornare le TipoOperazioni selezionabili
     */
    public class ClasseTipoDocumentoListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent arg0) {
            logger.debug("--> Enter propertyChange per il form Obje");
            Assert.notNull(getValueModel("tipoDocumento"), "Tipo documento non può essere nullo");
            reloadTipiOperazioni();
            logger.debug("--> Exit propertyChange");
        }
    }

    private class TipoOperazioneChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            TipoOperazione tipoOperazione = (TipoOperazione) getFormModel().getValueModel("tipoOperazione").getValue();
            TipoPartita tipoPartita = (TipoPartita) getFormModel().getValueModel("tipoPartita").getValue();

            if (chiusuraSuPagamentoComponents != null) {
                updateChiusuraSuDocumentoVisibility(tipoOperazione);
            }

            updateGestioneCorrispettiviVisibility(tipoOperazione, tipoPartita);

        }

    }

    private static final String FORM_ID = "tipoAreaPartitaForm";

    private static final String SEPARATOR_TIPO_AREA_PARTITA = "tipoAreaPartita.areaPartita.label";
    private JPanel pannelloLegendaMascheraTipoDocFatturazione;
    private final ValueHolder tipiOperazioni;
    private JComponent[] chiusuraSuPagamentoComponents = null;

    private PluginManager pluginManager;

    private JComponent[] corrispettiviComponents = null;

    /**
     * @param tipoAreaPartita
     *            tipoAreaPartita
     */
    public TipoAreaPartitaForm(final TipoAreaPartita tipoAreaPartita) {
        super(PanjeaFormModelHelper.createFormModel(tipoAreaPartita, false, FORM_ID), FORM_ID);
        this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);

        tipiOperazioni = new ValueHolder();
    }

    /**
     * Aggiunge i listener ai vari campi dal fomrmodel.
     */
    private void addListener() {
        logger.debug("--> Enter addListener");
        addFormValueChangeListener("tipoDocumento.classeTipoDocumento", new ClasseTipoDocumentoListener());
        addFormValueChangeListener("tipoOperazione", new TipoOperazioneChangeListener());
        addFormValueChangeListener("tipoPartita", new TipoOperazioneChangeListener());
        logger.debug("--> Exit addListener");
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref, fill:default:grow",
                "3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default, fill:default:grow");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
                                                                               // new
                                                                               // FormDebugPanel());
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        JLabel tdcodlabel = builder.addLabel("tipoDocumento.codice", getComponentFactory().createLabel(""), 1, 2);
        tdcodlabel.setFont(new Font(tdcodlabel.getFont().getName(), Font.BOLD, tdcodlabel.getFont().getSize()));
        JTextField tdcod = (JTextField) builder.addNestedPropertyReadOnly("tipoDocumento", "codice", 3, 2);
        tdcod.setColumns(20);
        tdcod.setFont(new Font(tdcod.getFont().getName(), Font.BOLD, tdcod.getFont().getSize()));

        JLabel tddesclabel = builder.addLabel("tipoDocumento.descrizione", getComponentFactory().createLabel(""), 5, 2);
        tddesclabel.setFont(new Font(tddesclabel.getFont().getName(), Font.BOLD, tddesclabel.getFont().getSize()));
        JTextField tddesc = (JTextField) builder.addNestedPropertyReadOnly("tipoDocumento", "descrizione", 7, 2);
        tddesc.setColumns(20);
        tddesc.setFont(new Font(tddesc.getFont().getName(), Font.BOLD, tddesc.getFont().getSize()));
        builder.nextRow();

        builder.addHorizontalSeparator(getMessage(SEPARATOR_TIPO_AREA_PARTITA), 7);
        builder.nextRow();

        builder.addPropertyAndLabel("tipoPartita");
        builder.nextRow();

        reloadTipiOperazioni();

        ComboBoxBinding comboBoxBinding = (ComboBoxBinding) bf.createBoundComboBox("tipoOperazione", tipiOperazioni);
        comboBoxBinding.setRenderer(new CustomEnumListRenderer(getMessages()));
        comboBoxBinding.setEditor(new CustomEnumComboBoxEditor(getMessages(), comboBoxBinding.getEditor()));
        builder.addLabel("tipoOperazione", 1);
        builder.addBinding(comboBoxBinding, 3);

        chiusuraSuPagamentoComponents = builder.addPropertyAndLabel("chiusuraSuPagamentoUnico", 5);

        // chiusuraSuPagamentoUnico è presente solo su un tipo documento incasso quindi il flag gestione
        // corrispettivi lo metto alla stesso posto visto che non lo avrò mai per quel tipo documento
        corrispettiviComponents = builder.addPropertyAndLabel("gestioneCorrispettivi", 5);

        builder.nextRow();

        builder.addPropertyAndLabel("tipoPagamentoChiusura");
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("descrizionePerFlusso")[1]).setColumns(15);
        builder.nextRow();

        builder.addPropertyAndLabel("speseIncasso");
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("mascheraFlussoBanca", 1, 16, 4)[1]).setColumns(45);
        builder.nextRow();

        // pannello per visualizzare la legenda della maschera per il tipo
        // documento di fatturazione
        createPanelLegendaFatturazione();
        builder.addComponent(pannelloLegendaMascheraTipoDocFatturazione, 3, 18, 5, 1);

        TipoOperazione tipoOperazione = (TipoOperazione) getValue("tipoOperazione");
        TipoPartita tipoPartita = (TipoPartita) getValue("tipoPartita");
        updateChiusuraSuDocumentoVisibility(tipoOperazione);
        updateGestioneCorrispettiviVisibility(tipoOperazione, tipoPartita);

        addListener();
        return builder.getPanel();
    }

    /**
     * Crea la legenda per le variabili possibili per la maschera di fatturazione.
     *
     * @return maschera in formato html
     */
    private String createLegendaTextForMascheraFatturazione() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html>");
        stringBuffer.append("<B>");
        stringBuffer.append(getMessage("legenda").toUpperCase());
        stringBuffer.append("</B><BR><hr></hr>");
        stringBuffer.append("<ul>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$numeroDocumento$</B> = ");
        stringBuffer.append(getMessage("numeroDocumento"));
        stringBuffer.append("</li>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$dataDocumento$</B> = ");
        stringBuffer.append(getMessage("dataDocumento"));
        stringBuffer.append("</li>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$codiceTipoDocumento$</B> = ");
        stringBuffer.append(getMessage("codiceTipoDocumento"));
        stringBuffer.append("</li>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$descrizioneTipoDocumento$</B> = ");
        stringBuffer.append(getMessage("descrizioneTipoDocumento"));
        stringBuffer.append("</li>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$numeroRata$</B> = ");
        stringBuffer.append(getMessage("numeroRata"));
        stringBuffer.append("</li>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$CIG$</B> = ");
        stringBuffer.append(getMessage("cig"));
        stringBuffer.append("</li>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$CUP$</B> = ");
        stringBuffer.append(getMessage("cup"));
        stringBuffer.append("</li>");

        stringBuffer.append("</ul>");
        stringBuffer.append("</html>");

        return stringBuffer.toString();
    }

    /**
     * @return pannello della legenda
     */
    private JPanel createPanelLegendaFatturazione() {
        pannelloLegendaMascheraTipoDocFatturazione = getComponentFactory().createPanel(new BorderLayout());
        JLabel labelLegenda = new JLabel(createLegendaTextForMascheraFatturazione());
        pannelloLegendaMascheraTipoDocFatturazione.add(labelLegenda, BorderLayout.CENTER);
        return pannelloLegendaMascheraTipoDocFatturazione;
    }

    /**
     * Ricarica i tipi opreazione possibili in base al tipoDocumento.
     */
    private void reloadTipiOperazioni() {
        List<TipoAreaPartita.TipoOperazione> tipiOperazioniCollection;
        if (getValueModel("tipoDocumento") != null) {
            TipoDocumento tipoDocumento = (TipoDocumento) getValueModel("tipoDocumento").getValue();
            tipiOperazioniCollection = Arrays.asList(TipoAreaPartita.TipoOperazione
                    .valuesForClasseTipoDocumento(tipoDocumento.getClasseTipoDocumento()));
        } else {
            // inizializza la collection con tutti gli elementi di
            // TipoOperazione
            tipiOperazioniCollection = Arrays.asList(TipoAreaPartita.TipoOperazione.values());
        }
        tipiOperazioni.setValue(tipiOperazioniCollection);
    }

    /**
     * Rende visibili o meno i controlli per la form property chiusuraSuPagamentoUnico.
     *
     * @param tipoOperazione
     *            il tipo operazione per decidere la visibilità dei controlli
     */
    private void updateChiusuraSuDocumentoVisibility(TipoOperazione tipoOperazione) {
        boolean visible = false;
        if (tipoOperazione != null) {
            visible = TipoOperazione.CHIUSURA.equals(tipoOperazione);
        }
        chiusuraSuPagamentoComponents[0].setVisible(visible);
        chiusuraSuPagamentoComponents[1].setVisible(visible);
    }

    /**
     * Rende visibili o meno i controlli per la form property gestioneCorrispettivi.
     *
     * @param tipoOperazione
     *            il tipo operazione per decidere la visibilità dei controlli
     */
    private void updateGestioneCorrispettiviVisibility(TipoOperazione tipoOperazione, TipoPartita tipoPartita) {
        boolean visible = pluginManager.isPresente(PluginManager.PLUGIN_CORRISPETTIVI);
        visible = visible && (tipoOperazione != null && TipoOperazione.GENERA.equals(tipoOperazione));
        visible = visible && (tipoPartita != null && TipoPartita.ATTIVA.equals(tipoPartita));

        corrispettiviComponents[0].setVisible(visible);
        corrispettiviComponents[1].setVisible(visible);

        if (!visible) {
            getFormModel().getValueModel("gestioneCorrispettivi").setValue(false);
        }
    }
}
