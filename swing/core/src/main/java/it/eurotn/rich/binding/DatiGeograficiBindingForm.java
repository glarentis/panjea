package it.eurotn.rich.binding;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.datigeografici.Lvl1PropertyChangeListener;
import it.eurotn.rich.binding.datigeografici.Lvl2PropertyChangeListener;
import it.eurotn.rich.binding.datigeografici.Lvl3PropertyChangeListener;
import it.eurotn.rich.binding.datigeografici.Lvl4PropertyChangeListener;
import it.eurotn.rich.binding.datigeografici.NazionePropertyChangeListener;
import it.eurotn.rich.binding.datigeografici.SuddivisioniAmministrativeControlController;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class DatiGeograficiBindingForm extends PanjeaAbstractForm {

    public class CapPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (!getFormModel().isEnabled()) {
                return;
            }
            Cap cap = (Cap) evt.getNewValue();
            // se l'oggetto presente è uguale a quello nuovo non faccio nulla
            Cap capPresente = (Cap) evt.getOldValue();
            if (cap != null && cap.getId() != null && capPresente != null && capPresente.getId() != null
                    && capPresente.getId().equals(cap.getId())) {
                return;
            }

            if (cap != null && cap.getId() != null) {
                Nazione nazione = cap.getNazione();
                LivelloAmministrativo1 livelloAmministrativo1cap = cap.getLivelloAmministrativo1();
                LivelloAmministrativo2 livelloAmministrativo2cap = cap.getLivelloAmministrativo2();
                LivelloAmministrativo3 livelloAmministrativo3cap = cap.getLivelloAmministrativo3();
                LivelloAmministrativo4 livelloAmministrativo4cap = cap.getLivelloAmministrativo4();
                Set<Localita> localita = cap.getLocalita();

                Nazione nazionePresente = (Nazione) getValueModel(formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH)
                        .getValue();
                if (nazionePresente == null || (nazionePresente != null && !nazionePresente.equals(nazione))) {
                    getValueModel(formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH).setValueSilently(nazione,
                            getNazionePropertyChangeListener());
                    lvlsController.updateLivelliVisibility(nazione);
                }

                if (localita.size() == 1) {
                    getValueModel(formPropertyPath + "." + LOCALITA_FORMPROPERTYPATH)
                            .setValueSilently(localita.iterator().next(), getLocalitaPropertyChangeListener());
                }

                getValueModel(formPropertyPath + "." + LVL1_FORMPROPERTYPATH)
                        .setValueSilently(livelloAmministrativo1cap, getLvl1PropertyChangeListener());
                getValueModel(formPropertyPath + "." + LVL2_FORMPROPERTYPATH)
                        .setValueSilently(livelloAmministrativo2cap, getLvl2PropertyChangeListener());
                getValueModel(formPropertyPath + "." + LVL3_FORMPROPERTYPATH)
                        .setValueSilently(livelloAmministrativo3cap, getLvl3PropertyChangeListener());
                getValueModel(formPropertyPath + "." + LVL4_FORMPROPERTYPATH)
                        .setValueSilently(livelloAmministrativo4cap, getLvl4PropertyChangeListener());
            }
        }

    }

    public class LocalitaPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (!getFormModel().isEnabled()) {
                return;
            }
            Localita localita = (Localita) evt.getNewValue();
            // se l'oggetto presente è uguale a quello nuovo non faccio nulla
            Localita localitaPresente = (Localita) evt.getOldValue();
            if (localita != null && localita.getId() != null && localitaPresente != null
                    && localitaPresente.getId() != null && localitaPresente.getId().equals(localita.getId())) {
                return;
            }

            if (localita != null && localita.getId() != null) {
                Nazione nazione = localita.getNazione();
                LivelloAmministrativo1 livelloAmministrativo1localita = localita.getLivelloAmministrativo1();
                LivelloAmministrativo2 livelloAmministrativo2localita = localita.getLivelloAmministrativo2();
                LivelloAmministrativo3 livelloAmministrativo3localita = localita.getLivelloAmministrativo3();
                LivelloAmministrativo4 livelloAmministrativo4localita = localita.getLivelloAmministrativo4();

                Set<Cap> caps = localita.getCap();

                Nazione nazionePresente = (Nazione) getValueModel(formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH)
                        .getValue();
                if (nazionePresente == null || (nazionePresente != null && !nazionePresente.equals(nazione))) {
                    getValueModel(formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH).setValueSilently(nazione,
                            getNazionePropertyChangeListener());
                    lvlsController.updateLivelliVisibility(nazione);
                }

                if (caps.size() == 1) {
                    getValueModel(formPropertyPath + "." + CAP_FORMPROPERTYPATH)
                            .setValueSilently(caps.iterator().next(), getCapPropertyChangeListener());
                }

                getValueModel(formPropertyPath + "." + LVL1_FORMPROPERTYPATH)
                        .setValueSilently(livelloAmministrativo1localita, getLvl1PropertyChangeListener());
                getValueModel(formPropertyPath + "." + LVL2_FORMPROPERTYPATH)
                        .setValueSilently(livelloAmministrativo2localita, getLvl2PropertyChangeListener());
                getValueModel(formPropertyPath + "." + LVL3_FORMPROPERTYPATH)
                        .setValueSilently(livelloAmministrativo3localita, getLvl3PropertyChangeListener());
                getValueModel(formPropertyPath + "." + LVL4_FORMPROPERTYPATH)
                        .setValueSilently(livelloAmministrativo4localita, getLvl4PropertyChangeListener());
            }
        }
    }

    private class ObjectChangedListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            lvlsController.updateLivelliVisibility(
                    (Nazione) getValueModel(formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH).getValue());
        }
    }

    /**
     * Property change che aspetta un booleano per attivare o disattivare i listeners associati al form model dei dati
     * geografici.
     *
     * @author leonardo
     */
    private class ReadOnlyPropertyChange implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Boolean b = (Boolean) evt.getNewValue();
            if (b) {
                deactivateChangeListeners();
            } else {
                activateChangeListeners();
            }
        }
    }

    public static final String NAZIONE_FORMPROPERTYPATH = "nazione";
    public static final String LVL1_FORMPROPERTYPATH = "livelloAmministrativo1";
    public static final String LVL2_FORMPROPERTYPATH = "livelloAmministrativo2";
    public static final String LVL3_FORMPROPERTYPATH = "livelloAmministrativo3";
    public static final String LVL4_FORMPROPERTYPATH = "livelloAmministrativo4";
    public static final String CAP_FORMPROPERTYPATH = "cap";
    public static final String LOCALITA_FORMPROPERTYPATH = "localita";

    private String formPropertyPath = null;
    private String firstColumnDef = null;

    private SearchPanel nazioneSearchPanel = null;
    private SearchPanel capSearchPanel = null;
    private SearchPanel localitaSearchPanel = null;

    private NazionePropertyChangeListener nazionePropertyChangeListener = null;
    private Lvl1PropertyChangeListener lvl1PropertyChangeListener = null;
    private Lvl2PropertyChangeListener lvl2PropertyChangeListener = null;
    private Lvl3PropertyChangeListener lvl3PropertyChangeListener = null;
    private Lvl4PropertyChangeListener lvl4PropertyChangeListener = null;
    private LocalitaPropertyChangeListener localitaPropertyChangeListener = null;
    private CapPropertyChangeListener capPropertyChangeListener = null;
    private SuddivisioniAmministrativeControlController lvlsController;

    private boolean showNazioneControls = true;

    /**
     * Costruttore.
     *
     * @param formModel
     *            formModel
     * @param formPropertyPath
     *            formPropertyPath
     * @param firstColumnDef
     *            la definizione della prima colonna ad es.: 70dlu
     * @param showNazioneControls
     *            visualizza o nasconde la nazione
     */
    public DatiGeograficiBindingForm(final FormModel formModel, final String formPropertyPath,
            final String firstColumnDef, final boolean showNazioneControls) {
        super(formModel);
        this.formPropertyPath = formPropertyPath;
        this.firstColumnDef = firstColumnDef;
        this.showNazioneControls = showNazioneControls;
        formModel.addPropertyChangeListener(FormModel.READONLY_PROPERTY, new ReadOnlyPropertyChange());
    }

    /**
     * Aggiunge al form model i listeners sulle proprietà del dato geografico in modo da collegare i dati tra loro.
     */
    public void activateChangeListeners() {
        getValueModel(formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH)
                .addValueChangeListener(getNazionePropertyChangeListener());
        getValueModel(formPropertyPath + "." + LOCALITA_FORMPROPERTYPATH)
                .addValueChangeListener(getLocalitaPropertyChangeListener());
        getValueModel(formPropertyPath + "." + CAP_FORMPROPERTYPATH)
                .addValueChangeListener(getCapPropertyChangeListener());
        getValueModel(formPropertyPath + "." + LVL1_FORMPROPERTYPATH)
                .addValueChangeListener(getLvl1PropertyChangeListener());
        getValueModel(formPropertyPath + "." + LVL2_FORMPROPERTYPATH)
                .addValueChangeListener(getLvl2PropertyChangeListener());
        getValueModel(formPropertyPath + "." + LVL3_FORMPROPERTYPATH)
                .addValueChangeListener(getLvl3PropertyChangeListener());
        getValueModel(formPropertyPath + "." + LVL4_FORMPROPERTYPATH)
                .addValueChangeListener(getLvl4PropertyChangeListener());
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                firstColumnDef
                        + ",4dlu,left:default, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default",
                "2dlu,default,2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
        builder.setLabelAttributes("r, c");
        builder.nextRow();
        builder.setRow(2);

        lvlsController = new SuddivisioniAmministrativeControlController();

        Binding localitaBinding = bf.createBoundSearchText(formPropertyPath + "." + LOCALITA_FORMPROPERTYPATH,
                new String[] { "descrizione" },
                new String[] { formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH,
                        formPropertyPath + "." + LVL1_FORMPROPERTYPATH, formPropertyPath + "." + LVL2_FORMPROPERTYPATH,
                        formPropertyPath + "." + LVL3_FORMPROPERTYPATH, formPropertyPath + "." + LVL4_FORMPROPERTYPATH,
                        formPropertyPath + "." + CAP_FORMPROPERTYPATH },
                new String[] { Nazione.class.getName(), LivelloAmministrativo1.class.getName(),
                        LivelloAmministrativo2.class.getName(), LivelloAmministrativo3.class.getName(),
                        LivelloAmministrativo4.class.getName(), Cap.class.getName() });

        Binding capBinding = bf.createBoundSearchText(formPropertyPath + "." + CAP_FORMPROPERTYPATH,
                new String[] { "descrizione" },
                new String[] { formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH,
                        formPropertyPath + "." + LVL1_FORMPROPERTYPATH, formPropertyPath + "." + LVL2_FORMPROPERTYPATH,
                        formPropertyPath + "." + LVL3_FORMPROPERTYPATH, formPropertyPath + "." + LVL4_FORMPROPERTYPATH,
                        formPropertyPath + "." + LOCALITA_FORMPROPERTYPATH },
                new String[] { Nazione.class.getName(), LivelloAmministrativo1.class.getName(),
                        LivelloAmministrativo2.class.getName(), LivelloAmministrativo3.class.getName(),
                        LivelloAmministrativo4.class.getName(), Localita.class.getName() });

        Binding nazioneBinding = bf.createBoundSearchText(formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH,
                new String[] { "codice" });

        Binding lvl1Binding = bf.createBoundSearchText(formPropertyPath + "." + LVL1_FORMPROPERTYPATH,
                new String[] { "nome" }, new String[] { formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH },
                new String[] { Nazione.class.getName() });

        Binding lvl2Binding = bf.createBoundSearchText(formPropertyPath + "." + LVL2_FORMPROPERTYPATH,
                new String[] { "nome" },
                new String[] { formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH,
                        formPropertyPath + "." + LVL1_FORMPROPERTYPATH },
                new String[] { Nazione.class.getName(), LivelloAmministrativo1.class.getName() });

        Binding lvl3Binding = bf.createBoundSearchText(formPropertyPath + "." + LVL3_FORMPROPERTYPATH,
                new String[] { "nome" },
                new String[] { formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH,
                        formPropertyPath + "." + LVL1_FORMPROPERTYPATH,
                        formPropertyPath + "." + LVL2_FORMPROPERTYPATH },
                new String[] { Nazione.class.getName(), LivelloAmministrativo1.class.getName(),
                        LivelloAmministrativo2.class.getName() });

        Binding lvl4Binding = bf.createBoundSearchText(formPropertyPath + "." + LVL4_FORMPROPERTYPATH,
                new String[] { "nome" },
                new String[] { formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH,
                        formPropertyPath + "." + LVL1_FORMPROPERTYPATH, formPropertyPath + "." + LVL2_FORMPROPERTYPATH,
                        formPropertyPath + "." + LVL3_FORMPROPERTYPATH },
                new String[] { Nazione.class.getName(), LivelloAmministrativo1.class.getName(),
                        LivelloAmministrativo2.class.getName(), LivelloAmministrativo3.class.getName() });

        int nazioneColumns = 0;
        if (showNazioneControls) {
            nazioneColumns = 4;
            builder.addLabel(formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH, 1);
            nazioneSearchPanel = (SearchPanel) builder.addBinding(nazioneBinding, 3);
            nazioneSearchPanel.getTextFields().get("codice").setColumns(3);
        }

        builder.addLabel(formPropertyPath + "." + LOCALITA_FORMPROPERTYPATH, 1 + nazioneColumns);
        localitaSearchPanel = (SearchPanel) builder.addBinding(localitaBinding, 3 + nazioneColumns);
        localitaSearchPanel.getTextFields().get("descrizione").setColumns(15);

        builder.addLabel(formPropertyPath + "." + CAP_FORMPROPERTYPATH, 5 + nazioneColumns);
        capSearchPanel = (SearchPanel) builder.addBinding(capBinding, 7 + nazioneColumns);
        capSearchPanel.getTextFields().get("descrizione").setColumns(9);

        builder.nextRow();

        JLabel lvl1Label = getComponentFactory().createLabel("");
        lvlsController.setLvl1Label(lvl1Label);
        JLabel lvl2Label = getComponentFactory().createLabel("");
        lvlsController.setLvl2Label(lvl2Label);
        JLabel lvl3Label = getComponentFactory().createLabel("");
        lvlsController.setLvl3Label(lvl3Label);
        JLabel lvl4Label = getComponentFactory().createLabel("");
        lvlsController.setLvl4Label(lvl4Label);

        builder.addComponent(lvl1Label, 1);
        SearchPanel lvl1SearchPanel = (SearchPanel) builder.addBinding(lvl1Binding, 3);
        lvl1SearchPanel.getTextFields().get("nome").setColumns(10);
        lvlsController.setLvl1SearchPanel(lvl1SearchPanel);

        builder.addComponent(lvl2Label, 5);
        SearchPanel lvl2SearchPanel = (SearchPanel) builder.addBinding(lvl2Binding, 7);
        lvl2SearchPanel.getTextFields().get("nome").setColumns(10);
        lvlsController.setLvl2SearchPanel(lvl2SearchPanel);

        builder.addComponent(lvl3Label, 9);
        SearchPanel lvl3SearchPanel = (SearchPanel) builder.addBinding(lvl3Binding, 11);
        lvl3SearchPanel.getTextFields().get("nome").setColumns(10);
        lvlsController.setLvl3SearchPanel(lvl3SearchPanel);

        builder.addComponent(lvl4Label, 13);
        SearchPanel lvl4SearchPanel = (SearchPanel) builder.addBinding(lvl4Binding, 15);
        lvl4SearchPanel.getTextFields().get("nome").setColumns(10);
        lvlsController.setLvl4SearchPanel(lvl4SearchPanel);

        initPropertyChangeReferences();
        getFormModel().getFormObjectHolder().addValueChangeListener(new ObjectChangedListener());

        return builder.getPanel();
    }

    /**
     * Rimuove dal form model i listeners sulle proprietà del dato geografico in modo da scollegare i dati tra loro.
     */
    public void deactivateChangeListeners() {
        getValueModel(formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH)
                .removeValueChangeListener(getNazionePropertyChangeListener());
        getValueModel(formPropertyPath + "." + LOCALITA_FORMPROPERTYPATH)
                .removeValueChangeListener(getLocalitaPropertyChangeListener());
        getValueModel(formPropertyPath + "." + CAP_FORMPROPERTYPATH)
                .removeValueChangeListener(getCapPropertyChangeListener());
        getValueModel(formPropertyPath + "." + LVL1_FORMPROPERTYPATH)
                .removeValueChangeListener(getLvl1PropertyChangeListener());
        getValueModel(formPropertyPath + "." + LVL2_FORMPROPERTYPATH)
                .removeValueChangeListener(getLvl2PropertyChangeListener());
        getValueModel(formPropertyPath + "." + LVL3_FORMPROPERTYPATH)
                .removeValueChangeListener(getLvl3PropertyChangeListener());
        getValueModel(formPropertyPath + "." + LVL4_FORMPROPERTYPATH)
                .removeValueChangeListener(getLvl4PropertyChangeListener());
    }

    /**
     * @return CapPropertyChangeListener
     */
    private CapPropertyChangeListener getCapPropertyChangeListener() {
        if (capPropertyChangeListener == null) {
            capPropertyChangeListener = new CapPropertyChangeListener();
        }
        return capPropertyChangeListener;
    }

    /**
     * @return LocalitaPropertyChangeListener
     */
    private LocalitaPropertyChangeListener getLocalitaPropertyChangeListener() {
        if (localitaPropertyChangeListener == null) {
            localitaPropertyChangeListener = new LocalitaPropertyChangeListener();
        }
        return localitaPropertyChangeListener;
    }

    /**
     * @return Lvl1PropertyChangeListener
     */
    private Lvl1PropertyChangeListener getLvl1PropertyChangeListener() {
        if (lvl1PropertyChangeListener == null) {
            lvl1PropertyChangeListener = new Lvl1PropertyChangeListener(formPropertyPath, getFormModel());
        }
        return lvl1PropertyChangeListener;
    }

    /**
     * @return Lvl2PropertyChangeListener
     */
    private Lvl2PropertyChangeListener getLvl2PropertyChangeListener() {
        if (lvl2PropertyChangeListener == null) {
            lvl2PropertyChangeListener = new Lvl2PropertyChangeListener(formPropertyPath, getFormModel());
        }
        return lvl2PropertyChangeListener;
    }

    /**
     * @return Lvl3PropertyChangeListener
     */
    private Lvl3PropertyChangeListener getLvl3PropertyChangeListener() {
        if (lvl3PropertyChangeListener == null) {
            lvl3PropertyChangeListener = new Lvl3PropertyChangeListener(formPropertyPath, getFormModel());
        }
        return lvl3PropertyChangeListener;
    }

    /**
     * @return Lvl1PropertyChangeListener
     */
    private Lvl4PropertyChangeListener getLvl4PropertyChangeListener() {
        if (lvl4PropertyChangeListener == null) {
            lvl4PropertyChangeListener = new Lvl4PropertyChangeListener(formPropertyPath, getFormModel());
        }
        return lvl4PropertyChangeListener;
    }

    /**
     * @return NazionePropertyChangeListener
     */
    private NazionePropertyChangeListener getNazionePropertyChangeListener() {
        if (nazionePropertyChangeListener == null) {
            nazionePropertyChangeListener = new NazionePropertyChangeListener(formPropertyPath, getFormModel());
        }
        return nazionePropertyChangeListener;
    }

    /**
     * Inizializza i property change e i loro reciproci riferimenti.
     */
    private void initPropertyChangeReferences() {
        Map<String, PropertyChangeListener> listeners = new HashMap<String, PropertyChangeListener>();
        listeners.put(formPropertyPath + "." + NAZIONE_FORMPROPERTYPATH, getNazionePropertyChangeListener());
        listeners.put(formPropertyPath + "." + LVL1_FORMPROPERTYPATH, getLvl1PropertyChangeListener());
        listeners.put(formPropertyPath + "." + LVL2_FORMPROPERTYPATH, getLvl2PropertyChangeListener());
        listeners.put(formPropertyPath + "." + LVL3_FORMPROPERTYPATH, getLvl3PropertyChangeListener());
        listeners.put(formPropertyPath + "." + LVL4_FORMPROPERTYPATH, getLvl4PropertyChangeListener());
        listeners.put(formPropertyPath + "." + CAP_FORMPROPERTYPATH, getCapPropertyChangeListener());
        listeners.put(formPropertyPath + "." + LOCALITA_FORMPROPERTYPATH, getLocalitaPropertyChangeListener());

        getNazionePropertyChangeListener().setListeners(listeners);
        getLvl1PropertyChangeListener().setListeners(listeners);
        getLvl2PropertyChangeListener().setListeners(listeners);
        getLvl3PropertyChangeListener().setListeners(listeners);
        getLvl4PropertyChangeListener().setListeners(listeners);

        getNazionePropertyChangeListener().setSuddivisioniAmministrativeControlController(lvlsController);
        getLvl1PropertyChangeListener().setSuddivisioniAmministrativeControlController(lvlsController);
        getLvl2PropertyChangeListener().setSuddivisioniAmministrativeControlController(lvlsController);
        getLvl3PropertyChangeListener().setSuddivisioniAmministrativeControlController(lvlsController);
        getLvl4PropertyChangeListener().setSuddivisioniAmministrativeControlController(lvlsController);
    }

}
