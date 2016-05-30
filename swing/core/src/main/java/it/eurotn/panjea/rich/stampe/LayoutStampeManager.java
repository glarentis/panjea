package it.eurotn.panjea.rich.stampe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.rich.bd.ILayoutStampeBD;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class LayoutStampeManager implements ILayoutStampeManager {

    public enum TipoLayoutPrefefinito {
        PREDEFINITO, MAIL, INTERNO
    }

    private static Logger logger = Logger.getLogger(LayoutStampeManager.class);

    public static final String BEAN_ID = "layoutStampeManager";

    public static final String PRINTER_CONFIG_FILE_NAME = "printers.properties";

    public static final String ETICHETTE_PRINTER_NAME_KEY = "StampanteEtichetteArticolo";

    private ILayoutStampeBD layoutStampeBD;

    /**
     * Carica il file di configurazione delle stampanti dell'utente e associa le stampanti ai relativi layout di stampa.
     *
     * @param layouts
     *            layout di stampa
     */
    private void applyPrinterAssociation(List<? extends LayoutStampa> layouts) {

        if (layouts == null) {
            return;
        }

        Properties properties = loadPrinterConfigFile();
        for (LayoutStampa layoutStampa : layouts) {
            layoutStampa.setStampante(properties.getProperty(layoutStampa.getChiave()));
        }
    }

    @Override
    public List<LayoutStampaDocumento> caricaLayoutStampaBatch(ITipoAreaDocumento tipoAreaDocumento, EntitaLite entita,
            SedeEntita sedeEntita) {
        List<LayoutStampaDocumento> layouts = layoutStampeBD.caricaLayoutStampaBatch(tipoAreaDocumento, entita,
                sedeEntita);
        // aggiorno le stampanti configurate nel file dell'utente
        applyPrinterAssociation(layouts);
        return layouts;
    }

    @Override
    public List<LayoutStampaDocumento> caricaLayoutStampaPerDocumenti() {
        List<LayoutStampaDocumento> layouts = layoutStampeBD.caricaLayoutStampaPerDocumenti();
        applyPrinterAssociation(layouts);
        return layouts;
    }

    @Override
    public List<LayoutStampa> caricaLayoutStampe() {
        List<LayoutStampa> layouts = layoutStampeBD.caricaLayoutStampe();
        // aggiorno le stampanti configurate nel file dell'utente
        applyPrinterAssociation(layouts);
        return layouts;
    }

    @Override
    public List<LayoutStampaDocumento> caricaLayoutStampe(Integer idEntita) {
        List<LayoutStampaDocumento> layouts = layoutStampeBD.caricaLayoutStampe(idEntita);
        // aggiorno le stampanti configurate nel file dell'utente
        applyPrinterAssociation(layouts);
        return layouts;
    }

    @Override
    public List<LayoutStampaDocumento> caricaLayoutStampe(ITipoAreaDocumento tipoArea, EntitaLite entita,
            SedeEntita sedeEntita) {
        List<LayoutStampaDocumento> layouts = layoutStampeBD.caricaLayoutStampe(tipoArea, entita, sedeEntita);
        applyPrinterAssociation(layouts);
        return layouts;
    }

    @Override
    public LayoutStampa caricaLayoutStampe(String reportPath) {
        List<LayoutStampa> layouts = new ArrayList<LayoutStampa>();
        LayoutStampa layout = layoutStampeBD.caricaLayoutStampe(reportPath);
        if (layout != null) {
            layouts.add(layout);
            applyPrinterAssociation(layouts);
            layout = layouts.get(0);
        }
        return layout;
    }

    @Override
    public LayoutStampaDocumento getLayoutStampaPredefinito(List<LayoutStampaDocumento> layouts,
            TipoLayoutPrefefinito tipoLayoutPrefefinito) {
        LayoutStampaDocumento layoutPredefinito = null;

        if (layouts != null && !layouts.isEmpty()) {
            // sicome l'ordinamento è per sede entità, entita e predefinito sia che si voglia il layout predefinito per
            // la mail o stampa posso prendere il primo
            switch (tipoLayoutPrefefinito) {
            case MAIL:
                for (LayoutStampaDocumento layoutStampaDocumento : layouts) {
                    if (layoutStampaDocumento.isMailLayout()) {
                        layoutPredefinito = layoutStampaDocumento;
                        break;
                    }
                }
                break;
            case INTERNO:
                for (LayoutStampaDocumento layoutStampaDocumento : layouts) {
                    if (layoutStampaDocumento.isInterno()) {
                        layoutPredefinito = layoutStampaDocumento;
                        break;
                    }
                }
                break;
            default:
                layoutPredefinito = null;
                break;
            }

            // se il layout è nullo carico quello predefinito ( vuol dire che o non voglio quello per la mail o interno
            // oppure che
            // voglio il layout per la mail (o interno) ma non ne è stato impostato uno )
            if (layoutPredefinito == null) {
                LayoutStampaDocumento layout = layouts.get(0);
                layoutPredefinito = (layout.getSedeEntita() != null || layout.getEntita() != null
                        || (layout.getEntita() == null && layout.getPredefinito())) ? layout : null;
            }
        }
        return layoutPredefinito;
    }

    /**
     * @return Returns the layoutStampeBD.
     */
    public ILayoutStampeBD getLayoutStampeBD() {
        return layoutStampeBD;
    }

    /**
     * Restituisce il file di configurazione delle stampanti per l'utente.
     *
     * @return file caricato
     */
    private File getPrinterConfigFile() {
        return PanjeaSwingUtil.getHome().resolve(PRINTER_CONFIG_FILE_NAME).toFile();
    }

    @Override
    public Properties loadPrinterConfigFile() {

        File userProperties = getPrinterConfigFile();

        Properties properties = new Properties();
        try {
            if (!userProperties.exists()) {
                userProperties.createNewFile();
            }
            properties.load(new FileInputStream(userProperties));
        } catch (IOException e) {
            logger.error("--> Errore durante la creazione del file " + userProperties.getName(), e);
        }

        return properties;
    }

    @Override
    public void salvaAssociazioneStampante(String nomeLayout, String nomeStampante) {
        logger.debug("--> Enter salvaAssociazioneStampante");

        if (nomeStampante == null) {
            return;
        }
        Properties properties = loadPrinterConfigFile();
        properties.setProperty(nomeLayout, nomeStampante);
        try {
            properties.store(new FileOutputStream(getPrinterConfigFile()), null);
        } catch (Exception e) {
            logger.error("--> errore durante il salvataggio delle stampanti utente", e);
        }

        logger.debug("--> Exit salvaAssociazioneStampante");
    }

    @Override
    public List<LayoutStampaDocumento> setLayoutAsDefault(LayoutStampaDocumento layoutStampa) {
        List<LayoutStampaDocumento> layouts = layoutStampeBD.setLayoutAsDefault(layoutStampa);

        applyPrinterAssociation(layouts);

        return layouts;
    }

    @Override
    public List<LayoutStampaDocumento> setLayoutForInvioMail(LayoutStampaDocumento layoutStampa) {
        List<LayoutStampaDocumento> layouts = layoutStampeBD.setLayoutForInvioMail(layoutStampa);

        applyPrinterAssociation(layouts);

        return layouts;
    }

    @Override
    public List<LayoutStampaDocumento> setLayoutForUsoInterno(LayoutStampaDocumento layoutStampa, boolean usoInterno) {
        List<LayoutStampaDocumento> layouts = layoutStampeBD.setLayoutForUsoInterno(layoutStampa, usoInterno);

        applyPrinterAssociation(layouts);

        return layouts;
    }

    /**
     * @param layoutStampeBD
     *            The layoutStampeBD to set.
     */
    public void setLayoutStampeBD(ILayoutStampeBD layoutStampeBD) {
        this.layoutStampeBD = layoutStampeBD;
    }

}
