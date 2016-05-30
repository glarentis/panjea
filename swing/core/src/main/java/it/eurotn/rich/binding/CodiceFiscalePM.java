package it.eurotn.rich.binding;

import it.eurotn.panjea.anagrafica.domain.Sesso;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;

import java.util.Date;
import java.util.Map;

public class CodiceFiscalePM {

    private final Map<String, Object> contextMap;
    private String nome = new String();
    private String cognome = new String();
    private Date dataNascita = new Date();
    private LivelloAmministrativo3 comune = new LivelloAmministrativo3();
    private String codiceFiscale = new String();
    private Sesso sesso;

    public static final String PROP_NOME = "nome";
    public static final String PROP_COGNOME = "cognome";
    public static final String PROP_DATA_NASCITA = "dataNascita";
    public static final String PROP_COMUNE = "comune";
    public static final String PROP_CODICE_FISCALE = "codiceFiscale";
    public static final String PROP_SESSO = "sesso";

    public CodiceFiscalePM(Map<String, Object> context) {
        super();
        this.contextMap = context;
        applyContext();
    }

    private void applyContext() {
        setNome((String) contextMap.get(CodiceFiscaleBinder.NOME_KEY));
        setCognome((String) contextMap.get(CodiceFiscaleBinder.COGNOME_KEY));
        setDataNascita((Date) contextMap.get(CodiceFiscaleBinder.DATA_NASCITA_KEY));
        setComune((LivelloAmministrativo3) contextMap.get(CodiceFiscaleBinder.COMUNE_KEY));
        setSesso((Sesso) contextMap.get(CodiceFiscaleBinder.SESSO_KEY));
        setCodiceFiscale("");
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getCognome() {
        return cognome;
    }

    public LivelloAmministrativo3 getComune() {
        return comune;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public String getNome() {
        return nome;
    }

    public Sesso getSesso() {
        return sesso;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setComune(LivelloAmministrativo3 comune) {
        if (comune == null) {
            comune = new LivelloAmministrativo3();
        }
        this.comune = comune;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSesso(Sesso sesso) {
        this.sesso = sesso;
    }
}
