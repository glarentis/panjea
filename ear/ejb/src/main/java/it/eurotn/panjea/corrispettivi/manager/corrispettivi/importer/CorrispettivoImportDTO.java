package it.eurotn.panjea.corrispettivi.manager.corrispettivi.importer;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;

public class CorrispettivoImportDTO implements Serializable {

    private static final long serialVersionUID = 8804085926011230954L;

    private TipoDocumento tipoDocumento;

    private CodiceIva codiceIva;

    private BigDecimal importo;

    /**
     * @return the codiceIva
     */
    public CodiceIva getCodiceIva() {
        return codiceIva;
    }

    /**
     * @return the importo
     */
    public BigDecimal getImporto() {
        return ObjectUtils.defaultIfNull(importo, BigDecimal.ZERO);
    }

    /**
     * @return the tipoDocumento
     */
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param codiceIva
     *            the codiceIva to set
     */
    public void setCodiceIva(CodiceIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    /**
     * @param importo
     *            the importo to set
     */
    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    /**
     * @param tipoDocumento
     *            the tipoDocumento to set
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
