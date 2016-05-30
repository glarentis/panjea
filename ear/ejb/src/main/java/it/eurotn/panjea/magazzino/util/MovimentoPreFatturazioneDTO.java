package it.eurotn.panjea.magazzino.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;

public class MovimentoPreFatturazioneDTO extends MovimentoFatturazioneDTO {

    private static final long serialVersionUID = 4859073847715673961L;

    private TipoPagamento tipoPagamento;

    private List<AreaMagazzinoLite> areeCollegate;

    /**
     * @return the areeCollegate
     */
    public List<AreaMagazzinoLite> getAreeCollegate() {
        return areeCollegate;
    }

    /**
     * @return the tipoPagamento
     */
    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    /**
     * @param areeCollegate
     *            the areeCollegate to set
     */
    public void setAreeCollegate(List<AreaMagazzinoLite> areeCollegate) {
        this.areeCollegate = areeCollegate;
    }

    @Override
    public void setNumeroDocumento(CodiceDocumento numeroDocumento) {
        super.setNumeroDocumento(numeroDocumento);

        // tolgo il codice dell'utente utilizzato per generare la pre fatturazione.
        // il codice è generato secondo lo schema "numero" "spazio" "utente"
        getAreaMagazzino().getDocumento().getCodice()
                .setCodice(StringUtils.split(getAreaMagazzino().getDocumento().getCodice().getCodice())[0]);
    }

    /**
     * @param tipoPagamento
     *            the tipoPagamento to set
     */
    public void setTipoPagamento(Integer tipoPagamento) {
        this.tipoPagamento = TipoPagamento.values()[tipoPagamento];
    }

    /**
     * @param tipoPagamento
     *            the tipoPagamento to set
     */
    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    /**
     * @param totaleDocumento
     *            the totaleDocumento to set
     */
    public void setTotaleDocumento(Importo totaleDocumento) {
        getAreaMagazzino().getDocumento().setTotale(totaleDocumento);
    }
}
