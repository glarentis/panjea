package it.eurotn.panjea.contabilita.rich.editors.areeiva;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.rich.bd.ContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.iva.util.parametriricerca.ParametriRicercaRigheIva;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author fattazzo
 *
 */
public class ParametriRicercaAreeIvaForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "parametriRicercaAreeIvaForm";

    private IContabilitaAnagraficaBD contabilitaAnagraficaBD;
    private AziendaCorrente aziendaCorrente;

    /**
     * Costruttore.
     */
    public ParametriRicercaAreeIvaForm() {
        super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaRigheIva(), false, FORM_ID), FORM_ID);
        this.contabilitaAnagraficaBD = RcpSupport.getBean(ContabilitaAnagraficaBD.BEAN_ID);
        this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,60dlu,10dlu,right:pref,4dlu,left:120dlu,10dlu,fill:default:grow",
                "2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default, fill:default:grow");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.nextRow();
        builder.setRow(2);

        ((JTextField) builder.addPropertyAndLabel("annoCompetenza")[1]).setColumns(4);

        builder.nextRow();
        builder.addPropertyAndLabel("dataRegistrazione", 1, 4, 5, 1);

        builder.nextRow();
        builder.addPropertyAndLabel("dataDocumento", 1, 6, 5, 1);

        builder.nextRow();
        builder.addLabel("entita", 1, 8);
        JComponent components = builder.addBinding(
                bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" }), 3, 8, 5, 1);
        ((SearchPanel) components).getTextFields().get("codice").setColumns(5);
        ((SearchPanel) components).getTextFields().get("anagrafica.denominazione").setColumns(23);

        builder.nextRow();
        builder.addLabel("codiceIva");
        builder.addBinding(bf.createBoundSearchText("codiceIva", new String[] { "codice" }), 3);

        builder.nextRow();
        builder.addLabel("registroIva");
        components = builder.addBinding(
                bf.createBoundSearchText("registroIva", new String[] { "numero", "descrizione" }), 3, 12, 5, 1);
        ((SearchPanel) components).getTextFields().get("numero").setColumns(5);
        ((SearchPanel) components).getTextFields().get("descrizione").setColumns(23);

        builder.setComponentAttributes("f, f");
        List<TipoAreaContabile> tipiAreeContabile = getTipiAreaContabile();
        List<TipoDocumento> tipiDocumentoSet = new ArrayList<TipoDocumento>();
        for (TipoAreaContabile tipoAreaContabile : tipiAreeContabile) {
            tipiDocumentoSet.add(tipoAreaContabile.getTipoDocumento());
        }

        builder.addBinding(bf.createBoundCheckBoxTree("tipiDocumento",
                new String[] { "abilitato", "classeTipoDocumento" }, new ValueHolder(tipiDocumentoSet)), 9, 2, 1, 12);

        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        logger.debug("--> Enter createNewObject");
        ParametriRicercaRigheIva parametriRicercaRigheIva = new ParametriRicercaRigheIva();
        parametriRicercaRigheIva.getDataDocumento().setTipoPeriodo(TipoPeriodo.MESE_CORRENTE);
        parametriRicercaRigheIva.setAnnoCompetenza(aziendaCorrente.getAnnoMagazzino());
        logger.debug("--> Exit createNewObject");
        return parametriRicercaRigheIva;
    }

    /**
     * @return List<TipoAreaContabile>
     */
    private List<TipoAreaContabile> getTipiAreaContabile() {
        return contabilitaAnagraficaBD.caricaTipiAreaContabile("tipoDocumento.codice", null, true);
    }
}
