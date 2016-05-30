/**
 *
 */
package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.codice.CodiceBinder.CodicePanel;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * Form per l'input dei parametri di ricerca di {@link AreaMagazzino}.
 *
 * @author adriano
 * @version 1.0, 30/set/2008
 */
public class ParametriRicercaAreaMagazzinoForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "parametriRicercaAreaMagazzinoForm";

    private static final String FORMMODEL_ID = "parametriRicercaAreaMagazzinoFormModel";

    private AziendaCorrente aziendaCorrente;

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    /**
     * @param parametriRicercaAreaMagazzino
     *            .
     */
    public ParametriRicercaAreaMagazzinoForm(final ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino) {
        super(PanjeaFormModelHelper.createFormModel(parametriRicercaAreaMagazzino, false, FORMMODEL_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        logger.debug("--> Enter createFormControl");
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,10dlu,fill:default:grow",
                "2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,fill:default:grow");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new
                                                                               // FormDebugPanelNumbered());
        builder.setLabelAttributes("r, c");
        builder.nextRow();
        builder.setRow(2);
        ((JTextField) builder.addPropertyAndLabel("annoCompetenza")[1]).setColumns(4);
        builder.nextRow();
        builder.addPropertyAndLabel("dataRegistrazione");
        builder.nextRow();
        builder.addPropertyAndLabel("dataDocumento");
        builder.nextRow();
        builder.setComponentAttributes("l, c");
        builder.addLabel("numeroDocumentoIniziale", 1);
        CodicePanel codicePanel = (CodicePanel) builder
                .addBinding(bf.createBoundCodice("numeroDocumentoIniziale", true, false), 3);
        codicePanel.getTextFieldCodice().setColumns(15);
        builder.nextRow();
        builder.addLabel("numeroDocumentoFinale", 1);
        codicePanel = (CodicePanel) builder.addBinding(bf.createBoundCodice("numeroDocumentoFinale", true, false), 3);
        codicePanel.getTextFieldCodice().setColumns(15);
        builder.nextRow();
        builder.addLabel("entita");
        JComponent components = builder.addBinding(
                bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" }), 3);
        ((SearchPanel) components).getTextFields().get("codice").setColumns(5);
        ((SearchPanel) components).getTextFields().get("anagrafica.denominazione").setColumns(23);

        builder.nextRow();
        builder.addLabel("utente");
        JComponent componentsUtente = builder
                .addBinding(bf.createBoundSearchText("utente", new String[] { "userName", "nome" }), 3);
        ((SearchPanel) componentsUtente).getTextFields().get("userName").setColumns(5);
        ((SearchPanel) componentsUtente).getTextFields().get("nome").setColumns(23);
        builder.nextRow();
        builder.addLabel("statiAreaMagazzino");
        Binding bindingStatoAreaMagazzino = bf.createBoundEnumCheckBoxList("statiAreaMagazzino",
                StatoAreaMagazzino.class, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
        builder.addBinding(bindingStatoAreaMagazzino, 3);
        builder.nextRow();
        builder.addLabel("tipiGenerazione");
        Binding bindingTipiGenerazione = bf.createBoundEnumCheckBoxList("tipiGenerazione", TipoGenerazione.class,
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
        builder.addBinding(bindingTipiGenerazione, 3);

        builder.setComponentAttributes("f, f");
        List<TipoAreaMagazzino> tipiAreeMagazzino = magazzinoDocumentoBD.caricaTipiAreaMagazzino("tipoDocumento.codice",
                null, true);
        builder.addBinding(bf.createBoundCheckBoxTree("tipiAreaMagazzino",
                new String[] { "tipoDocumento.abilitato", "tipoDocumento.classeTipoDocumento" },
                new ValueHolder(tipiAreeMagazzino)), 5, 1, 1, 19);
        logger.debug("--> Exit createFormControl");
        return builder.getPanel();
    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

}
