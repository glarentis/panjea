package it.eurotn.rich.dialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.docking.DockingManager;
import com.jidesoft.utils.Base64;

import it.eurotn.panjea.anagrafica.domain.DockedLayout;
import it.eurotn.panjea.rich.bd.ILayoutBD;
import it.eurotn.panjea.rich.bd.LayoutBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * Gestisce il caricamento ed il salvataggio dei file di layout per le docking page.<br/>
 * Sul caricamento se non trova il file di layout ripristina i layout predefinito che si trova nel package
 * it.eurotn.panjea.resources.dockinglayout.<br/>
 * Se non viene trovato il layout predefinito non succede nulla (non viene generato un errore)
 *
 * @author giangi
 */
public final class DockingLayoutManager {
    private static final ILayoutBD LAYOUTBD = RcpSupport.getBean(LayoutBD.LAYOUTBD_ID);

    private static Logger logger = Logger.getLogger(DockingLayoutManager.class);

    /**
     * Ripristina il layout di default.
     *
     * @param dockingManager
     *            docking manager
     * @param pageId
     *            id del layout
     */
    public static void restoreCurrentLayout(DockingManager dockingManager, String pageId) {
        logger.debug("--> Enter restoreInitialLayout per la pagina " + pageId);

        final DockedLayout dockedLayout = LAYOUTBD.caricaDefaultDockedLayout(pageId);

        if (dockedLayout != null) {
            dockingManager.loadLayoutFrom(new ByteArrayInputStream(Base64.decode(dockedLayout.getData())));
        } else {
            dockingManager.loadLayoutData();
        }
        logger.debug("--> Exit restoreInitialLayout");
    }

    /**
     * Ripristina il layout.
     *
     * @param dockingManager
     *            docking manager
     * @param pageId
     *            id del layout
     */
    public static void restoreLayout(DockingManager dockingManager, String pageId) {
        logger.debug("--> Enter restoreLayout per la pagina " + pageId);

        final DockedLayout dockedLayout = LAYOUTBD.caricaDockedLayout(pageId);

        if (dockedLayout != null) {
            dockingManager.loadLayoutFrom(new ByteArrayInputStream(Base64.decode(dockedLayout.getData())));
        } else {
            dockingManager.loadLayoutData();
        }
        logger.debug("--> Exit restoreLayout");
    }

    /**
     * Salva il layout.
     *
     * @param dockingManager
     *            docking manager
     * @param pageId
     *            id layout
     */
    public static void saveLayout(DockingManager dockingManager, String pageId) {
        logger.debug("--> Enter saveLayout per la pagina " + pageId);

        final DockedLayout dockedLayout = new DockedLayout();
        dockedLayout.setChiave(pageId);
        final String user = PanjeaSwingUtil.getUtenteCorrente() != null
                ? PanjeaSwingUtil.getUtenteCorrente().getUserName() : "unknown";
        dockedLayout.setUtente(user);

        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bout.reset();
        try {
            dockingManager.saveLayoutTo(bout);
            dockedLayout.setData(Base64.encodeBytes(bout.toByteArray()));
            LAYOUTBD.salva(dockedLayout);
        } catch (final IOException e) {
            logger.error("-->errore durante il salvataggio del layout", e);
        }

        logger.debug("--> Exit saveLayout");
    }
}
