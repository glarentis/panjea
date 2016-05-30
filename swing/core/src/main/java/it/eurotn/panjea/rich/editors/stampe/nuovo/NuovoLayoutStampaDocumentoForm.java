package it.eurotn.panjea.rich.editors.stampe.nuovo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;

import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.rich.bd.ILayoutStampeBD;
import it.eurotn.panjea.rich.bd.LayoutStampeBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.eurotn.rich.report.ReportManager;

public class NuovoLayoutStampaDocumentoForm extends PanjeaAbstractForm {

    private class ClasseTipoDocPropertyChange implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            tipiAreeValueHolder.refresh();
        }
    }

    private class RefreshReportsPropertyChange implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            selectableReports.refresh();
        }
    }

    public static final String FORM_ID = "nuovoLayoutStampaForm";

    private ILayoutStampeBD layoutStampeBD;

    private RefreshableValueHolder tipiAreeValueHolder;
    private RefreshReportsPropertyChange refreshReportsPropertyChange;

    private ClasseTipoDocPropertyChange classeTipoDocPropertyChange;

    private ReportManager reportManager;

    private RefreshableValueHolder selectableReports;

    {
        layoutStampeBD = RcpSupport.getBean(LayoutStampeBD.BEAN_ID);
        reportManager = RcpSupport.getBean("reportManager");
    }

    /**
     * Costruttore.
     * 
     * @param layoutStampaPM
     *            layout
     */
    public NuovoLayoutStampaDocumentoForm(final LayoutStampaPM layoutStampaPM) {
        super(PanjeaFormModelHelper.createFormModel(layoutStampaPM, false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:pref",
                "4dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
        builder.setLabelAttributes("r, c");

        builder.addLabel("classeTipoDocumentoInstance", 1, 2);
        builder.addBinding(bf.createI18NBoundComboBox("classeTipoDocumentoInstance",
                new ValueHolder(layoutStampeBD.caricaClassiTipoDocumento()), "class.name"), 3, 2);

        tipiAreeValueHolder = new RefreshableValueHolder(new Closure() {

            @Override
            public Object call(Object argument) {
                IClasseTipoDocumento classeTipoDoc = (IClasseTipoDocumento) getFormModel()
                        .getValueModel("classeTipoDocumentoInstance").getValue();
                List<ITipoAreaDocumento> listTipiAree = new ArrayList<ITipoAreaDocumento>();
                if (classeTipoDoc != null) {
                    listTipiAree = layoutStampeBD.caricaTipoAree(classeTipoDoc.getClass().getName());
                }
                return listTipiAree;
            }
        });
        tipiAreeValueHolder.refresh();
        Binding bindingTipiAree = bf.createBoundComboBox("tipoAreaDocumento", tipiAreeValueHolder,
                "tipoDocumento.descrizione");
        builder.addLabel("tipoDocumento", 1, 4);
        builder.addBinding(bindingTipiAree, 3, 4);

        builder.addLabel("entita", 1, 6);
        JComponent components = builder.addBinding(
                bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" }), 3, 6);
        ((SearchPanel) components).getTextFields().get("codice").setColumns(5);
        ((SearchPanel) components).getTextFields().get("anagrafica.denominazione").setColumns(23);

        builder.addLabel("sedeEntita", 1, 8);
        Binding sedeEntitaBinding = bf.createBoundSearchText("sedeEntita", new String[] { "sede.descrizione" },
                new String[] { "entita" }, new String[] { "paramEntita" });
        SearchPanel searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 3, 8);
        searchPanelSede.getTextFields().get("sede.descrizione").setColumns(30);

        selectableReports = new RefreshableValueHolder(new Closure() {

            @Override
            public Object call(Object arg0) {
                Set<String> reports = new TreeSet<String>();

                ITipoAreaDocumento tipoAreaDocumento = (ITipoAreaDocumento) getFormModel()
                        .getValueModel("tipoAreaDocumento").getValue();

                if (tipoAreaDocumento != null && tipoAreaDocumento.getId() != null) {
                    // carico i report disponibili
                    reports = reportManager.listReport(tipoAreaDocumento.getReportPath());

                    EntitaLite entita = (EntitaLite) getValueModel("entita").getValue();
                    SedeEntita sedeEntita = (SedeEntita) getValueModel("sedeEntita").getValue();

                    // tolgo quelli già associati al tipo area,entità e sedeEntita
                    List<LayoutStampaDocumento> layouts = layoutStampeBD.caricaLayoutStampe(tipoAreaDocumento, entita,
                            sedeEntita);
                    for (LayoutStampaDocumento layoutStampa : layouts) {
                        boolean sediUguali = (layoutStampa.getSedeEntita() == null && sedeEntita == null)
                                || (layoutStampa.getSedeEntita() != null && sedeEntita != null);
                        boolean entitaUguali = (layoutStampa.getEntita() == null && entita == null)
                                || (layoutStampa.getEntita() != null && entita != null);
                        if (sediUguali && entitaUguali) {
                            reports.remove(layoutStampa.getReportName());
                        }
                    }
                }

                return reports;
            }
        });
        selectableReports.refresh();
        builder.addLabel("reportName", 1, 10);
        builder.addBinding(bf.createBoundComboBox("reportName", selectableReports), 3, 10);

        getFormModel().getValueModel("classeTipoDocumentoInstance")
                .addValueChangeListener(getClasseTipoDocPropertyChange());

        getFormModel().getValueModel("tipoAreaDocumento").addValueChangeListener(getRefreshReportsPropertyChange());
        getFormModel().getValueModel("entita").addValueChangeListener(getRefreshReportsPropertyChange());
        getFormModel().getValueModel("sedeEntita").addValueChangeListener(getRefreshReportsPropertyChange());

        getFormModel().getFieldMetadata("entita").setReadOnly(((LayoutStampaPM) getFormObject()).isBloccaEntita());

        return builder.getPanel();
    }

    @Override
    public void dispose() {
        getFormModel().getValueModel("classeTipoDocumentoInstance")
                .removeValueChangeListener(getClasseTipoDocPropertyChange());

        getFormModel().getValueModel("tipoAreaDocumento").removeValueChangeListener(getRefreshReportsPropertyChange());
        getFormModel().getValueModel("entita").removeValueChangeListener(getRefreshReportsPropertyChange());
        getFormModel().getValueModel("sedeEntita").removeValueChangeListener(getRefreshReportsPropertyChange());

        super.dispose();
    }

    /**
     * @return Returns the classeTipoDocPropertyChange.
     */
    public ClasseTipoDocPropertyChange getClasseTipoDocPropertyChange() {
        if (classeTipoDocPropertyChange == null) {
            classeTipoDocPropertyChange = new ClasseTipoDocPropertyChange();
        }

        return classeTipoDocPropertyChange;
    }

    /**
     * @return the refreshReportsPropertyChange
     */
    private RefreshReportsPropertyChange getRefreshReportsPropertyChange() {
        if (refreshReportsPropertyChange == null) {
            refreshReportsPropertyChange = new RefreshReportsPropertyChange();
        }

        return refreshReportsPropertyChange;
    }
}
