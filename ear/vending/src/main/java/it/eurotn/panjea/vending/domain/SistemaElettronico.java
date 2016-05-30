package it.eurotn.panjea.vending.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "vend_sistemi_elettronici")
public class SistemaElettronico extends EntityBase {
    public enum Baud {
        _2400, _4800, _9600, _19200, _38400, _57600, _115200
    }

    public enum TipoComunicazione {
        NESSUNO, CONTATORE, ELKEY, DDCMP, ZIP, MIZIP, COGES, COGES_TITANIUM, MEI_CF7000, FAGE, CPU, PAYTEC, ELKEY_ATTO, FAGE_GIOTTO, NRI, _15, _16, _17, _18
    }

    public enum TipoSistemaElettronico {
        GETTONIERA, SISTEMA_DI_PAGAMENTO, LETTORE_BANCONOTE
    }

    private TipoSistemaElettronico tipo;

    @Column(length = 10)
    private String codice;

    @Column(length = 30)
    private String descrizione;

    private boolean caricaChiave;

    private boolean chiave;
    private boolean cassetta;

    private boolean resto;
    private TipoComunicazione tipoComunicazione;

    private Integer rx;
    private Integer tx;

    private Baud baud;

    private boolean start9600;

    /**
     * Costruttore
     */
    public SistemaElettronico() {
        baud = Baud._115200;
        tipoComunicazione = TipoComunicazione.NESSUNO;
        tipo = TipoSistemaElettronico.GETTONIERA;

        caricaChiave = false;
        chiave = false;
        cassetta = false;

        resto = false;

        start9600 = false;
    }

    /**
     * @return Returns the baud.
     */
    public Baud getBaud() {
        return baud;
    }

    /**
     * @return Returns the codice.
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return Returns the rx.
     */
    public Integer getRx() {
        return rx;
    }

    /**
     * @return Returns the tipo.
     */
    public TipoSistemaElettronico getTipo() {
        return tipo;
    }

    /**
     * @return Returns the tipoComunicazione.
     */
    public TipoComunicazione getTipoComunicazione() {
        return tipoComunicazione;
    }

    /**
     * @return Returns the tx.
     */
    public Integer getTx() {
        return tx;
    }

    /**
     * @return the caricaChiave
     */
    public boolean isCaricaChiave() {
        return caricaChiave;
    }

    /**
     * @return the cassetta
     */
    public boolean isCassetta() {
        return cassetta;
    }

    /**
     * @return the chiave
     */
    public boolean isChiave() {
        return chiave;
    }

    /**
     * @return the resto
     */
    public boolean isResto() {
        return resto;
    }

    /**
     * @return the start9600
     */
    public boolean isStart9600() {
        return start9600;
    }

    /**
     * @param baud
     *            The baud to set.
     */
    public void setBaud(Baud baud) {
        this.baud = baud;
    }

    /**
     * @param caricaChiave
     *            the caricaChiave to set
     */
    public void setCaricaChiave(boolean caricaChiave) {
        this.caricaChiave = caricaChiave;
    }

    /**
     * @param cassetta
     *            the cassetta to set
     */
    public void setCassetta(boolean cassetta) {
        this.cassetta = cassetta;
    }

    /**
     * @param chiave
     *            the chiave to set
     */
    public void setChiave(boolean chiave) {
        this.chiave = chiave;
    }

    /**
     * @param codice
     *            The codice to set.
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param resto
     *            the resto to set
     */
    public void setResto(boolean resto) {
        this.resto = resto;
    }

    /**
     * @param rx
     *            The rx to set.
     */
    public void setRx(Integer rx) {
        this.rx = rx;
    }

    /**
     * @param start9600
     *            the start9600 to set
     */
    public void setStart9600(boolean start9600) {
        this.start9600 = start9600;
    }

    /**
     * @param tipo
     *            The tipo to set.
     */
    public void setTipo(TipoSistemaElettronico tipo) {
        this.tipo = tipo;
    }

    /**
     * @param tipoComunicazione
     *            The tipoComunicazione to set.
     */
    public void setTipoComunicazione(TipoComunicazione tipoComunicazione) {
        this.tipoComunicazione = tipoComunicazione;
    }

    /**
     * @param tx
     *            The tx to set.
     */
    public void setTx(Integer tx) {
        this.tx = tx;
    }

}