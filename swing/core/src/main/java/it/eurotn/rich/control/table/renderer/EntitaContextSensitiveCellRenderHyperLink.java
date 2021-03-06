package it.eurotn.rich.control.table.renderer;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.Vettore;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JTable;

public class EntitaContextSensitiveCellRenderHyperLink extends IconHyperlinkContextSensitiveCellRenderer {

    private static final long serialVersionUID = -3572555804124859162L;

    private final Icon[] rendererIcons = new Icon[TipoEntita.values().length];

    @Override
    public void configureTableCellEditorRendererComponent(JTable table, Component editorRendererComponent,
            boolean forRenderer, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.configureTableCellEditorRendererComponent(table, editorRendererComponent, forRenderer, value, isSelected,
                hasFocus, row, column);

        if (editorRendererComponent instanceof AbstractButton) {
            Icon icon;
            try {
                Entita entita = (Entita) value;

                icon = getRendererIcon(entita);
            } catch (Exception e) {
                icon = null;
            }

            ((AbstractButton) editorRendererComponent).setIcon(icon);
        }
    }

    /**
     * @param entita
     *            entita
     * @return icona
     */
    private Icon getRendererIcon(Entita entita) {
        String keyImage = null;
        switch (entita.getTipo()) {
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

        if (rendererIcons[entita.getTipo().ordinal()] == null) {
            rendererIcons[entita.getTipo().ordinal()] = getIconSource().getIcon(keyImage);
        }

        return rendererIcons[entita.getTipo().ordinal()];
    }

    // @Override
    // public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
    // int row, int column) {
    // JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    //
    // Icon icon;
    // try {
    // Entita entita = (Entita) value;
    //
    // icon = getRendererIcon(entita);
    // } catch (Exception e) {
    // icon = null;
    // }
    //
    // label.setIcon(icon);
    // label.setHorizontalTextPosition(getTextPosition());
    //
    // return label;
    // }
}
