package it.eurotn.panjea.rich.editors.dms.allegati;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.jidesoft.list.ImagePreviewList;
import com.logicaldoc.webservice.document.WSDocument;

import it.eurotn.panjea.rich.bd.IDmsBD;
import it.eurotn.panjea.rich.editors.dms.allegati.action.ActionAllegatoCommand;
import it.eurotn.panjea.rich.editors.dms.allegati.action.CancellaAllegatoCommand;
import it.eurotn.panjea.rich.editors.dms.allegati.action.DownloadAllegatoCommand;
import it.eurotn.panjea.rich.editors.dms.allegati.action.OpenLinkAllegato;

public class AllegatiImagePreviewListMouseAdapter extends MouseAdapter {

    private List<ActionAllegatoCommand> commands = new ArrayList<>();
    private IDmsBD dmsBD;
    private ActionAllegatoCommand downloadAllegatoCommand;

    /**
     *
     * @param dmsBD
     *            bd dms
     */
    public AllegatiImagePreviewListMouseAdapter(final IDmsBD dmsBD) {
        this.dmsBD = dmsBD;
        commands.add(new CancellaAllegatoCommand());
        commands.add(new OpenLinkAllegato());
        downloadAllegatoCommand = new DownloadAllegatoCommand();
        commands.add(downloadAllegatoCommand);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        ImagePreviewList list = (ImagePreviewList) event.getSource();
        if (list.getSelectedValue() == null || !(list.getSelectedValue() instanceof AllegatoPreviewIcon)) {
            return;
        }
        AllegatoPreviewIcon previewIcon = (AllegatoPreviewIcon) list.getSelectedValue();
        final WSDocument document = previewIcon.getDocument();

        if (event.getClickCount() == 2) {
            downloadAllegatoCommand.execute(dmsBD, document.getId(), list);
            return;
        }

        Rectangle cellBounds = list.getCellBounds(list.getSelectedIndex(), list.getSelectedIndex());
        for (ActionAllegatoCommand actionAllegatoCommand : commands) {
            if (actionAllegatoCommand.isInButtonArea(cellBounds, event.getPoint())) {
                actionAllegatoCommand.execute(dmsBD, document.getId(), list);
                break;
            }
        }
    }

}
