package it.eurotn.panjea.magazzino.rich.editors.rendicontazione.anagrafica;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoSpedizione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class TipoEsportazioneForm extends PanjeaAbstractForm {

    private class EntitaPropertyChange implements PropertyChangeListener {

        private IAnagraficaBD anagraficaBD;

        /**
         * Costruttore.
         */
        public EntitaPropertyChange() {
            super();
            anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            if (getFormModel().isReadOnly()) {
                return;
            }

            SedeEntita sedeEntita = null;

            EntitaLite entitaLite = (EntitaLite) evt.getNewValue();
            if (entitaLite != null && !entitaLite.isNew()) {
                sedeEntita = anagraficaBD.caricaSedePrincipaleEntita(entitaLite.creaProxyEntita());
            }

            getFormModel().getValueModel("datiSpedizione.sedeEntita").setValue(sedeEntita);
        }
    }

    private class TipoSpedizioneListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            boolean ftpVisible = (evt.getNewValue() != null)
                    && ((TipoSpedizione) evt.getNewValue()) == TipoSpedizione.FTP;
            boolean emailVisible = (evt.getNewValue() != null)
                    && ((TipoSpedizione) evt.getNewValue()) == TipoSpedizione.EMAIL;

            for (Component component : ftpComponents) {
                component.setVisible(ftpVisible);
            }
            for (Component component : emailComponents) {
                component.setVisible(emailVisible);
            }
        }
    }

    private List<Component> ftpComponents = new ArrayList<Component>();
    private List<Component> emailComponents = new ArrayList<Component>();

    private TipoSpedizioneListener tipoSpedizioneListener;
    private EntitaPropertyChange entitaPropertyChange;

    /**
     * Costruttore.
     * 
     */
    public TipoEsportazioneForm() {
        super(PanjeaFormModelHelper.createFormModel(new TipoEsportazione(), false, "tipoEsportazioneForm"));
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("left:default, 4dlu, left:default,4dlu,left:default, 4dlu, left:default",
                "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());

        builder.setLabelAttributes("r, c");

        builder.addPropertyAndLabel("nome", 1, 2);

        builder.addPropertyAndLabel("jndiName", 1, 4);

        builder.addPropertyAndLabel("template", 1, 6);

        builder.addPropertyAndLabel("codiceCliente", 1, 8);
        builder.nextRow();

        builder.addHorizontalSeparator("Definizione nome del file", 1, 3);
        builder.nextRow();

        builder.addPropertyAndLabel("richiediData", 1, 12);
        builder.addPropertyAndLabel("datiSpedizione.nomeFile", 1, 14);
        builder.addPropertyAndLabel("datiSpedizione.suffissoNomeFile", 1, 16);
        builder.addPropertyAndLabel("datiSpedizione.applicaSuffissoAlPrimoFile", 1, 18);
        builder.nextRow();

        builder.addHorizontalSeparator("Dati spedizione", 1, 3);
        builder.nextRow();

        builder.addPropertyAndLabel("tipoSpedizione", 1, 22);

        ftpComponents.addAll(Arrays.asList(builder.addPropertyAndLabel("datiSpedizione.indirizzoFTP", 1, 24)));
        ftpComponents.addAll(Arrays.asList(builder.addPropertyAndLabel("datiSpedizione.remoteDirFTP", 1, 26)));
        ftpComponents.addAll(Arrays.asList(builder.addPropertyAndLabel("datiSpedizione.userName", 1, 28)));
        ftpComponents.addAll(Arrays.asList(builder.addPropertyAndLabel("datiSpedizione.password", 1, 30)));
        for (Component component : ftpComponents) {
            component.setVisible(false);
        }

        builder.setComponentAttributes("f,f");
        emailComponents.add(builder.addLabel("entita", 1, 24));
        JComponent components = builder.addBinding(bf.createBoundSearchText("datiSpedizione.entita",
                new String[] { "codice", "anagrafica.denominazione" }), 3, 24);
        ((SearchPanel) components).getTextFields().get("codice").setColumns(4);
        Binding sedeEntitaBinding = bf.createBoundSearchText("datiSpedizione.sedeEntita",
                new String[] { "sede.descrizione" }, new String[] { "datiSpedizione.entita" },
                new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
        emailComponents.add(components);
        emailComponents.add(builder.addLabel("sedeEntita", 1, 26));
        SearchPanel searchPanelSedeEntita = (SearchPanel) builder.addBinding(sedeEntitaBinding, 3, 26);
        emailComponents.add(searchPanelSedeEntita);
        for (Component component : emailComponents) {
            component.setVisible(false);
        }

        builder.setLabelAttributes("r, t");
        builder.addLabel("tipiDocumentoCollegati", 5, 2);
        InserimentoTipoAreaMagazzinoForm inserimentoTAMForm = new InserimentoTipoAreaMagazzinoForm(this);
        builder.setComponentAttributes("l,t");
        TableBinding<?> agenteTableBinding = (TableBinding<?>) bf.createTableBinding("tipiAreeMagazzino", 150,
                new TipoAreaMagazzinoTableModel(), inserimentoTAMForm);
        builder.addBinding(agenteTableBinding, 7, 2, 1, 8);

        tipoSpedizioneListener = new TipoSpedizioneListener();
        getFormModel().getValueModel("tipoSpedizione").addValueChangeListener(tipoSpedizioneListener);

        entitaPropertyChange = new EntitaPropertyChange();
        getFormModel().getValueModel("datiSpedizione.entita").addValueChangeListener(entitaPropertyChange);

        return builder.getPanel();
    }

    @Override
    public void dispose() {
        getFormModel().getValueModel("tipoSpedizione").removeValueChangeListener(tipoSpedizioneListener);
        getFormModel().getValueModel("datiSpedizione.entita").removeValueChangeListener(entitaPropertyChange);
        super.dispose();
    }
}
