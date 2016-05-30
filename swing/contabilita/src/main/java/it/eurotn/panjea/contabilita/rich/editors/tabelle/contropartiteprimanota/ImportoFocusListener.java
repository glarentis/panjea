package it.eurotn.panjea.contabilita.rich.editors.tabelle.contropartiteprimanota;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

/**
 * Listener che agisce sul componente passato come parametro e seleziona tutto il testo quando riceve il focus
 *
 * @author fattazzo
 *
 */
public class ImportoFocusListener implements FocusListener {

    private final JTextField field;

    /**
     *
     * Costruttore.
     *
     * @param field
     *            text filed
     */
    public ImportoFocusListener(final JTextField field) {
        super();
        this.field = field;
    }

    @Override
    public void focusGained(FocusEvent event) {
        // HACK devo risettare il testo al componente perchè altrimenti la selectAll non funziona
        field.setText(field.getText());

        field.selectAll();
    }

    /**
     * Focus lost: "read"-format.
     */
    @Override
    public void focusLost(FocusEvent e) {
        // non faccio niente
    }
}
