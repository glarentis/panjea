package it.eurotn.panjea.manutenzioni.rich.editors.righeinstallazione;

import java.awt.Color;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

@SuppressWarnings("serial")
public class RigheInstallazioneTableModel extends DefaultBeanTableModel<RigaInstallazione> implements StyleModel {

    static final CellStyle CELL_STYLE_NEW_ROW = new CellStyle();

    static {
        CELL_STYLE_NEW_ROW.setBackground(Color.lightGray);
    }

    /**
     * Installazione.
     */
    public RigheInstallazioneTableModel() {
        super("righeInstallazioneTablePageWidget",
                new String[] { "tipoMovimento", "installazione.ubicazione", "installazione", "installazione.articolo",
                        "articoloInstallazione", "causaleInstallazione", "articoloRitiro", "causaleRitiro" },
                RigaInstallazione.class);
    }

    @Override
    public CellStyle getCellStyleAt(int row, int column) {
        int actualRow = TableModelWrapperUtils.getActualRowAt(this, row);
        if (actualRow == -1) {
            return null;
        }

        RigaInstallazione riga = getObject(row);
        if (riga.isNew()) {
            return CELL_STYLE_NEW_ROW;
        }
        return null;
    }

    @Override
    public boolean isCellStyleOn() {
        return true;
    }
}
