package it.eurotn.panjea.ordini.rich.editors.righeinserimento.parametri;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jdesktop.swingx.VerticalLayout;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.editors.righeinserimento.RigheInserimentoController;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;

public class ParametriRigheInserimentoComponent extends JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 3775474724224697259L;

    private List<AbstractInserimentoComponent> inserimentoComponents;

    {
        inserimentoComponents = new ArrayList<>();
        // inserimentoComponents.add(new OrdineCorrenteInserimentoComponent());
        inserimentoComponents.add(new AnalisiInserimentoComponent());
        inserimentoComponents.add(new UltimiOrdiniInserimentoComponent());
    }

    private RigheInserimentoController righeInserimentoController;

    /**
     * Costruttore.
     *
     */
    public ParametriRigheInserimentoComponent() {
        super(new VerticalLayout(5));

        initControl();
    }

    private void initControl() {

        for (AbstractInserimentoComponent component : inserimentoComponents) {
            component.addParametriCreatedListener(this);
            add(component);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (AbstractInserimentoComponent.PARAMETRI_CREATED_PROPERTY.equals(evt.getPropertyName())) {

            ParametriRigheOrdineInserimento parametri = (ParametriRigheOrdineInserimento) evt.getNewValue();
            righeInserimentoController.loadRigheInserimento(parametri);
        }
    }

    /**
     * @param areaOrdine
     *            the areaOrdine to set
     */
    public void setAreaOrdine(AreaOrdine areaOrdine) {

        for (AbstractInserimentoComponent component : inserimentoComponents) {
            component.updateControl(areaOrdine);
        }
    }

    /**
     * @param righeInserimentoController
     *            the righeInserimentoController to set
     */
    public void setRigheInserimentoController(RigheInserimentoController righeInserimentoController) {
        this.righeInserimentoController = righeInserimentoController;
    }
}
