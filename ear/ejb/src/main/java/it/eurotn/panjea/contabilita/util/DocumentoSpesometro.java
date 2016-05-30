package it.eurotn.panjea.contabilita.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.TipologiaCodiceIvaSpesometro;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;

/**
 * @author fattazzo
 *
 */
public class DocumentoSpesometro implements Serializable {

    public enum TipologiaDocumento {
        FATTURA_ATTIVA, FATTURA_PASSIVA, NOTA_CREDITO_ATTIVA, NOTA_CREDITO_PASSIVA, FATTURA_PASSIVA_SERVIZI_NON_RESIDENTI, FATTURA_ATTIVA_NON_RESIDENTI, NON_DEFINITO
    }

    private static final long serialVersionUID = -6618989228672015038L;

    private Integer idDocumento;

    private Date dataRegistrazione;

    private CodiceDocumento codiceDocumento;

    private TipoDocumento tipoDocumento;

    private Entita entita;

    private BigDecimal imponibile;

    private BigDecimal imposta;

    private boolean notaCredito;

    private boolean attivo;

    private Integer documentiAggregati;

    private boolean reverseCharge;

    private String legaleRappresentanteCodiceStato;

    private TipologiaCodiceIvaSpesometro tipologiaCodiceIvaSpesometro;

    {
        tipoDocumento = new TipoDocumento();

        documentiAggregati = 1;

        notaCredito = Boolean.FALSE;
        attivo = Boolean.TRUE;

        reverseCharge = Boolean.FALSE;

        codiceDocumento = new CodiceDocumento();
    }

    /**
     * @return codiceFiscaleEntita
     */
    public Anagrafica getAnagrafica() {
        return this.entita.getAnagrafica();
    }

    /**
     * @return the codiceDocumento
     */
    public CodiceDocumento getCodiceDocumento() {
        return codiceDocumento;
    }

    /**
     * @return codiceFiscaleEntita
     */
    public String getCodiceFiscaleEntita() {
        return this.entita.getAnagrafica().getCodiceFiscale();
    }

    /**
     * @return the dataRegistrazione
     */
    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    /**
     * @return the documentiAggregati
     */
    public Integer getDocumentiAggregati() {
        return documentiAggregati;
    }

    /**
     * @return the entita
     */
    public Entita getEntita() {
        return entita;
    }

    /**
     * @return the idDocumento
     */
    public Integer getIdDocumento() {
        return idDocumento;
    }

    /**
     * @return the imponibile
     */
    public BigDecimal getImponibile() {
        return imponibile;
    }

    /**
     * @return the imposta
     */
    public BigDecimal getImposta() {
        return imposta;
    }

    /**
     * @return codiceFiscaleEntita
     */
    public String getPartitaIvaEntita() {
        return this.entita.getAnagrafica().getPartiteIVA();
    }

    /**
     * @return the tipoDocumento
     */
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @return the tipologiaCodiceIvaSpesometro
     */
    public TipologiaCodiceIvaSpesometro getTipologiaCodiceIvaSpesometro() {
        return tipologiaCodiceIvaSpesometro;
    }

    /**
     * @return tipologia documento
     */
    public TipologiaDocumento getTipologiaDocumento() {

        TipologiaDocumento tipologia = TipologiaDocumento.NON_DEFINITO;

        if (isNonResidente()) {
            if (!isAttivo() && getTipologiaCodiceIvaSpesometro() == TipologiaCodiceIvaSpesometro.SERVIZI) {
                tipologia = TipologiaDocumento.FATTURA_PASSIVA_SERVIZI_NON_RESIDENTI;
            }
            if (isAttivo() && getTipologiaCodiceIvaSpesometro() != TipologiaCodiceIvaSpesometro.SERVIZI) {
                tipologia = TipologiaDocumento.FATTURA_ATTIVA_NON_RESIDENTI;
            }

            return tipologia;
        }

        if (!isNotaCredito()) {
            if (isAttivo()) {
                tipologia = TipologiaDocumento.FATTURA_ATTIVA;
            } else {
                tipologia = TipologiaDocumento.FATTURA_PASSIVA;
            }
        } else {
            if (isAttivo()) {
                tipologia = TipologiaDocumento.NOTA_CREDITO_ATTIVA;
            } else {
                tipologia = TipologiaDocumento.NOTA_CREDITO_PASSIVA;
            }
        }

        return tipologia;
    }

    /**
     * @return the attivo
     */
    public boolean isAttivo() {
        return attivo;
    }

    /**
     * Indica se l'entità del documento risulta essere non residente, caso in cui lo stato del legale rappresentante è
     * "IT" e quello dell'entità è diverso da "IT".
     *
     * @return <code>true</code> se non residente
     */
    public boolean isNonResidente() {
        String statoLegale = StringUtils.defaultString(legaleRappresentanteCodiceStato);
        String statoEntita = entita.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getCodiceNazione();

        return "IT".equals(statoLegale.toUpperCase().trim()) && !"IT".equals(statoEntita.toUpperCase().trim());
    }

    /**
     * @return the notaCredito
     */
    public boolean isNotaCredito() {
        return notaCredito;
    }

    /**
     * @return the reverseCharge
     */
    public boolean isReverseCharge() {
        return reverseCharge;
    }

    /**
     * @return the riepilogativo
     */
    public boolean isRiepilogativo() {
        return entita.isRiepilogativo();
    }

    /**
     * @param attivo
     *            the attivo to set
     */
    public void setAttivo(int attivo) {
        this.attivo = attivo == 0 ? true : false;

        if (this.attivo) {
            this.entita = new Cliente();
        } else {
            this.entita = new Fornitore();
        }
    }

    /**
     * @param codiceEntita
     *            the codiceEntita to set
     */
    public void setCodiceEntita(Integer codiceEntita) {
        this.entita.setCodice(codiceEntita);
    }

    /**
     * @param codiceFiscaleEntita
     *            the codiceFiscaleEntita to set
     */
    public void setCodiceFiscaleEntita(String codiceFiscaleEntita) {
        this.entita.getAnagrafica().setCodiceFiscale(codiceFiscaleEntita);
    }

    /**
     * @param codiceNazioneSede
     *            the codiceNazioneSede to set
     */
    public void setCodiceNazioneSede(String codiceNazioneSede) {
        this.entita.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getNazione().setCodice(codiceNazioneSede);
    }

    /**
     * @param codiceNazioneSedeUIC
     *            the codiceNazioneSedeUIC to set
     */
    public void setCodiceNazioneUICSede(Integer codiceNazioneSedeUIC) {
        this.entita.getAnagrafica().getSedeAnagrafica().getDatiGeografici().setNazione(new Nazione());
        this.entita.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getNazione()
                .setCodiceNazioneUIC(codiceNazioneSedeUIC);
    }

    /**
     * @param codiceTipoDocumento
     *            the codiceTipoDocumento to set
     */
    public void setCodiceTipoDocumento(String codiceTipoDocumento) {
        this.tipoDocumento.setCodice(codiceTipoDocumento);
    }

    /**
     * @param dataRegistrazione
     *            the dataRegistrazione to set
     */
    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * @param denominazioneEntita
     *            the denominazioneEntita to set
     */
    public void setDenominazioneEntita(String denominazioneEntita) {
        this.entita.getAnagrafica().setDenominazione(denominazioneEntita);
    }

    /**
     * @param descrizioneLocalitaSede
     *            the descrizioneLocalitaSede to set
     */
    public void setDescrizioneLocalitaSede(String descrizioneLocalitaSede) {
        this.entita.getAnagrafica().getSedeAnagrafica().getDatiGeografici().setLocalita(new Localita());
        this.entita.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getLocalita()
                .setDescrizione(descrizioneLocalitaSede);
    }

    /**
     * @param descrizioneTipoDocumento
     *            the descrizioneTipoDocumento to set
     */
    public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
        this.tipoDocumento.setDescrizione(descrizioneTipoDocumento);
    }

    /**
     * @param documentiAggregati
     *            the documentiAggregati to set
     */
    public void setDocumentiAggregati(Integer documentiAggregati) {
        this.documentiAggregati = documentiAggregati;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(Entita entita) {
        this.entita = entita;
        if (entita != null) {
            if (entita instanceof Cliente) {
                attivo = true;
            } else if (entita instanceof Fornitore) {
                attivo = false;
            }
        }
    }

    /**
     * @param idAnagrafica
     *            the idAnagrafica to set
     */
    public void setIdAnagrafica(Integer idAnagrafica) {
        this.entita.getAnagrafica().setId(idAnagrafica);
    }

    /**
     * @param idDocumento
     *            the idDocumento to set
     */
    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    /**
     * @param idEntita
     *            the idEntita to set
     */
    public void setIdEntita(Integer idEntita) {
        this.entita.setId(idEntita);
    }

    /**
     * @param idTipoDocumento
     *            the idTipoDocumento to set
     */
    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.tipoDocumento.setId(idTipoDocumento);
    }

    /**
     * @param imponibile
     *            the imponibile to set
     */
    public void setImponibile(BigDecimal imponibile) {
        this.imponibile = imponibile;
    }

    /**
     * @param imposta
     *            the imposta to set
     */
    public void setImposta(BigDecimal imposta) {
        this.imposta = imposta;
    }

    /**
     * @param indirizzoSede
     *            the indirizzoSede to set
     */
    public void setIndirizzoSede(String indirizzoSede) {
        this.entita.getAnagrafica().getSedeAnagrafica().setIndirizzo(indirizzoSede);
    }

    /**
     * @param legaleRappresentanteCodiceStato
     *            the legaleRappresentanteCodiceStato to set
     */
    public void setLegaleRappresentanteCodiceStato(String legaleRappresentanteCodiceStato) {
        this.legaleRappresentanteCodiceStato = legaleRappresentanteCodiceStato;
    }

    /**
     * @param notaCredito
     *            the notaCredito to set
     */
    public void setNotaCredito(boolean notaCredito) {
        this.notaCredito = notaCredito;
    }

    /**
     * @param numero
     *            the numero to set
     */
    public void setNumero(String numero) {
        this.codiceDocumento.setCodice(numero);
    }

    /**
     * @param numeroOrder
     *            the numeroOrder to set
     */
    public void setNumeroOrder(String numeroOrder) {
        this.codiceDocumento.setCodiceOrder(numeroOrder);
    }

    /**
     * @param partitaIvaEntita
     *            the partitaIvaEntita to set
     */
    public void setPartitaIvaEntita(String partitaIvaEntita) {
        this.entita.getAnagrafica().setPartiteIVA(partitaIvaEntita);
    }

    /**
     * @param personaFisicaCodiceStato
     *            the personaFisicaCodiceStato to set
     */
    public void setPersonaFisicaCodiceStato(String personaFisicaCodiceStato) {
        this.entita.getAnagrafica().getPersonaFisica().getDatiGeograficiNascita().getNazione()
                .setCodice(personaFisicaCodiceStato);
    }

    /**
     * @param personaFisicaCodiceStatoUIC
     *            the personaFisicaCodiceStatoUIC to set
     */
    public void setPersonaFisicaCodiceStatoUIC(Integer personaFisicaCodiceStatoUIC) {
        this.entita.getAnagrafica().getPersonaFisica().getDatiGeograficiNascita().getNazione()
                .setCodiceNazioneUIC(personaFisicaCodiceStatoUIC);
    }

    /**
     * @param personaFisicaCognome
     *            the personaFisicaCognome to set
     */
    public void setPersonaFisicaCognome(String personaFisicaCognome) {
        this.entita.getAnagrafica().getPersonaFisica().setCognome(personaFisicaCognome);
    }

    /**
     * @param personaFisicaDataNascita
     *            the personaFisicaDataNascita to set
     */
    public void setPersonaFisicaDataNascita(Date personaFisicaDataNascita) {
        this.entita.getAnagrafica().getPersonaFisica().setDataNascita(personaFisicaDataNascita);
    }

    /**
     * @param personaFisicaDescrizioneStato
     *            the personaFisicaDescrizioneStato to set
     */
    public void setPersonaFisicaDescrizioneStato(String personaFisicaDescrizioneStato) {
        this.entita.getAnagrafica().getPersonaFisica().getDatiGeograficiNascita().setNazione(new Nazione());
        this.entita.getAnagrafica().getPersonaFisica().getDatiGeograficiNascita().getNazione()
                .setDescrizione(personaFisicaDescrizioneStato);
    }

    /**
     * @param personaFisicaNome
     *            the personaFisicaNome to set
     */
    public void setPersonaFisicaNome(String personaFisicaNome) {
        this.entita.getAnagrafica().getPersonaFisica().setNome(personaFisicaNome);
    }

    /**
     * @param reverseCharge
     *            the reverseCharge to set
     */
    public void setReverseCharge(Integer reverseCharge) {
        this.reverseCharge = (reverseCharge.intValue() == 0 ? false : true);
    }

    /**
     * @param riepilogativo
     *            the riepilogativo to set
     */
    public void setRiepilogativo(Boolean riepilogativo) {
        this.entita.setRiepilogativo(riepilogativo);
    }

    /**
     * @param tipoDocumento
     *            the tipoDocumento to set
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @param tipologiaIvaSpesomentro
     *            the tipologiaIvaSpesomentro to set
     */
    public void setTipologiaIvaSpesomentro(int tipologiaIvaSpesomentro) {
        tipologiaCodiceIvaSpesometro = TipologiaCodiceIvaSpesometro.values()[tipologiaIvaSpesomentro];
    }

}
