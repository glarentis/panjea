package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.beans.PropertyChangeEvent;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;
import it.eurotn.rich.form.PanjeaFormModel;

/**
 * PropertyChange legato all'attributo TipoAreaMagazzino incaricato di modificare i controlli in base alla variazione di
 * alcuni suoi attributi: <br>
 * <ul>
 * <li>Entita':cliente o fornitore visualizza componenti di ricerca</li>
 * <li>TipoAreaMgazzino con deposito assegnato:assegna il deposito origine dal TipoDocumento e visualizza i suoi
 * controlli e ne impedisce la variazione</li>
 * <li>TipoAreaMagazzino di scarico: disable controlli totale documento</li>
 * <li>TipoAreaMagazzino con strategia calcolo prezzo da listino: copia {@link Listino} da {@link SedeEntita} di
 * {@link Cliente}</li>
 * <li>Copia dei dati di {@link Cliente} che interessano {@link AreaMagazzino} e {@link AreaRate}</li>
 * </ul>
 * vedi UseCase PNJ.MA.02.02.
 *
 * @author adriano
 * @version 1.0, 04/set/2008
 */
public class TipoAreaMagazzinoPropertyChange implements FormModelPropertyChangeListeners {

    private static Logger logger = Logger.getLogger(TipoAreaMagazzinoPropertyChange.class);

    private FormModel formModel;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;
    private IAnagraficaBD anagraficaBD;

    private JLabel labelDepositoDestinazione;

    private JComponent[] entitaComponents;

    private JComponent[] rifornimentoComponents;

    private JComponent[] sedeEntitaComponents;

    /**
     * Costruttore.
     */
    public TipoAreaMagazzinoPropertyChange() {
        super();
        this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
        this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
    }

    private void applyDataRegistrazione() {
        if (formModel.isReadOnly()) {
            return;
        }
        TipoAreaMagazzino tipoAreaMagazzino = (TipoAreaMagazzino) formModel
                .getValueModel("areaMagazzino.tipoAreaMagazzino").getValue();
        if (tipoAreaMagazzino != null && tipoAreaMagazzino.isDataDocLikeDataReg()) {
            Date dataRegistrazione = (Date) formModel.getValueModel("areaMagazzino.dataRegistrazione").getValue();
            formModel.getValueModel("areaMagazzino.documento.dataDocumento").setValueSilently(dataRegistrazione, this);
        }
    }

    /**
     * Assegna deposito come depositoDestinazione; non viene assegnato se il depositoDestinazione e' valorizzato.
     *
     * @param deposito
     *            il deposito da settare al value model
     */
    private void assegnaDepositoDestinazione(DepositoLite deposito) {
        if (formModel.isReadOnly()) {
            return;
        }
        DepositoLite depositoDestinazione = (DepositoLite) formModel
                .getValueModel("areaMagazzino.tipoAreaMagazzino.depositoDestinazione").getValue();

        if (depositoDestinazione != null) {
            formModel.getValueModel("areaMagazzino.depositoDestinazione").setValueSilently(deposito, this);
        }
    }

    /**
     * Assegna deposito come depositoOrigine; non viene assegnato se il depositoOrigine e' valorizzato.
     *
     * @param deposito
     *            il deposito da settare al value model
     */
    private void assegnaDepositoOrigine(DepositoLite deposito) {
        if (formModel.isReadOnly()) {
            return;
        }
        DepositoLite depositoOrigine = (DepositoLite) formModel
                .getValueModel("areaMagazzino.tipoAreaMagazzino.depositoOrigine").getValue();
        if (depositoOrigine != null) {
            formModel.getValueModel("areaMagazzino.depositoOrigine").setValueSilently(deposito, this);
        }
    }

    /**
     * Modifica lo stato enabled del fieldmetadata in modo da attivare la visibility sul form dei componenti.
     */
    private void changeEnabled() {
        logger.debug("--> Enter changeEnabled");
        ((ValidatingFormModel) formModel).validate();

        formModel.getFieldMetadata("areaMagazzino.documento.entita").setEnabled(false);
        formModel.getFieldMetadata("areaMagazzino.mezzoTrasporto").setEnabled(false);
        formModel.getFieldMetadata("areaMagazzino.depositoDestinazione").setEnabled(false);

        TipoAreaMagazzino tipoAreaMagazzino = (TipoAreaMagazzino) formModel
                .getValueModel("areaMagazzino.tipoAreaMagazzino").getValue();
        if (tipoAreaMagazzino == null) {
            return;
        }

        // controllo di entita'
        if (tipoAreaMagazzino.isGestioneVending()
                || TipoEntita.CLIENTE.equals(tipoAreaMagazzino.getTipoDocumento().getTipoEntita())
                || (TipoEntita.FORNITORE.equals(tipoAreaMagazzino.getTipoDocumento().getTipoEntita()))) {
            logger.debug("--> abilita' entita'");
            formModel.getFieldMetadata("areaMagazzino.documento.entita").setEnabled(true);

        }

        formModel.getValueModel("soloDepositiFurgoni").setValue(tipoAreaMagazzino.isGestioneVending());

        // controllo di mezzo trasporto
        formModel.getFieldMetadata("areaMagazzino.mezzoTrasporto")
                .setEnabled(tipoAreaMagazzino.isRichiestaMezzoTrasporto());

        // controllo di deposito origine assegnato
        formModel.getFieldMetadata("areaMagazzino.depositoOrigine").setReadOnly(false);

        if (tipoAreaMagazzino.getDepositoOrigine() != null) {

            if (tipoAreaMagazzino.isDepositoOrigineBloccato()) {
                logger.debug("--> disable componente deposito origine ");
                formModel.getFieldMetadata("areaMagazzino.depositoOrigine").setReadOnly(true);
            }
        } else {
            formModel.getFieldMetadata("areaMagazzino.depositoOrigine").setEnabled(true);
        }

        boolean depositoDestinazione = tipoAreaMagazzino.getTipoMovimento().isEnableDepositoDestinazione()
                && !tipoAreaMagazzino.isGestioneVending();
        // controllo enabled/disable deposito destinazione
        formModel.getFieldMetadata("areaMagazzino.depositoDestinazione").setEnabled(depositoDestinazione);

        // controllo di deposito destinazione associato
        formModel.getFieldMetadata("areaMagazzino.depositoDestinazione").setReadOnly(false);
        if (depositoDestinazione) {
            if (tipoAreaMagazzino.getDepositoDestinazione() != null) {

                if (tipoAreaMagazzino.isDepositoDestinazioneBloccato()) {
                    logger.debug("--> disable componente deposito destinazione ");
                    formModel.getFieldMetadata("areaMagazzino.depositoDestinazione").setReadOnly(true);
                }
            }
        }

        // Controllo provenienza prezzo
        formModel.getFieldMetadata("areaMagazzino.listino")
                .setEnabled(tipoAreaMagazzino.getProvenienzaPrezzo() == ProvenienzaPrezzo.LISTINO);
        formModel.getFieldMetadata("areaMagazzino.listinoAlternativo")
                .setEnabled(tipoAreaMagazzino.getProvenienzaPrezzo() == ProvenienzaPrezzo.LISTINO);

        // Controllo se ho la gestione agenti
        formModel.getFieldMetadata("agenti").setEnabled(tipoAreaMagazzino.isGestioneAgenti());

        // formModel.getFieldMetadata("areaMagazzino.documento.codice").setEnabled(false);
        // formModel.getFieldMetadata("areaMagazzino.documento.codice").setEnabled(true);
        // formModel.getFieldMetadata("areaMagazzino.tipoAreaMagazzino").setEnabled(true);

        logger.debug("--> Exit changeEnabled");
    }

    /**
     * Imposta i valori nel form model a seconda del tipoareamagazzino.
     */
    private void changeValues() {
        logger.debug("--> Enter changeValues");

        ((ValidatingFormModel) formModel).validate();
        TipoAreaMagazzino tipoAreaMagazzino = (TipoAreaMagazzino) formModel
                .getValueModel("areaMagazzino.tipoAreaMagazzino").getValue();

        formModel.getValueModel("areaMagazzino.documento.entita").setValue(null);
        formModel.getValueModel("areaMagazzino.documento.codice.codice").setValue(null);

        if (tipoAreaMagazzino == null) {
            return;
        }

        // controllo di entita'
        if (TipoEntita.CLIENTE.equals(tipoAreaMagazzino.getTipoDocumento().getTipoEntita())
                || (TipoEntita.FORNITORE.equals(tipoAreaMagazzino.getTipoDocumento().getTipoEntita()))) {
            // Copio entità predefinita
            if (tipoAreaMagazzino.getEntitaPredefinita() != null) {
                if (!formModel.isReadOnly()) {
                    formModel.getValueModel("areaMagazzino.documento.entita")
                            .setValue(tipoAreaMagazzino.getEntitaPredefinita());
                }
            }
        }

        if (tipoAreaMagazzino.getDepositoOrigine() != null) {
            logger.debug("--> assegna deposito origine " + tipoAreaMagazzino.getDepositoOrigine());
            assegnaDepositoOrigine(tipoAreaMagazzino.getDepositoOrigine());
        } else {
            Deposito deposito = anagraficaBD.caricaDepositoPrincipale();
            if (deposito != null) {
                formModel.getValueModel("areaMagazzino.depositoOrigine").setValueSilently(deposito.getDepositoLite(),
                        this);
            }

        }

        boolean depositoDestinazione = tipoAreaMagazzino.getTipoMovimento().isEnableDepositoDestinazione();

        // controllo di deposito destinazione associato
        if (!depositoDestinazione) {
            formModel.getValueModel("areaMagazzino.depositoDestinazione").setValueSilently(null, this);
        } else {
            if (tipoAreaMagazzino.getDepositoDestinazione() != null) {
                logger.debug("--> assegna deposito destinazione " + tipoAreaMagazzino.getDepositoDestinazione());
                assegnaDepositoDestinazione(tipoAreaMagazzino.getDepositoDestinazione());
            }
        }

        Listino listino = (Listino) formModel.getValueModel("areaMagazzino.listino").getValue();
        // Controllo listino di default su tipoAreaMagazzino
        if (tipoAreaMagazzino.getProvenienzaPrezzo() == ProvenienzaPrezzo.LISTINO
                && tipoAreaMagazzino.getTipoDocumento().getTipoEntita() == TipoEntita.AZIENDA
                && tipoAreaMagazzino.getListino() != null && listino == null) {
            formModel.getValueModel("areaMagazzino.listino").setValue(tipoAreaMagazzino.getListino());
        }
        logger.debug("--> Exit changeValues");
    }

    /**
     * Il deposito di destinazione ha più label dipendenti dal tipoMovimento. Qui lo configuro.
     */
    private void configuraLabelDepositoDestinazione() {
        String idDepositoDestinazioneFace = "depositoDestinazione";
        TipoAreaMagazzino tam = (TipoAreaMagazzino) formModel.getValueModel("areaMagazzino.tipoAreaMagazzino")
                .getValue();
        if (tam != null && tam.getTipoMovimento() == TipoMovimento.CARICO_PRODUZIONE) {
            idDepositoDestinazioneFace = "depositoProduzione";
        }
        labelDepositoDestinazione.setText(RcpSupport.getMessage(idDepositoDestinazioneFace));
    }

    /**
     * @return the magazzinoDocumentoBD
     */
    public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
        return magazzinoDocumentoBD;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        configuraLabelDepositoDestinazione();
        // L'entità potrebbe essere disabilitata perchè c'è un area
        // contabile associata
        // ma il tipo documento la prevede e quindi devo tenerla
        // visualizzata
        TipoDocumento tipoDocumento = (TipoDocumento) formModel
                .getValueModel("areaMagazzino.tipoAreaMagazzino.tipoDocumento").getValue();
        TipoAreaMagazzino tam = (TipoAreaMagazzino) formModel.getValueModel("areaMagazzino.tipoAreaMagazzino")
                .getValue();
        TipoEntita tipoEntitaArea = tipoDocumento != null ? tipoDocumento.getTipoEntita() : null;
        boolean isGestioneVending = tam != null && tam.isGestioneVending();
        setEntitaComponentsVisible(isGestioneVending || (tipoEntitaArea != null
                && (TipoEntita.CLIENTE.equals(tipoEntitaArea) || (TipoEntita.FORNITORE.equals(tipoEntitaArea)))));

        setRifornimentoComponentsVisible(tam != null && tam.isGestioneVending());

        // chiamata per aggiornare lo stato enabled dei componenti
        // attiva i property change sul form per visualizzare/nascondere i
        // componenti a seconda del tipo documento
        changeEnabled();
        // agisce sui dati nel form al cambio del tipo documento, attivato solo
        // se il form non è read only
        if (!((PanjeaFormModel) formModel).isAdjustingMode() && !formModel.isReadOnly()) {
            changeValues();
        }
        applyDataRegistrazione();
    }

    /**
     * @param entitaComponents
     *            The entitaComponents to set.
     */
    public void setEntitaComponents(JComponent[] entitaComponents) {
        this.entitaComponents = entitaComponents;
    }

    private void setEntitaComponentsVisible(boolean visible) {
        if (sedeEntitaComponents != null) {
            for (JComponent component : sedeEntitaComponents) {
                component.setVisible(visible);
            }
        }
        if (entitaComponents != null) {
            for (JComponent component : entitaComponents) {
                component.setVisible(visible);
            }
        }
    }

    @Override
    public void setFormModel(FormModel formModel) {
        this.formModel = formModel;
    }

    /**
     * @param labelDepositoDestinazione
     *            The labelDepositoDestinazione to set.
     */
    public void setLabelDepositoDestinazione(JLabel labelDepositoDestinazione) {
        this.labelDepositoDestinazione = labelDepositoDestinazione;
    }

    /**
     *
     * @param rifornimentoComponents
     *            componenti dati rif.
     */
    public void setRifornimentoComponent(JComponent[] rifornimentoComponents) {
        this.rifornimentoComponents = rifornimentoComponents;
    }

    /**
     * @param visible
     *            visualizza o nasconde i componenti relativi al rifornimento
     */
    private void setRifornimentoComponentsVisible(boolean visible) {
        if (rifornimentoComponents != null) {
            for (JComponent component : rifornimentoComponents) {
                component.setVisible(visible);
            }
        }
    }

    /**
     *
     * @param sedeEntitaComponents
     *            comp. dati sede ent.
     */
    public void setSedeEntitaComponents(JComponent[] sedeEntitaComponents) {
        this.sedeEntitaComponents = sedeEntitaComponents;
    }
}
