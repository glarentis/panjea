package it.eurotn.rich.control.table.renderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;

public class EntitaLiteContextSensitiveCellRender extends IconContextSensitiveCellRenderer {

    private static final long serialVersionUID = -3572555804124859162L;

    private final Icon[] rendererIcons = new Icon[TipoEntita.values().length];

    /**
     * @param entita
     *            entita
     * @return icona
     */
    private Icon getRendererIcon(EntitaLite entita) {
        String keyImage = null;
        EntitaDocumento entitaDocumento = entita.creaEntitaDocumento();
        switch (entitaDocumento.getTipoEntita()) {
        case CLIENTE:
            keyImage = Cliente.class.getName();
            break;
        case FORNITORE:
            keyImage = Fornitore.class.getName();
            break;
        case AZIENDA:
            keyImage = Azienda.class.getName();
            break;
        case BANCA:
            keyImage = Banca.class.getName();
            break;
        case VETTORE:
            keyImage = Vettore.class.getName();
            break;
        case AGENTE:
            keyImage = Agente.class.getName();
            break;
        default:
            throw new UnsupportedOperationException("Tipo entita non prevista " + entita.getTipo());
        }

        if (rendererIcons[entitaDocumento.getTipoEntita().ordinal()] == null) {
            rendererIcons[entitaDocumento.getTipoEntita().ordinal()] = getIconSource().getIcon(keyImage);
        }

        return rendererIcons[entitaDocumento.getTipoEntita().ordinal()];
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Icon icon = null;
        try {
            EntitaLite entita = (EntitaLite) value;
            if (entita != null && !entita.isNew()) {
                icon = getRendererIcon(entita);
            }
        } catch (Exception e) {
            icon = null;
        }
        label.setIcon(icon);
        label.setHorizontalTextPosition(getTextPosition());

        return label;
    }
}
