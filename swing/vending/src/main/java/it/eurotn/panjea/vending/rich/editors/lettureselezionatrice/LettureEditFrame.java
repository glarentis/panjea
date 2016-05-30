package it.eurotn.panjea.vending.rich.editors.lettureselezionatrice;

import java.util.List;

import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;
import it.eurotn.panjea.vending.rich.bd.VendingDocumentoBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

public class LettureEditFrame extends EditFrame<LetturaSelezionatrice> {

    private static final long serialVersionUID = 6209304295848835850L;

    private IVendingDocumentoBD vendingDocumentoBD;

    /**
     * Costruttore.
     *
     * @param editView
     *            editView
     * @param pageEditor
     *            pageEditor
     * @param startQuickAction
     *            quick action
     */
    public LettureEditFrame(final EEditPageMode editView,
            final AbstractTablePageEditor<LetturaSelezionatrice> pageEditor, final String startQuickAction) {
        super(editView, pageEditor, startQuickAction);
        this.vendingDocumentoBD = RcpSupport.getBean(VendingDocumentoBD.BEAN_ID);
    }

    @Override
    public LetturaSelezionatrice getTableManagedObject(Object object) {

        LetturaSelezionatrice lettura = (LetturaSelezionatrice) object;

        List<LetturaSelezionatrice> letture = vendingDocumentoBD.ricercaLettureSelezionatrice(lettura.getId());
        return letture.isEmpty() ? lettura : letture.get(0);
    }

}
