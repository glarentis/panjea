package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;

/**
 * Dati per i risultati di una ricerca area magazzino.
 *
 * @author giangi
 */
public class AreaMagazzinoRicerca implements Serializable {

    private static final long serialVersionUID = -246604627995219829L;

    private int idAreaMagazzino;
    private Documento documento;

    private TipoAreaMagazzino tipoAreaMagazzino;

    private boolean chiuso;

    private Date dataRegistrazione;
    private StatoAreaMagazzino stato;
    private String note;
    private DatiGenerazione datiGenerazione;
    private boolean selezionata;

    private Integer idTipoAreaMagazzino;
    private TipoMovimento tipoMovimento;
    private boolean valoreFatturato;

    private Integer idTipoDocumento;
    private String codiceTipoDocumento;
    private String descrizioneTipoDocumento;

    private String codiceDepositoOrigine;
    private String descrizioneDepositoOrigine;
    private Integer idDepositoOrigine;
    private Integer versionDepositoOrigine;
    private DepositoLite depositoOrigine;

    private String codiceDepositoDestinazione;
    private String descrizioneDepositoDestinazione;
    private Integer idDepositoDestinazione;
    private DepositoLite depositoDestinazione;

    private String codiceSedeEntita;
    private String descrizioneLocalita;
    private String descrizioneSedeEntita;
    private Integer idSedeEntita;
    private String indirizzoSedeEntita;
    // utilizzata perchè nei risultati ricerca devo stampare i documenti e mi serve il nome del report
    private SedeEntita sedeEntita;

    private String azienda;
    private Integer codiceEntita;
    private String denominazione;
    private Integer idEntita;
    private TipoEntita tipoEntita;
    private EntitaDocumento entitaDocumento;

    private boolean stampaPrezzi;

    /**
     * Costruttore.
     */
    public AreaMagazzinoRicerca() {
        documento = new Documento();
        datiGenerazione = new DatiGenerazione();
        selezionata = Boolean.TRUE;
        stampaPrezzi = Boolean.FALSE;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AreaMagazzinoRicerca other = (AreaMagazzinoRicerca) obj;
        if (idAreaMagazzino != other.idAreaMagazzino) {
            return false;
        }
        return true;
    }

    /**
     * @return il codice documento
     */
    public CodiceDocumento getCodice() {
        return documento != null ? documento.getCodice() : null;
    }

    /**
     * @return la data documento
     */
    public Date getDataDocumento() {
        return documento != null ? documento.getDataDocumento() : null;
    }

    /**
     * @return the dataRegistrazione
     */
    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    /**
     * @return the datiGenerazione
     */
    public DatiGenerazione getDatiGenerazione() {
        return datiGenerazione;
    }

    /**
     * @return the depositoDestinazione
     */
    public DepositoLite getDepositoDestinazione() {
        if (depositoDestinazione == null && idDepositoDestinazione != null) {
            depositoDestinazione = new DepositoLite();
            depositoDestinazione.setId(idDepositoDestinazione);
            depositoDestinazione.setCodice(codiceDepositoDestinazione);
            depositoDestinazione.setDescrizione(descrizioneDepositoDestinazione);
        }
        return depositoDestinazione;
    }

    /**
     * @return the depositoOrigine
     */
    public DepositoLite getDepositoOrigine() {
        if (depositoOrigine == null && idDepositoOrigine != null) {
            depositoOrigine = new DepositoLite();
            depositoOrigine.setId(idDepositoOrigine);
            depositoOrigine.setVersion(versionDepositoOrigine);
            depositoOrigine.setCodice(codiceDepositoOrigine);
            depositoOrigine.setDescrizione(descrizioneDepositoOrigine);
        }
        return depositoOrigine;
    }

    /**
     * @return the documento
     */
    public Documento getDocumento() {
        documento.getTipoDocumento().setId(idTipoDocumento);
        documento.getTipoDocumento().setCodice(codiceTipoDocumento);
        documento.getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
        return documento;
    }

    /**
     * @return the entitaDocumento
     */
    public EntitaDocumento getEntitaDocumento() {
        if (entitaDocumento == null && (azienda != null || denominazione != null)) {
            entitaDocumento = new EntitaDocumento();
            entitaDocumento.setTipoEntita(tipoEntita);
            if (denominazione != null) {
                entitaDocumento.setId(idEntita);
                entitaDocumento.setCodice(codiceEntita);
                entitaDocumento.setDescrizione(denominazione);
            } else {
                entitaDocumento.setId(1);
                entitaDocumento.setDescrizione(azienda);
            }
        }
        return entitaDocumento;
    }

    /**
     * @return the id
     */
    public int getIdAreaMagazzino() {
        return idAreaMagazzino;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        if (sedeEntita == null && idSedeEntita != null) {
            sedeEntita = new SedeEntita();
            sedeEntita.setId(idSedeEntita);
            sedeEntita.setCodice(codiceSedeEntita);
            sedeEntita.getSede().setDescrizione(descrizioneSedeEntita);
            if (sedeEntita.getSede().getDatiGeografici().getLocalita() == null) {
                sedeEntita.getSede().getDatiGeografici().setLocalita(new Localita());
            }
            sedeEntita.getSede().getDatiGeografici().getLocalita().setDescrizione(descrizioneLocalita);
            sedeEntita.getSede().setIndirizzo(indirizzoSedeEntita);
        }
        return sedeEntita;
    }

    /**
     * @return the stato
     */
    public StatoAreaMagazzino getStato() {
        return stato;
    }

    /**
     * @return the tipoAreaMagazzino
     */
    public TipoAreaMagazzino getTipoAreaMagazzino() {
        if (tipoAreaMagazzino == null && idTipoAreaMagazzino != null) {
            tipoAreaMagazzino = new TipoAreaMagazzino();
            tipoAreaMagazzino.setId(idTipoAreaMagazzino);
            tipoAreaMagazzino.setTipoMovimento(tipoMovimento);
            tipoAreaMagazzino.setValoriFatturato(valoreFatturato);
            tipoAreaMagazzino.getTipoDocumento().setCodice(codiceTipoDocumento);
            tipoAreaMagazzino.getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
        }
        return tipoAreaMagazzino;
    }

    /**
     * @return l'importo che è il totale documento
     */
    public Importo getTotale() {
        return documento != null ? documento.getTotale() : null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idAreaMagazzino;
        return result;
    }

    /**
     * @return Returns the chiuso.
     */
    public boolean isChiuso() {
        return chiuso;
    }

    /**
     * @return the selezionata
     */
    public boolean isSelezionata() {
        return selezionata;
    }

    public boolean isStampaPrezzi() {
        return stampaPrezzi;
    }

    /**
     * @param azienda
     *            azienda del documento
     */
    public void setAzienda(String azienda) {
        this.azienda = azienda;
    }

    /**
     * @param chiuso
     *            The chiuso to set.
     */
    public void setChiuso(boolean chiuso) {
        this.chiuso = chiuso;
    }

    /**
     * @param codiceDepositoDestinazione
     *            the codiceDepositoDestinazione to set
     */
    public void setCodiceDepositoDestinazione(String codiceDepositoDestinazione) {
        this.codiceDepositoDestinazione = codiceDepositoDestinazione;
    }

    /**
     * @param codiceDepositoOrigine
     *            the codiceDepositoOrigine to set
     */
    public void setCodiceDepositoOrigine(String codiceDepositoOrigine) {
        this.codiceDepositoOrigine = codiceDepositoOrigine;
    }

    /**
     * @param codiceDocumento
     *            the codiceDocumento to set
     */
    public void setCodiceDocumento(CodiceDocumento codiceDocumento) {
        documento.setCodice(codiceDocumento);
    }

    /**
     * @param codiceEntita
     *            the codiceEntita to set
     */
    public void setCodiceEntita(Integer codiceEntita) {
        this.codiceEntita = codiceEntita;
    }

    /**
     * @param codiceSede
     *            codice della sede
     */
    public void setCodiceSede(String codiceSede) {
        this.codiceSedeEntita = codiceSede;
    }

    /**
     * @param codiceTipoDocumento
     *            the codiceTipoDocumento to set
     */
    public void setCodiceTipoDocumento(String codiceTipoDocumento) {
        this.codiceTipoDocumento = codiceTipoDocumento;
    }

    /**
     * @param codiceValuta
     *            codice valuta to Set
     */
    public void setCodiceValuta(String codiceValuta) {
        documento.getTotale().setCodiceValuta(codiceValuta);
    }

    /**
     * @param dataDocumento
     *            the dataDocumento to set
     */
    public void setDataDocumento(Date dataDocumento) {
        documento.setDataDocumento(dataDocumento);
    }

    /**
     * @param dataRegistrazione
     *            the dataRegistrazione to set
     */
    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * @param datiGenerazione
     *            the datiGenerazione to set
     */
    public void setDatiGenerazione(DatiGenerazione datiGenerazione) {
        this.datiGenerazione = datiGenerazione;
    }

    /**
     * @param denominazioneParam
     *            the denominazione to set
     */
    public void setDenominazioneEntita(String denominazioneParam) {
        this.denominazione = denominazioneParam;
    }

    /**
     * @param descrizioneDepositoDestinazione
     *            the descrizioneDepositoDestinazione to set
     */
    public void setDescrizioneDepositoDestinazione(String descrizioneDepositoDestinazione) {
        this.descrizioneDepositoDestinazione = descrizioneDepositoDestinazione;
    }

    /**
     * @param descrizioneDepositoOrigine
     *            the descrizioneDepositoOrigine to set
     */
    public void setDescrizioneDepositoOrigine(String descrizioneDepositoOrigine) {
        this.descrizioneDepositoOrigine = descrizioneDepositoOrigine;
    }

    /**
     * @param descrizioneLocalitaParam
     *            the descrizioneLocalita to set
     */
    public void setDescrizioneLocalitaSede(String descrizioneLocalitaParam) {
        this.descrizioneLocalita = descrizioneLocalitaParam;
    }

    /**
     * @param descrizioneSedeEntitaParam
     *            the descrizioneSedeEntita to set
     */
    public void setDescrizioneSede(String descrizioneSedeEntitaParam) {
        this.descrizioneSedeEntita = descrizioneSedeEntitaParam;
    }

    /**
     * @param descrizioneTipoDocumento
     *            the descrizioneTipoDocumento to set
     */
    public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
        this.descrizioneTipoDocumento = descrizioneTipoDocumento;
    }

    /**
     * @param idAreaMagazzino
     *            the id to set
     */
    public void setIdAreaMagazzino(int idAreaMagazzino) {
        this.idAreaMagazzino = idAreaMagazzino;
    }

    /**
     * @param idDepositoDestinazione
     *            id to set
     */
    public void setIdDepositoDestinazione(Integer idDepositoDestinazione) {
        this.idDepositoDestinazione = idDepositoDestinazione;
    }

    /**
     * @param idDepositoOrigine
     *            id to set
     */
    public void setIdDepositoOrigine(Integer idDepositoOrigine) {
        this.idDepositoOrigine = idDepositoOrigine;
    }

    /**
     * @param idDocumento
     *            id del documento
     */
    public void setIdDocumento(Integer idDocumento) {
        documento.setId(idDocumento);
    }

    /**
     * @param idEntita
     *            id entita
     */
    public void setIdEntita(Integer idEntita) {
        this.idEntita = idEntita;
    }

    /**
     * @param idSede
     *            id sede
     */
    public void setIdSede(Integer idSede) {
        this.idSedeEntita = idSede;
    }

    /**
     * @param idTipoAreaMagazzino
     *            the id to set
     */
    public void setIdTipoAreaMagazzino(int idTipoAreaMagazzino) {
        this.idTipoAreaMagazzino = idTipoAreaMagazzino;
    }

    /**
     * @param idTipoDocumento
     *            id Tipo Documento
     */
    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    /**
     * @param indirizzoSedeEntitaParam
     *            the indirizzoSedeEntita to set
     */
    public void setIndirizzoSede(String indirizzoSedeEntitaParam) {
        this.indirizzoSedeEntita = indirizzoSedeEntitaParam;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param selezionata
     *            the selezionata to set
     */
    public void setSelezionata(boolean selezionata) {
        this.selezionata = selezionata;
    }

    public void setStampaPrezzi(boolean stampaPrezzi) {
        this.stampaPrezzi = stampaPrezzi;
    }

    /**
     * @param stato
     *            the stato to set
     */
    public void setStato(StatoAreaMagazzino stato) {
        this.stato = stato;
    }

    /**
     * @param tipoEntitaParam
     *            the tipoEntita to set
     */
    public void setTipoEntita(TipoEntita tipoEntitaParam) {
        this.tipoEntita = tipoEntitaParam;
    }

    /**
     * @param tipoMovimentoParam
     *            the tipoMovimento to set
     */
    public void setTipoMovimento(TipoMovimento tipoMovimentoParam) {
        this.tipoMovimento = tipoMovimentoParam;
    }

    /**
     * @param totaleDocumentoInValuta
     *            the totaleDocumentoInValuta to set
     */
    public void setTotaleDocumentoInValuta(BigDecimal totaleDocumentoInValuta) {
        documento.getTotale().setImportoInValuta(totaleDocumentoInValuta);
    }

    /**
     * @param totaleDocumentoInValutaAzienda
     *            the totaleDocumentoInValutaAzienda to set
     */
    public void setTotaleDocumentoInValutaAzienda(BigDecimal totaleDocumentoInValutaAzienda) {
        documento.getTotale().setImportoInValutaAzienda(totaleDocumentoInValutaAzienda);
    }

    /**
     * @param tassoDiCambio
     *            tassoDiCambio to set
     */
    public void setTotaleDocumentoTassoDiCambio(BigDecimal tassoDiCambio) {
        documento.getTotale().setTassoDiCambio(tassoDiCambio);
    }

    /**
     * @param valoreFatturatoParam
     *            the valoreFatturato to set
     */
    public void setValoriFatturato(boolean valoreFatturatoParam) {
        this.valoreFatturato = valoreFatturatoParam;
    }

    /**
     * @param versionDepositoOrigine
     *            versionDepositoOrigine to set
     */
    public void setVersionDepositoOrigine(Integer versionDepositoOrigine) {
        this.versionDepositoOrigine = versionDepositoOrigine;
    }

}