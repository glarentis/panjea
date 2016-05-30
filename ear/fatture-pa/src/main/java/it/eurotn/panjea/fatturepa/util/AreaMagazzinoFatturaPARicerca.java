package it.eurotn.panjea.fatturepa.util;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

public class AreaMagazzinoFatturaPARicerca extends AreaMagazzinoRicerca {

    private static final long serialVersionUID = -334740896095705002L;

    private String fileXmlFattura;

    private String fileXmlFatturaFirmato;

    private StatoFatturaPA statoFatturaPA;

    private boolean statoFatturaEsitoPositivo;

    private String progressivoInvio;

    {
        statoFatturaEsitoPositivo = Boolean.TRUE;
    }

    /**
     * @return the fileXmlFattura
     */
    public String getFileXmlFattura() {
        return fileXmlFattura;
    }

    /**
     * @return the fileXmlFatturaFirmato
     */
    public String getFileXmlFatturaFirmato() {
        return fileXmlFatturaFirmato;
    }

    /**
     * @return the progressivoInvio
     */
    public String getProgressivoInvio() {
        return progressivoInvio;
    }

    /**
     * @return the statoFatturaPA
     */
    public StatoFatturaPA getStatoFatturaPA() {
        return statoFatturaPA;
    }

    /**
     * @return <code>true</code> se è possibile cancellare il file xml.
     */
    public boolean isDeleteXMLEnable() {
        return getStatoFatturaPA() == null || getStatoFatturaPA() == StatoFatturaPA._DI
                || !isStatoFatturaEsitoPositivo();
    }

    /**
     * @return <code>true</code> se è possibile applicare la firma al file xml
     */
    public boolean isSignEnable() {
        return !StringUtils.isBlank(getFileXmlFattura()) && StringUtils.isBlank(getFileXmlFatturaFirmato())
                && (getStatoFatturaPA() == null || getStatoFatturaPA() == StatoFatturaPA._DI
                        || !isStatoFatturaEsitoPositivo());
    }

    /**
     * @return the statoFatturaEsitoPositivo
     */
    public boolean isStatoFatturaEsitoPositivo() {
        return statoFatturaEsitoPositivo;
    }

    /**
     * @param fileXmlFattura
     *            the fileXmlFattura to set
     */
    public void setFileXmlFattura(String fileXmlFattura) {
        this.fileXmlFattura = fileXmlFattura;
    }

    /**
     * @param fileXmlFatturaFirmato
     *            the fileXmlFatturaFirmato to set
     */
    public void setFileXmlFatturaFirmato(String fileXmlFatturaFirmato) {
        this.fileXmlFatturaFirmato = fileXmlFatturaFirmato;
    }

    /**
     * @param numeroDocumento
     *            the numeroDocumento to set
     */
    public void setNumeroDocumento(String numeroDocumento) {
        getDocumento().getCodice().setCodice(numeroDocumento);
    }

    /**
     * @param numeroOrderDocumento
     *            the numeroOrderDocumento to set
     */
    public void setNumeroOrderDocumento(String numeroOrderDocumento) {
        getDocumento().getCodice().setCodiceOrder(numeroOrderDocumento);
    }

    /**
     * @param progressivoInvio
     *            the progressivoInvio to set
     */
    public void setProgressivoInvio(String progressivoInvio) {
        this.progressivoInvio = progressivoInvio;
    }

    /**
     * @param statoFatturaEsitoPositivo
     *            the statoFatturaEsitoPositivo to set
     */
    public void setStatoFatturaEsitoPositivo(Boolean statoFatturaEsitoPositivo) {
        if (statoFatturaEsitoPositivo != null) {
            this.statoFatturaEsitoPositivo = statoFatturaEsitoPositivo;
        }
    }

    /**
     * @param statoFatturaPA
     *            the statoFatturaPA to set
     */
    public void setStatoFatturaPA(StatoFatturaPA statoFatturaPA) {
        this.statoFatturaPA = statoFatturaPA;
    }

    /**
     * @param statoFatturaPANum
     *            the statoFatturaPANum to set
     */
    public void setStatoFatturaPANum(Integer statoFatturaPANum) {
        if (statoFatturaPANum != null) {
            this.statoFatturaPA = StatoFatturaPA.values()[statoFatturaPANum];
        }
    }

    /**
     * @param statoNum
     *            the statoNum to set
     */
    public void setStatoNum(Integer statoNum) {
        super.setStato(StatoAreaMagazzino.values()[statoNum]);
    }

    /**
     * @param tipoEntitaNum
     *            the tipoEntitaNum to set
     */
    public void setTipoEntitaNum(Integer tipoEntitaNum) {
        super.setTipoEntita(TipoEntita.values()[tipoEntitaNum]);
    }

    /**
     * @param tipoMovimentoNum
     *            the tipoMovimentoNum to set
     */
    public void setTipoMovimentoNum(Integer tipoMovimentoNum) {
        super.setTipoMovimento(TipoMovimento.values()[tipoMovimentoNum]);
    }

}
