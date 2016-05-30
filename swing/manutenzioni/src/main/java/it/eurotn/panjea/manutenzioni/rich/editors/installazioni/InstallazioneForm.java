package it.eurotn.panjea.manutenzioni.rich.editors.installazioni;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.form.FormGuard;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.magazzino.rich.search.TipoAreaMagazzinoSearchObject;
import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;
import it.eurotn.panjea.manutenzioni.domain.DepositoInstallazione;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.TipoContrattoInstallazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.panjea.manutenzioni.rich.bd.ManutenzioniBD;
import it.eurotn.panjea.manutenzioni.rich.commands.OpenArticoloMICommand;
import it.eurotn.panjea.manutenzioni.rich.search.ArticoloMISearchObject;
import it.eurotn.panjea.manutenzioni.rich.search.OperatoreSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class InstallazioneForm extends PanjeaAbstractForm implements PropertyChangeListener {

    private class DistributoreClienteCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public void postExecution(ActionCommand command) {
            ArticoloMI articoloMISalvato = ((OpenArticoloMICommand) command).getArticoloMISalvato();
            if (articoloMISalvato != null) {
                getValueModel("articolo").setValue(articoloMISalvato);
            }
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            return true;
        }
    }

    private class SedeEntitaListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            if (getFormModel().isReadOnly()) {
                return;
            }

            DepositoInstallazione deposito = null;
            SedeEntita sedeEntitaNew = (SedeEntita) evt.getNewValue();
            if (sedeEntitaNew != null && !sedeEntitaNew.isNew()) {
                deposito = manutenzioniBD.caricaDeposito(sedeEntitaNew);
            }
            getFormModel().getFieldMetadata("deposito.sedeDeposito").setReadOnly(deposito != null);
            if (deposito != null) {
                getValueModel("deposito.sedeEntita").removeValueChangeListener(sedeEntitaListener);
                getValueModel("deposito").setValue(deposito);
                getValueModel("deposito.sedeEntita").addValueChangeListener(sedeEntitaListener);
            }
        }

    }

    private static final String FORM_ID = "installazioneForm";

    private IAnagraficaBD anagraficaBD;
    private IManutenzioniBD manutenzioniBD;
    private AziendaCorrente aziendaCorrente;
    private SedeAzienda sedePrincipaleAzienda;

    private OpenArticoloMICommand openArticoloMICommand = null;

    private SedeEntitaListener sedeEntitaListener = new SedeEntitaListener();

    private SedeEntita sedeEntita;

    private boolean fromDistributore;

    /**
     * Costruttore.
     */
    public InstallazioneForm() {
        super(PanjeaFormModelHelper.createFormModel(new Installazione(), false, FORM_ID), FORM_ID);
        PanjeaSwingUtil.addValueModelToForm(new EntitaLite(), getFormModel(), Entita.class, "entita", true);
        PanjeaSwingUtil.addValueModelToForm(new SedeEntita(), getFormModel(), SedeEntita.class, "sedeEntita", true);
        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "soloCaricatori", true);
        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "soloTecnici", true);
        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "soloProprietaCliente", true);
        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "soloDisponibili", true);
        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class,
                TipoAreaMagazzinoSearchObject.PARAMETRO_GESTIONE_VENDING, true);
        anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
        aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
        manutenzioniBD = RcpSupport.getBean(ManutenzioniBD.BEAN_ID);

        openArticoloMICommand = RcpSupport.getCommand("openDistributoreCommand");
        if (openArticoloMICommand != null) {
            openArticoloMICommand.addCommandInterceptor(new DistributoreClienteCommandInterceptor());
        }
        new PanjeaFormGuard(getFormModel(), openArticoloMICommand, FormGuard.ON_ENABLED); // NOSONAR
    }

    /**
     *
     * @param fromDistributore
     *            true se il form viene costruito nell'editor dell'articolo
     */
    public InstallazioneForm(final boolean fromDistributore) {
        this();
        this.fromDistributore = fromDistributore;
        getFormModel().setReadOnly(true);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:200dlu,4dlu,right:pref,4dlu,fill:200dlu",
                "1dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addLabel("areaMagazzino.documento.sedeEntita");
        Binding sedeEntitaBinding = bf.createBoundSearchText("deposito.sedeEntita", null, new String[] { "entita" },
                new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
        builder.addBinding(sedeEntitaBinding, 3);

        builder.addLabel("sedeAzienda", 5);
        builder.addBinding(
                bf.createBoundComboBox("deposito.sedeDeposito", anagraficaBD.caricaSediAzienda(), "sede.indirizzo"), 7);
        builder.nextRow();

        builder.addLabel("ubicazione");
        builder.addBinding(bf.createBoundSearchText("ubicazione", new String[] { "descrizione" }), 3);
        builder.nextRow();

        if (!fromDistributore) {
            builder.addPropertyAndLabel("codice");
            builder.addPropertyAndLabel("descrizione", 5);
            builder.nextRow();

            builder.addLabel("articolo");
            SearchPanel searchPanelArticolo = (SearchPanel) builder
                    .addBinding(bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" },
                            new String[] { "soloProprietaCliente", "soloDisponibili" },
                            new String[] { ArticoloMISearchObject.PROPRIETA_CLIENTE_PARAM_KEY,
                                    ArticoloMISearchObject.SOLO_DISPONIBILI_PARAM_KEY }),
                            3);
            searchPanelArticolo.getTextFields().get("codice").setColumns(10);
            builder.nextRow();
        }

        builder.addLabel("caricatore");
        SearchPanel searchPanelCaricatore = (SearchPanel) builder.addBinding(
                bf.createBoundSearchText("datiInstallazione.caricatore", new String[] { "codice", "nome", "cognome" },
                        new String[] { "soloCaricatori" }, new String[] { OperatoreSearchObject.CARICATORE_PARAM_KEY }),
                3);
        searchPanelCaricatore.getTextFields().get("codice").setColumns(5);

        builder.addLabel("tecnico", 5);
        SearchPanel searchPanelTecnico = (SearchPanel) builder.addBinding(
                bf.createBoundSearchText("datiInstallazione.tecnico", new String[] { "codice", "nome", "cognome" },
                        new String[] { "soloTecnici" }, new String[] { OperatoreSearchObject.TECNICO_PARAM_KEY }),
                7);
        searchPanelTecnico.getTextFields().get("codice").setColumns(5);
        builder.nextRow();

        builder.addLabel("ivaSomministrazione", 1);
        SearchPanel searchPanel = (SearchPanel) builder.addBinding(
                bf.createBoundSearchText("datiInstallazione.ivaSomministrazione", new String[] { "codice" }), 3);
        searchPanel.getTextFields().get("codice").setColumns(6);
        builder.nextRow();

        builder.addLabel("contratto");
        builder.addProperty("datiInstallazione.tipoContrattoInstallazione", 3);
        if (openArticoloMICommand != null) {
            builder.addComponent(openArticoloMICommand.createButton(), 7);
        }
        builder.nextRow();

        builder.addLabel("tipoDocumento");
        Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaMagazzino",
                new String[] { "tipoDocumento.codice", "tipoDocumento.descrizione" },
                new String[] { TipoAreaMagazzinoSearchObject.PARAMETRO_GESTIONE_VENDING },
                new String[] { TipoAreaMagazzinoSearchObject.PARAMETRO_GESTIONE_VENDING });
        SearchPanel tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDoc, 3);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(23);
        builder.nextRow();

        builder.addLabel("listino", 1);
        SearchPanel searchListino = (SearchPanel) builder
                .addBinding(bf.createBoundSearchText("listino", new String[] { "codice", "descrizione" }), 3);
        searchListino.getTextFields().get("codice").setColumns(6);
        searchListino.getTextFields().get("descrizione").setColumns(27);

        builder.addLabel("listinoAlternativo", 5);
        SearchPanel searchListinoAlternativo = (SearchPanel) builder.addBinding(
                bf.createBoundSearchText("listinoAlternativo", new String[] { "codice", "descrizione" }), 7);
        searchListinoAlternativo.getTextFields().get("codice").setColumns(6);
        searchListinoAlternativo.getTextFields().get("descrizione").setColumns(27);
        builder.nextRow();

        builder.addPropertyAndLabel("datiInstallazione.pubblico");
        builder.addPropertyAndLabel("datiInstallazione.autoAlimentato", 5);
        builder.nextRow();

        builder.addLabel("pozzetto");
        Binding bindPozzetto = bf.createBoundSearchText("pozzetto", new String[] { "codice", "descrizione" });
        SearchPanel searchPanelDepositoOrigine = (SearchPanel) builder.addBinding(bindPozzetto, 3, 2, 1);
        searchPanelDepositoOrigine.getTextFields().get("codice").setColumns(5);
        searchPanelDepositoOrigine.getTextFields().get("descrizione").setColumns(20);

        addFormObjectChangeListener(this);
        getFormModel().getValueModel("datiInstallazione.tipoContrattoInstallazione").addValueChangeListener(this);
        getFormModel().getValueModel("deposito.sedeEntita").addValueChangeListener(sedeEntitaListener);
        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        DepositoInstallazione deposito = null;
        if (sedeEntita != null && !sedeEntita.isNew()) {
            deposito = manutenzioniBD.caricaDeposito(sedeEntita);
        }
        getFormModel().getFieldMetadata("deposito.sedeDeposito").setReadOnly(deposito != null);
        if (deposito == null) {
            deposito = new DepositoInstallazione();
            deposito.setSedeDeposito(getSedePrincipaleAzienda());
            deposito.setSedeEntita(sedeEntita);
        }

        Installazione inst = new Installazione();
        inst.setDeposito(deposito);
        return inst;
    }

    @Override
    public void dispose() {
        removeFormObjectChangeListener(this);
        getFormModel().getValueModel("datiInstallazione.tipoContrattoInstallazione").removeValueChangeListener(this);
        super.dispose();
    }

    private SedeAzienda getSedePrincipaleAzienda() {
        if (sedePrincipaleAzienda == null) {
            Azienda azienda = new Azienda();
            azienda.setId(aziendaCorrente.getId());
            sedePrincipaleAzienda = anagraficaBD.caricaSedePrincipaleAzienda(azienda);
        }
        return sedePrincipaleAzienda;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (openArticoloMICommand != null) {
            ArticoloMI articoloMI = (ArticoloMI) getValueModel("articolo").getValue();
            boolean articoloPresente = articoloMI != null && !articoloMI.isNew();

            TipoContrattoInstallazione tipoContratto = (TipoContrattoInstallazione) getValueModel(
                    "datiInstallazione.tipoContrattoInstallazione").getValue();
            openArticoloMICommand.setVisible(
                    !articoloPresente && tipoContratto != null && tipoContratto == TipoContrattoInstallazione.CLIENTE);
            getFormModel().getFieldMetadata("articolo")
                    .setReadOnly(tipoContratto == null || tipoContratto != TipoContrattoInstallazione.CLIENTE);
        }
    }

    /**
     * @param entita
     *            The entita to set.
     */
    public void setEntita(Entita entita) {
        EntitaLite entitalite = new EntitaLite();
        entitalite.setId(entita.getId());
        getValueModel("entita").setValue(anagraficaBD.caricaEntitaLite(entitalite));
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);

        if (formObject != null) {
            getFormModel().getFieldMetadata("deposito.sedeDeposito").setReadOnly(!((Installazione) formObject).isNew());
        }
    }

    /**
     * @param sedeEntita
     *            The sede entita to set.
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
        if (!getFormModel().isReadOnly()) {
            getValueModel("deposito.sedeEntita").setValue(sedeEntita);
            getFormModel().getFieldMetadata("deposito.sedeEntita").setReadOnly(sedeEntita != null);
        }
    }

}