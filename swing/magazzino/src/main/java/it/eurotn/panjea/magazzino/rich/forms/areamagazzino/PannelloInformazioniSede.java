package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.swing.StyledLabel;
import com.jidesoft.swing.StyledLabelBuilder;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;

@SuppressWarnings("serial")
public class PannelloInformazioniSede extends JPanel implements PropertyChangeListener {
    private static final ConverterContext CONTEXT_SENSITIVE_CELL_RENDERER = new ConverterContext("BIGDECIMAL");
    private static final String KEY_ICON_PAGAMENTO = "it.eurotn.panjea.pagamenti.domain.CodicePagamento";
    private final JLabel labelImportoFido;
    private final JLabel labelImportoFidoUtilizzato;
    private final JLabel labelFidoUtilizzatoDettaglio;
    private final FormModel formModel;
    private final JLabel labelImportoFidoUtilizzatoCaption;
    private final StyledLabel labelImportoRimanente;
    private EntitaLite entitaLite;
    private BigDecimal rateAperte;

    {
        CONTEXT_SENSITIVE_CELL_RENDERER.setUserObject(2);
    }

    /**
     * Costruttore.
     *
     * @param formModel
     *            form model per recuperare le proprietà da visualizzare
     */
    public PannelloInformazioniSede(final FormModel formModel) {
        this.formModel = formModel;
        setVisible(false);
        PanjeaComponentFactory panjeaComponentFactory = (PanjeaComponentFactory) Application.services()
                .getService(ComponentFactory.class);

        FormLayout formlayout = new FormLayout(
                "DEFAULT:NONE,FILL:4DLU:NONE,FILL:40DLU:NONE,FILL:10DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:40DLU:NONE",
                "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
        CellConstraints cc = new CellConstraints();
        setLayout(formlayout);

        Icon pagamento = RcpSupport.getIcon(KEY_ICON_PAGAMENTO);

        JLabel labelImportoFidoCaption = panjeaComponentFactory.createLabel("FidoAccordato");
        labelImportoFidoCaption.setIcon(pagamento);
        add(labelImportoFidoCaption, cc.xy(1, 1));
        labelImportoFido = new JLabel();
        add(labelImportoFido, cc.xy(3, 1));

        labelImportoFidoUtilizzatoCaption = panjeaComponentFactory.createLabel("FidoUtilizzato");
        labelImportoFidoUtilizzatoCaption.setIcon(pagamento);
        add(labelImportoFidoUtilizzatoCaption, cc.xy(5, 1));
        labelImportoFidoUtilizzato = new JLabel();
        add(labelImportoFidoUtilizzato, cc.xy(7, 1));

        labelFidoUtilizzatoDettaglio = new JLabel();
        add(labelFidoUtilizzatoDettaglio, cc.xyw(5, 2, 3));

        JLabel labelImportoDifferenzaCaption = new StyledLabel(RcpSupport.getMessage("FidoResiduo"));
        labelImportoDifferenzaCaption.setIcon(pagamento);
        add(labelImportoDifferenzaCaption, cc.xy(1, 2));

        labelImportoRimanente = new StyledLabel();
        add(labelImportoRimanente, cc.xy(3, 2));
    }

    /**
     * Calcola il fido data una areaMagazzino; si preoccupa di visualizzare il pannello per mostrare
     * i dati.
     *
     */
    public void calcolaFido() {
        BigDecimal fido = (BigDecimal) ObjectUtils.defaultIfNull(
                formModel.getValueModel("areaMagazzino.documento.entita.fido").getValue(), BigDecimal.ZERO);
        BigDecimal documentiAperti = (BigDecimal) ObjectUtils
                .defaultIfNull(formModel.getValueModel("importoDocumentiAperti").getValue(), BigDecimal.ZERO);

        BigDecimal fidoUtilizzato = rateAperte.add(documentiAperti);

        labelImportoFidoUtilizzato.setText(
                ObjectConverterManager.toString(fidoUtilizzato, BigDecimal.class, CONTEXT_SENSITIVE_CELL_RENDERER));

        StringBuilder importoFidoUtilizzatoDettaglio = new StringBuilder(200);
        importoFidoUtilizzatoDettaglio
                .append(ObjectConverterManager.toString(rateAperte, BigDecimal.class, CONTEXT_SENSITIVE_CELL_RENDERER));
        importoFidoUtilizzatoDettaglio.append(" + ").append(
                ObjectConverterManager.toString(documentiAperti, BigDecimal.class, CONTEXT_SENSITIVE_CELL_RENDERER));
        labelFidoUtilizzatoDettaglio.setText(importoFidoUtilizzatoDettaglio.toString());
        labelFidoUtilizzatoDettaglio.setVisible(fidoUtilizzato.compareTo(BigDecimal.ZERO) != 0);

        labelImportoFido
                .setText(ObjectConverterManager.toString(fido, BigDecimal.class, CONTEXT_SENSITIVE_CELL_RENDERER));

        BigDecimal rimanente = fido.subtract(fidoUtilizzato);
        String rimanenteDescrizione = ObjectConverterManager.toString(rimanente, BigDecimal.class,
                CONTEXT_SENSITIVE_CELL_RENDERER);

        if (rimanente.compareTo(BigDecimal.ZERO) < 0) {
            rimanenteDescrizione = "{" + rimanenteDescrizione + ":f:red,b}";
        }
        StyledLabelBuilder.setStyledText(labelImportoRimanente, rimanenteDescrizione);
        if (fido.compareTo(BigDecimal.ZERO) > 0 && rimanente.compareTo(BigDecimal.ZERO) < 0) {
            showWarningDialogMessage(rimanenteDescrizione);
        }
    }

    private boolean canShowPanel(AreaMagazzino areaMagazzino) {

        entitaLite = areaMagazzino.getDocumento().getEntita();
        boolean entitaValid = entitaLite != null && "C".equals(entitaLite.getTipo());

        TipoAreaMagazzino tam = areaMagazzino.getTipoAreaMagazzino();
        boolean tipoMovValid = tam != null && tam.getTipoMovimento() != TipoMovimento.TRASFERIMENTO
                && !tam.isGestioneVending();

        return areaMagazzino.isNew() && tipoMovValid && entitaValid;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        AreaMagazzino areaMagazzino = (AreaMagazzino) formModel.getValueModel("areaMagazzino").getValue();
        if (areaMagazzino == null || areaMagazzino.getDocumento().getEntita() == null
                || (areaMagazzino.getTipoAreaMagazzino() != null
                        && areaMagazzino.getTipoAreaMagazzino().getTipoMovimento() == TipoMovimento.TRASFERIMENTO)) {
            setVisible(false);
            return;
        }

        if (rateAperte == null) {
            rateAperte = BigDecimal.ZERO;
        }

        BigDecimal rateAperteCorrente = (BigDecimal) ObjectUtils
                .defaultIfNull(formModel.getValueModel("importoRateAperte").getValue(), BigDecimal.ZERO);
        boolean ricalcolaFido = entitaLite == null;
        ricalcolaFido = ricalcolaFido || !entitaLite.getId().equals(areaMagazzino.getDocumento().getEntita().getId());
        ricalcolaFido = ricalcolaFido || rateAperte.compareTo(rateAperteCorrente) != 0;

        if (ricalcolaFido) {
            entitaLite = areaMagazzino.getDocumento().getEntita();
            rateAperte = (BigDecimal) ObjectUtils.defaultIfNull(formModel.getValueModel("importoRateAperte").getValue(),
                    BigDecimal.ZERO);
            calcolaFido();
        }
        setVisible(canShowPanel(areaMagazzino));
    }

    /**
     *
     * visualizza il dialogo di avvertimento di fuori fido.
     *
     * @param rimanente
     *            fido rimanente negativo perchè richiamato se la diff è negativa
     */
    private void showWarningDialogMessage(final String rimanente) {
        RcpSupport.showWarningDialog(this, "FidoEsaurito", new String[] { rimanente });
    }

    /**
     * Visualizza il pannello se nascosto e viceversa
     */
    public void toggleVisible() {
        setVisible(!isVisible());
    }
}
