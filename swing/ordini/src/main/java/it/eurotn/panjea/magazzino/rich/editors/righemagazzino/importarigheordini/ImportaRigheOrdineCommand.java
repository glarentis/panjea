package it.eurotn.panjea.magazzino.rich.editors.righemagazzino.importarigheordini;

import java.awt.Dimension;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.lotti.exception.EvasioneLottiException;
import it.eurotn.panjea.lotti.exception.LottiException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

public class ImportaRigheOrdineCommand extends ActionCommand {

    private static final String COMMAND_ID = "importaRigheOrdineCommand";
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;
    private IOrdiniDocumentoBD ordiniDocumentoBD;
    private SettingsManager settingsManager;

    /**
     * Costruttore.
     *
     */
    public ImportaRigheOrdineCommand() {
        super(COMMAND_ID);
        this.setSecurityControllerId(COMMAND_ID + ".controller");
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        final ImportaRighePage page = new ImportaRighePage(ordiniDocumentoBD);
        final AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) getParameter("areaMagazzinoFullDTO");
        page.onPrePageOpen();
        page.setFormObject(areaMagazzinoFullDTO);
        page.loadData();
        page.restoreState(getSettings());

        PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(page) {

            @Override
            protected boolean isMessagePaneVisible() {
                return false;
            }

            @Override
            protected boolean onFinish() {
                evadiOrdini(page.getRigheDaEvadere(), areaMagazzinoFullDTO.getAreaMagazzino());
                return true;
            }

        };
        dialog.setPreferredSize(new Dimension(1224, 600));
        dialog.showDialog();
        page.saveState(getSettings());
        page.dispose();
    }

    /**
     *
     * @param righeEvasione
     *            .
     * @param documentoEvasione
     *            .
     */
    private void evadiOrdini(List<RigaDistintaCarico> righeEvasione, AreaMagazzino documentoEvasione) {

        boolean success = false;
        while (!success) {
            try {
                ordiniDocumentoBD.evadiOrdini(righeEvasione, documentoEvasione);
                success = true;
            } catch (EvasioneLottiException exception) {
                for (Entry<RigaDistintaCarico, LottiException> eccezione : exception.getEccezioni().entrySet()) {
                    AssegnaLottiDialog dialog = new AssegnaLottiDialog(eccezione.getKey(), eccezione.getValue(),
                            magazzinoDocumentoBD, documentoEvasione);
                    dialog.showDialog();
                    switch (dialog.getResult()) {
                    case 0:
                        int numberRow = righeEvasione.indexOf(eccezione.getKey());
                        righeEvasione.set(numberRow, dialog.getRigaDistintaCarico());
                        break;
                    case 1:
                        righeEvasione.remove(eccezione.getKey());
                        break;
                    default:
                        success = true;
                        break;
                    }
                    if (success) {
                        break;
                    }
                    dialog = null;
                }
            }
        }

    }

    /**
     * @return Returns the magazzinoDocumentoBD.
     */
    public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
        return magazzinoDocumentoBD;
    }

    /**
     * @return Returns the ordineDocumentoBD.
     */
    public IOrdiniDocumentoBD getOrdineDocumentoBD() {
        return ordiniDocumentoBD;
    }

    /**
     * @return settings locali
     */
    private Settings getSettings() {
        try {
            org.springframework.util.Assert.notNull(getSettingsManager(),
                    "Settings manager deve essere iniettato da xml nel panjea-pages-context.xml");
            Settings settings = getSettingsManager().getUserSettings();
            org.springframework.util.Assert.notNull(settings, "UserSettings è null");
            return settings;
        } catch (SettingsException e) {
            logger.error("--> Errore nell'accesso ai settings", e);
        }
        return null;
    }

    /**
     * @return Returns the settingsManager.
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    /**
     * @param ordiniDocumentoBD
     *            The ordineDocumentoBD to set.
     */
    public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
        this.ordiniDocumentoBD = ordiniDocumentoBD;
    }

    /**
     * @param settingsManager
     *            The settingsManager to set.
     */
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }
}