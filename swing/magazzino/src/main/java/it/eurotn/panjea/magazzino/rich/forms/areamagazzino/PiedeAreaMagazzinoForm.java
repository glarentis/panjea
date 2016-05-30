/**
 *
 */
package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.swing.ListBinding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.ImportoBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * @author fattazzo
 *
 */
public class PiedeAreaMagazzinoForm extends PanjeaAbstractForm {

    @SuppressWarnings("serial")
    private class AgentiRenderer extends DefaultListCellRenderer {

        private DecimalFormat format = new DecimalFormat("#,##0.00 €");

        @Override
        public Component getListCellRendererComponent(JList<?> jlist, Object obj, int i, boolean flag, boolean flag1) {
            JLabel label = new JLabel();
            label.setText(ObjectConverterManager.toString(obj));
            label.setIcon(RcpSupport.getIcon(Agente.class.getName()));
            label.setEnabled(true);
            label.setOpaque(false);

            FormLayout layout = new FormLayout("left:120dlu", "pref");
            JPanel panelAgente = new JPanel(layout);
            panelAgente.setOpaque(false);
            CellConstraints cc = new CellConstraints();
            panelAgente.add(label, cc.xy(1, 1));

            layout = new FormLayout("right:40dlu", "pref");
            JPanel panelProvvigione = new JPanel(layout);
            panelProvvigione.setOpaque(false);
            AgenteLite agente = (AgenteLite) obj;
            JLabel provvigioneLabel = new JLabel(format.format(agente.getImportoProvvigione()));
            panelProvvigione.add(provvigioneLabel, cc.xy(1, 1));

            JPanel rootPanel = new JPanel(new HorizontalLayout(5));
            rootPanel.setOpaque(false);
            rootPanel.add(panelAgente);
            rootPanel.add(panelProvvigione);
            return rootPanel;
        }
    }

    private class AreaPartitaVisiblePropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("--> Enter propertyChange");
            if (!getFormModel().isEnabled()) {
                return;
            }
            if ("enabled".equals(evt.getPropertyName())) {
                boolean visible = ((Boolean) evt.getNewValue()).booleanValue();
                speseIncassoLabel.setVisible(visible);
                speseIncassoComponent.setVisible(visible);
                giornoLimiteComponent[0].setVisible(visible);
                giornoLimiteComponent[1].setVisible(visible);
                percentualeScontoIncassoComponent.setVisible(visible);
                percentualeScontoIncassoLabel.setVisible(visible);
            }
        }
    }

    private class DirtyPropertyChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            getFormModel().getFieldMetadata("areaMagazzino.speseTrasporto").setReadOnly(true);
            getFormModel().getFieldMetadata("areaMagazzino.altreSpese").setReadOnly(true);
            getFormModel().getFieldMetadata("areaMagazzino.totaleMerce").setReadOnly(true);
            getFormModel().getFieldMetadata("areaMagazzino.documento.totale").setReadOnly(true);
            getFormModel().getFieldMetadata("areaMagazzino.documento.imposta").setReadOnly(true);
            getFormModel().getFieldMetadata("areaRate.percentualeSconto").setReadOnly(true);
            getFormModel().getFieldMetadata("areaRate.giorniLimite").setReadOnly(true);
        }
    }

    public class FormObjectPropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AreaMagazzinoFullDTO am = (AreaMagazzinoFullDTO) getFormObject();
            if (panelRiepilogoIva != null) {
                boolean riepilogoIva = am.getAreaMagazzino().getTipoAreaMagazzino() != null
                        && am.getAreaMagazzino().getTipoAreaMagazzino().isRiepilogoIva();
                panelRiepilogoIva.setVisible(riepilogoIva);
            }
            agentiPanel.setVisible(am.getAgenti() != null && !am.getAgenti().isEmpty());
        }

    }

    private class ImportoSpeseIncassoVisiblePropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("--> Enter propertyChange");
            if (!getFormModel().isEnabled()) {
                return;
            }

            boolean visible = getFormModel().getFieldMetadata("areaRate.speseIncasso").isEnabled();
            speseIncassoLabel.setVisible(visible);
            speseIncassoComponent.setVisible(visible);
        }

    }

    @SuppressWarnings("serial")
    private class RigaIvaRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> jlist, Object obj, int i, boolean flag, boolean flag1) {
            RigaIva rigaIva = (RigaIva) obj;
            FormLayout layout = new FormLayout("left:50dlu", "pref");
            JPanel panelRigaIva = new JPanel(layout);
            panelRigaIva.setOpaque(false);
            CellConstraints cc = new CellConstraints();
            if (rigaIva.getCodiceIva() != null) {
                JLabel codiceLabel = new JLabel(rigaIva.getCodiceIva().getCodice());
                panelRigaIva.add(codiceLabel, cc.xy(1, 1));
            }

            layout = new FormLayout("left:50dlu", "pref");
            JPanel panelImponibile = new JPanel(layout);
            panelImponibile.setOpaque(false);
            JLabel imponibileLabel = new JLabel(format.format(rigaIva.getImponibile().getImportoInValutaAzienda()));
            panelImponibile.add(imponibileLabel, cc.xy(1, 1));

            layout = new FormLayout("left:50dlu", "pref");
            JPanel panelImposta = new JPanel(layout);
            panelImposta.setOpaque(false);
            JLabel impostaLabel = new JLabel(format.format(rigaIva.getImposta().getImportoInValutaAzienda()));
            panelImposta.add(impostaLabel, cc.xy(1, 1));

            JPanel rootPanel = new JPanel(new HorizontalLayout(5));
            rootPanel.setOpaque(false);
            rootPanel.add(panelRigaIva);
            rootPanel.add(panelImponibile);
            rootPanel.add(panelImposta);
            return rootPanel;
        }
    }

    public static final String FORM_ID = "piedeAreaMagazzinoForm";

    private DecimalFormat format = new DecimalFormat("#,##0.00 €");

    private JComponent speseIncassoComponent = null;

    private JLabel speseIncassoLabel = null;

    private ImportoSpeseIncassoVisiblePropertyChange importoSpeseIncassoVisiblePropertyChange;
    private DirtyPropertyChangeListener dirtyPropertyChangeListener;

    private AreaPartitaVisiblePropertyChange areaPartitaVisiblePropertyChange;

    private JComponent percentualeScontoIncassoComponent;

    private JComponent[] giornoLimiteComponent;

    private JLabel percentualeScontoIncassoLabel;

    private JPanel panelRiepilogoIva;

    private JPanel agentiPanel;

    private PluginManager pluginManager = null;

    /**
     * Costruttore.
     *
     * @param pageFormModel
     *            form model principale della pagina
     */
    public PiedeAreaMagazzinoForm(final FormModel pageFormModel) {
        super(pageFormModel, FORM_ID);
        pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
    }

    private JPanel creaHeaderRiepilogoIva(ListBinding bindingRiepilogoIva, ListBinding bindingAgenti) {
        FormLayout layout = new FormLayout("left:50dlu", "pref");
        JPanel panelRigaIva = new JPanel(layout);
        panelRigaIva.setOpaque(false);
        CellConstraints cc = new CellConstraints();
        JLabel codiceLabel = new JLabel("<HTML><u>Codice IVA</u></HTML>");
        panelRigaIva.add(codiceLabel, cc.xy(1, 1));

        layout = new FormLayout("left:50dlu", "pref");
        JPanel panelImponibile = new JPanel(layout);
        panelImponibile.setOpaque(false);
        JLabel imponibileLabel = new JLabel("<HTML><u>Imponibile</u></HTML>");
        panelImponibile.add(imponibileLabel, cc.xy(1, 1));

        layout = new FormLayout("left:50dlu", "pref");
        JPanel panelImposta = new JPanel(layout);
        panelImposta.setOpaque(false);
        JLabel impostaLabel = new JLabel("<HTML><u>Imposta</u></HTML>");
        panelImposta.add(impostaLabel, cc.xy(1, 1));

        JPanel headerPanel = new JPanel(new HorizontalLayout(5));
        headerPanel.setOpaque(false);
        headerPanel.add(panelRigaIva);
        headerPanel.add(panelImponibile);
        headerPanel.add(panelImposta);
        headerPanel.setBorder(null);

        JPanel riepilogoIvaPanel = new JPanel(new BorderLayout());
        riepilogoIvaPanel.add(headerPanel, BorderLayout.NORTH);
        riepilogoIvaPanel.add(bindingRiepilogoIva.getControl(), BorderLayout.CENTER);
        riepilogoIvaPanel.setBorder(BorderFactory.createTitledBorder("Riepilogo Iva"));

        agentiPanel = new JPanel(new BorderLayout());
        agentiPanel.setBorder(BorderFactory.createTitledBorder("Agenti"));
        agentiPanel.add(bindingAgenti.getControl(), BorderLayout.CENTER);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(riepilogoIvaPanel, BorderLayout.NORTH);
        rootPanel.add(agentiPanel, BorderLayout.SOUTH);
        return rootPanel;
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();

        FormLayout layout = new FormLayout(
                "left:pref, 4dlu, fill:80dlu, 10dlu,right:pref, 4dlu,fill:80dlu,4dlu,left:default:grow",
                "default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new
                                                                              // FormDebugPanel());
        builder.setLabelAttributes("r,c");
        builder.setComponentAttributes("f,c");

        speseIncassoLabel = builder.addLabel("areaRate.speseIncasso", 1);
        speseIncassoComponent = builder.addProperty("areaRate.speseIncasso", 3);
        // riga 1

        ListBinding bindingRiepilogoIva = (ListBinding) bf.createBoundList("riepilogoIva");
        bindingRiepilogoIva.setRenderer(new RigaIvaRenderer());
        bindingRiepilogoIva.getControl().setOpaque(false);
        builder.setComponentAttributes("l,t");
        ListBinding bindingAgenti = (ListBinding) bf.createBoundList("agenti");
        bindingAgenti.setRenderer(new AgentiRenderer());
        bindingAgenti.getControl().setOpaque(false);
        panelRiepilogoIva = creaHeaderRiepilogoIva(bindingRiepilogoIva, bindingAgenti);
        builder.addComponent(panelRiepilogoIva, 9, 1, 1, 7);
        builder.setComponentAttributes("f,c");
        builder.nextRow();
        // riga 2
        builder.addLabel("areaMagazzino.speseTrasporto", 1);

        builder.addBinding(
                bf.createBoundImportoTextField("areaMagazzino.speseTrasporto", "areaMagazzino.documento.totale"), 3);
        builder.nextRow();

        // riga 3
        builder.addLabel("areaMagazzino.altreSpese");
        builder.addBinding(bf.createBoundImportoTextField("areaMagazzino.altreSpese", "areaMagazzino.documento.totale"),
                3);
        percentualeScontoIncassoLabel = builder.addLabel("areaRate.percentualeSconto", 5);
        percentualeScontoIncassoComponent = builder
                .addBinding(bf.createBoundPercentageText("areaRate.percentualeSconto"), 7);
        builder.nextRow();

        // riga 4
        builder.addLabel("areaMagazzino.totaleMerce");
        builder.addBinding(
                bf.createBoundImportoTextField("areaMagazzino.totaleMerce", "areaMagazzino.documento.totale"), 3);
        giornoLimiteComponent = builder.addPropertyAndLabel("areaRate.giorniLimite", 5);
        builder.nextRow();

        // riga 5
        builder.addLabel("areaMagazzino.documento.totale", 1);
        ImportoBinding totaleDocumentoBinding = (ImportoBinding) bf.createBoundImportoTassoCalcolatoTextField(
                "areaMagazzino.documento.totale", "areaMagazzino.documento.dataDocumento");
        builder.addBinding(totaleDocumentoBinding, 3);

        builder.addLabel("areaMagazzino.documento.imposta", 5);
        ImportoBinding impostaDocumentoBinding = (ImportoBinding) bf.createBoundImportoTassoCalcolatoTextField(
                "areaMagazzino.documento.imposta", "areaMagazzino.documento.dataDocumento");
        builder.addBinding(impostaDocumentoBinding, 7);
        builder.nextRow();

        // fine righe

        installPropertyChange();

        areaPartitaVisiblePropertyChange = new AreaPartitaVisiblePropertyChange();
        getFormModel().getFieldMetadata("areaRate").addPropertyChangeListener(FormModel.ENABLED_PROPERTY,
                areaPartitaVisiblePropertyChange);

        importoSpeseIncassoVisiblePropertyChange = new ImportoSpeseIncassoVisiblePropertyChange();
        getFormModel().getFieldMetadata("areaRate.speseIncasso")
                .addPropertyChangeListener(importoSpeseIncassoVisiblePropertyChange);
        getFormModel().getValueModel("areaMagazzino.documento.sedeEntita")
                .addValueChangeListener(importoSpeseIncassoVisiblePropertyChange);

        return builder.getPanel();
    }

    @Override
    public void dispose() {
        getFormModel().removePropertyChangeListener(FormModel.DIRTY_PROPERTY, dirtyPropertyChangeListener);
        getFormModel().getFieldMetadata("areaRate.speseIncasso")
                .removePropertyChangeListener(importoSpeseIncassoVisiblePropertyChange);
        getFormModel().getFieldMetadata("areaRate").removePropertyChangeListener(FormModel.ENABLED_PROPERTY,
                areaPartitaVisiblePropertyChange);

        super.dispose();
    }

    /**
     * Installa i propertu change per i readonly.
     */
    private void installPropertyChange() {
        dirtyPropertyChangeListener = new DirtyPropertyChangeListener();
        getFormModel().addPropertyChangeListener(FormModel.DIRTY_PROPERTY, dirtyPropertyChangeListener);
        addFormObjectChangeListener(new FormObjectPropertyChange());
    }
}
