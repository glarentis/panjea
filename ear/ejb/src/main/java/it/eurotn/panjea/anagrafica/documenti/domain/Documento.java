package it.eurotn.panjea.anagrafica.documenti.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;
import it.eurotn.entity.annotation.ExcludeFromQueryBuilder;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.util.CRC;

@Entity
@Audited
@Table(name = "docu_documenti")
@NamedQueries({
        @NamedQuery(name = "Documento.verificaPresenzaDocumentiByTipoDocumento", query = "select max(d.id) from Documento d where d.tipoDocumento.id=:paramIdTipoDoc and d.codiceAzienda=:paramCodiceAzienda") })
@EntityConverter(properties = "tipoDocumento.codice,codice,dataDocumento")
public class Documento extends EntityBase {

    private static final long serialVersionUID = 8490823454615565126L;

    @Index(name = "codiceAzienda")
    @Column(length = 20, nullable = false)
    private String codiceAzienda;

    @Embedded
    private CodiceDocumento codice;

    private Integer valoreProtocollo;

    @ManyToOne
    private SedeEntita sedeEntita;

    @Index(name = "dataDocumento")
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dataDocumento;

    @ManyToOne
    @JoinColumn(name = "entita_id")
    private EntitaLite entita;

    @ManyToOne
    @JoinColumn(name = "rapporto_bancario_azienda_id")
    private RapportoBancarioAzienda rapportoBancarioAzienda;

    @Embedded
    private Importo totale;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaImposta", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaImposta", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioImposta", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaImposta", length = 3) ) })
    private Importo imposta;

    // @Column(name = "totale", scale = 2, nullable = false)
    // private BigDecimal totale;

    @ManyToOne
    @JoinColumn(name = "tipo_documento_id")
    private TipoDocumento tipoDocumento;

    @Transient
    private EntitaDocumento entitaDocumento;

    @ExcludeFromQueryBuilder
    @ManyToOne
    @JoinColumn(name = "contratto_spesometro_id")
    private ContrattoSpesometro contrattoSpesometro;

    /**
     * La lista di documenti collegati a this.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "documentoDestinazione", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<LinkDocumento> documentiDestinazione;

    /**
     * La lista di documenti che hanno this come documento collegato.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "documentoOrigine", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<LinkDocumento> documentiOrigine;

    /**
     * Init degli oggetti null.
     */
    {
        totale = new Importo();
        imposta = new Importo();
        codice = new CodiceDocumento();
    }

    /**
     *
     * @return anno del documento.
     */
    public int getAnno() {
        return DateUtils.toCalendar(dataDocumento).get(Calendar.YEAR);
    }

    /**
     * @return the codice
     */
    public CodiceDocumento getCodice() {
        return codice;
    }

    /**
     * @return Returns the codiceAzienda.
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the contrattoSpesometro
     */
    public ContrattoSpesometro getContrattoSpesometro() {
        return contrattoSpesometro;
    }

    /**
     *
     * @return crc del documento
     */
    public long getCRC() {
        CRC crc = new CRC();
        crc.update(dataDocumento);
        crc.update(codice.getCodice());
        crc.update(valoreProtocollo);
        crc.update(entita);
        crc.update(sedeEntita);
        crc.update(rapportoBancarioAzienda);
        crc.update(totale.getImportoInValutaAzienda());
        return crc.getValue();
    }

    /**
     * @return dataDocumento
     */
    public Date getDataDocumento() {
        return dataDocumento;
    }

    /**
     * @return Returns the documentiDestinazione.
     */
    public Set<LinkDocumento> getDocumentiDestinazione() {
        return documentiDestinazione;
    }

    /**
     * @return Returns the documentiOrigine.
     */
    public Set<LinkDocumento> getDocumentiOrigine() {
        return documentiOrigine;
    }

    /**
     * @return entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * Restituisce l'entita' del documento corrente (azienda,rapporto bancario o entita').
     *
     * @return {@link EntitaDocumento}
     */
    public EntitaDocumento getEntitaDocumento() {
        if (entitaDocumento == null) {
            entitaDocumento = new EntitaDocumento(this);
        }
        return entitaDocumento;
    }

    /**
     * @return the imposta
     */
    public Importo getImposta() {
        return imposta;
    }

    /**
     * @return the rapportoBancarioAzienda
     */
    public RapportoBancarioAzienda getRapportoBancarioAzienda() {
        return rapportoBancarioAzienda;
    }

    /**
     * @return Returns the sedeEntita.
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return Returns the tipoDocumento.
     */
    public TipoDocumento getTipoDocumento() {
        if (tipoDocumento == null) {
            tipoDocumento = new TipoDocumento();
        }
        return tipoDocumento;
    }

    /**
     * @return Returns the totale.
     */
    public Importo getTotale() {
        return totale;
    }

    /**
     * @return the valoreProtocollo
     */
    public Integer getValoreProtocollo() {
        return valoreProtocollo;
    }

    /**
     * Determina se l'area intra è abilitata per il documento corrente.<br>
     * Devono essere soddisfatte tutte le seguenti condizioni perchè il documento sia abilitato intra:
     * <ul>
     * <li>tipo documento con gestione intra</li>
     * <li>nazione della sede entità intra</li>
     * <li>nazione della sede entità diversa dalla nazione dell'azienda</li>
     * </ul>
     *
     * @param codiceNazioneAzienda
     *            il codice nazione dell'azienda corrente
     * @return true se è abilitata la gestione intra per il documento corrente, false altrimeni
     */
    public boolean isAreaIntraAbilitata(String codiceNazioneAzienda) {
        boolean isTipoDocumentoIntra = getTipoDocumento().isGestioneIntra();
        boolean isNazioneEntitaIntra = getEntita() != null
                && getEntita().getAnagrafica().getSedeAnagrafica().getDatiGeografici().getNazione().isIntra();
        boolean isNazioneEntitaDiversaNazioneAzienda = getEntita() != null && !getEntita().getAnagrafica()
                .getSedeAnagrafica().getDatiGeografici().getCodiceNazione().equals(codiceNazioneAzienda);
        return isTipoDocumentoIntra && isNazioneEntitaIntra && isNazioneEntitaDiversaNazioneAzienda;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(CodiceDocumento codice) {
        this.codice = codice;
    }

    /**
     * @param codiceAzienda
     *            The codiceAzienda to set.
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param codiceEntita
     *            the codiceEntita to set
     */
    public void setCodiceEntita(Integer codiceEntita) {
        this.entita.setCodice(codiceEntita);
    }

    /**
     * @param codiceTipoDocumento
     *            the codiceTipoDocumento to set
     */
    public void setCodiceTipoDocumento(String codiceTipoDocumento) {
        getTipoDocumento().setCodice(codiceTipoDocumento);
    }

    /**
     * @param contrattoSpesometro
     *            the contrattoSpesometro to set
     */
    public void setContrattoSpesometro(ContrattoSpesometro contrattoSpesometro) {
        this.contrattoSpesometro = contrattoSpesometro;
    }

    /**
     * @param dataDocumento
     *            the dataDocumento to set
     */
    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    /**
     * @param denominazioneAnagrafica
     *            the denominazioneAnagrafica to set
     */
    public void setDenominazioneAnagrafica(String denominazioneAnagrafica) {
        this.entita.getAnagrafica().setDenominazione(denominazioneAnagrafica);
    }

    /**
     * @param descrizioneTipoDocumento
     *            the descrizioneTipoDocumento to set
     */
    public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
        getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
    }

    /**
     * @param documentiDestinazione
     *            The documentiDestinazione to set.
     */
    public void setDocumentiDestinazione(Set<LinkDocumento> documentiDestinazione) {
        this.documentiDestinazione = documentiDestinazione;
    }

    /**
     * @param documentiOrigine
     *            The documentiOrigine to set.
     */
    public void setDocumentiOrigine(Set<LinkDocumento> documentiOrigine) {
        this.documentiOrigine = documentiOrigine;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param idEntita
     *            the idEntita to set
     */
    public void setIdEntita(Integer idEntita) {
        this.entita = new ClienteLite();
        this.entita.setId(idEntita);
    }

    /**
     * @param idTipoDocumento
     *            the idTipoDocumento to set
     */
    public void setIdTipoDocumento(Integer idTipoDocumento) {
        getTipoDocumento().setId(idTipoDocumento);
    }

    /**
     * @param imposta
     *            the imposta to set
     */
    public void setImposta(Importo imposta) {
        this.imposta = imposta;
    }

    /**
     * @param rapportoBancarioAzienda
     *            the rapportoBancarioAzienda to set
     */
    public void setRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda) {
        this.rapportoBancarioAzienda = rapportoBancarioAzienda;
    }

    /**
     * @param sedeEntita
     *            The sedeEntita to set.
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

    /**
     * @param tipoDocumento
     *            The tipoDocumento to set.
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @param totale
     *            The totale to set.
     */
    public void setTotale(Importo totale) {
        this.totale = totale;
    }

    /**
     * @param valoreProtocollo
     *            the valoreProtocollo to set
     */
    public void setValoreProtocollo(Integer valoreProtocollo) {
        this.valoreProtocollo = valoreProtocollo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Documento[");
        buffer.append(super.toString());
        buffer.append(" codiceAzienda = ").append(codiceAzienda);
        buffer.append(" dataDocumento = ").append(dataDocumento);
        buffer.append(" totale = ").append(totale);
        buffer.append("]");
        return buffer.toString();
    }
}
