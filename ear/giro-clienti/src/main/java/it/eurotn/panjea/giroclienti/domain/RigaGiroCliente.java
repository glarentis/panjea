package it.eurotn.panjea.giroclienti.domain;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.NotAudited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Contatto;
import it.eurotn.panjea.anagrafica.domain.ContattoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;

@Entity
@Table(name = "gcli_riga_giro")
@NamedQueries({
        @NamedQuery(name = "RigaGiroCliente.deleteByUtenteEGiorno", query = "delete from RigaGiroCliente rgc where rgc.utente.id = :idUtente and rgc.giorno = :giorno"),
        @NamedQuery(name = "RigaGiroCliente.deleteByUtente", query = "delete from RigaGiroCliente rgc where rgc.utente.id = :idUtente") })
public class RigaGiroCliente extends EntityBase {

    private static final long serialVersionUID = 7311686575962795188L;

    @Temporal(TemporalType.DATE)
    private Date data;

    @ManyToOne(optional = false)
    private SedeEntita sedeEntita;

    @ManyToOne(optional = false)
    private Utente utente;

    private Giorni giorno;

    @Temporal(TemporalType.TIME)
    private Date ora;

    @ManyToOne
    private AreaOrdine areaOrdine;

    private boolean eseguito;

    @Transient
    private Set<ContattoSedeEntita> contatti;

    @Formula("(select coalesce(COUNT(rd.id),0) from ordi_riga_distinta_carico rd inner join ordi_righe_ordine ro on ro.id = rd.rigaArticolo_id where ro.areaOrdine_id =areaOrdine_id)")
    @NotAudited
    private int numeroRigheOrdineInMissione;

    {
        eseguito = Boolean.FALSE;
    }

    /**
     * Costruttore.
     */
    public RigaGiroCliente() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param giroSedeCliente
     *            giro sede
     * @param data
     *            data
     */
    public RigaGiroCliente(final GiroSedeCliente giroSedeCliente, final Date data) {
        super();
        this.data = data;
        this.sedeEntita = giroSedeCliente.getSedeEntita();
        this.utente = giroSedeCliente.getUtente();
        this.giorno = giroSedeCliente.getGiorno();
        this.ora = giroSedeCliente.getOra();
    }

    /**
     * @return the areaOrdine
     */
    public AreaOrdine getAreaOrdine() {
        return areaOrdine;
    }

    /**
     * @return the contatti
     */
    public Set<ContattoSedeEntita> getContatti() {
        if (contatti == null) {
            contatti = new TreeSet<ContattoSedeEntita>();

            if (!StringUtils.isBlank(sedeEntita.getSede().getTelefono())) {
                Contatto contattoSede = new Contatto();
                contattoSede.setNome("Sede");
                contattoSede.setInterno(sedeEntita.getSede().getTelefono());

                ContattoSedeEntita contattoSedeEntita = new ContattoSedeEntita();
                contattoSedeEntita.setContatto(contattoSede);
                contattoSedeEntita.setSedeEntita(sedeEntita);
                contatti.add(contattoSedeEntita);
            }

            if (sedeEntita.getContatti() != null) {
                contatti.addAll(sedeEntita.getContatti());
            }
        }

        return contatti;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return descrizione contatti
     */
    public String getDescrizioneContatti() {
        String newLine = "";
        StringBuilder sb = new StringBuilder(200);
        sb.append("<html>");
        for (ContattoSedeEntita contattoSedeEntita : getContatti()) {
            sb.append("<b>");
            sb.append(contattoSedeEntita.getContatto().getNome());
            sb.append("</b> Tel.");
            sb.append(contattoSedeEntita.getContatto().getInterno());
            if (!StringUtils.isBlank(contattoSedeEntita.getContatto().getCellulare())) {
                sb.append(" Cell.");
                sb.append(contattoSedeEntita.getContatto().getCellulare());
            }
            sb.append("     ");
            sb.append(newLine);
            newLine = StringUtils.isEmpty(newLine) ? "<br>" : "";
        }
        sb.append("</html>");

        return sb.toString();
    }

    /**
     * @return descrizione stato del giro
     */
    public String getDescrizioneStatoGiro() {
        StringBuilder sb = new StringBuilder();

        if (!eseguito) {
            sb.append("Non contattato");
        } else {
            if (areaOrdine != null) {
                sb.append(areaOrdine.getDocumento().getTipoDocumento().getCodice());
                sb.append(" n. ");
                sb.append(areaOrdine.getDocumento().getCodice().getCodice());
            }
        }

        return sb.toString();
    }

    /**
     * @return the giorno
     */
    public Giorni getGiorno() {
        return giorno;
    }

    /**
     * @return the numeroRigheOrdineInMissione
     */
    public int getNumeroRigheOrdineInMissione() {
        return numeroRigheOrdineInMissione;
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
     * @return the eseguito
     */
    public boolean isEseguito() {
        return eseguito;
    }

    /**
     * @param areaOrdine
     *            the areaOrdine to set
     */
    public void setAreaOrdine(AreaOrdine areaOrdine) {
        this.areaOrdine = areaOrdine;
    }

    /**
     * @param contatti
     *            the contatti to set
     */
    public void setContatti(Set<ContattoSedeEntita> contatti) {
        throw new UnsupportedOperationException();
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param eseguito
     *            the eseguito to set
     */
    public void setEseguito(boolean eseguito) {
        this.eseguito = eseguito;
    }

    /**
     * @param giorno
     *            the giorno to set
     */
    public void setGiorno(Giorni giorno) {
        this.giorno = giorno;
    }

    /**
     * @param numeroRigheOrdineInMissione
     *            the numeroRigheOrdineInMissione to set
     */
    public void setNumeroRigheOrdineInMissione(int numeroRigheOrdineInMissione) {
        this.numeroRigheOrdineInMissione = numeroRigheOrdineInMissione;
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
