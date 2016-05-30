package it.eurotn.panjea.rich.bd;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.domain.AbstractLayout;
import it.eurotn.panjea.anagrafica.domain.ChartLayout;
import it.eurotn.panjea.anagrafica.domain.DockedLayout;
import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.panjea.anagrafica.service.interfaces.LayoutService;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class LayoutBD extends AbstractBaseBD implements ILayoutBD {

    public static final String LAYOUTBD_ID = "layoutBD";

    private static final Logger LOGGER = Logger.getLogger(LayoutBD.class);

    private LayoutService layoutService;

    @Override
    public void cancella(AbstractLayout layout) {
        LOGGER.debug("--> Enter cancella");
        start("cancella");
        try {
            layoutService.cancella(layout);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancella");
        }
        LOGGER.debug("--> Exit cancella ");
    }

    @Override
    public List<ChartLayout> caricaChartLayout(String key) {
        LOGGER.debug("--> Enter caricaChartLayout");
        start("carica");

        List<ChartLayout> layouts = new ArrayList<ChartLayout>();
        try {
            layouts = layoutService.caricaChartLayout(key);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("carica");
        }
        LOGGER.debug("--> Exit caricaChartLayout");
        return layouts;
    }

    @Override
    public DockedLayout caricaDefaultDockedLayout(String key) {
        LOGGER.debug("--> Enter caricaDefaultDockedLayout");
        start("caricaDefaultDockedLayout");

        DockedLayout layout = null;
        try {
            layout = layoutService.caricaDefaultDockedLayout(key);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDefaultDockedLayout");
        }
        LOGGER.debug("--> Exit caricaDefaultDockedLayout ");
        return layout;

    }

    @Override
    public DockedLayout caricaDockedLayout(String key) {
        LOGGER.debug("--> Enter caricaDockedLayout");
        start("caricaDockedLayout");

        DockedLayout layout = null;
        try {
            layout = layoutService.caricaDockedLayout(key);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDockedLayout");
        }
        LOGGER.debug("--> Exit caricaDockedLayout ");
        return layout;
    }

    @Override
    public List<TableLayout> caricaTableLayout(String key) {
        LOGGER.debug("--> Enter carica");
        start("carica");
        List<TableLayout> layouts = new ArrayList<TableLayout>();
        try {
            layouts = layoutService.caricaTableLayout(key);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("carica");
        }
        LOGGER.debug("--> Exit carica ");
        return layouts;
    }

    @Override
    public AbstractLayout salva(AbstractLayout layout) {
        LOGGER.debug("--> Enter salva");
        start("salva");
        AbstractLayout layoutSalvato = null;
        try {
            layoutSalvato = layoutService.salva(layout);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salva");
        }
        LOGGER.debug("--> Exit salva ");
        return layoutSalvato;
    }

    /**
     * @param layoutService
     *            the layoutService to set
     */
    public void setLayoutService(LayoutService layoutService) {
        this.layoutService = layoutService;
    }

}
