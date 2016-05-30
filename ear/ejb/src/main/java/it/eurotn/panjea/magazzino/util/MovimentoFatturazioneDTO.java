package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica.TipoSpedizioneDocumenti;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClientePotenzialeLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.rate.domain.AreaRate;

/**
 * Contiene l'area magazzino e l'area partite di un documento.<br.> L'area magazzino non contiene il codice di pagamento
 * quindi creo un dto per avere area di magazzino a area partite collegata
 *
 * @author fattazzo
 */
public class MovimentoFatturazioneDTO implements Serializable {

    private static final long serialVersionUID = -7230273912767940402L;

    private AreaMagazzino areaMagazzino;

    private AreaRate areaRate;

    private ZonaGeografica zonaGeografica;

    private Agente agente;

    /**
     * Costruttore.
     */
    public MovimentoFatturazioneDTO() {
        super();
        this.areaMagazzino = new AreaMagazzino();
        this.areaMagazzino.setDocumento(new Documento());
        this.areaMagazzino.getDocumento().setTipoDocumento(new TipoDocumento());
        this.areaMagazzino.getDocumento().setSedeEntita(new SedeEntita());
        this.areaMagazzino.getDocumento().getSedeEntita().setSede(new SedeAnagrafica());
        this.areaMagazzino.getDocumento().getSedeEntita().getSede().setDatiGeografici(new DatiGeografici());
        this.areaMagazzino.getDocumento().setEntita(new EntitaLite());
        this.areaMagazzino.getDocumento().getEntita().setAnagrafica(new AnagraficaLite());
        this.areaMagazzino.getDocumento().getEntita().getAnagrafica().setSedeAnagrafica(new SedeAnagrafica());
        this.areaMagazzino.setTipoAreaMagazzino(new TipoAreaMagazzino());
        this.areaMagazzino.getTipoAreaMagazzino().setTipoDocumento(new TipoDocumento());
        this.areaRate = new AreaRate();
        this.areaRate.setCodicePagamento(new CodicePagamento());
        this.zonaGeografica = new ZonaGeografica();
        this.agente = new Agente();
        this.agente.setAnagrafica(new Anagrafica());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        MovimentoFatturazioneDTO other = (MovimentoFatturazioneDTO) obj;
        if (areaMagazzino == null) {
            if (other.areaMagazzino != null) {
                return false;
            }
        } else if (!areaMagazzino.equals(other.areaMagazzino)) {
            return false;
        }
        return true;
    }

    /**
     * @return Returns the agente.
     */
    public Agente getAgente() {
        return agente;
    }

    /**
     * @return the areaMagazzino
     * @uml.property name="areaMagazzino"
     */
    public AreaMagazzino getAreaMagazzino() {
        return areaMagazzino;
    }

    /**
     * @return the areaRate
     * @uml.property name="areaRate"
     */
    public AreaRate getAreaRate() {
        return areaRate;
    }

    /**
     * @return Returns the zonaGeografica.
     */
    public ZonaGeografica getZonaGeografica() {
        return zonaGeografica;
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
        result = prime * result + ((areaMagazzino == null) ? 0 : areaMagazzino.hashCode());
        return result;
    }

    /**
     * @param agenteDenominazione
     *            the agenteDenominazione to set
     */
    public void setAgenteDenominazione(String agenteDenominazione) {
        agente.getAnagrafica().setDenominazione(agenteDenominazione);
    }

    /**
     * @param agenteCodice
     *            the agenteCodice to set
     */
    public void setCodiceAgente(Integer agenteCodice) {
        agente.setCodice(agenteCodice);
    }

    /**
     * @param data
     *            the data to set
     */
    public void setDataDocumento(Date data) {
        this.areaMagazzino.getDocumento().setDataDocumento(data);
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizioneSede(String descrizione) {
        this.areaMagazzino.getDocumento().getSedeEntita().getSede().setDescrizione(descrizione);
    }

    /**
     * @param classe
     *            the classe to set
     */
    public void setEntitaClass(String classe) {
        if (Objects.equals("C", classe)) {
            getAreaMagazzino().getDocumento().setEntita(new ClienteLite());
        } else if (Objects.equals("CP", classe)) {
            getAreaMagazzino().getDocumento().setEntita(new ClientePotenzialeLite());
        } else if (Objects.equals("F", classe)) {
            getAreaMagazzino().getDocumento().setEntita(new FornitoreLite());
        }
    }

    /**
     * @param entitaCodice
     *            the entitaCodice to set
     */
    public void setEntitaCodice(Integer entitaCodice) {
        this.areaMagazzino.getDocumento().getEntita().setCodice(entitaCodice);
    }

    /**
     * @param entitaDenominazione
     *            the entitaDenominazione to set
     */
    public void setEntitaDenominazione(String entitaDenominazione) {
        this.areaMagazzino.getDocumento().getEntita().setDenominazione(entitaDenominazione);
    }

    /**
     * @param entitaId
     *            the entitaId to set
     */
    public void setEntitaId(Integer entitaId) {
        this.areaMagazzino.getDocumento().getEntita().setId(entitaId);
    }

    /**
     * @param entitaVersion
     *            the entitaVersion to set
     */
    public void setEntitaVersion(Integer entitaVersion) {
        this.areaMagazzino.getDocumento().getEntita().setVersion(entitaVersion);
    }

    /**
     * @param idAgente
     *            the idAgente to set
     */
    public void setIdAgente(Integer idAgente) {
        agente.setId(idAgente);
    }

    /**
     * @param id
     *            the idAreaMagazzino to set
     */
    public void setIdAreaMagazzino(int id) {
        this.areaMagazzino.setId(id);
    }

    /**
     *
     * @param id
     *            the IdAreaRate to set
     */
    public void setIdAreaRate(int id) {
        areaRate.setId(id);
    }

    /**
     * @param id
     *            the IdCodicePagamento to set
     */
    public void setIdCodicePagamento(int id) {
        areaRate.getCodicePagamento().setId(id);
    }

    /**
     * @param idDocumento
     *            the idDocumento to set
     */
    public void setIdDocumento(int idDocumento) {
        this.areaMagazzino.getDocumento().setId(idDocumento);
    }

    /**
     * @param id
     *            the IdSede to set
     */
    public void setIdSede(int id) {
        this.areaMagazzino.getDocumento().getSedeEntita().setId(id);
    }

    /**
     * @param id
     *            the idTipoAreaMagazzino to set
     */
    public void setidTipoAreaMagazzino(int id) {
        this.getAreaMagazzino().getTipoAreaMagazzino().setId(id);
    }

    /**
     * @param id
     *            the IdTipoDocumento to set
     */
    public void setIdTipoDocumento(int id) {
        this.areaMagazzino.getTipoAreaMagazzino().getTipoDocumento().setId(id);
    }

    /**
     * @param mail
     *            the IndirizzoMail to set
     */
    public void setIndirizzoMail(String mail) {
        this.areaMagazzino.getDocumento().getEntita().getAnagrafica().getSedeAnagrafica().setIndirizzoMail(mail);
    }

    /**
     * @param mail
     *            the mail to set
     */
    public void setIndirizzoMailSpedizione(String mail) {
        this.areaMagazzino.getDocumento().getEntita().getAnagrafica().getSedeAnagrafica()
                .setIndirizzoMailSpedizione(mail);
    }

    /**
     * @param pec
     *            the IndirizzoPec to set
     */
    public void setIndirizzoPec(String pec) {
        this.areaMagazzino.getDocumento().getEntita().getAnagrafica().getSedeAnagrafica().setIndirizzoPEC(pec);
    }

    /**
     * @param indirizzo
     *            the IndirizzoSede to set
     */
    public void setIndirizzoSede(String indirizzo) {
        this.areaMagazzino.getDocumento().getSedeEntita().getSede().setIndirizzo(indirizzo);
    }

    /**
     * @param numeroDocumento
     *            the numeroDocumento to set
     */
    public void setNumeroDocumento(CodiceDocumento numeroDocumento) {
        this.areaMagazzino.getDocumento().setCodice(numeroDocumento);
    }

    /**
     * @param codicePagamento
     *            the codicePagamento to set
     */
    public void setPagamentoCodice(String codicePagamento) {
        this.getAreaRate().getCodicePagamento().setCodicePagamento(codicePagamento);
    }

    /**
     * @param descrizione
     *            the PagamentoDescrizione to set
     */
    public void setPagamentoDescrizione(String descrizione) {
        this.getAreaRate().getCodicePagamento().setDescrizione(descrizione);
    }

    /**
     * @param localitaDescrizione
     *            the localitaDescrizione to set
     */
    public void setSedeLocalita(String localitaDescrizione) {
        Localita localita = new Localita();
        localita.setDescrizione(localitaDescrizione);
        this.areaMagazzino.getDocumento().getSedeEntita().getSede().getDatiGeografici().setLocalita(localita);
    }

    /**
     * @param spedizioneDocumentiViaPEC
     *            the spedizioneDocumentiViaPEC to set
     */
    public void setSpedizioneDocumentiViaPEC(boolean spedizioneDocumentiViaPEC) {
        this.areaMagazzino.getDocumento().getEntita().getAnagrafica().getSedeAnagrafica()
                .setSpedizioneDocumentiViaPEC(spedizioneDocumentiViaPEC);
    }

    /**
     * @param statoAreaMagazzino
     *            the statoAreaMagazzino to set
     */
    public void setStatoAreaMagazzino(StatoAreaMagazzino statoAreaMagazzino) {
        this.areaMagazzino.setStatoAreaMagazzino(statoAreaMagazzino);
    }

    /**
     * @param statoSpedizione
     *            the statoSpedizione to set
     */
    public void setStatoSpedizione(StatoSpedizione statoSpedizione) {
        this.areaMagazzino.setStatoSpedizione(statoSpedizione);
    }

    /**
     * @param codice
     *            the TipoDocumentoCodice to set
     */
    public void setTipoDocumentoCodice(String codice) {
        this.areaMagazzino.getTipoAreaMagazzino().getTipoDocumento().setCodice(codice);
        this.areaMagazzino.getDocumento().getTipoDocumento().setCodice(codice);
    }

    /**
     * @param descrizione
     *            the TipoDocumentoDescrizione to set
     */
    public void setTipoDocumentoDescrizione(String descrizione) {
        this.areaMagazzino.getTipoAreaMagazzino().getTipoDocumento().setDescrizione(descrizione);
        this.areaMagazzino.getDocumento().getTipoDocumento().setDescrizione(descrizione);
    }

    /**
     * @param tipoSpedizioneDocumenti
     *            the tipoSpedizioneDocumenti to set
     */
    public void setTipoSpedizioneDocumenti(TipoSpedizioneDocumenti tipoSpedizioneDocumenti) {
        this.areaMagazzino.getDocumento().getEntita().getAnagrafica().getSedeAnagrafica()
                .setTipoSpedizioneDocumenti(tipoSpedizioneDocumenti);
    }

    /**
     * @param version
     *            the VersionAreaMagazzino to set
     */
    public void setVersionAreaMagazzino(int version) {
        this.getAreaMagazzino().setVersion(version);
    }

    /**
     * @param zonaGeografica
     *            The zonaGeografica to set.
     */
    public void setZonaGeografica(ZonaGeografica zonaGeografica) {
        this.zonaGeografica = zonaGeografica;
    }

}
