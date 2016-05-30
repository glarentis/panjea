package it.eurotn.panjea.documenti.util;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseFattura;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica.TipoSpedizioneDocumenti;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Utente;

public class MovimentoSpedizioneDTO implements Serializable {

    private static final long serialVersionUID = -7230273912767940402L;

    private IAreaDocumento areaDocumento;

    private SedeEntita sedeSpedizione;
    private SedeEntita sedePrincipale;
    private Utente utente;
    private DatiMail datiMailUtente;
    private String indirizzoMailMovimento;
    private TipoSpedizioneDocumenti tipoSpedizioneDocumenti;
    private String esitoSpedizione;

    /**
     * Costruttore.
     */
    public MovimentoSpedizioneDTO() {
        super();
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
        MovimentoSpedizioneDTO other = (MovimentoSpedizioneDTO) obj;
        if (areaDocumento == null) {
            if (other.areaDocumento != null) {
                return false;
            }
        } else if (areaDocumento.getClass() != other.areaDocumento.getClass()
                || !areaDocumento.equals(other.areaDocumento)) {
            return false;
        }
        return true;
    }

    /**
     * @return the areaDocumento
     */
    public IAreaDocumento getAreaDocumento() {
        return areaDocumento;
    }

    /**
     * @return datiMail di spedizione
     */
    public DatiMail getDatiMailUtente() {

        if ((datiMailUtente == null || datiMailUtente.isNew()) && utente != null) {

            boolean usaPec = false;
            if (sedeSpedizione != null && !sedeSpedizione.isNew()) {
                usaPec = sedeSpedizione.getSede().isSpedizioneDocumentiViaPEC();
            } else if (sedePrincipale != null) {
                usaPec = sedePrincipale.getSede().isSpedizioneDocumentiViaPEC();
            }

            if (usaPec) {
                datiMailUtente = utente.getDatiMailPredefinitiPec();
            }

            // avvaloro i dati con quelli predefiniti se sono null anche se usa la pec perchè
            // significa che non c'è
            // nessun
            // account PEC configurato
            if (datiMailUtente == null || datiMailUtente.isNew()) {
                datiMailUtente = utente.getDatiMailPredefiniti();
            }
        }

        return datiMailUtente;
    }

    /**
     * @return the documento
     */
    public Documento getDocumento() {
        return areaDocumento != null ? areaDocumento.getDocumento() : null;
    }

    /**
     * @return the esitoSpedizione
     */
    public String getEsitoSpedizione() {
        return esitoSpedizione;
    }

    /**
     * @return the indirizzoMailMovimento
     */
    public String getIndirizzoMailMovimento() {
        if (indirizzoMailMovimento == null) {
            indirizzoMailMovimento = getIndirizzoMailSpedizione();
        }
        return indirizzoMailMovimento;
    }

    /**
     * @return indirizzo di spedizione del movimento
     */
    private String getIndirizzoMailSpedizione() {

        if (areaDocumento == null) {
            return null;
        }

        String indirizzo = "";
        if (sedeSpedizione != null && !sedeSpedizione.isNew()) {
            indirizzo = sedeSpedizione.getSede().getIndirizzoMailSpedizione();
        } else if (sedePrincipale != null) {
            indirizzo = sedePrincipale.getSede().getIndirizzoMailSpedizione();
        }

        return indirizzo;
    }

    /**
     * @return the sedePrincipale
     */
    public SedeEntita getSedePrincipale() {
        return sedePrincipale;
    }

    /**
     * @return the sedeSpedizione
     */
    public SedeEntita getSedeSpedizione() {
        return sedeSpedizione;
    }

    /**
     * @return the tipoDocumento
     */
    public TipoDocumento getTipoDocumento() {
        if (areaDocumento != null) {
            return areaDocumento.getTipoAreaDocumento().getTipoDocumento();
        }

        return null;
    }

    /**
     * @return tipo spedizione del documento
     */
    public TipoSpedizioneDocumenti getTipoSpedizioneDocumenti() {
        if (tipoSpedizioneDocumenti == null && areaDocumento != null) {
            tipoSpedizioneDocumenti = TipoSpedizioneDocumenti.STAMPA;
            if (sedeSpedizione != null && !sedeSpedizione.isNew()) {
                tipoSpedizioneDocumenti = sedeSpedizione.getSede().getTipoSpedizioneDocumenti();
            } else if (sedePrincipale != null) {
                tipoSpedizioneDocumenti = sedePrincipale.getSede().getTipoSpedizioneDocumenti();
            }
        }

        return tipoSpedizioneDocumenti;
    }

    /**
     * @return the utente
     */
    public Utente getUtente() {
        return utente;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((areaDocumento == null) ? 0 : areaDocumento.hashCode());
        return result;
    }

    /**
     *
     * @return true se i dati per la spedizione/stampa sono tutti presenti
     */
    public boolean isDatiSpedizioneValidi() {
        return tipoSpedizioneDocumenti == TipoSpedizioneDocumenti.EMAIL && datiMailUtente != null
                && datiMailUtente.isValid() && !StringUtils.isBlank(indirizzoMailMovimento);
    }

    /**
     *
     * @return true se il movimento deve essere spedito tramite fatturaPA
     */
    public boolean isFatturaPA() {
        return areaDocumento instanceof AreaMagazzino
                && areaDocumento.getTipoAreaDocumento().getTipoDocumento()
                        .getClasseTipoDocumentoInstance() instanceof ClasseFattura
                && areaDocumento.getDocumento().getEntita() != null
                && areaDocumento.getDocumento().getEntita().isFatturazionePA();
    }

    /**
     * @param areaDocumento
     *            the areaDocumento to set
     */
    public void setAreaDocumento(IAreaDocumento areaDocumento) {
        this.areaDocumento = areaDocumento;
    }

    /**
     * @param datiGeografici
     *            the datiGeografici to set
     */
    public void setDatiGeograficiSedeSpedizione(DatiGeografici datiGeografici) {
        this.sedeSpedizione.getSede().setDatiGeografici(datiGeografici);
    }

    /**
     * @param datiMailUtente
     *            the datiMailUtente to set
     */
    public void setDatiMailUtente(DatiMail datiMailUtente) {
        this.datiMailUtente = datiMailUtente;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizioneSedeSpedizione(String descrizione) {
        this.sedeSpedizione.getSede().setDescrizione(descrizione);
    }

    /**
     * @param esitoSpedizione
     *            the esitoSpedizione to set
     */
    public void setEsitoSpedizione(String esitoSpedizione) {
        this.esitoSpedizione = esitoSpedizione;
    }

    /**
     * @param id
     *            the IdSedeSpedizione to set
     */
    public void setIdSedeSpedizione(Integer id) {
        this.sedeSpedizione = new SedeEntita();
        this.sedeSpedizione.setId(id);
    }

    /**
     * @param indirizzoMailMovimento
     *            the indirizzoMailMovimento to set
     */
    public void setIndirizzoMailMovimento(String indirizzoMailMovimento) {
        this.indirizzoMailMovimento = indirizzoMailMovimento;
    }

    /**
     * @param mail
     *            the IndirizzoMail to set
     */
    public void setIndirizzoMailSedeSpedizione(String mail) {
        this.sedeSpedizione.getSede().setIndirizzoMail(mail);
    }

    /**
     * @param mail
     *            the mail to set
     */
    public void setIndirizzoMailSpedizioneSedeSpedizione(String mail) {
        this.sedeSpedizione.getSede().setIndirizzoMailSpedizione(mail);
    }

    /**
     * @param pec
     *            the IndirizzoPec to set
     */
    public void setIndirizzoPecSedeSpedizione(String pec) {
        this.sedeSpedizione.getSede().setIndirizzoPEC(pec);
    }

    /**
     * @param indirizzo
     *            the IndirizzoSede to set
     */
    public void setIndirizzoSedeSpedizione(String indirizzo) {
        this.sedeSpedizione.getSede().setIndirizzo(indirizzo);
    }

    /**
     * @param sedePrincipale
     *            the sedePrincipale to set
     */
    public void setSedePrincipale(SedeEntita sedePrincipale) {
        this.sedePrincipale = sedePrincipale;
    }

    /**
     * @param spedizioneDocumentiViaPECSedeSpedizione
     *            the spedizioneDocumentiViaPECSedeSpedizione to set
     */
    public void setSpedizioneDocumentiViaPECSedeSpedizione(Boolean spedizioneDocumentiViaPECSedeSpedizione) {
        if (spedizioneDocumentiViaPECSedeSpedizione != null) {
            this.sedeSpedizione.getSede().setSpedizioneDocumentiViaPEC(spedizioneDocumentiViaPECSedeSpedizione);
        }
    }

    /**
     * @param tipoSpedizioneDocumenti
     *            the tipoSpedizioneDocumenti to set
     */
    public void setTipoSpedizioneDocumenti(TipoSpedizioneDocumenti tipoSpedizioneDocumenti) {
        this.tipoSpedizioneDocumenti = tipoSpedizioneDocumenti;
    }

    /**
     * @param tipoSpedizioneDocumentiSedeSpedizione
     *            the tipoSpedizioneDocumentiSedeSpedizione to set
     */
    public void setTipoSpedizioneDocumentiSedeSpedizione(
            TipoSpedizioneDocumenti tipoSpedizioneDocumentiSedeSpedizione) {
        this.sedeSpedizione.getSede().setTipoSpedizioneDocumenti(tipoSpedizioneDocumentiSedeSpedizione);
    }

    /**
     * @param utente
     *            the utente to set
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

}
