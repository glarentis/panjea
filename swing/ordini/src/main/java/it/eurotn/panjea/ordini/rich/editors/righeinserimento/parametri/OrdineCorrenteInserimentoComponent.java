package it.eurotn.panjea.ordini.rich.editors.righeinserimento.parametri;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento.TipoRicerca;

public class OrdineCorrenteInserimentoComponent extends AbstractInserimentoComponent {

    private class OrdineCorrenteCommand extends ActionCommand {

        /**
         * Costruttore.
         *
         */
        public OrdineCorrenteCommand() {
            super();
        }

        @Override
        protected void doExecuteCommand() {
            ParametriRigheOrdineInserimento parametri = new ParametriRigheOrdineInserimento();
            parametri.setIdAreaOrdine(areaOrdineCorrente.getId());
            parametri.setTipoRicerca(TipoRicerca.ORDINE);
            fireParametriCreated(parametri);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setText(ObjectConverterManager.toString(areaOrdineCorrente));
            button.setFont(new Font(button.getFont().getName(), Font.BOLD, button.getFont().getSize()));
            button.setIcon(RcpSupport.getIcon(AreaOrdine.class.getName()));
        }
    }

    private static final long serialVersionUID = 5331035093523341727L;

    private AreaOrdine areaOrdineCorrente;

    /**
     * Costruttore.
     */
    public OrdineCorrenteInserimentoComponent() {
        super();
        setLayout(new GridLayout(1, 1));
    }

    @Override
    public String getTitle() {
        return "Ordine corrente";
    }

    @Override
    public void updateControl(AreaOrdine areaOrdine) {
        this.areaOrdineCorrente = areaOrdine;

        removeAll();

        if (areaOrdine != null && !areaOrdine.isNew()) {
            add(new OrdineCorrenteCommand().createButton());
        }
    }

}
