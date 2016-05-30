package it.eurotn.panjea.giroclienti.domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;

@Entity
@Table(name = "gcli_giro_sede_clienti")
@NamedQueries({
        @NamedQuery(name = "GiroSedeCliente.caricaByEntita", query = "select gsc from GiroSedeCliente gsc where gsc.sedeEntita.entita.id = :idEntita"),
        @NamedQuery(name = "GiroSedeCliente.caricaByUtenteEGiorno", query = "select gsc from GiroSedeCliente gsc where gsc.utente.id = :idUtente and gsc.giorno = :giorno order by gsc.ora") })
public class GiroSedeCliente extends EntityBase {

    private static final long serialVersionUID = -7599070204050096887L;

    @ManyToOne(optional = false)
    private SedeEntita sedeEntita;

    @ManyToOne(optional = false)
    private Utente utente;

    private Giorni giorno;

    @Temporal(TemporalType.TIME)
    private Date ora;

    {
        giorno = Giorni.LUNEDI;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        ora = calendar.getTime();
    }

    /**
     * @return the giorno
     */
    public Giorni getGiorno() {
        return giorno;
    }

    /**
     * @return the ora
     */
    public Date getOra() {
        return ora;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return the utente
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * @param giorno
     *            the giorno to set
     */
    public void setGiorno(Giorni giorno) {
        this.giorno = giorno;
    }

    /**
     * @param ora
     *            the ora to set
     */
    public void setOra(Date ora) {
        this.ora = ora;
    }

    /**
     * @param sedeEntita
     *            the sedeEntita to set
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

    /**
     * @param utente
     *            the utente to set
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

}
