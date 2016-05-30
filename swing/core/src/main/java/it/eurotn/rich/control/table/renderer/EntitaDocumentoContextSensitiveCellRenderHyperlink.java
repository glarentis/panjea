package it.eurotn.rich.control.table.renderer;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JTable;

import org.springframework.richclient.application.Application;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class EntitaDocumentoContextSensitiveCellRenderHyperlink extends IconHyperlinkContextSensitiveCellRenderer {

    private static final long serialVersionUID = -3572555804124859162L;

    private final Icon[] rendererIcons = new Icon[TipoEntita.values().length];

    /**
     * Costruttore.
     */
    public EntitaDocumentoContextSensitiveCellRenderHyperlink() {
        super();
    }

    @Override
    public void configureTableCellEditorRendererComponent(JTable table, Component editorRendererComponent,
            boolean forRenderer, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.configureTableCellEditorRendererComponent(table, editorRendererComponent, forRenderer, value, isSelected,
                hasFocus, row, column);

        if (editorRendererComponent instanceof AbstractButton) {
            Icon icon;
            try {
                EntitaDocumento entita = (EntitaDocumento) value;

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
    private Icon getRendererIcon(EntitaDocumento entita) {
        String keyImage = null;
        switch (entita.getTipoEntita()) {
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
        default:
            throw new UnsupportedOperationException("Tipo entita non prevista " + entita.getTipoEntita());
        }

        if (rendererIcons[entita.getTipoEntita().ordinal()] == null) {
            rendererIcons[entita.getTipoEntita().ordinal()] = getIconSource().getIcon(keyImage);
        }

        return rendererIcons[entita.getTipoEntita().ordinal()];
    }

    @Override
    protected void openEditor(Object value) {

        // se l'oggetto è una entità uso la open editor standard altrimenti lancio un evento di open editor
        // all'applicazione

        EntitaDocumento entitaDocumento = (EntitaDocumento) value;

        EntitaLite entita = null;
        switch (entitaDocumento.getTipoEntita()) {
        case CLIENTE:
            entita = new ClienteLite();
            break;
        case FORNITORE:
            entita = new FornitoreLite();
            break;
        case AZIENDA:
            Application.instance().getApplicationContext().publishEvent(new OpenEditorEvent("aziendaEditor"));
            return;
        case BANCA:
            Application.instance().getApplicationContext().publishEvent(new OpenEditorEvent("datiBancariEditor"));
            return;
        case VETTORE:
            entita = new VettoreLite();
            break;
        default:
            throw new UnsupportedOperationException("Tipo entita non prevista " + entitaDocumento.getTipoEntita());
        }

        if (entita != null) {
            entita.setId(entitaDocumento.getId());
            super.openEditor(entita);
        }
    }
}
