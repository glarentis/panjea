package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.Date;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.magazzino.domain.Contratto;

/**
 * Utilizzata per visualizzare tutti i contratti sulle sedi.
 *
 * @author giangi
 * @version 1.0, 02/dic/2011
 *
 */
public class ContrattoProspettoDTO implements Serializable {
    public enum ETIPOLINKENTITA {
        TUTTI, CATEGORIA, SEDE, SEDE_EREDITATA, ENTITA
    }

    private static final long serialVersionUID = 1L;

    private SedeEntita sedeEntita;
    private EntitaLite entita;
    private Contratto contratto;

    private ETIPOLINKENTITA tipoLinkEntita;

    /**
     * Costruttore.
     */
    public ContrattoProspettoDTO() {
        sedeEntita = new SedeEntita();
        contratto = new Contratto();
    }

    /**
     * @return Returns the contratto.
     */
    public Contratto getContratto() {
        return contratto;
    }

    /**
     * @return Returns the entita.
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return Returns the sedeEntita.
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return the tipoLinkEntita
     */
    public ETIPOLINKENTITA getTipoLinkEntita() {
        return tipoLinkEntita;
    }

    /**
     * @param classEntita
     *            classEntita
     */
    public void setClassEntita(String classEntita) {
        if ("C".equals(classEntita)) {
            entita = new ClienteLite();
        } else if ("F".equals(classEntita)) {
            entita = new FornitoreLite();
        } else if ("V".equals(classEntita)) {
            entita = new FornitoreLite();
        } else if ("A".equals(classEntita)) {
            entita = new AgenteLite();
        } else {
            entita = new EntitaLite();
        }
    }

    /**
     * @param contratto
     *            The contratto to set.
     */
    public void setContratto(Contratto contratto) {
        this.contratto = contratto;
    }

    /**
     * @param codiceContratto
     *            codice della Contratto
     */
    public void setContrattoCodice(String codiceContratto) {
        contratto.setCodice(codiceContratto);
    }

    /**
     * @param dataFine
     *            data fine contatto
     */
    public void setContrattoDataFine(Date dataFine) {
        contratto.setDataFine(dataFine);
    }

    /**
     * 
     * @param dataInizio
     *            data inizio contratto
     */
    public void setContrattoDataInizio(Date dataInizio) {
        contratto.setDataInizio(dataInizio);
    }

    /**
     * 
     * @param descrizioneContratto
     *            descrizione Contratto
     */
    public void setContrattoDescrizione(String descrizioneContratto) {
        contratto.setDescrizione(descrizioneContratto);
    }

    /**
     * 
     * @param idContratto
     *            id Contratto
     */
    public void setContrattoId(Integer idContratto) {
        contratto.setId(idContratto);
    }

    /**
     * @param entita
     *            The entita to set.
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param codiceEntita
     *            codice della Entita
     */
    public void setEntitaCodice(Integer codiceEntita) {
        entita.setCodice(codiceEntita);
    }

    /**
     * 
     * @param descrizioneEntita
     *            descrizione Entita
     */
    public void setEntitaDescrizione(String descrizioneEntita) {
        entita.getAnagrafica().setDenominazione(descrizioneEntita);
    }

    /**
     * 
     * @param idEntita
     *            id Entita
     */
    public void setEntitaId(int idEntita) {
        entita.setId(idEntita);
    }

    /**
     * @param codiceSede
     *            codice della sede
     */
    public void setSedeCodice(String codiceSede) {
        sedeEntita.setCodice(codiceSede);
    }

    /**
     * 
     * @param descrizioneSede
     *            descrizione sede
     */
    public void setSedeDescrizione(String descrizioneSede) {
        sedeEntita.getSede().setDescrizione(descrizioneSede);
    }

    /**
     * @param sedeEntita
     *            The sedeEntita to set.
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

    /**
     * 
     * @param idSede
     *            id sede
     */
    public void setSedeId(Integer idSede) {
        sedeEntita.setId(idSede);
    }

    /**
     * @param tipoLinkEntita
     *            the tipoLinkEntita to set
     */
    public void setTipoLinkEntita(ETIPOLINKENTITA tipoLinkEntita) {
        this.tipoLinkEntita = tipoLinkEntita;
    }

    /**
     * @param tipoLinkEntitaNum
     *            the tipoLinkEntitaNum to set
     */
    public void setTipoLinkEntitaNum(Integer tipoLinkEntitaNum) {
        this.tipoLinkEntita = ETIPOLINKENTITA.values()[tipoLinkEntitaNum];
    }

}
