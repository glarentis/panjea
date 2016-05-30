package it.eurotn.panjea.rich.editors.dms.allegati;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.list.DefaultPreviewImageIcon;
import com.jidesoft.list.ImagePreviewList;
import com.logicaldoc.webservice.document.WSDocument;

import it.eurotn.panjea.rich.bd.DmsBD;
import it.eurotn.panjea.rich.bd.IDmsBD;

public class AllegatiImagePreviewList extends ImagePreviewList {

    private class TileRunnable implements Runnable {

        private WSDocument document;
        private int listIndex;

        private IDmsBD dmsBD;

        private Integer idArticolo;

        /**
         * Costruttore.
         */
        public TileRunnable(final WSDocument document, final int listIndex, final IDmsBD dmsBD,
                final Integer idArticolo) {
            super();
            this.document = document;
            this.listIndex = listIndex;
            this.dmsBD = dmsBD;
            this.idArticolo = idArticolo;
        }

        @Override
        public void run() {

            byte[] imageByte = dmsBD.getAllegatoTile(document.getId());

            if (imageByte.length > 0) {
                final DefaultPreviewImageIcon icon = new AllegatoPreviewIcon(new ImageIcon(imageByte), document);
                SwingUtilities.invokeLater(new Runnable() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void run() {
                        // Se l'utente non ha cambiato articolo aggiorno l'immagine
                        if (Objects.equals(idArticolo, entityId)) {

                            // necessario per vedere la nuova immagine
                            ((DefaultListModel<ImagePreviewList.PreviewImageIcon>) getModel()).setElementAt(icon,
                                    listIndex);
                        }
                    }
                });
            }
        }

    }

    private static final Logger LOGGER = Logger.getLogger(AllegatiImagePreviewList.class);

    private static final long serialVersionUID = 8822562311583023961L;

    private IDmsBD dmsBD;

    private ExecutorService executor = null;

    private List<WSDocument> documents;

    private Integer entityId;

    /**
     * Costruttore.
     *
     */
    public AllegatiImagePreviewList() {
        super(new DefaultListModel<ImagePreviewList.PreviewImageIcon>());

        this.dmsBD = RcpSupport.getBean(DmsBD.BEAN_ID);

        initialize();
    }

    @SuppressWarnings("unchecked")
    private void initialize() {

        addMouseListener(new AllegatiImagePreviewListMouseAdapter(dmsBD));
        setShowDetails(0);
        setFixedCellWidth(300);
        setFixedCellHeight(200);
        setCellRenderer(new AllegatoImagePreviewCellRenderer());
    }

    private void loadTiles() {
        LOGGER.debug("--> Enter loadTiles");
        int index = 0;
        try {
            if (executor != null && !executor.isTerminated()) {
                executor.shutdownNow();
            }
            executor = Executors.newFixedThreadPool(3);
            for (WSDocument wsDocument : documents) {
                executor.execute(new TileRunnable(wsDocument, index, dmsBD, entityId));
                index++;
            }
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle anteprime: " + e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Aggiunge i documenti alla preview.
     *
     * @param docs
     *            documenti
     */
    @SuppressWarnings("unchecked")
    public void setAllegati(List<WSDocument> docs) {
        this.documents = docs;

        ((DefaultListModel<ImagePreviewList.PreviewImageIcon>) getModel()).clear();
        if (docs != null) {
            for (WSDocument wsDocument : this.documents) {
                AllegatoPreviewIcon icons = new AllegatoPreviewIcon(null, wsDocument);
                ((DefaultListModel<ImagePreviewList.PreviewImageIcon>) getModel()).addElement(icons);
            }
            loadTiles();
        }
    }
}
