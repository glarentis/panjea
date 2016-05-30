package it.eurotn.panjea.anagrafica.util;

import java.io.Serializable;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;

/**
 * Contiene i dati per renderizzare un'entità in tabella.<br/>
 * L'entità può essere un Cliente/Fornitore, l'azienda oppure un rapporto bancario
 *
 * @author giangi
 */
public class EntitaDocumento implements Serializable, Comparable<EntitaDocumento> {

    private static final long serialVersionUID = -8856574057642633685L;
    private static Logger logger = Logger.getLogger(EntitaDocumento.class);

    private Integer id;
    private Integer idAnagrafica;
    private Integer codice;
    private String codiceString;
    private String descrizione;
    private TipoEntita tipoEntita;

    private String indirizzo;
    private String nazione;
    private String livelloAmministrativo1;
    private String livelloAmministrativo2;
    private String livelloAmministrativo3;
    private String livelloAmministrativo4;
    private String localita;
    private String cap;

    private AnagraficaLite anagrafica;

    /**
     * Costruttore.
     */
    public EntitaDocumento() {
        super();
    }

    /**
     * Crea una {@link EntitaDocumento} da un documento.
     *
     * @param documento
     *            documento di riferimento
     */
    public EntitaDocumento(final Documento documento) {
        super();
        if (documento != null && documento.getId() != null) {
            this.setTipoEntita(documento.getTipoDocumento().getTipoEntita());
            switch (documento.getTipoDocumento().getTipoEntita()) {
            case AZIENDA:
                this.setId(1);
                this.setDescrizione(documento.getCodiceAzienda());
                break;
            case BANCA:
                if (documento.getRapportoBancarioAzienda() != null) {
                    this.setId(documento.getRapportoBancarioAzienda().getId());
                    this.setDescrizione(documento.getRapportoBancarioAzienda().getDescrizione());
                }
                break;
            case AGENTE:
            case CLIENTE:
            case FORNITORE:
            case VETTORE:
                if (documento.getEntita() != null) {
                    this.setId(documento.getEntita().getId());
                    this.setCodice(documento.getEntita().getCodice());
                    this.setDescrizione(documento.getEntita().getAnagrafica().getDenominazione());
                }
                break;
            default:
                logger.error("-->errore nel render di entità documento. TipoEntità non impostato: "
                        + documento.getTipoDocumento().getTipoEntita());
            }
        }
    }

    @Override
    public int compareTo(EntitaDocumento o) {
        String desc1 = getDescrizione() != null ? getDescrizione() : "";
        String desc2 = o.getDescrizione() != null ? o.getDescrizione() : "";
        return desc1.compareTo(desc2);
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

        EntitaDocumento other = (EntitaDocumento) obj;
        if (tipoEntita == TipoEntita.AZIENDA) {
            // NPE MAIL, verifico descrizione e other
            String desc = getDescrizione() != null ? getDescrizione() : "";
            String otherDesc = (other != null && other.getDescrizione() != null) ? other.getDescrizione() : "";
            return desc.equalsIgnoreCase(otherDesc);
        }

        if (id == null) {
            return false;
        }

        return id.equals(other.id);
    }

    /**
     * @return the anagrafica
     */
    public AnagraficaLite getAnagrafica() {
        if (anagrafica == null) {
            anagrafica = new AnagraficaLite();
            anagrafica.setId(idAnagrafica);
            anagrafica.setDenominazione(descrizione);
        }
        return anagrafica;
    }

    /**
     * @return the cap
     */
    public String getCap() {
        return cap;
    }

    /**
     * @return the codice
     */
    public Integer getCodice() {
        return codice;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the indirizzo
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * @return the livelloAmministrativo1
     */
    public String getLivelloAmministrativo1() {
        return livelloAmministrativo1;
    }

    /**
     * @return the livelloAmministrativo2
     */
    public String getLivelloAmministrativo2() {
        return livelloAmministrativo2;
    }

    /**
     * @return the livelloAmministrativo3
     */
    public String getLivelloAmministrativo3() {
        return livelloAmministrativo3;
    }

    /**
     * @return the livelloAmministrativo4
     */
    public String getLivelloAmministrativo4() {
        return livelloAmministrativo4;
    }

    /**
     * @return the localita
     */
    public String getLocalita() {
        return localita;
    }

    /**
     * @return the nazione
     */
    public String getNazione() {
        return nazione;
    }

    /**
     * @return the tipoEntita
     */
    public TipoEntita getTipoEntita() {
        return tipoEntita;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (id != null) {
            result = prime * result + id;
        }
        result = prime * result + ((tipoEntita == null) ? 0 : tipoEntita.hashCode());
        return result;
    }

    /**
     * @param cap
     *            the cap to set
     */
    public void setCap(String cap) {
        this.cap = cap;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(Integer codice) {
        this.codice = codice;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param idAnagrafica
     *            the idAnagrafica to set
     */
    public void setIdAnagrafica(Integer idAnagrafica) {
        this.idAnagrafica = idAnagrafica;
    }

    /**
     * @param indirizzo
     *            the indirizzo to set
     */
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    /**
     * @param livelloAmministrativo1
     *            the livelloAmministrativo1 to set
     */
    public void setLivelloAmministrativo1(String livelloAmministrativo1) {
        this.livelloAmministrativo1 = livelloAmministrativo1;
    }

    /**
     * @param livelloAmministrativo2
     *            the livelloAmministrativo2 to set
     */
    public void setLivelloAmministrativo2(String livelloAmministrativo2) {
        this.livelloAmministrativo2 = livelloAmministrativo2;
    }

    /**
     * @param livelloAmministrativo3
     *            the livelloAmministrativo3 to set
     */
    public void setLivelloAmministrativo3(String livelloAmministrativo3) {
        this.livelloAmministrativo3 = livelloAmministrativo3;
    }

    /**
     * @param livelloAmministrativo4
     *            the livelloAmministrativo4 to set
     */
    public void setLivelloAmministrativo4(String livelloAmministrativo4) {
        this.livelloAmministrativo4 = livelloAmministrativo4;
    }

    /**
     * @param localita
     *            the localita to set
     */
    public void setLocalita(String localita) {
        this.localita = localita;
    }

    /**
     * @param nazione
     *            the nazione to set
     */
    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    /**
     * @param tipoEntita
     *            the tipoEntita to set
     */
    public void setTipoEntita(String tipoEntita) {
        if ("C".equals(tipoEntita)) {
            setTipoEntita(TipoEntita.CLIENTE);
        } else if ("CP".equals(tipoEntita)) {
            setTipoEntita(TipoEntita.CLIENTE_POTENZIALE);
        } else if ("F".equals(tipoEntita)) {
            setTipoEntita(TipoEntita.FORNITORE);
        } else if ("V".equals(tipoEntita)) {
            setTipoEntita(TipoEntita.VETTORE);
        } else if ("A".equals(tipoEntita)) {
            setTipoEntita(TipoEntita.AGENTE);
        }
    }

    /**
     * @param tipoEntita
     *            the tipoEntita to set
     */
    public void setTipoEntita(TipoEntita tipoEntita) {
        this.tipoEntita = tipoEntita;
    }

    @Override
    public String toString() {
        return "EntitaDocumento [" + codice + " , " + descrizione + ", tipoEntita=" + tipoEntita + "]";
    }

}
