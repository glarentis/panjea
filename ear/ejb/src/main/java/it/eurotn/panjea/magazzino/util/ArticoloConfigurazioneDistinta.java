package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.Set;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;

public class ArticoloConfigurazioneDistinta implements Serializable {

    private static final long serialVersionUID = 606027020041746189L;
    private Set<Componente> componenti;
    private Set<Componente> distinte;
    private Set<FaseLavorazioneArticolo> fasi;
    private String codice;
    private String descrizione;
    private Integer idDistinta;
    private Integer numeroDecimaliQta;
    private ConfigurazioneDistinta configurazioneDistinta;

    /**
     * Costruttore.
     */
    public ArticoloConfigurazioneDistinta() {
        super();
    }

    /**
     * 
     * @param componenti
     *            componenti dell'articolo
     * @param distinte
     *            distinte padri dell'articolo
     * @param fasi
     *            fasi dell'articolo
     * @param idDistinta
     *            dell'articolo legato alla distinta
     * @param codice
     *            codice articolo
     * @param descrizione
     *            descrizione articolo
     * @param numeroDecimaliQta
     *            numeroDecimaliQta
     * @param configurazioneDistinta
     *            configurazione della distinta
     */
    public ArticoloConfigurazioneDistinta(final Set<Componente> componenti, final Set<Componente> distinte,
            final Set<FaseLavorazioneArticolo> fasi, final Integer idDistinta, final String codice,
            final String descrizione, final Integer numeroDecimaliQta,
            final ConfigurazioneDistinta configurazioneDistinta) {
        super();
        this.componenti = componenti;
        this.distinte = distinte;
        this.fasi = fasi;
        this.idDistinta = idDistinta;
        this.codice = codice;
        this.descrizione = descrizione;
        this.numeroDecimaliQta = numeroDecimaliQta;
        this.configurazioneDistinta = configurazioneDistinta;
    }

    /**
     * @return Returns the codice.
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return Returns the componenti.
     */
    public Set<Componente> getComponenti() {
        return componenti;
    }

    /**
     * @return Returns the configurazioneDistinta.
     */
    public ConfigurazioneDistinta getConfigurazioneDistinta() {
        return configurazioneDistinta;
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return Returns the distinte.
     */
    public Set<Componente> getDistinte() {
        return distinte;
    }

    /**
     * @return Returns the fasi.
     */
    public Set<FaseLavorazioneArticolo> getFasi() {
        return fasi;
    }

    /**
     * @return Returns the idDistinta.
     */
    public Integer getIdDistinta() {
        return idDistinta;
    }

    /**
     * @return numeroDecimaliQta
     */
    public Integer getNumeroDecimaliQta() {
        return numeroDecimaliQta;
    }

}
