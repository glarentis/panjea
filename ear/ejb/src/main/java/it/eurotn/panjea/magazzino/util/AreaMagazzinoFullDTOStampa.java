package it.eurotn.panjea.magazzino.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita.TipoSede;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniDTO;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.rate.domain.Rata;

/**
 * Area magazzino full DTO che contiene i dati necessari al report.
 *
 * @author Leonardo
 */
public class AreaMagazzinoFullDTOStampa extends AreaMagazzinoFullDTO {

    private static final long serialVersionUID = -4382073622134823040L;

    private SedeAzienda azienda;

    private SedeAzienda sedeAzienda = null;
    private Deposito deposito = null;
    private Deposito depositoOrigine = null;

    private DatiGeografici datiGeograficiDestinatario = null;
    private DatiGeografici datiGeograficiDestinazione = null;

    private TrasportoCura trasportoCura = null;

    private String destinatarioDenominazione = null;

    private String destinatarioDescrizione = null;
    private String destinazioneDenominazione = null;

    private String destinazioneTel = null;
    private String destinazioneFax = null;

    private String vettoreDenominazione = null;

    private String destinatarioIndirizzo = null;

    private String destinazioneIndirizzo = null;

    private String vettoreIndirizzo = null;

    private String destinatarioCap = null;
    private String destinazioneCap = null;

    private String vettoreCap = null;
    private String destinatarioLocalita = null;
    private String destinazioneLocalita = null;

    private String vettoreLocalita = null;
    private String destinatarioProvincia = null;
    private String destinazioneProvincia = null;

    private String destinatarioFax = null;
    private String destinatarioTel = null;
    private String destinatarioEmail = null;
    private String destinatarioPec = null;

    private String vettoreProvincia = null;

    private String vettorePIVA = null;
    private String vettoreCodiceFiscale = null;

    private String vettoreNRIscrizioneAlbo = null;

    private boolean vettorePresente = false;

    private Set<RapportoBancarioSedeEntita> rapportiBancariEntita = new HashSet<RapportoBancarioSedeEntita>();
    private int maxNumeroDecimaliQta;
    private int maxNumeroDecimaliPrezzo;

    private List<AgenteLite> agenti;

    private List<SituazioneCauzioniDTO> situazioneCauzioniDTO;

    private String destinazioneCodice;

    /**
     * Queste note vengono dalle note stampa della sede entita.
     */
    private String noteEntita;

    private SedeEntita sedeCollegata;

    /**
     * Costruttore.
     */
    public AreaMagazzinoFullDTOStampa() {
        super();
    }

    /**
     * Costruttore di default.
     *
     * @param areaMagazzinoFullDTO
     *            l'areaMagazzinoFullDTO da cui recuperare i dati
     * @param sedeAzienda
     *            la sede del deposito di origine se esiste
     * @param deposito
     *            il deposito
     * @param depositoOrigine
     *            il deposito di origine
     * @param trasportoCura
     *            trasportoCura
     * @param sedeCollegata
     *            sedeCollegata. Può essere la sedeCollegata oppuer la sede di rifatturazione
     * @param azienda
     *            la sede dell'azienda
     */
    public AreaMagazzinoFullDTOStampa(final AreaMagazzinoFullDTO areaMagazzinoFullDTO, final SedeAzienda sedeAzienda,
            final Deposito deposito, final Deposito depositoOrigine, final TrasportoCura trasportoCura,
            final SedeEntita sedeCollegata, final SedeAzienda azienda) {
        super();
        this.sedeCollegata = sedeCollegata;
        setAreaContabileLite(areaMagazzinoFullDTO.getAreaContabileLite());
        setAreaIva(areaMagazzinoFullDTO.getAreaIva());
        setAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());
        setAreaRate(areaMagazzinoFullDTO.getAreaRate());
        Set<RapportoBancarioSedeEntita> rapportiBancariSedeEntita = new HashSet<RapportoBancarioSedeEntita>();
        for (Rata rata : getAreaRate().getRate()) {
            RapportoBancarioSedeEntita rapportobancario = rata.getRapportoBancarioEntita();
            if (rapportobancario != null) {
                rapportiBancariSedeEntita.add(rapportobancario);
            }
        }
        setRapportiBancariEntita(rapportiBancariSedeEntita);
        setAreaRateEnabled(areaMagazzinoFullDTO.isAreaRateEnabled());
        setRiepilogoIva(areaMagazzinoFullDTO.getRiepilogoIva());
        setRigheMagazzino(areaMagazzinoFullDTO.getRigheMagazzino());
        setSedeAzienda(sedeAzienda);
        setDeposito(deposito);
        setDepositoOrigine(depositoOrigine);
        this.trasportoCura = trasportoCura;
        this.agenti = areaMagazzinoFullDTO.getAgenti();
        setAzienda(azienda);
        initDatiStampa();

        this.situazioneCauzioniDTO = new ArrayList<SituazioneCauzioniDTO>();
    }

    /**
     * Aggiunge le situazione cauzioni passate come parametro a quelle dell' {@link AreaMagazzinoFullDTOStampa}.
     *
     * @param listCauzioni
     *            cauzioni da aggiungere
     */
    public void addSituazioneCauzioni(List<SituazioneCauzioniDTO> listCauzioni) {
        if (listCauzioni != null) {
            situazioneCauzioniDTO.addAll(listCauzioni);
        }
    }

    /**
     * @return Il primo agente nella lista degli angenti delle righe. Null se non ho agenti.
     */
    public AgenteLite getAgente() {
        AgenteLite agente = null;
        if (agenti != null && !agenti.isEmpty()) {
            agente = agenti.get(0);
        }
        return agente;
    }

    /**
     * @return Returns the agenti.
     */
    @Override
    public List<AgenteLite> getAgenti() {
        return agenti;
    }

    /**
     * @return the azienda
     */
    public SedeAzienda getAzienda() {
        return azienda;
    }

    /**
     *
     * @return DatiGeograficiDestinatario
     */
    public DatiGeografici getDatiGeograficiDestinatario() {
        return datiGeograficiDestinatario;
    }

    /**
     *
     * @return DatiGeograficiDestinazione
     */
    public DatiGeografici getDatiGeograficiDestinazione() {
        return datiGeograficiDestinazione;
    }

    /**
     * @return the deposito
     */
    public Deposito getDeposito() {
        return deposito;
    }

    /**
     * @return the depositoOrigine
     */
    public Deposito getDepositoOrigine() {
        return depositoOrigine;
    }

    /**
     * @return the destinatarioCap
     */
    public String getDestinatarioCap() {
        return destinatarioCap;
    }

    /**
     * @return the destinatarioDenominazione
     */
    public String getDestinatarioDenominazione() {
        return destinatarioDenominazione;
    }

    /**
     * @return Returns the destinatarioDescrizione.
     */
    public String getDestinatarioDescrizione() {
        return destinatarioDescrizione;
    }

    /**
     *
     * @return email del destinatario
     */
    public String getDestinatarioEmail() {
        return destinatarioEmail;
    }

    /**
     * @return Returns the destinatarioFax.
     */
    public String getDestinatarioFax() {
        return destinatarioFax;
    }

    /**
     * @return the destinatarioIndirizzo
     */
    public String getDestinatarioIndirizzo() {
        return destinatarioIndirizzo;
    }

    /**
     * @return the destinatarioLocalita
     */
    public String getDestinatarioLocalita() {
        return destinatarioLocalita;
    }

    /**
     *
     * @return pec destinatario
     */
    public String getDestinatarioPec() {
        return destinatarioPec;
    }

    /**
     * @return the destinatarioProvincia
     */
    public String getDestinatarioProvincia() {
        return destinatarioProvincia;
    }

    /**
     * @return Returns the destinatarioTel.
     */
    public String getDestinatarioTel() {
        return destinatarioTel;
    }

    /**
     * @return the destinazioneCap
     */
    public String getDestinazioneCap() {
        return destinazioneCap;
    }

    /**
     * @return Returns the destinazioneCodice.
     */
    public String getDestinazioneCodice() {
        return destinazioneCodice;
    }

    /**
     * @return the destinazioneDenominazione
     */
    public String getDestinazioneDenominazione() {
        return destinazioneDenominazione;
    }

    /**
     * @return Returns the destinazioneFax.
     */
    public String getDestinazioneFax() {
        return destinazioneFax;
    }

    /**
     * @return the destinazioneIndirizzo
     */
    public String getDestinazioneIndirizzo() {
        return destinazioneIndirizzo;
    }

    /**
     * @return the destinazioneLocalita
     */
    public String getDestinazioneLocalita() {
        return destinazioneLocalita;
    }

    /**
     * @return the destinazioneProvincia
     */
    public String getDestinazioneProvincia() {
        return destinazioneProvincia;
    }

    /**
     * @return Returns the destinazioneTel.
     */
    public String getDestinazioneTel() {
        return destinazioneTel;
    }

    /**
     * @return the maxNumeroDecimaliPrezzo
     */
    public int getMaxNumeroDecimaliPrezzo() {
        return maxNumeroDecimaliPrezzo;
    }

    /**
     * @return the maxNumeroDecimaliQta
     */
    public int getMaxNumeroDecimaliQta() {
        return maxNumeroDecimaliQta;
    }

    /**
     * @return the noteEntita
     */
    public String getNoteEntita() {
        return noteEntita;
    }

    /**
     * @return rapporti bancari dell'entità
     */
    public Set<RapportoBancarioSedeEntita> getRapportiBancariEntita() {
        return rapportiBancariEntita;
    }

    /**
     * @return il primo rapporto bancario delle rate
     */
    public RapportoBancarioSedeEntita getRapportoBancarioEntita() {
        if (getRapportiBancariEntita().isEmpty()) {
            return new RapportoBancarioSedeEntita();
        } else {
            return rapportiBancariEntita.iterator().next();
        }
    }

    /**
     * Restituisce tutte le rate per la stampa. Se il numero rate richiesto è inferiore al numero rate verranno
     * restituite il numero rate meno 1 e l'ultima rata conterrà la somma dell'importo delle rimanenti.
     *
     * @param numeroRate
     *            numero rate richieste
     * @return Rate caricate
     */
    public Set<Rata> getRatePerStampa(int numeroRate) {
        Set<Rata> rateStampa = new TreeSet<Rata>(new Rata.Ratacomparator());

        if (getAreaRate() == null || getAreaRate().getRate() == null) {
            return rateStampa;
        }

        // se il numero delle rate è minore o uguale al numero rate richiesto, restituisco tutte le rate
        if (getAreaRate().getRate().size() <= numeroRate) {
            rateStampa = getAreaRate().getRate();
        } else {
            // restituisco tutte le rate -1 rispetto al parametro numeroRate
            Iterator<Rata> rateIterator = getAreaRate().getRate().iterator();
            int numRata = 1;
            while (rateIterator.hasNext() && numRata < numeroRate) {
                Rata rata = rateIterator.next();

                rateStampa.add(rata);
                numRata++;
            }
            // le rate in più a quelle richieste le sommo tutte in un'unica rata
            Rata rataCumulativa = new Rata();
            rataCumulativa.getImporto()
                    .setCodiceValuta(getAreaRate().getRate().iterator().next().getImporto().getCodiceValuta());
            rataCumulativa.setNumeroRata(99);
            while (rateIterator.hasNext()) {
                Rata rata = rateIterator.next();
                rataCumulativa.setImporto(rataCumulativa.getImporto().add(rata.getImporto(), 2));
            }
            rateStampa.add(rataCumulativa);
        }

        // riordino le rate
        Set<Rata> rateOrdered = new TreeSet<Rata>(new Rata.Ratacomparator());
        rateOrdered.addAll(rateStampa);
        return rateOrdered;
    }

    /**
     * @return the sedeAzienda
     */
    public SedeAzienda getSedeAzienda() {
        return sedeAzienda;
    }

    /**
     * @return the situazioneCauzioniDTO
     */
    public List<SituazioneCauzioniDTO> getSituazioneCauzioniDTO() {
        return Collections.unmodifiableList(situazioneCauzioniDTO);
    }

    /**
     * @return the trasportoCura
     */
    public TrasportoCura getTrasportoCura() {
        return trasportoCura;
    }

    /**
     * @return the vettoreCap
     */
    public String getVettoreCap() {
        return vettoreCap;
    }

    /**
     * @return the vettoreCodiceFiscale
     */
    public String getVettoreCodiceFiscale() {
        return vettoreCodiceFiscale;
    }

    /**
     * @return the vettoreDenominazione
     */
    public String getVettoreDenominazione() {
        return vettoreDenominazione;
    }

    /**
     * @return the vettoreIndirizzo
     */
    public String getVettoreIndirizzo() {
        return vettoreIndirizzo;
    }

    /**
     * @return the vettoreLocalita
     */
    public String getVettoreLocalita() {
        return vettoreLocalita;
    }

    /**
     * @return the vettoreNRIscrizioneAlbo
     */
    public String getVettoreNRIscrizioneAlbo() {
        return vettoreNRIscrizioneAlbo;
    }

    /**
     * @return the vettorePIVA
     */
    public String getVettorePIVA() {
        return vettorePIVA;
    }

    /**
     * @return the vettoreProvincia
     */
    public String getVettoreProvincia() {
        return vettoreProvincia;
    }

    /**
     * Inizializza i dati per la stampa.
     *
     * @param trasferimento
     */
    private void initDatiStampa() {
        initDestinatario();
        initDestinazione();
        initVettore();
    }

    /**
     * Inizializza tutti i dati del destinatario.
     */
    private void initDestinatario() {
        if (isTrasferimento()) {
            datiGeograficiDestinatario = sedeAzienda.getSede().getDatiGeografici();
            destinatarioDenominazione = sedeAzienda.getAzienda().getDenominazione();
            destinatarioIndirizzo = sedeAzienda.getSede().getIndirizzo();
            destinatarioCap = sedeAzienda.getSede().getDatiGeografici().getDescrizioneCap();
            destinatarioLocalita = sedeAzienda.getSede().getDatiGeografici().getDescrizioneLocalita();
            destinatarioProvincia = sedeAzienda.getSede().getDatiGeografici().getSiglaProvincia();
            setDestinatarioEmail(sedeAzienda.getSede().getIndirizzoMail());
            setDestinatarioPec(sedeAzienda.getSede().getIndirizzoPEC());
        } else if (getAreaMagazzino().getDocumento().getEntita() != null) {
            destinatarioDenominazione = getAreaMagazzino().getDocumento().getEntita().getAnagrafica()
                    .getDenominazione();
            if (sedeCollegata != null && sedeCollegata.getId() != null) {
                datiGeograficiDestinatario = sedeCollegata.getSede().getDatiGeografici();
                destinatarioIndirizzo = sedeCollegata.getSede().getIndirizzo();
                destinatarioCap = sedeCollegata.getSede().getDatiGeografici().getDescrizioneCap();
                destinatarioLocalita = sedeCollegata.getSede().getDatiGeografici().getDescrizioneLocalita();
                destinatarioProvincia = sedeCollegata.getSede().getDatiGeografici().getSiglaProvincia();
                destinatarioFax = sedeCollegata.getSede().getFax();
                destinatarioTel = sedeCollegata.getSede().getTelefono();
                if (sedeCollegata.getTipoSede().getTipoSede() == TipoSede.SERVIZIO) {
                    destinatarioDescrizione = sedeCollegata.getSede().getDescrizione();
                } else if (sedeCollegata.getTipoSede().getTipoSede() == TipoSede.INDIRIZZO_SPEDIZIONE) {
                    destinatarioDenominazione = sedeCollegata.getSede().getDescrizione();
                }
                setDestinatarioEmail(sedeCollegata.getSede().getIndirizzoMail());
                setDestinatarioPec(sedeCollegata.getSede().getIndirizzoPEC());
            } else {
                SedeAnagrafica sedeAnagrafica = getAreaMagazzino().getDocumento().getEntita().getAnagrafica()
                        .getSedeAnagrafica();
                datiGeograficiDestinatario = sedeAnagrafica.getDatiGeografici();
                destinatarioIndirizzo = sedeAnagrafica.getIndirizzo();
                destinatarioCap = sedeAnagrafica.getDatiGeografici().getDescrizioneCap();
                destinatarioLocalita = sedeAnagrafica.getDatiGeografici().getDescrizioneLocalita();
                destinatarioProvincia = sedeAnagrafica.getDatiGeografici().getSiglaProvincia();
                destinatarioFax = getAreaMagazzino().getDocumento().getSedeEntita().getSede().getFax();
                destinatarioTel = getAreaMagazzino().getDocumento().getSedeEntita().getSede().getTelefono();
                setDestinatarioEmail(sedeAnagrafica.getIndirizzoMail());
                setDestinatarioPec(sedeAnagrafica.getIndirizzoPEC());
            }
        }
    }

    /**
     * Inizializza tutti i dati della destinazione.
     */
    private void initDestinazione() {
        if (isTrasferimento()) {
            datiGeograficiDestinazione = deposito.getDatiGeografici();
            destinazioneCodice = deposito.getCodice();
            destinazioneDenominazione = deposito.getDescrizione();
            destinazioneIndirizzo = deposito.getIndirizzo();
            destinazioneCap = deposito.getDatiGeografici().getDescrizioneCap();
            destinazioneLocalita = deposito.getDatiGeografici().getDescrizioneLocalita();
            destinazioneProvincia = deposito.getDatiGeografici().getSiglaProvincia();
        } else if (getAreaMagazzino().getDocumento().getEntita() != null) {
            destinazioneCodice = getAreaMagazzino().getDocumento().getSedeEntita().getCodice();
            if (getAreaMagazzino().getDocumento().getSedeEntita().getTipoSede().isSedePrincipale()) {
                destinazioneDenominazione = getAreaMagazzino().getDocumento().getSedeEntita().getEntita()
                        .getAnagrafica().getDenominazione();
            } else {
                destinazioneDenominazione = getAreaMagazzino().getDocumento().getSedeEntita().getSede()
                        .getDescrizione();
            }
            datiGeograficiDestinazione = getAreaMagazzino().getDocumento().getSedeEntita().getSede()
                    .getDatiGeografici();
            destinazioneIndirizzo = getAreaMagazzino().getDocumento().getSedeEntita().getSede().getIndirizzo();
            destinazioneCap = getAreaMagazzino().getDocumento().getSedeEntita().getSede().getDatiGeografici()
                    .getDescrizioneCap();
            destinazioneLocalita = getAreaMagazzino().getDocumento().getSedeEntita().getSede().getDatiGeografici()
                    .getDescrizioneLocalita();
            destinazioneProvincia = getAreaMagazzino().getDocumento().getSedeEntita().getSede().getDatiGeografici()
                    .getSiglaProvincia();
            destinazioneFax = getAreaMagazzino().getDocumento().getSedeEntita().getSede().getFax();
            destinazioneTel = getAreaMagazzino().getDocumento().getSedeEntita().getSede().getTelefono();
        }
    }

    /**
     * Inizializza tutti i dati del vettore.
     */
    private void initVettore() {
        vettorePresente = false;
        vettoreDenominazione = "";
        vettoreIndirizzo = "";
        vettoreCap = "";
        vettoreLocalita = "";
        vettoreProvincia = "";
        vettorePIVA = "";
        vettoreCodiceFiscale = "";
        vettoreNRIscrizioneAlbo = "";

        if (getAreaMagazzino().getVettore() != null && getAreaMagazzino().getVettore().getId() != null) {
            vettorePresente = true;
            VettoreLite vettore = getAreaMagazzino().getVettore();
            SedeEntita sedeVettore = getAreaMagazzino().getSedeVettore();
            vettoreDenominazione = vettore.getAnagrafica().getDenominazione();
            if (sedeVettore != null && sedeVettore.getId() != null) {
                vettoreIndirizzo = sedeVettore.getSede().getIndirizzo();
                vettoreCap = sedeVettore.getSede().getDatiGeografici().getDescrizioneCap();
                vettoreLocalita = sedeVettore.getSede().getDatiGeografici().getDescrizioneLocalita();
                vettoreProvincia = sedeVettore.getSede().getDatiGeografici().getSiglaProvincia();
            }
            vettorePIVA = vettore.getAnagrafica().getPartiteIVA();
            vettoreCodiceFiscale = vettore.getAnagrafica().getCodiceFiscale();
            vettoreNRIscrizioneAlbo = vettore.getNumeroIscrizioneAlbo();
        }
    }

    /**
     * @return true se l'area di stampa è un trasferimento
     */
    public boolean isTrasferimento() {
        return getAreaMagazzino().getTipoAreaMagazzino().getTipoMovimento().equals(TipoMovimento.TRASFERIMENTO);
    }

    /**
     * @return the vettorePresente
     */
    public boolean isVettorePresente() {
        return vettorePresente;
    }

    /**
     * @param azienda
     *            the azienda to set
     */
    public void setAzienda(SedeAzienda azienda) {
        this.azienda = azienda;
    }

    /**
     * @param deposito
     *            the deposito to set
     */
    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    /**
     * @param depositoOrigine
     *            the depositoOrigine to set
     */
    public void setDepositoOrigine(Deposito depositoOrigine) {
        this.depositoOrigine = depositoOrigine;
    }

    /**
     * @param destinatarioCap
     *            the destinatarioCap to set
     */
    public void setDestinatarioCap(String destinatarioCap) {
        this.destinatarioCap = destinatarioCap;
    }

    /**
     * @param destinatarioDenominazione
     *            the destinatarioDenominazione to set
     */
    public void setDestinatarioDenominazione(String destinatarioDenominazione) {
        this.destinatarioDenominazione = destinatarioDenominazione;
    }

    /**
     *
     * @param destinatarioEmail
     *            email destinatario
     */
    public void setDestinatarioEmail(String destinatarioEmail) {
        this.destinatarioEmail = destinatarioEmail;
    }

    /**
     * @param destinatarioFax
     *            The destinatarioFax to set.
     */
    public void setDestinatarioFax(String destinatarioFax) {
        this.destinatarioFax = destinatarioFax;
    }

    /**
     * @param destinatarioIndirizzo
     *            the destinatarioIndirizzo to set
     */
    public void setDestinatarioIndirizzo(String destinatarioIndirizzo) {
        this.destinatarioIndirizzo = destinatarioIndirizzo;
    }

    /**
     * @param destinatarioLocalita
     *            the destinatarioLocalita to set
     */
    public void setDestinatarioLocalita(String destinatarioLocalita) {
        this.destinatarioLocalita = destinatarioLocalita;
    }

    /**
     *
     * @param destinatarioPec
     *            pec destinatario
     */
    public void setDestinatarioPec(String destinatarioPec) {
        this.destinatarioPec = destinatarioPec;
    }

    /**
     * @param destinatarioProvincia
     *            the destinatarioProvincia to set
     */
    public void setDestinatarioProvincia(String destinatarioProvincia) {
        this.destinatarioProvincia = destinatarioProvincia;
    }

    /**
     * @param destinatarioTel
     *            The destinatarioTel to set.
     */
    public void setDestinatarioTel(String destinatarioTel) {
        this.destinatarioTel = destinatarioTel;
    }

    /**
     * @param destinazioneCap
     *            the destinazioneCap to set
     */
    public void setDestinazioneCap(String destinazioneCap) {
        this.destinazioneCap = destinazioneCap;
    }

    /**
     * @param destinazioneCodice
     *            The destinazioneCodice to set.
     */
    public void setDestinazioneCodice(String destinazioneCodice) {
        this.destinazioneCodice = destinazioneCodice;
    }

    /**
     * @param destinazioneDenominazione
     *            the destinazioneDenominazione to set
     */
    public void setDestinazioneDenominazione(String destinazioneDenominazione) {
        this.destinazioneDenominazione = destinazioneDenominazione;
    }

    /**
     * @param destinazioneFax
     *            The destinazioneFax to set.
     */
    public void setDestinazioneFax(String destinazioneFax) {
        this.destinazioneFax = destinazioneFax;
    }

    /**
     * @param destinazioneIndirizzo
     *            the destinazioneIndirizzo to set
     */
    public void setDestinazioneIndirizzo(String destinazioneIndirizzo) {
        this.destinazioneIndirizzo = destinazioneIndirizzo;
    }

    /**
     * @param destinazioneLocalita
     *            the destinazioneLocalita to set
     */
    public void setDestinazioneLocalita(String destinazioneLocalita) {
        this.destinazioneLocalita = destinazioneLocalita;
    }

    /**
     * @param destinazioneProvincia
     *            the destinazioneProvincia to set
     */
    public void setDestinazioneProvincia(String destinazioneProvincia) {
        this.destinazioneProvincia = destinazioneProvincia;
    }

    /**
     * @param destinazioneTel
     *            The destinazioneTel to set.
     */
    public void setDestinazioneTel(String destinazioneTel) {
        this.destinazioneTel = destinazioneTel;
    }

    /**
     * @param maxNumeroDecimaliPrezzo
     *            the maxNumeroDecimaliPrezzo to set
     */
    public void setMaxNumeroDecimaliPrezzo(int maxNumeroDecimaliPrezzo) {
        this.maxNumeroDecimaliPrezzo = maxNumeroDecimaliPrezzo;
    }

    /**
     * @param maxNumeroDecimaliQta
     *            the maxNumeroDecimaliQta to set
     */
    public void setMaxNumeroDecimaliQta(int maxNumeroDecimaliQta) {
        this.maxNumeroDecimaliQta = maxNumeroDecimaliQta;
    }

    /**
     * @param noteEntita
     *            the noteEntita to set
     */
    public void setNoteEntita(String noteEntita) {
        this.noteEntita = noteEntita;
    }

    /**
     * @param rapportiBancariEntita
     *            rapporti bancari dell'entità
     */
    public void setRapportiBancariEntita(Set<RapportoBancarioSedeEntita> rapportiBancariEntita) {
        this.rapportiBancariEntita = rapportiBancariEntita;
    }

    /**
     * @param sedeAzienda
     *            the sedeAzienda to set
     */
    public void setSedeAzienda(SedeAzienda sedeAzienda) {
        this.sedeAzienda = sedeAzienda;
    }

}
