package it.eurotn.panjea.fatturepa.rich.editors.ricerca;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.fatturepa.util.ParametriRicercaFatturePA;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.codice.CodiceBinder.CodicePanel;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class ParametriRicercaFatturePAForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "parametriRicercaFatturePAForm";

    private AziendaCorrente aziendaCorrente;

    /**
     * Costruttore.
     */
    public ParametriRicercaFatturePAForm() {
        super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaFatturePA(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        logger.debug("--> Enter createFormControl");
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:pref,4dlu,right:pref,4dlu,left:pref,10dlu,fill:default:grow",
                "2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,fill:default:grow");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.nextRow();
        builder.setRow(2);
        ((JTextField) builder.addPropertyAndLabel("annoCompetenza")[1]).setColumns(4);
        builder.nextRow();
        builder.addPropertyAndLabel("dataRegistrazione", 1, 4, 5);
        builder.nextRow();
        builder.addPropertyAndLabel("dataDocumento", 1, 6, 5);
        builder.nextRow();
        builder.setComponentAttributes("l, c");
        builder.addLabel("numeroDocumentoIniziale", 1);
        CodicePanel codicePanel = (CodicePanel) builder
                .addBinding(bf.createBoundCodice("numeroDocumentoIniziale", true, false), 3);
        codicePanel.getTextFieldCodice().setColumns(15);
        builder.addLabel("numeroDocumentoFinale", 5);
        codicePanel = (CodicePanel) builder.addBinding(bf.createBoundCodice("numeroDocumentoFinale", true, false), 7);
        codicePanel.getTextFieldCodice().setColumns(15);
        builder.nextRow();
        ((JTextField) builder.addPropertyAndLabel("progressivoInvio")[1]).setColumns(4);
        builder.nextRow();
        builder.addLabel("entita");
        builder.setComponentAttributes("f, c");
        JComponent components = builder.addBinding(
                bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" }), 3, 12, 5, 1);
        ((SearchPanel) components).getTextFields().get("codice").setColumns(5);
        ((SearchPanel) components).getTextFields().get("anagrafica.denominazione").setColumns(23);
        builder.nextRow();

        builder.addLabel("statiAreaMagazzino");
        Binding bindingStatoAreaMagazzino = bf.createBoundEnumCheckBoxList("statiAreaMagazzino",
                StatoAreaMagazzino.class, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
        builder.addBinding(bindingStatoAreaMagazzino, 3, 14, 5, 1);
        builder.nextRow();

        builder.setLabelAttributes("l, c");
        builder.addLabel("statoFatturaPA", 9, 2);
        builder.addBinding(bf.createBoundEnumCheckBoxList("statiFatturaPA", StatoFatturaPA.class,
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.VERTICAL_WRAP), 9, 6, 1, 6);

        logger.debug("--> Exit createFormControl");
        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        logger.debug("--> Enter createNewObject");
        ParametriRicercaFatturePA parametriRicercaFatturePA = new ParametriRicercaFatturePA();
        parametriRicercaFatturePA.getDataDocumento().setTipoPeriodo(TipoPeriodo.MESE_CORRENTE);
        parametriRicercaFatturePA.setAnnoCompetenza(aziendaCorrente.getAnnoMagazzino());
        logger.debug("--> Exit createNewObject");
        return parametriRicercaFatturePA;
    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

}
