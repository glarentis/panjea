package it.eurotn.panjea.magazzino.rich.editors.settings;

import java.awt.Dimension;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.magazzino.domain.AddebitoDichiarazioneIntentoSettings;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.OrdinamentoFatturazione;
import it.eurotn.panjea.magazzino.domain.SogliaAddebitoDichiarazioneSettings;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class MagazzinoSettingsForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "magazzinoSettingsForm";

    /**
     * costruttore.
     */
    public MagazzinoSettingsForm() {
        super(PanjeaFormModelHelper.createFormModel(new MagazzinoSettings(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,fill:60dlu,10dlu,right:pref,4dlu,right:pref,4dlu,fill:pref,left:pref:grow",
                "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout); // , new FormDebugPanelNumbered());
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addHorizontalSeparator("Gestione magazzino", 9);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("annoCompetenza", 1)[1]).setColumns(5);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("codiceGS1", 1)[1]).setColumns(10);
        builder.addLabel("numeratoreBarCode", 7);
        builder.addBinding(bf.createBoundSearchText("numeratoreBarCode", null, Protocollo.class), 9);
        builder.nextRow();

        builder.addPropertyAndLabel("calcolaGiacenzeInCreazioneRiga", 1);
        builder.addPropertyAndLabel("aggiornaAutomaticamenteListiniCollegati", 7);
        builder.nextRow();

        builder.addLabel("descrizioneNotaAutomaticaOmaggio");
        builder.addLabel("descrizioneNotaAutomaticaSomministrazione", 7);
        builder.nextRow();

        builder.addBinding(bf.createBoundHTMLEditor("descrizioneNotaAutomaticaOmaggio"), 1, 3, 1);
        builder.addBinding(bf.createBoundHTMLEditor("descrizioneNotaAutomaticaSomministrazione"), 7, 3, 1);
        builder.nextRow();

        builder.addHorizontalSeparator("Fatturazione differita", 9);
        builder.nextRow();

        builder.addLabel("ordinamentoFatturazione.campiOrdinamento", 1);
        builder.addBinding(bf.createBoundShuttleList("ordinamentoFatturazione.campiOrdinamento",
                OrdinamentoFatturazione.ORDINAMENTO_DEFAULT), 3, 5, 1);
        builder.nextRow();

        builder.addPropertyAndLabel("riportaTestateOrdineInFatturazione", 1);
        builder.nextRow();

        PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
        if (pluginManager.isPresente(PluginManager.PLUGIN_LOTTI)) {
            builder.addHorizontalSeparator("Lotti", 9);
            builder.nextRow();
            builder.addPropertyAndLabel("gestioneLottiAutomatici", 1);
            builder.addPropertyAndLabel("nuovoDaUltimoLotto", 7);
            builder.nextRow();
        }

        builder.addHorizontalSeparator("Gestione dichiarazione di intento", 9);
        builder.nextRow();

        builder.setLabelAttributes("l, c");
        builder.addLabel("sogliaAddebitoDichiarazione", 1);
        builder.nextRow();
        AddebitoDichiarazioneIntentoSettingsTableModel addebitoDichiarazioneIntentoSettingsTableModel = new AddebitoDichiarazioneIntentoSettingsTableModel();
        TableEditableBinding<AddebitoDichiarazioneIntentoSettings> addebitoDichiarazioneIntentoBinding = new TableEditableBinding<AddebitoDichiarazioneIntentoSettings>(
                getFormModel(), "addebitiDichiarazioneIntento", Set.class,
                addebitoDichiarazioneIntentoSettingsTableModel);
        addebitoDichiarazioneIntentoBinding.getControl().setPreferredSize(new Dimension(300, 150));
        builder.addBinding(addebitoDichiarazioneIntentoBinding, 7, 3, 1);
        addebitoDichiarazioneIntentoBinding.getTableWidget().setEditable(false);

        SoglieAddebitoDichiarazioneIntentoSettingsTableModel soglieDITableModel = new SoglieAddebitoDichiarazioneIntentoSettingsTableModel();
        TableEditableBinding<SogliaAddebitoDichiarazioneSettings> soglieDichiarazioneIntentoBinding = new TableEditableBinding<SogliaAddebitoDichiarazioneSettings>(
                getFormModel(), "sogliaAddebitoDichiarazione", Set.class, soglieDITableModel);

        soglieDichiarazioneIntentoBinding.getControl().setPreferredSize(new Dimension(300, 150));
        builder.addBinding(soglieDichiarazioneIntentoBinding, 1, 4, 1);
        soglieDichiarazioneIntentoBinding.getTableWidget().setEditable(false);
        SortableTableModel sortableTableModel = (SortableTableModel) TableModelWrapperUtils.getActualTableModel(
                soglieDichiarazioneIntentoBinding.getTableWidget().getTable().getModel(), SortableTableModel.class);
        sortableTableModel.sortColumn(1, true, false);

        return builder.getPanel();
    }
}
