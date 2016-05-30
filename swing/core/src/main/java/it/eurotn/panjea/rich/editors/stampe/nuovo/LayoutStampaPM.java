package it.eurotn.panjea.rich.editors.stampe.nuovo;

import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

public class LayoutStampaPM {

    private IClasseTipoDocumento classeTipoDocumentoInstance;
    private ITipoAreaDocumento tipoAreaDocumento;

    private String reportName;

    private boolean bloccaEntita;
    private EntitaLite entita;
    private SedeEntita sedeEntita;

    /**
     * Costruttore.
     */
    public LayoutStampaPM() {
        super();
        bloccaEntita = false;
    }

    /**
     * @return Returns the classeTipoDocumentoInstance.
     */
    public IClasseTipoDocumento getClasseTipoDocumentoInstance() {
        return classeTipoDocumentoInstance;
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return Returns the reportName.
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return Returns the tipoAreaDocumento.
     */
    public ITipoAreaDocumento getTipoAreaDocumento() {
        return tipoAreaDocumento;
    }

    /**
     * @return the bloccaEntita
     */
    public boolean isBloccaEntita() {
        return bloccaEntita;
    }

    /**
     * @param bloccaEntita
     *            the bloccaEntita to set
     */
    public void setBloccaEntita(boolean bloccaEntita) {
        this.bloccaEntita = bloccaEntita;
    }

    /**
     * @param classeTipoDocumentoInstance
     *            The classeTipoDocumentoInstance to set.
     */
    public void setClasseTipoDocumentoInstance(IClasseTipoDocumento classeTipoDocumentoInstance) {
        this.classeTipoDocumentoInstance = classeTipoDocumentoInstance;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param reportName
     *            The reportName to set.
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     * @param sedeEntita
     *            the sedeEntita to set
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

    /**
     * @param tipoAreaDocumento
     *            The tipoAreaDocumento to set.
     */
    public void setTipoAreaDocumento(ITipoAreaDocumento tipoAreaDocumento) {
        this.tipoAreaDocumento = tipoAreaDocumento;
    }
}