package it.eurotn.panjea.contabilita.rich.editors.areacontabile;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.rich.commands.documento.DocumentiDocumentoCommand;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.rich.editors.righecontabili.RigheContabiliTablePage;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.iva.rich.editors.righeiva.RigaIvaPage;
import it.eurotn.panjea.iva.rich.editors.righeiva.RigheIvaTablePage;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rate.rich.editors.rate.RateTablePage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;

public class AreaContabileEditorController {

    /**
     * PropertyChange chiamato quando la testata (area contabile) viene validata, in questo caso quando viene salvata.
     */
    private class AreaContabileValidataChangeListener implements PropertyChangeListener {

        @SuppressWarnings("rawtypes")
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (AreaContabilePage.VALIDA_AREA_CONTABILE.equals(evt.getPropertyName())) {
                if (righeIvaTablePage.getAreaIvaModel().isAreaIvaPresente()) {
                    if (righeIvaTablePage.getAreaIvaModel().getAreaDocumento().getVersion() == 0) {
                        compositePage.setActivePage(righeIvaTablePage);
                        for (AbstractCommand command : righeIvaTablePage.getNewCommands().values()) {
                            if (((it.eurotn.rich.editors.AbstractTablePageEditor.NewCommand) command).getPage()
                                    .getPageEditorId().equals(RigaIvaPage.PAGE_ID)) {
                                command.execute();
                                break;
                            }
                        }
                    }
                } else {
                    if (righeIvaTablePage.getAreaIvaModel().getAreaDocumento().getVersion() == 0) {
                        righeContabiliTablePage.creaRigheContabili();
                    }
                }
            }
        }
    }

    /**
     * PropertyChange chiamato quando la parte iva viene validata.
     */
    private class AreaIvaValidataChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (RigheIvaTablePage.VALIDA_AREA_IVA.equals(evt.getPropertyName())) {
                compositePage.setActivePage(righeContabiliTablePage);
                righeContabiliTablePage.creaRigheContabili();
            }
        }
    }

    /**
     * PropertyChange chiamato quando la parte partita viene validata.
     */
    private class AreaPartitaValidataChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (VALIDA_AREA_RATE.equals(evt.getPropertyName())) {

                presentaIntra();

                compositePage.setActivePage(areaContabilePage);
                areaContabilePage.getNewCommand().execute();
            }
        }
    }

    public class RigheContabiliValidataChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (RigheContabiliTablePage.VALIDA_RIGHE_CONTABILI.equals(evt.getPropertyName())) {

                // se il tipo documento prevede la richiesta del documento collegato e la sto inserendo e non
                // modificando apro il dialogo per laq selezione
                AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) areaContabilePage.getForm()
                        .getFormObject();
                boolean richiestaDocCollegato = areaContabileFullDTO.getAreaContabile().getTipoAreaContabile()
                        .getTipoDocumento().isRichiediDocumentoCollegato();
                Integer versioneAC = areaContabileFullDTO.getAreaContabile().getVersion();
                if (richiestaDocCollegato && versioneAC != null && versioneAC == 1) {
                    DocumentiDocumentoCommand command = new DocumentiDocumentoCommand();
                    command.setDocumento(areaContabileFullDTO.getAreaContabile().getDocumento());
                    command.execute();
                }

                if (areaPartitePage.getAreaRateModel().isAreaRatePresente()) {
                    compositePage.setActivePage(areaPartitePage);
                } else {

                    presentaIntra();

                    compositePage.setActivePage(areaContabilePage);
                    areaContabilePage.getNewCommand().execute();
                }
            } else if (RateTablePage.VALIDA_AREA_RATE.equals(evt.getPropertyName())) {
                // in caso stia registrando un'area contabile con importo a 0, confermando le righe mi viene validata
                // anche
                // l'area rate quindi sto in attesa anche di questo evento.
                areaPartitaValidaChangeListener.propertyChange(evt);
            }
        }
    }

    private static final Logger LOGGER = Logger.getLogger(AreaContabileEditorController.class);

    // L'area partite potre non averla nel classPath. Devo ridefinirmi le
    // costanti per non avere una classNotFound
    private static final String RATE_PAGE_ID = "rateTablePage";
    private static final String VALIDA_AREA_RATE = "validaAreaRate";

    private RigheIvaTablePage righeIvaTablePage = null;
    private RigheContabiliTablePage righeContabiliTablePage = null;
    private AreaContabilePage areaContabilePage = null;
    private RateTablePage areaPartitePage = null;

    private PropertyChangeListener areaIvaValidaChangeListener = null;
    private PropertyChangeListener areaContabileValidaChangeListener = null;
    private PropertyChangeListener areaPartitaValidaChangeListener = null;
    private PropertyChangeListener righeContabiliValidaChangeListener = null;

    private final JecCompositeDialogPage compositePage;

    /**
     * Costruttore.
     *
     * @param compositePage
     *            compositePage
     */
    public AreaContabileEditorController(final JecCompositeDialogPage compositePage) {
        areaIvaValidaChangeListener = new AreaIvaValidataChangeListener();
        areaContabileValidaChangeListener = new AreaContabileValidataChangeListener();
        areaPartitaValidaChangeListener = new AreaPartitaValidataChangeListener();
        righeContabiliValidaChangeListener = new RigheContabiliValidataChangeListener();
        this.compositePage = compositePage;
    }

    /**
     * Aggiunge una pagina.
     *
     * @param page
     *            pagina
     */
    public void addPage(DialogPage page) {
        LOGGER.debug("--> Enter addPage");
        if (AreaContabilePage.PAGE_ID.equals(page.getId())) {
            areaContabilePage = (AreaContabilePage) page;
            areaContabilePage.addPropertyChangeListener(AreaContabilePage.VALIDA_AREA_CONTABILE,
                    areaContabileValidaChangeListener);

        } else if (RigheContabiliTablePage.PAGE_ID.equals(page.getId())) {
            righeContabiliTablePage = (RigheContabiliTablePage) page;
            righeContabiliTablePage.addPropertyChangeListener(RigheContabiliTablePage.VALIDA_RIGHE_CONTABILI,
                    righeContabiliValidaChangeListener);
            righeContabiliTablePage.addPropertyChangeListener(RateTablePage.VALIDA_AREA_RATE,
                    righeContabiliValidaChangeListener);
        } else if (RigheIvaTablePage.PAGE_ID.equals(page.getId())) {
            righeIvaTablePage = (RigheIvaTablePage) page;
            righeIvaTablePage.addPropertyChangeListener(RigheIvaTablePage.VALIDA_AREA_IVA, areaIvaValidaChangeListener);
        } else if (RATE_PAGE_ID.equals(page.getId())) {
            areaPartitePage = (RateTablePage) page;
            areaPartitePage.addPropertyChangeListener(VALIDA_AREA_RATE, areaPartitaValidaChangeListener);
        }
        LOGGER.debug("--> Exit addPage");
    }

    /**
     * Se il plugin intra è abilitato e il documento prevede la gestione intra, viene proposto all'utente il dialogo di
     * gestione intra.
     */
    private void presentaIntra() {
        PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
        if (pluginManager.isPresente(PluginManager.PLUGIN_INTRA)) {
            AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) areaContabilePage.getForm()
                    .getFormObject();
            AziendaCorrente azienda = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
            if (areaContabileFullDTO.getAreaContabile().getDocumento()
                    .isAreaIntraAbilitata(azienda.getNazione().getCodice())) {
                ActionCommand intraCommand = (ActionCommand) Application.instance().getActiveWindow()
                        .getCommandManager().getCommand("areaIntraCommand");
                intraCommand.addParameter("documento", areaContabileFullDTO.getAreaContabile().getDocumento());
                intraCommand.execute();
            }
        }
    }
}
