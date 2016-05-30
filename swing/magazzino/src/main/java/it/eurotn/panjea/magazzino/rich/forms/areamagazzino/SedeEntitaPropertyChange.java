package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.magazzino.domain.NoteAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

public class SedeEntitaPropertyChange implements FormModelPropertyChangeListeners {

    protected static Logger logger = Logger.getLogger(SedeEntitaPropertyChange.class);

    private FormModel formModel;

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private String areaDocumentoPropertyPath;

    /**
     * @return the areaDocumentoPropertyPath
     */
    public String getAreaDocumentoPropertyPath() {
        return areaDocumentoPropertyPath;
    }

    /**
     * Imposta la valuta per il totale documento.
     *
     * @param sedeEntitaCorrente
     *            nuova sede selezionata
     */
    private void impostaValuta(SedeEntita sedeEntitaCorrente) {
        Importo totaleDocumento = (Importo) formModel.getValueModel(areaDocumentoPropertyPath + ".documento.totale")
                .getValue();
        Importo nuovoImporto = totaleDocumento.clone();
        String codiceValuta = sedeEntitaCorrente.getCodiceValuta();
        // Se non ho la valuta sul cliente uso quella aziendale
        if (codiceValuta == null) {
            AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
            codiceValuta = aziendaCorrente.getCodiceValuta();
        }
        nuovoImporto.setCodiceValuta(codiceValuta);
        formModel.getValueModel(areaDocumentoPropertyPath + ".documento.totale").setValue(nuovoImporto);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.debug("--> Enter propertyChange");
        // a volte mi arriva una null pointer, probabilmente dallo stacktrace
        // dell'errore è il formModel.
        // per sicurezza testo il null
        if (formModel == null) {
            return;
        }
        if (evt.getNewValue() == null) {
            return;
        }

        if (evt.getNewValue().equals(evt.getOldValue())) {
            return;
        }
        // verifica variazione solamente se sono in modifica
        if (formModel.hasValueModel("importoRateAperte")) {
            setProperty("importoRateAperte", BigDecimal.ZERO);
            setProperty("importoDocumentiAperti", BigDecimal.ZERO);
        }
        if (formModel.isReadOnly()) {
            return;
        }

        final SedeEntita sedeEntitaCorrente = (SedeEntita) evt.getNewValue();

        // Se il tipo entita è azienda non carico nulla dalla sede entita
        final IAreaDocumento areaDocumento = (IAreaDocumento) formModel.getValueModel(getAreaDocumentoPropertyPath())
                .getValue();
        if (areaDocumento.getDocumento().getTipoDocumento().getTipoEntita() == TipoEntita.AZIENDA) {
            logger.debug("--> Exit propertyChange perchè l'entità su documento è azienda");
            return;
        }

        impostaValuta(sedeEntitaCorrente);

        SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO = magazzinoDocumentoBD.caricaSedeAreaMagazzinoDTO(sedeEntitaCorrente);
        if (sedeAreaMagazzinoDTO != null) {
            setProperty(getAreaDocumentoPropertyPath() + ".listinoAlternativo",
                    sedeAreaMagazzinoDTO.getListinoAlternativo());
            setProperty(getAreaDocumentoPropertyPath() + ".listino", sedeAreaMagazzinoDTO.getListino());
            setProperty(getAreaDocumentoPropertyPath() + ".vettore", sedeAreaMagazzinoDTO.getVettore());
            setProperty(getAreaDocumentoPropertyPath() + ".sedeVettore", sedeAreaMagazzinoDTO.getSedeVettore());
            setProperty(getAreaDocumentoPropertyPath() + ".causaleTrasporto",
                    sedeAreaMagazzinoDTO.getCausaleTrasporto());
            setProperty(getAreaDocumentoPropertyPath() + ".trasportoCura", sedeAreaMagazzinoDTO.getTrasportoCura());
            setProperty(getAreaDocumentoPropertyPath() + ".tipoPorto", sedeAreaMagazzinoDTO.getTipoPorto());
            setProperty(getAreaDocumentoPropertyPath() + ".codiceValuta", sedeAreaMagazzinoDTO.getCodiceValuta());
            setProperty(getAreaDocumentoPropertyPath() + ".addebitoSpeseIncasso",
                    sedeAreaMagazzinoDTO.isCalcoloSpese());
            setProperty(getAreaDocumentoPropertyPath() + ".inserimentoBloccato",
                    sedeAreaMagazzinoDTO.isInserimentoBloccato());
            setProperty(getAreaDocumentoPropertyPath() + ".raggruppamentoBolle",
                    sedeAreaMagazzinoDTO.isRaggruppamentoBolle());
            setProperty(getAreaDocumentoPropertyPath() + ".aspettoEsteriore",
                    sedeAreaMagazzinoDTO.getAspettoEsteriore());
            setProperty(getAreaDocumentoPropertyPath() + ".stampaPrezzi", sedeAreaMagazzinoDTO.isStampaPrezzi());
            setProperty(getAreaDocumentoPropertyPath() + ".idZonaGeografica",
                    sedeAreaMagazzinoDTO.getIdZonaGeografica());
            setProperty(getAreaDocumentoPropertyPath() + ".agente", sedeAreaMagazzinoDTO.getAgente());

            ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo = sedeAreaMagazzinoDTO
                    .getTipologiaCodiceIvaAlternativo();

            CodiceIva codiceIvaAlternativo = sedeAreaMagazzinoDTO.getCodiceIvaAlternativo();
            // se la tipologia del codice iva alternativo è
            // ESENZIONE_DICHIARAZIONE_INTENTO controllo che la
            // data del documento rientri nella scadenza altrimenti
            // imposto come tipologia NESSUNA e rimuovo il
            // codice iv alternativo
            if (sedeAreaMagazzinoDTO
                    .getTipologiaCodiceIvaAlternativo() == ETipologiaCodiceIvaAlternativo.ESENZIONE_DICHIARAZIONE_INTENTO) {
                Date dataScadenza = sedeAreaMagazzinoDTO.getDataScadenzaDichiarazioneIntento();
                Date dataDocumento = (Date) formModel
                        .getValueModel(getAreaDocumentoPropertyPath() + ".documento.dataDocumento").getValue();

                if (dataScadenza == null || (dataDocumento != null && dataDocumento.after(dataScadenza))) {
                    tipologiaCodiceIvaAlternativo = ETipologiaCodiceIvaAlternativo.NESSUNO;
                    codiceIvaAlternativo = null;
                }

            }
            setProperty(getAreaDocumentoPropertyPath() + ".tipologiaCodiceIvaAlternativo",
                    tipologiaCodiceIvaAlternativo);
            setProperty(getAreaDocumentoPropertyPath() + ".codiceIvaAlternativo", codiceIvaAlternativo);

            if (formModel.hasValueModel("importoRateAperte")) {
                // setto prima a 0 perchè posso avere 2 clienti con lo
                // stesso importo e non scatterebbe il
                // property change
                setProperty("importoDocumentiAperti", BigDecimal.ZERO);
                setProperty("importoDocumentiAperti", sedeAreaMagazzinoDTO.getImportoDocumentiAperti());
                setProperty("importoRateAperte", BigDecimal.ZERO);
                setProperty("importoRateAperte", sedeAreaMagazzinoDTO.getImportoRateAperte());
            }
            // Se ho l'area contabile lite non devo cambiare nulla sui
            // pagamenti
            // e se l'area magazzino è nuova ma ho un documento
            // associato significa che eredito l'area
            // pagamento
            // dalla contabilità quindi non devo modificarla
            AreaContabileLite areaContabileLite = new AreaContabileLite();

            if (formModel.hasValueModel("areaContabileLite")) {
                areaContabileLite = (AreaContabileLite) formModel.getValueModel("areaContabileLite").getValue();
            }
            boolean areaContabileNonPresente = (areaContabileLite == null
                    || areaContabileLite != null && areaContabileLite.getId() == null);
            if (areaContabileNonPresente
                    || !((areaDocumento.getId() == null && areaDocumento.getDocumento().getId() == null))) {
                boolean areaRateEnabled = ((Boolean) formModel.getValueModel("areaRateEnabled").getValue())
                        .booleanValue();
                if (areaRateEnabled) {
                    CodicePagamento codicePag = sedeAreaMagazzinoDTO.getCodicePagamento();
                    setProperty("areaRate.codicePagamento", codicePag);
                    setProperty("areaRate.percentualeSconto",
                            codicePag == null ? null : codicePag.getPercentualeSconto());
                    setProperty("areaRate.giorniLimite", codicePag == null ? null : codicePag.getGiorniLimite());
                }
            }
            SedeEntita sedeEntitaSelezionata = (SedeEntita) formModel
                    .getValueModel(getAreaDocumentoPropertyPath() + ".documento.sedeEntita").getValue();
            if (sedeEntitaSelezionata != null) {
                NoteAreaMagazzino note = magazzinoDocumentoBD.caricaNoteSede(sedeEntitaSelezionata);

                // se arriva note[]!=null allora visualizzo il dialogo
                if (!note.isEmpty()) {
                    NoteMagazzinoEntitaDialog dialog = new NoteMagazzinoEntitaDialog(note);
                    dialog.setPreferredSize(new Dimension(400, 400));
                    dialog.showDialog();
                }
            }
        }

        logger.debug("--> Exit propertyChange");
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
     *
     * @param magazzinoDocumentoBD
     *            setter magazzinoDocumentoBD
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
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
