package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;

public class AreaOrdinePM {
    private List<RigaDistintaCarico> righe;

    /**
     *
     * Costruttore di default.
     */
    public AreaOrdinePM() {
    }

    /**
     *
     * Costruttore.
     *
     * @param righe
     *            righe dell'area ordine.
     */
    public AreaOrdinePM(final List<RigaDistintaCarico> righe) {
        super();
        // Mi arriva una classe della glazed, creao un semplice lista.
        this.righe = new ArrayList<RigaDistintaCarico>();
        this.righe.addAll(righe);
    }

    /**
     * @return Returns the data.
     */
    public Date getData() {
        return righe.get(0).getDataRegistrazione();
    }

    /**
     *
     * @return eventuale deposito di destinazione in caso di evasione su trasferimento
     */
    public DepositoLite getDepositoDestinazione() {
        return righe.get(0).getDatiEvasioneDocumento().getDepositoDestinazione();
    }

    /**
     *
     * @return eventuale deposito di origine in caso di evasione su trasferimento
     */
    public DepositoLite getDepositoOrigine() {
        return righe.get(0).getDeposito();
    }

    /**
     * @return Returns the entita.
     */
    public EntitaDocumento getEntita() {
        return righe.get(0).getEntita();
    }

    /**
     * @return Returns the numero.
     */
    public String getNumero() {
        return righe.get(0).getNumeroDocumento().getCodice();
    }

    /**
     * @return Returns the righe.
     */
    public List<RigaDistintaCarico> getRighe() {
        return righe;
    }

    /**
     * @return Returns the sede.
     */
    public SedeEntita getSede() {
        return righe.get(0).getSedeEntita();
    }

    /**
     *
     * @return tipoareaMagazzino di evasione
     */
    public TipoAreaMagazzino getTipoAreaEvasione() {
        if (righe.get(0).getDatiEvasioneDocumento().getTipoAreaEvasione() == null) {
            return new TipoAreaMagazzino();
        }
        return righe.get(0).getDatiEvasioneDocumento().getTipoAreaEvasione();
    }

    /**
     *
     * @return tipo area magazzino sul quale evadere le righe
     */
    public TipoDocumento getTipoDocumento() {
        if (righe.get(0).getDatiEvasioneDocumento().getTipoAreaEvasione() == null) {
            TipoDocumento td = new TipoDocumento();
            return td;
        }
        return righe.get(0).getDatiEvasioneDocumento().getTipoAreaEvasione().getTipoDocumento();
    }

    /**
     *
     * @param deposito
     *            dep destinazione
     */
    public void setDepositoDestinazione(DepositoLite deposito) {
        for (RigaDistintaCarico rigaDistintaCarico : righe) {
            rigaDistintaCarico.getDatiEvasioneDocumento().setDepositoDestinazione(deposito);
        }
    }

    /**
     *
     * @param tipoDocumentoEvasione
     *            tipo area magazzino sul quale evadere le righe
     */
    public void setTipoDocumentoEvasione(TipoAreaMagazzino tipoDocumentoEvasione) {
        for (RigaDistintaCarico rigaDistintaCarico : righe) {
            rigaDistintaCarico.getDatiEvasioneDocumento().setTipoAreaEvasione(tipoDocumentoEvasione);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AreaOrdinePM [getData()=" + getData() + ", getEntita()=" + getEntita() + ", getNumero()=" + getNumero()
                + ", getSede()=" + getSede() + ", getTipoDocumentoEvasione()=" + getTipoDocumento() + "]";
    }

}
