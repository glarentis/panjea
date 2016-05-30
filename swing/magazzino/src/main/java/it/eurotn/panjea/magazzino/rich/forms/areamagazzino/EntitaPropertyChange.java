/**
 *
 */
package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.ClientePotenziale;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClientePotenzialeLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;
import it.eurotn.rich.form.PanjeaFormModel;

/**
 * PropertyChange incaricato di gestire le azioni successive alla variazione di {@link EntitaLite} dalla parte di
 * {@link AreaMagazzino}.
 *
 * @author adriano
 * @version 1.0, 08/set/2008
 *
 */
public class EntitaPropertyChange implements FormModelPropertyChangeListeners {

    private static Logger logger = Logger.getLogger(EntitaPropertyChange.class);

    private FormModel formModel;
    private IAnagraficaBD anagraficaBD;
    private IAnagraficaTabelleBD anagraficaTabelleBD;
    private String areaDocumentoPropertyPath;

    /**
     * Assegna il contratto se ne esiste uno, altrimenti ne prepara uno vuoto per il form.
     *
     * @param entitaLite
     *            l'entità di cui cercare il contratto
     */
    private void assegnaContratto(EntitaLite entitaLite) {
        Boolean gestioneContratto = (Boolean) formModel
                .getValueModel(getAreaDocumentoPropertyPath() + ".documento.tipoDocumento.gestioneContratto")
                .getValue();
        if (gestioneContratto == Boolean.TRUE) {
            List<ContrattoSpesometro> contratti = anagraficaTabelleBD.caricaContratti(entitaLite);
            ContrattoSpesometro contratto = null;
            if (contratti.size() == 1) {
                contratto = contratti.get(0);
            }
            formModel.getValueModel(getAreaDocumentoPropertyPath() + ".documento.contrattoSpesometro")
                    .setValue(contratto);
        }
    }

    /**
     * Assegna la sede di magazzino di default per l'argomento entita.
     *
     * @param entita
     *            entità alla quale assegnare la sede
     */
    private void assegnaSedeEntita(EntitaLite entita) {
        logger.debug("--> Enter assegnaSedeEntita");
        SedeEntita sedeEntita = getSedeEntitaPrincipale(entita);
        setProperty(getAreaDocumentoPropertyPath() + ".documento.sedeEntita", sedeEntita);
        logger.debug("--> Exit assegnaSedeEntita");
    }

    /**
     * Pulisce tutti i valori delle proprietà impostate da un eventuale sede precedente.
     */
    private void clearData() {
        setProperty(getAreaDocumentoPropertyPath() + ".documento.sedeEntita", null);
        setProperty(getAreaDocumentoPropertyPath() + ".listinoAlternativo", null);
        setProperty(getAreaDocumentoPropertyPath() + ".listino", null);
        setProperty(getAreaDocumentoPropertyPath() + ".vettore", null);
        setProperty(getAreaDocumentoPropertyPath() + ".causaleTrasporto", null);
        setProperty(getAreaDocumentoPropertyPath() + ".trasportoCura", null);
        setProperty(getAreaDocumentoPropertyPath() + ".tipoPorto", null);
        setProperty(getAreaDocumentoPropertyPath() + ".addebitoSpeseIncasso", false);
        setProperty(getAreaDocumentoPropertyPath() + ".inserimentoBloccato", false);
        setProperty(getAreaDocumentoPropertyPath() + ".raggruppamentoBolle", false);
        setProperty(getAreaDocumentoPropertyPath() + ".aspettoEsteriore", null);
        setProperty(getAreaDocumentoPropertyPath() + ".stampaPrezzi", true);
        setProperty(getAreaDocumentoPropertyPath() + ".idZonaGeografica", null);
        setProperty(getAreaDocumentoPropertyPath() + ".tipologiaCodiceIvaAlternativo",
                ETipologiaCodiceIvaAlternativo.NESSUNO);
        setProperty(getAreaDocumentoPropertyPath() + ".agente", null);
        setProperty(getAreaDocumentoPropertyPath() + ".codiceIvaAlternativo", null);

        setProperty("importoDocumentiAperti", BigDecimal.ZERO);
        setProperty("importoRateAperte", BigDecimal.ZERO);
    }

    /**
     * @return the areaDocumentoPropertyPath
     */
    public String getAreaDocumentoPropertyPath() {
        return areaDocumentoPropertyPath;
    }

    /**
     * restituisce {@link SedeEntita} principale di Entita.
     *
     * @param entitaLite
     *            entita della sede
     * @return sede principale dell'entità
     */
    private SedeEntita getSedeEntitaPrincipale(EntitaLite entitaLite) {
        logger.debug("--> Enter getSedeEntitaPrincipale");
        Entita entita;
        if (entitaLite.getTipo().equals(ClienteLite.TIPO)) {
            entita = new Cliente();
            entita.setId(entitaLite.getId());
            entita.setVersion(entitaLite.getVersion());
        } else if (entitaLite.getTipo().equals(FornitoreLite.TIPO)) {
            entita = new Fornitore();
            entita.setId(entitaLite.getId());
            entita.setVersion(entitaLite.getVersion());
        } else if (entitaLite.getTipo().equals(ClientePotenzialeLite.TIPO)) {
            entita = new ClientePotenziale();
            entita.setId(entitaLite.getId());
            entita.setVersion(entitaLite.getVersion());
        } else {
            // se Entita non è ne cliente ne fornitore restituisco una istanza vuota di SedeEntita
            return new SedeEntita();
        }
        SedeEntita sedeEntita = anagraficaBD.caricaSedePredefinitaEntita(entita);
        return sedeEntita;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        logger.debug("--> Enter propertyChange");
        // verifica variazione solamente se sono in modifica
        // tramite mail è arrivata una npe, come se il formModel fosse null,
        // teoricamente non dovrebbe essere però faccio un controllo in +
        if (formModel == null) {
            return;
        }
        if (formModel.isReadOnly() || ((PanjeaFormModel) formModel).isAdjustingMode()) {
            return;
        }

        clearData();

        if (event.getNewValue() == null) {
            return;
        }

        EntitaLite entita = (EntitaLite) event.getNewValue();
        ((PanjeaFormModel) formModel).setAdjustingMode(true);
        try {
            assegnaSedeEntita(entita);
        } finally {
            ((PanjeaFormModel) formModel).setAdjustingMode(false);
        }
        assegnaContratto(entita);
        logger.debug("--> Exit propertyChange");
    }

    /**
     * @param anagraficaBD
     *            The anagraficaBD to set.
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    /**
     * @param anagraficaTabelleBD
     *            the anagraficaTabelleBD to set
     */
    public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
        this.anagraficaTabelleBD = anagraficaTabelleBD;
    }

    /**
     * @param areaDocumentoPropertyPath
     *            the areaDocumentoPropertyPath to set
     */
    public void setAreaDocumentoPropertyPath(String areaDocumentoPropertyPath) {
        this.areaDocumentoPropertyPath = areaDocumentoPropertyPath;
    }

    @Override
    public void setFormModel(FormModel formModel) {
        this.formModel = formModel;
    }

    /**
     * Metodo di utilità per settare una proprietà nel form Model.
     *
     * @param propertyName
     *            nome della proprietà
     * @param value
     *            valore da settare
     */
    private void setProperty(String propertyName, Object value) {
        if (formModel.hasValueModel(propertyName)) {
            formModel.getValueModel(propertyName).setValue(value);
        }
    }

}
