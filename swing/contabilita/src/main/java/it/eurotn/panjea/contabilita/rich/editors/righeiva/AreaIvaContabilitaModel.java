/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.righeiva;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.CodiceIvaPrevalente;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.TipologiaCorrispettivo;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.rich.editors.righeiva.factory.StrategiaQuadraAreaIva;
import it.eurotn.panjea.iva.rich.editors.righeiva.factory.StrategiaQuadraAreaIvaFactory;
import it.eurotn.panjea.iva.rich.forms.righeiva.AbstractAreaIvaModel;

/**
 * Model di areaIva, presenta metodi e proprieta' utili per gestire l'area iva, le sue righe e il suo ciclo rispetto
 * alle altre aree.
 *
 * @author Leonardo
 */
public class AreaIvaContabilitaModel extends AbstractAreaIvaModel {

    private static Logger logger = Logger.getLogger(AreaIvaContabilitaModel.class);

    private IContabilitaBD contabilitaBD = null;

    private IContabilitaAnagraficaBD contabilitaAnagraficaBD = null;

    private AreaContabileFullDTO areaContabileFullDTO = null;

    /**
     *
     * Costruttore.
     */
    public AreaIvaContabilitaModel() {
        super();
    }

    @Override
    public Object caricaAreaDocumentoFullDTO(Integer id) {
        logger.debug("--> Enter caricaAreaDocumentoFullDTO");
        AreaContabileFullDTO areaContabileFullDTOCaricata = contabilitaBD.caricaAreaContabileFullDTO(id);
        logger.debug("--> Exit caricaAreaDocumentoFullDTO");
        return areaContabileFullDTOCaricata;
    }

    @Override
    public RigaIva creaNuovaRiga() {
        logger.debug("--> Enter createNewObject");
        RigaIva rigaIva = super.creaNuovaRiga();
        // inizializzazione di cambio e valuta
        AreaContabile areaContabile = (AreaContabile) getAreaDocumento();

        TipologiaCorrispettivo tipologiaCorrispettivo = areaContabile.getTipoAreaContabile()
                .getTipologiaCorrispettivo();

        if (tipologiaCorrispettivo != null
                && tipologiaCorrispettivo.compareTo(TipologiaCorrispettivo.DA_VENTILARE) == 0) {
            ContabilitaSettings contabilitaSettings = contabilitaAnagraficaBD.caricaContabilitaSettings();
            CodiceIva codiceIva = contabilitaSettings.getCodiceIvaPerVentilazione();
            rigaIva.setCodiceIva(codiceIva);
        }

        if (areaContabileFullDTO != null && (areaContabileFullDTO.getAreaIva().getRigheIva() == null
                || areaContabileFullDTO.getAreaIva().getRigheIva().isEmpty())) {
            if (rigaIva.getCodiceIva() == null) {
                EntitaLite entitaLite = areaContabile.getDocumento().getEntita();

                CodiceIvaPrevalente codiceIvaPrevalente = null;
                if (entitaLite != null && entitaLite.getId() != null && entitaLite.getId() != -1) {
                    // carico il codice iva del tipo documento ed entita
                    codiceIvaPrevalente = contabilitaAnagraficaBD
                            .caricaCodiceIvaPrevalente(areaContabile.getTipoAreaContabile(), entitaLite);
                }

                // se non esiste carico quello di default del tipo documento
                if (codiceIvaPrevalente == null) {
                    codiceIvaPrevalente = contabilitaAnagraficaBD
                            .caricaCodiceIvaPrevalente(areaContabile.getTipoAreaContabile(), null);
                }

                if (codiceIvaPrevalente != null) {
                    Importo imponibile = areaContabile.getDocumento().getTotale();
                    imponibile = imponibile.multiply(Importo.HUNDRED, RigaIva.SCALE_FISCALE).divide(
                            codiceIvaPrevalente.getCodiceIva().getPercApplicazione().add(Importo.HUNDRED),
                            RigaIva.SCALE_FISCALE, BigDecimal.ROUND_HALF_UP);
                    rigaIva.setImponibileVisualizzato(imponibile);
                    rigaIva.setCodiceIva(codiceIvaPrevalente.getCodiceIva());
                    rigaIva.calcolaImposta();
                }
            }
        }
        return rigaIva;

    }

    @Override
    protected void doDeleteRigaIva(RigaIva rigaIva) {
        contabilitaBD.cancellaRigaIva(rigaIva);
    }

    @Override
    protected RigaIva doSaveRigaIva(RigaIva rigaIva) throws CodiceIvaCollegatoAssenteException {
        TipoAreaContabile tipoAreaContabile = areaContabileFullDTO.getAreaIva().getAreaContabile()
                .getTipoAreaContabile();
        rigaIva.getAreaIva().setAreaContabile(areaContabileFullDTO.getAreaContabile());
        RigaIva rigaIvaSalvata = contabilitaBD.salvaRigaIva(rigaIva, tipoAreaContabile);
        return rigaIvaSalvata;
    }

    @Override
    public IAreaDocumento getAreaDocumento() {
        if (areaContabileFullDTO == null) {
            return new AreaContabile();
        }
        return areaContabileFullDTO.getAreaContabile();
    }

    @Override
    public AreaIva getAreaIva() {
        if (areaContabileFullDTO == null || areaContabileFullDTO.getAreaIva() == null) {
            return new AreaIva();
        }
        return areaContabileFullDTO.getAreaIva();
    }

    @Override
    public BigDecimal getImportoSquadrato() {

        GestioneIva gestioneIva = null;
        // se la parte iva non e' attiva allora gestione iva rimane null
        if (isAreaIvaPresente()
                && this.areaContabileFullDTO.getAreaIva().getDocumento().getTipoDocumento().isRigheIvaEnable()) {
            gestioneIva = this.areaContabileFullDTO.getAreaIva().getAreaContabile().getTipoAreaContabile()
                    .getGestioneIva();
        }
        // factory che recupera il corretto quadratore di area iva a seconda della gestione iva
        StrategiaQuadraAreaIva quadraAreaIva = StrategiaQuadraAreaIvaFactory.getQuadratoreAreaIva(gestioneIva);

        return quadraAreaIva.getImportoSquadrato(getAreaIva());
    }

    @Override
    public Object getObject() {
        return null;
    }

    /**
     * Verifica se le righe iva sono quadrate rispetto al totale documento verificando i diversi casi. (gestione iva
     * intra o normale)
     *
     * @return true o false
     */
    @Override
    public boolean isAreaIvaQuadrata() {
        GestioneIva gestioneIva = null;
        // se la parte iva non e' attiva allora gestione iva rimane null
        if (isAreaIvaPresente()
                && this.areaContabileFullDTO.getAreaIva().getDocumento().getTipoDocumento().isRigheIvaEnable()) {
            gestioneIva = this.areaContabileFullDTO.getAreaIva().getAreaContabile().getTipoAreaContabile()
                    .getGestioneIva();
        }
        // factory che recupera il corretto quadratore di area iva a seconda della gestione iva
        StrategiaQuadraAreaIva quadraAreaIva = StrategiaQuadraAreaIvaFactory.getQuadratoreAreaIva(gestioneIva);
        // richiede se quadrata
        boolean isAreaIvaQuadrata = quadraAreaIva.isQuadrata(areaContabileFullDTO.getAreaIva());
        return isAreaIvaQuadrata;
    }

    /**
     * Verifica se e' cambiato lo stato dell'areaContabile da confermato a provvisorio o se la parte iva passa da stato
     * verificato a invalido.
     *
     * @return true or false
     */
    @Override
    public boolean isChanged() {
        logger.debug("--> Enter isChanged");
        boolean changed = true;
        AreaContabile areaContabile = (AreaContabile) getAreaDocumento();
        if ((areaContabile.getStatoAreaContabile() == StatoAreaContabile.PROVVISORIO
                || areaContabile.getStatoAreaContabile() == StatoAreaContabile.SIMULATO) && !isRigheIvaValide()) {
            changed = false;
        }
        logger.debug("--> Exit isChanged con valore " + changed);
        return changed;
    }

    @Override
    public boolean isEnabled() {
        logger.debug("--> Enter isEnabled");
        boolean enabled = true;
        // disable se isRigheIvaEnable = false e se area documento è nuova
        if (getAreaDocumento() == null || (getAreaDocumento() != null
                && !getAreaDocumento().getDocumento().getTipoDocumento().isRigheIvaEnable())) {
            enabled = false;
        }
        AreaContabile areaContabile = (AreaContabile) getAreaDocumento();
        if (AreaContabile.StatoAreaContabile.VERIFICATO.equals(areaContabile.getStatoAreaContabile())) {
            enabled = false;
        }
        logger.debug("--> Exit isEnabled");
        return enabled;
    }

    @Override
    public boolean isIntraAbilitato() {
        return this.areaContabileFullDTO != null && this.areaContabileFullDTO.getAreaIva() != null
                && this.areaContabileFullDTO.getAreaIva().getAreaContabile().getTipoAreaContabile() != null
                && !this.areaContabileFullDTO.getAreaIva().getAreaContabile().getTipoAreaContabile().getGestioneIva()
                        .equals(GestioneIva.NORMALE);
    }

    /**
     * Verifica se l'areaIva e' legata a un tipo documento con validazione area iva automatica.
     *
     * @return true se la validazione e' automatica false altrimenti
     */
    @Override
    public boolean isValidazioneAreaIvaAutomatica() {
        return isAreaIvaPresente() && this.areaContabileFullDTO.getAreaIva().getAreaContabile().getTipoAreaContabile()
                .isValidazioneAreaIvaAutomatica();
    }

    @Override
    protected void reloadAreaIva() {
        AreaIva areaIva = contabilitaBD.caricaAreaIva(areaContabileFullDTO.getAreaIva());
        areaContabileFullDTO.setAreaIva(areaIva);
    }

    /**
     * @param contabilitaAnagraficaBD
     *            The contabilitaAnagraficaBD to set.
     */
    public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    /**
     * @param contabilitaBD
     *            the contabilitaBD to set
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    @Override
    public void setObject(Object object) {
        areaContabileFullDTO = ((AreaContabileFullDTO) object);
    }

    /**
     * Valida l'areaIva restituendo l'areaIvaModel aggiornata.
     */
    @Override
    public void validaAreaIva() {
        setObject(contabilitaBD.validaAreaIva(areaContabileFullDTO.getAreaIva(), getAreaDocumento().getId()));
        firePropertyChange(AREA_MODEL_AGGIORNATA, null, areaContabileFullDTO);
    }
}
