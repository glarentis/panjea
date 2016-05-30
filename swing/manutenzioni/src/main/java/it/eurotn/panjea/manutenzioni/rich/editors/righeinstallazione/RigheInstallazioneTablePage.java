package it.eurotn.panjea.manutenzioni.rich.editors.righeinstallazione;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.log4j.Logger;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

public class RigheInstallazioneTablePage extends AbstractTablePageEditor<RigaInstallazione> {

    public static final String PAGE_ID = "righeInstallazioneTablePage";

    private static final Logger LOGGER = Logger.getLogger(RigheInstallazioneTablePage.class);

    private IManutenzioniBD manutenzioniBD = null;

    private AreaInstallazione areaInstallazione;

    private boolean areaMagazzinoPresente = false;

    /**
     * Costruttore di default.
     */
    public RigheInstallazioneTablePage() {
        super(PAGE_ID, new RigheInstallazioneTableModel());
    }

    @Override
    protected EditFrame<RigaInstallazione> createEditFrame() {
        return new RigheInstallazioneEditFrame(editPageMode, this, EditFrame.QUICK_ACTION_DEFAULT);
    }

    @Override
    public Collection<RigaInstallazione> loadTableData() {
        if (areaInstallazione.isNew()) {
            return new ArrayList<>();
        }
        return manutenzioniBD.caricaRigheInstallazioneByAreaInstallazione(areaInstallazione.getId());
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void processTableData(Collection<RigaInstallazione> results) {
        if (areaMagazzinoPresente) {
            CollectionUtils.filter(results, new Predicate<RigaInstallazione>() {

                @Override
                public boolean evaluate(RigaInstallazione riga) {
                    return riga.getTipoMovimento() != null;
                }
            });
        }
        super.processTableData(results);
    }

    @Override
    public Collection<RigaInstallazione> refreshTableData() {
        if (areaInstallazione != null) {
            try {
                return loadTableData();
            } catch (Exception ex) {
                // Sulla cancellazione viene lanciato il refreshData con l'area già cancellata.
                // Salto l'objectnotfound
                if (LOGGER.isDebugEnabled()) {
                    if (ex.getCause().getCause() instanceof ObjectNotFoundException) {
                        LOGGER.debug("--> Area Installazione cancellata");
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param areaMagazzinoPresente
     *            the areaMagazzinoPresente to set
     */
    public void setAreaMagazzinoPresente(boolean areaMagazzinoPresente) {
        this.areaMagazzinoPresente = areaMagazzinoPresente;
        refreshData();
    }

    @Override
    public void setFormObject(Object object) {
        areaInstallazione = (AreaInstallazione) object;
        areaMagazzinoPresente = areaInstallazione.getIdAreaMagazzino() != null;
    }

    /**
     * @param manutenzioniBD
     *            the manutenzioniBD to set
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

    @Override
    public void update(Observable observable, Object obj) {
        super.update(observable, obj);
        ((RigaInstallazionePage) getEditFrame().getCurrentEditPage()).setAreaInstallazione(areaInstallazione);
    }

}
