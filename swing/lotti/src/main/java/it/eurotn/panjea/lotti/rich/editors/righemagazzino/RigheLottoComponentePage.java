package it.eurotn.panjea.lotti.rich.editors.righemagazzino;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.rich.bd.ILottiBD;
import it.eurotn.panjea.lotti.rich.bd.LottiBD;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class RigheLottoComponentePage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    private class RigheLottoComponenteTableModel extends DefaultBeanTableModel<RigaLotto> {

        private static final long serialVersionUID = -7459046993887812446L;

        private ConverterContext qtaContext;

        {
            qtaContext = new NumberWithDecimalConverterContext();
            qtaContext.setUserObject(2);
        }

        /**
         * 
         * Costruttore.
         */
        public RigheLottoComponenteTableModel() {
            super("righeLottoComponenteTableModel",
                    new String[] { "lotto", "quantita", "lotto.priorita", "lotto.dataScadenza" }, RigaLotto.class);
        }

        @Override
        public ConverterContext getConverterContextAt(int row, int column) {
            switch (column) {
            case 1:
                qtaContext.setUserObject(getElementAt(row).getRigaArticolo().getArticolo().getNumeroDecimaliQta());
                return qtaContext;
            default:
                return super.getConverterContextAt(row, column);
            }
        }

    }

    private JideTableWidget<RigaLotto> righeLottoTableWidget;

    private IRigaArticoloDocumento rigaArticoloDocumento = null;

    private ILottiBD lottiBD;

    {
        lottiBD = RcpSupport.getBean(LottiBD.BEAN_ID);
    }

    /**
     * 
     * Costruttore.
     */
    protected RigheLottoComponentePage() {
        super("righeLottoComponentePage");
    }

    @Override
    protected JComponent createControl() {
        righeLottoTableWidget = new JideTableWidget<RigaLotto>("righeLottoComponenteTableWidget",
                new RigheLottoComponenteTableModel());
        return righeLottoTableWidget.getComponent();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {
        if (!(rigaArticoloDocumento instanceof RigaArticolo)) {
            righeLottoTableWidget.setRows(new ArrayList<RigaLotto>());
        } else {
            List<RigaLotto> righeLotto = lottiBD.caricaRigheLotto((RigaArticolo) rigaArticoloDocumento);
            righeLottoTableWidget.setRows(righeLotto);
        }
    }

    @Override
    public void restoreState(Settings arg0) {
    }

    @Override
    public void saveState(Settings arg0) {
    }

    @Override
    public void setFormObject(Object object) {
        rigaArticoloDocumento = (IRigaArticoloDocumento) object;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

}
