package it.eurotn.panjea.compoli.rich.editors.entitacointestate;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.EditorContext;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.compoli.rich.bd.ComunicazionePolivalenteBD;
import it.eurotn.panjea.compoli.rich.bd.IComunicazionePolivalenteBD;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.EntitaCointestazione;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

public class EntitaCointestateTableModel extends DefaultBeanEditableTableModel<EntitaCointestazione> {

    private static final long serialVersionUID = 1151107485271201898L;

    private final SearchContext entitaCodiceEditorContext = new SearchContext("codice", "entita");
    private final SearchContext entitaDenominazioneEditorContext = new SearchContext("anagrafica.denominazione",
            "entita");

    private IComunicazionePolivalenteBD comunicazionePolivalenteBD;

    private AreaContabile areaContabile;

    /**
     * Costruttore.
     */
    public EntitaCointestateTableModel() {
        super("entitaCointestateTableModel", new String[] { "entita.codice", "entita.anagrafica.denominazione" },
                EntitaCointestazione.class);
        comunicazionePolivalenteBD = RcpSupport.getBean(ComunicazionePolivalenteBD.BEAN_ID);
    }

    @Override
    protected EntitaCointestazione createNewObject() {
        EntitaCointestazione entitaCointestazione = new EntitaCointestazione();
        entitaCointestazione.setAreaContabile(areaContabile);
        entitaCointestazione.setEntita(new EntitaLite());
        return entitaCointestazione;
    }

    /**
     * Verifica se le 2 entita sono diverse.
     *
     * @param oldEntita
     *            oldEntita
     * @param newEntita
     *            newEntita
     * @return <code>true</code> se diverse
     */
    private boolean entitaChanged(EntitaLite oldEntita, EntitaLite newEntita) {
        if (oldEntita != null) {
            return !oldEntita.equals(newEntita);
        } else {
            return newEntita != null;
        }
    }

    @Override
    public EditorContext getEditorContextAt(int row, int col) {

        TipoEntita tipoEntita = getElementAt(row).getAreaContabile().getTipoAreaContabile().getTipoDocumento()
                .getTipoEntita();
        switch (col) {
        case 0:
            entitaCodiceEditorContext.addPropertyFilterValue(EntitaByTipoSearchObject.TIPOENTITA_KEY,
                    "tipoEntitaFilter", tipoEntita);
            return entitaCodiceEditorContext;
        case 1:
            entitaDenominazioneEditorContext.addPropertyFilterValue(EntitaByTipoSearchObject.TIPOENTITA_KEY,
                    "tipoEntitaFilter", tipoEntita);
            return entitaDenominazioneEditorContext;
        default:
            break;
        }
        return super.getEditorContextAt(row, col);
    }

    @Override
    public void removeObject(EntitaCointestazione entitaCointestazione) {
        super.removeObject(entitaCointestazione);
        if (entitaCointestazione != null && !entitaCointestazione.isNew()) {
            comunicazionePolivalenteBD.cancellaEntitaCointestazione(entitaCointestazione.getId());
        }
    }

    /**
     * @param areaContabile
     *            the areaContabile to set
     */
    public void setAreaContabile(AreaContabile areaContabile) {
        this.areaContabile = areaContabile;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        EntitaLite oldEntita = getElementAt(row).getEntita();

        super.setValueAt(value, row, column);

        EntitaCointestazione entitaNew = getElementAt(row);

        if (entitaChanged(oldEntita, entitaNew.getEntita())) {
            entitaNew = comunicazionePolivalenteBD.salvaEntitaCointestazione(entitaNew);
            setObject(entitaNew, row);
        }
    }

}
