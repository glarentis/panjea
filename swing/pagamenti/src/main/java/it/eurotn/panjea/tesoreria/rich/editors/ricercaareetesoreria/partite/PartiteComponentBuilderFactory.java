package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite;

import javax.swing.JComponent;
import javax.swing.JPanel;

import it.eurotn.panjea.tesoreria.domain.AreaAnticipo;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;

public final class PartiteComponentBuilderFactory {

    /**
     * Crea i componenti delle partite.
     * 
     * @param areaTesoreria
     *            area tesoreria
     * @param tesoreriaBD
     *            tesoreriaBD
     * @return componenti creati
     */
    public static JComponent createComponent(AreaTesoreria areaTesoreria, ITesoreriaBD tesoreriaBD) {

        JComponent component = new JPanel();

        if (areaTesoreria instanceof AreaEffetti) {
            component = new EffettiComponentBuilder(areaTesoreria, tesoreriaBD).getControl();
        } else if (areaTesoreria instanceof AreaPagamenti) {
            component = new PagamentiComponentBuilder(areaTesoreria).getControl();
        } else if (areaTesoreria instanceof AreaAnticipo) {
            component = new AnticipiComponentBuilder(areaTesoreria).getControl();
        } else {
            throw new UnsupportedOperationException("Area tesoreria non gestita");
        }

        return component;
    }

    /**
     * Costruttore.
     */
    private PartiteComponentBuilderFactory() {
        super();
    }
}
