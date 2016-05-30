package it.eurotn.panjea.iva.domain;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "cont_righe_iva")
@NamedQueries({
        @NamedQuery(name = "RigaIva.caricaTotaliForCalendarioCorrispettivo", query = "select new it.eurotn.panjea.corrispettivi.domain.TotaliCodiceIvaDTO(r.codiceIva.codice,r.codiceIva.descrizioneRegistro,sum(r.imponibile.importoInValutaAzienda + r.imposta.importoInValutaAzienda)) "
                + " from RigaIva r where r.areaIva.areaContabile.documento.codiceAzienda = :paramCodiceAzienda "
                + " and r.areaIva.areaContabile.tipoAreaContabile.tipoDocumento.id = :paramTipoDocumentoId "
                + " and r.areaIva.areaContabile.tipoAreaContabile.stampaRegistroIva=true "
                + " and (r.areaIva.areaContabile.dataRegistrazione >= :paramDataIni) "
                + " and (r.areaIva.areaContabile.dataRegistrazione <= :paramDataFin) "
                + " group by r.codiceIva.codice "),
        @NamedQuery(name = "RigaIva.verificaPresenzaRigheTipoCorrispettivo", query = "select count(id) from RigaIva r where r.areaIva.areaContabile.documento.codiceAzienda=:paramCodiceAzienda and r.areaIva.registroIva.id=:paramIdRegistroIva"),
        @NamedQuery(name = "RigaIva.eliminaRigheIvaByAreaIva", query = " delete from RigaIva r where r.areaIva = :paramAreaIva ") })
public class RigaIva extends EntityBase implements java.io.Serializable {

    private static final long serialVersionUID = -6253033361908708486L;

    private static Logger logger = Logger.getLogger(RigaIva.class);

    /**
     * scale da utilizzare per tutti i calcoli legati ai calcoli dei valori fiscali.
     */
    public static final int SCALE_FISCALE = 2;

    private long ordinamento;

    @ManyToOne(optional = false)
    private AreaIva areaIva;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaImposta", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaImposta", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioImposta", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaImposta", length = 3) ) })
    private Importo imposta;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaCollegata", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaCollegata", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioCollegata", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaCollegata", length = 3) ) })
    private Importo impostaCollegata;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaImponibile", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaImponibile", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioImponibile", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaImponibile", length = 3) ) })
    private Importo imponibile;

    @ManyToOne
    private CodiceIva codiceIva;

    @ManyToOne
    private CodiceIva codiceIvaCollegato;

    @Column(length = 200)
    private String descrizione;

    @Transient
    private Importo imponibileVisualizzato = null;

    @Transient
    private Importo impostaVisualizzata = null;

    @Transient
    private Importo impostaCollegataVisualizzata = null;

    {
        this.areaIva = new AreaIva();
        // come ordinamento delle righe iva uso la data di inserimento
        this.ordinamento = Calendar.getInstance().getTimeInMillis();
        this.imponibile = new Importo();
        this.imposta = new Importo();
        this.impostaCollegata = new Importo();
    }

    /**
     * Default constructor.
     */
    public RigaIva() {
        super();
    }

    /**
     * Calcolo dell'attributo imposta per default il valore di arrotondamento è fissato a 2.
     */
    public void calcolaImposta() {
        logger.debug("--> Enter calcolaImposta");
        // se non e' ancora stato settato il codice iva o l'imponibile non
        // calcolo l'imposta
        impostaVisualizzata = null;
        if (getCodiceIva() == null || getImponibile() == null) {
            return;
        }
        // potrebbero essere a null i valori BigDecimal
        if (getImponibile().getImportoInValuta() == null) {
            return;
        }
        logger.debug("--> calcolo imposta ");
        getImposta().setTassoDiCambio(getImponibile().getTassoDiCambio());
        getImposta().setCodiceValuta(getImponibile().getCodiceValuta());

        BigDecimal impostaCalcolata = getImponibile().getImportoInValuta()
                .multiply(getCodiceIva().getPercApplicazione())
                .divide(Importo.HUNDRED, SCALE_FISCALE, BigDecimal.ROUND_HALF_UP);
        getImposta().setImportoInValuta(impostaCalcolata);
        getImposta().calcolaImportoValutaAzienda(SCALE_FISCALE);

        logger.debug("--> Exit calcolaImposta");
    }

    /**
     * Calcolo dell'attributo imposta collegata per default il valore di arrotondamento è fissato a 2.
     */
    public void calcolaImpostaCollegata() {
        // se non e' ancora stato settato il codice iva collegato o l'imponibile
        // non calcolo l'imposta collegata
        // TODO modificare perché il calcolo dell'imposta deve essere basato
        // esclusivamente sull'importo in valuta azienda
        if ((getCodiceIvaCollegato() == null) || (getImponibile() == null)) {
            return;
        } else {
            if (getImponibile().getImportoInValuta() == null) {
                return;
            }
            BigDecimal impostaCollegataCalcolata = getImponibile().getImportoInValuta()
                    .multiply(getCodiceIvaCollegato().getPercApplicazione())
                    .divide(Importo.HUNDRED, SCALE_FISCALE, BigDecimal.ROUND_HALF_UP);
            getImpostaCollegata().setImportoInValuta(impostaCollegataCalcolata);
            getImpostaCollegata().calcolaImportoValutaAzienda(SCALE_FISCALE);
            // setImpostaCollegata(getImponibile().multiply(getCodiceIvaCollegato().getPercApplicazione(),
            // SCALE_FISCALE)
            // .divide(Importo.HUNDRED, SCALE_FISCALE,
            // BigDecimal.ROUND_HALF_UP));
        }
        impostaCollegataVisualizzata = null;
    }

    /**
     * @return Returns the areaIva.
     */
    public AreaIva getAreaIva() {
        return areaIva;
    }

    /**
     * @return Returns the codiceIva.
     */
    public CodiceIva getCodiceIva() {
        return codiceIva;
    }

    /**
     * @return Returns the codiceIvaCollegato.
     */
    public CodiceIva getCodiceIvaCollegato() {
        return codiceIvaCollegato;
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return Returns the imponibile.
     */
    public Importo getImponibile() {
        return imponibile;
    }

    /**
     * Restituisce l'oggetto imponibileVisualizzato; se l'oggetto imponibileVisualizzato non è istanziato provvede ad
     * inizializzarlo attraverso l'oggetto imponibile, nel caso in cui il tipo documento collegato e' nota di credito
     * restituisce l'imponibile con segno invertito.
     * 
     * @return l'imponibile o il suo inverso se tipo documento e' nota di credito
     */
    public Importo getImponibileVisualizzato() {
        if (imponibileVisualizzato == null) {
            if (isNotaCredito()) {
                imponibileVisualizzato = imponibile.negate();
            } else {
                imponibileVisualizzato = imponibile;
            }
        }
        return imponibileVisualizzato;
    }

    /**
     * @return Returns the imposta.
     */
    public Importo getImposta() {
        return imposta;
    }

    /**
     * @return Returns the impostaCollegata.
     */
    public Importo getImpostaCollegata() {
        return impostaCollegata;
    }

    /**
     * Restituisce l'imposta collegata; se l'oggetto impostaCollegataVisualizzata non è istanziata viene istanziata
     * attraverso l'attributo impostaCollegata, nel caso in cui il tipo documento collegato e' nota di credito
     * restituisce l'imposta collegata con segno invertito.
     * 
     * @return l'imposta collegata o il suo inverso se tipo documento e' nota di credito
     */
    public Importo getImpostaCollegataVisualizzata() {
        if (impostaCollegataVisualizzata == null) {
            if (isNotaCredito()) {
                impostaCollegataVisualizzata = impostaCollegata.negate();
            } else {
                impostaCollegataVisualizzata = impostaCollegata;
            }
        }
        return impostaCollegataVisualizzata;
    }

    /**
     * Restituisce l'imposta; se l'oggetto impostaVisualizzata non è istanziata viene creata attraverso l'attributo
     * imposta, nel caso in cui il tipo documento collegato e' nota di credito restituisce l'imposta con segno
     * invertito.
     * 
     * @return l'imposta o il suo inverso se tipo documento e' nota di credito
     */
    public Importo getImpostaVisualizzata() {
        if (impostaVisualizzata == null) {
            if (isNotaCredito()) {
                impostaVisualizzata = imposta.negate();
            } else {
                impostaVisualizzata = imposta;
            }
        }
        return impostaVisualizzata;
    }

    /**
     * @return the ordinamento
     */
    public long getOrdinamento() {
        return ordinamento;
    }

    /**
     * Verifica se il documento collegato e' di tipo documento con caratteristica nota di credito.
     * 
     * @return true se e' nota di credito, false altrimenti
     */
    public boolean isNotaCredito() {
        return this.areaIva != null && this.areaIva.getDocumento() != null
                && this.areaIva.getDocumento().getTipoDocumento() != null
                && this.areaIva.getDocumento().getTipoDocumento().isNotaCreditoEnable();
    }

    /**
     * @param areaIva
     *            the areaIva to set
     */
    public void setAreaIva(AreaIva areaIva) {
        this.areaIva = areaIva;
    }

    /**
     * @param codiceIva
     *            the codiceIva to set
     */
    public void setCodiceIva(CodiceIva codiceIva) {
        this.codiceIva = codiceIva;
        calcolaImposta();
    }

    /**
     * @param codiceIvaCollegato
     *            the codiceIvaCollegato to set
     */
    public void setCodiceIvaCollegato(CodiceIva codiceIvaCollegato) {
        this.codiceIvaCollegato = codiceIvaCollegato;
        calcolaImpostaCollegata();
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Set dell'imponibile che calcola i valori di imposta e imposta collegata se e' presente un codice iva.
     * 
     * @param imponibile
     *            The imponibile to set.
     */
    public void setImponibile(Importo imponibile) {
        this.imponibile = imponibile;
        calcolaImposta();
        calcolaImpostaCollegata();
    }

    /**
     * Set dell'imponibile visualizzato, se la riga e' collegata ad un tipo documento con caratteristica nota di credito
     * viene invertito il segno in modo da salvare il valore corretto per quel tipo.<br>
     * Il metodo getImponibile invece restituisce sempre il valore salvato per garantire la correttezza nei calcoli per
     * registro iva e liquidazione iva.<br>
     * Per quanto riguarda la parte client il valore deve essere lo stesso inserito dall'utente,cioe' con segno inverso
     * rispetto a quello salvato sul database, risulta quindi necessario usare un rendered per la tabella delle righe
     * iva e cambiare il segno dell'imponibile all'apertura del dialogo di modifica.
     * 
     * @param imponibileVisualizzato
     *            imponibileVisualizzato
     */
    public void setImponibileVisualizzato(Importo imponibileVisualizzato) {
        this.imponibileVisualizzato = imponibileVisualizzato;
        this.impostaVisualizzata = null;
        Importo importo;
        importo = this.imponibileVisualizzato.clone();
        if (isNotaCredito() && importo.getImportoInValuta() != null) {
            importo = importo.negate();
        }
        setImponibile(importo);
    }

    /**
     * @param imposta
     *            The imposta to set.
     */
    public void setImposta(Importo imposta) {
        this.imposta = imposta;
    }

    /**
     * @param impostaCollegata
     *            The impostaCollegata to set.
     */
    public void setImpostaCollegata(Importo impostaCollegata) {
        this.impostaCollegata = impostaCollegata;
    }

    /**
     * Setter dell'oggetto Importo di impostaCollegataVisualizzata.<br>
     * il metodo set provvede inoltre ad aggiornare l'attributo impostaCollegata<br>
     * viene invertito il segno se il documento è una nota di credito perchè si presuppone che l'oggetto
     * impostaCollegataVisualizzata abbia al suo interno gli importi positivi che per questo tipo di documento devono
     * essere negativi
     * 
     * @param impostaCollegataVisualizzata
     *            impostaCollegataVisualizzata
     */
    public void setImpostaCollegataVisualizzata(Importo impostaCollegataVisualizzata) {
        this.impostaCollegataVisualizzata = impostaCollegataVisualizzata;
        Importo importo;
        importo = this.impostaCollegataVisualizzata.clone();
        if (isNotaCredito()) {
            importo = importo.negate();
        }
        setImpostaCollegata(importo);
    }

    /**
     * Setter dell'oggetto Importo di impostaVisualizzata.<br>
     * il metodo set provvede inoltre ad aggiornare l'attributo imposta<br>
     * viene invertito il segno se il documento è una nota di credito perchè si presuppone che l'oggetto
     * impostaVisualizzata abbia al suo interno gli importi positivi che per questo tipo di documento devono essere
     * negativi
     * 
     * @param impostaVisualizzata
     *            impostaVisualizzata
     */
    public void setImpostaVisualizzata(Importo impostaVisualizzata) {
        this.impostaVisualizzata = impostaVisualizzata;
        Importo importo;
        importo = this.impostaVisualizzata.clone();

        if (isNotaCredito()) {
            importo = importo.negate();
        }
        setImposta(importo);
    }

    /**
     * @param ordinamento
     *            the ordinamento to set
     */
    public void setOrdinamento(long ordinamento) {
        this.ordinamento = ordinamento;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("RigaIva[");
        buffer.append(super.toString());
        buffer.append(" codiceIva = ").append(codiceIva != null ? codiceIva.getId() : null);
        buffer.append(" descrizione = ").append(descrizione);
        buffer.append(" importo = ").append(imponibile);
        buffer.append(" imposta = ").append(imposta);
        buffer.append("]");
        return buffer.toString();
    }
}
