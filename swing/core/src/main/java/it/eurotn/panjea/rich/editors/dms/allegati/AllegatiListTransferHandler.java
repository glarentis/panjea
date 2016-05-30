package it.eurotn.panjea.rich.editors.dms.allegati;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.rich.bd.IDmsBD;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class AllegatiListTransferHandler extends TransferHandler {
    private static final Logger LOGGER = Logger.getLogger(AllegatiListTransferHandler.class);

    private static final long serialVersionUID = -4824144179902256995L;
    private IDmsBD dmsBD;
    private AllegatoDMS allegato;
    private String path;
    private IPageLifecycleAdvisor pageLifecycleAdvisor;

    /**
     * @param dmsBD
     *            bd dms
     * @param pageLifecycleAdvisor
     *            utilizzato per richiamare la loadData dopo aver pubblicato gli allegati
     */
    public AllegatiListTransferHandler(final IDmsBD dmsBD, final IPageLifecycleAdvisor pageLifecycleAdvisor) {
        super();
        this.dmsBD = dmsBD;
        this.pageLifecycleAdvisor = pageLifecycleAdvisor;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        if (allegato == null) {
            return false;
        }
        return info.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    /**
     * dispose.
     */
    public void dispose() {
        pageLifecycleAdvisor = null;
    }

    @Override
    public int getSourceActions(JComponent component) {
        return COPY;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        if (!info.isDrop()) {
            return false;
        }

        if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            return false;
        }

        // Get the string that is being dropped.
        Transferable transferable = info.getTransferable();
        try {
            @SuppressWarnings("unchecked")
            List<File> pathAllegato = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
            for (File file : pathAllegato) {
                dmsBD.pubblicaAllegato(path, file.getAbsolutePath(), file.getName(), allegato);
            }
            if (pageLifecycleAdvisor != null) {
                pageLifecycleAdvisor.refreshData();
            }
        } catch (Exception e) {
            LOGGER.error("-->errore nel drop del file per aggiungere gli allegati ", e);
            return false;
        }
        return true;
    }

    /**
     *
     * @param allegatoParam
     *            attributi dell'allegato
     */
    public void setAttributiAllegato(AllegatoDMS allegatoParam) {
        allegato = allegatoParam;
    }

    /**
     *
     * @param paramPath
     *            paramPath di pubblicazione
     */
    public void setDefaultPath(String paramPath) {
        this.path = paramPath;
    }

}