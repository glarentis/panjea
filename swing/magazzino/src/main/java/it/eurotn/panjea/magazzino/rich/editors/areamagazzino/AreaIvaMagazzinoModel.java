/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.rich.editors.righeiva.factory.StrategiaQuadraAreaIva;
import it.eurotn.panjea.iva.rich.editors.righeiva.factory.StrategiaQuadraAreaIvaFactory;
import it.eurotn.panjea.iva.rich.forms.righeiva.AbstractAreaIvaModel;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;

/**
 * Model di {@link AreaIva} per il modulo di magazzino.
 *
 * @author adriano
 * @version 1.0, 06/ott/2008
 */
public class AreaIvaMagazzinoModel extends AbstractAreaIvaModel {

    private static Logger logger = Logger.getLogger(AreaIvaMagazzinoModel.class);

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private AreaMagazzinoFullDTO areaMagazzinoFullDTO;

    public AreaIvaMagazzinoModel() {
        areaMagazzinoFullDTO = new AreaMagazzinoFullDTO();
    }

    @Override
    public Object caricaAreaDocumentoFullDTO(Integer id) {
        AreaMagazzino areaMagazzinoToLoad = new AreaMagazzino();
        areaMagazzinoToLoad.setId(id);
        areaMagazzinoToLoad.setVersion(0);
        AreaMagazzinoFullDTO result = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzinoToLoad);
        return result;
    }

    @Override
    protected void doDeleteRigaIva(RigaIva rigaIva) {
        magazzinoDocumentoBD.cancellaRigaIva(rigaIva);
    }

    @Override
    protected RigaIva doSaveRigaIva(RigaIva rigaIva) throws CodiceIvaCollegatoAssenteException {
        RigaIva rigaIvaSalvata = magazzinoDocumentoBD.salvaRigaIva(rigaIva, null);
        return rigaIvaSalvata;
    }

    @Override
    public IAreaDocumento getAreaDocumento() {
        return areaMagazzinoFullDTO.getAreaMagazzino();
    }

    @Override
    public AreaIva getAreaIva() {
        if (areaMagazzinoFullDTO == null || areaMagazzinoFullDTO.getAreaIva() == null) {
            return new AreaIva();
        }
        return areaMagazzinoFullDTO.getAreaIva();
    }

    @Override
    public BigDecimal getImportoSquadrato() {
        GestioneIva gestioneIva = null;
        // Dal magazzino posso solamente create area iva NORMALE
        gestioneIva = GestioneIva.NORMALE;
        // factory che recupera il corretto validatore di area iva a seconda
        // della gestione iva
        StrategiaQuadraAreaIva validaAreaIva = StrategiaQuadraAreaIvaFactory.getQuadratoreAreaIva(gestioneIva);

        return validaAreaIva.getImportoSquadrato(getAreaIva());
    }

    @Override
    public Object getObject() {
        return areaMagazzinoFullDTO;
    }

    @Override
    public boolean isAreaIvaQuadrata() {
        GestioneIva gestioneIva = null;
        // Dal magazzino posso solamente create area iva NORMALE
        gestioneIva = GestioneIva.NORMALE;
        // factory che recupera il corretto validatore di area iva a seconda
        // della gestione iva
        StrategiaQuadraAreaIva validaAreaIva = StrategiaQuadraAreaIvaFactory.getQuadratoreAreaIva(gestioneIva);
        // richiede se valido
        boolean isAreaIvaValidata = validaAreaIva.isQuadrata(getAreaIva());
        return isAreaIvaValidata;
    }

    @Override
    public boolean isChanged() {
        logger.debug("--> Enter isChanged");
        org.springframework.util.Assert.isInstanceOf(AreaMagazzino.class, this.getAreaDocumento(),
                "area documento non è istanza di area magazzino");
        AreaMagazzino areaMagazzino = (AreaMagazzino) this.getAreaDocumento();
        if (areaMagazzino.getStatoAreaMagazzino() == StatoAreaMagazzino.PROVVISORIO && !isRigheIvaValide()) {
            // Sicuramente non è cambiato lo stato. Non ricarico nulla
            logger.debug("--> Exit isChanged " + false);
            return false;
        } else {
            // è cambiato uno dei due stati, quindi devo ricaricare il fullDto
            logger.debug("--> Exit isChanged " + true);
            return true;
        }
    }

    /*
     * In magazzino controllo se ho un'area contabile attiva. In questo caso disabilito tutto. <br>
     * Disabilito anche se le righe magazzino non sono validate
     */
    @Override
    public boolean isEnabled() {
        logger.debug("--> Enter isEnabled");
        boolean enabled = true;
        AreaMagazzino areaMagazzino = (AreaMagazzino) getAreaDocumento();
        AreaContabileLite areaContabileLite = areaMagazzinoFullDTO.getAreaContabileLite();
        if (getAreaDocumento() == null || (!getAreaDocumento().getDocumento().getTipoDocumento().isRigheIvaEnable())
                || (areaContabileLite != null) || !areaMagazzinoFullDTO.getAreaMagazzino().isCarico()
                || !areaMagazzino.getDatiValidazioneRighe().isValid() || areaMagazzinoFullDTO.getAreaMagazzino()
                        .getStatoAreaMagazzino() == StatoAreaMagazzino.INFATTURAZIONE) {
            enabled = false;
        }
        logger.debug("--> Exit isEnabled");
        return enabled;
    }

    @Override
    public boolean isIntraAbilitato() {
        return false;
    }

    /**
     * Oltre al normale controllo per la abilitare la validazione dell'area iva devo controllare se
     * ho un'area contabile associata. Se la trovo disabilito la validazione.
     *
     * @return <true> se le righe sono valide
     */
    @Override
    public boolean isRigheIvaValide() {
        if (areaMagazzinoFullDTO.getAreaContabileLite() != null) {
            return true;
        } else {
            return super.isRigheIvaValide();
        }
    }

    @Override
    public boolean isValidazioneAreaIvaAutomatica() {
        // Questo metodo viene utilizzato se inserisco l'iva riga per riga.
        // Quando dal documento creo l'iva tramite la conferma delle righe
        // magazzino
        // l'iva viene creata nella confermaRigheMagazzino
        AreaMagazzino areaMagazzino = (AreaMagazzino) getAreaDocumento();
        return areaMagazzino.getTipoAreaMagazzino().getTipoMovimento() != TipoMovimento.CARICO;
    }

    @Override
    protected void reloadAreaIva() {
        areaMagazzinoFullDTO.setAreaIva(magazzinoDocumentoBD.caricaAreaIva(getAreaIva()));
    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    @Override
    public void setObject(Object object) {
        areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) object;
    }

    @Override
    public void validaAreaIva() {
        setObject(magazzinoDocumentoBD.validaRigheIva(getAreaIva(), getAreaDocumento().getId()));
        firePropertyChange(AbstractAreaIvaModel.AREA_MODEL_AGGIORNATA, null, areaMagazzinoFullDTO);
    }
}
