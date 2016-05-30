package it.eurotn.panjea.ordini.rich.editors.righeinserimento.parametri;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.ActionCommand;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento.TipoRicerca;

public class UltimiOrdiniInserimentoComponent extends AbstractInserimentoComponent {

    private class UltimoOrdineCommand extends ActionCommand {

        private int numeroOrdini;

        /**
         * Costruttore.
         *
         * @param numeroOrdini
         *            numero ordini
         */
        public UltimoOrdineCommand(final int numeroOrdini) {
            super();
            this.numeroOrdini = numeroOrdini;
        }

        @Override
        protected void doExecuteCommand() {
            ParametriRigheOrdineInserimento parametri = new ParametriRigheOrdineInserimento();
            parametri.setNumeroOrdiniDaConsiderare(numeroOrdini);
            parametri.setSedeEntita(areaOrdineCorrente.getDocumento().getSedeEntita());
            parametri.setTipoRicerca(TipoRicerca.ULTIMI_ORDINI);
            fireParametriCreated(parametri);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setText(String.valueOf(numeroOrdini));
            button.setFont(new Font(button.getFont().getName(), Font.BOLD, 40));
        }

    }

    private static final long serialVersionUID = 5331035093523341727L;

    private AreaOrdine areaOrdineCorrente;

    /**
     * Costruttore.
     */
    public UltimiOrdiniInserimentoComponent() {
        super();
        setLayout(new GridLayout(2, 3, 5, 5));
    }

    @Override
    public String getTitle() {
        return "Ultimi ordini";
    }

    @Override
    public void updateControl(AreaOrdine areaOrdine) {
        this.areaOrdineCorrente = areaOrdine;

        removeAll();

        add(new UltimoOrdineCommand(1).createButton());
        add(new UltimoOrdineCommand(2).createButton());
        add(new UltimoOrdineCommand(3).createButton());
        add(new UltimoOrdineCommand(4).createButton());
        add(new UltimoOrdineCommand(5).createButton());
        add(new UltimoOrdineCommand(10).createButton());
    }

}
