package it.eurotn.panjea.vending.rich.editors.distributore.modelli;

import java.util.List;
import java.util.Map;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.util.KeyFromValueProvider;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Classe per creare il tree dei modelli.
 */
public class ModelloTreeTableFactory {

    /**
     * Crea il tree node dei modelli.
     *
     * @param tipiModello
     *            tipo modello
     * @param modelli
     *            modelli con cui creare il tree node
     * @return DefaultMutableTreeTableNode
     */
    public DefaultMutableTreeTableNode create(List<TipoModello> tipiModello, List<Modello> modelli) {
        DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();

        // aggiungo un modello con id-1 er indicare che è il modello "root"
        Modello modelloRoot = new Modello();
        modelloRoot.setId(-1);
        root.setUserObject(modelloRoot);

        Map<TipoModello, List<Modello>> modelliMap = PanjeaEJBUtil.listToMap(modelli,
                new KeyFromValueProvider<Modello, TipoModello>() {
                    @Override
                    public TipoModello keyFromValue(Modello modello) {
                        return modello.getTipoModello();
                    }
                });

        for (TipoModello tipoModello : tipiModello) {
            DefaultMutableTreeTableNode tipoModelloNode = new DefaultMutableTreeTableNode(tipoModello);
            if (modelliMap.containsKey(tipoModello)) {
                for (Modello modello : modelliMap.get(tipoModello)) {
                    DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(modello);
                    tipoModelloNode.add(node);
                }
            }
            root.add(tipoModelloNode);
        }

        return root;
    }

}