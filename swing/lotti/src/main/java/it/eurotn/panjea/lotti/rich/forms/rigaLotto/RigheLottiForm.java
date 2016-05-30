package it.eurotn.panjea.lotti.rich.forms.rigaLotto;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.table.TableCellEditor;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.rich.bd.ILottiBD;
import it.eurotn.panjea.lotti.rich.bd.LottiBD;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;

public class RigheLottiForm extends PanjeaAbstractForm implements Focussable {

    private class ArticoloPropertyChange implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            righeLottoTableModel.setRigaArticolo((RigaArticolo) getFormObject());
        }
    }

    private final class CreaRigaArticoloCommand extends ActionCommand {

        public static final String COMMAND_ID = "creaRigaArticoloCommand";

        private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

        /**
         * Costruttore.
         *
         */
        private CreaRigaArticoloCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
            this.magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
        }

        @Override
        protected void doExecuteCommand() {

            TableCellEditor cellEditor = righeLottoBinding.getTableWidget().getTable().getCellEditor();
            if (cellEditor != null) {
                cellEditor.stopCellEditing();
            }

            @SuppressWarnings("unchecked")
            Set<RigaLotto> righeLotti = (Set<RigaLotto>) getFormModel().getValueModel("righeLotto").getValue();

            if (!righeLotti.isEmpty()) {

                // ciclo sulle righe lotto per verificare che tutte abbiano il lotto settato. Non posso farlo tramite
                // una regola sul plugin rules source perchè anche se le righe lotto non sono valide il pulsante salva
                // deve rimanere abilitato ( caso in cui abbia i lotti automatici ).
                boolean lottiPresenti = true;
                for (RigaLotto rigaLotto : righeLotti) {
                    if (rigaLotto.getLotto() == null) {
                        lottiPresenti = false;
                        break;
                    }
                }

                if (lottiPresenti) {
                    ArticoloLite articoloLite = magazzinoAnagraficaBD
                            .caricaArticoloLite(righeLotti.iterator().next().getLotto().getArticolo().getId());
                    getFormModel().getValueModel("articolo").setValue(articoloLite);

                    Double qta = 0.0;
                    for (RigaLotto rigaLotto : righeLotti) {
                        qta = qta + rigaLotto.getQuantita();
                    }
                    getFormModel().getValueModel("qta").setValue(qta);
                    getFormModel().getValueModel("righeLotto").setValue(righeLotti);
                } else {
                    MessageDialog dialog = new MessageDialog("ATTENZIONE",
                            "Assegnare il lotto su tutte le righe presenti.");
                    dialog.showDialog();
                }
            }
        }

    }

    private class RigheLottoPropertyChange implements PropertyChangeListener {

        public static final String LABEL_RIGHE_LOTTI_VALID = "labelRigheLotti.valid";
        public static final String LABEL_RIGHE_LOTTI_NOT_VALID = "labelRigheLotti.notValid";

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            RigaArticolo rigaArticolo = (RigaArticolo) getFormModel().getFormObject();
            if (rigaArticolo.getArticolo() == null) {
                return;
            }

            righeLottoTableModel.setRigaArticolo(rigaArticolo);

            creaRigaArticoloCommand.setEnabled(!getFormModel().isReadOnly() && rigaArticolo.getRigheLotto().size() > 0);

            TipoMovimento tipoMovimento = rigaArticolo.getAreaMagazzino().getTipoAreaMagazzino().getTipoMovimento();
            boolean isCarico = tipoMovimento == TipoMovimento.CARICO
                    || tipoMovimento == TipoMovimento.CARICO_PRODUZIONE;

            if (!isCarico) {
                lottoPrecedente = null;
            }

            if (usaUltimoLotto && isCarico) {
                if (rigaArticolo.getRigheLotto().size() == 0) {
                    nuovaRigaLottoPrecedente();

                } else {
                    if (!getFormModel().isReadOnly()) {
                        lottoPrecedente = rigaArticolo.getRigheLotto().iterator().next().getLotto();
                    }
                }

                if (rigaArticolo.isNew() && rigaArticolo.getRigheLotto().size() > 0) {
                    Set<RigaLotto> righeLotto = rigaArticolo.getRigheLotto();
                    righeLotto.iterator().next().setQuantita(rigaArticolo.getQta());
                    getFormModel().getValueModel("righeLotto").setValueSilently(new HashSet<RigaLotto>(righeLotto),
                            RigheLottiForm.this.righeLottoPropertyChange);
                }
            }
            if (rigaArticolo.isLottiValid()) {
                labelRigheLottiTitle
                        .setText(getMessage(LABEL_RIGHE_LOTTI_VALID, new Object[] { rigaArticolo.getQta() }));
                labelRigheLottiTitle.setForeground(Color.BLACK);
            } else {
                Double qtaLotti = 0.0;
                for (RigaLotto rigaLotto : rigaArticolo.getRigheLotto()) {
                    qtaLotti = qtaLotti + rigaLotto.getQuantita();
                }
                labelRigheLottiTitle.setText(
                        getMessage(LABEL_RIGHE_LOTTI_NOT_VALID, new Object[] { qtaLotti, rigaArticolo.getQta() }));
                labelRigheLottiTitle.setForeground(Color.RED);
            }
        }

    }

    public static final String FORM_ID = "righeLottiForm";

    private JLabel labelRigheLottiTitle = null;

    private Lotto lottoPrecedente = null;

    private RigheLottoTableModel righeLottoTableModel;

    private TableEditableBinding<RigaLotto> righeLottoBinding;

    private CreaRigaArticoloCommand creaRigaArticoloCommand;

    private RigheLottoPropertyChange righeLottoPropertyChange;

    private ILottiBD lottibd;

    private boolean usaUltimoLotto;

    /**
     * Costruttore.
     *
     * @param formModel
     *            form model
     */
    public RigheLottiForm(final FormModel formModel) {
        super(formModel, FORM_ID);
        lottibd = RcpSupport.getBean(LottiBD.BEAN_ID);
        IMagazzinoAnagraficaBD magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
        usaUltimoLotto = magazzinoAnagraficaBD.caricaMagazzinoSettings().isNuovoDaUltimoLotto();
    }

    @Override
    protected JComponent createFormControl() {

        labelRigheLottiTitle = getComponentFactory().createLabel("");

        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("left:230dlu,11dlu,11dlu,left:default:grow",
                "default,10dlu,default,default,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
        builder.setLabelAttributes("l, t");

        builder.addComponent(labelRigheLottiTitle, 1, 1, 4, 1);
        builder.nextRow();

        righeLottoTableModel = new RigheLottoTableModel((RigaArticolo) getFormObject());
        righeLottoBinding = new TableEditableBinding<RigaLotto>(getFormModel(), "righeLotto", Set.class,
                righeLottoTableModel);
        builder.addBinding(righeLottoBinding, 1, 5);
        builder.setComponentAttributes("f,t");
        creaRigaArticoloCommand = new CreaRigaArticoloCommand();
        builder.addComponent(creaRigaArticoloCommand.createButton(), 2, 5);
        builder.setComponentAttributes("f,c");

        righeLottoPropertyChange = new RigheLottoPropertyChange();
        getFormModel().getValueModel("righeLotto").addValueChangeListener(righeLottoPropertyChange);
        getFormModel().getValueModel("qta").addValueChangeListener(righeLottoPropertyChange);
        addFormObjectChangeListener(new ArticoloPropertyChange());
        getFormModel().getValueModel("articolo").addValueChangeListener(new ArticoloPropertyChange());

        return builder.getPanel();
    }

    @Override
    public void dispose() {

        getFormModel().getValueModel("righeLotto").removeValueChangeListener(righeLottoPropertyChange);
        getFormModel().getValueModel("qta").removeValueChangeListener(righeLottoPropertyChange);

        super.dispose();
    }

    @Override
    public void grabFocus() {
        righeLottoBinding.getTableWidget().getTable().requestFocusInWindow();
        righeLottoBinding.getTableWidget().getTable().changeSelection(0, 0, false, false);
    }

    /**
     * Dal lotto precedente recupero data scadenza e codice e inserisco la riga.
     */
    private void nuovaRigaLottoPrecedente() {
        ArticoloLite articolo = (ArticoloLite) getFormModel().getValueModel("articolo").getValue();

        if (lottoPrecedente != null && articolo != null && articolo.getTipoLotto() != TipoLotto.NESSUNO) {
            // Verifico se ho il lotto per l'articolo con codice e data scadenza.
            Lotto lotto = lottibd.caricaLotto(lottoPrecedente.getCodice(), lottoPrecedente.getDataScadenza(),
                    articolo.getId());
            if (lotto == null) {
                lotto = new Lotto();
                lotto.setArticolo(articolo);
                lotto.setDataScadenza(lottoPrecedente.getDataScadenza());
                lotto.setCodice(lottoPrecedente.getCodice());
                lotto.setCodiceAzienda(lottoPrecedente.getCodiceAzienda());
                lotto = lottibd.salvaLotto(lotto);
            }
            RigaLotto rigaLotto = new RigaLotto();
            RigaArticolo rigaArticolo = (RigaArticolo) getFormModel().getFormObject();
            rigaLotto.setLotto(lotto);
            rigaLotto.setRigaArticolo(rigaArticolo);
            rigaLotto.setQuantita(0.0);
            Set<RigaLotto> righeLotto = new HashSet<RigaLotto>();
            righeLotto.add(rigaLotto);
            getFormModel().getValueModel("righeLotto").setValueSilently(righeLotto, righeLottoPropertyChange);
        }
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);
    }
}
